package com.app.ioapp.init;

import com.app.ioapp.data.Context;
import com.app.ioapp.data.State;
import com.app.ioapp.dispatcher.Dispatcher;

public class Synchronizer {
	
	private Dispatcher dispatcher;
	private Context context;
	private State state;
	
	public Synchronizer(Dispatcher dispatcher, Context context, State state) {
		this.dispatcher = dispatcher;
		this.context = context;
		this.state = state;
	}

	public void synchronizeState() {
	}
	
	public void synchronizeContext() {
	}

}
