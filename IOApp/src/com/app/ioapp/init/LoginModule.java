package com.app.ioapp.init;

import android.util.Log;

import com.app.ioapp.communication.Dispatcher;

/**
 * Class responsible for registering, logging in and logging out
 *
 */
public class LoginModule {

	private static final String ID = "LoginModule";
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
	
	/**
	 * @param dispatcher
	 * @param userMail
	 * @param password
	 */
	public LoginModule(Dispatcher dispatcher, String userMail, String password) {
		this.dispatcher = dispatcher;
		this.userMail = userMail;
		this.password = password;
	}

	/**
	 * Registers player in system
	 * @param mail
	 * @param password
	 * @throws RegisterException
	 */
	public void register(String mail, String password) throws RegisterException{
		Log.e(ID,"Registering user");
		//dispatcher.send(message, category);
		// cos tam
		this.userMail = mail;
		this.password = password;
	}
	
	/**
	 * Responsible for logging in
	 * @throws LogInException
	 */
	public void logIn() throws LogInException{
		Log.e(ID,"Logging in user");
	}
	
	/**
	 * Responsible for logging out
	 * @throws LogOutException
	 */
	public void logOut() throws LogOutException{
		Log.e(ID,"Logging out user");
	}
	

}
