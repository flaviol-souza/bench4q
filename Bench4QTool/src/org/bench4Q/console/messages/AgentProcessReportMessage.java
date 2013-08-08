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

package org.bench4Q.console.messages;

import org.bench4Q.agent.messages.CacheHighWaterMark;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.ProcessIdentity;

/**
 * Message for informing the console of agent process status.
 */
public final class AgentProcessReportMessage implements AgentAndCacheReport,
		Message {

	private static final long serialVersionUID = 3L;

	private final AgentIdentity m_identity;
	private final short m_state;
	private final CacheHighWaterMark m_cacheHighWaterMark;

	/**
	 * Creates a new <code>AgentProcessReportMessage</code> instance.
	 * 
	 * @param identity
	 *            Process identity.
	 * @param state
	 *            The process state. See
	 *            {@link org.bench4Q.common.processidentity.AgentProcessReport}.
	 * @param cacheHighWaterMark
	 *            The current cache status.
	 */
	public AgentProcessReportMessage(AgentIdentity identity, short state,
			CacheHighWaterMark cacheHighWaterMark) {
		m_identity = identity;
		m_state = state;
		m_cacheHighWaterMark = cacheHighWaterMark;
	}

	/**
	 * Accessor for the process identity.
	 * 
	 * @return The process identity.
	 */
	public ProcessIdentity getIdentity() {
		return m_identity;
	}

	/**
	 * Accessor for the process identity.
	 * 
	 * @return The process identity.
	 */
	public AgentIdentity getAgentIdentity() {
		return m_identity;
	}

	/**
	 * Accessor for the process state.
	 * 
	 * @return The process state.
	 */
	public short getState() {
		return m_state;
	}

	/**
	 * Accessor for the cache status.
	 * 
	 * @return The cache status.
	 */
	public CacheHighWaterMark getCacheHighWaterMark() {
		return m_cacheHighWaterMark;
	}
}
