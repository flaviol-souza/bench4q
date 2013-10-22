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
import java.net.SocketException;
import java.util.Scanner;

/**
 * The class for starting the subordinate
 * 
 * @author xiaowei zhou 2010-8-18
 * 
 */
public class SubordinateStarter {

	/**
	 * the runnable to send the monitor data to the leader
	 */
	private SubordinateDataSender dataSender;

	/**
	 * the thread object wrapping dataSender
	 */
	private Thread dataSenderThread;
	/**
	 * the runnable to retrieve leader's address through multicast
	 */
	private MulticastReceiver multicastReceiver;

	/**
	 * the thread object wrapping multicastReceiver
	 */
	private Thread multicastReceiverThread;

	/**
	 * start the subordinate and wait for the user to exit it
	 */
	public void startSubordinate() {

		try {

			try {
				dataSender = new SubordinateDataSender();
				multicastReceiver = new MulticastReceiver(dataSender);
				multicastReceiver.joinMulticastGroup();

				dataSenderThread = new Thread(dataSender);
				multicastReceiverThread = new Thread(multicastReceiver);
				multicastReceiverThread.setName("MulticastReceiver");
				dataSenderThread.setName("MonitoringDataSender");
				dataSenderThread.start();
				multicastReceiverThread.start();

//				System.out
//						.println("The subordinate server monitor has started successfully!");
			} catch (SocketException e) {
				System.err
						.println("The subordinate server monitor failed to start, for the exception below:");
				e.printStackTrace();
				return;
			} catch (IOException e) {
				System.err
						.println("The subordinate server monitor failed to start, for the exception below:");
				e.printStackTrace();
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
			if (dataSender != null) {
				dataSender.scheduleStop();
				if (dataSenderThread.isAlive()) {
					dataSenderThread.interrupt();
				}
			}
			if (multicastReceiver != null) {
				multicastReceiver.scheduleStop();
				if (multicastReceiverThread.isAlive()) {
					multicastReceiverThread.interrupt();
				}
			}
		}

	} // the end of startSubordinate method
}
