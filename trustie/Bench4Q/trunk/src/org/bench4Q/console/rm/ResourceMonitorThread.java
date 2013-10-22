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

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.servermonitor.IServerDataManager;
import org.bench4Q.servermonitor.ServerData;
import org.bench4Q.servermonitor.ServerDataWithTimeOfReceipt;

/**
 * 
 * @author wangsa
 * 2010-6-29
 * 
 * the thread which will be used to connect with resource monitor
 *
 */
public class ResourceMonitorThread extends Thread {
	
	private String m_hostaddr;
	
    private long m_interval;
	
	private int m_port;
	
	private Map<String, ServerInfo> m_MultiServer;
	
	private int count;
	
	/**
	 * @param hostaddr
	 *     the address of resource server
	 * @param interval 
	 *     the time of monitoring
	 */
	public ResourceMonitorThread(String hostaddr, Map<String, ServerInfo> multiServer, long interval, int port){
		super();
		
		m_MultiServer = multiServer;
		/**/
		m_hostaddr = hostaddr;
		m_interval = interval;
		m_port = port;
		
		
	}
	
	public void run(){
		
		Registry registry = null;
		Remote stub = null;
		int size;
		
		try {
			registry = LocateRegistry.getRegistry(m_hostaddr,m_port);
			stub = registry.lookup("ServerDataManager");   //  connect with the server
			ConcurrentHashMap<String, ServerDataWithTimeOfReceipt> stat;
			
			
			long starttime = System.currentTimeMillis();
//			try {
//				Thread.sleep(100L);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			

			count = 0;
			long lastReceiptime = 0;
			while(true)
			{
				stat = ((IServerDataManager) stub).getServerDataFromRMI();
				
				
				// read information from the resource monitor
				size = stat.size();
				Iterator datiIterator = stat.entrySet().iterator();
				Map.Entry<String, ServerDataWithTimeOfReceipt> dataEntry;
				for(int i=0; i<size; i++)
				{
					dataEntry = (Map.Entry<String, ServerDataWithTimeOfReceipt>) datiIterator.next();
					if(!m_MultiServer.containsKey(dataEntry.getKey())){
						ServerInfo sInfo = new ServerInfo();
						for(int j=0; j<=count; j++){
							sInfo.getCpu_ratio().add(-2.0);
							sInfo.getDiskRead().add(-2.0);
							sInfo.getDiskWrite().add(-2.0);
							sInfo.getMemory_usage().add(-2.0);
							sInfo.getNetworkRecv().add(-2.0);
							sInfo.getNetworkSent().add(-2.0);
						}
//						ServerDataWithTimeOfReceipt sReceipt = dataEntry.getValue();
//						sInfo.getCpu_ratio().add(sReceipt.getCpuPercent());
//						sInfo.getDiskRead().add(sReceipt.getDiskReadBytesPerSecond());
//						sInfo.getDiskWrite().add(sReceipt.getDiskWriteBytesPerSecond());
//						sInfo.getMemory_usage().add(sReceipt.getMemAvailMB());
//						sInfo.getNetworkRecv().add(sReceipt.getNetworkRecvBytesPerSecond());
//						sInfo.getNetworkSent().add(sReceipt.getNetworkSentBytesPerSecond());
						m_MultiServer.put(dataEntry.getKey(), sInfo);
					}
					else {
						ServerDataWithTimeOfReceipt sReceipt = dataEntry.getValue();
						ServerInfo sInfo = m_MultiServer.get(dataEntry.getKey());
						if(sReceipt.getReceiptTimeMillis() == lastReceiptime){
							sInfo.getCpu_ratio().add(-2.0);
							sInfo.getDiskRead().add(-2.0);
							sInfo.getDiskWrite().add(-2.0);
							sInfo.getMemory_usage().add(-2.0);
							sInfo.getNetworkRecv().add(-2.0);
							sInfo.getNetworkSent().add(-2.0);
						}
                        else {
                        	if(sReceipt.getMemAvailMB() > 1800.0){
                        		double m = sReceipt.getMemAvailMB();
                        		m = m * 2;
                        	}
							sInfo.getCpu_ratio().add(sReceipt.getCpuPercent());
							sInfo.getDiskRead().add(
									sReceipt.getDiskReadBytesPerSecond());
							sInfo.getDiskWrite().add(
									sReceipt.getDiskWriteBytesPerSecond());
							sInfo.getMemory_usage().add(
									sReceipt.getMemAvailMB());
							sInfo.getNetworkRecv().add(
									sReceipt.getNetworkRecvBytesPerSecond());
							sInfo.getNetworkSent().add(
									sReceipt.getNetworkSentBytesPerSecond());
						}
						m_MultiServer.put(dataEntry.getKey(), sInfo);
						
					}
				}  
					
				
				
				try {
					Thread.sleep(1000L);
					count++;
					if(System.currentTimeMillis() - starttime >= m_interval){
						break;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  catch (NotBoundException e) {
		    // TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 */
	public void tostop()
	{
		Thread.interrupted();
		
	}
	
	

}
