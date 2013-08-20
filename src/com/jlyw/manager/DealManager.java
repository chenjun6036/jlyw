package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Deal;
import com.jlyw.hibernate.DealDAO;
import com.jlyw.hibernate.DealItem;
import com.jlyw.hibernate.DealItemDAO;
import com.jlyw.hibernate.Quotation;
import com.jlyw.util.KeyValueWithOperator;

public class DealManager {
private DealDAO m_dao = new DealDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return Deal对象
	 */
	public Deal findById(String id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Deal记录
	 * @param Deal Deal对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Deal Deal){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Deal);
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
	 * 更新一条Deal记录
	 * @param Deal Deal对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Deal Deal){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Deal);
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
	 * 根据Deal Id,删除Deal对象
	 * @param id Deal id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(String id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Deal u = m_dao.findById(id);
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
	 * @return 分页后的Deal列表
	 */
	public List<Deal> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Deal", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Deal记录数
	 * @return Deal记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Deal",arr);		
	}
	
	/**
	 * 得到所有Deal记录数
	 * @return Deal记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Deal",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(Deal instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<Deal> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("Deal", arr);
	}
	
/**
	
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
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
	* @return 分页后的数据列表- List<Quotation>
	*/
	public List<Deal> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator> arr){
		try{	
			return m_dao.findPagedAllBySort("Deal", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception ex){
			return null;
		}
	}
	
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	
	
	public boolean savaByBatch(Deal deal, List<DealItem> dealItemList){
		if(dealItemList == null || deal==null){
			return false;
		}
		DealItemDAO dDAO = new DealItemDAO(); 
		
		Transaction tran = m_dao.getSession().beginTransaction();		
		try {			
			m_dao.save(deal);			
			for(int i = 0; i < dealItemList.size(); i++){
				DealItem dealItem = dealItemList.get(i);
				dDAO.save(dealItem);				
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
