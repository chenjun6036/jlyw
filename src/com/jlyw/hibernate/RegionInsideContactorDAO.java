package com.jlyw.hibernate;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * RegionInsideContactor entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.RegionInsideContactor
 * @author MyEclipse Persistence Tools
 */
public class RegionInsideContactorDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(RegionInsideContactorDAO.class);
	// property constants
	public static final String STATUS = "status";

	public void save(RegionInsideContactor transientInstance) {
		log.debug("saving RegionInsideContactor instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RegionInsideContactor persistentInstance) {
		log.debug("deleting RegionInsideContactor instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RegionInsideContactor findById(java.lang.Integer id) {
		log.debug("getting RegionInsideContactor instance with id: " + id);
		try {
			RegionInsideContactor instance = (RegionInsideContactor) getSession()
					.get("com.jlyw.hibernate.RegionInsideContactor", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RegionInsideContactor instance) {
		log.debug("finding RegionInsideContactor instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.RegionInsideContactor")
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
		log.debug("finding RegionInsideContactor instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RegionInsideContactor as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all RegionInsideContactor instances");
		try {
			String queryString = "from RegionInsideContactor";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RegionInsideContactor merge(RegionInsideContactor detachedInstance) {
		log.debug("merging RegionInsideContactor instance");
		try {
			RegionInsideContactor result = (RegionInsideContactor) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RegionInsideContactor instance) {
		log.debug("attaching dirty RegionInsideContactor instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RegionInsideContactor instance) {
		log.debug("attaching clean RegionInsideContactor instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}