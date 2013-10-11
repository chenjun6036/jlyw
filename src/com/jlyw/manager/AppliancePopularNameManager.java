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
	 * ����AppliancePopularName Id ���� AppliancePopularName����
	 * @param id: AppliancePopularName Id
	 * @return AppliancePopularName����
	 */
	public AppliancePopularName findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��AppliancePopularName��¼
	 * @param appSpecies AppliancePopularName����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��AppliancePopularName��¼
	 * @param appSpecies AppliancePopularName����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����AppliancePopularName Id,ɾ��AppliancePopularName����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���AppliancePopularName�б�
	 */
	public List<AppliancePopularName> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("AppliancePopularName", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����AppliancePopularName��¼��
	 * @param arr ������ֵ��
	 * @return AppliancePopularName��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("AppliancePopularName", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
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
		result.add("���");
		result.add("��׼����");
		result.add("��������");
		result.add("ƴ������");
		result.add("״̬");
		
		return result;
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
	
	
}
