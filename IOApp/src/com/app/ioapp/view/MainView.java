package com.app.ioapp.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.app.ioapp.customDroidViews.AbstractModuleView;
import com.app.ioapp.modules.ConfiguredModule;
import com.app.ioapp.modules.IModule;
import com.app.ioapp.modules.Module;
import com.google.inject.Inject;


/**
 * Implements facade between android module views and modules
 *
 */
public class MainView implements View{
	
	private static final String ID = "MainView";
	
	/**
	 * Map of modules
	 */
	@Inject
	private Map<String, ConfiguredModule> modules;
	
	/**
	 * Mapping of modules and moduleViews which are interested in changes in these modules
	 */
	//private Map<String, List<Class<? extends AbstractModuleView>>> registeredViews = 
	//		new HashMap<String, List<Class<? extends AbstractModuleView>>>();
	private Map<String, List<AbstractModuleView>> registeredViews = new HashMap<String, List<AbstractModuleView>>();
	

	/**
	 * {@inheritDoc}
	 */
	//TODO Kasia, zmieñ to tam ¿eby
	@Override
	//public void register(Class<? extends AbstractModuleView> moduleView,
	//		String moduleName) {
	public void register(AbstractModuleView moduleView, String moduleName){
		if (!modules.containsKey(moduleName)) {
			Log.e(ID, "Registering view to a module that doesn't exist");
			throw new ModuleNotExists();
		}
		if (!registeredViews.containsKey(moduleName)) {
			registeredViews.put(moduleName, new ArrayList<Class<? extends AbstractModuleView>>());
		}
		registeredViews.get(moduleName).add(moduleView);		
	}
	
	public void refreshViews(String moduleName){
		for(AbstractModuleView a : registeredViews.get(moduleName)){
			a.postInvalidate();
		}
	}
	public void addModule(String n, Module m){
		modules.put(n, m);
	}
	
	public boolean stateChanged(String moduleName){
		IModule m = findModule(moduleName);
		if(m != null) return m.stateChanged();
		return false;
	}
	public void stateReceived(String moduleName){
		IModule m = findModule(moduleName);
		if(m != null) m.stateReceived();
	}
	public <T> T getData(String moduleName, Class<T> returnType){
		IModule m = findModule(moduleName);
		if(m != null) return m.getData();
		return null;
	}
	
	public <T> void setData(String moduleName, T data){
		IModule m = findModule(moduleName);
		if(m != null) m.setData(data);
	}



	
}
	/*
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
/*	Map<String,Module> modules;
	
	private Module findModule(String name){
		for(String n : modules.keySet()){
			if(n.equals(name))
				 return modules.get(name);
		}
		return null;
	}
	
	
}
	
	/*
	public void handleMenuAction(String moduleName){
		//TODO
	}
*/


