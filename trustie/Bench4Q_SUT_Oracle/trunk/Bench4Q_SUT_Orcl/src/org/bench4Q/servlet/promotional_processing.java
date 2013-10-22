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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class promotional_processing {

	public static void DisplayPromotions(PrintWriter out, HttpServletRequest req,
			HttpServletResponse res, int new_sid) {
		int I_ID = Util.getRandomI_ID();
		Vector related_item_ids = new Vector();
		Vector thumbnails = new Vector();
		int i;
		String url;

		Database.getRelated(I_ID, related_item_ids, thumbnails);

		String C_ID = req.getParameter("C_ID");
		String SHOPPING_ID = req.getParameter("SHOPPING_ID");

		// Create table and "Click on our latest books..." row
		out.print("<TABLE ALIGN=CENTER BORDER=0 WIDTH=660>\n");
		out.print("<TR ALIGN=CENTER VALIGN=top>\n");
		out.print("<TD COLSPAN=5><B><FONT COLOR=#ff0000 SIZE=+1>"
				+ "Click on one of our latest books to find out more!" + "</FONT></B></TD></TR>\n");
		out.print("<TR ALIGN=CENTER VALIGN=top>\n");

		// Create links and references to book images
		for (i = 0; i < related_item_ids.size(); i++) {
			url = "product_detail";
			url = url + "?I_ID=" + String.valueOf(related_item_ids.elementAt(i));
			if (SHOPPING_ID != null)
				url = url + "&SHOPPING_ID=" + SHOPPING_ID;
			else if (new_sid != -1)
				url = url + "&SHOPPING_ID=" + new_sid;
			if (C_ID != null)
				url = url + "&C_ID=" + C_ID;
			out.print("<TD><A HREF=\"" + res.encodeUrl(url));
			out.print("\"><IMG SRC=\"Images/" + thumbnails.elementAt(i) + "\" ALT=\"Book "
					+ String.valueOf(i + 1) + "\" WIDTH=\"100\" HEIGHT=\"150\"></A>\n");
			out.print("</TD>");
		}
		out.print("</TR></TABLE>\n");
	}

}
