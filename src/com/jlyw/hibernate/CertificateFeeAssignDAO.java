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
 * CertificateFeeAssign entities. Transaction control of the save(), update()
 * and delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.CertificateFeeAssign
 * @author MyEclipse Persistence Tools
 */

public class CertificateFeeAssignDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CertificateFeeAssignDAO.class);
	// property constants
	public static final String TEST_FEE = "testFee";
	public static final String REPAIR_FEE = "repairFee";
	public static final String MATERIAL_FEE = "materialFee";
	public static final String CAR_FEE = "carFee";
	public static final String DEBUG_FEE = "debugFee";
	public static final String OTHER_FEE = "otherFee";
	public static final String TOTAL_FEE = "totalFee";
	public static final String REMARK = "remark";
	public static final String TEST_FEE_OLD = "testFeeOld";
	public static final String REPAIR_FEE_OLD = "repairFeeOld";
	public static final String MATERIAL_FEE_OLD = "materialFeeOld";
	public static final String CAR_FEE_OLD = "carFeeOld";
	public static final String DEBUG_FEE_OLD = "debugFeeOld";
	public static final String OTHER_FEE_OLD = "otherFeeOld";
	public static final String TOTAL_FEE_OLD = "totalFeeOld";

	public void save(CertificateFeeAssign transientInstance) {
		log.debug("saving CertificateFeeAssign instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CertificateFeeAssign persistentInstance) {
		log.debug("deleting CertificateFeeAssign instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CertificateFeeAssign findById(java.lang.Integer id) {
		log.debug("getting CertificateFeeAssign instance with id: " + id);
		try {
			CertificateFeeAssign instance = (CertificateFeeAssign) getSession()
					.get("com.jlyw.hibernate.CertificateFeeAssign", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CertificateFeeAssign instance) {
		log.debug("finding CertificateFeeAssign instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.CertificateFeeAssign").add(
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
		log.debug("finding CertificateFeeAssign instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CertificateFeeAssign as model where model."
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

	public List findByRepairFee(Object repairFee) {
		return findByProperty(REPAIR_FEE, repairFee);
	}

	public List findByMaterialFee(Object materialFee) {
		return findByProperty(MATERIAL_FEE, materialFee);
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

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByTestFeeOld(Object testFeeOld) {
		return findByProperty(TEST_FEE_OLD, testFeeOld);
	}

	public List findByRepairFeeOld(Object repairFeeOld) {
		return findByProperty(REPAIR_FEE_OLD, repairFeeOld);
	}

	public List findByMaterialFeeOld(Object materialFeeOld) {
		return findByProperty(MATERIAL_FEE_OLD, materialFeeOld);
	}

	public List findByCarFeeOld(Object carFeeOld) {
		return findByProperty(CAR_FEE_OLD, carFeeOld);
	}

	public List findByDebugFeeOld(Object debugFeeOld) {
		return findByProperty(DEBUG_FEE_OLD, debugFeeOld);
	}

	public List findByOtherFeeOld(Object otherFeeOld) {
		return findByProperty(OTHER_FEE_OLD, otherFeeOld);
	}

	public List findByTotalFeeOld(Object totalFeeOld) {
		return findByProperty(TOTAL_FEE_OLD, totalFeeOld);
	}

	public List findAll() {
		log.debug("finding all CertificateFeeAssign instances");
		try {
			String queryString = "from CertificateFeeAssign";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CertificateFeeAssign merge(CertificateFeeAssign detachedInstance) {
		log.debug("merging CertificateFeeAssign instance");
		try {
			CertificateFeeAssign result = (CertificateFeeAssign) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CertificateFeeAssign instance) {
		log.debug("attaching dirty CertificateFeeAssign instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CertificateFeeAssign instance) {
		log.debug("attaching clean CertificateFeeAssign instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}