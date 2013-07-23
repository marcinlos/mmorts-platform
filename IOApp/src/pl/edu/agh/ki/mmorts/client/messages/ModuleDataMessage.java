package pl.edu.agh.ki.mmorts.client.messages;

import pl.edu.agh.ki.mmorts.client.GUIGenericMessage;

public class ModuleDataMessage extends GUIGenericMessage {
	public <T> ModuleDataMessage(String sender, T content){
		super(sender,content);
	} 


}
