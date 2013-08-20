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

import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DepartmentManager;
import com.jlyw.manager.ProjectTeamManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

public class ProjectTeamServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(ProjectTeamServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		ProjectTeamManager proteammag = new ProjectTeamManager();
		switch(method)
		{
		case 1://新建项目组
			ProjectTeam projectTeam = initProjectTeam(req, 0);
			JSONObject retObj=new JSONObject();
			try {
				boolean res = proteammag.save(projectTeam);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 1", e);
				}else{
					log.error("error in ProjectTeamServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询项目组
			JSONObject res = new JSONObject();
			try{
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<ProjectTeam> result;
				int total;
				List<Object> keys = new ArrayList<Object>();
				String queryStr = "from ProjectTeam as model where 1=1";
				String proTeamName = req.getParameter("ProjectTeamName");
				String departmentName = req.getParameter("DepartmentName");
				if(proTeamName!=null&&!proTeamName.equals(""))
				{
					String proTeamNameStr = URLDecoder.decode(proTeamName, "UTF-8");
					queryStr = queryStr + " and model.name like ? or model.brief like ?";
					keys.add("%" + proTeamNameStr + "%");
					keys.add("%" + proTeamNameStr + "%");
				}
				if(departmentName!=null&&!departmentName.equals(""))
				{
					String departmentNameStr = URLDecoder.decode(departmentName, "UTF-8");
					queryStr = queryStr + " and model.department.name like ? or model.department.brief like ?";
					keys.add("%" + departmentNameStr + "%");
					keys.add("%" + departmentNameStr + "%");
				}
				
				result = proteammag.findPageAllByHQL(queryStr + " order by model.proTeamCode asc, model.id asc", page, rows, keys);
				total = proteammag.getTotalCountByHQL("select count(*)" + queryStr, keys);
				
				JSONArray options = new JSONArray();
			
				for (ProjectTeam proteam : result) {
					JSONObject option = new JSONObject();
					option.put("Id", proteam.getId());
					option.put("DeptId", proteam.getDepartment().getId());
					option.put("DeptCode", proteam.getDepartment().getDeptCode());
					option.put("ProTeamCode", proteam.getProTeamCode());
					option.put("Name", proteam.getName());
					option.put("Brief", proteam.getBrief());
					option.put("Status", proteam.getStatus());					
					
					options.put(option);
				}
				
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception ex) {
				if(ex.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 2", ex);
				}else{
					log.error("error in ProjectTeamServlet-->case 2", ex);
				}
			}
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res.toString());
			break;
		case 3://修改项目组信息
			int id = Integer.valueOf(req.getParameter("Id"));
			ProjectTeam proTeam = initProjectTeam(req, id);
			JSONObject retObj1=new JSONObject();
			try {
				boolean res1 = proteammag.update(proTeam);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (Exception e) {
				
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", "修改失败，请重新修改！");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 3", e);
				}else{
					log.error("error in ProjectTeamServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4://删除项目组
			JSONObject ret=new JSONObject();
			try {
				ProjectTeam pro = proteammag.findById(Integer.valueOf(req.getParameter("id")));
				pro.setStatus(1);
				boolean res1 = proteammag.update(pro);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				
				try {
					ret.put("IsOK", false);
					ret.put("msg", "删除失败，请重新删除！");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 4", e);
				}else{
					log.error("error in ProjectTeamServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 5://根据部门ID查询下属所属项目组
			int DeptId = Integer.valueOf(req.getParameter("DepartmentId"));
			JSONObject res1;
			res1 = new JSONObject();
			try{
				int page = 0;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 0;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());	
				List<ProjectTeam> result;
				int total;
				result = proteammag.findPagedAll(page, rows, new KeyValueWithOperator("department.id", DeptId, "="));
				total = proteammag.getTotalCount(new KeyValueWithOperator("department.id", DeptId, "="));
				
				JSONArray options = new JSONArray();
				for (ProjectTeam proteam : result) {
					JSONObject option = new JSONObject();
					option.put("Id", proteam.getId());
					option.put("Name", proteam.getName());
					option.put("Brief", proteam.getBrief());
					option.put("Status", proteam.getStatus());					
					
					options.put(option);
				}
				
				res1.put("total", total);
				res1.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 5", e);
				}else{
					log.error("error in ProjectTeamServlet-->case 5", e);
				}
			}
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res1.toString());
			break;
		case 6://查询项目组（仅返回ID和Name）
			List<ProjectTeam> result1;
			result1 = proteammag.findByPropertyBySort("proTeamCode", true,new KeyValueWithOperator("status", 0, "="));
			
			JSONArray options1 = new JSONArray();
			try {
				for (ProjectTeam proteam : result1) {
					JSONObject option = new JSONObject();
					option.put("Id", proteam.getId());
					option.put("Name", proteam.getName());
					
					options1.put(option);
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 6", e);
				}else{
					log.error("error in ProjectTeamServlet-->case 6", e);
				}
			}
			resp.setContentType("text/json;charset=gbk");
			resp.getWriter().write(options1.toString());
			break;
		case 7://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from ProjectTeam as model where 1=1";
				
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String proTeamName = params.getString("ProjectTeamName");
					String departmentName = params.getString("DepartmentName");
					if(proTeamName!=null&&!proTeamName.equals(""))
					{
						String proTeamNameStr = URLDecoder.decode(proTeamName, "UTF-8");
						queryStr = queryStr + " and model.name like ? or model.brief like ?";
						keys.add("%" + proTeamNameStr + "%");
						keys.add("%" + proTeamNameStr + "%");
					}
					if(departmentName!=null&&!departmentName.equals(""))
					{
						String departmentNameStr = URLDecoder.decode(departmentName, "UTF-8");
						queryStr = queryStr + " and model.department.name like ? or model.department.brief like ?";
						keys.add("%" + departmentNameStr + "%");
						keys.add("%" + departmentNameStr + "%");
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", ProjectTeamManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeam-->case 7", e);
				}else{
					log.error("error in ProjectTeam-->case 7", e);
				}
				try {
					retObj7.put("IsOK", false);
					retObj7.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj7.toString());
			}
			break;
		case 8://根据部门ID查询下属所属项目组(仅返回ID和Name)
			int DepartmentId = Integer.valueOf(req.getParameter("DepartmentId"));
			JSONArray res8 = new JSONArray();
			try{	
				List<ProjectTeam> result;
				result = proteammag.findByVarProperty(new KeyValueWithOperator("department.id", DepartmentId, "="));
				
				for (ProjectTeam proteam : result) {
					JSONObject option = new JSONObject();
					option.put("Id", proteam.getId());
					option.put("Name", proteam.getName());				
					
					res8.put(option);
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ProjectTeamServlet-->case 8", e);
				}else{
					log.error("error in ProjectTeamServlet-->case 8", e);
				}
			}
			resp.setContentType("text/json;charset=gbk");
			resp.getWriter().write(res8.toString());
			break;
		}
	}
	
	private ProjectTeam initProjectTeam(HttpServletRequest req, int id)
	{
		ProjectTeam projectTeam;
		if(id == 0)
		{
			projectTeam = new ProjectTeam();
		}
		else
		{
			projectTeam = (new ProjectTeamManager()).findById(id);
		}
		
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String DeptId = req.getParameter("DepartmentId");
		String Status = req.getParameter("Status");
		String ProjectTeamCode = req.getParameter("ProjectTeamCode");
		
		projectTeam.setName(Name);
		projectTeam.setBrief(Brief);
		projectTeam.setDepartment((new DepartmentManager()).findById(Integer.valueOf(DeptId)));
		projectTeam.setStatus(Integer.valueOf(Status));
		projectTeam.setProTeamCode(ProjectTeamCode);
		
		return projectTeam;
	}

}
