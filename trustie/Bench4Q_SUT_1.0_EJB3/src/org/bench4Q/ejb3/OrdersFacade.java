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
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity Orders.
 * @see org.bench4q.ejb3.Orders
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class OrdersFacade  implements OrdersFacadeLocal {
	//property constants
	public static final String _OCID = "OCId";
	public static final String _OSUB_TOTAL = "OSubTotal";
	public static final String _OTAX = "OTax";
	public static final String _OTOTAL = "OTotal";
	public static final String _OSHIP_TYPE = "OShipType";
	public static final String _OBILL_ADDR_ID = "OBillAddrId";
	public static final String _OSHIP_ADDR_ID = "OShipAddrId";
	public static final String _OSTATUS = "OStatus";

	public final static String JNDI_NAME = OrdersFacade.class.getName()        
    + "_" + OrdersFacadeLocal.class.getName() + "@Local";


    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved Orders entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Orders entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Orders entity) {
    				LogUtil.log("saving Orders instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent Orders entity.
	  @param entity Orders entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Orders entity) {
    				LogUtil.log("deleting Orders instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(Orders.class, entity.getOId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved Orders entity and return it or a copy of it to the sender. 
	 A copy of the Orders entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Orders entity to update
	 @return Orders the persisted Orders entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public Orders update(Orders entity) {
    				LogUtil.log("updating Orders instance", Level.INFO, null);
	        try {
            Orders result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public Orders findById( Integer id) {
    				LogUtil.log("finding Orders instance with id: " + id, Level.INFO, null);
	        try {
            Orders instance = entityManager.find(Orders.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all Orders entities with a specific property value.  
	 
	  @param propertyName the name of the Orders property to query
	  @param value the property value to match
	  	  @return List<Orders> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<Orders> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding Orders instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from Orders model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<Orders> findByOCId(Object OCId
	) {
		return findByProperty(_OCID, OCId
		);
	}
	
	public List<Orders> findByOSubTotal(Object OSubTotal
	) {
		return findByProperty(_OSUB_TOTAL, OSubTotal
		);
	}
	
	public List<Orders> findByOTax(Object OTax
	) {
		return findByProperty(_OTAX, OTax
		);
	}
	
	public List<Orders> findByOTotal(Object OTotal
	) {
		return findByProperty(_OTOTAL, OTotal
		);
	}
	
	public List<Orders> findByOShipType(Object OShipType
	) {
		return findByProperty(_OSHIP_TYPE, OShipType
		);
	}
	
	public List<Orders> findByOBillAddrId(Object OBillAddrId
	) {
		return findByProperty(_OBILL_ADDR_ID, OBillAddrId
		);
	}
	
	public List<Orders> findByOShipAddrId(Object OShipAddrId
	) {
		return findByProperty(_OSHIP_ADDR_ID, OShipAddrId
		);
	}
	
	public List<Orders> findByOStatus(Object OStatus
	) {
		return findByProperty(_OSTATUS, OStatus
		);
	}
	
	
	/**
	 * Find all Orders entities.
	  	  @return List<Orders> all Orders entities
	 */
	@SuppressWarnings("unchecked")
	public List<Orders> findAll(
		) {
					LogUtil.log("finding all Orders instances", Level.INFO, null);
			try {
			final String queryString = "select model from Orders model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}