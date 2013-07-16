package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public class PresentersMessage {
	private String sender;
	private Object content;	
	
	
	
	public <T> PresentersMessage(String sender, T object) {
		this.sender = sender;
		this.content = object;
	}
	
	/**
	 * 
	 * @param clazz expected return type
	 * @return object of type clazz or null it cast is impossible
	 */
	public <T> T getMessage(Class<T> clazz){
		if(carries(clazz)){
			return clazz.cast(content);
		}
		else{
			return null;
		}
	}
	
	/**
	 * Checks whether message is of type clazz
	 * @param clazz type we check with
	 * @return
	 */
	public <T> boolean carries(Class<T> clazz){
		try{
			clazz.cast(content);
			return true;
		}
		catch(ClassCastException e){
			return false;
		}
	}
	
	public String getSender(){
		return sender;	
	}
}
