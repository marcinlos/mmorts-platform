package com.app.ioapp.modules;

import pl.edu.agh.ki.mmorts.common.message.Message;

public class InfrastructureCommModule implements GUICommModule {
	
	private InfrastructureModule m;
	
	public InfrastructureCommModule(Module m){
		this.m = (InfrastructureModule) m;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        //TODO dunno
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
        //TODO dunno
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        //TODO dunno
    }
    
    @Override
	public void receive(Message message, Context context) {
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
	public <T> void setData(T data) {
		m.setData(data);

	}

	@Override
	public <T> T getData() {
		return m.getData();
	}

	

}
