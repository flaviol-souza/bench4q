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
 * Facade for entity Author.
 * @see org.bench4q.ejb3.Author
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class AuthorFacade  implements AuthorFacadeLocal {
	//property constants
	public static final String _AFNAME = "AFname";
	public static final String _ALNAME = "ALname";
	public static final String _AMNAME = "AMname";
	public static final String _ABIO = "ABio";
	public final static String JNDI_NAME = AuthorFacade.class.getName()        
    + "_" + AuthorFacadeLocal.class.getName() + "@Local";




    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved Author entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Author entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Author entity) {
    				LogUtil.log("saving Author instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent Author entity.
	  @param entity Author entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Author entity) {
    				LogUtil.log("deleting Author instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(Author.class, entity.getAId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved Author entity and return it or a copy of it to the sender. 
	 A copy of the Author entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Author entity to update
	 @return Author the persisted Author entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public Author update(Author entity) {
    				LogUtil.log("updating Author instance", Level.INFO, null);
	        try {
            Author result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public Author findById( Integer id) {
    				LogUtil.log("finding Author instance with id: " + id, Level.INFO, null);
	        try {
            Author instance = entityManager.find(Author.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all Author entities with a specific property value.  
	 
	  @param propertyName the name of the Author property to query
	  @param value the property value to match
	  	  @return List<Author> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<Author> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding Author instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from Author model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<Author> findByAFname(Object AFname
	) {
		return findByProperty(_AFNAME, AFname
		);
	}
	
	public List<Author> findByALname(Object ALname
	) {
		return findByProperty(_ALNAME, ALname
		);
	}
	
	public List<Author> findByAMname(Object AMname
	) {
		return findByProperty(_AMNAME, AMname
		);
	}
	
	public List<Author> findByABio(Object ABio
	) {
		return findByProperty(_ABIO, ABio
		);
	}
	
	
	/**
	 * Find all Author entities.
	  	  @return List<Author> all Author entities
	 */
	@SuppressWarnings("unchecked")
	public List<Author> findAll(
		) {
					LogUtil.log("finding all Author instances", Level.INFO, null);
			try {
			final String queryString = "select model from Author model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}