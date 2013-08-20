package com.jlyw.manager.view;

import java.util.List;

import com.jlyw.hibernate.view.ViewTargetApplianceDetailInfo;
import com.jlyw.hibernate.view.ViewTargetApplianceDetailInfoDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewTargetApplianceDetailInfoManager {
private ViewTargetApplianceDetailInfoDAO m_dao = new ViewTargetApplianceDetailInfoDAO();
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的ViewTargetApplianceDetailInfo列表
	 */
	public List<ViewTargetApplianceDetailInfo> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewTargetApplianceDetailInfo", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的ViewTargetApplianceDetailInfo列表
	 */
	public List<ViewTargetApplianceDetailInfo> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
			return m_dao.findPagedAll("ViewTargetApplianceDetailInfo", currentPage,pageSize, arr);
	}
	
	
	/**
	 * 得到所有ViewTargetApplianceDetailInfo记录数
	 * @param arr 条件键值对
	 * @return ViewTargetApplianceDetailInfo记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewTargetApplianceDetailInfo", arr);
	}
	/**
	 * 得到所有ViewTargetApplianceDetailInfo记录数
	 * @param arr 查询条件列表
	 * @return ViewTargetApplianceDetailInfo记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		
			return m_dao.getTotalCount("ViewTargetApplianceDetailInfo", arr);
		
		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ViewTargetApplianceDetailInfo> findByExample(ViewTargetApplianceDetailInfo instance) {
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
	* @return 分页后的数据列表- List<ViewTargetApplianceDetailInfo>
	*/
	public List<ViewTargetApplianceDetailInfo> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewTargetApplianceDetailInfo", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<ViewTargetApplianceDetailInfo>
	*/
	public List<ViewTargetApplianceDetailInfo> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewTargetApplianceDetailInfo", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<ViewTargetApplianceDetailInfo> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewTargetApplianceDetailInfo", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllBySQL(String queryString, int currentPage, int pageSize, List<Object>arr) throws Exception{
		try{
			return m_dao.findPageAllBySQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			throw e;
		}
	}
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（SQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountBySQL(String queryString,List<Object>arr) throws Exception {
		try{
			return m_dao.getTotalCountBySQL(queryString, arr);
		}catch(Exception ex){
			throw ex;
		}
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object>arr) throws Exception{
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			throw e;
		}
	}
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（SQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object>arr) throws Exception {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			throw ex;
		}
	}
	
}
