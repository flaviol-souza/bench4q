/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
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
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.console.ui.transfer;

import org.bench4Q.console.common.ConsoleException;

/**
 * those classes witch need to know Agent messages implement this interface.
 * 
 * @author duanzhiquan
 * 
 */
public interface AgentInfoObserver {

	/**
	 * @param agentInfo
	 * @throws ConsoleException
	 */
	public void addAgent(AgentInfo agentInfo) throws ConsoleException;

	/**
	 * @param agentInfo
	 */
	public void removeAgent(AgentInfo agentInfo);

	/**
	 * @param agentInfo
	 */
	public void getResult(AgentInfo agentInfo);

	/**
	 * 
	 */
	public void restartTest();
	
	public void saveTheChart(String prefix);

}
