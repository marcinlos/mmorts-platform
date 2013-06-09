package com.app.ioapp.view;

import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.interfaces.IModule;
import com.app.ioapp.interfaces.UIListener;

public class MainView {
	
	Map<String,IModule> modules = new HashMap<String,IModule>();
	UIListener listener;
	
	public MainView(){
	}
	
	public void setListener(UIListener l){
		listener = l;
	}
	
	public void addModule(String name, IModule module){
		modules.put(name, module);
	}
	
	public void removeModule(String name){
		modules.remove(name);
	}
	
	public void StuffHappened(Object wtf){
		listener.stuffHappened(wtf);
	}

}

