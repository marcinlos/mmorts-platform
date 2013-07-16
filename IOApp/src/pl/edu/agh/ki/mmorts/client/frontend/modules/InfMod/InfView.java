package pl.edu.agh.ki.mmorts.client.frontend.modules.InfMod;

import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.ki.mmorts.client.frontend.modules.Tile;
import pl.edu.agh.ki.mmorts.client.frontend.modules.InfMod.InfrastructureModulePresenter.Controller;

import com.app.ioapp.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

public class InfView extends View{

	private static final String ID = InfView.class.getName();
	
	private int imageSize = 50;
	
	private Controller mapState;

	private InfrastructureModulePresenter presenter;
	
	public InfView(Context context, InfrastructureModulePresenter presenter) {
		super(context);
		this.presenter = presenter;
		mapState = presenter.getActualMapState();
		//setupBoard();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBuildings(canvas);
		drawEmpty(canvas);
	}


	private void drawEmpty(Canvas canvas) {
		
		Log.d(ID, "Drawing empties");
		for(int x = 0; x<mapState.getMapWidth(); ++x ){
			for(int y = 0; y<mapState.getMapHeight(); ++y){
				if(mapState.checkAvailability(x, y)){
					canvas.drawBitmap(mapState.getFreeBitmap(), x*imageSize,
							y*imageSize, null);
				}else{
					Log.d(ID, "Not at: " + x+ "  " + y);
				}
			}
		}
		
		
	}

	private void drawBuildings(Canvas canvas) {
		Log.d(ID, "Drawing buildings");
		List<PlacedBuilding> placed = mapState.getPlacedBuildings();
		
		for(PlacedBuilding placedB : placed){
			int xSize = mapState.getMapWidth();
			//int ySize = mapState.getMapHeight();
			int counter=0;
			for(int x = placedB.getX(); x<placedB.getX() + placedB.getBuilding().getSizeX(); ++x){
				for(int y = placedB.getY(); y<placedB.getY() + placedB.getBuilding().getSizeY(); ++y){
					Log.d(ID, "Drawing building at: " + x+ "  "  + y);
					canvas.drawBitmap(placedB.getBuilding().getBitmaps().get(counter/xSize + counter%xSize), x*imageSize,
						y*imageSize, null);
					++counter;
				}
			}
			
			
		}
	}

	/*
	private void setupBoard() {
		Log.d(ID, "setupBoard - it's debug only procedure!");
		Tile tile1 = new Tile(BitmapFactory.decodeResource(getResources(),
				R.drawable.tile), 0, 0, 1, 1);
		currentFields.add(tile1);
		Tile tile2 = new Tile(BitmapFactory.decodeResource(getResources(),
				R.drawable.tile_orange), 2, 2, 1, 1);
		currentFields.add(tile2);
		Tile tile3 = new Tile(BitmapFactory.decodeResource(getResources(),
				R.drawable.tile_1x2), 5, 3, 1, 2);
		currentFields.add(tile3);
	}
/*
	
	@Override
	public void onDraw(Canvas canvas) {
		if(modulesBroker.stateChanged(moduleName)){
			modulesBroker.stateReceived(moduleName);
			currentData = modulesBroker.getData(moduleName, InfrastructureModuleData.class);
			refresh();
		}
		super.onDraw(canvas);
		Log.d(ID, "Drawing stuff");
		//canvas.save();
		if (fields != null) {
			Log.d(ID, "Drawing what needs to be drawn");
			drawBasicElements(canvas, fields);
		}
		//canvas.restore();
	}
*/
	private void drawBasicElements(Canvas canvas, List<ITile> elements) {
		for (ITile element : elements) {
			canvas.drawBitmap(element.getBitmap(), element.getX()*imageSize,
					element.getY()*imageSize, null);
		}
	}
}
