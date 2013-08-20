package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.hibernate.ProjectTeamDAO;
import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.util.KeyValueWithOperator;

public class ProjectTeamManager {
private ProjectTeamDAO m_dao = new ProjectTeamDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return ProjectTeam����
	 */
	public ProjectTeam findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��ProjectTeam��¼
	 * @param ProjectTeam ProjectTeam����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(ProjectTeam ProjectTeam){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(ProjectTeam);
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
	 * ����һ��ProjectTeam��¼
	 * @param ProjectTeam ProjectTeam����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(ProjectTeam ProjectTeam){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(ProjectTeam);
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
	 * ����ProjectTeam Id,ɾ��ProjectTeam����
	 * @param id ProjectTeam id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ProjectTeam u = m_dao.findById(id);
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
	 * @return ��ҳ���ProjectTeam�б�
	 */
	public List<ProjectTeam> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("ProjectTeam", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���ProjectTeam�б�
	 */
	public List<ProjectTeam> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("ProjectTeam", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ProjectTeam��¼��
	 * @return ProjectTeam��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ProjectTeam",arr);		
	}
	
	/**
	 * �õ�����ProjectTeam��¼��
	 * @return ProjectTeam��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ProjectTeam",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(ProjectTeam instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<ProjectTeam> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ProjectTeam", arr);
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
	* @param currentPage
	* ��ǰҳ��, �� 1 ��ʼ
	* @param pageSize
	* ÿҳ��ʾ������
	* @param arr ��ѯ�����?��Ӧ��ֵ
	* @return ��ҳ��������б�- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
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
	 * �õ���¼����
	 * @param queryString ��ѯ��䣨HQL��
	 * @param arr ��ѯ�����?��Ӧ��ֵ
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
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
	public List<ProjectTeam> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("ProjectTeam", orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((ProjectTeam)obj).getId().toString());
		result.add(((ProjectTeam)obj).getName().toString());
		result.add(((ProjectTeam)obj).getBrief().toString());
		result.add(((ProjectTeam)obj).getDepartment().getName().toString());
		result.add(((ProjectTeam)obj).getStatus().toString());
				
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("��Ŀ������");
		result.add("ƴ������");
		result.add("��������");
		result.add("״̬");
		
		return result;
	}
}
