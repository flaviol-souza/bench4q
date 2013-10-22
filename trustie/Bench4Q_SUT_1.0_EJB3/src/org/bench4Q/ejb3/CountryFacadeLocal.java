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
 * Local interface for CountryFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface CountryFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved Country entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Country entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Country entity);
    /**
	 Delete a persistent Country entity.
	  @param entity Country entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Country entity);
   /**
	 Persist a previously saved Country entity and return it or a copy of it to the sender. 
	 A copy of the Country entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Country entity to update
	 @return Country the persisted Country entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public Country update(Country entity);
	public Country findById( Integer id);
	 /**
	 * Find all Country entities with a specific property value.  
	 
	  @param propertyName the name of the Country property to query
	  @param value the property value to match
	  	  @return List<Country> found by query
	 */
	public List<Country> findByProperty(String propertyName, Object value
		);
	public List<Country> findByCoName(Object coName
		);
	public List<Country> findByCoExchange(Object coExchange
		);
	public List<Country> findByCoCurrency(Object coCurrency
		);
	/**
	 * Find all Country entities.
	  	  @return List<Country> all Country entities
	 */
	public List<Country> findAll(
		);	
}