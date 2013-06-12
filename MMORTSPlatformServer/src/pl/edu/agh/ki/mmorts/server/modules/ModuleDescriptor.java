package pl.edu.agh.ki.mmorts.server.modules;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Module configuration data class.
 */
public class ModuleDescriptor {

    /** Name of the module */
    public final String name;

    /** Unique unicast address of the moduleClass */
    public final String unicastAddress;

    /**
     * Set of multicast group names (message categories) the moduleClass wishes
     * to subscribe
     */
    public final Set<String> multicastGroups;

    /** Implementation of the moduleClass */
    public final Class<? extends Module> moduleClass;

    /*
     * Used internally by the builder
     */
    private ModuleDescriptor(String name, String unicastAddress,
            Set<String> multicastGroups, Class<? extends Module> module) {
        this.name = name;
        this.unicastAddress = unicastAddress;
        this.multicastGroups = multicastGroups;
        this.moduleClass = module;
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

            private String unicast;
            private Set<String> multicast = new HashSet<String>();

            @Override
            public ModuleDescriptor build() {
                Set<String> groups = Collections.unmodifiableSet(multicast);
                return new ModuleDescriptor(name, unicast, groups, module);
            }

            @Override
            public void addGroup(String group) {
                multicast.add(group);
            }
        };
    }

}
