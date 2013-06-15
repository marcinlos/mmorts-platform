package com.example;

import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Module;

import com.google.inject.Inject;

public class BuildingModule implements Module {

    private static final Logger logger = Logger.getLogger(BuildingModule.class);

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
        logger.debug("Let's build the building!");
        Random rand = new Random();
        int cost = message.get(Integer.class);
        logger.debug("Let's try to take money");
        /*gateway.later(new ContChain(gateway, 
        new ContAdapter() {
            @Override
            public void execute(Context context) {
                logger.debug("Good, we have enough money");
            }
        },
        new ContAdapter() {
            @Override
            public void execute(Context context) {
                logger.debug("Another time!");
            }
        }));*/
    }

    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }

}
