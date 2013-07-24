package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDone;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.widget.Button;

public class LoginModulePresenter extends AbstractModulePresenter implements LoginListener{
	
	

	@Override
	public boolean hasMenuButton() {
		return false;
	}

	@Override
	public Button getMenuButton() {
		return null;
	}
	
	/**
	 * Called after proper creation of the universe. 
	 * Checks with module whether we can login from file, logs in an says that it's done
	 * or sends it's view to top and awaits login attempt
	 */
	@Override
	@OnInit
	public void init() {
		presenterId = "LoginModulePresenter";
		
		//moduuu³, mamy plik do logowania?
		boolean loginFromFile = false;
		//bo jak tak to nas zaloguj
		if(loginFromFile)
		{
			//if(zalogowalNas) sendMessage(); return;
		}
		else{
			mainSpaceManager.toTop(presenterId);
		}
		
	}


	/**
	 * Idea of dataChanged is that module sends info about underlying change
	 * in case of login, no such thing should happen, or if the implementation causes it
	 * it needs to do nothing
	 * @param data
	 */
	@Override
	public void dataChanged(ModuleDataMessage data) {
		return;
		
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
		// TODO Auto-generated method stub
		//tell module to log in
		//update tell the view that he rejected us if he rejected us
		//if he didn't, sentMessage()
	}
	
	private void sendMessage(){
		PresentersMessage pm = new PresentersMessage(presenterId, new LoginDone());
		bus.sendMessage(pm);
		
	}

	

}
