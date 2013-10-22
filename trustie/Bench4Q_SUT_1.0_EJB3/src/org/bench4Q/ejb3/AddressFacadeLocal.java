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
 * Local interface for AddressFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Local
public interface AddressFacadeLocal {
	/**
	 * Perform an initial save of a previously unsaved Address entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            Address entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Address entity);

	/**
	 * Delete a persistent Address entity.
	 * 
	 * @param entity
	 *            Address entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Address entity);

	/**
	 * Persist a previously saved Address entity and return it or a copy of it
	 * to the sender. A copy of the Address entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            Address entity to update
	 * @return Address the persisted Address entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Address update(Address entity);

	public Address findById(Integer id);

	/**
	 * Find all Address entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Address property to query
	 * @param value
	 *            the property value to match
	 * @return List<Address> found by query
	 */
	public List<Address> findByProperty(String propertyName, Object value);

	public List<Address> findByAddrStreet1(Object addrStreet1);

	public List<Address> findByAddrStreet2(Object addrStreet2);

	public List<Address> findByAddrCity(Object addrCity);

	public List<Address> findByAddrState(Object addrState);

	public List<Address> findByAddrZip(Object addrZip);

	public List<Address> findByAddrCoId(Object addrCoId);

	/**
	 * Find all Address entities.
	 * 
	 * @return List<Address> all Address entities
	 */
	public List<Address> findAll();

	public int enterAddress(String street1, String street2, String city,
			String state, String zip, String country);

	/**
	 * return id of all addresses
	 * 
	 * @return
	 */
	public List getAllIds();
}