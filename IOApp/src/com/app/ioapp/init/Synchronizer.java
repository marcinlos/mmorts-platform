package com.app.ioapp.init;

import android.util.Log;

import com.app.ioapp.communication.Gateway;
import com.app.ioapp.data.PlayersContext;
import com.app.ioapp.data.State;

/**
 * A class used to synchronize local state with the state on the server. 
 * It enables also synchronizing context which will not be so often.
 *
 */
public class Synchronizer {
	
	public static final String ID = "Synchronizer";
	
	/**
	 * Dispatcher which enables communication with server
	 */
	private Gateway dispatcher;
	/**
	 * Reference to context. It will be changed by method {@code synchronizeContext()}
	 */
	private PlayersContext context;
	/**
	 * Reference to state. It will be changed by method {@code synchronizeState()}
	 */
	private State state;
	
	/**
	 * @param dispatcher
	 * @param context
	 * @param state
	 */
	public Synchronizer(Gateway dispatcher, PlayersContext context, State state) {
		this.dispatcher = dispatcher;
		this.context = context;
		this.state = state;
	}

	/**
	 * Synchronizes state with server.
	 */
	public void synchronizeState() {
		Log.e(ID, "Synchronizing state started");
	}
	
	/**
	 * Synchronizes context with server.
	 */
	public void synchronizeContext() {
		Log.e(ID, "Synchronizing context started");
	}

}
