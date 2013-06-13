package com.app.ioapp.init;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.app.ioapp.config.PropertiesLoader;
import com.app.ioapp.config.ReadingPropertiesException;

/**
 * Responsible for checking if this is the first start-up of the application on this phone
 * If it is not, reads properties
 *
 */
public class Checker {
	
    /** 
     * Info file location
     * This file stores information about players account: mail, password
     */
    private static final String info = "appInfo/info.properties";
	
	/**
     * Reads properties from a file
     */
    private PropertiesLoader loader = new PropertiesLoader();
    
    /**
     * @return read properties
     */
    public Properties getProperties() {
    	return loader.getProperties();
    }

	/**
	 * Checks if account exists which means info file contains correct user info
	 * @return true if exists
	 */
	public boolean checkIfAccountExists() {
		File file = new File(info);
		if (!file.exists()) {
			return false;
		}
		try {
			loader.load(info);
		} catch (IOException e) {
			throw new ReadingPropertiesException();
		}
		String userMail = loader.getProperties().getProperty("mail");
		String password = loader.getProperties().getProperty("password");
		if (userMail == null || password == null) {
			return false;
		}
		return true;
	}

}
