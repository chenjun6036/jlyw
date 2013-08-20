package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.CustomerContactorDAO;
import com.jlyw.util.KeyValueWithOperator;

public class CustomerContactorManager {

private CustomerContactorDAO m_dao = new CustomerContactorDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return CustomerContactor����
	 */
	public CustomerContactor findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��CustomerContactor��¼
	 * @param CustomerContactor CustomerContactor����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(CustomerContactor CustomerContactor){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(CustomerContactor);
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
	 * ����һ��CustomerContactor��¼
	 * @param CustomerContactor CustomerContactor����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(CustomerContactor CustomerContactor){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(CustomerContactor);
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
	 * ����CustomerContactor Id,ɾ��CustomerContactor����
	 * @param id CustomerContactor id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			CustomerContactor u = m_dao.findById(id);
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
	 * @return ��ҳ���CustomerContactor�б�
	 */
	public List<CustomerContactor> findPagedAll(int currentPage, int pageSize) {
		try {
			return m_dao.findPagedAll("CustomerContactor", currentPage,pageSize);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Customer�б�
	 */
	public List<CustomerContactor> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("CustomerContactor", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceAccuracy��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceAccuracy��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("CustomerContactor", arr);		
	}
	
	/**
	 * �õ�����CustomerContactor��¼��
	 * @return CustomerContactor��¼��
	 */
	public int getTotalCount(){
		return m_dao.getTotalCount("CustomerContactor");		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(CustomerContactor instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<CustomerContactor> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("CustomerContactor", orderby, asc, arr);
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
	public List<CustomerContactor> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("CustomerContactor", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
}
