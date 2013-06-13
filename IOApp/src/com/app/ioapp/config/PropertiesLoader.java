package com.app.ioapp.config;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class reading .properties files
 */
public class PropertiesLoader {
	
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
     * @throws IOException
     */
    public void load(String file) throws IOException{
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            properties.load(input);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	

}
