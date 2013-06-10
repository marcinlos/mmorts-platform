package com.app.ioapp.dispatcher;

import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.interfaces.IModule;

public class Dispatcher {

	private Map<String,IModule> modules = new HashMap<String,IModule>();
	
	public Dispatcher(Map<String,IModule> modules) {
		this.modules = modules;
	}
	
}
