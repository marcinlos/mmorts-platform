package com.app.ioapp.init;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.data.Context;
import com.app.ioapp.data.State;
import com.app.ioapp.dispatcher.Dispatcher;
import com.app.ioapp.interfaces.IModule;
import com.app.ioapp.view.MainView;

/**
 * A class responsible for initializing the environment.
 */
public class Initializer {
	
	private File configureFile;
	
	private Synchronizer synchronizer;
	private LoginModule loginModule;
	private Dispatcher dispatcher;
	private MainView view;
	private Context context;
	private State state;
	private Map<String,IModule> modules;
	
	/**
	 * @param configure file 
	 */
	public Initializer(File configureFile) {
		this.configureFile = configureFile;
	}

/**
 * Initializes all classes
 */
	public void initialize() {

		initializeModules();
		this.dispatcher = new Dispatcher(modules);
		this.loginModule = new LoginModule(dispatcher);
		loginModule.logIn();
		this.context = new Context();
		this.state = new State();
		this.synchronizer = new Synchronizer(dispatcher, context, state);
		this.view = new MainView(modules, context);
		
		synchronizer.synchronizeContext();
		synchronizer.synchronizeState();
		
	}
	
	/**
	 * initializes the modules according to configure file
	 */
	private void initializeModules(){
		modules = new HashMap<String,IModule>();
		// inicjalizacja na podstawie pliku konf.
	}

}
