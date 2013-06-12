package pl.agh.edu.ki.mmorts.server.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.util.PropertiesAdapter;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.PlayersManager;

/**
 * Implementation of configuration interface, processes the properties at the
 * creation time.
 */
class ConfigImpl implements Config {

    private static final Logger logger = Logger.getLogger(ConfigImpl.class);

    private Properties properties;
    private Map<String, String> propertiesAdapter;

    /** (Lazy) set of missing required properties */
    private Set<String> missing;

    // Processed properties

    /** Used dispatcher implementation class */
    private Class<? extends Dispatcher> dispatcherClass;

    /** Used custom persistor interface */
    private Class<?> customPersistorInterface;

    /** Used custom persistor class */
    private Class<?> customPersistorClass;

    /** Used database class */
    private Class<? extends Database> databaseClass;

    /** Used players manager class */
    private Class<? extends PlayersManager> playersManagerClass;

    /** Used message channel class */
    private Class<? extends MessageChannel> channelClass;

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
        this.properties = properties;
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

        loadDatabaseClass();
        loadPlayersManagerClass();
        loadCustomPersistorInterface();
        loadCustomPersistorClass();
        loadChannelClass();
        loadDispatcherClass();

        // We have delayed the exception to gather all the missing values
        if (missing != null) {
            throw new MissingRequiredPropertiesException(missing);
        }
        propertiesAdapter = new PropertiesAdapter(properties);
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
                logger.fatal("Failed to load Dispatcher class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load Dispatcher class");
            logger.fatal(e);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified message channel class
     */
    private void loadChannelClass() {
        logger.debug("Loading pmessage channel class");
        try {
            channelClass = loadClass(CHANNEL_CLASS, MessageChannel.class);
            if (playersManagerClass == null) {
                addMissing(CHANNEL_CLASS);
                logger.fatal("Failed to load message channel class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load message channel class");
            logger.fatal(e);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified custom persistor
     * interface
     */
    private void loadCustomPersistorInterface() {
        logger.debug("Loading custom persistor interface class");
        try {
            customPersistorInterface = loadClass(CUSTOM_PERSISTOR_INTERFACE,
                    Object.class);
            if (customPersistorInterface == null) {
                addMissing(CUSTOM_PERSISTOR_INTERFACE);
                logger.fatal("Failed to load custom persistor interface (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load custom persistor interface");
            logger.fatal(e);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified custom persistor
     * class. Checks whether the loaded class is indeed an implementation of the
     * custom persistor interface.
     */
    private void loadCustomPersistorClass() {
        logger.debug("Loading CustomPersistor class");
        try {
            customPersistorClass = loadClass(CUSTOM_PERSISTOR_CLASS,
                    customPersistorInterface);
            if (customPersistorClass == null) {
                addMissing(CUSTOM_PERSISTOR_CLASS);
                logger.fatal("Failed to load custom persistor class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load custom persistor class");
            logger.fatal(e);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified database class
     */
    private void loadDatabaseClass() {
        logger.debug("Loading database class");
        try {
            databaseClass = loadClass(DATABASE_CLASS, Database.class);
            if (databaseClass == null) {
                addMissing(DATABASE_CLASS);
                logger.fatal("Failed to load database class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load database class");
            logger.fatal(e);
        }
    }

    /*
     * Retrieves a {@code Class} object for the specified players manager class
     */
    private void loadPlayersManagerClass() {
        logger.debug("Loading players manager class");
        try {
            playersManagerClass = loadClass(PLAYERS_MANAGER_CLASS,
                    PlayersManager.class);
            if (playersManagerClass == null) {
                addMissing(PLAYERS_MANAGER_CLASS);
                logger.fatal("Failed to load players manager class (missing)");
            }
        } catch (Exception e) {
            logger.fatal("Failed to load players manager class");
            logger.fatal(e);
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
    public Class<?> getCustomPersistorClass() {
        return customPersistorClass;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getCustomPersistorInterface() {
        return customPersistorInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(propertiesAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends Database> getDatabaseClass() {
        return databaseClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends PlayersManager> getPlayerManagerClass() {
        return playersManagerClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? extends MessageChannel> getChannelClass() {
        return channelClass;
    }


}
