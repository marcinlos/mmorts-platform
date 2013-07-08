package pl.edu.agh.ki.mmorts.server.modules.basic.map.protocol;

import java.io.Serializable;

public class SimpleMessage implements Serializable{
	private String playerName;

	public String getPlayerName() {
		return playerName;
	}

	public SimpleMessage(String playerName) {
		this.playerName = playerName;
	}
	
}
