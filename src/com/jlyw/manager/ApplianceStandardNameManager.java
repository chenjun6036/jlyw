package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.ApplianceStandardNameDAO;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class ApplianceStandardNameManager {
	private ApplianceStandardNameDAO m_dao = new ApplianceStandardNameDAO();
	
	/**
	 * 根据ApplianceStandardName Id 查找 ApplianceStandardName对象
	 * @param id: ApplianceStandardName Id
	 * @return ApplianceStandardName对象
	 */
	public ApplianceStandardName findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条ApplianceStandardName记录
	 * @param appSpecies ApplianceStandardName对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(ApplianceStandardName appSpecies){
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
	 * 更新一条ApplianceStandardName记录
	 * @param appSpecies ApplianceStandardName对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(ApplianceStandardName appSpecies){
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
	 * 根据ApplianceStandardName Id,删除ApplianceStandardName对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ApplianceStandardName u = m_dao.findById(id);
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
	 * @return 分页后的ApplianceStandardName列表
	 */
	public List<ApplianceStandardName> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceStandardName", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ApplianceStandardName记录数
	 * @param arr 条件键值对
	 * @return ApplianceStandardName记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceStandardName", arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<ApplianceStandardName> findByExample(ApplianceStandardName instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<ApplianceStandardName> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ApplianceStandardName", arr);
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
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findBySQL(String queryString, Object...arr){
		try{
			return m_dao.findBySQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 查询一个标准名称是否在一个分类下面
	 * @param stdNameId
	 * @param speId
	 * @return 若一个标准名称在该分类下面，则返回true，否则，返回false
	 */
	public boolean checkStandardNameInSpecial(int stdNameId, int speId) throws Exception{
		String queryAppSpeString = "with appspe as "+
			" ( " +
			" select a.Id, a.Type, a.ParentId, a.Name, a.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as a where a.Type=0 and a.Id=? "+
			" union all " +
			" select b.Id, b.Type, b.Parentid, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Type=1 and b.Id = appspe.ParentId " +
			" ) ";
		String queryString = " select count(*) from appspe where appspe.Type=1 and appspe.Id=? ";
		Query queryObject = m_dao.getSession().createSQLQuery(queryAppSpeString + queryString);
		int j=0;
		queryObject.setParameter(j++, stdNameId);
		queryObject.setParameter(j++, speId);
		
		List cc = queryObject.list();
		Integer a = (Integer) cc.get(0);
		return (a.intValue() > 0)?true:false;
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((ApplianceStandardName)obj).getId().toString());
		String parentStr = "";
		ApplianceSpecies parentSpec = ((ApplianceStandardName)obj).getApplianceSpecies();
		while(parentSpec!=null)
		{
			parentStr = parentSpec.getName().concat("-" + parentStr);
			parentSpec = parentSpec.getParentSpecies();
		}
		result.add(parentStr.length()==0?"":parentStr.substring(0, parentStr.length()-1));
		result.add(((ApplianceStandardName)obj).getName().toString());
		result.add(((ApplianceStandardName)obj).getBrief().toString());
		result.add(((ApplianceStandardName)obj).getNameEn().toString());
		result.add(((ApplianceStandardName)obj).getHoldMany().toString());
		result.add(((ApplianceStandardName)obj).getStatus().toString());
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("父分类名称");
		result.add("标准名称");
		result.add("拼音简码");
		result.add("标准名称英文");
		result.add("是否允许一证多件");
		result.add("状态");
		
		return result;
	}
	
}
