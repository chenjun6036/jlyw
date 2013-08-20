package com.jlyw.hibernate.crm;

import com.jlyw.hibernate.BaseHibernateDAO;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * CustomerFeedback entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.crm.CustomerFeedback
 * @author MyEclipse Persistence Tools
 */
public class CustomerFeedbackDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CustomerFeedbackDAO.class);
	// property constants
	public static final String COMPLAIN_ABOUT = "complainAbout";
	public static final String CUSTOMER_CONTACTOR_NAME = "customerContactorName";
	public static final String HANDLE_LEVEL = "handleLevel";
	public static final String FEEDBACK = "feedback";
	public static final String STATUS = "status";
	public static final String METHOD = "method";
	public static final String REMARK = "remark";
	public static final String RETURN_VISIT_TYPE = "returnVisitType";
	public static final String RETURN_VISIT_INFO = "returnVisitInfo";
	public static final String MARK = "mark";
	public static final String ANALYSIS = "analysis";

	public void save(CustomerFeedback transientInstance) {
		log.debug("saving CustomerFeedback instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CustomerFeedback persistentInstance) {
		log.debug("deleting CustomerFeedback instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerFeedback findById(java.lang.Integer id) {
		log.debug("getting CustomerFeedback instance with id: " + id);
		try {
			CustomerFeedback instance = (CustomerFeedback) getSession().get(
					"com.jlyw.hibernate.crm.CustomerFeedback", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CustomerFeedback instance) {
		log.debug("finding CustomerFeedback instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.crm.CustomerFeedback")
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
		log.debug("finding CustomerFeedback instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CustomerFeedback as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByComplainAbout(Object complainAbout) {
		return findByProperty(COMPLAIN_ABOUT, complainAbout);
	}

	public List findByCustomerContactorName(Object customerContactorName) {
		return findByProperty(CUSTOMER_CONTACTOR_NAME, customerContactorName);
	}

	public List findByHandleLevel(Object handleLevel) {
		return findByProperty(HANDLE_LEVEL, handleLevel);
	}

	public List findByFeedback(Object feedback) {
		return findByProperty(FEEDBACK, feedback);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByMethod(Object method) {
		return findByProperty(METHOD, method);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByReturnVisitType(Object returnVisitType) {
		return findByProperty(RETURN_VISIT_TYPE, returnVisitType);
	}

	public List findByReturnVisitInfo(Object returnVisitInfo) {
		return findByProperty(RETURN_VISIT_INFO, returnVisitInfo);
	}

	public List findByMark(Object mark) {
		return findByProperty(MARK, mark);
	}

	public List findByAnalysis(Object analysis) {
		return findByProperty(ANALYSIS, analysis);
	}

	public List findAll() {
		log.debug("finding all CustomerFeedback instances");
		try {
			String queryString = "from CustomerFeedback";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CustomerFeedback merge(CustomerFeedback detachedInstance) {
		log.debug("merging CustomerFeedback instance");
		try {
			CustomerFeedback result = (CustomerFeedback) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CustomerFeedback instance) {
		log.debug("attaching dirty CustomerFeedback instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CustomerFeedback instance) {
		log.debug("attaching clean CustomerFeedback instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}