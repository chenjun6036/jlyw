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
 * Withdraw entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Withdraw
 * @author MyEclipse Persistence Tools
 */

public class WithdrawDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(WithdrawDAO.class);
	// property constants
	public static final String NUMBER = "number";
	public static final String DESCRIPTION = "description";
	public static final String LOCATION = "location";
	public static final String EXECUTE_RESULT = "executeResult";
	public static final String EXECUTE_MSG = "executeMsg";

	public void save(Withdraw transientInstance) {
		log.debug("saving Withdraw instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Withdraw persistentInstance) {
		log.debug("deleting Withdraw instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Withdraw findById(java.lang.Integer id) {
		log.debug("getting Withdraw instance with id: " + id);
		try {
			Withdraw instance = (Withdraw) getSession().get(
					"com.jlyw.hibernate.Withdraw", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Withdraw instance) {
		log.debug("finding Withdraw instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.Withdraw")
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
		log.debug("finding Withdraw instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Withdraw as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByNumber(Object number) {
		return findByProperty(NUMBER, number);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByLocation(Object location) {
		return findByProperty(LOCATION, location);
	}

	public List findByExecuteResult(Object executeResult) {
		return findByProperty(EXECUTE_RESULT, executeResult);
	}

	public List findByExecuteMsg(Object executeMsg) {
		return findByProperty(EXECUTE_MSG, executeMsg);
	}

	public List findAll() {
		log.debug("finding all Withdraw instances");
		try {
			String queryString = "from Withdraw";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Withdraw merge(Withdraw detachedInstance) {
		log.debug("merging Withdraw instance");
		try {
			Withdraw result = (Withdraw) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Withdraw instance) {
		log.debug("attaching dirty Withdraw instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Withdraw instance) {
		log.debug("attaching clean Withdraw instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}