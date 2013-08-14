package org.bench4Q.servermonitor.leader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bench4Q.servermonitor.ServerData;
import org.bench4Q.servermonitor.ServerDataManager;

public class SubordinateDataReceiver implements Runnable {
	private DatagramSocket receiverSocket;
	private ServerDataManager serverDM;
	private int receiverPort = 10726;

	private AtomicBoolean toStop = new AtomicBoolean(false);

	public SubordinateDataReceiver(ServerDataManager serverDM)
			throws SocketException {
		String strReceiverPort = System.getProperty("subordinatereceiverport");
		if (strReceiverPort != null) {
			try {
				this.receiverPort = Integer.parseInt(strReceiverPort);
			} catch (NumberFormatException localNumberFormatException) {
			}
		}
		this.receiverSocket = new DatagramSocket(this.receiverPort);
		this.serverDM = serverDM;
	}

	public void run() {
		DatagramPacket monitorDataPacket = new DatagramPacket(new byte[65536],
				65536);

		while (!this.toStop.get())
			try {
				this.receiverSocket.receive(monitorDataPacket);

				InetAddress inetAddrSender = monitorDataPacket.getAddress();
				String strIPAddrSender = inetAddrSender.getHostAddress();

				byte[] recvBuffer = monitorDataPacket.getData();
				ByteArrayInputStream byteArrInStream = new ByteArrayInputStream(
						recvBuffer);
				ObjectInputStream objInStream = null;
				try {
					objInStream = new ObjectInputStream(byteArrInStream);
					ServerData subordinateData = (ServerData) objInStream
							.readObject();
					this.serverDM.updateSubordinateData(strIPAddrSender,
							subordinateData);
				} catch (IOException localIOException1) {
				}
				try {
					if (objInStream != null)
						objInStream.close();
				} catch (IOException localIOException2) {
				}
				try {
					byteArrInStream.close();
				} catch (IOException localIOException3) {
				}
			} catch (IOException e) {
				if (!this.toStop.get())
					e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	}

	public void scheduleStop() {
		this.toStop.set(true);
		if (this.receiverSocket != null)
			this.receiverSocket.close();
	}
}
