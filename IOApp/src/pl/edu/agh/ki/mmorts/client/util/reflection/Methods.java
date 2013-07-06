package pl.edu.agh.ki.mmorts.client.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class containing various methods facilitating working reflectively
 * with methods.
 * 
 */
public class Methods {

    private Methods() {
        // Non-instantiable
    }

    /**
     * Creates a collection of all methods of a given class (declared by this
     * class and all its superclasses and implemented interfaces).
     * 
     * @param clazz
     *            Class to extract methods from
     * @return {@linkplain Iterable} containing all the class' method.
     */
    public static Iterable<Method> all(Class<?> clazz) {
        Set<Method> methods = new HashSet<Method>();
        for (Class<?> c : Classes.ancestors(clazz)) {
            Collections.addAll(methods, c.getDeclaredMethods());
        }
        return methods;
    }

    /**
     * Creates a collection of all the methods of a given class having specified
     * annotation.
     * 
     * @param clazz
     *            Class to extract methods from
     * @param annotation
     *            Class of a desired annotation
     * @return {@linkplain Iterable} containing all the annotated methods.
     * @see #all(Class)
     */
    public static Iterable<Method> annotated(Class<?> clazz,
            Class<? extends Annotation> annotation) {
        Set<Method> methods = new HashSet<Method>();
        for (Class<?> c : Classes.ancestors(clazz)) {
            for (Method m : c.getDeclaredMethods()) {
                if (m.isAnnotationPresent(annotation)) {
                    methods.add(m);
                }
            }
        }
        return methods;
    }

    /**
     * Tries to call method(s) marked with the specified annotation.
     * 
     * @param annotation
     *            Class of the annotation
     * @param target
     *            Object to be searched
     * 
     * @throws InvocationException
     *             If some invocation fails
     */
    public static void callAnnotated(Class<? extends Annotation> annotation,
            Object target, Object... args) {
        Class<?> clazz = target.getClass();
        try {
            for (Method method : annotated(clazz, annotation)) {
                invoke(target, method, args);
            }
        } catch (Exception e) {
            throw new InvocationException(e);
        }
    }

    /*
     * Helper function, invokes arbitrary method temporarily changing its'
     * accesibility if necessary, and rolling it back after invocation.
     */
    private static void invoke(Object o, Method m, Object... args)
            throws Exception {
        boolean acc = m.isAccessible();
        try {
            m.setAccessible(true);
            m.invoke(o, args);
        } finally {
            m.setAccessible(acc);
        }
    }

}
