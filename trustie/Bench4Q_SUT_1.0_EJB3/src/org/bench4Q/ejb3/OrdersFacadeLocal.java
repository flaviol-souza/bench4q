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
import javax.ejb.Local;

/**
 * Local interface for OrdersFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface OrdersFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved Orders entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Orders entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Orders entity);
    /**
	 Delete a persistent Orders entity.
	  @param entity Orders entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Orders entity);
   /**
	 Persist a previously saved Orders entity and return it or a copy of it to the sender. 
	 A copy of the Orders entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Orders entity to update
	 @return Orders the persisted Orders entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public Orders update(Orders entity);
	public Orders findById( Integer id);
	 /**
	 * Find all Orders entities with a specific property value.  
	 
	  @param propertyName the name of the Orders property to query
	  @param value the property value to match
	  	  @return List<Orders> found by query
	 */
	public List<Orders> findByProperty(String propertyName, Object value
		);
	public List<Orders> findByOCId(Object OCId
		);
	public List<Orders> findByOSubTotal(Object OSubTotal
		);
	public List<Orders> findByOTax(Object OTax
		);
	public List<Orders> findByOTotal(Object OTotal
		);
	public List<Orders> findByOShipType(Object OShipType
		);
	public List<Orders> findByOBillAddrId(Object OBillAddrId
		);
	public List<Orders> findByOShipAddrId(Object OShipAddrId
		);
	public List<Orders> findByOStatus(Object OStatus
		);
	/**
	 * Find all Orders entities.
	  	  @return List<Orders> all Orders entities
	 */
	public List<Orders> findAll(
		);	
}