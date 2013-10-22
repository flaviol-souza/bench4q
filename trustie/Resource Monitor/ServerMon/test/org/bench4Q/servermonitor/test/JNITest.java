/**
 * =========================================================================
 * 						Bench4Q Server Monitor
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
package org.bench4Q.servermonitor.test;

import org.bench4Q.servermonitor.ServerData;
import org.bench4Q.servermonitor.ServerDataManager;

/**
 * The class for testing native method calling
 * @author zhouxiaowei08
 *
 */
public class JNITest {

	public static void main(String args[]) throws InterruptedException {
		while (true) {
			ServerData serverDat = ServerDataManager.getServerData();
			System.out.println("CPU Usage: " + serverDat.getCpuPercent());
			System.out.println("Available Memory in MBytes: " + serverDat.getMemAvailMB());
			System.out.println("Disk read per second in bytes: " + serverDat.getDiskReadBytesPerSecond());
			System.out.println("Disk write per second in bytes: " + serverDat.getDiskWriteBytesPerSecond());
			System.out.println("Network receive per second in bytes: " + serverDat.getNetworkRecvBytesPerSecond());
			System.out.println("Network send per second in bytes: " + serverDat.getNetworkSentBytesPerSecond());
			System.out.println();
			Thread.sleep(1000);
		}
	}
}
