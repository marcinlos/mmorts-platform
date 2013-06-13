package com.app.ioapp.init;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.communication.Dispatcher;
import com.app.ioapp.communication.ThreadedDispatcher;
import com.app.ioapp.data.Context;
import com.app.ioapp.data.State;
import com.app.ioapp.modules.Module;
import com.app.ioapp.view.MainView;

/**
 * A class responsible for the initialization of the environment.
 * <p> Performs the following operations:
 * <ul>
 * <li>Reads configuration file
 * <li>Creates & initializes modules, based on the configuration
 * 
 */
public class Initializer {
	
    /** Configuration file location */
    private String configFile;
	
	private Synchronizer synchronizer;
	private LoginModule loginModule;
	
    /**
     * Dispatcher object created using the class specified in the configuration
     */
	private Dispatcher dispatcher;
	private MainView view;
	private Context context;
	private State state;
	private Map<String,Module> modules;
	
	public Initializer(String configFile) {
		this.configFile = configFile;
	}
	

/**
 * Initializes all classes
 */
	public void initialize() {

		//this.dispatcher = new ThreadedDispatcher();
		this.loginModule = new LoginModule(dispatcher);
		loginModule.logIn();
		this.context = new Context();
		this.state = new State();
		this.synchronizer = new Synchronizer(dispatcher, context, state);
		this.view = new MainView(modules, context);
		
		synchronizer.synchronizeContext();
		synchronizer.synchronizeState();
		
	}
	

}
