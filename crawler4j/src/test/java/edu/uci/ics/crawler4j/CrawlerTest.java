package edu.uci.ics.crawler4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.example.advanced.CrawlStat;
import edu.uci.ics.crawler4j.example.advanced.MyCrawler;

public class CrawlerTest
{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		// String baseUrl = "http://turnbull.mcs.st-andrews.ac.uk/~edmund";
		// String baseUrl = "http://www.spark-online.com/august00/media/romano.html";
		// String baseUrl = "http://www.bmw.com/";
		// String baseUrl = "http://www.citroen.co.uk/";

		List<String> websiteUrls = new ArrayList<String>();
		websiteUrls.add("http://www.audi.com/");
		websiteUrls.add("http://www.bmw.com/");
		websiteUrls.add("http://animated-divots.net");
		websiteUrls.add("http://www.fiat.co.uk/");
		websiteUrls.add("http://www.zangbezang.com");
		websiteUrls.add("http://www.citroen.co.uk");
		websiteUrls.add("http://nnanime.com");

		int maxDepth = 10;
		int maxPagesToFetch = 100;
		int numberOfCrawlers = 20;
		String crawlStorageDir = "c://temp";

		Map<String, Integer> urlDataMap = new HashMap<String, Integer>();

		for (String baseUrl : websiteUrls)
		{

			String data = getWebsiteData(baseUrl, maxDepth, maxPagesToFetch, numberOfCrawlers, crawlStorageDir);
			System.out.println("All text size: " + data.length());

			urlDataMap.put(baseUrl, new Integer(data.length()));
		}

		Iterator<Entry<String, Integer>> iter = urlDataMap.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<String, Integer> mapEntry = iter.next();
			System.out.println("Url =  " + mapEntry.getKey()  + " " + mapEntry.getValue());
		}

	}

	/**
	 * Gets the website data.
	 * 
	 * @param baseUrl
	 *            the base url
	 * @param maxDepth
	 *            the max depth
	 * @param maxPagesToFetch
	 *            the max pages to fetch
	 * @param numberOfCrawlers
	 *            the number of crawlers
	 * @param crawlStorageDir
	 *            the crawl storage dir
	 * @return the website data
	 */
	private String getWebsiteData(String baseUrl, int maxDepth, int maxPagesToFetch, int numberOfCrawlers,
			String crawlStorageDir)
	{
		StringBuilder allPageText = new StringBuilder();

		try
		{
			CrawlController controller = new CrawlController(crawlStorageDir, false);
			// CrawlController controller = new CrawlController(crawlStorageDir, maxWords);
			controller.addSeed(baseUrl);
			controller.setMaximumCrawlDepth(maxDepth);
			controller.setMaximumPagesToFetch(maxPagesToFetch);

			controller.start(MyCrawler.class, numberOfCrawlers);

			List<Object> crawlersLocalData = controller.getCrawlersLocalData();
			for (Object obj : crawlersLocalData)
			{
				CrawlStat crawlStat = (CrawlStat) obj;
				allPageText.append(crawlStat.getTextBuff().toString());
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allPageText.toString();
	}

}
