package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

/**
 * Thrown by LogInModule when registering does not succeed
 *
 */
public class RegisterException extends RuntimeException{
	
	public RegisterException() {
		//empty
	}
	
	public RegisterException(String message) {
		super(message);
	}
	
	public RegisterException(Throwable cause) {
		super(cause);
	}
	
	public RegisterException(String message, Throwable cause) {
		super(message, cause);
	}

}
