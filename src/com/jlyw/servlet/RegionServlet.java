package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.jlyw.hibernate.Region;
import com.jlyw.manager.RegionManager;
import com.jlyw.util.KeyValueWithOperator;

public class RegionServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(RegionServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		RegionManager regMgr = new RegionManager();
		switch(method)
		{
		case 1://新增或修改
			JSONObject retObj = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				Region reg;
				if(Id.equals("")){
					reg = initRegion(req, 0);
					boolean res = regMgr.save(reg);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
				}
				else{
					reg = initRegion(req, Integer.valueOf(Id));
					boolean res = regMgr.update(reg);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"修改成功！":"修改失败，请重新修改！");
				}
			}catch(Exception e){
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RegionServlet-->case 1", e);
				}else{
					log.error("error in RegionServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://查询地区(combobox)
			JSONArray jsonArray = new JSONArray();
			try {
				List<Region> regList = regMgr.findByVarProperty(new KeyValueWithOperator("status", 0, "="));
				if(regList != null){
					for(Region reg : regList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("name", reg.getName());
						jsonObj.put("id", reg.getId());
						jsonArray.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RegionServlet-->case 2", e);
				}else{
					log.error("error in RegionServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 3://查询地区(datagrid)
			JSONObject retObj3 = new JSONObject();
			try{
				int total = 0;
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				String Status = req.getParameter("Status");
				
				List<Region> result;
				
				if(Status!=null&&!Status.equals("")){
					String statusStr = URLDecoder.decode(Status,"utf-8");
					result  = regMgr.findPagedAll(page, rows, new KeyValueWithOperator("status", Integer.valueOf(statusStr), "="));
					total = regMgr.getTotalCount(new KeyValueWithOperator("status", Integer.valueOf(Status), "="));
				}
				else{
					result = regMgr.findPagedAll(page, rows);
					total = regMgr.getTotalCount();
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0){
					for(Region reg : result)
					{
						JSONObject option = new JSONObject();
						option.put("Id", reg.getId());
						option.put("Name", reg.getName());
						option.put("Code", reg.getCode());
						option.put("Brief", reg.getBrief());
						option.put("Status", reg.getStatus());
						
						options.put(option);
					}
				}
				retObj3.put("total", total);
				retObj3.put("rows", options);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RegionServlet-->case 3", e);
				}else{
					log.error("error in RegionServlet-->case 3", e);
				}
				try{
					retObj3.put("total", 0);
					retObj3.put("rows", new JSONArray());
				}catch(Exception e1){}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		case 4://注销
			JSONObject retObj4 = new JSONObject();
			try{
				String Id = req.getParameter("id");
				Region reg = regMgr.findById(Integer.valueOf(Id));
				reg.setStatus(1);
				boolean res = regMgr.update(reg);
				retObj4.put("IsOK", res);
				retObj4.put("msg", res?"注销成功！":"注销失败，请重新注销！");
			}catch(Exception e){
				try {
					retObj4.put("IsOK", false);
					retObj4.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RegionServlet-->case 4", e);
				}else{
					log.error("error in RegionServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		}
	}
	
	private Region initRegion(HttpServletRequest req, int id){
		Region reg;
		if(id == 0){
			reg = new Region();
		}
		else{
			reg = (new RegionManager()).findById(id);
		}
		
		String Name = req.getParameter("Name");
		String Code = req.getParameter("Code");
		String Brief = req.getParameter("Brief");
		String Status = req.getParameter("Status");
		
		reg.setName(Name);
		reg.setCode(Code);
		reg.setBrief(Brief);
		reg.setStatus(Integer.valueOf(Status));
		
		return reg;
	}

}
