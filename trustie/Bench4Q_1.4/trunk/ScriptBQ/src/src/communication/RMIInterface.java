/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at  
 * http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * http://www.trustie.com/projects/project/show/Bench4Q_Script
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
 *  * Initial developer(s): Wangsa , Tianfei , WUYulong , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */


package src.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The RMIInterface is the interface implemented by the agent, 
 * the console makes commands through the methods of this it.
 * It is the basis of the communication between console and agents.
 */
public interface RMIInterface extends Remote{
	
	/**
	 * This method is used to make agents generate load to SUT.
	 */
	public void getStarted() throws RemoteException;
	
	/**
	 * Console uses this method to get the real time throughput result.
	 * @return  The throughput data
	 */
	public int getThroughput() throws RemoteException;
	
	/**
	 * Console uses this method to get the real time response-time result.
	 * @return  The response time data
	 */	
	public double getResponsetime() throws RemoteException;
	
	
}
