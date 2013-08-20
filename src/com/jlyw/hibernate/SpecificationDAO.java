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
 * Specification entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Specification
 * @author MyEclipse Persistence Tools
 */

public class SpecificationDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(SpecificationDAO.class);
	// property constants
	public static final String SPECIFICATION_CODE = "specificationCode";
	public static final String NAME_CN = "nameCn";
	public static final String NAME_EN = "nameEn";
	public static final String BRIEF = "brief";
	public static final String TYPE = "type";
	public static final String IN_CHARGE = "inCharge";
	public static final String STATUS = "status";
	public static final String LOCALE_CODE = "localeCode";
	public static final String OLD_SPECIFICATION = "oldSpecification";
	public static final String COPY = "copy";
	public static final String METHOD_CONFIRM = "methodConfirm";
	public static final String REMARK = "remark";
	public static final String WARN_SLOT = "warnSlot";

	public void save(Specification transientInstance) {
		log.debug("saving Specification instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Specification persistentInstance) {
		log.debug("deleting Specification instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Specification findById(java.lang.Integer id) {
		log.debug("getting Specification instance with id: " + id);
		try {
			Specification instance = (Specification) getSession().get(
					"com.jlyw.hibernate.Specification", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Specification instance) {
		log.debug("finding Specification instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.Specification")
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
		log.debug("finding Specification instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Specification as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySpecificationCode(Object specificationCode) {
		return findByProperty(SPECIFICATION_CODE, specificationCode);
	}

	public List findByNameCn(Object nameCn) {
		return findByProperty(NAME_CN, nameCn);
	}

	public List findByNameEn(Object nameEn) {
		return findByProperty(NAME_EN, nameEn);
	}

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByInCharge(Object inCharge) {
		return findByProperty(IN_CHARGE, inCharge);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByLocaleCode(Object localeCode) {
		return findByProperty(LOCALE_CODE, localeCode);
	}

	public List findByOldSpecification(Object oldSpecification) {
		return findByProperty(OLD_SPECIFICATION, oldSpecification);
	}

	public List findByCopy(Object copy) {
		return findByProperty(COPY, copy);
	}

	public List findByMethodConfirm(Object methodConfirm) {
		return findByProperty(METHOD_CONFIRM, methodConfirm);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByWarnSlot(Object warnSlot) {
		return findByProperty(WARN_SLOT, warnSlot);
	}

	public List findAll() {
		log.debug("finding all Specification instances");
		try {
			String queryString = "from Specification";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Specification merge(Specification detachedInstance) {
		log.debug("merging Specification instance");
		try {
			Specification result = (Specification) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Specification instance) {
		log.debug("attaching dirty Specification instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Specification instance) {
		log.debug("attaching clean Specification instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}