package edu.uci.ics.crawler4j;

import java.util.List;

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
	public void test2()
	{

		try
		{
			int numberOfCrawlers = 10;

			CrawlController controller = new CrawlController("c://temp");
			controller.addSeed("http://www.ics.uci.edu/");
			controller.start(MyCrawler.class, numberOfCrawlers);

			List<Object> crawlersLocalData = controller.getCrawlersLocalData();
			long totalLinks = 0;
			long totalTextSize = 0;
			int totalProcessedPages = 0;
			for (Object localData : crawlersLocalData)
			{
				CrawlStat stat = (CrawlStat) localData;
				totalLinks += stat.getTotalLinks();
				totalTextSize += stat.getTotalTextSize();
				totalProcessedPages += stat.getTotalProcessedPages();
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test()
	{
		//String baseUrl = "http://www.fiat.co.uk/‎";
		//String baseUrl = "http://www.zangbezang.com/";
		String baseUrl = "http://www.empowered-systems.com/";
		int maxDepth = 10;
		int maxPagesToFetch = 100;
		int numberOfCrawlers = 10;
		String crawlStorageDir = "c://temp";
		List<Object> crawlersLocalData = null;

		try
		{

			CrawlController controller = new CrawlController(crawlStorageDir);
			controller.addSeed(baseUrl);
			controller.setMaximumCrawlDepth(maxDepth);
			controller.setMaximumPagesToFetch(maxPagesToFetch);

			controller.start(MyCrawler.class, numberOfCrawlers);

			StringBuilder allPageText = new StringBuilder();
			
			crawlersLocalData = controller.getCrawlersLocalData();
			for(Object obj : crawlersLocalData)
			{
				CrawlStat  crawlStat = (CrawlStat) obj;
				allPageText.append(crawlStat.getTextBuff().toString());				
			}
			
			System.out.println("All text : " + allPageText.toString());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("All text : ");

	}

}
