package pl.edu.agh.ki.mmorts.client.frontend.view;

/**
 * Base class for exceptions connected with modules 
 *
 */
public class ModuleException extends RuntimeException{

	public ModuleException() {
		// empty
	}

	public ModuleException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ModuleException(String detailMessage) {
		super(detailMessage);
	}

	public ModuleException(Throwable throwable) {
		super(throwable);
	}



}
