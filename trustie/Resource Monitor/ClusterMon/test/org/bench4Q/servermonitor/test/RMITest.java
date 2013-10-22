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
package org.bench4Q.servermonitor.test;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bench4Q.servermonitor.IServerDataManager;
import org.bench4Q.servermonitor.ServerData;
import org.bench4Q.servermonitor.ServerDataWithTimeOfReceipt;

/**
 * The class for testing retrieving data through RMI
 * @author xiaowei zhou
 *
 */
public class RMITest {

	/**
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException, InterruptedException {

		Scanner in = new Scanner(System.in);
		System.out.print("Please input the IP address of the monitored server:");
		
		String serverIP = "";
		if (in.hasNextLine()) {
			serverIP = in.nextLine();
		}

		Registry registry = LocateRegistry.getRegistry(serverIP, 10724);

		Remote stub = registry.lookup("ServerDataManager");

		ConcurrentHashMap<String, ServerDataWithTimeOfReceipt> serverDataMap;

		while (true) {
			serverDataMap = ((IServerDataManager) stub).getServerDataFromRMI();
			Iterator<Entry<String, ServerDataWithTimeOfReceipt>> iter = serverDataMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, ServerDataWithTimeOfReceipt> entry = iter.next();
				ServerData serverDat = entry.getValue();
				System.out.println("IP" + entry.getKey());
				System.out.println("CPU Usage: " + serverDat.getCpuPercent());
				System.out.println("Available Memory in MBytes: "
						+ serverDat.getMemAvailMB());
				System.out.println("Disk read per second in bytes: "
						+ serverDat.getDiskReadBytesPerSecond());
				System.out.println("Disk write per second in bytes: "
						+ serverDat.getDiskWriteBytesPerSecond());
				System.out.println("Network receive per second in bytes: "
						+ serverDat.getNetworkRecvBytesPerSecond());
				System.out.println("Network send per second in bytes: "
						+ serverDat.getNetworkSentBytesPerSecond());
				System.out.println();
			}
			System.out.println();

			Thread.sleep(1000);
		}

	}

}
