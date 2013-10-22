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
import java.util.List;
import javax.ejb.Local;

/**
 * Local interface for ShoppingCartFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface ShoppingCartFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved ShoppingCart entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity ShoppingCart entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(ShoppingCart entity);
    /**
	 Delete a persistent ShoppingCart entity.
	  @param entity ShoppingCart entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(ShoppingCart entity);
   /**
	 Persist a previously saved ShoppingCart entity and return it or a copy of it to the sender. 
	 A copy of the ShoppingCart entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity ShoppingCart entity to update
	 @return ShoppingCart the persisted ShoppingCart entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public ShoppingCart update(ShoppingCart entity);
	public ShoppingCart findById( Integer id);
	 /**
	 * Find all ShoppingCart entities with a specific property value.  
	 
	  @param propertyName the name of the ShoppingCart property to query
	  @param value the property value to match
	  	  @return List<ShoppingCart> found by query
	 */
	public List<ShoppingCart> findByProperty(String propertyName, Object value
		);
	/**
	 * Find all ShoppingCart entities.
	  	  @return List<ShoppingCart> all ShoppingCart entities
	 */
	public List<ShoppingCart> findAll(
		);
	public int createEmptyCart();	
}