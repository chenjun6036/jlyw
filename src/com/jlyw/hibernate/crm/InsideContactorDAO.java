package com.jlyw.hibernate.crm;

import com.jlyw.hibernate.BaseHibernateDAO;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * InsideContactor entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.jlyw.hibernate.crm.InsideContactor
 * @author MyEclipse Persistence Tools
 */
public class InsideContactorDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(InsideContactorDAO.class);
	// property constants
	public static final String ROLE = "role";

	public void save(InsideContactor transientInstance) {
		log.debug("saving InsideContactor instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(InsideContactor persistentInstance) {
		log.debug("deleting InsideContactor instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public InsideContactor findById(java.lang.Integer id) {
		log.debug("getting InsideContactor instance with id: " + id);
		try {
			InsideContactor instance = (InsideContactor) getSession().get(
					"com.jlyw.hibernate.crm.InsideContactor", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(InsideContactor instance) {
		log.debug("finding InsideContactor instance by example");
		try {
			List results = getSession()
					.createCriteria("com.jlyw.hibernate.crm.InsideContactor")
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
		log.debug("finding InsideContactor instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from InsideContactor as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByRole(Object role) {
		return findByProperty(ROLE, role);
	}

	public List findAll() {
		log.debug("finding all InsideContactor instances");
		try {
			String queryString = "from InsideContactor";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public InsideContactor merge(InsideContactor detachedInstance) {
		log.debug("merging InsideContactor instance");
		try {
			InsideContactor result = (InsideContactor) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(InsideContactor instance) {
		log.debug("attaching dirty InsideContactor instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(InsideContactor instance) {
		log.debug("attaching clean InsideContactor instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}