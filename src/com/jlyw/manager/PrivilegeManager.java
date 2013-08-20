package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Privilege;
import com.jlyw.hibernate.PrivilegeDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.util.KeyValueWithOperator;


public class PrivilegeManager {
private PrivilegeDAO m_dao = new PrivilegeDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return Privilege����
	 */
	public Privilege findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Privilege��¼
	 * @param Privilege Privilege����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Privilege Privilege){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Privilege);
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
	 * ����һ��Privilege��¼
	 * @param Privilege Privilege����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Privilege Privilege){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Privilege);
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
	 * ����Role Id,ɾ��Privilege����
	 * @param id Privilege id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Privilege u = m_dao.findById(id);
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
	 * @return ��ҳ���Privilege�б�
	 */
	public List<Privilege> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Privilege", currentPage,pageSize, arr);
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
	public List<Privilege> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("Privilege", currentPage,pageSize, arr);
		} catch (Exception e) {
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
	* @return ��ҳ��������б�- List<Privilege>
	*/
	public List<Privilege> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator>list){
		return m_dao.findPagedAllBySort("Privilege", currentPage, pageSize, orderby, asc, list);
	}
	/**
	 * �õ�����Specification��¼��
	 * @return Specification��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Privilege",arr);		
	}
	
	/**
	 * �õ�����Privilege��¼��
	 * @return Privilege��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Privilege",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Privilege instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * �ж�Ȩ�����Ƿ��Ѿ�����
	 * @param privilegeName Ȩ����
	 * @return ���ڷ���true�������ڷ���false
	 */
	public boolean isPrivilegeNameExist(String privilegeName){
		Privilege privilege= new Privilege();
		privilege.setName(privilegeName);
		List<Privilege> list= m_dao.findByExample(privilege);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<Privilege> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Privilege", arr);
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
