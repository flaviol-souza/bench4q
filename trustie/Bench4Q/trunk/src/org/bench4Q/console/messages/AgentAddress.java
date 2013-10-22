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

package org.bench4Q.console.messages;

import org.bench4Q.common.communication.Address;
import org.bench4Q.common.processidentity.AgentIdentity;

/**
 * {@link Address} for an Agent.
 */
public final class AgentAddress implements Address {

	private static final long serialVersionUID = 1;

	private final AgentIdentity m_agentIdentity;

	/**
	 * Constructor.
	 * 
	 * @param agentIdentity
	 *            The agent identity.
	 */
	public AgentAddress(AgentIdentity agentIdentity) {
		m_agentIdentity = agentIdentity;
	}

	/**
	 * Whether this address includes the given address.
	 * 
	 * <p>
	 * The general <code>includes</code> relationship is transitive, reflexive,
	 * and asymmetric. Simple implementations of "physical addresses" should
	 * just delegate to equals().
	 * </p>
	 * 
	 * @param address
	 *            The address to check.
	 * @return <code>true</code> if and only if we include <code>address</code>.
	 */
	public boolean includes(Address address) {
		return equals(address);
	}

	/**
	 * Hash code.
	 * 
	 * @return The hash code.
	 */
	public int hashCode() {
		return m_agentIdentity.hashCode();
	}

	/**
	 * Equality.
	 * 
	 * @param o
	 *            Object to compare.
	 * @return <code>true</code> if and only if the given object is equal.
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final AgentAddress other = (AgentAddress) o;

		return m_agentIdentity.equals(other.m_agentIdentity);
	}
}
