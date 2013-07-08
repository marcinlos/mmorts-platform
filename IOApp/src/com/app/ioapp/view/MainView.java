package com.app.ioapp.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.app.ioapp.customDroidViews.AbstractModuleView;
import com.app.ioapp.modules.ConfiguredModule;
import com.app.ioapp.modules.Module;
import com.google.inject.Inject;


/**
 * Implements facade between android module views and modules
 *
 *TODO About communication methods - this here needs some magic. I'm not sure about the flow of info during config
 *or anything like that, but it would seem that after config we're going to be using some fancy stuff
 *with annotations, to make available GUICommModule implementations (like InfrastructureCommModule) created from
 *ConfiguredModule map we hold and use them instead of the insides of ConfiguredModule. In doubt ask Andrew.
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
	@Override
	//public void register(Class<? extends AbstractModuleView> moduleView,
	//		String moduleName) {
	public void register(AbstractModuleView moduleView, String moduleName){
		if (!modules.containsKey(moduleName)) {
			Log.e(ID, "Registering view to a module that doesn't exist");
			throw new ModuleNotExists();
		}
		if (!registeredViews.containsKey(moduleName)) {
			registeredViews.put(moduleName, new ArrayList<AbstractModuleView>());
		}
		registeredViews.get(moduleName).add(moduleView);		
	}
	
	public void refreshViews(String moduleName){
		for(AbstractModuleView a : registeredViews.get(moduleName)){
			a.postInvalidate();
		}
	}
	public void addModule(String n, ConfiguredModule m){
		modules.put(n, m);
	}
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 * @return
	 */
	public boolean stateChanged(String moduleName){
		ConfiguredModule m = modules.get(moduleName);
		if(m != null) return m.stateChanged();
		return false;
	}
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 */
	public void stateReceived(String moduleName){
		ConfiguredModule m = modules.get(moduleName);
		if(m != null) m.stateReceived();
	}
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 * @param returnType
	 * @return
	 */
	public <T> T getData(String moduleName, Class<T> returnType){
		ConfiguredModule m = modules.get(moduleName);
		if(m != null) return m.getData();
		return null;
	}
	
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 * @param data
	 */
	public <T> void setData(String moduleName, T data){
		ConfiguredModule m = modules.get(moduleName);
		if(m != null) m.setData(data);
	}

}


