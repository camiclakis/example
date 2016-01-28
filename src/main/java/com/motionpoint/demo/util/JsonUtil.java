package com.motionpoint.demo.util;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motionpoint.demo.config.ConfigurationWrapper;

/**
 * This class is intended to help on object-json transformation operations
 * 
 * @author renemorell
 *
 */
public final class JsonUtil {
	/**
	 * Logger to keep trace of events and exceptions. See log4j.properties file.
	 */
	private static Logger log = Logger.getLogger(ConfigurationWrapper.class.getName());

	/**
	 * Mapper to transform objects to/from json string
	 */
	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	/**
	 * Hidden constructor for utilities
	 */
	private JsonUtil() {
	}

	/**
	 * Transform object to json string representation
	 * 
	 * @param obj
	 *            To be transformed into json representation
	 * @return Json string representation of the object or null in case an
	 *         exception occurs
	 */
	public static String getAsJSONString(Object obj) {
		try {
			return JSON_MAPPER.writeValueAsString(obj);
		} catch (Exception ex) {
			log.error(ex);
			return null;
		}
	}

	/**
	 * Transform json string into a corresponding object
	 * 
	 * @param jsonString
	 *            Json string representation of the object
	 * @param classType
	 *            Class type of target object
	 * @return Object corresponding to the json string structure and class type
	 */
	public static Object getObjectFromJSONString(String jsonString, Class<?> classType) {
		try {
			return JSON_MAPPER.readValue(new StringReader(jsonString), classType);
		} catch (IOException exception) {
			log.error(exception);
			return null;
		}
	}
}
