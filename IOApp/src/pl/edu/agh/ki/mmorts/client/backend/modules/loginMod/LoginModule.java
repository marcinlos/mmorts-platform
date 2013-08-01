package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import messages.login.LoginMessage;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.init.LoginChecker;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;

import com.google.inject.Inject;

/**
 * Module responsible for registering, logging in and logging out
 * 
 */
public class LoginModule extends ModuleBase implements GUICommModule {

	private static final String ID = "LoginModule";
	
	@Inject(optional=true)
	private FileInputStream loginDataInputStream;
	
	@Inject(optional=true)
	private FileOutputStream loginDataOutputStream;

	private LoginChecker checker = new LoginChecker();
	
	@Inject(optional= true)
	ModulesBroker modulesBroker;
	
	private boolean loggedCorrectly = false; 
	
	private String mail;
	private String password;

	
	private boolean fromFile = false;

	/**
	 * Tries to log in from file
	 * 
	 * @throws LogInException
	 */
	private boolean logInFromFile() throws LogInException {
		Log.d(ID, "Trying to log in from file");
		fromFile = true;
		if(!checker.checkIfAccountExists(loginDataInputStream)) {
			Log.d(ID, "File does not have correct properties");
			return false;
		}
		else {
			mail = checker.getProperties().getProperty("mail");
			password = checker.getProperties().getProperty("password");
			loginOnServer();
			Log.d(ID, "Log in succeeded");
			return true;
		}
	}
	
	private boolean logInWithoutFile(String mail, String password) throws LogInException {
		Log.d(ID, "Trying to log in without file");
		fromFile = false;
		loginOnServer();
		writePropertiesToFile(mail, password);
		this.mail = mail;
		this.password = password;
		Log.d(ID, "Log in succeeded");
		return true;
	}
	
	private void writePropertiesToFile(String mail, String password) {
		Log.d(ID, "Writing user data to file");
		Properties properties = new Properties();
		properties.setProperty("mail",mail);
		properties.setProperty("password", password);
		try {
			properties.store(loginDataOutputStream, null);
			
		} catch (Exception e) {
			Log.e(ID, "Error in writing user data to file. " + e.getMessage());
		}
	}

	/**
	 * Responsible for logging out. Currently empty.
	 * 
	 * @throws LogOutException
	 */
	private void logOut() throws LogOutException {
		Log.d(ID, "Logging out user");
	}

	@Override
	public void dataChanged(ModuleDataMessage message) {
		
		if (!message.carries(LoginMessageContent.class)) {
			throw new IllegalArgumentException("Unacceptable message passed");
		}
		LoginMessageContent m = message.getMessage(LoginMessageContent.class);

		if (!m.isRequest()) {
			throw new IllegalArgumentException("Unacceptable message passed");
		}
		
		if (m.getLogin()==null) {
			Log.d(ID, "Logging from file");
			//logInFromFile();
		}
		else {
			Log.d(ID, "Logging without file");
			//logInWithoutFile(m.getLogin(), m.getPassword());
		}
		loginOnServer();
		if(loggedCorrectly){
			Log.d(ID, "Correctly logged");
			LoginMessageContent responseContent = new LoginMessageContent.Builder().logged().reply().build();
			ModuleDataMessage responseMessage = new ModuleDataMessage(name(), responseContent);
			modulesBroker.tellPresenters(responseMessage, name());
		}else{
			Log.d(ID, "Uncorrectly logged");
		}
		
		
	}
	
	private void loginOnServer() {
		Log.d(ID, "Sending to server");
		send(anyAddress(), "auth", new LoginMessage("TestEmail@Email.org", "ala123"));
	}
	
	@MessageMapping("auth-success")
	public void getSuccess(Message messg, TransactionContext ctx){
		Log.d(ID, "Auth success!");
		loggedCorrectly = true;
	}
	

}
