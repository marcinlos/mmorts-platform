package pl.edu.agh.ki.mmorts.server.modules.dsl;

public abstract class AbstractValue<T> implements Value<T> {

    @Override
    public Condition is(Predicate<? super T> pred) {
        return make(pred, this);
    }
    
    private static <T> Condition make(Predicate<T> pred, Value<? extends T> val) {
        return new Satisfies<T>(val, pred);
    }

}
