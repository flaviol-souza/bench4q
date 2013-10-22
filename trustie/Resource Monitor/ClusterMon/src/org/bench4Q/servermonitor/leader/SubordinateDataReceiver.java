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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bench4Q.servermonitor.ServerData;
import org.bench4Q.servermonitor.ServerDataManager;

/**
 * Continually receiving subordinates' monitoring data from UDP socket and
 * updating local data structure
 * 
 * @author xiaowei zhou 2010-8-18
 * 
 */
public class SubordinateDataReceiver implements Runnable {
	
	private DatagramSocket receiverSocket;
	
	private ServerDataManager serverDM;
	
	/**
	 * UDP port to receive subordinate monitor data
	 */
	private int receiverPort = 10726;
	
	private AtomicBoolean toStop = new AtomicBoolean(false);

	public SubordinateDataReceiver(ServerDataManager serverDM) throws SocketException {
		super();
		
		// get subordinate monitoring data receiver port from system
		// property
		// if none, continue to use default
		String strReceiverPort = System.getProperty("subordinatereceiverport");
		if (strReceiverPort != null) {
			try {
				receiverPort = Integer.parseInt(strReceiverPort);
			} catch (NumberFormatException e) {
				// do nothing, continue to use the default value
			}
		}
		
		receiverSocket = new DatagramSocket(receiverPort);
		this.serverDM = serverDM;
	}

	public void run() {
		
		DatagramPacket monitorDataPacket = new DatagramPacket(new byte[65536], 65536);

		// continually receiving subordinates' monitoring data from UDP socket
		while(!toStop.get()) {
			try {
				receiverSocket.receive(monitorDataPacket);
				
				// get the subordinate's address
				InetAddress inetAddrSender = monitorDataPacket.getAddress();
				String strIPAddrSender = inetAddrSender.getHostAddress();
				
				// extract the subordinate monitor data, and serialize it
				byte[] recvBuffer = monitorDataPacket.getData();
				ByteArrayInputStream byteArrInStream = new ByteArrayInputStream(recvBuffer);
				ObjectInputStream objInStream = null;
				ServerData subordinateData;
				try {
					objInStream = new ObjectInputStream(byteArrInStream);
					subordinateData = (ServerData)objInStream.readObject();
					serverDM.updateSubordinateData(strIPAddrSender, subordinateData);
				} catch (IOException e1) {
					// ignore
				}
								
				try {
					if (objInStream != null) {
						objInStream.close();
					}
				} catch (IOException e) {
				}
				try {
					byteArrInStream.close();
				} catch (IOException e) {
				}
				
			} catch (IOException e) {
				if (!toStop.get()) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * properly stop the thread of this runnable 
	 */
	public void scheduleStop() {
		toStop.set(true);
		if(receiverSocket != null) {
			receiverSocket.close();
		}
	}
}
