package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.DrawMapContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDoneMessageContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.MapDrawnContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.views.MenuButton;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import pl.edu.agh.ki.mmorts.client.messages.StateChangedContent;
import android.util.Log;
import android.widget.Button;

/**
 * Presenter for map module. The main presenter for {@code MapModuleView}
 *
 */
public class MapModulePresenter extends AbstractModulePresenter{
	private static final String ID = "MapModulePresenter";

	/**
	 * Name of module that I want to communicate with
	 */
	private static final String MODULE_NAME = "MapModule";

	private MapModuleView mapModuleView;
	private MenuButton menuButton;
	
	/**
	 * Data displayed by {@code MapModuleView}
	 */
	private MapModuleData mapModuleData;
	
	@Override
	@OnInit
	public void init() {
		presenterId = "MapModulePresenter";
		modulesBroker.registerPresenter(this, MODULE_NAME);
		mapModuleView = new MapModuleView(context);
		mainSpaceManager.register(MapModuleView.getViewId(), mapModuleView);
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
			ModuleDataMessageContent content = (ModuleDataMessageContent) message.getMessage(ModuleDataMessage.class);
			if (content instanceof ResponseContent) {
				if (((ResponseContent) content).isResponseToChange() && !((ResponseContent) content).isPositive()) {
					informViewAboutFailure();
					return;
				}
					mapModuleData = (MapModuleData) ((ResponseContent) content).getState();
			}
			else {
				mapModuleData = (MapModuleData) ((StateChangedContent) content).getState();
			}
			updateView();
			mainSpaceManager.toTop(MapModuleView.getViewId());
			PresentersMessage presentersMessage = new PresentersMessage(ID, new MapDrawnContent());
			bus.sendMessage(presentersMessage);
		}
		
	}
	
	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(LoginDoneMessageContent.class) || m.carries(DrawMapContent.class)) {
			ModuleDataMessage message = new ModuleDataMessage(ID, new GetStateContent());
			modulesBroker.tellModule(message, MODULE_NAME);
		}
		
	}

	

	private void updateView() {
		// TODO Auto-generated method stub
		
	}

	private void informViewAboutFailure() {
		// TODO Auto-generated method stub
		
	}




}
