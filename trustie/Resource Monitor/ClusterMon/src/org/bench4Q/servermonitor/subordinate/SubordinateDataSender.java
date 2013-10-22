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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bench4Q.servermonitor.ServerData;
import org.bench4Q.servermonitor.ServerDataManager;

/**
 * This class is a runnable to continually send monitor data to the monitor leader
 * @author xiaowei zhou
 * 2010-8-18
 */
public class SubordinateDataSender implements Runnable {

	private InetAddress leaderAddr;
	
	/**
	 * the port to which the monitor data to send
	 */
	private int leaderPort = 10726;
	
	/**
	 * the UDP socket to send the monitor data
	 */
	private DatagramSocket senderSocket = null;
	
	private AtomicBoolean toStop = new AtomicBoolean(false);
	
	public SubordinateDataSender() throws SocketException {
		super();
		senderSocket = new DatagramSocket();
	}

	public void run() {
		
		// get subordinate monitoring data receiver port of the leader from system
		// property
		// if none, continue to use default
		String strLeaderPort = System
				.getProperty("subordinatereceiverport");
		if (strLeaderPort != null) {
			try {
				leaderPort = Integer.parseInt(strLeaderPort);
			} catch (NumberFormatException e) {
				// do nothing, continue to use the default value
			}
		}
		
		// wait for the leader to send its ip address
		System.out.println("Looking up a leader...");
		synchronized(this) {
			while(leaderAddr == null && !toStop.get()) {
				try {
					this.wait();
				} catch (InterruptedException e) {
				}
			}
		}
		if (!toStop.get()) {
			System.out.println("Succeed in finding a leader at "
					+ leaderAddr.getHostAddress() + "!");
			System.out
					.println("Now sending monitor data to the leader continually!");
		}
		
		// continually send monitor data to the monitor leader
		while(!toStop.get()) {
			ServerData serverData = ServerDataManager.getServerData();
			ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
			ObjectOutputStream objOutStream = null;
			try {
				objOutStream = new ObjectOutputStream(byteArrOutStream);
				objOutStream.writeObject(serverData);
				objOutStream.flush();
				byte[] serverDataBytes = byteArrOutStream.toByteArray();
				DatagramPacket serverDataPacket = new DatagramPacket(
						serverDataBytes, serverDataBytes.length, leaderAddr,
						leaderPort);
				senderSocket.send(serverDataPacket);
			} catch (IOException e1) {
				if (!toStop.get()) {
					e1.printStackTrace();
				}
			} finally {
				if (objOutStream != null) {
					try {
						objOutStream.close();
					} catch (IOException e) {
					}
				}
				if (byteArrOutStream != null) {
					try {
						byteArrOutStream.close();
					} catch (IOException e) {
					}
				}
			}
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
		
	}
	
	/**
	 * update the leader's address
	 * @param newAddr
	 */
	public synchronized void updateLeaderAddr(InetAddress newAddr) {
		if(!newAddr.equals(leaderAddr)) {
			leaderAddr = newAddr;
			this.notify();
		}
	}
	
	/**
	 * properly stop the thread of this runnable 
	 */
	public void scheduleStop() {
		toStop.set(true);
		if(senderSocket != null) {
			senderSocket.close();
		}
	}

}
