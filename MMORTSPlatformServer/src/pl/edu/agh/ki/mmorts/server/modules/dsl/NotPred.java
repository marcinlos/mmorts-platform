package pl.edu.agh.ki.mmorts.server.modules.dsl;

public class NotPred<T> implements Predicate<T> {

    private Predicate<T> pred;
    
    public NotPred(Predicate<T> pred) {
        this.pred = pred;
    }

    @Override
    public boolean satisfied(Value<T> entity) {
        return ! pred.satisfied(entity);
    }

}
