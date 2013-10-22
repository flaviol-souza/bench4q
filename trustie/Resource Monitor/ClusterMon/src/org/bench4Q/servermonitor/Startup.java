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
package org.bench4Q.servermonitor;

import java.rmi.registry.Registry;
import java.util.Scanner;

import org.bench4Q.servermonitor.leader.MonitorLeader;
import org.bench4Q.servermonitor.subordinate.SubordinateStarter;


/**
 * The class for starting ClusterMon
 * 
 * @author xiaowei zhou
 * 2010-7-8
 *
 */
public class Startup {

	/**
	 * The RMI registry
	 */
	private static Registry registry = null;
	
	/**
	 * The port of RMI registry
	 */
	private static int rmiPort = 10724;
	
	/**
	 * RMI service name
	 */
	private static String rmiName = "ServerDataManager";
	

	public static void main(String[] args) {
		
		boolean isLeader = false;
		
		if(args.length > 0 && "-leader".equals(args[0])) {
			// command-line argument says this instance should be a leader
			isLeader = true;
		} else if(args.length > 0 && "-subordinate".equals(args[0])) {
			// command-line argument says this instance should be a subordinate
			isLeader = false;
		} else {
			// let user to choose whether this instance will be a leader or a subordinate		
			System.out.print("Will this instance cluster server monitor be a leader, please type Y or N for Yes or No:");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String strChoice = in.nextLine();
				if ("Y".equalsIgnoreCase(strChoice)) {
					isLeader = true;
					break;
				} else if ("N".equalsIgnoreCase(strChoice)) {
					isLeader = false;
					break;
				}
			}
		}
		
		// start the leader or subordinate
		if(isLeader) {
			System.out.println("This instance will be a leader!");
			MonitorLeader leaderObj = new MonitorLeader();
			leaderObj.startLeader();
		} else {
			System.out.println("This instance will be a subordinate!");
			SubordinateStarter subordinate = new SubordinateStarter();
			subordinate.startSubordinate();
		}
		
		System.exit(0);
	}

}
