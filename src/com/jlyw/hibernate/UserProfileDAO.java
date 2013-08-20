package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * UserProfile entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.UserProfile
 * @author MyEclipse Persistence Tools
 */

public class UserProfileDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UserProfileDAO.class);
	// property constants
	public static final String TYPE = "type";
	public static final String F1 = "f1";
	public static final String F2 = "f2";
	public static final String F3 = "f3";
	public static final String F4 = "f4";

	public void save(UserProfile transientInstance) {
		log.debug("saving UserProfile instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UserProfile persistentInstance) {
		log.debug("deleting UserProfile instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserProfile findById(java.lang.Integer id) {
		log.debug("getting UserProfile instance with id: " + id);
		try {
			UserProfile instance = (UserProfile) getSession().get(
					"com.jlyw.hibernate.UserProfile", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserProfile instance) {
		log.debug("finding UserProfile instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.UserProfile").add(
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
		log.debug("finding UserProfile instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserProfile as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByF1(Object f1) {
		return findByProperty(F1, f1);
	}

	public List findByF2(Object f2) {
		return findByProperty(F2, f2);
	}

	public List findByF3(Object f3) {
		return findByProperty(F3, f3);
	}

	public List findByF4(Object f4) {
		return findByProperty(F4, f4);
	}

	public List findAll() {
		log.debug("finding all UserProfile instances");
		try {
			String queryString = "from UserProfile";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserProfile merge(UserProfile detachedInstance) {
		log.debug("merging UserProfile instance");
		try {
			UserProfile result = (UserProfile) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserProfile instance) {
		log.debug("attaching dirty UserProfile instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserProfile instance) {
		log.debug("attaching clean UserProfile instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}