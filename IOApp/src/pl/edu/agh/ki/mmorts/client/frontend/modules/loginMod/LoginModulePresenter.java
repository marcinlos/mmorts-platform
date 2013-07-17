package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import android.widget.Button;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;

public class LoginModulePresenter implements ModulePresenter{

	@Override
	public boolean hasMenuButton() {
		return false;
	}

	@Override
	public Button getMenuButton() {
		return null;
	}


	@Override
	public void dataChanged(ModuleDataMessage data) {
		// TODO Auto-generated method stub
		
	}

}
