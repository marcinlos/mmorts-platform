package client.view;

import java.util.HashMap;
import java.util.Map;

import client.interfaces.IModule;
import client.interfaces.UIListener;

public class MainView {
	
	Map<String,IModule> modules = new HashMap<String,IModule>();
	UIListener listener;
	
	public MainView(UIListener l){
		listener = l;
	}
	
	public void addModule(String name, IModule module){
		modules.put(name, module);
	}
	
	public void removeModule(String name){
		modules.remove(name);
	}

}
