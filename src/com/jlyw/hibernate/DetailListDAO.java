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
 * DetailList entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.DetailList
 * @author MyEclipse Persistence Tools
 */

public class DetailListDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DetailListDAO.class);
	// property constants
	public static final String INVOICE_CODE = "invoiceCode";
	public static final String CODE = "code";
	public static final String TOTAL_FEE = "totalFee";
	public static final String PAID_FEE = "paidFee";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";
	public static final String VERSION = "version";
	public static final String CASH_PAID = "cashPaid";
	public static final String CHEQUE_PAID = "chequePaid";
	public static final String ACCOUNT_PAID = "accountPaid";

	public void save(DetailList transientInstance) {
		log.debug("saving DetailList instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DetailList persistentInstance) {
		log.debug("deleting DetailList instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetailList findById(java.lang.Integer id) {
		log.debug("getting DetailList instance with id: " + id);
		try {
			DetailList instance = (DetailList) getSession().get(
					"com.jlyw.hibernate.DetailList", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DetailList instance) {
		log.debug("finding DetailList instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.DetailList").add(
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
		log.debug("finding DetailList instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DetailList as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByInvoiceCode(Object invoiceCode) {
		return findByProperty(INVOICE_CODE, invoiceCode);
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByTotalFee(Object totalFee) {
		return findByProperty(TOTAL_FEE, totalFee);
	}

	public List findByPaidFee(Object paidFee) {
		return findByProperty(PAID_FEE, paidFee);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findByCashPaid(Object cashPaid) {
		return findByProperty(CASH_PAID, cashPaid);
	}

	public List findByChequePaid(Object chequePaid) {
		return findByProperty(CHEQUE_PAID, chequePaid);
	}

	public List findByAccountPaid(Object accountPaid) {
		return findByProperty(ACCOUNT_PAID, accountPaid);
	}

	public List findAll() {
		log.debug("finding all DetailList instances");
		try {
			String queryString = "from DetailList";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DetailList merge(DetailList detachedInstance) {
		log.debug("merging DetailList instance");
		try {
			DetailList result = (DetailList) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DetailList instance) {
		log.debug("attaching dirty DetailList instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetailList instance) {
		log.debug("attaching clean DetailList instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}