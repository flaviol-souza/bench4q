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

public class order_display_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException,
			ServletException {

		PrintWriter out = res.getWriter();
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
		
		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");
		String url;

		// Set the content type of this servlet's result.
		res.setContentType("text/html");

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML><HEAD><TITLE>Order Display Page</TITLE></HEAD>\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");
		out.print("<H2 ALIGN=\"CENTER\">Order Display Page</H2>\n");
		out.print("<BLOCKQUOTE> <BLOCKQUOTE> <BLOCKQUOTE> <BLOCKQUOTE> <HR>\n");

		String uname = req.getParameter("UNAME");
		String passwd = req.getParameter("PASSWD");
		if (uname != null && passwd != null) {

			String storedpasswd = Database.GetPassword(uname);
			if (!storedpasswd.equals(passwd)) {
				out.print("Error: Incorrect password.\n");
			} else {
				Vector lines = new Vector();
				Order order = Database.GetMostRecentOrder(uname, lines);
				if (order != null)
					printOrder(order, lines, out);
				else
					out.print("User has no order!\n");
			}

		} else
			out.print("Error:order_display, " + "uname and passwd not set!.\n");

		// Print out the buttons that are on the bottom of the page
		out.print("<CENTER>\n");
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
		out.print("</CENTER></FORM></BODY></HTML>");
	}

	private void printOrder(Order order, Vector lines, PrintWriter out) {
		int i;
		out.print("<P>Order ID:" + order.o_id + "<BR>\n");
		out.print("Order Placed on " + order.o_date + "<BR>\n");
		out.print("Shipping Type:" + order.o_ship_type + "<BR>\n");
		out.print("Ship Date: " + order.o_ship_date + "<BR>\n");
		out.print("Order Subtotal: " + order.o_subtotal + "<BR>\n");
		out.print("Order Tax: " + order.o_tax + "<BR>\n");
		out.print("Order Total:" + order.o_total + "<BR></P>\n");

		out.print("<TABLE BORDER=\"0\" WIDTH=\"80%\">\n");
		out.print("<TR><TD><B>Bill To:</B></TD><TD><B>Ship To:</B></TD></TR>");
		out.print("<TR><TD COLSPAN=\"2\"> <H4>" + order.c_fname + " " + order.c_lname
				+ "</H4></TD></TR>\n");
		out.print("<TR><TD WIDTH=\"50%\"><ADDRESS>" + order.ship_addr_street1 + "<BR>\n");
		out.print(order.ship_addr_street2 + "<BR>\n");
		out.print(order.ship_addr_state + " " + order.ship_addr_zip + "<BR>\n");
		out.print(order.ship_co_name + "<BR><BR>\n");
		out.print("Email: " + order.c_email + "<BR>\n");
		out.print("Phone: " + order.c_phone + "</ADDRESS><BR><P>\n");
		out.print("Credit Card Type: " + order.cx_type + "<BR>\n");
		out.print("Order Status: " + order.o_status + "</P></TD>\n");
		out
				.print("<TD VALIGN=\"TOP\" WIDTH=\"50%\"><ADDRESS>" + order.bill_addr_street1
						+ "<BR>\n");
		out.print(order.bill_addr_street2 + "<BR>\n");
		out.print(order.bill_addr_state + " " + order.bill_addr_zip + "<BR>\n");
		out.print(order.bill_co_name + "\n");
		out.print("</ADDRESS></TD></TR></TABLE>");
		out.print("</BLOCKQUOTE></BLOCKQUOTE></BLOCKQUOTE></ BLOCKQUOTE>");

		// Print out the list of items
		out.print("<CENTER><TABLE BORDER=\"1\" CELLPADDING=\"5\"" + " CELLSPACING=\"0\">\n");
		out.print("<TR><TD><H4>Item #</H4></TD>");
		out.print("<TD><H4>Title</H4></TD>");
		out.print("<TD> <H4>Cost</H4></TD>");
		out.print("<TD> <H4>Qty</H4></TD> ");
		out.print("<TD> <H4>Discount</H4></TD>");
		out.print("<TD> <H4>Comment</H4></TD></TR>\n");
		if (lines != null) {
			for (i = 0; i < lines.size(); i++) {
				OrderLine line = (OrderLine) lines.elementAt(i);
				out.print("<TR>");
				out.print("<TD> <H4>" + line.ol_i_id + "</H4></TD>\n");
				out.print("<TD VALIGN=\"top\"><H4>" + line.i_title + "<BR>Publisher: "
						+ line.i_publisher + "</H4></TD>\n");
				out.print("<TD> <H4>" + line.i_cost + "</H4></TD>\n"); // Cost
				out.print("<TD> <H4>" + line.ol_qty + "</H4></TD>\n"); // Qty
				out.print("<TD> <H4>" + line.ol_discount + "</H4></TD>\n"); // Discount
				out.print("<TD> <H4>" + line.ol_comments + "</H4></TD></TR>\n");
			}
		}
		out.print("</TABLE><BR></CENTER>\n");
		out.close();
		return;
	}
}
