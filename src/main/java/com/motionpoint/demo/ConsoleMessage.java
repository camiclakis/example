package com.motionpoint.demo;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motionpoint.demo.dao.MongoDAO;
import com.motionpoint.demo.exception.PersistException;
import com.motionpoint.demo.model.Message;
import com.motionpoint.demo.util.JsonUtil;


/**
 * ConsoleMessage class encapsulates very simple message behavior.
 * @author renemorell
 *
 */
public class ConsoleMessage implements Message {
	/**
	 * Logger to keep trace of events and exceptions. See log4j.properties file.
	 */
	private static Logger log = Logger.getLogger(ConsoleMessage.class.getName());

	private MongoDAO dao;
	
	/**
	 * Unique message identifier useful for persistence.
	 */
	private int id;
	/**
	 * User who sends the message
	 */
	private String sender;
	/**
	 * Text body of message
	 */
	private String text;

	/**
	 * Simple constructor needed to initialize instance and deserialize from json string
	 */
	public ConsoleMessage() {
		super();
		dao = new MongoDAO();
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Persistent#store()
	 */
	public boolean store() {
		boolean retValue = true;
		try {
			dao.save(id, getContent(), this.getClass().getSimpleName());
			log.debug("Stored: " + toString());
		} catch (InvalidParameterException | PersistException e) {
			log.error("Error storing object", e);
			retValue = false;
		}
		return retValue;
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Persistent#load()
	 */
	public boolean load() {
		boolean retValue = true;
		try {
			String content = dao.retrieve(id, this.getClass().getSimpleName());
			ConsoleMessage consoleMessage = (ConsoleMessage) JsonUtil.getObjectFromJSONString(content,
					ConsoleMessage.class);
			setSender(consoleMessage.getSender());
			setText(consoleMessage.getText());
			log.debug("Loaded: " + toString());
		} catch (InvalidParameterException | PersistException e) {
			log.error("Error loading object", e);
			retValue = false;
		}
		return retValue;
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Message#send()
	 */
	public void send() {
		System.out.println(this.getClass().getSimpleName() + ":\n" + id);
		System.out.println("Sender:\n" + sender);
		System.out.println("Text:\n" + text);
	}

	/**
	 * Identifier setter
	 * @param id New message identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Persistent#getId()
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Retrieves message sender
	 * @return User who sends message
	 */
	public String getSender() {
		return sender;
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Message#setSender(java.lang.String)
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * Retrieves message text
	 * @return Message string text
	 */
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Message#setText(java.lang.String)
	 */
	public void setText(String text) {
		this.text = text;
	}

	/* (non-Javadoc)
	 * @see com.motionpoint.demo.model.Persistent#getContent()
	 */
	@JsonIgnore
	public String getContent() {
		String json = JsonUtil.getAsJSONString(this);
		return json;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConsoleMessage [id=" + id + ", sender=" + sender + ", text=" + text + "]";
	}

}
