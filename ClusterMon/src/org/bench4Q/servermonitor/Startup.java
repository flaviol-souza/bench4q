package org.bench4Q.servermonitor;

import java.io.PrintStream;
import java.rmi.registry.Registry;
import java.util.Scanner;
import org.bench4Q.servermonitor.leader.MonitorLeader;
import org.bench4Q.servermonitor.subordinate.SubordinateStarter;

public class Startup {
	private static Registry registry = null;

	private static int rmiPort = 10724;

	private static String rmiName = "ServerDataManager";

	public static void main(String[] args) {
		boolean isLeader = false;

		if ((args.length > 0) && ("-leader".equals(args[0]))) {
			isLeader = true;
		} else if ((args.length > 0) && ("-subordinate".equals(args[0]))) {
			isLeader = false;
		} else {
			System.out
					.print("Will this instance cluster server monitor be a leader, please type Y or N for Yes or No:");
			Scanner in = new Scanner(System.in);
			while (in.hasNextLine()) {
				String strChoice = in.nextLine();
				if ("Y".equalsIgnoreCase(strChoice)) {
					isLeader = true;
					break;
				}
				if ("N".equalsIgnoreCase(strChoice)) {
					isLeader = false;
					break;
				}
			}

		}

		if (isLeader) {
			System.out.println("This instance will be a leader!");
			MonitorLeader leaderObj = new MonitorLeader();
			leaderObj.startLeader();
		} else {
			System.out.println("This instance will be a subordinate!");
			SubordinateStarter subordinate = new SubordinateStarter();
			subordinate.startSubordinate();
		}

		System.exit(0);
	}
}
