package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

/**
 * 
 * Stores LoginModuleData
 *
 */
public class LoginModuleData {
	
	/**
	 * Mail identifies user
	 */
	private String userMail;
	
	/**
	 * Password is checked on server. It is actually  entered by user only when registering and changing phone
	 */
	private String password;
	

	public String getUserMail() {
		return userMail;
	}

	public String getPassword() {
		return password;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
