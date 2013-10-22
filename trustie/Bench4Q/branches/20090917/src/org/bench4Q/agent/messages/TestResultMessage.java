package org.bench4Q.agent.messages;

import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.ProcessIdentity;
import org.bench4Q.console.messages.ResultAndCacheReport;

public final class TestResultMessage implements ResultAndCacheReport, Message {

	private static final long serialVersionUID = 4L;

	private final AgentIdentity m_identity;

	private final CacheHighWaterMark m_cacheHighWaterMark;
	private final EBStats m_stats;

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
