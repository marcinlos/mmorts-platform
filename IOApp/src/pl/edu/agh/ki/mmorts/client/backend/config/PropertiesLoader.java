package pl.edu.agh.ki.mmorts.client.backend.config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class reading files with properties
 */
public class PropertiesLoader {
	/**
	 * Properties read from file
	 */
	private Properties properties = new Properties();

	/**
	 * @return properties read from file
	 */
	 public Properties getProperties() {
		return properties;
	}


	/**
     * Loads configuration from the file.
     * 
     * @param inputStream
     * @throws IOException
     */
    public void load(InputStream inputStream) throws IOException {
        try {
           BufferedInputStream input = new BufferedInputStream(inputStream);
            properties.load(input);
        } finally {

        }
    }
	

}
