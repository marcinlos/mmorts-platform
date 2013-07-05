package pl.edu.agh.ki.mmorts.client.util.reflection;

import java.util.Iterator;

/**
 * Utility class containing various methods facilitating working with java's
 * Class objects.
 * 
 * @author los
 */
public class Classes {

    private Classes() {
        // Non-instantiable
    }

    /**
     * Returns an {@linkplain Iterable} collection of all the superclasses of a
     * given class, in order from the most derived ({@code clazz}) to the
     * {@code Object}.
     * 
     * @param clazz
     *            Class whose superclasses are to be enumerated
     * @return {@linkplain Iterable} instance yielding the superclasses
     */
    public static Iterable<Class<?>> superClasses(final Class<?> clazz) {
        return new Iterable<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                return new SuperClassIterator(clazz);
            }
        };
    }

    /**
     * Returns an {@linkplain Iterable} collection of all the ancestor types of
     * a given class. Both superclases and implemented interfaces are included.
     * <p>
     * The order and other characteristics of how the ancestor types are
     * enumerated are as specified by the {@linkplain AncestorsIterator}'s doc.
     * 
     * @param clazz
     *            Class whose ancestor types are to be enumerated
     * @return {@linkplain Iterable} instance yielding the ancestor types
     */
    public static Iterable<Class<?>> ancestors(final Class<?> clazz) {
        return new Iterable<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                return new AncestorsIterator(clazz);
            }
        };
    }

}
