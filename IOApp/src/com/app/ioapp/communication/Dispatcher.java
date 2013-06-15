package com.app.ioapp.communication;

import com.app.ioapp.modules.CommunicatingModule;


/**
 * Interface of the dispatcher used by the {@linkplain Init} to initialize
 * modules.
 * 
 * <p>Initialization after constructor call is supported 
 */
public interface Dispatcher extends Gateway {

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain CommunicatingModule#init()} in the first traversal, and
     * {@linkplain CommunicatingModule#started()} in the second.
     * 
     * @param modules Modules to initialize
     */
    void registerModules(CommunicatingModule... modules);

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain CommunicatingModule#init()} in the first traversal, and
     * {@linkplain CommunicatingModule#started()} in the second.
     * 
     * @param modules Modules to initialize
     */
    void registerModules(Iterable<? extends CommunicatingModule> modules);
    
    /**
     * Registers a moduleClass with its own unique unicast address. Other modules
     * @param moduleClass
     * @param category
     */
    void registerUnicastReceiver(CommunicatingModule module, String category);
    
}
