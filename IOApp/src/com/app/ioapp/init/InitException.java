package com.app.ioapp.init;

public class InitException extends RuntimeException{

	public InitException() {
		// empty
	}

	public InitException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InitException(String detailMessage) {
		super(detailMessage);
	}

	public InitException(Throwable throwable) {
		super(throwable);
	}
	
	

}
