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
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity ShoppingCartLine.
 * @see org.bench4q.ejb3.ShoppingCartLine
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class ShoppingCartLineFacade  implements ShoppingCartLineFacadeLocal {
	//property constants
	public static final String SCL_QTY = "sclQty";


	public final static String JNDI_NAME = ShoppingCartLineFacade.class.getName()        
    + "_" + ShoppingCartLineFacadeLocal.class.getName() + "@Local";


    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved ShoppingCartLine entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity ShoppingCartLine entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(ShoppingCartLine entity) {
    				LogUtil.log("saving ShoppingCartLine instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent ShoppingCartLine entity.
	  @param entity ShoppingCartLine entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(ShoppingCartLine entity) {
    				LogUtil.log("deleting ShoppingCartLine instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(ShoppingCartLine.class, entity.getId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved ShoppingCartLine entity and return it or a copy of it to the sender. 
	 A copy of the ShoppingCartLine entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity ShoppingCartLine entity to update
	 @return ShoppingCartLine the persisted ShoppingCartLine entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public ShoppingCartLine update(ShoppingCartLine entity) {
    				LogUtil.log("updating ShoppingCartLine instance", Level.INFO, null);
	        try {
            ShoppingCartLine result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public ShoppingCartLine findById( ShoppingCartLineId id) {
    				LogUtil.log("finding ShoppingCartLine instance with id: " + id, Level.INFO, null);
	        try {
            ShoppingCartLine instance = entityManager.find(ShoppingCartLine.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all ShoppingCartLine entities with a specific property value.  
	 
	  @param propertyName the name of the ShoppingCartLine property to query
	  @param value the property value to match
	  	  @return List<ShoppingCartLine> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<ShoppingCartLine> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding ShoppingCartLine instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from ShoppingCartLine model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<ShoppingCartLine> findBySclQty(Object sclQty
	) {
		return findByProperty(SCL_QTY, sclQty
		);
	}
	
	
	/**
	 * Find all ShoppingCartLine entities.
	  	  @return List<ShoppingCartLine> all ShoppingCartLine entities
	 */
	@SuppressWarnings("unchecked")
	public List<ShoppingCartLine> findAll(
		) {
					LogUtil.log("finding all ShoppingCartLine instances", Level.INFO, null);
			try {
			final String queryString = "select model from ShoppingCartLine model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}