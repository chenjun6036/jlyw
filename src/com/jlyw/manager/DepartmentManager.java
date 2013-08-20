package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.Department;
import com.jlyw.hibernate.DepartmentDAO;
import com.jlyw.util.KeyValueWithOperator;

public class DepartmentManager {
private DepartmentDAO m_dao = new DepartmentDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return Department对象
	 */
	public Department findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Department记录
	 * @param Department Department对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(Department Department){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(Department);
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
	 * 更新一条Department记录
	 * @param Department Department对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(Department Department){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(Department);
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
	 * 根据Department Id,删除Department对象
	 * @param id Department id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			Department u = m_dao.findById(id);
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
	 * @return 分页后的Department列表
	 */
	public List<Department> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("Department", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的Department列表
	 */
	public List<Department> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("Department", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有Department记录数
	 * @return Department记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("Department",arr);		
	}
	
	/**
	 * 得到所有Department记录数
	 * @return Department记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("Department",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(Department instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<Department> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("Department", arr);
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
		
		result.add(((Department)obj).getId().toString());
		result.add(((Department)obj).getName().toString());
		result.add(((Department)obj).getBrief().toString());
		result.add(((Department)obj).getStatus().toString());
		result.add(((Department)obj).getDeptCode());
	
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("单位名称");
		result.add("拼音简码");		
		result.add("部门状态");		
		result.add("部门代码");
		
		return result;
	}
}
