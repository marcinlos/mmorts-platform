package pl.edu.agh.ki.mmorts.client.messages;

import java.io.File;

public class LoginMessageContent implements ModuleDataMessageContent{
	
	
	/**
	 * login from file
	 */
	public static final int TO_MODULE_FILE_LOGIN = 0;
	public static final int TO_PRESENTER_FILE_LOGIN = 1;
	
	/**
	 * note, that if these messages are sent it's really a registration attempt,
	 * if it fails, it's because username already exists, or something tragic happened
	 */
	public static final int TO_MODULE_LOGIN = 2;
	public static final int TO_PRESENTER_LOGIN_RESPONSE = 3;
	
	private int mode;
	
	boolean logInSuccess;
	
	
	String login;
	String password;
	
	
	public <T> LoginMessageContent(int mode) {
		this.mode = mode;
	}
	
	
	public <T> LoginMessageContent(String login, String password, int mode) {
		this.login = login;
		this.password = password;
		this.mode = mode;
	}
	
	
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public boolean isLogInSuccess() {
		return logInSuccess;
	}
	public void setLogInSuccess(boolean logInSuccess) {
		this.logInSuccess = logInSuccess;
	}
	

}
