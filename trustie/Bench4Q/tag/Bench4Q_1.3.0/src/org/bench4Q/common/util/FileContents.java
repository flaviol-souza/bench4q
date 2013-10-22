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

package org.bench4Q.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Closer;
import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Pairing of relative filename and file contents.
 */
public final class FileContents implements Serializable {

	private static final long serialVersionUID = -3140708892260600117L;

	/** @serial The file name. */
	private final File m_filename;

	/** @serial The file data. */
	private final byte[] m_contents;

	/**
	 * Constructor. Builds a FileContents from local file system.
	 * 
	 * @param baseDirectory
	 *            Base directory used to resolve relative filenames.
	 * @param file
	 *            Relative filename.
	 * @exception FileContentsException
	 *                If an error occurs.
	 */
	public FileContents(File baseDirectory, File file)
			throws FileContentsException {

		if (file.isAbsolute()) {
			throw new FileContentsException("Original file name '" + file
					+ "' is not relative");
		}

		m_filename = file;

		final File localFile = new File(baseDirectory, file.getPath());

		final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

		try {
			new StreamCopier(4096, true).copy(new FileInputStream(localFile),
					byteOutputStream);
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new FileContentsException("Failed to read file: "
					+ e.getMessage(), e);
		}

		m_contents = byteOutputStream.toByteArray();
	}

	/**
	 * Allow unit tests access to the relative file name.
	 * 
	 * @return The file name.
	 */
	File getFilename() {
		return m_filename;
	}

	/**
	 * Allow unit tests access to the file contents.
	 * 
	 * @return a <code>byte[]</code> value
	 */
	byte[] getContents() {
		return m_contents;
	}

	/**
	 * Write the <code>FileContents</code> to the given directory, overwriting
	 * any existing content.
	 * 
	 * @param baseDirectory
	 *            The base directory.
	 * @exception FileContentsException
	 *                If an error occurs.
	 */
	public void create(Directory baseDirectory) throws FileContentsException {

		final File localFile = baseDirectory.getFile(getFilename());

		localFile.getParentFile().mkdirs();

		OutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(localFile);
			outputStream.write(getContents());
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new FileContentsException("Failed to create file: "
					+ e.getMessage(), e);
		} finally {
			Closer.close(outputStream);
		}
	}

	/**
	 * Return a description of the <code>FileContents</code>.
	 * 
	 * @return The description.
	 */
	public String toString() {
		return "\"" + getFilename() + "\" (" + getContents().length + " bytes)";
	}

	/**
	 * Exception that indicates a <code>FileContents</code> related problem.
	 */
	public static final class FileContentsException extends Bench4QException {
		FileContentsException(String message) {
			super(message);
		}

		FileContentsException(String message, Throwable nested) {
			super(message, nested);
		}
	}
}
