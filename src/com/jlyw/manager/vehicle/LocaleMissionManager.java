package com.jlyw.manager.vehicle;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.LocaleApplianceItemDAO;
import com.jlyw.hibernate.LocaleMission;
import com.jlyw.hibernate.LocaleMissionDAO;
import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationDAO;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.util.KeyValueWithOperator;

public class LocaleMissionManager {
private LocaleMissionDAO m_dao = new LocaleMissionDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return LocaleMission对象
	 */
	public LocaleMission findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条LocaleMission记录
	 * @param LocaleMission LocaleMission对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(LocaleMission LocaleMission){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(LocaleMission);
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
	 * 更新一条LocaleMission记录
	 * @param LocaleMission LocaleMission对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(LocaleMission LocaleMission){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(LocaleMission);
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
	 * 根据LocaleMission Id,删除LocaleMission对象
	 * @param id LocaleMission id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			LocaleMission u = m_dao.findById(id);
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
	 * @return 分页后的LocaleMission列表
	 */
	public List<LocaleMission> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("LocaleMission", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有LocaleMission记录数
	 * @return LocaleMission记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("LocaleMission",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(LocaleMission instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<LocaleMission> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("LocaleMission", arr);
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
	* @param arr:为查询条件的(键-值)对列表
	* @return 分页后的数据列表- List<CommissionSheet>
	*/
	public List<LocaleMission> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAll("LocaleMission", currentPage, pageSize, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 得到所有CommissionSheet记录数
	 * @param arr 查询条件列表
	 * @return CommissionSheet记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("LocaleMission", arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
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
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<LocaleMission>
	*/
	public List<LocaleMission> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("LocaleMission", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
		/**
	* 
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
	
	
}
