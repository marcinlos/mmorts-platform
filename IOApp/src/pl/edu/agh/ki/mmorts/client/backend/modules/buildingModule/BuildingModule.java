package pl.edu.agh.ki.mmorts.client.backend.modules.buildingModule;

import java.util.List;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Mode;
import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.backend.modules.annotations.MessageMapping;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.messages.ChangeStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import protocol.buildingsModule.BuildingInstance;
import protocol.buildingsModule.BuildingMessage;
import protocol.buildingsModule.BuildingModuleData;
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

	
	@OnInit
	public void debug(){
		Log.d(ID,persistor().toString());
	}
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
				persistor().receiveBinding(name(), "test", BuildingModuleData.class).getBuildings();
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
		//BuildingMessage buildingMessage = new BuildingMessage(buildingToChange, "test");
		//Message m = new Message(0, name(), name(), Mode.UNICAST, Requests.DEMOLISH, buildingMessage);
		//gateway().send(m);
		send(anyAddress(), Requests.DEMOLISH, new BuildingMessage(buildingToChange,"test"));
		
	}

	private void sendAddBuildingQuery() {
		Log.d(ID, "Sending add building query");
		//BuildingMessage buildingMessage = new BuildingMessage(buildingToChange, "test");
		//Message m = new Message(0, name(), name(), Mode.UNICAST, Requests.CAN_BUILD, buildingMessage);
		//gateway().send(m);
		send(anyAddress(), Requests.CAN_BUILD, new BuildingMessage(buildingToChange,"test"));
		
	}

	

	private void getState() {
		BuildingModuleData dbData = persistor().receiveBinding(name(), "test", BuildingModuleData.class);
		requestedData = new BuildingModuleData();
		if (dbData == null) {
			sendGetStateRequest();
		}
		else {
			for (BuildingInstance building : dbData.getBuildings()) {
				requestedData.addBuilding(building);
			}
		sendResponse();	
		}
	}
	
	
	private void sendGetStateRequest() {
		Log.d(ID, "Sending get state request");
		//Message m = new Message(0, name(), name(), Mode.UNICAST, Requests.GET_BUILDINGS, null);
		//gateway().send(m);
		send(anyAddress(), Requests.GET_BUILDINGS, new BuildingMessage(null,"test"));
		
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
		
		requestedData = new BuildingModuleData();
		for (BuildingInstance building :   messg.get(BuildingModuleData.class).getBuildings()) {
			requestedData.addBuilding(building);
		} 
		BuildingModuleData inDatabase = new BuildingModuleData();
		for (BuildingInstance building : requestedData.getBuildings()) {
			inDatabase.addBuilding(building);
		}
		persistor().createBinding(name(), "test", inDatabase);
		sendResponse();
	}
	
	@MessageMapping(Requests.YES_CAN_BUILD)
	public void canBuild(Message messg, TransactionContext ctx){
		Log.d(ID, "Received yes - can build");
		//BuildingMessage buildingMessage = new BuildingMessage(buildingToChange, "test");
		//Message m = new Message(0, name(), name(), Mode.UNICAST, "build", buildingMessage);
		//gateway().send(m);
		send(anyAddress(), Requests.BUILD, new BuildingMessage(buildingToChange,"test"));
	}
	
	@MessageMapping(Requests.NO_CAN_BUILD)
	public void cannotBuild(Message messg, TransactionContext ctx){
		Log.d(ID, "Received no - cannnot build");
		success = false;
		requestedData = persistor().receiveBinding(name(), "test", BuildingModuleData.class);
		sendResponse();
	}
	
	@MessageMapping(Requests.BUILD_SUCCESS)
	public void buildSucceeded(Message messg, TransactionContext ctx){
		Log.d(ID, "Received build success");
		success = true;
		persistor().updateBinding(name(), "test", requestedData);
		sendResponse();
	}
	
	
	@MessageMapping(Requests.DEMOLISH_SUCCESS)
	public void demolishSucceeded(Message messg, TransactionContext ctx){
		Log.d(ID, "Received demolish succeded");
		success = true;
		persistor().updateBinding(name(), "test", requestedData);
		sendResponse();
	}

}
