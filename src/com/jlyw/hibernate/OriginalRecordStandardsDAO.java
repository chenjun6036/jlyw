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
 * OriginalRecordStandards entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.OriginalRecordStandards
 * @author MyEclipse Persistence Tools
 */

public class OriginalRecordStandardsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(OriginalRecordStandardsDAO.class);

	// property constants

	public void save(OriginalRecordStandards transientInstance) {
		log.debug("saving OriginalRecordStandards instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OriginalRecordStandards persistentInstance) {
		log.debug("deleting OriginalRecordStandards instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OriginalRecordStandards findById(java.lang.Integer id) {
		log.debug("getting OriginalRecordStandards instance with id: " + id);
		try {
			OriginalRecordStandards instance = (OriginalRecordStandards) getSession()
					.get("com.jlyw.hibernate.OriginalRecordStandards", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OriginalRecordStandards instance) {
		log.debug("finding OriginalRecordStandards instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.OriginalRecordStandards").add(
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
		log.debug("finding OriginalRecordStandards instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from OriginalRecordStandards as model where model."
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
		log.debug("finding all OriginalRecordStandards instances");
		try {
			String queryString = "from OriginalRecordStandards";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OriginalRecordStandards merge(
			OriginalRecordStandards detachedInstance) {
		log.debug("merging OriginalRecordStandards instance");
		try {
			OriginalRecordStandards result = (OriginalRecordStandards) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OriginalRecordStandards instance) {
		log.debug("attaching dirty OriginalRecordStandards instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OriginalRecordStandards instance) {
		log.debug("attaching clean OriginalRecordStandards instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}