package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.UserQual;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.UserQualManager;
import com.jlyw.util.KeyValueWithOperator;

public class UserQualServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(UserQualServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		UserQualManager userqualmag = new UserQualManager();
		switch(method)
		{
		case 1://新建一个用户资质记录
			SysUser user = (new UserManager()).findById(Integer.valueOf(req.getParameter("EmpId")));
			UserQual new_userqual = initUserQual(req, 0);
			new_userqual.setSysUser(user);
			JSONObject retObj=new JSONObject();
			try {
				boolean res = userqualmag.save(new_userqual);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
			} catch (Exception e) {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write("{IsOK:false,msg:'添加失败，请重新添加！'}");	
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserQualServlet-->case 1", e);
				}else{
					log.error("error in UserQualServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询用户的资质
			String Emp = req.getParameter("EmpId");
			List<KeyValueWithOperator> list = new ArrayList<KeyValueWithOperator>();
			if(Emp != null&&Emp != "")
			{
				String EmpStr = URLDecoder.decode(Emp, "UTF-8");
				list.add(new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpStr), "="));
			}
			JSONObject res = new JSONObject();
			int page = 0;
			if (req.getParameter("page") != null)
				page = Integer.parseInt(req.getParameter("page").toString());
			int rows = 0;
			if (req.getParameter("rows") != null)
				rows = Integer.parseInt(req.getParameter("rows").toString());
			List<UserQual> result;
			int total;
			result = userqualmag.findPagedAll(page, rows, list);
			total = userqualmag.getTotalCount(list);
			JSONArray options = new JSONArray();
			try {
				for (UserQual userqual : result) {
					JSONObject option = new JSONObject();
					option.put("Id", userqual.getId());
					option.put("EmpName", userqual.getSysUser().getName());
					option.put("JobNum", userqual.getSysUser().getJobNum());
					option.put("Type", userqual.getType());
					option.put("QualNum", userqual.getQualNum());
					option.put("AuthItems", userqual.getAuthItems());
					option.put("AuthDate", userqual.getAuthDate());
					option.put("Expiration", userqual.getExpiration());
					option.put("AuthDept", userqual.getAuthDept());
					option.put("Remark", userqual.getRemark());
					
					options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserQualServlet-->case 2", e);
				}else{
					log.error("error in UserQualServlet-->case 2", e);
				}
			}
			resp.setContentType("text/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res.toString());
			break;
		case 3://修改一个用户资质
			int id = Integer.valueOf(req.getParameter("Id"));
			UserQual userqual = initUserQual(req, id);
			JSONObject retObj1=new JSONObject();
			try {
				boolean res1 = userqualmag.update(userqual);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserQualServlet-->case 3", e);
				}else{
					log.error("error in UserQualServlet-->case 3", e);
				}
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write("{'IsOK':false,'msg':'修改失败，请重新修改！'}");	
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj1.toString());
			break;
		case 4://删除或取销一条用户资质
			JSONObject ret=new JSONObject();
			try {
				boolean res1 = userqualmag.deleteById(Integer.valueOf(req.getParameter("id")));
				ret.put("IsOK", res1);
				ret.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserQualServlet-->case 4", e);
				}else{
					log.error("error in UserQualServlet-->case 4", e);
				}
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write("{'IsOK':false,'msg':'删除失败，请重新删除！'}");	
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret.toString());
			break;
		}
	}
	
	private UserQual initUserQual(HttpServletRequest req, int id)
	{
		UserQual userqual;
		if(id==0)
		{
			userqual = new UserQual();
		}
		else
		{
			UserQualManager mag = new UserQualManager();
			userqual = mag.findById(id);
		}
		String Type = req.getParameter("Type");
		String QualNum = req.getParameter("QualNum");
		String AuthItems = req.getParameter("AuthItems");
		String AuthDate = req.getParameter("AuthDate");
		String Expiration = req.getParameter("Expiration");
		String AuthDept = req.getParameter("AuthDept");
		String Remark = req.getParameter("Remark");
		
		userqual.setType(Type);
		userqual.setQualNum(QualNum);
		userqual.setAuthItems(AuthItems);
		userqual.setAuthDate(Date.valueOf(AuthDate));
		userqual.setExpiration(Date.valueOf(Expiration));
		userqual.setAuthDept(AuthDept);
		userqual.setRemark(Remark);
		
				
		return userqual;
	}

}
