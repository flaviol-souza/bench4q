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
import java.util.Properties;

/**
 * @author duanzhiquan
 * 
 */
public final class Bench4QBuild {

	private static final String s_versionString;
	private static final String s_dateString;

	static {
		try {
			final InputStream buildPropertiesStream = Bench4QBuild.class
					.getClassLoader().getResourceAsStream(
							"org/bench4Q/resources/build.properties");

			if (buildPropertiesStream == null) {
				throw new IOException("Could not find build.properties");
			}

			final Properties properties = new Properties();
			properties.load(buildPropertiesStream);

			s_versionString = properties.getProperty("version");
			s_dateString = properties.getProperty("date");
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new ExceptionInInitializerError(e);
		}
	}

	private Bench4QBuild() {
	}

	/**
	 * @return
	 */
	public static String getName() {
		return "The Bench4Q " + getVersionString();
	}

	/**
	 * @return
	 */
	public static String getVersionString() {
		return s_versionString;
	}

	/**
	 * @return
	 */
	public static String getDateString() {
		return s_dateString;
	}
}
