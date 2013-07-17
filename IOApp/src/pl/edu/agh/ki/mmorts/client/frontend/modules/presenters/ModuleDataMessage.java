package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public class ModuleDataMessage {
	private String sender;
	private Object content;	
	
	public <T> ModuleDataMessage(String sender, T content){
		this.sender = sender;
		this.content = content;
	}
	
	public <T> T getMessage(Class<T> clazz) throws ClassCastException{
		return clazz.cast(content);

	}
	
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
