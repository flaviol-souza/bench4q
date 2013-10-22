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
 * Facade for entity Item.
 * @see org.Item.ejb3.Item
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class BookFacade  implements BookFacadeLocal {
	//property constants
	public static final String _ITITLE = "ITitle";
	public static final String _IAID = "IAId";
	public static final String _IPUBLISHER = "IPublisher";
	public static final String _ISUBJECT = "ISubject";
	public static final String _IDESC = "IDesc";
	public static final String _IRELATED1 = "IRelated1";
	public static final String _IRELATED2 = "IRelated2";
	public static final String _IRELATED3 = "IRelated3";
	public static final String _IRELATED4 = "IRelated4";
	public static final String _IRELATED5 = "IRelated5";
	public static final String _ITHUMBNAIL = "IThumbnail";
	public static final String _IIMAGE = "IImage";
	public static final String _ISRP = "ISrp";
	public static final String _ICOST = "ICost";
	public static final String _ISTOCK = "IStock";
	public static final String _IISBN = "IIsbn";
	public static final String _IPAGE = "IPage";
	public static final String _IBACKING = "IBacking";
	public static final String _IDIMENSIONS = "IDimensions";


	public final static String JNDI_NAME = BookFacade.class.getName()        
    + "_" + BookFacadeLocal.class.getName() + "@Local";



    @PersistenceContext private EntityManager entityManager;
	
		/**
	 Perform an initial save of a previously unsaved Item entity. 
	 All subsequent persist actions of this entity should use the #update() method.
	  @param entity Item entity to persist
	  @throws RuntimeException when the operation fails
	 */
    public void save(Item entity) {
    				LogUtil.log("saving Item instance", Level.INFO, null);
	        try {
            entityManager.persist(entity);
            			LogUtil.log("save successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("save failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Delete a persistent Item entity.
	  @param entity Item entity to delete
	 @throws RuntimeException when the operation fails
	 */
    public void delete(Item entity) {
    				LogUtil.log("deleting Item instance", Level.INFO, null);
	        try {
        	entity = entityManager.getReference(Item.class, entity.getIId());
            entityManager.remove(entity);
            			LogUtil.log("delete successful", Level.INFO, null);
	        } catch (RuntimeException re) {
        				LogUtil.log("delete failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    /**
	 Persist a previously saved Item entity and return it or a copy of it to the sender. 
	 A copy of the Item entity parameter is returned when the JPA persistence mechanism has not previously been tracking the updated entity. 
	  @param entity Item entity to update
	 @return Item the persisted Item entity instance, may not be the same
	 @throws RuntimeException if the operation fails
	 */
    public Item update(Item entity) {
    				LogUtil.log("updating Item instance", Level.INFO, null);
	        try {
            Item result = entityManager.merge(entity);
            			LogUtil.log("update successful", Level.INFO, null);
	            return result;
        } catch (RuntimeException re) {
        				LogUtil.log("update failed", Level.SEVERE, re);
	            throw re;
        }
    }
    
    public Item findById( Integer id) {
    				LogUtil.log("finding Item instance with id: " + id, Level.INFO, null);
	        try {
            Item instance = entityManager.find(Item.class, id);
            return instance;
        } catch (RuntimeException re) {
        				LogUtil.log("find failed", Level.SEVERE, re);
	            throw re;
        }
    }    
    

/**
	 * Find all Item entities with a specific property value.  
	 
	  @param propertyName the name of the Item property to query
	  @param value the property value to match
	  	  @return List<Item> found by query
	 */
    @SuppressWarnings("unchecked")
    public List<Item> findByProperty(String propertyName, final Object value
        ) {
    				LogUtil.log("finding Item instance with property: " + propertyName + ", value: " + value, Level.INFO, null);
			try {
			final String queryString = "select model from Item model where model." 
			 						+ propertyName + "= :propertyValue";
								Query query = entityManager.createQuery(queryString);
					query.setParameter("propertyValue", value);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find by property name failed", Level.SEVERE, re);
				throw re;
		}
	}			
	public List<Item> findByITitle(Object ITitle
	) {
		return findByProperty(_ITITLE, ITitle
		);
	}
	
	public List<Item> findByIAId(Object IAId
	) {
		return findByProperty(_IAID, IAId
		);
	}
	
	public List<Item> findByIPublisher(Object IPublisher
	) {
		return findByProperty(_IPUBLISHER, IPublisher
		);
	}
	
	public List<Item> findByISubject(Object ISubject
	) {
		return findByProperty(_ISUBJECT, ISubject
		);
	}
	
	public List<Item> findByIDesc(Object IDesc
	) {
		return findByProperty(_IDESC, IDesc
		);
	}
	
	public List<Item> findByIRelated1(Object IRelated1
	) {
		return findByProperty(_IRELATED1, IRelated1
		);
	}
	
	public List<Item> findByIRelated2(Object IRelated2
	) {
		return findByProperty(_IRELATED2, IRelated2
		);
	}
	
	public List<Item> findByIRelated3(Object IRelated3
	) {
		return findByProperty(_IRELATED3, IRelated3
		);
	}
	
	public List<Item> findByIRelated4(Object IRelated4
	) {
		return findByProperty(_IRELATED4, IRelated4
		);
	}
	
	public List<Item> findByIRelated5(Object IRelated5
	) {
		return findByProperty(_IRELATED5, IRelated5
		);
	}
	
	public List<Item> findByIThumbnail(Object IThumbnail
	) {
		return findByProperty(_ITHUMBNAIL, IThumbnail
		);
	}
	
	public List<Item> findByIImage(Object IImage
	) {
		return findByProperty(_IIMAGE, IImage
		);
	}
	
	public List<Item> findByISrp(Object ISrp
	) {
		return findByProperty(_ISRP, ISrp
		);
	}
	
	public List<Item> findByICost(Object ICost
	) {
		return findByProperty(_ICOST, ICost
		);
	}
	
	public List<Item> findByIStock(Object IStock
	) {
		return findByProperty(_ISTOCK, IStock
		);
	}
	
	public List<Item> findByIIsbn(Object IIsbn
	) {
		return findByProperty(_IISBN, IIsbn
		);
	}
	
	public List<Item> findByIPage(Object IPage
	) {
		return findByProperty(_IPAGE, IPage
		);
	}
	
	public List<Item> findByIBacking(Object IBacking
	) {
		return findByProperty(_IBACKING, IBacking
		);
	}
	
	public List<Item> findByIDimensions(Object IDimensions
	) {
		return findByProperty(_IDIMENSIONS, IDimensions
		);
	}
	
	
	/**
	 * Find all Item entities.
	  	  @return List<Item> all Item entities
	 */
	@SuppressWarnings("unchecked")
	public List<Item> findAll(
		) {
					LogUtil.log("finding all Item instances", Level.INFO, null);
			try {
			final String queryString = "select model from Item model";
								Query query = entityManager.createQuery(queryString);
					return query.getResultList();
		} catch (RuntimeException re) {
						LogUtil.log("find all failed", Level.SEVERE, re);
				throw re;
		}
	}
	
}