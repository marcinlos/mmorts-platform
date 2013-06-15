package com.example;

import java.util.Random;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;

public class BuildingModule extends ModuleBase {


    @Override
    public void receive(Message message, Context context) {
        logger().debug("Received message: " + message);
        //logger().debug("Let's build the building!");
        //Random rand = new Random();
        //int cost = message.get(Integer.class);
        //logger().debug("Let's try to take money");
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

}
