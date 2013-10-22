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
package org.bench4Q.console.common;

import org.bench4Q.agent.AgentIdentityImplementation;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.AgentProcessReport;

/**
 * Factory that converts process reports into descriptions that can be used in
 * the user interface.
 * 
 */
public final class ProcessReportDescriptionFactory {
	private final String m_threadsString;
	private final String m_agentString;
	private final String m_workerString;
	private final String m_stateStartedString;
	private final String m_stateRunningString;
	private final String m_stateFinishedString;
	private final String m_stateConnectedString;
	private final String m_stateDisconnectedString;
	private final String m_stateUnknownString;

	/**
	 * Constructor.
	 * 
	 * @param resources
	 *            Console resources.
	 */
	public ProcessReportDescriptionFactory(Resources resources) {
		m_threadsString = resources.getString("processTable.threads.label");

		m_agentString = resources.getString("processTable.agentProcess.label");
		m_workerString = resources
				.getString("processTable.workerProcess.label");

		m_stateStartedString = resources
				.getString("processState.started.label");
		m_stateRunningString = resources
				.getString("processState.running.label");
		m_stateFinishedString = resources
				.getString("processState.finished.label");
		m_stateConnectedString = resources
				.getString("processState.connected.label");
		m_stateDisconnectedString = resources
				.getString("processState.disconnected.label");
		m_stateUnknownString = resources
				.getString("processState.unknown.label");
	}

	/**
	 * Factory method that creates a description from an agent process report.
	 * 
	 * @param agentProcessReport
	 *            The process report.
	 * @return The description.
	 */
	public ProcessDescription create(AgentProcessReport agentProcessReport) {

		final String state;

		switch (agentProcessReport.getState()) {
		case AgentProcessReport.STATE_STARTED:
			state = m_stateConnectedString;
			break;
		case AgentProcessReport.STATE_RUNNING:
			state = m_stateRunningString;
			break;

		case AgentProcessReport.STATE_FINISHED:
			state = m_stateFinishedString;
			break;

		case AgentProcessReport.STATE_UNKNOWN:
		default:
			state = m_stateUnknownString;
			break;
		}

		final AgentIdentity agentIdentity = agentProcessReport
				.getAgentIdentity();

		final StringBuffer name = new StringBuffer(agentIdentity.getName());

		if (agentIdentity.getNumber() >= 0) {
			name.append(" (");
			name.append(m_agentString);
			name.append(" ");
			name.append(agentIdentity.getNumber());
			name.append(")");
		}

		return new ProcessDescription(
				name.toString(),
				((AgentIdentityImplementation) agentProcessReport.getIdentity())
						.getIP(), state);
	}

	/**
	 * Various descriptions of the attributes of a process report.
	 */
	public static final class ProcessDescription {
		private final String m_name;
		private final String m_IP;
		private final String m_state;

		private ProcessDescription(String name, String IP, String state) {
			m_name = name;
			m_IP = IP;
			m_state = state;
		}

		/**
		 * The process name.
		 * 
		 * @return The name.
		 */
		public String getName() {
			return m_name;
		}

		/**
		 * Description of the process type.
		 * 
		 * @return The process type.
		 */
		public String getIP() {
			return m_IP;
		}

		/**
		 * Description of the process state.
		 * 
		 * @return The process state.
		 */
		public String getState() {
			return m_state;
		}

		/**
		 * All descriptions in one string.
		 * 
		 * @return The descriptions.
		 */
		public String toString() {
			return getIP() + " " + getName() + " [" + getState() + "]";
		}
	}
}
