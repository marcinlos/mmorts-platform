package pl.edu.agh.ki.mmorts.client.backend.common.message;

/**
 * Message destination multiplicity (unicast/multicast).
 * 
 */
public enum Mode {

    /** Unicast message - single receiver */
    UNICAST,

    /** Multicast message - possibly multiple receivers */
    MULTICAST;

}
