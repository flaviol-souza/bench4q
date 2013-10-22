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
package scriptbq.tree;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import scriptbq.BqConstant;
import scriptbq.ssh.BqSsh;
import src.communication.RMIInterface;

/**
 * The class BqTreeObject is the basic class for the BqTreeView. In Bench4Q_Script, it 
 * stands for the agent node and provides several method for user to control the agent
 * from the BqTreeView.
 */
public class BqTreeObject {
	
	/**
	 * name is the node's name stands for agent
	 */
	private String name;
	/**
	 * parent is the node's parent
	 */
	private BqTreeParent parent;
	/**
	 * The IP address stands of the agent
	 */
	private String IpAddress;
	/**
	 * The username of the agent corresponding to the BqTreeObject
	 */
	private String username;
	/**
	 * The password of the agent corresponding to the BqTreeObject
	 */
	private String password;
	/**
	 * The SSH connection of the agent corresponding to the BqTreeObject
	 */
	private BqSsh agentSsh;
	/**
	 * The RMI interface of the agent corresponding to the BqTreeObject
	 */
	private RMIInterface BqRMI;
	
	/**
	 * Method to get the IP address of the agent
	 * @return
	 */
	public String getIpAddress(){
		return IpAddress;
	}
	
	/**
	 * Method to get the username of the agent
	 * @return
	 */
	public String getUsername(){
		return username;
	}
	
	/**
	 * Method to get the password of the agent
	 * @return
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Method to get the BqSsh of the agent
	 * @return
	 */
	public BqSsh getSsh(){
		return agentSsh;
	}
	
	/**
	 * Method to set the BqSsh of the agent
	 * @param AgentSsh
	 */
	public void setSsh(BqSsh AgentSsh){
		agentSsh = AgentSsh;
	}
	
	/**
	 * Method to set the RMI interface of the agent
	 * @param tmp
	 */
	public void setRMI(RMIInterface tmp){
		this.BqRMI = tmp;
	}
	
	/**
	 * Method to get the RMI interface of the agent
	 * @return
	 */
	public RMIInterface getRMI(){
		return this.BqRMI;
	}
	
	/**
	 * Method to set the information of the agent
	 * @param IP
	 * @param username
	 * @param password
	 */
	public void SetInfo(String IP, String username, String password){
		this.IpAddress = IP;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * constructor
	 * @param name
	 */
	public BqTreeObject(String name){
		this.name = name;
	}
	
	/**
	 * Method to get the name of the BqTreeObject
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Method to set the parent of BqTreeObject
	 * @param parent
	 */
	public void setParent(BqTreeParent parent) {
		this.parent = parent;
	}
	
	/**
	 * Method to get the parent
	 * @return
	 */
	public BqTreeParent getParent() {
		return parent;
	}
	
	/**
	 * Method to show the information of the class
	 */
	public String toString() {
		return getName();
	}
	
	/**
	 * Method to reset the RMI interface of the BqTreeObject and the agent,
	 * it is important when the network fails
	 */
	public void reset(){
		try {
			getSsh().Command("pkill java");
			getSsh().Command(". /etc/profile;java -jar agent.jar");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String rmiSite = BqConstant.RMIPrefix+this.getIpAddress()+BqConstant.RMISuffix;
		//String rmiSite = BqConstant.RMIPrefix+"ie"+BqConstant.RMISuffix;
		RMIInterface rmi = null;
		try {
			rmi = (RMIInterface)Naming.lookup(rmiSite);
		} catch (Exception e)
		{
			e.printStackTrace();
			this.reset();
		}
		if(rmi!=null){
			this.setRMI(rmi);
		}
	}
	
}
