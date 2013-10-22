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

import javax.swing.ImageIcon;

/**
 * Type safe interface to resource bundle.
 */
public interface Resources {

	/**
	 * Overloaded version of {@link #getString(String, boolean)} which writes
	 * out a waning if the resource is missing.
	 * 
	 * @param key
	 *            The resource key.
	 * @return The string.
	 */
	String getString(String key);

	/**
	 * Use key to look up resource which names image URL. Return the image.
	 * 
	 * @param key
	 *            The resource key.
	 * @param warnIfMissing
	 *            true => write out an error message if the resource is missing.
	 * @return The string.
	 */
	String getString(String key, boolean warnIfMissing);

	/**
	 * Overloaded version of {@link #getImageIcon(String, boolean)} which
	 * doesn't write out a waning if the resource is missing.
	 * 
	 * @param key
	 *            The resource key.
	 * @return The image.
	 */
	ImageIcon getImageIcon(String key);

	/**
	 * Use key to look up resource which names image URL. Return the image.
	 * 
	 * @param key
	 *            The resource key.
	 * @param warnIfMissing
	 *            true => write out an error message if the resource is missing.
	 * @return The image
	 */
	ImageIcon getImageIcon(String key, boolean warnIfMissing);

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
	String getStringFromFile(String key, boolean warnIfMissing);
}
