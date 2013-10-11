package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * LocaleMission entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.LocaleMission
 * @author MyEclipse Persistence Tools
 */

public class LocaleMissionDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(LocaleMissionDAO.class);

	public void save(LocaleMission transientInstance) {
		log.debug("saving LocaleMission instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(LocaleMission persistentInstance) {
		log.debug("deleting LocaleMission instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LocaleMission findById(java.lang.Integer id) {
		log.debug("getting LocaleMission instance with id: " + id);
		try {
			LocaleMission instance = (LocaleMission) getSession().get(
					"com.jlyw.hibernate.LocaleMission", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(LocaleMission instance) {
		log.debug("finding LocaleMission instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.LocaleMission")
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
		log.debug("finding LocaleMission instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from LocaleMission as model where model."
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
		log.debug("finding all LocaleMission instances");
		try {
			String queryString = "from LocaleMission";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public LocaleMission merge(LocaleMission detachedInstance) {
		log.debug("merging LocaleMission instance");
		try {
			LocaleMission result = (LocaleMission) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(LocaleMission instance) {
		log.debug("attaching dirty LocaleMission instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LocaleMission instance) {
		log.debug("attaching clean LocaleMission instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}