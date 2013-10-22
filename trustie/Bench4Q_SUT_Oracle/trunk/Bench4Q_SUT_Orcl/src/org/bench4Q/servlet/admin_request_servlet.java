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

public class admin_request_servlet extends HttpServlet {

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
		PrintWriter out = res.getWriter();

		// Set the content type of this servlet's result.
		res.setContentType("text/html");

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
		
		String I_IDstr = req.getParameter("I_ID");
		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");
		
		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4
		String sessionIdStrToAppend = req.getRequestedSessionId();
		if (sessionIdStrToAppend != null) {
			sessionIdStrToAppend = ";jsessionid=" + sessionIdStrToAppend;
		} else {
			sessionIdStrToAppend = "";
		}
		

		int I_ID = Integer.parseInt(I_IDstr, 10);

		Book book = Database.getBook(I_ID);

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML><HEAD><TITLE>Product Update Page</TITLE></HEAD>");
		out.print("<BODY BGCOLOR=\"#ffffff\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");

		out.print("<H2 ALIGN=\"center\">Admin Request Page</H2>");

		out.print("<H2 ALIGN=\"center\">Title:" + book.i_title + "</H2>\n");
		out.print("<P ALIGN=\"LEFT\">Author: " + book.a_fname + " " + book.a_lname + "<BR></P>\n");
		out.print("<IMG SRC=\"Images/" + book.i_image + "\" ALIGN=\"RIGHT\" BORDER=\"0\" "
				+ "WIDTH=\"200\" HEIGHT=\"200\" >\n");
		out.print("<IMG SRC=\"Images/" + book.i_thumbnail + "\" ALIGN=\"RIGHT\" BORDER=\"0\">");
		out.print("<P><BR><BR></P>");
		
		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4
		out.print("<FORM ACTION=\"admin_response" + sessionIdStrToAppend
				+ "\" METHOD=\"get\">\n");
		
		out.print("<INPUT NAME=\"I_ID\" TYPE=\"hidden\" VALUE=\"" + I_ID + "\">\n");
		out.print("<TABLE BORDER=\"0\">\n");
		out.print("<TR><TD><B>Suggested Retail:</B></TD><TD><B>$ " + book.i_srp
				+ "</B></TD></TR>\n");
		out.print("<TR><TD><B>Our Current Price: </B></TD>" + "<TD><FONT COLOR=\"#dd0000\"><B>$ "
				+ book.i_cost + "</B></FONT></TD></TR>\n");
		out.print("<TR><TD><B>Enter New Price</B></TD>"
				+ "<TD ALIGN=\"right\">$ <INPUT NAME=\"I_NEW_COST\"></TD></TR>");
		out.print("<TR><TD><B>Enter New Picture</B></TD><TD ALIGN=\"right\">"
				+ "<INPUT NAME=\"I_NEW_IMAGE\"></TD></TR>\n");
		out.print("<TR><TD><B>Enter New Thumbnail</B></TD><TD ALIGN=\"RIGHT\">"
				+ "<INPUT TYPE=\"TEXT\" NAME=\"I_NEW_THUMBNAIL\"></TD></TR>\n");
		out.print("</TABLE>");
		out.print("<P><BR CLEAR=\"ALL\"></P> <P ALIGN=\"center\">");
		if (SHOPPING_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"SHOPPING_ID\" value = \"" + SHOPPING_ID + "\">\n");
		if (C_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"C_ID\" value = \"" + C_ID + "\">\n");
		out.print("<INPUT TYPE=\"IMAGE\" NAME=\"Submit\"" + " SRC=\"Images/submit_B.gif\">\n");
		url = "search_request";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/search_B.gif\" " + "ALT=\"Search\"></A>\n");
		url = "home";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/home_B.gif\" " + "ALT=\"Home\"></A></P>\n");

		out.print("</FORM></BODY></HTML>");
		out.close();
		return;
	}
}
