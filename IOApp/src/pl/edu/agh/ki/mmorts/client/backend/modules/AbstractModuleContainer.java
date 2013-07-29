package pl.edu.agh.ki.mmorts.client.backend.modules;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.util.Log;


/**
 * Abstract base dispatcher managing modules.
 * 
 */
public abstract class AbstractModuleContainer implements ModuleContainer {
	
	private static final String ID = "AbstractModuleContainer";


    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    protected final Lock readLock = rwLock.readLock();
    protected final Lock writeLock = rwLock.writeLock();

    private final Map<String, ConfiguredModule> modules;
    private final Map<String, Module> unicast;
    private final Map<String, Set<Module>> multicast;
    

    /** Implementation of service locator */
    private final ServiceLocator services = new ServiceLocatorDelgate();
    
    /** Version of the server */
    private int version;

    
    public AbstractModuleContainer() {
        modules = new HashMap<String, ConfiguredModule>();
        unicast = new HashMap<String, Module>();
        multicast = new HashMap<String, Set<Module>>();
    }

    protected Map<String, ConfiguredModule> modules() {
        return modules;
    }

    protected Map<String, Module> unicast() {
        return unicast;
    }

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
        ++ version;
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
            registerModule(conf);
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
     * Register single module.
     * 
     * @param conf
     *            Configuration & module to register
     */
    private void registerModule(ConfiguredModule conf) {
        ModuleDescriptor desc = conf.descriptor;
        Log.d(ID, "Registering module" + desc.name);
        Log.d(ID, "Calling init() on " + desc.name);
        try {
            Module module = conf.module;
//            module.init();
            Log.d(ID, "Adding to internal map structures");
            modules.put(desc.name, conf);
            // Register module with all its' unicast addresses
            for (String address : desc.unicast) {
                registerUnicast(address, conf);
            }
            // Same for multicast groups
            for (String group : desc.multicast) {
            	// TODO: EVIL CAST, fix it
                registerMulticast(group, module);
            }
        } catch (Exception e) {
        	Log.e(ID, "Module " + desc.name + " registration failed");
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
        Log.d(ID, "Module " + desc.name + " registered as " + address);
        Module prev = unicast.put(address, module);
        if (prev != null) {
            for (Entry<String, ConfiguredModule> e : modules.entrySet()) {
                if (e.getValue().module == module) {
                    String msg = String.format("Unicast address conflict "
                            + "(%s); %s has overriden %s", address, desc.name,
                            e.getKey());
                    Log.e(ID, msg);
                    break;
                }
            }
        }
    }

    /**
     * Subscribes a module to a given multicast group.
     */
    private void registerMulticast(String group, Module module) {
    	Log.d(ID, "Module " + module + " added to [" + group + "]");
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
    	Log.d(ID, "Second phase of module initialization");
        Set<String> moduleNames = new HashSet<String>(modules.keySet());
        for (String name : moduleNames) {
            ConfiguredModule conf = modules.get(name);
            Log.d(ID, "Calling started() on " + name);
            Module module = conf.module;
            try {
//                module.started();
            } catch (Exception e) {
            	Log.e(ID, "Module " + name + " started() has failed", e);
                removeModule(name);
            }
        }
    }

    /**
     * Shuts down all the modules, logging exceptions thrown in the process.
     */
    protected void shutdownModules() {
    	Log.d(ID, "Shutting down modules");
        for (ConfiguredModule conf : modules.values()) {
            try {
//                conf.module.shutdown();
            } catch (Exception e) {
                String name = conf.descriptor.name;
                Log.e(ID, "Error while shutting down module " + name);
            }
        }
        Log.e(ID, "Modules shat down");
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void register(Class<? super T> service, T provider) {
        Log.d(ID, "Registering " + provider.getClass() + " as " + service);
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
        Log.d(ID, "Registering " + provider.getClass() + " as " + service);
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

}
