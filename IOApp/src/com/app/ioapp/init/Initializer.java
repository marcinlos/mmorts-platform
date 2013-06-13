package com.app.ioapp.init;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import com.app.ioapp.communication.Dispatcher;
import com.app.ioapp.config.Config;
import com.app.ioapp.config.ConfigException;
import com.app.ioapp.config.ConfigReader;
import com.app.ioapp.config.PropertiesLoader;
import com.app.ioapp.config.ReadingPropertiesException;
import com.app.ioapp.data.Context;
import com.app.ioapp.data.State;
import com.app.ioapp.modules.Module;
import com.app.ioapp.view.MainView;

/**
 * A class responsible for the initialization of the environment.
 * <p> Performs the following operations:
 * <ul>
 * <li>Creates class with configuration
 * <li>Creates & initializes modules, based on the configuration
 * 
 */
public class Initializer {
	
	/**
	 * Tells if player should be logged in or registered
	 */
	private boolean alreadyRegistered;
	
	/**
	 * Identifies player
	 */
	private String mail;
	private String password;
	
    /** 
     * Info file location
     * This file stores information about players account: mail, password
     */
    private static final String info = "appInfo/info.properties";
	
    /** Configuration file location */
    private static final String configFile = "resources/client.properties";
    
    /**
     * Stores properties read from  configuration file
     */
    private Config config;
    private ConfigReader reader;
	
	private Synchronizer synchronizer;
	private LoginModule loginModule;
	
    /**
     * Dispatcher object
     */
	private Dispatcher dispatcher;
	private MainView view;
	private Context context;
	private State state;
	private Map<String,Module> modules;
	
	
	public Initializer(String mail, String password, boolean alreadyRegistered) throws ConfigException, IOException {
		this.mail = mail;
		this.password = password;
		this.alreadyRegistered = alreadyRegistered;
		initialize();
	}
	
	/**
	 * Returns object with configuration
	 * @return Config object
	 */
	public Config getConfig() {
		return config;
	}
	

/**
 * Initializes all classes
 * Exceptions must be handled by phone application
 * @throws ConfigException
 * @throws IOException 
 */
	private void initialize() throws ConfigException, IOException{
			reader = new ConfigReader(configFile);
			reader.configure();
			config = reader.getConfig();
	
			//this.dispatcher = new ThreadedDispatcher();
			this.loginModule = new LoginModule(dispatcher, mail, password);
			if (alreadyRegistered) {
				loginModule.logIn();
			}
			else {
				FileOutputStream stream = new FileOutputStream(new File(info));
				Properties ps = new Properties();
				ps.setProperty("mail", mail);
				ps.setProperty("password", password);
				ps.store(stream, null);
				loginModule.register(mail, password);
			}
			
			this.context = new Context();
			this.state = new State();
			this.synchronizer = new Synchronizer(dispatcher, context, state);
			this.view = new MainView(modules, context);
			
			synchronizer.synchronizeContext();
			synchronizer.synchronizeState();
		
	}
	
	public MainView getView(){
		return view;
	}

	

}
