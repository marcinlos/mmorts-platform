package pl.edu.agh.ki.mmorts.server.modules.dsl;

import java.util.Map;

/**
 * 
 * @author los
 */
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

    public static <K, V> Value<V> map(Value<? extends K> key, Map<K, V> map) {
        return new MapVal<K, V>(key, map);
    }

    public static <K, V> Value<V> map(K key, Map<? extends K, V> map) {
        return map(val(key), map);
    }

    public static <K, V> Value<V> map(Value<? extends K> key,
            Map<K, ? super V> map, Class<V> clazz) {
        return new CastMapVal<K, V>(key, map, clazz);
    }

    public static <K, V> Value<V> map(K key, Map<? super K, ? super V> map,
            Class<V> clazz) {
        return map(val(key), map, clazz);
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

    public static <T> Condition is(Value<? extends T> value, Predicate<T> pred) {
        return new Satisfies<T>(value, pred);
    }

    public static <T> Condition isNot(Value<? extends T> value,
            Predicate<T> pred) {
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

    public static WhileBuilder _while(Condition cond) {
        return new WhileBuilder(cond);
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
            this.contIf = contIf;
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

    public static class WhileBuilder {
        Condition cond;

        public WhileBuilder(Condition cond) {
            this.cond = cond;
        }

        public Cont _do(Cont body) {
            return new While(cond, body);
        }
    }
}
