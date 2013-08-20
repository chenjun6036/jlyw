package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Privilege;
import com.jlyw.hibernate.SysResources;
import com.jlyw.hibernate.SysResourcesDAO;
import com.jlyw.util.KeyValueWithOperator;

public class SysResourcesManager {
	private SysResourcesDAO m_dao = new SysResourcesDAO();
	/**
	 * 根据SysResources Id 查找 SysResources对象
	 * @param id: SysResources Id
	 * @return SysResources对象
	 */
	public SysResources findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条SysResources记录
	 * @param appSpecies SysResources对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(SysResources appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(appSpecies);
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
	 * 更新一条SysResources记录
	 * @param appSpecies SysResources对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(SysResources appSpecies){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(appSpecies);
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
	 * 根据SysResources Id,删除SysResources对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SysResources u = m_dao.findById(id);
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
	 * 按条件查找
	 * @param arr
	 * @return
	 */
	public List<SysResources> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SysResources", arr);
		}
		catch(Exception e){
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
	* @return 分页后的数据列表- List<SysResources>
	*/
	public List<SysResources> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>list){
		return m_dao.findPagedAllBySort("SysResources", currentPage, pageSize, orderby, asc, list);
	}
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的SysResources列表
	 */
	public List<SysResources> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("SysResources", currentPage,pageSize, arr);
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
	public List<SysResources> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("SysResources", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SysResources",arr);		
	}
	
	/**
	 * 得到所有SysResources记录数
	 * @return SysResources记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SysResources",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(SysResources instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 判断权限名是否已经存在
	 * @param SysResourcesName 权限名
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isSysResourcesNameExist(String SysResourcesName){
		SysResources SysResources= new SysResources();
		SysResources.setName(SysResourcesName);
		List<SysResources> list= m_dao.findByExample(SysResources);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
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
