package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ViewListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDoneMessageContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.ViewCreatedContent;
import pl.edu.agh.ki.mmorts.client.frontend.views.MenuButton;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
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
		modulesBroker.registerPresenter(this, name());
		bus.register(this);
		createView();
		emptySpace = BitmapFactory.decodeResource(context.getResources(),R.drawable.tile);
		
		menuButton = new MenuButton(context);
		menuButton.setView(mapModuleView);
		menuButton.setId(name());
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
				new PresentersMessage(name(), new ViewCreatedContent());
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
		if (!message.carries(ResponseContent.class)) {
			throw new IllegalArgumentException();
		}
		ResponseContent content = message.getMessage(ResponseContent.class);	
		if (content.isResponseToChange()) {
			informViewAboutAction(content.isPositive());
			return;
		}
		mapModuleData = (MapModuleData) content.getState();
		
		
	}
	
	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(LoginDoneMessageContent.class)) {
			mainSpaceManager.toTop(MapModuleView.getViewId());
		}
		
	}


	private void informViewAboutAction(boolean result) {
		mapModuleView.actionFinished(result);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void drawStuff(Canvas c) {
		ModuleDataMessage message = new ModuleDataMessage(ID, new GetStateContent());
		modulesBroker.tellModule(message, name());
		boolean[][] map = mapModuleData.getMap();
		Log.d(ID,"byc moze cos rysuje no...");
		for(int i=0;i<mapModuleData.getMapWidth();i++){
			for(int j=0;j<mapModuleData.getMapHeight();j++){
				if (!map[i][j]){ //if there is nothing there, map wants to draw the nothing
					//TODO check if changing around i and j is required, I never could tell
					c.drawBitmap(emptySpace, i*TILE_SIZE, j*TILE_SIZE, null);
					Log.d(ID,"rysuje na "+i*TILE_SIZE+", " + j*TILE_SIZE);
				}
			}
		}
		Log.d(ID,"porysowalem, powinno byc");
		
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void touchEvent(float x, float y) {
		return;
	}




}
