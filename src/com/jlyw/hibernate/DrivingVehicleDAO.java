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
 * DrivingVehicle entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.DrivingVehicle
 * @author MyEclipse Persistence Tools
 */

public class DrivingVehicleDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DrivingVehicleDAO.class);
	// property constants
	public static final String KILOMETERS = "kilometers";
	public static final String DESCRIPTION = "description";
	public static final String PEOPLE = "people";
	public static final String ASSEMBLING_PLACE = "assemblingPlace";
	public static final String TOTAL_FEE = "totalFee";
	public static final String STATUS = "status";

	public void save(DrivingVehicle transientInstance) {
		log.debug("saving DrivingVehicle instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DrivingVehicle persistentInstance) {
		log.debug("deleting DrivingVehicle instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DrivingVehicle findById(java.lang.Integer id) {
		log.debug("getting DrivingVehicle instance with id: " + id);
		try {
			DrivingVehicle instance = (DrivingVehicle) getSession().get(
					"com.jlyw.hibernate.DrivingVehicle", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DrivingVehicle instance) {
		log.debug("finding DrivingVehicle instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.DrivingVehicle").add(
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
		log.debug("finding DrivingVehicle instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DrivingVehicle as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByKilometers(Object kilometers) {
		return findByProperty(KILOMETERS, kilometers);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByPeople(Object people) {
		return findByProperty(PEOPLE, people);
	}

	public List findByAssemblingPlace(Object assemblingPlace) {
		return findByProperty(ASSEMBLING_PLACE, assemblingPlace);
	}

	public List findByTotalFee(Object totalFee) {
		return findByProperty(TOTAL_FEE, totalFee);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findAll() {
		log.debug("finding all DrivingVehicle instances");
		try {
			String queryString = "from DrivingVehicle";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DrivingVehicle merge(DrivingVehicle detachedInstance) {
		log.debug("merging DrivingVehicle instance");
		try {
			DrivingVehicle result = (DrivingVehicle) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DrivingVehicle instance) {
		log.debug("attaching dirty DrivingVehicle instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DrivingVehicle instance) {
		log.debug("attaching clean DrivingVehicle instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}