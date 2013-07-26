package pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleView;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.DrawMapContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.MapDrawnContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import pl.edu.agh.ki.mmorts.client.messages.StateChangedContent;
/**
 * Presenter for building module. It is not the main presenter for any view.
 * It does not have menu button
 *
 */
public class BuildingModulePresenter extends AbstractModulePresenter{
	private static final String ID = "BuildingModulePresenter";
	/**
	 * Name of module that I want to communicate with
	 */
	private static final String MODULE_NAME = "BuildingModule";
	
	
	/**
	 * This presenter is not the main presenter for this view so 
	 * it does not create it
	 */
	private MapModuleView mapModuleView;
	
	/**
	 * Data displayed by {@code MapModuleView}
	 */
	private BuildingModuleData buildingModuleData;
	
	@Override
	@OnInit
	public void init() {
		presenterId = "BuildingModulePresenter";
		modulesBroker.registerPresenter(this, MODULE_NAME);
		mapModuleView = (MapModuleView) mainSpaceManager.getViewById(MapModuleView.getViewId());
	}


	@Override
	public void dataChanged(ModuleDataMessage message) {
		if (message.carries(BuildingModuleData.class)) {
			ModuleDataMessageContent content = (ModuleDataMessageContent) message.getMessage(ModuleDataMessage.class);
			if (content instanceof ResponseContent) {
				if (((ResponseContent) content).isResponseToChange() && !((ResponseContent) content).isPositive()) {
					informViewAboutFailure();
					return;
				}
					buildingModuleData = (BuildingModuleData) ((ResponseContent) content).getState();
			}
			else {
				buildingModuleData = (BuildingModuleData) ((StateChangedContent) content).getState();
			}
			PresentersMessage presentersMessage = new PresentersMessage(ID, new DrawMapContent());
			bus.sendMessage(presentersMessage);
		}
		
	}



	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(MapDrawnContent.class)) {
			ModuleDataMessage message = new ModuleDataMessage(ID, new GetStateContent());
			modulesBroker.tellModule(message, MODULE_NAME);
		}
	
	}
	
	private void informViewAboutFailure() {
		// TODO Auto-generated method stub
		
	}
}
