package pl.edu.agh.ki.mmorts.client.frontend.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.backend.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.client.backend.modules.Module;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.view.ModuleNotExists;
import pl.edu.agh.ki.mmorts.client.frontend.views.AbstractModuleView;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;


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
	private Map<AbstractModulePresenter, String> modulePresenters = new HashMap<AbstractModulePresenter, String>();
	/**
	 * Map of modules to send messages
	 */
	@Inject
	private Map<String, GUICommModule> modules; 


	@Override
	public void registerPresenter(ModulePresenter presenter, String moduleName) {
		if (modulePresenters.containsKey(presenter)) {
			Log.e(ID, "Trying to register presenter again");
			throw new ModulesBrokerException("Presenter is already registered.");
		}
		if (!modules.containsKey(moduleName)) {
			Log.e(ID, "Trying to register presenter to a module that doesn't exist");
			throw new ModulesBrokerException("Module doesn't exist");
		}
		modulePresenters.put((AbstractModulePresenter) presenter, moduleName);
		
	}

	@Override
	public void unregisterPresenter(ModulePresenter presenter) {
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
		for (AbstractModulePresenter presenter : modulePresenters.keySet()) {
			if (modulePresenters.get(presenter).equals(moduleName)) {
				presenter.dataChanged(message);
			}
		}
		
	}

}


