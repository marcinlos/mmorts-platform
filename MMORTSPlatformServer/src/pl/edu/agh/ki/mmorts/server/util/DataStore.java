package pl.edu.agh.ki.mmorts.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic retrieval-typesafe map, storing arbitrary values and providing access
 * through an interface taking a class token to perform a cast and thus ensure
 * type safety.
 * 
 * @author los
 */
public class DataStore extends HashMap<Object, Object> {

    public DataStore() {
        // empty
    }

    /**
     * Creates the context with some initial capacity
     */
    public DataStore(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Copies the content of passed map
     */
    public DataStore(Map<? extends Object, ? extends Object> m) {
        super(m);
    }

    /**
     * Gets the value with a specified type instead of plain object.
     * 
     * <p>
     * Note: this is inherently unsafe, as the type checking cannot be done
     * here. Prefer {@link #get(Object, Class)} when possible.
     */
    @SuppressWarnings("unchecked")
    public <T> T getAs(Object key) {
        return (T) get(key);
    }

    /**
     * Type-safe version of get, checks at runtime if the value is
     * assignment-compatible with a given class, and returns false otherwise.
     * 
     * <p>
     * Note: there is no way to know whether the returned {@code null} denotes
     * lack of value or type error
     * 
     * @param key
     *            Key whose mapping is to be returned
     * @param clazz
     *            Expected type
     * @return Value associated with {@code key} of a type {@code clazz}, or
     *         {@code null} if key is not mapped or the types do not match
     */
    public <T> T get(Object key, Class<T> clazz) {
        Object obj = get(key);
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        } else {
            return null;
        }
    }
}
