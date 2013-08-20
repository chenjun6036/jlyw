package com.jlyw.hibernate;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * DealItem entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.DealItem
 * @author MyEclipse Persistence Tools
 */

public class DealItemDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(DealItemDAO.class);
	// property constants
	public static final String STANDARD_NAME = "standardName";
	public static final String MODEL = "model";
	public static final String RANGE = "range";
	public static final String ACCURACY = "accuracy";
	public static final String DEAL_PRICE = "dealPrice";
	public static final String APP_FACTORY_CODE = "appFactoryCode";
	public static final String APP_MANAGE_CODE = "appManageCode";
	public static final String MANUFACTURER = "manufacturer";
	public static final String REMARK = "remark";
	public static final String XH = "xh";

	public void save(DealItem transientInstance) {
		log.debug("saving DealItem instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(DealItem persistentInstance) {
		log.debug("deleting DealItem instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DealItem findById(java.lang.Integer id) {
		log.debug("getting DealItem instance with id: " + id);
		try {
			DealItem instance = (DealItem) getSession().get(
					"com.jlyw.hibernate.DealItem", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(DealItem instance) {
		log.debug("finding DealItem instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.DealItem")
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
		log.debug("finding DealItem instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from DealItem as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByStandardName(Object standardName) {
		return findByProperty(STANDARD_NAME, standardName);
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

	public List findByDealPrice(Object dealPrice) {
		return findByProperty(DEAL_PRICE, dealPrice);
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

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByXh(Object xh) {
		return findByProperty(XH, xh);
	}

	public List findAll() {
		log.debug("finding all DealItem instances");
		try {
			String queryString = "from DealItem";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public DealItem merge(DealItem detachedInstance) {
		log.debug("merging DealItem instance");
		try {
			DealItem result = (DealItem) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(DealItem instance) {
		log.debug("attaching dirty DealItem instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DealItem instance) {
		log.debug("attaching clean DealItem instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}