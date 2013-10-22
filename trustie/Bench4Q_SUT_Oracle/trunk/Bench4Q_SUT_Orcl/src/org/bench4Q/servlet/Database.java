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
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
	static Date date = new Date(System.currentTimeMillis());

	public static String dsName;
	//public static String username;
	//public static String password;
	
	//private static final String driverName = "oracle.jdbc.driver.OracleDriver";

	private static InitialContext ctx;

	private static void initial() {
		DBHelper dbhelper = DBHelper.getInstance();
		dsName = dbhelper.getProperty("dsName").trim();
		//username=dbhelper.getProperty("username").trim();
		//password=dbhelper.getProperty("password").trim();
		//System.out.println("Data base name is:"+dsName);
		//System.out.println("user name is:"+username);
		//System.out.println("password is:"+password);
		try {
			ctx = new InitialContext();
		} catch (javax.naming.NamingException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection conn = null;
		initial();
		/*try {
			//Class.forName(driverName);
			//conn= DriverManager.getConnection(dsName,username,password);
			//System.out.println("connect sucessfully");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}*/
		try {
			if (ctx == null)
				initial();
			DataSource ds = null;
			ds = (DataSource) ctx.lookup(dsName);
			conn = ds.getConnection();
			conn.setAutoCommit(false);
		} catch (NamingException ne) {
			ne.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeStmt(PreparedStatement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeStmt(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String[] getName(int c_id) {
		String name[] = new String[2];
		Connection con = null;
		PreparedStatement get_name = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			// out.println("About to call getConnection!");
			// out.flush();
			con = getConnection();
			// out.println("About to preparestatement!");
			// out.flush();
			get_name = con.prepareStatement("SELECT c_fname,c_lname FROM customer WHERE c_id = ?");

			// Set parameter
			get_name.setInt(1, c_id);
			// out.println("About to execute query!");
			// out.flush();

			rs = get_name.executeQuery();

			// Results
			rs.next();
			name[0] = rs.getString("c_fname");
			name[1] = rs.getString("c_lname");
			con.commit();

		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(get_name);
			closeConnection(con);
		}
		return name;
	}

	public static Book getBook(int i_id) {
		Book book = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("SELECT * FROM item,author WHERE item.i_a_id = author.a_id AND i_id = ?");
			// Set parameter
			statement.setInt(1, i_id);
			rs = statement.executeQuery();
			// Results
			rs.next();
			book = new Book(rs);
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return book;
	}

	public static Customer getCustomer(String UNAME) {
		Customer cust = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("SELECT * FROM customer, address, country WHERE customer.c_addr_id = address.addr_id AND address.addr_co_id = country.co_id AND customer.c_uname = ?");

			// Set parameter
			statement.setString(1, UNAME);
			rs = statement.executeQuery();

			// Results
			if (rs.next())
				cust = new Customer(rs);
			else {
				System.err.println("ERROR: NULL returned in getCustomer!");
				return null;
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return cust;
	}

	public static Vector doSubjectSearch(String search_key) {
		Vector vec = new Vector();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("SELECT * FROM item, author WHERE item.i_a_id = author.a_id AND item.i_subject = ? AND rownum < 51 ORDER BY item.i_title");

			// Set parameter
			statement.setString(1, search_key);
			rs = statement.executeQuery();

			// Results
			while (rs.next()) {
				vec.addElement(new Book(rs));
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return vec;
	}

	public static Vector doTitleSearch(String search_key) {
		Vector vec = new Vector();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("SELECT * FROM item, author WHERE item.i_a_id = author.a_id AND item.i_title LIKE ? AND rownum < 51 ORDER BY item.i_title");

			// Set parameter
			statement.setString(1, search_key + "%");
			rs = statement.executeQuery();

			// Results
			while (rs.next()) {
				vec.addElement(new Book(rs));
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return vec;
	}

	public static Vector doAuthorSearch(String search_key) {
		Vector vec = new Vector();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("SELECT * FROM author, item WHERE author.a_lname LIKE ? AND item.i_a_id = author.a_id AND rownum < 51 ORDER BY item.i_title");

			// Set parameter
			statement.setString(1, search_key + "%");
			rs = statement.executeQuery();

			// Results
			while (rs.next()) {
				vec.addElement(new Book(rs));
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return vec;
	}

	public static Vector getNewProducts(String subject) {
		Vector vec = new Vector(); // Vector of Books
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con.prepareStatement("SELECT i_id, i_title, a_fname, a_lname "
					+ "FROM item, author " + "WHERE item.i_a_id = author.a_id "
					+ "AND item.i_subject = ? " 
					+ "AND rownum < 51"
					+ "ORDER BY item.i_pub_date DESC,item.i_title ");

			// Set parameter
			statement.setString(1, subject);
			rs = statement.executeQuery();

			// Results
			while (rs.next()) {
				vec.addElement(new ShortBook(rs));
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return vec;
	}

	public static Vector getBestSellers(String subject) {
		Vector vec = new Vector(); // Vector of Books
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			// The following is the original, unoptimized best sellers query.
			statement = con.prepareStatement("SELECT i_id, i_title, a_fname, a_lname "
					+ "FROM item, author, order_line " + "WHERE item.i_id = order_line.ol_i_id "
					+ "AND item.i_a_id = author.a_id "
					+ "AND order_line.ol_o_id > (SELECT MAX(o_id)-3333 FROM orders)"
					+ "AND item.i_subject = ? "
					+ "AND rownum < 51"
					+ "GROUP BY i_id, i_title, a_fname, a_lname "
					+ "ORDER BY SUM(ol_qty) DESC ");

			// Set parameter
			statement.setString(1, subject);
			rs = statement.executeQuery();

			// Results
			while (rs.next()) {
				vec.addElement(new ShortBook(rs));
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
		return vec;
	}

	public static void getRelated(int i_id, Vector i_id_vec, Vector i_thumbnail_vec) {
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("SELECT J.i_id,J.i_thumbnail from item I, item J where (I.i_related1 = J.i_id or I.i_related2 = J.i_id or I.i_related3 = J.i_id or I.i_related4 = J.i_id or I.i_related5 = J.i_id) and I.i_id = ?");

			// Set parameter
			statement.setInt(1, i_id);
			rs = statement.executeQuery();

			// Clear the vectors
			i_id_vec.removeAllElements();
			i_thumbnail_vec.removeAllElements();

			// Results
			while (rs.next()) {
				i_id_vec.addElement(new Integer(rs.getInt(1)));
				i_thumbnail_vec.addElement(rs.getString(2));
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeConnection(con);
		}
	}

	public static void adminUpdate(int i_id, double cost, String image, String thumbnail) {
		Connection con = null;
		PreparedStatement statement = null;
		PreparedStatement related = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			statement = con
					.prepareStatement("UPDATE item SET i_cost = ?, i_image = ?, i_thumbnail = ?, i_pub_date = CURRENT_DATE WHERE i_id = ?");

			// Set parameter
			statement.setDouble(1, cost);
			statement.setString(2, image);
			statement.setString(3, thumbnail);
			statement.setInt(4, i_id);
			statement.executeUpdate();
			statement.close();
			related = con
					.prepareStatement("SELECT ol_i_id "
							+ "FROM orders, order_line "
							+ "WHERE orders.o_id = order_line.ol_o_id "
							+ "AND NOT (order_line.ol_i_id = ?) "
							+ "AND orders.o_c_id IN (SELECT o_c_id "
							+ "                      FROM orders, order_line "
							+ "                      WHERE orders.o_id = order_line.ol_o_id "
							+ "                      AND orders.o_id > (SELECT MAX(o_id)-10000 FROM orders)"
							+ "                      AND order_line.ol_i_id = ?) "
							+ "                      AND rownum < 6"
							+ "GROUP BY ol_i_id " + "ORDER BY SUM(ol_qty) DESC ");

			// Set parameter
			related.setInt(1, i_id);
			related.setInt(2, i_id);
			rs = related.executeQuery();

			int[] related_items = new int[5];
			// Results
			int counter = 0;
			int last = 0;
			while (rs.next()) {
				last = rs.getInt(1);
				related_items[counter] = last;
				counter++;
			}

			// This is the case for the situation where there are not 5 related
			// books.
			for (int i = counter; i < 5; i++) {
				last++;
				related_items[i] = last;
			}

			{
				// Prepare SQL
				statement = con
						.prepareStatement("UPDATE item SET i_related1 = ?, i_related2 = ?, i_related3 = ?, i_related4 = ?, i_related5 = ? WHERE i_id = ?");

				// Set parameter
				statement.setInt(1, related_items[0]);
				statement.setInt(2, related_items[1]);
				statement.setInt(3, related_items[2]);
				statement.setInt(4, related_items[3]);
				statement.setInt(5, related_items[4]);
				statement.setInt(6, i_id);
				statement.executeUpdate();
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
			closeStmt(related);
			closeConnection(con);
		}
	}

	public static String GetUserName(int C_ID) {
		String u_name = null;
		Connection con = null;
		PreparedStatement get_user_name = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			con = getConnection();
			get_user_name = con.prepareStatement("SELECT c_uname FROM customer WHERE c_id = ?");

			// Set parameter
			get_user_name.setInt(1, C_ID);
			rs = get_user_name.executeQuery();

			// Results
			rs.next();
			u_name = rs.getString("c_uname");
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(get_user_name);
			closeConnection(con);
		}
		return u_name;
	}

	public static String GetPassword(String C_UNAME) {
		String passwd = null;
		Connection con = null;
		PreparedStatement get_passwd = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			get_passwd = con.prepareStatement("SELECT c_passwd FROM customer WHERE c_uname = ?");

			// Set parameter
			get_passwd.setString(1, C_UNAME);
			rs = get_passwd.executeQuery();

			// Results
			rs.next();
			passwd = rs.getString("c_passwd");
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(get_passwd);
			closeConnection(con);
		}
		return passwd;
	}

	// This function gets the value of I_RELATED1 for the row of
	// the item table corresponding to I_ID
	private static int getRelated1(int I_ID, Connection con) {
		int related1 = -1;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = con.prepareStatement("SELECT i_related1 FROM item where i_id = ?");
			statement.setInt(1, I_ID);
			rs = statement.executeQuery();
			if (rs.next()) {
				related1 = rs.getInt(1);// Is 1 the correct index?
			} else
				related1 = 10;
		} catch (java.lang.Exception ex) {
			System.out.println("I_ID is " + I_ID);
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
		}
		return related1;
	}

	public static Order GetMostRecentOrder(String c_uname, Vector order_lines) {
		Connection con = null;
		PreparedStatement get_most_recent_order_id = null;
		PreparedStatement get_order = null;
		PreparedStatement get_order_lines = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		try {
			order_lines.removeAllElements();
			int order_id;
			Order order;

			// Prepare SQL
			con = getConnection();

			// *** Get the o_id of the most recent order for this user
			get_most_recent_order_id = con.prepareStatement("SELECT o_id "
					+ "FROM customer, orders " + "WHERE customer.c_id = orders.o_c_id "
					+ "AND c_uname = ? "
					+ "AND rownum < 2"
					+ "ORDER BY o_date, orders.o_id DESC ");

			// Set parameter
			get_most_recent_order_id.setString(1, c_uname);
			rs = get_most_recent_order_id.executeQuery();

			if (rs.next()) {
				order_id = rs.getInt("o_id");
			} else {
				con.commit();
				return null;
			}

			// *** Get the order info for this o_id
			get_order = con.prepareStatement("SELECT orders.*, customer.*, "
					+ "  cc_xacts.cx_type, " + "  ship.addr_street1 AS ship_addr_street1, "
					+ "  ship.addr_street2 AS ship_addr_street2, "
					+ "  ship.addr_state AS ship_addr_state, "
					+ "  ship.addr_zip AS ship_addr_zip, " + "  ship_co.co_name AS ship_co_name, "
					+ "  bill.addr_street1 AS bill_addr_street1, "
					+ "  bill.addr_street2 AS bill_addr_street2, "
					+ "  bill.addr_state AS bill_addr_state, "
					+ "  bill.addr_zip AS bill_addr_zip, " + "  bill_co.co_name AS bill_co_name "
					+ "FROM customer, orders, cc_xacts," + "  address  ship, "
					+ "  country  ship_co, " + "  address  bill,  " + "  country  bill_co "
					+ "WHERE orders.o_id = ? " + "  AND cc_xacts.cx_o_id = orders.o_id "
					+ "  AND customer.c_id = orders.o_c_id "
					+ "  AND orders.o_bill_addr_id = bill.addr_id "
					+ "  AND bill.addr_co_id = bill_co.co_id "
					+ "  AND orders.o_ship_addr_id = ship.addr_id "
					+ "  AND ship.addr_co_id = ship_co.co_id "
					+ "  AND orders.o_c_id = customer.c_id");

			// Set parameter
			get_order.setInt(1, order_id);
			rs2 = get_order.executeQuery();

			// Results
			if (!rs2.next()) {
				// FIXME - This case is due to an error due to a database
				// population error
				con.commit();
				return null;
			}
			order = new Order(rs2);

			// *** Get the order_lines for this o_id
			get_order_lines = con.prepareStatement("SELECT * " + "FROM order_line, item "
					+ "WHERE ol_o_id = ? " + "AND ol_i_id = i_id");

			// Set parameter
			get_order_lines.setInt(1, order_id);
			rs3 = get_order_lines.executeQuery();

			// Results
			while (rs3.next()) {
				order_lines.addElement(new OrderLine(rs3));
			}

			con.commit();
			return order;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeResultSet(rs2);
			closeResultSet(rs3);
			closeStmt(get_most_recent_order_id);
			closeStmt(get_order);
			closeStmt(get_order_lines);
			closeConnection(con);
		}
		return null;
	}

	// ********************** Shopping Cart code below *************************

	// Called from: TPCW_shopping_cart_interaction
	public static int createEmptyCart() {
		Connection con = null;
		Statement insert_cart = null;
		PreparedStatement get_id = null;
		ResultSet rs = null;
		int SHOPPING_ID = 0;
		try {
			con = getConnection();
			insert_cart = null;
			rs = null;
			insert_cart = con.createStatement();
			insert_cart.executeUpdate(
					"INSERT INTO shopping_cart (sc_id, sc_time) VALUES (SHOPPING_CART_SEQ.nextval, CURRENT_TIMESTAMP )",
					Statement.RETURN_GENERATED_KEYS);
			get_id=con.prepareStatement("SELECT SHOPPING_CART_SEQ.currval FROM SHOPPING_CART");
			rs=get_id.executeQuery();
//			rs = insert_cart.getGeneratedKeys();
			if (rs.next()) {
				SHOPPING_ID = rs.getInt(1);
			}	
			// System.out.println(SHOPPING_ID);
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(insert_cart);
			closeConnection(con);
		}
		return SHOPPING_ID;
	}

	public static Cart doCart(int SHOPPING_ID, Integer I_ID, Vector ids, Vector quantities) {
		Cart cart = null;
		Connection con = null;

		try {
			con = getConnection();

			if (I_ID != null) {
				addItem(con, SHOPPING_ID, I_ID.intValue());
			}
			refreshCart(con, SHOPPING_ID, ids, quantities);
			addRandomItemToCartIfNecessary(con, SHOPPING_ID);
			resetCartTime(con, SHOPPING_ID);
			cart = Database.getCart(con, SHOPPING_ID, 0.0);

			// Close connection
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con);
		}
		return cart;
	}

	// This function finds the shopping cart item associated with SHOPPING_ID
	// and I_ID. If the item does not already exist, we create one with QTY=1,
	// otherwise we increment the quantity.

	private static void addItem(Connection con, int SHOPPING_ID, int I_ID) {
		PreparedStatement find_entry = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			find_entry = con
					.prepareStatement("SELECT scl_qty FROM shopping_cart_line WHERE scl_sc_id = ? AND scl_i_id = ?");

			// Set parameter
			find_entry.setInt(1, SHOPPING_ID);
			find_entry.setInt(2, I_ID);
			rs = find_entry.executeQuery();

			// Results
			if (rs.next()) {
				// The shopping cart id, item pair were already in the table
				int currqty = rs.getInt("scl_qty");
				currqty += 1;
				PreparedStatement update_qty = con
						.prepareStatement("UPDATE shopping_cart_line SET scl_qty = ? WHERE scl_sc_id = ? AND scl_i_id = ?");
				update_qty.setInt(1, currqty);
				update_qty.setInt(2, SHOPPING_ID);
				update_qty.setInt(3, I_ID);
				update_qty.executeUpdate();
				update_qty.close();
			} else {// We need to add a new row to the table.

				// Stick the item info in a new shopping_cart_line
				PreparedStatement put_line = con
						.prepareStatement("INSERT into shopping_cart_line (scl_sc_id, scl_qty, scl_i_id) VALUES (?,?,?)");
				put_line.setInt(1, SHOPPING_ID);
				put_line.setInt(2, 1);
				put_line.setInt(3, I_ID);
				put_line.executeUpdate();
				put_line.close();
			}

		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(find_entry);
		}
	}

	private static void refreshCart(Connection con, int SHOPPING_ID, Vector ids, Vector quantities) {
		PreparedStatement statement = null;
		int i;
		try {
			for (i = 0; i < ids.size(); i++) {
				String I_IDstr = (String) ids.elementAt(i);
				String QTYstr = (String) quantities.elementAt(i);
				int I_ID = Integer.parseInt(I_IDstr);
				int QTY = Integer.parseInt(QTYstr);

				if (QTY == 0) { // We need to remove the item from the cart
					statement = con
							.prepareStatement("DELETE FROM shopping_cart_line WHERE scl_sc_id = ? AND scl_i_id = ?");
					statement.setInt(1, SHOPPING_ID);
					statement.setInt(2, I_ID);
					statement.executeUpdate();
					con.commit();
				} else { // we update the quantity
					statement = con
							.prepareStatement("UPDATE shopping_cart_line SET scl_qty = ? WHERE scl_sc_id = ? AND scl_i_id = ?");
					statement.setInt(1, QTY);
					statement.setInt(2, SHOPPING_ID);
					statement.setInt(3, I_ID);
					statement.executeUpdate();
					con.commit();
				}
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(statement);
		}
	}

	private static void addRandomItemToCartIfNecessary(Connection con, int SHOPPING_ID) {
		// check and see if the cart is empty. If it's not, we do
		// nothing.
		int related_item = 0;
		PreparedStatement get_cart = null;
		ResultSet rs = null;

		try {
			// Check to see if the cart is empty
			get_cart = con
					.prepareStatement("SELECT COUNT(*) from shopping_cart_line where scl_sc_id = ?");
			get_cart.setInt(1, SHOPPING_ID);
			rs = get_cart.executeQuery();
			rs.next();
			if (rs.getInt(1) == 0) {
				// Cart is empty
				int rand_id = Util.getRandomI_ID();
				related_item = getRelated1(rand_id, con);
				addItem(con, SHOPPING_ID, related_item);
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			System.out.println("Adding entry to shopping cart failed: shopping id = " + SHOPPING_ID
					+ " related_item = " + related_item);
		} finally {
			closeResultSet(rs);
			closeStmt(get_cart);
		}
	}

	// Only called from this class
	private static void resetCartTime(Connection con, int SHOPPING_ID) {
		PreparedStatement statement = null;
		try {
			statement = con
					.prepareStatement("UPDATE shopping_cart SET sc_time = current_timestamp WHERE sc_id = ?");
			// Set parameter
			statement.setInt(1, SHOPPING_ID);
			statement.executeUpdate();
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(statement);
		}
	}

	public static Cart getCart(int SHOPPING_ID, double c_discount) {
		Cart mycart = null;
		Connection con = null;
		try {
			con = getConnection();
			mycart = getCart(con, SHOPPING_ID, c_discount);
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con);
		}
		return mycart;
	}

	// time .05s
	private static Cart getCart(Connection con, int SHOPPING_ID, double c_discount) {
		Cart mycart = null;
		PreparedStatement get_cart = null;
		ResultSet rs = null;
		try {
			get_cart = con.prepareStatement("SELECT * " + "FROM shopping_cart_line, item "
					+ "WHERE scl_i_id = item.i_id AND scl_sc_id = ?");
			get_cart.setInt(1, SHOPPING_ID);
			rs = get_cart.executeQuery();
			mycart = new Cart(rs, c_discount);
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(get_cart);
		}
		return mycart;
	}

	// ************** Customer / Order code below *************************

	// This should probably return an error code if the customer
	// doesn't exist, but ...
	public static void refreshSession(int C_ID) {
		Connection con = null;
		PreparedStatement updateLogin = null;
		try {
			// Prepare SQL
			con = getConnection();
			updateLogin = con
					.prepareStatement("UPDATE customer SET c_login = CURRENT_TIMESTAMP, c_expiration = CURRENT_TIMESTAMP + interval '2' hour WHERE c_id = ?");

			// Set parameter
			updateLogin.setInt(1, C_ID);
			updateLogin.executeUpdate();
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(updateLogin);
			closeConnection(con);
		}
	}

	public static Customer createNewCustomer(Customer cust) {
		Connection con = null;
		PreparedStatement get_id = null;
		PreparedStatement insert_customer_row = null;
		ResultSet rs = null;
		try {
			// Get largest customer ID already in use.
			con = getConnection();

			cust.c_discount = (int) (java.lang.Math.random() * 51);
			cust.c_balance = 0.0;
			cust.c_ytd_pmt = 0.0;
			// FIXME - Use SQL CURRENT_TIME to do this
			cust.c_last_visit = new Date(System.currentTimeMillis());
			cust.c_since = new Date(System.currentTimeMillis());
			cust.c_login = new Date(System.currentTimeMillis());
			cust.c_expiration = new Date(System.currentTimeMillis() + 7200000);// milliseconds
			// in 2
			// hours
			insert_customer_row = con
					.prepareStatement(
							"INSERT into customer (c_id, c_uname, c_passwd, c_fname, c_lname, c_addr_id, c_phone, c_email, c_since, c_last_login, c_login, c_expiration, c_discount, c_balance, c_ytd_pmt, c_birthdate, c_data) VALUES ( CUSTOMER_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
			insert_customer_row.setString(3, cust.c_fname);
			insert_customer_row.setString(4, cust.c_lname);
			insert_customer_row.setString(6, cust.c_phone);
			insert_customer_row.setString(7, cust.c_email);
			insert_customer_row.setDate(8, new java.sql.Date(cust.c_since.getTime()));
			insert_customer_row.setDate(9, new java.sql.Date(cust.c_last_visit.getTime()));
			insert_customer_row.setDate(10, new java.sql.Date(cust.c_login.getTime()));
			insert_customer_row.setDate(11, new java.sql.Date(cust.c_expiration.getTime()));
			insert_customer_row.setDouble(12, cust.c_discount);
			insert_customer_row.setDouble(13, cust.c_balance);
			insert_customer_row.setDouble(14, cust.c_ytd_pmt);
			insert_customer_row.setDate(15, new java.sql.Date(cust.c_birthdate.getTime()));
			insert_customer_row.setString(16, cust.c_data);

			cust.addr_id = enterAddress(con, cust.addr_street1, cust.addr_street2, cust.addr_city,
					cust.addr_state, cust.addr_zip, cust.co_name);

			insert_customer_row.setString(1, cust.c_uname);
			insert_customer_row.setString(2, cust.c_passwd);
			insert_customer_row.setInt(5, cust.addr_id);
			insert_customer_row.executeUpdate();
			get_id=con.prepareStatement("SELECT CUSTOMER_SEQ.currval FROM customer");
			rs=get_id.executeQuery();
			//rs = insert_customer_row.getGeneratedKeys();
			if (rs.next()) {
				cust.c_id = rs.getInt(1);
			}
			cust.c_uname = Util.DigSyl(cust.c_id, 0);
			cust.c_passwd = cust.c_uname.toLowerCase();
			PreparedStatement updateUnameANDPasswd = con
					.prepareStatement("UPDATE customer SET c_uname = ?, c_passwd = ? WHERE c_id = ?");
			updateUnameANDPasswd.setString(1, cust.c_uname);
			updateUnameANDPasswd.setString(2, cust.c_passwd);
			updateUnameANDPasswd.setLong(3, cust.c_id);
			updateUnameANDPasswd.executeUpdate();
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(insert_customer_row);
			closeConnection(con);
		}
		return cust;
	}

	// BUY CONFIRM

	public static BuyConfirmResult doBuyConfirm(int shopping_id, int customer_id, String cc_type,
			long cc_number, String cc_name, Date cc_expiry, String shipping) {

		BuyConfirmResult result = new BuyConfirmResult();
		Connection con = null;

		try {
			con = getConnection();
			double c_discount = getCDiscount(con, customer_id);
			result.cart = getCart(con, shopping_id, c_discount);
			int ship_addr_id = getCAddr(con, customer_id);
			result.order_id = enterOrder(con, customer_id, result.cart, ship_addr_id, shipping,
					c_discount);
			enterCCXact(con, result.order_id, cc_type, cc_number, cc_name, cc_expiry,
					result.cart.SC_TOTAL, ship_addr_id);
			clearCart(con, shopping_id);
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con);
		}
		return result;
	}

	public static BuyConfirmResult doBuyConfirm(int shopping_id, int customer_id, String cc_type,
			long cc_number, String cc_name, Date cc_expiry, String shipping, String street_1,
			String street_2, String city, String state, String zip, String country) {

		BuyConfirmResult result = new BuyConfirmResult();
		Connection con = null;
		try {
			con = getConnection();
			double c_discount = getCDiscount(con, customer_id);
			result.cart = getCart(con, shopping_id, c_discount);
			int ship_addr_id = enterAddress(con, street_1, street_2, city, state, zip, country);
			result.order_id = enterOrder(con, customer_id, result.cart, ship_addr_id, shipping,
					c_discount);
			enterCCXact(con, result.order_id, cc_type, cc_number, cc_name, cc_expiry,
					result.cart.SC_TOTAL, ship_addr_id);
			clearCart(con, shopping_id);
			con.commit();

		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeConnection(con);
		}
		return result;
	}

	// DB query time: .05s
	public static double getCDiscount(Connection con, int c_id) {
		double c_discount = 0.0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			statement = con
					.prepareStatement("SELECT c_discount FROM customer WHERE customer.c_id = ?");

			// Set parameter
			statement.setInt(1, c_id);
			rs = statement.executeQuery();

			// Results
			rs.next();
			c_discount = rs.getDouble(1);

		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
		}
		return c_discount;
	}

	// DB time: .05s
	public static int getCAddrID(Connection con, int c_id) {
		int c_addr_id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			statement = con
					.prepareStatement("SELECT c_addr_id FROM customer WHERE customer.c_id = ?");

			// Set parameter
			statement.setInt(1, c_id);
			rs = statement.executeQuery();

			// Results
			rs.next();
			c_addr_id = rs.getInt(1);
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
		}
		return c_addr_id;
	}

	public static int getCAddr(Connection con, int c_id) {
		int c_addr_id = 0;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			// Prepare SQL
			statement = con
					.prepareStatement("SELECT c_addr_id FROM customer WHERE customer.c_id = ?");

			// Set parameter
			statement.setInt(1, c_id);
			rs = statement.executeQuery();

			// Results
			rs.next();
			c_addr_id = rs.getInt(1);
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(statement);
		}
		return c_addr_id;
	}

	public static void enterCCXact(Connection con, int o_id, // Order id
			String cc_type, long cc_number, String cc_name, Date cc_expiry, double total, // Total
			// from
			// shopping
			// cart
			int ship_addr_id) {
		PreparedStatement statement = null;

		// Updates the CC_XACTS table
		if (cc_type.length() > 10)
			cc_type = cc_type.substring(0, 10);
		if (cc_name.length() > 30)
			cc_name = cc_name.substring(0, 30);

		try {
			// Prepare SQL
			statement = con
					.prepareStatement("INSERT into cc_xacts (cx_o_id, cx_type, cx_num, cx_name, cx_expire, cx_xact_amt, cx_xact_date, cx_co_id) "
							+ "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE, (SELECT co_id FROM address, country WHERE addr_id = ? AND addr_co_id = co_id))");

			// Set parameter
			statement.setInt(1, o_id); // cx_o_id
			statement.setString(2, cc_type); // cx_type
			statement.setLong(3, cc_number); // cx_num
			statement.setString(4, cc_name); // cx_name
			statement.setDate(5, cc_expiry); // cx_expiry
			statement.setDouble(6, total); // cx_xact_amount
			statement.setInt(7, ship_addr_id); // ship_addr_id
			statement.executeUpdate();
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(statement);
		}
	}

	public static void clearCart(Connection con, int shopping_id) {
		// Empties all the lines from the shopping_cart_line for the
		// shopping id. Does not remove the actually shopping cart
		PreparedStatement statement = null;
		try {
			// Prepare SQL
			statement = con.prepareStatement("DELETE FROM shopping_cart_line WHERE scl_sc_id = ?");

			// Set parameter
			statement.setInt(1, shopping_id);
			statement.executeUpdate();
			// by dch
			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(statement);
		}
	}

	public static int enterAddress(Connection con, // Do we need to do this as
			// part of a transaction?
			String street1, String street2, String city, String state, String zip, String country) {
		// returns the address id of the specified address. Adds a
		// new address to the table if needed
		int addr_id = 0;
		PreparedStatement get_co_id = null;
		PreparedStatement match_address = null;
		PreparedStatement insert_address_row = null;
		ResultSet rs = null;
		ResultSet rs2 = null;

		// Get the country ID from the country table matching this address.

		// Is it safe to assume that the country that we are looking
		// for will be there?
		try {
			get_co_id = con.prepareStatement("SELECT co_id FROM country WHERE co_name = ?");
			get_co_id.setString(1, country);
			rs = get_co_id.executeQuery();
			rs.next();
			int addr_co_id = rs.getInt("co_id");
			rs.close();

			// Get address id for this customer, possible insert row in
			// address table
			match_address = con.prepareStatement("SELECT addr_id FROM address "
					+ "WHERE addr_street1 = ? " + "AND addr_street2 = ? " + "AND addr_city = ? "
					+ "AND addr_state = ? " + "AND addr_zip = ? " + "AND addr_co_id = ?");
			match_address.setString(1, street1);
			match_address.setString(2, street2);
			match_address.setString(3, city);
			match_address.setString(4, state);
			match_address.setString(5, zip);
			match_address.setInt(6, addr_co_id);
			rs = match_address.executeQuery();
			if (!rs.next()) {// We didn't match an address in the addr table
				PreparedStatement get_id = null;
				insert_address_row = con.prepareStatement(
						"INSERT into address (addr_id, addr_street1, addr_street2, addr_city, addr_state, addr_zip, addr_co_id) "
								+ "VALUES (ADDRESS_SEQ.nextval, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				insert_address_row.setString(1, street1);
				insert_address_row.setString(2, street2);
				insert_address_row.setString(3, city);
				insert_address_row.setString(4, state);
				insert_address_row.setString(5, zip);
				insert_address_row.setInt(6, addr_co_id);
				insert_address_row.executeUpdate();
				get_id=con.prepareStatement("SELECT ADDRESS_SEQ.currval FROM address");
				rs2=get_id.executeQuery();
				//rs2 = insert_address_row.getGeneratedKeys();
				if (rs2.next()) {
					addr_id = rs2.getInt(1);
				}
			} else { // We actually matched
				addr_id = rs.getInt("addr_id");
			}
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeResultSet(rs2);
			closeStmt(get_co_id);
			closeStmt(match_address);
			closeStmt(insert_address_row);
		}
		return addr_id;
	}

	public static int enterOrder(Connection con, int customer_id, Cart cart, int ship_addr_id,
			String shipping, double c_discount) {
		int o_id = 0;
		PreparedStatement get_id = null;
		PreparedStatement insert_row = null;
		ResultSet rs = null;
		try {
			insert_row = con
					.prepareStatement(
							"INSERT into orders (o_id, o_c_id, o_date, o_sub_total, o_tax, o_total, o_ship_type, o_ship_date, o_bill_addr_id, o_ship_addr_id, o_status) "
									+ "VALUES ( ORDERS_SEQ.nextval, ?, CURRENT_DATE, ?, 8.25, ?, ?, CURRENT_DATE + ?, ?, ?, 'Pending')",
							Statement.RETURN_GENERATED_KEYS);
			insert_row.setInt(1, customer_id);
			insert_row.setDouble(2, cart.SC_SUB_TOTAL);
			insert_row.setDouble(3, cart.SC_TOTAL);
			insert_row.setString(4, shipping);
			insert_row.setInt(5, Util.getRandom(7));
			insert_row.setInt(6, getCAddrID(con, customer_id));
			insert_row.setInt(7, ship_addr_id);

			insert_row.executeUpdate();
			get_id=con.prepareStatement("SELECT ORDERS_SEQ.currval FROM orders");
			rs=get_id.executeQuery();
			//rs = insert_row.getGeneratedKeys();
			if (rs.next()) {
				o_id = rs.getInt(1);
			}
			//System.out.println(o_id);
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(insert_row);
		}

		Enumeration e = cart.lines.elements();
		int counter = 0;
		while (e.hasMoreElements()) {
			// - Creates one or more 'order_line' rows.
			CartLine cart_line = (CartLine) e.nextElement();
			addOrderLine(con, counter, o_id, cart_line.scl_i_id, cart_line.scl_qty, c_discount,
					Util.getRandomString(20, 100));
			counter++;

			// - Adjusts the stock for each item ordered
			int stock = getStock(con, cart_line.scl_i_id);
			if ((stock - cart_line.scl_qty) < 10) {
				setStock(con, cart_line.scl_i_id, stock - cart_line.scl_qty + 21);
			} else {
				setStock(con, cart_line.scl_i_id, stock - cart_line.scl_qty);
			}
		}
		return o_id;
	}

	public static void addOrderLine(Connection con, int ol_id, int ol_o_id, int ol_i_id,
			int ol_qty, double ol_discount, String ol_comment) {
		int success = 0;
		PreparedStatement insert_row = null;
		try {
			insert_row = con
					.prepareStatement("INSERT into order_line (ol_id, ol_o_id, ol_i_id, ol_qty, ol_discount, ol_comments) "
							+ "VALUES (?, ?, ?, ?, ?, ?)");

			insert_row.setInt(1, ol_id);
			insert_row.setInt(2, ol_o_id);
			insert_row.setInt(3, ol_i_id);
			insert_row.setInt(4, ol_qty);
			insert_row.setDouble(5, ol_discount);
			insert_row.setString(6, ol_comment);
			insert_row.executeUpdate();
			// dch
			// con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(insert_row);
		}
	}

	public static int getStock(Connection con, int i_id) {
		int stock = 0;
		PreparedStatement get_stock = null;
		ResultSet rs = null;
		try {
			get_stock = con.prepareStatement("SELECT i_stock FROM item WHERE i_id = ?");

			// Set parameter
			get_stock.setInt(1, i_id);
			rs = get_stock.executeQuery();

	  		// Results
			rs.next();
			stock = rs.getInt("i_stock");
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(get_stock);
		}
		return stock;
	}

	public static void setStock(Connection con, int i_id, int new_stock) {
		PreparedStatement update_row = null;
		try {
			update_row = con.prepareStatement("UPDATE item SET i_stock = ? WHERE i_id = ?");
			update_row.setInt(1, new_stock);
			update_row.setInt(2, i_id);
			update_row.executeUpdate();
			// by dch
			// con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeStmt(update_row);
		}
	}

	public static void verifyDBConsistency() {
		Connection con = null;
		PreparedStatement get_ids = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			int this_id;
			int id_expected = 1;
			// First verify customer table
			get_ids = con.prepareStatement("SELECT c_id FROM customer");
			rs = get_ids.executeQuery();
			while (rs.next()) {
				this_id = rs.getInt("c_id");
				while (this_id != id_expected) {
					System.out.println("Missing C_ID " + id_expected);
					id_expected++;
				}
				id_expected++;
			}

			id_expected = 1;
			// Verify the item table
			get_ids = con.prepareStatement("SELECT i_id FROM item");
			rs = get_ids.executeQuery();
			while (rs.next()) {
				this_id = rs.getInt("i_id");
				while (this_id != id_expected) {
					System.out.println("Missing I_ID " + id_expected);
					id_expected++;
				}
				id_expected++;
			}

			id_expected = 1;
			// Verify the address table
			get_ids = con.prepareStatement("SELECT addr_id FROM address");
			rs = get_ids.executeQuery();
			while (rs.next()) {
				this_id = rs.getInt("addr_id");
				// System.out.println(this_cid+"\n");
				while (this_id != id_expected) {
					System.out.println("Missing ADDR_ID " + id_expected);
					id_expected++;
				}
				id_expected++;
			}

			con.commit();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		} finally {
			closeResultSet(rs);
			closeStmt(get_ids);
			closeConnection(con);
		}
	}

}
