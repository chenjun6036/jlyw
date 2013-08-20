package com.jlyw.servlet;

import java.io.IOException;
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
import com.jlyw.hibernate.UserProfile;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.UserProfileManager;
import com.jlyw.util.KeyValueWithOperator;

public class UserProfileServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(UserProfileServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		UserProfileManager userpromag = new UserProfileManager();
		switch(method)
		{
		case 1://新增用户档案记录
			String userId = req.getParameter("EmpId");
			SysUser user = (new UserManager()).findById(Integer.valueOf(userId));
			int Type = Integer.valueOf(req.getParameter("ProfileType"));
			UserProfile userprofile = new UserProfile();
			String F1 = req.getParameter("F1");
			String F2 = req.getParameter("F2");
			String F3 = req.getParameter("F3");
			String F4 = req.getParameter("F4");
			userprofile.setSysUser(user);
			if(F1!=null)
				userprofile.setF1(F1);
			if(F2!=null)
				userprofile.setF2(F2);
			if(F3!=null)
				userprofile.setF3(F3);
			if(F4!=null)
				userprofile.setF4(F4);
			userprofile.setType(Type);
			JSONObject retObj=new JSONObject();
			try {
				boolean res = userpromag.save(userprofile);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserProfileServlet-->case 1", e);
				}else{
					log.error("error in UserProfileServlet-->case 1", e);
				}
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write("{IsOK:false,msg:'添加失败，请重新添加！'}");	
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询用户档案记录
			String EmpId = req.getParameter("EmpId");
			String type = req.getParameter("Type");
			JSONObject res;
			int page = 0;
			if (req.getParameter("page") != null)
				page = Integer.parseInt(req.getParameter("page").toString());
			int rows = 0;
			if (req.getParameter("rows") != null)
				rows = Integer.parseInt(req.getParameter("rows").toString());
			if(EmpId==null)
				EmpId="0";
			List<UserProfile> result;
			int total;
			if(type==null)
			{
				result = userpromag.findPagedAll(page, rows, new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpId), "="));
				total = userpromag.getTotalCount(new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpId), "="));
			}
			else
			{
				result = userpromag.findPagedAll(page, rows, new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpId), "="), new KeyValueWithOperator("type", Integer.valueOf(type), "="));
				total = userpromag.getTotalCount(new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpId), "="), new KeyValueWithOperator("type", Integer.valueOf(type), "="));
			}
			JSONArray options = new JSONArray();
			res = new JSONObject();
			try {
				for (UserProfile userpro : result) {
					JSONObject option = new JSONObject();
					option.put("Id", userpro.getId());
					option.put("Type", userpro.getType());
					option.put("F1", userpro.getF1());
					option.put("F2", userpro.getF2());
					option.put("F3", userpro.getF3());
					option.put("F4", userpro.getF4());
					
					options.put(option);
				}
				
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserProfileServlet-->case 2", e);
				}else{
					log.error("error in UserProfileServlet-->case 2", e);
				}
			}
			resp.setContentType("text/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res.toString());
			break;
		case 3://修改用户档案记录
			String id = req.getParameter("Id");
			UserProfile UserProfile = userpromag.findById(Integer.valueOf(id));
			String newF1 = req.getParameter("F1");
			String newF2 = req.getParameter("F2");
			String newF3 = req.getParameter("F3");
			String newF4 = req.getParameter("F4");
			int newType = Integer.valueOf(req.getParameter("ProfileType"));
			UserProfile.setF1(newF1);
			UserProfile.setF2(newF2);
			UserProfile.setF3(newF3);
			UserProfile.setF4(newF4);
			UserProfile.setType(newType);
			JSONObject retObj1=new JSONObject();
			try {
				boolean res1 = userpromag.update(UserProfile);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserProfileServlet-->case 3", e);
				}else{
					log.error("error in UserProfileServlet-->case 3", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj1.toString());
			break;
		case 4://删除一条用户档案记录
			String del_id = req.getParameter("id");
			JSONObject retObj2=new JSONObject();
			try {
				boolean res1 = userpromag.deleteById(Integer.valueOf(del_id));
				retObj2.put("IsOK", res1);
				retObj2.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in UserProfileServlet-->case 4", e);
				}else{
					log.error("error in UserProfileServlet-->case 4", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj2.toString());
			break;
		}
	}
	
}
