package org.bench4Q.agent.messages;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.Message;

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
	 * @param agentNumber
	 *            The console allocated agent number.
	 */
	public StartJASptEMessage(Bench4QProperties properties, Args args, int agentNumber) {
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

	public Args getArgs() {
		return m_args;
	}
}
