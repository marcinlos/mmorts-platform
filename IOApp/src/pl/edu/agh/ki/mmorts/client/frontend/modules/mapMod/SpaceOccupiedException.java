package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

public class SpaceOccupiedException extends RuntimeException {
	
	public SpaceOccupiedException() {
		// empty
	}
	
	public SpaceOccupiedException(String message) {
		super(message);
	}
	
	public SpaceOccupiedException(Throwable cause) {
		super(cause);
	}
	
	public SpaceOccupiedException(String message, Throwable cause) {
		super(message, cause);
	}

}
