package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

public interface ModuleDataChangedListener {
	/**
	 * method used as a listener in module - receives concrete object with new data.
	 */
	public void dataChanged(ModuleDataMessage data); //TODO sialalala

}
