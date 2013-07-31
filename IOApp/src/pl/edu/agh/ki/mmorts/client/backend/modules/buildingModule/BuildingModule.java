package pl.edu.agh.ki.mmorts.client.backend.modules.buildingModule;

import com.google.inject.Inject;

import android.util.Log;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod.BuildingModuleData;
import pl.edu.agh.ki.mmorts.client.messages.ChangeStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;

public class BuildingModule extends ModuleBase implements GUICommModule {
	
	/**
	 * for logger
	 */
	private static final String ID = "BuildingModule";
	
	@Inject(optional= true)
	ModulesBroker modulesBroker;

	@Override
	public void dataChanged(ModuleDataMessage message) {
		Log.d(ID, "Got message");
		ModuleDataMessageContent content = (ModuleDataMessageContent) message.getMessage(ModuleDataMessage.class);
		BuildingModuleData dataFromServer = null;
		ResponseContent responseContent;
		if (content instanceof ChangeStateContent) {
			Log.d(ID, "It was change state request");
			boolean success = false;
			// TODO wyslac wiadomosc do serwera
			responseContent = new ResponseContent(true, success, dataFromServer);
		}
		else {
			Log.d(ID, "It was get state request");
			// TODO wyslac wiadomosc do serwera
			responseContent = new ResponseContent(false, true, dataFromServer);
		}
		ModuleDataMessage responseMessage = new ModuleDataMessage(ID, responseContent);
		modulesBroker.tellPresenters(responseMessage, ID);
	}

}
