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
import com.app.ioapp.modules.ITile;
import com.app.ioapp.modules.InfrastructureModule;
import com.app.ioapp.modules.Module;
import com.app.ioapp.modules.Tile;

public class BoardView extends AbstractModuleView{

	private static final String ID = "BoardView";
	private List<ITile> fields;
	private int imageSize = 50;
	//private int mapSize = 25;
	private int mapWidth = 25;
	private int mapHeight = 25;
	private InfrastructureModule map;
	private Map<String,Bitmap> cache;
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
        timer.schedule(new ViewRefresher(this), 5, 10000);
	}
	
	@Override
	public void setModuleImpl(Module module){
		if(!(module instanceof InfrastructureModule)){
			Log.e(ID,"Wrong module uset for init");
			throw new ConfigException();
		}
		InfrastructureModule board = (InfrastructureModule) module;
		this.map = board;
		this.mapWidth = board.getWidth();
		this.mapHeight = board.getHeight();
		//TODO not sure if that's how it'll work
		Properties p = board.getProperties();
		if(p != null){
			Integer tmp = Integer.valueOf((String) p.get("InfrastructureModule.tileSize"));
			if(tmp != null){
				imageSize = tmp;
			}
		}
		addRefresher();
	}
	
	private void cacheBitmap(Bitmap b){
		
	}
	
	
	@Override
	public void refresh(){
		Log.d(ID,"Refresh called");
		virtual_map = map.getMap();
		fields = new ArrayList<ITile>();
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				ITile tile = virtual_map[i][j];
				if(tile != null && tile.isValid()){
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
		super.onDraw(canvas);
		Log.d(ID, "Drawing stuff");
		canvas.save();
		if (fields != null) {
			Log.d(ID, "Drawing what needs to be drawn");
			drawBasicElements(canvas, fields);
		}
		canvas.restore();
	}

	private void drawBasicElements(Canvas canvas, List<ITile> elements) {
		for (ITile element : elements) {
			canvas.drawBitmap(element.getBitmap(), element.getX()*imageSize,
					element.getY()*imageSize, null);
		}
	}

}
