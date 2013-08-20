package com.jlyw.manager.view;

import java.util.ArrayList;
import java.util.List;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.view.ViewQjtz;
import com.jlyw.hibernate.view.ViewQjtzDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ViewQjtzManager {
private ViewQjtzDAO m_dao = new ViewQjtzDAO();
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param arr 条件键值对
	 * @return 分页后的ViewQjtz列表
	 */
	public List<ViewQjtz> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ViewQjtz", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ViewQjtz记录数
	 * @param arr 条件键值对
	 * @return ViewQjtz记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ViewQjtz", arr);
	}
	/**
	 * 得到所有ViewQjtz记录数
	 * @param arr 查询条件列表
	 * @return ViewQjtz记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ViewQjtz", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ViewQjtz> findByExample(ViewQjtz instance) {
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
	* @return 分页后的数据列表- List<ViewQjtz>
	*/
	public List<ViewQjtz> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("ViewQjtz", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<ViewQjtz>
	*/
	public List<ViewQjtz> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("ViewQjtz", currentPage, pageSize, orderby, asc, arr);
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
	public List<ViewQjtz> findByVarProperty(List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByVarProperty("ViewQjtz", arr);
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
	public List<ViewQjtz> findByVarPropertyBySort(String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findByPropertyBySort("ViewQjtz",orderby, asc,arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((ViewQjtz)obj).getId().toString());
		result.add(((ViewQjtz)obj).getCustomerName().toString());
		result.add(((ViewQjtz)obj).getCustomerZipCode().toString());
		result.add(((ViewQjtz)obj).getCustomerAddress().toString());
		result.add(((ViewQjtz)obj).getApplianceName().toString());
		result.add(((ViewQjtz)obj).getMandatoryCode().toString());
		result.add(((ViewQjtz)obj).getApplianceModel().toString());
		result.add(((ViewQjtz)obj).getTestCycle().toString());
		result.add(((ViewQjtz)obj).getRemark().toString());
		result.add(((ViewQjtz)obj).getMandatoryDate().toString());
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("委托单位名称");
		result.add("委托单位邮编");
		result.add("委托单位地址");
		result.add("器具名称");
		result.add("强检唯一性编号");
		result.add("器具型号规格");
		result.add("检定周期");
		result.add("备注");
		result.add("检定时间");
		
		return result;
	}
}
