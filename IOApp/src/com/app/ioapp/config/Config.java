package com.app.ioapp.config;

import java.util.Properties;

import android.util.Log;


/**
 * Stores information written in configuration file.
 * Created and initialized by {@code ConfigReader}
 */
public class Config {
	
	private static final String ID = "Config";
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
		Log.e(ID,"Someone gets properties");
		return properties.getProperty(key);
	}
	

}
