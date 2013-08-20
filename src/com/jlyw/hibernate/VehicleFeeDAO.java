package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * VehicleFee entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.VehicleFee
 * @author MyEclipse Persistence Tools
 */

public class VehicleFeeDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(VehicleFeeDAO.class);
	// property constants
	public static final String FEE = "fee";
	public static final String REMARK = "remark";

	public void save(VehicleFee transientInstance) {
		log.debug("saving VehicleFee instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(VehicleFee persistentInstance) {
		log.debug("deleting VehicleFee instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public VehicleFee findById(java.lang.Integer id) {
		log.debug("getting VehicleFee instance with id: " + id);
		try {
			VehicleFee instance = (VehicleFee) getSession().get(
					"com.jlyw.hibernate.VehicleFee", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(VehicleFee instance) {
		log.debug("finding VehicleFee instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.VehicleFee").add(
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
		log.debug("finding VehicleFee instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from VehicleFee as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByFee(Object fee) {
		return findByProperty(FEE, fee);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all VehicleFee instances");
		try {
			String queryString = "from VehicleFee";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public VehicleFee merge(VehicleFee detachedInstance) {
		log.debug("merging VehicleFee instance");
		try {
			VehicleFee result = (VehicleFee) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(VehicleFee instance) {
		log.debug("attaching dirty VehicleFee instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(VehicleFee instance) {
		log.debug("attaching clean VehicleFee instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}