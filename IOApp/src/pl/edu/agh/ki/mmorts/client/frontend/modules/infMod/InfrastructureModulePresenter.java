package pl.edu.agh.ki.mmorts.client.frontend.modules.infMod;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.ki.mmorts.client.frontend.activities.RunningActivity;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod.Building;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;


public class InfrastructureModulePresenter implements ModulePresenter {

	private Controller controller = new Controller();
	
	private InfView moduleView;
	
	@Override
	public boolean hasMenuButton() {
		return true;
	}

	public Button getButton(Context context) {
		Button button = new Button(context);
		button.setText("Infrastructure module");
		return button;
	}

	public View getMainModuleView(Context context, Activity activ,
			ViewGroup parent) {
		if(moduleView==null){
			moduleView = new InfView(context, this);
			moduleView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(event.getAction()==MotionEvent.ACTION_DOWN/* && event.getButtonState() == MotionEvent.BUTTON_PRIMARY*/){
						controller.setUpBuilding((int)event.getX()/50, (int)event.getY()/50, Building.getCrossy());
						moduleView.invalidate();
					}/*
					if(event.getAction()==MotionEvent.ACTION_DOWN && event.getButtonState() == MotionEvent.BUTTON_SECONDARY){
						controller.setUpBuilding((int)event.getX()/50, (int)event.getY()/50, Building.getOrangy());
						moduleView.invalidate();
					}*/
					return true;
				}
			});
		}
		
		/*
		LinearLayout horizontal = new LinearLayout(context);
		horizontal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		Button button = new Button(context);
		button.setText("BIGDEAL");
		button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		horizontal.addView(button);
		// return horizontal;*/
		return moduleView;
	}

	public boolean isMainView() {
		return true;
	}
	
	public Controller getActualMapState(){
		return controller;
	}

	static class Controller {
		private static final String ID = Controller.class.getName();
		
		private int mapHeight = 40;
		private int mapWidth = 40;

		private List<PlacedBuilding> placedBuildings;
		private List<List<Boolean>> rawMap;	 //true if free
		
		private Bitmap freeBitmap;

		public Controller() {
			placedBuildings = new ArrayList<PlacedBuilding>();
			rawMap = new ArrayList<List<Boolean>>();
			for (int x = 0; x < mapWidth; ++x) {
				rawMap.add(new ArrayList<Boolean>());
				for (int y = 0; y < mapHeight; ++y) {
					rawMap.get(x).add(true);
				}
			}
//			freeBitmap = BitmapFactory.decodeResource(RunningActivity.getContext().getResources(), R.drawable.tile_fill);
		}

		public Bitmap getFreeBitmap(){
			return freeBitmap;
		}
		
		public boolean checkAvailability(int x, int y) {
			return rawMap.get(x).get(y);
		}

		public boolean setUpBuilding(int x, int y, Building building) {
			Log.d(ID, "Building set up at: "+ x + "  " + y);
			for (int xi = x; xi < x+building.getSizeX(); ++xi) {
				if (!checkAvailability(xi, y)) {
					return false;
				}
			}
			for (int yi = y; yi < x+building.getSizeY(); ++yi) {
				if (!checkAvailability(x, yi)) {
					return false;
				}
			}

			placedBuildings.add(new PlacedBuilding(building, x, y));

			for (int xi = x; xi < y+ building.getSizeX(); ++xi) {
				setField(xi, y, false);
			}
			for (int yi = y; yi < y+ building.getSizeY(); ++yi) {
				setField(x, yi, false);
			}

			return true;
		}

		public boolean removeBuilding(PlacedBuilding building){
			if(!placedBuildings.remove(building)){
				return false;
			}
			for (int xi = building.getX(); xi < building.getBuilding().getSizeX(); ++xi) {
				setField(xi, building.getY(), true);
			}
			for (int yi = building.getY(); yi  < building.getBuilding().getSizeY(); ++yi) {
				setField(building.getX(), yi, true);
			}
			return true;
		}
		
		
		public List<PlacedBuilding> getPlacedBuildings() {
			return placedBuildings;
		}

		private void setField(int x, int y, boolean val) {
			rawMap.get(x).set(y, val);
		}

		public int getMapHeight() {
			return mapHeight;
		}

		public int getMapWidth() {
			return mapWidth;
		}
		
		
		

	}

	@Override
	public Button getMenuButton() {
		// TODO Auto-generated method stub
		return null;
	}


}
