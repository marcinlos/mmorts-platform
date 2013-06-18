package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 *
 * @param <T>
 */
public class Satisfies<T> implements Condition {
    
    private Value<? extends T> val;
    private Predicate<T> pred;

    public Satisfies(Value<? extends T> val, Predicate<T> pred) {
        this.val = val;
        this.pred = pred;
    }

    @Override
    public boolean holds() {
        return pred.satisfied(val);
    }
    
}
