package pl.agh.edu.ki.mmorts.server.util;

import java.util.Arrays;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Utility class for DI operations.
 */
public class DI {

    private DI() {
        // non-instantiable
    }

    /**
     * Creates an injector for a single class/instance pair.
     * 
     * @param obj
     *            Object to inject
     * @param clazz
     *            Class being target of injection
     * @return Guice {@linkplain Module} realizing this injection
     */
    public static <T> Module objectModule(final T obj,
            final Class<? super T> clazz) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(clazz).toInstance(obj);
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

}
