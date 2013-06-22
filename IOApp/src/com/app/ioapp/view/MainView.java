package com.app.ioapp.view;

import java.util.Map;

import com.app.ioapp.modules.Module;


public class MainView {
	/*
	private Map<String,CommunicatingModule> modules;
	private UIListener listener;
	private PlayersContext context;
	
	public MainView(Map<String,CommunicatingModule> modules, PlayersContext context){
		this.modules = modules;
		this.context = context;
	}
	
	public void setListener(UIListener l){
		listener = l;
	}
	
	public void updateModules(Map<String,CommunicatingModule> modules){       // bo te same moduly sa tez w innych miejscach
		this.modules = modules;
	}
	
	
	public void StuffHappened(Object wtf){
		listener.stuffHappened(wtf);
	}
	
	//public Object getModuleData(String moduleName){
		
	//}
	
	
	
	/**
	 * invoked by MenuManager when a button has been clicked.
	 * @param menuNameClicked module it needs to be directed to
	 */
	Map<String,Module> modules;
	
	private Module findModule(String name){
		for(String n : modules.keySet()){
			if(n.equals(name))
				 return modules.get(name);
		}
		return null;
	}
	
	public void addModule(String n, Module m){
		modules.put(n, m);
	}
	
	public boolean stateChanged(String moduleName){
		Module m = findModule(moduleName);
		if(m != null) return m.stateChanged();
		return false;
	}
	public void stateReceived(String moduleName){
		Module m = findModule(moduleName);
		if(m != null) m.stateReceived();
	}
	public <T> T getData(String moduleName, Class<T> returnType){
		Module m = findModule(moduleName);
		if(m != null) return m.getData();
		return null;
	}
	
	public <T> void setData(String moduleName, T data){
		Module m = findModule(moduleName);
		if(m != null) m.setData(data);
	}
}
	
	/*
	public void handleMenuAction(String moduleName){
		//TODO
	}
*/


