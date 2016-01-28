package com.motionpoint.demo.util;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Random;

/**
 * This class is intended to help on integer operations
 * 
 * @author renemorell
 */
public class IntegerUtil {
	/**
	 * Hidden constructor for utility class.
	 */
	private IntegerUtil() {
		super();
	}

	/**
	 * Generates a random integer.
	 * 
	 * @param min
	 *            minimal bound
	 * @param max
	 *            maximal bound
	 * @return random integer in range of min and max parameters
	 * @throws InvalidParameterException
	 *             when min is bigger than max
	 */
	public static int getRandom(int min, int max) throws InvalidParameterException {
		if (min > max) {
			throw new InvalidParameterException("Min parameter value is bigger than max.");
		}
		Random random = new Random();
		return random.nextInt((max - min) + 1) + min;
	}
}
