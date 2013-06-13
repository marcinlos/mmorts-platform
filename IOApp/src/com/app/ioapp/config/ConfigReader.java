package com.app.ioapp.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Reads configuration file. Creates {@code Config}
 *
 */
public class ConfigReader {
	
	private String configFile;
	private Config config;
	 /** String property map */
    private Properties properties = new Properties();
	
	public Config getConfig() {
		return config;
	}

	public ConfigReader(String configFile) {
		this.configFile = configFile;
	}
	
	/**
	 * Creates new {@code Config} with read properties.
	 * @throws ConfigException
	 */
	public void configure() throws ConfigException{
		try {
			PropertiesLoader loader = new PropertiesLoader();
			loader.load(configFile);
			properties = loader.getProperties();
		} catch (IOException e) {
			throw new ConfigException("IO exception");
		}
		config = new Config(properties);
	}
	

	


}
