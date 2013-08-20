package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Deal;
import com.jlyw.hibernate.DealDAO;
import com.jlyw.hibernate.DealItem;
import com.jlyw.hibernate.DealItemDAO;
import com.jlyw.util.KeyValueWithOperator;


public class DealItemManager {
private DealItemDAO m_dao = new DealItemDAO();
	
	/**
	 * 根据DealItem Id 查找 DealItem对象
	 * @param id: DealItem Id
	 * @return DealItem对象
	 */
	public DealItem findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条DealItem记录
	 * @param appSpecies DealItem对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(DealItem appSpecies){
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
	 * 更新一条DealItem记录
	 * @param appSpecies DealItem对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(DealItem appSpecies){
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
	 * 根据DealItem Id,删除DealItem对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DealItem u = m_dao.findById(id);
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
	 * @return 分页后的DealItem列表
	 */
	public List<DealItem> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DealItem", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有DealItem记录数
	 * @param arr 条件键值对
	 * @return DealItem记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DealItem", arr);
	}
	/**
	 * 得到所有DealItem记录数
	 * @param arr 查询条件列表
	 * @return DealItem记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DealItem", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<DealItem> findByExample(DealItem instance) {
		return m_dao.findByExample(instance);
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
	* @return 分页后的数据列表- List<DealItem>
	*/
	public List<DealItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("DealItem", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
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
	* @param arr:为查询条件的(键-值)对列表
	* @return 分页后的数据列表- List<DealItem>
	*/
	public List<DealItem> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("DealItem", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<DealItem> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("DealItem", arr);
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
	public List<DealItem> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("DealItem",orderby,asc,arr);
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
	public List<DealItem> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("DealItem", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public boolean updateByBatch(List<DealItem> saveDealItemList, List<DealItem> updateDealItemList){
		if(saveDealItemList == null || updateDealItemList==null){
			return false;
		} 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {					
			for(int i = 0; i < saveDealItemList.size(); i++){
				DealItem dealItem = saveDealItemList.get(i);
				m_dao.save(dealItem);				
			}
			for(int i = 0; i < updateDealItemList.size(); i++){
				DealItem dealItem = updateDealItemList.get(i);
				m_dao.update(dealItem);				
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
