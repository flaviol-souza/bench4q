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
package org.bench4Q.agent.messages;

import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.ProcessIdentity;
import org.bench4Q.console.messages.ResultAndCacheReport;

/**
 * @author duanzhiquan
 * 
 */
public final class TestResultMessage implements ResultAndCacheReport, Message {

	private static final long serialVersionUID = 4L;

	private final AgentIdentity m_identity;

	private final CacheHighWaterMark m_cacheHighWaterMark;
	private final EBStats m_stats;

	/**
	 * @param identity
	 * @param stats
	 * @param cacheHighWaterMark
	 */
	public TestResultMessage(AgentIdentity identity, EBStats stats,
			CacheHighWaterMark cacheHighWaterMark) {
		m_identity = identity;
		m_stats = stats;
		m_cacheHighWaterMark = cacheHighWaterMark;
	}

	public EBStats getEBStats() {
		return m_stats;
	}

	public AgentIdentity getAgentIdentity() {
		return m_identity;
	}

	public CacheHighWaterMark getCacheHighWaterMark() {
		return m_cacheHighWaterMark;
	}

	public ProcessIdentity getIdentity() {
		return null;
	}
}
