package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;

import org.bench4Q.agent.AgentIdentityImplementation;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.ui.transfer.AgentInfo;

public class AgentNode implements TestElement {

	private AgentInfo m_agentInfo;
	private final Resources m_resources;

	private String m_name;
	private String m_tip;

	public AgentNode(AgentInfo agentInfo, Resources resources) {
		m_agentInfo = agentInfo;
		m_resources = resources;
		// m_name = m_agentInfo.getAgentIdentity().getName() + "("
		// + m_agentInfo.getAgentIdentity().getNumber() + ")";
		m_name = "AGENT(" + m_agentInfo.getAgentIdentity().getName() + ")";
		m_tip = ((AgentIdentityImplementation) m_agentInfo.getAgentIdentity()).getIP();
	}

	public String getTip() {
		return m_tip;
	}

	public String getName() {
		return m_name;
	}

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
