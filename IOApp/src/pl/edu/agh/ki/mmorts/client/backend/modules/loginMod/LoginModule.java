package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Mode;
import pl.edu.agh.ki.mmorts.client.backend.init.LoginChecker;
import pl.edu.agh.ki.mmorts.client.backend.modules.Context;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod.LoginModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDoneMessageContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
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
		LoginMessageContent content = message.getMessage(LoginMessageContent.class);
		LoginMessageContent responseContent;
		if (content.getMode() == LoginMessageContent.TO_MODULE_FILE_LOGIN) {
			Log.d(ID, "It was file login");
			responseContent = new LoginMessageContent(LoginMessageContent.TO_PRESENTER_FILE_LOGIN);
			responseContent.setLogInSuccess(logInFromFile());
		}
		else {
			Log.d(ID, "It was login without file");
			String mail = content.getLogin();
			String password = content.getPassword();
			responseContent = new LoginMessageContent(LoginMessageContent.TO_PRESENTER_LOGIN_RESPONSE);
			responseContent.setLogInSuccess(logInWithoutFile(mail, password));
		}
		ModuleDataMessage responseMessage = new ModuleDataMessage(ID, responseContent);
		Log.d(ID, "Telling response to presenters");
		modulesBroker.tellPresenters(responseMessage, ID);
	}
	
	private boolean loginOnServer() {
		Log.d(ID, "Sending to server");
		send(anyAddress(), "auth");
		return false;
	}
	
	@MessageMapping("auth-success")
	public void getSuccess(Message messg, TransactionContext ctx){
		Log.d(ID, "Auth success!");
		modulesBroker.tellPresenters(new ModuleDataMessage(messg.target, new LoginMessageContent(fromFile? LoginMessageContent.TO_MODULE_FILE_LOGIN: LoginMessageContent.TO_MODULE_LOGIN)), messg.target);
	}
	

}
