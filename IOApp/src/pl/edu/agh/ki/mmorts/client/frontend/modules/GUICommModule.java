package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.backend.modules.Module;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

/**
 * Represents a module that is able to communicate with {@code ModulesBroker}
 *
 */
public interface GUICommModule extends Module {
	
	/**
	 * Called by {@code ModulesBroker} when {@code ModulesPresenter} sends a message
	 * @param data
	 */
	public void dataChanged(ModuleDataMessage data);

}
