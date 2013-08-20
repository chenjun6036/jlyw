package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * LocaleApplianceItem entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.LocaleApplianceItem
 * @author MyEclipse Persistence Tools
 */

public class LocaleApplianceItemDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(LocaleApplianceItemDAO.class);
	// property constants
	public static final String SPECIES_TYPE = "speciesType";
	public static final String APPLIANCE_SPECIES_ID = "applianceSpeciesId";
	public static final String APPLIANCE_NAME = "applianceName";
	public static final String QUANTITY = "quantity";
	public static final String ASSIST_STAFF = "assistStaff";
	public static final String REMARK = "remark";
	public static final String MODEL = "model";
	public static final String RANGE = "range";
	public static final String ACCURACY = "accuracy";
	public static final String TEST_COST = "testCost";
	public static final String CERT_TYPE = "certType";
	public static final String APP_FACTORY_CODE = "appFactoryCode";
	public static final String APP_MANAGE_CODE = "appManageCode";
	public static final String MANUFACTURER = "manufacturer";
	public static final String REPAIR_COST = "repairCost";
	public static final String MATERIAL_COST = "materialCost";

	public void save(LocaleApplianceItem transientInstance) {
		log.debug("saving LocaleApplianceItem instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(LocaleApplianceItem persistentInstance) {
		log.debug("deleting LocaleApplianceItem instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LocaleApplianceItem findById(java.lang.Integer id) {
		log.debug("getting LocaleApplianceItem instance with id: " + id);
		try {
			LocaleApplianceItem instance = (LocaleApplianceItem) getSession()
					.get("com.jlyw.hibernate.LocaleApplianceItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(LocaleApplianceItem instance) {
		log.debug("finding LocaleApplianceItem instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.LocaleApplianceItem").add(
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
		log.debug("finding LocaleApplianceItem instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from LocaleApplianceItem as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySpeciesType(Object speciesType) {
		return findByProperty(SPECIES_TYPE, speciesType);
	}

	public List findByApplianceSpeciesId(Object applianceSpeciesId) {
		return findByProperty(APPLIANCE_SPECIES_ID, applianceSpeciesId);
	}

	public List findByApplianceName(Object applianceName) {
		return findByProperty(APPLIANCE_NAME, applianceName);
	}

	public List findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List findByAssistStaff(Object assistStaff) {
		return findByProperty(ASSIST_STAFF, assistStaff);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByModel(Object model) {
		return findByProperty(MODEL, model);
	}

	public List findByRange(Object range) {
		return findByProperty(RANGE, range);
	}

	public List findByAccuracy(Object accuracy) {
		return findByProperty(ACCURACY, accuracy);
	}

	public List findByTestCost(Object testCost) {
		return findByProperty(TEST_COST, testCost);
	}

	public List findByCertType(Object certType) {
		return findByProperty(CERT_TYPE, certType);
	}

	public List findByAppFactoryCode(Object appFactoryCode) {
		return findByProperty(APP_FACTORY_CODE, appFactoryCode);
	}

	public List findByAppManageCode(Object appManageCode) {
		return findByProperty(APP_MANAGE_CODE, appManageCode);
	}

	public List findByManufacturer(Object manufacturer) {
		return findByProperty(MANUFACTURER, manufacturer);
	}

	public List findByRepairCost(Object repairCost) {
		return findByProperty(REPAIR_COST, repairCost);
	}

	public List findByMaterialCost(Object materialCost) {
		return findByProperty(MATERIAL_COST, materialCost);
	}

	public List findAll() {
		log.debug("finding all LocaleApplianceItem instances");
		try {
			String queryString = "from LocaleApplianceItem";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public LocaleApplianceItem merge(LocaleApplianceItem detachedInstance) {
		log.debug("merging LocaleApplianceItem instance");
		try {
			LocaleApplianceItem result = (LocaleApplianceItem) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(LocaleApplianceItem instance) {
		log.debug("attaching dirty LocaleApplianceItem instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LocaleApplianceItem instance) {
		log.debug("attaching clean LocaleApplianceItem instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}