package com.app.ioapp.view;


public class MainView {
	/*
	private Map<String,CommunicatingModule> modules;
	private UIListener listener;
	private PlayersContext context;
	
	public MainView(Map<String,CommunicatingModule> modules, PlayersContext context){
		this.modules = modules;
		this.context = context;
	}
	
	public void setListener(UIListener l){
		listener = l;
	}
	
	public void updateModules(Map<String,CommunicatingModule> modules){       // bo te same moduly sa tez w innych miejscach
		this.modules = modules;
	}
	
	
	public void StuffHappened(Object wtf){
		listener.stuffHappened(wtf);
	}
	
	//public Object getModuleData(String moduleName){
		
	//}
	
	/**
	 * invoked by MenuManager when a button has been clicked.
	 * @param menuNameClicked module it needs to be directed to
	 */
	public void handleMenuAction(String moduleName){
		//TODO
	}

}

