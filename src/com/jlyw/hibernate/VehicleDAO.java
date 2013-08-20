package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Vehicle entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Vehicle
 * @author MyEclipse Persistence Tools
 */

public class VehicleDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(VehicleDAO.class);
	// property constants
	public static final String LICENCE = "licence";
	public static final String LIMIT = "limit";
	public static final String MODEL = "model";
	public static final String BRAND = "brand";
	public static final String LICENCE_TYPE = "licenceType";
	public static final String FUEL_FEE = "fuelFee";
	public static final String STATUS = "status";

	public void save(Vehicle transientInstance) {
		log.debug("saving Vehicle instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Vehicle persistentInstance) {
		log.debug("deleting Vehicle instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Vehicle findById(java.lang.Integer id) {
		log.debug("getting Vehicle instance with id: " + id);
		try {
			Vehicle instance = (Vehicle) getSession().get(
					"com.jlyw.hibernate.Vehicle", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Vehicle instance) {
		log.debug("finding Vehicle instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.Vehicle").add(Example.create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Vehicle instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Vehicle as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByLicence(Object licence) {
		return findByProperty(LICENCE, licence);
	}

	public List findByLimit(Object limit) {
		return findByProperty(LIMIT, limit);
	}

	public List findByModel(Object model) {
		return findByProperty(MODEL, model);
	}

	public List findByBrand(Object brand) {
		return findByProperty(BRAND, brand);
	}

	public List findByLicenceType(Object licenceType) {
		return findByProperty(LICENCE_TYPE, licenceType);
	}

	public List findByFuelFee(Object fuelFee) {
		return findByProperty(FUEL_FEE, fuelFee);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all Vehicle instances");
		try {
			String queryString = "from Vehicle";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Vehicle merge(Vehicle detachedInstance) {
		log.debug("merging Vehicle instance");
		try {
			Vehicle result = (Vehicle) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Vehicle instance) {
		log.debug("attaching dirty Vehicle instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Vehicle instance) {
		log.debug("attaching clean Vehicle instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}