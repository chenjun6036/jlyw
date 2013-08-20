package com.jlyw.servlet;

import java.io.IOException;
import java.util.Calendar;
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

import com.jlyw.hibernate.StdStdApp;
import com.jlyw.hibernate.StdTgtApp;
import com.jlyw.hibernate.TgtAppSpec;
import com.jlyw.manager.StdStdAppManager;
import com.jlyw.manager.StdTgtAppManager;
import com.jlyw.manager.TgtAppSpecManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;
/**
 * 原始记录依据的技术规范、计量标准、使用的标准器具查询
 * @author Zhan
 * 2012-06-06
 *
 */
public class OriginalRecordDocsServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(OriginalRecordDocsServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		
		switch(method){
		case 0:		//查询一个受检器具依据的技术规范
			JSONObject retJSON = new JSONObject();
			try{
				String TargetApplianceId = req.getParameter("TargetApplianceId");
				if(TargetApplianceId == null || TargetApplianceId.length() == 0){
					throw new Exception("");
				}
				TgtAppSpecManager tsMgr = new TgtAppSpecManager();
				List<TgtAppSpec> retList = tsMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", Integer.parseInt(TargetApplianceId), "="),
						new KeyValueWithOperator("specification.status", 1, "<>"));
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				java.sql.Date nowDate = new java.sql.Date(c.getTimeInMillis());
				JSONArray jsonArray = new JSONArray();
				for(TgtAppSpec ts : retList){
					if(ts.getSpecification().getRepealDate()==null || ts.getSpecification().getRepealDate().after(nowDate)){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", ts.getId());
						jsonObj.put("SpecificationId", ts.getSpecification().getId());
						jsonObj.put("SpecificationName", ts.getSpecification().getNameCn());
						jsonObj.put("SpecificationCode", ts.getSpecification().getSpecificationCode());
						jsonObj.put("RepealDate", ts.getSpecification().getRepealDate()==null?"":DateTimeFormatUtil.DateFormat.format(ts.getSpecification().getRepealDate()));

						jsonArray.put(jsonObj);
					}
				}
				retJSON.put("total", jsonArray.length());
				retJSON.put("rows", jsonArray);
			}catch(Exception e){
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordDocsServlet-->case 0", e);
				}else{
					log.error("error in OriginalRecordDocsServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//查询一个受检器具依据的计量标准
			JSONObject retJSON1 = new JSONObject();
			try{
				String TargetApplianceId = req.getParameter("TargetApplianceId");
				if(TargetApplianceId == null || TargetApplianceId.length() == 0){
					throw new Exception("");
				}
				StdTgtAppManager stMgr = new StdTgtAppManager();
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				java.sql.Date nowDate = new java.sql.Date(c.getTimeInMillis());
				
				List<StdTgtApp> retList = stMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", Integer.parseInt(TargetApplianceId), "="),
						new KeyValueWithOperator("standard.status", 1, "<>"));
				JSONArray jsonArray = new JSONArray();
				for(StdTgtApp st : retList){
					if(st.getStandard().getValidDate() == null || st.getStandard().getValidDate().after(nowDate)){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", st.getId());
						jsonObj.put("StandardId", st.getStandard().getId());
						jsonObj.put("StandardName", st.getStandard().getName());
						jsonObj.put("CertificateCode", st.getStandard().getCertificateCode());
						jsonObj.put("StandardCode", st.getStandard().getStandardCode());
						jsonObj.put("ValidDate", DateTimeFormatUtil.DateFormat.format(st.getStandard().getValidDate()));
						jsonObj.put("Range", st.getStandard().getRange());
						jsonObj.put("Uncertain", st.getStandard().getUncertain());
						jsonArray.put(jsonObj);
					}
				}
				retJSON1.put("total", jsonArray.length());
				retJSON1.put("rows", jsonArray);
			}catch(Exception e){
				try {
					retJSON1.put("total", 0);
					retJSON1.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordDocsServlet-->case 1", e);
				}else{
					log.error("error in OriginalRecordDocsServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//获取一个受检器具依据的计量标准下的所有标准器具
			JSONObject retJSON2 = new JSONObject();
			try{
				String TargetApplianceId = req.getParameter("TargetApplianceId");
				if(TargetApplianceId == null || TargetApplianceId.length() == 0){
					throw new Exception("");
				}
				StdStdAppManager ssMgr = new StdStdAppManager();
				String queryString = "from StdStdApp as a where a.standard.status<>1 and a.standardAppliance.status<>1 " +
						" and a.standard.id in (select b.standard.id from StdTgtApp as b where b.targetAppliance.id=? ) " +
						" order by a.standard.id asc, a.sequence asc ";
				List<StdStdApp> retList = ssMgr.findPageAllByHQL(queryString, 1, 500, Integer.parseInt(TargetApplianceId));
				Calendar c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				java.sql.Date nowDate = new java.sql.Date(c.getTimeInMillis());

				JSONArray jsonArray = new JSONArray();
				for(StdStdApp ss : retList){
					if(ss.getStandard().getValidDate() == null || ss.getStandard().getValidDate().after(nowDate)){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", ss.getId());
						jsonObj.put("StandardId", ss.getStandard().getId());
						jsonObj.put("StandardName", ss.getStandard().getName());
						jsonObj.put("CertificateCode", ss.getStandard().getCertificateCode());
						jsonObj.put("StandardApplianceId", ss.getStandardAppliance().getId());
						jsonObj.put("StandardApplianceName", ss.getStandardAppliance().getName());
						jsonObj.put("Model", ss.getStandardAppliance().getModel());
						jsonObj.put("Range", ss.getStandardAppliance().getRange());
						jsonObj.put("Uncertain", ss.getStandardAppliance().getUncertain());
						jsonObj.put("SeriaNumber", ss.getStandardAppliance().getSeriaNumber());
						jsonArray.put(jsonObj);
					}
				}
				retJSON2.put("total", jsonArray.length());
				retJSON2.put("rows", jsonArray);
			}catch(Exception e){
				try {
					retJSON2.put("total", 0);
					retJSON2.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordDocsServlet-->case 1", e);
				}else{
					log.error("error in OriginalRecordDocsServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		}
	}

	
}
