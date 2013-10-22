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

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of resource monitoring RMI server
 * Calls native method to retrieve data
 * 
 * @author xiaowei zhou
 * 2010-8-18
 */
public class ServerDataManager extends UnicastRemoteObject implements
		IServerDataManager {
	
	private ConcurrentHashMap<String, ServerDataWithTimeOfReceipt> serverDatas = new ConcurrentHashMap<String, ServerDataWithTimeOfReceipt>();
	
	private final String localAddress = "127.0.0.1";

	public ServerDataManager() throws RemoteException {
		super();
	}

	static {
		System.loadLibrary("ServerDataProvider"); // load the native library file
	}

	public static native ServerData getServerData();
	
	/**
	 * this method is invoked by the client through RMI to retrieve monitor data
	 */
	public ConcurrentHashMap<String, ServerDataWithTimeOfReceipt> getServerDataFromRMI()
			throws RemoteException {
		ServerData localData = getServerData();
		ServerDataWithTimeOfReceipt dt = new ServerDataWithTimeOfReceipt(localData);
		dt.setReceiptTimeMillis(System.currentTimeMillis());
		serverDatas.put(localAddress, dt);
		return serverDatas;
	}

	/**
	 * add or update the monitor data of a subordinate
	 * @param ipAddr
	 * @param monData
	 */
	public void updateSubordinateData(String ipAddr, ServerData monData) {
		ServerDataWithTimeOfReceipt dt = new ServerDataWithTimeOfReceipt(monData);
		dt.setReceiptTimeMillis(System.currentTimeMillis());
		serverDatas.put(ipAddr, dt);
	}


}
