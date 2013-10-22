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
package org.bench4Q.console.ui.transfer;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.processidentity.AgentIdentity;

/**
 * @author duanzhiquan
 * 
 */
public class AgentInfo {

	private final AgentIdentity m_agentIdentity;
	private Args args;
	private EBStats m_stats;

	/**
	 * @param agentIdentity
	 */
	public AgentInfo(AgentIdentity agentIdentity) {
		m_agentIdentity = agentIdentity;
	}

	/**
	 * @return
	 */
	public AgentIdentity getAgentIdentity() {
		return m_agentIdentity;
	}

	/**
	 * @return
	 */
	public Args getArgs() {
		return args;
	}

	/**
	 * @param args
	 */
	public void setArgs(Args args) {
		this.args = args;
	}

	/**
	 * @return
	 */
	public EBStats getStats() {
		return m_stats;
	}

	/**
	 * @param stats
	 */
	public void setStats(EBStats stats) {
		this.m_stats = stats;
	}

}
