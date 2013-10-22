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
 * Facade for entity CcXacts.
 * @see org.bench4q.ejb3.CcXacts
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class CcXactsFacade  implements CcXactsFacadeLocal {
	//property constants
	public static final String CX_TYPE = "cxType";
	public static final String CX_NUM = "cxNum";
	public static final String CX_NAME = "cxName";
	public static final String CX_AUTH_ID = "cxAuthId";
	public static final String CX_XACT_AMT = "cxXactAmt";
	public static final String CX_CO_ID = "cxCoId";


	public final static String JNDI_NAME = CcXactsFacade.class.getName()        
    + "_" + CcXactsFacadeLocal.class.getName() + "@Local";



    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved CcXacts entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity CcXacts entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(CcXacts entity) {
    				LogUtil.log("saving CcXacts instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent CcXacts entity.
	  @param entity CcXacts entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(CcXacts entity) {
    				LogUtil.log("deleting CcXacts instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(CcXacts.class, entity.getCxOId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved CcXacts entity and return it or a copy of it to the sender. 
	 A copy of the CcXacts entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity CcXacts entity to update
	 @return CcXacts the persisted CcXacts entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public CcXacts update(CcXacts entity) {
    				LogUtil.log("updating CcXacts instance", Level.INFO, null);
	        try {
            CcXacts result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public CcXacts findById( Integer id) {
    				LogUtil.log("finding CcXacts instance with id: " + id, Level.INFO, null);
	        try {
            CcXacts instance = entityManager.find(CcXacts.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all CcXacts entities with a specific property value.  
	 
	  @param propertyName the name of the CcXacts property to query
	  @param value the property value to match
	  	  @return List<CcXacts> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<CcXacts> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding CcXacts instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from CcXacts model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<CcXacts> findByCxType(Object cxType
	) {
		return findByProperty(CX_TYPE, cxType
		);
	}
	
	public List<CcXacts> findByCxNum(Object cxNum
	) {
		return findByProperty(CX_NUM, cxNum
		);
	}
	
	public List<CcXacts> findByCxName(Object cxName
	) {
		return findByProperty(CX_NAME, cxName
		);
	}
	
	public List<CcXacts> findByCxAuthId(Object cxAuthId
	) {
		return findByProperty(CX_AUTH_ID, cxAuthId
		);
	}
	
	public List<CcXacts> findByCxXactAmt(Object cxXactAmt
	) {
		return findByProperty(CX_XACT_AMT, cxXactAmt
		);
	}
	
	public List<CcXacts> findByCxCoId(Object cxCoId
	) {
		return findByProperty(CX_CO_ID, cxCoId
		);
	}
	
	
	/**
	 * Find all CcXacts entities.
	  	  @return List<CcXacts> all CcXacts entities
	 */
	@SuppressWarnings("unchecked")
	public List<CcXacts> findAll(
		) {
					LogUtil.log("finding all CcXacts instances", Level.INFO, null);
			try {
			final String queryString = "select model from CcXacts model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}