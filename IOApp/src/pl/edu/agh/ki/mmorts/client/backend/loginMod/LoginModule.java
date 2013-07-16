package pl.edu.agh.ki.mmorts.client.backend.loginMod;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
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
		Log.d(ID,"Registering user");
		this.userMail = mail;
		this.password = password;
	}
	
	/**
	 * Responsible for logging in
	 * @throws LogInException
	 */
	public void logIn() throws LogInException{
		Log.d(ID,"Logging in user");
	}
	
	/**
	 * Responsible for logging out
	 * @throws LogOutException
	 */
	public void logOut() throws LogOutException{
		Log.d(ID,"Logging out user");
	}

	@Override
	public void receive(Message message, TransactionContext context) {
		return;
	}
	

}
