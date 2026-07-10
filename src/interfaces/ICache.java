package interfaces;

public interface ICache {
    void put(String key, int value);
    int get(String key);
    void remove(String key);
    void clear();
    int getSize();
    boolean containsKey(String key);

    
}