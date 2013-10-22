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
 * Facade for entity OrderLine.
 * @see org.bench4q.ejb3.OrderLine
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class OrderLineFacade  implements OrderLineFacadeLocal {
	//property constants
	public static final String OL_IID = "olIId";
	public static final String OL_QTY = "olQty";
	public static final String OL_DISCOUNT = "olDiscount";
	public static final String OL_COMMENTS = "olComments";


	public final static String JNDI_NAME = OrderLineFacade.class.getName()        
    + "_" + OrderLineFacadeLocal.class.getName() + "@Local";


    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved OrderLine entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity OrderLine entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(OrderLine entity) {
    				LogUtil.log("saving OrderLine instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent OrderLine entity.
	  @param entity OrderLine entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(OrderLine entity) {
    				LogUtil.log("deleting OrderLine instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(OrderLine.class, entity.getId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved OrderLine entity and return it or a copy of it to the sender. 
	 A copy of the OrderLine entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity OrderLine entity to update
	 @return OrderLine the persisted OrderLine entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public OrderLine update(OrderLine entity) {
    				LogUtil.log("updating OrderLine instance", Level.INFO, null);
	        try {
            OrderLine result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public OrderLine findById( OrderLineId id) {
    				LogUtil.log("finding OrderLine instance with id: " + id, Level.INFO, null);
	        try {
            OrderLine instance = entityManager.find(OrderLine.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all OrderLine entities with a specific property value.  
	 
	  @param propertyName the name of the OrderLine property to query
	  @param value the property value to match
	  	  @return List<OrderLine> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<OrderLine> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding OrderLine instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from OrderLine model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<OrderLine> findByOlIId(Object olIId
	) {
		return findByProperty(OL_IID, olIId
		);
	}
	
	public List<OrderLine> findByOlQty(Object olQty
	) {
		return findByProperty(OL_QTY, olQty
		);
	}
	
	public List<OrderLine> findByOlDiscount(Object olDiscount
	) {
		return findByProperty(OL_DISCOUNT, olDiscount
		);
	}
	
	public List<OrderLine> findByOlComments(Object olComments
	) {
		return findByProperty(OL_COMMENTS, olComments
		);
	}
	
	
	/**
	 * Find all OrderLine entities.
	  	  @return List<OrderLine> all OrderLine entities
	 */
	@SuppressWarnings("unchecked")
	public List<OrderLine> findAll(
		) {
					LogUtil.log("finding all OrderLine instances", Level.INFO, null);
			try {
			final String queryString = "select model from OrderLine model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}