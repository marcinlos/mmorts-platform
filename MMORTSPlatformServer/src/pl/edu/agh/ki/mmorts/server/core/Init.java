package pl.edu.agh.ki.mmorts.server.core;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.agh.edu.ki.mmorts.server.config.ConfigException;
import pl.agh.edu.ki.mmorts.server.config.ConfigReader;
import pl.agh.edu.ki.mmorts.server.util.DI;
import pl.edu.agh.ki.mmorts.server.Main;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.data.PlayersDAO;
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

    /**
     * Custom persistor object created using the class specified in the
     * configuration
     */
    private CustomPersistor customPersistor;

    private PlayersManager playersManager;

    private PlayersDAO playersDao;
    private Module daoModule;

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
        logger.info("Begin server initialization");
        try {
            init();
            logger.info("Server successfully initialized");
        } catch (Exception e) {
            logger.fatal("Server initialization error");
            throw new InitException(e);
        }
    }

    /*
     * Handles details of initialization
     */
    private void init() {
        readConfig(CONFIG);
        createDataSource();
        createDispatcher();
        createCustomPersistor();
        createPersistence();
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
        Class<? extends PlayersDAO> cl = config.getPlayersDaoClass();
        playersDao = DI.createWith(cl, configModule);
        daoModule = DI.objectModule(playersDao, PlayersDAO.class);
        logger.debug("Database connection successfully initialized");
    }

    private void createDispatcher() {
        logger.debug("Creating dispatcher");
        Class<? extends Dispatcher> cl = config.getDispatcherClass();
        dispatcher = DI.createWith(cl, configModule);
        logger.debug("Dispatcher created");
    }

    private void createCustomPersistor() {
        logger.debug("Creating custom persistor");
        Class<? extends CustomPersistor> cl = config.getCustomPersistorClass();
        customPersistor = DI.createWith(cl, configModule, daoModule);
        logger.debug("Custom persistor created");
    }

    private void createPersistence() {
        logger.debug("Creating players persistence");
        // throw new NotImplementedException();
        logger.debug("Players persistence created");
    }

}
