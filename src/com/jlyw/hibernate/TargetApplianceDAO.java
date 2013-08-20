package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * TargetAppliance entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.TargetAppliance
 * @author MyEclipse Persistence Tools
 */

public class TargetApplianceDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(TargetApplianceDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String NAME_EN = "nameEn";
	public static final String BRIEF = "brief";
	public static final String CODE = "code";
	public static final String FEE = "fee";
	public static final String SRFEE = "srfee";
	public static final String MRFEE = "mrfee";
	public static final String LRFEE = "lrfee";
	public static final String PROMISE_DURATION = "promiseDuration";
	public static final String TEST_CYCLE = "testCycle";
	public static final String STATUS = "status";
	public static final String CERTIFICATION = "certification";
	public static final String REMARK = "remark";

	public void save(TargetAppliance transientInstance) {
		log.debug("saving TargetAppliance instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TargetAppliance persistentInstance) {
		log.debug("deleting TargetAppliance instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TargetAppliance findById(java.lang.Integer id) {
		log.debug("getting TargetAppliance instance with id: " + id);
		try {
			TargetAppliance instance = (TargetAppliance) getSession().get(
					"com.jlyw.hibernate.TargetAppliance", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TargetAppliance instance) {
		log.debug("finding TargetAppliance instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.TargetAppliance").add(
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
		log.debug("finding TargetAppliance instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from TargetAppliance as model where model."
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

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByFee(Object fee) {
		return findByProperty(FEE, fee);
	}

	public List findBySrfee(Object srfee) {
		return findByProperty(SRFEE, srfee);
	}

	public List findByMrfee(Object mrfee) {
		return findByProperty(MRFEE, mrfee);
	}

	public List findByLrfee(Object lrfee) {
		return findByProperty(LRFEE, lrfee);
	}

	public List findByPromiseDuration(Object promiseDuration) {
		return findByProperty(PROMISE_DURATION, promiseDuration);
	}

	public List findByTestCycle(Object testCycle) {
		return findByProperty(TEST_CYCLE, testCycle);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCertification(Object certification) {
		return findByProperty(CERTIFICATION, certification);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all TargetAppliance instances");
		try {
			String queryString = "from TargetAppliance";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TargetAppliance merge(TargetAppliance detachedInstance) {
		log.debug("merging TargetAppliance instance");
		try {
			TargetAppliance result = (TargetAppliance) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TargetAppliance instance) {
		log.debug("attaching dirty TargetAppliance instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TargetAppliance instance) {
		log.debug("attaching clean TargetAppliance instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}