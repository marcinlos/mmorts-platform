package pl.edu.agh.ki.mmorts.server.core;

import java.util.Collection;

import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

/**
 * Provides read-only information about loaded modules. 
 */
public interface ModuleTable {
    
    /**
     * @return Descriptors of successfully loaded
     */
    Collection<ModuleDescriptor> getModuleDescriptors();

}
