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

package edu.uci.ics.crawler4j.url;

import java.io.*;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

// TODO: Auto-generated Javadoc
/**
 * The Class WebURL.
 * 
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

@Entity
public final class WebURL implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The url. */
	@PrimaryKey
	private String url;

	/** The docid. */
	private int docid;

	/** The parent docid. */
	private int parentDocid;

	/** The depth. */
	private short depth;

	/**
	 * Gets the docid.
	 * 
	 * @return the docid
	 */
	public int getDocid()
	{
		return docid;
	}

	/**
	 * Sets the docid.
	 * 
	 * @param docid
	 *            the new docid
	 */
	public void setDocid(int docid)
	{
		this.docid = docid;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getURL()
	{
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		WebURL url2 = (WebURL) o;
		if (url == null)
		{
			return false;
		}
		return url.equals(url2.getURL());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return url;
	}

	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */
	public void setURL(String url)
	{
		this.url = url;
	}

	/**
	 * Gets the parent docid.
	 * 
	 * @return the parent docid
	 */
	public int getParentDocid()
	{
		return parentDocid;
	}

	/**
	 * Sets the parent docid.
	 * 
	 * @param parentDocid
	 *            the new parent docid
	 */
	public void setParentDocid(int parentDocid)
	{
		this.parentDocid = parentDocid;
	}

	/**
	 * Gets the depth.
	 * 
	 * @return the depth
	 */
	public short getDepth()
	{
		return depth;
	}

	/**
	 * Sets the depth.
	 * 
	 * @param depth
	 *            the new depth
	 */
	public void setDepth(short depth)
	{
		this.depth = depth;
	}
}
