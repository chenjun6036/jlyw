package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * DiscountComSheet entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.DiscountComSheet
 * @author MyEclipse Persistence Tools
 */

public class DiscountComSheetDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DiscountComSheetDAO.class);
	// property constants
	public static final String TEST_FEE = "testFee";
	public static final String MATERIAL_FEE = "materialFee";
	public static final String REPAIR_FEE = "repairFee";
	public static final String CAR_FEE = "carFee";
	public static final String DEBUG_FEE = "debugFee";
	public static final String OTHER_FEE = "otherFee";
	public static final String TOTAL_FEE = "totalFee";
	public static final String OLD_TEST_FEE = "oldTestFee";
	public static final String OLD_MATERIAL_FEE = "oldMaterialFee";
	public static final String OLD_REPAIR_FEE = "oldRepairFee";
	public static final String OLD_CAR_FEE = "oldCarFee";
	public static final String OLD_DEBUG_FEE = "oldDebugFee";
	public static final String OLD_OTHER_FEE = "oldOtherFee";
	public static final String OLD_TOTAL_FEE = "oldTotalFee";
	public static final String REMARK = "remark";

	public void save(DiscountComSheet transientInstance) {
		log.debug("saving DiscountComSheet instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DiscountComSheet persistentInstance) {
		log.debug("deleting DiscountComSheet instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DiscountComSheet findById(java.lang.Integer id) {
		log.debug("getting DiscountComSheet instance with id: " + id);
		try {
			DiscountComSheet instance = (DiscountComSheet) getSession().get(
					"com.jlyw.hibernate.DiscountComSheet", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DiscountComSheet instance) {
		log.debug("finding DiscountComSheet instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.DiscountComSheet").add(
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
		log.debug("finding DiscountComSheet instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from DiscountComSheet as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByTestFee(Object testFee) {
		return findByProperty(TEST_FEE, testFee);
	}

	public List findByMaterialFee(Object materialFee) {
		return findByProperty(MATERIAL_FEE, materialFee);
	}

	public List findByRepairFee(Object repairFee) {
		return findByProperty(REPAIR_FEE, repairFee);
	}

	public List findByCarFee(Object carFee) {
		return findByProperty(CAR_FEE, carFee);
	}

	public List findByDebugFee(Object debugFee) {
		return findByProperty(DEBUG_FEE, debugFee);
	}

	public List findByOtherFee(Object otherFee) {
		return findByProperty(OTHER_FEE, otherFee);
	}

	public List findByTotalFee(Object totalFee) {
		return findByProperty(TOTAL_FEE, totalFee);
	}

	public List findByOldTestFee(Object oldTestFee) {
		return findByProperty(OLD_TEST_FEE, oldTestFee);
	}

	public List findByOldMaterialFee(Object oldMaterialFee) {
		return findByProperty(OLD_MATERIAL_FEE, oldMaterialFee);
	}

	public List findByOldRepairFee(Object oldRepairFee) {
		return findByProperty(OLD_REPAIR_FEE, oldRepairFee);
	}

	public List findByOldCarFee(Object oldCarFee) {
		return findByProperty(OLD_CAR_FEE, oldCarFee);
	}

	public List findByOldDebugFee(Object oldDebugFee) {
		return findByProperty(OLD_DEBUG_FEE, oldDebugFee);
	}

	public List findByOldOtherFee(Object oldOtherFee) {
		return findByProperty(OLD_OTHER_FEE, oldOtherFee);
	}

	public List findByOldTotalFee(Object oldTotalFee) {
		return findByProperty(OLD_TOTAL_FEE, oldTotalFee);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findAll() {
		log.debug("finding all DiscountComSheet instances");
		try {
			String queryString = "from DiscountComSheet";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DiscountComSheet merge(DiscountComSheet detachedInstance) {
		log.debug("merging DiscountComSheet instance");
		try {
			DiscountComSheet result = (DiscountComSheet) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DiscountComSheet instance) {
		log.debug("attaching dirty DiscountComSheet instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DiscountComSheet instance) {
		log.debug("attaching clean DiscountComSheet instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}