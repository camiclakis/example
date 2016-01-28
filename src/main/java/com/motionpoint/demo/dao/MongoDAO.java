package com.motionpoint.demo.dao;

import java.security.InvalidParameterException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.motionpoint.demo.config.ConfigurationWrapper;
import com.motionpoint.demo.exception.PersistException;

/**
 * Intended to isolate MongoDB DAO operations like connect, disconnect, store
 * content, retrieve content...
 * Configuration can be set in src/main/resources/config.properties
 * 
 * @author renemorell
 *
 */
public class MongoDAO {
	/**
	 * Logger to keep trace of events and exceptions. See log4j.properties file.
	 */
	private static Logger log = Logger.getLogger(MongoDAO.class.getName());

	/**
	 * Client object to access MongoDB
	 */
	private MongoClient mongoClient;

	/**
	 * Host name where mongodb is running
	 */
	private String host;

	/**
	 * Port number in host where mongodb instance is published
	 */
	private int port;

	/**
	 * Mongodb database name to connect to
	 */
	private String dbName;

	/**
	 * Connected mongodb database
	 */
	private MongoDatabase mongoDatabase;

	/**
	 * Constructor to initialize properties
	 */
	public MongoDAO() {
		this.host = ConfigurationWrapper.getString("mongodb.host");
		this.port = ConfigurationWrapper.getInt("mongodb.port");
		this.dbName = ConfigurationWrapper.getString("mongodb.dbname");
	}

	/**
	 * Connects to mongodb using host, port and dbname properties
	 * 
	 * @return True if operation successfully occurs, otherwise false
	 */
	private boolean connect() {
		boolean retValue = false;
		disconnect();
		try {
			mongoClient = new MongoClient(host, port);
			this.mongoDatabase = mongoClient.getDatabase(dbName);
			retValue = true;
		} catch (Exception e) {
			log.error("Error trying to open client connection", e);
		}
		return retValue;
	}

	/**
	 * Determines if collection exists in connected database
	 * 
	 * @param name
	 *            Name of collection to ask for
	 * @return True if already exists in database, otherwise false
	 */
	private boolean collectionExists(String name) {
		MongoCollection<Document> collection = mongoDatabase.getCollection(name);
		return (collection != null);
	}

	/**
	 * Disconnects from connected mongodb
	 */
	private void disconnect() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	/**
	 * Save supplied content to database
	 * 
	 * @param id
	 *            Identifier for new document in collection
	 * @param content
	 *            Content to be saved
	 * @param collectionName
	 *            Collection name where content will be saved
	 * @throws PersistException
	 *             Thrown in case connection couldn't be established
	 * @throws InvalidParameterException
	 *             Thrown when any key parameter (id, collectionName) is null or
	 *             empty value
	 */
	public void save(Integer id, String content, String collectionName)
			throws PersistException, InvalidParameterException {
		if ((StringUtils.isEmpty(collectionName)) || (id == null)) {
			throw new InvalidParameterException(
					String.format("Parameters can not be null or empty: id=$d, collectionName=%s", id, collectionName));
		}
		if (!connect()) {
			throw new PersistException("Error creating mongodb connection", this.toString());
		}
		MongoCollection<Document> collection = createCollection(collectionName);
		Document document = new Document();
		document.put("id", id);
		document.put("content", content);
		document.put("modificationDate", new Date());
		collection.insertOne(document);
		disconnect();
	}

	/**
	 * Retrieves saved content
	 * 
	 * @param id
	 *            Identifier to access content to retrieve
	 * @param collectionName
	 *            Collection name where content will be retrieved from
	 * @return Content as String value
	 * @throws PersistException
	 *             Thrown in case connection couldn't be established
	 * @throws InvalidParameterException
	 *             Thrown when any key parameter (id, collectionName) is null or
	 *             empty value
	 */
	public String retrieve(Integer id, String collectionName) throws PersistException, InvalidParameterException {
		String retValue = "";
		if ((StringUtils.isEmpty(collectionName)) || (id == null)) {
			throw new InvalidParameterException(
					String.format("Parameters can not be null or empty: id=$d, collectionName=%s", id, collectionName));
		}
		if (!connect()) {
			throw new PersistException("Error creating mongodb connection", this.toString());
		}
		MongoCollection<Document> collection = createCollection(collectionName);
		FindIterable<Document> cursor = collection.find(new Document("id", id)).limit(1);
		Document document = cursor.first();
		retValue = document.getString("content");
		disconnect();
		return retValue;
	}

	/**
	 * Creates new collection
	 * 
	 * @param collectionName
	 *            New collection name
	 * @return Created collection
	 */
	private MongoCollection<Document> createCollection(String collectionName) {
		if (!collectionExists(collectionName)) {
			mongoDatabase.createCollection(collectionName);
		}
		return mongoDatabase.getCollection(collectionName);
	}

	@Override
	public String toString() {
		return "MongoDAO [mongoClient=" + mongoClient + ", host=" + host + ", port=" + port + ", dbName=" + dbName
				+ ", mongoDatabase=" + mongoDatabase + "]";
	}

}
