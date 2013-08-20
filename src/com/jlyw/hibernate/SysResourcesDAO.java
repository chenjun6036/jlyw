package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * SysResources entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.SysResources
 * @author MyEclipse Persistence Tools
 */

public class SysResourcesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SysResourcesDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String MAPPING_URL = "mappingUrl";
	public static final String STATUS = "status";

	public void save(SysResources transientInstance) {
		log.debug("saving SysResources instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SysResources persistentInstance) {
		log.debug("deleting SysResources instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SysResources findById(java.lang.Integer id) {
		log.debug("getting SysResources instance with id: " + id);
		try {
			SysResources instance = (SysResources) getSession().get(
					"com.jlyw.hibernate.SysResources", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SysResources instance) {
		log.debug("finding SysResources instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.SysResources").add(
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
		log.debug("finding SysResources instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SysResources as model where model."
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

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByMappingUrl(Object mappingUrl) {
		return findByProperty(MAPPING_URL, mappingUrl);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all SysResources instances");
		try {
			String queryString = "from SysResources";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SysResources merge(SysResources detachedInstance) {
		log.debug("merging SysResources instance");
		try {
			SysResources result = (SysResources) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SysResources instance) {
		log.debug("attaching dirty SysResources instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SysResources instance) {
		log.debug("attaching clean SysResources instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}