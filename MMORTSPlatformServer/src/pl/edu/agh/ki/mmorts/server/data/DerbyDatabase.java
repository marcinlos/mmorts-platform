package pl.edu.agh.ki.mmorts.server.data;



import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.server.core.ModuleTable;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;



/**
 * @author drew
 *
 */
public class DerbyDatabase implements Database {
	
	@Inject
	TransactionManager tm;
	
	public static int MAX_POOL = 10;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PlayerData receivePlayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object receiveBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBinding(String modulename, String playerName, Object o)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteBinding(String modulename, String playerName)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

}
