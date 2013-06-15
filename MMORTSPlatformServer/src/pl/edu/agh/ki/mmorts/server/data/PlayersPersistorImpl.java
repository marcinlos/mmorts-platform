package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;



public class PlayersPersistorImpl implements PlayersPersistor {
	
	@Inject
	private Database db;

	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public PlayerData receivePlayer(String name)
			throws IllegalArgumentException {
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

}
