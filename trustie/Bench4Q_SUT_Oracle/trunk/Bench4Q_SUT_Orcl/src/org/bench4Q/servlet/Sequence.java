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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sequence {
	public Sequence() {
	}

	public static synchronized String getSequenceNumber(String SequenceName) {
		String result = "0";
		Connection conn = null;
		Statement ps = null;
		ResultSet rs = null;

		try {

			conn = Database.getConnection();
			conn.setAutoCommit(false);
			String sql = "select num from sequence where name='" + SequenceName + "' for update";

			ps = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = ps.executeQuery(sql);
			long num = 0;
			while (rs.next()) {
				num = rs.getLong(1);
				// result=new Long(rs.getLong(1)).toString();
				result = new Long(num).toString();
			}
			num++;
			sql = "update sequence set num=" + num + " where name='" + SequenceName + "'";
			int res = ps.executeUpdate(sql);
			if (res == 1) {
				conn.commit();
			} else
				conn.rollback();

		} catch (Exception e) {
			System.out.println("Error Happens when trying to obtain the senquence number");
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}

		}
		return result;
	}
}
