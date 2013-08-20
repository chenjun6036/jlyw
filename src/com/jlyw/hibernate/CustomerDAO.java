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
 * Customer entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.Customer
 * @author MyEclipse Persistence Tools
 */
public class CustomerDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(CustomerDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String NAME_EN = "nameEn";
	public static final String BRIEF = "brief";
	public static final String CUSTOMER_TYPE = "customerType";
	public static final String CODE = "code";
	public static final String ADDRESS = "address";
	public static final String ADDRESS_EN = "addressEn";
	public static final String TEL = "tel";
	public static final String FAX = "fax";
	public static final String ZIP_CODE = "zipCode";
	public static final String REMARK = "remark";
	public static final String STATUS = "status";
	public static final String BALANCE = "balance";
	public static final String ACCOUNT = "account";
	public static final String CLASSIFICATION = "classification";
	public static final String FIELD_DEMANDS = "fieldDemands";
	public static final String CERTIFICATE_DEMANDS = "certificateDemands";
	public static final String SPECIAL_DEMANDS = "specialDemands";
	public static final String CREDIT_AMOUNT = "creditAmount";
	public static final String ACCOUNT_BANK = "accountBank";
	public static final String PAY_VIA = "payVia";
	public static final String PAY_TYPE = "payType";
	public static final String ACCOUNT_CYCLE = "accountCycle";
	public static final String CUSTOMER_VALUE_LEVEL = "customerValueLevel";
	public static final String CUSTOMER_LEVEL = "customerLevel";
	public static final String TRENDENCY = "trendency";
	public static final String OUTPUT = "output";
	public static final String OUTPUT_EXPECTATION = "outputExpectation";
	public static final String SERVICE_FEE_LIMITATION = "serviceFeeLimitation";
	public static final String INDUSTRY = "industry";

	public void save(Customer transientInstance) {
		log.debug("saving Customer instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Customer persistentInstance) {
		log.debug("deleting Customer instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Customer findById(java.lang.Integer id) {
		log.debug("getting Customer instance with id: " + id);
		try {
			Customer instance = (Customer) getSession().get(
					"com.jlyw.hibernate.Customer", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Customer instance) {
		log.debug("finding Customer instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.Customer")
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
		log.debug("finding Customer instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Customer as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByNameEn(Object nameEn) {
		return findByProperty(NAME_EN, nameEn);
	}

	public List findByBrief(Object brief) {
		return findByProperty(BRIEF, brief);
	}

	public List findByCustomerType(Object customerType) {
		return findByProperty(CUSTOMER_TYPE, customerType);
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}

	public List findByAddressEn(Object addressEn) {
		return findByProperty(ADDRESS_EN, addressEn);
	}

	public List findByTel(Object tel) {
		return findByProperty(TEL, tel);
	}

	public List findByFax(Object fax) {
		return findByProperty(FAX, fax);
	}

	public List findByZipCode(Object zipCode) {
		return findByProperty(ZIP_CODE, zipCode);
	}

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByBalance(Object balance) {
		return findByProperty(BALANCE, balance);
	}

	public List findByAccount(Object account) {
		return findByProperty(ACCOUNT, account);
	}

	public List findByClassification(Object classification) {
		return findByProperty(CLASSIFICATION, classification);
	}

	public List findByFieldDemands(Object fieldDemands) {
		return findByProperty(FIELD_DEMANDS, fieldDemands);
	}

	public List findByCertificateDemands(Object certificateDemands) {
		return findByProperty(CERTIFICATE_DEMANDS, certificateDemands);
	}

	public List findBySpecialDemands(Object specialDemands) {
		return findByProperty(SPECIAL_DEMANDS, specialDemands);
	}

	public List findByCreditAmount(Object creditAmount) {
		return findByProperty(CREDIT_AMOUNT, creditAmount);
	}

	public List findByAccountBank(Object accountBank) {
		return findByProperty(ACCOUNT_BANK, accountBank);
	}

	public List findByPayVia(Object payVia) {
		return findByProperty(PAY_VIA, payVia);
	}

	public List findByPayType(Object payType) {
		return findByProperty(PAY_TYPE, payType);
	}

	public List findByAccountCycle(Object accountCycle) {
		return findByProperty(ACCOUNT_CYCLE, accountCycle);
	}

	public List findByCustomerValueLevel(Object customerValueLevel) {
		return findByProperty(CUSTOMER_VALUE_LEVEL, customerValueLevel);
	}

	public List findByCustomerLevel(Object customerLevel) {
		return findByProperty(CUSTOMER_LEVEL, customerLevel);
	}

	public List findByTrendency(Object trendency) {
		return findByProperty(TRENDENCY, trendency);
	}

	public List findByOutput(Object output) {
		return findByProperty(OUTPUT, output);
	}

	public List findByOutputExpectation(Object outputExpectation) {
		return findByProperty(OUTPUT_EXPECTATION, outputExpectation);
	}

	public List findByServiceFeeLimitation(Object serviceFeeLimitation) {
		return findByProperty(SERVICE_FEE_LIMITATION, serviceFeeLimitation);
	}

	public List findByIndustry(Object industry) {
		return findByProperty(INDUSTRY, industry);
	}

	public List findAll() {
		log.debug("finding all Customer instances");
		try {
			String queryString = "from Customer";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Customer merge(Customer detachedInstance) {
		log.debug("merging Customer instance");
		try {
			Customer result = (Customer) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Customer instance) {
		log.debug("attaching dirty Customer instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Customer instance) {
		log.debug("attaching clean Customer instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}