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
	 * ����Qualification Id ���� Qualification����
	 * @param id: Qualification Id
	 * @return Qualification����
	 */
	public Qualification findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Qualification��¼
	 * @param appSpecies Qualification����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��Qualification��¼
	 * @param appSpecies Qualification����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����Qualification Id,ɾ��Qualification����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���Qualification�б�
	 */
	public List<Qualification> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("Qualification", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Qualification��¼��
	 * @param arr ������ֵ��
	 * @return Qualification��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Qualification", arr);
	}
	/**
	 * �õ�����Qualification��¼��
	 * @param arr ��ѯ�����б�
	 * @return Qualification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Qualification", arr);
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List<Qualification> findByExample(Qualification instance) {
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
	* @return ��ҳ��������б�- List<Qualification>
	*/
	public List<Qualification> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findPagedAllBySort("Qualification", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
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
	* @return ��ҳ��������б�- List<Qualification>
	*/
	public List<Qualification> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>arr){
		try{
			return m_dao.findPagedAllBySort("Qualification", currentPage, pageSize, orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, Object...arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨SQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findBySQL(String queryString, Object...arr){
		try{
			return m_dao.findBySQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findByHQL(String queryString, List<Object> arr){
		try{
			return m_dao.findByHQL(queryString, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��������ѯ
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
	 * ��������ѯ
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
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
	 * ��ȡһ����Ȩ��Ŀ���������ʣ��춨/У׼/���飩�ļ�����Ա
	 * @param Type�����������б�
	 * @param AuthItemId����Ȩ��ĿId
	 * @param AuthItemType����Ȩ��Ŀ���ͣ���0�����߱�׼���ƣ�1���������ƣ�
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
				" and u.Id not in (select m.UserId from "+ SystemCfgUtil.DBPrexName + "Qualification as m, appspe where m.Status<>1 and m.AuthItemId=appspe.Id and m.AuthItemType=appspe.Type and m.Type="+FlagUtil.QualificationType.Type_Except+" )";	//�ų��춨�������Ա
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
	 * ��ȡһ����Ȩ��Ŀ�����к����ǩ�����ʵļ�����Ա
	 * @param Type����������(����Orǩ��)
	 * @param AuthItemId����Ȩ��ĿId
	 * @param AuthItemType����Ȩ��Ŀ���ͣ���0�����߱�׼���ƣ�1���������ƣ�
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
	 * ����һ��Ա���Ƿ����ĳ����Ŀ��ĳ������
	 * @param UserId��Ա��ID
	 * @param AuthItemId:��Ȩ��ĿId
	 * @param AuthItemType����Ȩ��Ŀ���ͣ���0�����߱�׼���ƣ�1���������ƣ�
	 * @param Type����������
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
	 * ��ȡһ����Ȩ��Ŀ�����������ҷ�������������������Ա
	 * @param UserName�� �û�����
	 * @param Type�����������б�
	 * @param AuthItemId����Ȩ��ĿId
	 * @param AuthItemType����Ȩ��Ŀ���ͣ���0�����߱�׼���ƣ�1���������ƣ�
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
	 * ����һ��Qualification��¼
	 * @param QualificationList Qualification����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��Qualification��¼
	 * @param QualificationList Qualification����
	 * @return ���³ɹ�������true�����򷵻�false
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
		result.add("Ա��");
		result.add("��Ŀ����");
		result.add("��Ȩ��Ŀ");
		result.add("�춨");
		result.add("У׼");
		result.add("����");
		result.add("��������");
		result.add("����");		
		result.add("��Ȩǩ��");
		
		return result;
	}
	
}
