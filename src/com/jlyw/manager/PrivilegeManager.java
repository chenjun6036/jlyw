package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Privilege;
import com.jlyw.hibernate.PrivilegeDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.util.KeyValueWithOperator;


public class PrivilegeManager {
private PrivilegeDAO m_dao = new PrivilegeDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return Privilege对象
	 */
	public Privilege findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Privilege记录
	 * @param Privilege Privilege对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Privilege Privilege){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Privilege);
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
	 * 更新一条Privilege记录
	 * @param Privilege Privilege对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Privilege Privilege){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Privilege);
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
	 * 根据Role Id,删除Privilege对象
	 * @param id Privilege id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Privilege u = m_dao.findById(id);
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
	 * @return 分页后的Privilege列表
	 */
	public List<Privilege> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Privilege", currentPage,pageSize, arr);
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
	public List<Privilege> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("Privilege", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<Privilege>
	*/
	public List<Privilege> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>list){
		return m_dao.findPagedAllBySort("Privilege", currentPage, pageSize, orderby, asc, list);
	}
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Privilege",arr);		
	}
	
	/**
	 * 得到所有Privilege记录数
	 * @return Privilege记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Privilege",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(Privilege instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 判断权限名是否已经存在
	 * @param privilegeName 权限名
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isPrivilegeNameExist(String privilegeName){
		Privilege privilege= new Privilege();
		privilege.setName(privilegeName);
		List<Privilege> list= m_dao.findByExample(privilege);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<Privilege> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Privilege", arr);
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
