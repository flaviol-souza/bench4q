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
package org.bench4Q.agent.common;

import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.CommunicationDefaults;
import org.bench4Q.common.communication.ConnectionType;
import org.bench4Q.common.communication.Connector;

/**
 * @author duanzhiquan
 * 
 */
public class ConnectorFactory {

	private final ConnectionType m_connectionType;

	/**
	 * Constructor.
	 * 
	 * @param connectionType
	 *            The connection type.
	 */
	public ConnectorFactory(ConnectionType connectionType) {
		m_connectionType = connectionType;
	}

	/**
	 * Factory method.
	 * 
	 * @param properties
	 *            Properties.
	 * @return A connector which can be used to contact the console.
	 */
	public Connector create(Bench4QProperties properties) {
		return new Connector(properties.getProperty(
				Bench4QProperties.CONSOLE_HOST,
				CommunicationDefaults.CONSOLE_HOST), properties.getInt(
				Bench4QProperties.CONSOLE_PORT,
				CommunicationDefaults.CONSOLE_PORT), m_connectionType);
	}
}
