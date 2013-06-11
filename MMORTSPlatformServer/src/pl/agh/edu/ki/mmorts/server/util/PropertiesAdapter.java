package pl.agh.edu.ki.mmorts.server.util;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Nasty, nasty wrapper for {@link Properties} object to provide
 * {@code Map<String, String>} interface.
 */
public class PropertiesAdapter implements Map<String, String> {

    private Properties p;

    public PropertiesAdapter(Properties properties) {
        this.p = properties;
    }

    @Override
    public int size() {
        return p.size();
    }

    @Override
    public boolean isEmpty() {
        return p.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return p.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return p.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return (String) p.get(key);
    }

    @Override
    public String put(String key, String value) {
        return (String) p.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return (String) p.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        p.putAll(m);
    }

    @Override
    public void clear() {
        p.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> keySet() {
        return (Set<String>) (Set<?>) p.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<String> values() {
        return (Collection<String>) (Collection<?>) p.values();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<Entry<String, String>> entrySet() {
        return (Set<Entry<String, String>>) (Set<?>) p.entrySet();
    }

}
