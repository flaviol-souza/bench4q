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

package org.bench4Q.common.communication;

import java.io.OutputStream;

import org.bench4Q.common.Closer;
import org.bench4Q.common.communication.ResourcePool.Resource;
import org.bench4Q.common.util.thread.Executor;

/**
 * Manages the sending of messages to many streams.
 */
public final class FanOutStreamSender extends AbstractFanOutSender {

	/**
	 * Constructor.
	 * 
	 * @param numberOfThreads
	 *            Number of sender threads to use.
	 */
	public FanOutStreamSender(int numberOfThreads) {
		this(new Executor(numberOfThreads));
	}

	/**
	 * Constructor.
	 * 
	 * @param executor
	 *            Executor to use.
	 */
	private FanOutStreamSender(Executor executor) {
		super(executor, new ResourcePoolImplementation());
	}

	/**
	 * Add a stream.
	 * 
	 * @param stream
	 *            The stream.
	 */
	public void add(OutputStream stream) {
		getResourcePool().add(new OutputStreamResource(stream));
	}

	/**
	 * Shut down this sender.
	 */
	public void shutdown() {
		super.shutdown();
		getResourcePool().closeCurrentResources();
	}

	/**
	 * Return an output stream from a resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The output stream.
	 */
	protected OutputStream resourceToOutputStream(ResourcePool.Resource resource) {

		return ((OutputStreamResource) resource).getOutputStream();
	}

	/**
	 * We don't support addressing individual streams.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The address, or <code>null</code> if the resource has no address.
	 */
	protected Address getAddress(Resource resource) {
		return null;
	}

	private static final class OutputStreamResource implements
			ResourcePool.Resource {

		private final OutputStream m_outputStream;

		public OutputStreamResource(OutputStream outputStream) {
			m_outputStream = outputStream;
		}

		public OutputStream getOutputStream() {
			return m_outputStream;
		}

		public void close() {
			Closer.close(m_outputStream);
		}
	}
}
