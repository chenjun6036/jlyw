package com.jlyw.hibernate;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * RepairRecord entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.RepairRecord
 * @author MyEclipse Persistence Tools
 */

public class RepairRecordDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(RepairRecordDAO.class);
	// property constants
	public static final String REPAIR_ITEM_ID = "repairItemId";
	public static final String FEE = "fee";
	public static final String PART_NAME = "partName";
	public static final String PART_FEE = "partFee";

	public void save(RepairRecord transientInstance) {
		log.debug("saving RepairRecord instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(RepairRecord persistentInstance) {
		log.debug("deleting RepairRecord instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RepairRecord findById(java.lang.Integer id) {
		log.debug("getting RepairRecord instance with id: " + id);
		try {
			RepairRecord instance = (RepairRecord) getSession().get(
					"com.jlyw.hibernate.RepairRecord", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(RepairRecord instance) {
		log.debug("finding RepairRecord instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.RepairRecord").add(
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
		log.debug("finding RepairRecord instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from RepairRecord as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRepairItemId(Object repairItemId) {
		return findByProperty(REPAIR_ITEM_ID, repairItemId);
	}

	public List findByFee(Object fee) {
		return findByProperty(FEE, fee);
	}

	public List findByPartName(Object partName) {
		return findByProperty(PART_NAME, partName);
	}

	public List findByPartFee(Object partFee) {
		return findByProperty(PART_FEE, partFee);
	}

	public List findAll() {
		log.debug("finding all RepairRecord instances");
		try {
			String queryString = "from RepairRecord";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public RepairRecord merge(RepairRecord detachedInstance) {
		log.debug("merging RepairRecord instance");
		try {
			RepairRecord result = (RepairRecord) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(RepairRecord instance) {
		log.debug("attaching dirty RepairRecord instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RepairRecord instance) {
		log.debug("attaching clean RepairRecord instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}