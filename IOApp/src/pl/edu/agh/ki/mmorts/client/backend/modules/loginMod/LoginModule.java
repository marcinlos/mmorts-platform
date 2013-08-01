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

	@Inject(optional = true)
	private FileInputStream loginDataInputStream;

	@Inject(optional = true)
	private FileOutputStream loginDataOutputStream;

	private LoginChecker checker = new LoginChecker();

	@Inject(optional = true)
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
	private boolean isLoginFromFilePossible() throws LogInException {
		Log.d(ID, "Checking if log from file is possible");
		return checker.checkIfAccountExists(loginDataInputStream);
	}

	private LoginMessage getLoginDataFromFile() {
		Log.d(ID, "Getting log data from file");
		fromFile = true;
		mail = checker.getProperties().getProperty("mail");
		password = checker.getProperties().getProperty("password");
		Log.d(ID, "Success");
		return new LoginMessage(mail, password);
	}
	

	private LoginMessage getLoginDataFromPresenterMessage(LoginMessageContent m) {
		fromFile=false;
		mail = "test";
		password = "test";
		return new LoginMessage(mail, password);
	}


	private void writePropertiesToFile() {
		Log.d(ID, "Writing user data to file");
		Properties properties = new Properties();
		properties.setProperty("mail", mail);
		properties.setProperty("password", password);
		try {
			properties.store(loginDataOutputStream, null);

		} catch (Exception e) {
			Log.e(ID, "Error in writing user data to file. " + e.getMessage());
		}
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
		LoginMessage loginMessage = null;
		if (m.getLogin() == null) {
			Log.d(ID, "Logging from file");
			if(isLoginFromFilePossible()){
				loginMessage=getLoginDataFromFile();
			}
		} else {
			Log.d(ID, "Logging without file");
			loginMessage = getLoginDataFromPresenterMessage(m);
		}
		loginOnServer(loginMessage);
		if (loggedCorrectly) {
			Log.d(ID, "Correctly logged");
			LoginMessageContent responseContent = new LoginMessageContent.Builder()
					.logged().reply().build();
			ModuleDataMessage responseMessage = new ModuleDataMessage(name(),
					responseContent);
			modulesBroker.tellPresenters(responseMessage, name());
		} else {
			Log.d(ID, "Uncorrectly logged");
		}

	}


	private void loginOnServer(LoginMessage message) {
		Log.d(ID, "Sending to server");
		send(anyAddress(), "auth", message);
	}

	@MessageMapping("auth-success")
	public void getSuccess(Message messg, TransactionContext ctx) {
		if(fromFile){
			writePropertiesToFile();
		}
		Log.d(ID, "Auth success!");
		loggedCorrectly = true;
	}

}
