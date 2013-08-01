package pl.edu.agh.ki.mmorts.client.backend.modules.mapModule;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Mode;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleData;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;

import android.util.Log;

import com.google.inject.Inject;

public class MapModule extends ModuleBase implements GUICommModule{
	
	private static final String ID = "MapModule";
	
	@Inject(optional= true)
	ModulesBroker modulesBroker;

	@Override
	public void dataChanged(ModuleDataMessage message) {
		Log.d(ID, "Got message");
		getState();
	}

	private void getState() {
		Message m = new Message(0, name(), name(), Mode.UNICAST, "full", null);
		Log.d(ID, "Sending request to gateway");
		gateway().send(m);
		
	}
	
	@MessageMapping("full")
	public void stateReceived(Message messg, TransactionContext ctx) {
		MapModuleData receivedData = messg.get(MapModuleData.class);
		ResponseContent content = new ResponseContent(false, true, receivedData);
		ModuleDataMessage message = new ModuleDataMessage(name(), content);
		Log.d(ID, "Sending reply");
		modulesBroker.tellPresenters(message, name());
		
	}
	

}
