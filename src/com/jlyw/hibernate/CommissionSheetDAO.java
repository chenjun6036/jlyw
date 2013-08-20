package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * CommissionSheet entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.CommissionSheet
 * @author MyEclipse Persistence Tools
 */

public class CommissionSheetDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CommissionSheetDAO.class);
	// property constants
	public static final String CODE = "code";
	public static final String PWD = "pwd";
	public static final String CUSTOMER_ID = "customerId";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String CUSTOMER_TEL = "customerTel";
	public static final String CUSTOMER_ADDRESS = "customerAddress";
	public static final String CUSTOMER_ZIP_CODE = "customerZipCode";
	public static final String CUSTOMER_CONTACTOR = "customerContactor";
	public static final String CUSTOMER_CONTACTOR_TEL = "customerContactorTel";
	public static final String SAMPLE_FROM = "sampleFrom";
	public static final String BILLING_TO = "billingTo";
	public static final String SPECIES_TYPE = "speciesType";
	public static final String APPLIANCE_SPECIES_ID = "applianceSpeciesId";
	public static final String APPLIANCE_NAME = "applianceName";
	public static final String APP_FACTORY_CODE = "appFactoryCode";
	public static final String APP_MANAGE_CODE = "appManageCode";
	public static final String MANUFACTURER = "manufacturer";
	public static final String QUANTITY = "quantity";
	public static final String MANDATORY = "mandatory";
	public static final String MANDATORY_CODE = "mandatoryCode";
	public static final String APPEARANCE = "appearance";
	public static final String REPORT_TYPE = "reportType";
	public static final String OTHER_REQUIREMENTS = "otherRequirements";
	public static final String URGENT = "urgent";
	public static final String REPAIR = "repair";
	public static final String SUBCONTRACT = "subcontract";
	public static final String LOCATION = "location";
	public static final String ALLOTEE = "allotee";
	public static final String COMMISSION_TYPE = "commissionType";
	public static final String RECEIVER_ID = "receiverId";
	public static final String SAMPLE_ADDRESS = "sampleAddress";
	public static final String CREATOR_ID = "creatorId";
	public static final String REPORT_ADDRESS = "reportAddress";
	public static final String QUOTATION_ID = "quotationId";
	public static final String TEST_FEE = "testFee";
	public static final String REPAIR_FEE = "repairFee";
	public static final String MATERIAL_FEE = "materialFee";
	public static final String CAR_FEE = "carFee";
	public static final String DEBUG_FEE = "debugFee";
	public static final String OTHER_FEE = "otherFee";
	public static final String TOTAL_FEE = "totalFee";
	public static final String STATUS = "status";
	public static final String LOCALE_COMMISSION_CODE = "localeCommissionCode";
	public static final String LOCALE_STAFF_ID = "localeStaffId";
	public static final String FINISH_LOCATION = "finishLocation";
	public static final String FINISH_STAFF_ID = "finishStaffId";
	public static final String CANCEL_REQUESTER_ID = "cancelRequesterId";
	public static final String CANCEL_EXECUTER_ID = "cancelExecuterId";
	public static final String CANCEL_REASON = "cancelReason";
	public static final String INVOICE_CODE = "invoiceCode";
	public static final String REMARK = "remark";
	public static final String APPLIANCE_MODEL = "applianceModel";
	public static final String CUSTOMER_HANDLER = "customerHandler";
	public static final String RANGE = "range";
	public static final String ACCURACY = "accuracy";
	public static final String RECEIVER_NAME = "receiverName";
	public static final String HEAD_NAME_ID = "headNameId";
	public static final String HEAD_NAME = "headName";
	public static final String HEAD_NAME_EN = "headNameEn";
	public static final String IS_FROM_OLD_SYSTEM = "isFromOldSystem";
	public static final String CHECK_OUT_STAFF_ID = "checkOutStaffId";
	public static final String DETAIL_LIST_CODE = "detailListCode";
	public static final String ATTACHMENT = "attachment";
	public static final String LOCALE_APPLIANCE_ITEM_ID = "localeApplianceItemId";

	public void save(CommissionSheet transientInstance) {
		log.debug("saving CommissionSheet instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(CommissionSheet persistentInstance) {
		log.debug("deleting CommissionSheet instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CommissionSheet findById(java.lang.Integer id) {
		log.debug("getting CommissionSheet instance with id: " + id);
		try {
			CommissionSheet instance = (CommissionSheet) getSession().get(
					"com.jlyw.hibernate.CommissionSheet", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(CommissionSheet instance) {
		log.debug("finding CommissionSheet instance by example");
		try {
			List results = getSession().createCriteria(
					"com.jlyw.hibernate.CommissionSheet").add(
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
		log.debug("finding CommissionSheet instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from CommissionSheet as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByPwd(Object pwd) {
		return findByProperty(PWD, pwd);
	}

	public List findByCustomerId(Object customerId) {
		return findByProperty(CUSTOMER_ID, customerId);
	}

	public List findByCustomerName(Object customerName) {
		return findByProperty(CUSTOMER_NAME, customerName);
	}

	public List findByCustomerTel(Object customerTel) {
		return findByProperty(CUSTOMER_TEL, customerTel);
	}

	public List findByCustomerAddress(Object customerAddress) {
		return findByProperty(CUSTOMER_ADDRESS, customerAddress);
	}

	public List findByCustomerZipCode(Object customerZipCode) {
		return findByProperty(CUSTOMER_ZIP_CODE, customerZipCode);
	}

	public List findByCustomerContactor(Object customerContactor) {
		return findByProperty(CUSTOMER_CONTACTOR, customerContactor);
	}

	public List findByCustomerContactorTel(Object customerContactorTel) {
		return findByProperty(CUSTOMER_CONTACTOR_TEL, customerContactorTel);
	}

	public List findBySampleFrom(Object sampleFrom) {
		return findByProperty(SAMPLE_FROM, sampleFrom);
	}

	public List findByBillingTo(Object billingTo) {
		return findByProperty(BILLING_TO, billingTo);
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

	public List findByAppFactoryCode(Object appFactoryCode) {
		return findByProperty(APP_FACTORY_CODE, appFactoryCode);
	}

	public List findByAppManageCode(Object appManageCode) {
		return findByProperty(APP_MANAGE_CODE, appManageCode);
	}

	public List findByManufacturer(Object manufacturer) {
		return findByProperty(MANUFACTURER, manufacturer);
	}

	public List findByQuantity(Object quantity) {
		return findByProperty(QUANTITY, quantity);
	}

	public List findByMandatory(Object mandatory) {
		return findByProperty(MANDATORY, mandatory);
	}

	public List findByMandatoryCode(Object mandatoryCode) {
		return findByProperty(MANDATORY_CODE, mandatoryCode);
	}

	public List findByAppearance(Object appearance) {
		return findByProperty(APPEARANCE, appearance);
	}

	public List findByReportType(Object reportType) {
		return findByProperty(REPORT_TYPE, reportType);
	}

	public List findByOtherRequirements(Object otherRequirements) {
		return findByProperty(OTHER_REQUIREMENTS, otherRequirements);
	}

	public List findByUrgent(Object urgent) {
		return findByProperty(URGENT, urgent);
	}

	public List findByRepair(Object repair) {
		return findByProperty(REPAIR, repair);
	}

	public List findBySubcontract(Object subcontract) {
		return findByProperty(SUBCONTRACT, subcontract);
	}

	public List findByLocation(Object location) {
		return findByProperty(LOCATION, location);
	}

	public List findByAllotee(Object allotee) {
		return findByProperty(ALLOTEE, allotee);
	}

	public List findByCommissionType(Object commissionType) {
		return findByProperty(COMMISSION_TYPE, commissionType);
	}

	public List findByReceiverId(Object receiverId) {
		return findByProperty(RECEIVER_ID, receiverId);
	}

	public List findBySampleAddress(Object sampleAddress) {
		return findByProperty(SAMPLE_ADDRESS, sampleAddress);
	}

	public List findByCreatorId(Object creatorId) {
		return findByProperty(CREATOR_ID, creatorId);
	}

	public List findByReportAddress(Object reportAddress) {
		return findByProperty(REPORT_ADDRESS, reportAddress);
	}

	public List findByQuotationId(Object quotationId) {
		return findByProperty(QUOTATION_ID, quotationId);
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

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByLocaleCommissionCode(Object localeCommissionCode) {
		return findByProperty(LOCALE_COMMISSION_CODE, localeCommissionCode);
	}

	public List findByLocaleStaffId(Object localeStaffId) {
		return findByProperty(LOCALE_STAFF_ID, localeStaffId);
	}

	public List findByFinishLocation(Object finishLocation) {
		return findByProperty(FINISH_LOCATION, finishLocation);
	}

	public List findByFinishStaffId(Object finishStaffId) {
		return findByProperty(FINISH_STAFF_ID, finishStaffId);
	}

	public List findByCancelRequesterId(Object cancelRequesterId) {
		return findByProperty(CANCEL_REQUESTER_ID, cancelRequesterId);
	}

	public List findByCancelExecuterId(Object cancelExecuterId) {
		return findByProperty(CANCEL_EXECUTER_ID, cancelExecuterId);
	}

	public List findByCancelReason(Object cancelReason) {
		return findByProperty(CANCEL_REASON, cancelReason);
	}

	public List findByInvoiceCode(Object invoiceCode) {
		return findByProperty(INVOICE_CODE, invoiceCode);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByApplianceModel(Object applianceModel) {
		return findByProperty(APPLIANCE_MODEL, applianceModel);
	}

	public List findByCustomerHandler(Object customerHandler) {
		return findByProperty(CUSTOMER_HANDLER, customerHandler);
	}

	public List findByRange(Object range) {
		return findByProperty(RANGE, range);
	}

	public List findByAccuracy(Object accuracy) {
		return findByProperty(ACCURACY, accuracy);
	}

	public List findByReceiverName(Object receiverName) {
		return findByProperty(RECEIVER_NAME, receiverName);
	}

	public List findByHeadNameId(Object headNameId) {
		return findByProperty(HEAD_NAME_ID, headNameId);
	}

	public List findByHeadName(Object headName) {
		return findByProperty(HEAD_NAME, headName);
	}

	public List findByHeadNameEn(Object headNameEn) {
		return findByProperty(HEAD_NAME_EN, headNameEn);
	}

	public List findByIsFromOldSystem(Object isFromOldSystem) {
		return findByProperty(IS_FROM_OLD_SYSTEM, isFromOldSystem);
	}

	public List findByCheckOutStaffId(Object checkOutStaffId) {
		return findByProperty(CHECK_OUT_STAFF_ID, checkOutStaffId);
	}

	public List findByDetailListCode(Object detailListCode) {
		return findByProperty(DETAIL_LIST_CODE, detailListCode);
	}

	public List findByAttachment(Object attachment) {
		return findByProperty(ATTACHMENT, attachment);
	}

	public List findByLocaleApplianceItemId(Object localeApplianceItemId) {
		return findByProperty(LOCALE_APPLIANCE_ITEM_ID, localeApplianceItemId);
	}

	public List findAll() {
		log.debug("finding all CommissionSheet instances");
		try {
			String queryString = "from CommissionSheet";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public CommissionSheet merge(CommissionSheet detachedInstance) {
		log.debug("merging CommissionSheet instance");
		try {
			CommissionSheet result = (CommissionSheet) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(CommissionSheet instance) {
		log.debug("attaching dirty CommissionSheet instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CommissionSheet instance) {
		log.debug("attaching clean CommissionSheet instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}