package protocol.mapModule.helpers;

import protocol.mapModule.SimpleMessage;


public class DetailedMessage extends SimpleMessage {
	private int row;
	private int col;
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	
	public DetailedMessage(String player,int row, int col) {
		super(player);
		this.row = row;
		this.col = col;
	}

}