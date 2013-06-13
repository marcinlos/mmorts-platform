package com.app.ioapp.communication;

import com.app.ioapp.modules.Module;


/**
 * Interface of the dispatcher used by the {@linkplain Init} to initialize
 * modules.
 * 
 * <p>Initialization after constructor call is supported 
 */
public interface Dispatcher extends Gateway {

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second.
     * 
     * @param modules Modules to initialize
     */
    void registerModules(Module... modules);

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second.
     * 
     * @param modules Modules to initialize
     */
    void registerModules(Iterable<? extends Module> modules);
    
    /**
     * Registers a moduleClass with its own unique unicast address. Other modules
     * @param moduleClass
     * @param category
     */
    void registerUnicastReceiver(Module module, String category);
    
}
