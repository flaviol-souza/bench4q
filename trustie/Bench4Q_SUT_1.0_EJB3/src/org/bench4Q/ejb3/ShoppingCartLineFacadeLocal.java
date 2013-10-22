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
 * Local interface for ShoppingCartLineFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface ShoppingCartLineFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved ShoppingCartLine entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity ShoppingCartLine entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(ShoppingCartLine entity);
    /**
	 Delete a persistent ShoppingCartLine entity.
	  @param entity ShoppingCartLine entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(ShoppingCartLine entity);
   /**
	 Persist a previously saved ShoppingCartLine entity and return it or a copy of it to the sender. 
	 A copy of the ShoppingCartLine entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity ShoppingCartLine entity to update
	 @return ShoppingCartLine the persisted ShoppingCartLine entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public ShoppingCartLine update(ShoppingCartLine entity);
	public ShoppingCartLine findById( ShoppingCartLineId id);
	 /**
	 * Find all ShoppingCartLine entities with a specific property value.  
	 
	  @param propertyName the name of the ShoppingCartLine property to query
	  @param value the property value to match
	  	  @return List<ShoppingCartLine> found by query
	 */
	public List<ShoppingCartLine> findByProperty(String propertyName, Object value
		);
	public List<ShoppingCartLine> findBySclQty(Object sclQty
		);
	/**
	 * Find all ShoppingCartLine entities.
	  	  @return List<ShoppingCartLine> all ShoppingCartLine entities
	 */
	public List<ShoppingCartLine> findAll(
		);	
}