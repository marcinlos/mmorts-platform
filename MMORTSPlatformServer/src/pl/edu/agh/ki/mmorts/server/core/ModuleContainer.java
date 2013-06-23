package pl.edu.agh.ki.mmorts.server.core;

import java.util.Collection;

import pl.edu.agh.ki.mmorts.server.communication.ServiceLocator;
import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.Module;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

/**
 * Interface of the module container used by the {@linkplain Init} to initialize
 * modules, and to manage it afterwards.
 * 
 * <p>
 * Initialization after constructor call is supported
 * 
 * @author los
 */
public interface ModuleContainer extends ServiceLocator, ModuleEventsNotifier {

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second. Designed to be used to load
     * the initial modules during server initialization sequence.
     * 
     * <p>
     * Note: remember to call {@linkplain #beforeLoad(ModuleDescriptor)} for
     * each module on the list before invoking this method.
     * 
     * @param modules
     *            Modules to initialize
     */
    void registerModules(ConfiguredModule... modules);

    /**
     * Register all the modules, i.e. iterate through all of them twice, calling
     * {@linkplain Module#init()} in the first traversal, and
     * {@linkplain Module#started()} in the second. Designed to be used to load
     * the initial modules during server initialization sequence.
     * 
     * <p>
     * Note: remember to call {@linkplain #beforeLoad(ModuleDescriptor)} for
     * each module on the list before invoking this method.
     * 
     * @param modules
     *            Modules to initialize
     */
    void registerModules(Iterable<ConfiguredModule> modules);

    /**
     * Invoked before the actual module creation and configuration.
     * 
     * @param descriptor
     *            Descriptor of the module to be loaded
     */
    void beforeLoad(ModuleDescriptor descriptor);

    /**
     * Registers the module in the module container.
     * 
     * @param modules
     *            Module to register
     */
    void registerModule(ConfiguredModule module);

    /**
     * @return Immutable collection of module information
     */
    Collection<ConfiguredModule> getModules();

}
