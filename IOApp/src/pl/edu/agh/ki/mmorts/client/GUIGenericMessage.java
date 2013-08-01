package pl.edu.agh.ki.mmorts.client;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;


public class GUIGenericMessage {

	protected String sender;
	protected Object content;
	
	public <T> GUIGenericMessage(String sender, T content){
		this.sender = sender;
		this.content = content;
	}

	/**
	 * 
	 * @param clazz expected return type
	 * @return object of type clazz or null it cast is impossible
	 * @throws ClassCastException use {@link PresentersMessage#carries(Class)} before using!
	 */
	public <T> T getMessage(Class<T> clazz) throws ClassCastException {
		return clazz.cast(content);
	
	}
	
	/**
	 * Checks whether message is of type clazz
	 * @param clazz type we check with
	 * @return
	 */
	public <T> boolean carries(Class<T> clazz) {
		try{
			clazz.cast(content);
			return true;
		}
		catch(ClassCastException e){
			return false;
		}
	}

	public String getSender() {
		return sender;	
	}

}