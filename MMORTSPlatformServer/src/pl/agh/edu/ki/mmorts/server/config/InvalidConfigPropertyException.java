package pl.agh.edu.ki.mmorts.server.config;

/**
 * Thrown when some configuration property has invalid value. For example, if
 * some implementation class cannot be found on the classpath.
 */
public class InvalidConfigPropertyException extends ConfigException {

    private String key;
    private String value;

    public InvalidConfigPropertyException(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public InvalidConfigPropertyException(String msg, String key, String value) {
        super(msg);
        this.key = key;
        this.value = value;
    }

    public InvalidConfigPropertyException(String key, String value,
            Throwable cause) {
        super(cause);
        this.key = key;
        this.value = value;
    }

    public InvalidConfigPropertyException(String msg, String key, String value,
            Throwable cause) {
        super(msg, cause);
        this.key = key;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Prints an invalid property and its value, as well as the cause if it was
     * specified in the constructor.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append(String.format("Properties: {%s: %s}", key, value));
        Throwable cause = getCause();
        if (cause != null) {
            builder.append("Because of: ").append(cause);
        }
        return builder.toString();
    }

    /**
     * @return Invalid property name
     */
    public String getKey() {
        return key;
    }

    /**
     * @return Invalid property value
     */
    public String getValue() {
        return value;
    }

}
