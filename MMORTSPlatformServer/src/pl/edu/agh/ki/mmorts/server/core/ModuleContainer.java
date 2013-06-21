package pl.edu.agh.ki.mmorts.server.core;

import java.util.Collection;

import pl.edu.agh.ki.mmorts.server.communication.ServiceLocator;
import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.Module;

/**
 * Interface of the module container used by the {@linkplain Init} to initialize
 * modules, and to manage it afterwards.
 * 
 * <p>
 * Initialization after constructor call is supported
 * 
 * @author los
 */
public interface ModuleContainer extends ServiceLocator  {

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second.
     * 
     * @param modules
     *            Modules to initialize
     */
    void registerModules(ConfiguredModule... modules);

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second.
     * 
     * @param modules
     *            Modules to initialize
     */
    void registerModules(Iterable<ConfiguredModule> modules);

    /**
     * @return Immutable collection of module information
     */
    Collection<ConfiguredModule> getModules();

}
