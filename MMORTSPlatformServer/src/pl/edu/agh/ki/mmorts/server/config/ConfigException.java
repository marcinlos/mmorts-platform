package pl.edu.agh.ki.mmorts.server.config;

/**
 * Thrown when configuration processing encounters an error .
 * 
 * @author los
 */
public class ConfigException extends RuntimeException {

    public ConfigException() {
        // empty
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
