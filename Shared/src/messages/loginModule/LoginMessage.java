package messages.loginModule;

public class LoginMessage {
	private String login;
	private String password;
	public String getLogin() {
		return login;
	}
	public String getPassword() {
		return password;
	}
	public LoginMessage(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}
	
}
