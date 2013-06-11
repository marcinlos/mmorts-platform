package pl.agh.edu.ki.mmorts.server.config;

import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;

/**
 * Allows retrieval of configuration properties
 */
public interface Config {

    /** Used implementation of dispatcher */
    public static final String DISPATCHER_CLASS = "sv.dispatcher.class";

    /** Implementation of the custom persistor */
    public static final String CUSTOM_PERSISTOR_CLASS = "sv.persistor.class";

    /**
     * Retrieves a string value of a property
     * 
     * @param key
     *            Name of the property
     * @return Value of the property or {@code null} if it is not present in the
     *         configuration.
     */
    String getString(String key);

    /**
     * @return Dispatcher implementation class specified in the configuration
     */
    Class<? extends Dispatcher> getDispatcherClass();

    /**
     * @return Implementation of custom persistor specified in the configuration
     */
    Class<? extends CustomPersistor> getCustomPersistorClass();

}