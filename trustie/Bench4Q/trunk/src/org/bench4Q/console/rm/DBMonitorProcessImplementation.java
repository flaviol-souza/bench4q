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

import java.util.HashMap;
import java.util.Map;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;

/**
 * @author wang sa
 *
 *  this class will control the process of monitoring the database server
 *  
 */
public class DBMonitorProcessImplementation implements MonitorProcess{
	
	private ResourceMonitorThread m_resourceMonitor;
	private Args m_arg;
//	private ServerInfo m_serverInfo;
	private String m_hostaddr;
	private int port;
	private Map<String, ServerInfo> MultiServer;
	
	/**
	 * @param arg
	 * 
	 *   arg is the configuration information.
	 *   
	 */
	public DBMonitorProcessImplementation(Args arg){
		m_arg = arg;
		MultiServer = new HashMap<String, ServerInfo>();
	}

	@Override
	public void StartMonitor() {
//		m_serverInfo = new ServerInfo();
		int max = 0;
		int workerEndTime;
		
		// count the time of the whole test
		for (TestPhase testPhase : m_arg.getEbs()) {
			workerEndTime = testPhase.getStdyTime()
					+ testPhase.getTriggerTime();
			if (workerEndTime > max) {
				max = workerEndTime;
			}
		}
		// get the address of the database server
		m_hostaddr = m_arg.getDBURL();
		// get the port of the resource monitor
		port = m_arg.getDBPort();
		m_resourceMonitor = new ResourceMonitorThread(m_hostaddr, MultiServer, max * 1000L, port);
		
		// start the monitor
		m_resourceMonitor.setDaemon(true);
		m_resourceMonitor.start();
		
		
	}

	@Override
	public void StopMonitor() {
		m_resourceMonitor.tostop();
		
	}

	@Override
	public Map<String, ServerInfo> getData() {
		// TODO Auto-generated method stub
		return MultiServer;
	}

}
