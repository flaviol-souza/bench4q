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

public class admin_response_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,
			ServletException {
		PrintWriter out = res.getWriter();
		String url;

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

		// Pull out the parameters
		int I_ID = Integer.parseInt(req.getParameter("I_ID"));
		String I_NEW_IMAGE = req.getParameter("I_NEW_IMAGE");
		String I_NEW_THUMBNAIL = req.getParameter("I_NEW_THUMBNAIL");
		String I_NEW_COSTstr = req.getParameter("I_NEW_COST");
		Double I_NEW_COSTdbl = Double.valueOf(I_NEW_COSTstr);

		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		// Get this book out of the database
		Book book = Database.getBook(I_ID);

		// Spit out the HTML
		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML> <HEAD><TITLE>Admin Response Page</TITLE></HEAD>\n");
		out.print("<BODY BGCOLOR=\"#FFFFFF\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");

		if (I_NEW_COSTstr.length() == 0 || I_NEW_IMAGE.length() == 0
				|| I_NEW_THUMBNAIL.length() == 0) {
			out.print("<H2>Invalid Input</H2>");
		} else {
			// Update the database
			Database.adminUpdate(I_ID, I_NEW_COSTdbl.doubleValue(), I_NEW_IMAGE, I_NEW_THUMBNAIL);

			out.print("<H2>Product Updated</H2>");
			out.print("<H2>Title: " + book.i_title + "</H2>\n");
			out.print("<P>Author: " + book.a_fname + " " + book.a_lname + "</P>\n");
			out.print("<P><IMG SRC=\"Images/" + I_NEW_IMAGE
					+ "\" ALIGN=\"RIGHT\" BORDER=\"0\" WIDTH=\"200\" " + "HEIGHT=\"200\">");
			out.print("<IMG SRC=\"Images/" + I_NEW_THUMBNAIL
					+ "\" ALT=\"Book 1\" ALIGN=\"RIGHT\" WIDTH=\"100\"" + " HEIGHT=\"150\">\n");
			out.print("Description: " + book.i_desc + "</P>\n");
			out.print("<BLOCKQUOTE><P><B>Suggested Retail: $" + book.i_srp
					+ "</B><BR><B>Our Price: </B><FONT COLOR=\"#DD0000\"><B>" + I_NEW_COSTstr
					+ "</B></FONT><BR><B>You Save: </B><FONT " + "COLOR=\"#DD0000\"><B>"
					+ Double.toString((book.i_srp - (Double.valueOf(I_NEW_COSTstr)).doubleValue()))
					+ "</B></FONT></P></BLOCKQUOTE> ");
			out
					.print("<P><FONT SIZE=\"2\">" + book.i_backing + ", " + book.i_page
							+ " pages<BR>\n");
			out.print("Published by " + book.i_publisher + "<BR>\n");
			out.print("Publication date: " + book.i_pub_Date + "<BR>\n");
			out.print("Dimensions (in inches): " + book.i_dimensions + "<BR>\n");
			out.print("ISBN: " + book.i_isbn + "</FONT><BR CLEAR=\"ALL\"></P>\n");

			out.print("<CENTER>");
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

			out.print("\"><IMG SRC=\"Images/home_B.gif\" " + "ALT=\"Home\"></A></P></CENTER>\n");

			out.print("</FORM>\n");
		}
		out.print("</BODY></HTML>");
		out.close();
		return;
	}
}
