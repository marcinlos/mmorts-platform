package pl.edu.agh.ki.mmorts.client.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link Database} which uses Maps to storing information.
 * For client usage, which shouldn't be more than remembering state of player
 * which client is playing as currently, Maps implementation is enough.
 * 
 * No situation when client tries to remember more data is considered!
 * 
 * @author drew
 *
 */
public class InMemDatabase implements Database {

	// TODO logging, or i will create it while i'll download ADT
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
	public void init() {
		playersTable = new HashMap<String, PlayerData>();
		modulesDataTable = new HashMap<String, Map<String, Object>>();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		if (playersTable.containsKey(player.getName())) {
			throw new IllegalArgumentException();
		}
		playersTable.put(player.getName(), player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerData receivePlayer(String name) {
		return playersTable.get(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		if (playersTable.containsKey(name) == false) {
			throw new IllegalArgumentException();
		}
		playersTable.put(player.getName(), player);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		if (playersTable.containsKey(name) == false) {
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
		Map<String, Object> moduleMap;
		if(modulesDataTable.containsKey(moduleName)==false){
			moduleMap = new HashMap<String, Object>();
			modulesDataTable.put(moduleName, moduleMap);
		}else{
			moduleMap = modulesDataTable.get(moduleName);
		}
		if(moduleMap.containsKey(playerName)){
			throw new IllegalArgumentException();
		}
		moduleMap.put(playerName, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object receiveBinding(String moduleName, String playerName) {
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
		Map<String, Object> moduleMap = modulesDataTable.get(moduleName);
		if(moduleMap==null || moduleMap.containsKey(playerName)==false){
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
