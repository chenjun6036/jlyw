package com.jlyw.manager;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Address;
import com.jlyw.hibernate.AddressDAO;
import com.jlyw.util.KeyValueWithOperator;

public class AddressManager {
	private AddressDAO m_dao = new AddressDAO();
	/**
	 * 根据Address Id 查找 Address对象
	 * @param id: Address Id
	 * @return Address对象
	 */
	public Address findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Address记录
	 * @param appSpecies Address对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条Address记录
	 * @param appSpecies Address对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据Address Id,删除Address对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
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
	 * 按条件查找
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
	 * 得到所有ApplianceAccuracy记录数
	 * @param arr 条件键值对
	 * @return ApplianceAccuracy记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Address", arr);		
	}
}
