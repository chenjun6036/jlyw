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
 * SubContractor entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.SubContractor
 * @author MyEclipse Persistence Tools
 */

public class SubContractorDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SubContractorDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String BRIEF = "brief";
	public static final String CODE = "code";
	public static final String ADDRESS = "address";
	public static final String TEL = "tel";
	public static final String ZIP_CODE = "zipCode";
	public static final String CONTACTOR = "contactor";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";
	public static final String COPY = "copy";
	public static final String CONTACTOR_TEL = "contactorTel";

	public void save(SubContractor transientInstance) {
		log.debug("saving SubContractor instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SubContractor persistentInstance) {
		log.debug("deleting SubContractor instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SubContractor findById(java.lang.Integer id) {
		log.debug("getting SubContractor instance with id: " + id);
		try {
			SubContractor instance = (SubContractor) getSession().get(
					"com.jlyw.hibernate.SubContractor", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SubContractor instance) {
		log.debug("finding SubContractor instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.SubContractor").add(
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
		log.debug("finding SubContractor instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from SubContractor as model where model."
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

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByTel(Object tel) {
		return findByProperty(TEL, tel);
	}

	public List findByZipCode(Object zipCode) {
		return findByProperty(ZIP_CODE, zipCode);
	}

	public List findByContactor(Object contactor) {
		return findByProperty(CONTACTOR, contactor);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCopy(Object copy) {
		return findByProperty(COPY, copy);
	}

	public List findByContactorTel(Object contactorTel) {
		return findByProperty(CONTACTOR_TEL, contactorTel);
	}

	public List findAll() {
		log.debug("finding all SubContractor instances");
		try {
			String queryString = "from SubContractor";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SubContractor merge(SubContractor detachedInstance) {
		log.debug("merging SubContractor instance");
		try {
			SubContractor result = (SubContractor) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SubContractor instance) {
		log.debug("attaching dirty SubContractor instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SubContractor instance) {
		log.debug("attaching clean SubContractor instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}