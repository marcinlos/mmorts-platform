package pl.edu.agh.ki.mmorts.server.config;

import java.util.HashSet;
import java.util.Set;

/**
 * Exception thrown when a vital, required configuration properties are missing
 * in the configuration file. Thrown when creating {@code Config}
 * implementation, having previously read all the configuration, or in the
 * classes using {@linkplain Config}.
 * 
 * @author los
 */
public class MissingRequiredPropertiesException extends ConfigException {

    private Set<String> properties;

    /**
     * Creates new exception, containing single missing property.
     * 
     * @param property
     *            Missing property that caused the exception
     */
    public MissingRequiredPropertiesException(String property) {
        this.properties = new HashSet<String>();
        properties.add(property);
    }

    /**
     * Creates a new exception from the set of missing properties
     * 
     * @param properties
     *            Set of mising properties
     */
    public MissingRequiredPropertiesException(Set<String> properties) {
        this.properties = properties;
    }

    /**
     * @return Missing property that caused the exception
     */
    public Set<String> getMissingProperties() {
        return properties;
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Prints an information about which properties were determined to be
     * missing.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        builder.append("\nMissing properties: ");
        for (String property : properties) {
            builder.append("\t").append(property);
        }
        return builder.toString();
    }

}
