package com.app.ioapp.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Reads configuration file. Creates {@code Config}
 *
 */
public class ConfigReader {
	/**
	 * Stream to read from file
	 */
	private InputStream inputStream;
	/**
	 * {@code Config} created by {@code configure()}
	 */
	private Config config;
	 /** String property map */
    private Properties properties = new Properties();
	
	/**
	 * @return {@code Config}
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @param configInput
	 */
	public ConfigReader(InputStream configInput) {
		this.inputStream = configInput;
	}
	
	/**
	 * Creates new {@code Config} with read properties.
	 * @throws ConfigException
	 */
	public void configure() throws ConfigException{
		try {
			PropertiesLoader loader = new PropertiesLoader();
			loader.load(inputStream);
			properties = loader.getProperties();
		} catch (IOException e) {
			throw new ConfigException("IO exception");
		}
		config = new Config(properties);
	}
	

	


}
