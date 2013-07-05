package com.app.ioapp.modules;

/**
 * Thrown when module initialization fails.
 * 
 */
public class ModuleInitException extends RuntimeException {

    public ModuleInitException() {
        // empty
    }

    public ModuleInitException(String message) {
        super(message);
    }

    public ModuleInitException(Throwable cause) {
        super(cause);
    }

    public ModuleInitException(String message, Throwable cause) {
        super(message, cause);
    }

}
