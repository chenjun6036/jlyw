package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.UserQual;
import com.jlyw.hibernate.UserQualDAO;
import com.jlyw.util.KeyValueWithOperator;

public class UserQualManager {
private UserQualDAO m_dao = new UserQualDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return UserQual对象
	 */
	public UserQual findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条UserQual记录
	 * @param UserQual UserQual对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(UserQual UserQual){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(UserQual);
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
	 * 更新一条UserQual记录
	 * @param UserQual UserQual对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(UserQual UserQual){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(UserQual);
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
	 * 根据UserQual Id,删除UserQual对象
	 * @param id UserQual id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			UserQual u = m_dao.findById(id);
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
	 * @return 分页后的UserQual列表
	 */
	public List<UserQual> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("UserQual", currentPage,pageSize, arr);
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
	public List<UserQual> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("UserQual", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有UserQual记录数
	 * @return UserQual记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("UserQual",arr);		
	}
	
	/**
	 * 得到所有UserQual记录数
	 * @return UserQual记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("UserQual",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(UserQual instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<UserQual> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("UserQual", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
		
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
}
