package com.jlyw.servlet;

import java.io.IOException;
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

import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.manager.ApplianceManufacturerManager;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;

public class ApplianceManufacturerServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(ApplianceManufacturerServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int method = Integer.valueOf(req.getParameter("method"));
		ApplianceManufacturerManager appManufacturerMgr = new ApplianceManufacturerManager();
		switch(method){
		case 1://新增常用名称
			JSONObject retObj = new JSONObject();
			try {
				ApplianceManufacturer appManufacturer = initApplianceManufacturer(req, 0);
				boolean res1 = appManufacturerMgr.save(appManufacturer);
				retObj.put("IsOK", res1);
				retObj.put("msg", res1?"新建成功！":"新建失败，请重新新建！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceManufacturerServlet-->case 1", e);
				}else{
					log.error("error in ApplianceManufacturerServlet-->case 1", e);
				}
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://根据标准名称查询常用名称
			JSONObject res = new JSONObject();
			try{
				String StandardNameId = req.getParameter("StandardNameId");

				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total = 0;
				List<ApplianceManufacturer> result = new ArrayList<ApplianceManufacturer>();
				result = appManufacturerMgr.findPagedAll(page, rows, new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="));
				total = appManufacturerMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="));
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()!=0)
				{
					for(ApplianceManufacturer temp : result){
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("StandardName", temp.getApplianceStandardName().getName());
						option.put("Name", temp.getManufacturer());
						option.put("Brief", temp.getBrief());
						option.put("Status", temp.getStatus());
						
						options.put(option);
					}
				}
				res.put("total", total);
				res.put("rows", options);
			}catch(Exception e){
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in ApplianceManufacturerServlet-->case 2",e);
				} else {
					log.error("error in ApplianceManufacturerServlet-->case 2", e);
				}
				try {
					res.put("total", 0);
					res.put("rows", new JSONArray());
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3://修改常用名称
			JSONObject retObj1 = new JSONObject();
			try {
				int id = Integer.valueOf(req.getParameter("Id"));
				ApplianceManufacturer appManufacturer = initApplianceManufacturer(req, id);
				boolean res1 = appManufacturerMgr.update(appManufacturer);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceManufacturerServlet-->case 3", e);
				}else{
					log.error("error in ApplianceManufacturerServlet-->case 3", e);
				}
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("修改失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4://注销常用名称
			ApplianceManufacturer delManufacturer = appManufacturerMgr.findById(Integer.valueOf(req.getParameter("id")));
			delManufacturer.setStatus(1);
			JSONObject retObj2 = new JSONObject();
			try {
				boolean res1 = appManufacturerMgr.update(delManufacturer);
				retObj2.put("IsOK", res1);
				retObj2.put("msg", res1 ? "注销成功！" : "注销失败，请重新注销！");
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in ApplianceManufacturerServlet-->case 4",e);
				} else {
					log.error("error in ApplianceManufacturerServlet-->case 4", e);
				}
				try {
					retObj2.put("IsOK", false);
					retObj2.put("msg", String.format("注销失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj2.toString());
			}
			break;
		case 5://导出所有生产厂商
			JSONObject retObj5 = new JSONObject();
			try {
				String queryStr = "from ApplianceManufacturer model where 1=1";
				String filePath = ExportUtil.ExportToExcel(queryStr, new ArrayList<Object>(), null, "formatExcel", "formatTitle", ApplianceManufacturerManager.class);				
				retObj5.put("IsOK", filePath.equals("")?false:true);
				retObj5.put("Path", filePath);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AppliancePopularNameServlet-->case 6", e);
				}else{
					log.error("error in AppliancePopularNameServlet-->case 6", e);
				}
				try {
					retObj5.put("IsOK", false);
					retObj5.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj5.toString());
			}
			break;
		}
	}
	
	private ApplianceManufacturer initApplianceManufacturer(HttpServletRequest req, int id) throws Exception
	{
		String StandardNameId = req.getParameter("StandardNameId");
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String Status = req.getParameter("Status");
		
		List<ApplianceManufacturer> manufactureList = (new ApplianceManufacturerManager()).findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="), new KeyValueWithOperator("manufacturer", Name, "="));
		if(manufactureList!=null&&manufactureList.size()>0)
			throw new Exception("该生产厂商已存在！");
		
		ApplianceManufacturer Manufacturer ;
		if(id==0)
			Manufacturer = new ApplianceManufacturer();
		else
			Manufacturer = (new ApplianceManufacturerManager()).findById(id);
		Manufacturer.setApplianceStandardName((new ApplianceStandardNameManager()).findById(Integer.valueOf(StandardNameId)));
		Manufacturer.setManufacturer(Name);
		Manufacturer.setBrief(Brief);
		Manufacturer.setStatus(Integer.valueOf(Status));
		return Manufacturer;
	}
	
}
