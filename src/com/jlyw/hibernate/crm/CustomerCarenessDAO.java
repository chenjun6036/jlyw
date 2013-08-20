package com.jlyw.hibernate.crm;

import com.jlyw.hibernate.BaseHibernateDAO;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * CustomerCareness entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.crm.CustomerCareness
 * @author MyEclipse Persistence Tools
 */
public class CustomerCarenessDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CustomerCarenessDAO.class);
	// property constants
	public static final String CUSTOMER_NAME = "customerName";
	public static final String PRIORITY = "priority";
	public static final String STATUS = "status";
	public static final String WAY = "way";
	public static final String CARE_CONTACTOR = "careContactor";
	public static final String REMARK = "remark";

	public void save(CustomerCareness transientInstance) {
		log.debug("saving CustomerCareness instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CustomerCareness persistentInstance) {
		log.debug("deleting CustomerCareness instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerCareness findById(java.lang.Integer id) {
		log.debug("getting CustomerCareness instance with id: " + id);
		try {
			CustomerCareness instance = (CustomerCareness) getSession().get(
					"com.jlyw.hibernate.crm.CustomerCareness", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CustomerCareness instance) {
		log.debug("finding CustomerCareness instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.crm.CustomerCareness")
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
		log.debug("finding CustomerCareness instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CustomerCareness as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCustomerName(Object customerName) {
		return findByProperty(CUSTOMER_NAME, customerName);
	}

	public List findByPriority(Object priority) {
		return findByProperty(PRIORITY, priority);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByWay(Object way) {
		return findByProperty(WAY, way);
	}

	public List findByCareContactor(Object careContactor) {
		return findByProperty(CARE_CONTACTOR, careContactor);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all CustomerCareness instances");
		try {
			String queryString = "from CustomerCareness";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CustomerCareness merge(CustomerCareness detachedInstance) {
		log.debug("merging CustomerCareness instance");
		try {
			CustomerCareness result = (CustomerCareness) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CustomerCareness instance) {
		log.debug("attaching dirty CustomerCareness instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CustomerCareness instance) {
		log.debug("attaching clean CustomerCareness instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}