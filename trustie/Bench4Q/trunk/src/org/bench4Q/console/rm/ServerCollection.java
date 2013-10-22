/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
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
 *  * Developer(s): Wang Sa.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.console.rm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bench4Q.common.processidentity.AgentProcessReport;
import org.bench4Q.common.processidentity.AgentResultReport;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.transfer.AgentInfo;
import org.bench4Q.console.ui.transfer.AgentInfoObserver;
import org.bench4Q.console.ui.transfer.AgentsCollection;


/**
 * @author wang sa
 *
 *  this class will trigger the update of every server information observer  
 *  
 */
public class ServerCollection{
	private ArrayList observers;
	
//	private Map agents;
//	
//	private int finishedNumbers;      //the numbers of agents which have finished
//	private final ProcessControl m_processControl;
//	
//	private final MonitorProcess m_serverProcess;
	
	private boolean m_run;
//	
//	private Map<String, ServerInfo> multiServerData;
	
	
	/**
	 *     control the process of resource monitors
     */
	public ServerCollection(){
//		m_processControl = processControl;
//		m_serverProcess = serverProcess;
//		finishedNumbers = 0;
		observers = new ArrayList();
//		agents = Collections.synchronizedMap(new HashMap());
//		multiServerData = m_serverProcess.getData();
		
//		m_processControl
//		.addProcessStatusListener((ProcessControl.Listener) swingDispatcherFactory
//				.create(new ProcessControl.Listener() {
//					public void update(
//							ProcessControl.ProcessReports[] processReports) {
//						for (int i = 0; i < processReports.length; ++i) {
//							final AgentProcessReport agentProcessStatus = processReports[i]
//									.getAgentProcessReport();
//							if (!agents.containsKey(agentProcessStatus
//									.getAgentIdentity())) {
//								final AgentInfo created = new AgentInfo(agentProcessStatus.getAgentIdentity());
//								agents.put(agentProcessStatus.getAgentIdentity(), created);
//							}
//						}
//
//					}
//				}));
//
//       m_processControl
//	   .addProcessResultListener((ProcessControl.ResultListener) swingDispatcherFactory
//				.create(new ProcessControl.ResultListener() {
//					public void update(
//							ProcessControl.ResultReports[] resultReports) {
//
//						for (int i = 0; i < resultReports.length; ++i) {
//							final AgentResultReport agentResultReport = resultReports[i]
//									.getAgentResultReport();
//							if (agents.containsKey(agentResultReport
//									.getAgentIdentity())) {
//
//								finishedNumbers++;
////								if(isAllDone())
//									
////									notifyObserverResult(m_serverProcess.getData());
//								
//							}
//						}
//					}
//				}));
	}
//	/**
//	 * @return
//	 * 
//	 * Have all agents finished?
//	 */
//	public boolean isAllDone() {
//		return (finishedNumbers == agents.size());
//	}
	
	/**
	 * @param serverInfo 
	 * 
	 */
	public void notifyObserverResult(ServerInfo serverInfo){
		
//		if (m_run) {
//			ServerInfo serverInfo = multiServerData.get(IP);
			
			// update the information of every server observer
			for (int i = 0; i < observers.size(); i++) {
				ServerInfoObserver observer = (ServerInfoObserver) observers
						.get(i);
				observer.getResult(serverInfo);
//			}
		}
	}
	/**
	 * @param o
	 */
	public void registerObserver(ServerInfoObserver o) {
		observers.add(o);

	}
	
	/**
	 * 
	 */
//	public void reset()
//	{
//		finishedNumbers = 0;
//	}
	public boolean isM_run() {
		return m_run;
	}
	/**
	 * @param mRun
	 */
	public void setM_run(boolean mRun) {
		m_run = mRun;
	}
//	public Map<String, ServerInfo> getMultiServerData() {
//		return multiServerData;
//	}
}
