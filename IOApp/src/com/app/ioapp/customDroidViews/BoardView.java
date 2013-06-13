package com.app.ioapp.customDroidViews;

import java.util.ArrayList;
import java.util.List;

import com.app.board.SpaceOccupiedException;
import com.app.ioapp.R;
import com.app.ioapp.modules.Board;
import com.app.ioapp.modules.ITile;
import com.app.ioapp.modules.Tile;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;

public class BoardView extends View {

	private static final String ID = "BoardView";
	private ScaleGestureDetector detector;
	private List<ITile> fields;
	private static final int imageSize = 50;
	private static final int mapSize = 25;
	private Board map;
	/**
	 * 0 - empty tile
	 * 1 - tile occupied by
	 */
	private ITile[][] virtual_map = new Tile[mapSize][mapSize];
	

	public BoardView(Context context) {
		super(context);
		Log.d(ID, "created1");
	}

	public BoardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setBackgroundColor(Color.GREEN);
		Log.d(ID, "created2");
	}

	public BoardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Log.d(ID, "created3");
	}
	
	public void setMap(Board board){
		this.map = board;
	}
	
	/*
	public void fillVirtual(List<ITile> tiles){
		for (ITile tile : tiles) {
			int x = tile.getX();
			int y = tile.getY();
			int xs = tile.getSize_x();
			int ys = tile.getSize_y();
			Log.d(ID, "Tile loaded: x=" + x + " y= " + y);
			for(int i = xs;i>0;i--){
				for(int j = ys;j>0;j--){
					if(i == 1 && j == 1){
						virtual_map[x][y] = tile;
						Log.d(ID, "Added tile to virtual at [" + x + "," + y + "]");
					}
					else{
						Tile temp = new Tile("",0,0,0,0);
						temp.validity(false);
						virtual_map[x+i-1][y+j-1] = temp;
					}
						
				}
			}
		}
	}*/
	
	private void refreshFields(){
		virtual_map = map.getMap();
		fields = new ArrayList<ITile>();
		for(int i=0;i<mapSize;i++){
			for(int j=0;j<mapSize;j++){
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

	/**
	 * I'm assuming we only use BoardView.imageSize px tiles
	 * This method should be invoked once by init from server state
	 * It is required to be a valid board or bad things might happen
	 * @param tiles
	 *            will contain either ID of image or a bitmap along with x and y
	 *            coords and sizes (0,0 being top left corner and numbers rising
	 *            towards SE) coordinates given for top left corner if size > 1
	 */
	public void setupFields(List<ITile> tiles) {
		Log.d(ID, "Starting the setup");
		map.fillVirtual(tiles);
		
		refreshFields();
		Log.d(ID, "Ending the setup");
	}
	
	public void moveBuilding(int from_x, int from_y, int to_x, int to_y) throws SpaceOccupiedException, RuntimeException{
		map.moveBuilding(from_x, from_y, to_x, to_y);
		refreshFields();
	}
	private void destroyBuilding(Tile t){
		map.destroyBuilding(t);
		refreshFields();
	}
	private void addBuilding(Tile t) throws SpaceOccupiedException{
		map.addBuilding(t);
		refreshFields();
	}
	

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    setMeasuredDimension(mapSize*50, mapSize*50);
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
