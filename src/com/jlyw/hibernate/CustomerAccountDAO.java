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
 * CustomerAccount entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.CustomerAccount
 * @author MyEclipse Persistence Tools
 */

public class CustomerAccountDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CustomerAccountDAO.class);
	// property constants
	public static final String HANDLE_TYPE = "handleType";
	public static final String HANDLER_NAME = "handlerName";
	public static final String AMOUNT = "amount";
	public static final String REMARK = "remark";

	public void save(CustomerAccount transientInstance) {
		log.debug("saving CustomerAccount instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CustomerAccount persistentInstance) {
		log.debug("deleting CustomerAccount instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerAccount findById(java.lang.Integer id) {
		log.debug("getting CustomerAccount instance with id: " + id);
		try {
			CustomerAccount instance = (CustomerAccount) getSession().get(
					"com.jlyw.hibernate.CustomerAccount", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CustomerAccount instance) {
		log.debug("finding CustomerAccount instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.CustomerAccount").add(
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
		log.debug("finding CustomerAccount instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CustomerAccount as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByHandleType(Object handleType) {
		return findByProperty(HANDLE_TYPE, handleType);
	}

	public List findByHandlerName(Object handlerName) {
		return findByProperty(HANDLER_NAME, handlerName);
	}

	public List findByAmount(Object amount) {
		return findByProperty(AMOUNT, amount);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all CustomerAccount instances");
		try {
			String queryString = "from CustomerAccount";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CustomerAccount merge(CustomerAccount detachedInstance) {
		log.debug("merging CustomerAccount instance");
		try {
			CustomerAccount result = (CustomerAccount) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CustomerAccount instance) {
		log.debug("attaching dirty CustomerAccount instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CustomerAccount instance) {
		log.debug("attaching clean CustomerAccount instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}