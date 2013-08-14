package org.bench4Q.servermonitor.leader;

import java.io.IOException;
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

public class MonitorLeader {
	private Registry registry = null;

	private int rmiPort = 10724;

	private String rmiName = "ServerDataManager";

	private ServerDataManager serverDM = null;
	private MulticastNofitier multicastNotifier;
	private Thread multicasterThread;
	private SubordinateDataReceiver subordinateReceiver;
	private Thread subordinateReceiverThread;

	public void startLeader() {
		try {
			this.multicastNotifier = new MulticastNofitier();
			try {
				this.multicastNotifier.joinMulticastGroup();
			} catch (IOException e) {
				System.err
						.println("The server monitor leader failed to start, for the exception below:");
				e.printStackTrace();
				return;
			}
			this.multicasterThread = new Thread(this.multicastNotifier);
			this.multicasterThread.setName("MulticastNotifier");
			this.multicasterThread.start();

			String strRmiPort = System.getProperty("servermonregport");
			if (strRmiPort != null)
				try {
					this.rmiPort = Integer.parseInt(strRmiPort);
				} catch (NumberFormatException localNumberFormatException) {
				}
			try {
				this.serverDM = new ServerDataManager();
				this.registry = LocateRegistry.createRegistry(this.rmiPort);
				this.registry.rebind(this.rmiName, this.serverDM);

				System.out
						.println("The server monitor leader has started successfully!");
				System.out.println("The client may connect to port "
						+ this.rmiPort + " through RMI, and look up \""
						+ this.rmiName + "\"");
			} catch (RemoteException e) {
				System.err
						.println("The server monitor leader failed to start, for the exception below:");
				e.printStackTrace();
				return;
			}

			try {
				this.subordinateReceiver = new SubordinateDataReceiver(
						this.serverDM);
				this.subordinateReceiverThread = new Thread(
						this.subordinateReceiver);
				this.subordinateReceiverThread
						.setName("SubordinateDataReceiver");
				this.subordinateReceiverThread.start();
			} catch (SocketException e1) {
				System.err
						.println("The server monitor leader failed to start, for the exception below:");
				e1.printStackTrace();
				return;
			}

			System.out.println("You can type \"exit\" to quit at any time!");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String qString = in.nextLine();
				if ("exit".equalsIgnoreCase(qString)) {
					break;
				}
			}
		} finally {
			if (this.registry != null)
				try {
					this.registry.unbind(this.rmiName);
				} catch (AccessException localAccessException3) {
				} catch (RemoteException localRemoteException4) {
				} catch (NotBoundException localNotBoundException3) {
				}
			if (this.registry != null)
				try {
					UnicastRemoteObject.unexportObject(this.registry, true);
				} catch (NoSuchObjectException localNoSuchObjectException3) {
				}
			if (this.multicastNotifier != null) {
				this.multicastNotifier.scheduleStop();
				if (this.multicasterThread.isAlive()) {
					this.multicasterThread.interrupt();
				}
			}
			if (this.subordinateReceiver != null) {
				this.subordinateReceiver.scheduleStop();
				if (this.subordinateReceiverThread.isAlive())
					this.subordinateReceiverThread.interrupt();
			}
		}
		if (this.registry != null)
			try {
				this.registry.unbind(this.rmiName);
			} catch (AccessException localAccessException4) {
			} catch (RemoteException localRemoteException5) {
			} catch (NotBoundException localNotBoundException4) {
			}
		if (this.registry != null)
			try {
				UnicastRemoteObject.unexportObject(this.registry, true);
			} catch (NoSuchObjectException localNoSuchObjectException4) {
			}
		if (this.multicastNotifier != null) {
			this.multicastNotifier.scheduleStop();
			if (this.multicasterThread.isAlive()) {
				this.multicasterThread.interrupt();
			}
		}
		if (this.subordinateReceiver != null) {
			this.subordinateReceiver.scheduleStop();
			if (this.subordinateReceiverThread.isAlive())
				this.subordinateReceiverThread.interrupt();
		}
	}
}
