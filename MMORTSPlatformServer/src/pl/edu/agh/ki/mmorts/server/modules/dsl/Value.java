package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 *
 * @param <T>
 */
public interface Value<T> {

    T compute();
    
    Condition is(Predicate<? super T> pred);
    
}
