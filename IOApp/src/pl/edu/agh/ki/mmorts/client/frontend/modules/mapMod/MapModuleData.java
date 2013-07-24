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
	 */
	private boolean[][] map;
	
	
}
