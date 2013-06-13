package com.app.ioapp.init;

import com.app.ioapp.communication.Gateway;

/**
 * Class responsible for registering, logging in and logging out
 *
 */
public class LoginModule {

	/**
	 * Dispatcher which enables communication with server
	 */
	private Gateway dispatcher;
	/**
	 * Mail identifies user
	 */
	private String userMail;
	
	/**
	 * Password is checked on server. It is actually  entered by user
	 */
	private String password;
	
	public LoginModule(Gateway dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void register(String mail) throws RegisterException{
	}
	
	public void logIn() {
	}
	
	public void logOut() {
	}
	

}
