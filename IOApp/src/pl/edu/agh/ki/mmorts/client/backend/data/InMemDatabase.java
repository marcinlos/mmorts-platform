package pl.edu.agh.ki.mmorts.client.backend.data;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import android.util.Log;

/**
 * Implementation of {@link Database} which uses Maps to storing information.
 * For client usage, which shouldn't be more than remembering state of player
 * which client is playing as currently, Maps implementation is enough.
 * 
 * No situation when client tries to remember more data is considered!
 * 
 *
 */
public class InMemDatabase implements Database {

	private static final String ID = InMemDatabase.class.getName();

	
	/**
	 * Represents players table containing player's information
	 */
	private Map<String, PlayerData> playersTable;
	
	/**
	 * Represents tables containing player's information for custom modules for all modules
	 */
	private Map<String, Map<String, Object>> modulesDataTable;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@OnInit
	public void init() {
		Log.d(ID, "Initialization started");
		playersTable = new HashMap<String, PlayerData>();
		modulesDataTable = new HashMap<String, Map<String, Object>>();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		Log.d(ID, "Creating new player: " + player.getName());
		if (playersTable.containsKey(player.getName())) {
			Log.e(ID, "Player already exists");
			throw new IllegalArgumentException();
		}
		playersTable.put(player.getName(), player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerData receivePlayer(String name) {
		Log.d(ID, "Receiving player: " + name);
		return playersTable.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		Log.d(ID, "Updating player: " + name);
		if (playersTable.containsKey(name) == false) {
			Log.e(ID, "Player: " + name + " does not exists");
			throw new IllegalArgumentException();
		}
		playersTable.put(player.getName(), player);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		Log.d(ID, "Deleting player: " + name);
		if (playersTable.containsKey(name) == false) {
			Log.e(ID, "Player: " + name + " does not exists");
			throw new IllegalArgumentException();
		}
		playersTable.remove(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		Log.d(ID, "Creating binding in module: " + moduleName + 
				" between object: " + o.toString() + " and player: " + playerName);
		Map<String, Object> moduleMap;
		if(modulesDataTable.containsKey(moduleName)==false){
			moduleMap = new HashMap<String, Object>();
			modulesDataTable.put(moduleName, moduleMap);
		}else{
			moduleMap = modulesDataTable.get(moduleName);
		}
		if(moduleMap.containsKey(playerName)){
			Log.e(ID, "Player is already bound");
			throw new IllegalArgumentException();
		}
		moduleMap.put(playerName, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object receiveBinding(String moduleName, String playerName) {
		Log.d(ID, "Receiving binding for player: " + playerName + " in module: " + moduleName);
		Map<String, Object> moduleMap = modulesDataTable.get(moduleName);
		if(moduleMap==null){
			return null;
		}
		return moduleMap.get(playerName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		Log.d(ID, "Updating binding for player: " + playerName + " in module: " + moduleName);
		Map<String, Object> moduleMap = modulesDataTable.get(moduleName);
		if(moduleMap==null || moduleMap.containsKey(playerName)==false){
			Log.e(ID, "Updating failed");
			throw new IllegalArgumentException();
		}
		moduleMap.put(playerName, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		Log.d(ID, "Deleting binding for player: " + playerName + " in module: " + moduleName);
		Map<String, Object> moduleMap = modulesDataTable.get(moduleName);
		if(moduleMap==null || moduleMap.containsKey(playerName)==false){
			throw new IllegalArgumentException();
		}
		moduleMap.remove(moduleName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void shutdown() {
		// skipped, nothing to do!
	}
	
}
