/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Developer(s): Wang Sa.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.console.rm;

import java.util.ArrayList;

/**
 * @author wang sa
 * 
 * this class is the data structure of server information.
 * 
 */
public class ServerInfo {
	
	//  CPU usage of the server
	private ArrayList<Double> cpu_ratio = new ArrayList<Double>();

	//  free memory of the server
	private ArrayList<Double> memory_usage = new ArrayList<Double>();
	
	//  the DISK read per second of the server
	private ArrayList<Double> DiskRead = new ArrayList<Double>();
	
	//  the DISK write per second of the server
	private ArrayList<Double> DiskWrite = new ArrayList<Double>();
	
	//  the network received per second of the server
	private ArrayList<Double> NetworkRecv = new ArrayList<Double>();
	
	//  the network sent per second of the server
	private ArrayList<Double> NetworkSent = new ArrayList<Double>();
	
	/**
	 * @return
	 */
	public ArrayList<Double> getCpu_ratio() {
		return cpu_ratio;
	}


	/**
	 * @param cpuRatio
	 */
	public void setCpu_ratio(ArrayList<Double> cpuRatio) {
		cpu_ratio = cpuRatio;
	}


	/**
	 * @return
	 */
	public ArrayList<Double> getMemory_usage() {
		return memory_usage;
	}


	/**
	 * @param memoryUsage
	 */
	public void setMemory_usage(ArrayList<Double> memoryUsage) {
		memory_usage = memoryUsage;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Double> getDiskRead() {
		return DiskRead;
	}


	/**
	 * @param diskRead
	 */
	public void setDiskRead(ArrayList<Double> diskRead) {
		DiskRead = diskRead;
	}


	/**
	 * @return
	 */
	public ArrayList<Double> getDiskWrite() {
		return DiskWrite;
	}


	/**
	 * @param diskWrite
	 */
	public void setDiskWrite(ArrayList<Double> diskWrite) {
		DiskWrite = diskWrite;
	}


	/**
	 * @return
	 */
	public ArrayList<Double> getNetworkRecv() {
		return NetworkRecv;
	}


	/**
	 * @param networkRecv
	 */
	public void setNetworkRecv(ArrayList<Double> networkRecv) {
		NetworkRecv = networkRecv;
	}


	/**
	 * @return
	 */
	public ArrayList<Double> getNetworkSent() {
		return NetworkSent;
	}


	/**
	 * @param networkSent
	 */
	public void setNetworkSent(ArrayList<Double> networkSent) {
		NetworkSent = networkSent;
	}


}
