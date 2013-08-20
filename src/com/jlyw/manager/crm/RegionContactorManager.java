/**
 * 
 */
package com.jlyw.manager.crm;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;
import org.json.me.JSONObject;

import com.jlyw.hibernate.RegionInsideContactor;
import com.jlyw.hibernate.RegionInsideContactorDAO;
import com.jlyw.hibernate.crm.InsideContactor;
import com.jlyw.hibernate.crm.InsideContactorDAO;
import com.jlyw.util.KeyValueWithOperator;

/**
 * @author xx
 *
 */
public class RegionContactorManager {
	private RegionInsideContactorDAO m_dao=new RegionInsideContactorDAO();
	/**
	 * 
	 */
	public RegionInsideContactor FindById(int id)
	{
		return m_dao.findById(id);
	}
	
	public boolean save(RegionInsideContactor c){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(c);
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
	 * 更新一条CustomerServiceFee记录
	 * @param CustomerServiceFee CustomerServiceFee对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(RegionInsideContactor c){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(c);
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
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			RegionInsideContactor u = m_dao.findById(id);
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
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(RegionInsideContactor instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<RegionInsideContactor> findByVarProperty(KeyValueWithOperator...arr){
		return m_dao.findByVarProperty("RegionInsideContactor", arr);
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
	/////////////////传进来一查询语句，返回结果数
	public int getTotalCountByHQL1(String queryString,List<Object> arr) {
		try{
			return m_dao.getTotalCountByHQL1(queryString, arr);
		}catch(Exception ex){
			return 0;
		}
	}
	
	/*public List<String> formatExcel(Object obj) {
		List<String> result = new ArrayList<String>();
		JSONObject tmp = (JSONObject) obj;
		try {
			result.add(tmp.get("customerName")==null?"/":tmp.get("customerName").toString());
			result.add(tmp.get("address")==null?"/":tmp.get("address").toString());
			result.add(tmp.get("SysName")==null?"/":tmp.get("SysName").toString());
			result.add(tmp.get("jobNum")==null?"/":tmp.get("jobNum").toString());
			result.add(tmp.get("cellphone1")==null?"/":tmp.get("cellphone1").toString());
			result.add(tmp.get("cellphone2")==null?"/":tmp.get("cellphone2").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public List<String> formatTitle() {
		List<String> result = new ArrayList<String>();
		try {
			result.add("单位名称");//编号
			result.add("地址");
			result.add("联系人姓名");
			result.add("联系人工号");
			result.add("联系电话");
			result.add("电话2");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}*/
	
	public RegionContactorManager() {
		// TODO Auto-generated constructor stub


}


}

