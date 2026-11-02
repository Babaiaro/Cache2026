package CacheIpml;

import interfaces.ICache;
import java.util.HashMap;
import java.util.LinkedList;

// FIFO = First In, First Out: when full, evicts the key added earliest
public class FIFOCache implements ICache {
    private static final int DEFAULT_CAPACITY = 3;

    private final int capacity;                  // Maximum number of items
    private final HashMap<String, Integer> dictMap; // Stores key -> value
    private final LinkedList<String> queue;      // Keys in insertion order (oldest first)

    // Uses the default capacity
    public FIFOCache() {
        this(DEFAULT_CAPACITY);
    }

    public FIFOCache(int capacity) {
        this.capacity = capacity;
        this.dictMap = new HashMap<>();
        this.queue = new LinkedList<>();
    }

    @Override
    public void put(String key, int value) {
        // A cache with no capacity stores nothing
        if (capacity <= 0) {
            return;
        }

        // Existing key: update the value but keep its place in the queue
        if (dictMap.containsKey(key)) {
            dictMap.put(key, value);
            return;
        }

        // Full: evict the oldest key
        if (getSize() >= capacity) {
            String oldestKey = queue.removeFirst();
            dictMap.remove(oldestKey);
        }

        // Add the new key at the back of the queue
        dictMap.put(key, value);
        queue.addLast(key);
    }

    // Reading does not affect eviction order in FIFO
    @Override
    public int get(String key) {
        if (dictMap.containsKey(key)) {
            return dictMap.get(key);
        }
        return -1;
    }

    // Removes the key from both the map and the queue
    @Override
    public void remove(String key) {
        if (dictMap.containsKey(key)) {
            dictMap.remove(key);
            queue.remove(key);
        }
    }

    // Empties both structures
    @Override
    public void clear() {
        dictMap.clear();
        queue.clear();
    }

    @Override
    public int getSize() {
        return queue.size();
    }

    @Override
    public boolean containsKey(String key) {
        return dictMap.containsKey(key);
    }
}
