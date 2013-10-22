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

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Customer entity.
 * 
 * @author MyEclipse Persistence Tools, Xin Zhu
 */
// we defined all namedqueries here for convenience
@Entity
@Table(name = "CUSTOMER", schema = "DB2ADMIN")
@NamedQueries( {
		@NamedQuery(name = "subjectSearch", query = "SELECT item,author FROM Item item, Author author WHERE item.IAId = author.AId AND item.ISubject = ?1 ORDER BY item.ITitle"),
		@NamedQuery(name = "titleSearch", query = "SELECT item,author FROM Item item, Author author WHERE item.IAId = author.AId AND item.ITitle like ?1 ORDER BY item.ITitle"),
		@NamedQuery(name = "authorSearch", query = "SELECT item,author FROM Item item, Author author WHERE item.IAId = author.AId AND author.ALname like ?1 ORDER BY item.ITitle"),
		@NamedQuery(name = "getNewProducts", query = "SELECT item,author FROM Item item, Author author WHERE item.IAId = author.AId AND item.ISubject = ?1 ORDER BY item.IPubDate DESC"),
		@NamedQuery(name = "getBestSellers", query = "select item.IId, item.ITitle, author.AFname, author.ALname from Item item, Author author, OrderLine order_line where item.IId = order_line.olIId and item.IAId = author.AId and order_line.id.olOId > (select max(orders.OId)-3333 from Orders orders) and item.ISubject =?1 group by item.IId,item.ITitle,author.AFname,author.ALname order by sum(order_line.olQty) desc"),
		@NamedQuery(name = "getRelated", query = "select J.IId,J.IThumbnail from Item I, Item J where (I.IRelated1 = J.IId or I.IRelated2 = J.IId or I.IRelated3 = J.IId or I.IRelated4 = J.IId or I.IRelated5 = J.IId) and I.IId =?1"),
		@NamedQuery(name = "getRelatedIds", query = "SELECT order_line.olIId FROM Orders orders, OrderLine order_line WHERE orders.OId = order_line.id.olOId "
				+ "AND NOT (order_line.olIId = ?1) "
				+ "AND orders.OCId IN (SELECT orders.OCId "
				+ "                      FROM Orders orders, OrderLine order_line "
				+ "                      WHERE orders.OId = order_line.id.olOId "
				+ "                      AND orders.OId > (SELECT MAX(orders.OId)-10000 FROM Orders orders)"
				+ "                      AND order_line.olIId = ?2) "
				+ "GROUP BY order_line.olIId "
				+ "ORDER BY SUM(order_line.olQty) DESC "),
		@NamedQuery(name = "getQtyForCartItem", query = "SELECT shopping_cart_line.sclQty FROM ShoppingCartLine shopping_cart_line WHERE shopping_cart_line.id.sclScId = ?1 AND shopping_cart_line.id.sclIId = ?2"),
		@NamedQuery(name = "updateQtyForCartItem", query = "UPDATE ShoppingCartLine shopping_cart_line SET shopping_cart_line.sclQty = ?1 WHERE shopping_cart_line.id.sclScId = ?2 AND shopping_cart_line.id.sclIId = ?3"),
		@NamedQuery(name = "getSclCountForSc", query = "SELECT COUNT(shopping_cart_line.id.sclScId) from ShoppingCartLine shopping_cart_line where shopping_cart_line.id.sclScId = ?1"),
		@NamedQuery(name = "getCart", query = "SELECT shopping_cart_line, item "
				+ "FROM ShoppingCartLine shopping_cart_line, Item item "
				+ "WHERE shopping_cart_line.id.sclIId = item.IId AND shopping_cart_line.id.sclScId = ?1"),
		@NamedQuery(name = "getRelated1", query = "SELECT item.IRelated1 FROM Item item where item.IId = ?1"),
		@NamedQuery(name = "getCountryIdForAddress", query = "SELECT country.coId FROM Address address, Country country WHERE address.addrId = ?1 AND address.addrCoId = country.coId"),
		@NamedQuery(name = "clearCart", query = "DELETE FROM ShoppingCartLine shopping_cart_line WHERE shopping_cart_line.id.sclScId = ?1"),
		@NamedQuery(name = "getOrderIdForCustomer", query = "SELECT orders.OId "
				+ "FROM Customer customer, Orders orders "
				+ "WHERE customer.CId = orders.OCId "
				+ "AND customer.CUname = ?1 "
				+ "ORDER BY orders.ODate, orders.OId DESC"),
		@NamedQuery(name = "getOrder", query = "SELECT orders, customer, "
				+ "  cc_xacts.cxType, " + "  ship, " + "  ship_co, "
				+ "  bill, " + "  bill_co "
				+ "FROM Customer customer, Orders orders, CcXacts cc_xacts,"
				+ "  Address ship, " + "  Country ship_co, "
				+ "  Address bill,  " + "  Country bill_co "
				+ "WHERE orders.OId = ?1 "
				+ "  AND cc_xacts.cxOId = orders.OId "
				+ "  AND customer.CId = orders.OCId "
				+ "  AND orders.OBillAddrId = bill.addrId "
				+ "  AND bill.addrCoId = bill_co.coId "
				+ "  AND orders.OShipAddrId = ship.addrId "
				+ "  AND ship.addrCoId = ship_co.coId "
				+ "  AND orders.OCId = customer.CId"),
		@NamedQuery(name = "getOrderLines", query = "SELECT order_line, item "
				+ "FROM OrderLine order_line, Item item "
				+ "WHERE order_line.id.olOId = ?1 "
				+ "AND order_line.olIId = item.IId"),
		@NamedQuery(name = "getAllCustomerIds", query = "SELECT c.CId FROM Customer c"),
		@NamedQuery(name = "getAllItemIds", query = "SELECT item.IId FROM Item item"),
		@NamedQuery(name = "getAllAddressIds", query = "SELECT address.addrId FROM Address address")

})
public class Customer implements java.io.Serializable {

	// Fields

	private Integer CId;
	private String CUname;
	private String CPasswd;
	private String CFname;
	private String CLname;
	private Integer CAddrId;
	private String CPhone;
	private String CEmail;
	private Date CSince;
	private Date CLastLogin;
	private Timestamp CLogin;
	private Timestamp CExpiration;
	private Float CDiscount;
	private Double CBalance;
	private Double CYtdPmt;
	private Date CBirthdate;
	private String CData;

	// Constructors

	/** default constructor */
	public Customer() {
	}

	/** minimal constructor */
	public Customer(Integer CId) {
		this.CId = CId;
	}

	/** full constructor */
	public Customer(Integer CId, String CUname, String CPasswd, String CFname,
			String CLname, Integer CAddrId, String CPhone, String CEmail,
			Date CSince, Date CLastLogin, Timestamp CLogin,
			Timestamp CExpiration, Float CDiscount, Double CBalance,
			Double CYtdPmt, Date CBirthdate, String CData) {
		this.CId = CId;
		this.CUname = CUname;
		this.CPasswd = CPasswd;
		this.CFname = CFname;
		this.CLname = CLname;
		this.CAddrId = CAddrId;
		this.CPhone = CPhone;
		this.CEmail = CEmail;
		this.CSince = CSince;
		this.CLastLogin = CLastLogin;
		this.CLogin = CLogin;
		this.CExpiration = CExpiration;
		this.CDiscount = CDiscount;
		this.CBalance = CBalance;
		this.CYtdPmt = CYtdPmt;
		this.CBirthdate = CBirthdate;
		this.CData = CData;
	}

	// Property accessors
	@Id
	@Column(name = "C_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getCId() {
		return this.CId;
	}

	public void setCId(Integer CId) {
		this.CId = CId;
	}

	@Column(name = "C_UNAME", length = 20)
	public String getCUname() {
		return this.CUname;
	}

	public void setCUname(String CUname) {
		this.CUname = CUname;
	}

	@Column(name = "C_PASSWD", length = 20)
	public String getCPasswd() {
		return this.CPasswd;
	}

	public void setCPasswd(String CPasswd) {
		this.CPasswd = CPasswd;
	}

	@Column(name = "C_FNAME", length = 17)
	public String getCFname() {
		return this.CFname;
	}

	public void setCFname(String CFname) {
		this.CFname = CFname;
	}

	@Column(name = "C_LNAME", length = 17)
	public String getCLname() {
		return this.CLname;
	}

	public void setCLname(String CLname) {
		this.CLname = CLname;
	}

	@Column(name = "C_ADDR_ID")
	public Integer getCAddrId() {
		return this.CAddrId;
	}

	public void setCAddrId(Integer CAddrId) {
		this.CAddrId = CAddrId;
	}

	@Column(name = "C_PHONE", length = 18)
	public String getCPhone() {
		return this.CPhone;
	}

	public void setCPhone(String CPhone) {
		this.CPhone = CPhone;
	}

	@Column(name = "C_EMAIL", length = 50)
	public String getCEmail() {
		return this.CEmail;
	}

	public void setCEmail(String CEmail) {
		this.CEmail = CEmail;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "C_SINCE", length = 10)
	public Date getCSince() {
		return this.CSince;
	}

	public void setCSince(Date CSince) {
		this.CSince = CSince;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "C_LAST_LOGIN", length = 10)
	public Date getCLastLogin() {
		return this.CLastLogin;
	}

	public void setCLastLogin(Date CLastLogin) {
		this.CLastLogin = CLastLogin;
	}

	@Column(name = "C_LOGIN", length = 26)
	public Timestamp getCLogin() {
		return this.CLogin;
	}

	public void setCLogin(Timestamp CLogin) {
		this.CLogin = CLogin;
	}

	@Column(name = "C_EXPIRATION", length = 26)
	public Timestamp getCExpiration() {
		return this.CExpiration;
	}

	public void setCExpiration(Timestamp CExpiration) {
		this.CExpiration = CExpiration;
	}

	@Column(name = "C_DISCOUNT", precision = 24, scale = 0)
	public Float getCDiscount() {
		return this.CDiscount;
	}

	public void setCDiscount(Float CDiscount) {
		this.CDiscount = CDiscount;
	}

	@Column(name = "C_BALANCE", precision = 53, scale = 0)
	public Double getCBalance() {
		return this.CBalance;
	}

	public void setCBalance(Double CBalance) {
		this.CBalance = CBalance;
	}

	@Column(name = "C_YTD_PMT", precision = 53, scale = 0)
	public Double getCYtdPmt() {
		return this.CYtdPmt;
	}

	public void setCYtdPmt(Double CYtdPmt) {
		this.CYtdPmt = CYtdPmt;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "C_BIRTHDATE", length = 10)
	public Date getCBirthdate() {
		return this.CBirthdate;
	}

	public void setCBirthdate(Date CBirthdate) {
		this.CBirthdate = CBirthdate;
	}

	@Column(name = "C_DATA", length = 510)
	public String getCData() {
		return this.CData;
	}

	public void setCData(String CData) {
		this.CData = CData;
	}

}