package pl.edu.agh.ki.mmorts.server.modules.dsl;

public abstract class AbstractValue<T> implements Value<T> {

    @Override
    public Condition is(Predicate<T> pred) {
        return new Satisfies<T>(this, pred);
    }

}
