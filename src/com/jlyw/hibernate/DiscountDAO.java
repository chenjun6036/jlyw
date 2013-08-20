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
 * Discount entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Discount
 * @author MyEclipse Persistence Tools
 */

public class DiscountDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DiscountDAO.class);
	// property constants
	public static final String CONTECTOR = "contector";
	public static final String CONTECTOR_TEL = "contectorTel";
	public static final String EXECUTE_RESULT = "executeResult";
	public static final String EXECUTE_MSG = "executeMsg";

	public void save(Discount transientInstance) {
		log.debug("saving Discount instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Discount persistentInstance) {
		log.debug("deleting Discount instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Discount findById(java.lang.Integer id) {
		log.debug("getting Discount instance with id: " + id);
		try {
			Discount instance = (Discount) getSession().get(
					"com.jlyw.hibernate.Discount", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Discount instance) {
		log.debug("finding Discount instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.Discount")
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
		log.debug("finding Discount instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Discount as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByContector(Object contector) {
		return findByProperty(CONTECTOR, contector);
	}

	public List findByContectorTel(Object contectorTel) {
		return findByProperty(CONTECTOR_TEL, contectorTel);
	}

	public List findByExecuteResult(Object executeResult) {
		return findByProperty(EXECUTE_RESULT, executeResult);
	}

	public List findByExecuteMsg(Object executeMsg) {
		return findByProperty(EXECUTE_MSG, executeMsg);
	}

	public List findAll() {
		log.debug("finding all Discount instances");
		try {
			String queryString = "from Discount";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Discount merge(Discount detachedInstance) {
		log.debug("merging Discount instance");
		try {
			Discount result = (Discount) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Discount instance) {
		log.debug("attaching dirty Discount instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Discount instance) {
		log.debug("attaching clean Discount instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}