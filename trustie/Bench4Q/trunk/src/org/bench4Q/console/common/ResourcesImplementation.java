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

package org.bench4Q.console.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.bench4Q.common.Closer;
import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Type safe interface to resource bundle.
 */
public final class ResourcesImplementation implements Resources {

	private PrintWriter m_errorWriter = new PrintWriter(System.err, true);
	private final ResourceBundle m_resources;
	private final String m_package;

	/**
	 * Constructor.
	 * 
	 * @param bundleName
	 *            Name of resource bundle. The package part of the name is used
	 *            to resolve the location of resources referred to in the
	 *            resource bundle.
	 */
	public ResourcesImplementation(String bundleName) {

		m_resources = ResourceBundle.getBundle(bundleName);

		final int lastDot = bundleName.lastIndexOf(".");

		if (lastDot > 0) {
			m_package = "/"
					+ bundleName.substring(0, lastDot + 1).replace('.', '/');
		} else {
			m_package = "/";
		}
	}

	/**
	 * Set a writer to report warnings to.
	 * 
	 * @param writer
	 *            The writer.
	 */
	public void setErrorWriter(PrintWriter writer) {
		m_errorWriter = writer;
	}

	/**
	 * Overloaded version of {@link #getString(String, boolean)} which writes
	 * out a waning if the resource is missing.
	 * 
	 * @param key
	 *            The resource key.
	 * @return The string.
	 */
	public String getString(String key) {
		return getString(key, true);
	}

	/**
	 * Use key to look up resource which names image URL. Return the image.
	 * 
	 * @param key
	 *            The resource key.
	 * @param warnIfMissing
	 *            true => write out an error message if the resource is missing.
	 * @return The string.
	 */
	public String getString(String key, boolean warnIfMissing) {

		try {
			return m_resources.getString(key);
		} catch (MissingResourceException e) {
			if (warnIfMissing) {
				m_errorWriter.println("Warning - resource " + key
						+ " not specified");
				return "";
			}

			return null;
		}
	}

	/**
	 * Overloaded version of {@link #getImageIcon(String, boolean)} which
	 * doesn't write out a waning if the resource is missing.
	 * 
	 * @param key
	 *            The resource key.
	 * @return The image.
	 */
	public ImageIcon getImageIcon(String key) {
		return getImageIcon(key, false);
	}

	/**
	 * Use key to look up resource which names image URL. Return the image.
	 * 
	 * @param key
	 *            The resource key.
	 * @param warnIfMissing
	 *            true => write out an error message if the resource is missing.
	 * @return The image
	 */
	public ImageIcon getImageIcon(String key, boolean warnIfMissing) {
		final URL resource = get(key, warnIfMissing);

		return resource != null ? new ImageIcon(resource) : null;
	}

	/**
	 * Use <code>key</code> to identify a file by URL. Return contents of file
	 * as a String.
	 * 
	 * @param key
	 *            Resource key used to look up URL of file.
	 * @param warnIfMissing
	 *            true => write out an error message if the resource is missing.
	 * @return Contents of file.
	 */
	public String getStringFromFile(String key, boolean warnIfMissing) {

		final URL resource = get(key, warnIfMissing);

		if (resource != null) {
			Reader in = null;

			try {
				in = new InputStreamReader(resource.openStream());

				final StringWriter out = new StringWriter();

				final char[] buffer = new char[128];

				while (true) {
					final int n = in.read(buffer);

					if (n == -1) {
						break;
					}

					out.write(buffer, 0, n);
				}

				out.close();

				return out.toString();
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
				m_errorWriter.println("Warning - could not read " + resource);
			} finally {
				Closer.close(in);
			}
		}

		return null;
	}

	private URL get(String key, boolean warnIfMissing) {
		final String name = getString(key, warnIfMissing);

		if (name == null || name.length() == 0) {
			return null;
		}

		final URL url = this.getClass().getResource(m_package + name);

		if (url == null) {
			m_errorWriter.println("Warning - could not load resource " + name);
		}

		return url;
	}
}
