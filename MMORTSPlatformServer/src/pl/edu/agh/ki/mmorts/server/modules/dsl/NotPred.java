package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 *
 * @param <T>
 */
public class NotPred<T> implements Predicate<T> {

    private Predicate<T> pred;
    
    public NotPred(Predicate<T> pred) {
        this.pred = pred;
    }

    @Override
    public boolean satisfied(Value<? extends T> entity) {
        return ! pred.satisfied(entity);
    }

}
