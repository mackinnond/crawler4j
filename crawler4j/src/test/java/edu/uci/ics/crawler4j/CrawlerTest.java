package edu.uci.ics.crawler4j;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
		// String baseUrl = "http://www.fiat.co.uk/‎";
		// String baseUrl = "http://www.zangbezang.com/";
		String baseUrl = "http://www.empowered-systems.com/";
		//String baseUrl = "http://www.citroen.co.uk/";
		int maxDepth = 10;
		int maxPagesToFetch = 100;
		int numberOfCrawlers = 2;
		int maxWords = 2000;
		String crawlStorageDir = "c://temp";


		
		List<Object> crawlersLocalData = null;

		try
		{

			FileUtils.deleteDirectory(new File(crawlStorageDir + "/frontier"));

			CrawlController controller = new CrawlController(crawlStorageDir);
			//CrawlController controller = new CrawlController(crawlStorageDir, maxWords);
			controller.addSeed(baseUrl);
			controller.setMaximumCrawlDepth(maxDepth);
			controller.setMaximumPagesToFetch(maxPagesToFetch);

			controller.start(MyCrawler.class, numberOfCrawlers);

			StringBuilder allPageText = new StringBuilder();

			crawlersLocalData = controller.getCrawlersLocalData();
			for (Object obj : crawlersLocalData)
			{
				CrawlStat crawlStat = (CrawlStat) obj;
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
