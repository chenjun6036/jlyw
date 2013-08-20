package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Deal
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.jlyw.hibernate.Deal
 * @author MyEclipse Persistence Tools
 */

public class DealDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(DealDAO.class);
	// property constants
	public static final String CONTACTOR_NAME = "contactorName";
	public static final String CONTACTOR_TEL = "contactorTel";
	public static final String STATUS = "status";
	public static final String REMARK = "remark";
	public static final String ATTACHMENT = "attachment";

	public void save(Deal transientInstance) {
		log.debug("saving Deal instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Deal persistentInstance) {
		log.debug("deleting Deal instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Deal findById(java.lang.String id) {
		log.debug("getting Deal instance with id: " + id);
		try {
			Deal instance = (Deal) getSession().get("com.jlyw.hibernate.Deal",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Deal instance) {
		log.debug("finding Deal instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.Deal").add(Example.create(instance))
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
		log.debug("finding Deal instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Deal as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByContactorName(Object contactorName) {
		return findByProperty(CONTACTOR_NAME, contactorName);
	}

	public List findByContactorTel(Object contactorTel) {
		return findByProperty(CONTACTOR_TEL, contactorTel);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByAttachment(Object attachment) {
		return findByProperty(ATTACHMENT, attachment);
	}

	public List findAll() {
		log.debug("finding all Deal instances");
		try {
			String queryString = "from Deal";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Deal merge(Deal detachedInstance) {
		log.debug("merging Deal instance");
		try {
			Deal result = (Deal) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Deal instance) {
		log.debug("attaching dirty Deal instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Deal instance) {
		log.debug("attaching clean Deal instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}