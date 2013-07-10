package com.app.ioapp.modules.example;

import pl.edu.agh.ki.mmorts.client.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.common.message.Message;

import android.util.Log;

import com.app.ioapp.modules.Context;
import com.app.ioapp.modules.Module;
import com.google.inject.Inject;

public class ExampleModule implements Module {

	
	protected static final String ID = ExampleModule.class.getName();
	
	@Override
	@OnInit
	public void init() {
		Log.d(ID, "Initialized");
	}

	@Override
	public void started() {
		Log.d(ID, "Started");
	}

	@Override
	public void receive(Message message, Context context) {
		Log.d(ID, "Received msg");
	}

	@Override
	public void shutdown() {
		Log.d(ID, "Shutted down");
	}

}
