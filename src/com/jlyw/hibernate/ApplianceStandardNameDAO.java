package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ApplianceStandardName entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.ApplianceStandardName
 * @author MyEclipse Persistence Tools
 */

public class ApplianceStandardNameDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ApplianceStandardNameDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String NAME_EN = "nameEn";
	public static final String BRIEF = "brief";
	public static final String HOLD_MANY = "holdMany";
	public static final String STATUS = "status";
	public static final String REMARK1 = "remark1";
	public static final String REMARK2 = "remark2";
	public static final String REMARK3 = "remark3";
	public static final String FILESET_NAME = "filesetName";

	public void save(ApplianceStandardName transientInstance) {
		log.debug("saving ApplianceStandardName instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(ApplianceStandardName persistentInstance) {
		log.debug("deleting ApplianceStandardName instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ApplianceStandardName findById(java.lang.Integer id) {
		log.debug("getting ApplianceStandardName instance with id: " + id);
		try {
			ApplianceStandardName instance = (ApplianceStandardName) getSession()
					.get("com.jlyw.hibernate.ApplianceStandardName", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ApplianceStandardName instance) {
		log.debug("finding ApplianceStandardName instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.ApplianceStandardName").add(
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
		log.debug("finding ApplianceStandardName instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from ApplianceStandardName as model where model."
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

	public List findByNameEn(Object nameEn) {
		return findByProperty(NAME_EN, nameEn);
	}

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByHoldMany(Object holdMany) {
		return findByProperty(HOLD_MANY, holdMany);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByRemark1(Object remark1) {
		return findByProperty(REMARK1, remark1);
	}

	public List findByRemark2(Object remark2) {
		return findByProperty(REMARK2, remark2);
	}

	public List findByRemark3(Object remark3) {
		return findByProperty(REMARK3, remark3);
	}

	public List findByFilesetName(Object filesetName) {
		return findByProperty(FILESET_NAME, filesetName);
	}

	public List findAll() {
		log.debug("finding all ApplianceStandardName instances");
		try {
			String queryString = "from ApplianceStandardName";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ApplianceStandardName merge(ApplianceStandardName detachedInstance) {
		log.debug("merging ApplianceStandardName instance");
		try {
			ApplianceStandardName result = (ApplianceStandardName) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ApplianceStandardName instance) {
		log.debug("attaching dirty ApplianceStandardName instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ApplianceStandardName instance) {
		log.debug("attaching clean ApplianceStandardName instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}