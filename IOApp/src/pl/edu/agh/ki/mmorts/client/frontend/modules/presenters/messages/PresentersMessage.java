package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages;

import pl.edu.agh.ki.mmorts.client.GUIGenericMessage;


public class PresentersMessage extends GUIGenericMessage {
	public <T> PresentersMessage(String sender, T object) {
		super(sender,object);
	}
}
