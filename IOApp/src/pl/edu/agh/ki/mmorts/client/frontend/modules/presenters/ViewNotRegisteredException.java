package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

public class ViewNotRegisteredException extends RuntimeException{

	public ViewNotRegisteredException() {
		super();
	}

	public ViewNotRegisteredException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ViewNotRegisteredException(String detailMessage) {
		super(detailMessage);
	}

	public ViewNotRegisteredException(Throwable throwable) {
		super(throwable);
	}
	

}
