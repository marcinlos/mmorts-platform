package pl.edu.agh.ki.mmorts.server.modules.dsl;

import java.util.Map;

public class MapVal<K, V> extends AbstractValue<V> {
    
    private Value<K> key;
    private Map<K, V> map;

    public MapVal(Value<K> key, Map<K, V> map) {
        this.key = key;
        this.map = map;
    }

    @Override
    public V compute() {
        return map.get(key.compute());
    }

}
