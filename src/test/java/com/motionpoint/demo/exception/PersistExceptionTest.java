package com.motionpoint.demo.exception;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PersistExceptionTest {

	static final String MSG ="Exception message";
	static final String DATA ="{some:data}";
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPersistException() {
		PersistException exception = new PersistException(MSG, DATA);
		assertNotNull(exception);
	}

	@Test
	public void testGetEnvironmentData() {
		PersistException exception = new PersistException(MSG, DATA);
		assertEquals(DATA, exception.getEnvironmentData());
	}

}
