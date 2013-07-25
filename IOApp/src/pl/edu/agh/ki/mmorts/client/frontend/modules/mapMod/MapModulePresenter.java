package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDone;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.MapViewCreated;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.views.MenuButton;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;
import android.widget.Button;

/**
 * Presenter for map module. The main presenter for {@code MapModuleView}
 *
 */
public class MapModulePresenter extends AbstractModulePresenter{
	private static final String ID = "MapModulePresenter";
	private static final String VIEW_ID = "MapView";
	/**
	 * Name of module that I want to communicate with
	 */
	private static final String MODULE_NAME = "MapModule";

	private MapModuleView mapModuleView;
	private MenuButton menuButton;
	
	@Override
	@OnInit
	public void init() {
		presenterId = "MapModulePresenter";
		modulesBroker.registerPresenter(this, MODULE_NAME);
		mapModuleView = new MapModuleView(context);
		mainSpaceManager.register(VIEW_ID, mapModuleView);
		informOthersAboutView();
		menuButton = new MenuButton(context);
		menuButton.setView(mapModuleView);
		Log.d(ID, "context:");
		Log.d(ID, String.format("%s", context));
		Log.d(ID, "topSpaceManager:");
		Log.d(ID, String.format("%s", topSpaceManager));
		Log.d(ID, " mainSpaceManager:");
		Log.d(ID, String.format("%s", mainSpaceManager));
		Log.d(ID, "modulesBroker:");
		Log.d(ID, String.format("%s", modulesBroker));
	}
	
	@Override
	public boolean hasMenuButton() {
		return true;
	}


	@Override
	public Button getMenuButton() {
		return menuButton;
	}

	@Override
	public void dataChanged(ModuleDataMessage message) {
		if (message.carries(MapModuleData.class)) {
			MapModuleData data = message.getMessage(MapModuleData.class);
			mapModuleView.updateMapData(data);
			mapModuleView.postInvalidate();
			mainSpaceManager.toTop(mapModuleView.getViewId());
		}
		
	}
	
	/**
	 * It's called to send an object of {@code MapModuleView}  to other presenters which want to have it.
	 */
	private void informOthersAboutView() {
		MapViewCreated content = new MapViewCreated();
		content.setView(mapModuleView);
		PresentersMessage message = new PresentersMessage(presenterId, content);
		bus.sendMessage(message);
	}
	

	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(LoginDone.class)) {
			// TODO
			//It means that it's a request for data from server
			ModuleDataMessage message = new ModuleDataMessage(presenterId, null);
			modulesBroker.tellModule(message, MODULE_NAME);
		}
		
	}


}
