package com.app.ioapp.init;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pl.edu.agh.ki.mmorts.client.communication.ice.IceOutputChannel;
import pl.edu.agh.ki.mmorts.client.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionManagerImpl;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionProvider;
import pl.edu.agh.ki.mmorts.client.data.Database;
import pl.edu.agh.ki.mmorts.client.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.client.util.DI;
import pl.edu.agh.ki.mmorts.client.util.reflection.Methods;
import android.util.Log;

import com.app.ioapp.communication.Dispatcher;
import com.app.ioapp.communication.Gateway;
import com.app.ioapp.communication.MessageOutputChannel;
import com.app.ioapp.config.Config;
import com.app.ioapp.config.ConfigReader;
import com.app.ioapp.config.ModuleConfigException;
import com.app.ioapp.config.ModuleConfigReader;
import com.app.ioapp.modules.ConfiguredModule;
import com.app.ioapp.modules.Module;
import com.app.ioapp.modules.ModuleDescriptor;
import com.app.ioapp.modules.ModuleInitException;
import com.app.ioapp.modules.ServiceLocator;
import com.app.ioapp.view.MainView;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * A class responsible for the initialization of the environment.
 * <p> Performs the following operations:
 * <ul>
 * <li>Creates class with configuration
 * <li>Creates & initializes modules, based on the configuration
 * 
 * Methods should be called in the following order:
 * 1. {@code initialize()}
 * 2. {@code logIn()}
 * 3. {@code synchronizeState()}
 * 4. {@code getMainView}
 */
public class Initializer {
	/**
	 * Used by logger
	 */
	public static final String ID = "Initializer";
	
	/**
	 * Tells if player should be logged in or registered
	 */
	private boolean alreadyRegistered;
	
	/**
	 * Identifies player
	 */
	private String mail;
	/**
	 * Player's password
	 */
	private String password;
	
	/**
	 * Facade between phone application and module views
	 */
	private MainView view;
    
    /**
     * Stores properties read from  configuration file
     */
    private Config config;
    private com.google.inject.Module configModule;
    
    /**
     * Reads configuration
     */
    private ConfigReader reader;
	/**
	 * Synchronizes state with server
	 */
	private Synchronizer synchronizer;
	/**
	 * Responsible for logging in, logging out and registering
	 */
	private LoginModule loginModule;
	
    /**
     * Dispatcher object
     */
	private Dispatcher dispatcher;
	private com.google.inject.Module dispatcherModule;
	
    /**
     * Message channel created using the class specified in the configuration
     */
    private MessageOutputChannel channel;
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
    private PlayersPersistor playersPersistor;
    private com.google.inject.Module playersPersistorModule;
    
    /**
     * Database interface, implementation as in the configuration file
     */
    private Database database;
    private com.google.inject.Module databaseModule;
	
	/**
	 * Stream to read configuration from file
	 */
	private InputStream configInput;
	/**
	 * Stream to read module configuration from file
	 */
	private InputStream moduleConfigInput;
	/**
	 * Stream to write player information if he is not registered yet
	 */
	private FileOutputStream infoOutput;

	/**
	 * Transaction manager object
	 */
	private TransactionManager txManager;
	private com.google.inject.Module txManagerModule;
	
	
	/**
	 * @param mail
	 * @param password
	 * @param alreadyRegistered true if player has been registered (a file with mail and password exists and is correct) 
	 * @param configInput to read configuration from file
	 * @param infoOutput to write players info to file if he has not been registered yet. If he has, it is {@code null}
	 */
	public Initializer
	(String mail, String password, boolean alreadyRegistered, InputStream configInput, 
			InputStream moduleConfigInput, FileOutputStream infoOutput)  {
		this.mail = mail;
		this.password = password;
		this.alreadyRegistered = alreadyRegistered;
		this.configInput = configInput;
		this.moduleConfigInput = moduleConfigInput;
		this.infoOutput = infoOutput;
	}

	
	/**
	 * Called after initializing the rest of environment
	 * Exceptions must be handled by phone application
	 * @throws IOException
	 * @throws RegisterException
	 */
	public void logIn() throws IOException, RegisterException, LogInException {
		Log.e(ID,"Logging in started");
		this.loginModule = new LoginModule(dispatcher, mail, password);
		if (alreadyRegistered) {
			Log.e(ID,"User has already been registered");
			loginModule.logIn();
		}
		else {
			Log.e(ID,"User needs to be registered");
			Properties ps = new Properties();
			ps.setProperty("mail", mail);
			ps.setProperty("password", password);
			Log.e(ID,"User's data stored in a file");
			ps.store(infoOutput, null);
			loginModule.register(mail, password);
		}
		
	}
	


/**
 * Initializes all classes. Called after logging in
 * Exceptions must be handled by phone application
 * @throws InitException
 */
	public void initialize() throws InitException{
		try {
			Log.e(ID,"Initializing environment started");
			readConfig();
			createTransactionManager();
			createChannel();
			createDispatcher();
			//createDataSource();               //  <-----------------------------------------
            //createCustomPersistor();
            //createPlayersPersistor();
			initModules();
			Log.d(ID, "Server successfully initialized");
		}
		catch (Exception e) {
			Log.e(ID, "Error during initialization");
			throw new InitException(e);
		}
			
		
	}

    private void createTransactionManager() {
        Log.d(ID, "Creating transaction manager");
        Class<? extends TransactionManager> cl = TransactionManagerImpl.class;
        txManager = DI.createWith(cl, configModule);
        callInit(txManager);
        txManagerModule = DI.objectModule(txManager, TransactionManager.class);
        Log.d(ID, "Transaction manager successfully initialized");
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



	private void readConfig() {
		Log.d(ID, "Reading config");
		reader = new ConfigReader(configInput);
		reader.configure();
		config = reader.getConfig();
}


    private void createChannel() {
        Log.d(ID, "Creating message channel");
        Class<? extends MessageOutputChannel> cl = IceOutputChannel.class;
        channel = DI.createWith(cl, configModule);
        callInit(channel);
        channelModule = DI.objectModule(channel, MessageOutputChannel.class);
        Log.d(ID, "Message channel created");
    }
    
    private void createDispatcher() {
        Log.d(ID, "Creating dispatcher");
        Class<? extends Dispatcher> cl = Dispatcher.class;
        dispatcher = DI.createWith(cl, configModule, channelModule,
                txManagerModule);
        callInit(dispatcher);
        dispatcherModule = new AbstractModule() {
            @Override
            protected void configure() {
                install(DI.objectModule(dispatcher, Gateway.class));
                install(DI.objectModule(dispatcher, ServiceLocator.class));
            }
        };
        Log.d(ID, "Dispatcher created");
    }
    
    
    /**
     * Uses {@linkplain ModuleConfigReader} to read module config file, creates
     * the modules and registers them with a dispatcher.
     */
    private void initModules() {
        Log.d(ID, "Beginning initialization of modules");
        try {
            ModuleConfigReader confReader = new ModuleConfigReader();
            confReader.load(moduleConfigInput);
            Log.d(ID, "Loaded module configuration");
            // initialize
            Map<String, ModuleDescriptor> loaded = confReader.getModules();
            List<ConfiguredModule> modules = new ArrayList<ConfiguredModule>();
            Log.d(ID, "Creating modules");
            for (ModuleDescriptor desc : loaded.values()) {
                Log.d(ID, "Creating module " + desc.name);
                try {
                    Module m = createModule(desc);
                    Log.d(ID, "Module " + desc.name + " created");
                    modules.add(new ConfiguredModule(m, desc));
                } catch (ModuleInitException e) {
                    Log.e(ID, "Module " + desc.name + " creation failed", e);
                }
            }
            // register with the dispatcher
            Log.d(ID, "Registering modules with a dispatcher");
            dispatcher.registerModules(modules);
        } catch (ModuleConfigException e) {
            Log.e(ID, "Error while readin module configuration");
            Log.e(ID, e.getMessage());
            throw new InitException(e);
        }
    }
	
	private Module createModule(final ModuleDescriptor desc) {
	    try {
	        Class<? extends Module> cl = desc.moduleClass;
	        // Inject config, dispatcher, tx manager and individual module
	        // configuration
	        com.google.inject.Module properties = new AbstractModule() {
	            @Override
	            protected void configure() {
	                Names.bindProperties(binder(), desc.config.asMap());
	            }
	        };
	        Module module = DI.createWith(cl, configModule, dispatcherModule,
	                DI.objectModule(txManager.getProvider(),
	                        TransactionProvider.class), properties,
	                playersPersistorModule, customPersistorModule, DI
	                        .objectModule(desc, ModuleDescriptor.class));
	        callInit(module);
	        return module;
	    } catch (Exception e) {
	        throw new ModuleInitException(e);
	    }
	}
	
	
	/**
	 * Synchronizes local state with server state
	 */
	public void synchronizeState() {
		Log.d(ID, "Synchronizing state began");
		synchronizer.synchronizeState();
	}
	
	/**
	 * A method which will be needed by phone application to get the MainView
	 * @return MainView object
	 */
	public MainView getMainView() {
		return view;
	}
	

	

}
