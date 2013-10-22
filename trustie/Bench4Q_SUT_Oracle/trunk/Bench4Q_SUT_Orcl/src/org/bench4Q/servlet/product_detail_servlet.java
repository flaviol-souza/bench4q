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

public class product_detail_servlet extends HttpServlet {

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
		
		String I_IDstr = req.getParameter("I_ID");
		int I_ID = Integer.parseInt(I_IDstr);
		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		PrintWriter out = res.getWriter();
		res.setContentType("text/html");

		Book mybook = Database.getBook(I_ID);

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML><HEAD> <TITLE>Product Detail Page</TITLE>\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");

		out.print("</CENTER> <H2 ALIGN=\"center\">Product Detail Page</H2>\n");

		out.print("<H2> Title: " + mybook.i_title + "</H2>\n");
		out.print("<P>Author: " + mybook.a_fname + " " + mybook.a_lname + "<BR>\n");
		out.print("Subject: " + mybook.i_subject + "\n");

		out.print("Decription: <I>" + mybook.i_desc + "</I></P>\n");
		out.print("<BLOCKQUOTE><P><B>Suggested Retail: " + mybook.i_srp + "</B>\n");
		out.print("<BR><B>Our Price:</B>\n");
		out.print("<FONT COLOR=\"#dd0000\"> <B> " + mybook.i_cost + "</B></FONT><BR>\n");
		out.print("<B>You Save:</B><FONT COLOR=\"#dd0000\"> $" + (mybook.i_srp - mybook.i_cost)
				+ "</B></FONT></P>\n");
		out.print("</BLOCKQUOTE><DL><DT><FONT SIZE=\"2\">\n");
		out.print("Backing: " + mybook.i_backing + ", " + mybook.i_page + " pages<BR>\n");
		out.print("Published by " + mybook.i_publisher + "<BR>\n");
		out.print("Publication date: " + mybook.i_pub_Date + "<BR>\n");
		out.print("Avail date: " + mybook.i_avail + "<BR>\n");
		out.print("Dimensions (in inches): " + mybook.i_dimensions + "<BR>\n");
		out.print("ISBN: " + mybook.i_isbn + "</FONT></DT></DL><P>\n");

		url = "shopping_cart?I_ID=" + I_ID + "&QTY=1";
		if (SHOPPING_ID != null)
			url = url + "&SHOPPING_ID=" + SHOPPING_ID;
		if (C_ID != null)
			url = url + "&C_ID=" + C_ID;
		url = url + "&ADD_FLAG=Y";
		out.print("<CENTER> <A HREF=\"" + res.encodeUrl(url));
		out.print("\">\n");

		out.print("<IMG SRC=\"Images/add_B.gif\"" + " ALT=\"Add to Basket\"></A>\n");
		url = "search_request";

		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));

		out.print("\"><IMG SRC=\"Images/search_B.gif\"" + " ALT=\"Search\"></A>\n");
		url = "home";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/home_B.gif\" " + "ALT=\"Home\"></A>\n");

		url = "admin_request?I_ID=" + I_ID;
		if (SHOPPING_ID != null)
			url = url + "&SHOPPING_ID=" + SHOPPING_ID;
		if (C_ID != null)
			url = url + "&C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/update_B.gif\"" + " ALT=\"Update\"></A>\n");

		out.print("</BODY> </HTML>\n");
		out.close();
		return;
	}
}
