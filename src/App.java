import interfaces.ICache;

// Runs the same demo on each cache type to compare eviction behavior
public class App {
    public static void main(String[] args) {
        System.out.println("=== FIFO Cache (capacity 3) ===");
        demo(new CacheIpml.FIFOCache(3));

        System.out.println("\n=== LRU Cache (capacity 3) ===");
        demo(new CacheIpml.LRUCache(3));

        System.out.println("\n=== LFU Cache (capacity 3) ===");
        demo(new CacheIpml.LFUCache(3));
    }

    private static void demo(ICache cache) {
        // Fill the cache to capacity
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        System.out.println("After put a,b,c -> size=" + cache.getSize());

        // Use "a", then add a fourth key to force an eviction
        cache.get("a");
        cache.put("d", 4);
        // Print each value; an evicted key shows -1
        System.out.println("After get(a) and put(d) -> a=" + cache.get("a")
                + ", b=" + cache.get("b")
                + ", c=" + cache.get("c")
                + ", d=" + cache.get("d")
                + ", size=" + cache.getSize());
    }
}
