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

public class SubordinateDataSender implements Runnable {
	private InetAddress leaderAddr;
	private int leaderPort = 10726;

	private DatagramSocket senderSocket = null;

	private AtomicBoolean toStop = new AtomicBoolean(false);

	public SubordinateDataSender() throws SocketException {
		this.senderSocket = new DatagramSocket();
	}

	public void run() {
		String strLeaderPort = System.getProperty("subordinatereceiverport");
		if (strLeaderPort != null) {
			try {
				this.leaderPort = Integer.parseInt(strLeaderPort);
			} catch (NumberFormatException localNumberFormatException) {
			}
		}

		System.out.println("Looking up a leader...");
		synchronized (this) {
			do {
				try {
					wait();
				} catch (InterruptedException localInterruptedException) {
				}
				if (this.leaderAddr != null)
					break;
			} while (!this.toStop.get());
		}

		if (!this.toStop.get()) {
			System.out.println("Succeed in finding a leader at "
					+ this.leaderAddr.getHostAddress() + "!");
			System.out
					.println("Now sending monitor data to the leader continually!");
		}

		while (!this.toStop.get()) {
			ServerData serverData = ServerDataManager.getServerData();
			ByteArrayOutputStream byteArrOutStream = new ByteArrayOutputStream();
			ObjectOutputStream objOutStream = null;
			try {
				objOutStream = new ObjectOutputStream(byteArrOutStream);
				objOutStream.writeObject(serverData);
				objOutStream.flush();
				byte[] serverDataBytes = byteArrOutStream.toByteArray();
				DatagramPacket serverDataPacket = new DatagramPacket(
						serverDataBytes, serverDataBytes.length,
						this.leaderAddr, this.leaderPort);
				this.senderSocket.send(serverDataPacket);
			} catch (IOException e1) {
				if (!this.toStop.get()) {
					e1.printStackTrace();
				}

				if (objOutStream != null)
					try {
						objOutStream.close();
					} catch (IOException localIOException1) {
					}
				if (byteArrOutStream != null)
					try {
						byteArrOutStream.close();
					} catch (IOException localIOException2) {
					}
			} finally {
				if (objOutStream != null)
					try {
						objOutStream.close();
					} catch (IOException localIOException3) {
					}
				if (byteArrOutStream != null)
					try {
						byteArrOutStream.close();
					} catch (IOException localIOException4) {
					}
			}
			try {
				Thread.sleep(500L);
			} catch (InterruptedException localInterruptedException1) {
			}
		}
	}

	public synchronized void updateLeaderAddr(InetAddress newAddr) {
		if (!newAddr.equals(this.leaderAddr)) {
			this.leaderAddr = newAddr;
			notify();
		}
	}

	public void scheduleStop() {
		this.toStop.set(true);
		if (this.senderSocket != null)
			this.senderSocket.close();
	}
}
