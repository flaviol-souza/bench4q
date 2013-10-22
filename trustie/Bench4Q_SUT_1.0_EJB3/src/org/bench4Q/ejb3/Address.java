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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.Table;

/**
 * Address entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ADDRESS", schema = "DB2ADMIN")
@NamedQuery(name = "checkAddress", query = "SELECT address.addrId FROM Address address "
		+ "WHERE address.addrStreet1 = :street1 "
		+ "AND address.addrStreet2 = :street2 "
		+ "AND address.addrCity = :city "
		+ "AND address.addrState = :state "
		+ "AND address.addrZip = :zip " + "AND address.addrCoId = :coId")
public class Address implements java.io.Serializable {

	// Fields

	private Integer addrId;
	private String addrStreet1;
	private String addrStreet2;
	private String addrCity;
	private String addrState;
	private String addrZip;
	private Integer addrCoId;

	// Constructors

	/** default constructor */
	public Address() {
	}

	/** minimal constructor */
	public Address(Integer addrId) {
		this.addrId = addrId;
	}

	/** full constructor */
	public Address(Integer addrId, String addrStreet1, String addrStreet2,
			String addrCity, String addrState, String addrZip, Integer addrCoId) {
		this.addrId = addrId;
		this.addrStreet1 = addrStreet1;
		this.addrStreet2 = addrStreet2;
		this.addrCity = addrCity;
		this.addrState = addrState;
		this.addrZip = addrZip;
		this.addrCoId = addrCoId;
	}

	// Property accessors
	@Id
	@Column(name = "ADDR_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getAddrId() {
		return this.addrId;
	}

	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}

	@Column(name = "ADDR_STREET1", length = 40)
	public String getAddrStreet1() {
		return this.addrStreet1;
	}

	public void setAddrStreet1(String addrStreet1) {
		this.addrStreet1 = addrStreet1;
	}

	@Column(name = "ADDR_STREET2", length = 40)
	public String getAddrStreet2() {
		return this.addrStreet2;
	}

	public void setAddrStreet2(String addrStreet2) {
		this.addrStreet2 = addrStreet2;
	}

	@Column(name = "ADDR_CITY", length = 30)
	public String getAddrCity() {
		return this.addrCity;
	}

	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}

	@Column(name = "ADDR_STATE", length = 20)
	public String getAddrState() {
		return this.addrState;
	}

	public void setAddrState(String addrState) {
		this.addrState = addrState;
	}

	@Column(name = "ADDR_ZIP", length = 10)
	public String getAddrZip() {
		return this.addrZip;
	}

	public void setAddrZip(String addrZip) {
		this.addrZip = addrZip;
	}

	@Column(name = "ADDR_CO_ID")
	public Integer getAddrCoId() {
		return this.addrCoId;
	}

	public void setAddrCoId(Integer addrCoId) {
		this.addrCoId = addrCoId;
	}

}