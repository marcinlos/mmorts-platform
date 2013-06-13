package com.app.ioapp.init;

import com.app.ioapp.communication.Dispatcher;

/**
 * Class responsible for registering, logging in and logging out
 *
 */
public class LoginModule {

	/**
	 * Dispatcher which enables communication with server
	 */
	private Dispatcher dispatcher;
	/**
	 * Mail identifies user
	 */
	private String userMail;
	
	/**
	 * Password is checked on server. It is actually  entered by user only when registering and changing phone
	 */
	private String password;
	
	public LoginModule(Dispatcher dispatcher, String userMail, String password) {
		this.dispatcher = dispatcher;
		this.userMail = userMail;
		this.password = password;
	}

	public void register(String mail, String password) throws RegisterException{
		//dispatcher.send(message, category);
		// cos tam
		this.userMail = mail;
		this.password = password;
	}
	
	public void logIn() {
	}
	
	public void logOut() {
	}
	

}
