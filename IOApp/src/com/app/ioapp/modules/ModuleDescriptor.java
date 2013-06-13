package com.app.ioapp.modules;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * Module configuration data class.
 */
public class ModuleDescriptor {

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
    private ModuleDescriptor(String unicastAddress,
            Set<String> multicastGroups, Class<? extends Module> module) {
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
     * @param address
     *            Unicast address of the moduleClass
     * @param moduleClass
     *            Module implementation class
     * @return {@linkplain Builder}
     */
    public static Builder create(final String address,
            final Class<? extends Module> module) {
        return new Builder() {

            private Set<String> multicast = new HashSet<String>();

            @Override
            public ModuleDescriptor build() {
                Set<String> immutableGroups = Collections
                        .unmodifiableSet(multicast);
                return new ModuleDescriptor(address, immutableGroups, module);
            }

            @Override
            public void addGroup(String group) {
                multicast.add(group);
            }
        };
    }

}
