package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ApplianceManufacturer entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.ApplianceManufacturer
 * @author MyEclipse Persistence Tools
 */

public class ApplianceManufacturerDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ApplianceManufacturerDAO.class);
	// property constants
	public static final String MANUFACTURER = "manufacturer";
	public static final String BRIEF = "brief";
	public static final String STATUS = "status";

	public void save(ApplianceManufacturer transientInstance) {
		log.debug("saving ApplianceManufacturer instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ApplianceManufacturer persistentInstance) {
		log.debug("deleting ApplianceManufacturer instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ApplianceManufacturer findById(java.lang.Integer id) {
		log.debug("getting ApplianceManufacturer instance with id: " + id);
		try {
			ApplianceManufacturer instance = (ApplianceManufacturer) getSession()
					.get("com.jlyw.hibernate.ApplianceManufacturer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ApplianceManufacturer instance) {
		log.debug("finding ApplianceManufacturer instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.ApplianceManufacturer").add(
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
		log.debug("finding ApplianceManufacturer instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ApplianceManufacturer as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByManufacturer(Object manufacturer) {
		return findByProperty(MANUFACTURER, manufacturer);
	}

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all ApplianceManufacturer instances");
		try {
			String queryString = "from ApplianceManufacturer";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ApplianceManufacturer merge(ApplianceManufacturer detachedInstance) {
		log.debug("merging ApplianceManufacturer instance");
		try {
			ApplianceManufacturer result = (ApplianceManufacturer) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ApplianceManufacturer instance) {
		log.debug("attaching dirty ApplianceManufacturer instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ApplianceManufacturer instance) {
		log.debug("attaching clean ApplianceManufacturer instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}