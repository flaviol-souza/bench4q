package org.bench4Q.servermonitor.leader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastNofitier implements Runnable {
	private String groupIp = "237.83.173.8";

	private int targetPort = 10728;

	private String notifyStr = "";
	private MulticastSocket multicastSocket;
	private InetAddress groupAddr;
	private AtomicBoolean toStop = new AtomicBoolean(false);

	public void run() {
		String strTargetPort = System.getProperty("multicasttargetport");
		if (strTargetPort != null) {
			try {
				this.targetPort = Integer.parseInt(strTargetPort);
			} catch (NumberFormatException localNumberFormatException) {
			}
		}
		byte[] notifyBytes = this.notifyStr.getBytes();
		DatagramPacket notifyPacket = new DatagramPacket(notifyBytes,
				notifyBytes.length, this.groupAddr, this.targetPort);

		while (!this.toStop.get()) {
			try {
				this.multicastSocket.send(notifyPacket);
			} catch (IOException e1) {
				if (!this.toStop.get()) {
					System.err
							.println("There is an exception when sending a multicast notification packet:");
					e1.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException localInterruptedException) {
			}
		}
	}

	public void joinMulticastGroup() throws IOException {
		String multicastGrpIP = System.getProperty("multicastgroupip");
		if ((multicastGrpIP != null) && (!"".equals(multicastGrpIP))) {
			this.groupIp = multicastGrpIP;
		}

		this.multicastSocket = new MulticastSocket();
		this.groupAddr = InetAddress.getByName(this.groupIp);
		this.multicastSocket.joinGroup(this.groupAddr);
	}

	public void scheduleStop() {
		this.toStop.set(true);
		if (this.multicastSocket != null)
			this.multicastSocket.close();
	}
}
