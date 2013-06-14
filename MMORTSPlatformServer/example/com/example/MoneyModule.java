package com.example;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Module;

public class MoneyModule implements Module {

    private static final Logger logger = Logger.getLogger(MoneyModule.class);
    
    
    @Inject(optional = true)
    private Gateway gateway;

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void started() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receive(Message message, Context context) {
        logger.debug("Received message: " + message);
        
        
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub
        
    }

}
