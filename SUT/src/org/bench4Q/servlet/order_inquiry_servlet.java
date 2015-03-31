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

public class order_inquiry_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,
			ServletException {
		HttpSession session = req.getSession(false);

		PrintWriter out = res.getWriter();
		// Set the content type of this servlet's result.
		res.setContentType("text/html");
		String username = "";
		String url;
		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");
		
		String sLoad = req.getParameter("bench4q_add_load");
		String sOpt = req.getParameter("bench4q_add_load_opt");
		
		if(sLoad == null || sOpt == null) {
			sLoad = "0";
			sOpt = "0";
		}
			
		int iLoad = Integer.parseInt(sLoad);
		int iOpt = Integer.parseInt(sOpt);

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML><HEAD><TITLE>Order Inquiry Page</TITLE>\n");
		out.print("</HEAD><BODY BGCOLOR=\"#ffffff\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");
		out.print("<H2 ALIGN=\"center\">Order Inquiry Page</H2>\n");

		out.print("<FORM ACTION=\"order_display;$sessionid$" + req.getRequestedSessionId()
				+ "\" METHOD=\"get\">\n");
		out.print("<TABLE ALIGN=\"CENTER\">\n");
		out.print("<TR> <TD> <H4>Username:</H4></TD>\n");
		out.print("<TD><INPUT NAME=\"UNAME\" VALUE=\"" + username + "\" SIZE=\"23\"></TD></TR>\n");
		out.print("<TR><TD> <H4>Password:</H4></TD>\n");
		out.print("<TD> <INPUT NAME=\"PASSWD\" SIZE=\"14\" " + "TYPE=\"password\"></TD>\n");
		out.print("</TR></TABLE> <P ALIGN=\"CENTER\"><CENTER>\n");

		out.print("<INPUT TYPE=\"IMAGE\" NAME=\"Display Last Order\" "
				+ "SRC=\"Images/display_last_order_B.gif\">\n");
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
		out.print("</CENTER></FORM>");
		
		out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0>");
		out.println("<TR><TD>Load:</TD> <TD> 0 * "+sLoad+" </TD> </TR>");
		out.println("<TR><TD>Option:</TD> <TD> "+sOpt+" </TD> </TR>");
		out.println("</TABLE>");
		
		out.print("</BODY></HTML>");
		out.close();
		return;
	}
}
