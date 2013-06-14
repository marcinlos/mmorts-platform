package pl.edu.agh.ki.mmorts.server.data.stub;

import pl.edu.agh.ki.mmorts.server.data.PlayerData;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;

public class DummyPersistor implements PlayersPersistor {

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
