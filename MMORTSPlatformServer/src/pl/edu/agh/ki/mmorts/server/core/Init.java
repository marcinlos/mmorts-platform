package pl.edu.agh.ki.mmorts.server.core;

import java.util.Scanner;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.agh.edu.ki.mmorts.server.config.ConfigException;
import pl.agh.edu.ki.mmorts.server.config.ConfigReader;
import pl.agh.edu.ki.mmorts.server.util.DI;
import pl.agh.edu.ki.mmorts.server.util.reflection.Methods;
import pl.edu.agh.ki.mmorts.server.Main;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.PlayersManager;

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
            waitForShutdown();
        } catch (Exception e) {
            logger.fatal("Server error, cannot continue", e);
        } finally {
            shutdown();
        }
    }

    /*
     * Waits until the shutdown is desired.
     */
    private void waitForShutdown() {
        // TODO: Simple waiting for EOF in the input, change for fully-fledged
        // interactive CLI?
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            
        }
    }

    /*
     * Handles details of initialization
     */
    private void init() {
        logger.info("Begin server initialization");
        try {
            registerShutdownHook();
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
        logger.debug("Shutting down dispatcher");
        callShutdown(dispatcher);
        logger.debug("Shutting down communication channel");
        callShutdown(channel);
        logger.debug("Shutting down custom persistor");
        callShutdown(customPersistor);
        logger.debug("Shutting down players manager");
        callShutdown(playersManager);
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
        callInit(database);
        databaseModule = DI.objectModule(database, Database.class);
        logger.debug("Database connection successfully initialized");
    }

    private void createChannel() {
        logger.debug("Creating message channel");
        Class<? extends MessageChannel> cl = config.getChannelClass();
        channel = DI.createWith(cl, configModule);
        callInit(channel);
        channelModule = DI.objectModule(channel, MessageChannel.class);
        logger.debug("Message channel created");
    }

    private void createDispatcher() {
        logger.debug("Creating dispatcher");
        Class<? extends Dispatcher> cl = config.getDispatcherClass();
        dispatcher = DI.createWith(cl, configModule, channelModule);
        callInit(dispatcher);
        logger.debug("Dispatcher created");
    }

    private void createCustomPersistor() {
        logger.debug("Creating custom persistor");
        Class<? extends CustomPersistor> cl = config.getCustomPersistorClass();
        customPersistor = DI.createWith(cl, configModule, databaseModule);
        callInit(customPersistor);
        logger.debug("Custom persistor created");
    }

    private void createPlayersManager() {
        logger.debug("Creating players manager");
        Class<? extends PlayersManager> cl = config.getPlayerManagerClass();
        playersManager = DI.createWith(cl, configModule, databaseModule);
        callInit(playersManager);
        logger.debug("Players manager created");
    }

    /**
     * Attempts to call method annotated with {@linkplain OnInit}.
     * 
     * @param o
     *            Object on which the method is to be invocated
     */
    private void callInit(Object o) {
        Methods.callAnnotated(OnInit.class, o);
    }

    /**
     * Attempts to call method annotated with {@linkplain OnShutdown}.
     * 
     * @param o
     *            Object on which the method is to be invocated
     */
    private void callShutdown(Object o) {
        Methods.callAnnotated(OnShutdown.class, o);
    }
    
    /*
     * Registers a shutdown hook, which causes the cleanup to be performed even
     * when the application is shut down in a brutal manner (e.g. after ctrl+c).
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

}
