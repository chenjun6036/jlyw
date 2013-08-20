package com.jlyw.manager.vehicle;

import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.VehicleMission;
import com.jlyw.hibernate.VehicleMissionDAO;
import com.jlyw.util.KeyValueWithOperator;

public class VehicleMissionManager {
private VehicleMissionDAO m_dao = new VehicleMissionDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return VehicleMission对象
	 */
	public VehicleMission findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条VehicleMission记录
	 * @param VehicleMission VehicleMission对象
	 * @return 插入成功，返回true；否则返回false
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
	 * 更新一条VehicleMission记录
	 * @param VehicleMission VehicleMission对象
	 * @return 更新成功，返回true；否则返回false
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
	 * 根据VehicleMission Id,删除VehicleMission对象
	 * @param id VehicleMission id
	 * @return 删除成功，返回true；否则返回false
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
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的VehicleMission列表
	 */
	public List<VehicleMission> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("VehicleMission", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有VehicleMission记录数
	 * @return VehicleMission记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("VehicleMission",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
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
		
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		
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
		
			return m_dao.findPageAllByHQL(queryString, currentPage, pageSize, arr);
		
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
}
