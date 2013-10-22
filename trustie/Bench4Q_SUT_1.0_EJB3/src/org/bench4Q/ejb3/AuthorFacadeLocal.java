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
 * Local interface for AuthorFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface AuthorFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved Author entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Author entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Author entity);
    /**
	 Delete a persistent Author entity.
	  @param entity Author entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Author entity);
   /**
	 Persist a previously saved Author entity and return it or a copy of it to the sender. 
	 A copy of the Author entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Author entity to update
	 @return Author the persisted Author entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public Author update(Author entity);
	public Author findById( Integer id);
	 /**
	 * Find all Author entities with a specific property value.  
	 
	  @param propertyName the name of the Author property to query
	  @param value the property value to match
	  	  @return List<Author> found by query
	 */
	public List<Author> findByProperty(String propertyName, Object value
		);
	public List<Author> findByAFname(Object AFname
		);
	public List<Author> findByALname(Object ALname
		);
	public List<Author> findByAMname(Object AMname
		);
	public List<Author> findByABio(Object ABio
		);
	/**
	 * Find all Author entities.
	  	  @return List<Author> all Author entities
	 */
	public List<Author> findAll(
		);	
}