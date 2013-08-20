package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StdTgtApp;
import com.jlyw.hibernate.StdTgtAppDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class StdTgtAppManager {
private StdTgtAppDAO m_dao = new StdTgtAppDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return StdTgtApp对象
	 */
	public StdTgtApp findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条StdTgtApp记录
	 * @param StdTgtApp StdTgtApp对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(StdTgtApp StdTgtApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(StdTgtApp);
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
	 * 更新一条StdTgtApp记录
	 * @param StdTgtApp StdTgtApp对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(StdTgtApp StdTgtApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(StdTgtApp);
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
	
	public boolean update(List<KeyValue> list_KeyValue ,KeyValue...arr){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update("StdTgtApp",list_KeyValue,arr);
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
	 * 根据StdTgtApp Id,删除StdTgtApp对象
	 * @param id StdTgtApp id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			StdTgtApp u = m_dao.findById(id);
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
	 * @return 分页后的StdTgtApp列表
	 */
	public List<StdTgtApp> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		return m_dao.findPagedAll("StdTgtApp", currentPage,pageSize, arr);
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Specification列表
	 */
	public List<StdTgtApp> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		return m_dao.findPagedAll("StdTgtApp", currentPage,pageSize, arr);
	}
	
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("StdTgtApp",arr);		
	}
	
	/**
	 * 得到所有StdTgtApp记录数
	 * @return StdTgtApp记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("StdTgtApp",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(StdTgtApp instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<StdTgtApp> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("StdTgtApp", arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception {
		return m_dao.getTotalCountByHQL(queryString, arr);
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
	* @return 分页后的数据列表- List<StdTgtApp>
	*/
	public List<StdTgtApp> findPagedAllBySort(int currentPage, int pageSize, String orderby, boolean asc,List<KeyValueWithOperator>arr) throws Exception{
		return m_dao.findPagedAllBySort("StdTgtApp", currentPage, pageSize, orderby, asc, arr);
	}
	
	public List findByHQL(String queryString,Object...arr) {
		return m_dao.findByHQL(queryString, arr);
	}
}
