package pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleData;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleView;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.MapViewCreated;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.widget.Button;
/**
 * Presenter for building module. It is not the main presenter for any view.
 * It does not have menu button
 *
 */
public class BuildingModulePresenter extends AbstractModulePresenter{
	
	/**
	 * Name of module that I want to communicate with
	 */
	private String moduleName = "BuildingModule";
	
	/**
	 * This presenter is not the main presenter for this view so 
	 * it does not create it
	 */
	private MapModuleView mapModuleView;
	
	@Override
	@OnInit
	public void init() {
		
	}


	@Override
	public void dataChanged(ModuleDataMessage message) {
		if (message.carries(BuildingModuleData.class)) {
			BuildingModuleData data = message.getMessage(BuildingModuleData.class);
			mapModuleView.updateBuildingData(data);
			mapModuleView.postInvalidate();
			mainSpaceManager.toTop(mapModuleView.getViewId());
		}
		
	}


	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(MapViewCreated.class)) {
			mapModuleView = (m.getMessage(MapViewCreated.class)).getView();
		}
/*			//It means that it's a request for data from server
			ModuleDataMessage message = new ModuleDataMessage(presenterId, null);
			modulesBroker.tellModule(message, moduleName);
		} */
		
	}

}
