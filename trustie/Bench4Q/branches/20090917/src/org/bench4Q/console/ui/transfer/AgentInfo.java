package org.bench4Q.console.ui.transfer;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.processidentity.AgentIdentity;

public class AgentInfo {

	private final AgentIdentity m_agentIdentity;
	private Args args;
	private EBStats m_stats;

	public AgentInfo(AgentIdentity agentIdentity) {
		m_agentIdentity = agentIdentity;
	}

	public AgentIdentity getAgentIdentity() {
		return m_agentIdentity;
	}

	public Args getArgs() {
		return args;
	}

	public void setArgs(Args args) {
		this.args = args;
	}

	public EBStats getStats() {
		return m_stats;
	}

	public void setStats(EBStats stats) {
		this.m_stats = stats;
	}

}
