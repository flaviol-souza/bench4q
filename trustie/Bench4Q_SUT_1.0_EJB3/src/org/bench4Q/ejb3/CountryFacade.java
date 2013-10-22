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
 * Facade for entity Country.
 * @see org.bench4q.ejb3.Country
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class CountryFacade  implements CountryFacadeLocal {
	//property constants
	public static final String CO_NAME = "coName";
	public static final String CO_EXCHANGE = "coExchange";
	public static final String CO_CURRENCY = "coCurrency";

	public final static String JNDI_NAME = CountryFacade.class.getName()        
    + "_" + CountryFacadeLocal.class.getName() + "@Local";




    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved Country entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Country entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Country entity) {
    				LogUtil.log("saving Country instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent Country entity.
	  @param entity Country entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Country entity) {
    				LogUtil.log("deleting Country instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(Country.class, entity.getCoId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved Country entity and return it or a copy of it to the sender. 
	 A copy of the Country entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Country entity to update
	 @return Country the persisted Country entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public Country update(Country entity) {
    				LogUtil.log("updating Country instance", Level.INFO, null);
	        try {
            Country result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public Country findById( Integer id) {
    				LogUtil.log("finding Country instance with id: " + id, Level.INFO, null);
	        try {
            Country instance = entityManager.find(Country.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all Country entities with a specific property value.  
	 
	  @param propertyName the name of the Country property to query
	  @param value the property value to match
	  	  @return List<Country> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<Country> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding Country instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from Country model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<Country> findByCoName(Object coName
	) {
		return findByProperty(CO_NAME, coName
		);
	}
	
	public List<Country> findByCoExchange(Object coExchange
	) {
		return findByProperty(CO_EXCHANGE, coExchange
		);
	}
	
	public List<Country> findByCoCurrency(Object coCurrency
	) {
		return findByProperty(CO_CURRENCY, coCurrency
		);
	}
	
	
	/**
	 * Find all Country entities.
	  	  @return List<Country> all Country entities
	 */
	@SuppressWarnings("unchecked")
	public List<Country> findAll(
		) {
					LogUtil.log("finding all Country instances", Level.INFO, null);
			try {
			final String queryString = "select model from Country model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}