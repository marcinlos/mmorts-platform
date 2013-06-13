package com.app.board;

import java.util.ArrayList;
import java.util.List;

import com.app.ioapp.R;

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
	}
	
	private void refreshFields(){
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
		fillVirtual(tiles);
		
		refreshFields();
		Log.d(ID, "Ending the setup");
	}
	
	/**
	 * 
	 * @param from_x
	 * @param from_y
	 * @param to_x
	 * @param to_y
	 * @throws SpaceOccupiedException
	 * @throws RuntimeException when there is no building to move, that should not happen
	 */
	public void moveBuilding(int from_x, int from_y, int to_x, int to_y) throws SpaceOccupiedException, RuntimeException{
		Tile t = (Tile) virtual_map[from_x][from_y];
		if(t == null)
			throw new RuntimeException();
		if(!isSpaceAvailable(to_x,to_y,t.getSize_x(),t.getSize_y())){
			throw new SpaceOccupiedException();
		}
		else{
			destroyBuilding(t);
			t.setX(to_x);
			t.setY(to_y);
			addBuilding(t);
			refreshFields();
		}
		
	}
	
	private void destroyBuilding(Tile t){
		for(int i = t.getSize_x();i>0;i--){
			for(int j = t.getSize_y();j>0;j--){
				virtual_map[t.getX()+i-1][t.getY()+j-1] = null;
			}
		}
		
	}
	
	private void addBuilding(Tile t) throws SpaceOccupiedException{
		if(!isSpaceAvailable(t.getX(),t.getY(),t.getSize_x(),t.getSize_y())){ //redundant check if from moveBuilding
			throw new SpaceOccupiedException(); 
		}
		int x = t.getX();
		int y = t.getY();
		int xs = t.getSize_x();
		int ys = t.getSize_y();
		for(int i = xs;i>0;i--){
			for(int j = ys;j>0;j--){
				if(i == 1 && j == 1){
					virtual_map[x][y] = t;
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
	public boolean isSpaceAvailable(int x, int y, int sx, int sy){
		for(int i=sx; i>0;i--){
			for(int j=sy;j>0;j--){
				if(virtual_map[x+i-1][y+j-1] != null)
					return false;
			}
		}
		return true;
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
