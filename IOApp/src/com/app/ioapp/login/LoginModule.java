package com.app.ioapp.login;

import com.app.ioapp.modules.ModuleBase;

import android.util.Log;

/**
 * Module responsible for registering, logging in and logging out
 *
 */
public class LoginModule extends ModuleBase{

	private static final String ID = "LoginModule";

	/**
	 * Mail identifies user
	 */
	private String userMail;
	
	/**
	 * Password is checked on server. It is actually  entered by user only when registering and changing phone
	 */
	private String password;
	

	/**
	 * Registers player in system
	 * @param mail
	 * @param password
	 * @throws RegisterException
	 */
	public void register(String mail, String password) throws RegisterException{
		Log.e(ID,"Registering user");
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
