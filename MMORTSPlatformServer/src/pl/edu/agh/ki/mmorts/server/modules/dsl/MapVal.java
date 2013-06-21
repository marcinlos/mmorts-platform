package pl.edu.agh.ki.mmorts.server.modules.dsl;

import java.util.Map;

/**
 * 
 * @author los
 *
 * @param <K>
 * @param <V>
 */
public class MapVal<K, V> extends AbstractValue<V> {
    
    private Value<? extends K> key;
    private Map<K, V> map;

    public MapVal(Value<? extends K> key, Map<K, V> map) {
        this.key = key;
        this.map = map;
    }

    @Override
    public V compute() {
        return map.get(key.compute());
    }

}
