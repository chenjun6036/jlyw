package com.jlyw.hibernate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * SharingFolder entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.SharingFolder
 * @author MyEclipse Persistence Tools
 */

public class SharingFolderDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SharingFolderDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String FILESET_NAME = "filesetName";
	public static final String STATUS = "status";

	public void save(SharingFolder transientInstance) {
		log.debug("saving SharingFolder instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SharingFolder persistentInstance) {
		log.debug("deleting SharingFolder instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SharingFolder findById(java.lang.Integer id) {
		log.debug("getting SharingFolder instance with id: " + id);
		try {
			SharingFolder instance = (SharingFolder) getSession().get(
					"com.jlyw.hibernate.SharingFolder", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SharingFolder instance) {
		log.debug("finding SharingFolder instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.SharingFolder").add(
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
		log.debug("finding SharingFolder instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SharingFolder as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByFilesetName(Object filesetName) {
		return findByProperty(FILESET_NAME, filesetName);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all SharingFolder instances");
		try {
			String queryString = "from SharingFolder";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SharingFolder merge(SharingFolder detachedInstance) {
		log.debug("merging SharingFolder instance");
		try {
			SharingFolder result = (SharingFolder) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SharingFolder instance) {
		log.debug("attaching dirty SharingFolder instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SharingFolder instance) {
		log.debug("attaching clean SharingFolder instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}