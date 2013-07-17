package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public interface Bus {
	
	/**
	 * sends message to every {@link BusListener} that registered itself within
	 * @param m
	 */
	public void sendMessage(GUIGenericMessage m);
	
	/**
	 * registers listener for message receiving
	 * @param bl
	 */
	public void register(BusListener bl);

}
