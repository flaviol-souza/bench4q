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
 *  * Developer(s): Xin Zhu.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.ejb3;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sound.sampled.Line;

@Stateless
/**
 * This class is used to carry out complex JPA queries related to more than one table
 * @author Xin Zhu
 *
 */
public class ComplexQuery implements ComplexQueryLocal {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB
	private ItemFacadeLocal fItem;
	@EJB
	private ShoppingCartLineFacadeLocal fShoppingCartLine;
	@EJB
	private ShoppingCartFacadeLocal fShoppingCart;

	@EJB
	private AddressFacadeLocal fAddress;
	@EJB
	private CustomerFacadeLocal fCustomer;

	public final static String JNDI_NAME = ComplexQuery.class.getName() + "_"
			+ ComplexQueryLocal.class.getName() + "@Local";

	public List doSubjectSearch(String search_key) {
		/**
		 * statement = con // .prepareStatement("SELECT * FROM item, author WHERE item.i_a_id = author.a_id AND item.i_subject = ? ORDER BY item.i_title FETCH FIRST 50 ROWS ONLY"
		 * );
		 */

		Query query = entityManager.createNamedQuery("subjectSearch");
		query.setFirstResult(0);
		query.setMaxResults(50);
		query.setParameter(1, search_key);

		return query.getResultList();

	}

	public List doTitleSearch(String searchKey) {
		/**
		 * statement = con .prepareStatement("SELECT * FROM item, author WHERE item.i_a_id = author.a_id AND item.i_title LIKE ? ORDER BY item.i_title FETCH FIRST 50 ROWS ONLY"
		 * );
		 * 
		 * // Set parameter statement.setString(1, search_key + "%");
		 */
		Query query = entityManager.createNamedQuery("titleSearch");
		query.setFirstResult(0);
		query.setMaxResults(50);
		query.setParameter(1, searchKey + "%"); // TODO: zx:here miss a "%"?
		return query.getResultList();
	}

	public List doAuthorSearch(String searchKey) {
		// statement = con
		// .prepareStatement("SELECT * FROM author, item WHERE author.a_lname LIKE ? AND item.i_a_id = author.a_id ORDER BY item.i_title FETCH FIRST 50 ROWS ONLY");
		//
		// // Set parameter
		// statement.setString(1, search_key + "%");
		Query query = entityManager.createNamedQuery("authorSearch");
		query.setFirstResult(0);
		query.setMaxResults(50);
		query.setParameter(1, searchKey + "%"); // TODO: zx:here miss a "%"?
		// maybe intended for
		// performance
		return query.getResultList();
	}

	public List getNewProducts(String subject) {
		/*
		 * statement = con
		 * .prepareStatement("SELECT i_id, i_title, a_fname, a_lname " +
		 * "FROM item, author " + "WHERE item.i_a_id = author.a_id " +
		 * "AND item.i_subject = ? " +
		 * "ORDER BY item.i_pub_date DESC,item.i_title " +
		 * "FETCH FIRST 50 ROWS ONLY");
		 * 
		 * // Set parameter statement.setString(1, subject);
		 */
		Query query = entityManager.createNamedQuery("getNewProducts");
		query.setFirstResult(0);
		query.setMaxResults(50);
		query.setParameter(1, subject); // TODO: zx:here miss a "%"?
		return query.getResultList();
	}

	public List getBestSellers(String subject) {
		/**
		 * statement = con
		 * .prepareStatement("SELECT i_id, i_title, a_fname, a_lname " +
		 * "FROM item, author, order_line " +
		 * "WHERE item.i_id = order_line.ol_i_id " +
		 * "AND item.i_a_id = author.a_id " +
		 * "AND order_line.ol_o_id > (SELECT MAX(o_id)-3333 FROM orders)" +
		 * "AND item.i_subject = ? " +
		 * "GROUP BY i_id, i_title, a_fname, a_lname " +
		 * "ORDER BY SUM(ol_qty) DESC " + "FETCH FIRST 50 ROWS ONLY ");
		 * 
		 * // Set parameter statement.setString(1, subject);
		 */
		Query query = entityManager.createNamedQuery("getBestSellers");
		query.setFirstResult(0);
		query.setMaxResults(50);
		query.setParameter(1, subject); // TODO: zx:here miss a "%"?
		return query.getResultList();
	}

	public void getRelated(int iId, Vector iIdVec, Vector iThumbnailVec) {
		Query query = entityManager.createNamedQuery("getRelated");
		query.setFirstResult(0);
		query.setMaxResults(50);
		query.setParameter(1, iId); // TODO: zx:here miss a "%"?
		List results = query.getResultList();
		iIdVec.removeAllElements();
		iThumbnailVec.removeAllElements();

		for (Object object : results) {
			Object[] arr = (Object[]) object;
			iIdVec.add((Integer) arr[0]);
			iThumbnailVec.add((String) arr[1]);
		}
	}

	public void adminUpdate(int iId, double cost, String image, String thumbnail) {
		Item item = fItem.findById(iId);
		item.setICost(cost);
		item.setIImage(image);
		item.setIThumbnail(thumbnail);
		item.setIPubDate(new Date());
		fItem.update(item);

		// retrive ids of realted items
		Query getRelatedIds = entityManager.createNamedQuery("getRelatedIds");
		getRelatedIds.setParameter(1, iId);
		getRelatedIds.setParameter(2, iId);
		getRelatedIds.setFirstResult(0);
		getRelatedIds.setMaxResults(5);
		List relatedIdList = getRelatedIds.getResultList();

		int[] related_items = new int[5];
		// Results
		int counter = 0;
		int last = 0;

		Iterator iter = relatedIdList.iterator();
		while (iter.hasNext()) {
			last = (Integer) iter.next();
			related_items[counter] = last;
			counter++;
		}

		for (int i = counter; i < 5; i++) {
			last++;
			related_items[i] = last;
		}

		// update "related" fields of the item table
		item = fItem.findById(iId);
		item.setIRelated1(related_items[0]);
		item.setIRelated2(related_items[1]);
		item.setIRelated3(related_items[2]);
		item.setIRelated4(related_items[3]);
		item.setIRelated5(related_items[4]);

		fItem.update(item);
		/*
		 * Session session = null; Transaction tx = null; try {
		 * 
		 * session = HibernateSessionFactory.getSession(); tx =
		 * session.beginTransaction();
		 * 
		 * // Prepare SQL // con = getConnection(); // statement = con //
		 * .prepareStatement(
		 * "UPDATE item SET i_cost = ?, i_image = ?, i_thumbnail = ?, i_pub_date = CURRENT DATE WHERE i_id = ?"
		 * ); String updateString =
		 * "UPDATE Item item SET item.ICost = ?, item.IImage = ?, item.IThumbnail = ?, item.IPubDate = ? WHERE item.IId = ?"
		 * ; Query updateObject = session.createQuery(updateString); //
		 * updateObject.setCacheable(true); updateObject.setDouble(0, cost);
		 * updateObject.setString(1, image); updateObject.setString(2,
		 * thumbnail); updateObject.setDate(3, new java.util.Date());
		 * updateObject.setInteger(4, i_id); updateObject.executeUpdate();
		 * 
		 * // Set parameter // statement.setDouble(1, cost); //
		 * statement.setString(2, image); // statement.setString(3, thumbnail);
		 * // statement.setInt(4, i_id); // statement.executeUpdate(); //
		 * statement.close(); String queryString = "SELECT order_line.olIId " +
		 * "FROM Orders orders, OrderLine order_line " +
		 * "WHERE orders.OId = order_line.id.olOId " +
		 * "AND NOT (order_line.olIId = ?) " +
		 * "AND orders.OCId IN (SELECT orders.OCId " +
		 * "                      FROM Orders orders, OrderLine order_line " +
		 * "                      WHERE orders.OId = order_line.id.olOId " +
		 * "                      AND orders.OId > (SELECT MAX(orders.OId)-10000 FROM Orders orders)"
		 * + "                      AND order_line.olIId = ?) " +
		 * "GROUP BY order_line.olIId " +
		 * "ORDER BY SUM(order_line.olQty) DESC "; Query query =
		 * session.createQuery(queryString); query.setCacheable(true);
		 * query.setFirstResult(0); query.setMaxResults(5); query.setInteger(0,
		 * i_id); query.setInteger(1, i_id);
		 * 
		 * // Set parameter // related.setInt(1, i_id); // related.setInt(2,
		 * i_id); // rs = related.executeQuery();
		 * 
		 * int[] related_items = new int[5]; // Results int counter = 0; int
		 * last = 0;
		 * 
		 * List list = query.list(); Iterator iter = list.iterator(); while
		 * (iter.hasNext()) { last = (Integer)iter.next();
		 * related_items[counter] = last; counter++; }
		 * 
		 * // This is the case for the situation where there are not 5 related
		 * // books. for (int i = counter; i < 5; i++) { last++;
		 * related_items[i] = last; }
		 * 
		 * { // Prepare SQL // statement = con //.prepareStatement(
		 * "UPDATE item SET i_related1 = ?, i_related2 = ?, i_related3 = ?, i_related4 = ?, i_related5 = ? WHERE i_id = ?"
		 * ); String updateString1 =
		 * "UPDATE Item item SET IRelated1 = ?, IRelated2 = ?, IRelated3 = ?, IRelated4 = ?, IRelated5 = ? WHERE IId = ?"
		 * ; Query updateObject1 = session.createQuery(updateString1); //
		 * updateObject1.setCacheable(true); updateObject1.setInteger(0,
		 * related_items[0]); updateObject1.setInteger(1, related_items[1]);
		 * updateObject1.setInteger(2, related_items[2]);
		 * updateObject1.setInteger(3, related_items[3]);
		 * updateObject1.setInteger(4, related_items[4]);
		 * updateObject1.setInteger(5, i_id); updateObject1.executeUpdate();
		 * 
		 * 
		 * // Set parameter // statement.setInt(1, related_items[0]); //
		 * statement.setInt(2, related_items[1]); // statement.setInt(3,
		 * related_items[2]); // statement.setInt(4, related_items[3]); //
		 * statement.setInt(5, related_items[4]); // statement.setInt(6, i_id);
		 * // statement.executeUpdate(); } // con.commit(); tx.commit(); } catch
		 * (java.lang.Exception ex) { if(tx != null) { tx.rollback(); }
		 * ex.printStackTrace(); } finally { // closeResultSet(rs); //
		 * closeStmt(statement); // closeStmt(related); // closeConnection(con);
		 * if(session != null) { session.close(); } }
		 */
	}

	/**
	 * This function finds the shopping cart item associated with SHOPPING_ID
	 * and I_ID. If the item does not already exist, we create one with QTY=1,
	 * otherwise we increment the quantity.
	 * 
	 * @param SHOPPING_ID
	 * @param I_ID
	 */
	private void addItem(int SHOPPING_ID, int I_ID) {

		// // Prepare SQL
		// find_entry = con
		// .prepareStatement("SELECT scl_qty FROM shopping_cart_line WHERE scl_sc_id = ? AND scl_i_id = ?");

		// check if the item is already in shopping cart
		Query getQtyForCartItem = entityManager
				.createNamedQuery("getQtyForCartItem");

		getQtyForCartItem.setParameter(1, SHOPPING_ID);
		getQtyForCartItem.setParameter(2, I_ID);

		List qtyList = getQtyForCartItem.getResultList();
		if (qtyList != null && !qtyList.isEmpty()) { // if the item is already
			// in shopping cart,just
			// update the quantity
			int currQty = (Integer) qtyList.get(0);
			currQty += 1;

			Query updateQtyForCartItem = entityManager
					.createNamedQuery("updateQtyForCartItem");

			updateQtyForCartItem.setParameter(1, currQty);
			updateQtyForCartItem.setParameter(2, SHOPPING_ID);
			updateQtyForCartItem.setParameter(3, I_ID);

			updateQtyForCartItem.executeUpdate();

		} else {// We need to add a new row to the table.
			ShoppingCartLineId sclId = new ShoppingCartLineId(SHOPPING_ID, I_ID);
			ShoppingCartLine sclObj = new ShoppingCartLine(sclId, 1);
			entityManager.persist(sclObj);

			/*
			 * // Stick the item info in a new shopping_cart_line
			 * PreparedStatement put_line = con.prepareStatement(
			 * "INSERT into shopping_cart_line (scl_sc_id, scl_qty, scl_i_id) VALUES (?,?,?)"
			 * );
			 * 
			 * put_line.setInt(1, SHOPPING_ID); put_line.setInt(2, 1);
			 * put_line.setInt(3, I_ID); put_line.executeUpdate();
			 * put_line.close();
			 */
		}
	}

	/**
	 * For each item in the shopping cart, update its quantity, if the new
	 * quantity is 0, then remove the item from the cart
	 * 
	 * @param SHOPPING_ID
	 *            id of the shopping cart
	 * @param ids
	 *            ids of the items in the shopping cart
	 * @param quantities
	 *            new quantities of the items
	 */
	private void refreshCart(int SHOPPING_ID, Vector ids, Vector quantities) {
		for (int i = 0; i < ids.size(); i++) {
			String I_IDstr = (String) ids.elementAt(i);
			String QTYstr = (String) quantities.elementAt(i);
			int I_ID = Integer.parseInt(I_IDstr);
			int QTY = Integer.parseInt(QTYstr);

			if (QTY == 0) { // We need to remove the item from the cart
				// statement = con
				// .prepareStatement("DELETE FROM shopping_cart_line WHERE scl_sc_id = ? AND scl_i_id = ?");
				// statement.setInt(1, SHOPPING_ID);
				// statement.setInt(2, I_ID);
				// statement.executeUpdate();
				// con.commit();

				ShoppingCartLineId sclId = new ShoppingCartLineId(SHOPPING_ID,
						I_ID);
				ShoppingCartLine scl = fShoppingCartLine.findById(sclId);

				if (scl != null)
					fShoppingCartLine.delete(scl);
			} else {// update quantity of the item
				ShoppingCartLineId sclId = new ShoppingCartLineId(SHOPPING_ID,
						I_ID);
				ShoppingCartLine scl = fShoppingCartLine.findById(sclId);

				scl.setSclQty(QTY);
				entityManager.merge(scl);// TODO:zx right?
				/*
				 * Query updateObject = session.createQuery(
				 * "UPDATE ShoppingCartLine shopping_cart_line SET shopping_cart_line.sclQty = ? WHERE shopping_cart_line.id.sclScId = ? AND shopping_cart_line.id.sclIId = ?"
				 * ); // updateObject.setCacheable(true);
				 * updateObject.setInteger(0, QTY); updateObject.setInteger(1,
				 * SHOPPING_ID); updateObject.setInteger(2, I_ID);
				 * updateObject.executeUpdate();
				 */
			}

			/*
			 * PreparedStatement statement = null; int i; try {
			 * 
			 * for (i = 0; i < ids.size(); i++) { String I_IDstr = (String)
			 * ids.elementAt(i); String QTYstr = (String)
			 * quantities.elementAt(i); int I_ID = Integer.parseInt(I_IDstr);
			 * int QTY = Integer.parseInt(QTYstr);
			 * 
			 * if (QTY == 0) { // We need to remove the item from the cart //
			 * statement = con //.prepareStatement(
			 * "DELETE FROM shopping_cart_line WHERE scl_sc_id = ? AND scl_i_id = ?"
			 * ); // statement.setInt(1, SHOPPING_ID); // statement.setInt(2,
			 * I_ID); // statement.executeUpdate(); // con.commit();
			 * 
			 * Query deleteObject = session.createQuery(
			 * "DELETE FROM ShoppingCartLine shopping_cart_line WHERE shopping_cart_line.id.sclScId = ? AND shopping_cart_line.id.sclIId = ?"
			 * ); // deleteObject.setCacheable(true); deleteObject.setInteger(0,
			 * SHOPPING_ID); deleteObject.setInteger(1, I_ID);
			 * deleteObject.executeUpdate(); } else { // we update the quantity
			 * // statement = con //.prepareStatement(
			 * "UPDATE shopping_cart_line SET scl_qty = ? WHERE scl_sc_id = ? AND scl_i_id = ?"
			 * ); // statement.setInt(1, QTY); // statement.setInt(2,
			 * SHOPPING_ID); // statement.setInt(3, I_ID); //
			 * statement.executeUpdate(); // con.commit();
			 * 
			 * Query updateObject = session.createQuery(
			 * "UPDATE ShoppingCartLine shopping_cart_line SET shopping_cart_line.sclQty = ? WHERE shopping_cart_line.id.sclScId = ? AND shopping_cart_line.id.sclIId = ?"
			 * ); // updateObject.setCacheable(true); updateObject.setInteger(0,
			 * QTY); updateObject.setInteger(1, SHOPPING_ID);
			 * updateObject.setInteger(2, I_ID); updateObject.executeUpdate(); }
			 * }
			 */
		}
	}

	// time .05s
	public Cart getCart(int SHOPPING_ID, double c_discount) {
		return innerGetCart(SHOPPING_ID, c_discount);
	}

	/**
	 * reset the timestamp of a shopping cart to current time
	 * 
	 * @param SHOPPING_ID
	 */
	private void resetCartTime(int SHOPPING_ID) {
		/*
		 * Query updateObject = session.createQuery(
		 * "UPDATE ShoppingCart shopping_cart SET shopping_cart.scTime = ? WHERE shopping_cart.scId = ?"
		 * ); // updateObject.setCacheable(true); updateObject.setTimestamp(0,
		 * new java.util.Date()); updateObject.setInteger(1, SHOPPING_ID);
		 * updateObject.executeUpdate();
		 */
		ShoppingCart sc = fShoppingCart.findById(SHOPPING_ID);
		sc.setScTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(sc);
	}

	/**
	 * This function gets the value of I_RELATED1 for the row of the item table
	 * corresponding to I_ID. This means get the id of the first related item.
	 * 
	 * @param I_ID
	 * @return
	 */
	private int getRelated1(int I_ID) {
		int related1 = -1;

		Query query = entityManager.createNamedQuery("getRelated1");
		query.setParameter(1, I_ID);

		List relatedList = query.getResultList();

		// if an item doesn't have related items,return 10 as a default value
		if (relatedList == null || relatedList.size() == 0) {
			return 10;
		}

		return (Integer) relatedList.get(0);
	}

	/**
	 * If the shopping cart is empty, add a random item into the cart
	 * 
	 * @param SHOPPING_ID
	 */
	private void addRandomItemToCartIfNecessary(int SHOPPING_ID) {
		/*
		 * int related_item = 0; // PreparedStatement get_cart = null; //
		 * ResultSet rs = null;
		 * 
		 * try {
		 * 
		 * // Check to see if the cart is empty // get_cart = con //
		 * .prepareStatement
		 * ("SELECT COUNT(*) from shopping_cart_line where scl_sc_id = ?"); //
		 * get_cart.setInt(1, SHOPPING_ID); // rs = get_cart.executeQuery(); //
		 * rs.next(); // if (rs.getInt(1) == 0) { // // Cart is empty // int
		 * rand_id = Util.getRandomI_ID(); // related_item =
		 * getRelated1(rand_id); // addItem(con, SHOPPING_ID, related_item); //
		 * }
		 * 
		 * String queryString =
		 * "SELECT COUNT(*) from ShoppingCartLine shopping_cart_line where shopping_cart_line.id.sclScId = ?"
		 * ; Query queryObject = session.createQuery(queryString);
		 * queryObject.setCacheable(true); queryObject.setInteger(0,
		 * SHOPPING_ID); List list = queryObject.list(); long countResult =
		 * (Long)list.get(0);; if(countResult==0){ int rand_id =
		 * Util.getRandomI_ID(); related_item = getRelated1(session, rand_id);
		 * addItem(session, SHOPPING_ID, related_item); }
		 */
		int related_item = 0;
		Query queryObject = entityManager.createNamedQuery("getSclCountForSc");
		queryObject.setParameter(1, SHOPPING_ID);
		List list = queryObject.getResultList();
		long countResult = (Long) list.get(0);
		if (countResult == 0) {// add a random item into the cart
			int rand_id = Util.getRandomI_ID();
			related_item = getRelated1(rand_id);
			addItem(SHOPPING_ID, related_item);
		}
	}

	/**
	 * encapsulate common logic of {@link #getCart(int, double)} and
	 * {@link #doCart(int, Integer, Vector, Vector)}
	 * 
	 * @param SHOPPING_ID
	 * @param c_discount
	 * @return
	 */
	private Cart innerGetCart(int SHOPPING_ID, double c_discount) {
		Cart mycart = null;
		/*
		 * Query query = session.createQuery("SELECT shopping_cart_line, item "
		 * + "FROM ShoppingCartLine shopping_cart_line, Item item " +
		 * "WHERE shopping_cart_line.id.sclIId = item.IId AND shopping_cart_line.id.sclScId = ?"
		 * );
		 */
		try {
			// used namequery to retrieve required data
			Query query = entityManager.createNamedQuery("getCart");

			query.setParameter(1, SHOPPING_ID);
			List list = query.getResultList();
			if (list == null) {
				list = new ArrayList();
			}

			// store data in a Cart object
			mycart = new Cart(list, c_discount);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return mycart;
	}

	public Cart doCart(int sHOPPINGID, Integer iID, Vector ids,
			Vector quantities) {
		Cart cart = null;

		if (iID != null) {
			addItem(sHOPPINGID, iID.intValue());
		}
		refreshCart(sHOPPINGID, ids, quantities);
		addRandomItemToCartIfNecessary(sHOPPINGID);
		resetCartTime(sHOPPINGID);

		try {
			cart = innerGetCart(sHOPPINGID, 0.0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return cart;
	}

	public Customer createNewCustomer(Customer cust, Address addr,
			String countryName) {
		cust.setCDiscount((float) (java.lang.Math.random() * 51));
		cust.setCBalance(0.0);
		cust.setCYtdPmt(0.0);
		// FIXME - Use SQL CURRENT_TIME to do this
		cust.setCLastLogin(new Timestamp(System.currentTimeMillis()));
		cust.setCSince(new Timestamp(System.currentTimeMillis()));
		cust.setCLogin(new Timestamp(System.currentTimeMillis()));
		cust
				.setCExpiration(new Timestamp(
						System.currentTimeMillis() + 7200000));// milliseconds

		// in 2
		// hours
		// insert_customer_row = con
		// .prepareStatement(
		// "INSERT into customer (c_uname, c_passwd, c_fname, c_lname, c_addr_id, c_phone, c_email, c_since, c_last_login, c_login, c_expiration, c_discount, c_balance, c_ytd_pmt, c_birthdate, c_data) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
		// Statement.RETURN_GENERATED_KEYS);
		// insert_customer_row.setString(3, cust.c_fname);
		// insert_customer_row.setString(4, cust.c_lname);
		// insert_customer_row.setString(6, cust.c_phone);
		// insert_customer_row.setString(7, cust.c_email);
		// insert_customer_row.setDate(8, new
		// java.sql.Date(cust.c_since.getTime()));
		// insert_customer_row.setDate(9, new
		// java.sql.Date(cust.c_last_visit.getTime()));
		// insert_customer_row.setDate(10, new
		// java.sql.Date(cust.c_login.getTime()));
		// insert_customer_row.setDate(11, new
		// java.sql.Date(cust.c_expiration.getTime()));
		// insert_customer_row.setDouble(12, cust.c_discount);
		// insert_customer_row.setDouble(13, cust.c_balance);
		// insert_customer_row.setDouble(14, cust.c_ytd_pmt);
		// insert_customer_row.setDate(15, new
		// java.sql.Date(cust.c_birthdate.getTime()));
		// insert_customer_row.setString(16, cust.c_data);

		cust.setCAddrId(enterAddress(addr.getAddrStreet1(), addr
				.getAddrStreet2(), addr.getAddrCity(), addr.getAddrState(),
				addr.getAddrZip(), countryName));

		//		

		fCustomer.save(cust);
		/*
		 * cust.c_id = customerHib.getCId();
		 * 
		 * cust.c_uname = Util.DigSyl(cust.c_id, 0); cust.c_passwd =
		 * cust.c_uname.toLowerCase();
		 * 
		 * customerHib.setCUname(cust.c_uname);
		 * customerHib.setCPasswd(cust.c_passwd);
		 */
		// PreparedStatement updateUnameANDPasswd = con
		// .prepareStatement("UPDATE customer SET c_uname = ?, c_passwd = ? WHERE c_id = ?");
		// updateUnameANDPasswd.setString(1, cust.c_uname);
		// updateUnameANDPasswd.setString(2, cust.c_passwd);
		// updateUnameANDPasswd.setLong(3, cust.c_id);
		// updateUnameANDPasswd.executeUpdate();

		// set username to a string generated from the id
		cust.setCUname(Util.DigSyl(cust.getCId(), 0));
		cust.setCPasswd(cust.getCUname().toLowerCase());
		fCustomer.update(cust);
		return cust;
	}

	/**
	 * insert an address and return the generated key(id of the address)
	 * 
	 * @param street1
	 * @param street2
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @return
	 */
	private int enterAddress(String street1, String street2, String city,
			String state, String zip, String country) {
		return fAddress.enterAddress(street1, street2, city, state, zip,
				country);
	}

	public BuyConfirmResult doBuyConfirm(int shoppingId, int customerId,
			String ccType, long ccNumber, String ccName, Date ccExpiry,
			String shipping) {
		try {
			BuyConfirmResult result = new BuyConfirmResult();
			double c_discount = fCustomer.getCDiscount(customerId);
			result.cart = getCart(shoppingId, c_discount);
			int ship_addr_id = fCustomer.getCAddrId(customerId);
			result.order_id = enterOrder(customerId, result.cart, ship_addr_id,
					shipping, c_discount);
			enterCCXact(result.order_id, ccType, ccNumber, ccName, ccExpiry,
					result.cart.SC_TOTAL, ship_addr_id);
			clearCart(shoppingId);
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("doBuyConfirm failed");
		}
	}

	/**
	 * 
	 * @param c_id
	 * @return id of the address of the customer with <code>c_id</code> as its
	 *         id
	 */
	private int getCAddrID(int c_id) {
		return fCustomer.getCAddrId(c_id);
	}

	/**
	 * convert data in shopping cart into an order,which means insert a row in
	 * table <code>Orders</code>£¬and several rows in table
	 * <code>order_line</code>
	 * 
	 * @param customer_id
	 * @param cart
	 * @param ship_addr_id
	 * @param shipping
	 * @param c_discount
	 * @return
	 * @throws Exception
	 */
	private int enterOrder(int customer_id, Cart cart, int ship_addr_id,
			String shipping, double c_discount) throws Exception {
		// first insert a row into table Orders
		int o_id = 0;
		// PreparedStatement insert_row = null;
		// ResultSet rs = null;
		try {
			java.util.Date now = new java.util.Date();
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(now);
			cal.add(java.util.Calendar.DAY_OF_MONTH, Util.getRandom(7));

			java.util.Date shipDate = cal.getTime();

			Orders orders = new Orders();
			orders.setOCId(customer_id);
			orders.setODate(now);
			orders.setOSubTotal(cart.SC_SUB_TOTAL);
			orders.setOTax(8.25);
			orders.setOTotal(cart.SC_TOTAL);
			orders.setOShipType(shipping);
			orders.setOShipDate(shipDate);
			orders.setOBillAddrId(getCAddrID(customer_id));
			orders.setOShipAddrId(ship_addr_id);
			orders.setOStatus("Pending");

			entityManager.persist(orders);

			// insert_row = con
			// .prepareStatement(
			// "INSERT into orders (o_c_id, o_date, o_sub_total, o_tax, o_total, o_ship_type, o_ship_date, o_bill_addr_id, o_ship_addr_id, o_status) "
			// +
			// "VALUES ( ?, CURRENT DATE, ?, 8.25, ?, ?, CURRENT DATE + ? DAYS, ?, ?, 'Pending')",
			// Statement.RETURN_GENERATED_KEYS);
			// insert_row.setInt(1, customer_id);
			// insert_row.setDouble(2, cart.SC_SUB_TOTAL);
			// insert_row.setDouble(3, cart.SC_TOTAL);
			// insert_row.setString(4, shipping);
			// insert_row.setInt(5, Util.getRandom(7));
			// insert_row.setInt(6, getCAddrID(con, customer_id));
			// insert_row.setInt(7, ship_addr_id);
			//
			// insert_row.executeUpdate();
			// rs = insert_row.getGeneratedKeys();
			// if (rs.next()) {
			// o_id = rs.getInt(1);
			// }
			o_id = orders.getOId();

		} catch (java.lang.Exception ex) {
			throw ex;
			// ex.printStackTrace();
		} finally {
			// closeResultSet(rs);
			// closeStmt(insert_row);
		}

		// insert rows into table order_line
		Enumeration e = cart.lines.elements();
		int counter = 0;
		while (e.hasMoreElements()) {
			// - Creates one or more 'order_line' rows.
			CartLine cart_line = (CartLine) e.nextElement();
			addOrderLine(counter, o_id, cart_line.scl_i_id, cart_line.scl_qty,
					c_discount, Util.getRandomString(20, 100));
			counter++;

			// - Adjusts the stock for each item ordered
			int stock = getStock(cart_line.scl_i_id);
			if ((stock - cart_line.scl_qty) < 10) {
				setStock(cart_line.scl_i_id, stock - cart_line.scl_qty + 21);
			} else {
				setStock(cart_line.scl_i_id, stock - cart_line.scl_qty);
			}
		}
		return o_id;
	}

	/**
	 * deal with payment
	 * 
	 * @param o_id
	 * @param cc_type
	 * @param cc_number
	 * @param cc_name
	 * @param cc_expiry
	 * @param total
	 * @param ship_addr_id
	 * @throws Exception
	 */
	private void enterCCXact(
			int o_id, // Order id
			String cc_type, long cc_number, String cc_name, Date cc_expiry,
			double total, // Total
			// from
			// shopping
			// cart
			int ship_addr_id) throws Exception {
		// PreparedStatement statement = null;

		// Updates the CC_XACTS table
		if (cc_type.length() > 10)
			cc_type = cc_type.substring(0, 10);
		if (cc_name.length() > 30)
			cc_name = cc_name.substring(0, 30);

		// Prepare SQL
		// statement = con
		// .prepareStatement("INSERT into cc_xacts (cx_o_id, cx_type, cx_num, cx_name, cx_expire, cx_xact_amt, cx_xact_date, cx_co_id) "
		// +
		// "VALUES (?, ?, ?, ?, ?, ?, CURRENT DATE, (SELECT co_id FROM address, country WHERE addr_id = ? AND addr_co_id = co_id))");
		//
		// // Set parameter
		// statement.setInt(1, o_id); // cx_o_id
		// statement.setString(2, cc_type); // cx_type
		// statement.setLong(3, cc_number); // cx_num
		// statement.setString(4, cc_name); // cx_name
		// statement.setDate(5, cc_expiry); // cx_expiry
		// statement.setDouble(6, total); // cx_xact_amount
		// statement.setInt(7, ship_addr_id); // ship_addr_id
		// statement.executeUpdate();

		Query query = entityManager.createNamedQuery("getCountryIdForAddress");
		// Query queryObject = session
		// .createQuery("SELECT country.coId FROM Address address, Country country WHERE address.addrId = ? AND address.addrCoId = country.coId");

		query.setParameter(1, ship_addr_id);
		List listCountryId = query.getResultList();

		CcXacts ccXacts = new CcXacts(o_id, cc_type, String.valueOf(cc_number),
				cc_name, cc_expiry, (String) null, total, new java.util.Date(),
				(Integer) listCountryId.get(0));

		entityManager.persist(ccXacts);
	}

	/**
	 * delete rows from the table <code>shopping_cart_line</code> which no
	 * longer make sense
	 * 
	 * @param shopping_id
	 */
	private void clearCart(int shopping_id) {
		// Empties all the lines from the shopping_cart_line for the
		// shopping id. Does not remove the actually shopping cart
		Query query = entityManager.createNamedQuery("clearCart");
		query.setParameter(1, shopping_id);
		query.executeUpdate();
	}

	public BuyConfirmResult doBuyConfirm(int shopping_id, int customer_id,
			String cc_type, long cc_number, String cc_name, Date cc_expiry,
			String shipping, String street_1, String street_2, String city,
			String state, String zip, String country) throws Exception {

		BuyConfirmResult result = new BuyConfirmResult();
		Connection con = null;

		double c_discount = fCustomer.getCDiscount(customer_id);
		result.cart = getCart(shopping_id, c_discount);
		int ship_addr_id = enterAddress(street_1, street_2, city, state, zip,
				country);
		result.order_id = enterOrder(customer_id, result.cart, ship_addr_id,
				shipping, c_discount);
		enterCCXact(result.order_id, cc_type, cc_number, cc_name, cc_expiry,
				result.cart.SC_TOTAL, ship_addr_id);
		clearCart(shopping_id);

		return result;
	}

	/**
	 * insert a row into table <code>order_line</code>
	 * 
	 * @param ol_id
	 * @param ol_o_id
	 * @param ol_i_id
	 * @param ol_qty
	 * @param ol_discount
	 * @param ol_comment
	 * @throws Exception
	 */
	private void addOrderLine(int ol_id, int ol_o_id, int ol_i_id, int ol_qty,
			double ol_discount, String ol_comment) throws Exception {
		// PreparedStatement insert_row = null;
		// insert_row = con
		// .prepareStatement("INSERT into order_line (ol_id, ol_o_id, ol_i_id, ol_qty, ol_discount, ol_comments) "
		// + "VALUES (?, ?, ?, ?, ?, ?)");
		//
		// insert_row.setInt(1, ol_id);
		// insert_row.setInt(2, ol_o_id);
		// insert_row.setInt(3, ol_i_id);
		// insert_row.setInt(4, ol_qty);
		// insert_row.setDouble(5, ol_discount);
		// insert_row.setString(6, ol_comment);
		// insert_row.executeUpdate();

		OrderLine ol = new OrderLine();
		OrderLineId olId = new OrderLineId();

		olId.setOlId(ol_id);
		olId.setOlOId(ol_o_id);
		ol.setId(olId);
		ol.setOlComments(ol_comment);
		ol.setOlDiscount(ol_discount);
		ol.setOlIId(ol_i_id);
		ol.setOlQty(ol_qty);

		entityManager.persist(ol);

	}

	/**
	 * return the quantity of stock of the item with id passed in
	 * 
	 * @param i_id
	 * @return
	 * @throws Exception
	 */
	private int getStock(int i_id) throws Exception {
		return fItem.getStock(i_id);
	}

	/**
	 * update the quantity of stock
	 * 
	 * @param i_id
	 * @param new_stock
	 * @throws Exception
	 */
	private void setStock(int i_id, int new_stock) throws Exception {
		fItem.updateStock(i_id, new_stock);
	}

	public Order getMostRecentOrder(String cUname, Vector order_lines) {
		order_lines.removeAllElements();

		// get order_id
		int order_id;
		Order order;

		// Prepare SQL
		// con = getConnection();

		// *** Get the o_id of the most recent order for this user
		// String str_get_most_recent_order_id = "SELECT orders.OId "
		// + "FROM Customer customer, Orders orders " +
		// "WHERE customer.CId = orders.OCId "
		// + "AND customer.CUname = ? " +
		// "ORDER BY orders.ODate, orders.OId DESC ";
		Query queryGetMostRecentOrderId = entityManager
				.createNamedQuery("getOrderIdForCustomer");
		queryGetMostRecentOrderId.setFirstResult(0);
		queryGetMostRecentOrderId.setMaxResults(1);
		queryGetMostRecentOrderId.setParameter(1, cUname);

		List listMostRecentOrderId = queryGetMostRecentOrderId.getResultList();

		// // Set parameter
		// get_most_recent_order_id.setString(1, c_uname);
		// rs = get_most_recent_order_id.executeQuery();

		if (listMostRecentOrderId != null && !listMostRecentOrderId.isEmpty()) {
			// order_id = rs.getInt("o_id");
			// Object[]
			// arrMostRecentOrderId=(Object[])listMostRecentOrderId.get(0);
			order_id = (Integer) listMostRecentOrderId.get(0);
		} else {
			return null;
		}

		// *** Get the order info for this o_id
		// String str_get_order = "SELECT orders.*, customer.*, "
		// + "  cc_xacts.cx_type, " +
		// "  ship.addr_street1 AS ship_addr_street1, "
		// + "  ship.addr_street2 AS ship_addr_street2, "
		// + "  ship.addr_state AS ship_addr_state, "
		// + "  ship.addr_zip AS ship_addr_zip, " +
		// "  ship_co.co_name AS ship_co_name, "
		// + "  bill.addr_street1 AS bill_addr_street1, "
		// + "  bill.addr_street2 AS bill_addr_street2, "
		// + "  bill.addr_state AS bill_addr_state, "
		// + "  bill.addr_zip AS bill_addr_zip, " +
		// "  bill_co.co_name AS bill_co_name "
		// + "FROM customer, orders, cc_xacts," + "  address AS ship, "
		// + "  country AS ship_co, " + "  address AS bill,  " +
		// "  country AS bill_co "
		// + "WHERE orders.o_id = ? " + "  AND cx_o_id = orders.o_id "
		// + "  AND customer.c_id = orders.o_c_id "
		// + "  AND orders.o_bill_addr_id = bill.addr_id "
		// + "  AND bill.addr_co_id = bill_co.co_id "
		// + "  AND orders.o_ship_addr_id = ship.addr_id "
		// + "  AND ship.addr_co_id = ship_co.co_id "
		// + "  AND orders.o_c_id = customer.c_id";

		// get complete order info
		Query queryGetOrder = entityManager.createNamedQuery("getOrder");

		queryGetOrder.setParameter(1, order_id);

		// Set parameter
		// get_order.setInt(1, order_id);
		// rs2 = get_order.executeQuery();

		List listGetOrder = queryGetOrder.getResultList();

		// Results
		// if (!rs2.next()) {
		// // FIXME - This case is due to an error due to a database
		// // population error
		// con.commit();
		// return null;
		// }

		if (listGetOrder == null || listGetOrder.isEmpty()) {
			return null;
		}

		Object[] arrGetOrder = (Object[]) listGetOrder.get(0);
		order = new Order((Orders) arrGetOrder[0], (Customer) arrGetOrder[1],
				(String) arrGetOrder[2], (Address) arrGetOrder[3],
				(Country) arrGetOrder[4], (Address) arrGetOrder[5],
				(Country) arrGetOrder[6]);

		// get order lines for the order

		// *** Get the order_lines for this o_id
		// get_order_lines = con.prepareStatement("SELECT * "
		// + "FROM order_line, item " + "WHERE ol_o_id = ? "
		// + "AND ol_i_id = i_id");
		// String str_get_order_lines = "SELECT order_line, item " +
		// "FROM OrderLine order_line, Item item "
		// + "WHERE order_line.id.olOId = ? " +
		// "AND order_line.olIId = item.IId";
		Query queryGetOrderLines = entityManager
				.createNamedQuery("getOrderLines");
		queryGetOrderLines.setParameter(1, order_id);

		// Set parameter
		// get_order_lines.setInt(1, order_id);
		// rs3 = get_order_lines.executeQuery();

		List listGetOrderLines = queryGetOrderLines.getResultList();

		// Results
		// while (rs3.next()) {
		// order_lines.addElement(new OrderLine(rs3));
		// }

		if (listGetOrderLines != null) {
			Iterator iterGetOrderLines = listGetOrderLines.iterator();
			while (iterGetOrderLines.hasNext()) {
				Object[] arrGetOrderLines = (Object[]) iterGetOrderLines.next();
				order_lines.addElement(new OrderLineTemp(
						(OrderLine) arrGetOrderLines[0],
						(Item) arrGetOrderLines[1]));
			}
		}

		return order;
	}
}