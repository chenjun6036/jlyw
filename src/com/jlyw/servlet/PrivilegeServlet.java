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

import com.jlyw.hibernate.Privilege;
import com.jlyw.hibernate.Role;
import com.jlyw.hibernate.RolePrivilege;
import com.jlyw.hibernate.SysResources;
import com.jlyw.manager.PrivilegeManager;
import com.jlyw.manager.RolePrivilegeManager;
import com.jlyw.manager.SysResourcesManager;
import com.jlyw.util.KeyValueWithOperator;

public class PrivilegeServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(PrivilegeServlet.class);
	private static String ClassName = "PrivilegeServlet";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		PrivilegeManager privilegemag = new PrivilegeManager();
		switch (method) {
		case 1:// 新建权限
			JSONObject retObj = new JSONObject();	
			try {
				Privilege privilege = initPrivilege(req, 0);
				boolean res = privilegemag.save(privilege);
				retObj.put("IsOK", res);
				retObj.put("msg", res ? "创建成功！" : "创建失败，请重新创建！");
			} catch (JSONException e) {
				log.error(String.format("error in %s", ClassName), e);
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
			
		case 2:// 查询权限
			JSONObject res = new JSONObject();
			try {
				String PrivilegeName = req.getParameter("queryname");
				
				List<KeyValueWithOperator> list = new ArrayList<KeyValueWithOperator>();
				if (PrivilegeName != null && PrivilegeName != "") {
					String PrivilegeNameStr = URLDecoder.decode(PrivilegeName,
							"UTF-8");

					list.add(new KeyValueWithOperator("name", "%"
							+ PrivilegeNameStr + "%", "like"));
				}
				list.add(new KeyValueWithOperator("status", 0, "="));
				
				int page = 0;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 0;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
	
				List<Privilege> result;
				int total;
				result = privilegemag.findPagedAllBySort(page, rows,"name",false, list);
				total = privilegemag.getTotalCount(list);

				JSONArray options = new JSONArray();
				for (Privilege privilege1 : result) {
					JSONObject option = new JSONObject();
					option.put("Id", privilege1.getId());
					option.put("Name", privilege1.getName());
					option.put("Description", privilege1.getDescription()==null?"":privilege1.getDescription());
					option.put("MappingUrl", privilege1.getSysResources().getMappingUrl());
					option.put("ResourcesId", privilege1.getSysResources().getId());
					option.put("ResourcesName", privilege1.getSysResources().getName());
					option.put("Parameters", privilege1.getParameters()==null?"":privilege1.getParameters());
					option.put("Status", privilege1.getStatus());

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
		case 3:// 修改权限
			JSONObject retObj1 = new JSONObject();
			try {
				int id1=Integer.parseInt(req.getParameter("Id"));
				Privilege privilege1=initPrivilege(req, id1);
				boolean res1 = privilegemag.update(privilege1);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1 ? "修改成功！" : "修改失败，请重新修改！");
			} catch (Exception e) {
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
		case 4:// 删除权限	
			JSONObject ret = new JSONObject();
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				Privilege privilege2 = privilegemag.findById(id);
				privilege2.setStatus(1);
				if(privilegemag.update(privilege2))
					ret.put("IsOK", true);
				else
					throw new Exception("");
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					ret.put("IsOK", false);
					ret.put("msg", String.format("删除失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(ret.toString());	
			}
			break;
		case 5:// 为角色分配多个权限
			JSONObject retJSON5 = new JSONObject();
			
			try{
				List<RolePrivilege> RolePrivilegeList = new ArrayList<RolePrivilege>();
				RolePrivilegeManager roleprivilegemag = new RolePrivilegeManager();
				Role role = new Role();
				role.setId(Integer.valueOf(req.getParameter("roleId")));
	
				String privilegeStr = req.getParameter("privileges");
				String[] privileges = privilegeStr.split("\\|");
				
				int i;
				for (i = 0; i < privileges.length; i++) {
					RolePrivilege roleprivilege = new RolePrivilege();
					
					Privilege privilege5 = new Privilege();
					privilege5.setId(Integer.parseInt(privileges[i]));	
					roleprivilege.setRole(role);
					roleprivilege.setPrivilege(privilege5);
					List<RolePrivilege> check = roleprivilegemag.findByVarProperty(new KeyValueWithOperator("role.id", role.getId(), "="),new KeyValueWithOperator("privilege.id", Integer.valueOf(privileges[i]), "="));
					//RolePrivilege uu = check.get(0);
					if (check == null || check.size() == 0) {
						roleprivilege.setStatus(0);
						RolePrivilegeList.add(roleprivilege);
					} else {
						RolePrivilege uu = check.get(0);
						if (uu.getStatus() == 1){
							uu.setStatus(0);
							roleprivilegemag.update(uu);
						}
					}
				}
				if (RolePrivilegeList.size() == 0) {
					retJSON5.put("IsOK", true);
					retJSON5.put("msg", "权限授予成功！");
				}else
				{
					if (roleprivilegemag.saveByBatch(RolePrivilegeList)) {
						retJSON5.put("IsOK", true);
						retJSON5.put("msg", "权限授予成功！");
					} else {
						throw new Exception();
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
		case 6:// 查询某角色的权限
			JSONObject res1 = new JSONObject();
			try {
				int total=0;
				String roleId = req.getParameter("roleId");
				if (roleId == null || roleId.length() == 0) {
					throw new Exception("");
				}
				RolePrivilegeManager role_privilege_mag = new RolePrivilegeManager();
				total=role_privilege_mag.getTotalCount(new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId,"UTF-8")), "="),
						new KeyValueWithOperator("status", 0, "="));
				List<RolePrivilege> role_privileges = role_privilege_mag.findByPropertyBySort("privilege.name",false,
						new KeyValueWithOperator("role.id",Integer.valueOf(URLDecoder.decode(roleId,"UTF-8")), "="),
						new KeyValueWithOperator("status", 0, "="));
				
				JSONArray options1 = new JSONArray();
				for (RolePrivilege temp : role_privileges) {
					JSONObject option = new JSONObject();
					option.put("RolePrivilegeId", temp.getId());
					option.put("Id", temp.getPrivilege().getId());
					option.put("Name", temp.getPrivilege().getName());
					option.put("Description", temp.getPrivilege().getDescription()==null?"":temp.getPrivilege().getDescription());
					options1.put(option);
				}
				res1.put("total",total);
				res1.put("rows", options1);
			} catch (Exception ex) {
				log.debug(String.format("error in %s", ClassName), ex);
				try{
					res1.put("total", 0);
					res1.put("rows", new JSONArray());
				}catch(Exception e){}
			}finally{
				
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(res1.toString());
			}
			break;
		case 7:// 取消某角色的权限
			JSONObject retJSON7 = new JSONObject();
			try{
				String RolePrivilegeId = req.getParameter("RolePrivilegeId");
				RolePrivilegeManager rpMgr = new RolePrivilegeManager();
				RolePrivilege rolep = (RolePrivilege)rpMgr.findByVarProperty(new KeyValueWithOperator("id", Integer.valueOf(RolePrivilegeId), "=")).get(0);
				rolep.setStatus(1);
				if(rpMgr.update(rolep)){
					retJSON7.put("IsOK", true);
				}else{
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
		case 8://下拉框中的权限名
			JSONArray jsonArray = new JSONArray();
			try {
				String PrivilegeNameStr = req.getParameter("QueryName");
				if(PrivilegeNameStr != null && PrivilegeNameStr.trim().length() > 0){
					String privilegeName =  new String(PrivilegeNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//解决URL传递中文乱码问题
					
					privilegeName = "%" + privilegeName + "%";
					
					List<Privilege> retList = privilegemag.findByVarProperty(new KeyValueWithOperator("name", privilegeName, "like"),new KeyValueWithOperator("status", 0, "="));
					if(retList != null){
						for(Privilege privilege3 : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", privilege3.getName());
							jsonObj.put("id", privilege3.getId());
							jsonArray.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 9:   //查询权限对应的角色
			JSONObject retJSON9 = new JSONObject();
			try {
				int total=0;
				String privilegeId = req.getParameter("privilegeId");
				if (privilegeId == null) {
					throw new Exception("");
				}
				RolePrivilegeManager role_privilege_mag1 = new RolePrivilegeManager();
				total=role_privilege_mag1.getTotalCount(new KeyValueWithOperator("privilege.id",Integer.valueOf(URLDecoder.decode(privilegeId,"UTF-8")), "="),
					new KeyValueWithOperator("status", 0, "="));
				List<RolePrivilege> role_privileges1 = role_privilege_mag1.findByVarProperty(
					new KeyValueWithOperator("privilege.id",Integer.valueOf(URLDecoder.decode(privilegeId,"UTF-8")), "="),
					new KeyValueWithOperator("status", 0, "="));
				
				JSONArray options2 = new JSONArray();
				for (RolePrivilege temp1 : role_privileges1) {
					JSONObject option = new JSONObject();
					option.put("RolePrivilegeId", temp1.getId());
					option.put("privilegeId", temp1.getPrivilege().getId());
					option.put("Id", temp1.getRole().getId());
					option.put("Name", temp1.getRole().getName());
					option.put("Description", temp1.getRole().getDescription());
					options2.put(option);
				}
				
				retJSON9.put("total", total);
				retJSON9.put("rows", options2);
			} catch (Exception ex) {
				log.debug(String.format("error in %s", ClassName), ex);
				try{
					retJSON9.put("total", 0);
					retJSON9.put("rows", new JSONArray());
				}catch(Exception e){}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			break;
		}
	}

	private Privilege initPrivilege(HttpServletRequest req, int id) {
		String Name = req.getParameter("Name");
		String Description = req.getParameter("Description");
		int ResourcesId = Integer.parseInt(req.getParameter("ResourcesId"));
		String Parameters = req.getParameter("Parameters");
	    
		Privilege privilege;
		if (id == 0) {
			privilege = new Privilege();
		} else {
			PrivilegeManager privilegeMag = new PrivilegeManager();
			privilege = privilegeMag.findById(id);
		}

		privilege.setName(Name);
		privilege.setDescription(Description);
		privilege.setParameters(Parameters);
		privilege.setStatus(0);
		
		SysResources resource=(new SysResourcesManager()).findById(ResourcesId);
        privilege.setSysResources(resource);
		return privilege;

	}

}