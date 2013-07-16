package pl.edu.agh.ki.mmorts.client.frontend.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.ki.mmorts.client.backend.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.views.AbstractModuleView;
import android.util.Log;


/**
 * Implements facade between android module views and modules
 *
 *TODO About communication methods - this here needs some magic. I'm not sure about the flow of info during config
 *or anything like that, but it would seem that after config we're going to be using some fancy stuff
 *with annotations, to make available GUICommModule implementations (like InfrastructureCommModule) created from
 *ConfiguredModule map we hold and use them instead of the insides of ConfiguredModule. In doubt ask Andrew.
 */
public class ModulesBroker implements View{
	
	private static final String ID = "ModulesBroker";
	
	/**
	 * Map of modules
	 */
	//temporarily not injected
	private Map<String, GUICommModule> modules;
	
	private List<ConfiguredModule> configuredModules;
	
	
	
	public Map<String, GUICommModule> getModules() {
		return modules;
	}

	public List<ConfiguredModule> getConfiguredModules() {
		return configuredModules;
	}

	public ModulesBroker(Map<String, GUICommModule> modules,
			List<ConfiguredModule> configuredModules) {
		super();
		this.modules = modules;
		this.configuredModules = configuredModules;
	}

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
	/*
	public void addModule(String n, ConfiguredModule m){
		modules.put(n, m);
	}
	*/
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 * @return
	 */
	public boolean stateChanged(String moduleName){
		GUICommModule m = modules.get(moduleName);
		if(m != null) return m.isStateChanged();
		return false;
	}
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 */
	public void stateReceived(String moduleName){
		GUICommModule m = modules.get(moduleName);
		if(m != null) m.stateReceived();
	}
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 * @param returnType
	 * @return
	 */
	public <T> T getData(String moduleName, Class<T> returnType){
		GUICommModule m = modules.get(moduleName);
		if(m != null) return m.getData(returnType);
		return null;
	}
	
	/**
	 * @see com.app.ioapp.module.GUICommModule
	 * @param moduleName
	 * @param data
	 */
	public <T> void setData(String moduleName, T data, Class<T> clazz){
		GUICommModule m = modules.get(moduleName);
		if(m != null) m.setData(data, clazz);
	}

}


