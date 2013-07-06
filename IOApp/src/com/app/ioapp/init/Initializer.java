package com.app.ioapp.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import pl.edu.agh.ki.mmorts.client.communication.ice.IceOutputChannel;
import pl.edu.agh.ki.mmorts.client.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionManagerImpl;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionProvider;
import pl.edu.agh.ki.mmorts.client.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.client.data.CustomPersistorImpl;
import pl.edu.agh.ki.mmorts.client.data.Database;
import pl.edu.agh.ki.mmorts.client.data.InMemDatabase;
import pl.edu.agh.ki.mmorts.client.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.client.data.PlayersPersistorImpl;
import pl.edu.agh.ki.mmorts.client.util.DI;
import pl.edu.agh.ki.mmorts.client.util.reflection.Methods;
import Ice.Util;
import android.util.Log;

import com.app.ioapp.communication.Dispatcher;
import com.app.ioapp.communication.Gateway;
import com.app.ioapp.communication.MessageOutputChannel;
import com.app.ioapp.config.Config;
import com.app.ioapp.config.ConfigReader;
import com.app.ioapp.config.ModuleConfigException;
import com.app.ioapp.config.ModuleConfigReader;
import com.app.ioapp.customDroidViews.AbstractModuleView;
import com.app.ioapp.login.LogInException;
import com.app.ioapp.login.LoginModule;
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
 * 3. {@code getMainView()}
 */
public class Initializer {
	/**
	 * Used by logger
	 */
	public static final String ID = "Initializer";
	
	/**
	 * Facade between phone application and module views
	 */
	private MainView view;
	
	/**
	 * Collection of Module Views
	 */
	private Set<Class<? extends AbstractModuleView>> moduleViews;
    
    /**
     * Stores properties read from configuration file
     */
    private Config config;
    private com.google.inject.Module configModule;
    
    /**
     * Reads configuration
     */
    private ConfigReader reader;

	/**
	 * Responsible for logging in, logging out and registering
	 */
	//private LoginModule loginModule;
	
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
    private CustomPersistor customPersistor;
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
     * Module which enables logging in and registering
     */
    private LoginModule loginModule;
	
	/**
	 * Stream to read configuration from file
	 */
	private InputStream configInput;
	/**
	 * Stream to read module configuration from file
	 */
	private InputStream moduleConfigInput;
	/**
	 * Stream to read ice configuration
	 */
	private InputStream iceConfigInput;
	
	/**
	 * Stream to write player information if he is not registered yet
	 */
	private OutputStream infoOutput;

	/**
	 * Transaction manager object
	 */
	private TransactionManager txManager;
	private com.google.inject.Module txManagerModule;
	
	
	/**
	 * @param configInput to read configuration from file
	 * @param infoOutput to write players info to file if he has not been registered yet. If he has, it is {@code null}
	 */
	public Initializer
	(InputStream configInput, InputStream moduleConfigInput, InputStream iceConfigInput, OutputStream infoOutput)  {
		this.configInput = configInput;
		this.moduleConfigInput = moduleConfigInput;
		this.iceConfigInput = iceConfigInput;
		this.infoOutput = infoOutput;
	}

	
	/**
	 * Called after initializing the rest of environment
	 * @param mail
	 * @param password
	 * @param alreadyRegistered true if player has been registered (a file with mail and password exists and is correct) 
	 * Exceptions must be handled by phone application
	 * @throws LogInxception
	 */
	public void logIn(String mail, String password, boolean alreadyRegistered) throws LogInException {
		try {
			Log.d(ID,"Logging in started");
			if (alreadyRegistered) {
				Log.d(ID,"User has already been registered");
				loginModule.logIn();
			}
			else {
				Log.d(ID,"User needs to be registered");
				Properties ps = new Properties();
				ps.setProperty("mail", mail);
				ps.setProperty("password", password);
				Log.d(ID,"User's data stored in a file");
				ps.store(infoOutput, null);
				loginModule.register(mail, password);
			}
		} catch (Exception e) {
			Log.e(ID, "Exception during logging in");
			throw new LogInException(e.getMessage());
		}
		
	}
	


/**
 * Initializes all classes. Called after logging in
 * Exceptions must be handled by phone application
 * @throws InitException
 */
	public void initialize() throws InitException{
		try {
			Log.d(ID,"Initializing environment started");
			readConfig();
			createTransactionManager();
			createChannel();
			createDispatcher();
			createDataSource();              
            createCustomPersistor();
            createPlayersPersistor();
			initModules();
			initMainView();
			initModuleViews();
			Log.d(ID, "Server successfully initialized");
		}
		catch (Exception e) {
			Log.e(ID, "Error during initialization");
			throw new InitException(e);
		}
			
		
	}

	


	private void createDataSource() {
        Log.d(ID, "Creating database connection");
        Class<? extends Database> cl = InMemDatabase.class;
        database = DI.createWith(cl);
        callInit(database);
        databaseModule = DI.objectModule(database, Database.class);
        Log.d(ID, "Database connection successfully initialized");
    }
	
	private void createCustomPersistor() {
        Log.d(ID, "Creating custom persistor");
        Class<CustomPersistor> ifcl = CustomPersistor.class;
        Class<? extends CustomPersistor> cl = CustomPersistorImpl.class;
        customPersistor = DI.createWith(cl, databaseModule);
        // Create special module
        customPersistorModule = DI.objectModule(
                customPersistor, ifcl);
        callInit(customPersistor);
        Log.d(ID, "Custom persistor created");
    }

    private void createPlayersPersistor() {
        Log.d(ID, "Creating players persistor");
        Class<? extends PlayersPersistor> cl = PlayersPersistorImpl.class;
        playersPersistor = DI.createWith(cl, databaseModule);
        playersPersistorModule = DI.objectModule(playersPersistor,
                PlayersPersistor.class);
        callInit(playersPersistor);
        Log.d(ID, "Players manager created");
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


    private void createChannel() throws IOException {
        Log.d(ID, "Creating message channel");
        Ice.Properties iceProperties = readIceProperties();
        channel = new IceOutputChannel(iceProperties);
        callInit(channel);
        channelModule = DI.objectModule(channel, MessageOutputChannel.class);
        Log.d(ID, "Message channel created");
    }
    
    private Ice.Properties readIceProperties() throws IOException {
    	try {
	    	Properties properties = new Properties();
	    	properties.load(iceConfigInput);
	    	Ice.Properties iceProperties = Util.createProperties();
	    	for (Object p : properties.keySet()) {
	    		iceProperties.setProperty((String)p, properties.getProperty((String)p));
	    	}
	    	return iceProperties;
    	} catch (IOException e) {
    		Log.e(ID, "Error while reading file with ice configuration");
    		throw e;
    	}
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
	
	
	private void initModuleViews() {
	// TODO Auto-generated method stub
	
}


	private void initMainView() {
	// TODO Auto-generated method stub
	
}

	
	/**
	 * A method which will be needed by phone application to get the Module Views
	 * @return collection of Module Views
	 */
	public Collection<Class<? extends AbstractModuleView>> getModuleViews() {
		return moduleViews;
	}
	
	public MessageOutputChannel getChannel() {
		return channel;
	}
	

	

}
