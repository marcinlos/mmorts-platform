package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import android.widget.Button;
import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.GUIGenericMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataChangedListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.PresentersMessage;

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
		// TODO Auto-generated method stub
		
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
	public void LogMeIn() {
		// TODO Auto-generated method stub
		
	}

	

}
