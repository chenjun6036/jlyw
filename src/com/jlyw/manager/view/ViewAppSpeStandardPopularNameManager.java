package com.jlyw.manager.view;

import java.util.List;

import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName;
import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularNameDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewAppSpeStandardPopularNameManager {
	private ViewApplianceSpecialStandardNamePopularNameDAO m_dao = new ViewApplianceSpecialStandardNamePopularNameDAO();

	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的ViewApplianceSpecialStandardNamePopularName列表
	 */
	public List<ViewApplianceSpecialStandardNamePopularName> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewApplianceSpecialStandardNamePopularName", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ViewApplianceSpecialStandardNamePopularName记录数
	 * @param arr 条件键值对
	 * @return ViewApplianceSpecialStandardNamePopularName记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewApplianceSpecialStandardNamePopularName", arr);
	}
	/**
	 * 得到所有ViewApplianceSpecialStandardNamePopularName记录数
	 * @param arr 查询条件列表
	 * @return ViewApplianceSpecialStandardNamePopularName记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewApplianceSpecialStandardNamePopularName", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ViewApplianceSpecialStandardNamePopularName> findByExample(ViewApplianceSpecialStandardNamePopularName instance) {
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
	* @return 分页后的数据列表- List<ViewApplianceSpecialStandardNamePopularName>
	*/
	public List<ViewApplianceSpecialStandardNamePopularName> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewApplianceSpecialStandardNamePopularName", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<ViewApplianceSpecialStandardNamePopularName>
	*/
	public List<ViewApplianceSpecialStandardNamePopularName> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewApplianceSpecialStandardNamePopularName", currentPage, pageSize, orderby, asc, arr);
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
	public List<ViewApplianceSpecialStandardNamePopularName> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewApplianceSpecialStandardNamePopularName", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 按HQL分页查询
	 * @param queryString
	 * @param currentPage
	 * @param pageSize
	 * @param arr
	 * @return
	 */
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			return null;
		}
	}
}
