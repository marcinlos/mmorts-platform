package protocol.loginModule;


import java.io.Serializable;

public class LoginMessage implements Serializable {
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
