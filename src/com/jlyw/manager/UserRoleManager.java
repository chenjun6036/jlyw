package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.UserRoleDAO;
import com.jlyw.util.KeyValueWithOperator;

public class UserRoleManager {
private UserRoleDAO m_dao = new UserRoleDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return User对象
	 */
	public UserRole findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条User记录
	 * @param user User对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(UserRole user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(user);
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
	 * 更新一条User记录
	 * @param user User对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(UserRole user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(user);
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
	 * 根据User Id,删除User对象
	 * @param id User id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			UserRole u = m_dao.findById(id);
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
	 * @param arr 条件键值对
	 * @return 分页后的User列表
	 */
	public List<UserRole> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("UserRole", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Specification列表
	 */
	public List<UserRole> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("UserRole", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 得到所有User记录数
	 * @return User记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("UserRole", arr);		
	}
	
	/**
	 * 得到所有User记录数
	 * @return User记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("UserRole",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(UserRole instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<UserRole> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("UserRole", arr);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 插入一批UserRole记录
	 * @param UserRoleList UserRole对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<UserRole> UserRoleList){
		if(UserRoleList == null || UserRoleList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(UserRole UserRole : UserRoleList){
				m_dao.save(UserRole);
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
	
}
