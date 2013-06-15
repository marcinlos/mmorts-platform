package com.example;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;

public class MoneyModule extends ModuleBase {

    @Override
    public void receive(Message message, Context context) {
        logger().debug("Received message: " + message);

    }

}
