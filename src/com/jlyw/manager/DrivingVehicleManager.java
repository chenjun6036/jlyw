package com.jlyw.manager;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.DrivingVehicle;
import com.jlyw.hibernate.DrivingVehicleDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.SysUserDAO;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.VehicleMission;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;


public class DrivingVehicleManager {
	private DrivingVehicleDAO m_dao = new DrivingVehicleDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return User对象
	 */
	public DrivingVehicle findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条User记录
	 * @param user User对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(DrivingVehicle user){
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
	 * 更新一条User记录
	 * @param user User对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(DrivingVehicle user){
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
	 * 根据User Id,删除User对象
	 * @param id User id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			DrivingVehicle u = m_dao.findById(id);
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
	 * @param arr 条件键值对
	 * @return 分页后的User列表
	 */
	public List<DrivingVehicle> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("DrivingVehicle", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Specification列表
	 */
	public List<DrivingVehicle> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("DrivingVehicle", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 得到所有User记录数
	 * @return User记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("DrivingVehicle", arr);		
	}
	
	/**
	 * 得到所有User记录数
	 * @return User记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("DrivingVehicle",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(DrivingVehicle instance) {
		return m_dao.findByExample(instance);
	}
	

	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<DrivingVehicle> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("DrivingVehicle", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	/**
	
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List<DrivingVehicle> findAllByHQL(String queryString,List<Object> arr){
		try{
			return m_dao.findByHQL(queryString,arr);
		}catch(Exception e){
			e.printStackTrace();
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
	* 分页显示数据
	*@param queryString:查询语句（HQL）
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param arr 查询语句中?对应的值
	* @return 分页后的数据列表- List
	*/
	public List findPageAllByHQL(String queryString, int currentPage, int pageSize, List<Object> arr){
		try{
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	* 分页显示数据
	* @param currentPage
	* 当前页码, 从 1 开始
	* @param pageSize
	* 每页显示数据量
	* @param orderby：按照哪个字段排序
	* @param asc：true 增序 false 减序
	* @param arr:为查询条件的(键-值)对数组
	* @return 分页后的数据列表- List<TaskAssign>
	*/
	public List<DrivingVehicle> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator> condList){
		try{
			return m_dao.findPagedAllBySort("DrivingVehicle", currentPage, pageSize, orderby, asc, condList);
		}catch(Exception e){
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
	
	/**
	 * 得到记录总数
	 * @param queryString 查询语句（HQL）
	 * @param arr 查询语句中?对应的值
	 * @return
	 */
	public int getTotalCountByHQL(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	/**
	 *更新一批DrivingVehicle记录
	 * @param DrivingVehicleList DrivingVehicle对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean updateByBatch(List<DrivingVehicle> DrivingVehicleList){
		if(DrivingVehicleList == null || DrivingVehicleList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(DrivingVehicle drivingVehicle : DrivingVehicleList){
				m_dao.update(drivingVehicle);
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
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((DrivingVehicle)obj).getId().toString());
		result.add(((DrivingVehicle)obj).getVehicle().getLicence().toString());
		result.add(((DrivingVehicle)obj).getSysUserByDriverId().getName().toString());
		result.add(DateTimeFormatUtil.DateFormat.format(((DrivingVehicle)obj).getBeginDate()));
		//result.add(DateTimeFormatUtil.DateTimeFormat.format(((DrivingVehicle)obj).getEndDate()));
		result.add(((DrivingVehicle)obj).getPeople()==null?"":((DrivingVehicle)obj).getPeople().toString());
		result.add(((DrivingVehicle)obj).getAssemblingPlace()==null?"":((DrivingVehicle)obj).getAssemblingPlace().toString());
		result.add(((DrivingVehicle)obj).getKilometers()==null?"0":((DrivingVehicle)obj).getKilometers().toString());
		result.add(((DrivingVehicle)obj).getTotalFee()==null?"0":((DrivingVehicle)obj).getTotalFee().toString());
		result.add(((DrivingVehicle)obj).getStatus()==0?"未结算":"已结算");
		result.add(((DrivingVehicle)obj).getSysUserBySettlementId()==null?"":((DrivingVehicle)obj).getSysUserBySettlementId().getName().toString());
		result.add(((DrivingVehicle)obj).getSettlementTime()==null?"":DateTimeFormatUtil.DateFormat.format(((DrivingVehicle)obj).getSettlementTime()));
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("车牌号");
		result.add("司机");
		result.add("开车日期");
		result.add("乘车人员");
		result.add("集合地点");
		result.add("行驶公里数");
		result.add("总费用");
		result.add("是否结算");
		result.add("结算人");
		result.add("结算时间");
		
		return result;
	}
	
}

