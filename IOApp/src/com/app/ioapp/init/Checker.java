package com.app.ioapp.init;

import java.io.FileInputStream;
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
	 * Stream to read file with player's information
	 */
	private FileInputStream inputSream;
	
	/**
     * Reads properties from a file
     */
    private PropertiesLoader loader = new PropertiesLoader();
    
    /**
     * @param inputStream
     */
    public Checker (FileInputStream inputStream) {
    	this.inputSream = inputStream;
    }
    
    /**
     * @return read properties
     */
    public Properties getProperties() {
    	return loader.getProperties();
    }

	/**
	 * Checks if account exists which means info file contains correct user info
	 * @return true if exists
	 * @throws ReadingPropertiesException
	 */
	public boolean checkIfAccountExists() throws ReadingPropertiesException {
		try {
			loader.load(inputSream);
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
