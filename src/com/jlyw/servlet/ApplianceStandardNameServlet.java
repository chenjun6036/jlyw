package com.jlyw.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

import com.jlyw.hibernate.AppStdNameProTeam;
import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.ProjectTeam;
import com.jlyw.manager.AppStdNameProTeamManager;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.ProjectTeamManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.UIDUtil;

public class ApplianceStandardNameServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(ApplianceStandardNameServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		ApplianceStandardNameManager appstannameMgr = new ApplianceStandardNameManager();
		switch(method)
		{
		case 0://模糊查询器具标准名称
			JSONArray jsonArray2 = new JSONArray();
			try {
				String appStanNameStr = req.getParameter("ApplianceStandardName");
				if(appStanNameStr != null && appStanNameStr.trim().length() > 0){
					String appStanName =  new String(appStanNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					//appStanName = LetterUtil.String2Alpha(appStanName);	//转换成拼音简码
					String[] queryName = appStanName.split(" \\s+");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					appStanName = "";
					for(int i = 0; i < queryName.length; i++){
						appStanName += queryName[i];
						if(i != queryName.length-1)
							appStanName += "%";
					}
					appStanName = "%" + appStanName + "%";
					String queryString = String.format("from ApplianceStandardName as model where (model.brief like ? or model.name like ?)and model.status = 0");
					List<ApplianceStandardName> retList = appstannameMgr.findPageAllByHQL(queryString, 1, 30, appStanName, appStanName);
					if(retList != null){
						for(ApplianceStandardName temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("standardname", temp.getName());
							jsonObj.put("name", temp.getApplianceSpecies().getName() + "--" + temp.getName());
							jsonObj.put("id", temp.getId());
							jsonArray2.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 0", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
		case 1://新建器具标准名称
			ApplianceStandardName appStandardName = initApplianceStandardName(req, 0);
			JSONObject retObj = new JSONObject();
			try {
				boolean res1 = appstannameMgr.save(appStandardName);
				retObj.put("IsOK", res1);
				retObj.put("msg", res1?"新建成功！":"新建失败，请重新新建！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 1", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询所有器具标准名称，返回tree的json格式
			String parentid = req.getParameter("parentid");
			String res;
			if(parentid==null)
			{
				res = getTreeJSON(0).toString();
				//System.out.println(parentid);
			}
			else
			{
				String parentidStr = new String(parentid.trim().getBytes("ISO-8859-1"), "GBK");
				res = getTreeJSON(Integer.valueOf(parentidStr)).toString();
				//System.out.println(parentid);
			}
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res);
			break;
		case 3://修改器具标准名称
			ApplianceStandardName appStandardName2 = initApplianceStandardName(req, Integer.valueOf(req.getParameter("id")));
			JSONObject retObj1 = new JSONObject();
			try {
				boolean res1 = appstannameMgr.update(appStandardName2);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 3", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 3", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj1.toString());
			break;
		case 4://注销器具标准名称
			ApplianceStandardName appStandardName3 = appstannameMgr.findById(Integer.valueOf(req.getParameter("id")));
			JSONObject retObj2 = new JSONObject();
			appStandardName3.setStatus(1);
			try {
				boolean res1 = appstannameMgr.update(appStandardName3);
				retObj2.put("IsOK", res1);
				retObj2.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 4", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 4", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj2.toString());
			break;
		case 5://查询标准名称下所关联的项目组
			int appStandardNameId = Integer.valueOf(req.getParameter("ApplianceStandardNameId"));
			JSONObject retObj5 = new JSONObject();
			try{
				List<AppStdNameProTeam> result = (new AppStdNameProTeamManager()).findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", appStandardNameId, "="));
				JSONArray options = new JSONArray();
				for(AppStdNameProTeam temp : result)
				{
					JSONObject option = new JSONObject();
					option.put("Id", temp.getId());
					option.put("StandardName", temp.getApplianceStandardName().getName());
					option.put("Name", temp.getProjectTeam().getName());
					option.put("ProjectName", temp.getProjectName());
					
					options.put(option);
				}
				retObj5.put("total", result.size());
				retObj5.put("rows", options);
			}catch(Exception e){
				
				try {
					retObj5.put("total", 0);
					retObj5.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 5", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj5.toString());
			}
			break;
		case 6://新建标准名称与一个项目组的关联
			AppStdNameProTeam StdNameProTeam = new AppStdNameProTeam();
			String StandardNameId = req.getParameter("StdName_ProTeam_StdNameId");
			String ProjectTeamId = req.getParameter("ProjectTeamId");
			String ProjectName = req.getParameter("ProjectName");
			
			StdNameProTeam.setApplianceStandardName((new ApplianceStandardNameManager()).findById(Integer.valueOf(StandardNameId)));
			ProjectTeam proTeam = (new ProjectTeamManager()).findById(Integer.valueOf(ProjectTeamId));
			if(proTeam!=null)
				StdNameProTeam.setProjectTeam(proTeam);
			StdNameProTeam.setProjectName(ProjectName);
			StdNameProTeam.setStatus(0);
			
			JSONObject retObj6 = new JSONObject();
			try {
				boolean res1 = (new AppStdNameProTeamManager()).save(StdNameProTeam);
				retObj6.put("IsOK", res1);
				retObj6.put("msg", res1?"新建成功！":"新建失败，请重新新建！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 6", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 6", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj6.toString());
			break;
		case 7://删除一个项目组
			int del_id = Integer.valueOf(req.getParameter("id"));
			JSONObject retObj3 = new JSONObject();
			try {
				boolean res1 = (new AppStdNameProTeamManager()).deleteById(del_id);
				retObj3.put("IsOK", res1);
				retObj3.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardNameServlet-->case 7", e);
				}else{
					log.error("error in ApplianceStandardNameServlet-->case 7", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj3.toString());
			break;
		case 8://导出所有标准名称
			JSONObject retObj8 = new JSONObject();
			try {
				List<ApplianceStandardName> result = getStandardName(0);
				File f = null;
				FileOutputStream os = null;
					
					List<String> title = appstannameMgr.formatTitle();
					
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
						List<String> excel = (List<String>) appstannameMgr.formatExcel(result.get(j));
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
				retObj8.put("IsOK", f.length()>0?true:false);
				retObj8.put("Path", f.length()>0?f.getAbsolutePath():"");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in ApplianceStandardName-->case 8", e);
				}else{
					log.error("error in ApplianceStandardName-->case 8", e);
				}
				try {
					retObj8.put("IsOK", false);
					retObj8.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj8.toString());
			}
			break;
		}

	}
	
	private ApplianceStandardName initApplianceStandardName(HttpServletRequest req, int id)
	{
		ApplianceStandardName appStandardName;
		if(id==0)
		{
			appStandardName = new ApplianceStandardName();
			appStandardName.setFilesetName(UIDUtil.get22BitUID());
		}
		else
		{
			appStandardName = (new ApplianceStandardNameManager()).findById(id);
		}
		String Name = req.getParameter("Name");
		String NameEn = req.getParameter("NameEn");
		int SpeciesId = Integer.valueOf(req.getParameter("SpeciesId"));
		String Brief = req.getParameter("Brief");
		String HoldMany = req.getParameter("HoldMany");
		String Status = req.getParameter("Status");		
		
		appStandardName.setName(Name);
		appStandardName.setNameEn(NameEn);
		appStandardName.setApplianceSpecies((new ApplianceSpeciesManager()).findById(SpeciesId));
		appStandardName.setBrief(Brief);
		appStandardName.setHoldMany(Integer.valueOf(HoldMany)==0?false:true);
		appStandardName.setStatus(Integer.valueOf(Status));
		
		return appStandardName;
	}

	private JSONArray getTreeJSON(int parentid)
	{
		JSONArray nodes = new JSONArray();
		ApplianceSpeciesManager appSpecMgr = new ApplianceSpeciesManager();
		ApplianceStandardNameManager appStandardNameMgr = new ApplianceStandardNameManager();
		JSONArray children;		
		try{
			List<ApplianceSpecies> specList;
			if(parentid!=0)
				specList = appSpecMgr.findByPropertyBySort("sequence", true, new KeyValueWithOperator("parentSpecies.id", parentid, "="));
			else
				specList = appSpecMgr.findByPropertyBySort("sequence", true, new KeyValueWithOperator("parentSpecies.id", null, "is null"));
			List<ApplianceStandardName> stanNameList = appStandardNameMgr.findByVarProperty(new KeyValueWithOperator("applianceSpecies.id", parentid, "="));
			for(ApplianceSpecies temp : specList)
			{
				JSONObject option = new JSONObject();
				option.put("id", temp.getId());
				option.put("treeId", "1-"+temp.getId());
				option.put("text", temp.getName());
				JSONObject attr = new JSONObject();
				if(temp.getParentSpecies()!=null)
					attr.put("ParentId", temp.getParentSpecies().getId());
				else
					attr.put("ParentId", 0);
				attr.put("type", 0);
				option.put("attributes", attr);
				//children = getTreeJSON(temp.getId());
				//option.put("children", children);
				option.put("state", "closed");
				nodes.put(option);
			}
			for(ApplianceStandardName stanName : stanNameList)
			{
				JSONObject option = new JSONObject();
				option.put("id", stanName.getId());
				option.put("treeId", "0-"+stanName.getId());
				option.put("text", stanName.getName()+(stanName.getStatus()==0?"":"(已注销)"));
				JSONObject attr = new JSONObject();
				attr.put("ParentId", stanName.getApplianceSpecies().getId());
				attr.put("type", 1);
				attr.put("Name", stanName.getName());
				attr.put("NameEn", stanName.getNameEn());
				attr.put("Brief", stanName.getBrief());
				attr.put("HoldMany", stanName.getHoldMany()?1:0);
				attr.put("Status", stanName.getStatus());
				attr.put("FilesetName", stanName.getFilesetName());
				option.put("attributes", attr);
				nodes.put(option);
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		//System.out.println(nodes.toString());
		return nodes;
	}
	
	private List<ApplianceStandardName> getStandardName(int parentid)
	{
		List<ApplianceStandardName> result = new ArrayList<ApplianceStandardName>();
		ApplianceSpeciesManager appSpecMgr = new ApplianceSpeciesManager();
		ApplianceStandardNameManager appStandardNameMgr = new ApplianceStandardNameManager();
		JSONArray children;		
		try{
			List<ApplianceSpecies> specList;
			if(parentid!=0)
				specList = appSpecMgr.findByVarProperty(new KeyValueWithOperator("parentSpecies.id", parentid, "="));
			else
				specList = appSpecMgr.findByVarProperty(new KeyValueWithOperator("parentSpecies.id", null, "is null"));
			List<ApplianceStandardName> stanNameList = appStandardNameMgr.findByVarProperty(new KeyValueWithOperator("applianceSpecies.id", parentid, "="));
			for(ApplianceSpecies temp : specList)
			{
				List<ApplianceStandardName> stanList = getStandardName(temp.getId());
				for(ApplianceStandardName stanTemp : stanList)
					result.add(stanTemp);
			}
			for(ApplianceStandardName stanName : stanNameList)
			{
				result.add(stanName);
			}
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}
	
}
