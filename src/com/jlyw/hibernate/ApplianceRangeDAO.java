package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ApplianceRange entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.ApplianceRange
 * @author MyEclipse Persistence Tools
 */

public class ApplianceRangeDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ApplianceRangeDAO.class);
	// property constants
	public static final String RANGE = "range";
	public static final String RANGE_EN = "rangeEn";

	public void save(ApplianceRange transientInstance) {
		log.debug("saving ApplianceRange instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ApplianceRange persistentInstance) {
		log.debug("deleting ApplianceRange instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ApplianceRange findById(java.lang.Integer id) {
		log.debug("getting ApplianceRange instance with id: " + id);
		try {
			ApplianceRange instance = (ApplianceRange) getSession().get(
					"com.jlyw.hibernate.ApplianceRange", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ApplianceRange instance) {
		log.debug("finding ApplianceRange instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.ApplianceRange").add(
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
		log.debug("finding ApplianceRange instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ApplianceRange as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRange(Object range) {
		return findByProperty(RANGE, range);
	}

	public List findByRangeEn(Object rangeEn) {
		return findByProperty(RANGE_EN, rangeEn);
	}

	public List findAll() {
		log.debug("finding all ApplianceRange instances");
		try {
			String queryString = "from ApplianceRange";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ApplianceRange merge(ApplianceRange detachedInstance) {
		log.debug("merging ApplianceRange instance");
		try {
			ApplianceRange result = (ApplianceRange) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ApplianceRange instance) {
		log.debug("attaching dirty ApplianceRange instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ApplianceRange instance) {
		log.debug("attaching clean ApplianceRange instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}