package com.app.ioapp.config;

/**
 * Thrown when occurs an exception during configuration.
 *
 */
public class ConfigException extends RuntimeException{
	
	public ConfigException() {
		//empty
	}
	
	   public ConfigException(String message) {
	        super(message);
	    }

	    public ConfigException(Throwable cause) {
	        super(cause);
	    }

	    public ConfigException(String message, Throwable cause) {
	        super(message, cause);
	    }


}