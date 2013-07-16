package com.app.ioapp.modules.example;

import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import android.util.Log;

public class ExampleModuleComm extends ExampleModule implements GUICommModule{

	
	
	@Override
	public boolean isStateChanged() {
		Log.d(ID, "Is state changed");
		return false;
	}

	@Override
	public void stateReceived() {
		Log.d(ID, "State received");
	}

	@Override
	public <T> void setData(T data, Class<T> clazz) {
		Log.d(ID, "Data setting");
	}

	@Override
	public <T> T getData(Class<T> clazz) {
		Log.d(ID, "Data getting");
		return clazz.cast(new ExampleData(190));
	}

}
