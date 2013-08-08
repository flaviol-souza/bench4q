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

package org.bench4Q.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * Static utility methods to close resource handles.
 * 
 * <p>
 * I considered a template method approach, but that's equally ugly.
 * </p>
 * 
 * <p>
 * The methods are intended to be called from finally blocks, and so handle all
 * exceptions quietly; with the exception of {@link InterruptedException}s which
 * are always converted to {@link UncheckedInterruptedException}s.
 * 
 * @author Philip Aston
 * @version $Revision:$
 */
public final class Closer {

	private Closer() {
	}

	/**
	 * Close the resource.
	 * 
	 * @param reader
	 *            The resource to close.
	 */
	public static void close(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
			}
		}
	}

	/**
	 * Close the resource.
	 * 
	 * @param writer
	 *            The resource to close.
	 */
	public static void close(Writer writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
			}
		}
	}

	/**
	 * Close the resource.
	 * 
	 * @param inputStream
	 *            The resource to close.
	 */
	public static void close(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
			}
		}
	}

	/**
	 * Close the resource.
	 * 
	 * @param outputStream
	 *            The resource to close.
	 */
	public static void close(OutputStream outputStream) {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
			}
		}
	}

	/**
	 * Close the resource.
	 * 
	 * @param socket
	 *            The resource to close.
	 */
	public static void close(Socket socket) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
			}
		}
	}
}
