package com.motionpoint.demo.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * Utility class to load configuration file
 * 
 * @author renemorell
 */
public class ConfigurationWrapper {
	/**
	 * Logger to keep trace of events and exceptions. See log4j.properties file.
	 */
	private static Logger log = Logger.getLogger(ConfigurationWrapper.class.getName());
	/**
	 * Config object to access properties values.
	 */
	private static PropertiesConfiguration config;
	/**
	 * Configuration file name under 'src/main/resources'.
	 */
	private static String configFileName = "config.properties";

	static {
		try {
			config = new PropertiesConfiguration(configFileName);
		} catch (ConfigurationException e) {
			log.fatal("Error trying to load file: " + configFileName, e);
		}
	}

	/**
	 * Hidden constructor for utility class.
	 */
	private ConfigurationWrapper() {
		super();
	}

	/**
	 * Wraps configuration getString()
	 * @param name
	 *            Key name of property
	 * @return property string value
	 */
	public static String getString(String name) {
		return config.getString(name);
	}

	/**
	 * Wraps configuration getInt()
	 * @param name
	 *            Key name of property
	 * @return property integer value
	 */
	public static int getInt(String name) {
		return config.getInt(name);
	}
}
