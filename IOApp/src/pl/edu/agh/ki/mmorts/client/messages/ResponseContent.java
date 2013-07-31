package pl.edu.agh.ki.mmorts.client.messages;

/**
 * A class sent from module to presenter as a response to {@code ChangeStateContent} or {@code GetStateContent}.
 *
 */
public class ResponseContent implements ModuleDataMessageContent {
	/**
	 * true if it's response to {@code ChangeStateContent}, otherwise false 
	 */
	private boolean responseToChange;
	/**
	 * It is used when it's response to {@code ChangeStateContent}. If module agrees to change - it's true.
	 * When it's response to {@code GetStateContent} it's always true.
	 */
	private boolean positive;
	/**
	 * An object that presenter needed
	 */
	private Object state;
	
	public ResponseContent(boolean responseToChange, boolean positive,
			Object state) {
		this.responseToChange = responseToChange;
		this.positive = positive;
		this.state = state;
	}

	public boolean isResponseToChange() {
		return responseToChange;
	}

	public boolean isPositive() {
		return positive;
	}

	public Object getState() {
		return state;
	}

	public void setResponseToChange(boolean responseToChange) {
		this.responseToChange = responseToChange;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}

	public void setState(Object state) {
		this.state = state;
	}
	


}
