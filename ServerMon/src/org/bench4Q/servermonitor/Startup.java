package org.bench4Q.servermonitor;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Startup {
	private static Registry registry = null;

	private static int rmiPort = 10724;

	private static String rmiName = "ServerDataManager";

	public static void main(String[] args) {
		String strRmiPort = System.getProperty("servermonregport");
		if (strRmiPort != null) {
			try {
				rmiPort = Integer.parseInt(strRmiPort);
			} catch (NumberFormatException localNumberFormatException) {
			}
		}
		boolean started = false;
		try {
			registry = LocateRegistry.createRegistry(rmiPort);
			IServerDataManager serverDM = new ServerDataManager();
			registry.rebind(rmiName, serverDM);

			System.out.println("The server monitor has started successfully!");
			System.out.println("You can connect to port " + rmiPort
					+ " through RMI at the client side, and lookup \""
					+ rmiName + "\"");

			started = true;
		} catch (RemoteException e) {
			System.err
					.println("The server monitor failed to start, for the exception below:");
			e.printStackTrace();
		}

		if (started) {
			System.out.println("Type \"exit\" to quit!");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String qString = in.nextLine();
				if ("exit".equalsIgnoreCase(qString)) {
					break;
				}

			}

			if (registry != null)
				try {
					registry.unbind(rmiName);
				} catch (AccessException localAccessException) {
				} catch (RemoteException localRemoteException1) {
				} catch (NotBoundException localNotBoundException) {
				}
			if (registry != null) {
				try {
					UnicastRemoteObject.unexportObject(registry, true);
				} catch (NoSuchObjectException localNoSuchObjectException) {
				}
			}
			registry = null;

			started = false;

			System.exit(0);
		}
	}
}
