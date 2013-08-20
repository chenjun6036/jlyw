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
 * Overdue entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Overdue
 * @author MyEclipse Persistence Tools
 */

public class OverdueDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(OverdueDAO.class);
	// property constants
	public static final String DELAY_DAYS = "delayDays";
	public static final String EXECUTE_RESULT = "executeResult";
	public static final String EXECUTE_MSG = "executeMsg";

	public void save(Overdue transientInstance) {
		log.debug("saving Overdue instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Overdue persistentInstance) {
		log.debug("deleting Overdue instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Overdue findById(java.lang.Integer id) {
		log.debug("getting Overdue instance with id: " + id);
		try {
			Overdue instance = (Overdue) getSession().get(
					"com.jlyw.hibernate.Overdue", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Overdue instance) {
		log.debug("finding Overdue instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.Overdue").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Overdue instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Overdue as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByDelayDays(Object delayDays) {
		return findByProperty(DELAY_DAYS, delayDays);
	}

	public List findByExecuteResult(Object executeResult) {
		return findByProperty(EXECUTE_RESULT, executeResult);
	}

	public List findByExecuteMsg(Object executeMsg) {
		return findByProperty(EXECUTE_MSG, executeMsg);
	}

	public List findAll() {
		log.debug("finding all Overdue instances");
		try {
			String queryString = "from Overdue";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Overdue merge(Overdue detachedInstance) {
		log.debug("merging Overdue instance");
		try {
			Overdue result = (Overdue) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Overdue instance) {
		log.debug("attaching dirty Overdue instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Overdue instance) {
		log.debug("attaching clean Overdue instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}