// Copyright (C) 2004 - 2008 Philip Aston
// All rights reserved.
//
// This file is part of The Grinder software distribution. Refer to
// the file LICENSE which is part of The Grinder distribution for
// licensing details. The Grinder distribution is available on the
// Internet at http://grinder.sourceforge.net/
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
 * @author Philip Aston
 * @author Dirk Feufel
 * @version $Revision: 3940 $
 */
public interface ProcessControl {

	/**
	 * Signal the worker processes to start.
	 * 
	 * @param properties
	 *            Properties that override the agent's local properties.
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

	void collectResultProcesses();

	/**
	 * Add a listener for process status data.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addProcessStatusListener(Listener listener);

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

	public interface ResultListener extends EventListener {

		/**
		 * Called with regular updates on process status.
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
			return m_processReportComparator.compare(((ProcessReports) o1).getAgentProcessReport(),
					((ProcessReports) o2).getAgentProcessReport());
		}
	}
}
