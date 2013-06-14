package pl.edu.agh.ki.mmorts.common.message;

/**
 * Structure representing the full address (
 */
public final class Address {

    /** Internal (unicast or multicast module) address */
    public final String internal;

    /** External (dispatcher) address */
    public final String dispatcher;

    private Address(String internal, String dispatcher) {
        this.internal = internal;
        this.dispatcher = dispatcher;
    }

    /**
     * Creates a new local {@code Address} with {@code null} dispatcher
     * component and given internal address.
     * 
     * @param internal
     *            Internal address
     * @return Local {@code Address} object
     */
    public static Address local(String internal) {
        return new Address(internal, null);
    }

    /**
     * Creates a new remote {@code Address} (with a non-{@code null} dispatcher
     * component)
     * 
     * @param internal
     *            Internal dispatcher address
     * @param dispatcher
     *            Host address, necesserily non-{@code null}
     * @return
     * @throws {@code NullPointerException} if the dispatcher part is
     *         {@code null}
     */
    public static Address remote(String internal, String dispatcher) {
        if (dispatcher == null) {
            throw new NullPointerException("Null dispatcher in remote address");
        }
        return new Address(internal, dispatcher);
    }

    /**
     * @return {@code true} if the address is local (i.e. dispatcher component
     *         is {@code null})
     */
    public boolean isLocal() {
        return dispatcher == null;
    }

    /**
     * @return {@code Destination#LOCAL} if the address is local,
     *         {@code Destination#REMOTE} otherwise.
     */
    public Destination type() {
        return isLocal() ? Destination.LOCAL : Destination.REMOTE;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>Example of a format: Addr[login-module@server], Addr[buildings]
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Addr[");
        sb.append(internal);
        if (dispatcher != null) {
            sb.append("@").append(dispatcher);
        }
        return sb.append("]").toString();
    }
}
