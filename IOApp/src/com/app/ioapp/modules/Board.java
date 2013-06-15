package com.app.ioapp.modules;

import java.util.List;
import java.util.Properties;

import android.util.Log;

import com.app.board.SpaceOccupiedException;
import com.app.ioapp.customDroidViews.BoardView;

public class Board {
	
	private final static String ID = "Board";
	private Properties pr;
	//private int mapSize = 35;
	private int mapHeight = 25;
	private int mapWidth = 25;
	private ITile[][] map;
	private BoardView view;
	
	public ITile[][] getMap(){
		return map;
	}
	
	public Board(Properties p, BoardView v){
		pr = p;
		if(p != null){
			Integer tmp1 = Integer.valueOf((String) p.get("boardHeight"));
			Integer tmp2 = Integer.valueOf((String) p.get("boardWidth"));
			if(tmp1 != null)
				mapHeight = tmp1;
			if(tmp2 != null)
				mapWidth = tmp2;
		}
		map = new Tile[mapWidth][mapHeight];
		view = v;
		view.setMap(this);
	}
	
	public int getWidth(){
		return mapWidth;
	}
	public int getHeight(){
		return mapHeight;
	}
	public Properties getProperties(){
		return pr;
	}
	
	/**
	 * I'm assuming we only use {@link #BoardView.imageSize} px tiles
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
		
		view.refresh();
		Log.d(ID, "Ending the setup");
	}
	
	/**
	 * Used by the setup to put on virtual board representation tiles sent from server
	 * and to fill spots that were left with blank fields.
	 * @param tiles meaningfull tiles to add from server state of map, see {@link #setupFields(List)}
	 */
	private void fillVirtual(List<ITile> tiles){
		for (ITile tile : tiles) {
			int x = tile.getX();
			int y = tile.getY();
			int xs = tile.getSize_x();
			int ys = tile.getSize_y();
			Log.d(ID, "Tile loaded: x=" + x + " y= " + y);
			for(int i = xs;i>0;i--){
				for(int j = ys;j>0;j--){
					if(i == 1 && j == 1){
						map[x][y] = tile;
						Log.d(ID, "Added tile to virtual at [" + x + "," + y + "]");
					}
					else{
						Tile temp = new Tile("",0,0,0,0);
						temp.validity(false);
						map[x+i-1][y+j-1] = temp;
					}
						
				}
			}
		}
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
		Tile t = (Tile) map[from_x][from_y];
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
		}
		view.refresh();
		
	}
	public void destroyBuilding(Tile t){
		for(int i = t.getSize_x();i>0;i--){
			for(int j = t.getSize_y();j>0;j--){
				map[t.getX()+i-1][t.getY()+j-1] = null;
			}
		}
		view.refresh();
		
	}
	
	public void addBuilding(Tile t) throws SpaceOccupiedException{
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
					map[x][y] = t;
					Log.d(ID, "Added tile to virtual at [" + x + "," + y + "]");
				}
				else{
					Tile temp = new Tile("",0,0,0,0);
					temp.validity(false);
					map[x+i-1][y+j-1] = temp;
				}
					
			}
		}
		view.refresh();
	}
	public boolean isSpaceAvailable(int x, int y, int sx, int sy){
		for(int i=sx; i>0;i--){
			for(int j=sy;j>0;j--){
				if(map[x+i-1][y+j-1] != null)
					return false;
			}
		}
		return true;
	}
	

}
