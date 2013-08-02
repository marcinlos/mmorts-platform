package protocol.mapModule;

import java.io.Serializable;

import protocol.mapModule.helpers.Board;
import protocol.mapModule.helpers.ImmutableBoard;





/**
 * Class to persist in database. Now only {@link Board} is stored but
 * in the future here could be more
 * 
 * @author drew
 *
 */
public class MapModuleData implements Serializable{
		/**
		 * Board of player
		 */
		Board board;

		public MapModuleData(Board board) {
			this.board = board;
		}

		public ImmutableBoard getBoard() {
			return board;
		}
		
}
