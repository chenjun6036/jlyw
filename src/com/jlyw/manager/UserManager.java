package com.jlyw.manager;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Transaction;

import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.SysUserDAO;
import com.jlyw.util.KeyValueWithOperator;


public class UserManager {
	private SysUserDAO m_dao = new SysUserDAO();
	
	/**
	 * 根据User Id 查找 User对象
	 * @param id User Id
	 * @return User对象
	 */
	public SysUser findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条User记录
	 * @param user User对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(SysUser user){
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
	public boolean update(SysUser user){
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
			SysUser u = m_dao.findById(id);
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
	public List<SysUser> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("SysUser", currentPage,pageSize, arr);
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
	public List<SysUser> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("SysUser", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 得到所有User记录数
	 * @return User记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("SysUser", arr);		
	}
	
	/**
	 * 得到所有User记录数
	 * @return User记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("SysUser",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(SysUser instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 判断用户名是否已经存在
	 * @param userName 用户名
	 * @return 存在返回true，不存在返回false
	 */
	public boolean isUserNameExist(String userName){
		SysUser user = new SysUser();
		user.setUserName(userName);
		List<SysUser> list= m_dao.findByExample(user);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<SysUser> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("SysUser", arr);
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
	public List<SysUser> findPagedAllBySort(int currentPage, int pageSize, String orderby,boolean asc,List<KeyValueWithOperator> condList){
		try{
			return m_dao.findPagedAllBySort("SysUser", currentPage, pageSize, orderby, asc, condList);
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
	* 根据HQL更新
	* @param updateString HQL语句（update 表名 set 字段=值 where 条件）
	* @param arr 参数
	* @return 更新操作影响的记录数
	*/
	public int updateByHQL(String updateString, Object...arr) {		
		Transaction tran = m_dao.getSession().beginTransaction();
		try{
			int i = m_dao.updateByHQL(updateString, arr);
			tran.commit();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			tran.rollback();
			return 0;
		}finally {
			m_dao.closeSession();
		}
	}
	
	public List<String> formatExcel(Object obj){
		List<String> result = new ArrayList<String>();
		result.add(((SysUser)obj).getJobNum().toString());
		result.add(((SysUser)obj).getName().toString());
		result.add(((SysUser)obj).getGender()?"男":"女");
		result.add(((SysUser)obj).getWorkLocationId().toString());
		result.add(((SysUser)obj).getBirthday().toString());
		result.add(((SysUser)obj).getBirthplace().toString());
		result.add(((SysUser)obj).getIdnum().toString());
		result.add(((SysUser)obj).getPoliticsStatus().toString());
		result.add(((SysUser)obj).getNation().toString());
		result.add(((SysUser)obj).getWorkSince().toString());
		result.add(((SysUser)obj).getWorkHereSince().toString());
		result.add(((SysUser)obj).getEducation().toString());
		result.add(((SysUser)obj).getEducationDate().toString());
		result.add(((SysUser)obj).getEducationFrom().toString());
		result.add(((SysUser)obj).getDegree().toString());
		result.add(((SysUser)obj).getDegreeDate().toString());
		result.add(((SysUser)obj).getDegreeFrom().toString());
		result.add(((SysUser)obj).getJobTitle().toString());
		result.add(((SysUser)obj).getSpecialty().toString());
		result.add(((SysUser)obj).getAdministrationPost().toString());
		result.add(((SysUser)obj).getPartyPost()==null?"":((SysUser)obj).getPartyPost().toString());
		result.add(((SysUser)obj).getPartyDate()==null?"":((SysUser)obj).getPartyDate().toString());
		result.add(((SysUser)obj).getHomeAdd().toString());
		result.add(((SysUser)obj).getWorkAdd().toString());
		result.add(((SysUser)obj).getTel().toString());
		result.add(((SysUser)obj).getCellphone1().toString());
		result.add(((SysUser)obj).getCellphone2()==null?"":((SysUser)obj).getCellphone2().toString());
		result.add(((SysUser)obj).getEmail().toString());
		result.add(((SysUser)obj).getProjectTeamId().toString());
		result.add(((SysUser)obj).getStatus().toString());
		result.add(((SysUser)obj).getType().toString());
		result.add(((SysUser)obj).getCancelDate()==null?"":((SysUser)obj).getCancelDate().toString());
		result.add(((SysUser)obj).getRemark()==null?"":((SysUser)obj).getRemark().toString());
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("工号");
		result.add("姓名");
		result.add("性别");
		result.add("所在中心");
		result.add("出生日期");
		result.add("出生地");
		result.add("身份证号");
		result.add("政治面貌");
		result.add("民族");
		result.add("参加工作日期");
		result.add("进所工作日期");
		result.add("学历");
		result.add("取得学历时间");
		result.add("学历毕业院校");
		result.add("学位");
		result.add("取得学位时间");
		result.add("学位毕业院校");
		result.add("职称");
		result.add("专业");
		result.add("行政职务");
		result.add("党内职务");
		result.add("入党时间");
		result.add("家庭住址");
		result.add("工作地址");
		result.add("办公电话");
		result.add("联系电话1");
		result.add("联系电话2");
		result.add("Email");
		result.add("所属项目组");
		result.add("状态");
		result.add("人员性质");
		result.add("注销时间");
		result.add("备注");
		
		return result;
	}
	
}

