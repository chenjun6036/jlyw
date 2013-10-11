package com.jlyw.manager.view;

import java.util.ArrayList;
import java.util.List;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.view.ViewTransaction;
import com.jlyw.hibernate.view.ViewTransactionDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewTransactionManager {
private ViewTransactionDAO m_dao = new ViewTransactionDAO();
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的ViewTransaction列表
	 */
	public List<ViewTransaction> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewTransaction", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ViewTransaction记录数
	 * @param arr 条件键值对
	 * @return ViewTransaction记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewTransaction", arr);
	}
	/**
	 * 得到所有ViewTransaction记录数
	 * @param arr 查询条件列表
	 * @return ViewTransaction记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewTransaction", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ViewTransaction> findByExample(ViewTransaction instance) {
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
	* @return 分页后的数据列表- List<ViewTransaction>
	*/
	public List<ViewTransaction> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewTransaction", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<ViewTransaction>
	*/
	public List<ViewTransaction> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewTransaction", currentPage, pageSize, orderby, asc, arr);
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
		
			return m_dao.findByHQL(queryString, arr);

	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		
			return m_dao.findByHQL(queryString, arr);

	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<ViewTransaction> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ViewTransaction", arr);
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
	public List<ViewTransaction> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewTransaction", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * 多条件查询
	 * @param orderby：按照哪个字段排序
	 * @param asc：true 增序 false 减序
	 * @param arr
	 * @return
	 */
	public List<ViewTransaction> findByVarPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByPropertyBySort("ViewTransaction",orderby, asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
}
