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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class buy_request_servlet extends HttpServlet {

	/**
	 * 2009-3-6 author: duanzhiquan Technology Center for Software Engineering
	 * Institute of Software, Chinese Academy of Sciences Beijing 100190, China
	 * Email:duanzhiquan07@otcaix.iscas.ac.cn
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

		String SHOPPING_ID = req.getParameter("SHOPPING_ID");
		String RETURNING_FLAG = req.getParameter("RETURNING_FLAG");

		Customer cust = null;

		out.print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD W3 HTML//EN\">\n");
		out.print("<HTML><HEAD><TITLE>Buy Request</TITLE></HEAD>\n");
		out.print("<BODY BGCOLOR=\"ffffff\">\n");
		out.print("<H1 ALIGN=\"center\">Bench4Q</H1>\n");
		out
				.print("<H1 ALIGN=\"center\">A QoS oriented B2C benchmark for Internetware Middleware</H1>\n");
		out.print("<H2 ALIGN=\"CENTER\">Buy Request Page</H2>\n");

		if (RETURNING_FLAG == null) {
			out.print("ERROR: RETURNING_FLAG not set!</BODY><HTML>");
			return;
		}
		if (RETURNING_FLAG.equals("Y")) {
			String UNAME = req.getParameter("UNAME");
			String PASSWD = req.getParameter("PASSWD");
			if (UNAME.length() == 0 || PASSWD.length() == 0) {
				out.print("Error: Invalid Input</BODY></HTML>");
				return;
			}

			cust = Database.getCustomer(UNAME);
			Database.refreshSession(cust.c_id);
			if (!PASSWD.equals(cust.c_passwd)) {
				out.print("Error: Incorrect Password</BODY></HTML>");
				return;
			}
		} else if (RETURNING_FLAG.equals("N")) {
			cust = new Customer();
			cust.c_fname = req.getParameter("FNAME");
			cust.c_lname = req.getParameter("LNAME");
			cust.addr_street1 = req.getParameter("STREET1");
			cust.addr_street2 = req.getParameter("STREET2");
			cust.addr_city = req.getParameter("CITY");
			cust.addr_state = req.getParameter("STATE");
			cust.addr_zip = req.getParameter("ZIP");
			cust.co_name = req.getParameter("COUNTRY");
			cust.c_phone = req.getParameter("PHONE");
			cust.c_email = req.getParameter("EMAIL");
			cust.c_birthdate = new Date(req.getParameter("BIRTHDATE"));
			cust.c_data = req.getParameter("DATA");
			cust = Database.createNewCustomer(cust);
		} else
			out.print("ERROR: RETURNING_FLAG not set to Y or N!\n");

		if (SHOPPING_ID == null) {
			out.print("ERROR: Shopping Cart ID not set!</BODY></HTML>");
			return;
		}
		// Update the shopping cart cost and get the current contents
		Cart mycart = Database.getCart(Integer.parseInt(SHOPPING_ID), cust.c_discount);

		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4
		String sessionIdStrToAppend = req.getRequestedSessionId();
		if (sessionIdStrToAppend != null) {
			sessionIdStrToAppend = ";jsessionid=" + sessionIdStrToAppend;
		} else {
			sessionIdStrToAppend = "";
		}		
		
		// Print out the web page
		
		// by xiaowei zhou, change "$sessionid$" to "jsessionid=", 2010.11.4
		out.print("<HR><FORM ACTION=\"buy_confirm" + sessionIdStrToAppend
				+ "\" METHOD=\"GET\">\n");
		
		out.print("<TABLE BORDER=\"0\" WIDTH=\"90%\">\n");
		out.print("<TR ALIGN=\"LEFT\" VALIGN=\"TOP\">\n");
		out.print("<TD VALIGN=\"TOP\" WIDTH=\"45%\">\n");
		out.print("<H2>Billing Information:</H2>\n");
		out.print("<TABLE WIDTH=\"100%\" BORDER=\"0\"><TR>\n");

		out.print("<TR><TD>Firstname:</TD><TD>" + cust.c_fname + "</TD></TR>\n");
		out.print("<TR><TD>Lastname: </TD><TD>" + cust.c_lname + "</TD></TR>\n");
		out.print("<TR><TD>Addr_street_1:</TD><TD>" + cust.addr_street1 + "</TD></TR>\n");
		out.print("<TR><TD>Addr_street_2:</TD><TD>" + cust.addr_street2 + "</TD></TR>\n");
		out.print("<TR><TD>City:</TD><TD>" + cust.addr_city + "</TD></TR>\n");
		out.print("<TR><TD>State:</TD><TD>" + cust.addr_state + "</TD></TR>\n");
		out.print("<TR><TD>Zip:</TD><TD>" + cust.addr_zip + "</TD></TR>\n");
		out.print("<TR><TD>Country:</TD><TD>" + cust.co_name + "</TD></TR>\n");
		out.print("<TR><TD>Email:</TD><TD>" + cust.c_email + "</TD></TR>\n");
		out.print("<TR><TD>Phone:</TD><TD>" + cust.c_phone + "</TD></TR>\n");
		if (RETURNING_FLAG.equals("N")) {
			out.print("<TR><TD>USERNAME:</TD><TD>" + cust.c_uname + "</TD></TR>\n");
			out.print("<TR><TD>C_ID:</TD><TD>" + cust.c_id + "</TD></TR>\n");
		}
		out.print("</TABLE></TD>");

		//
		// The Shipping Info Form
		//

		out.print("<TD VALIGN=\"TOP\" WIDTH=\"45%\">\n");
		out.print("<H2>Shipping Information:</H2>\n");
		out.print("<TABLE BORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"0\" WIDTH=\"100%\">\n");
		out.print("<TR><TD WIDTH=\"50%\">Addr_street_1:</TD>\n");
		out.print("<TD><INPUT NAME=\"STREET_1\" SIZE=\"40\" VALUE=\"\"></TD></TR>\n");
		out.print("<TR><TD>Addr_street_ 2:</TD>\n");
		out.print("<TD><INPUT NAME=\"STREET_2\" SIZE=\"40\" VALUE=\"\"></TD></TR>\n");
		out.print("<TR><TD>City:</TD><TD><INPUT NAME=\"CITY\" SIZE=\"30\" VALUE=\"\"></TD></TR>\n");
		out
				.print("<TR><TD>State:</TD><TD><INPUT NAME=\"STATE\" SIZE=\"20\" VALUE=\"\"></TD></TR>\n");
		out.print("<TR><TD>Zip:</TD><TD><INPUT NAME=\"ZIP\" SIZE=\"10\" VALUE=\"\"></TD></TR>\n");
		out
				.print("<TR><TD>Country:</TD><TD><INPUT NAME=\"COUNTRY\" VALUE=\"\" SIZE=\"40\"></TD></TR>\n");

		//
		// Order Information Section
		//

		out.print("</TABLE></TD></TR></TABLE>\n");
		out.print("<HR><H2>Order Information:</H2>\n");
		out.print("<TABLE BORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"0\">\n");
		out.print("<TR><TD><B>Qty</B></TD><TD><B>Product</B></TD></TR>\n");

		// Insert Shopping Cart Contents Here!
		//
		int i;
		for (i = 0; i < mycart.lines.size(); i++) {
			CartLine thisline = (CartLine) mycart.lines.elementAt(i);
			out.print("<TR><TD VALIGN=\"TOP\">" + thisline.scl_qty + "</TD>\n");
			out.print("<TD VALIGN=\"TOP\">Title:<I>" + thisline.scl_title + "</I> - Backing: "
					+ thisline.scl_backing);
			out.print("<BR>SRP. $" + thisline.scl_srp);
			out.print("<FONT COLOR=\"#aa0000\">\n");
			out.print("<B>Your Price:" + thisline.scl_cost + "</B>\n");
			out.print("</FONT></TD></TR>");
		}

		out.print("</TABLE>\n");
		out.print("<P><BR></P><TABLE BORDER=\"0\">\n");
		out
				.print("<TR><TD><B>Subtotal with discount (" + cust.c_discount
						+ "%):</B></TD><TD ALIGN=\"RIGHT\"><B>$" + mycart.SC_SUB_TOTAL
						+ "</B></TD></TR>\n");
		out.print("<TR><TD><B>Tax</B></TD><TD ALIGN=\"RIGHT\"><B>$" + mycart.SC_TAX
				+ "</B></TD></TR>\n");
		out.print("<TR><TD><B>Shipping &amp; Handling</B></TD><TD ALIGN=\"RIGHT\"><B>$"
				+ mycart.SC_SHIP_COST + "</B></TD></TR>\n");
		out.print("<TR><TD><B>Total</B></TD><TD ALIGN=\"RIGHT\"><B>$" + mycart.SC_TOTAL
				+ "</B></TD></TR></TABLE>\n");

		//
		// Credit Card Stuff
		//
		out.print("<HR WIDTH=\"700\"><P><BR></P>\n");

		out.print("<TABLE BORDER=\"1\" CELLPADDING=\"5\" " + "CELLSPACING=\"0\"><TR>\n");
		out.print("<TD>Credit Card Type</TD>\n");
		out.print("<TD><INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" VALUE=\"Visa\" "
				+ "CHECKED=\"CHECKED\">Visa\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" " + "VALUE=\"Master\">MasterCard\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" " + "VALUE=\"Discover\">Discover\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" " + "VALUE=\"Amex\">American Express\n");
		out
				.print("<INPUT TYPE=\"RADIO\" NAME=\"CC_TYPE\" "
						+ "VALUE=\"Diners\">Diners</TD></TR>\n");

		out.print("<TR><TD>Name on Credit Card</TD>\n");
		out.print("<TD><INPUT NAME=\"CC_NAME\" SIZE=\"30\" VALUE=\"\"></TD></TR>\n");
		out.print("<TR><TD>Credit Card Number</TD>\n");
		out.print("<TD><INPUT NAME=\"CC_NUMBER\" SIZE=\"16\" VALUE=\"\"></TD></TR>\n");
		out.print("<TR><TD>Credit Card Expiration Date</TD>\n");
		out.print("<TD><INPUT NAME=\"CC_EXPIRY\" SIZE=\"15\" VALUE=\"\"></TD></TR>\n");

		out.print("<TR><TD>Shipping Method</TD>\n");
		out
				.print("<TD><INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"AIR\" CHECKED=\"CHECKED\">AIR");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"UPS\">UPS\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"FEDEX\">FEDEX\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"SHIP\">SHIP\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"COURIER\">COURIER\n");
		out.print("<INPUT TYPE=\"RADIO\" NAME=\"SHIPPING\" VALUE=\"MAIL\">MAIL\n");
		out.print("</TD></TR></TABLE><P><CENTER>\n");
		if (SHOPPING_ID != null)
			out.print("<INPUT TYPE=HIDDEN NAME=\"SHOPPING_ID\" value = \"" + SHOPPING_ID + "\">\n");
		out.print("<INPUT TYPE=HIDDEN NAME=\"C_ID\" value = \"" + cust.c_id + "\">\n");
		out.print("<INPUT TYPE=\"IMAGE\" NAME=\"Confirm Buy\" SRC=\"Images/submit_B.gif\">\n");
		url = "shopping_cart?ADD_FLAG=N&C_ID=" + cust.c_id;
		if (SHOPPING_ID != null)
			url = url + "&SHOPPING_ID=" + SHOPPING_ID;
		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/shopping_cart_B.gif\" " + "ALT=\"Shopping Cart\"></A>\n");

		url = "order_inquiry?C_ID=" + cust.c_id;
		if (SHOPPING_ID != null)
			url = url + "&SHOPPING_ID=" + SHOPPING_ID;

		out.print("<A HREF=\"" + res.encodeUrl(url));
		out.print("\"><IMG SRC=\"Images/order_status_B.gif\" " + "ALT=\"Order Status\"></A>\n");
		out.print("</P></CENTER></BODY></HTML>");
		out.close();
		return;
	}
}
