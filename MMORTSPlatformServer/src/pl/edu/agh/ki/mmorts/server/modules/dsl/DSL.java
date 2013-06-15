package pl.edu.agh.ki.mmorts.server.modules.dsl;

import java.util.Map;

public class DSL {

    private DSL() {
        // non-instantiable
    }
    
    public static void with(Control c, Cont cont) {
        cont.execute(c);
    }
    
    public static <T> Predicate<T> not(Predicate<T> pred) {
        return new NotPred<T>(pred);
    }
    
    public static Condition not(Condition cond) {
        return new Not(cond);
    }
    
    
    public static <K, V> Value<V> map(Value<K> key, Map<K, V> map) {
        return new MapVal<K, V>(key, map);
    }
    
    public static <V> Value<V> val(V value) {
        return new ConstVal<V>(value);
    }
    
    public static <T> Predicate<T> eq(T value) {
        return new EqualTo<T>(val(value));
    }
    
    public static <T> Predicate<T> eq(Value<T> value) {
        return new EqualTo<T>(value);
    }
    
    public static <T> Predicate<T> neq(T value) {
        return not(eq(value));
    }
    
    public static <T> Predicate<T> neq(Value<T> value) {
        return not(eq(value));
    }
    
    public static <T> Condition is(Value<T> value, Predicate<T> pred) {
        return new Satisfies<T>(value, pred);
    }
    
    public static <T> Condition isNot(Value<T> value, Predicate<T> pred) {
        return not(is(value, pred));
    }
    
    public static Condition and(Condition... conds) {
        return new And(conds);
    }

    public static Condition or(Condition... conds) {
        return new Or(conds);
    }
    
    public static Cont seq(Cont... conts) {
        return new ContChain(conts);
    }
    
    public static IfBuilder _if(Condition cond) {
        return new IfBuilder(cond);
    }
    
    public static class IfBuilder {
        
        Condition cond;
        
        IfBuilder(Condition cond) {
            this.cond = cond;
        }
        
        public ThenBuilder then(Cont contIf) {
            return new ThenBuilder(cond, contIf);
        }
        
    }
    
    public static class ThenBuilder implements Cont {
        Condition cond;
        Cont contIf;
        
        ThenBuilder(Condition cond, Cont contIf) {
            this.cond = cond;
            this.contIf  = contIf;
        }
        
        @Override
        public void execute(Control c) {
            if (cond.holds()) {
                c.continueWith(contIf);
            }
        }
        
        public Cont _else(Cont contElse) {
            return new IfThenElse(cond, contIf, contElse);
        }
        
    }
}
