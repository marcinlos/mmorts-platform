package com.app.ioapp.modules.example;

import pl.edu.agh.ki.mmorts.client.core.annotations.CustomPersistor;
import pl.edu.agh.ki.mmorts.client.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.data.PlayersPersistor;
import pl.edu.agh.ki.mmorts.common.message.Message;
import android.util.Log;

import com.app.ioapp.modules.Context;
import com.app.ioapp.modules.Module;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ExampleModule implements Module {

	
	protected static final String ID = ExampleModule.class.getName();
	
	@Inject(optional = true)
	@Named("example.number")
	protected int number;
	
	@Inject(optional = true)
	@CustomPersistor
	protected CustomPersistor customPersistor;
	
	@Inject(optional = true)
	protected PlayersPersistor playersPersistor;
	
	@Override
	@OnInit
	public void init() {
		Log.d(ID, "Initialized");
		System.out.println("Number: " + number);
		System.out.println("Players persistor: " + customPersistor);
		System.out.println("Custom persistor: " + playersPersistor);
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
