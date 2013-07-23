package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import android.widget.Button;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataChangedListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;

public class LoginModulePresenter extends AbstractModulePresenter{

	@Override
	public boolean hasMenuButton() {
		return false;
	}

	@Override
	public Button getMenuButton() {
		return null;
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

}
