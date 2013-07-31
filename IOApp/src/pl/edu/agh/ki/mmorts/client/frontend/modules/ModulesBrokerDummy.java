package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataChangedListener;
import pl.edu.agh.ki.mmorts.client.messages.ChangeStateContent;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;

public class ModulesBrokerDummy implements ModulesBroker {

	ModuleDataChangedListener p;
	Object lastState;
	@Override
	public void registerPresenter(ModuleDataChangedListener presenter, String moduleName) {
		p = presenter;
		return;
		
	}

	@Override
	public void unregisterPresenter(String presenterName) {
		return;
		
	}

	@Override
	public void tellModule(ModuleDataMessage message, String moduleName) {
		if (message.carries(LoginMessageContent.class)){
			LoginMessageContent m = message.getMessage(LoginMessageContent.class);
			switch(m.getMode()){
			case LoginMessageContent.TO_MODULE_FILE_LOGIN:
				LoginMessageContent mnew = new LoginMessageContent(LoginMessageContent.TO_PRESENTER_FILE_LOGIN);
				mnew.setLogInSuccess(false); //chcemy cos zobaczyc, wiec z pliku sie nie udaje
				tellPresenters(new ModuleDataMessage("", mnew), moduleName);
				break;
			case LoginMessageContent.TO_MODULE_LOGIN:
				LoginMessageContent mnew2 = new LoginMessageContent(LoginMessageContent.TO_PRESENTER_LOGIN_RESPONSE);
				mnew2.setLogInSuccess(true); //przyjmiemy zwykly login
				tellPresenters(new ModuleDataMessage("", mnew2), moduleName);
				break;
			}
		}
		if(message.carries(ChangeStateContent.class)){
			lastState = message.getMessage(ChangeStateContent.class).getState();
			ResponseContent rc = new ResponseContent(true,true,message.getMessage(ChangeStateContent.class).getState());
			
			tellPresenters(new ModuleDataMessage("",rc), moduleName);
		}
		if(message.carries(GetStateContent.class)){
			ResponseContent rc = new ResponseContent(false,true,lastState);
			tellPresenters(new ModuleDataMessage("",rc), moduleName);
			
		}
		//get map message and answer it
		
	}

	@Override
	public void tellPresenters(ModuleDataMessage message, String moduleName) {
		p.dataChanged(message);
		
	}

	@Override
	public void registerModule(GUICommModule module, String moduleName) {
		// stub
		
	}

	@Override
	public void unregisterModule(String presenterName) {
		// stub
		
	}

}
