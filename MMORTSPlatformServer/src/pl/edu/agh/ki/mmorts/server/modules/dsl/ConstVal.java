package pl.edu.agh.ki.mmorts.server.modules.dsl;

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
