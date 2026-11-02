package interfaces;

// Common contract that all three cache types implement
public interface ICache {
    void put(String key, int value);     // Add or update a value
    int get(String key);                 // Return the value, or -1 if missing
    void remove(String key);             // Delete one key
    void clear();                        // Delete everything
    int getSize();                       // Number of stored items
    boolean containsKey(String key);     // Check if a key exists
}
