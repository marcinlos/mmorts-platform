package com.app.ioapp.view;

import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.data.Context;
import com.app.ioapp.interfaces.UIListener;
import com.app.ioapp.modules.Module;

public class MainView {
	
	private Map<String,Module> modules;
	private UIListener listener;
	private Context context;
	
	public MainView(Map<String,Module> modules, Context context){
		this.modules = modules;
		this.context = context;
	}
	
	public void setListener(UIListener l){
		listener = l;
	}
	
	public void updateModules(Map<String,Module> modules){       // bo te same moduly sa tez w innych miejscach
		this.modules = modules;;
	}

	
	public void StuffHappened(Object wtf){
		listener.stuffHappened(wtf);
	}

}

