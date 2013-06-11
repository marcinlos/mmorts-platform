package pl.edu.agh.ki.mmorts.server.core;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.agh.edu.ki.mmorts.server.config.ConfigException;
import pl.agh.edu.ki.mmorts.server.config.ConfigReader;
import pl.agh.edu.ki.mmorts.server.util.DI;
import pl.edu.agh.ki.mmorts.server.Main;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.PlayersManager;

import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * First class to be instantiated in {@link Main#main(String[])}. Responsible
 * for the initialization of the whole server.
 * 
 * <p>
 * Performs roughly the following operations (in order):
 * <ul>
 * <li>Reads configuration files
 * <li>Sets up {@linkplain Gateway}
 * <li>Creates & initializes modules, based on the configuration
 * <li>Initializes database connection, creates persistence interfaces</li>
 */
public class Init {

    private static final Logger logger = Logger.getLogger(Init.class);

    /** Configuration file location */
    private static final String CONFIG = "resources/server.properties";

    /**
     * Dispatcher object created using the class specified in the configuration
     */
    private Dispatcher dispatcher;
    private Module dispatcherModule;
    
    /**
     * Message channel created using the class specified in the configuration
     */
    private MessageChannel channel;
    private Module channelModule;

    /**
     * Custom persistor object created using the class specified in the
     * configuration
     */
    private CustomPersistor customPersistor;

    private PlayersManager playersManager;

    /**
     * Database interface, implementation as in the configuration file
     */
    private Database database;
    private Module databaseModule;

    /**
     * Configuration read from the config file and processed a bit
     */
    private Config config;
    private Module configModule;

    /**
     * Creates the {@code Init} object and initializes the server.
     * 
     * @param args
     *            Command line arguments
     * @throws InitException
     *             In case of an initialization failure
     */
    public Init(String[] args) {
        try {
            init();
            // Dispatch incoming messages
            dispatcher.run();
        } catch (Exception e) {
            logger.fatal("Server error, cannot continue", e);
        } finally {
            shutdown();
        }
    }

    /*
     * Handles details of initialization
     */
    private void init() {
        logger.info("Begin server initialization");
        try {
            readConfig(CONFIG);
            createDataSource();
            createChannel();
            createDispatcher();
            createCustomPersistor();
            createPlayersManager();
            logger.info("Server successfully initialized");
        } catch (Exception e) {
            logger.fatal("Server initialization error");
            throw new InitException(e);
        } 
    }
    
    /*
     * Handles shutdown sequence
     */
    private void shutdown() {
        logger.info("Server shutting down");
        channel.shutdown();
        dispatcher.shutdown();
        logger.info("Shutdown sequence completed");
    }

    /*
     * Reads configuration file
     */
    private void readConfig(String file) {
        logger.debug("Reading configuration file (" + file + ")");
        ConfigReader reader = new ConfigReader();
        try {
            reader.loadFrom(file);
            config = reader.getConfig();
            configModule = DI.objectModule(config, Config.class);
            logger.debug("Configuration read");
        } catch (Exception e) {
            logger.fatal("Failed to load configuration file (" + file + ")", e);
            throw new ConfigException(e);
        }
    }

    private void createDataSource() {
        logger.debug("Creating database connection");
        Class<? extends Database> cl = config.getDatabaseClass();
        database = DI.createWith(cl, configModule);
        databaseModule = DI.objectModule(database, Database.class);
        logger.debug("Database connection successfully initialized");
    }
    
    private void createChannel() {
        logger.debug("Creating message channel");
        Class<? extends MessageChannel> cl = config.getChannelClass();
        channel = DI.createWith(cl, configModule);
        channelModule = DI.objectModule(channel, MessageChannel.class);
        logger.debug("Message channel created");
    }

    private void createDispatcher() {
        logger.debug("Creating dispatcher");
        Class<? extends Dispatcher> cl = config.getDispatcherClass();
        dispatcher = DI.createWith(cl, configModule, channelModule);
        logger.debug("Dispatcher created");
    }

    private void createCustomPersistor() {
        logger.debug("Creating custom persistor");
        Class<? extends CustomPersistor> cl = config.getCustomPersistorClass();
        customPersistor = DI.createWith(cl, configModule, databaseModule);
        logger.debug("Custom persistor created");
    }

    private void createPlayersManager() {
        logger.debug("Creating players manager");
        Class<? extends PlayersManager> cl = config.getPlayerManagerClass();
        playersManager = DI.createWith(cl, configModule, databaseModule);
        logger.debug("Players manager created");
    }

}
