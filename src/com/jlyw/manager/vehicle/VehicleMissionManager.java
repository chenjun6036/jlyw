package com.jlyw.manager.vehicle;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.VehicleMission;
import com.jlyw.hibernate.VehicleMissionDAO;
import com.jlyw.util.KeyValueWithOperator;

public class VehicleMissionManager {
private VehicleMissionDAO m_dao = new VehicleMissionDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return VehicleMission����
	 */
	public VehicleMission findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��VehicleMission��¼
	 * @param VehicleMission VehicleMission����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(VehicleMission VehicleMission){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(VehicleMission);
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
	 * ����һ��VehicleMission��¼
	 * @param VehicleMission VehicleMission����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(VehicleMission VehicleMission){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(VehicleMission);
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
	 * ����VehicleMission Id,ɾ��VehicleMission����
	 * @param id VehicleMission id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			VehicleMission u = m_dao.findById(id);
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
	 * @return ��ҳ���VehicleMission�б�
	 */
	public List<VehicleMission> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("VehicleMission", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����VehicleMission��¼��
	 * @return VehicleMission��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("VehicleMission",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(VehicleMission instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<VehicleMission> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("VehicleMission", arr);
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
		
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		
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
		
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		
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
}
