package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

/**
 * Thrown by LoginModule when logging in does not succeed
 *
 */
public class LogInException extends RuntimeException {
	
	public LogInException() {
		//empty
	}
	
	public LogInException(String message) {
		super(message);
	}
	
	public LogInException(Throwable cause) {
		super(cause);
	}
	
	public LogInException(String message, Throwable cause) {
		super(message, cause);
	}


}
