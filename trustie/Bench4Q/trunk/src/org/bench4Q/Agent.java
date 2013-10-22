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
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q;

import java.io.File;

import org.bench4Q.agent.AgentImplementation;
import org.bench4Q.agent.AgentInterface;
import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.util.Logger;

/**
 * The entry of Agent.
 * 
 * @author duanzhiquan
 * 
 */
public final class Agent {

	private final AgentInterface m_agent;

	private Agent(String[] args) throws Bench4QException {

		File propertiesFile = null;

		for (int i = 0; i < args.length; ++i) {
			if (i == args.length - 1 && !args[i].startsWith("-")) {
				propertiesFile = new File(args[i]);
			} else {
				// other possible arguments.
			}
		}
		m_agent = new AgentImplementation(propertiesFile);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			final Agent agent = new Agent(args);
			agent.run();
		} catch (Bench4QException e) {
			Logger.getLogger().fatal("Cann't initiate agent.", e);
			System.exit(1);
		}
		System.exit(0);
	}

	private void run() throws Bench4QException {
		m_agent.run();
		m_agent.shutdown();
	}
}
