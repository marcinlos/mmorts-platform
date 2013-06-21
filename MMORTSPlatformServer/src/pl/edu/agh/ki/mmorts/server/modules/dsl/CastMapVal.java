package pl.edu.agh.ki.mmorts.server.modules.dsl;

import java.util.Map;

/**
 * 
 * @author los
 *
 * @param <K>
 * @param <T>
 */
public class CastMapVal<K, T> extends AbstractValue<T> {

    private Value<? extends K> key;
    private Map<K, ? super T> map;
    private Class<T> clazz;

    public CastMapVal(Value<? extends K> key, Map<K, ? super T> map,
            Class<T> clazz) {
        this.key = key;
        this.map = map;
        this.clazz = clazz;
    }

    @Override
    public T compute() {
        return clazz.cast(map.get(key.compute()));
    }

}
