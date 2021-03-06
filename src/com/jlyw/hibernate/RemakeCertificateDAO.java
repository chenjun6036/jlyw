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
 * RemakeCertificate entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.RemakeCertificate
 * @author MyEclipse Persistence Tools
 */

public class RemakeCertificateDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(RemakeCertificateDAO.class);
	// property constants
	public static final String CERTIFICATE_CODE = "certificateCode";
	public static final String CREATE_REMARK = "createRemark";
	public static final String FINISH_REMARK = "finishRemark";

	public void save(RemakeCertificate transientInstance) {
		log.debug("saving RemakeCertificate instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RemakeCertificate persistentInstance) {
		log.debug("deleting RemakeCertificate instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RemakeCertificate findById(java.lang.Integer id) {
		log.debug("getting RemakeCertificate instance with id: " + id);
		try {
			RemakeCertificate instance = (RemakeCertificate) getSession().get(
					"com.jlyw.hibernate.RemakeCertificate", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RemakeCertificate instance) {
		log.debug("finding RemakeCertificate instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.RemakeCertificate").add(
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
		log.debug("finding RemakeCertificate instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RemakeCertificate as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCertificateCode(Object certificateCode) {
		return findByProperty(CERTIFICATE_CODE, certificateCode);
	}

	public List findByCreateRemark(Object createRemark) {
		return findByProperty(CREATE_REMARK, createRemark);
	}

	public List findByFinishRemark(Object finishRemark) {
		return findByProperty(FINISH_REMARK, finishRemark);
	}

	public List findAll() {
		log.debug("finding all RemakeCertificate instances");
		try {
			String queryString = "from RemakeCertificate";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RemakeCertificate merge(RemakeCertificate detachedInstance) {
		log.debug("merging RemakeCertificate instance");
		try {
			RemakeCertificate result = (RemakeCertificate) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RemakeCertificate instance) {
		log.debug("attaching dirty RemakeCertificate instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RemakeCertificate instance) {
		log.debug("attaching clean RemakeCertificate instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}