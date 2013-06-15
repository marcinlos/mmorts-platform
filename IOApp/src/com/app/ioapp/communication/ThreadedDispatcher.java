package com.app.ioapp.communication;

import java.util.Arrays;

import com.app.ioapp.config.Config;
import com.app.ioapp.modules.CommunicatingModule;


/**
 * Default implementation of a message dispatcher.
 */
public class ThreadedDispatcher implements MessageReceiver, Dispatcher {


    /** Configuration object */
    private Config config;

    /** Message service */
    private MessageChannel channel;

    public ThreadedDispatcher(Config config, MessageChannel channel) {
        this.config = config;
        this.channel = channel;
        channel.startReceiving(this);
    }

    @Override
    public void registerModules(CommunicatingModule... modules) {
        registerModules(Arrays.asList(modules));
    }

    @Override
    public void receive(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerModules(Iterable<? extends CommunicatingModule> modules) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerUnicastReceiver(CommunicatingModule module, String category) {
        // TODO Auto-generated method stub

    }

    /**
     * Shutdown callback, notifies modules.
     */
    public void shutdown() {
        try {
            // TODO: Shut down modules
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendTo(Message message, String address) {
        // TODO Auto-generated method stub

    }

    @Override
    public void send(Message mesage, String category) {
        // TODO Auto-generated method stub

    }

}
