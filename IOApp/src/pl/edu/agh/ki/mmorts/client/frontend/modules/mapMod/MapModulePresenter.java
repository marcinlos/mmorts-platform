package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ViewListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.DrawMapContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDoneMessageContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.ViewCreatedContent;
import pl.edu.agh.ki.mmorts.client.frontend.views.MenuButton;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import pl.edu.agh.ki.mmorts.client.messages.StateChangedContent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.Button;

/**
 * Presenter for map module. The main presenter for {@code MapModuleView}
 *
 */
public class MapModulePresenter extends AbstractModulePresenter implements ViewListener{
	private static final String ID = "MapModulePresenter";
	

	/**
	 * Name of module that I want to communicate with
	 */
	private static final String MODULE_NAME = "MapModule";
	
	private static final int TILE_SIZE = 50;
	private Bitmap emptySpace; 

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
		bus.register(this);
		createView();
		emptySpace = BitmapFactory.decodeResource(context.getResources(),R.drawable.tile);
		
		menuButton = new MenuButton(context);
		menuButton.setView(mapModuleView);
		menuButton.setId(presenterId);
		menuButton.setMSM(mainSpaceManager);
		
		Log.d(ID, "context:");
		Log.d(ID, String.format("%s", context));
		Log.d(ID, "topSpaceManager:");
		Log.d(ID, String.format("%s", topSpaceManager));
		Log.d(ID, " mainSpaceManager:");
		Log.d(ID, String.format("%s", mainSpaceManager));
		Log.d(ID, "modulesBroker:");
		Log.d(ID, String.format("%s", modulesBroker));
	}
	
	private void createView() {
		mapModuleView = new MapModuleView(context);
		mapModuleView.addListener(this);
		mapModuleView.createListeners();
		
		mainSpaceManager.register(MapModuleView.getViewId(), mapModuleView);
		
		PresentersMessage createdMessage = 
				new PresentersMessage(presenterId, new ViewCreatedContent());
		bus.sendMessage(createdMessage);
		
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
			ModuleDataMessageContent content = (ModuleDataMessageContent) message.getMessage(ModuleDataMessageContent.class);
			if (content instanceof ResponseContent) {
				if (((ResponseContent) content).isResponseToChange() ) {
					informViewAboutAction(((ResponseContent) content).isPositive());
					return;
				}
					mapModuleData = (MapModuleData) ((ResponseContent) content).getState();
			}
			else {
				mapModuleData = (MapModuleData) ((StateChangedContent) content).getState();
			}
			updateView();
			mainSpaceManager.toTop(MapModuleView.getViewId());
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
		mapModuleView.postInvalidate(); //TODO toTop?
	}

	private void informViewAboutAction(boolean result) {
		mapModuleView.actionFinished(result);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawStuff(Canvas c) {
		// TODO Auto-generated method stub
		boolean[][] map = mapModuleData.getMap();
		for(int i=0;i<mapModuleData.getMapWidth();i++){
			for(int j=0;j<mapModuleData.getMapHeight();j++){
				if (!map[i][j]){ //if there is nothing there, map wants to draw the nothing
					//TODO check if changing around i and j is required, I never could tell
					c.drawBitmap(emptySpace, i*TILE_SIZE, j*TILE_SIZE, null);
				}
			}
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void touchEvent(float x, float y) {
		return;
	}




}
