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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.processidentity.AgentProcessReport;
import org.bench4Q.common.processidentity.AgentResultReport;
import org.bench4Q.common.util.ListenerSupport;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;

/**
 * @author duanzhiquan
 * 
 */
public class AgentsCollection implements AgentsCollectionSubject {

	private Map agents;
	private ArrayList observers;
	private final ListenerSupport m_listeners = new ListenerSupport();

	private final ProcessControl m_processControl;

	/**
	 * @param resources
	 * @param processControl
	 * @param swingDispatcherFactory
	 */
	public AgentsCollection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory swingDispatcherFactory) {
		m_processControl = processControl;

		agents = Collections.synchronizedMap(new HashMap());
		observers = new ArrayList();

		m_processControl
				.addProcessStatusListener((ProcessControl.Listener) swingDispatcherFactory
						.create(new ProcessControl.Listener() {
							public void update(
									ProcessControl.ProcessReports[] processReports) {
								for (int i = 0; i < processReports.length; ++i) {
									final AgentProcessReport agentProcessStatus = processReports[i]
											.getAgentProcessReport();
									if (!agents.containsKey(agentProcessStatus
											.getAgentIdentity())) {
										try {
											AddAgent(agentProcessStatus
													.getAgentIdentity());
										} catch (ConsoleException e) {
											e.printStackTrace();
										}
									}
								}

							}
						}));

		m_processControl
				.addProcessResultListener((ProcessControl.ResultListener) swingDispatcherFactory
						.create(new ProcessControl.ResultListener() {
							public void update(
									ProcessControl.ResultReports[] resultReports) {

								for (int i = 0; i < resultReports.length; ++i) {
									final AgentResultReport agentResultReport = resultReports[i]
											.getAgentResultReport();
									if (agents.containsKey(agentResultReport
											.getAgentIdentity())) {
										((AgentInfo) agents
												.get(agentResultReport
														.getAgentIdentity()))
												.setStats(agentResultReport
														.getEBStats());
										notifyObserverResult((AgentInfo) agents
												.get(agentResultReport
														.getAgentIdentity()));
									}
								}
							}
						}));
	}

	/**
	 * @param agentIdentity
	 * @throws ConsoleException
	 */
	public void AddAgent(AgentIdentity agentIdentity) throws ConsoleException {
		final AgentInfo created = new AgentInfo(agentIdentity);
		agents.put(agentIdentity, created);
		notifyObserverAdd(created);
	}

	/**
	 * @param agentIdentity
	 */
	public void DelAgent(AgentIdentity agentIdentity) {
		notifyObserverDel((AgentInfo) agents.get(agentIdentity));
		agents.remove(agentIdentity);
	}

	public void notifyObserverAdd(AgentInfo agentInfo) throws ConsoleException {
		for (int i = 0; i < observers.size(); i++) {
			AgentInfoObserver observer = (AgentInfoObserver) observers.get(i);
			observer.addAgent(agentInfo);
		}
	}

	public void notifyObserverDel(AgentInfo agentInfo) {
		for (int i = 0; i < observers.size(); i++) {
			AgentInfoObserver observer = (AgentInfoObserver) observers.get(i);
			observer.removeAgent(agentInfo);
		}
	}

	/**
	 * @param agentInfo
	 */
	public void notifyObserverResult(AgentInfo agentInfo) {
		for (int i = 0; i < observers.size(); i++) {
			AgentInfoObserver observer = (AgentInfoObserver) observers.get(i);
			observer.getResult(agentInfo);
		}
	}

	public void registerObserver(AgentInfoObserver o) {
		observers.add(o);

	}

	public void removeObserver(AgentInfoObserver o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(i);
		}
	}

	/**
	 * 
	 */
	public void resetAllResult() {
		// when a new test start, results need to reset.

	}

	/**
	 * @return
	 */
	public int getAgentNumber() {
		return agents.size();
	}

}
