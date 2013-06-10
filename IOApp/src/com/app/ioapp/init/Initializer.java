package com.app.ioapp.init;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.data.Context;
import com.app.ioapp.data.State;
import com.app.ioapp.dispatcher.Dispatcher;
import com.app.ioapp.interfaces.IModule;
import com.app.ioapp.view.MainView;

public class Initializer {
	
	private File configureFile;
	
	private Synchronizer synchronizer;
	private LoginModule loginModule;
	private Dispatcher dispatcher;
	private MainView view;
	private Context context;
	private State state;
	private Map<String,IModule> modules;
	
	public Initializer(File configureFile) {
		this.configureFile = configureFile;
	}


	public void initialize() {
		initializeModules();
		this.context = new Context();
		this.state = new State();
		this.dispatcher = new Dispatcher(modules);
		this.synchronizer = new Synchronizer(dispatcher, context, state);
		this.view = new MainView(modules, context);
		this.loginModule = new LoginModule();
		synchronizer.synchronizeContext();
		synchronizer.synchronizeState();
		loginModule.logIn();
	}
	
	private void initializeModules(){
		modules = new HashMap<String,IModule>();
		// inicjalizacja na podstawie pliku konf.
	}

}
