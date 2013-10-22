/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
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
package org.bench4Q.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBHelper {
	private static Properties prop = new Properties();
	private static DBHelper instance = new DBHelper();

	private DBHelper() {
		InputStream in = this.getClass().getResourceAsStream("database.properties");
		if (in != null) {
			try {
				prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can't find database.properties");

		}

	}

	public static DBHelper getInstance() {
		return instance;
	}

	public String getProperty(String key) {
		return (String) prop.get(key);
	}
}
