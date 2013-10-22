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
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import org.bench4Q.servermonitor.ServerDataManager;

/**
 * The class for starting and exiting the leader
 * 
 * @author xiaowei zhou 2010-8-18
 * 
 */
public class MonitorLeader {

	/**
	 * The RMI registry
	 */
	private Registry registry = null;

	/**
	 * The port of RMI registry
	 */
	private int rmiPort = 10724;

	/**
	 * RMI service name for the client to retrieve cluster monitor data
	 */
	private String rmiName = "ServerDataManager";

	private ServerDataManager serverDM = null;

	/**
	 * the runnable to notify subordinates of leader's address
	 */
	private MulticastNofitier multicastNotifier;
	
	/**
	 * the thread object wrapping multicastNotifier
	 */
	private Thread multicasterThread;

	/**
	 * the runnable to receive subordinates' monitor data
	 */
	private SubordinateDataReceiver subordinateReceiver;

	/**
	 * the thread object wrapping subordinateReceiver
	 */
	private Thread subordinateReceiverThread;

	/**
	 * start the leader and wait for the user to exit it
	 */
	public void startLeader() {

		try {

			multicastNotifier = new MulticastNofitier();
			try {
				multicastNotifier.joinMulticastGroup();
			} catch (IOException e) {
				// fail to join multicast group, exits
				System.err
						.println("The server monitor leader failed to start, for the exception below:");
				e.printStackTrace();
				return;
			}
			multicasterThread = new Thread(multicastNotifier);
			multicasterThread.setName("MulticastNotifier");
			multicasterThread.start();

			// get RMI registry port from system property
			// if none, continue to use default
			String strRmiPort = System.getProperty("servermonregport");
			if (strRmiPort != null) {
				try {
					rmiPort = Integer.parseInt(strRmiPort);
				} catch (NumberFormatException e) {
					// do nothing, continue to use the default value
				}
			}

			try {
				serverDM = new ServerDataManager();
				registry = LocateRegistry.createRegistry(rmiPort);
				registry.rebind(rmiName, serverDM);

				System.out
						.println("The server monitor leader has started successfully!");
				System.out.println("The client may connect to port " + rmiPort
						+ " through RMI, and look up \"" + rmiName + "\"");
			} catch (RemoteException e) {
				System.err
						.println("The server monitor leader failed to start, for the exception below:");
				e.printStackTrace();
				return;
			}

			// start the subordinate monitor data receiver thread
			try {
				subordinateReceiver = new SubordinateDataReceiver(serverDM);
				subordinateReceiverThread = new Thread(subordinateReceiver);
				subordinateReceiverThread.setName("SubordinateDataReceiver");
				subordinateReceiverThread.start();
			} catch (SocketException e1) {
				System.err
						.println("The server monitor leader failed to start, for the exception below:");
				e1.printStackTrace();
				return;
			}

			// wait for the user to type the exit command
			System.out.println("You can type \"exit\" to quit at any time!");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String qString = in.nextLine();
				if ("exit".equalsIgnoreCase(qString)) {
					break;
				}
			}

		} finally {
			// release resources
			if (registry != null) {
				try {
					registry.unbind(rmiName);
				} catch (AccessException e) {
					// e.printStackTrace();
				} catch (RemoteException e) {
					// e.printStackTrace();
				} catch (NotBoundException e) {
					// e.printStackTrace();
				}
			}
			if (registry != null) {
				try {
					UnicastRemoteObject.unexportObject(registry, true);
				} catch (NoSuchObjectException e) {
					// e.printStackTrace();
				}
			}
			if (multicastNotifier != null) {
				multicastNotifier.scheduleStop();
				if (multicasterThread.isAlive()) {
					multicasterThread.interrupt();
				}
			}
			if (subordinateReceiver != null) {
				subordinateReceiver.scheduleStop();
				if (subordinateReceiverThread.isAlive()) {
					subordinateReceiverThread.interrupt();
				}
			}
		}
	} // end of startLeader method
}
