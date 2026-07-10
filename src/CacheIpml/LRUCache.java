package CacheIpml;

import interfaces.ICache;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache implements ICache {
    private static final int DEFAULT_CAPACITY = 3;

    private final int capacity;
    private final Map<String, Integer> cache;

    public LRUCache() {
        this(DEFAULT_CAPACITY);
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    @Override
    public void put(String key, int value) {
        if (capacity <= 0) {
            return;
        }
        cache.put(key, value);
    }

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

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }
}
