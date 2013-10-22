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

import javax.ejb.Local;

/**
 * Local interface for CustomerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Local
public interface CustomerFacadeLocal {
	/**
	 * Perform an initial save of a previously unsaved Customer entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            Customer entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Customer entity);

	/**
	 * Delete a persistent Customer entity.
	 * 
	 * @param entity
	 *            Customer entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Customer entity);

	/**
	 * Persist a previously saved Customer entity and return it or a copy of it
	 * to the sender. A copy of the Customer entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            Customer entity to update
	 * @return Customer the persisted Customer entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Customer update(Customer entity);

	public Customer findById(Integer id);

	/**
	 * Find all Customer entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Customer property to query
	 * @param value
	 *            the property value to match
	 * @return List<Customer> found by query
	 */
	public List<Customer> findByProperty(String propertyName, Object value);

	public List<Customer> findByCUname(Object CUname);

	public List<Customer> findByCPasswd(Object CPasswd);

	public List<Customer> findByCFname(Object CFname);

	public List<Customer> findByCLname(Object CLname);

	public List<Customer> findByCAddrId(Object CAddrId);

	public List<Customer> findByCPhone(Object CPhone);

	public List<Customer> findByCEmail(Object CEmail);

	public List<Customer> findByCDiscount(Object CDiscount);

	public List<Customer> findByCBalance(Object CBalance);

	public List<Customer> findByCYtdPmt(Object CYtdPmt);

	public List<Customer> findByCData(Object CData);

	/**
	 * Find all Customer entities.
	 * 
	 * @return List<Customer> all Customer entities
	 */
	public List<Customer> findAll();

	public String getUserName(int cID);

	public String getPassword(String cUNAME);

	public void refreshSession(int cID);

	// DB query time: .05s
	public double getCDiscount(int cID);

	public int getCAddrId(int customerId);

	/**
	 * return id of all customers
	 * 
	 * @return
	 */
	public List getAllIds();

}