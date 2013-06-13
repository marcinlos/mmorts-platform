package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Destination type - local or remote.
 */
public enum Destination {

    /** Local delivery, inter-module communication */
    LOCAL,
    
    /** Remote delivery, inter-dispatcher communication */
    REMOTE
    
}
