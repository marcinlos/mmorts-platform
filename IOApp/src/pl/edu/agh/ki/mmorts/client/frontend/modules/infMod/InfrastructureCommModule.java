package pl.edu.agh.ki.mmorts.client.frontend.modules.infMod;


import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.backend.modules.Module;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;

public class InfrastructureCommModule implements GUICommModule {
	
	private static final String ID = InfrastructureModule.class.getName();
	
	private InfrastructureModule m;
	
	
	public InfrastructureCommModule(Module m){
		this.m = (InfrastructureModule) m;
	}
	


	@Override
	public void dataChanged(ModuleDataMessage data) {
		// TODO Auto-generated method stub
		
	}
    
 

}
