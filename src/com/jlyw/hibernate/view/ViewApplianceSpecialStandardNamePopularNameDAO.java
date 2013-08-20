package com.jlyw.hibernate.view;

import com.jlyw.hibernate.BaseHibernateDAO;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ViewApplianceSpecialStandardNamePopularName entities. Transaction control of
 * the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle
 * user-managed Spring transactions. Each of these methods provides additional
 * information for how to configure it for the desired type of transaction
 * control.
 * 
 * @see com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName
 * @author MyEclipse Persistence Tools
 */

public class ViewApplianceSpecialStandardNamePopularNameDAO extends
		BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ViewApplianceSpecialStandardNamePopularNameDAO.class);

	// property constants

	public void save(
			ViewApplianceSpecialStandardNamePopularName transientInstance) {
		log
				.debug("saving ViewApplianceSpecialStandardNamePopularName instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(
			ViewApplianceSpecialStandardNamePopularName persistentInstance) {
		log
				.debug("deleting ViewApplianceSpecialStandardNamePopularName instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ViewApplianceSpecialStandardNamePopularName findById(
			com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularNameId id) {
		log
				.debug("getting ViewApplianceSpecialStandardNamePopularName instance with id: "
						+ id);
		try {
			ViewApplianceSpecialStandardNamePopularName instance = (ViewApplianceSpecialStandardNamePopularName) getSession()
					.get(
							"com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(
			ViewApplianceSpecialStandardNamePopularName instance) {
		log
				.debug("finding ViewApplianceSpecialStandardNamePopularName instance by example");
		try {
			List results = getSession()
					.createCriteria(
							"com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName")
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
		log
				.debug("finding ViewApplianceSpecialStandardNamePopularName instance with property: "
						+ propertyName + ", value: " + value);
		try {
			String queryString = "from ViewApplianceSpecialStandardNamePopularName as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log
				.debug("finding all ViewApplianceSpecialStandardNamePopularName instances");
		try {
			String queryString = "from ViewApplianceSpecialStandardNamePopularName";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ViewApplianceSpecialStandardNamePopularName merge(
			ViewApplianceSpecialStandardNamePopularName detachedInstance) {
		log
				.debug("merging ViewApplianceSpecialStandardNamePopularName instance");
		try {
			ViewApplianceSpecialStandardNamePopularName result = (ViewApplianceSpecialStandardNamePopularName) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ViewApplianceSpecialStandardNamePopularName instance) {
		log
				.debug("attaching dirty ViewApplianceSpecialStandardNamePopularName instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ViewApplianceSpecialStandardNamePopularName instance) {
		log
				.debug("attaching clean ViewApplianceSpecialStandardNamePopularName instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}