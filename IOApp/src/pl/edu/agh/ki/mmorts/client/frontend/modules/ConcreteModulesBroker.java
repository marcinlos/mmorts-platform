package pl.edu.agh.ki.mmorts.client.frontend.modules;

import java.util.HashMap;
import java.util.Map;

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
	private Map<String, GUICommModule> modules; 


	@Override
	public void registerPresenter(ModuleDataChangedListener presenter, String moduleName) {
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
	public void unregisterPresenter(ModuleDataChangedListener presenter) {
		if (!modulePresenters.containsKey(presenter)) {
			Log.e(ID, "Trying to unregister not registered presenter");
			throw new ModulesBrokerException("Presenter is not registered.");
		}
	}

	@Override
	public void tellModule(ModuleDataMessage message, String moduleName) {
		modules.get(moduleName).dataChanged(message);
		
	}

	@Override
	public void tellPresenters(ModuleDataMessage message, String moduleName) {
		for (ModuleDataChangedListener presenter : modulePresenters.keySet()) {
			if (modulePresenters.get(presenter).equals(moduleName)) {
				presenter.dataChanged(message);
			}
		}
		
	}

}


