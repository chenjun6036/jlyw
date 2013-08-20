package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.UserProfile;
import com.jlyw.hibernate.UserProfileDAO;
import com.jlyw.util.KeyValueWithOperator;

public class UserProfileManager {
	private UserProfileDAO m_dao = new UserProfileDAO();
	/**
	 * ����UserProfile Id ���� UserProfile����
	 * 
	 * @param id
	 *            UserProfile Id
	 * @return UserProfile����
	 */
	public UserProfile findById(int id) {
		return m_dao.findById(id);
	}

	/**
	 * ����һ��UserProfile��¼
	 * 
	 * @param UserProfile
	 *            UserProfile����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(UserProfile UserProfile) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.save(UserProfile);
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
	 * ����һ��UserProfile��¼
	 * 
	 * @param UserProfile
	 *            UserProfile����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(UserProfile UserProfile) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			m_dao.update(UserProfile);
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
	 * ����UserProfile Id,ɾ��UserProfile����
	 * 
	 * @param id
	 *            UserProfile id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id) {
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			UserProfile u = m_dao.findById(id);
			if (u == null) {
				return true;
			} else {
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
	 * 
	 * @param currentPage
	 *            ��ǰҳ��
	 * @param pageSize
	 *            ÿҳ�ļ�¼��
	 * @return ��ҳ���UserProfile�б�
	 */
	public List<UserProfile> findPagedAll(int currentPage, int pageSize,
			KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("UserProfile", currentPage, pageSize, arr);
		} catch (Exception e) {
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
	 * 
	 * @param arr
	 * @return
	 */
	public List<UserProfile> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("UserProfile", arr);
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
	 * �õ�����UserProfile��¼��
	 * 
	 * @return UserProfile��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr) {
		return m_dao.getTotalCount("UserProfile",arr);
	}

	/**
	 * ��������ϲ�ѯ
	 * 
	 * @param instance
	 *            ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(UserProfile instance) {
		return m_dao.findByExample(instance);

	}
}
