package pl.edu.agh.ki.mmorts.server.modules.basic.map.commons;

import java.io.Serializable;

/**
 * Immutable interface for Board to be used by client
 * 
 * @author drew
 *
 */
public interface ImmutableBoard extends Serializable{
	public FieldContent getAt(int row, int col);
}
