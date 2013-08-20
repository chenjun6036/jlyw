package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.UserRoleDAO;
import com.jlyw.util.KeyValueWithOperator;

public class UserRoleManager {
private UserRoleDAO m_dao = new UserRoleDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return User����
	 */
	public UserRole findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��User��¼
	 * @param user User����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(UserRole user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(user);
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
	 * ����һ��User��¼
	 * @param user User����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(UserRole user){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(user);
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
	 * ����User Id,ɾ��User����
	 * @param id User id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			UserRole u = m_dao.findById(id);
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
	 * @return ��ҳ���User�б�
	 */
	public List<UserRole> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("UserRole", currentPage,pageSize, arr);
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
	public List<UserRole> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("UserRole", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * �õ�����User��¼��
	 * @return User��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("UserRole", arr);		
	}
	
	/**
	 * �õ�����User��¼��
	 * @return User��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("UserRole",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(UserRole instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<UserRole> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("UserRole", arr);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * ����һ��UserRole��¼
	 * @param UserRoleList UserRole����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean saveByBatch(List<UserRole> UserRoleList){
		if(UserRoleList == null || UserRoleList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(UserRole UserRole : UserRoleList){
				m_dao.save(UserRole);
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
	
}
