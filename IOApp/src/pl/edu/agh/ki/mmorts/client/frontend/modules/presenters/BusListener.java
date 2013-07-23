package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;

public interface BusListener {

	/**
	 * parses and reacts to message from Bus
	 * @param m
	 */
	public void gotMessage(PresentersMessage m);
}
