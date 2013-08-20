package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.UserProfile;
import com.jlyw.hibernate.UserProfileDAO;
import com.jlyw.util.KeyValueWithOperator;

public class UserProfileManager {
	private UserProfileDAO m_dao = new UserProfileDAO();
	/**
	 * 根据UserProfile Id 查找 UserProfile对象
	 * 
	 * @param id
	 *            UserProfile Id
	 * @return UserProfile对象
	 */
	public UserProfile findById(int id) {
		return m_dao.findById(id);
	}

	/**
	 * 插入一条UserProfile记录
	 * 
	 * @param UserProfile
	 *            UserProfile对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(UserProfile UserProfile) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.save(UserProfile);
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
	 * 更新一条UserProfile记录
	 * 
	 * @param UserProfile
	 *            UserProfile对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(UserProfile UserProfile) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.update(UserProfile);
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
	 * 根据UserProfile Id,删除UserProfile对象
	 * 
	 * @param id
	 *            UserProfile id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			UserProfile u = m_dao.findById(id);
			if (u == null) {
				return true;
			} else {
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
	 * 
	 * @param currentPage
	 *            当前页码
	 * @param pageSize
	 *            每页的记录数
	 * @return 分页后的UserProfile列表
	 */
	public List<UserProfile> findPagedAll(int currentPage, int pageSize,
			KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("UserProfile", currentPage, pageSize, arr);
		} catch (Exception e) {
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
	 * 
	 * @param arr
	 * @return
	 */
	public List<UserProfile> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("UserProfile", arr);
		}
		catch(Exception e){
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

	/**
	 * 得到所有UserProfile记录数
	 * 
	 * @return UserProfile记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr) {
		return m_dao.getTotalCount("UserProfile",arr);
	}

	/**
	 * 多条件组合查询
	 * 
	 * @param instance
	 *            条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(UserProfile instance) {
		return m_dao.findByExample(instance);

	}
}
