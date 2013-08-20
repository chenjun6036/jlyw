package com.jlyw.hibernate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * OriginalRecordExcel entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.OriginalRecordExcel
 * @author MyEclipse Persistence Tools
 */

public class OriginalRecordExcelDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(OriginalRecordExcelDAO.class);
	// property constants
	public static final String CODE = "code";
	public static final String VERSION = "version";
	public static final String PDF = "pdf";
	public static final String DOC = "doc";
	public static final String XML = "xml";
	public static final String FILE_NAME = "fileName";
	public static final String CERTIFICATE_CODE = "certificateCode";

	public void save(OriginalRecordExcel transientInstance) {
		log.debug("saving OriginalRecordExcel instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OriginalRecordExcel persistentInstance) {
		log.debug("deleting OriginalRecordExcel instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OriginalRecordExcel findById(java.lang.Integer id) {
		log.debug("getting OriginalRecordExcel instance with id: " + id);
		try {
			OriginalRecordExcel instance = (OriginalRecordExcel) getSession()
					.get("com.jlyw.hibernate.OriginalRecordExcel", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OriginalRecordExcel instance) {
		log.debug("finding OriginalRecordExcel instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.OriginalRecordExcel").add(
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
		log.debug("finding OriginalRecordExcel instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from OriginalRecordExcel as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findByPdf(Object pdf) {
		return findByProperty(PDF, pdf);
	}

	public List findByDoc(Object doc) {
		return findByProperty(DOC, doc);
	}

	public List findByXml(Object xml) {
		return findByProperty(XML, xml);
	}

	public List findByFileName(Object fileName) {
		return findByProperty(FILE_NAME, fileName);
	}

	public List findByCertificateCode(Object certificateCode) {
		return findByProperty(CERTIFICATE_CODE, certificateCode);
	}

	public List findAll() {
		log.debug("finding all OriginalRecordExcel instances");
		try {
			String queryString = "from OriginalRecordExcel";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OriginalRecordExcel merge(OriginalRecordExcel detachedInstance) {
		log.debug("merging OriginalRecordExcel instance");
		try {
			OriginalRecordExcel result = (OriginalRecordExcel) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OriginalRecordExcel instance) {
		log.debug("attaching dirty OriginalRecordExcel instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OriginalRecordExcel instance) {
		log.debug("attaching clean OriginalRecordExcel instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}