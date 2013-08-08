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

import java.util.Comparator;
import java.util.EventListener;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.processidentity.ProcessReport;
import org.bench4Q.console.messages.AgentAndCacheReport;
import org.bench4Q.console.messages.ResultAndCacheReport;

/**
 * Interface for issuing commands to the agent and worker processes.
 * 
 */
public interface ProcessControl {

	/**
	 * Signal the worker processes to start.
	 * 
	 * @param properties
	 *            Properties that override the agent's local properties.
	 * @param args
	 */
	void startWorkerProcesses(Bench4QProperties properties, Args args);

	/**
	 * Signal the worker processes to reset.
	 */
	void resetWorkerProcesses();

	/**
	 * Signal the agent and worker processes to stop.
	 */
	void stopAgentAndWorkerProcesses();

	/**
	 * 
	 */
	void collectResultProcesses();

	/**
	 * Add a listener for process status data.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addProcessStatusListener(Listener listener);

	/**
	 * @param resultListener
	 */
	void addProcessResultListener(ResultListener resultListener);

	/**
	 * How many agents are live?
	 * 
	 * @return The number of agents.
	 */
	int getNumberOfLiveAgents();

	/**
	 * Listener interface for receiving updates about process status.
	 */
	public interface Listener extends EventListener {

		/**
		 * Called with regular updates on process status.
		 * 
		 * @param processReports
		 *            Process status information.
		 */
		void update(ProcessReports[] processReports);
	}

	/**
	 * @author duanzhiquan
	 * 
	 */
	public interface ResultListener extends EventListener {

		/**
		 * Called with regular updates on process status.
		 * 
		 * @param receivedResults
		 * 
		 * @param processReports
		 *            Process status information.
		 */
		void update(ResultReports[] receivedResults);
	}

	/**
	 * Interface to the information the console has about an agent and its
	 * worker processes.
	 */
	interface ProcessReports {

		/**
		 * Returns the latest agent process report.
		 * 
		 * @return The agent process report.
		 */
		AgentAndCacheReport getAgentProcessReport();
	}

	/**
	 * @author duanzhiquan
	 * 
	 */
	interface ResultReports {

		/**
		 * Returns the latest agent process report.
		 * 
		 * @return The agent process report.
		 */
		ResultAndCacheReport getAgentResultReport();
	}

	/**
	 * Comparator for {@link ProcessControl.ProcessReports} that sorts according
	 * to the agent report.
	 */
	final class ProcessReportsComparator implements Comparator {
		private final Comparator m_processReportComparator = new ProcessReport.StateThenNameThenNumberComparator();

		public int compare(Object o1, Object o2) {
			return m_processReportComparator.compare(((ProcessReports) o1)
					.getAgentProcessReport(), ((ProcessReports) o2)
					.getAgentProcessReport());
		}
	}
}
