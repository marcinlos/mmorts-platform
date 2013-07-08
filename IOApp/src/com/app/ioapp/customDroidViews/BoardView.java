package com.app.ioapp.customDroidViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.app.ioapp.R;
import com.app.ioapp.config.ConfigException;
import com.app.ioapp.modules.Building;
import com.app.ioapp.modules.ITile;
import com.app.ioapp.modules.InfrastructureModule;
import com.app.ioapp.modules.InfrastructureModuleData;
import com.app.ioapp.modules.Module;
import com.app.ioapp.modules.Tile;
import com.app.ioapp.view.MainView;

public class BoardView extends AbstractModuleView{

	private static final String ID = "BoardView";
	private String moduleName;
	private List<ITile> fields;
	private int imageSize = 50;
	//private int mapSize = 25;
	private int mapWidth = 25;
	private int mapHeight = 25;
	//private InfrastructureModule map;
	private Map<String,Bitmap> cache;
	private boolean refresh_in_progress;
	private InfrastructureModuleData currentData;
	/**
	 * 0 - empty tile
	 * 1 - tile occupied by
	 */
	private ITile[][] virtual_map = new Tile[mapWidth][mapHeight];
	

	public BoardView(Context context) {
		super(context);
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(Color.GREEN);
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}
	
	private void addRefresher(){
		Timer timer = new Timer();
        timer.schedule(new ViewRefresher(this), 5, 1000);
	}
	
	
	private void cacheBitmap(Bitmap b){
		
	}
	
	
	@Override
	public void refresh(){
		Log.d(ID,"Refresh called");
		virtual_map = currentData.map;
		fields = new ArrayList<ITile>();
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				ITile tile = virtual_map[i][j];
				if(tile != null && tile.isValid()){
					//TODO wybieranie obrazka na podstawie budynku który tam jest a nie
					Building b = tile.getBuilding();
					if(b == null){
						Log.e(ID,"Tile is valid but no building is built here, hmm");
					}
					
					if (tile.getBitmap() == null){
						int id = getResources().getIdentifier(tile.getBitmapID(), "drawable", "com.app.ioapp");
						tile.setBitmap(BitmapFactory.decodeResource(getResources(),id));
					}
					fields.add(tile);
					Log.d(ID, "Added a tile");
					
					
				}
				else if(tile == null){
					fields.add(new Tile(
							BitmapFactory.decodeResource(getResources(),R.drawable.tile),i,j,1,1)
							);
					Log.d(ID, "Added empty tile at [" + i + "," + j + "]");
				}
			}
		}
		this.invalidate();
	}

	
	

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    setMeasuredDimension(mapWidth*imageSize, mapHeight*imageSize);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if(view.stateChanged(moduleName)){
			view.stateReceived(moduleName);
			currentData = view.getData(moduleName, InfrastructureModuleData.class);
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

	private void drawBasicElements(Canvas canvas, List<ITile> elements) {
		for (ITile element : elements) {
			canvas.drawBitmap(element.getBitmap(), element.getX()*imageSize,
					element.getY()*imageSize, null);
		}
	}

	@Override
	public void iWasClicked(float x, float y) {
		Log.d(ID,"I was clicked on: x=" + x + " y=" + y);
		float tmp = x/imageSize;
		int truex = (int)tmp;
		tmp = y/imageSize;
		int truey = (int)tmp;
		//map.click(truex,truey);
		//TODO - how to invoke specific methods? Pass functions defining object?
		
	}

	@Override
	public void init(List<String> modules, MainView v){
		view = v;
		moduleName = modules.get(0);
		currentData = view.getData(moduleName, InfrastructureModuleData.class);
		view.register(this.getClass(), moduleName);
		this.mapWidth = currentData.mapWidth;
		this.mapHeight = currentData.mapHeight;
		//TODO set tile size to somethin, based on phone specifics or somethin?
		addRefresher();
	}

}
