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

package edu.uci.ics.crawler4j.robotstxt;

// TODO: Auto-generated Javadoc
/**
 * The Class HostDirectives.
 * 
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

public class HostDirectives
{

	// If we fetched the directives for this host more than
	// 24 hours, we have to refetch it.
	/** The Constant EXPIRATION_DELAY. */
	private static final long EXPIRATION_DELAY = 24 * 60 * 1000L;

	/** The disallows. */
	private RuleSet disallows = new RuleSet();

	/** The allows. */
	private RuleSet allows = new RuleSet();

	/** The time fetched. */
	private long timeFetched;

	/** The time last accessed. */
	private long timeLastAccessed;

	/**
	 * Instantiates a new host directives.
	 */
	public HostDirectives()
	{
		timeFetched = System.currentTimeMillis();
	}

	/**
	 * Needs refetch.
	 * 
	 * @return true, if successful
	 */
	public boolean needsRefetch()
	{
		return (System.currentTimeMillis() - timeFetched > EXPIRATION_DELAY);
	}

	/**
	 * Allows.
	 * 
	 * @param path
	 *            the path
	 * @return true, if successful
	 */
	public boolean allows(String path)
	{
		timeLastAccessed = System.currentTimeMillis();
		if (disallows.containsPrefixOf(path))
		{
			return allows.containsPrefixOf(path);
		}
		return true;
	}

	/**
	 * Adds the disallow.
	 * 
	 * @param path
	 *            the path
	 */
	public void addDisallow(String path)
	{
		disallows.add(path);
	}

	/**
	 * Adds the allow.
	 * 
	 * @param path
	 *            the path
	 */
	public void addAllow(String path)
	{
		allows.add(path);
	}

	/**
	 * Gets the last access time.
	 * 
	 * @return the last access time
	 */
	public long getLastAccessTime()
	{
		return timeLastAccessed;
	}
}