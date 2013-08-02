package pl.edu.agh.ki.mmorts.server.modules.builtin;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.data.PlayerData;
import pl.edu.agh.ki.mmorts.server.data.PlayerDataImpl;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;
import protocol.loginModule.LoginMessage;
import protocol.loginModule.Requests;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Module responsible for receiving login messages, authenticating players and
 * sending back player state when necessary.
 * 
 * @author los
 */
public class LoginModule extends ModuleBase {

    /** Need players manager for data retrieval */
    @Inject(optional = true)
    private PlayersPersistor players;

    @Inject(optional = true)
    @Named("login.number")
    private int number;
    
    @MessageMapping(Requests.AUTH_REQ)
    public void handleAuth(Message message, Context ctx) {
        logger().debug("Got auth message");
        if(!message.carries(LoginMessage.class)){
        	throw new IllegalArgumentException("Bad message received!");
        }
        LoginMessage loginMessage = message.get(LoginMessage.class);
		String playerRequest = loginMessage.getLogin();
		String passwordRequest = loginMessage.getPassword();
		
		PlayerData databasePlayerData = null;
		databasePlayerData = players.receivePlayer(playerRequest);
		if(databasePlayerData == null){
			logger().debug(String.format("Player %s created", playerRequest));
			players.createPlayer(new PlayerDataImpl(playerRequest, passwordRequest, playerRequest));
	        outputResponse(message, Requests.AUTH_SUCC);
	        return;
		}
		
		String request = null;
		if(databasePlayerData.getPasswordHash().equals(passwordRequest)){
			logger().debug(String.format("Player %s: login succedeed", playerRequest));
			request = Requests.AUTH_SUCC;
		}else{
			logger().debug(String.format("Player %s: login failed", playerRequest));
			request = Requests.AUTH_FAIL;
		}
		outputResponse(message, request);
    }
    
}
