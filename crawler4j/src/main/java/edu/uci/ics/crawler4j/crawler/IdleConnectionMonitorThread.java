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

import java.util.concurrent.TimeUnit;

import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

// TODO: Auto-generated Javadoc
/**
 * The Class IdleConnectionMonitorThread.
 */
public class IdleConnectionMonitorThread extends Thread
{

	/** The conn mgr. */
	private final ThreadSafeClientConnManager connMgr;

	/** The shutdown. */
	private volatile boolean shutdown;

	/**
	 * Instantiates a new idle connection monitor thread.
	 * 
	 * @param connMgr
	 *            the conn mgr
	 */
	public IdleConnectionMonitorThread(ThreadSafeClientConnManager connMgr)
	{
		super("Connection Manager");
		this.connMgr = connMgr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run()
	{
		try
		{
			while (!shutdown)
			{
				synchronized (this)
				{
					wait(5000);
					// Close expired connections
					connMgr.closeExpiredConnections();
					// Optionally, close connections
					// that have been idle longer than 30 sec
					connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
				}
			}
		}
		catch (InterruptedException ex)
		{
			// terminate
		}
	}

	/**
	 * Shutdown.
	 */
	public void shutdown()
	{
		shutdown = true;
		synchronized (this)
		{
			notifyAll();
		}
	}

}
