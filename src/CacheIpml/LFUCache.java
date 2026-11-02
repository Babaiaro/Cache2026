package CacheIpml;

import interfaces.ICache;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

// LFU = Least Frequently Used: when full, evicts the key used the fewest times.
// On a tie, it evicts the key that has been at that count the longest.
public class LFUCache implements ICache {
    private static final int DEFAULT_CAPACITY = 3;

    private final int capacity;                  // Maximum number of items
    private int minFreq;                         // Lowest use count, used to find the eviction target
    private final Map<String, Integer> keyToVal; // key -> value
    private final Map<String, Integer> keyToFreq; // key -> use count
    private final Map<Integer, LinkedHashSet<String>> freqToKeys; // use count -> keys, oldest first

    // Uses the default capacity
    public LFUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyToVal = new HashMap<>();
        this.keyToFreq = new HashMap<>();
        this.freqToKeys = new HashMap<>();
    }

    @Override
    public void put(String key, int value) {
        // A cache with no capacity stores nothing
        if (capacity <= 0) {
            return;
        }

        // Existing key: update the value and count this as a use
        if (keyToVal.containsKey(key)) {
            keyToVal.put(key, value);
            increaseFreq(key);
            return;
        }

        // Full: evict the oldest key at the lowest use count
        if (keyToVal.size() >= capacity) {
            String evictKey = freqToKeys.get(minFreq).iterator().next();
            freqToKeys.get(minFreq).remove(evictKey);
            if (freqToKeys.get(minFreq).isEmpty()) {
                freqToKeys.remove(minFreq);
            }
            keyToVal.remove(evictKey);
            keyToFreq.remove(evictKey);
        }

        // A new key always starts with a count of 1, so the minimum becomes 1
        minFreq = 1;
        keyToVal.put(key, value);
        keyToFreq.put(key, 1);
        freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
    }

    // A successful get raises the key's use count
    @Override
    public int get(String key) {
        if (!keyToVal.containsKey(key)) {
            return -1;
        }
        increaseFreq(key);
        return keyToVal.get(key);
    }

    // Removes the key from all three maps
    @Override
    public void remove(String key) {
        if (!keyToVal.containsKey(key)) {
            return;
        }

        int freq = keyToFreq.remove(key);
        keyToVal.remove(key);
        LinkedHashSet<String> keysAtFreq = freqToKeys.get(freq);
        keysAtFreq.remove(key);
        if (keysAtFreq.isEmpty()) {
            freqToKeys.remove(freq);
            // Safe even if this count is unused: the next eviction needs a new key first,
            // and adding one resets minFreq to 1
            if (minFreq == freq) {
                minFreq++;
            }
        }
    }

    // Empties all maps and resets the minimum
    @Override
    public void clear() {
        keyToVal.clear();
        keyToFreq.clear();
        freqToKeys.clear();
        minFreq = 0;
    }

    @Override
    public int getSize() {
        return keyToVal.size();
    }

    // containsKey does not count as a use
    @Override
    public boolean containsKey(String key) {
        return keyToVal.containsKey(key);
    }

    // Moves a key from its current use-count group to the next one up
    private void increaseFreq(String key) {
        int freq = keyToFreq.get(key);
        LinkedHashSet<String> keysAtFreq = freqToKeys.get(freq);
        keysAtFreq.remove(key);
        if (keysAtFreq.isEmpty()) {
            freqToKeys.remove(freq);
            // The key was the only one at the minimum, and it now sits at minimum + 1
            if (minFreq == freq) {
                minFreq++;
            }
        }

        freq++;
        keyToFreq.put(key, freq);
        freqToKeys.computeIfAbsent(freq, k -> new LinkedHashSet<>()).add(key);
    }
}
