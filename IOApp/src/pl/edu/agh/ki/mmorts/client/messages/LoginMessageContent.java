package pl.edu.agh.ki.mmorts.client.messages;

import java.io.File;

public class LoginMessageContent implements ModuleDataMessageContent{
	
	
	public static final int TO_MODULE_FILE_LOGIN = 0;
	public static final int TO_PRESENTER_FILE_LOGIN = 1;
	public static final int TO_MODULE_LOGIN = 2;
	public static final int TO_PRESENTER_LOGIN_RESPONSE = 3;
	
	private int mode;
	/**
	 * set in LoginMessage.mode == 1, true if login from file was successful
	 */
	boolean logFromFileSuccess;
	/**
	 * set in LoginMessage.mode == 3, true if login with given login and pass was succesfull
	 * note, that if these messages are sent it's really a registration attempt,
	 * if it failed, it's because username already exists, or something tragic happened
	 */
	boolean logInSuccess;
	
	
	String login;
	String password;
	
	File file;
	
	
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
	public boolean isLogFromFileSuccess() {
		return logFromFileSuccess;
	}
	public void setLogFromFileSuccess(boolean logFromFileSuccess) {
		this.logFromFileSuccess = logFromFileSuccess;
	}
	public boolean isLogInSuccess() {
		return logInSuccess;
	}
	public void setLogInSuccess(boolean logInSuccess) {
		this.logInSuccess = logInSuccess;
	}
	
	
	
	
	

}
