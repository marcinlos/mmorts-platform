package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

/**
 * Thrown by LoginModule when logging out does not succeed
 *
 */
public class LogOutException extends RuntimeException{
	
	public LogOutException() {
		//empty
	}
	
	public LogOutException(String message) {
		super(message);
	}
	
	public LogOutException(Throwable cause) {
		super(cause);
	}
	
	public LogOutException(String message, Throwable cause) {
		super(message, cause);
	}

}
