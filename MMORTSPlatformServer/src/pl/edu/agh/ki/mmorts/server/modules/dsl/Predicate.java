package pl.edu.agh.ki.mmorts.server.modules.dsl;

public interface Predicate<T> {

    boolean satisfied(Value<T> entity);
    
}
