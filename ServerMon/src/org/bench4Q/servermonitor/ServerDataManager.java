package org.bench4Q.servermonitor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDataManager extends UnicastRemoteObject implements
		IServerDataManager {
	private ConcurrentHashMap<String, ServerDataWithTimeOfReceipt> serverDatas = new ConcurrentHashMap<String, ServerDataWithTimeOfReceipt>();

	private final String localAddress = "127.0.0.1";

	static {
		System.loadLibrary("ServerDataProvider");
	}

	public ServerDataManager() throws RemoteException {
	}

	public static native ServerData getServerData();

	public ConcurrentHashMap<String, ServerDataWithTimeOfReceipt> getServerDataFromRMI()
			throws RemoteException {
		ServerData localData = getServerData();
		ServerDataWithTimeOfReceipt dt = new ServerDataWithTimeOfReceipt(
				localData);
		dt.setReceiptTimeMillis(System.currentTimeMillis());
		this.serverDatas.put("127.0.0.1", dt);
		return this.serverDatas;
	}
}
