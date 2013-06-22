package pl.edu.agh.ki.mmorts.server.core;

import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

/**
 * Interface for receiving notifications about module-related events, such as
 * loading or unloading a module.
 * 
 * @author los
 * 
 */
public interface ModuleEventsListener {

    /**
     * Invoked before creation and initialization of the module, after having
     * read its' configuration.
     * 
     * @param descriptor
     *            Descriptor of the module to be loaded
     */
    void loadingModule(ModuleDescriptor descriptor);

    /**
     * Invoked after the module is created and initialized.
     * 
     * @param module
     *            Newly created module
     */
    void moduleLoaded(ConfiguredModule module);

    /**
     * Invoked before deinitialization and removal of the module.
     * 
     * @param module
     *            Module to be unloaded
     */
    void unloadingModule(ConfiguredModule module);

    /**
     * Invoked after the module is fully unloaded
     * 
     * @param module
     *            Unloaded module
     */
    void moduleUnloaded(ConfiguredModule module);

}
