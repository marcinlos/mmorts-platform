package pl.edu.agh.ki.mmorts.client.frontend.modules;

public class ModulesBrokerException extends RuntimeException {

	public ModulesBrokerException() {
		// empty
	}

	public ModulesBrokerException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ModulesBrokerException(String detailMessage) {
		super(detailMessage);
	}

	public ModulesBrokerException(Throwable throwable) {
		super(throwable);
	}
	
	

}
