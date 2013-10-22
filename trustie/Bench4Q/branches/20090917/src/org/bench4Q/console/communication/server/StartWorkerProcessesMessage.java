// Copyright (C) 2007 - 2008 Philip Aston
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

package org.bench4Q.console.communication.server;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.Message;

/**
 * Message indicating that all worker processes should be started.
 * 
 * @author Philip Aston
 * @version $Revision:$
 */
public class StartWorkerProcessesMessage implements Message {
	private static final long serialVersionUID = 1;

	/** @serial Properties to use to start the worker processes. */
	private final Bench4QProperties m_properties;
	private final Args m_args;

	/**
	 * Constructor.
	 * 
	 * @param properties
	 *            Properties that override the agents' local properties.
	 */
	public StartWorkerProcessesMessage(Bench4QProperties properties, Args args) {
		m_args = args;
		m_properties = properties;
	}

	/**
	 * Accessor for the properties.
	 * 
	 * @return The properties.
	 */
	public Bench4QProperties getProperties() {
		return m_properties;
	}

	public Args getArgs() {
		return m_args;
	}

}
