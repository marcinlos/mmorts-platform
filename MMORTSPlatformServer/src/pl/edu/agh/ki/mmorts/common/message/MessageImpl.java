package pl.edu.agh.ki.mmorts.common.message;

/**
 * Concrete implementation of the message.
 */
class MessageImpl implements Message {

    private Address target;
    private Address source;
    private int convId;
    private String type;
    private Mode mode;
    private Object content;

    MessageImpl(Address target, Address source, int convId, String type,
            Mode mode, Object content) {
        super();
        this.target = target;
        this.source = source;
        this.convId = convId;
        this.type = type;
        this.mode = mode;
        this.content = content;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address getAddress() {
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getConversationId() {
        return convId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mode getMode() {
        return mode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getContent() {
        return content;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Msg[");
        return sb.append("id=").append(convId).append(", ").append(source)
                .append(" -> ").append(target).append(" (").append(mode)
                .append(")").append(", content=[").append(content).append("]")
                .append("]").toString();
    }
}
