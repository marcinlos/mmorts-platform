package pl.edu.agh.ki.mmorts.server.modules.dsl;

/**
 * 
 * @author los
 *
 * @param <T>
 */
public class ConstVal<T> extends AbstractValue<T> {
    
    private T value;

    public ConstVal(T value) {
        this.value = value;
    }

    @Override
    public T compute() {
        return value;
    }
    
    

}
