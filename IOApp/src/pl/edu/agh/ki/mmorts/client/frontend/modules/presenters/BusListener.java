package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public interface BusListener {

	/**
	 * parses and reacts to message from Bus
	 * @param m
	 */
	public void gotMessage(GUIGenericMessage m);
}
