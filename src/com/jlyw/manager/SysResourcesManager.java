package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Privilege;
import com.jlyw.hibernate.SysResources;
import com.jlyw.hibernate.SysResourcesDAO;
import com.jlyw.util.KeyValueWithOperator;

public class SysResourcesManager {
	private SysResourcesDAO m_dao = new SysResourcesDAO();
	/**
	 * ����SysResources Id ���� SysResources����
	 * @param id: SysResources Id
	 * @return SysResources����
	 */
	public SysResources findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��SysResources��¼
	 * @param appSpecies SysResources����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(SysResources appSpecies){
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
	 * ����һ��SysResources��¼
	 * @param appSpecies SysResources����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(SysResources appSpecies){
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
	 * ����SysResources Id,ɾ��SysResources����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			SysResources u = m_dao.findById(id);
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
	 * ����������
	 * @param arr
	 * @return
	 */
	public List<SysResources> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SysResources", arr);
		}
		catch(Exception e){
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
	* @return ��ҳ��������б�- List<SysResources>
	*/
	public List<SysResources> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>list){
		return m_dao.findPagedAllBySort("SysResources", currentPage, pageSize, orderby, asc, list);
	}
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���SysResources�б�
	 */
	public List<SysResources> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("SysResources", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Specification�б�
	 */
	public List<SysResources> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("SysResources", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SysResources",arr);		
	}
	
	/**
	 * �õ�����SysResources��¼��
	 * @return SysResources��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SysResources",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(SysResources instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * �ж�Ȩ�����Ƿ��Ѿ�����
	 * @param SysResourcesName Ȩ����
	 * @return ���ڷ���true�������ڷ���false
	 */
	public boolean isSysResourcesNameExist(String SysResourcesName){
		SysResources SysResources= new SysResources();
		SysResources.setName(SysResourcesName);
		List<SysResources> list= m_dao.findByExample(SysResources);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
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
}
