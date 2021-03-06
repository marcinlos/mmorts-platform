package protocol.mapModule.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * This class represents board(map) as polygon mesh of squares. It cannot be
 * instantiated directly
 * 
 * @author drew
 * 
 */
public class Board implements ImmutableBoard {

	private static final BoardFactory BoardFactory = new BoardFactory();
	/**
	 * Map representation as list of lists of {@link FieldContent}
	 */
	private List<List<FieldContent>> fieldContents;

	/**
	 * Started map to properly do realease on the field
	 */
	private List<List<FieldContent>> startingContents;

	/**
	 * Only for internal use!
	 * 
	 * @param fieldContents
	 */
	private Board(List<List<FieldContent>> fieldContents) {
		this.fieldContents = fieldContents;
		this.startingContents = makeShallowCopy(fieldContents);
	}

	private List<List<FieldContent>> makeShallowCopy(
			List<List<FieldContent>> fieldContents) {
		List<List<FieldContent>> shallowCopy = new ArrayList<List<FieldContent>>();
		Iterator<List<FieldContent>> rowsIterator = fieldContents.iterator();
		while (rowsIterator.hasNext()) {
			shallowCopy.add(new ArrayList<FieldContent>(rowsIterator.next()));
		}
		return shallowCopy;
	}

	public FieldContent getAt(int row, int col) {
		return fieldContents.get(row).get(col);
	}

	public void putAt(int row, int col) {
		fieldContents.get(row).set(col, FieldContent.O);
	}

	public void realeaseAt(int row, int col) {
		fieldContents.get(row).set(col, startingContents.get(row).get(col));
	}
	
	public int getRowsSize(){
		return fieldContents.size();
	}
	public int getColsSize(){
		return fieldContents.get(0).size();
	}

	/**
	 * Factory class for producing predefined boards.
	 * 
	 * @author drew
	 * 
	 */
	public static class BoardFactory {

		private static final int ROAD_COL = 10;
		private static final int ROAD_ROW = 10;

/**
		 * Create standard board with given size constructed typically. That means that
		 * wherever {@code row % 10 == 9 && column % 10 == 9) there is road.
		 * 
		 * @param rows
		 * 		rows size of map
		 * @param cols
		 * 		columns size of map
		 * @return
		 * 		created {@link Board}
		 */
		public Board getTypicalBoard(int rows, int cols) {
			List<List<FieldContent>> newBoard = new ArrayList<List<FieldContent>>();
			for (int r = 0; r < rows; ++r) {
				newBoard.add(new ArrayList<FieldContent>());
				for (int c = 0; c < cols; ++c) {
					newBoard.get(r)
							.add((isRoadBecauseOfColumn(c) || isRoadBecauseOfRow(r)) ? FieldContent.R
									: FieldContent.G);
				}
			}
			return new Board(newBoard);
		}
		public Board getEasyBoard(int rows, int cols) {
			List<List<FieldContent>> newBoard = new ArrayList<List<FieldContent>>();
			for (int r = 0; r < rows; ++r) {
				newBoard.add(new ArrayList<FieldContent>());
				for (int c = 0; c < cols; ++c) {
					newBoard.get(r).add(FieldContent.G);
				}
			}
			return new Board(newBoard);
		}

		private boolean isRoadBecauseOfRow(int r) {
			return r % ROAD_ROW == ROAD_ROW - 1;
		}

		private boolean isRoadBecauseOfColumn(int c) {
			return c % ROAD_COL == ROAD_COL - 1;
		}
	}

	public static BoardFactory getFactory() {
		return BoardFactory;
	}
}
