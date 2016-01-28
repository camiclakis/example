package com.motionpoint.demo;

import java.security.InvalidParameterException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motionpoint.demo.dao.MongoDAO;
import com.motionpoint.demo.exception.PersistException;
import com.motionpoint.demo.model.Persistent;
import com.motionpoint.demo.util.JsonUtil;

/**
 * Acts as integer holder with additional persistence behavior
 * 
 * @author renemorell
 *
 */
public class IntegerKeeper implements Persistent {
	/**
	 * Logger to keep trace of events and exceptions. See log4j.properties file.
	 */
	private static Logger log = Logger.getLogger(ConsoleMessage.class.getName());

	private MongoDAO dao;

	/**
	 * unique identifier to use on persistence
	 */
	private int id;
	/**
	 * Integer value to keep
	 */
	private int guest;

	/**
	 * Simple constructor needed for deserialize from json string
	 */
	public IntegerKeeper() {
		super();
		dao = new MongoDAO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.motionpoint.demo.model.Persistent#store()
	 */
	public boolean store() {
		boolean retValue = true;
		try {
			dao.save(id, getContent(), this.getClass().getSimpleName());
			log.debug("Stored:" + this.toString());
		} catch (InvalidParameterException | PersistException e) {
			log.error("Error storing object", e);
			retValue = false;
		}
		return retValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.motionpoint.demo.model.Persistent#load()
	 */
	public boolean load() {
		boolean retValue = true;
		try {
			String content = dao.retrieve(id, this.getClass().getSimpleName());
			IntegerKeeper intKeeper = (IntegerKeeper) JsonUtil.getObjectFromJSONString(content, IntegerKeeper.class);
			setGuest(intKeeper.getGuest());
			log.debug("Loaded: " + toString());
		} catch (InvalidParameterException | PersistException e) {
			log.error("Error loading object", e);
			retValue = false;
		}
		return retValue;
	}

	/**
	 * Sets guest value
	 * 
	 * @param guest
	 *            New guest integer value
	 */
	public void setGuest(int guest) {
		this.guest = guest;
	}

	/**
	 * Retrieves guest value
	 * 
	 * @return Guest integer value
	 */
	public int getGuest() {
		return guest;
	}

	/**
	 * Retrieves identifier
	 * 
	 * @param id
	 *            Integer identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.motionpoint.demo.model.Persistent#getId()
	 */
	public int getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.motionpoint.demo.model.Persistent#getContent()
	 */
	@JsonIgnore
	public String getContent() {
		String json = JsonUtil.getAsJSONString(this);
		return json;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IntegerKeeper [id=" + id + ", guest=" + guest + "]";
	}

}
