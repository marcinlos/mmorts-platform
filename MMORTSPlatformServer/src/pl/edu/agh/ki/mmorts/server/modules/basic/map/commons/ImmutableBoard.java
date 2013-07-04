package pl.edu.agh.ki.mmorts.server.modules.basic.map.commons;

/**
 * Immutable interface for Board to be used by client
 * 
 * @author drew
 *
 */
public interface ImmutableBoard {
	public FieldContent getAt(int row, int col);
}
