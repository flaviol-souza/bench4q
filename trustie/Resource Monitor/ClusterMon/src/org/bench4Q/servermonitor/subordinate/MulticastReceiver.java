/**
 * =========================================================================
 * 					Bench4Q Server Cluster Monitor
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
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Developer(s): Xiaowei Zhou.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.servermonitor.subordinate;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author xiaowei zhou
 * 2010-8-18
 *
 */
public class MulticastReceiver implements Runnable {
	
	/**
	 * multicast group IP
	 */
	private String groupIp = "237.83.173.8";
	
	private SubordinateDataSender dataSender;
	
	public MulticastReceiver(SubordinateDataSender dataSender) {
		super();
		this.dataSender = dataSender;
	}

	/**
	 * multicast receiving port
	 */
	private int recvPort = 10728;
	
	private AtomicBoolean toStop = new AtomicBoolean(false);
	
	private MulticastSocket multicastSocket;
	
	/**
	 * inner representation of multicast group IP
	 */
	private InetAddress groupAddr;

	public void run() {
		DatagramPacket multicastPacket = new DatagramPacket(new byte[100], 100);
		while (!toStop.get()) {
			try {
				multicastSocket.receive(multicastPacket);
				InetAddress leaderAddr = multicastPacket.getAddress();
				if (leaderAddr != null) {
					dataSender.updateLeaderAddr(leaderAddr);
				}
			} catch (IOException e) {
				if (!toStop.get()) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * properly stop the thread of this runnable 
	 */
	public void scheduleStop() {
		toStop.set(true);
		if (multicastSocket != null) {
			multicastSocket.close();
		}
	}
	
	/**
	 * init the multicast socket, must be called before the thread of this runnable starts
	 * @throws IOException
	 */
	public void joinMulticastGroup() throws IOException {
		
		// get multicast group IP from system property
		// if none, continue to use default
		String multicastGrpIP = System.getProperty("multicastgroupip");
		if (multicastGrpIP != null && !"".equals(multicastGrpIP)) {
			groupIp = multicastGrpIP;
		}
		
		// get the multicast target port
		// if none, continue to use default
		String strRecvPort = System.getProperty("multicasttargetport");
		if (strRecvPort != null) {
			try {
				recvPort = Integer.parseInt(strRecvPort);
			} catch (NumberFormatException e) {
				// do nothing, continue to use the default value
			}
		}
		
		// join the group, may raise exceptions
		multicastSocket = new MulticastSocket(recvPort);
		groupAddr = InetAddress.getByName(groupIp);
		multicastSocket.joinGroup(groupAddr);
	}

}
