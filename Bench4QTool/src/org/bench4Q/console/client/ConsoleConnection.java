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
package org.bench4Q.console.client;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;

/**
 * Console API.
 * 
 * <p>
 * <b>Warning: </b> This API is under development and not stable. It will
 * change.
 * </p>
 */
public interface ConsoleConnection {

	/**
	 * Close the connection.
	 */
	void close();

	/**
	 * Start the console recording.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	void startRecording() throws ConsoleConnectionException;

	/**
	 * Stop the console recording.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	void stopRecording() throws ConsoleConnectionException;

	/**
	 * Reset the console recording.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	void resetRecording() throws ConsoleConnectionException;

	/**
	 * How many agents are connected?
	 * 
	 * @return The number of agents.
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	int getNumberOfAgents() throws ConsoleConnectionException;

	/**
	 * Stop all agent processes.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	void stopAgents() throws ConsoleConnectionException;

	/**
	 * Start all the worker processes.
	 * 
	 * @param properties
	 *            Properties that override the agents' local properties.
	 * @param args
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	void startWorkerProcesses(Bench4QProperties properties, Args args)
			throws ConsoleConnectionException;

	/**
	 * Reset all the worker processes.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	void resetWorkerProcesses() throws ConsoleConnectionException;
}
