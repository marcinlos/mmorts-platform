package com.app.ioapp.init;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.util.Log;

import com.app.ioapp.communication.Dispatcher;
import com.app.ioapp.config.Config;
import com.app.ioapp.config.ConfigException;
import com.app.ioapp.config.ConfigReader;
import com.app.ioapp.data.PlayersContext;
import com.app.ioapp.data.State;

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
	 * Stores information
	 */
	private PlayersContext context;
	/**
	 * Stores information which changes frequently
	 */
	private State state;
	
	/**
	 * Stream to read configuration from file
	 */
	private InputStream configInput;
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
	 */
	public Initializer
	(String mail, String password, boolean alreadyRegistered, InputStream configInput, FileOutputStream infoOutput)  {
		this.mail = mail;
		this.password = password;
		this.alreadyRegistered = alreadyRegistered;
		this.configInput = configInput;
		this.infoOutput = infoOutput;
		
		//this.dispatcher = new ThreadedDispatcher(config);   //must be initialized before logIn()
	}
	
	/**
	 * Returns object with configuration
	 * @return {@code Config} object
	 */
	public Config getConfig() {
		return config;
	}

	
	/**
	 * Called before initializing the rest of environment
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
 * @throws ConfigException
 * @throws IOException 
 */
	public void initialize() throws ConfigException, IOException{
			Log.e(ID,"Initializing environment started");
			reader = new ConfigReader(configInput);
			reader.configure();
			config = reader.getConfig();
			
			this.context = new PlayersContext();
			this.state = new State();
			this.synchronizer = new Synchronizer(dispatcher, context, state);
			
			synchronizer.synchronizeContext();
			synchronizer.synchronizeState();
		
	}
	


	

}
