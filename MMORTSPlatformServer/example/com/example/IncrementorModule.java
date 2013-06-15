package com.example;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.ModuleBase;

public class IncrementorModule extends ModuleBase {

    @Override
    public void receive(Message message, Context context) {
        if (message.request.equals("increment")) {
            String var = message.get(String.class);
            int n = context.get(var, Integer.class);
            context.put(var, n + 1);
            logger().debug("Incremented " + var + ", now it's " + (n + 1));
            sendNotification("cool_modules", "var-incremented");
        } else {
            logger().warn("Unable to respond: " + message);
        }
    }

}
