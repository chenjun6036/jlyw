package com.jlyw.hibernate;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * CustomerContactor entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.CustomerContactor
 * @author MyEclipse Persistence Tools
 */

public class CustomerContactorDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CustomerContactorDAO.class);
	// property constants
	public static final String CUSTOMER_ID = "customerId";
	public static final String NAME = "name";
	public static final String CELLPHONE1 = "cellphone1";
	public static final String CELLPHONE2 = "cellphone2";
	public static final String EMAIL = "email";
	public static final String COUNT = "count";

	public void save(CustomerContactor transientInstance) {
		log.debug("saving CustomerContactor instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CustomerContactor persistentInstance) {
		log.debug("deleting CustomerContactor instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerContactor findById(java.lang.Integer id) {
		log.debug("getting CustomerContactor instance with id: " + id);
		try {
			CustomerContactor instance = (CustomerContactor) getSession().get(
					"com.jlyw.hibernate.CustomerContactor", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CustomerContactor instance) {
		log.debug("finding CustomerContactor instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.CustomerContactor").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding CustomerContactor instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CustomerContactor as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCustomerId(Object customerId) {
		return findByProperty(CUSTOMER_ID, customerId);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByCellphone1(Object cellphone1) {
		return findByProperty(CELLPHONE1, cellphone1);
	}

	public List findByCellphone2(Object cellphone2) {
		return findByProperty(CELLPHONE2, cellphone2);
	}

	public List findByEmail(Object email) {
		return findByProperty(EMAIL, email);
	}

	public List findByCount(Object count) {
		return findByProperty(COUNT, count);
	}

	public List findAll() {
		log.debug("finding all CustomerContactor instances");
		try {
			String queryString = "from CustomerContactor";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CustomerContactor merge(CustomerContactor detachedInstance) {
		log.debug("merging CustomerContactor instance");
		try {
			CustomerContactor result = (CustomerContactor) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CustomerContactor instance) {
		log.debug("attaching dirty CustomerContactor instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CustomerContactor instance) {
		log.debug("attaching clean CustomerContactor instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}