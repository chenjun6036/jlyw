package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Address;
import com.jlyw.hibernate.AddressDAO;
import com.jlyw.util.KeyValueWithOperator;

public class AddressManager {
	private AddressDAO m_dao = new AddressDAO();
	/**
	 * ����Address Id ���� Address����
	 * @param id: Address Id
	 * @return Address����
	 */
	public Address findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Address��¼
	 * @param appSpecies Address����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Address appSpecies){
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
	 * ����һ��Address��¼
	 * @param appSpecies Address����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Address appSpecies){
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
	 * ����Address Id,ɾ��Address����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Address u = m_dao.findById(id);
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
	public List<Address> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Address", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceAccuracy��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceAccuracy��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Address", arr);		
	}
}
