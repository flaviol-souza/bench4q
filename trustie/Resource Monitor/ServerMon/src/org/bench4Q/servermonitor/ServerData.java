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
package org.bench4Q.servermonitor;

import java.io.Serializable;

/**
 * The data structure for server resource monitoring data
 * 
 * @author xiaowei zhou
 * 2010-7-8
 *
 */
public class ServerData implements Serializable{

	private static final long serialVersionUID = -8341326814648270123L;
	
	private double cpuPercent;
	private double memAvailMB;
	private double diskReadBytesPerSecond;
	private double diskWriteBytesPerSecond;
	private double networkRecvBytesPerSecond;
	private double networkSentBytesPerSecond;
	private long curTimeMillis;
	
	public ServerData(ServerData monData) {
		this.cpuPercent=monData.cpuPercent;
		this.memAvailMB=monData.memAvailMB;
		this.diskReadBytesPerSecond=monData.diskReadBytesPerSecond;
		this.diskWriteBytesPerSecond=monData.diskWriteBytesPerSecond;
		this.networkRecvBytesPerSecond=monData.networkRecvBytesPerSecond;
		this.networkSentBytesPerSecond=monData.networkSentBytesPerSecond;
		this.curTimeMillis=monData.curTimeMillis;
	}
	
	public ServerData() {
	}
	
	public double getCpuPercent() {
		return cpuPercent;
	}
	public void setCpuPercent(double cpuPercent) {
		this.cpuPercent = cpuPercent;
	}
	public double getMemAvailMB() {
		return memAvailMB;
	}
	public void setMemAvailMB(double memAvailMB) {
		this.memAvailMB = memAvailMB;
	}
	public double getDiskReadBytesPerSecond() {
		return diskReadBytesPerSecond;
	}
	public void setDiskReadBytesPerSecond(double diskReadBytesPerSecond) {
		this.diskReadBytesPerSecond = diskReadBytesPerSecond;
	}
	public double getDiskWriteBytesPerSecond() {
		return diskWriteBytesPerSecond;
	}
	public void setDiskWriteBytesPerSecond(double diskWriteBytesPerSecond) {
		this.diskWriteBytesPerSecond = diskWriteBytesPerSecond;
	}
	public double getNetworkRecvBytesPerSecond() {
		return networkRecvBytesPerSecond;
	}
	public void setNetworkRecvBytesPerSecond(double networkRecvBytesPerSecond) {
		this.networkRecvBytesPerSecond = networkRecvBytesPerSecond;
	}
	public double getNetworkSentBytesPerSecond() {
		return networkSentBytesPerSecond;
	}
	public void setNetworkSentBytesPerSecond(double networkSentBytesPerSecond) {
		this.networkSentBytesPerSecond = networkSentBytesPerSecond;
	}
	public long getCurTimeMillis() {
		return curTimeMillis;
	}
	public void setCurTimeMillis(long curTimeMillis) {
		this.curTimeMillis = curTimeMillis;
	}	
}
