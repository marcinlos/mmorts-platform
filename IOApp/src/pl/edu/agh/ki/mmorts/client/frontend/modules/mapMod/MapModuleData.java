package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

/**
 * Represents a content of a message sent between {@code MapModulePresenter} and {@code MapModule}
 * If {@code MapModule} gets a message with the content of {@code null} then it's a request for data.
 */
public class MapModuleData {

	private int mapHeight;
	private int mapWidth;
	/**
	 * Stores information about which tile is occupied.
	 * {@code true} if it is occupied
	 * 
	 * If you also wanted to have a state of "nothing stands here but you can't build here" or something
	 * (like space buing in Crime City) this map should probably have more states than two.
	 */
	private boolean[][] map;
	
	
	public int getMapHeight() {
		return mapHeight;
	}
	public int getMapWidth() {
		return mapWidth;
	}
	public boolean[][] getMap() {
		return map;
	}
	public void setMapHeight(int mapHeight) {
		this.mapHeight = mapHeight;
	}
	public void setMapWidth(int mapWidth) {
		this.mapWidth = mapWidth;
	}
	public void setMap(boolean[][] map) {
		this.map = map;
	}
	
	
}
