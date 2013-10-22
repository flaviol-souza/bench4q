/**
 * =========================================================================
 * 					Bench4Q Server Cluster Monitor
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

/**
 * @author Administrator
 *
 */
public class ServerDataWithTimeOfReceipt extends ServerData {

	private static final long serialVersionUID = -4333012583324933330L;

	/**
	 * the time (on leader machine) the leader received this ServerData,
	 * it can be converted to absolute time with Java's GregorianCalendar class
	 */
	private long receiptTimeMillis;

	/**
	 * @param monData
	 */
	public ServerDataWithTimeOfReceipt(ServerData monData) {
		super(monData);
	}
	
	/**
	 * 
	 */
	public ServerDataWithTimeOfReceipt() {
		super();
	}

	/**
	 * @return
	 */
	public long getReceiptTimeMillis() {
		return receiptTimeMillis;
	}

	/**
	 * @param receiptTimeMillis
	 */
	public void setReceiptTimeMillis(long receiptTimeMillis) {
		this.receiptTimeMillis = receiptTimeMillis;
	}
}
