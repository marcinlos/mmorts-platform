package pl.edu.agh.ki.mmorts.client.messages;

import pl.edu.agh.ki.mmorts.client.GUIGenericMessage;

/**
 * A message sent between presenter and module
 *
 */
public class ModuleDataMessage extends GUIGenericMessage {
	public <T> ModuleDataMessage(String sender, T content){
		super(sender,content);
	} 


}
