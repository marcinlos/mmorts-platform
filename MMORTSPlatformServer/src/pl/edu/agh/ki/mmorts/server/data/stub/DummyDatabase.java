package pl.edu.agh.ki.mmorts.server.data.stub;

import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.PlayerData;

public class DummyDatabase implements Database {

    public DummyDatabase() {
        // TODO Auto-generated constructor stub
    }

    @Override
    @OnInit
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
    @OnShutdown
    public void shutdown() {
        // TODO Auto-generated method stub
        
    }

}
