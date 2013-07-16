package com.app.ioapp.init;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import pl.edu.agh.ki.mmorts.client.backend.communication.Dispatcher;
import pl.edu.agh.ki.mmorts.client.backend.communication.Gateway;
import pl.edu.agh.ki.mmorts.client.backend.communication.MessageOutputChannel;
import pl.edu.agh.ki.mmorts.client.backend.communication.SingleThreadedDispatcher;
import pl.edu.agh.ki.mmorts.client.backend.communication.ice.IceOutputChannel;
import pl.edu.agh.ki.mmorts.client.backend.config.Config;
import pl.edu.agh.ki.mmorts.client.backend.config.ConfigReader;
import pl.edu.agh.ki.mmorts.client.backend.config.ModuleConfigException;
import pl.edu.agh.ki.mmorts.client.backend.config.ModuleConfigReader;
import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.backend.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.client.backend.core.transaction.TransactionManagerImpl;
import pl.edu.agh.ki.mmorts.client.backend.core.transaction.TransactionProvider;
import pl.edu.agh.ki.mmorts.client.backend.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.client.backend.data.CustomPersistorImpl;
import pl.edu.agh.ki.mmorts.client.backend.data.Database;
import pl.edu.agh.ki.mmorts.client.backend.data.InMemDatabase;
import pl.edu.agh.ki.mmorts.client.backend.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.client.backend.data.PlayersPersistorImpl;
import pl.edu.agh.ki.mmorts.client.backend.init.InitException;
import pl.edu.agh.ki.mmorts.client.backend.loginMod.LogInException;
import pl.edu.agh.ki.mmorts.client.backend.loginMod.LoginModule;
import pl.edu.agh.ki.mmorts.client.backend.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.client.backend.modules.Module;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleDescriptor;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleInitException;
import pl.edu.agh.ki.mmorts.client.backend.modules.ServiceLocator;
import pl.edu.agh.ki.mmorts.client.backend.util.DI;
import pl.edu.agh.ki.mmorts.client.backend.util.reflection.Methods;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.view.ModulesBroker;
import Ice.Util;
import android.content.Context;
import android.util.Log;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

/**
 * A class responsible for the initialization of the environment.
 * <p>
 * Performs the following operations:
 * <ul>
 * <li>Creates class with configuration
 * <li>Creates & initializes modules, based on the configuration
 * 
 * Methods should be called in the following order: 1. {@code initialize()} 2.
 * {@code logIn()} 3. {@code getMainView()}
 */
public class Initializer {
	/**
	 * Used by logger
	 */
	public static final String ID = "Initializer";

	/**
	 * Facade between phone application and module views
	 */
	private ModulesBroker modulesBroker;

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
	private OutputStream logDataOutput;

	/**
	 * Transaction manager object
	 */
	private TransactionManager txManager;
	private com.google.inject.Module txManagerModule;
	
	private List<ConfiguredModule> configuredModules;
	
	
	private Context context;

	/**
	 * @param context
	 */

	public Initializer(Context context) {
		this.context = context;
	}
	
	
	/**
	 * Initializes all classes. Called after logging in Exceptions must be
	 * handled by phone application
	 * 
	 * @throws InitException
	 */
	public void initialize() throws InitException {
		try {
			Log.d(ID, "Initializing environment started");
			openFiles();
			readConfig();
			createTransactionManager();
			createChannel();
			createDispatcher();
			createDataSource();
			createCustomPersistor();
			createPlayersPersistor();
			initModules();
			initModulesBroker();
			Log.d(ID, "Successfully initialized");
		} catch (Exception e) {
			Log.e(ID, "Error during initialization");
			throw new InitException(e);
		}

	}
	
	private void openFiles() {
		try {
			iceConfigInput = context.getAssets().open("iceClient.config");
			configInput = context.getAssets().open("client.properties");
			moduleConfigInput = context.getAssets().open("modules.json");
		} catch (IOException e) {
			Log.e(ID, "Exception during opening config files");
			e.printStackTrace();
		}
		
	}

	private void readConfig() {
		Log.d(ID, "Reading config");
		reader = new ConfigReader(configInput);
		reader.configure();
		config = reader.getConfig();
		configModule = DI.objectModule(config, Config.class);
		Log.d(ID, "Read: " + config);
	}

	private void createTransactionManager() {
		Log.d(ID, "Creating transaction manager");
		Class<? extends TransactionManager> cl = TransactionManagerImpl.class;
		txManager = DI.createWith(cl, configModule);
		callInit(txManager);
		txManagerModule = DI.objectModule(txManager, TransactionManager.class);
		Log.d(ID, "Transaction manager successfully initialized");
	}

	private void createChannel() throws IOException {
		Log.d(ID, "Creating message channel");
		Ice.Properties iceProperties = readIceProperties();
		channel = new IceOutputChannel(iceProperties);
		callInit(channel);
		channelModule = DI.objectModule(channel, MessageOutputChannel.class);
		Log.d(ID, "Message channel created");
	}



	private void createDispatcher() {
		Log.d(ID, "Creating dispatcher");
		Class<? extends Dispatcher> cl = SingleThreadedDispatcher.class;
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
		customPersistorModule = DI.objectModuleAnnotated(customPersistor, ifcl, pl.edu.agh.ki.mmorts.client.backend.core.annotations.CustomPersistor.class);
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
			configuredModules = new ArrayList<ConfiguredModule>();
			Log.d(ID, "Creating modules");
			for (ModuleDescriptor desc : loaded.values()) {
				Log.d(ID, "Creating module " + desc.name);
				try {
					Module m = createModule(desc);
					Log.d(ID, "Module " + desc.name + " created");
					configuredModules.add(new ConfiguredModule(m, desc));
				} catch (ModuleInitException e) {
					Log.e(ID, "Module " + desc.name + " creation failed", e);
				}
			}
			// register with the dispatcher
			Log.d(ID, "Registering modules with a dispatcher");
			dispatcher.registerModules(configuredModules);
		} catch (ModuleConfigException e) {
			Log.e(ID, "Error while readin module configuration");
			Log.e(ID, e.getMessage());
			throw new InitException(e);
		}
	}
	
	private void initModulesBroker() {
		Log.d(ID, "Initiliazing broker and his modules");
		Map<String, GUICommModule> communicatingModules = new HashMap<String, GUICommModule>();
		for(ConfiguredModule module :configuredModules){
			try {
				communicatingModules.put(module.descriptor.name, (GUICommModule)module.module);
			} catch (Exception e) {
				Log.e(ID, module.descriptor.name + " cannot be load as communicating module");
			}
		}
		modulesBroker = new ModulesBroker(communicatingModules, configuredModules);
		Log.d(ID, "Broker initialized");
	}


	/**
	 * Called after initializing the rest of environment
	 * 
	 * @param mail
	 * @param password
	 * @param alreadyRegistered
	 *            true if player has been registered (a file with mail and
	 *            password exists and is correct) Exceptions must be handled by
	 *            phone application
	 * @throws LogInxception
	 */
	public void logIn(String mail, String password, boolean alreadyRegistered)
			throws LogInException {
		try {
			Log.d(ID, "Logging in started");
			if (alreadyRegistered) {
				Log.d(ID, "User has already been registered");
				loginModule.logIn();
			} else {
				Log.d(ID, "User needs to be registered");
				Log.d(ID, "Registering");
				loginModule.register(mail, password);
				Log.d(ID, "Writing mail and password to file");
				Properties ps = new Properties();
				ps.setProperty("mail", mail);
				ps.setProperty("password", password);
				ps.store(logDataOutput, null);
				Log.d(ID, "User's data stored in a file");
			}
		} catch (Exception e) {
			Log.e(ID, "Exception during logging in");
			throw new LogInException(e);
		}

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

	private Ice.Properties readIceProperties() throws IOException {
		try {
			Properties properties = new Properties();
			properties.load(iceConfigInput);
			Ice.Properties iceProperties = Util.createProperties();
			for (Object p : properties.keySet()) {
				iceProperties.setProperty((String) p,
						properties.getProperty((String) p));
			}
			return iceProperties;
		} catch (IOException e) {
			Log.e(ID, "Error while reading file with ice configuration");
			throw e;
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
	 * Returns MainView for Module Views
	 * 
	 * @return MainView object
	 */
	public ModulesBroker getModulesBroker() {
		return modulesBroker;
	}

	public MessageOutputChannel getChannel() {
		return channel;
	}

	public List<String> getModules() {
		// TODO Kasiaa, uzupe�nij t� metod�
		return Arrays.asList(new String[] { "JEDNE", "DRUGIE" });
	}

}
