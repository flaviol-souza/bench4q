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
package org.bench4Q.servermonitor.leader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Continually multicast dummy packets to inform subordinates the leader's address
 * @author xiaowei zhou
 * 2010-8-18
 *
 */
public class MulticastNofitier implements Runnable {
	
	/**
	 * multicast group IP
	 */
	private String groupIp = "237.83.173.8";
	
	/**
	 * multicast target port
	 */
	private int targetPort = 10728;
	
	private String notifyStr = "";
	
	private MulticastSocket multicastSocket;
	
	/**
	 * inner representation of multicast group IP
	 */
	private InetAddress groupAddr;
	
	private AtomicBoolean toStop = new AtomicBoolean(false);

	public void run() {
		
		// get the multicast target port
		// if none, continue to use default
		String strTargetPort = System.getProperty("multicasttargetport");
		if (strTargetPort != null) {
			try {
				targetPort = Integer.parseInt(strTargetPort);
			} catch (NumberFormatException e) {
				// do nothing, continue to use the default value
			}
		}
		
		byte[] notifyBytes = notifyStr.getBytes();
		DatagramPacket notifyPacket = new DatagramPacket(notifyBytes,
				notifyBytes.length, groupAddr, targetPort);

		// send notification packets to notify the subordinates of the leader's
		// address at intervals
		while (!toStop.get()) {
			try {
				multicastSocket.send(notifyPacket);
			} catch (IOException e1) {
				if (!toStop.get()) {
					System.err
							.println("There is an exception when sending a multicast notification packet:");
					e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
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
		
		// join the group, may raise exceptions
		multicastSocket = new MulticastSocket();
		groupAddr = InetAddress.getByName(groupIp);
		multicastSocket.joinGroup(groupAddr);
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

}
