package pl.edu.agh.ki.mmorts.server.core;

import pl.edu.agh.ki.mmorts.server.modules.Module;

/**
 * Interface of the dispatcher used by the {@linkplain Init} to initialize
 * modules.
 */
public interface ModuleContainer {

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
     * Releases resources (such as connections etc) held by this object.
     */
    void shutdown();

}
