package com.jlyw.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.StandardApplianceManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.UIDUtil;

public class ApplianceSpeciesServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(ApplianceSpeciesServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		ApplianceSpeciesManager appSpeMgr = new ApplianceSpeciesManager();
		switch(method)
		{
		case 1://新建分类
			ApplianceSpecies applianceSpecies = initApplianceSpecies(req, 0);
			JSONObject retObj=new JSONObject();
			try {
				boolean res = appSpeMgr.save(applianceSpecies);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"新建成功！":"新建失败，请重新新建！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceSpeciesServlet-->case 1", e);
				}else{
					log.error("error in ApplianceSpeciesServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询所有分类
			String res = null;
			try{
				String parentid = req.getParameter("parentid");
				
				if(parentid==null)
					res = getTreeJSON(0).toString();
				else
				{
					String parentidStr = new String(parentid.trim().getBytes("ISO-8859-1"), "GBK");
					res = getTreeJSON(Integer.valueOf(parentidStr)).toString();
				}
				/*List<ApplianceSpecies> list = appSpeMgr.findByVarProperty();
				int total = appSpeMgr.getTotalCount();
				JSONObject res;
				JSONArray options = new JSONArray();
				try {
					for (ApplianceSpecies app : list) {
						JSONObject option = new JSONObject();
						option.put("id", app.getId());
						option.put("Name", app.getName());
						option.put("Status", app.getStatus());
						if(app.getParentSpecies()!=null)
							option.put("_parentId", app.getParentSpecies().getId());
						option.put("Hierarchy", app.getHierarchy());
						options.put(option);
					}
					res = new JSONObject();
					res.put("total", total);
					res.put("rows", options);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}*/
			}catch(Exception e){
				res = new JSONArray().toString();
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceSpeciesServlet-->case 2", e);
				}else{
					log.error("error in ApplianceSpeciesServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/plain");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res);
			}
			break;
		case 3://修改分类
			ApplianceSpecies applianceSpecies1 = initApplianceSpecies(req, Integer.valueOf(req.getParameter("Id")));
			JSONObject retObj1=new JSONObject();
			try {
				if(applianceSpecies1==null)
				{
					retObj1.put("IsOK", false);
					retObj1.put("msg", "父分类编号无效");
				}
				else{
					boolean res1 = appSpeMgr.update(applianceSpecies1);
					retObj1.put("IsOK", res1);
					retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceSpeciesServlet-->case 3", e);
				}else{
					log.error("error in ApplianceSpeciesServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4://注销分类
			int id = Integer.valueOf(req.getParameter("id"));
			ApplianceSpecies appSpe = appSpeMgr.findById(id);
			appSpe.setStatus(1);
			JSONObject ret=new JSONObject();
			try {
				boolean res1 = appSpeMgr.update(appSpe);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceSpeciesServlet-->case 4", e);
				}else{
					log.error("error in ApplianceSpeciesServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 5://导出分类
			JSONObject retObj5 = new JSONObject();
			try {
				List<ApplianceSpecies> result = getSpecies(0);
				File f = null;
				FileOutputStream os = null;
					
					List<String> title = appSpeMgr.formatTitle();
					
					HSSFWorkbook wb = new HSSFWorkbook();
					HSSFSheet sheet = wb.createSheet();
					HSSFRow row;
					HSSFCell cell;
					row = sheet.createRow(0);
					for(int i = 0; i < title.size();i++)
					{
						cell = row.createCell(i);
						cell.setCellValue(title.get(i));
					}
					for(int j = 0;j<result.size();j++)
					{	
						row = sheet.createRow(j+1);
						//System.out.println(j);
						List<String> excel = (List<String>) appSpeMgr.formatExcel(result.get(j));
						for(int i = 0; i<excel.size(); i++)
						{
							cell = row.createCell(i);
							cell.setCellValue(excel.get(i).toString());
						}
					}
					
					f = File.createTempFile(UIDUtil.get22BitUID(), ".xls");
					os = new FileOutputStream(f);
					wb.write(os);
					os.flush();			
				retObj5.put("IsOK", f.length()>0?true:false);
				retObj5.put("Path", f.length()>0?f.getAbsolutePath():"");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceSpecesServlet-->case 5", e);
				}else{
					log.error("error in ApplianceSpecesServlet-->case 5", e);
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
	
	private ApplianceSpecies initApplianceSpecies(HttpServletRequest req, int id)
	{
		ApplianceSpecies applianceSpecies;
		if(id==0)
		{
			applianceSpecies = new ApplianceSpecies();
		}
		else
		{
			applianceSpecies = (new ApplianceSpeciesManager()).findById(id);
		}
		
		String ParentId = req.getParameter("ParentId");
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String Sequence = req.getParameter("Sequence");
		String Status = req.getParameter("Status");
		
		if(ParentId!=null&&!ParentId.equals(""))
		{
			ApplianceSpecies parent = (new ApplianceSpeciesManager()).findById(Integer.valueOf(ParentId));
			if(parent!=null)
				applianceSpecies.setParentSpecies(parent);
			else
				return null;
		}
		applianceSpecies.setName(Name);
		applianceSpecies.setBrief(Brief);
		applianceSpecies.setHierarchy(applianceSpecies.getParentSpecies()==null?1:applianceSpecies.getParentSpecies().getHierarchy()+1);
		if(Sequence!=null&&!Sequence.equals(""))
			applianceSpecies.setSequence(Integer.valueOf(Sequence));
		applianceSpecies.setStatus(Integer.valueOf(Status));
		
		return applianceSpecies;
	}

	private JSONArray getTreeJSON(int parentid)
	{
		JSONArray nodes = new JSONArray();
		ApplianceSpeciesManager appSpecMgr = new ApplianceSpeciesManager();
		try{
			List<ApplianceSpecies> specList;
			if(parentid!=0)
				specList = appSpecMgr.findByPropertyBySort("sequence", true, new KeyValueWithOperator("parentSpecies.id", parentid, "="));
			else
				specList = appSpecMgr.findByPropertyBySort("sequence", true, new KeyValueWithOperator("parentSpecies.id", null, "is null"));
			for(ApplianceSpecies temp : specList)
			{
				JSONObject option = new JSONObject();
				option.put("id", temp.getId());
				option.put("Name", temp.getName());
				option.put("Brief", temp.getBrief());
				option.put("Hierarchy", temp.getHierarchy());
				option.put("Status", temp.getStatus());
				option.put("Sequence", temp.getSequence()==null?0:temp.getSequence());
				if(temp.getParentSpecies()!=null)
					option.put("_parentId", temp.getParentSpecies().getId());
				int children = appSpecMgr.getTotalCount(new KeyValueWithOperator("parentSpecies.id", temp.getId(), "="));
				if(children!=0)
					option.put("state", "closed");
				nodes.put(option);
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return nodes;
	}
	
	private List<ApplianceSpecies> getSpecies(int parentid)
	{
		List<ApplianceSpecies> result = new ArrayList<ApplianceSpecies>();
		ApplianceSpeciesManager appSpecMgr = new ApplianceSpeciesManager();
		try{
			List<ApplianceSpecies> specList;
			if(parentid!=0)
				specList = appSpecMgr.findByPropertyBySort("sequence", true, new KeyValueWithOperator("parentSpecies.id", parentid, "="));
			else
				specList = appSpecMgr.findByPropertyBySort("sequence", true, new KeyValueWithOperator("parentSpecies.id", null, "is null"));
			for(ApplianceSpecies temp : specList)
			{
				result.add(temp);
				List<ApplianceSpecies> childrens = getSpecies(temp.getId());
				for(ApplianceSpecies children : childrens)
					result.add(children);
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}
	
}
