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
import org.bench4Q.servermonitor.ServerData;

/**
 * @author wang sa
 *
 * this class will control the process of monitoring the application server
 * 
 */
public class AppMonitorProcessImplementation implements MonitorProcess {
	
	private ServerInfo m_serverInfo;
	

	private ResourceMonitorThread m_resourceMonitor;
	
	private Args m_arg;
	
	private String m_hostaddr;
	
	private int port;
	
	private Map MultiServer;
	/**
	 * @param arg 
	 *    arg is the configuration information
	 * 
	 */
	public AppMonitorProcessImplementation(Args arg){
		
		m_arg = arg;
		MultiServer = new HashMap<String, ServerInfo>();
	}
	
	
	/**
	 * 
	 */
	public void StartMonitor(){
		
		m_serverInfo = new ServerInfo();
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
		String hostaddr = m_arg.getBaseURL();
		
		//extract the address of application server
		if(hostaddr.startsWith("http://"))
			m_hostaddr = hostaddr.substring(7);
		m_hostaddr = m_hostaddr.subSequence(0, 15).toString();
//		int index = m_hostaddr.indexOf('/');
//		m_hostaddr = m_hostaddr.substring(0, index);
		
		// get the port of the resource monitor
		port = m_arg.getWebPort();
		

//		m_resourceMonitor = new ResourceMonitorThread(m_hostaddr, MultiServer, max * 1000L, port);

		m_resourceMonitor = new ResourceMonitorThread(m_hostaddr, MultiServer, max * 1000L, port);

		
		// start the monitor thread
		m_resourceMonitor.setDaemon(true);
		m_resourceMonitor.start();
	}
	
	
	/**
	 * 
	 */
	public void StopMonitor(){
		
		m_resourceMonitor.tostop();
	}
	

	@Override
	public Map<String, ServerInfo> getData() {
		return MultiServer;
	}


}
