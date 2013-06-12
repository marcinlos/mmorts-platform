package pl.edu.agh.ki.mmorts.server.modules;

import pl.edu.agh.ki.mmorts.server.core.ModuleConfigException;

/**
 * Thrown by the {@linkplain ModuleDescriptor} builder when some fields are 
 * missing.
 */
public class IncompleteDescriptorException extends ModuleConfigException {

    public IncompleteDescriptorException() {
        // empty
    }

    public IncompleteDescriptorException(String message) {
        super(message);
    }

    public IncompleteDescriptorException(Throwable cause) {
        super(cause);
    }

    public IncompleteDescriptorException(String message, Throwable cause) {
        super(message, cause);
    }

}
