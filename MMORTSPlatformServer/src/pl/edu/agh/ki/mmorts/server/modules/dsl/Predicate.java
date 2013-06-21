package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 *
 * @param <T>
 */
public interface Predicate<T> {

    boolean satisfied(Value<? extends T> entity);
    
}
