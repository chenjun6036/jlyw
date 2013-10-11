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
	 * ����ApplianceSpecies Id ���� ApplianceSpecies����
	 * @param id: ApplianceSpecies Id
	 * @return ApplianceSpecies����
	 */
	public ApplianceSpecies findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��ApplianceSpecies��¼
	 * @param appSpecies ApplianceSpecies����
	 * @return ����ɹ�������true�����򷵻�false
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
	 * ����һ��ApplianceSpecies��¼
	 * @param appSpecies ApplianceSpecies����
	 * @return ���³ɹ�������true�����򷵻�false
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
	 * ����ApplianceSpecies Id,ɾ��ApplianceSpecies����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
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
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param arr ������ֵ��
	 * @return ��ҳ���ApplianceSpecies�б�
	 */
	public List<ApplianceSpecies> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("ApplianceSpecies", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceSpecies��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceSpecies��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ApplianceSpecies", arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
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
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param orderby�������ĸ��ֶ�����
	* @param asc��true ���� false ����
	* @param arr:Ϊ��ѯ������(��-ֵ)������
	* @return ��ҳ��������б�- List<TaskAssign>
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
		result.add("���");
		result.add("����������");
		result.add("��������");
		result.add("ƴ������");
		result.add("״̬");
		result.add("�����");
		
		return result;
	}
}
