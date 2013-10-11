package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceSpeciesDAO;
import com.jlyw.util.KeyValueWithOperator;

public class ApplianceSpeciesManager {
	private ApplianceSpeciesDAO m_dao = new ApplianceSpeciesDAO();
	
	/**
	 * 根据ApplianceSpecies Id 查找 ApplianceSpecies对象
	 * @param id: ApplianceSpecies Id
	 * @return ApplianceSpecies对象
	 */
	public ApplianceSpecies findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条ApplianceSpecies记录
	 * @param appSpecies ApplianceSpecies对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(ApplianceSpecies appSpecies){
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
	 * 更新一条ApplianceSpecies记录
	 * @param appSpecies ApplianceSpecies对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(ApplianceSpecies appSpecies){
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
	 * 根据ApplianceSpecies Id,删除ApplianceSpecies对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ApplianceSpecies u = m_dao.findById(id);
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
	 * @return 分页后的ApplianceSpecies列表
	 */
	public List<ApplianceSpecies> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceSpecies", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ApplianceSpecies记录数
	 * @param arr 条件键值对
	 * @return ApplianceSpecies记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceSpecies", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ApplianceSpecies> findByExample(ApplianceSpecies instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<ApplianceSpecies> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ApplianceSpecies", arr);
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
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<TaskAssign>
	*/
	public List<ApplianceSpecies> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("ApplianceSpecies", orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((ApplianceSpecies)obj).getId().toString());
		String parentStr = "";
		ApplianceSpecies parentSpec = ((ApplianceSpecies)obj).getParentSpecies();
		while(parentSpec!=null)
		{
			parentStr = parentSpec.getName().concat("-" + parentStr);
			parentSpec = parentSpec.getParentSpecies();
		}
		result.add(parentStr.length()==0?"":parentStr.substring(0, parentStr.length()-1));
		result.add(((ApplianceSpecies)obj).getName().toString());
		result.add(((ApplianceSpecies)obj).getBrief().toString());
		result.add(((ApplianceSpecies)obj).getStatus().toString());
		result.add(((ApplianceSpecies)obj).getSequence()==null?"":((ApplianceSpecies)obj).getSequence().toString());
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("父分类名称");
		result.add("分类名称");
		result.add("拼音简码");
		result.add("状态");
		result.add("排序号");
		
		return result;
	}
}
