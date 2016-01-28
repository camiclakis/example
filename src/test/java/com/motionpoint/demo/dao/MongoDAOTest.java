package com.motionpoint.demo.dao;

import com.mongodb.client.FindIterable;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

import java.security.InvalidParameterException;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.motionpoint.demo.exception.PersistException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MongoDAO.class)
public class MongoDAOTest {

	@InjectMocks
	MongoDAO dao = new MongoDAO();

	@Mock
	MongoClient mongoClient;

	@Mock
	MongoDatabase mongoDatabase;

	@Mock
	MongoCollection<Document> collection;

	@Mock
	FindIterable<Document> cursor;

	@Mock
	FindIterable<Document> limitCursor;

	@Mock
	Document document;

	static final Integer ID = 1;
	static final String CONTENT = "{\"id\":1,\"guest\":0}";
	static final String COLLECTION_NAME = "CollectionName";
	static final String EMPTY_COLLECTION_NAME = "";

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mongoClient = Mockito.mock(MongoClient.class);
		mongoDatabase = Mockito.mock(MongoDatabase.class);
		cursor = Mockito.mock(FindIterable.class);
		limitCursor = Mockito.mock(FindIterable.class);
		document = Mockito.mock(Document.class);
		ReflectionTestUtils.setField(dao, "mongoClient", mongoClient);
	}

	@Test
	public void testMongoDAO() {
		assertNotNull(dao);
	}

	@Test
	public void testSave() throws Exception {
		doNothing().when(mongoClient).close();
		whenNew(MongoClient.class).withParameterTypes(String.class, int.class).withArguments(anyString(), anyInt())
				.thenReturn(mongoClient);
		when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);

		doNothing().when(mongoDatabase).createCollection(anyString());
		when(mongoDatabase.getCollection(anyString())).thenReturn(collection);
		doNothing().when(collection).insertOne(any(Document.class));

		dao.save(ID, CONTENT, COLLECTION_NAME);

		Mockito.verify(mongoClient, Mockito.times(2)).close();
		Mockito.verify(collection, Mockito.times(1)).insertOne(any(Document.class));
	}

	@Test(expected = PersistException.class)
	public void testSaveWithPersistException() throws Exception {
		doNothing().when(mongoClient).close();
		whenNew(MongoClient.class).withParameterTypes(String.class, int.class).withArguments(anyString(), anyInt())
				.thenThrow(new Exception());
		dao.save(ID, CONTENT, COLLECTION_NAME);
	}

	@Test(expected = InvalidParameterException.class)
	public void testSaveWithInvalidParameterException() throws Exception {
		dao.save(ID, CONTENT, EMPTY_COLLECTION_NAME);
	}

	@Test
	public void testRetrieve() throws Exception {
		doNothing().when(mongoClient).close();
		whenNew(MongoClient.class).withParameterTypes(String.class, int.class).withArguments(anyString(), anyInt())
				.thenReturn(mongoClient);
		when(mongoClient.getDatabase(anyString())).thenReturn(mongoDatabase);

		doNothing().when(mongoDatabase).createCollection(anyString());
		when(mongoDatabase.getCollection(anyString())).thenReturn(collection);
		when(collection.find(any(Document.class))).thenReturn(limitCursor);
		when(limitCursor.limit(anyInt())).thenReturn(cursor);
		when(cursor.first()).thenReturn(document);
		when(document.getString(anyString())).thenReturn(CONTENT);

		String content = dao.retrieve(ID, COLLECTION_NAME);
		assertEquals(content, CONTENT);
		Mockito.verify(mongoClient, Mockito.times(2)).close();
	}

	@Test(expected = PersistException.class)
	public void testRetrieveWithPersistException() throws Exception {
		doNothing().when(mongoClient).close();
		whenNew(MongoClient.class).withParameterTypes(String.class, int.class).withArguments(anyString(), anyInt())
				.thenThrow(new Exception());
		dao.retrieve(ID, COLLECTION_NAME);
	}

	@Test(expected = InvalidParameterException.class)
	public void testRetrieveWithInvalidParameterException() throws Exception {
		dao.retrieve(ID, EMPTY_COLLECTION_NAME);
	}

	@Test
	public void testToString() {
		assertNotNull(dao.toString());
	}

}
