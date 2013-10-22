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

package org.bench4Q.agent;

import java.io.Serializable;

import org.bench4Q.common.util.UniqueIdentityGenerator;

/**
 * Common process identity implementation.
 * 
 */
abstract class AbstractProcessIdentityImplementation implements Serializable {

	private static final UniqueIdentityGenerator s_identityGenerator = new UniqueIdentityGenerator();

	private final String m_identity;
	private String m_name;
	private String m_IP;

	protected AbstractProcessIdentityImplementation(String name) {
		m_identity = s_identityGenerator.createUniqueString(name);
		m_name = name;
	}

	/**
	 * Return the process name.
	 * 
	 * @return The process name.
	 */
	public final String getName() {
		return m_name;
	}

	/**
	 * Allows the public process name to be changed.
	 * 
	 * @param name
	 *            The new process name.
	 */
	public void setName(String name) {
		m_name = name;
	}

	public void setIP(String IP) {
		m_IP = IP;

	}

	public final String getIP() {
		return m_IP;
	}

	/**
	 * Implement equality semantics. We compare equal to all copies of ourself,
	 * but nothing else.
	 * 
	 * @return The hash code.
	 */
	public final int hashCode() {
		return m_identity.hashCode();
	}

	/**
	 * Implement equality semantics. We compare equal to all copies of ourself,
	 * but nothing else.
	 * 
	 * @param o
	 *            Object to compare.
	 * @return <code>true</code> => its equal.
	 */
	public final boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		// instanceof does not break symmetry since equals() is final.
		if (!(o instanceof AbstractProcessIdentityImplementation)) {
			return false;
		}

		final String otherIdentity = ((AbstractProcessIdentityImplementation) o).m_identity;

		return m_identity.equals(otherIdentity)
				&& getClass().equals(o.getClass());
	}

	/**
	 * String representation.
	 * 
	 * @return A string representation of this process identity.
	 */
	public final String toString() {
		return "Process '" + m_name + "' [" + m_identity + "]";
	}
}
