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
	 * ����ApplianceStandardName Id ���� ApplianceStandardName����
	 * @param id: ApplianceStandardName Id
	 * @return ApplianceStandardName����
	 */
	public ApplianceStandardName findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��ApplianceStandardName��¼
	 * @param appSpecies ApplianceStandardName����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��ApplianceStandardName��¼
	 * @param appSpecies ApplianceStandardName����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����ApplianceStandardName Id,ɾ��ApplianceStandardName����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ApplianceStandardName�б�
	 */
	public List<ApplianceStandardName> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceStandardName", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceStandardName��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceStandardName��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceStandardName", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
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
	* ��ҳ��ʾ����
	*@param queryString:��ѯ��䣨HQL��
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
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
	*@param queryString:��ѯ��䣨HQL��
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
	 * ��ѯһ����׼�����Ƿ���һ����������
	 * @param stdNameId
	 * @param speId
	 * @return ��һ����׼�����ڸ÷������棬�򷵻�true�����򣬷���false
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
		result.add("���");
		result.add("����������");
		result.add("��׼����");
		result.add("ƴ������");
		result.add("��׼����Ӣ��");
		result.add("�Ƿ�����һ֤���");
		result.add("״̬");
		
		return result;
	}
	
}
