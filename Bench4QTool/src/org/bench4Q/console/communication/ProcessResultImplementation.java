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

package org.bench4Q.console.communication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.bench4Q.agent.messages.CacheHighWaterMark;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.ProcessIdentity;
import org.bench4Q.common.util.AllocateLowestNumber;
import org.bench4Q.common.util.ListenerSupport;
import org.bench4Q.console.messages.ResultAndCacheReport;

/**
 * Handles process status information.
 * 
 */
final class ProcessResultImplementation {

	/**
	 * Period at which to update the listeners.
	 */
	private static final long UPDATE_PERIOD = 500;

	/**
	 * We keep a record of processes for a few seconds after they have been
	 * terminated.
	 * 
	 * Every FLUSH_PERIOD, process statuses are checked. Those haven't reported
	 * for a while are marked and are discarded if they still haven't been
	 * updated by the next FLUSH_PERIOD.
	 */
	private static final long FLUSH_PERIOD = 2000;

	/**
	 * Map of agent identities to AgentAndWorkers instances. Access is
	 * synchronised on the map itself.
	 */
	private final Map m_agentResults = new HashMap();

	/**
	 * We have exclusive write access to m_agentNumberMap.We rely on our
	 * synchronisation on m_agentIdentityToAgentAndWorkers to avoid race
	 * conditions where the timer might otherwise remove an agent immediately
	 * after a new report has just arrived.
	 */
	private final AllocateLowestNumber m_agentNumberMap;

	private final ListenerSupport m_listeners = new ListenerSupport();

	private volatile boolean m_newData = false;

	/**
	 * Constructor.
	 * 
	 * @param timer
	 *            Timer which can be used to schedule housekeeping tasks.
	 * @param agentNumberMap
	 *            Map of {@link AgentIdentity}s to integers.
	 */
	public ProcessResultImplementation(Timer timer,
			AllocateLowestNumber agentNumberMap) {
		m_agentNumberMap = agentNumberMap;
		timer.schedule(new TimerTask() {
			public void run() {
				update();
			}
		}, 0, UPDATE_PERIOD);

		timer.schedule(new TimerTask() {
			public void run() {
				synchronized (m_agentResults) {
					// purge(m_agentResults);
				}
			}
		}, 0, FLUSH_PERIOD);
	}

	/**
	 * Add a new listener.
	 * 
	 * @param listener
	 *            A listener.
	 */
	public void addListener(ProcessControl.ResultListener listener) {
		m_listeners.add(listener);
	}

	/**
	 * How many agents are live?
	 * 
	 * @return The number of agents.
	 */
	public int getNumberOfReceivedResults() {
		synchronized (m_agentResults) {
			return m_agentResults.size();
		}
	}

	private void update() {
		if (!m_newData) {
			return;
		}

		m_newData = false;

		final ReceivedResults[] processStatuses;

		synchronized (m_agentResults) {
			processStatuses = (ReceivedResults[]) m_agentResults.values()
					.toArray(new ReceivedResults[m_agentResults.size()]);
		}

		m_listeners.apply(new ListenerSupport.Informer() {
			public void inform(Object listener) {
				((ProcessControl.ResultListener) listener)
						.update(processStatuses);
			}
		});
	}

	private ReceivedResults getReceivedResults(AgentIdentity agentIdentity) {

		synchronized (m_agentResults) {
			final ReceivedResults existing = (ReceivedResults) m_agentResults
					.get(agentIdentity);

			if (existing != null) {
				return existing;
			}

			final ReceivedResults created = new ReceivedResults(agentIdentity);
			m_agentResults.put(agentIdentity, created);

			m_agentNumberMap.add(agentIdentity);

			return created;
		}
	}

	/**
	 * Add an agent status report.
	 * 
	 * @param testResultMessage
	 *            Process status.
	 */
	public void addAgentTestResultReport(ResultAndCacheReport testResultMessage) {

		final ReceivedResults receivedResults = getReceivedResults(testResultMessage
				.getAgentIdentity());

		receivedResults.setAgentProcessStatus(testResultMessage);

		m_newData = true;
	}

	/**
	 * Callers are responsible for synchronisation.
	 * 
	 * @param purgableMap
	 */
	public void purge(Map purgableMap) {
		final Set zombies = new HashSet();

		final Iterator iterator = purgableMap.entrySet().iterator();

		while (iterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) iterator.next();
			final Object key = entry.getKey();
			final Purgable purgable = (Purgable) entry.getValue();

			if (purgable.shouldPurge()) {
				zombies.add(key);
			}
		}

		if (zombies.size() > 0) {
			purgableMap.keySet().removeAll(zombies);
			m_newData = true;
		}
	}

	private interface Purgable {
		boolean shouldPurge();
	}

	private abstract class AbstractTimedReference implements Purgable {
		private int m_purgeDelayCount;

		public boolean shouldPurge() {
			// Processes have a short time to report - see the javadoc for
			// FLUSH_PERIOD.
			if (m_purgeDelayCount > 0) {
				return true;
			}

			++m_purgeDelayCount;

			return false;
		}
	}

	private final class ResultReference extends AbstractTimedReference {
		private final ResultAndCacheReport m_resultProcessReport;

		ResultReference(ResultAndCacheReport resultProcessReport) {
			m_resultProcessReport = resultProcessReport;
		}

		public ResultAndCacheReport getResultProcessReport() {
			return m_resultProcessReport;
		}

		public boolean shouldPurge() {
			final boolean purge = super.shouldPurge();

			if (purge) {
				// Protected against race with add since the caller holds
				// m_agentIdentityToAgentAndWorkers, and we are about to be
				// removed from m_agentIdentityToAgentAndWorkers.
				m_agentNumberMap.remove(m_resultProcessReport
						.getAgentIdentity());
			}

			return purge;
		}
	}

	private static final class UnknownResultProcessReport implements
			ResultAndCacheReport {

		private final AgentIdentity m_identity;

		public UnknownResultProcessReport(AgentIdentity identity) {
			m_identity = identity;
		}

		public ProcessIdentity getIdentity() {
			return m_identity;
		}

		public AgentIdentity getAgentIdentity() {
			return m_identity;
		}

		public CacheHighWaterMark getCacheHighWaterMark() {
			return null;
		}

		public EBStats getEBStats() {
			return null;
		}
	}

	/**
	 * Implementation of {@link ProcessControl.ProcessReports}.
	 * 
	 * Package scope for unit tests.
	 */
	final class ReceivedResults implements ProcessControl.ResultReports,
			Purgable {

		private volatile ResultReference m_agentReportReference;

		// Synchronise on map before accessing.
		private final Map m_workerReportReferences = new HashMap();

		ReceivedResults(AgentIdentity agentIdentity) {
			setAgentProcessStatus(new UnknownResultProcessReport(agentIdentity));
		}

		void setAgentProcessStatus(ResultAndCacheReport resultProcessStatus) {
			m_agentReportReference = new ResultReference(resultProcessStatus);
		}

		public ResultAndCacheReport getAgentResultReport() {
			return m_agentReportReference.getResultProcessReport();
		}

		public boolean shouldPurge() {
			synchronized (m_workerReportReferences) {
				purge(m_workerReportReferences);
			}

			return m_agentReportReference.shouldPurge();
		}
	}
}
