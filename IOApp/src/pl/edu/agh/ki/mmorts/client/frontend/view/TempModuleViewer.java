package pl.edu.agh.ki.mmorts.client.frontend.view;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ConcreteModulesBroker;
import android.util.Log;

public class TempModuleViewer {
	
	private static ConcreteModulesBroker main;
	private static final String ID = "TempModuleViewer";
	
	
	public static void setMainView (ConcreteModulesBroker v){
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
