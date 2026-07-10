import interfaces.ICache;

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
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        System.out.println("After put a,b,c -> size=" + cache.getSize());

        cache.get("a");
        cache.put("d", 4);
        System.out.println("After get(a) and put(d) -> a=" + cache.get("a")
                + ", b=" + cache.get("b")
                + ", c=" + cache.get("c")
                + ", d=" + cache.get("d")
                + ", size=" + cache.getSize());
    }
}
