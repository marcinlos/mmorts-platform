package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public class ViewAlreadyRegisteredException extends RuntimeException{

	public ViewAlreadyRegisteredException() {
		super();
	}

	public ViewAlreadyRegisteredException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ViewAlreadyRegisteredException(String detailMessage) {
		super(detailMessage);
	}

	public ViewAlreadyRegisteredException(Throwable throwable) {
		super(throwable);
	}

}
