package org.bench4Q.common.util;

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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used for logging of Bench4Q Tool.
 * 
 * @author duanzhiquan
 * 
 */
public class Logger {
	private static Log log;
	/**
	 * Define a static Log variable,
	 */
	static {
		try {
			log = LogFactory.getLog(Logger.class);
		} catch (Exception ex) {
			System.out.println("can't init the Logger, caused by: " + ex);
		}
	}

	/**
	 * Get the log object
	 * 
	 * @return Log
	 */
	public static Log getLogger() {
		return log;
	}

}
