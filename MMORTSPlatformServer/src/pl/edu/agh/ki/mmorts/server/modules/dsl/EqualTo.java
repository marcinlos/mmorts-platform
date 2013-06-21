package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 *
 * @param <T>
 */
public class EqualTo<T> implements Predicate<T> {

    private Value<T> value;
    
    public EqualTo(Value<T> value) {
        this.value = value;
    }

    @Override
    public boolean satisfied(Value<? extends T> entity) {
        return value.compute().equals(entity.compute());
    }

}
