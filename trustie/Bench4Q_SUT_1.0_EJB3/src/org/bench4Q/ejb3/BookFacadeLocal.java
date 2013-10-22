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
 * Local interface for ItemFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface BookFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved Item entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Item entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Item entity);
    /**
	 Delete a persistent Item entity.
	  @param entity Item entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Item entity);
   /**
	 Persist a previously saved Item entity and return it or a copy of it to the sender. 
	 A copy of the Item entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Item entity to update
	 @return Item the persisted Item entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public Item update(Item entity);
	public Item findById( Integer id);
	 /**
	 * Find all Item entities with a specific property value.  
	 
	  @param propertyName the name of the Item property to query
	  @param value the property value to match
	  	  @return List<Item> found by query
	 */
	public List<Item> findByProperty(String propertyName, Object value
		);
	public List<Item> findByITitle(Object ITitle
		);
	public List<Item> findByIAId(Object IAId
		);
	public List<Item> findByIPublisher(Object IPublisher
		);
	public List<Item> findByISubject(Object ISubject
		);
	public List<Item> findByIDesc(Object IDesc
		);
	public List<Item> findByIRelated1(Object IRelated1
		);
	public List<Item> findByIRelated2(Object IRelated2
		);
	public List<Item> findByIRelated3(Object IRelated3
		);
	public List<Item> findByIRelated4(Object IRelated4
		);
	public List<Item> findByIRelated5(Object IRelated5
		);
	public List<Item> findByIThumbnail(Object IThumbnail
		);
	public List<Item> findByIImage(Object IImage
		);
	public List<Item> findByISrp(Object ISrp
		);
	public List<Item> findByICost(Object ICost
		);
	public List<Item> findByIStock(Object IStock
		);
	public List<Item> findByIIsbn(Object IIsbn
		);
	public List<Item> findByIPage(Object IPage
		);
	public List<Item> findByIBacking(Object IBacking
		);
	public List<Item> findByIDimensions(Object IDimensions
		);
	/**
	 * Find all Item entities.
	  	  @return List<Item> all Item entities
	 */
	public List<Item> findAll(
		);	
}