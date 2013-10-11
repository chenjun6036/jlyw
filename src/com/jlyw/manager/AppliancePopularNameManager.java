package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.AppliancePopularNameDAO;
import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.util.KeyValueWithOperator;

public class AppliancePopularNameManager {
private AppliancePopularNameDAO m_dao = new AppliancePopularNameDAO();
	
	/**
	 * 根据AppliancePopularName Id 查找 AppliancePopularName对象
	 * @param id: AppliancePopularName Id
	 * @return AppliancePopularName对象
	 */
	public AppliancePopularName findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条AppliancePopularName记录
	 * @param appSpecies AppliancePopularName对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(AppliancePopularName appSpecies){
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
	 * 更新一条AppliancePopularName记录
	 * @param appSpecies AppliancePopularName对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(AppliancePopularName appSpecies){
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
	 * 根据AppliancePopularName Id,删除AppliancePopularName对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			AppliancePopularName u = m_dao.findById(id);
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
	 * @return 分页后的AppliancePopularName列表
	 */
	public List<AppliancePopularName> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("AppliancePopularName", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有AppliancePopularName记录数
	 * @param arr 条件键值对
	 * @return AppliancePopularName记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("AppliancePopularName", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<AppliancePopularName> findByExample(AppliancePopularName instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<AppliancePopularName> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("AppliancePopularName", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	public List<Object> findBySQL(String queryString, Object...arr){
		return m_dao.findBySQL(queryString, arr);
	}

	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((AppliancePopularName)obj).getId().toString());
		String parentStr = "";
		ApplianceSpecies parentSpec = ((AppliancePopularName)obj).getApplianceStandardName().getApplianceSpecies();
		while(parentSpec!=null)
		{
			parentStr = parentSpec.getName().concat("-" + parentStr);
			parentSpec = parentSpec.getParentSpecies();
		}
		parentStr = parentStr + ((AppliancePopularName)obj).getApplianceStandardName().getName();
		result.add(parentStr.length()==0?"":parentStr);
		result.add(((AppliancePopularName)obj).getPopularName().toString());
		result.add(((AppliancePopularName)obj).getBrief().toString());
		result.add(((AppliancePopularName)obj).getStatus().toString());
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("标准名称");
		result.add("常用名称");
		result.add("拼音简码");
		result.add("状态");
		
		return result;
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
	
	
}
