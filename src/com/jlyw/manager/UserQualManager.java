package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.UserQual;
import com.jlyw.hibernate.UserQualDAO;
import com.jlyw.util.KeyValueWithOperator;

public class UserQualManager {
private UserQualDAO m_dao = new UserQualDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return UserQual����
	 */
	public UserQual findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��UserQual��¼
	 * @param UserQual UserQual����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(UserQual UserQual){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(UserQual);
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
	 * ����һ��UserQual��¼
	 * @param UserQual UserQual����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(UserQual UserQual){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(UserQual);
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
	 * ����UserQual Id,ɾ��UserQual����
	 * @param id UserQual id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			UserQual u = m_dao.findById(id);
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
	 * @return ��ҳ���UserQual�б�
	 */
	public List<UserQual> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("UserQual", currentPage,pageSize, arr);
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
	public List<UserQual> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("UserQual", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����UserQual��¼��
	 * @return UserQual��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("UserQual",arr);		
	}
	
	/**
	 * �õ�����UserQual��¼��
	 * @return UserQual��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("UserQual",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(UserQual instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * ��������ѯ
	 * @param arr
	 * @return
	 */
	public List<UserQual> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("UserQual", arr);
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
