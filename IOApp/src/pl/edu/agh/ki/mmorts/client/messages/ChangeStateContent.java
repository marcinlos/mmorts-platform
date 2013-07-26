package pl.edu.agh.ki.mmorts.client.messages;

/**
 * A content of message sent from presenter to module when presenter wants to change state.
 *
 */
public class ChangeStateContent implements ModuleDataMessageContent{

	private Object state;
	
	public  ChangeStateContent(Object state) {
		this.state = state;
	}

	public Object getState() {
		return state;
	}

	public void setState(Object state) {
		this.state = state;
	}
	
	
}
