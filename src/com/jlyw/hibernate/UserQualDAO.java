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
 * UserQual entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.UserQual
 * @author MyEclipse Persistence Tools
 */

public class UserQualDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(UserQualDAO.class);
	// property constants
	public static final String TYPE = "type";
	public static final String QUAL_NUM = "qualNum";
	public static final String AUTH_ITEMS = "authItems";
	public static final String AUTH_DEPT = "authDept";
	public static final String REMARK = "remark";
	public static final String WARN_SLOT = "warnSlot";

	public void save(UserQual transientInstance) {
		log.debug("saving UserQual instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(UserQual persistentInstance) {
		log.debug("deleting UserQual instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UserQual findById(java.lang.Integer id) {
		log.debug("getting UserQual instance with id: " + id);
		try {
			UserQual instance = (UserQual) getSession().get(
					"com.jlyw.hibernate.UserQual", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(UserQual instance) {
		log.debug("finding UserQual instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.UserQual")
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
		log.debug("finding UserQual instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from UserQual as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List findByQualNum(Object qualNum) {
		return findByProperty(QUAL_NUM, qualNum);
	}

	public List findByAuthItems(Object authItems) {
		return findByProperty(AUTH_ITEMS, authItems);
	}

	public List findByAuthDept(Object authDept) {
		return findByProperty(AUTH_DEPT, authDept);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByWarnSlot(Object warnSlot) {
		return findByProperty(WARN_SLOT, warnSlot);
	}

	public List findAll() {
		log.debug("finding all UserQual instances");
		try {
			String queryString = "from UserQual";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public UserQual merge(UserQual detachedInstance) {
		log.debug("merging UserQual instance");
		try {
			UserQual result = (UserQual) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(UserQual instance) {
		log.debug("attaching dirty UserQual instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UserQual instance) {
		log.debug("attaching clean UserQual instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}