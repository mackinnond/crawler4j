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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;

import edu.uci.ics.crawler4j.crawler.Configurations;
import edu.uci.ics.crawler4j.url.WebURL;

// TODO: Auto-generated Javadoc
/**
 * The Class Frontier.
 * 
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

public final class Frontier
{

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(Frontier.class.getName());

	/** The work queues. */
	private static WorkQueues workQueues;

	/** The inprocess pages. */
	private static InProcessPagesDB inprocessPages;

	/** The mutex. */
	private static Object mutex = Frontier.class.toString() + "_Mutex";

	/** The waiting list. */
	private static Object waitingList = Frontier.class.toString() + "_WaitingList";

	/** The is finished. */
	private static boolean isFinished = false;

	/** The max pages to fetch. */
	private static int maxPagesToFetch = Configurations.getIntProperty("crawler.max_pages_to_fetch", -1);

	/** The scheduled pages. */
	private static int scheduledPages;

	/**
	 * Inits the.
	 * 
	 * @param env
	 *            the env
	 * @param resumable
	 *            the resumable
	 */
	public static void init(Environment env, boolean resumable)
	{
		try
		{

			// Init
			isFinished = false;
			scheduledPages = 0;

			workQueues = new WorkQueues(env, "PendingURLsDB", resumable);
			if (resumable)
			{
				inprocessPages = new InProcessPagesDB(env);
				long docCount = inprocessPages.getLength();
				if (docCount > 0)
				{
					logger.info("Rescheduling " + docCount + " URLs from previous crawl.");
					while (true)
					{
						List<WebURL> urls = inprocessPages.get(100);
						if (urls.size() == 0)
						{
							break;
						}
						inprocessPages.delete(urls.size());
						scheduleAll(urls);
					}
				}
			}
			else
			{
				inprocessPages = null;
				scheduledPages = 0;
			}
		}
		catch (DatabaseException e)
		{
			logger.error("Error while initializing the Frontier: " + e.getMessage());
			workQueues = null;
		}
	}

	/**
	 * Schedule all.
	 * 
	 * @param urls
	 *            the urls
	 */
	public static void scheduleAll(List<WebURL> urls)
	{
		synchronized (mutex)
		{
			Iterator<WebURL> it = urls.iterator();
			while (it.hasNext())
			{
				WebURL url = it.next();
				if (maxPagesToFetch < 0 || scheduledPages < maxPagesToFetch)
				{
					try
					{
						workQueues.put(url);
						scheduledPages++;
					}
					catch (DatabaseException e)
					{
						logger.error("Error while puting the url in the work queue.");
					}
				}
			}
			synchronized (waitingList)
			{
				waitingList.notifyAll();
			}
		}
	}

	/**
	 * Schedule.
	 * 
	 * @param url
	 *            the url
	 */
	public static void schedule(WebURL url)
	{
		synchronized (mutex)
		{
			try
			{
				if (maxPagesToFetch < 0 || scheduledPages < maxPagesToFetch)
				{
					workQueues.put(url);
					scheduledPages++;
				}
			}
			catch (DatabaseException e)
			{
				logger.error("Error while puting the url in the work queue.");
			}
		}
	}

	/**
	 * Gets the next ur ls.
	 * 
	 * @param max
	 *            the max
	 * @param result
	 *            the result
	 * @return the next ur ls
	 */
	public static void getNextURLs(int max, List<WebURL> result)
	{
		while (true)
		{
			synchronized (mutex)
			{
				try
				{
					List<WebURL> curResults = workQueues.get(max);
					workQueues.delete(curResults.size());
					if (inprocessPages != null)
					{
						for (WebURL curPage : curResults)
						{
							inprocessPages.put(curPage);
						}
					}
					result.addAll(curResults);
				}
				catch (DatabaseException e)
				{
					logger.error("Error while getting next urls: " + e.getMessage());
					e.printStackTrace();
				}
				if (result.size() > 0)
				{
					return;
				}
			}
			try
			{
				synchronized (waitingList)
				{
					waitingList.wait();
				}
			}
			catch (InterruptedException e)
			{
			}
			if (isFinished)
			{
				return;
			}
		}
	}

	/**
	 * Sets the processed.
	 * 
	 * @param webURL
	 *            the new processed
	 */
	public static void setProcessed(WebURL webURL)
	{
		if (inprocessPages != null)
		{
			if (!inprocessPages.removeURL(webURL))
			{
				logger.warn("Could not remove: " + webURL.getURL() + " from list of processed pages.");
			}
		}
	}

	/**
	 * Gets the queue length.
	 * 
	 * @return the queue length
	 */
	public static long getQueueLength()
	{
		return workQueues.getLength();
	}

	/**
	 * Gets the number of assigned pages.
	 * 
	 * @return the number of assigned pages
	 */
	public static long getNumberOfAssignedPages()
	{
		return inprocessPages.getLength();
	}

	/**
	 * Sync.
	 */
	public static void sync()
	{
		workQueues.sync();
		DocIDServer.sync();
	}

	/**
	 * Checks if is finished.
	 * 
	 * @return true, if is finished
	 */
	public static boolean isFinished()
	{
		return isFinished;
	}

	/**
	 * Sets the maximum pages to fetch.
	 * 
	 * @param max
	 *            the new maximum pages to fetch
	 */
	public static void setMaximumPagesToFetch(int max)
	{
		maxPagesToFetch = max;
	}

	/**
	 * Close.
	 */
	public static void close()
	{
		sync();
		workQueues.close();
		DocIDServer.close();
		inprocessPages.close();

	}

	/**
	 * Finish.
	 */
	public static void finish()
	{
		isFinished = true;
		synchronized (waitingList)
		{
			waitingList.notifyAll();
		}
	}
}
