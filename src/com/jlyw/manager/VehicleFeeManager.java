package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.DrivingVehicle;
import com.jlyw.hibernate.VehicleFee;
import com.jlyw.hibernate.VehicleFeeDAO;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;

public class VehicleFeeManager {
	private VehicleFeeDAO m_dao = new VehicleFeeDAO();
	/**
	 * ����VehicleFee Id ���� VehicleFee����
	 * @param id: VehicleFee Id
	 * @return VehicleFee����
	 */
	public VehicleFee findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * ����һ��VehicleFee��¼
	 * @param appSpecies VehicleFee����
	 * @return ����ɹ�������true�����򷵻�false
	 */
	public boolean save(VehicleFee appSpecies){
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
	 * ����һ��VehicleFee��¼
	 * @param appSpecies VehicleFee����
	 * @return ���³ɹ�������true�����򷵻�false
	 */
	public boolean update(VehicleFee appSpecies){
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
	 * ����VehicleFee Id,ɾ��VehicleFee����
	 * @param id
	 * @return ɾ���ɹ�������true�����򷵻�false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			VehicleFee u = m_dao.findById(id);
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
	public List<VehicleFee> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("VehicleFee", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	 * ��ҳ����
	 * @param currentPage ��ǰҳ��
	 * @param pageSize ÿҳ�ļ�¼��
	 * @param condList ������ֵ��
	 * @return ��ҳ���ApplianceAccuracy�б�
	 */
	public List<VehicleFee> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> condList) {
		try {
			return m_dao.findPagedAll("VehicleFee", currentPage,pageSize, condList);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * �õ�����ApplianceAccuracy��¼��
	 * @param arr ������ֵ��
	 * @return ApplianceAccuracy��¼��
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("VehicleFee", arr);		
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		//result.add(((VehicleFee)obj).getId().toString());
		result.add(((VehicleFee)obj).getDrivingVehicle().getId().toString());
		result.add(((VehicleFee)obj).getDrivingVehicle().getVehicle().getLicence().toString());
		result.add(((VehicleFee)obj).getDrivingVehicle().getSysUserByDriverId().getName().toString());
		result.add(DateTimeFormatUtil.DateFormat.format(((VehicleFee)obj).getDrivingVehicle().getBeginDate()));
		//result.add(DateTimeFormatUtil.DateTimeFormat.format(((VehicleFee)obj).getEndDate()));
		result.add(((VehicleFee)obj).getDrivingVehicle().getPeople()==null?"":((VehicleFee)obj).getDrivingVehicle().getPeople().toString());
		result.add(((VehicleFee)obj).getDrivingVehicle().getAssemblingPlace()==null?"":((VehicleFee)obj).getDrivingVehicle().getAssemblingPlace().toString());
		result.add(((VehicleFee)obj).getDrivingVehicle().getKilometers()==null?"0":((VehicleFee)obj).getDrivingVehicle().getKilometers().toString());
		result.add(((VehicleFee)obj).getDrivingVehicle().getTotalFee()==null?"0":((VehicleFee)obj).getDrivingVehicle().getTotalFee().toString());
		
		result.add(((VehicleFee)obj).getSysUser().getName().toString());
		result.add(((VehicleFee)obj).getFee().toString());
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("������¼���");
		result.add("���ƺ�");
		result.add("˾��");
		result.add("��������");
		result.add("�˳���Ա");
		result.add("���ϵص�");
		result.add("��ʻ������");
		result.add("�ܷ���");
		
		result.add("���óе���");
		result.add("�е�����");
		
		return result;
	}
}
