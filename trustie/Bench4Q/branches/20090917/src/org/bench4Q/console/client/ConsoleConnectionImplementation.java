// Copyright (C) 2006 - 2008 Philip Aston
// All rights reserved.
//
// This file is part of The Grinder software distribution. Refer to
// the file LICENSE which is part of The Grinder distribution for
// licensing details. The Grinder distribution is available on the
// Internet at http://grinder.sourceforge.net/
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.bench4Q.console.client;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.BlockingSender;
import org.bench4Q.common.communication.CommunicationException;
import org.bench4Q.common.communication.Message;
import org.bench4Q.console.communication.server.GetNumberOfAgentsMessage;
import org.bench4Q.console.communication.server.ResetRecordingMessage;
import org.bench4Q.console.communication.server.ResetWorkerProcessesMessage;
import org.bench4Q.console.communication.server.ResultMessage;
import org.bench4Q.console.communication.server.StartRecordingMessage;
import org.bench4Q.console.communication.server.StartWorkerProcessesMessage;
import org.bench4Q.console.communication.server.StopAgentAndWorkerProcessesMessage;
import org.bench4Q.console.communication.server.StopRecordingMessage;

/**
 * Implementation of {@link ConsoleConnection} that uses a
 * {@link BlockingSender} to communicate with the console.
 * 
 * @author Philip Aston
 * @version $Revision:$
 */
final class ConsoleConnectionImplementation implements ConsoleConnection {

	private final BlockingSender m_consoleSender;

	ConsoleConnectionImplementation(BlockingSender consoleSender) {
		m_consoleSender = consoleSender;
	}

	/**
	 * Close the connection.
	 */
	public void close() {
		m_consoleSender.shutdown();
	}

	/**
	 * Start the console recording.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public void startRecording() throws ConsoleConnectionException {
		try {
			m_consoleSender.blockingSend(new StartRecordingMessage());
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to start recording", e);
		}
	}

	/**
	 * Stop the console recording.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public void stopRecording() throws ConsoleConnectionException {
		try {
			m_consoleSender.blockingSend(new StopRecordingMessage());
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to stop recording", e);
		}
	}

	/**
	 * Reset the console recording.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public void resetRecording() throws ConsoleConnectionException {
		try {
			m_consoleSender.blockingSend(new ResetRecordingMessage());
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to reset recording", e);
		}
	}

	/**
	 * How many agents are connected?
	 * 
	 * @return The number of agents.
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public int getNumberOfAgents() throws ConsoleConnectionException {
		final Message response;

		try {
			response = m_consoleSender.blockingSend(new GetNumberOfAgentsMessage());
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("getNumberOfLiveAgents()", e);
		}

		if (response instanceof ResultMessage) {
			final Object result = ((ResultMessage) response).getResult();

			if (result instanceof Integer) {
				return ((Integer) result).intValue();
			}
		}

		throw new ConsoleConnectionException("Unexpected response: " + response);
	}

	/**
	 * Stop all agent processes and their worker processes.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public void stopAgents() throws ConsoleConnectionException {

		try {
			m_consoleSender.blockingSend(new StopAgentAndWorkerProcessesMessage());
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to stop agent processes", e);
		}
	}

	/**
	 * Start all the worker processes.
	 * 
	 * @param properties
	 *            Properties that override the agents' local properties.
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public void startWorkerProcesses(Bench4QProperties properties, Args args)
			throws ConsoleConnectionException {

		try {
			m_consoleSender.blockingSend(new StartWorkerProcessesMessage(properties, args));
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to start worker processes", e);
		}
	}

	/**
	 * Reset all the worker processes.
	 * 
	 * @throws ConsoleConnectionException
	 *             If a communication error occurred.
	 */
	public void resetWorkerProcesses() throws ConsoleConnectionException {
		try {
			m_consoleSender.blockingSend(new ResetWorkerProcessesMessage());
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to reset worker processes", e);
		}
	}
}
