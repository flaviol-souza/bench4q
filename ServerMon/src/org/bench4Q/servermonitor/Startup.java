/**
 * =========================================================================
 * 						Bench4Q Server Monitor
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

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


/**
 * The class for starting ServerMon
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
		
		boolean started = false;

		// create RMI registry and bind the service
		try {
			registry = LocateRegistry.createRegistry(rmiPort);
			IServerDataManager serverDM = new ServerDataManager();
			registry.rebind(rmiName, serverDM);

			System.out.println("The server monitor has started successfully!");
			System.out.println("You can connect to port " + rmiPort
					+ " through RMI at the client side, and lookup \"" + rmiName + "\"");
			
			started = true;

		} catch (RemoteException e) {
			System.err
					.println("The server monitor failed to start, for the exception below:");
			e.printStackTrace();
		}
		
		// wait and exit
		if (started) {
			
			// wait for the user to type the exit command
			System.out.println("Type \"exit\" to quit!");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String qString = in.nextLine();
				if ("exit".equalsIgnoreCase(qString)) {
					break;
				}
			}

			// release resources and exit
			
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

			registry = null;

			started = false;
			
			System.exit(0);
		}
		
	}

}
