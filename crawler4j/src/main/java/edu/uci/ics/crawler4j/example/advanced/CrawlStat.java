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

// TODO: Auto-generated Javadoc
/**
 * The Class CrawlStat.
 */
public class CrawlStat
{

	/** The total processed pages. */
	private int totalProcessedPages;

	/** The total links. */
	private long totalLinks;

	/** The total text size. */
	private long totalTextSize;

	/** The text buff. */
	private StringBuilder textBuff;

	/**
	 * Instantiates a new crawl stat.
	 */
	public CrawlStat()
	{
		textBuff = new StringBuilder(1024);
	}

	/**
	 * Gets the total processed pages.
	 * 
	 * @return the total processed pages
	 */
	public int getTotalProcessedPages()
	{
		return totalProcessedPages;
	}

	/**
	 * Sets the total processed pages.
	 * 
	 * @param totalProcessedPages
	 *            the new total processed pages
	 */
	public void setTotalProcessedPages(int totalProcessedPages)
	{
		this.totalProcessedPages = totalProcessedPages;
	}

	/**
	 * Inc processed pages.
	 */
	public void incProcessedPages()
	{
		this.totalProcessedPages++;
	}

	/**
	 * Gets the total links.
	 * 
	 * @return the total links
	 */
	public long getTotalLinks()
	{
		return totalLinks;
	}

	/**
	 * Sets the total links.
	 * 
	 * @param totalLinks
	 *            the new total links
	 */
	public void setTotalLinks(long totalLinks)
	{
		this.totalLinks = totalLinks;
	}

	/**
	 * Gets the total text size.
	 * 
	 * @return the total text size
	 */
	public long getTotalTextSize()
	{
		return totalTextSize;
	}

	/**
	 * Sets the total text size.
	 * 
	 * @param totalTextSize
	 *            the new total text size
	 */
	public void setTotalTextSize(long totalTextSize)
	{
		this.totalTextSize = totalTextSize;
	}

	/**
	 * Inc total links.
	 * 
	 * @param count
	 *            the count
	 */
	public void incTotalLinks(int count)
	{
		this.totalLinks += count;
	}

	/**
	 * Inc total text size.
	 * 
	 * @param count
	 *            the count
	 */
	public void incTotalTextSize(int count)
	{
		this.totalTextSize += count;
	}

	/**
	 * Gets the text buff.
	 * 
	 * @return the text buff
	 */
	public StringBuilder getTextBuff()
	{
		return textBuff;
	}

	/**
	 * Sets the text buff.
	 * 
	 * @param textBuff
	 *            the new text buff
	 */
	public void setTextBuff(StringBuilder textBuff)
	{
		this.textBuff = textBuff;
	}
}
