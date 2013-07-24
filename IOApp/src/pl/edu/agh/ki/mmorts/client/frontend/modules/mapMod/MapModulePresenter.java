package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDone;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.views.MenuButton;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.content.Context;
import android.util.Log;
import android.widget.Button;

import com.google.inject.Inject;

/**
 * Presenter for map module. The main presenter for {@code MapModuleView}
 *
 */
public class MapModulePresenter extends AbstractModulePresenter{
	private static final String ID = "MapModulePresenter";
	/**
	 * Name of module that I want to communicate with
	 */
	private String moduleName = "MapModule";
	
	@Inject
	private Context context;
	/**
	 * Not used
	 */
	@Inject
	private TopSpaceManager topSpaceManager;
	@Inject
	private MainSpaceManager mainSpaceManager;
	@Inject
	private ModulesBroker modulesBroker;
	
	private MapModuleView mapModuleView;
	private MenuButton menuButton;
	
	@Override
	public void init() {
		presenterId = "MapModulePresenter";
		mapModuleView = new MapModuleView(context);
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
	public void dataChanged(ModuleDataMessage data) {
		
		// TODO view.update(data)
		mapModuleView.postInvalidate();
		mainSpaceManager.toTop(mapModuleView.getViewId());
		
	}


	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(LoginDone.class)) {
			//It means that it's a request for data from server
			ModuleDataMessage message = new ModuleDataMessage(presenterId, null);
			modulesBroker.tellModule(message, moduleName);

		}
		
	}


}
