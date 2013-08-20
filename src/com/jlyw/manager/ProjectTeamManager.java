package com.jlyw.manager;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.hibernate.ProjectTeamDAO;
import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.util.KeyValueWithOperator;

public class ProjectTeamManager {
private ProjectTeamDAO m_dao = new ProjectTeamDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return ProjectTeam对象
	 */
	public ProjectTeam findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条ProjectTeam记录
	 * @param ProjectTeam ProjectTeam对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(ProjectTeam ProjectTeam){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(ProjectTeam);
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
	 * 更新一条ProjectTeam记录
	 * @param ProjectTeam ProjectTeam对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(ProjectTeam ProjectTeam){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(ProjectTeam);
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
	 * 根据ProjectTeam Id,删除ProjectTeam对象
	 * @param id ProjectTeam id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			ProjectTeam u = m_dao.findById(id);
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
	 * @return 分页后的ProjectTeam列表
	 */
	public List<ProjectTeam> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator... arr) {
		try {
			return m_dao.findPagedAll("ProjectTeam", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 分页函数
	 * @param currentPage 当前页码
	 * @param pageSize 每页的记录数
	 * @return 分页后的ProjectTeam列表
	 */
	public List<ProjectTeam> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("ProjectTeam", currentPage,pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到所有ProjectTeam记录数
	 * @return ProjectTeam记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("ProjectTeam",arr);		
	}
	
	/**
	 * 得到所有ProjectTeam记录数
	 * @return ProjectTeam记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("ProjectTeam",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(ProjectTeam instance) {
		return m_dao.findByExample(instance);
		
	}
	
	/**
	 * 多条件查询
	 * @param arr
	 * @return
	 */
	public List<ProjectTeam> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("ProjectTeam", arr);
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
	public List<ProjectTeam> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		try{
			return m_dao.findByPropertyBySort("ProjectTeam", orderby, asc, arr);
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		
		result.add(((ProjectTeam)obj).getId().toString());
		result.add(((ProjectTeam)obj).getName().toString());
		result.add(((ProjectTeam)obj).getBrief().toString());
		result.add(((ProjectTeam)obj).getDepartment().getName().toString());
		result.add(((ProjectTeam)obj).getStatus().toString());
				
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("序号");
		result.add("项目组名称");
		result.add("拼音简码");
		result.add("所述部门");
		result.add("状态");
		
		return result;
	}
}
