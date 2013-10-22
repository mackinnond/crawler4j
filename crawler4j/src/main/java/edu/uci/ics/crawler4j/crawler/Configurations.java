/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.crawler;

import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class Configurations.
 *
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

public final class Configurations
{

	/** The prop. */
	private static Properties prop = new Properties();

	/**
	 * Gets the string property.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the string property
	 */
	public static String getStringProperty(String key, String defaultValue)
	{
		if (prop == null || prop.getProperty(key) == null)
		{
			return defaultValue;
		}
		return prop.getProperty(key);
	}

	/**
	 * Gets the int property.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the int property
	 */
	public static int getIntProperty(String key, int defaultValue)
	{
		if (prop == null || prop.getProperty(key) == null)
		{
			return defaultValue;
		}
		return Integer.parseInt(prop.getProperty(key));
	}

	/**
	 * Gets the short property.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the short property
	 */
	public static short getShortProperty(String key, short defaultValue)
	{
		if (prop == null || prop.getProperty(key) == null)
		{
			return defaultValue;
		}
		return Short.parseShort(prop.getProperty(key));
	}

	/**
	 * Gets the long property.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the long property
	 */
	public static long getLongProperty(String key, long defaultValue)
	{
		if (prop == null || prop.getProperty(key) == null)
		{
			return defaultValue;
		}
		return Long.parseLong(prop.getProperty(key));
	}

	/**
	 * Gets the boolean property.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 * @return the boolean property
	 */
	public static boolean getBooleanProperty(String key, boolean defaultValue)
	{
		if (prop == null || prop.getProperty(key) == null)
		{
			return defaultValue;
		}
		return prop.getProperty(key).toLowerCase().trim().equals("true");
	}

	static
	{
		try
		{
			prop.load(Configurations.class.getClassLoader().getResourceAsStream("crawler4j.properties"));
		}
		catch (Exception e)
		{
			prop = null;
			System.err
					.println("WARNING: Could not find crawler4j.properties file in class path. I will use the default values.");
		}
	}
}
