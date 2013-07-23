package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
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
	
	@Inject
	private Context context;
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
		Log.d(ID, "context:");
		Log.d(ID, String.format("%s", context));
		Log.d(ID, "topSpaceManager:");
		Log.d(ID, String.format("%s", topSpaceManager));
		Log.d(ID, " mainSpaceManager:");
		Log.d(ID, String.format("%s", mainSpaceManager));
		Log.d(ID, "modulesBroker:");
		Log.d(ID, String.format("%s", modulesBroker));
		menuButton = new MenuButton(context);
		mapModuleView = new MapModuleView(context);
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
		// TODO Auto-generated method stub
		mapModuleView.postInvalidate();
		
		
	}


	@Override
	public void gotMessage(PresentersMessage m) {
/*		if (m.carries(LoginDone.class)) {
			PresentersMessage message
			mainSpaceManager.toTop(mapModuleView.getViewId());
		}*/
		
	}


}
