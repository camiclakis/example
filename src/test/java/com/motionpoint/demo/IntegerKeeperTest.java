package com.motionpoint.demo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.InvalidParameterException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.motionpoint.demo.dao.MongoDAO;
import com.motionpoint.demo.exception.PersistException;

@RunWith(MockitoJUnitRunner.class)
public class IntegerKeeperTest {

	@InjectMocks
	IntegerKeeper integerKeeper = new IntegerKeeper();

	@Mock
	MongoDAO dao;

	@Mock
	Logger log;

	static final String CONTENT = "{\"id\":1129425659,\"guest\":694313055}";

	static final int GUEST = 0;

	@Before
	public void setUp() throws Exception {
		dao = Mockito.mock(MongoDAO.class);
		log = Mockito.mock(Logger.class);
		ReflectionTestUtils.setField(integerKeeper, "dao", dao);
		ReflectionTestUtils.setField(integerKeeper, "log", log);
	}

	@Test
	public void testIntegerKeeper() {
		assertNotNull(integerKeeper);
	}

	@Test
	public void testStore() throws Exception {
		doNothing().when(dao).save(anyInt(), anyString(), anyString());
		boolean stored = integerKeeper.store();
		assertTrue(stored);
		Mockito.verify(log, Mockito.times(1)).debug(anyString());
	}

	@Test
	public void testStoreWithInvalidParameterException() throws Exception {
		doThrow(new InvalidParameterException()).when(dao).save(anyInt(), anyString(), anyString());
		boolean stored = integerKeeper.store();
		assertFalse(stored);
		Mockito.verify(log, Mockito.times(1)).error(anyString(), any(InvalidParameterException.class));
	}
	
	@Test
	public void testLoad() throws Exception {
		JSONObject jsonParser = new JSONObject(CONTENT);
		when(dao.retrieve(anyInt(), anyString())).thenReturn(CONTENT);
		boolean loaded = integerKeeper.load();
		assertEquals(integerKeeper.getGuest(), jsonParser.get("guest"));
		assertTrue(loaded);
		Mockito.verify(log, Mockito.times(1)).debug(anyString());
	}

	@Test
	public void testLoadWithInvalidParameterException() throws Exception {
		when(dao.retrieve(anyInt(), anyString())).thenThrow(new InvalidParameterException());
		boolean loaded = integerKeeper.load();
		assertFalse(loaded);
		Mockito.verify(log, Mockito.times(1)).error(anyString(), any(InvalidParameterException.class));
	}
	
	@Test
	public void testSetGuest() {
		integerKeeper.setGuest(GUEST);
		assertEquals(integerKeeper.getGuest(), GUEST);
	}

	@Test
	public void testGetGuest() {
		integerKeeper.setGuest(GUEST);
		assertEquals(integerKeeper.getGuest(), GUEST);
	}

	@Test
	public void testSetId() {
		integerKeeper.setId(GUEST);
		assertEquals(integerKeeper.getId(), GUEST);
	}

	@Test
	public void testGetId() {
		integerKeeper.setId(GUEST);
		assertEquals(integerKeeper.getId(), GUEST);
	}

	@Test
	public void testGetContent() {
	    assertNotNull(integerKeeper.getContent());
	}

	@Test
	public void testToString() {
		assertNotNull(integerKeeper.toString());
	}

}
