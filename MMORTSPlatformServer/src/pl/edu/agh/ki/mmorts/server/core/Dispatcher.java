package pl.edu.agh.ki.mmorts.server.core;

import java.util.Collection;

import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.Module;

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
    void registerModules(ConfiguredModule... modules);

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second.
     * 
     * @param modules Modules to initialize
     */
    void registerModules(Iterable<ConfiguredModule> modules);
    
    /**
     * @return Immutable collection of module information
     */
    Collection<ConfiguredModule> getModules();
    
}
