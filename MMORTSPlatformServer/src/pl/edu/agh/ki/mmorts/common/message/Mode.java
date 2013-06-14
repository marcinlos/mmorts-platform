package pl.edu.agh.ki.mmorts.common.message;

/**
 * Message destination multiplicity (unicast/multicast).
 */
public enum Mode {

    /** Unicast message */
    UNICAST,
    
    /** Multicast message */
    MULTICAST;
    
    public static Mode fromInt(int i) {
        return values()[i]; 
    }
    
}
