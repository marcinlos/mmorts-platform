package com.app.ioapp.config;

import java.util.Properties;


/**
 * Stores information written in configuration file.
 * Created and initialized by {@code ConfigReader}
 */
public class Config {
	/** String property map */
	private Properties properties;
	
	
	/**
	 * @param properties
	 */
	public Config(Properties properties) {
		this.properties = properties;
	}



	/**
	 * Gives the value of searched property
	 * @param key
	 * @return value of property which name is the argument
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	

}
