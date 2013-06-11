package pl.edu.agh.ki.mmorts.server.core;

import pl.agh.edu.ki.mmorts.server.config.ConfigException;

/**
 * Base class for exceptions caused by errors in module configuration file.
 */
public class ModuleConfigException extends ConfigException {

    public ModuleConfigException() {
        // empty
    }

    public ModuleConfigException(String message) {
        super(message);
    }

    public ModuleConfigException(Throwable cause) {
        super(cause);
    }

    public ModuleConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
