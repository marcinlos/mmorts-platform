package com.app.ioapp.modules;

/**
 * Simple pair (module, descriptor).
 * 
 */
public final class ConfiguredModule {

    /** Module object */
    public final Module module;
    
    /** Module descriptor */
    public final ModuleDescriptor descriptor;

    public ConfiguredModule(Module module, ModuleDescriptor descriptor) {
        this.module = module;
        this.descriptor = descriptor;
    }

}
