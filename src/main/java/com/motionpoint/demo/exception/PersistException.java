package com.motionpoint.demo.exception;

/**
 * To encapsulate bussiness logic exceptions
 * 
 * @author renemorell
 *
 */
public class PersistException extends Exception {

	/**
	 * {@link java.io.Serializable}
	 */
	private static final long serialVersionUID = 6351852849375131432L;

	/**
	 * Data associated to exception environment
	 */
	private String environmentData;

	/**
	 * Constructor
	 * 
	 * @param message
	 *            Basic reason/source of exception
	 * @param data
	 *            Data associated to exception environment
	 */
	public PersistException(String message, String data) {
		super(message);
		this.environmentData = data;
	}

	/**
	 * Retrieves data associated to exception environment
	 * 
	 * @return Data associated to exception environment
	 */
	public String getEnvironmentData() {
		return environmentData;
	}

}
