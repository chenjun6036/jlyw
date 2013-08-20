package com.jlyw.manager;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.Region;
import com.jlyw.hibernate.RegionDAO;
import com.jlyw.util.KeyValueWithOperator;

public class RegionManager {
	private RegionDAO m_dao = new RegionDAO();
	/**
	 * ����Region Id ���� Region����
	 * @param id: Region Id
	 * @return Region����
	 */
	public Region findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Region��¼
	 * @param appSpecies Region����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Region appSpecies){
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
	 * ����һ��Region��¼
	 * @param appSpecies Region����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Region appSpecies){
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
	 * ����Region Id,ɾ��Region����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Region u = m_dao.findById(id);
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
	public List<Region> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Region", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @return ��ҳ���Customer�б�
	 */
	public List<Region> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Region", currentPage,pageSize, arr);
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
		return m_dao.getTotalCount("Region", arr);		
	}
}
