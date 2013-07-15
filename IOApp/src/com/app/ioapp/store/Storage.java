package com.app.ioapp.store;

import java.util.List;

import com.app.ioapp.modules.ConfiguredModule;
import com.app.ioapp.view.ModulesBroker;

public class Storage {

	private static Storage instance;
	
	private List<ConfiguredModule> loadedModules;
	
	private ModulesBroker broker;
	
	
	
	public ModulesBroker getBroker() {
		return broker;
	}



	public void setBroker(ModulesBroker broker) {
		this.broker = broker;
	}



	public static Storage getStorage(){
		if(instance==null){
			instance = new Storage();
		}
		return instance;
	}



	public List<ConfiguredModule> getLoadedModules() {
		return loadedModules;
	}



	public void setLoadedModules(List<ConfiguredModule> loadedModules) {
		this.loadedModules = loadedModules;
	}
	
}
