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
 * Quotation entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Quotation
 * @author MyEclipse Persistence Tools
 */

public class QuotationDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(QuotationDAO.class);
	// property constants
	public static final String VERSION = "version";
	public static final String CONTACTOR = "contactor";
	public static final String CONTACTOR_TEL = "contactorTel";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String CAR_COST = "carCost";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";
	public static final String CONTACTOR_EMAIL = "contactorEmail";

	public void save(Quotation transientInstance) {
		log.debug("saving Quotation instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Quotation persistentInstance) {
		log.debug("deleting Quotation instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Quotation findById(java.lang.String id) {
		log.debug("getting Quotation instance with id: " + id);
		try {
			Quotation instance = (Quotation) getSession().get(
					"com.jlyw.hibernate.Quotation", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Quotation instance) {
		log.debug("finding Quotation instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.Quotation").add(
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
		log.debug("finding Quotation instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Quotation as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findByContactor(Object contactor) {
		return findByProperty(CONTACTOR, contactor);
	}

	public List findByContactorTel(Object contactorTel) {
		return findByProperty(CONTACTOR_TEL, contactorTel);
	}

	public List findByCustomerName(Object customerName) {
		return findByProperty(CUSTOMER_NAME, customerName);
	}

	public List findByCarCost(Object carCost) {
		return findByProperty(CAR_COST, carCost);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByContactorEmail(Object contactorEmail) {
		return findByProperty(CONTACTOR_EMAIL, contactorEmail);
	}

	public List findAll() {
		log.debug("finding all Quotation instances");
		try {
			String queryString = "from Quotation";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Quotation merge(Quotation detachedInstance) {
		log.debug("merging Quotation instance");
		try {
			Quotation result = (Quotation) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Quotation instance) {
		log.debug("attaching dirty Quotation instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Quotation instance) {
		log.debug("attaching clean Quotation instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}