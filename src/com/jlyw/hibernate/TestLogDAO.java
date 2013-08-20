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
 * TestLog entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.TestLog
 * @author MyEclipse Persistence Tools
 */

public class TestLogDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(TestLogDAO.class);
	// property constants
	public static final String TESTER = "tester";
	public static final String CERTIFICATE_ID = "certificateId";
	public static final String CONFIRM_MEASURE = "confirmMeasure";
	public static final String CERTIFICATE_COPY = "certificateCopy";
	public static final String DURATION_CHECK = "durationCheck";
	public static final String MAINTENANCE = "maintenance";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";

	public void save(TestLog transientInstance) {
		log.debug("saving TestLog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(TestLog persistentInstance) {
		log.debug("deleting TestLog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TestLog findById(java.lang.Integer id) {
		log.debug("getting TestLog instance with id: " + id);
		try {
			TestLog instance = (TestLog) getSession().get(
					"com.jlyw.hibernate.TestLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(TestLog instance) {
		log.debug("finding TestLog instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.TestLog")
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
		log.debug("finding TestLog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from TestLog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTester(Object tester) {
		return findByProperty(TESTER, tester);
	}

	public List findByCertificateId(Object certificateId) {
		return findByProperty(CERTIFICATE_ID, certificateId);
	}

	public List findByConfirmMeasure(Object confirmMeasure) {
		return findByProperty(CONFIRM_MEASURE, confirmMeasure);
	}

	public List findByCertificateCopy(Object certificateCopy) {
		return findByProperty(CERTIFICATE_COPY, certificateCopy);
	}

	public List findByDurationCheck(Object durationCheck) {
		return findByProperty(DURATION_CHECK, durationCheck);
	}

	public List findByMaintenance(Object maintenance) {
		return findByProperty(MAINTENANCE, maintenance);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all TestLog instances");
		try {
			String queryString = "from TestLog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public TestLog merge(TestLog detachedInstance) {
		log.debug("merging TestLog instance");
		try {
			TestLog result = (TestLog) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TestLog instance) {
		log.debug("attaching dirty TestLog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TestLog instance) {
		log.debug("attaching clean TestLog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}