package com.app.ioapp.config;

public class ReadingPropertiesException extends RuntimeException{
	
	public ReadingPropertiesException() {
		//empty
	}
	
	public ReadingPropertiesException(String message) {
		super(message);
	}
	
	public ReadingPropertiesException(Throwable cause) {
		super(cause);
	}
	
	public ReadingPropertiesException(String message, Throwable cause) {
		super(message, cause);
	}

}
