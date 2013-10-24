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

package edu.uci.ics.crawler4j.example.advanced;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

// TODO: Auto-generated Javadoc
/**
 * The Class MyCrawler.
 */
public class MyCrawler extends WebCrawler
{

	/** The base url. */
	private String baseUrl;

	/** The Constant FILTERS. */
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|doc|js|bmp|gif|jpe?g"
			+ "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	/** The my crawl stat. */
	CrawlStat myCrawlStat;

	/**
	 * Instantiates a new my crawler.
	 */
	public MyCrawler()
	{
		myCrawlStat = new CrawlStat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#shouldVisit(edu.uci.ics.crawler4j.url.WebURL)
	 */
	public boolean shouldVisit(WebURL url)
	{
		if (url == null)
		{
			return false;
		}

		if (url.getURL() == null)
		{
			return false;
		}

		String href = url.getURL().toLowerCase();

		// Save base Url and check that we have not left site...
		if (baseUrl == null)
		{
			int lastPos = href.indexOf("//") + 2;
			lastPos = href.indexOf("/", lastPos);
			if (lastPos == -1)
			{
				baseUrl = href;
			}
			else
			{
				baseUrl = href.substring(0, lastPos);
			}
		}

		if (!href.startsWith(baseUrl))
		{
			System.out.println("Excluded external site  : " + href);
			return false;
		}

		if (FILTERS.matcher(href).matches())
		{
			System.out.println("Exclude : " + href);
			return false;
		}
		if (href.contains(".ico"))
		{
			System.out.println("Exclude : " + href);
			return false;
		}
		else if (href.contains(".css"))
		{
			System.out.println("Exclude : " + href);
			return false;
		}
		System.out.println("Include : " + href);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#visit(edu.uci.ics.crawler4j.crawler.Page)
	 */
	public void visit(Page page)
	{
		System.out.println("visit()");

		myCrawlStat.incProcessedPages();

		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		int parentDocid = page.getWebURL().getParentDocid();
		System.out.println("\t Docid: " + docid);
		System.out.println("\t URL: " + url);
		System.out.println("\t Docid of parent page: " + parentDocid);
		String text = page.getText();

		//String html = page.getHTML();

		myCrawlStat.getTextBuff().append(text + "\n ");
		myCrawlStat.getTextBuff().append("========\n ");

		myCrawlStat.incProcessedPages();
		List<WebURL> links = page.getURLs();
		myCrawlStat.incTotalLinks(links.size());
		try
		{
			myCrawlStat.incTotalTextSize(page.getText().getBytes("UTF-8").length);
		}
		catch (UnsupportedEncodingException e)
		{
		}
		// We dump this crawler statistics after processing every 50 pages
		if (myCrawlStat.getTotalProcessedPages() % 50 == 0)
		{
			dumpMyData();
		}
	}

	// This function is called by controller to get the local data of this crawler when job is finished
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#getMyLocalData()
	 */
	public Object getMyLocalData()
	{
		return myCrawlStat;
	}

	// This function is called by controller before finishing the job.
	// You can put whatever stuff you need here.
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uci.ics.crawler4j.crawler.WebCrawler#onBeforeExit()
	 */
	public void onBeforeExit()
	{
		dumpMyData();
	}

	/**
	 * Dump my data.
	 */
	public void dumpMyData()
	{
		int myId = getMyId();
		// This is just an example. Therefore I print on screen. You may probably want to write in a text file.
		System.out.println("Crawler " + myId + "> Processed Pages: " + myCrawlStat.getTotalProcessedPages());
		System.out.println("Crawler " + myId + "> Total Links Found: " + myCrawlStat.getTotalLinks());
		System.out.println("Crawler " + myId + "> Total Text Size: " + myCrawlStat.getTotalTextSize());
	}
}
