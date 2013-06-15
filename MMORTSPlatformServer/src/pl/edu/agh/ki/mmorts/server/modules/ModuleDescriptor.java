package pl.edu.agh.ki.mmorts.server.modules;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.ki.mmorts.server.util.PropertyMap;

/**
 * Module configuration data class.
 */
public class ModuleDescriptor {

    /** Name of the module */
    public final String name;

    /** Unique unicast address of the moduleClass */
    public final Set<String> unicast;

    /**
     * Set of multicast group names (message categories) the moduleClass wishes
     * to subscribe
     */
    public final Set<String> multicast;

    /** Implementation of the moduleClass */
    public final Class<? extends Module> moduleClass;

    /** Configuration of a module */
    public final PropertyMap config;

    /*
     * Used internally by the builder
     */
    private ModuleDescriptor(String name, Set<String> unicastAddresses,
            Set<String> multicastGroups, Class<? extends Module> module,
            PropertyMap config) {
        this.name = name;
        this.unicast = unicastAddresses;
        this.multicast = multicastGroups;
        this.moduleClass = module;
        this.config = config;
    }

    /**
     * Builder interface to safely create moduleClass descriptors.
     */
    public static interface Builder {

        /**
         * Add new multicast group.
         * 
         * @param group
         *            Multicast group to subscribe
         */
        void addGroup(String group);

        /**
         * Creates a descriptor using information saved in the builder.
         * 
         * @return New moduleClass descriptor
         */
        ModuleDescriptor build();

        /**
         * Add object's unicast address
         * 
         * @param address
         *            Unicast address to add
         */
        void addUnicast(String address);

        /**
         * Add configuration parameter
         * 
         * @param name
         *            Property name
         * @param value
         *            Property value
         */
        void addProperty(String name, String value);
    }

    /**
     * Creates a builder that can be used to create a {@code ModuleDescriptor}.
     * Address and implementation are arguments to ensure their presence.
     * 
     * @param name
     *            name of the module
     * @param moduleClass
     *            Module implementation class
     * @return {@linkplain Builder}
     */
    public static Builder create(final String name,
            final Class<? extends Module> module) {
        if (name == null) {
            throw new NullPointerException("Module name cannot be null");
        } else if (module == null) {
            throw new NullPointerException("Module cannot be null");
        }
        return new Builder() {

            private Set<String> unicast = new HashSet<String>();
            private Set<String> multicast = new HashSet<String>();
            private Map<String, String> props = new HashMap<String, String>();

            @Override
            public ModuleDescriptor build() {
                Set<String> groups = Collections.unmodifiableSet(multicast);
                Set<String> addresses = Collections.unmodifiableSet(unicast);
                PropertyMap map = new PropertyMap(props);
                return new ModuleDescriptor(name, addresses, groups, module,
                        map);
            }

            @Override
            public void addGroup(String group) {
                multicast.add(group);
            }

            @Override
            public void addUnicast(String address) {
                unicast.add(address);
            }

            @Override
            public void addProperty(String name, String value) {
                props.put(name, value);
            }
        };
    }

}
