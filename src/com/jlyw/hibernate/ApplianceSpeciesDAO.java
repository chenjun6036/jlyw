package com.jlyw.hibernate;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ApplianceSpecies entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.ApplianceSpecies
 * @author MyEclipse Persistence Tools
 */

public class ApplianceSpeciesDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ApplianceSpeciesDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String BRIEF = "brief";
	public static final String HIERARCHY = "hierarchy";
	public static final String STATUS = "status";
	public static final String SEQUENCE = "sequence";

	public void save(ApplianceSpecies transientInstance) {
		log.debug("saving ApplianceSpecies instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ApplianceSpecies persistentInstance) {
		log.debug("deleting ApplianceSpecies instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ApplianceSpecies findById(java.lang.Integer id) {
		log.debug("getting ApplianceSpecies instance with id: " + id);
		try {
			ApplianceSpecies instance = (ApplianceSpecies) getSession().get(
					"com.jlyw.hibernate.ApplianceSpecies", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ApplianceSpecies instance) {
		log.debug("finding ApplianceSpecies instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.ApplianceSpecies").add(
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
		log.debug("finding ApplianceSpecies instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ApplianceSpecies as model where model."
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

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByHierarchy(Object hierarchy) {
		return findByProperty(HIERARCHY, hierarchy);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}
	
	public List findBySequence(Object sequence) {
		return findByProperty(SEQUENCE, sequence);
	}

	public List findAll() {
		log.debug("finding all ApplianceSpecies instances");
		try {
			String queryString = "from ApplianceSpecies";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ApplianceSpecies merge(ApplianceSpecies detachedInstance) {
		log.debug("merging ApplianceSpecies instance");
		try {
			ApplianceSpecies result = (ApplianceSpecies) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ApplianceSpecies instance) {
		log.debug("attaching dirty ApplianceSpecies instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ApplianceSpecies instance) {
		log.debug("attaching clean ApplianceSpecies instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}