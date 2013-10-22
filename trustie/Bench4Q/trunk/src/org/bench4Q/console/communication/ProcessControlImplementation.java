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

import java.util.Timer;

import org.bench4Q.agent.messages.CollectResultMessage;
import org.bench4Q.agent.messages.ResetBench4QMessage;
import org.bench4Q.agent.messages.StartJASptEMessage;
import org.bench4Q.agent.messages.StopJASptEMessage;
import org.bench4Q.agent.messages.TestResultMessage;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.communication.MessageDispatchRegistry;
import org.bench4Q.common.communication.MessageDispatchRegistry.AbstractHandler;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.util.AllocateLowestNumber;
import org.bench4Q.common.util.AllocateLowestNumberImplementation;
import org.bench4Q.console.messages.AgentAddress;
import org.bench4Q.console.messages.AgentProcessReportMessage;
import org.bench4Q.console.rm.ResourceMonitorThread;

/**
 * Implementation of ProcessControl.
 * 
 * @author duanzhiquan
 * 
 */
public class ProcessControlImplementation implements ProcessControl {

	private final ConsoleCommunication m_consoleCommunication;

	private final ProcessStatusImplementation m_processStatusSet;

	private final ProcessResultImplementation m_processResultSet;

	private final AllocateLowestNumber m_agentNumberMap = new AllocateLowestNumberImplementation();


	/**
	 * Constructor.
	 * 
	 * @param timer
	 *            Timer that can be used to schedule housekeeping tasks.
	 * @param consoleCommunication
	 *            The console communication handler.
	 */
	public ProcessControlImplementation(Timer timer,
			ConsoleCommunication consoleCommunication) {

		m_consoleCommunication = consoleCommunication;
		m_processStatusSet = new ProcessStatusImplementation(timer,
				m_agentNumberMap);
		m_processResultSet = new ProcessResultImplementation(timer,
				m_agentNumberMap);
		
		

		final MessageDispatchRegistry messageDispatchRegistry = consoleCommunication
				.getMessageDispatchRegistry();

		messageDispatchRegistry.set(AgentProcessReportMessage.class,
				new AbstractHandler() {
					public void send(Message message) {
						m_processStatusSet
								.addAgentStatusReport((AgentProcessReportMessage) message);
					}
				});

		messageDispatchRegistry.set(TestResultMessage.class,
				new AbstractHandler() {
					public void send(Message message) {
						m_processResultSet
								.addAgentTestResultReport((TestResultMessage) message);
					}
				});
	}
	

	/**
	 * Signal the worker processes to start.
	 * 
	 * @param properties
	 *            Properties that override the agent's local properties.
	 */
	public void startWorkerProcesses(Bench4QProperties properties, Args args) {
		final Bench4QProperties propertiesToSend = properties != null ? properties
				: new Bench4QProperties();

		final Args m_args = args;

		m_agentNumberMap.forEach(new AllocateLowestNumber.IteratorCallback() {
			public void objectAndNumber(Object object, int number) {
				m_consoleCommunication.sendToAddressedAgents(new AgentAddress(
						(AgentIdentity) object), new StartJASptEMessage(
						propertiesToSend, m_args, number));
			}
		});
		
		
	}

	/**
	 * Signal the worker processes to reset.
	 */
	public void resetWorkerProcesses() {
		m_consoleCommunication.sendToAgents(new ResetBench4QMessage());
	}

	public void collectResultProcesses() {
		m_consoleCommunication.sendToAgents(new CollectResultMessage());
	}

	/** Signal the agent and worker processes to stop.
	 */
	public void stopAgentAndWorkerProcesses() {
		m_agentNumberMap.forEach(new AllocateLowestNumber.IteratorCallback() {
			public void objectAndNumber(Object object, int number) {
				m_consoleCommunication.sendToAddressedAgents(new AgentAddress(
						(AgentIdentity) object), new StopJASptEMessage());
			}
		});
//		m_consoleCommunication.sendToAgents(new StopJASptEMessage());
	}

	/**
	 * Add a listener for process status data.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void addProcessStatusListener(Listener listener) {
		m_processStatusSet.addListener(listener);
	}

	public void addProcessResultListener(ResultListener resultListener) {
		m_processResultSet.addListener(resultListener);
	}

	/**
	 * How many agents are live?
	 * 
	 * @return The number of agents.
	 */
	public int getNumberOfLiveAgents() {
		return m_processStatusSet.getNumberOfLiveAgents();
	}
}
