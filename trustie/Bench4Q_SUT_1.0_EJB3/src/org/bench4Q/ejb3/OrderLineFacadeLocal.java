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
 * Local interface for OrderLineFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface OrderLineFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved OrderLine entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity OrderLine entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(OrderLine entity);
    /**
	 Delete a persistent OrderLine entity.
	  @param entity OrderLine entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(OrderLine entity);
   /**
	 Persist a previously saved OrderLine entity and return it or a copy of it to the sender. 
	 A copy of the OrderLine entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity OrderLine entity to update
	 @return OrderLine the persisted OrderLine entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public OrderLine update(OrderLine entity);
	public OrderLine findById( OrderLineId id);
	 /**
	 * Find all OrderLine entities with a specific property value.  
	 
	  @param propertyName the name of the OrderLine property to query
	  @param value the property value to match
	  	  @return List<OrderLine> found by query
	 */
	public List<OrderLine> findByProperty(String propertyName, Object value
		);
	public List<OrderLine> findByOlIId(Object olIId
		);
	public List<OrderLine> findByOlQty(Object olQty
		);
	public List<OrderLine> findByOlDiscount(Object olDiscount
		);
	public List<OrderLine> findByOlComments(Object olComments
		);
	/**
	 * Find all OrderLine entities.
	  	  @return List<OrderLine> all OrderLine entities
	 */
	public List<OrderLine> findAll(
		);	
}