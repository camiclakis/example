package com.motionpoint.demo.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationWrapperTest {
	static final String  HOST_KEY = "mongodb.host";
	static final String  PORT_KEY = "mongodb.port";

	@Test
	public void testGetString() {
		assertNull(ConfigurationWrapper.getString(""));
		assertNotNull(ConfigurationWrapper.getString(HOST_KEY));
	}

	@Test
	public void testGetInt() {
		assertNotNull(ConfigurationWrapper.getInt(PORT_KEY));
	}

	@Test(expected=NoSuchElementException.class)
	public void testGetIntWithNoSuchElementException() {
		assertNull(ConfigurationWrapper.getInt(""));
	}

}
