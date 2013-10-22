/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there. 
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.agent.rbe;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * @author duanzhiquan
 * 
 */
public class HttpClientFactory {

	private static HttpClient m_client;

	private static int DEFAULT_RETRY_COUNT = 3;

	static {
		if (m_client == null) {
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			connectionManager.setMaxConnectionsPerHost(10000);
			connectionManager.setMaxTotalConnections(10000);
			m_client = new HttpClient(connectionManager);

			DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
			retryhandler.setRetryCount(DEFAULT_RETRY_COUNT);
			m_client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					retryhandler);
		}
	}

	/**
	 * get a HttpClient.
	 * 
	 * @return HttpClient
	 */
	public static HttpClient getInstance() {
		if (m_client == null) {
			MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
			connectionManager.setMaxConnectionsPerHost(10000);
			connectionManager.setMaxTotalConnections(10000);
			m_client = new HttpClient(connectionManager);

			DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
			retryhandler.setRetryCount(DEFAULT_RETRY_COUNT);
			m_client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					retryhandler);
		}
		return m_client;

	}

	/**
	 * set retry count.
	 * 
	 * @param count
	 */
	public static void setRetryCount(int count) {
		if (m_client != null) {
			DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
			retryhandler.setRetryCount(count);
			m_client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					retryhandler);
		}

	}

	/**
	 * When HttpClient instance is no longer needed, shut down the connection
	 * manager to ensure immediate deallocation of all system resources
	 * 
	 * @param client
	 */
	public static void closeClient(HttpClient client) {

	}

}
