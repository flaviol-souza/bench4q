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

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.ejb.Local;

/**
 * This interface contains methods that handle cross-table queries. The methods
 * defined in this interface accord to some methods in class
 * {@link org.bench4Q.servlet.EJBDao} .They have similar signatures and
 * meanings.
 * 
 * @author Xin Zhu
 * 
 */
@Local
public interface ComplexQueryLocal {
	/**
	 * Search books by subject
	 * 
	 * @param search_key
	 *            the subject to be matched
	 * @return A list of matched books.The elements are of type Object[2].For
	 *         each element array, the first element stores an {@link Item}
	 *         object, and the second element stores an
	 *         {@link org.bench4Q.ejb3.Author} object.
	 */
	public List doSubjectSearch(String search_key);

	/**
	 * Search books by title
	 * 
	 * @param search_key
	 *            the title to be matched
	 * @return A list of matched books.The elements are of type Object[2].For
	 *         each element array, the first element stores an {@link Item}
	 *         object, and the second element stores an
	 *         {@link org.bench4Q.ejb3.Author} object.
	 */
	public List doTitleSearch(String search_key);

	/**
	 * Search books by author
	 * 
	 * @param search_key
	 *            the keyword to be matched
	 * @return A list of matched books.The elements are of type Object[2].For
	 *         each element array, the first element stores an {@link Item}
	 *         object, and the second element stores an
	 *         {@link org.bench4Q.ejb3.Author} object.
	 */
	public List doAuthorSearch(String searchKey);

	/**
	 * get new books by subject
	 * 
	 * @param subject
	 *            subject to be matched
	 * @return A list of object arrays. For each element array, the length is 2;
	 *         the first element stores an {@link Item} object, and the second
	 *         element stores an {@link org.bench4Q.ejb3.Author} object.
	 */
	public List getNewProducts(String subject);

	/**
	 * get bestsellers by subject
	 * 
	 * @param subject
	 *            subject to be matched
	 * @return A list of object arrays. Each element array has length 4 and
	 *         stores <blockquote>id of the book(int)
	 *         <p>
	 *         title of the book(String)
	 *         <p>
	 *         first name of the author of the book(String)
	 *         <p>
	 *         last name of the author of the book(String)<blockquote>.
	 */
	public List getBestSellers(String subject);

	/**
	 * 
	 * find books related to the book with specific id and store their ids and
	 * thumnails into the vectors passed in
	 * 
	 * @param iId
	 * @param iIdVec
	 * @param iThumbnailVec
	 */
	public void getRelated(int iId, Vector iIdVec, Vector iThumbnailVec);

	/**
	 * update a book
	 * 
	 * @param iId
	 *            the identifier of a book
	 * @param cost
	 *            the new cost
	 * @param image
	 *            the new image
	 * @param thumbnail
	 *            the new thumbnail
	 */
	public void adminUpdate(int iId, double cost, String image, String thumbnail);

	/**
	 * modify shopping cart. On one hand, if <code>I_ID != null</code>, try
	 * adding a new item into the shopping cart, on the other hand, for each
	 * element in ids, update the indicated book's quantity according to thr
	 * corresponding element in parameter quantities
	 * 
	 * @param sHOPPINGID
	 * @param iID
	 * @param ids
	 *            identifiers of books the quantities of which need to be
	 *            updated
	 * @param quantities
	 *            each element indicates the new quantity of a corresponding
	 *            book
	 * @return
	 */
	public Cart doCart(int sHOPPINGID, Integer iID, Vector ids,
			Vector quantities);

	/**
	 * 
	 * Retrieve a {@link Cart} object with the specified sHOPPINGID and
	 * cDiscount
	 * 
	 * @param sHOPPINGID
	 * @param cDiscount
	 * @return
	 */
	public Cart getCart(int sHOPPINGID, double cDiscount);

	/**
	 * 
	 * Add a new customer into the database.
	 * 
	 * 
	 * @param cust
	 *            containers the info of the customer
	 * @param addr
	 *            address info of the customer
	 * @param countryName
	 *            country of the customer
	 * @return
	 */
	public Customer createNewCustomer(Customer cust, Address addr,
			String countryName);

	/**
	 * check out
	 * 
	 * @param shopping_id
	 * @param customer_id
	 * @param cc_type
	 * @param cc_number
	 * @param cc_name
	 * @param cc_expiry
	 * @param shipping
	 * @return
	 * @throws Exception
	 */
	public BuyConfirmResult doBuyConfirm(int shopping_id, int customer_id,
			String cc_type, long cc_number, String cc_name, Date cc_expiry,
			String shipping) throws Exception;

	/**
	 * check out
	 * 
	 * @param shopping_id
	 * @param customer_id
	 * @param cc_type
	 * @param cc_number
	 * @param cc_name
	 * @param cc_expiry
	 * @param shipping
	 * @param street_1
	 * @param street_2
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 * @return
	 * @throws Exception
	 */
	public BuyConfirmResult doBuyConfirm(int shopping_id, int customer_id,
			String cc_type, long cc_number, String cc_name, Date cc_expiry,
			String shipping, String street_1, String street_2, String city,
			String state, String zip, String country) throws Exception;

	/**
	 * Retrieve the most recent order of the customer with specified username
	 * 
	 * @param cUname
	 *            username of a customer
	 * @param orderLines
	 *            orderlines corresponding to the customers's most recent order
	 * @return {@link Order} object corresponding to the most recent order
	 */
	public Order getMostRecentOrder(String cUname, Vector orderLines);
}