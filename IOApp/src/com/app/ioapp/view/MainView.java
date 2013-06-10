package com.app.ioapp.view;

import java.util.HashMap;
import java.util.Map;

import com.app.ioapp.data.Context;
import com.app.ioapp.interfaces.IModule;
import com.app.ioapp.interfaces.UIListener;

public class MainView {
	
	private Map<String,IModule> modules;
	private UIListener listener;
	private Context context;
	
	public MainView(Map<String,IModule> modules, Context context){
		this.modules = modules;
		this.context = context;
	}
	
	public void setListener(UIListener l){
		listener = l;
	}
	
	public void updateModules(Map<String,IModule> modules){       // bo te same moduly sa tez w innych miejscach
		this.modules = modules;;
	}

	
	public void StuffHappened(Object wtf){
		listener.stuffHappened(wtf);
	}

}

