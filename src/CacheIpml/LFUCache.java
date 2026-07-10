package CacheIpml;

import interfaces.ICache;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LFUCache implements ICache {
    private static final int DEFAULT_CAPACITY = 3;

    private final int capacity;
    private int minFreq;
    private final Map<String, Integer> keyToVal;
    private final Map<String, Integer> keyToFreq;
    private final Map<Integer, LinkedHashSet<String>> freqToKeys;

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
        if (capacity <= 0) {
            return;
        }

        if (keyToVal.containsKey(key)) {
            keyToVal.put(key, value);
            increaseFreq(key);
            return;
        }

        if (keyToVal.size() >= capacity) {
            String evictKey = freqToKeys.get(minFreq).iterator().next();
            freqToKeys.get(minFreq).remove(evictKey);
            if (freqToKeys.get(minFreq).isEmpty()) {
                freqToKeys.remove(minFreq);
            }
            keyToVal.remove(evictKey);
            keyToFreq.remove(evictKey);
        }

        minFreq = 1;
        keyToVal.put(key, value);
        keyToFreq.put(key, 1);
        freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
    }

    @Override
    public int get(String key) {
        if (!keyToVal.containsKey(key)) {
            return -1;
        }
        increaseFreq(key);
        return keyToVal.get(key);
    }

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
            if (minFreq == freq) {
                minFreq++;
            }
        }
    }

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

    @Override
    public boolean containsKey(String key) {
        return keyToVal.containsKey(key);
    }

    private void increaseFreq(String key) {
        int freq = keyToFreq.get(key);
        LinkedHashSet<String> keysAtFreq = freqToKeys.get(freq);
        keysAtFreq.remove(key);
        if (keysAtFreq.isEmpty()) {
            freqToKeys.remove(freq);
            if (minFreq == freq) {
                minFreq++;
            }
        }

        freq++;
        keyToFreq.put(key, freq);
        freqToKeys.computeIfAbsent(freq, k -> new LinkedHashSet<>()).add(key);
    }
}
