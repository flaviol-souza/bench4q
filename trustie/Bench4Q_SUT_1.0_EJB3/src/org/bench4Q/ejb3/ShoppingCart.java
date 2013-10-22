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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * ShoppingCart entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SHOPPING_CART", schema = "DB2ADMIN")
@NamedQuery(name = "getMaxCartId", query = "SELECT MAX(sc.scId) FROM ShoppingCart sc")
public class ShoppingCart implements java.io.Serializable {

	// Fields

	private Integer scId;
	private Timestamp scTime;

	// Constructors

	/** default constructor */
	public ShoppingCart() {
	}

	/** minimal constructor */
	public ShoppingCart(Integer scId) {
		this.scId = scId;
	}

	/** full constructor */
	public ShoppingCart(Integer scId, Timestamp scTime) {
		this.scId = scId;
		this.scTime = scTime;
	}

	// Property accessors
	@Id
	@Column(name = "SC_ID", unique = true, nullable = false)	
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getScId() {
		return this.scId;
	}

	public void setScId(Integer scId) {
		this.scId = scId;
	}

	@Column(name = "SC_TIME", length = 26)
	public Timestamp getScTime() {
		return this.scTime;
	}

	public void setScTime(Timestamp scTime) {
		this.scTime = scTime;
	}

}