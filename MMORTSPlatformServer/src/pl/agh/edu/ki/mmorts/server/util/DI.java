package pl.agh.edu.ki.mmorts.server.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

/**
 * Utility class for DI operations.
 */
public class DI {

    private DI() {
        // non-instantiable
    }

    /**
     * Creates an module definition for a single class/instance pair.
     * 
     * @param obj
     *            Object to inject
     * @param clazz
     *            Class being target of injection
     * @return Guice {@linkplain Module} realizing this injection
     */
    public static <T> Module objectModule(final T obj,
            final Class<? super T> clazz) {
        /*
         * One could imagine simple bind(...).toInstance(...) should do the
         * trick, but it turns out it results in implicit creation of singleton
         * binding, which is affected by other module bindings. In effect, if
         * the specified instance has dependencies, Guice is very sad it cannot
         * satisfy them, even if they're already set. Hence the nasty trick.
         */
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toProvider(new Provider<T>() {
                    @Override
                    public T get() {
                        return obj;
                    }
                });
            }
        };
    }

    /**
     * Creates an injector with a single class -> object binding.
     * 
     * @param obj
     *            Object to be injected
     * @param clazz
     *            Target of the injection
     * @return Injector using clazz -> obj binding
     */
    public static <T> Injector objectInjector(T obj, Class<? super T> clazz) {
        return Guice.createInjector(objectModule(obj, clazz));
    }

    /**
     * Creates an instance of the class using the list of modules specified as
     * varargs.
     * 
     * @param clazz
     *            Class whose instance is to be created with dependencies
     *            injected as defined in module list
     * @param modules
     *            Modules to be used to find dependencies
     * @return {@code clazz} instance
     */
    public static <T> T createWith(Class<T> clazz, Module... modules) {
        return createWith(clazz, Arrays.asList(modules));
    }

    /**
     * Creates an instance of the class using the list of modules specified as
     * an iterable.
     * 
     * @param clazz
     *            Class whose instance is to be created with dependencies
     *            injected as defined in module list
     * @param modules
     *            Modules to be used to find dependencies
     * @return {@code clazz} instance
     */
    public static <T> T createWith(Class<T> clazz,
            Iterable<? extends Module> modules) {
        Injector injector = Guice.createInjector(modules);
        return injector.getInstance(clazz);
    }

    /**
     * Creates an module definition that injects a concrete object of a given
     * type in places annotated with given annotation.
     * 
     * <p>
     * Note: generics' type inference rules make it unsuitable for usage with
     * types not determined statically, i.e. when the class object's wildcard
     * has no precise bound.
     * 
     * @param obj
     *            Object to inject
     * @param clazz
     *            Target type of the injected object
     * @param ann
     *            Annotation marking the injection targets
     * @return Modules realizing injection described above
     * 
     * @see #objectModuleAnnotatedDynamic(Object, Class, Class)
     */
    public static <T> Module objectModuleAnnotated(final T obj,
            final Class<? super T> clazz, final Class<? extends Annotation> ann) {
        /*
         * See the comment in objectModule for why is such a workaround required
         * instead of bind(...).toInstance(...)
         */
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).annotatedWith(ann).toProvider(new Provider<T>() {
                    @Override
                    public T get() {
                        return obj;
                    }
                });
            }
        };
    }

    /**
     * Creates a module definition that injects a concrete object of a given
     * type in places annotated with given annotation.
     * 
     * <p>
     * This works with classes not determined statically, at the cost of
     * performing a cast inside the method.
     * 
     * @param o
     *            Object to inject
     * @param clazz
     *            Target type of the injected object
     * @param ann
     *            Annotation marking the injection targets
     * @return Modules realizing injection described above
     */
    public static <T> Module objectModuleAnnotatedDynamic(Object o,
            Class<T> clazz, Class<? extends Annotation> ann) {
        T object = clazz.cast(o);
        return objectModuleAnnotated(object, clazz, ann);
    }

}
