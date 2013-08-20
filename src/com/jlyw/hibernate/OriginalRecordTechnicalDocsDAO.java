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
 * OriginalRecordTechnicalDocs entities. Transaction control of the save(),
 * update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see com.jlyw.hibernate.OriginalRecordTechnicalDocs
 * @author MyEclipse Persistence Tools
 */

public class OriginalRecordTechnicalDocsDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(OriginalRecordTechnicalDocsDAO.class);

	// property constants

	public void save(OriginalRecordTechnicalDocs transientInstance) {
		log.debug("saving OriginalRecordTechnicalDocs instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OriginalRecordTechnicalDocs persistentInstance) {
		log.debug("deleting OriginalRecordTechnicalDocs instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OriginalRecordTechnicalDocs findById(java.lang.Integer id) {
		log
				.debug("getting OriginalRecordTechnicalDocs instance with id: "
						+ id);
		try {
			OriginalRecordTechnicalDocs instance = (OriginalRecordTechnicalDocs) getSession()
					.get("com.jlyw.hibernate.OriginalRecordTechnicalDocs", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OriginalRecordTechnicalDocs instance) {
		log.debug("finding OriginalRecordTechnicalDocs instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.OriginalRecordTechnicalDocs").add(
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
		log
				.debug("finding OriginalRecordTechnicalDocs instance with property: "
						+ propertyName + ", value: " + value);
		try {
			String queryString = "from OriginalRecordTechnicalDocs as model where model."
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
		log.debug("finding all OriginalRecordTechnicalDocs instances");
		try {
			String queryString = "from OriginalRecordTechnicalDocs";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OriginalRecordTechnicalDocs merge(
			OriginalRecordTechnicalDocs detachedInstance) {
		log.debug("merging OriginalRecordTechnicalDocs instance");
		try {
			OriginalRecordTechnicalDocs result = (OriginalRecordTechnicalDocs) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OriginalRecordTechnicalDocs instance) {
		log.debug("attaching dirty OriginalRecordTechnicalDocs instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OriginalRecordTechnicalDocs instance) {
		log.debug("attaching clean OriginalRecordTechnicalDocs instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}