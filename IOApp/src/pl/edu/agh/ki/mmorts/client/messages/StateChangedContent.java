package pl.edu.agh.ki.mmorts.client.messages;

public class StateChangedContent implements ModuleDataMessageContent {
	
	private Object state;

	public StateChangedContent(Object state) {
		this.state = state;
	}

	public Object getState() {
		return state;
	}

	public void setState(Object state) {
		this.state = state;
	}
	
	
}
