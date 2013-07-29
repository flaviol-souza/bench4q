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

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class say_hello {

	public static void print_hello(HttpSession session, HttpServletRequest req, PrintWriter out) {

		// If we have seen this session id before
		if (!session.isNew()) {
			int C_ID[] = (int[]) session.getValue("C_ID");
			// check and see if we have a customer name yet
			if (C_ID != null) // Say hello.
				out.println("Hello " + (String) session.getValue("C_FNAME") + " "
						+ (String) session.getValue("C_LNAME"));
			else
				out.println("Hello unknown user");
		} else {
			// This is a brand new session
			out.println("This is a new session!");
			// Check to see if a C_ID was given. If so, get the customer name
			// from the database and say hello.
			String C_IDstr = req.getParameter("C_ID");
			if (C_IDstr != null) {
				String name[];
				int C_ID[] = new int[1];
				C_ID[0] = Integer.parseInt(C_IDstr, 10);
				out.flush();
				// Use C_ID to get the user name from the database.
				name = Database.getName(C_ID[0]);
				// Set the values for this session.
				if (name == null) {
					out.println("Hello unknown user!");
					return;
				}
				session.putValue("C_ID", C_ID);
				session.putValue("C_FNAME", name[0]);
				session.putValue("C_LNAME", name[1]);
				out.println("Hello " + name[0] + " " + name[1] + ".");

			} else
				out.println("Hello unknown user!");
		}
	}
}
