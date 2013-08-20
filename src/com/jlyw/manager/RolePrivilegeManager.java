package com.jlyw.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Transaction;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.Role;
import com.jlyw.hibernate.RolePrivilege;
import com.jlyw.hibernate.RolePrivilegeDAO;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.UserRoleDAO;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.UrlInfo;

public class RolePrivilegeManager {

private RolePrivilegeDAO m_dao = new RolePrivilegeDAO();
	
	/**
	 * 根据Role Id 查找 Role对象
	 * @param id Role Id
	 * @return User对象
	 */
	public RolePrivilege findById(int id) {
		return m_dao.findById(id);
	}
	
	/**
	 * 插入一条Role记录
	 * @param role Role对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean save(RolePrivilege role){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {	
			m_dao.save(role);
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
	 * 更新一条Role记录
	 * @param role Role对象
	 * @return 更新成功，返回true；否则返回false
	 */
	public boolean update(RolePrivilege role){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			m_dao.update(role);
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
	 * 根据Role Id,删除Role对象
	 * @param id Role id
	 * @return 删除成功，返回true；否则返回false
	 */
	public boolean deleteById(int id){
		Transaction tran = m_dao.getSession().beginTransaction();
		try {			
			RolePrivilege u = m_dao.findById(id);
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
	 * @return 分页后的Role列表
	 */
	public List<RolePrivilege> findPagedAll(int currentPage, int pageSize, KeyValueWithOperator...arr) {
		try {
			return m_dao.findPagedAll("RolePrivilege", currentPage,pageSize, arr);
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
	public List<RolePrivilege> findPagedAll(int currentPage, int pageSize, List<KeyValueWithOperator> arr) {
		try {
			return m_dao.findPagedAll("RolePrivilege", currentPage, pageSize, arr);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 得到所有Role记录数
	 * @return Role记录数
	 */
	public int getTotalCount(KeyValueWithOperator...arr){
		return m_dao.getTotalCount("RolePrivilege", arr);		
	}
	
	/**
	 * 得到所有Role记录数
	 * @return Role记录数
	 */
	public int getTotalCount(List<KeyValueWithOperator> arr){
		return m_dao.getTotalCount("RolePrivilege",arr);		
	}
	
	/**
	 * 多条件组合查询
	 * @param instance 条件的组合
	 * @return 符合条件的记录
	 */
	public List findByExample(RolePrivilege instance) {
		return m_dao.findByExample(instance);
	}
	
	/**
	 * 
	 * @param arr
	 * @return
	 */
	public List<RolePrivilege> findByVarProperty(KeyValueWithOperator...arr){
		try{
			return m_dao.findByVarProperty("RolePrivilege", arr);
		}
		catch(Exception e){
			return null;
		}
	}
	public List<RolePrivilege> findByPropertyBySort(String orderby,boolean asc,KeyValueWithOperator...arr){
		return m_dao.findByPropertyBySort("RolePrivilege",orderby, asc, arr);
	}
	/**
	 * 插入一批RolePrivilege记录
	 * @param RolePrivilegeList RolePrivilege对象
	 * @return 插入成功，返回true；否则返回false
	 */
	public boolean saveByBatch(List<RolePrivilege> RolePrivilegeList){
		if(RolePrivilegeList == null || RolePrivilegeList.size() == 0){
			return false;
		}
		Transaction tran = m_dao.getSession().beginTransaction();
		try {
			for(RolePrivilege RolePrivilege : RolePrivilegeList){
				m_dao.save(RolePrivilege);
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
	 * 返回一个用户的所有权限信息，包括其角色的父角色的权限
	 * @param userid
	 * @return 返回所有权限(Url?参数)Map<Integer, UrlInfo>的集合(Integer存放权限的ID)，出错则返回null
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, UrlInfo> getPrivilegesByUserId(Integer userid){
		try{
			Map<Integer, UrlInfo> privilegeMap = new HashMap<Integer, UrlInfo>();	//已有的权限集合
			
			UserRoleDAO urDao = new UserRoleDAO();
			List<UserRole> urList = urDao.findByVarProperty("UserRole",
					new KeyValueWithOperator("sysUser.id", userid, "="),
					new KeyValueWithOperator("status", 1, "<>"),
					new KeyValueWithOperator("role.status", 1, "<>"));
			Set<Integer> roleSet = new HashSet<Integer>();	//已查找的角色集合
			KeyValueWithOperator k1 = new KeyValueWithOperator("privilege.status", 1, "<>");
			KeyValueWithOperator k2 = new KeyValueWithOperator("status", 1, "<>");
			KeyValueWithOperator k3 = new KeyValueWithOperator("role.status", 1, "<>");
			for(UserRole ur :urList){
				Role role = ur.getRole();
				boolean bFlag = true;				
				while(bFlag){	//递归查找角色及其父角色拥有的权限
					if(roleSet.contains(role.getId())){
						bFlag = false;
						continue;
					}
					roleSet.add(role.getId());
					List<RolePrivilege> retList = m_dao.findByVarProperty("RolePrivilege", 
							new KeyValueWithOperator("role.id", role.getId(), "="), k1, k2, k3);
					for(RolePrivilege rp : retList){
						String url = String.format("%s%s", rp.getPrivilege().getSysResources().getMappingUrl(), 
								(rp.getPrivilege().getParameters()==null || rp.getPrivilege().getParameters().length() == 0)?"":"?"+rp.getPrivilege().getParameters() );
						if(!privilegeMap.containsKey(rp.getPrivilege().getId())){
							privilegeMap.put(rp.getPrivilege().getId(), new UrlInfo(url));
						}
					}
					role = role.getRole();	//父角色
					if(role == null){
						bFlag = false;
					}
				}
			}
			
			return privilegeMap;
		}catch(Exception e){
			return null;
		}
	}
	/**导出用户权限
	 * 返回一个用户的所有权限信息，包括其角色的父角色的权限
	 * @param userid
	 * @return
	 */
	public List<JSONObject> ExportPrivilegesByUserId(Integer userid){
		try{
			Map<Integer, UrlInfo> privilegeMap = new HashMap<Integer, UrlInfo>();	//已有的权限集合
			List<JSONObject> result=new ArrayList<JSONObject>();
			
			UserRoleDAO urDao = new UserRoleDAO();
			List<UserRole> urList=new ArrayList<UserRole>();
			
			if(userid==null){				
				urList = urDao.findByVarProperty("UserRole",
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("role.status", 1, "<>"));
			}else{
				
				urList = urDao.findByVarProperty("UserRole",
					new KeyValueWithOperator("sysUser.id", userid, "="),
					new KeyValueWithOperator("status", 1, "<>"),
					new KeyValueWithOperator("role.status", 1, "<>"));
			}
			Set<Integer> roleSet = new HashSet<Integer>();	//已查找的角色集合
			KeyValueWithOperator k1 = new KeyValueWithOperator("privilege.status", 1, "<>");
			KeyValueWithOperator k2 = new KeyValueWithOperator("status", 1, "<>");
			KeyValueWithOperator k3 = new KeyValueWithOperator("role.status", 1, "<>");
			SysUser user=new SysUser();
			for(UserRole ur :urList){
				
				Role role = ur.getRole();
				user=ur.getSysUser();
				boolean bFlag = true;				
				while(bFlag){	//递归查找角色及其父角色拥有的权限
					if(roleSet.contains(role.getId())){
						bFlag = false;
						continue;
					}
					roleSet.add(role.getId());
					List<RolePrivilege> retList = m_dao.findByVarProperty("RolePrivilege", 
							new KeyValueWithOperator("role.id", role.getId(), "="), k1, k2, k3);
					for(RolePrivilege rp : retList){
						String url = String.format("%s%s", rp.getPrivilege().getSysResources().getMappingUrl(), 
								(rp.getPrivilege().getParameters()==null || rp.getPrivilege().getParameters().length() == 0)?"":"?"+rp.getPrivilege().getParameters() );
						if(!privilegeMap.containsKey(rp.getPrivilege().getId())){//不同角色的交叉权限只导出一个权限
							privilegeMap.put(rp.getPrivilege().getId(), new UrlInfo(url));
							JSONObject record=new JSONObject();
							record.put("UserName", user.getUserName());
							record.put("JobNum", user.getJobNum());
							record.put("Name", user.getName());
							record.put("RoleName", rp.getRole().getName());
							record.put("PrivilegeName", rp.getPrivilege().getName());
							result.add(record);
						}
					}
					role = role.getRole();	//父角色
					if(role == null){
						bFlag = false;
					}
				}
			}
			
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	public List<String> formatExcel(Object obj) throws JSONException{
		List<String> result = new ArrayList<String>();
		JSONObject jsonObj = (JSONObject)obj;
		result.add(jsonObj.has("UserName")?jsonObj.getString("UserName"):"");
		//result.add(jsonObj.has("JobNum")?jsonObj.getString("CommissionTypeName"):"");
		result.add(jsonObj.has("Name")?jsonObj.getString("Name"):"");
		result.add(jsonObj.has("RoleName")?jsonObj.getString("RoleName"):"");
		result.add(jsonObj.has("PrivilegeName")?jsonObj.getString("PrivilegeName"):"");	
		
		return result;
	}
	
	public List<String> formatTitle(){
		List<String> result = new ArrayList<String>();
		result.add("用户名");
		result.add("用户姓名");
		result.add("角色名");
		result.add("权限名");
		
		return result;
	}
	
}
