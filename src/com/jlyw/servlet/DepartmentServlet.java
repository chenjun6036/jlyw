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

import com.jlyw.hibernate.Department;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DepartmentManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

public class DepartmentServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(DepartmentServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		DepartmentManager deptmag = new DepartmentManager();
		switch(method)
		{
		case 1://新建部门
			Department department = initDepartment(req, 0);
			JSONObject retObj=new JSONObject();
			try {
				boolean res = deptmag.save(department);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DepartmentServlet-->case 1", e);
				}else{
					log.error("error in DepartmentServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询部门
			JSONObject res = new JSONObject();
			int page = Integer.parseInt(req.getParameter("page"));
			int rows = Integer.parseInt(req.getParameter("rows"));
			try{
				String proTeamName = req.getParameter("DepartmentName");
				List<Department> result;
				int total;
				if(proTeamName!=null&&!proTeamName.equals(""))
				{
					String proTeamNameStr = URLDecoder.decode(proTeamName, "UTF-8");
					String queryStr = "from Department as model where model.name like ? or model.brief like ?";
					result = deptmag.findPageAllByHQL(queryStr + " order by model.deptCode asc", page, rows, "%" + proTeamNameStr + "%","%" + proTeamNameStr + "%");
					total = deptmag.getTotalCountByHQL("select count(*) "+queryStr, "%" + proTeamNameStr + "%","%" + proTeamNameStr + "%");
				}
				else
				{
					String queryStr = "from Department as model order by model.deptCode asc";
					result = deptmag.findPageAllByHQL(queryStr, page, rows);
					total = deptmag.getTotalCount();
				}
				JSONArray options = new JSONArray();
				for (Department temp : result) {
					JSONObject option = new JSONObject();
					option.put("Id", temp.getId());
					option.put("Name", temp.getName());
					option.put("Brief", temp.getBrief());
					option.put("DeptCode", temp.getDeptCode());
					option.put("Status", temp.getStatus());					
					
					options.put(option);
				}
				
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DepartmentServlet-->case 2", e);
				}else{
					log.error("error in DepartmentServlet-->case 2", e);
				}
			}
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res.toString());
			break;
		case 3://修改部门信息
			int id = Integer.valueOf(req.getParameter("Id"));
			Department dept = initDepartment(req, id);
			JSONObject retObj1=new JSONObject();
			try {
				boolean res1 = deptmag.update(dept);
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
					log.debug("exception in DepartmentServlet-->case 3", e);
				}else{
					log.error("error in DepartmentServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4://注销部门
			JSONObject ret=new JSONObject();
			
			try {
				Department Department = deptmag.findById(Integer.valueOf(req.getParameter("id")));
				Department.setStatus(1);
				boolean res1 = deptmag.update(Department);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				
				try {
					ret.put("IsOK", false);
					ret.put("msg", "注销失败，请重新注销！");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DepartmentServlet-->case 4", e);
				}else{
					log.error("error in DepartmentServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 6://查询部门（仅返回ID和Name）
			List<Department> result1;
			result1 = deptmag.findPageAllByHQL("from Department as model order by model.deptCode asc", 1, 30);
			
			JSONArray options1 = new JSONArray();
			try {
				for (Department temp : result1) {
					JSONObject option = new JSONObject();
					option.put("Id", temp.getId());
					option.put("Name", temp.getName());	
					option.put("DeptCode", temp.getDeptCode());
					
					options1.put(option);
				}
			} catch (Exception ex) {
				if(ex.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DepartmentServlet-->case 6", ex);
				}else{
					log.error("error in DepartmentServlet-->case 6", ex);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(options1.toString());
			break;
		case 7://导出
			JSONObject res7 = new JSONObject();
			String paramsStr = req.getParameter("paramsStr");
			try{
				String queryStr="";
				List<Object> keys = new ArrayList<Object>();
				JSONObject params = new JSONObject(paramsStr);			
				if(params.length()!=0){
					String proTeamName = params.getString("DepartmentName");
				    keys = new ArrayList<Object>();
					List<Department> result;
					
					if(proTeamName!=null&&!proTeamName.equals(""))
					{
						String proTeamNameStr = URLDecoder.decode(proTeamName, "UTF-8");
					    queryStr = "from Department as model where model.name like ? or model.brief like ?" + " order by model.deptCode asc";
						keys.add("%" + proTeamNameStr + "%");
						keys.add("%" + proTeamNameStr + "%");
					}
					
				}
				else{
					queryStr = "from Department as model order by model.deptCode asc";
				}
				System.out.println(queryStr);
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", DepartmentManager.class);				
				res7.put("IsOK", filePath.equals("")?false:true);
				res7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DepartmentServlet-->case 7", e);
				}else{
					log.error("error in DepartmentServlet-->case 7", e);
				}
				try {
					res7.put("IsOK", false);
					res7.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(res7.toString());
			}
			break;
		}
	}
	private Department initDepartment(HttpServletRequest req, int id)
	{
		Department department;
		if(id == 0)
		{
			department = new Department();
		}
		else
		{
			department = (new DepartmentManager()).findById(id);
		}
		
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String Status = req.getParameter("Status");
		String DeptCode = req.getParameter("DeptCode");
		
		department.setName(Name);
		department.setBrief(Brief);
		department.setStatus(Integer.valueOf(Status));
		department.setDeptCode(DeptCode);
		
		return department;
	}
	
}
