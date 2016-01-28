package com.motionpoint.demo.model;

/**
 * Message interface including simple operations.
 * 
 * @author renemorell
 *
 */
public interface Message extends Persistent {
	/**
	 * Sets message text
	 * 
	 * @param text
	 *            Message text also know as body
	 */
	void setText(String text);

	/**
	 * Sets person who sends/create message
	 * 
	 * @param sender
	 *            Person who sends/create message
	 */
	void setSender(String sender);

	/**
	 * Sends current message
	 */
	void send();
}
