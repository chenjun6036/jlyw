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
 * InsideContactorFeeAssign entities. Transaction control of the save(),
 * update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see com.jlyw.hibernate.crm.InsideContactorFeeAssign
 * @author MyEclipse Persistence Tools
 */
public class InsideContactorFeeAssignDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(InsideContactorFeeAssignDAO.class);
	// property constants
	public static final String YEAR = "year";
	public static final String FEE = "fee";
	public static final String REMARK = "remark";

	public void save(InsideContactorFeeAssign transientInstance) {
		log.debug("saving InsideContactorFeeAssign instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(InsideContactorFeeAssign persistentInstance) {
		log.debug("deleting InsideContactorFeeAssign instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public InsideContactorFeeAssign findById(java.lang.Integer id) {
		log.debug("getting InsideContactorFeeAssign instance with id: " + id);
		try {
			InsideContactorFeeAssign instance = (InsideContactorFeeAssign) getSession()
					.get("com.jlyw.hibernate.crm.InsideContactorFeeAssign", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(InsideContactorFeeAssign instance) {
		log.debug("finding InsideContactorFeeAssign instance by example");
		try {
			List results = getSession()
					.createCriteria(
							"com.jlyw.hibernate.crm.InsideContactorFeeAssign")
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
		log.debug("finding InsideContactorFeeAssign instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from InsideContactorFeeAssign as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByYear(Object year) {
		return findByProperty(YEAR, year);
	}

	public List findByFee(Object fee) {
		return findByProperty(FEE, fee);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all InsideContactorFeeAssign instances");
		try {
			String queryString = "from InsideContactorFeeAssign";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public InsideContactorFeeAssign merge(
			InsideContactorFeeAssign detachedInstance) {
		log.debug("merging InsideContactorFeeAssign instance");
		try {
			InsideContactorFeeAssign result = (InsideContactorFeeAssign) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(InsideContactorFeeAssign instance) {
		log.debug("attaching dirty InsideContactorFeeAssign instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(InsideContactorFeeAssign instance) {
		log.debug("attaching clean InsideContactorFeeAssign instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}