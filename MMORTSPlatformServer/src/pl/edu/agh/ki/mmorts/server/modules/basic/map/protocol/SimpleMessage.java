package pl.edu.agh.ki.mmorts.server.modules.basic.map.protocol;

public class SimpleMessage {
	private String playerName;

	public String getPlayerName() {
		return playerName;
	}

	public SimpleMessage(String playerName) {
		this.playerName = playerName;
	}
	
}
