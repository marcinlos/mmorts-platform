package pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ViewListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleView;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.ViewCreatedContent;
import pl.edu.agh.ki.mmorts.client.messages.ChangeStateContent;
import pl.edu.agh.ki.mmorts.client.messages.GetStateContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.messages.ResponseContent;
import protocol.buildingsModule.BuildingData;
import protocol.buildingsModule.BuildingInstance;
import protocol.buildingsModule.BuildingModuleData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
/**
 * Presenter for building module. It is not the main presenter for any view.
 * It does not have menu button
 *
 */
public class BuildingModulePresenter extends AbstractModulePresenter implements ViewListener{
	private static final String ID = "BuildingModulePresenter";
	/**
	 * Name of module that I want to communicate with
	 */
	private static final String MODULE_NAME = "BuildingModule";
	private static final int TILE_SIZE = 50;
	
	private static final Map<String, Bitmap> buildingImages = new HashMap<String,Bitmap>();
	
	
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
		//buildingModuleData = new BuildingModuleData();
		
		buildingImages.put(BuildingTypes.PRZEDSZKOLE.getCaption(), BitmapFactory.decodeResource(context.getResources(),R.drawable.tile_orange));
		buildingImages.put(BuildingTypes.MCDONALDS.getCaption(), BitmapFactory.decodeResource(context.getResources(),R.drawable.tile_fill));
		buildingImages.put(BuildingTypes.CMENTARZ.getCaption(), BitmapFactory.decodeResource(context.getResources(),R.drawable.tile_cross));
		
		modulesBroker.registerPresenter(this, MODULE_NAME);
		bus.register(this);

		
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
		buildingModuleData = (BuildingModuleData) content.getState();
		
	}



	@Override
	public void gotMessage(PresentersMessage m) {
		if (m.carries(ViewCreatedContent.class)) {
			mapModuleView = (MapModuleView) mainSpaceManager.getViewById(MapModuleView.getViewId());
			mapModuleView.addListener(this);
		}
		Log.d(ID,"got message, ignored it");
	
	}
	
	private void informViewAboutAction(boolean result) {
		mapModuleView.actionFinished(result);
	}


	@Override
	public void drawStuff(Canvas c) {
		if(buildingModuleData != null)
			Log.e(ID,"size: " +buildingModuleData.getBuildings().size());
		ModuleDataMessage message = new ModuleDataMessage(ID, new GetStateContent());
		modulesBroker.tellModule(message, MODULE_NAME);
		Log.e(ID,"size: " +buildingModuleData.getBuildings().size());
		for(BuildingInstance b : buildingModuleData.getBuildings()){
			String name = b.getData().getName();
			Bitmap bit = buildingImages.get(name);
			c.drawBitmap(bit, b.getColumn()*TILE_SIZE, b.getRow()*TILE_SIZE, null);
			Log.d(ID,"rysuje na "+b.getColumn()*TILE_SIZE+", " + b.getRow()*TILE_SIZE);
		}
		Log.d(ID,"bleh");
	}


	@Override
	public void touchEvent(float x, float y) {
		Log.d(ID,"I was clicked on: x=" + x + " y=" + y);
		float tmp = x/TILE_SIZE;
		int truex = (int)tmp;
		tmp = y/TILE_SIZE;
		int truey = (int)tmp;
		boolean exists;
		if ( exists = checkIfBuildingExists(truex, truey)) {
			buildingModuleData.removeBuilding(truex, truey);
		}
		else {
			BuildingInstance building = new BuildingInstance();
			BuildingData data = new BuildingData(getBuildingType().getCaption(), 1, 1);
			building.setColumn(truex);
			building.setRow(truey);
			building.setData(data);
					
			buildingModuleData.addBuilding(building);
		}
		Log.e(ID,"x: " + truex + " y: " + truey + "exists?: " + exists);
		ChangeStateContent content = new ChangeStateContent(buildingModuleData);
		ModuleDataMessage message = new ModuleDataMessage(name(), content);
		modulesBroker.tellModule(message, name());
		Log.e(ID,"size: " +buildingModuleData.getBuildings().size());
		
	}
	
	private boolean checkIfBuildingExists(int x, int y) {
		for (BuildingInstance building : buildingModuleData.getBuildings()) {
			if (building.getColumn() == x) {
				if (building.getRow() == y) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	private BuildingTypes getBuildingType() {
		Random r = new Random();
		int type = r.nextInt(3); 
		if (type == 0) {
			return BuildingTypes.PRZEDSZKOLE;
		}
		else if (type == 1) {
			return BuildingTypes.MCDONALDS;
		}
		else {
			return BuildingTypes.CMENTARZ; 
		}
		
	}
}
