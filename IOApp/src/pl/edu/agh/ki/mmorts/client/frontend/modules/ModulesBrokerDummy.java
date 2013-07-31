package pl.edu.agh.ki.mmorts.client.frontend.modules;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleData;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataChangedListener;
import pl.edu.agh.ki.mmorts.client.messages.ChangeStateContent;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;

public class ModulesBrokerDummy implements ModulesBroker {

	private static final String ID = "DummyBroker";
	Object lastState;
	private Map<ModuleDataChangedListener, String> modulePresenters = new HashMap<ModuleDataChangedListener, String>();
	@Override
	public void registerPresenter(ModuleDataChangedListener presenter, String moduleName) {
		Log.d(ID, "Registering presenter" + moduleName);
		if (modulePresenters.containsKey(presenter)) {
			Log.e(ID, "Trying to register presenter again");
			throw new ModulesBrokerException("Presenter is already registered.");
		}
		modulePresenters.put(presenter, moduleName);
		Log.d(ID,"Registered someone to " + moduleName);
		
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
				Log.e(ID,"Imma log that dude");
				tellPresenters(new ModuleDataMessage("", mnew2), moduleName);
				break;
			}
		}
		if(message.carries(ChangeStateContent.class)){
			lastState = message.getMessage(ChangeStateContent.class).getState();
			ResponseContent rc = new ResponseContent(true,true,message.getMessage(ChangeStateContent.class).getState());
			Log.e(ID,"yes, you change that state");
			
			tellPresenters(new ModuleDataMessage("",rc), moduleName);
		}
		if(message.carries(GetStateContent.class)){
			MapModuleData data = new MapModuleData();
			data.setMap(new boolean[10][10]);
			ResponseContent rc;
			if(lastState == null){
				Log.e(ID,"yeah, get new state from " + moduleName);
				rc = new ResponseContent(false,true,data);
			}
			else{
				Log.e(ID,"I've got a state for you to get from " + moduleName);
				rc = new ResponseContent(false,true,lastState);
			}
			
			tellPresenters(new ModuleDataMessage("",rc), moduleName);
			
		}
		//get map message and answer it
		
	}

	@Override
	public void tellPresenters(ModuleDataMessage message, String moduleName) {
		Log.d(ID, "Invoking presenters " + moduleName);
		for (ModuleDataChangedListener presenter : modulePresenters.keySet()) {
			if (modulePresenters.get(presenter).equals(moduleName)) {
				presenter.dataChanged(message);
			}
		}
		
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
