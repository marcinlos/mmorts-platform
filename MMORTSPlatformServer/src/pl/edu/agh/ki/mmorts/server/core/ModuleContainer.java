package pl.edu.agh.ki.mmorts.server.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.Module;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

/**
 * Abstract base dispatcher managing modules.
 */
public abstract class ModuleContainer implements Dispatcher {

    public static final Logger logger = Logger.getLogger(ModuleContainer.class);

    private Map<String, ConfiguredModule> modules = new HashMap<String, ConfiguredModule>();
    private Map<String, Module> unicast = new HashMap<String, Module>();
    private Map<String, Set<Module>> multicast = new HashMap<String, Set<Module>>();

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
     * Register single module.
     * 
     * @param conf
     *            Configuration & module to register
     */
    private void registerModule(ConfiguredModule conf) {
        ModuleDescriptor desc = conf.descriptor;
        logger.debug("Registering module " + desc.name);
        logger.debug("Calling init() on " + desc.name);
        try {
            Module module = conf.module;
            module.init();
            logger.debug("Adding to internal map structures");
            modules.put(desc.name, conf);
            for (String address : desc.unicast) {
                if (address != null) {
                    logger.debug("Module " + desc.name + " registered as "
                            + address);
                    unicast.put(address, module);
                }
            }
            for (String group : desc.multicast) {
                registerMulticast(group, module);
            }
        } catch (Exception e) {
            logger.error("Module " + desc.name + " registration failed", e);
        }
    }

    private void registerMulticast(String group, Module module) {
        logger.debug("Module " + module + " added to [" + group + "]");
        Set<Module> set = multicast.get(group);
        if (set == null) {
            set = new HashSet<Module>();
            multicast.put(group, set);
        }
        set.add(module);
    }

    private void removeModule(String name) {
        ConfiguredModule conf = modules.remove(name);
        for (String address : conf.descriptor.unicast) {
            unicast.remove(address);
        }
        for (String group : conf.descriptor.multicast) {
            multicast.get(group).remove(conf.module);
        }
    }

    private void phaseTwo() {
        logger.debug("Second phase of module initialization");
        Set<String> moduleNames = new HashSet<String>(modules.keySet());
        for (String name : moduleNames) {
            ConfiguredModule conf = modules.get(name);
            logger.debug("Calling started() on " + name);
            Module module = conf.module;
            try {
                module.started();
            } catch (Exception e) {
                logger.error("Module " + name + " started() has failed", e);
                removeModule(name);
            }
        }
    }

}
