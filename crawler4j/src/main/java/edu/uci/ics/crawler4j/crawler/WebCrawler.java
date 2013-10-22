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

import java.util.*;

import org.apache.log4j.Logger;

import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.URLCanonicalizer;
import edu.uci.ics.crawler4j.url.WebURL;

// TODO: Auto-generated Javadoc
/**
 * The Class WebCrawler.
 * 
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

public class WebCrawler implements Runnable
{

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(WebCrawler.class.getName());

	/** The my thread. */
	private Thread myThread;

	/** The Constant PROCESS_OK. */
	private final static int PROCESS_OK = -12;

	/** The html parser. */
	private HTMLParser htmlParser;

	/** The myid. */
	int myid;

	/** The my controller. */
	private CrawlController myController;

	/** The max crawl depth. */
	private static short MAX_CRAWL_DEPTH = Configurations.getShortProperty("crawler.max_depth", (short) -1);

	/** The ignore binary content. */
	private static boolean IGNORE_BINARY_CONTENT = !Configurations.getBooleanProperty("crawler.include_binary_content",
			false);

	/** The Constant FOLLOW_REDIRECTS. */
	private static final boolean FOLLOW_REDIRECTS = Configurations.getBooleanProperty("fetcher.follow_redirects", true);

	/**
	 * Gets the my controller.
	 * 
	 * @return the my controller
	 */
	public CrawlController getMyController()
	{
		return myController;
	}

	/**
	 * Sets the my controller.
	 * 
	 * @param myController
	 *            the new my controller
	 */
	public void setMyController(CrawlController myController)
	{
		this.myController = myController;
	}

	/**
	 * Instantiates a new web crawler.
	 */
	public WebCrawler()
	{
		htmlParser = new HTMLParser();
	}

	/**
	 * Instantiates a new web crawler.
	 * 
	 * @param myid
	 *            the myid
	 */
	public WebCrawler(int myid)
	{
		this.myid = myid;
	}

	/**
	 * Sets the my id.
	 * 
	 * @param myid
	 *            the new my id
	 */
	public void setMyId(int myid)
	{
		this.myid = myid;
	}

	/**
	 * Gets the my id.
	 * 
	 * @return the my id
	 */
	public int getMyId()
	{
		return myid;
	}

	/**
	 * On start.
	 */
	public void onStart()
	{

	}

	/**
	 * On before exit.
	 */
	public void onBeforeExit()
	{

	}

	/**
	 * Gets the my local data.
	 * 
	 * @return the my local data
	 */
	public Object getMyLocalData()
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		logger.info("run() ");
		onStart();

		while (true)
		{
			List<WebURL> assignedURLs = new ArrayList<WebURL>(50);
			Frontier.getNextURLs(50, assignedURLs);
			if (assignedURLs.size() == 0)
			{
				if (Frontier.isFinished())
				{
					return;
				}
				try
				{
					Thread.sleep(3000);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				for (WebURL curURL : assignedURLs)
				{
					if (curURL != null)
					{
						if (curURL.getDocid() == 14)
						{
							System.out.println();
						}
						processPage(curURL);
						Frontier.setProcessed(curURL);
					}
				}
			}
		}
	}

	/**
	 * Should visit.
	 * 
	 * @param url
	 *            the url
	 * @return true, if successful
	 */
	public boolean shouldVisit(WebURL url)
	{
		return true;
	}

	/**
	 * Visit.
	 * 
	 * @param page
	 *            the page
	 */
	public void visit(Page page)
	{
		// Should be implemented in sub classes
	}

	/**
	 * Process page.
	 * 
	 * @param curURL
	 *            the cur url
	 * @return the int
	 */
	private int processPage(WebURL curURL)
	{
		logger.info("processPage() " + curURL.getURL());

		if (curURL == null)
		{
			return -1;
		}
		Page page = new Page(curURL);
		int statusCode = PageFetcher.fetch(page, IGNORE_BINARY_CONTENT);
		// The page might have been redirected. So we have to refresh curURL
		curURL = page.getWebURL();
		int docid = curURL.getDocid();
		if (statusCode != PageFetchStatus.OK)
		{
			if (statusCode == PageFetchStatus.Moved)
			{
				if (FOLLOW_REDIRECTS)
				{
					String movedToUrl = curURL.getURL();
					if (movedToUrl == null)
					{
						return PageFetchStatus.MovedToUnknownLocation;
					}
					movedToUrl = URLCanonicalizer.getCanonicalURL(movedToUrl);
					if (movedToUrl == null)
					{
						// Null url so just ignore
						return PageFetchStatus.PageTooBig;
					}

					int newdocid = DocIDServer.getDocID(movedToUrl);
					if (newdocid > 0)
					{
						return PageFetchStatus.RedirectedPageIsSeen;
					}
					else
					{
						WebURL webURL = new WebURL();
						webURL.setURL(movedToUrl);
						webURL.setParentDocid(curURL.getParentDocid());
						webURL.setDepth((short) (curURL.getDepth()));
						webURL.setDocid(-1);
						if (shouldVisit(webURL) && RobotstxtServer.allows(webURL))
						{
							webURL.setDocid(DocIDServer.getNewDocID(movedToUrl));
							Frontier.schedule(webURL);
						}
					}
				}
				return PageFetchStatus.Moved;
			}
			else if (statusCode == PageFetchStatus.PageTooBig)
			{
				logger.error("Page was bigger than max allowed size: " + curURL.getURL());
			}
			return statusCode;
		}

		try
		{
			if (!page.isBinary())
			{
				htmlParser.parse(page.getHTML(), curURL.getURL());
				page.setText(htmlParser.getText());
				page.setTitle(htmlParser.getTitle());

				if (page.getText() == null)
				{
					return PageFetchStatus.NotInTextFormat;
				}

				Iterator<String> it = htmlParser.getLinks().iterator();
				List<WebURL> toSchedule = new ArrayList<WebURL>();
				List<WebURL> toList = new ArrayList<WebURL>();
				while (it.hasNext())
				{
					String url = it.next();
					if (url != null)
					{
						int newdocid = DocIDServer.getDocID(url);
						if (newdocid > 0)
						{
							if (newdocid != docid)
							{
								WebURL webURL = new WebURL();
								webURL.setURL(url);
								webURL.setDocid(newdocid);
								toList.add(webURL);
							}
						}
						else
						{
							WebURL webURL = new WebURL();
							webURL.setURL(url);
							webURL.setDocid(-1);
							webURL.setParentDocid(docid);
							webURL.setDepth((short) (curURL.getDepth() + 1));
							if (shouldVisit(webURL) && RobotstxtServer.allows(webURL))
							{
								if (MAX_CRAWL_DEPTH == -1 || curURL.getDepth() < MAX_CRAWL_DEPTH)
								{
									webURL.setDocid(DocIDServer.getNewDocID(url));
									toSchedule.add(webURL);
									toList.add(webURL);
								}
							}
						}
					}
				}
				Frontier.scheduleAll(toSchedule);
				page.setURLs(toList);
			}
			visit(page);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage() + ", while processing: " + curURL.getURL());
		}
		return PROCESS_OK;
	}

	/**
	 * Gets the thread.
	 * 
	 * @return the thread
	 */
	public Thread getThread()
	{
		return myThread;
	}

	/**
	 * Sets the thread.
	 * 
	 * @param myThread
	 *            the new thread
	 */
	public void setThread(Thread myThread)
	{
		this.myThread = myThread;
	}

	/**
	 * Sets the maximum crawl depth.
	 * 
	 * @param depth
	 *            the new maximum crawl depth
	 */
	public static void setMaximumCrawlDepth(short depth)
	{
		MAX_CRAWL_DEPTH = depth;
	}
}
