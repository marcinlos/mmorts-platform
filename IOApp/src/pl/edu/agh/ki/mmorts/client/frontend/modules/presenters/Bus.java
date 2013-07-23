package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;

public interface Bus {
	
	/**
	 * sends message to every {@link BusListener} that registered itself within
	 * @param m
	 */
	public void sendMessage(PresentersMessage m);
	
	/**
	 * registers listener for message receiving
	 * @param bl
	 */
	public void register(BusListener bl);

}
