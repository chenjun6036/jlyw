package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.TgtAppStdApp;
import com.jlyw.hibernate.TgtAppStdApp;
import com.jlyw.hibernate.TgtAppStdAppDAO;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;

public class TgtAppStdAppManager {
private TgtAppStdAppDAO m_dao = new TgtAppStdAppDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return TgtAppStdApp对象
	 */
	public TgtAppStdApp findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条TgtAppStdApp记录
	 * @param TgtAppStdApp TgtAppStdApp对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(TgtAppStdApp TgtAppStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(TgtAppStdApp);
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
	 * 更新一条TgtAppStdApp记录
	 * @param TgtAppStdApp TgtAppStdApp对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(TgtAppStdApp TgtAppStdApp){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(TgtAppStdApp);
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
			m_dao.update("TgtAppStdApp",list_KeyValue,arr);
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
	 * 根据TgtAppStdApp Id,删除TgtAppStdApp对象
	 * @param id TgtAppStdApp id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			TgtAppStdApp u = m_dao.findById(id);
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
	 * @return 分页后的TgtAppStdApp列表
	 */
	public List<TgtAppStdApp> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		return m_dao.findPagedAll("TgtAppStdApp", currentPage,pageSize, arr);
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Specification列表
	 */
	public List<TgtAppStdApp> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		return m_dao.findPagedAll("TgtAppStdApp", currentPage,pageSize, arr);
	}
	
	/**
	 * 得到所有Specification记录数
	 * @return Specification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("TgtAppStdApp",arr);		
	}
	
	/**
	 * 得到所有TgtAppStdApp记录数
	 * @return TgtAppStdApp记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TgtAppStdApp",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(TgtAppStdApp instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<TgtAppStdApp> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("TgtAppStdApp", arr);
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
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object>arr){
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) {
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * 插入一批TgtAppStdApp记录
	 * @param TgtAppStdAppList TgtAppStdApp对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<TgtAppStdApp> TgtAppStdAppList){
		if(TgtAppStdAppList == null || TgtAppStdAppList.size() == 0){
			return true;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(TgtAppStdApp TgtAppStdApp : TgtAppStdAppList){
				m_dao.save(TgtAppStdApp);
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
	
	public List findByHQL(String queryString,Object...arr) {
		return m_dao.findByHQL(queryString, arr);
	}
}
