package pl.edu.agh.ki.mmorts.client.backend.modules.buildingModule;

import java.util.List;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Mode;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod.BuildingInstance;
import pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod.BuildingModuleData;
import pl.edu.agh.ki.mmorts.client.messages.ChangeStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import android.util.Log;

import com.google.inject.Inject;

public class BuildingModule extends ModuleBase implements GUICommModule {
	
	/**
	 * for logger
	 */
	private static final String ID = "BuildingModule";
	
	/**
	 * Field set when communicating with presenter
	 */
	private BuildingModuleData requestedData;
	/**
	 * Field set when communicating with presenter
	 */
	private boolean success;
	/**
	 * Field set when communicating with presenter
	 */
	private boolean change;
	
	
	@Inject(optional= true)
	ModulesBroker modulesBroker;

	@Override
	public void dataChanged(ModuleDataMessage message) {
		Log.d(ID, "Got message");
		if (message.carries(ChangeStateContent.class)) {
			Log.d(ID, "It was change state request");
			requestedData = (BuildingModuleData) message.getMessage(ChangeStateContent.class).getState();
			change = true;
			changeState(requestedData);
		}
		else {
			Log.d(ID, "It was get state request");
			getState();
		}
	}

	private void changeState(BuildingModuleData newState) {
		List<BuildingInstance> oldBuildings = 
				persistor().receiveBinding(name(), "", BuildingModuleData.class).getBuildings();
		List<BuildingInstance> newBuildings = newState.getBuildings();
		if (newBuildings.size() > oldBuildings.size()) {
			BuildingInstance newBuilding = null;
			for (BuildingInstance building : newBuildings) {
				if (!oldBuildings.contains(building)) {
					newBuilding = building;
				}
			}
			sendAddBuildingRequest(newBuilding);
		}
		else {
			BuildingInstance oldBuilding = null;
			for (BuildingInstance building : oldBuildings) {
				if (!newBuildings.contains(building)) {
					oldBuilding = building;
				}
			}
			sendRemoveBuildingRequest(oldBuilding);
		}
		
	}
	
	private void sendRemoveBuildingRequest(BuildingInstance oldBuilding) {
		Message m = new Message(0, name(), name(), Mode.UNICAST, "RemoveBuilding", oldBuilding);
		gateway().send(m);
		
	}

	private void sendAddBuildingRequest(BuildingInstance newBuilding) {
		BuildingMessage buildingMessage = new BuildingMessage(newBuilding, "");
		Message m = new Message(0, name(), name(), Mode.UNICAST, "build", buildingMessage);
		gateway().send(m);
		
	}

	private void getState() {
		BuildingModuleData buildingData = persistor().receiveBinding(name(), "", BuildingModuleData.class);
		if (buildingData != null) {
			sendResponse();	
		}
		else {
			sendGetStateRequest();
		}
	}
	
	private void sendGetStateRequest() {
		Message m = new Message(0, name(), name(), Mode.UNICAST, "GetState", null);
		gateway().send(m);
		
	}

	private void sendResponse() {
		ResponseContent content = new ResponseContent(change, success, requestedData);
		ModuleDataMessage message = new ModuleDataMessage(name(), content);
		modulesBroker.tellPresenters(message, name());
	}
	
	@MessageMapping("yes-can-build")
	public void buildSucceeded(Message messg, TransactionContext ctx){
		success = true;
		persistor().updateBinding(name(), "", requestedData);
		sendResponse();
	}
	
	@MessageMapping("no-can-build")
	public void buildNotSucceeded(Message messg, TransactionContext ctx){
		success = false;
		requestedData = persistor().receiveBinding(name(), "", BuildingModuleData.class);
		sendResponse();
	}

}
