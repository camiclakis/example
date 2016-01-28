package com.motionpoint.demo.model;

/**
 * Persistent interface including simple operations
 * 
 * @author renemorell
 *
 */
public interface Persistent {
	/**
	 * Intended to retrieve identifier
	 * 
	 * @return Integer identifier
	 */
	int getId();

	/**
	 * Retrieves object content as String
	 * 
	 * @return String representation of content
	 */
	String getContent();

	/**
	 * Encapsulate operations to persist object
	 * 
	 * @return True if operation successfully occurs, otherwise false
	 */
	boolean store();

	/**
	 * Encapsulate operations to retrieve persisted object
	 * 
	 * @return True if operation successfully occurs, otherwise false
	 */
	boolean load();
}
