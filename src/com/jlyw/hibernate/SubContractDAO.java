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
 * SubContract entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.SubContract
 * @author MyEclipse Persistence Tools
 */

public class SubContractDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SubContractDAO.class);
	// property constants
	public static final String ATTACHMENT = "attachment";
	public static final String STATUS = "status";
	public static final String REMARK = "remark";
	public static final String TOTAL_FEE = "totalFee";

	public void save(SubContract transientInstance) {
		log.debug("saving SubContract instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(SubContract persistentInstance) {
		log.debug("deleting SubContract instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SubContract findById(java.lang.Integer id) {
		log.debug("getting SubContract instance with id: " + id);
		try {
			SubContract instance = (SubContract) getSession().get(
					"com.jlyw.hibernate.SubContract", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(SubContract instance) {
		log.debug("finding SubContract instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.SubContract").add(
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
		log.debug("finding SubContract instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from SubContract as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByAttachment(Object attachment) {
		return findByProperty(ATTACHMENT, attachment);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByTotalFee(Object totalFee) {
		return findByProperty(TOTAL_FEE, totalFee);
	}

	public List findAll() {
		log.debug("finding all SubContract instances");
		try {
			String queryString = "from SubContract";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public SubContract merge(SubContract detachedInstance) {
		log.debug("merging SubContract instance");
		try {
			SubContract result = (SubContract) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SubContract instance) {
		log.debug("attaching dirty SubContract instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SubContract instance) {
		log.debug("attaching clean SubContract instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}