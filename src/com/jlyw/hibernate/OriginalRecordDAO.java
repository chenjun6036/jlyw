package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * OriginalRecord entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.OriginalRecord
 * @author MyEclipse Persistence Tools
 */

public class OriginalRecordDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(OriginalRecordDAO.class);
	// property constants
	public static final String MODEL = "model";
	public static final String RANGE = "range";
	public static final String MANUFACTURER = "manufacturer";
	public static final String APPLIANCE_CODE = "applianceCode";
	public static final String ACCURACY = "accuracy";
	public static final String WORK_TYPE = "workType";
	public static final String WORK_LOCATION = "workLocation";
	public static final String TEMP = "temp";
	public static final String HUMIDITY = "humidity";
	public static final String PRESSURE = "pressure";
	public static final String CONCLUSION = "conclusion";
	public static final String TEST_FEE = "testFee";
	public static final String REPAIR_FEE = "repairFee";
	public static final String MATERIAL_FEE = "materialFee";
	public static final String CAR_FEE = "carFee";
	public static final String DEBUG_FEE = "debugFee";
	public static final String OTHER_FEE = "otherFee";
	public static final String TOTAL_FEE = "totalFee";
	public static final String REMARK = "remark";
	public static final String MANAGE_CODE = "manageCode";
	public static final String STATUS = "status";
	public static final String REPAIR_LEVEL = "repairLevel";
	public static final String REPAIR_CONTENT = "repairContent";
	public static final String MATERIAL_DETAIL = "materialDetail";
	public static final String MANDATORY = "mandatory";
	public static final String MANDATORY_CODE = "mandatoryCode";
	public static final String MANDATORY_ITEM_TYPE = "mandatoryItemType";
	public static final String MANDATORY_TYPE = "mandatoryType";
	public static final String MANDATORY_PURPOSE = "mandatoryPurpose";
	public static final String MANDATORY_APPLY_PLACE = "mandatoryApplyPlace";
	public static final String QUANTITY = "quantity";
	public static final String APPLIANCE_NAME = "applianceName";
	public static final String OTHER_COND = "otherCond";
	public static final String STD_OR_STD_APP_USAGE_STATUS = "stdOrStdAppUsageStatus";
	public static final String ABNORMAL_DESC = "abnormalDesc";
	public static final String FIRST_IS_UNQUALIFIED = "firstIsUnqualified";
	public static final String UNQUALIFIED_REASON = "unqualifiedReason";

	public void save(OriginalRecord transientInstance) {
		log.debug("saving OriginalRecord instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(OriginalRecord persistentInstance) {
		log.debug("deleting OriginalRecord instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OriginalRecord findById(java.lang.Integer id) {
		log.debug("getting OriginalRecord instance with id: " + id);
		try {
			OriginalRecord instance = (OriginalRecord) getSession().get(
					"com.jlyw.hibernate.OriginalRecord", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(OriginalRecord instance) {
		log.debug("finding OriginalRecord instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.OriginalRecord").add(
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
		log.debug("finding OriginalRecord instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from OriginalRecord as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByModel(Object model) {
		return findByProperty(MODEL, model);
	}

	public List findByRange(Object range) {
		return findByProperty(RANGE, range);
	}

	public List findByManufacturer(Object manufacturer) {
		return findByProperty(MANUFACTURER, manufacturer);
	}

	public List findByApplianceCode(Object applianceCode) {
		return findByProperty(APPLIANCE_CODE, applianceCode);
	}

	public List findByAccuracy(Object accuracy) {
		return findByProperty(ACCURACY, accuracy);
	}

	public List findByWorkType(Object workType) {
		return findByProperty(WORK_TYPE, workType);
	}

	public List findByWorkLocation(Object workLocation) {
		return findByProperty(WORK_LOCATION, workLocation);
	}

	public List findByTemp(Object temp) {
		return findByProperty(TEMP, temp);
	}

	public List findByHumidity(Object humidity) {
		return findByProperty(HUMIDITY, humidity);
	}

	public List findByPressure(Object pressure) {
		return findByProperty(PRESSURE, pressure);
	}

	public List findByConclusion(Object conclusion) {
		return findByProperty(CONCLUSION, conclusion);
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

	public List findByManageCode(Object manageCode) {
		return findByProperty(MANAGE_CODE, manageCode);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByRepairLevel(Object repairLevel) {
		return findByProperty(REPAIR_LEVEL, repairLevel);
	}

	public List findByRepairContent(Object repairContent) {
		return findByProperty(REPAIR_CONTENT, repairContent);
	}

	public List findByMaterialDetail(Object materialDetail) {
		return findByProperty(MATERIAL_DETAIL, materialDetail);
	}

	public List findByMandatory(Object mandatory) {
		return findByProperty(MANDATORY, mandatory);
	}

	public List findByMandatoryCode(Object mandatoryCode) {
		return findByProperty(MANDATORY_CODE, mandatoryCode);
	}

	public List findByMandatoryItemType(Object mandatoryItemType) {
		return findByProperty(MANDATORY_ITEM_TYPE, mandatoryItemType);
	}

	public List findByMandatoryType(Object mandatoryType) {
		return findByProperty(MANDATORY_TYPE, mandatoryType);
	}

	public List findByMandatoryPurpose(Object mandatoryPurpose) {
		return findByProperty(MANDATORY_PURPOSE, mandatoryPurpose);
	}

	public List findByMandatoryApplyPlace(Object mandatoryApplyPlace) {
		return findByProperty(MANDATORY_APPLY_PLACE, mandatoryApplyPlace);
	}

	public List findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List findByApplianceName(Object applianceName) {
		return findByProperty(APPLIANCE_NAME, applianceName);
	}

	public List findByOtherCond(Object otherCond) {
		return findByProperty(OTHER_COND, otherCond);
	}

	public List findByStdOrStdAppUsageStatus(Object stdOrStdAppUsageStatus) {
		return findByProperty(STD_OR_STD_APP_USAGE_STATUS,
				stdOrStdAppUsageStatus);
	}

	public List findByAbnormalDesc(Object abnormalDesc) {
		return findByProperty(ABNORMAL_DESC, abnormalDesc);
	}

	public List findByFirstIsUnqualified(Object firstIsUnqualified) {
		return findByProperty(FIRST_IS_UNQUALIFIED, firstIsUnqualified);
	}

	public List findByUnqualifiedReason(Object unqualifiedReason) {
		return findByProperty(UNQUALIFIED_REASON, unqualifiedReason);
	}

	public List findAll() {
		log.debug("finding all OriginalRecord instances");
		try {
			String queryString = "from OriginalRecord";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public OriginalRecord merge(OriginalRecord detachedInstance) {
		log.debug("merging OriginalRecord instance");
		try {
			OriginalRecord result = (OriginalRecord) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OriginalRecord instance) {
		log.debug("attaching dirty OriginalRecord instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OriginalRecord instance) {
		log.debug("attaching clean OriginalRecord instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}