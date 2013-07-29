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

import java.sql.ResultSet;

public class OrderLine {
	public OrderLine(ResultSet rs) {
		try {
			ol_i_id = rs.getInt("ol_i_id");
			i_title = rs.getString("i_title");
			i_publisher = rs.getString("i_publisher");
			i_cost = rs.getDouble("i_cost");
			ol_qty = rs.getInt("ol_qty");
			ol_discount = rs.getDouble("ol_discount");
			ol_comments = rs.getString("ol_comments");
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}

	public int ol_i_id;
	public String i_title;
	public String i_publisher;
	public double i_cost;
	public int ol_qty;
	public double ol_discount;
	public String ol_comments;
}
