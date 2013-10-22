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
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity ShoppingCart.
 * @see org.bench4q.ejb3.ShoppingCart
  * @author MyEclipse Persistence Tools 
 */
@Stateless
public class ShoppingCartFacade  implements ShoppingCartFacadeLocal {
	//property constants

	public final static String JNDI_NAME = ShoppingCartFacade.class.getName()        
    + "_" + ShoppingCartFacadeLocal.class.getName() + "@Local";



    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved ShoppingCart entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity ShoppingCart entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(ShoppingCart entity) {
    				LogUtil.log("saving ShoppingCart instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent ShoppingCart entity.
	  @param entity ShoppingCart entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(ShoppingCart entity) {
    				LogUtil.log("deleting ShoppingCart instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(ShoppingCart.class, entity.getScId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved ShoppingCart entity and return it or a copy of it to the sender. 
	 A copy of the ShoppingCart entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity ShoppingCart entity to update
	 @return ShoppingCart the persisted ShoppingCart entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public ShoppingCart update(ShoppingCart entity) {
    				LogUtil.log("updating ShoppingCart instance", Level.INFO, null);
	        try {
            ShoppingCart result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public ShoppingCart findById( Integer id) {
    				LogUtil.log("finding ShoppingCart instance with id: " + id, Level.INFO, null);
	        try {
            ShoppingCart instance = entityManager.find(ShoppingCart.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all ShoppingCart entities with a specific property value.  
	 
	  @param propertyName the name of the ShoppingCart property to query
	  @param value the property value to match
	  	  @return List<ShoppingCart> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<ShoppingCart> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding ShoppingCart instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from ShoppingCart model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	
	/**
	 * Find all ShoppingCart entities.
	  	  @return List<ShoppingCart> all ShoppingCart entities
	 */
	@SuppressWarnings("unchecked")
	public List<ShoppingCart> findAll(
		) {
					LogUtil.log("finding all ShoppingCart instances", Level.INFO, null);
			try {
			final String queryString = "select model from ShoppingCart model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}

	public int createEmptyCart() {
		//TODO:gain max CartId and insert a cart
		/*Query query = entityManager.createNamedQuery("getMaxCartId");
		List idList = query.getResultList();
		int maxId = (Integer)idList.get(0);*/
		Timestamp timeStamp = new Timestamp(new java.util.Date().getTime());
		ShoppingCart newSC = new ShoppingCart(null, timeStamp);
		save(newSC);
		return newSC.getScId();
	}	
}