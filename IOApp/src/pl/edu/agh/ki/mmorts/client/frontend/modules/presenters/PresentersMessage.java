package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public class PresentersMessage extends GUIGenericMessage {
	public <T> PresentersMessage(String sender, T object) {
		super(sender,object);
	}
}
