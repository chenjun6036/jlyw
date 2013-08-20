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
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return Vehicle对象
	 */
	public Vehicle findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Vehicle记录
	 * @param Vehicle Vehicle对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条Vehicle记录
	 * @param Vehicle Vehicle对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据Vehicle Id,删除Vehicle对象
	 * @param id Vehicle id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Vehicle列表
	 */
	public List<Vehicle> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Vehicle", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Vehicle记录数
	 * @return Vehicle记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Vehicle",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
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
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
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
		result.add("序号");
		result.add("车牌号");
		result.add("限载人数");
		result.add("车辆型号");
		result.add("车辆品牌");
		result.add("百公里油耗");	
		result.add("默认司机");
		result.add("车辆状态");
		
		return result;
	}
}
