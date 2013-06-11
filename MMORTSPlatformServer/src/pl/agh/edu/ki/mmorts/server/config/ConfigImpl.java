package pl.agh.edu.ki.mmorts.server.config;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;

/**
 * Implementation of configuration interface, processes the properties at the
 * creation time.
 */
class ConfigImpl implements Config {

    private static final Logger logger = Logger.getLogger(ConfigImpl.class);

    private Properties properties;

    /** (Lazy) set of missing required properties */
    private Set<String> missing;

    // Processed properties

    /** Used dispatcher implementation class */
    private Class<? extends Dispatcher> dispatcherClass;

    /** Used custom persistor */
    private Class<? extends CustomPersistor> customPersistorClass;

    /**
     * Creates new {@code Config} implementation.
     * 
     * @param properties
     *            Properties to be used to build config object
     * 
     * @throws MissingRequiredPropertiesException
     *             If some properties are missing
     */
    public ConfigImpl(Properties properties) {
        process();
    }

    /*
     * Adds a missing property, if necessary initializes the set
     */
    private void addMissing(String property) {
        if (missing == null) {
            missing = new HashSet<String>();
        }
        missing.add(property);
    }

    /*
     * Does the actual job of building complex config properties
     */
    private void process() {
        logger.debug("Processing read configuration properties");

        loadDispatcherClass();
        loadCustomPersistorClass();

        // We have delayed the exception to gather all the missing values
        if (missing != null) {
            throw new MissingRequiredPropertiesException(missing);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified dispatcher class
     */
    private void loadDispatcherClass() {
        logger.debug("Loading Dispatcher class");
        try {
            dispatcherClass = loadClass(DISPATCHER_CLASS, Dispatcher.class);
            if (dispatcherClass == null) {
                addMissing(DISPATCHER_CLASS);
            } else {
                logger.fatal("Failed to load Dispatcher class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load Dispatcher class", e);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified custom persistor class
     */
    private void loadCustomPersistorClass() {
        logger.debug("Loading CustomPersistor class");
        try {
            customPersistorClass = loadClass(CUSTOM_PERSISTOR_CLASS,
                    CustomPersistor.class);
            if (customPersistorClass == null) {
                addMissing(CUSTOM_PERSISTOR_CLASS);
                logger.fatal("Failed to load custom persistor class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load custom persistor class", e);
        }
    }

    /**
     * Generic class-loading facility.
     * 
     * @param key
     *            Name of the property specifying the class
     * @param target
     *            Target type {@code Class} object
     * @return {@code Class} object specified by the {@code key} property, or
     *         {@code null} if there was no way to load it
     */
    private <T> Class<? extends T> loadClass(String key, Class<T> target) {
        String className = getString(key);
        if (className != null) {
            logger.debug("Loading " + key + " class (" + target.getName() + ")");
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
                return clazz.asSubclass(target);
            } catch (ClassNotFoundException e) {
                throw new InvalidConfigPropertyException(className
                        + " cannot be found", key, className, e);
            } catch (ClassCastException e) {
                throw new InvalidConfigPropertyException(className
                        + " is not a " + target.getName() + " implementation",
                        key, className, e);
            } catch (Exception e) {
                throw new InvalidConfigPropertyException("Unexpected error",
                        key, className, e);
            }
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getString(String key) {
        return properties.getProperty(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Dispatcher> getDispatcherClass() {
        return dispatcherClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends CustomPersistor> getCustomPersistorClass() {
        return customPersistorClass;
    }

}
