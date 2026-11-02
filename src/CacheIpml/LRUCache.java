package CacheIpml;

import interfaces.ICache;
import java.util.LinkedHashMap;
import java.util.Map;

// LRU = Least Recently Used: when full, evicts the key unused for the longest time
public class LRUCache implements ICache {
    private static final int DEFAULT_CAPACITY = 3;

    private final int capacity;              // Maximum number of items
    private final Map<String, Integer> cache; // Stores key -> value, ordered by use

    // Uses the default capacity
    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        // "true" = access order: each read or write moves the key to the newest position
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            // Called after each insert: removes the eldest entry if over capacity
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    // Add or update a key; eviction happens automatically
    @Override
    public void put(String key, int value) {
        if (capacity <= 0) {
            return;
        }
        cache.put(key, value);
    }

    // A successful get marks the key as recently used
    @Override
    public int get(String key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        return -1;
    }

    @Override
    public void remove(String key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public int getSize() {
        return cache.size();
    }

    // containsKey does not change the usage order
    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }
}
