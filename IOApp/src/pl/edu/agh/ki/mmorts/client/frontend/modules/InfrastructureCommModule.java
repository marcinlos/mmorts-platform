package pl.edu.agh.ki.mmorts.client.frontend.modules;


import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.backend.modules.Module;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;
import pl.edu.agh.ki.mmorts.client.frontend.modules.InfMod.InfrastructureModule;
import android.util.Log;

public class InfrastructureCommModule implements GUICommModule {
	
	private static final String ID = InfrastructureModule.class.getName();
	
	private InfrastructureModule m;
	
	
	public InfrastructureCommModule(Module m){
		this.m = (InfrastructureModule) m;
	}
	

	/**
     * {@inheritDoc}
     */
	@OnInit
    @Override
    public void init() {
        Log.d(ID, "Initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
    	Log.d(ID, "Started");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        //TODO dunno
    }
    
    @Override
	public void receive(Message message, TransactionContext context) {
		// TODO dunno
		
	}

	@Override
	public boolean isStateChanged() {
		return m.isStateChanged();
	}

	@Override
	public void stateReceived() {
		m.stateReceived();

	}

	@Override
	public <T> void setData(T data, Class<T> clazz) {
		m.setData(data);

	}

	@Override
	public <T> T getData(Class<T> clazz) {
		return m.getData();
	}

	

}
