package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;



public class PlayersPersistorImpl implements PlayersPersistor {
	
	@Inject
	private Database db;

	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		db.createPlayer(player);
	}

	@Override
	public PlayerData receivePlayer(String name)
			throws IllegalArgumentException {
		return db.receivePlayer(name);
	}

	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		if (name.equals(player.getName())) {
			throw new IllegalArgumentException();
		}
		db.updatePlayer(name, player);
	}

	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		db.deletePlayer(name);
	}



}
