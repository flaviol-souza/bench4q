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
 * Facade for entity Customer.
 * 
 * @see org.bench4q.ejb3.Customer
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CustomerFacade implements CustomerFacadeLocal {
	// property constants
	public static final String _CUNAME = "CUname";
	public static final String _CPASSWD = "CPasswd";
	public static final String _CFNAME = "CFname";
	public static final String _CLNAME = "CLname";
	public static final String _CADDR_ID = "CAddrId";
	public static final String _CPHONE = "CPhone";
	public static final String _CEMAIL = "CEmail";
	public static final String _CDISCOUNT = "CDiscount";
	public static final String _CBALANCE = "CBalance";
	public static final String _CYTD_PMT = "CYtdPmt";
	public static final String _CDATA = "CData";

	public final static String JNDI_NAME = CustomerFacade.class.getName() + "_"
			+ CustomerFacadeLocal.class.getName() + "@Local";

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved Customer entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            Customer entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Customer entity) {
		LogUtil.log("saving Customer instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Customer entity.
	 * 
	 * @param entity
	 *            Customer entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Customer entity) {
		LogUtil.log("deleting Customer instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(Customer.class, entity.getCId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Customer entity and return it or a copy of it
	 * to the sender. A copy of the Customer entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            Customer entity to update
	 * @return Customer the persisted Customer entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Customer update(Customer entity) {
		LogUtil.log("updating Customer instance", Level.INFO, null);
		try {
			Customer result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Customer findById(Integer id) {
		LogUtil.log("finding Customer instance with id: " + id, Level.INFO,
				null);
		try {
			Customer instance = entityManager.find(Customer.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all Customer entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Customer property to query
	 * @param value
	 *            the property value to match
	 * @return List<Customer> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding Customer instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Customer model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<Customer> findByCUname(Object CUname) {
		return findByProperty(_CUNAME, CUname);
	}

	public List<Customer> findByCPasswd(Object CPasswd) {
		return findByProperty(_CPASSWD, CPasswd);
	}

	public List<Customer> findByCFname(Object CFname) {
		return findByProperty(_CFNAME, CFname);
	}

	public List<Customer> findByCLname(Object CLname) {
		return findByProperty(_CLNAME, CLname);
	}

	public List<Customer> findByCAddrId(Object CAddrId) {
		return findByProperty(_CADDR_ID, CAddrId);
	}

	public List<Customer> findByCPhone(Object CPhone) {
		return findByProperty(_CPHONE, CPhone);
	}

	public List<Customer> findByCEmail(Object CEmail) {
		return findByProperty(_CEMAIL, CEmail);
	}

	public List<Customer> findByCDiscount(Object CDiscount) {
		return findByProperty(_CDISCOUNT, CDiscount);
	}

	public List<Customer> findByCBalance(Object CBalance) {
		return findByProperty(_CBALANCE, CBalance);
	}

	public List<Customer> findByCYtdPmt(Object CYtdPmt) {
		return findByProperty(_CYTD_PMT, CYtdPmt);
	}

	public List<Customer> findByCData(Object CData) {
		return findByProperty(_CDATA, CData);
	}

	/**
	 * Find all Customer entities.
	 * 
	 * @return List<Customer> all Customer entities
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> findAll() {
		LogUtil.log("finding all Customer instances", Level.INFO, null);
		try {
			final String queryString = "select model from Customer model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * first implement as this, to get better performance use Namedquery in the
	 * future
	 */
	public String getUserName(int cID) {
		Customer cus = findById(cID);
		return cus.getCUname();
	}

	public String getPassword(String cUNAME) {
		List<Customer> cus = findByCUname(cUNAME);
		if (cus != null && cus.size() > 0)
			return cus.get(0).getCPasswd();
		return null;
	}

	public void refreshSession(int cID) {
		/*
		 * session = HibernateSessionFactory.getSession(); tx =
		 * session.beginTransaction();
		 * 
		 * Query query = session.createQuery(
		 * "update Customer customer set customer.CLogin=?, customer.CExpiration=? where customer.CId=?"
		 * );
		 * 
		 * // query.setCacheable(true);
		 * 
		 * java.util.Date now = new java.util.Date(); java.util.Calendar
		 * cal=java.util.Calendar.getInstance(); cal.setTime(now);
		 * cal.add(java.util.Calendar.HOUR_OF_DAY,2);
		 * 
		 * query.setTimestamp(0, now); query.setTimestamp(1, cal.getTime());
		 * query.setInteger(2, C_ID);
		 * 
		 * query.executeUpdate();
		 * 
		 * tx.commit();
		 */

		java.util.Date now = new java.util.Date();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(now);
		cal.add(java.util.Calendar.HOUR_OF_DAY, 2);

		Customer cus = findById(cID);
		cus.setCLogin(new Timestamp(now.getTime()));
		cus.setCExpiration(new Timestamp(cal.getTimeInMillis()));

		entityManager.merge(cus);
	}

	public double getCDiscount(int cID) {
		Customer customer = findById(cID);
		if (customer != null)
			return customer.getCDiscount();
		else {
			return 0.0;
		}
	}

	public int getCAddrId(int customerId) {
		Customer customer = findById(customerId);
		if (customer != null)
			return customer.getCAddrId();
		else {
			throw new RuntimeException("Customer not found");
		}
	}

	public List getAllIds() {
		Query getAllCustomerIds = entityManager
				.createNamedQuery("getAllCustomerIds");

		return getAllCustomerIds.getResultList();
	}
}