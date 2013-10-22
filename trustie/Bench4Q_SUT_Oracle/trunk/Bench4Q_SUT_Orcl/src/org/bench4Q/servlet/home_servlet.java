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
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class home_servlet extends HttpServlet {

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
		int i;
		String url;
		Vector column1 = new Vector();
		Vector column2 = new Vector();

		column1.addElement(new String("ARTS"));// 1
		column2.addElement(new String("NON-FICTION"));
		column1.addElement(new String("BIOGRAPHIES"));// 2
		column2.addElement(new String("PARENTING"));
		column1.addElement(new String("BUSINESS"));// 3
		column2.addElement(new String("POLITICS"));
		column1.addElement(new String("CHILDREN"));// 4
		column2.addElement(new String("REFERENCE"));
		column1.addElement(new String("COMPUTERS"));// 5
		column2.addElement(new String("RELIGION"));
		column1.addElement(new String("COOKING"));// 6
		column2.addElement(new String("ROMANCE"));
		column1.addElement(new String("HEALTH"));// 7
		column2.addElement(new String("SELF-HELP"));
		column1.addElement(new String("HISTORY"));// 8
		column2.addElement(new String("SCIENCE-NATURE"));
		column1.addElement(new String("HOME"));// 9
		column2.addElement(new String("SCIENCE-FICTION"));
		column1.addElement(new String("HUMOR"));// 10
		column2.addElement(new String("SPORTS"));
		column1.addElement(new String("LITERATURE"));// 11
		column2.addElement(new String("MYSTERY"));

		/* SERVLET SETUP */
		HttpSession session = req.getSession(false);
		if (session == null)
			session = req.getSession(true);
		
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
		
		// This must be after the getSession() call.
		PrintWriter out = res.getWriter();
		// Set the content type of this servlet's result.
		res.setContentType("text/html");

		// int C_ID = -1;
		// int SHOPPING_ID = -1;
		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		// Generate Home Page Head
		out.print("<HTML> <HEAD> <TITLE>Home Page</TITLE></HEAD>\n");
		out.print("<BODY BGCOLOR=\"#ffffff\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");

		out.print("<P ALIGN=\"CENTER\">\n");
		out.print("<H2 ALIGN=\"center\">Home Page</H2>\n");

		// Say Hello!
		say_hello.print_hello(session, req, out);

		// Insert the promotional processing
		promotional_processing.DisplayPromotions(out, req, res, -1);

		// Generate Table of What's New and BestSellers
		// Table headings
		out.print("<TABLE ALIGN=\"center\" BGCOLOR=\"#c0c0c0\" BORDER=\"0\""
				+ " CELLPADDING=\"6\" CELLSPACING=\"0\" WIDTH=\"700\">\n");
		out.print("<TR ALIGN=\"CENTER\" BGCOLOR=\"#ffffff\" VALIGN=\"top\">\n");
		out.print("<TD COLSPAN=\"2\" VALIGN=\"MIDDLE\" WIDTH=\"300\">\n");
		out.print("<IMG SRC=\"Images/whats_new.gif\"" + " ALT=\"New Product\">\n");
		out.print("</TD>\n");
		out.print("<TD BGCOLOR=\"#ffffff\" WIDTH=\"100\"></TD>\n");
		out.print("<TD COLSPAN=\"2\" WIDTH=\"300\">\n");
		out.print("<IMG SRC=\"Images/best_sellers.gif\"" + " ALT=\"Best Seller\"></TD></TR>\n");

		for (i = 0; i < column1.size(); i++) {
			out.print("<TR><TD><P ALIGN=\"center\">");
			url = "new_products";
			url = url + "?subject=" + column1.elementAt(i);
			if (SHOPPING_ID != null)
				url = url + "&SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
			out.print("<A HREF=\"" + res.encodeUrl(url));

			out.print("\">" + column1.elementAt(i) + "</A></P></TD>\n");
			url = "new_products";
			url = url + "?subject=" + column2.elementAt(i);
			if (SHOPPING_ID != null)
				url = url + "&SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;

			out.print("<TD><P ALIGN=\"center\"><A HREF=\"" + res.encodeUrl(url));

			out.print("\">" + column2.elementAt(i) + "</A></P></TD>\n");
			out.print("<TD BGCOLOR=\"#ffffff\" WIDTH=\"50\"></TD>\n");
			out.print("<TD> <P ALIGN=\"center\">");
			url = "best_sellers";
			url = url + "?subject=" + column1.elementAt(i);
			if (SHOPPING_ID != null)
				url = url + "&SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;

			out.print("<A HREF=\"" + res.encodeUrl(url));

			out.print("\">" + column1.elementAt(i) + "</A></P></TD>\n");

			url = "best_sellers";
			url = url + "?subject=" + column2.elementAt(i);
			if (SHOPPING_ID != null)
				url = url + "&SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;

			out.print("<TD><P ALIGN=\"center\"><A HREF=\"" + res.encodeUrl(url));
			out.print("\">" + column2.elementAt(i) + "</A></P></TD>\n");
			out.print("</TR>\n");
		}
		out.print("</TABLE>\n");

		// Generate shopping cart, search, and order status buttons.
		out.print("<P ALIGN=\"CENTER\">\n");
		url = "shopping_cart";
		url = url + "?ADD_FLAG=N";
		if (SHOPPING_ID != null)
			url = url + "&SHOPPING_ID=" + SHOPPING_ID;
		if (C_ID != null)
			url = url + "&C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));

		out.print("\"><IMG SRC=\"Images/shopping_cart_B.gif\"" + " ALT=\"Shopping Cart\"></A>\n");

		url = "search_request";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/search_B.gif\"" + " ALT=\"Search\"></A>\n");

		url = "order_inquiry";
		if (SHOPPING_ID != null) {
			url = url + "?SHOPPING_ID=" + SHOPPING_ID;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
		} else if (C_ID != null)
			url = url + "?C_ID=" + C_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));

		out.print("\"><IMG SRC=\"Images/order_status_B.gif\"" + " ALT=\"Order Status\"></A>\n");

		out.print("</font> </BODY> </HTML>\n");
		out.close();
		return;
	}

}
