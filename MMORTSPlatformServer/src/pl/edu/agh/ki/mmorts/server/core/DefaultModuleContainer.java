package pl.edu.agh.ki.mmorts.server.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.communication.ServiceLocator;
import pl.edu.agh.ki.mmorts.server.communication.ServiceLocatorDelgate;
import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.Module;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

/**
 * Component responsible for modules initialization and management.
 * 
 * @author los
 */
public class DefaultModuleContainer implements ModuleContainer {

    public static final Logger logger = Logger
            .getLogger(DefaultModuleContainer.class);

    /**
     * Lock protecting internal structures containing module configuration and
     * address mappings.
     */
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /** Read lock, used to obtain shared access to the internal structures */
    protected final Lock readLock = rwLock.readLock();

    /** Write lock, used to obtain exclusive access to the internal structures */
    protected final Lock writeLock = rwLock.writeLock();

    /** Name -> Configuration mapping */
    private final Map<String, ConfiguredModule> modules;
    /** Address -> Module mapping */
    private final Map<String, Module> unicast;
    /** Address -> Multicast group mapping */
    private final Map<String, Set<Module>> multicast;

    /** Implementation of service locator */
    private final ServiceLocator services = new ServiceLocatorDelgate();

    /** Version of the server */
    private int version;

    /**
     * List of global module events listeners, used to implement the
     * {@linkplain ModuleEventsNotifier} interface
     */
    private List<ModuleEventsListener> listeners;

    /**
     * Creates new {@code DefaultModuleContainer} with empty module list and
     * mappings
     */
    public DefaultModuleContainer() {
        modules = new HashMap<String, ConfiguredModule>();
        unicast = new HashMap<String, Module>();
        multicast = new HashMap<String, Set<Module>>();

        // Synchronization is necessary only in case of fairly strange usage,
        // i.e. when the listeners are registered outside the initialization
        // sequence and module loading.
        listeners = new ArrayList<ModuleEventsListener>();
        listeners = Collections.synchronizedList(listeners);

    }

    /**
     * @return {@code Name -> module} info mapping
     */
    protected Map<String, ConfiguredModule> modules() {
        return modules;
    }

    /**
     * @return {@code Unicast address -> module} mapping
     */
    protected Map<String, Module> unicast() {
        return unicast;
    }

    /**
     * @return {@code Multicast address -> members} mapping
     */
    protected Map<String, Set<Module>> multicast() {
        return multicast;
    }

    /**
     * @return Version of the application
     */
    protected int version() {
        return version;
    }

    /**
     * Updates the version number
     */
    protected void updateVersion() {
        ++version;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerModules(ConfiguredModule... modules) {
        registerModules(Arrays.asList(modules));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerModules(Iterable<ConfiguredModule> modules) {
        for (ConfiguredModule conf : modules) {
            addModule(conf);
        }
        phaseTwo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ConfiguredModule> getModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    /**
     * Calls module's {@code init()} and add it to internal structures.
     * 
     * @param conf
     *            Configuration & module to register
     */
    private void addModule(ConfiguredModule conf) {
        ModuleDescriptor desc = conf.descriptor;
        logger.debug("Registering module " + desc.name);
        logger.debug("Calling init() on " + desc.name);
        try {
            Module module = conf.module;
            module.init();
            logger.debug("Adding to internal map structures");
            modules.put(desc.name, conf);
            // Register module with all its' unicast addresses
            for (String address : desc.unicast) {
                registerUnicast(address, conf);
            }
            // Same for multicast groups
            for (String group : desc.multicast) {
                registerMulticast(group, module);
            }
        } catch (Exception e) {
            logger.error("Module " + desc.name + " registration failed", e);
        }
    }

    /**
     * Registers module with a given unicast address. In case of a conflict
     * (i.e. two modules share the unicast address) gives the priority to the
     * new one and logs address and conflicting module names.
     */
    private void registerUnicast(String address, ConfiguredModule conf) {
        ModuleDescriptor desc = conf.descriptor;
        Module module = conf.module;
        logger.debug("Module " + desc.name + " registered as " + address);
        Module prev = unicast.put(address, module);
        if (prev != null) {
            for (Entry<String, ConfiguredModule> e : modules.entrySet()) {
                if (e.getValue().module == module) {
                    String msg = String.format("Unicast address conflict "
                            + "(%s); %s has overriden %s", address, desc.name,
                            e.getKey());
                    logger.warn(msg);
                    break;
                }
            }
        }
    }

    /**
     * Subscribes a module to a given multicast group.
     */
    private void registerMulticast(String group, Module module) {
        logger.debug("Module " + module + " added to [" + group + "]");
        Set<Module> set = multicast.get(group);
        if (set == null) {
            set = new HashSet<Module>();
            multicast.put(group, set);
        }
        set.add(module);
    }

    /**
     * Removes the module from the internal structures. Used to rollback module
     * registration in case of phase-two initialization failure.
     */
    private void removeModule(String name) {
        ConfiguredModule conf = modules.remove(name);
        for (String address : conf.descriptor.unicast) {
            unicast.remove(address);
        }
        for (String group : conf.descriptor.multicast) {
            multicast.get(group).remove(conf.module);
        }
    }

    /**
     * Second phase of initialization - calling all the modules'
     * {@code started()} methods.
     */
    private void phaseTwo() {
        logger.debug("Second phase of module initialization");
        Set<String> moduleNames = new HashSet<String>(modules.keySet());
        for (String name : moduleNames) {
            ConfiguredModule conf = modules.get(name);
            logger.debug("Calling started() on " + name);
            Module module = conf.module;
            try {
                module.started();
                notifyModuleLoaded(conf);
            } catch (Exception e) {
                logger.error("Module " + name + " started() has failed", e);
                removeModule(name);
            }
        }
    }

    /**
     * Shuts down all the modules, logging exceptions thrown in the process.
     */
    protected void shutdownModules() {
        logger.debug("Shutting down modules");
        for (ConfiguredModule conf : modules.values()) {
            try {
                conf.module.shutdown();
            } catch (Exception e) {
                String name = conf.descriptor.name;
                logger.error("Error while shutting down module " + name, e);
            }
        }
        logger.debug("Modules shat down");
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void register(Class<? super T> service, T provider) {
        logger.debug("Registering " + provider.getClass() + " as " + service);
        services.register(service, provider);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void registerIfAbsent(Class<? super T> service, T provider) {
        logger.debug("Registering " + provider.getClass() + " as " + service);
        services.registerIfAbsent(service, provider);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> T lookup(Class<T> service) {
        return services.lookup(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(ModuleEventsListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(ModuleEventsListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all the registered listeners about the module about to be
     * loaded.
     * 
     * @param descriptor
     *            Descriptor of module to be loaded
     */
    protected void notifyLoadingModule(ModuleDescriptor descriptor) {
        for (ModuleEventsListener listener : listeners) {
            listener.loadingModule(descriptor);
        }
    }

    /**
     * Notifies all the registered listeners about the module just loaded.
     * 
     * @param module
     *            Loaded module
     */
    protected void notifyModuleLoaded(ConfiguredModule module) {
        for (ModuleEventsListener listener : listeners) {
            listener.moduleLoaded(module);
        }
    }

    /**
     * Notifies all the registered listeners before the module unloading.
     * 
     * @param module
     *            Module to be unloaded
     */
    protected void notifyUnloadingModule(ConfiguredModule module) {
        for (ModuleEventsListener listener : listeners) {
            listener.unloadingModule(module);
        }
    }

    /**
     * Notifies all the registered listeners after the module has been unloaded.
     * 
     * @param module
     *            Module to be unloaded
     */
    protected void notifyModuleUnloaded(ConfiguredModule module) {
        for (ModuleEventsListener listener : listeners) {
            listener.moduleUnloaded(module);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeLoad(ModuleDescriptor descriptor) {
        notifyLoadingModule(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerModule(ConfiguredModule module) {
        // TODO Auto-generated method stub
        
    }

}
