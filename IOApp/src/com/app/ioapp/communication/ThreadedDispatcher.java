package com.app.ioapp.communication;

import java.util.Arrays;


import com.app.ioapp.annotations.OnShutdown;
import com.app.ioapp.config.Config;
import com.app.ioapp.modules.Module;
import com.google.inject.Inject;


/**
 * Default implementation of a message dispatcher.
 */
public class ThreadedDispatcher implements Gateway, Dispatcher {


    /** Configuration object */
    private Config config;

    /** Message service */
    private MessageChannel channel;

    @Inject
    public ThreadedDispatcher(Config config, MessageChannel channel) {
        this.config = config;
        this.channel = channel;
        channel.startReceiving(this);
    }

    @Override
    public void registerModules(Module... modules) {
        registerModules(Arrays.asList(modules));
    }

    @Override
    public void receive(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerModules(Iterable<? extends Module> modules) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerUnicastReceiver(Module module, String category) {
        // TODO Auto-generated method stub

    }

    /**
     * Shutdown callback, notifies modules.
     */
    @OnShutdown
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
