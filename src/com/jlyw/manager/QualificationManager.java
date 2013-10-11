package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jlyw.hibernate.Qualification;
import com.jlyw.hibernate.QualificationDAO;
import com.jlyw.hibernate.Specification;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class QualificationManager {
private QualificationDAO m_dao = new QualificationDAO();
	
	/**
	 * 根据Qualification Id 查找 Qualification对象
	 * @param id: Qualification Id
	 * @return Qualification对象
	 */
	public Qualification findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Qualification记录
	 * @param appSpecies Qualification对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Qualification appSpecies){
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
	 * 更新一条Qualification记录
	 * @param appSpecies Qualification对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Qualification appSpecies){
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
	 * 根据Qualification Id,删除Qualification对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Qualification u = m_dao.findById(id);
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
	 * @return 分页后的Qualification列表
	 */
	public List<Qualification> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Qualification", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Qualification记录数
	 * @param arr 条件键值对
	 * @return Qualification记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Qualification", arr);
	}
	/**
	 * 得到所有Qualification记录数
	 * @param arr 查询条件列表
	 * @return Qualification记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Qualification", arr);
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List<Qualification> findByExample(Qualification instance) {
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
	* @return 分页后的数据列表- List<Qualification>
	*/
	public List<Qualification> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Qualification", currentPage, pageSize, orderby, asc, arr);
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
	* @return 分页后的数据列表- List<Qualification>
	*/
	public List<Qualification> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Qualification", currentPage, pageSize, orderby, asc, arr);
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
	* 分页显示数据
	*@param queryString:查询语句（SQL）
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
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
	public List<Qualification> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Qualification", arr);
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
	public List<Qualification> findByVarProperty(List<KeyValueWithOperator> arr){
		try{
			return m_dao.findByVarProperty("Qualification", arr);
		}
		catch(Exception e){
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
	 * 获取一个授权项目所有有资质（检定/校准/检验）的检验人员
	 * @param Type：检验类型列表
	 * @param AuthItemId：授权项目Id
	 * @param AuthItemType：授权项目类型：（0、器具标准名称；1、分类名称）
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getInspectQualifyUsers(int AuthItemId, int AuthItemType, List<Integer> TypeList) throws Exception{
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
				" select b.Id, b.Type, b.ParentId, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.Type=1 and b.Id = appspe.ParentId " +
				" ) ";
			
			String queryString = " select DISTINCT u.Id, u.Name from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
				" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )";	//排除检定排外的人员
			Query queryObject = m_dao.getSession().createSQLQuery(queryAppSpeString + queryString);
			int j=0;
			queryObject.setParameter(j++, AuthItemType);
			queryObject.setParameter(j++, AuthItemId);
			for(Object i:TypeList){
				queryObject.setParameter(j++, i);
			}
			
			return queryObject.list();
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 获取一个授权项目所有有核验和签字资质的检验人员
	 * @param Type：资质类型(核验Or签字)
	 * @param AuthItemId：授权项目Id
	 * @param AuthItemType：授权项目类型：（0、器具标准名称；1、分类名称）
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getVerifyOrAuthorizeQualifyUsers(int AuthItemId, int AuthItemType, int Type) throws Exception{
		try{
			String queryAppSpeString = "with appspe as "+
				" ( " +
				" select a.Id, a.Type, a.ParentId, a.Name, a.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as a where a.Status<>1 and a.Type=? and a.Id=? "+
				" union all " +
				" select b.Id, b.Type, b.ParentId, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.Type=1 and b.Id = appspe.ParentId " +
				" ) ";
			
			String queryString = " select DISTINCT u.Id, u.Name from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type and model.Type=? ";
			return m_dao.findBySQL(queryAppSpeString + queryString, AuthItemType, AuthItemId, Type);
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 检验一个员工是否具有某个项目的某种资质
	 * @param UserId：员工ID
	 * @param AuthItemId:授权项目Id
	 * @param AuthItemType：授权项目类型：（0、器具标准名称；1、分类名称）
	 * @param Type：资质类型
	 * @return
	 * @throws Exception
	 */
	public boolean checkUserQualify(int UserId, int AuthItemId, int AuthItemType, int Type) throws Exception{
		try{
			String queryAppSpeString = "with appspe as "+
				" ( " +
				" select a.Id, a.Type, a.ParentId, a.Name, a.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as a where a.Status<>1 and a.Type=? and a.Id=? "+
				" union all " +
				" select b.Id, b.Type, b.ParentId, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.Type=1 and b.Id = appspe.ParentId " +
				" ) ";
			
			String queryString = " select DISTINCT u.Id, u.Name from " + SystemCfgUtil.DBPrexName + "SysUser as u, " + SystemCfgUtil.DBPrexName + "Qualification as model, appspe where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type and u.Id=? and model.Type=? ";
			return (m_dao.findBySQL(queryAppSpeString + queryString, AuthItemType, AuthItemId, UserId, Type).size() > 0);
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 获取一个授权项目所有有资质且符合条件（姓名）的人员
	 * @param UserName： 用户姓名
	 * @param Type：检验类型列表
	 * @param AuthItemId：授权项目Id
	 * @param AuthItemType：授权项目类型：（0、器具标准名称；1、分类名称）
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getQualifyUsers(String UserName, int AuthItemId, int AuthItemType, List<Integer> TypeList) throws Exception{
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
				" select b.Id, b.Type, b.ParentId, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.Type=1 and b.Id = appspe.ParentId " +
				" ) ";
			
			String queryString = " select DISTINCT u.Id, u.Name from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe where u.Name=? and u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList ;
			Query queryObject = m_dao.getSession().createSQLQuery(queryAppSpeString + queryString);
			int j=0;
			queryObject.setParameter(j++, AuthItemType);
			queryObject.setParameter(j++, AuthItemId);
			queryObject.setParameter(j++, UserName);
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
				queryObject1.setParameter(j++, UserName);
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
	 * 插入一批Qualification记录
	 * @param QualificationList Qualification对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<Qualification> QualificationList){
		if(QualificationList == null || QualificationList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(Qualification Qualification : QualificationList){
				m_dao.save(Qualification);
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
	 * 更新一批Qualification记录
	 * @param QualificationList Qualification对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean updateByBatch(List<Qualification> QualificationList){
		if(QualificationList == null || QualificationList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(Qualification Qualification : QualificationList){
				m_dao.update(Qualification);
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
	
	public List<String> formatExcel(Object[] obj){
		List<String> result = new ArrayList<String>();
		result.add(obj[0].toString());
		result.add(obj[1].toString());
		result.add(obj[2].toString());
		result.add(obj[3]==null?"":obj[3].toString());
		result.add(obj[4]==null?"":obj[4].toString());
		result.add(obj[5]==null?"":obj[5].toString());
		result.add(obj[6]==null?"":obj[6].toString());
		result.add(obj[7]==null?"":obj[7].toString());	
		result.add(obj[8]==null?"":obj[8].toString());
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("员工");
		result.add("项目类型");
		result.add("授权项目");
		result.add("检定");
		result.add("校准");
		result.add("检验");
		result.add("检验排外");
		result.add("核验");		
		result.add("授权签字");
		
		return result;
	}
	
}
