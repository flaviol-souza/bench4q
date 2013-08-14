package org.bench4Q.servermonitor.subordinate;

import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Scanner;

public class SubordinateStarter {
	private SubordinateDataSender dataSender;
	private Thread dataSenderThread;
	private MulticastReceiver multicastReceiver;
	private Thread multicastReceiverThread;

	public void startSubordinate() {
		try {
			try {
				this.dataSender = new SubordinateDataSender();
				this.multicastReceiver = new MulticastReceiver(this.dataSender);
				this.multicastReceiver.joinMulticastGroup();

				this.dataSenderThread = new Thread(this.dataSender);
				this.multicastReceiverThread = new Thread(
						this.multicastReceiver);
				this.multicastReceiverThread.setName("MulticastReceiver");
				this.dataSenderThread.setName("MonitoringDataSender");
				this.dataSenderThread.start();
				this.multicastReceiverThread.start();
			} catch (SocketException e) {
				System.err
						.println("The subordinate server monitor failed to start, for the exception below:");
				e.printStackTrace();
				return;
			}
		} catch (IOException e) {
			System.err
					.println("The subordinate server monitor failed to start, for the exception below:");
			e.printStackTrace();
			return;

		} finally {
			if (this.dataSender != null) {
				this.dataSender.scheduleStop();
				if (this.dataSenderThread.isAlive()) {
					this.dataSenderThread.interrupt();
				}
			}
			if (this.multicastReceiver != null) {
				this.multicastReceiver.scheduleStop();
				if (this.multicastReceiverThread.isAlive())
					this.multicastReceiverThread.interrupt();
			}
		}

		System.out.println("You can type \"exit\" to quit at any time!");
		Scanner in = new Scanner(System.in);
		while (in.hasNextLine()) {
			String qString = in.nextLine();
			if ("exit".equalsIgnoreCase(qString))
				break;
		}
	}
}
