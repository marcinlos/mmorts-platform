package pl.edu.agh.ki.mmorts.client.backend.config;

import java.util.Properties;

import android.util.Log;


/**
 * Stores information written in configuration file.
 * Properties connected with module MySuperModule have a name like in the example below:
 * MySuperModule.mySuperProperty
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
	
	
	
	/**
	 * Returns properties which are connected with specified module
	 * @param moduleName
	 * @return properties
	 *//*
	public Properties getModuleProperties(String moduleName) {
		Properties toReturn = new Properties();
		for (Object propertyKey : properties.keySet()) {
			if (((String)propertyKey).startsWith(moduleName)) {
				toReturn.put(propertyKey, properties.get(propertyKey));
			}
		}
		return toReturn;
	}*/
	

}
