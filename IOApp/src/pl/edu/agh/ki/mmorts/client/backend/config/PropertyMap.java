package pl.edu.agh.ki.mmorts.client.backend.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.ki.mmorts.client.backend.util.GsonUtil;

import com.google.gson.JsonParseException;

/**
 * Simple immutable property map, allowing conversion to a precise type at
 * retrieval.
 * 
 * @author los
 */
public class PropertyMap {

    private Map<String, String> map;

    /**
     * Creates a property map from a given string -> string mapping.
     * 
     * @param map
     *            Mapping to build {@code PropertyMap} from
     */
    public PropertyMap(Map<String, String> map) {
        this.map = Collections
                .unmodifiableMap(new HashMap<String, String>(map));
    }

    /**
     * Retrieves a raw string associated with a given key
     * 
     * @param key
     *            Key
     * @return Associated string
     */
    public String get(String key) {
        return map.get(key);
    }

    /**
     * Checks whether a key is in the mapping
     * 
     * @param key
     *            Key to check
     * @return {@code true} if the property is present, {@code false} otherwise
     */
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    /**
     * Returns underlying java.util.Map object.
     * 
     * @return Map
     */
    public Map<String, String> asMap() {
        return map;
    }

    /**
     * Retrieve value based on a key and a type. If the
     * 
     * @param key
     *            Key
     * @param clazz
     *            Desired type
     * @return Value associated with a key, converted to a desired type
     */
    public <T> T get(String key, Class<T> clazz) {
        String str = map.get(key);
        if (str != null) {
          try {
                return GsonUtil.gson.fromJson(str, clazz);
            } catch (JsonParseException e) {
                //swallowed exception - on purpose!
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Get the value associated with the key as a char
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public char getChar(String key) {
        return get(key, Character.class);
    }

    /**
     * Get the value associated with the key as a byte
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public byte getByte(String key) {
        return get(key, Byte.class);
    }

    /**
     * Get the value associated with the key as a short
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public short getShort(String key) {
        return get(key, Short.class);
    }

    /**
     * Get the value associated with the key as an int
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public int getInt(String key) {
        return get(key, Integer.class);
    }

    /**
     * Get the value associated with the key as a long
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public long getLong(String key) {
        return get(key, Long.class);
    }

    /**
     * Get the value associated with the key as a float
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public float getFloat(String key) {
        return get(key, Float.class);
    }

    /**
     * Get the value associated with the key as a double
     * 
     * @param key
     *            Key
     * @return Associated value
     */
    public double getDouble(String key) {
        return get(key, Double.class);
    }
}
