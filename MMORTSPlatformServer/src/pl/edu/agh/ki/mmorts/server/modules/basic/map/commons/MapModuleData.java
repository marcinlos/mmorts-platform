package pl.edu.agh.ki.mmorts.server.modules.basic.map.commons;

import pl.edu.agh.ki.mmorts.server.modules.basic.map.Board;



/**
 * Class to persist in database. Now only {@link Board} is stored but
 * in the future here could be more
 * 
 * @author drew
 *
 */
public class MapModuleData {
		/**
		 * Board of player
		 */
		ImmutableBoard board;

		private MapModuleData(ImmutableBoard board) {
			this.board = board;
		}

		public ImmutableBoard getBoard() {
			return board;
		}
		
}
