package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.jlyw.hibernate.TestLog;
import com.jlyw.manager.TestLogManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.UIDUtil;

public class TestLogServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(TestLogServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		TestLogManager testlogMgr = new TestLogManager();
		switch (method) {
		case 1: // 新建量值溯源记录
			JSONObject retObj = new JSONObject();
			try {
				String user = req.getParameter("Confirmer");
				if(user != null && !user.equals("") ){
					List<SysUser> check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", user, "="));
					if(check==null||check.size()==0)
						throw new Exception("员工输入有误，请确认！");
				}
				TestLog testlog = initTestLog(req, 0);
				if(testlogMgr.saveTestLog(testlog)){
					retObj.put("IsOK", true);
					retObj.put("msg","新建成功！");
					retObj.put("CertificateCopy_filesetname", testlog.getCertificateCopy());
				}else{
					throw new Exception("更新数据库失败！");
				}
			} catch (Exception e) {
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TestLogServlet-->case 1", e);
				}else{
					log.error("error in TestLogServlet-->case 1", e);
				}
				
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2:					//查询testlog
			JSONObject retObj2 = new JSONObject();
			try {
				String StdAppId = req.getParameter("StdAppId"); //标准器具名称
				String queryValidDateFrom = req.getParameter("queryValidDateFrom");
				String queryValidDateEnd = req.getParameter("queryValidDateEnd");
				String queryTestDateFrom = req.getParameter("queryTestDateFrom");
				String queryTestDateEnd = req.getParameter("queryTestDateEnd");
				String queryTester = req.getParameter("queryTester");
				String queryCertificateId = req.getParameter("queryCertificateId");
				String queryStatus = req.getParameter("queryStatus");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<TestLog> result;
				int total;
				List<Object> keys = new ArrayList<Object>();
				String queryStr = "from TestLog as model where 1=1 ";
				
				if(StdAppId!=null&&!StdAppId.equals(""))
				{
					String stdAppIdStr = URLDecoder.decode(StdAppId, "UTF-8");
					queryStr = queryStr + " and model.standardAppliance.id = ?";
					keys.add(Integer.valueOf(stdAppIdStr));
				}
				if(queryValidDateFrom!=null&&!queryValidDateFrom.equals("")&&queryValidDateEnd!=null&&!queryValidDateEnd.equals(""))
				{
					String ValidDateFrom = URLDecoder.decode(queryValidDateFrom, "UTF-8");
					String ValidDateEnd = URLDecoder.decode(queryValidDateEnd, "UTF-8");
					Date Start = Date.valueOf(ValidDateFrom);
					Date End = Date.valueOf(ValidDateEnd);
					queryStr = queryStr + " and (model.validDate >= ? and model.validDate <= ?)";
					keys.add(Start);
					keys.add(End);
				}
				if(queryTestDateFrom!=null&&!queryTestDateFrom.equals("")&&queryTestDateEnd!=null&&!queryTestDateEnd.equals(""))
				{
					String TestDateFrom = URLDecoder.decode(queryTestDateFrom, "UTF-8");
					String TetstDateEnd = URLDecoder.decode(queryTestDateEnd, "UTF-8");
					Date Start = Date.valueOf(TestDateFrom);
					Date End = Date.valueOf(TetstDateEnd);
					queryStr = queryStr + " and (model.testDate >= ? and model.testDate <= ?)";
					keys.add(Start);
					keys.add(End);
				}
				if(queryTester!=null&&!queryTester.equals(""))
				{
					String Tester = URLDecoder.decode(queryTester, "UTF-8");
					queryStr = queryStr + " and model.tester like ?";
					keys.add("%" + Tester + "%");
				}
				if(queryCertificateId!=null&&!queryCertificateId.equals(""))
				{
					String CertificateId = URLDecoder.decode(queryCertificateId, "UTF-8");
					queryStr = queryStr + " and model.certificateId like ?";
					keys.add("%" + CertificateId + "%");
				}
				if(queryStatus!= null&&!queryStatus.equals(""))
				{
					String stanStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
					keys.add(LetterUtil.isNumeric(stanStatusStr)?Integer.valueOf(stanStatusStr):0);
					queryStr = queryStr + " and model.status = ? ";
				}
				
				result = testlogMgr.findPageAllByHQL(queryStr + " order by model.testDate desc", page, rows, keys);
				total = testlogMgr.getTotalCountByHQL("select count(*) " + queryStr, keys);
				JSONArray options = new JSONArray();
				for (TestLog testlog : result) {
					JSONObject option = new JSONObject();
					option.put("Id", testlog.getId());
					option.put("StandardApplianceId", testlog.getStandardAppliance().getId());
					option.put("StandardApplianceLocaleCode", testlog.getStandardAppliance().getLocaleCode());
					option.put("StandardApplianceName", testlog.getStandardAppliance().getName());
					option.put("StandardApplianceModel", testlog.getStandardAppliance().getModel());
					option.put("StandardApplianceRange", testlog.getStandardAppliance().getRange());
					option.put("StandardApplianceUncertain", testlog.getStandardAppliance().getUncertain());
					option.put("TestDate", testlog.getTestDate());
					option.put("ValidDate", testlog.getValidDate());
					option.put("Tester", testlog.getTester());
					option.put("CertificateId", testlog.getCertificateId());//检定证书编号varchar
					
					option.put("ConfirmMeasure", testlog.getConfirmMeasure()==null?"":testlog.getConfirmMeasure());
					option.put("Confirmer", testlog.getSysUser()==null?"":testlog.getSysUser().getName());
					option.put("ConfirmDate", testlog.getConfirmDate()==null?"":testlog.getConfirmDate());
					option.put("DurationCheck", testlog.getDurationCheck()==null?"":testlog.getDurationCheck());
					option.put("Maintenance", testlog.getMaintenance()==null?"":testlog.getMaintenance());
					option.put("Remark", testlog.getRemark()==null?"":testlog.getRemark());
					option.put("Status", testlog.getStatus());
					
					if(testlog.getCertificateCopy()!=null&&!testlog.getCertificateCopy().equals(""))
					{
						HashMap<String, Object> h = new HashMap<String, Object>();
						h.put(MongoDBUtil.KEYNAME_FileSetName, testlog.getCertificateCopy());
						JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
						option.put("Copy", file==null?"":file);
					}
					else
					{
						option.put("Copy", "");
					}
					
					options.put(option);
				}
				retObj2.put("total", total);
				retObj2.put("rows", options);
			} catch (Exception e) {
				try {
					retObj2.put("total", 0);
					retObj2.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TestLogServlet-->case 2", e);
				}else{
					log.error("error in TestLogServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(retObj2.toString());
			}
			break;
		case 3:					//修改teatlog
			
			//testlog.setId(Integer.valueOf(req.getParameter("Id")));
			JSONObject retObj3=new JSONObject();
			try {
				String user = req.getParameter("Confirmer");
				if(user != null && !user.equals("") ){
					List<SysUser> check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", user, "="));
					if(check==null||check.size()==0)
						throw new Exception("员工输入有误，请确认！");
				}
				TestLog testlog = initTestLog(req,Integer.valueOf(req.getParameter("Id")));
				if(testlogMgr.updateTestLog(testlog)){
					retObj3.put("IsOK", true);
					retObj3.put("msg","修改成功！");
					retObj3.put("CertificateCopy_filesetname", testlog.getCertificateCopy());
					
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e) {
				try {
					retObj3.put("IsOK", false);
					retObj3.put("msg", String.format("修改失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TestLogServlet-->case 3", e);
				}else{
					log.error("error in TestLogServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			
			break;
		case 4:					//删除testlog
			int id = Integer.parseInt(req.getParameter("id"));
			TestLog testlog = testlogMgr.findById(id);
			testlog.setStatus(1);
			JSONObject ret=new JSONObject();
			try {
				boolean res1 = testlogMgr.updateTestLog(testlog);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StandardServlet-->case 4", e);
				}else{
					log.error("error in StandardServlet-->case 4", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(ret.toString());
			break;
		case 5://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from TestLog as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String StdAppId = params.has("StdAppId")?params.getString("StdAppId"):""; //标准器具名称
					String queryValidDateFrom = params.has("queryValidDateFrom")?params.getString("queryValidDateFrom"):"";
					String queryValidDateEnd = params.has("queryValidDateEnd")?params.getString("queryValidDateEnd"):"";
					String queryTestDateFrom = params.has("queryTestDateFrom")?params.getString("queryTestDateFrom"):"";
					String queryTestDateEnd = params.has("queryTestDateEnd")?params.getString("queryTestDateEnd"):"";
					String queryTester = params.has("queryTester")?params.getString("queryTester"):"";
					String queryCertificateId = params.has("queryCertificateId")?params.getString("queryCertificateId"):"";
					String queryStatus = params.has("queryStatus")?params.getString("queryStatus"):"";					
					
					if(StdAppId!=null&&!StdAppId.equals(""))
					{
						String stdAppIdStr = URLDecoder.decode(StdAppId, "UTF-8");
						queryStr = queryStr + " and model.standardAppliance.id = ?";
						keys.add(Integer.valueOf(stdAppIdStr));
					}
					if(queryValidDateFrom!=null&&!queryValidDateFrom.equals("")&&queryValidDateEnd!=null&&!queryValidDateEnd.equals(""))
					{
						String ValidDateFrom = URLDecoder.decode(queryValidDateFrom, "UTF-8");
						String ValidDateEnd = URLDecoder.decode(queryValidDateEnd, "UTF-8");
						Date Start = Date.valueOf(ValidDateFrom);
						Date End = Date.valueOf(ValidDateEnd);
						queryStr = queryStr + " and (model.validDate >= ? and model.validDate <= ?)";
						keys.add(Start);
						keys.add(End);
					}
					if(queryTestDateFrom!=null&&!queryTestDateFrom.equals("")&&queryTestDateEnd!=null&&!queryTestDateEnd.equals(""))
					{
						String TestDateFrom = URLDecoder.decode(queryTestDateFrom, "UTF-8");
						String TetstDateEnd = URLDecoder.decode(queryTestDateEnd, "UTF-8");
						Date Start = Date.valueOf(TestDateFrom);
						Date End = Date.valueOf(TetstDateEnd);
						queryStr = queryStr + " and (model.testDate >= ? and model.testDate <= ?)";
						keys.add(Start);
						keys.add(End);
					}
					if(queryTester!=null&&!queryTester.equals(""))
					{
						String Tester = URLDecoder.decode(queryTester, "UTF-8");
						queryStr = queryStr + " and model.tester like ?";
						keys.add("%" + Tester + "%");
					}
					if(queryCertificateId!=null&&!queryCertificateId.equals(""))
					{
						String CertificateId = URLDecoder.decode(queryCertificateId, "UTF-8");
						queryStr = queryStr + " and model.certificateId like ?";
						keys.add("%" + CertificateId + "%");
					}
					if(queryStatus != null&&!queryStatus.equals(""))
					{
						String stanStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
						keys.add(LetterUtil.isNumeric(stanStatusStr)?Integer.valueOf(stanStatusStr):0);
						queryStr = queryStr + " and model.status = ? ";
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", TestLogManager.class);				
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
		}
	}
	private TestLog initTestLog(HttpServletRequest req, int id)
	{	
		TestLog testlog;
		int StandardNameId =Integer.parseInt(req.getParameter("StandardApplianceId"));
		
		Date TestDate = Date.valueOf(req.getParameter("TestDate"));
		Date ValidDate = Date.valueOf(req.getParameter("ValidDate"));
		String Tester = req.getParameter("Tester");
		String CertificateId = req.getParameter("CertificateId");
		String ConfirmMeasure = req.getParameter("ConfirmMeasure");
		String Confirmer = req.getParameter("Confirmer");
		String ConfirmDate = req.getParameter("ConfirmDate");
		String DurationCheck = req.getParameter("DurationCheck");
		String Maintenance = req.getParameter("Maintenance");
		String Remark = req.getParameter("Remark");
		String Status = req.getParameter("Status");
		if(id==0)
		{
			testlog = new TestLog();
			testlog.setCertificateCopy(UIDUtil.get22BitUID());
		}
		else
		{
			testlog = (new TestLogManager()).findById(id);  //通过文件夹ID找到要修改 的TestLog
		}
		
		testlog.setCertificateId(CertificateId);
		if(ConfirmDate != null && ConfirmDate.length() > 0){
			testlog.setConfirmDate(Date.valueOf(ConfirmDate));
		}else{
			testlog.setConfirmDate(null);
		}
		
		testlog.setConfirmMeasure(ConfirmMeasure);
		testlog.setDurationCheck(DurationCheck);
		testlog.setMaintenance(Maintenance);
		testlog.setRemark(Remark);
		
		StandardAppliance stdApp = new StandardAppliance();
		stdApp.setId(StandardNameId);
		testlog.setStandardAppliance(stdApp);
		
//		int ConfirmerId = Integer.parseInt(req.getParameter("ConfirmerId"));
		
		if(Confirmer != null && !Confirmer.equals("")){
			testlog.setSysUser((new UserManager()).findByVarProperty(new KeyValueWithOperator("name", Confirmer, "=")).get(0));
		}else{
			testlog.setSysUser(null);
		}
		testlog.setTestDate(TestDate);
		testlog.setValidDate(ValidDate);
		testlog.setTester(Tester);
		testlog.setStatus(Integer.parseInt(Status));
		
		return testlog;
	}
}
