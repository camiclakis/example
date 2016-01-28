package com.motionpoint.demo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.InvalidParameterException;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.motionpoint.demo.dao.MongoDAO;
import com.motionpoint.demo.exception.PersistException;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleMessageTest {

	@InjectMocks
	ConsoleMessage consoleMessage = new ConsoleMessage();

	@Mock
	MongoDAO dao;

	@Mock
	Logger log;

	final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	static final String CONTENT = "{\"id\":373109970,\"sender\":\"renemorell\",\"text\":\"Lorem ipsum dolor sit amet, consetetur sadipscing elitr,\"}";

	static final int INT_ID = 0;

	static final String TEXT = "Text";

	@Before
	public void setUp() throws Exception {
		System.setOut(new PrintStream(outContent));
		dao = Mockito.mock(MongoDAO.class);
		log = Mockito.mock(Logger.class);
		ReflectionTestUtils.setField(consoleMessage, "dao", dao);
		ReflectionTestUtils.setField(consoleMessage, "log", log);
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	@Test
	public void testConsoleMessage() {
		assertNotNull(consoleMessage);
	}

	@Test
	public void testStore() throws Exception {
		doNothing().when(dao).save(anyInt(), anyString(), anyString());
		boolean stored = consoleMessage.store();
		assertTrue(stored);
		Mockito.verify(log, Mockito.times(1)).debug(anyString());
	}

	@Test
	public void testStoreWithInvalidParameterException() throws Exception {
		doThrow(new InvalidParameterException()).when(dao).save(anyInt(), anyString(), anyString());
		boolean stored = consoleMessage.store();
		assertFalse(stored);
		Mockito.verify(log, Mockito.times(1)).error(anyString(), any(InvalidParameterException.class));
	}

	@Test
	public void testLoad() throws Exception {
		JSONObject jsonParser = new JSONObject(CONTENT);
		when(dao.retrieve(anyInt(), anyString())).thenReturn(CONTENT);
		boolean loaded = consoleMessage.load();
		assertEquals(consoleMessage.getSender(), jsonParser.get("sender"));
		assertEquals(consoleMessage.getText(), jsonParser.get("text"));
		assertTrue(loaded);
		Mockito.verify(log, Mockito.times(1)).debug(anyString());
	}

	@Test
	public void testLoadWithInvalidParameterException() throws Exception {
		when(dao.retrieve(anyInt(), anyString())).thenThrow(new InvalidParameterException());
		boolean loaded = consoleMessage.load();
		assertFalse(loaded);
		Mockito.verify(log, Mockito.times(1)).error(anyString(), any(InvalidParameterException.class));
	}
	
	@Test
	public void testSend() {
		consoleMessage.send();
	    assertNotNull(outContent.toString());
	}

	@Test
	public void testSetId() {
		consoleMessage.setId(INT_ID);
		assertEquals(consoleMessage.getId(), INT_ID);
	}

	@Test
	public void testGetId() {
		consoleMessage.setId(INT_ID);
		assertEquals(consoleMessage.getId(), INT_ID);
	}

	@Test
	public void testGetSender() {
		consoleMessage.setSender(TEXT);
		assertEquals(consoleMessage.getSender(), TEXT);

	}

	@Test
	public void testSetSender() {
		consoleMessage.setSender(TEXT);
		assertEquals(consoleMessage.getSender(), TEXT);
	}

	@Test
	public void testGetText() {
		consoleMessage.setText(TEXT);
		assertEquals(consoleMessage.getText(), TEXT);
	}

	@Test
	public void testSetText() {
		consoleMessage.setText(TEXT);
		assertEquals(consoleMessage.getText(), TEXT);
	}

	@Test
	public void testGetContent() {
		assertNotNull(consoleMessage.getContent());
	}

	@Test
	public void testToString() {
		assertNotNull(consoleMessage.toString());
	}

}
