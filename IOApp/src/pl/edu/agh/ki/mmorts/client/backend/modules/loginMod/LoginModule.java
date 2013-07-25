package pl.edu.agh.ki.mmorts.client.backend.modules.loginMod;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.init.LoginChecker;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;

import com.google.inject.Inject;

/**
 * Module responsible for registering, logging in and logging out
 * 
 */
public class LoginModule extends ModuleBase implements GUICommModule {

	private static final String ID = "LoginModule";
	
	@Inject
	private File loginDataFile;

	@Inject 
	private LoginChecker checker;
	
	private String mail;
	private String password;


	/**
	 * Tries to log in from file
	 * 
	 * @throws LogInException
	 */
	private boolean logInFromFile() throws LogInException {
		Log.d(ID, "Trying to log in from file");
		if(!checker.checkIfAccountExists()) {
			Log.d(ID, "File does not have correct properties");
			return false;
		}
		else {
			mail = checker.getProperties().getProperty("mail");
			password = checker.getProperties().getProperty("password");
			// wyslij wiadomosc do serwera ze chcesz sie zalogowac
			// jesli sie nie uda - zwroc false
			Log.d(ID, "Log in succeeded");
			return true;
		}
	}
	
	private boolean logInWithoutFile(String mail, String password) throws LogInException {
		Log.d(ID, "Trying to log in without file");
		// wyslij wiadomosc do serwera ze chcesz sie zalogowac
		// jesli sie nie uda - zwroc false
		writePropertiesToFile(mail, password);
		this.mail = mail;
		this.password = password;
		Log.d(ID, "Log in succeeded");
		return true;
	}
	
	private void writePropertiesToFile(String mail, String password) {
		Properties properties = new Properties();
		properties.setProperty("mail",mail);
		properties.setProperty("password", password);
		try {
			FileOutputStream loginOutputStream = new FileOutputStream(loginDataFile);
			properties.store(loginOutputStream, null);
			
		} catch (Exception e) {
			Log.e(ID, "Error in writing user data to file. " + e.getMessage());
		}
	}

	/**
	 * Responsible for logging out. Currently not used.
	 * 
	 * @throws LogOutException
	 */
	private void logOut() throws LogOutException {
		Log.d(ID, "Logging out user");
		//wyslij wiadomosc do serwera
	}

	/**
	 * Does not need to do anything
	 */
	@Override
	public void receive(Message message, TransactionContext context) {
		return;
	}

	@Override
	public void dataChanged(ModuleDataMessage data) {
		// TODO Auto-generated method stub

	}

}
