package com.app.ioapp.init;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.app.ioapp.communication.Dispatcher;
import com.app.ioapp.config.Config;
import com.app.ioapp.config.ConfigException;
import com.app.ioapp.config.ConfigReader;
import com.app.ioapp.data.Context;
import com.app.ioapp.data.State;
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
	/**
	 * Player's password
	 */
	private String password;
    
    /**
     * Stores properties read from  configuration file
     */
    private Config config;
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
	/**
	 * View object
	 */
	private MainView view;
	/**
	 * Stores information
	 */
	private Context context;
	/**
	 * Stores information which changes frequently
	 */
	private State state;
	
	/**
	 * Stream to read configuration from file
	 */
	private FileInputStream configInput;
	/**
	 * Stream to write player information if he is not registered yet
	 */
	private FileOutputStream infoOutput;
	
	
	/**
	 * @param mail
	 * @param password
	 * @param alreadyRegistered true if player has been registered (a file with mail and password exists and is correct) 
	 * @param configInput to read configuration from file
	 * @param infoOutput to write players info to file if he has not been registered yet. If he has, it is {@code null}
	 * @throws ConfigException
	 * @throws IOException
	 */
	public Initializer
	(String mail, String password, boolean alreadyRegistered, FileInputStream configInput, FileOutputStream infoOutput) 
			throws ConfigException, IOException {
		this.mail = mail;
		this.password = password;
		this.alreadyRegistered = alreadyRegistered;
		this.configInput = configInput;
		this.infoOutput = infoOutput;
		initialize();
	}
	
	/**
	 * Returns object with configuration
	 * @return {@code Config} object
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
			reader = new ConfigReader(configInput);
			reader.configure();
			config = reader.getConfig();
	
			//this.dispatcher = new ThreadedDispatcher();
			this.loginModule = new LoginModule(dispatcher, mail, password);
			if (alreadyRegistered) {
				loginModule.logIn();
			}
			else {
				Properties ps = new Properties();
				ps.setProperty("mail", mail);
				ps.setProperty("password", password);
				ps.store(infoOutput, null);
				loginModule.register(mail, password);
			}
			
			this.context = new Context();
			this.state = new State();
			this.synchronizer = new Synchronizer(dispatcher, context, state);
			//this.view = new MainView(dispatcher.getModules(), context);
			
			synchronizer.synchronizeContext();
			synchronizer.synchronizeState();
		
	}
	
	public MainView getView(){
		return view;
	}

	

}
