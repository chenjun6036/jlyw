package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * TgtAppStdApp entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.TgtAppStdApp
 * @author MyEclipse Persistence Tools
 */

public class TgtAppStdAppDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(TgtAppStdAppDAO.class);

	// property constants

	public void save(TgtAppStdApp transientInstance) {
		log.debug("saving TgtAppStdApp instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TgtAppStdApp persistentInstance) {
		log.debug("deleting TgtAppStdApp instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TgtAppStdApp findById(java.lang.Integer id) {
		log.debug("getting TgtAppStdApp instance with id: " + id);
		try {
			TgtAppStdApp instance = (TgtAppStdApp) getSession().get(
					"com.jlyw.hibernate.TgtAppStdApp", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TgtAppStdApp instance) {
		log.debug("finding TgtAppStdApp instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.TgtAppStdApp")
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
		log.debug("finding TgtAppStdApp instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TgtAppStdApp as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all TgtAppStdApp instances");
		try {
			String queryString = "from TgtAppStdApp";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TgtAppStdApp merge(TgtAppStdApp detachedInstance) {
		log.debug("merging TgtAppStdApp instance");
		try {
			TgtAppStdApp result = (TgtAppStdApp) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TgtAppStdApp instance) {
		log.debug("attaching dirty TgtAppStdApp instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TgtAppStdApp instance) {
		log.debug("attaching clean TgtAppStdApp instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}