package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.StdStdApp;
import com.jlyw.hibernate.StdStdApp;
import com.jlyw.hibernate.StdStdAppDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class StdStdAppManager {
private StdStdAppDAO m_dao = new StdStdAppDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return StdStdApp对象
	 */
	public StdStdApp findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条StdStdApp记录
	 * @param StdStdApp StdStdApp对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(StdStdApp StdStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(StdStdApp);
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
	 * 更新一条StdStdApp记录
	 * @param StdStdApp StdStdApp对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(StdStdApp StdStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(StdStdApp);
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
			m_dao.update("StdStdApp",list_KeyValue,arr);
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
	 * 根据StdStdApp Id,删除StdStdApp对象
	 * @param id StdStdApp id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			StdStdApp u = m_dao.findById(id);
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
	 * @return 分页后的StdStdApp列表
	 */
	public List<StdStdApp> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("StdStdApp", currentPage,pageSize, arr);
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
	public List<StdStdApp> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("StdStdApp", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("StdStdApp",arr);		
	}
	
	/**
	 * 得到所有StdStdApp记录数
	 * @return StdStdApp记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("StdStdApp",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(StdStdApp instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<StdStdApp> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("StdStdApp", arr);
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	 * 插入一批StdStdApp记录
	 * @param StdStdAppList StdStdApp对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<StdStdApp> StdStdAppList){
		if(StdStdAppList == null || StdStdAppList.size() == 0){
			return true;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(StdStdApp StdStdApp : StdStdAppList){
				m_dao.save(StdStdApp);
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
	 * 更新一批StdStdApp记录
	 * @param StdStdAppList StdStdApp对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean updateByBatch(List<StdStdApp> StdStdAppList){
		if(StdStdAppList == null || StdStdAppList.size() == 0){
			return true;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(StdStdApp StdStdApp : StdStdAppList){
				m_dao.update(StdStdApp);
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
