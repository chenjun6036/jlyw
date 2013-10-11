package com.jlyw.manager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.TaskAssign;
import com.jlyw.hibernate.TaskAssignDAO;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

/**
 * 任务分配
 * @author Administrator
 *
 */
public class TaskAssignManager {
private TaskAssignDAO m_dao = new TaskAssignDAO();

	/**
	 * 根据TaskAssign Id 查找 TaskAssign对象
	 * @param id: TaskAssign Id
	 * @return TaskAssign对象
	 */
	public TaskAssign findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条TaskAssign记录
	 * @param appSpecies TaskAssign对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(TaskAssign appSpecies){
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
	 * 更新一条TaskAssign记录
	 * @param appSpecies TaskAssign对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(TaskAssign appSpecies){
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
	 * 根据TaskAssign Id,删除TaskAssign对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			TaskAssign u = m_dao.findById(id);
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
	 * @return 分页后的TaskAssign列表
	 */
	public List<TaskAssign> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		return m_dao.findPagedAll("TaskAssign", currentPage,pageSize, arr);
	}
	
	/**
	 * 得到所有TaskAssign记录数
	 * @param arr 条件键值对
	 * @return TaskAssign记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TaskAssign", arr);
	}
	/**
	 * 得到所有TaskAssign记录数
	 * @param arr 查询条件列表
	 * @return TaskAssign记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("TaskAssign", arr);
	}
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountBySQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountBySQL(queryString, arr);
	}
	
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<TaskAssign> findByExample(TaskAssign instance) {
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
	* @return 分页后的数据列表- List<TaskAssign>
	*/
	public List<TaskAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr) throws Exception{
		return m_dao.findPagedAllBySort("TaskAssign", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<TaskAssign>
	*/
	public List<TaskAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr) throws Exception{
		return m_dao.findPagedAllBySort("TaskAssign", currentPage, pageSize, orderby, asc, arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
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
	public List findPageAllBySQL(String queryString, int currentPage, int pageSize, Object...arr) throws Exception{
		return m_dao.findPageAllBySQL(queryString, currentPage, pageSize, arr);
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, Object...arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
	}
	
	/**
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
	}
	
	/**
	* 显示数据
	*@param queryString:查询语句（SQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findBySQL(String queryString, Object...arr) throws Exception{
		return m_dao.findBySQL(queryString, arr);
	}
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<TaskAssign> findByVarProperty(KeyValueWithOperator...arr) throws Exception{
		return m_dao.findByVarProperty("TaskAssign", arr);
	}
	
	/**
	 * 更新
	 * @param updateList
	 * @param arr
	 * @return
	 */
	public int update(List<KeyValue> updateList, KeyValue...arr){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			int iRet = m_dao.update("TaskAssign", updateList, arr);
			tran.commit();
			return iRet;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return -1;
		} finally {
			m_dao.closeSession();
		}
		
	}
	
	/**
	 * 获取一个授权项目及其子项目（最末级为    标准名称）所对应的检验项目
	 * @param AuthItemId：授权项目Id
	 * @param AuthItemType：授权项目类型：（0、器具标准名称；1、分类名称）
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getInspectProjects(int AuthItemId, int AuthItemType) throws Exception{
		try{
			String queryAppSpeString = "with appspe as "+	//缓存表：一个项目下的所有子项目（最末级为：标准名称）
				" ( " +
				" select a.Id, a.Type, a.ParentId, a.Name, a.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as a where a.Status<>1 and a.Type=? and a.Id=? "+
				" union all " +
				" select b.Id, b.Type, b.Parentid, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.ParentId = appspe.Id and appspe.Type=1 and (b.Type=1 or b.Type=0)" +
				" ) ";
			
			String queryString = " select DISTINCT p.Id, p.AppStdNameId, p.ProjectName from " + SystemCfgUtil.DBPrexName + "AppStdName_ProTeam as p where p.Status=0 and p.AppStdNameId in (select Id from appspe where Type=0)";
			return m_dao.findBySQL(queryAppSpeString + queryString, AuthItemType, AuthItemId);
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 获取一个授权项目所有有资质（检定/校准/检验）的检验人员
	 * @param Type：检验类型列表
	 * @param AuthItemId：授权项目Id
	 * @param AuthItemType：授权项目类型：（0、器具标准名称；1、分类名称）
	 * @param TaskAssignRule:任务分配规则（0：按业务量排序；1、按产值排序）
	 * @param beginTime:业务量或产值统计的开始时间
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getInspectQualifyUsersByRule(int AuthItemId, int AuthItemType, List<Integer> TypeList, int TaskAssignRule, Timestamp beginTime) throws Exception{
		try{
			String typeList = "";
			if(TypeList.size() > 0){
				typeList += " and ( ";
				for(int i = 0; i < TypeList.size(); i++){
					typeList += " model.Type = ? ";
					if(i < TypeList.size() - 1){
						typeList += " or ";
					}
				}
				typeList += " ) ";
			}
			
			String queryAppSpeString = "with appspe as "+
				" ( " +
				" select a.Id, a.Type, a.ParentId, a.Name, a.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as a where a.Status<>1 and a.Type=? and a.Id=? "+
				" union all " +
				" select b.Id, b.Type, b.Parentid, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.Type=1 and b.Id = appspe.ParentId " +
				" ) ";
			
//			//有错误，不能连着两个with？			
//			String queryUserString = " with inspuser as " +
//				" ( "+
//				" select DISTINCT u.Id, u.Name from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
//				" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//排除检定排外的人员
//				" ) ";
//			String queryString = "";
//			if(TaskAssignRule == 0){	//按业务量升序排序
//				queryString =" select inspuser.Id, inspuser.Name, (select count(*) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.AssignTime>=? and t.AlloteeId=inspuser.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
//					" from inspuser order by field3 asc ";
//			}else{	//按产值升序排序
//				queryString =" select inspuser.Id, inspuser.Name, (select sum(t.totalFee) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.finishTime>=? and t.AlloteeId=inspuser.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
//					" from inspuser order by field3 asc ";
//			}
			
			String queryString = "";
			if(TaskAssignRule == 0){	//按业务量升序排序
				queryString = " select DISTINCT u.Id, u.Name,(select count(*) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.FinishTime>=? and t.AlloteeId=u.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
					" from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe " + 
					" where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
					" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//排除检定排外的人员
					" order by field3 asc ";
			}else{	//按产值升序排序
//				queryString = " select DISTINCT u.Id, u.Name, (select sum(t.totalFee) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.finishTime>=? and t.AlloteeId=u.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
//					" from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe " + 
//					" where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
//					" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//排除检定排外的人员
//					" order by field3 asc ";
				
				queryString = " select DISTINCT u.Id, u.Name, (select sum(t.totalFee) from " + SystemCfgUtil.DBPrexName+"CertificateFeeAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where c.CheckOutDate>=? and t.FeeAlloteeId=u.Id and t.CommissionSheetId=c.Id and c.status<>10 and ( (t.OriginalRecordId is null and t.CertificateId is null) or (t.OriginalRecordId in (select Id from  " + SystemCfgUtil.DBPrexName+ "OriginalRecord as o where o.Status<>1 and o.CertificateId = t.CertificateId))  )  ) as field3 " +
					" from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe " + 
					" where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
					" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//排除检定排外的人员
					" order by field3 asc ";
			}
			
			Query queryObject = m_dao.getSession().createSQLQuery(queryAppSpeString + queryString);
			int j=0;
			queryObject.setParameter(j++, AuthItemType);
			queryObject.setParameter(j++, AuthItemId);
			queryObject.setParameter(j++, beginTime);
			for(Object i:TypeList){
				queryObject.setParameter(j++, i);
			}
			List list = queryObject.list();
			if(list.size()==0&&AuthItemType==1){//若该类没有任何有资质的人员，则加载该分类下所有类别有资质的人
				String queryStr = "with appspe as( " + 
								"select a.Id, a.ParentId, 1 as Type from" +  SystemCfgUtil.DBPrexName + "ApplianceSpecies as a where a.Id = ? " + 
								"union all " + 
								"select b.Id, b.ParentId, 1 as Type from" + SystemCfgUtil.DBPrexName + "ApplianceSpecies as b, appspe where b.ParentId = appspe.Id and appspe.type = 1 " +  
								"union all " + 
								"select c.Id, c.SpeciesId, 0 as Type from" + SystemCfgUtil.DBPrexName + "ApplianceStandardName as c, appspe where c.SpeciesId = appspe.Id and appspe.type = 1 " + 
								")";
				Query queryObject1 = m_dao.getSession().createSQLQuery(queryStr + queryString);
				j = 0;
				queryObject1.setParameter(j++, AuthItemId);
				queryObject1.setParameter(j++, beginTime);
				for(Object i : TypeList){
					queryObject1.setParameter(j++, i);
				}
				list = queryObject1.list();
			}
			return list;
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 导出超期结果excel内容
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("Allotte")?jsonObj.getString("Allotte"):"");
		result.add(jsonObj.has("Days")?jsonObj.getString("Days"):"");
		result.add(jsonObj.has("Code")?jsonObj.getString("Code"):"");
		result.add(jsonObj.has("Customer")?jsonObj.getString("Customer"):"");
		result.add(jsonObj.has("CommissionDate")?jsonObj.getString("CommissionDate"):"");
		result.add(jsonObj.has("PromiseDate")?jsonObj.getString("PromiseDate"):"");
		result.add(jsonObj.has("ApplianceName")?jsonObj.getString("ApplianceName"):"");
		result.add(jsonObj.has("ApplianceSpeciesName")?jsonObj.getString("ApplianceSpeciesName"):"");
		result.add(jsonObj.has("Quantity")?jsonObj.getString("Quantity"):"");
		result.add(jsonObj.has("WithQuantity")?jsonObj.getString("WithQuantity"):"");
		result.add(jsonObj.has("Status")?jsonObj.getString("Status"):"");
		result.add(jsonObj.has("LocaleSiteManager")?jsonObj.getString("LocaleSiteManager"):"");
		result.add(jsonObj.has("OtherRequirements")?jsonObj.getString("OtherRequirements"):"");
		result.add(jsonObj.has("Remark")?jsonObj.getString("Remark"):"");
		
		return result;
	}
	
	/**
	 * 导出超期结果title
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> formatTitle() throws JSONException{
			List<String> result = new ArrayList<String>();
			result.add("派定人");
			result.add("超期天数");
			result.add("委托单号");
			result.add("委托单位");
			result.add("委托日期");
			result.add("承诺日期");
			result.add("器具名称");
			result.add("器具授权名");
			result.add("数量");
			result.add("退样数量");
			result.add("状态");
			result.add("现场负责人");
			result.add("其他要求");
			result.add("备注");
			
			return result;
	}
}
