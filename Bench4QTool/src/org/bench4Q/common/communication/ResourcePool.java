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

import java.util.List;

/**
 * A pool of resources.
 * 
 * <p>
 * Each resource in the pool is wrapped in a wrapper that keeps track of whether
 * it is currently in use. Clients access resources through {@link Reservation}
 * s.
 * </p>
 */
interface ResourcePool {

	/**
	 * Adds a resource to the pool.
	 * 
	 * @param resource
	 *            The resource to add.
	 * @return Allows the client to notify the resource pool if the resource has
	 *         been closed.
	 */
	Closeable add(final Resource resource);

	/**
	 * Returns a resource, reserved for exclusive use by the caller.
	 * 
	 * <p>
	 * Resources are handed out to callers in order. A Sentinel is returned once
	 * every cycle; if no resources are free the Sentinel is always returned.
	 * </p>
	 * 
	 * @return The resource. It is up to the caller to free or close the
	 *         resource.
	 */
	Reservation reserveNext();

	/**
	 * Returns a list of all the current resources. Blocks until all
	 * Reservations can be reserved. The Sentinel is not included in the list.
	 * 
	 * @return The resources. It is up to the caller to free or close each
	 *         resource.
	 */
	List reserveAll();

	/**
	 * Close the resources currently in the pool. Resources can be closed while
	 * reserved.
	 * 
	 * <p>
	 * This doesn't "shutdown" the pool. I don't want to introduce an extra
	 * shutdown state, and the pollute our interface with a shutdown exception.
	 * It's up to the owner of the pool to prevent new things from being added
	 * to the pool if necessary.
	 * </p>
	 */
	void closeCurrentResources();

	/**
	 * Count the active resources.
	 * 
	 * @return The number of active resources.
	 */
	int countActive();

	/**
	 * Add a new listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addListener(Listener listener);

	/**
	 * Public interface to a resource.
	 */
	public interface Resource {
		void close();
	}

	/**
	 * Listener interface.
	 */
	public interface Listener {

		void resourceAdded(Resource resource);

		void resourceClosed(Resource resource);
	}

	/**
	 * Something that can be closed.
	 */
	public interface Closeable {

		void close();

		boolean isClosed();
	}

	/**
	 * Public interface to a resource reservation.
	 */
	public interface Reservation extends Closeable {

		boolean isSentinel();

		Resource getResource();

		void free();
	}
}
