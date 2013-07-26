package pl.edu.agh.ki.mmorts.client.backend.init;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import pl.edu.agh.ki.mmorts.client.backend.config.PropertiesLoader;
import pl.edu.agh.ki.mmorts.client.backend.config.ReadingPropertiesException;


/**
 * Responsible for checking if this is the first start-up of the application on this phone
 * If it is not, reads properties
 *
 */
public class LoginChecker {
	
	
	/**
     * Properties from a file
     */
    private Properties properties;
    
    /**
     * @return read properties
     */
    public Properties getProperties() {
    	return properties;
    }

	/**
	 * Checks if account exists which means loginDataFile contains correct user info
	 * @return {@code true} if exists
	 * @throws ReadingPropertiesException
	 */
	public boolean checkIfAccountExists(FileInputStream inputStream) throws ReadingPropertiesException {
		PropertiesLoader loader = new PropertiesLoader();
		try {
			loader.load(inputStream);
		} catch (IOException e) {
			throw new ReadingPropertiesException();
		}
		properties = loader.getProperties();
		String userMail = loader.getProperties().getProperty("mail");
		String password = loader.getProperties().getProperty("password");
		if (userMail == null || password == null) {
			return false;
		}
		return true;
	}

}
