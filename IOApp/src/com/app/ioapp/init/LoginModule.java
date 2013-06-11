package com.app.ioapp.init;

import com.app.ioapp.dispatcher.Dispatcher;
import com.app.ioapp.exceptions.RegisterException;

/**
 * Class responsible for registering, logging in and logging out
 *
 */
public class LoginModule {

	private Dispatcher dispatcher;
	private String userMail;         // identyfikator
	
	public LoginModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void register(String mail) throws RegisterException{
	}
	
	public void logIn() {
	}
	
	public void logOut() {
	}
	

}
