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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class customer_registration_servlet extends HttpServlet {

	/**
	 * 2009-3-6 author: duanzhiquan Technology Center for Software Engineering
	 * Institute of Software, Chinese Academy of Sciences Beijing 100190, China
	 * Email:duanzhiquan07@otcaix.iscas.ac.cn
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,
			ServletException {

		String url;
		HttpSession session = req.getSession(false);
		
		// by xiaowei zhou, determine session-based differentiated service priority level, 20101116
		String strSessionPriorityLevel = req
				.getParameter(Util.SESSION_PRIORITY_KEY);
		Integer igrSessionPri = null;
		if (strSessionPriorityLevel != null
				&& !strSessionPriorityLevel.equals("")) {
			try {
				igrSessionPri = Integer.valueOf(strSessionPriorityLevel);
			} catch (NumberFormatException e) {
				// ignore, use default
			}
			if (igrSessionPri != null) {
				if (igrSessionPri < 1 || igrSessionPri > Util.PRIORITY_LEVELS) {
					igrSessionPri = Util.DEFAULT_PRIORITY;
				}
				if (session != null) {
					session.setAttribute(Util.DIFFSERV_SESSION_PRIORITY_KEY,
							igrSessionPri);
				}
			}
		}
		
		PrintWriter out = res.getWriter();
		// Set the content type of this servlet's result.
		res.setContentType("text/html");

		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		String username;
		if (C_ID != null) {
			int c_idnum = Integer.parseInt(C_ID);
			username = Database.GetUserName(c_idnum);
		} else
			username = "";

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML>\n");
		out.print("<HEAD><TITLE>Customer Registration</TITLE></HEAD>\n");
		out.print("<BODY BGCOLOR=\"#ffffff\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");
		out.print("</H1><H2 ALIGN=\"center\">Customer Registration Page</H2>\n");
		
		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4
		String sessionIdStrToAppend = req.getRequestedSessionId();
		if (sessionIdStrToAppend != null) {
			sessionIdStrToAppend = ";jsessionid=" + sessionIdStrToAppend;
		} else {
			sessionIdStrToAppend = "";
		}

		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4		
		out.print("<FORM ACTION=\"buy_request" + sessionIdStrToAppend
				+ "\" METHOD=\"get\">");
		
		out.print("<BLOCKQUOTE><BLOCKQUOTE>\n");
		out.print("<HR><TABLE BORDER=\"0\"><TR>\n");
		out.print("<TD><INPUT CHECKED=\"CHECKED\" NAME=\"RETURNING_FLAG\" "
				+ "TYPE=\"radio\" VALUE=\"Y\">I am an existing customer");
		out.print("</TD></TR><TR><TD>\n");
		out.print("<INPUT NAME=\"RETURNING_FLAG\" TYPE=\"radio\" VALUE=\"N\">"
				+ "I am a first time customer</TD></TR></TABLE>\n");
		out.print("<HR><P><B>If you're an existing customer, enter your User "
				+ "ID and Password:</B><BR><BR></P>\n");
		out.print("<TABLE><TR ALIGN=\"left\">\n");
		out.print("<TD>User ID: <INPUT NAME=\"UNAME\" SIZE=\"23\"></TD></TR>\n");
		out.print("<TR ALIGN=\"left\">\n");
		out.print("<TD>Password: <INPUT SIZE=\"14\" NAME=\"PASSWD\" "
				+ "TYPE=\"password\"></TD></TR></TABLE> \n");
		out.print("<HR><P><B>If you re a first time customer, enter the "
				+ "details below:</B><BR></P>\n");
		out.print("<TABLE><TR><TD>Enter your birth date (mm/dd/yyyy):</TD>\n");
		out.print("<TD> <INPUT NAME=\"BIRTHDATE\" SIZE=\"10\"></TD></TR>");
		out.print("<TR><TD>Enter your First Name:</TD>\n");
		out.print("<TD> <INPUT NAME=\"FNAME\" SIZE=\"15\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Last Name:</TD>\n");
		out.print("<TD><INPUT NAME=\"LNAME\" SIZE=\"15\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Address 1:</TD>\n");
		out.print("<TD><INPUT NAME=\"STREET1\" SIZE=\"40\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Address 2:</TD>\n");
		out.print("<TD> <INPUT NAME=\"STREET2\" SIZE=\"40\"></TD></TR>\n");

		out.print("<TR><TD>Enter your City, State, Zip:</TD>\n");
		out.print("<TD><INPUT NAME=\"CITY\" SIZE=\"30\">"
				+ "<INPUT NAME=\"STATE\"><INPUT NAME=\"ZIP\" SIZE=\"10\">\n");
		out.print("</TD></TR>");

		out.print("<TR><TD>Enter your Country:</TD>\n");
		out.print("<TD><INPUT NAME=\"COUNTRY\" SIZE=\"50\"></TD></TR>\n");
		out.print("<TR><TD>Enter your Phone:</TD>\n");
		out.print("<TD><INPUT NAME=\"PHONE\" SIZE=\"16\"></TD></TR>\n");
		out.print("<TR><TD>Enter your E-mail:</TD>\n");
		out.print("<TD> <INPUT NAME=\"EMAIL\" SIZE=\"50\"></TD></TR></TABLE>\n");

		out.print("<HR><TABLE><TR><TD COLSPAN=\"2\">Special Instructions:");
		out.print("<TEXTAREA COLS=\"65\" NAME=\"DATA\" ROWS=\"4\">"
				+ "</TEXTAREA></TD></TR></TABLE></BLOCKQUOTE></BLOCKQUOTE>" + "<CENTER>\n");
		out.print("<INPUT TYPE=\"IMAGE\" NAME=\"Enter Order\" " + "SRC=\"Images/submit_B.gif\">\n");
		if (SHOPPING_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"SHOPPING_ID\" value = \"" + SHOPPING_ID + "\">\n");
		if (C_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"C_ID\" value = \"" + C_ID + "\">\n");
		url = "search_request";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/search_B.gif\" ALT=\"Search Item\"></A>");

		url = "home";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/home_B.gif\" ALT=\"Home\"></A>");
		out.print("</CENTER></FORM>");
		out.print("</BODY></HTML>");
		out.close();
		return;
	}
}
