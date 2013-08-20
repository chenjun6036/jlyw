package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.CustomerContactorDAO;
import com.jlyw.util.KeyValueWithOperator;

public class CustomerContactorManager {

private CustomerContactorDAO m_dao = new CustomerContactorDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return CustomerContactor对象
	 */
	public CustomerContactor findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条CustomerContactor记录
	 * @param CustomerContactor CustomerContactor对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(CustomerContactor CustomerContactor){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(CustomerContactor);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 更新一条CustomerContactor记录
	 * @param CustomerContactor CustomerContactor对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(CustomerContactor CustomerContactor){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(CustomerContactor);
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}
	
	/**
	 * 根据CustomerContactor Id,删除CustomerContactor对象
	 * @param id CustomerContactor id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			CustomerContactor u = m_dao.findById(id);
			if(u == null){
				return true;
			}else{
				m_dao.delete(u);
			}			
			tran.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return false;
		} finally {
			m_dao.closeSession();
		}
	}	
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的CustomerContactor列表
	 */
	public List<CustomerContactor> findPagedAll(int currentPage, int pageSize) {
		try {
			return m_dao.findPagedAll("CustomerContactor", currentPage,pageSize);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Customer列表
	 */
	public List<CustomerContactor> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("CustomerContactor", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ApplianceAccuracy记录数
	 * @param arr 条件键值对
	 * @return ApplianceAccuracy记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CustomerContactor", arr);		
	}
	
	/**
	 * 得到所有CustomerContactor记录数
	 * @return CustomerContactor记录数
	 */
	public int getTotalCount(){
		return m_dao.getTotalCount("CustomerContactor");		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(CustomerContactor instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<CustomerContactor> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("CustomerContactor", orderby, asc, arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<CustomerContactor> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("CustomerContactor", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
}
