package com.jlyw.hibernate.crm;

import com.jlyw.hibernate.BaseHibernateDAO;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PotentialCustomer entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.crm.PotentialCustomer
 * @author MyEclipse Persistence Tools
 */
public class PotentialCustomerDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PotentialCustomerDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String NAME_EN = "nameEn";
	public static final String BRIEF = "brief";
	public static final String ADDRESS = "address";
	public static final String STATUS = "status";
	public static final String POTENTIAL_CUSTOMER_FROM = "potentialCustomerFrom";
	public static final String COOPERATION_INTENSION = "cooperationIntension";
	public static final String INDUSTRY = "industry";

	public void save(PotentialCustomer transientInstance) {
		log.debug("saving PotentialCustomer instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PotentialCustomer persistentInstance) {
		log.debug("deleting PotentialCustomer instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PotentialCustomer findById(java.lang.Integer id) {
		log.debug("getting PotentialCustomer instance with id: " + id);
		try {
			PotentialCustomer instance = (PotentialCustomer) getSession().get(
					"com.jlyw.hibernate.crm.PotentialCustomer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PotentialCustomer instance) {
		log.debug("finding PotentialCustomer instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.crm.PotentialCustomer")
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
		log.debug("finding PotentialCustomer instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from PotentialCustomer as model where model."
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

	public List findByNameEn(Object nameEn) {
		return findByProperty(NAME_EN, nameEn);
	}

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByPotentialCustomerFrom(Object potentialCustomerFrom) {
		return findByProperty(POTENTIAL_CUSTOMER_FROM, potentialCustomerFrom);
	}

	public List findByCooperationIntension(Object cooperationIntension) {
		return findByProperty(COOPERATION_INTENSION, cooperationIntension);
	}

	public List findByIndustry(Object industry) {
		return findByProperty(INDUSTRY, industry);
	}

	public List findAll() {
		log.debug("finding all PotentialCustomer instances");
		try {
			String queryString = "from PotentialCustomer";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PotentialCustomer merge(PotentialCustomer detachedInstance) {
		log.debug("merging PotentialCustomer instance");
		try {
			PotentialCustomer result = (PotentialCustomer) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PotentialCustomer instance) {
		log.debug("attaching dirty PotentialCustomer instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PotentialCustomer instance) {
		log.debug("attaching clean PotentialCustomer instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}