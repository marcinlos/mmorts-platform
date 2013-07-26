package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

/**
 * Interface enabling module communicating with {@code ModulesBroker}
 *
 */
public interface GUICommModule {
	
	/**
	 * Called by {@code ModulesBroker} when {@code ModulesPresenter} sends a message
	 * @param data
	 */
	public void dataChanged(ModuleDataMessage data);

}
