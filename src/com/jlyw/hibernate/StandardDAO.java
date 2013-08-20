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
 * Standard entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Standard
 * @author MyEclipse Persistence Tools
 */
public class StandardDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(StandardDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String NAME_EN = "nameEn";
	public static final String BRIEF = "brief";
	public static final String CERTIFICATE_CODE = "certificateCode";
	public static final String STANDARD_CODE = "standardCode";
	public static final String PROJECT_CODE = "projectCode";
	public static final String STATUS = "status";
	public static final String CREATED_BY = "createdBy";
	public static final String ISSUED_BY = "issuedBy";
	public static final String RANGE = "range";
	public static final String UNCERTAIN = "uncertain";
	public static final String SISSUED_BY = "sissuedBy";
	public static final String SCERTIFICATE_CODE = "scertificateCode";
	public static final String SREGION = "sregion";
	public static final String SAUTHORIZATION_CODE = "sauthorizationCode";
	public static final String SLOCALE_CODE = "slocaleCode";
	public static final String COPY = "copy";
	public static final String SCOPY = "scopy";
	public static final String REMARK = "remark";
	public static final String WARN_SLOT = "warnSlot";
	public static final String PROJECT_TYPE = "projectType";

	public void save(Standard transientInstance) {
		log.debug("saving Standard instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Standard persistentInstance) {
		log.debug("deleting Standard instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Standard findById(java.lang.Integer id) {
		log.debug("getting Standard instance with id: " + id);
		try {
			Standard instance = (Standard) getSession().get(
					"com.jlyw.hibernate.Standard", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Standard instance) {
		log.debug("finding Standard instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.Standard")
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
		log.debug("finding Standard instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Standard as model where model."
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

	public List findByCertificateCode(Object certificateCode) {
		return findByProperty(CERTIFICATE_CODE, certificateCode);
	}

	public List findByStandardCode(Object standardCode) {
		return findByProperty(STANDARD_CODE, standardCode);
	}

	public List findByProjectCode(Object projectCode) {
		return findByProperty(PROJECT_CODE, projectCode);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByCreatedBy(Object createdBy) {
		return findByProperty(CREATED_BY, createdBy);
	}

	public List findByIssuedBy(Object issuedBy) {
		return findByProperty(ISSUED_BY, issuedBy);
	}

	public List findByRange(Object range) {
		return findByProperty(RANGE, range);
	}

	public List findByUncertain(Object uncertain) {
		return findByProperty(UNCERTAIN, uncertain);
	}

	public List findBySissuedBy(Object sissuedBy) {
		return findByProperty(SISSUED_BY, sissuedBy);
	}

	public List findByScertificateCode(Object scertificateCode) {
		return findByProperty(SCERTIFICATE_CODE, scertificateCode);
	}

	public List findBySregion(Object sregion) {
		return findByProperty(SREGION, sregion);
	}

	public List findBySauthorizationCode(Object sauthorizationCode) {
		return findByProperty(SAUTHORIZATION_CODE, sauthorizationCode);
	}

	public List findBySlocaleCode(Object slocaleCode) {
		return findByProperty(SLOCALE_CODE, slocaleCode);
	}

	public List findByCopy(Object copy) {
		return findByProperty(COPY, copy);
	}

	public List findByScopy(Object scopy) {
		return findByProperty(SCOPY, scopy);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByWarnSlot(Object warnSlot) {
		return findByProperty(WARN_SLOT, warnSlot);
	}

	public List findByProjectType(Object projectType) {
		return findByProperty(PROJECT_TYPE, projectType);
	}

	public List findAll() {
		log.debug("finding all Standard instances");
		try {
			String queryString = "from Standard";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Standard merge(Standard detachedInstance) {
		log.debug("merging Standard instance");
		try {
			Standard result = (Standard) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Standard instance) {
		log.debug("attaching dirty Standard instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Standard instance) {
		log.debug("attaching clean Standard instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}