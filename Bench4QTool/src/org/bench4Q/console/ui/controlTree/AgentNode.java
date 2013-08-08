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
package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;

import org.bench4Q.agent.AgentIdentityImplementation;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.ui.transfer.AgentInfo;

/**
 * @author duanzhiquan
 * 
 */
public class AgentNode implements TestElement {

	private AgentInfo m_agentInfo;
	private final Resources m_resources;

	private String m_name;
	private String m_tip;

	/**
	 * @param agentInfo
	 * @param resources
	 */
	public AgentNode(AgentInfo agentInfo, Resources resources) {
		m_agentInfo = agentInfo;
		m_resources = resources;
		// m_name = m_agentInfo.getAgentIdentity().getName() + "("
		// + m_agentInfo.getAgentIdentity().getNumber() + ")";
		m_name = "AGENT(" + m_agentInfo.getAgentIdentity().getName() + ")";
		m_tip = ((AgentIdentityImplementation) m_agentInfo.getAgentIdentity())
				.getIP();
	}

	public String getTip() {
		return m_tip;
	}

	public String getName() {
		return m_name;
	}

	/**
	 * @return
	 */
	public AgentInfo getAgentInfo() {
		return m_agentInfo;
	}

	public ImageIcon getImageIcon() {
		return m_resources.getImageIcon("ControlTree.Agent.image");
	}

	public String toString() {
		return m_name;
	}
}
