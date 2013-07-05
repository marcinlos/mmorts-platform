package com.app.ioapp.init;

import java.util.Map;
import java.util.Properties;

import android.util.Log;

import com.app.ioapp.communication.Gateway;
import com.app.ioapp.modules.Module;

/**
 * A class used to synchronize local state with the state on the server. 
 * It enables also synchronizing context which will not be so often.
 *
 */
public class Synchronizer {
	
	public static final String ID = "Synchronizer";
	
	/**
	 * Dispatcher which enables communication with server
	 */
	private Gateway dispatcher;
	
	private Map<String, Module> modules;
	
	/**
	 * @param dispatcher
	 * @param context
	 * @param state
	 */
	public Synchronizer(Gateway dispatcher, Map<String, Module> modules) {
		this.dispatcher = dispatcher;
		this.modules = modules;
	}

	/**
	 * Synchronizes state of each module with server.
	 */
	public void synchronizeState() {
		Log.e(ID, "Synchronizing state started");
		Properties properties = null;
		// get state from server as properties
		for (String moduleName : modules.keySet()) {
			Properties moduleProperties = new Properties();
			for (Object propertyKey : properties.keySet()) {
				if (((String)propertyKey).startsWith(moduleName)) {
					moduleProperties.put(propertyKey, properties.get(propertyKey));
				}
			}
			//(modules.get(moduleName)).setSynchronizedState(moduleProperties);
		}
	}
	
}
