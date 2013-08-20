package com.jlyw.servlet.appliance;

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

import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.StandardApplianceManager;
import com.jlyw.manager.StdStdAppManager;
import com.jlyw.manager.TgtAppStdAppManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

public class StandardApplianceServlet extends HttpServlet{
	private static Log log = LogFactory.getLog(StandardApplianceServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		StandardApplianceManager stanappMgr = new StandardApplianceManager();
		switch(method)
		{
		case 1://添加标准器具
			JSONObject retObj=new JSONObject();
			try {
				String user = req.getParameter("KeeperId");
				if(user != null && !user.equals("") ){
					List<SysUser> check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", user, "="));
					if(check==null||check.size()==0)
						throw new Exception("员工输入有误，请确认！");
				}
				String ReleaseDateStr = req.getParameter("ReleaseDate");
				if(ReleaseDateStr!=null&&!ReleaseDateStr.equals(""))
				{
					Date ReleaseDate = Date.valueOf(ReleaseDateStr);
					if(ReleaseDate.compareTo(new Date(System.currentTimeMillis()))>0)
						throw new Exception("出厂日期晚于当前日期，请确认！");
				}
				
				StandardAppliance stanapp = initStandardAppliance(req, 0);
				boolean res = stanappMgr.save(stanapp);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
			} catch (Exception e) {
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 1", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 1", e);
				} 
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://查询标准器具
			JSONObject res = new JSONObject();
			try {
				String queryName = req.getParameter("queryname");
				String queryModel = req.getParameter("queryModel");
				String queryRealseiaNumber = req.getParameter("queryRealseiaNumber");
				String queryKeeper = req.getParameter("queryKeeper");
				String queryLocaleCode = req.getParameter("queryLocaleCode");
				String queryPermanentCode = req.getParameter("queryPermanentCode");
				String queryInspectMonth = req.getParameter("queryInspectMonth");
				String queryDept = req.getParameter("queryDept");
				String queryStatus = req.getParameter("queryStatus");
				String Warn = req.getParameter("Warn");
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<StandardAppliance> result;
				int total;
	
				String queryStr = "from StandardAppliance as model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				
				if(queryName!=null&&!queryName.equals(""))
				{
					String stanappNameStr = URLDecoder.decode(queryName, "UTF-8");
					queryStr = queryStr + " and (model.name like ? or model.brief like ?)";
					keys.add("%" + stanappNameStr + "%");
					keys.add("%" + stanappNameStr + "%");
				}
				if(queryModel!=null&&!queryModel.equals(""))
				{
					String stanappModelStr = URLDecoder.decode(queryModel, "UTF-8");
					queryStr = queryStr + " and (model.model like ? or model.range like ? or model.uncertain like ?)";
					keys.add("%" + stanappModelStr + "%");
					keys.add("%" + stanappModelStr + "%");
					keys.add("%" + stanappModelStr + "%");
				}
				if(queryRealseiaNumber!=null&&!queryRealseiaNumber.equals(""))
				{
					String stanappReleaseNumberStr = URLDecoder.decode(queryRealseiaNumber, "UTF-8");
					queryStr = queryStr + " and model.releaseNumber like ? ";
					keys.add("%" + stanappReleaseNumberStr + "%");
				}
				if(queryKeeper!=null&&!queryKeeper.equals(""))
				{
					String stanappKeeperStr = URLDecoder.decode(queryKeeper, "UTF-8");
					queryStr = queryStr + " and (model.sysUser.name like ? or model.sysUser.brief like ?)";
					keys.add("%" + stanappKeeperStr + "%");
					keys.add("%" + stanappKeeperStr + "%");
				}
				if(queryLocaleCode!=null&&!queryLocaleCode.equals(""))
				{
					String stanappLocaleCodeStr = URLDecoder.decode(queryLocaleCode, "UTF-8");
					queryStr = queryStr + " and model.localeCode like ?";
					keys.add("%" + stanappLocaleCodeStr + "%");
				}
				if(queryPermanentCode!=null&&!queryPermanentCode.equals(""))
				{
					String stanappPermanentCodeStr = URLDecoder.decode(queryPermanentCode, "UTF-8");
					queryStr = queryStr + " and model.permanentCode like ?";
					keys.add("%" + stanappPermanentCodeStr + "%");
				}
				if(queryInspectMonth!=null&&!queryInspectMonth.equals(""))
				{
					String stanappInspectMonthStr = URLDecoder.decode(queryInspectMonth, "UTF-8");
					queryStr = queryStr + " and model.inspectMonth = ?";
					keys.add(Integer.valueOf(stanappInspectMonthStr));
				}
				if(queryDept != null&&!queryDept.equals(""))
				{
					String stanDeptStr = URLDecoder.decode(queryDept, "UTF-8");
					keys.add("%" + stanDeptStr + "%");
					keys.add("%" + stanDeptStr + "%");
					queryStr = queryStr + " and model.sysUser.projectTeamId in (select model1.id from ProjectTeam as model1 where (model1.department.name like ? or model1.department.brief like ?))";
				}
				if(queryStatus!=null&&!queryStatus.equals(""))
				{
					String stanappStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
					queryStr = queryStr + " and model.status = ?";
					keys.add(LetterUtil.isNumeric(stanappStatusStr)?Integer.valueOf(stanappStatusStr):0);
				}
				if(Warn != null&&!Warn.equals(""))
				{
					queryStr = queryStr + " and model.validDate >= ? and DATEADD(dd,0-model.warnSlot,model.validDate) <= ?";
					keys.add(new Date(System.currentTimeMillis()));
					keys.add(new Date(System.currentTimeMillis()));
				}
				
				result = stanappMgr.findPageAllByHQL(queryStr + " order by model.status asc, model.localeCode asc, model.id asc", page, rows, keys);
				total = stanappMgr.getTotalCountByHQL("select count(*) " + queryStr, keys);
				
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0){
					for (StandardAppliance stanApp : result) {
						JSONObject option = new JSONObject();
						option.put("Id", stanApp.getId());
						option.put("Name", stanApp.getName());
						option.put("Brief", stanApp.getBrief());
						option.put("Model", stanApp.getModel());
						option.put("Range", stanApp.getRange());
						option.put("Uncertain", stanApp.getUncertain());
						option.put("Manufacturer", stanApp.getManufacturer());
						option.put("SeriaNumber", stanApp.getSeriaNumber());
						option.put("ReleaseDate", stanApp.getReleaseDate());
						option.put("TestCycle", stanApp.getTestCycle());
						option.put("ReleaseNumber", stanApp.getReleaseNumber());
						option.put("Num", stanApp.getNum());
						option.put("Price", stanApp.getPrice());
						option.put("Status", stanApp.getStatus());
						option.put("KeeperId", stanApp.getSysUser().getName());
						option.put("LocaleCode", stanApp.getLocaleCode());
						option.put("PermanentCode", stanApp.getPermanentCode());
						option.put("ProjectCode", stanApp.getProjectCode());
						option.put("Budget", stanApp.getBudget());
						option.put("ValidDate", stanApp.getValidDate());
						option.put("WarnSlot", stanApp.getWarnSlot());
						option.put("Remark", stanApp.getRemark());
						option.put("InspectMonth", stanApp.getInspectMonth());
						
						options.put(option);
					}
				}

				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 2", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 2", e);
				} 
				try{
					res.put("total", 0);
					res.put("rows", new JSONArray());
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3://修改标准器具信息
			JSONObject retObj1=new JSONObject();
			try {
				String user = req.getParameter("KeeperId");
				if(user != null && !user.equals("") ){
					List<SysUser> check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", user, "="));
					if(check==null||check.size()==0)
						throw new Exception("员工输入有误，请确认！");
				}
				String ReleaseDateStr = req.getParameter("ReleaseDate");
				if(ReleaseDateStr!=null&&!ReleaseDateStr.equals(""))
				{
					Date ReleaseDate = Date.valueOf(ReleaseDateStr);
					if(ReleaseDate.compareTo(new Date(System.currentTimeMillis()))>0)
						throw new Exception("出厂日期晚于当前日期，请确认！");
				}
				
				int id = Integer.valueOf(req.getParameter("Id"));
				StandardAppliance stanapp1 = initStandardAppliance(req, id);
				boolean res1 = stanappMgr.update(stanapp1);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 3", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 3", e);
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
		case 4://注销标准器具
			JSONObject ret=new JSONObject();
			int del_id = Integer.valueOf(req.getParameter("id"));
			StandardAppliance del_stdApp = stanappMgr.findById(del_id);
			del_stdApp.setStatus(1);
			try {
				boolean res1 = stanappMgr.update(del_stdApp);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 4", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 4", e);
				} 
				try{
					ret.put("IsOK", false);
					ret.put("msg", "注销失败，请重新注销！");
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 5:	
			JSONArray jsonArray = new JSONArray();
			try {
				List<StandardAppliance> List = stanappMgr.findByVarProperty(new KeyValueWithOperator("status", 0, "="));
				if(List != null){
					for(StandardAppliance stdapp : List){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("name", stdapp.getName());
						jsonObj.put("name_num", stdapp.getName()+stdapp.getLocaleCode());
						jsonObj.put("id", stdapp.getId());
						jsonArray.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 5", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 5", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 6://标准器具combobox模糊查询，返回前30个(仅返回name)
			JSONArray jsonArray6 = new JSONArray();
			try {
				String stdAppNameStr = req.getParameter("QueryName");	
				if(stdAppNameStr != null && stdAppNameStr.trim().length() > 0){
					String stdAppName =  new String(stdAppNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					String queryStr = "select distinct model.name from StandardAppliance model where model.brief like ? or model.name like ? or model.localeCode like ?  or model.model like ?";
					List<Object> retList = stanappMgr.findPageAllByHQL(queryStr, 1, 30, "%" + stdAppName + "%", "%" + stdAppName + "%", "%" + stdAppName + "%", "%" + stdAppName + "%");
					if(retList != null){
						for(Object obj : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", obj.toString());
							
							jsonArray6.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 6", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 6", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray6.toString());
			}
			break;
		case 7://根据标准器具名称查询已有的型号规格
			JSONArray jsonArray7 = new JSONArray();
			try{
				String stdAppNameStr = req.getParameter("StdAppId");
				if(stdAppNameStr != null && stdAppNameStr.trim().length() > 0){
					String stdAppName = new String(stdAppNameStr.trim().getBytes("ISO-8859-1"), "GBK");
					String queryStr = "select distinct model.model from StandardAppliance model where model.name = ?";
					List<Object> retList = stanappMgr.findPageAllByHQL(queryStr, 1, 30, stdAppName);
					if(retList != null){
						for(Object obj : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("model", obj.toString());
							jsonArray7.put(jsonObj);
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 7", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 7", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray7.toString());
			}
			break;
		case 8://根据标准器具名称查询已有的测量范围
			JSONArray jsonArray8 = new JSONArray();
			try{
				String stdAppNameStr = req.getParameter("StdAppId");
				if(stdAppNameStr != null && stdAppNameStr.trim().length() > 0){
					String stdAppName = new String(stdAppNameStr.trim().getBytes("ISO-8859-1"), "GBK");
					String queryStr = "select distinct model.range from StandardAppliance model where model.name = ?";
					List<Object> retList = stanappMgr.findPageAllByHQL(queryStr, 1, 30, stdAppName);
					if(retList != null){
						for(Object obj : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("range", obj.toString());
							jsonArray8.put(jsonObj);
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 8", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 8", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray8.toString());
			}
			break;
		case 9://根据标准器具名称查询已有的不确定度
			JSONArray jsonArray9 = new JSONArray();
			try{
				String stdAppNameStr = req.getParameter("StdAppId");
				if(stdAppNameStr != null && stdAppNameStr.trim().length() > 0){
					String stdAppName = new String(stdAppNameStr.trim().getBytes("ISO-8859-1"), "GBK");
					String queryStr = "select distinct model.uncertain from StandardAppliance model where model.name = ?";
					List<Object> retList = stanappMgr.findPageAllByHQL(queryStr, 1, 30, stdAppName);
					if(retList != null){
						for(Object obj : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("uncertain", obj.toString());
							jsonArray9.put(jsonObj);
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 9", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 9", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray9.toString());
			}
			break;
		case 10://标准器具combobox模糊查询，返回前30个
			String stAppNameStr = req.getParameter("QueryName");	//
			JSONArray jsonArray10 = new JSONArray();
			try {
				if(stAppNameStr != null && stAppNameStr.trim().length() > 0){
					String stdAppName =  new String(stAppNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					String[] queryName = stdAppName.split(" \\s+");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					stdAppName = "";
					for(int i = 0; i < queryName.length; i++){
						stdAppName += queryName[i];
						if(i != queryName.length-1)
							stdAppName += "%";
					}
					stdAppName = "%" + stdAppName + "%";
					String queryString = String.format("from StandardAppliance as model where (model.brief like ? or model.name like ?) and model.status = ?");
					List<StandardAppliance> retList  = stanappMgr.findPageAllByHQL(queryString, 1, 30, "%" + stdAppName + "%", "%" + stdAppName + "%", Integer.valueOf(0));
					if(retList != null){
						for(StandardAppliance temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("id", temp.getId());
							jsonObj.put("name",temp.getName()+":"+temp.getRange());
							jsonObj.put("model", temp.getModel());
							jsonObj.put("range", temp.getRange());
							jsonObj.put("uncertain", temp.getUncertain());
							jsonArray10.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 10", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 10", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray10.toString());
			}
			break;
		case 11://新增一个标准器具替换原有被注销的标准器具，并update原有标准器具所在的关系
			JSONObject retObj11 = new JSONObject();
			try{
				String old_id = req.getParameter("Id");
				StandardAppliance newStdApp = initStandardAppliance(req, 0);
				if(stanappMgr.save(newStdApp))
				{
					int newId = newStdApp.getId();
					List<KeyValue> key = new ArrayList<KeyValue>();
					key.add(new KeyValue("standardAppliance.id", newId));
					
					if((new StdStdAppManager()).update(key, new KeyValue("standardAppliance.id", Integer.valueOf(old_id)))&&
								(new TgtAppStdAppManager()).update(key, new KeyValue("standardAppliance.id", Integer.valueOf(old_id)))){
						retObj11.put("IsOK", true);
						retObj11.put("msg", "替代成功！");
					}
					else{
						retObj11.put("IsOK", false);
						retObj11.put("msg", "替代失败，请重新替代！");
					}
					StandardAppliance oldStdApp = stanappMgr.findById(Integer.valueOf(old_id));
					oldStdApp.setStatus(1);
					stanappMgr.update(oldStdApp);
				}
				else
				{
					throw new Exception("新增新标准器具失败");
				}
			}catch(Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 11", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 11", e);
				} 
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj11.toString());
			}
			break;
		case 12://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj12 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from StandardAppliance as model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String queryName = params.has("queryname")?params.getString("queryname"):"";
					String queryModel = params.has("queryModel")?params.getString("queryModel"):"";
					String queryReleaseNumber = params.has("queryReleaseNumber")?params.getString("queryReleaseNumber"):"";
					String queryKeeper = params.has("queryKeeper")?params.getString("queryKeeper"):"";
					String queryLocaleCode = params.has("queryLocaleCode")?params.getString("queryLocaleCode"):"";
					String queryPermanentCode = params.has("queryPermanentCode")?params.getString("queryPermanentCode"):"";
					String queryInspectMonth = params.has("queryInspectMonth")?params.getString("queryInspectMonth"):"";
					String queryDept = params.has("queryDept")?params.getString("queryDept"):"";
					String queryStatus = params.has("queryStatus")?params.getString("queryStatus"):"";
					String Warn = params.has("Warn")?params.getString("Warn"):"";
					
					if(queryName!=null&&!queryName.equals(""))
					{
						String stanappNameStr = URLDecoder.decode(queryName, "UTF-8");
						queryStr = queryStr + " and (model.name like ? or model.brief like ?)";
						keys.add("%" + stanappNameStr + "%");
						keys.add("%" + stanappNameStr + "%");
					}
					if(queryModel!=null&&!queryModel.equals(""))
					{
						String stanappModelStr = URLDecoder.decode(queryModel, "UTF-8");
						queryStr = queryStr + " and (model.model like ? or model.range like ? or model.uncertain like ?)";
						keys.add("%" + stanappModelStr + "%");
						keys.add("%" + stanappModelStr + "%");
						keys.add("%" + stanappModelStr + "%");
					}
					if(queryReleaseNumber!=null&&!queryReleaseNumber.equals(""))
					{
						String stanappReleaseNumberStr = URLDecoder.decode(queryReleaseNumber, "UTF-8");
						queryStr = queryStr + " and model.releaseNumber like ? ";
						keys.add("%" + stanappReleaseNumberStr + "%");
					}
					if(queryKeeper!=null&&!queryKeeper.equals(""))
					{
						String stanappKeeperStr = URLDecoder.decode(queryKeeper, "UTF-8");
						queryStr = queryStr + " and (model.sysUser.name like ? or model.sysUser.brief like ?)";
						keys.add("%" + stanappKeeperStr + "%");
						keys.add("%" + stanappKeeperStr + "%");
					}
					if(queryLocaleCode!=null&&!queryLocaleCode.equals(""))
					{
						String stanappLocaleCodeStr = URLDecoder.decode(queryLocaleCode, "UTF-8");
						queryStr = queryStr + " and model.localeCode like ?";
						keys.add("%" + stanappLocaleCodeStr + "%");
					}
					if(queryPermanentCode!=null&&!queryPermanentCode.equals(""))
					{
						String stanappPermanentCodeStr = URLDecoder.decode(queryPermanentCode, "UTF-8");
						queryStr = queryStr + " and model.permanentCode like ?";
						keys.add("%" + stanappPermanentCodeStr + "%");
					}
					if(queryInspectMonth!=null&&!queryInspectMonth.equals(""))
					{
						String stanappInspectMonthStr = URLDecoder.decode(queryInspectMonth, "UTF-8");
						queryStr = queryStr + " and model.inspectMonth = ?";
						keys.add(Integer.valueOf(stanappInspectMonthStr));
					}
					if(queryStatus!=null&&!queryStatus.equals(""))
					{
						String stanappStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
						queryStr = queryStr + " and model.status = ?";
						keys.add(LetterUtil.isNumeric(stanappStatusStr)?Integer.valueOf(stanappStatusStr):0);
					}
					if(queryDept != null&&!queryDept.equals(""))
					{
						String stanDeptStr = URLDecoder.decode(queryDept, "UTF-8");
						keys.add("%" + stanDeptStr + "%");
						keys.add("%" + stanDeptStr + "%");
						queryStr = queryStr + " and model.sysUser.projectTeamId in (select model1.id from ProjectTeam as model1 where (model1.department.name like ? or model1.department.brief like ?))";
					}
					if(Warn != null&&!Warn.equals(""))
					{
						queryStr = queryStr + " and model.validDate >= ? and DATEADD(dd,0-model.warnSlot,model.validDate) <= ?";
						keys.add(new Date(System.currentTimeMillis()));
						keys.add(new Date(System.currentTimeMillis()));
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", StandardApplianceManager.class);				
				retObj12.put("IsOK", filePath.equals("")?false:true);
				retObj12.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 12", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 12", e);
				}
				try {
					retObj12.put("IsOK", false);
					retObj12.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj12.toString());
			}
			break;
		case 13://根据标准器具名称查询标准器具（仅返回型号、测量范围、不确定度、出厂等信息）
			JSONObject retObj13 = new JSONObject();
			try{
				String StdAppName = req.getParameter("StdAppName");
				List<StandardAppliance> result = null;
				int total = 0;
				if(StdAppName!=null&&!StdAppName.equals(""))
				{
					String StdAppNameStr = URLDecoder.decode(StdAppName, "UTF-8");
					result = stanappMgr.findByVarProperty(new KeyValueWithOperator("name", StdAppNameStr, "="),new KeyValueWithOperator("status", Integer.valueOf(0), "="));
					total = stanappMgr.getTotalCount(new KeyValueWithOperator("name", StdAppNameStr, "="),new KeyValueWithOperator("status", Integer.valueOf(0), "="));
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(StandardAppliance temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("Name", temp.getName());
						option.put("Model", temp.getModel());
						option.put("Range", temp.getRange());
						option.put("Uncertain", temp.getUncertain());
						option.put("Release", temp.getManufacturer()+temp.getReleaseNumber());
						option.put("PermanentCode", temp.getPermanentCode());
						
						options.put(option);
					}
				}
				retObj13.put("total", total);
				retObj13.put("rows", options);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 15", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 15", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj13.toString());
			}
			break;
		case 14://标准器具combobox模糊查询，返回前30个，在“直接编辑证书”之前的选择标准器具，计量规程等中用到
			JSONArray jsonArray14 = new JSONArray();
			try {
				String stdAppNameStr = req.getParameter("QueryName");	
				List<StandardAppliance> result = new ArrayList<StandardAppliance>();
				if(stdAppNameStr != null && stdAppNameStr.trim().length() > 0){
					String stdAppName =  new String(stdAppNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					String[] queryName = stdAppName.split(" \\s+");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					stdAppName = "";
					for(int i = 0; i < queryName.length; i++){
						stdAppName += queryName[i];
						if(i != queryName.length-1)
							stdAppName += "%";
					}
					stdAppName = "%" + stdAppName + "%";
					String queryString = String.format("from StandardAppliance as model where (model.brief like ? or model.name like ?) and model.status = ?");
					List<StandardAppliance> retList  = stanappMgr.findPageAllByHQL(queryString, 1, 30, "%" + stdAppName + "%", "%" + stdAppName + "%", Integer.valueOf(0));
					if(retList != null){
						for(StandardAppliance temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("id", temp.getId());
							jsonObj.put("name",temp.getName());
							
							StringBuilder str=new StringBuilder(temp.getName());
							str.append("-");
							str.append(temp.getModel());
							str.append("-");
							str.append(temp.getRange());
							str.append("-");
							str.append(temp.getUncertain());
							str.append("-");
							str.append(temp.getLocaleCode());
							jsonObj.put("name_num",str.toString());
							str=null;
							jsonObj.put("localecode",temp.getLocaleCode());
							jsonObj.put("model", temp.getModel());
							jsonObj.put("range", temp.getRange());
							jsonObj.put("uncertain", temp.getUncertain());
							jsonObj.put("release", temp.getManufacturer()+temp.getReleaseNumber());
							jsonObj.put("permanentcode", temp.getPermanentCode());
							jsonArray14.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in StandardApplianceServlet-->case 6", e);
				}else{
					log.error("error in StandardApplianceServlet-->case 6", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray14.toString());
			}
			break;
		}
	}
	
	private StandardAppliance initStandardAppliance(HttpServletRequest req, int id)
	{
		StandardAppliance stanapp = new StandardAppliance();
		if(id==0)
		{
			stanapp = new StandardAppliance();
		}
		else
		{
			StandardApplianceManager stanappmag = new StandardApplianceManager();
			stanapp = stanappmag.findById(id);
		}
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String Model = req.getParameter("Model");
		String Range = req.getParameter("Range");
		String Uncertain = req.getParameter("Uncertain");
		String Manufacturer = req.getParameter("Manufacturer");
		String ReleaseDateStr = req.getParameter("ReleaseDate");
		if(ReleaseDateStr!=null&&!ReleaseDateStr.equals(""))
		{
			Date ReleaseDate = Date.valueOf(ReleaseDateStr);
			if(ReleaseDate.compareTo(new Date(System.currentTimeMillis()))>0)
				return null;
			else
				stanapp.setReleaseDate(ReleaseDate);
		}
		else
		{
			stanapp.setReleaseDate(null);
		}
		String ReleaseNumber = req.getParameter("ReleaseNumber");
		String TestCycle = req.getParameter("TestCycle");
		String Num = req.getParameter("Num");
		String Price = req.getParameter("Price");
		String Status = req.getParameter("Status");
		String KeeperId = req.getParameter("KeeperId");
		String LocaleCode = req.getParameter("LocaleCode");
		String PermanentCode = req.getParameter("PermanentCode");
		String ProjectCode = req.getParameter("ProjectCode");
		String Budget = req.getParameter("Budget");
		String InspectMonth = req.getParameter("InspectMonth");
		String Remark = req.getParameter("Remark");
		String WarnSlot = req.getParameter("WarnSlot");
		
		
		stanapp.setName(Name);
		stanapp.setBrief(Brief);
		stanapp.setModel(Model);
		stanapp.setRange(Range);
		stanapp.setUncertain(Uncertain);
		stanapp.setManufacturer(Manufacturer);
		stanapp.setReleaseNumber(ReleaseNumber);
		stanapp.setTestCycle(Integer.valueOf(TestCycle));
		stanapp.setNum(Integer.valueOf(Num));
		stanapp.setPrice(Double.valueOf(Price));
		stanapp.setStatus(Integer.valueOf(Status));
		stanapp.setSysUser((new UserManager()).findByVarProperty(new KeyValueWithOperator("name", KeeperId, "=")).get(0));	
		int temp = 10000000 + Integer.valueOf(LocaleCode);
		stanapp.setLocaleCode(Integer.toString(temp).substring(1));
		stanapp.setPermanentCode(PermanentCode);
		stanapp.setProjectCode(ProjectCode);
		stanapp.setBudget(Budget);
		if(InspectMonth!=null&&!InspectMonth.equals(""))
			stanapp.setInspectMonth(Integer.valueOf(InspectMonth));
		else
			stanapp.setInspectMonth(null);
		if(Remark!=null)
			stanapp.setRemark(Remark);
		if(WarnSlot!=null&&!WarnSlot.equals(""))
			stanapp.setWarnSlot(Integer.valueOf(WarnSlot));
		else
			stanapp.setWarnSlot(null);
		
		return stanapp;
	}

}
