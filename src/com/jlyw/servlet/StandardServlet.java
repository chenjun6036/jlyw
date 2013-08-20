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
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.StandardManager;
import com.jlyw.manager.StdStdAppManager;
import com.jlyw.manager.StdTgtAppManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.UIDUtil;

public class StandardServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(StandardServlet.class);
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.parseInt(req.getParameter("method"));
		StandardManager stanmag = new StandardManager();
		switch(method)
		{
		case 1:					//添加标准
			JSONObject retObj=new JSONObject();
			try {
				String user = req.getParameter("Handler");
				if(user != null && !user.equals("") ){
					List<SysUser> check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", user, "="));
					if(check==null||check.size()==0)
						throw new Exception("员工输入有误，请确认！");
				}
				Standard standard = initStandard(req,0);
				boolean res = stanmag.save(standard);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
				retObj.put("file_upload_filesetname", standard.getCopy());
				/*retObj.put("copy_filesetname", standard.getCopy());
				retObj.put("scopy_filesetname", standard.getScopy());*/
			} catch (Exception e) {
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 1", e);
				}else{
					log.error("error in StandardServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2:					//查询标准
			JSONObject res = new JSONObject();
			try {
				String queryStandardName = req.getParameter("queryName");
				String queryIssuedBy = req.getParameter("queryIssuedBy");
				String queryStatus = req.getParameter("queryStatus");
				String queryStart = req.getParameter("queryStart");
				String queryEnd = req.getParameter("queryEnd");
				String queryType = req.getParameter("queryType");
				String queryHandler = req.getParameter("queryHandler");
				String queryDept = req.getParameter("queryDept");
				String OverValid = req.getParameter("OverValid");
				String Warn = req.getParameter("Warn");
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<Standard> result;
				int total;
				
				String queryStr = "from Standard as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();

				if(queryStandardName!=null&&!queryStandardName.equals(""))
				{
					String stanNameStr = URLDecoder.decode(queryStandardName, "UTF-8");
					queryStr = queryStr + " and (model.name like ? or model.brief like ?) ";
					keys.add("%" + stanNameStr + "%");
					keys.add("%" + stanNameStr + "%");
				}
				if(queryIssuedBy!=null&&!queryIssuedBy.equals(""))
				{
					String stanIssuedBy = URLDecoder.decode(queryIssuedBy, "UTF-8");
					queryStr = queryStr + " and (model.issuedBy like ?) ";
					keys.add("%" + stanIssuedBy + "%");
				}
				if(queryStatus != null&&!queryStatus.equals(""))
				{
					String stanStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
					keys.add(LetterUtil.isNumeric(stanStatusStr)?Integer.valueOf(stanStatusStr):0);
					queryStr = queryStr + " and model.status = ? ";
				}
				if(queryStart != null&&!queryStart.equals("")&&queryEnd != null&&!queryEnd.equals(""))
				{
					Date Start = Date.valueOf(URLDecoder.decode(queryStart, "utf-8"));
					Date End = Date.valueOf(URLDecoder.decode(queryEnd, "utf-8"));
					keys.add(Start);
					keys.add(End);
					queryStr  = queryStr + " and (model.validDate >= ? and model.validDate <= ?)";
				}
				if(queryType != null&&!queryType.equals(""))
				{
					String stanTypeStr = URLDecoder.decode(queryType, "UTF-8");
					keys.add("%" + stanTypeStr + "%");
					queryStr = queryStr + " and model.projectType like ?";
				}
				if(queryHandler != null&&!queryHandler.equals(""))
				{
					String stanHandlerStr = URLDecoder.decode(queryHandler, "UTF-8");
					keys.add(stanHandlerStr);
					queryStr = queryStr + " and model.sysUser.name = ?";
				}
				if(queryDept != null&&!queryDept.equals(""))
				{
					String stanDeptStr = URLDecoder.decode(queryDept, "UTF-8");
					keys.add("%" + stanDeptStr + "%");
					keys.add("%" + stanDeptStr + "%");
					queryStr = queryStr + " and model.sysUser.projectTeamId in (select model1.id from ProjectTeam as model1 where (model1.department.name like ? or model1.department.brief like ?))";
				}
				if(OverValid != null&&!OverValid.equals(""))
				{
					queryStr = queryStr + " and model.validDate <= ? and model.status = ?";
					keys.add(new Date(System.currentTimeMillis()));
					keys.add(Integer.valueOf(0));
				}
				if(Warn != null&&!Warn.equals(""))
				{
					queryStr = queryStr + " and model.validDate >= ? and DATEADD(dd,0-model.warnSlot,model.validDate) <= ?";
					keys.add(new Date(System.currentTimeMillis()));
					keys.add(new Date(System.currentTimeMillis()));
				}
				
				result = stanmag.findPageAllByHQL(queryStr + " order by model.status asc, model.slocaleCode asc", page, rows, keys);
				total = stanmag.getTotalCountByHQL("select count(model) " + queryStr, keys);
				
				JSONArray options = new JSONArray();
				for (Standard stan : result) {
					JSONObject option = new JSONObject();
					option.put("Id", stan.getId());
					option.put("Name", stan.getName());
					option.put("NameEn", stan.getNameEn());
					option.put("Brief", stan.getBrief());
					option.put("CertificateCode", stan.getCertificateCode());
					option.put("StandardCode", stan.getStandardCode());
					option.put("ProjectCode", stan.getProjectCode());
					option.put("Status", stan.getStatus());
					option.put("CreatedBy", stan.getCreatedBy());
					option.put("IssuedBy", stan.getIssuedBy());
					option.put("IssueDate", stan.getIssueDate());
					option.put("ValidDate", stan.getValidDate());
					option.put("Range", stan.getRange());
					option.put("Uncertain", stan.getUncertain());
					option.put("SIssuedBy", stan.getSissuedBy());
					option.put("SCertificateCode", stan.getScertificateCode());
					option.put("SIssueDate", stan.getSissueDate());
					option.put("SValidDate", stan.getSvalidDate());
					option.put("SRegion", stan.getSregion());
					option.put("SAuthorizationCode", stan.getSauthorizationCode());
					option.put("SLocaleCode", stan.getSlocaleCode());
					option.put("WarnSlot", stan.getWarnSlot());
					option.put("Remark", stan.getRemark());
					option.put("Handler", stan.getSysUser().getName());
					option.put("ProjectType", stan.getProjectType());
					
					option.put("filesetname", stan.getCopy());
					
					/*if(stan.getCopy()!=null&&stan.getCopy()!="")
					{
						HashMap<String, Object> h = new HashMap<String, Object>();
						h.put(MongoDBUtil.KEYNAME_FileSetName, stan.getCopy());
						JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
						option.put("Copy", file==null?"":file);
					}
					else
					{
						option.put("Copy", "");
					}
					
					if(stan.getScopy()!=null&&stan.getScopy()!="")
					{
						HashMap<String, Object> h = new HashMap<String, Object>();
						h.put(MongoDBUtil.KEYNAME_FileSetName, stan.getScopy());
						JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
						option.put("SCopy", file==null?"":file);
					}
					else
					{
						option.put("SCopy", "");
					}*/
					options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				try {
					res.put("total", 0);
					res.put("rows", res);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 2", e);
				}else{
					log.error("error in StandardServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3:					//修改标准
			JSONObject retObj1=new JSONObject();
			try {
				String user = req.getParameter("Handler");
				if(user != null && !user.equals("") ){
					List<SysUser> check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", user, "="));
					if(check==null||check.size()==0)
						throw new Exception("员工输入有误，请确认！");
				}
				Standard standard1 = initStandard(req,Integer.valueOf(req.getParameter("Id")));
				boolean res1 = stanmag.update(standard1);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
				retObj1.put("file_upload_filesetname", standard1.getCopy());
				/*retObj1.put("copy_filesetname", standard1.getCopy());
				retObj1.put("scopy_filesetname", standard1.getScopy());*/
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 3", e);
				}else{
					log.error("error in StandardServlet-->case 3", e);
				}
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("修改失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4:					//删除标准
			int id = Integer.parseInt(req.getParameter("id"));
			Standard stan1 = stanmag.findById(id);
			stan1.setStatus(1);
			JSONObject ret=new JSONObject();
			try {
				boolean res1 = stanmag.update(stan1);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 4", e);
				}else{
					log.error("error in StandardServlet-->case 4", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret.toString());
			break;
		case 5:	
			JSONArray jsonArray = new JSONArray();
			try {
				List<Standard> List = stanmag.findByVarProperty(new KeyValueWithOperator("status", 0, "="));
				if(List != null){
					for(Standard stan : List){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("name", stan.getName());
						jsonObj.put("name_num", stan.getName()+"-"+stan.getSlocaleCode());
						jsonObj.put("id", stan.getId());
						jsonArray.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 5", e);
				}else{
					log.error("error in StandardServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 6://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from Standard as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String queryStandardName = params.has("queryName")?params.getString("queryName"):"";
					String queryIssuedBy = params.has("queryIssuedBy")?params.getString("queryIssuedBy"):"";
					String queryStatus = params.has("queryStatus")?params.getString("queryStatus"):"";
					String queryStart = params.has("queryStart")?params.getString("queryStart"):"";
					String queryEnd = params.has("queryEnd")?params.getString("queryEnd"):"";
					String queryType = params.has("queryType")?params.getString("queryType"):"";
					String queryHandler = params.has("queryHandler")?params.getString("queryHandler"):"";
					String queryDept = params.has("queryDept")?params.getString("queryDept"):"";
					String OverValid = params.has("OverValid")?params.getString("OverValid"):"";
					String Warn = params.has("Warn")?params.getString("Warn"):"";
					
					if(queryStandardName!=null&&!queryStandardName.equals(""))
					{
						String stanNameStr = URLDecoder.decode(queryStandardName, "UTF-8");
						queryStr = queryStr + " and (model.name like ? or model.brief like ?) ";
						keys.add("%" + stanNameStr + "%");
						keys.add("%" + stanNameStr + "%");
					}
					if(queryIssuedBy!=null&&!queryIssuedBy.equals(""))
					{
						String stanIssuedBy = URLDecoder.decode(queryIssuedBy, "UTF-8");
						queryStr = queryStr + " and (model.issuedBy like ?) ";
						keys.add("%" + stanIssuedBy + "%");
					}
					if(queryStatus != null&&!queryStatus.equals(""))
					{
						String stanStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
						keys.add(LetterUtil.isNumeric(stanStatusStr)?Integer.valueOf(stanStatusStr):0);
						queryStr = queryStr + " and model.status = ? ";
					}
					if(queryStart != null&&!queryStart.equals("")&&queryEnd != null&&!queryEnd.equals(""))
					{
						Date Start = Date.valueOf(URLDecoder.decode(queryStart, "utf-8"));
						Date End = Date.valueOf(URLDecoder.decode(queryEnd, "utf-8"));
						keys.add(Start);
						keys.add(End);
						queryStr  = queryStr + " and (model.validDate >= ? and model.validDate <= ?)";
					}
					if(queryType != null&&!queryType.equals(""))
					{
						String stanTypeStr = URLDecoder.decode(queryType, "UTF-8");
						keys.add("%" + stanTypeStr + "%");
						queryStr = queryStr + " and model.projectType like ?";
					}
					if(queryHandler != null&&!queryHandler.equals(""))
					{
						String stanHandlerStr = URLDecoder.decode(queryHandler, "UTF-8");
						keys.add(stanHandlerStr);
						queryStr = queryStr + " and model.sysUser.name = ?";
					}
					if(queryDept != null&&!queryDept.equals(""))
					{
						String stanDeptStr = URLDecoder.decode(queryDept, "UTF-8");
						keys.add("%" + stanDeptStr + "%");
						keys.add("%" + stanDeptStr + "%");
						queryStr = queryStr + " and model.sysUser.projectTeamId in (select model1.id from ProjectTeam as model1 where (model1.department.name like ? or model1.department.brief like ?))";
					}
					if(OverValid != null&&!OverValid.equals(""))
					{
						queryStr = queryStr + " and model.validDate <= ? and model.status = ?";
						keys.add(new Date(System.currentTimeMillis()));
						keys.add(Integer.valueOf(0));
					}
					if(Warn != null&&!Warn.equals(""))
					{
						queryStr = queryStr + " and model.validDate >= ? and DATEADD(dd,0-model.warnSlot,model.validDate) <= ?";
						keys.add(new Date(System.currentTimeMillis()));
						keys.add(new Date(System.currentTimeMillis()));
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", StandardManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 6", e);
				}else{
					log.error("error in StandardServlet-->case 6", e);
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
		case 7://标准combobox模糊查询，返回前30个
			String stdNameStr = req.getParameter("StandardName");	//
			JSONArray jsonArray10 = new JSONArray();
			try {
				if(stdNameStr != null && stdNameStr.trim().length() > 0){
					String stdName =  new String(stdNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					stdName = LetterUtil.String2Alpha(stdName);	//返回字母简码
					String[] queryName = stdName.split(" \\s+");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					stdName = "";
					for(int i = 0; i < queryName.length; i++){
						stdName += queryName[i];
						if(i != queryName.length-1)
							stdName += "%";
					}
					stdName = "%" + stdName + "%";
					String queryString = String.format("from Standard as model where model.brief like ? and model.status = ?");
					List<Standard> retList  = stanmag.findPageAllByHQL(queryString, 1, 30, "%" + stdName + "%", Integer.valueOf(0));
					if(retList != null){
						for(Standard temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", temp.getName());
							jsonObj.put("id", temp.getId());
							jsonArray10.put(jsonObj);		
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardServlet-->case 7", e);
				}else{
					log.error("error in StandardServlet-->case 7", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray10.toString());
			}
			break;
		case 8:
			JSONObject retObj8 = new JSONObject();
			try{
				String old_id = req.getParameter("Id");
				Standard newStd = initStandard(req, 0);
				if(stanmag.save(newStd))
				{
					int newId = newStd.getId();
					List<KeyValue> key = new ArrayList<KeyValue>();
					key.add(new KeyValue("standard.id", newId));
					
					if((new StdStdAppManager()).update(key, new KeyValue("standard.id", Integer.valueOf(old_id)))&&
								(new StdTgtAppManager()).update(key, new KeyValue("standard.id", Integer.valueOf(old_id)))){
						retObj8.put("IsOK", true);
						retObj8.put("msg", "替代成功！");
					}
					else{
						retObj8.put("IsOK", false);
						retObj8.put("msg", "替代失败，请重新替代！");
					}
					Standard oldStd = stanmag.findById(Integer.valueOf(old_id));
					oldStd.setStatus(1);
					stanmag.update(oldStd);
				}
				else
				{
					throw new Exception("新增新计量标准失败");
				}
			}catch(Exception e) {
				try {
					retObj8.put("IsOK", false);
					retObj8.put("msg", String.format("替代失败，请重新尝试替代！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardServlet-->case 8", e);
				}else{
					log.error("error in StandardServlet-->case 8", e);
				} 
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj8.toString());
			}
			break;
		case 9://标准combobox模糊查询，返回前30个，在“直接编辑证书”之前的选择标准器具，计量规程等中用到
			String standNameStr = req.getParameter("StandardName");	//
			JSONArray jsonArray9 = new JSONArray();
			try {
				if(standNameStr != null && standNameStr.trim().length() > 0){
					String stdName =  new String(standNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					stdName = LetterUtil.String2Alpha(stdName);	//返回字母简码
					String[] queryName = stdName.split(" \\s+");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					stdName = "";
					for(int i = 0; i < queryName.length; i++){
						stdName += queryName[i];
						if(i != queryName.length-1)
							stdName += "%";
					}
					stdName = "%" + stdName + "%";
					String queryString = String.format("from Standard as model where model.brief like ? and model.status = ?");
					List<Standard> retList  = stanmag.findPageAllByHQL(queryString, 1, 30, "%" + stdName + "%", Integer.valueOf(0));
					if(retList != null){
						for(Standard temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name_num", temp.getName()+temp.getSlocaleCode());
							jsonObj.put("id", temp.getId());
							
							jsonObj.put("name", temp.getName());
							jsonObj.put("certificatecode", temp.getCertificateCode());
							jsonObj.put("standardcode", temp.getStandardCode());
							jsonObj.put("projectcode", temp.getProjectCode());
							jsonObj.put("issuedby", temp.getIssuedBy());
							jsonObj.put("issuedate", temp.getIssueDate());
							jsonObj.put("validdate", temp.getValidDate());
							jsonObj.put("range", temp.getRange());
							jsonObj.put("uncertain", temp.getUncertain());
							jsonObj.put("sissuedby", temp.getSissuedBy());
							jsonObj.put("scertificatecode", temp.getScertificateCode());
							jsonObj.put("sissuedate", temp.getSissueDate());
							jsonObj.put("svaliddate", temp.getSvalidDate());
							jsonObj.put("region", temp.getSregion());
							jsonObj.put("sauthorizationcode", temp.getSauthorizationCode());
							jsonObj.put("slocalecode", temp.getSlocaleCode());
							jsonObj.put("warnslot", temp.getWarnSlot());
							jsonObj.put("handler", temp.getSysUser().getName());
							jsonObj.put("projecttype", temp.getProjectType());
							jsonArray9.put(jsonObj);		
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardServlet-->case 9", e);
				}else{
					log.error("error in StandardServlet-->case 9", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray9.toString());
			}
			break;
		}
	}

	private Standard initStandard(HttpServletRequest req,int id)
	{
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String NameEn = req.getParameter("NameEn");
		String CertificateCode = req.getParameter("CertificateCode");
		String StandardCode = req.getParameter("StandardCode");
		String ProjectCode = req.getParameter("ProjectCode");
		String Status = req.getParameter("Status");
		String CreatedBy = req.getParameter("CreatedBy");
		String IssuedBy = req.getParameter("IssuedBy");
		String IssueDate = req.getParameter("IssueDate");
		String ValidDate = req.getParameter("ValidDate");
		String Range = req.getParameter("Range");
		String Uncertain = req.getParameter("Uncertain");
		String SIssuedBy = req.getParameter("SIssuedBy");
		String SCertificateCode = req.getParameter("SCertificateCode");
		String SIssueDate = req.getParameter("SIssueDate");
		String SValidDate = req.getParameter("SValidDate");
		String SRegion = req.getParameter("SRegion");
		String SAuthorizationCode = req.getParameter("SAuthorizationCode");
		String SLocaleCode = req.getParameter("SLocaleCode");
		String Remark = req.getParameter("Remark");
		String WarnSlot = req.getParameter("WarnSlot");
		String Handler = req.getParameter("Handler");
		String ProjectType = req.getParameter("Type");
		
		Standard stan;
		if(id==0)
		{
			stan = new Standard();
			stan.setCopy(UIDUtil.get22BitUID());
			stan.setScopy(UIDUtil.get22BitUID());
		}
		else
		{
			StandardManager stanmag = new StandardManager();
			stan = stanmag.findById(id);
		}
		
		stan.setName(Name);
		stan.setNameEn(NameEn);
		stan.setBrief(Brief);
		stan.setCertificateCode(CertificateCode);
		stan.setStandardCode(StandardCode);
		stan.setProjectCode(ProjectCode);
		stan.setStatus(Integer.parseInt(Status));
		stan.setCreatedBy(CreatedBy);
		stan.setIssuedBy(IssuedBy);
		stan.setIssueDate(Date.valueOf(IssueDate));
		stan.setValidDate(Date.valueOf(ValidDate));
		stan.setRange(Range);
		stan.setUncertain(Uncertain);
		stan.setSissuedBy(SIssuedBy);
		stan.setScertificateCode(SCertificateCode);
		stan.setSissueDate(Date.valueOf(SIssueDate));
		stan.setSvalidDate(Date.valueOf(SValidDate));
		stan.setSregion(SRegion);
		stan.setSauthorizationCode(SAuthorizationCode);
		stan.setSlocaleCode(SLocaleCode);
		if(Remark!=null)
			stan.setRemark(Remark);
		if(WarnSlot!=null&&!WarnSlot.equals(""))
			stan.setWarnSlot(Integer.valueOf(WarnSlot));
		else
			stan.setWarnSlot(null);
		stan.setSysUser((new UserManager()).findByVarProperty(new KeyValueWithOperator("name", Handler, "=")).get(0));	
		stan.setProjectType(ProjectType);
		
		return stan;
		
	}
	
}
