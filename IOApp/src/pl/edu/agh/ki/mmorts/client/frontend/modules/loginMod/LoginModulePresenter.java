package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import java.io.File;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBrokerDummy;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDone;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessageContent;
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
	/**
	 * Called after proper creation of the universe. 
	 * Checks with module whether we can login from file, logs in an says that it's done
	 * or sends it's view to top and awaits login attempt
	 */
	@Override
	@OnInit
	public void init() {
		
		//DEBUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUG TODO
		modulesBroker = new ModulesBrokerDummy();
		//DEBUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUG
		
		
		presenterId = "LoginModulePresenter";
		createView(); 
		mainSpaceManager.register(LoginView.getViewid(), myView);
		modulesBroker.registerPresenter(this, moduleName);
		
		//moduuu³, mamy plik do logowania?
		modulesBroker.tellModule(new ModuleDataMessage(presenterId, new LoginMessageContent(LoginMessageContent.TO_MODULE_FILE_LOGIN)), moduleName);
		
	}

	@Override
	public void dataChanged(ModuleDataMessage data) {
		if(data.carries(LoginMessageContent.class)){ //possibly reduntant - what else could our module send us.
			LoginMessageContent m = data.getMessage(LoginMessageContent.class);
			switch(m.getMode()){
			case LoginMessageContent.TO_PRESENTER_FILE_LOGIN:
				if(m.isLogFromFileSuccess()){
					sendBusMessage();
					return;
				}
				else{
					mainSpaceManager.toTop(presenterId);
					return;
				}
			case LoginMessageContent.TO_PRESENTER_LOGIN_RESPONSE:
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
		modulesBroker.tellModule(new ModuleDataMessage(presenterId, new LoginMessageContent(login, pass, LoginMessageContent.TO_MODULE_LOGIN)), moduleName);
		
	}
	
	/**
	 * sends the I'm done message
	 */
	private void sendBusMessage(){
		PresentersMessage pm = new PresentersMessage(presenterId, new LoginDone());
		bus.sendMessage(pm);
		
	}
	
	private void createView(){
		
		myView = new LoginView(context, this);
	}

	

}
