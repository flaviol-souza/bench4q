/**
 * =========================================================================
 * 					Bench4Q version 1.1.1
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
import java.io.PrintWriter;

import org.bench4Q.agent.AgentImplementation;
import org.bench4Q.agent.AgentInterface;
import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Logger;
import org.bench4Q.common.util.SimpleLogger;

/**
 * @author duanzhiquan
 *
 */
public final class Agent extends AbstractMainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final Logger logger = new SimpleLogger("agent", new PrintWriter(System.out),
				new PrintWriter(System.err));

		try {
			final Agent agent = new Agent(args, logger);
			agent.run();
		} catch (LoggedInitialisationException e) {
			System.exit(1);
		} catch (Throwable e) {
			final PrintWriter errorWriter = logger.getErrorLogWriter();
			e.printStackTrace(errorWriter);
			errorWriter.flush();
			System.exit(2);
		}

		System.exit(0);
	}

	private final AgentInterface m_agent;

	private Agent(String[] args, Logger logger) throws Bench4QException {
		super(logger);

		File propertiesFile = null;

		for (int i = 0; i < args.length; ++i) {
			if (i == args.length - 1 && !args[i].startsWith("-")) {
				propertiesFile = new File(args[i]);
			} else {
				throw barfUsage();
			}
		}

		m_agent = new AgentImplementation(logger, propertiesFile, true);

	}

	private void run() throws Bench4QException {
		m_agent.run();
		m_agent.shutdown();
	}
}
