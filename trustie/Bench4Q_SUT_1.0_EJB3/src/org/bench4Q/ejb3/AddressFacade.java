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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity Address.
 * 
 * @see org.bench4q.ejb3.Address
 * @author MyEclipse Persistence Tools
 * @a
 */
@Stateless
public class AddressFacade implements AddressFacadeLocal {
	// property constants
	public static final String ADDR_STREET1 = "addrStreet1";
	public static final String ADDR_STREET2 = "addrStreet2";
	public static final String ADDR_CITY = "addrCity";
	public static final String ADDR_STATE = "addrState";
	public static final String ADDR_ZIP = "addrZip";
	public static final String ADDR_CO_ID = "addrCoId";

	public final static String JNDI_NAME = AddressFacade.class.getName() + "_"
			+ AddressFacadeLocal.class.getName() + "@Local";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB
	private CountryFacadeLocal fCountry;

	/**
	 * Perform an initial save of a previously unsaved Address entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            Address entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Address entity) {
		LogUtil.log("saving Address instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Address entity.
	 * 
	 * @param entity
	 *            Address entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Address entity) {
		LogUtil.log("deleting Address instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(Address.class, entity
					.getAddrId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Address entity and return it or a copy of it
	 * to the sender. A copy of the Address entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            Address entity to update
	 * @return Address the persisted Address entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Address update(Address entity) {
		LogUtil.log("updating Address instance", Level.INFO, null);
		try {
			Address result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Address findById(Integer id) {
		LogUtil
				.log("finding Address instance with id: " + id, Level.INFO,
						null);
		try {
			Address instance = entityManager.find(Address.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all Address entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Address property to query
	 * @param value
	 *            the property value to match
	 * @return List<Address> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Address> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding Address instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Address model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<Address> findByAddrStreet1(Object addrStreet1) {
		return findByProperty(ADDR_STREET1, addrStreet1);
	}

	public List<Address> findByAddrStreet2(Object addrStreet2) {
		return findByProperty(ADDR_STREET2, addrStreet2);
	}

	public List<Address> findByAddrCity(Object addrCity) {
		return findByProperty(ADDR_CITY, addrCity);
	}

	public List<Address> findByAddrState(Object addrState) {
		return findByProperty(ADDR_STATE, addrState);
	}

	public List<Address> findByAddrZip(Object addrZip) {
		return findByProperty(ADDR_ZIP, addrZip);
	}

	public List<Address> findByAddrCoId(Object addrCoId) {
		return findByProperty(ADDR_CO_ID, addrCoId);
	}

	/**
	 * Find all Address entities.
	 * 
	 * @return List<Address> all Address entities
	 */
	@SuppressWarnings("unchecked")
	public List<Address> findAll() {
		LogUtil.log("finding all Address instances", Level.INFO, null);
		try {
			final String queryString = "select model from Address model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public int enterAddress(String street1, String street2, String city,
			String state, String zip, String country) {
		/*
		 * int addr_id = 0; // PreparedStatement get_co_id = null; //
		 * PreparedStatement match_address = null; // PreparedStatement
		 * insert_address_row = null; // ResultSet rs = null; // ResultSet rs2 =
		 * null;
		 * 
		 * // Get the country ID from the country table matching this address.
		 * 
		 * // Is it safe to assume that the country that we are looking // for
		 * will be there? try { // get_co_id =
		 * con.prepareStatement("SELECT co_id FROM country WHERE co_name = ?");
		 * // get_co_id.setString(1, country); // rs = get_co_id.executeQuery();
		 * // rs.next(); // int addr_co_id = rs.getInt("co_id"); // rs.close();
		 * 
		 * CountryDAO countryDAO = new CountryDAO(); List listCountry =
		 * countryDAO.findByCoName(country); Country countryObj =
		 * (Country)listCountry.get(0); int addr_co_id = countryObj.getCoId();
		 * 
		 * 
		 * 
		 * // Get address id for this customer, possible insert row in //
		 * address table Query match_address =
		 * session.createQuery("SELECT address.addrId FROM Address address " +
		 * "WHERE address.addrStreet1 = ? " + "AND address.addrStreet2 = ? " +
		 * "AND address.addrCity = ? " + "AND address.addrState = ? " +
		 * "AND address.addrZip = ? " + "AND address.addrCoId = ?");
		 * match_address.setCacheable(true); match_address.setString(0,
		 * street1); match_address.setString(1, street2);
		 * match_address.setString(2, city); match_address.setString(3, state);
		 * match_address.setString(4, zip); match_address.setInteger(5,
		 * addr_co_id); // rs = match_address.executeQuery(); List
		 * listMatchAddress = match_address.list();
		 * if(listMatchAddress==null||listMatchAddress.isEmpty()){
		 * org.bench4Q.hibernate.Address addrNew = new
		 * org.bench4Q.hibernate.Address( (Integer) null, street1, street2,
		 * city, state, zip, addr_co_id); session.save(addrNew);
		 * addr_id=addrNew.getAddrId(); } else { addr_id =
		 * (Integer)listMatchAddress.get(0); }
		 * 
		 * // if (!rs.next()) {// We didn't match an address in the addr table
		 * // insert_address_row = con.prepareStatement( //
		 * "INSERT into address (addr_street1, addr_street2, addr_city, addr_state, addr_zip, addr_co_id) "
		 * // + "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		 * // insert_address_row.setString(1, street1); //
		 * insert_address_row.setString(2, street2); //
		 * insert_address_row.setString(3, city); //
		 * insert_address_row.setString(4, state); //
		 * insert_address_row.setString(5, zip); // insert_address_row.setInt(6,
		 * addr_co_id); // insert_address_row.executeUpdate(); // rs2 =
		 * insert_address_row.getGeneratedKeys(); // if (rs2.next()) { //
		 * addr_id = rs2.getInt(1); // } // } else { // We actually matched //
		 * addr_id = rs.getInt("addr_id"); // } } catch (java.lang.Exception ex)
		 * { // ex.printStackTrace(); throw ex; } finally { //
		 * closeResultSet(rs); // closeResultSet(rs2); // closeStmt(get_co_id);
		 * // closeStmt(match_address); // closeStmt(insert_address_row); }
		 */

		int addrId;

		List listCountry = fCountry.findByCoName(country);
		Country countryObj = (Country) listCountry.get(0);
		int addr_co_id = countryObj.getCoId();

		// Get address id for this customer, possible insert row in
		// address table
		Query checkAddr = entityManager.createNamedQuery("checkAddress");

		checkAddr.setParameter("street1", street1);
		checkAddr.setParameter("street2", street2);
		checkAddr.setParameter("city", city);
		checkAddr.setParameter("state", state);
		checkAddr.setParameter("coId", addr_co_id);
		checkAddr.setParameter("zip", zip);

		List addrList = checkAddr.getResultList();
		if (addrList != null && addrList.size() != 0) {
			addrId = (Integer) addrList.get(0);
			return addrId;
		} else {
			// FIXME: no addr_id specified, can the address object successfully
			// saved into the db?
			Address address = new Address((Integer) null, street1, street2,
					city, state, zip, addr_co_id);
			save(address);
			if (address.getAddrId() == 0)
				throw new RuntimeException("address equals 0");
			return address.getAddrId();
		}

	}

	public List getAllIds() {
		Query getAllAddressIds = entityManager
				.createNamedQuery("getAllAddressIds");

		return getAllAddressIds.getResultList();
	}

}