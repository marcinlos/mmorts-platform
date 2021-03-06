package com.app.ioapp.init;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import pl.edu.agh.ki.mmorts.client.backend.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.client.backend.data.CustomPersistorImpl;
import pl.edu.agh.ki.mmorts.client.backend.data.Database;
import pl.edu.agh.ki.mmorts.client.backend.data.InMemDatabase;
import pl.edu.agh.ki.mmorts.client.backend.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.client.backend.data.PlayersPersistorImpl;
import pl.edu.agh.ki.mmorts.client.backend.init.InitException;
import pl.edu.agh.ki.mmorts.client.backend.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.client.backend.modules.Module;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleDescriptor;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleInitException;
import pl.edu.agh.ki.mmorts.client.backend.modules.ServiceLocator;
import pl.edu.agh.ki.mmorts.client.backend.util.DI;
import pl.edu.agh.ki.mmorts.client.backend.util.reflection.Methods;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ConcreteModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.Bus;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ConcreteBus;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.ConcreteMainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.ConcreteTopSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;
import roboguice.RoboGuice;
import Ice.Util;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
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

@Singleton
public class Initializer {

	@Inject
	private AssetManager assetManager;

	@Inject
	private Context context;
	@Inject
	private LayoutInflater inflater;
	/**
	 * Used by logger
	 */
	public static final String ID = "Initializer";

	/**
	 * Facade between phone application and module views
	 */
	private ModulesBroker modulesBroker;
	private com.google.inject.Module modulesBrokerModule;

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

	private TopSpaceManager topSpaceManager;
	private com.google.inject.Module topSpaceManagerModule;

	private MainSpaceManager mainSpaceManager;
	private com.google.inject.Module mainSpaceManagerModule;

	private Bus bus;
	private com.google.inject.Module busModule;
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
	 * input stream with login contents, should never be null due to
	 * implementation
	 */

	private FileInputStream loginInput;

	/**
	 * output stream to file with login contents, starts at the end of already
	 * existing file if it had some content
	 */
	private FileOutputStream loginOutput;

	/**
	 * Transaction manager object
	 */
	private TransactionManager txManager;
	private com.google.inject.Module txManagerModule;

	private List<ConfiguredModule> configuredModules;

	private View mainScreenView;
	// private com.google.inject.Module mainScreenViewModule;
	private View topView;
	// private com.google.inject.Module topViewModule;
	private View mainModulesView;

	// private com.google.inject.Module mainModulesViewModule;

	/**
	 * Initializes all classes. Called after logging in Exceptions must be
	 * handled by phone application
	 * 
	 * @throws InitException
	 */
	public void initialize() throws InitException {
		try {
			Log.d(ID, "Initializing environment started");
			prepareViewModules();
			openFiles();
			readConfig();
			createTransactionManager();
			createChannel();
			createDispatcher();
			createDataSource();
			createCustomPersistor();
			createPlayersPersistor();
			createBus();
			createMainSpaceManager();
			createTopSpaceManager();
			initModulesBroker();
			initModules();

			Log.d(ID, "Successfully initialized");
		} catch (Exception e) {
			Log.e(ID, "Error during initialization");
			throw new InitException(e);
		}

	}

	public View getMainScreenView() {
		return mainScreenView;
	}

	public View getTopView() {
		return topView;
	}

	public View getMainModulesView() {
		return mainModulesView;
	}

	private void prepareViewModules() {
		Log.d(ID, "Preparing Views");
		mainScreenView = inflater.inflate(R.layout.activity_main, null);
		Log.d(ID, String.format("MainScreenView: %s", mainScreenView));
		topView = mainScreenView.findViewById(R.id.topSpace);
		Log.d(ID, String.format("TopView: %s", topView));
		Log.d(ID, String.format("TopViewParent: %s", topView.getParent()));
		View v = mainScreenView.findViewById(R.id.topParentForDebug); // TODO
																		// remove
		Log.d(ID, String.format("TopViewTrueParent: %s", v));
		Log.d(ID, String.format("TopViewTrueParentsParent: %s", v.getParent()));
		mainModulesView = mainScreenView.findViewById(R.id.mainSpace);
		Log.d(ID, String.format("MainModulesView: %s", mainModulesView));

		// mainActivityViewModule = DI.objectModule(mainActivityView,
		// View.class);
		// topViewModule = DI.objectModule(topView, View.class);
		// mainModulesViewModule = DI.objectModule(mainModulesView, View.class);
		// mainScreenView = inflater.inflate(R.layout.activity_main, null);
		// LayoutInflater inf =inflater.cloneInContext(context);
		// inf.setFactory(new FactoryMine());

	}

	private void openFiles() {
		Log.d(ID, "Opening files");
		try {
			iceConfigInput = assetManager.open("iceClient.config");
			configInput = assetManager.open("client.properties");
			moduleConfigInput = assetManager.open("modules.json");
			loginOutput = context.openFileOutput("loginDataFile",
					Context.MODE_APPEND);
			try {
				loginInput = context.openFileInput("loginDataFile");
			} catch (FileNotFoundException e) {
				Log.e(ID,
						"file with login not found, shouldn't be possible with this order of creating",
						e);
			}

		} catch (IOException e) {
			Log.e(ID, "Exception during opening config files");
			e.printStackTrace();
		}
		Log.d(ID, "Files opened");

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
		customPersistorModule = DI
				.objectModuleAnnotated(
						customPersistor,
						ifcl,
						pl.edu.agh.ki.mmorts.client.backend.core.annotations.CustomPersistor.class);
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

	private void createBus() {
		Log.d(ID, "Creating frontend bus");
		bus = DI.createWith(ConcreteBus.class);
		RoboGuice.injectMembers(context, bus);
		busModule = DI.objectModule(bus, Bus.class);
		Log.d(ID, "Frontend bus created");
	}

	private void createMainSpaceManager() {
		Log.d(ID, "Initiliazing main space manager");
		mainSpaceManager = DI.createWith(ConcreteMainSpaceManager.class,
				busModule);
		RoboGuice.injectMembers(context, mainSpaceManager);
		mainSpaceManagerModule = DI.objectModule(mainSpaceManager,
				MainSpaceManager.class);
		callInit(mainSpaceManager);
		Log.d(ID, "Main space manager initialized");
	}

	private void createTopSpaceManager() {
		Log.d(ID, "Initiliazing top space manager");
		topSpaceManager = DI.createWith(ConcreteTopSpaceManager.class,
				busModule);
		RoboGuice.injectMembers(context, topSpaceManager);
		topSpaceManagerModule = DI.objectModule(topSpaceManager,
				TopSpaceManager.class);
		callInit(topSpaceManager);
		Log.d(ID, "Top space manager initialized");
	}

	private void initModulesBroker() {
		Log.d(ID, "Initiliazing broker");

		modulesBroker = DI.createWith(ConcreteModulesBroker.class, busModule);
		modulesBrokerModule = DI.objectModule(modulesBroker,
				ModulesBroker.class);
		callInit(modulesBroker);
		Log.d(ID, "Broker initialized");
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
					// TODO: oki, ale brzydkie
					Log.d(ID, String.format("Creating %s module presenter",
							desc.config.get("presenter")));
					if (desc.config.get("presenter") != null) {
						
						Class<? extends ModulePresenter> presenterClass = Class
								.forName(desc.config.get("presenter"))
								.asSubclass(ModulePresenter.class);
						ModulePresenter pres = DI.createWith(presenterClass,
								modulesBrokerModule, mainSpaceManagerModule,
								topSpaceManagerModule, busModule,DI
								.objectModule(desc, ModuleDescriptor.class));
						RoboGuice.getInjector(context).injectMembers(pres);
						// TODO: EVIL CAST - remove next time
						modulesBroker.registerModule((GUICommModule) m, desc.name);
						callInit(pres);
					}
				} catch (ModuleInitException e) {
					Log.e(ID, "Module " + desc.name + " creation failed", e);
				} catch (ClassNotFoundException e) {
					// TODO wrzuci� do konfiguracji modu�u!
					Log.e(ID, "Module " + desc.name + " presenter isn't valid",
							e);
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
					DI.objectModule(txManager,
							TransactionManager.class), properties,
					playersPersistorModule, customPersistorModule, DI
							.objectModule(desc, ModuleDescriptor.class),
							DI.objectModule(loginInput, FileInputStream.class),
							DI.objectModule(loginOutput, FileOutputStream.class),
							modulesBrokerModule);
			callInit(module);
			return module;
		} catch (Exception e) {
			throw new ModuleInitException(e);
		}
	}

}
