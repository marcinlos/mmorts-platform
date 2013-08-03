package pl.edu.agh.ki.mmorts.client.backend.modules.mapModule;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleData;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import protocol.mapModule.Requests;
import protocol.mapModule.SimpleMessage;
import protocol.mapModule.helpers.Board;
import protocol.mapModule.helpers.FieldContent;
import protocol.mapModule.helpers.ImmutableBoard;
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
		//Message m = new Message(0, name(), name(), Mode.UNICAST, "full", null);
		Log.d(ID, "Sending request to gateway");
		//gateway().send();
		send(anyAddress(), Requests.FULL_EXTERNAL, new SimpleMessage("test"));
	}
	
	@MessageMapping(Requests.FULL_EXTERNAL)
	public void stateReceived(Message messg, TransactionContext ctx) {
		 //TODO wrong data, two different classes
		protocol.mapModule.MapModuleData receivedData = messg.get(protocol.mapModule.MapModuleData.class);
		MapModuleData convertedData = convert(receivedData);
		ResponseContent content = new ResponseContent(false, true, convertedData);
		ModuleDataMessage message = new ModuleDataMessage(name(), content);
		Log.d(ID, "Sending reply");
		modulesBroker.tellPresenters(message, name());
		
	}

	private MapModuleData convert(protocol.mapModule.MapModuleData receivedData) {
		ImmutableBoard b = receivedData.getBoard();
		boolean[][] map = new boolean[b.getRowsSize()][b.getColsSize()];
		for(int i=0;i<b.getRowsSize();i++){
			for(int j=0;j<b.getColsSize();j++){
				if(b.getAt(i, j).equals(FieldContent.G) || b.getAt(i, j).equals(FieldContent.R)){
					map[i][j] = false;
				}
				else{
					map[i][j] = true;
				}
			}
		}
		MapModuleData converted = new MapModuleData();
		converted.setMapWidth(b.getRowsSize());
		converted.setMapHeight(b.getColsSize()); //this might be upside down, I dunno, it might throw nullPointers if it is
		converted.setMap(map);
		return converted;
	}
	

}
