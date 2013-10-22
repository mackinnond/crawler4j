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

package edu.uci.ics.crawler4j.frontier;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.Util;

// TODO: Auto-generated Javadoc
/**
 * The Class WorkQueues.
 * 
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

public class WorkQueues
{

	/** The urls db. */
	protected Database urlsDB = null;

	/** The env. */
	protected Environment env;

	/** The resumable. */
	private boolean resumable;

	/** The db name. */
	private String dbName;

	/** The web url binding. */
	private WebURLTupleBinding webURLBinding;

	/** The mutex. */
	protected Object mutex = "WorkQueues_Mutex";

	/**
	 * Instantiates a new work queues.
	 * 
	 * @param env
	 *            the env
	 * @param dbName
	 *            the db name
	 * @param resumable
	 *            the resumable
	 * @throws DatabaseException
	 *             the database exception
	 */
	public WorkQueues(Environment env, String dbName, boolean resumable) throws DatabaseException
	{
		this.env = env;
		this.dbName = dbName;
		this.resumable = resumable;
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(resumable);
		dbConfig.setDeferredWrite(!resumable);
		urlsDB = env.openDatabase(null, dbName, dbConfig);
		webURLBinding = new WebURLTupleBinding();
	}

	/**
	 * Gets the.
	 * 
	 * @param max
	 *            the max
	 * @return the list
	 * @throws DatabaseException
	 *             the database exception
	 */
	public List<WebURL> get(int max) throws DatabaseException
	{
		synchronized (mutex)
		{
			int matches = 0;
			List<WebURL> results = new ArrayList<WebURL>(max);

			Cursor cursor = null;
			OperationStatus result = null;
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry value = new DatabaseEntry();
			Transaction txn;
			if (resumable)
			{
				txn = env.beginTransaction(null, null);
			}
			else
			{
				txn = null;
			}
			try
			{
				cursor = urlsDB.openCursor(txn, null);
				result = cursor.getFirst(key, value, null);

				while (matches < max && result == OperationStatus.SUCCESS)
				{
					if (value.getData().length > 0)
					{
						WebURL curi = (WebURL) webURLBinding.entryToObject(value);
						results.add(curi);
						matches++;
					}
					result = cursor.getNext(key, value, null);
				}
			}
			catch (DatabaseException e)
			{
				if (txn != null)
				{
					txn.abort();
					txn = null;
				}
				throw e;
			}
			finally
			{
				if (cursor != null)
				{
					cursor.close();
				}
				if (txn != null)
				{
					txn.commit();
				}
			}
			return results;
		}
	}

	/**
	 * Delete.
	 * 
	 * @param count
	 *            the count
	 * @throws DatabaseException
	 *             the database exception
	 */
	public void delete(int count) throws DatabaseException
	{
		synchronized (mutex)
		{
			int matches = 0;

			Cursor cursor = null;
			OperationStatus result = null;
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry value = new DatabaseEntry();
			Transaction txn;
			if (resumable)
			{
				txn = env.beginTransaction(null, null);
			}
			else
			{
				txn = null;
			}
			try
			{
				cursor = urlsDB.openCursor(txn, null);
				result = cursor.getFirst(key, value, null);

				while (matches < count && result == OperationStatus.SUCCESS)
				{
					cursor.delete();
					matches++;
					result = cursor.getNext(key, value, null);
				}
			}
			catch (DatabaseException e)
			{
				if (txn != null)
				{
					txn.abort();
					txn = null;
				}
				throw e;
			}
			finally
			{
				if (cursor != null)
				{
					cursor.close();
				}
				if (txn != null)
				{
					txn.commit();
				}
			}
		}
	}

	/**
	 * Put.
	 * 
	 * @param curi
	 *            the curi
	 * @throws DatabaseException
	 *             the database exception
	 */
	public void put(WebURL curi) throws DatabaseException
	{
		byte[] keyData = Util.int2ByteArray(curi.getDocid());
		DatabaseEntry value = new DatabaseEntry();
		webURLBinding.objectToEntry(curi, value);
		Transaction txn;
		if (resumable)
		{
			txn = env.beginTransaction(null, null);
		}
		else
		{
			txn = null;
		}
		urlsDB.put(txn, new DatabaseEntry(keyData), value);
		if (resumable)
		{
			txn.commit();
		}
	}

	/**
	 * Gets the length.
	 * 
	 * @return the length
	 */
	public long getLength()
	{
		try
		{
			return urlsDB.count();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Sync.
	 */
	public void sync()
	{
		if (resumable)
		{
			return;
		}
		if (urlsDB == null)
		{
			return;
		}
		try
		{
			urlsDB.sync();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Close.
	 */
	public void close()
	{
		try
		{
			urlsDB.close();
			Environment env = urlsDB.getEnvironment();

			Transaction txn = env.beginTransaction(null, null);

			long truncated = env.truncateDatabase(txn, dbName, true);
			// env.removeDatabase(txn, dbName);

			txn.commit();

			System.out.println("truncated urlsDB = " + truncated);

		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
	}
}
