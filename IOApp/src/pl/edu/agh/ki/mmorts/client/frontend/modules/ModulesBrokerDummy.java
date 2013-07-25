package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod.LoginModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataChangedListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

public class ModulesBrokerDummy implements ModulesBroker {

	ModuleDataChangedListener p;
	@Override
	public void registerPresenter(ModuleDataChangedListener presenter, String moduleName) {
		p = presenter;
		return;
		
	}

	@Override
	public void unregisterPresenter(ModuleDataChangedListener presenter) {
		return;
		
	}

	@Override
	public void tellModule(ModuleDataMessage message, String moduleName) {
		if (message.carries(LoginMessageContent.class)){
			LoginMessageContent m = message.getMessage(LoginMessageContent.class);
			switch(m.getMode()){
			case LoginMessageContent.TO_MODULE_FILE_LOGIN:
				LoginMessageContent mnew = new LoginMessageContent(LoginMessageContent.TO_PRESENTER_FILE_LOGIN);
				mnew.setLogFromFileSuccess(false); //chcemy cos zobaczyc, wiec z pliku sie nie udaje
				tellPresenters(new ModuleDataMessage("", mnew), moduleName);
				break;
			case LoginMessageContent.TO_MODULE_LOGIN:
				LoginMessageContent mnew2 = new LoginMessageContent(LoginMessageContent.TO_PRESENTER_LOGIN_RESPONSE);
				mnew2.setLogInSuccess(true); //przyjmiemy zwykly login
				tellPresenters(new ModuleDataMessage("", mnew2), moduleName);
				break;
			}
		}
		
	}

	@Override
	public void tellPresenters(ModuleDataMessage message, String moduleName) {
		p.dataChanged(message);
		
	}

}
