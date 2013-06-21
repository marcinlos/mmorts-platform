package pl.edu.agh.ki.mmorts.server.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Reads the server configuration, possibly combining multiple sources.
 * 
 * @author los
 */
public class ConfigReader {

    private static final Logger logger = Logger.getLogger(ConfigReader.class);

    /** String property map */
    private Properties properties = new Properties();

    /**
     * Loads configuration from the file denoted by {@linkplain File} object.
     * 
     * @param file
     *            {@code File}
     * 
     * @throws ConfigException
     *             If reading configuration encounters an error
     */
    public void loadFrom(File file) {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            loadFrom(input);
        } catch (IOException e) {
            logger.error("Error reading configuration file " + file, e);
            throw new ConfigException("IO error", e);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e1) {
                    logger.error("Error while closing config file" + file, e1);
                }
            }
        }
    }

    /**
     * Loads configuration from the file at the given location.
     * 
     * @param path
     *            File path
     * 
     * @throws ConfigException
     *             If reading configuration encounters an error
     */
    public void loadFrom(String path) {
        loadFrom(new File(path));
    }

    /**
     * Loads configuration from the passed input stream.
     * 
     * @param stream
     *            Input stream to read configuration form
     * @throws IOException
     *             In case of plain IO error
     */
    public void loadFrom(InputStream stream) throws IOException {
        properties.load(stream);
    }

    /**
     * @return {@linkplain Config} object containing read configuration
     * 
     * @throws MissingRequiredPropertiesException
     *             If some required configuration properties were not loaded
     *             frmo any source
     * 
     * @throws ConfigException
     *             If some other error occured while creating configuration
     *             object
     */
    public Config getConfig() {
        return new ConfigImpl(properties);
    }

}
