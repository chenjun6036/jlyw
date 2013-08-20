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
 * CustomerServiceFee entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.crm.CustomerServiceFee
 * @author MyEclipse Persistence Tools
 */
public class CustomerServiceFeeDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CustomerServiceFeeDAO.class);
	// property constants
	public static final String BILL_NUM = "billNum";
	public static final String PAID_VIA = "paidVia";
	public static final String PAID_SUBJECT = "paidSubject";
	public static final String MONEY = "money";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";
	public static final String ATTACHMENT = "attachment";

	public void save(CustomerServiceFee transientInstance) {
		log.debug("saving CustomerServiceFee instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CustomerServiceFee persistentInstance) {
		log.debug("deleting CustomerServiceFee instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CustomerServiceFee findById(java.lang.Integer id) {
		log.debug("getting CustomerServiceFee instance with id: " + id);
		try {
			CustomerServiceFee instance = (CustomerServiceFee) getSession()
					.get("com.jlyw.hibernate.crm.CustomerServiceFee", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CustomerServiceFee instance) {
		log.debug("finding CustomerServiceFee instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.crm.CustomerServiceFee")
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
		log.debug("finding CustomerServiceFee instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CustomerServiceFee as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByBillNum(Object billNum) {
		return findByProperty(BILL_NUM, billNum);
	}

	public List findByPaidVia(Object paidVia) {
		return findByProperty(PAID_VIA, paidVia);
	}

	public List findByPaidSubject(Object paidSubject) {
		return findByProperty(PAID_SUBJECT, paidSubject);
	}

	public List findByMoney(Object money) {
		return findByProperty(MONEY, money);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByAttachment(Object attachment) {
		return findByProperty(ATTACHMENT, attachment);
	}

	public List findAll() {
		log.debug("finding all CustomerServiceFee instances");
		try {
			String queryString = "from CustomerServiceFee";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CustomerServiceFee merge(CustomerServiceFee detachedInstance) {
		log.debug("merging CustomerServiceFee instance");
		try {
			CustomerServiceFee result = (CustomerServiceFee) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CustomerServiceFee instance) {
		log.debug("attaching dirty CustomerServiceFee instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CustomerServiceFee instance) {
		log.debug("attaching clean CustomerServiceFee instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}