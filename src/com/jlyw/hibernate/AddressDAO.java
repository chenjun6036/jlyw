package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Address entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Address
 * @author MyEclipse Persistence Tools
 */

public class AddressDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(AddressDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String ADDRESS = "address";
	public static final String BRIEF = "brief";
	public static final String STATUS = "status";
	public static final String TEL = "tel";
	public static final String HEAD_NAME = "headName";
	public static final String HEAD_NAME_EN = "headNameEn";
	public static final String FAX = "fax";
	public static final String ZIP_CODE = "zipCode";
	public static final String ADDRESS_EN = "addressEn";
	public static final String COMPLAIN_TEL = "complainTel";
	public static final String WEB_SITE = "webSite";
	public static final String AUTHORIZATION_STATEMENT = "authorizationStatement";
	public static final String AUTHORIZATION_STATEMENT_EN = "authorizationStatementEn";
	public static final String CNASSTATEMENT = "cnasstatement";
	public static final String CNASSTATEMENT_EN = "cnasstatementEn";
	public static final String STANDARD_STATEMENT = "standardStatement";
	public static final String STANDARD_STATEMENT_EN = "standardStatementEn";

	public void save(Address transientInstance) {
		log.debug("saving Address instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Address persistentInstance) {
		log.debug("deleting Address instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Address findById(java.lang.Integer id) {
		log.debug("getting Address instance with id: " + id);
		try {
			Address instance = (Address) getSession().get(
					"com.jlyw.hibernate.Address", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Address instance) {
		log.debug("finding Address instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.Address")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Address instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Address as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByTel(Object tel) {
		return findByProperty(TEL, tel);
	}

	public List findByHeadName(Object headName) {
		return findByProperty(HEAD_NAME, headName);
	}

	public List findByHeadNameEn(Object headNameEn) {
		return findByProperty(HEAD_NAME_EN, headNameEn);
	}

	public List findByFax(Object fax) {
		return findByProperty(FAX, fax);
	}

	public List findByZipCode(Object zipCode) {
		return findByProperty(ZIP_CODE, zipCode);
	}

	public List findByAddressEn(Object addressEn) {
		return findByProperty(ADDRESS_EN, addressEn);
	}

	public List findByComplainTel(Object complainTel) {
		return findByProperty(COMPLAIN_TEL, complainTel);
	}

	public List findByWebSite(Object webSite) {
		return findByProperty(WEB_SITE, webSite);
	}

	public List findByAuthorizationStatement(Object authorizationStatement) {
		return findByProperty(AUTHORIZATION_STATEMENT, authorizationStatement);
	}

	public List findByAuthorizationStatementEn(Object authorizationStatementEn) {
		return findByProperty(AUTHORIZATION_STATEMENT_EN,
				authorizationStatementEn);
	}

	public List findByCnasstatement(Object cnasstatement) {
		return findByProperty(CNASSTATEMENT, cnasstatement);
	}

	public List findByCnasstatementEn(Object cnasstatementEn) {
		return findByProperty(CNASSTATEMENT_EN, cnasstatementEn);
	}

	public List findByStandardStatement(Object standardStatement) {
		return findByProperty(STANDARD_STATEMENT, standardStatement);
	}

	public List findByStandardStatementEn(Object standardStatementEn) {
		return findByProperty(STANDARD_STATEMENT_EN, standardStatementEn);
	}

	public List findAll() {
		log.debug("finding all Address instances");
		try {
			String queryString = "from Address";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Address merge(Address detachedInstance) {
		log.debug("merging Address instance");
		try {
			Address result = (Address) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Address instance) {
		log.debug("attaching dirty Address instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Address instance) {
		log.debug("attaching clean Address instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}