package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public class ModuleDataMessage extends GUIGenericMessage {
	public <T> ModuleDataMessage(String sender, T content){
		super(sender,content);
	}


}
