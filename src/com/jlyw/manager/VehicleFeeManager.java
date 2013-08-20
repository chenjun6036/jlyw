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
	 * 根据VehicleFee Id 查找 VehicleFee对象
	 * @param id: VehicleFee Id
	 * @return VehicleFee对象
	 */
	public VehicleFee findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条VehicleFee记录
	 * @param appSpecies VehicleFee对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条VehicleFee记录
	 * @param appSpecies VehicleFee对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据VehicleFee Id,删除VehicleFee对象
	 * @param id
	 * @return 删除成功，返回true；否则返回false
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
	 * 按条件查找
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @param condList 条件键值对
	 * @return 分页后的ApplianceAccuracy列表
	 */
	public List<VehicleFee> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> condList) {
		try {
			return m_dao.findPagedAll("VehicleFee", currentPage,pageSize, condList);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ApplianceAccuracy记录数
	 * @param arr 条件键值对
	 * @return ApplianceAccuracy记录数
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
		result.add("出车记录序号");
		result.add("车牌号");
		result.add("司机");
		result.add("开车日期");
		result.add("乘车人员");
		result.add("集合地点");
		result.add("行驶公里数");
		result.add("总费用");
		
		result.add("费用承担人");
		result.add("承担费用");
		
		return result;
	}
}
