package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

public interface ModulesBroker {
	
	/**
	 * Registers presenters in broker
	 * @param presenter
	 * @param moduleName
	 */
	public void registerPresenter(ModulePresenter presenter, String moduleName);
	/**
	 * Unregisters presenter
	 * @param presenter
	 */
	public void unregisterPresenter(ModulePresenter presenter);
	/**
	 * Sends message from presenter to module. Presenter needs to know the name of module
	 * @param message
	 * @param moduleName
	 */
	public void tellModule(ModuleDataMessage message, String moduleName);
	/**
	 * Sends message to all presenters that are registered to the name of module
	 * @param message
	 * @param moduleName
	 */
	public void tellPresenters(ModuleDataMessage message, String moduleName);

}
