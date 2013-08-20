package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * VehicleMission entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.VehicleMission
 * @author MyEclipse Persistence Tools
 */

public class VehicleMissionDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(VehicleMissionDAO.class);

	// property constants

	public void save(VehicleMission transientInstance) {
		log.debug("saving VehicleMission instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(VehicleMission persistentInstance) {
		log.debug("deleting VehicleMission instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public VehicleMission findById(java.lang.Integer id) {
		log.debug("getting VehicleMission instance with id: " + id);
		try {
			VehicleMission instance = (VehicleMission) getSession().get(
					"com.jlyw.hibernate.VehicleMission", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(VehicleMission instance) {
		log.debug("finding VehicleMission instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.VehicleMission").add(
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
		log.debug("finding VehicleMission instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from VehicleMission as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all VehicleMission instances");
		try {
			String queryString = "from VehicleMission";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public VehicleMission merge(VehicleMission detachedInstance) {
		log.debug("merging VehicleMission instance");
		try {
			VehicleMission result = (VehicleMission) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(VehicleMission instance) {
		log.debug("attaching dirty VehicleMission instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(VehicleMission instance) {
		log.debug("attaching clean VehicleMission instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}