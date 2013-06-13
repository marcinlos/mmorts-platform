package com.app.ioapp.view;

import android.util.Log;

public class TempModuleViewer {
	
	private static MainView main;
	private static final String ID = "TempModuleViewer";
	
	
	public static void setMainView (MainView v){
		main = v;
	}
	
	public static String getTempData(int parameter, int param){
		if(main == null)
			Log.e(ID,"ModuleViewer not initialized!");
		//Object o = main.getModuleData("myName");
		String s = null;
		//s = parse to good format (o)
		return s;
	}

}
