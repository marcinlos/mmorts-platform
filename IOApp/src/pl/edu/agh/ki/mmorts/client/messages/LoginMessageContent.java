package pl.edu.agh.ki.mmorts.client.messages;


public class LoginMessageContent {
	
	private final String login;
	private final String password;
	private final boolean request;
	private final boolean loggedIn;
	
	
	private LoginMessageContent(String login, String password, boolean request,boolean loggedIn) {
		this.login = login;
		this.password = password;
		this.request = request;
		this.loggedIn = loggedIn;
	}
	
	public boolean isRequest(){
		return request;
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public boolean isLoggedIn(){
		return loggedIn;
	}
	
	public static class Builder{
		private String login;
		private String password;
		private boolean request;
		private boolean loggedIn;
		
		public Builder() {
			request=true;
		}
		
		public Builder request(){
			request = true;
			return this;
		}
		
		public Builder reply(){
			request = false;
			return this;
		}
		
		public Builder password(String password){
			this.password=password;
			return this;
		}
		
		public Builder login(String login){
			this.login=login;
			return this;
		}
		
		public Builder failed(){
			this.loggedIn=false;
			return this;
		}
		
		public Builder logged(){
			this.loggedIn=true;
			return this;
		}
		
		public LoginMessageContent build(){
			return new LoginMessageContent(login, password,request,loggedIn);
		}
	}
	
	
//	/*
//	/**
//	 * login from file
//	 */
//	public static final int TO_MODULE_FILE_LOGIN = 0;
//	public static final int TO_PRESENTER_FILE_LOGIN = 1;
//	
//	/**
//	 * note, that if these messages are sent it's really a registration attempt,
//	 * if it fails, it's because username already exists, or something tragic happened
//	 */
//	public static final int TO_MODULE_LOGIN = 2;
//	public static final int TO_PRESENTER_LOGIN_RESPONSE = 3;
//	
//	private int mode;
//	
//	private boolean logInSuccess;
//	
//	private String login;
//	private String password;
//	
//	public String getLogin() {
//		return login;
//	}
//
//	public String getPassword() {
//		return password;
//	}	
//	
//	public LoginMessageContent(int mode) {
//		this.mode = mode;
//	}
//	
//	
//	public LoginMessageContent(String login, String password, int mode) {
//		this.login = login;
//		this.password = password;
//		this.mode = mode;
//	}
//	
//	
//	public int getMode() {
//		return mode;
//	}
//	public void setMode(int mode) {
//		this.mode = mode;
//	}
//	public boolean isLogInSuccess() {
//		return logInSuccess;
//	}
//	public void setLogInSuccess(boolean logInSuccess) {
//		this.logInSuccess = logInSuccess;
//	}
	

}
