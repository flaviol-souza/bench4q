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
 * Local interface for CcXactsFacade.
 * @author MyEclipse Persistence Tools
 */
@Local

public interface CcXactsFacadeLocal {
		/**
	 Perform an initial save of a previously unsaved CcXacts entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity CcXacts entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(CcXacts entity);
    /**
	 Delete a persistent CcXacts entity.
	  @param entity CcXacts entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(CcXacts entity);
   /**
	 Persist a previously saved CcXacts entity and return it or a copy of it to the sender. 
	 A copy of the CcXacts entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity CcXacts entity to update
	 @return CcXacts the persisted CcXacts entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
	public CcXacts update(CcXacts entity);
	public CcXacts findById( Integer id);
	 /**
	 * Find all CcXacts entities with a specific property value.  
	 
	  @param propertyName the name of the CcXacts property to query
	  @param value the property value to match
	  	  @return List<CcXacts> found by query
	 */
	public List<CcXacts> findByProperty(String propertyName, Object value
		);
	public List<CcXacts> findByCxType(Object cxType
		);
	public List<CcXacts> findByCxNum(Object cxNum
		);
	public List<CcXacts> findByCxName(Object cxName
		);
	public List<CcXacts> findByCxAuthId(Object cxAuthId
		);
	public List<CcXacts> findByCxXactAmt(Object cxXactAmt
		);
	public List<CcXacts> findByCxCoId(Object cxCoId
		);
	/**
	 * Find all CcXacts entities.
	  	  @return List<CcXacts> all CcXacts entities
	 */
	public List<CcXacts> findAll(
		);	
}