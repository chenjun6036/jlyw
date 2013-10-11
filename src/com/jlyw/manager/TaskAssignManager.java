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
 * �������
 * @author Administrator
 *
 */
public class TaskAssignManager {
private TaskAssignDAO m_dao = new TaskAssignDAO();

	/**
	 * ����TaskAssign Id ���� TaskAssign����
	 * @param id: TaskAssign Id
	 * @return TaskAssign����
	 */
	public TaskAssign findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��TaskAssign��¼
	 * @param appSpecies TaskAssign����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��TaskAssign��¼
	 * @param appSpecies TaskAssign����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����TaskAssign Id,ɾ��TaskAssign����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���TaskAssign�б�
	 */
	public List<TaskAssign> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		return m_dao.findPagedAll("TaskAssign", currentPage,pageSize, arr);
	}
	
	/**
	 * �õ�����TaskAssign��¼��
	 * @param arr ������ֵ��
	 * @return TaskAssign��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("TaskAssign", arr);
	}
	/**
	 * �õ�����TaskAssign��¼��
	 * @param arr ��ѯ�����б�
	 * @return TaskAssign��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("TaskAssign", arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) throws Exception{
		return m_dao.getTotalCountByHQL(queryString, arr);
	}
	
	/**
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountBySQL(String queryString,Object...arr) throws Exception{
		return m_dao.getTotalCountBySQL(queryString, arr);
	}
	
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<TaskAssign> findByExample(TaskAssign instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<TaskAssign>
	*/
	public List<TaskAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr) throws Exception{
		return m_dao.findPagedAllBySort("TaskAssign", currentPage, pageSize, orderby, asc, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)���б�
	* @return ��ҳ��������б�- List<TaskAssign>
	*/
	public List<TaskAssign> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr) throws Exception{
		return m_dao.findPagedAllBySort("TaskAssign", currentPage, pageSize, orderby, asc, arr);
	}
	
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, Object...arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr) throws Exception{
		return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPageAllBySQL(String queryString, int currentPage, int pageSize, Object...arr) throws Exception{
		return m_dao.findPageAllBySQL(queryString, currentPage, pageSize, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr) throws Exception{
		return m_dao.findByHQL(queryString, arr);
	}
	
	/**
	* ��ʾ����
	*@param queryString:��ѯ��䣨SQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findBySQL(String queryString, Object...arr) throws Exception{
		return m_dao.findBySQL(queryString, arr);
	}
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<TaskAssign> findByVarProperty(KeyValueWithOperator...arr) throws Exception{
		return m_dao.findByVarProperty("TaskAssign", arr);
	}
	
	/**
	 * ����
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
	 * ��ȡһ����Ȩ��Ŀ��������Ŀ����ĩ��Ϊ    ��׼���ƣ�����Ӧ�ļ�����Ŀ
	 * @param AuthItemId����Ȩ��ĿId
	 * @param AuthItemType����Ȩ��Ŀ���ͣ���0�����߱�׼���ƣ�1���������ƣ�
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getInspectProjects(int AuthItemId, int AuthItemType) throws Exception{
		try{
			String queryAppSpeString = "with appspe as "+	//�����һ����Ŀ�µ���������Ŀ����ĩ��Ϊ����׼���ƣ�
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
	 * ��ȡһ����Ȩ��Ŀ���������ʣ��춨/У׼/���飩�ļ�����Ա
	 * @param Type�����������б�
	 * @param AuthItemId����Ȩ��ĿId
	 * @param AuthItemType����Ȩ��Ŀ���ͣ���0�����߱�׼���ƣ�1���������ƣ�
	 * @param TaskAssignRule:����������0����ҵ��������1������ֵ����
	 * @param beginTime:ҵ�������ֵͳ�ƵĿ�ʼʱ��
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
			
//			//�д��󣬲�����������with��			
//			String queryUserString = " with inspuser as " +
//				" ( "+
//				" select DISTINCT u.Id, u.Name from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
//				" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//�ų��춨�������Ա
//				" ) ";
//			String queryString = "";
//			if(TaskAssignRule == 0){	//��ҵ������������
//				queryString =" select inspuser.Id, inspuser.Name, (select count(*) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.AssignTime>=? and t.AlloteeId=inspuser.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
//					" from inspuser order by field3 asc ";
//			}else{	//����ֵ��������
//				queryString =" select inspuser.Id, inspuser.Name, (select sum(t.totalFee) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.finishTime>=? and t.AlloteeId=inspuser.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
//					" from inspuser order by field3 asc ";
//			}
			
			String queryString = "";
			if(TaskAssignRule == 0){	//��ҵ������������
				queryString = " select DISTINCT u.Id, u.Name,(select count(*) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.FinishTime>=? and t.AlloteeId=u.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
					" from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe " + 
					" where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
					" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//�ų��춨�������Ա
					" order by field3 asc ";
			}else{	//����ֵ��������
//				queryString = " select DISTINCT u.Id, u.Name, (select sum(t.totalFee) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.finishTime>=? and t.AlloteeId=u.Id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
//					" from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe " + 
//					" where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
//					" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//�ų��춨�������Ա
//					" order by field3 asc ";
				
				queryString = " select DISTINCT u.Id, u.Name, (select sum(t.totalFee) from " + SystemCfgUtil.DBPrexName+"CertificateFeeAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where c.CheckOutDate>=? and t.FeeAlloteeId=u.Id and t.CommissionSheetId=c.Id and c.status<>10 and ( (t.OriginalRecordId is null and t.CertificateId is null) or (t.OriginalRecordId in (select Id from  " + SystemCfgUtil.DBPrexName+ "OriginalRecord as o where o.Status<>1 and o.CertificateId = t.CertificateId))  )  ) as field3 " +
					" from " + SystemCfgUtil.DBPrexName + "SysUser as u, " +SystemCfgUtil.DBPrexName + "Qualification as model, appspe " + 
					" where u.Id=model.UserId and u.Status=0 and model.Status<>1 and model.AuthItemId=appspe.Id and model.AuthItemType=appspe.Type " + typeList + 
					" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )" +	//�ų��춨�������Ա
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
			if(list.size()==0&&AuthItemType==1){//������û���κ������ʵ���Ա������ظ÷�����������������ʵ���
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
	 * �������ڽ��excel����
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
	 * �������ڽ��title
	 * @param obj
	 * @return
	 * @throws JSONException
	 */
	public List<String> formatTitle() throws JSONException{
			List<String> result = new ArrayList<String>();
			result.add("�ɶ���");
			result.add("��������");
			result.add("ί�е���");
			result.add("ί�е�λ");
			result.add("ί������");
			result.add("��ŵ����");
			result.add("��������");
			result.add("������Ȩ��");
			result.add("����");
			result.add("��������");
			result.add("״̬");
			result.add("�ֳ�������");
			result.add("����Ҫ��");
			result.add("��ע");
			
			return result;
	}
}
