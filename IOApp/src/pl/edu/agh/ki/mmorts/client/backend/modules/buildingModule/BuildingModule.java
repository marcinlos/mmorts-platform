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
import protocol.buildingsModule.Requests;
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
	private BuildingInstance buildingToChange;
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
			buildingToChange = newBuilding;
			sendAddBuildingQuery();
		}
		else {
			BuildingInstance oldBuilding = null;
			for (BuildingInstance building : oldBuildings) {
				if (!newBuildings.contains(building)) {
					oldBuilding = building;
				}
			}
			buildingToChange = oldBuilding;
			sendRemoveBuildingRequest();
		}
		
	}
	
	private void sendRemoveBuildingRequest() {
		Log.d(ID, "Sending remove building request");
		BuildingMessage buildingMessage = new BuildingMessage(buildingToChange, "");
		Message m = new Message(0, name(), name(), Mode.UNICAST, Requests.DEMOLISH, buildingMessage);
		gateway().send(m);
		
	}

	private void sendAddBuildingQuery() {
		Log.d(ID, "Sending add building query");
		BuildingMessage buildingMessage = new BuildingMessage(buildingToChange, "");
		Message m = new Message(0, name(), name(), Mode.UNICAST, Requests.CAN_BUILD, buildingMessage);
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
		Log.d(ID, "Sending get state request");
		Message m = new Message(0, name(), name(), Mode.UNICAST, Requests.GET_BUILDINGS, null);
		gateway().send(m);
		
	}
	
	private void sendResponse() {
		Log.d(ID, "Sending response");
		ResponseContent content = new ResponseContent(change, success, requestedData);
		ModuleDataMessage message = new ModuleDataMessage(name(), content);
		modulesBroker.tellPresenters(message, name());
	}
	
	@MessageMapping(Requests.GET_BUILDINGS)
	public void gotState(Message messg, TransactionContext ctx) {
		Log.d(ID, "Received full state");
		persistor().createBinding(name(), "", requestedData);
		requestedData = new BuildingModuleData();
		for (BuildingInstance building :  (List<BuildingInstance>) messg.get(List.class)) {
			requestedData.addBuilding(building);
		} 
		sendResponse();
	}
	
	@MessageMapping(Requests.YES_CAN_BUILD)
	public void canBuild(Message messg, TransactionContext ctx){
		Log.d(ID, "Received yes - can build");
		BuildingMessage buildingMessage = new BuildingMessage(buildingToChange, "");
		Message m = new Message(0, name(), name(), Mode.UNICAST, "build", buildingMessage);
		gateway().send(m);
	}
	
	@MessageMapping(Requests.NO_CAN_BUILD)
	public void cannotBuild(Message messg, TransactionContext ctx){
		Log.d(ID, "Received no - cannnot build");
		success = false;
		requestedData = persistor().receiveBinding(name(), "", BuildingModuleData.class);
		sendResponse();
	}
	
	@MessageMapping(Requests.BUILD_SUCCESS)
	public void buildSucceeded(Message messg, TransactionContext ctx){
		Log.d(ID, "Received build success");
		success = true;
		persistor().updateBinding(name(), "", requestedData);
		sendResponse();
	}
	
	
	@MessageMapping(Requests.DEMOLISH_SUCCESS)
	public void demolishSucceeded(Message messg, TransactionContext ctx){
		Log.d(ID, "Received demolish succeded");
		success = true;
		persistor().updateBinding(name(), "", requestedData);
		sendResponse();
	}

}
