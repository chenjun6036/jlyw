package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppStdNameProTeam entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.AppStdNameProTeam
 * @author MyEclipse Persistence Tools
 */

public class AppStdNameProTeamDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(AppStdNameProTeamDAO.class);
	// property constants
	public static final String PROJECT_NAME = "projectName";
	public static final String STATUS = "status";

	public void save(AppStdNameProTeam transientInstance) {
		log.debug("saving AppStdNameProTeam instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(AppStdNameProTeam persistentInstance) {
		log.debug("deleting AppStdNameProTeam instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AppStdNameProTeam findById(java.lang.Integer id) {
		log.debug("getting AppStdNameProTeam instance with id: " + id);
		try {
			AppStdNameProTeam instance = (AppStdNameProTeam) getSession().get(
					"com.jlyw.hibernate.AppStdNameProTeam", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(AppStdNameProTeam instance) {
		log.debug("finding AppStdNameProTeam instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.AppStdNameProTeam").add(
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
		log.debug("finding AppStdNameProTeam instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from AppStdNameProTeam as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByProjectName(Object projectName) {
		return findByProperty(PROJECT_NAME, projectName);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all AppStdNameProTeam instances");
		try {
			String queryString = "from AppStdNameProTeam";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public AppStdNameProTeam merge(AppStdNameProTeam detachedInstance) {
		log.debug("merging AppStdNameProTeam instance");
		try {
			AppStdNameProTeam result = (AppStdNameProTeam) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(AppStdNameProTeam instance) {
		log.debug("attaching dirty AppStdNameProTeam instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AppStdNameProTeam instance) {
		log.debug("attaching clean AppStdNameProTeam instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}