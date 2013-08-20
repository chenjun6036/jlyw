package com.jlyw.hibernate;

import java.sql.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * StandardAppliance entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.StandardAppliance
 * @author MyEclipse Persistence Tools
 */

public class StandardApplianceDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(StandardApplianceDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String BRIEF = "brief";
	public static final String MODEL = "model";
	public static final String RANGE = "range";
	public static final String UNCERTAIN = "uncertain";
	public static final String SERIA_NUMBER = "seriaNumber";
	public static final String MANUFACTURER = "manufacturer";
	public static final String RELEASE_NUMBER = "releaseNumber";
	public static final String NUM = "num";
	public static final String TEST_CYCLE = "testCycle";
	public static final String PRICE = "price";
	public static final String STATUS = "status";
	public static final String LOCALE_CODE = "localeCode";
	public static final String PERMANENT_CODE = "permanentCode";
	public static final String PROJECT_CODE = "projectCode";
	public static final String BUDGET = "budget";
	public static final String REMARK = "remark";
	public static final String WARN_SLOT = "warnSlot";
	public static final String INSPECT_MONTH = "inspectMonth";

	public void save(StandardAppliance transientInstance) {
		log.debug("saving StandardAppliance instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(StandardAppliance persistentInstance) {
		log.debug("deleting StandardAppliance instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public StandardAppliance findById(java.lang.Integer id) {
		log.debug("getting StandardAppliance instance with id: " + id);
		try {
			StandardAppliance instance = (StandardAppliance) getSession().get(
					"com.jlyw.hibernate.StandardAppliance", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(StandardAppliance instance) {
		log.debug("finding StandardAppliance instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.StandardAppliance")
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
		log.debug("finding StandardAppliance instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from StandardAppliance as model where model."
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

	public List findByModel(Object model) {
		return findByProperty(MODEL, model);
	}

	public List findByRange(Object range) {
		return findByProperty(RANGE, range);
	}

	public List findByUncertain(Object uncertain) {
		return findByProperty(UNCERTAIN, uncertain);
	}

	public List findBySeriaNumber(Object seriaNumber) {
		return findByProperty(SERIA_NUMBER, seriaNumber);
	}

	public List findByManufacturer(Object manufacturer) {
		return findByProperty(MANUFACTURER, manufacturer);
	}

	public List findByReleaseNumber(Object releaseNumber) {
		return findByProperty(RELEASE_NUMBER, releaseNumber);
	}

	public List findByNum(Object num) {
		return findByProperty(NUM, num);
	}

	public List findByTestCycle(Object testCycle) {
		return findByProperty(TEST_CYCLE, testCycle);
	}

	public List findByPrice(Object price) {
		return findByProperty(PRICE, price);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByLocaleCode(Object localeCode) {
		return findByProperty(LOCALE_CODE, localeCode);
	}

	public List findByPermanentCode(Object permanentCode) {
		return findByProperty(PERMANENT_CODE, permanentCode);
	}

	public List findByProjectCode(Object projectCode) {
		return findByProperty(PROJECT_CODE, projectCode);
	}

	public List findByBudget(Object budget) {
		return findByProperty(BUDGET, budget);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByWarnSlot(Object warnSlot) {
		return findByProperty(WARN_SLOT, warnSlot);
	}

	public List findByInspectMonth(Object inspectMonth) {
		return findByProperty(INSPECT_MONTH, inspectMonth);
	}

	public List findAll() {
		log.debug("finding all StandardAppliance instances");
		try {
			String queryString = "from StandardAppliance";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public StandardAppliance merge(StandardAppliance detachedInstance) {
		log.debug("merging StandardAppliance instance");
		try {
			StandardAppliance result = (StandardAppliance) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(StandardAppliance instance) {
		log.debug("attaching dirty StandardAppliance instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(StandardAppliance instance) {
		log.debug("attaching clean StandardAppliance instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}