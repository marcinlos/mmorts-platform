package pl.edu.agh.ki.mmorts.server.modules.basic.map.commons;

import java.io.Serializable;

import pl.edu.agh.ki.mmorts.server.modules.basic.map.Board;



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
