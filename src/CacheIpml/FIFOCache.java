package CacheIpml;

import interfaces.ICache;
import java.util.HashMap;
import java.util.LinkedList;

public class FIFOCache implements ICache {
    private static final int DEFAULT_CAPACITY = 3;

    private final int capacity;
    private final HashMap<String, Integer> dictMap;
    private final LinkedList<String> queue;

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
        if (capacity <= 0) {
            return;
        }

        if (dictMap.containsKey(key)) {
            dictMap.put(key, value);
            return;
        }

        if (getSize() >= capacity) {
            String oldestKey = queue.removeFirst();
            dictMap.remove(oldestKey);
        }

        dictMap.put(key, value);
        queue.addLast(key);
    }

    @Override
    public int get(String key) {
        if (dictMap.containsKey(key)) {
            return dictMap.get(key);
        }
        return -1;
    }

    @Override
    public void remove(String key) {
        if (dictMap.containsKey(key)) {
            dictMap.remove(key);
            queue.remove(key);
        }
    }

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
