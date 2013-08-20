package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Role;
import com.jlyw.hibernate.RolePrivilege;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.UserRole;
import com.jlyw.manager.RoleManager;
import com.jlyw.manager.RolePrivilegeManager;
import com.jlyw.manager.UserRoleManager;
import com.jlyw.util.KeyValueWithOperator;

public class RoleServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(RoleServlet.class);
	private static String ClassName = "RoleServlet";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		RoleManager rolemag = new RoleManager();
		switch(method)
		{
		case 1://新建角色
			JSONObject retObj=new JSONObject();
			try {
				Role role=initRole(req,0);
				boolean res = rolemag.save(role);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"创建成功！":"创建失败，请重新创建！");
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());		
			break;
		case 2://查询角色
			JSONObject res = new JSONObject();	
			try {
				List<Role> result;
				int total;	
				String RoleName = req.getParameter("queryname");
				List<KeyValueWithOperator> list = new ArrayList<KeyValueWithOperator>();
				if(RoleName != null&&RoleName != "")
				{
					String RoleNameStr = URLDecoder.decode(RoleName, "UTF-8");
					list.add(new KeyValueWithOperator("name","%" + RoleNameStr + "%","like"));
				}
					//list.add(new KeyValueWithOperator("status", 0,"="));
					
				int page = 0;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 0;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				result = rolemag.findPagedAllBySort(page, rows,"name",false, list);
				total = rolemag.getTotalCount(list);
				JSONArray options = new JSONArray();
				for (Role role1:result) {
					JSONObject option = new JSONObject();
					option.put("Id", role1.getId());
					option.put("Name", role1.getName());
					option.put("Description", role1.getDescription());
					option.put("Status", role1.getStatus());
					if(role1.getRole()!=null){
						option.put("Parent", role1.getRole().getName());
						option.put("ParentId", role1.getRole().getId());
					}
					options.put(option);
				}
				
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception ex) {
				log.error(String.format("error in %s", ClassName), ex);
				try{
					res.put("total", 0);
					res.put("rows", new JSONArray());
				}catch(Exception e){}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3://修改角色

			JSONObject retObj1=new JSONObject();
			try {
				int id1=Integer.parseInt(req.getParameter("roleId"));
				Role role1=initRole(req,id1);
				boolean res1 = rolemag.update(role1);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (JSONException e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("修改失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());	
			}
			break;
		case 4://删除角色		
			JSONObject ret=new JSONObject();
			try {
				int id = Integer.parseInt(req.getParameter("id"));				
				Role role2 = rolemag.findById(id);
				role2.setStatus(1);
				boolean res1 = rolemag.update(role2);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (JSONException e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					ret.put("IsOK", false);
					ret.put("msg", String.format("删除失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}	
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(ret.toString());
			break;
		case 5://为用户分配角色
			JSONObject retJSON5 = new JSONObject();
		
			try{			
				UserRoleManager userrolemag = new UserRoleManager(); 
				List<UserRole> UserRoleList = new ArrayList<UserRole>();
				SysUser user = new SysUser();
				user.setId(Integer.valueOf(req.getParameter("userId")));
				
				String roleStr = req.getParameter("roles");
				String[] roles = roleStr.split("\\|");
				
				int i;
				for(i = 0;i < roles.length; i++)
				{
					List<UserRole> check = userrolemag.findByVarProperty(new KeyValueWithOperator("sysUser.id", user.getId(), "="),new KeyValueWithOperator("role.id", Integer.valueOf(roles[i]), "="));
					if(check==null||check.size()==0)
					{
						Role Role = new Role();
						Role.setId(Integer.valueOf(roles[i]));
						UserRole userrole = new UserRole();
						userrole.setSysUser(user);
						userrole.setRole(Role);
						userrole.setStatus(0);
						UserRoleList.add(userrole);
					}
					else if(check.get(0).getStatus()==1)
					{
						UserRole uu = check.get(0);
						uu.setStatus(0);
						userrolemag.update(uu);
					}
				}
				if(UserRoleList.size()==0)
				{
					retJSON5.put("IsOK", true);
					retJSON5.put("msg", "角色授予成功！");
				}
				else{
					if(userrolemag.saveByBatch(UserRoleList))
					{
						retJSON5.put("IsOK", true);
						retJSON5.put("msg", "角色授予成功！");
					}
					else
					{
						retJSON5.put("IsOK", false);
						retJSON5.put("msg", "角色授予失败！");
					}
				}
			}catch(Exception e){
				log.debug(String.format("error in %s", ClassName), e);
				try{
					retJSON5.put("IsOK", false);
					retJSON5.put("msg", "授予权限失败，请重新授予！");
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6://查询某用户的角色
			JSONObject res1=new JSONObject();	
			int total=0;
			try {
				UserRoleManager user_role_mag = new UserRoleManager();
				String userId = req.getParameter("userId");
				if(userId==null)
				{
					throw new Exception("");
				}	
				total=user_role_mag.getTotalCount(new KeyValueWithOperator("sysUser.id",Integer.valueOf(URLDecoder.decode(userId, "UTF-8")),"="),new KeyValueWithOperator("status", 0, "="),new KeyValueWithOperator("role.status", 0, "="));
				List<UserRole> user_roles = user_role_mag.findByVarProperty(new KeyValueWithOperator("sysUser.id",Integer.valueOf(URLDecoder.decode(userId, "UTF-8")),"="),new KeyValueWithOperator("status", 0, "="),new KeyValueWithOperator("role.status", 0, "="));
				if(user_roles==null||user_roles.size()==0)
				{
					throw new Exception("");
				}
				JSONArray options1 = new JSONArray();
				for (UserRole temp:user_roles) {
					JSONObject option = new JSONObject();
					option.put("userId", temp.getSysUser().getId());
					option.put("Id", temp.getRole().getId());
					option.put("Name", temp.getRole().getName());
					option.put("Description", temp.getRole().getDescription());
					options1.put(option);
				}
				res1 = new JSONObject();
				res1.put("total", total);
				res1.put("rows", options1);
			} catch (Exception ex) {
				log.debug(String.format("error in %s", ClassName), ex);
				try{
					res1.put("total", 0);
					res1.put("rows", new JSONArray());
				}catch(Exception e){}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res1.toString());
			}
			break;
		case 7://取消某用户的角色
			JSONObject retJSON7 = new JSONObject();
			UserRoleManager userrolemag1= new UserRoleManager();	
			try{	
				String UserId = req.getParameter("userId"); 
				String RoleId = req.getParameter("roleId");
				
				SysUser user = new SysUser();
				user.setId(Integer.valueOf(UserId));
				Role role = new Role();
				role.setId(Integer.valueOf(RoleId));
				
				UserRole temp = new UserRole();
				temp.setSysUser(user);
				temp.setRole(role);
				UserRole userrole = (UserRole)userrolemag1.findByVarProperty(new KeyValueWithOperator("sysUser.id", Integer.valueOf(UserId), "="), new KeyValueWithOperator("role.id", Integer.valueOf(RoleId), "=")).get(0);
				userrole.setStatus(1);
				if(userrolemag1.update(userrole))
				{
					retJSON7.put("IsOK", true);
				}
				else{
					throw new Exception("");
				}
			}catch(Exception e){
				log.debug(String.format("error in %s", ClassName), e);
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("注销权限失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		case 8://下拉框中的角色名数据
			JSONArray jsonArray = new JSONArray();
			try {
				String roleNameStr = req.getParameter("QueryName");
				if(roleNameStr != null && roleNameStr.trim().length() > 0){
					String roleName =  new String(roleNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//解决URL传递中文乱码问题
					roleName = "%" + roleName + "%";
					
					List<Role> retList = rolemag.findByVarProperty(new KeyValueWithOperator("name", roleName, "like"),new KeyValueWithOperator("status", 0, "="));
					if(retList != null){
						for(Role role3 : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", role3.getName());
							jsonObj.put("id", role3.getId());
							jsonArray.put(jsonObj);	
						}
					}
				}
			} catch (JSONException e) {
				log.error(String.format("error in %s", ClassName), e);
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 9://查询某角色的用户
			JSONObject res2= new JSONObject();;
			
			try {
				String roleId = req.getParameter("roleId");
				if(roleId==null)
				{
					resp.setContentType("text/json");
					resp.setCharacterEncoding("UTF-8");
					resp.getWriter().write("{\"total\":0,\"rows\":[]}");
					return;
				}
				UserRoleManager role_user_mag = new UserRoleManager();
				
				List<UserRole> role_users = role_user_mag.findByVarProperty(new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId, "UTF-8")),"="),new KeyValueWithOperator("status", 0, "="));
				JSONArray options2 = new JSONArray();
				for (UserRole temp1:role_users) {
					JSONObject option = new JSONObject();
					option.put("userroleId", temp1.getId());
					option.put("roleId", temp1.getRole().getId());
					option.put("Id", temp1.getSysUser().getId());
					option.put("JobNum", temp1.getSysUser().getJobNum());  //用户工号
					option.put("Name", temp1.getSysUser().getName());//用户姓名
					option.put("userName", temp1.getSysUser().getUserName());//用户名
					option.put("JobTitle", temp1.getSysUser().getJobTitle());//行政职务
					options2.put(option);
				}
				res2.put("total", role_users.size());
				res2.put("rows", options2);
			} catch (Exception ex) {
				log.error(String.format("error in %s", ClassName), ex);
				try {
					res2.put("total", 0);
					res2.put("rows", new JSONArray());
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res2.toString());
			}
			break;
		case 10://为多个用户分配一个角色
			JSONObject retJSON10 = new JSONObject();
			
			try{
				UserRoleManager userrolemag2 = new UserRoleManager(); 
				List<UserRole> RoleUserList = new ArrayList<UserRole>();
				Role role3 = new Role();
				role3.setId(Integer.valueOf(req.getParameter("roleId")));				
				String userStr = req.getParameter("users");
	
				String[] users = userStr.split("\\|");
				
				int m;
				
				for(m = 0;m < users.length; m++)
				{
					SysUser sysuser = new SysUser();
					sysuser.setId(Integer.valueOf(users[m]));
					UserRole userrole2 = new UserRole();
					userrole2.setRole(role3);
					userrole2.setSysUser(sysuser);
					
					List<UserRole> check1 = userrolemag2.findByVarProperty(new KeyValueWithOperator("sysUser.id", sysuser.getId(), "="),new KeyValueWithOperator("role.id", role3.getId(), "="));
	
					if(check1==null||check1.size()==0)
					{
						userrole2.setStatus(0);
						RoleUserList.add(userrole2);
					}
					else 
					{
						UserRole uu = check1.get(0);
						if(uu.getStatus()==1)
						{
							uu.setStatus(0);
							userrolemag2.update(uu);
						}
					}
				}
				if(RoleUserList.size()==0)
				{
					retJSON10.put("IsOK", true);
					retJSON10.put("msg", "分配角色成功！");
				}
				else{
					if(userrolemag2.saveByBatch(RoleUserList))
					{
						retJSON10.put("IsOK", true);
						retJSON10.put("msg", "分配角色成功！");
					}
					else
					{
						retJSON10.put("IsOK", false);
						retJSON10.put("msg", "分配角色失败！");
					}
				}
			}catch(Exception e){
				log.error(String.format("error in %s", ClassName), e);
				try{
					retJSON10.put("IsOK", false);
					retJSON10.put("msg", "分配角色失败！");
				}catch(Exception ex){}
			}finally{
			
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
			}
			break;
		case 11://根据角色和权限名称查是否存在该授权
			JSONObject res11 = new JSONObject();	
			
			try {
				List<Role> result;
				int total1=0;
				String queryname = req.getParameter("queryname");
				String roleId = req.getParameter("roleId");
				if(roleId==null)
				{
					throw new Exception("角色为空");//角色为空
				}
				String querynameStr="";
				if(queryname != null&&queryname != "")
				{
					querynameStr = URLDecoder.decode(queryname, "UTF-8");		
				}
			
				RolePrivilegeManager role_privilege_mag = new RolePrivilegeManager();
				total1=role_privilege_mag.getTotalCount(new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId,"UTF-8")), "="),
						new KeyValueWithOperator("status", 0, "="),new KeyValueWithOperator("privilege.name","%" + querynameStr + "%", "like"));
				List<RolePrivilege> role_privileges = role_privilege_mag.findByPropertyBySort("privilege.name",false,
						new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId,"UTF-8")), "="),
						new KeyValueWithOperator("status", 0, "="),new KeyValueWithOperator("privilege.name","%" + querynameStr + "%", "like"));
				
				JSONArray options1 = new JSONArray();
				for (RolePrivilege temp : role_privileges) {
					JSONObject option = new JSONObject();
					option.put("RolePrivilegeId", temp.getId());
					option.put("Id", temp.getPrivilege().getId());
					option.put("Name", temp.getPrivilege().getName());
					option.put("Description", temp.getPrivilege().getDescription()==null?"":temp.getPrivilege().getDescription());
					options1.put(option);
				}
				res11.put("total", total1);
				res11.put("rows", options1);
			} catch (Exception ex) {
				if(ex.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RoleServlet-->case 11", ex);
				}else{
					log.error("error in RoleServlet-->case 11", ex);
				}
				try{
					res11.put("total", 0);
					res11.put("rows", new JSONArray());
				}catch(Exception e){}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res11.toString());
			}
			break;
		}
		
			
	}
	private Role initRole(HttpServletRequest req,int id)
	{
		String Name = req.getParameter("Name");
		String Description = req.getParameter("Description");
		String ParentId = req.getParameter("ParentId");
		//int pid=Integer.parseInt(ParentId);
		
		Role role;
		if(id==0)
		{
			role = new Role();
		}
		else
		{
			RoleManager roleMag = new RoleManager();
			role = roleMag.findById(id);
		}
		
		role.setName(Name);
		role.setDescription(Description);	
		role.setStatus(0);
		
		Role ParentRole = new Role();
		if(ParentId!=null && ParentId.trim().length()>0)
		{
			ParentRole = (new RoleManager()).findById(Integer.parseInt(ParentId));
			role.setRole(ParentRole);
		}
		else
			role.setRole(null);
		
		return role;
		
	}
	
}
