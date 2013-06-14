package pl.edu.agh.ki.mmorts.server.core;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.Main;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.config.ConfigException;
import pl.edu.agh.ki.mmorts.server.config.ConfigReader;
import pl.edu.agh.ki.mmorts.server.core.annotations.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.ConnectionCreator;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.server.data.SimpleConnectionPool;
import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.Module;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;
import pl.edu.agh.ki.mmorts.server.modules.ModuleInitException;
import pl.edu.agh.ki.mmorts.server.util.DI;
import pl.edu.agh.ki.mmorts.server.util.reflection.Methods;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;

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
 * </ul>
 */
public class Init {

    private static final Logger logger = Logger.getLogger(Init.class);

    /** Configuration file location */
    private static final String CONFIG = "resources/server.properties";

    /**
     * To prevent double shutdown in case the shutdown hook would cause it
     * without additional protection
     */
    private AtomicBoolean finished = new AtomicBoolean();

    /**
     * Transaction manager object created using the class specified in the
     * configuration
     */
    private TransactionManager txManager;
    private com.google.inject.Module txManagerModule;

    /**
     * Dispatcher object created using the class specified in the configuration
     */
    private Dispatcher dispatcher;
    private com.google.inject.Module dispatcherModule;

    /**
     * Message channel created using the class specified in the configuration
     */
    private MessageChannel channel;
    private com.google.inject.Module channelModule;

    /**
     * Custom persistor object created using the class specified in the
     * configuration
     */
    private Object customPersistor;
    private com.google.inject.Module customPersistorModule;

    /**
     * Players manager
     */
    private PlayersPersistor playersManager;
    private com.google.inject.Module playersManagerModule;

    /**
     * Database interface, implementation as in the configuration file
     */
    private Database database;
    private com.google.inject.Module databaseModule;

    /**
     * Configuration read from the config file and processed a bit
     */
    private Config config;
    private com.google.inject.Module configModule;

    /**
     * Runtime information about the modules
     */
    private ModuleTable moduleTable;
    private com.google.inject.Module moduleTableModule;

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

    /**
     * Waits until the shutdown is desired. Used as a filler between server init
     * and shutdown.
     */
    private void waitForShutdown() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            logger.debug("Input line: " + line);
        }
    }

    /**
     * Handles details of initialization. Called at the beginning of the
     * constructor.
     */
    private void init() {
        logger.info("Begin server initialization");
        try {
            registerShutdownHook();
            readConfig(CONFIG);
            createTransactionManager();
            createChannel();
            createDispatcher();
            initModules();
            createDataSource();
            createCustomPersistor();
            createPlayersManager();
            injectPersistors();
            logger.info("Server successfully initialized");
        } catch (Exception e) {
            logger.fatal("Server initialization error");
            throw new InitException(e);
        }
    }

    /**
     * Uses {@linkplain ModuleConfigReader} to read module config file, creates
     * the modules and registers them with a dispatcher.
     */
    private void initModules() {
        logger.info("Beginning initialization of modules");
        String confFile = config.getString(Config.MODULE_CONFIG_FILE);
        logger.info("Reading module data from " + confFile);
        try {
            ModuleConfigReader confReader = new ModuleConfigReader();
            confReader.load(confFile);
            logger.info("Loaded module configuration");
            // initialize
            Map<String, ModuleDescriptor> loaded = confReader.getModules();
            List<ConfiguredModule> modules = new ArrayList<ConfiguredModule>();
            logger.debug("Creating modules");
            for (ModuleDescriptor desc : loaded.values()) {
                logger.debug("Creating module " + desc.name);
                try {
                    Module m = createModule(desc);
                    logger.debug("Module " + desc.name + " created");
                    modules.add(new ConfiguredModule(m, desc));
                } catch (ModuleInitException e) {
                    logger.error("Module " + desc.name + " creation failed", e);
                }
            }
            // register with the dispatcher
            logger.debug("Registering modules with a dispatcher");
            dispatcher.registerModules(modules);
            createModuleTable();
        } catch (ModuleConfigException e) {
            logger.fatal("Error while readin module configuration");
            logger.fatal(e);
            throw new InitException(e);
        }
    }

    /**
     * Creates a module table and its' corresponding Guice module object.
     */
    private void createModuleTable() {
        int n = dispatcher.getModules().size();
        List<ModuleDescriptor> descs = new ArrayList<ModuleDescriptor>(n);
        for (ConfiguredModule conf : dispatcher.getModules()) {
            descs.add(conf.descriptor);
        }
        final Collection<ModuleDescriptor> modules = Collections
                .unmodifiableCollection(descs);
        moduleTable = new ModuleTable() {
            @Override
            public Collection<ModuleDescriptor> getModuleDescriptors() {
                return modules;
            }
        };
        moduleTableModule = DI.objectModule(moduleTable, ModuleTable.class);
    }

    /**
     * Injects persistors (players manager and custom persistor) into the
     * modules. Called at the end of initialization seqnece.
     */
    private void injectPersistors() {
        logger.debug("Injecting persistors");
        Injector injector = Guice.createInjector(customPersistorModule,
                playersManagerModule);
        for (ConfiguredModule conf : dispatcher.getModules()) {
            injector.injectMembers(conf.module);
        }
        logger.debug("Persistors injected");
    }

    /**
     * Creates the module described by the {@code desc} and calls @OnInit
     * methods.
     * 
     * @param desc
     *            Descriptor of the module to create
     * @return Created module
     */
    private Module createModule(ModuleDescriptor desc) {
        try {
            Class<? extends Module> cl = desc.moduleClass;
            Module module = DI.createWith(cl, configModule, dispatcherModule,
                    txManagerModule);
            callInit(module);
            return module;
        } catch (Exception e) {
            throw new ModuleInitException(e);
        }
    }

    /**
     * Handles shutdown sequence. Called at the end of the constructor.
     */
    private void shutdown() {
        // Only if not already cleaned up once - could happen because of
        // the shutdown hook
        if (!finished.getAndSet(true)) {
            logger.info("Server shutting down");
            if (dispatcher != null) {
                logger.debug("Shutting down dispatcher");
                callShutdown(dispatcher);
            }
            if (channel != null) {
                logger.debug("Shutting down communication channel");
                callShutdown(channel);
            }
            if (customPersistor != null) {
                logger.debug("Shutting down custom persistor");
                callShutdown(customPersistor);
            }
            if (playersManager != null) {
                logger.debug("Shutting down players manager");
                callShutdown(playersManager);
            }
            if (database != null) {
                logger.debug("Shutting down data source");
                callShutdown(database);
            }
            if (txManager != null) {
                logger.debug("Shutting down transaction manager");
                callShutdown(txManager);
            }
            logger.info("Shutdown sequence completed");
        }
    }

    /**
     * Reads configuration file. First part of the initialization sequence.
     */
    private void readConfig(String file) {
        logger.debug("Reading configuration file (" + file + ")");
        ConfigReader reader = new ConfigReader();
        try {
            reader.loadFrom(file);
            config = reader.getConfig();
            configModule = new AbstractModule() {
                @Override
                protected void configure() {
                    install(DI.objectModule(config, Config.class));
                    Names.bindProperties(binder(), config.getProperties());
                }
            };
            logger.debug("Configuration read");
        } catch (Exception e) {
            logger.fatal("Failed to load configuration file (" + file + ")");
            logger.fatal(e);
            throw new ConfigException(e);
        }
    }

    private void createTransactionManager() {
        logger.debug("Creating transaction manager");
        Class<? extends TransactionManager> cl = config
                .getTransactionManagerClass();
        txManager = DI.createWith(cl, configModule);
        callInit(txManager);
        txManagerModule = DI.objectModule(txManager, TransactionManager.class);
        logger.debug("Transaction manager successfully initialized");
    }

    private void createDataSource() {
        logger.debug("Creating database connection");
        Class<? extends Database> cl = config.getDatabaseClass();
        database = DI.createWith(cl, configModule, txManagerModule,
                moduleTableModule, new AbstractModule() {
                    @Override
                    protected void configure() {
                        bind(ConnectionCreator.class).to(
                                config.getConnectionCreatorClass());
                        bind(Driver.class).to(config.getJdbcDriverClass());
                        bind(SimpleConnectionPool.class);
                    }
                });
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
        dispatcher = DI.createWith(cl, configModule, channelModule,
                txManagerModule);
        callInit(dispatcher);
        dispatcherModule = DI.objectModule(dispatcher, Gateway.class);
        logger.debug("Dispatcher created");
    }

    private void createCustomPersistor() {
        logger.debug("Creating custom persistor");
        Class<?> ifcl = config.getCustomPersistorInterface();
        Class<?> cl = config.getCustomPersistorClass();
        customPersistor = DI.createWith(cl, configModule, databaseModule,
                txManagerModule);
        // Create special module
        customPersistorModule = DI.objectModuleAnnotatedDynamic(
                customPersistor, ifcl, CustomPersistor.class);
        callInit(customPersistor);
        logger.debug("Custom persistor created");
    }
    
    
    


    private void createPlayersManager() {
        logger.debug("Creating players manager");
        Class<? extends PlayersPersistor> cl = config.getPlayerManagerClass();
        playersManager = DI.createWith(cl, configModule, databaseModule,
                txManagerModule);
        playersManagerModule = DI.objectModule(playersManager,
                PlayersPersistor.class);
        callInit(playersManager);
        logger.debug("Players manager created");
    }

    /**
     * Attempts to call method annotated with {@linkplain OnInit}.
     * 
     * @param o
     *            Object on which the method is to be invocated
     */
    private static void callInit(Object o) {
        Methods.callAnnotated(OnInit.class, o);
    }

    /**
     * Attempts to call method annotated with {@linkplain OnShutdown}.
     * 
     * @param o
     *            Object on which the method is to be invocated
     */
    private static void callShutdown(Object o) {
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
