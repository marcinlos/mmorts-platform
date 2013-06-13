package com.app.ioapp.init;

import com.app.ioapp.communication.Dispatcher;

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
