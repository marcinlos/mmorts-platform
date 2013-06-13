package com.app.ioapp.communication;

/**
 * Base class for communication-related exceptions. Derives from
 * {@linkplain RuntimeException} to avoid cluttering the code with things like
 * handling the missing moduleClass when we know for sure it does exist.
 */
public class CommunicationException extends RuntimeException {

    public CommunicationException() {
        // empty
    }

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

}
