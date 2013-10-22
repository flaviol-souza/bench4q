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


package scriptbq.ssh;

import java.io.*;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

/**
 * This class mainly aims at manipulating the SSH protocal. SSH protocal is the
 * basis of the communication bettween console and agent. Console deploys the agent
 * tool, configuration and script file to the agent through SSH. In addition, agent
 * start its RMI server by the shell command through SSH.
 */
public class BqSsh{
	
	/**
	 * The host name of the agent, in form of IP address
	 */
	private String hostname;
	/**
	 * The user name of the agent
	 */
	private String username;
	/**
	 * The password of the agent, corresponds to the username
	 */
	private String password;
	/**
	 * SSH connection bettween the console and agent
	 */
	private Connection conn;
	/**
	 * The session bettween the console and agent
	 */
	private Session sess;
	
	/**
	 * The constructor
	 * @param hostname  The name of agent's IP address.
	 * @param username  The name of agent's username.
	 * @param password  The password of the agent.
	 */
	public BqSsh(String hostname,String username,String password){
		this.hostname = hostname;
		this.username = username;
		this.password = password;	
	}
	
	/**
	 * This method is used to connect the agent.
	 * @return whether the console has been connected to the agent.
	 */
	public boolean Connect(){
		boolean isAuthenticated = false;
		conn = new Connection(hostname);	
		try {
			conn.connect();
			isAuthenticated = conn.authenticateWithPassword(username, password);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isAuthenticated;
	}
	
	/**
	 * This method is used to make shell command to agent by console.
	 * @param commandLine   The content of shell command.
	 * @throws IOException
	 */
	public void Command(String commandLine) throws IOException{
		sess = conn.openSession();
		sess.execCommand(commandLine);
		sess.close();
	}
	
	/**
	 * This method is used to copy file from local to remote directory
	 * @param localFile    The path of local file
	 * @param remoteDir	   The path of remote directory
	 */
	public void ScpFile(String localFile,String remoteDir){
		SCPClient sc = new SCPClient(conn);
		try {
			sc.put(localFile, remoteDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to colose SSH connection
	 */
	public void CloseSsh(){
		conn.close();
	}
	
	/**
	 * Method to display the SSH information
	 */
	public String toString(){
		return hostname+"/"+username+"/"+password;
	}
	
	/**
	 * Console uses this method to get remote file to local file
	 * system. It's mainly used for collecting the final testing
	 * result file.
	 * @param remoteFile   The path of remote file
	 * @param localDir     The path of local directory
	 */
	public void GetFile(String remoteFile,String localDir){
		SCPClient sc = new SCPClient(conn);
		try {
			sc.get(remoteFile, localDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
