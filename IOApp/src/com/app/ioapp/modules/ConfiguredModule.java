package com.app.ioapp.modules;

/**
 * Simple pair (module, descriptor).
 * 
 */
public final class ConfiguredModule {

    /** Module object */
    public final IModule module;
    
    /** Module descriptor */
    public final ModuleDescriptor descriptor;

    public ConfiguredModule(IModule module, ModuleDescriptor descriptor) {
        this.module = module;
        this.descriptor = descriptor;
    }

}
