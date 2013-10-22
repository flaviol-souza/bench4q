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
package org.bench4Q.agent.messages;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.Message;

/**
 * @author duanzhiquan
 * 
 */
public final class StartJASptEMessage implements Message {

	private static final long serialVersionUID = 4L;

	private final Bench4QProperties m_properties;

	private final int m_agentNumber;

	private final Args m_args;

	/**
	 * Constructor.
	 * 
	 * @param properties
	 *            A set of properties that override values in the Agents' local
	 *            files.
	 * @param args
	 * @param agentNumber
	 *            The console allocated agent number.
	 */
	public StartJASptEMessage(Bench4QProperties properties, Args args,
			int agentNumber) {
		m_args = args;
		m_properties = properties;
		m_agentNumber = agentNumber;
	}

	/**
	 * A set of properties that override values in the Agents' local files.
	 * 
	 * @return The properties.
	 */
	public Bench4QProperties getProperties() {
		return m_properties;
	}

	/**
	 * The console allocated agent number.
	 * 
	 * @return The agent number.
	 */
	public int getAgentNumber() {
		return m_agentNumber;
	}

	/**
	 * @return args
	 */
	public Args getArgs() {
		return m_args;
	}
}
