package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import java.io.File;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDone;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class LoginModulePresenter extends AbstractModulePresenter implements LoginListener{
	
	private static final String moduleName = "LoginModule";
	private static final String ID = "LoginModulePresenter"; //for Android.Log
	
	private LoginView myView;
	private LayoutInflater inflater;
	
	/**
	 * Called after proper creation of the universe. 
	 * Checks with module whether we can login from file, logs in an says that it's done
	 * or sends it's view to top and awaits login attempt
	 */
	@Override
	@OnInit
	public void init() {
		presenterId = "LoginModulePresenter";
		createView(); //TODO should it be here?
		
		//moduuu�, mamy plik do logowania?
		modulesBroker.tellModule(new ModuleDataMessage(presenterId, new LoginMessage(LoginMessage.TO_MODULE_FILE_LOGIN)), moduleName);
		
	}

	@Override
	public void dataChanged(ModuleDataMessage data) {
		if(data.carries(LoginMessage.class)){
			LoginMessage m = data.getMessage(LoginMessage.class);
			switch(m.getMode()){
			case LoginMessage.TO_PRESENTER_FILE_LOGIN:
				if(m.isLogFromFileSuccess()){
					sendBusMessage();
					return;
				}
				else{
					mainSpaceManager.toTop(presenterId);
					return;
				}
			case LoginMessage.TO_PRESENTER_LOGIN_RESPONSE:
				if(m.isLogInSuccess()){
					sendBusMessage();
					return;
				}
				else{
					Log.d(ID,"Login with email failed, it shouldn't happen exactly often");
					myView.invalidLogIn();
				}
				
			}
		}
		
	}
	/**
	 * Login doesn't care what other modules do or say to each other.
	 */
	@Override
	public void gotMessage(PresentersMessage m) {
		return;
	}

	@Override
	public void LogMeIn(String login, String pass) {
		modulesBroker.tellModule(new ModuleDataMessage(presenterId, new LoginMessage(login, pass, LoginMessage.TO_MODULE_LOGIN)), moduleName);
		
	}
	
	/**
	 * sends the I'm done message
	 */
	private void sendBusMessage(){
		PresentersMessage pm = new PresentersMessage(presenterId, new LoginDone());
		bus.sendMessage(pm);
		
	}
	
	private void createView(){
		
		myView = new LoginView(context);
	}

	

}
