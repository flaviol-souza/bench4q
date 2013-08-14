package org.bench4Q.servermonitor.subordinate;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MulticastReceiver implements Runnable {
	private String groupIp = "237.83.173.8";
	private SubordinateDataSender dataSender;
	private int recvPort = 10728;

	private AtomicBoolean toStop = new AtomicBoolean(false);
	private MulticastSocket multicastSocket;
	private InetAddress groupAddr;

	public MulticastReceiver(SubordinateDataSender dataSender) {
		this.dataSender = dataSender;
	}

	public void run() {
		DatagramPacket multicastPacket = new DatagramPacket(new byte[100], 100);
		while (!this.toStop.get())
			try {
				this.multicastSocket.receive(multicastPacket);
				InetAddress leaderAddr = multicastPacket.getAddress();
				if (leaderAddr != null)
					this.dataSender.updateLeaderAddr(leaderAddr);
			} catch (IOException e) {
				if (!this.toStop.get())
					e.printStackTrace();
			}
	}

	public void scheduleStop() {
		this.toStop.set(true);
		if (this.multicastSocket != null)
			this.multicastSocket.close();
	}

	public void joinMulticastGroup() throws IOException {
		String multicastGrpIP = System.getProperty("multicastgroupip");
		if ((multicastGrpIP != null) && (!"".equals(multicastGrpIP))) {
			this.groupIp = multicastGrpIP;
		}

		String strRecvPort = System.getProperty("multicasttargetport");
		if (strRecvPort != null) {
			try {
				this.recvPort = Integer.parseInt(strRecvPort);
			} catch (NumberFormatException localNumberFormatException) {
			}
		}

		this.multicastSocket = new MulticastSocket(this.recvPort);
		this.groupAddr = InetAddress.getByName(this.groupIp);
		this.multicastSocket.joinGroup(this.groupAddr);
	}
}
