package com.jlyw.manager.vehicle;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.Vehicle;
import com.jlyw.hibernate.VehicleDAO;
import com.jlyw.util.KeyValueWithOperator;

public class VehicleManager {
private VehicleDAO m_dao = new VehicleDAO();
	
	/**
	 * ����User Id ���� User����
	 * @param id User Id
	 * @return Vehicle����
	 */
	public Vehicle findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��Vehicle��¼
	 * @param Vehicle Vehicle����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(Vehicle Vehicle){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Vehicle);
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
	 * ����һ��Vehicle��¼
	 * @param Vehicle Vehicle����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(Vehicle Vehicle){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Vehicle);
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
	 * ����Vehicle Id,ɾ��Vehicle����
	 * @param id Vehicle id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Vehicle u = m_dao.findById(id);
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
	 * @return ��ҳ���Vehicle�б�
	 */
	public List<Vehicle> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Vehicle", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����Vehicle��¼��
	 * @return Vehicle��¼��
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Vehicle",arr);		
	}
	
	/**
	 * ��������ϲ�ѯ
	 * @param instance ���������
	 * @return ���������ļ�¼
	 */
	public List findByExample(Vehicle instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<Vehicle> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Vehicle", arr);
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((Vehicle)obj).getId().toString());
		result.add(((Vehicle)obj).getLicence().toString());
		result.add(((Vehicle)obj).getLimit().toString());
		result.add(((Vehicle)obj).getModel().toString());
		result.add(((Vehicle)obj).getBrand().toString());
		result.add(((Vehicle)obj).getFuelFee().toString());
		result.add(((Vehicle)obj).getSysUser()==null?"":((Vehicle)obj).getSysUser().getName().toString());
		result.add(((Vehicle)obj).getStatus().toString());
				
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("���");
		result.add("���ƺ�");
		result.add("��������");
		result.add("�����ͺ�");
		result.add("����Ʒ��");
		result.add("�ٹ����ͺ�");	
		result.add("Ĭ��˾��");
		result.add("����״̬");
		
		return result;
	}
}
