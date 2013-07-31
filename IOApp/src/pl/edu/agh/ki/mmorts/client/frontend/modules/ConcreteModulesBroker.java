package pl.edu.agh.ki.mmorts.client.frontend.modules;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataChangedListener;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;

import com.google.inject.Inject;


/**
 * Implements facade between android module views and modules
 *
 *TODO About communication methods - this here needs some magic. I'm not sure about the flow of info during config
 *or anything like that, but it would seem that after config we're going to be using some fancy stuff
 *with annotations, to make available GUICommModule implementations (like InfrastructureCommModule) created from
 *ConfiguredModule map we hold and use them instead of the insides of ConfiguredModule. In doubt ask Andrew.
 */

// TODO: refactor interface - bad param names!
public class ConcreteModulesBroker implements ModulesBroker{
	
	private static final String ID = "ModulesBroker";
	
	/**
	 * Map of presenters and name of modules
	 */
	private Map<ModuleDataChangedListener, String> modulePresenters = new HashMap<ModuleDataChangedListener, String>();
	/**
	 * Map of modules to send messages
	 */
	@Inject(optional = true)
	private Map<String, GUICommModule> modules = new HashMap<String, GUICommModule>(); 


	@OnInit
	public void onInit(){
		Log.d(ID, "Broker initialized");
		Log.d(ID, String.format("Registered GUICommModules: %s", modules));
	}
	
	@Override
	public void registerPresenter(ModuleDataChangedListener presenter, String moduleName) {
		Log.d(ID, "Registering presenter " + moduleName);
		if (modulePresenters.containsKey(presenter)) {
			Log.e(ID, "Trying to register presenter again");
			throw new ModulesBrokerException("Presenter is already registered.");
		}
		if (!modules.containsKey(moduleName)) {
			Log.e(ID, "Trying to register presenter to a module that doesn't exist");
			throw new ModulesBrokerException("Module doesn't exist");
		}
		modulePresenters.put(presenter, moduleName);
		
	}

	@Override
	public void unregisterPresenter(String presenterName) {
		Log.d(ID, "Unregistering presenter " + presenterName);
		modulePresenters.remove(presenterName);
	}

	@Override
	public void tellModule(ModuleDataMessage message, String moduleName) {
		Log.d(ID, "Sending to module " + moduleName);
		modules.get(moduleName).dataChanged(message);
		
	}

	@Override
	public void tellPresenters(ModuleDataMessage message, String moduleName) {
		Log.d(ID, "Invoking presenters " + moduleName);
		for (ModuleDataChangedListener presenter : modulePresenters.keySet()) {
			if (modulePresenters.get(presenter).equals(moduleName)) {
				presenter.dataChanged(message);
			}
		}
		
	}

	@Override
	public void registerModule(GUICommModule module, String moduleName) {
		Log.d(ID, "Registering module "+ moduleName);
		if(modules.containsKey(moduleName)){
			throw new ModulesBrokerException("GUICommModules conflict!");
		}
		modules.put(moduleName, module);
		
	}

	@Override
	public void unregisterModule(String moduleName) {
		Log.d(ID, "Unregistering module "+ moduleName);
		modules.remove(moduleName);
	}

}


