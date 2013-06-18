package pl.edu.agh.ki.mmorts.common.message;

/**
 * Message destination multiplicity (unicast/multicast).
 * 
 * @author los
 */
public enum Mode {

    /** Unicast message - single receiver */
    UNICAST,

    /** Multicast message - possibly multiple receivers */
    MULTICAST;

}
