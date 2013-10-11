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

import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.servlet.appliance.TargetApplianceServlet;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

public class AppliancePopularNameServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(AppliancePopularNameServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int method = Integer.valueOf(req.getParameter("method"));
		AppliancePopularNameManager appPopularNameMgr = new AppliancePopularNameManager();
		switch(method){
		case 1://������������
			JSONObject retObj = new JSONObject();
			try {
				AppliancePopularName appPopularName = initAppliancePopularName(req, 0);
				boolean res1 = appPopularNameMgr.save(appPopularName);
				retObj.put("IsOK", res1);
				retObj.put("msg", res1?"�½��ɹ���":"�½�ʧ�ܣ��������½���");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 1", e);
				}else{
					log.error("error in AppliancePopularNameServlet-->case 1", e);
				}
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("���ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://���ݱ�׼���Ʋ�ѯ��������
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
				List<AppliancePopularName> result = new ArrayList<AppliancePopularName>();
				result = appPopularNameMgr.findPagedAll(page, rows, new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="));
				total = appPopularNameMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="));
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()!=0)
				{
					for(AppliancePopularName temp : result){
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("StandardName", temp.getApplianceStandardName().getName());
						option.put("Name", temp.getPopularName());
						option.put("Brief", temp.getBrief());
						option.put("Status", temp.getStatus());
						
						options.put(option);
					}
				}
				res.put("total", total);
				res.put("rows", options);
			}catch(Exception e){
				if (e.getClass() == java.lang.Exception.class) { // �Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 2",e);
				} else {
					log.error("error in AppliancePopularNameServlet-->case 2", e);
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
		case 3://�޸ĳ�������
			JSONObject retObj1 = new JSONObject();
			try {
				int id = Integer.valueOf(req.getParameter("Id"));
				AppliancePopularName appPopularName = initAppliancePopularName(req, id);
				boolean res1 = appPopularNameMgr.update(appPopularName);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"�޸ĳɹ���":"�޸�ʧ�ܣ��������޸ģ�");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 3", e);
				}else{
					log.error("error in AppliancePopularNameServlet-->case 3", e);
				}
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("�޸�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4://ע����������
			AppliancePopularName delPopularName = appPopularNameMgr.findById(Integer.valueOf(req.getParameter("id")));
			delPopularName.setStatus(1);
			JSONObject retObj2 = new JSONObject();
			try {
				boolean res1 = appPopularNameMgr.update(delPopularName);
				retObj2.put("IsOK", res1);
				retObj2.put("msg", res1 ? "ע���ɹ���" : "ע��ʧ�ܣ�������ע����");
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // �Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 4",e);
				} else {
					log.error("error in AppliancePopularNameServlet-->case 4", e);
				}
				try {
					retObj2.put("IsOK", false);
					retObj2.put("msg", String.format("ע��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj2.toString());
			}
			break;
		case 5://���ݱ�׼���Ʋ�ѯ��������
			JSONArray options = new JSONArray();
			try{
				String StandardNameId = req.getParameter("StandardNameId");

				
				List<AppliancePopularName> result = new ArrayList<AppliancePopularName>();
				result = appPopularNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="),new KeyValueWithOperator("status", 1, "<>"));
			
				
				if(result!=null&&result.size()!=0)
				{
					for(AppliancePopularName temp : result){
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("StandardName", temp.getApplianceStandardName().getName());
						option.put("Name", temp.getPopularName());
						option.put("Brief", temp.getBrief());
						option.put("Status", temp.getStatus());
						
						options.put(option);
					}
				}
				
			}catch(Exception e){
				if (e.getClass() == java.lang.Exception.class) { // �Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 5",e);
				} else {
					log.error("error in AppliancePopularNameServlet-->case 5", e);
				}
				
			}finally{
				
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(options.toString());
			}
			break;
		case 6://�������г�������
			JSONObject retObj6 = new JSONObject();
			try {
				String queryStr = "from AppliancePopularName model where 1=1";
				String filePath = ExportUtil.ExportToExcel(queryStr, new ArrayList<Object>(), null, "formatExcel", "formatTitle", AppliancePopularNameManager.class);				
				retObj6.put("IsOK", filePath.equals("")?false:true);
				retObj6.put("Path", filePath);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 6", e);
				}else{
					log.error("error in AppliancePopularNameServlet-->case 6", e);
				}
				try {
					retObj6.put("IsOK", false);
					retObj6.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj6.toString());
			}
			break;
		case 7://���ݱ�׼���Ƶ����Ʋ�ѯ��������
			JSONArray options7 = new JSONArray();
			try{
				String standardNameName = req.getParameter("standardNameName");

				standardNameName = new String(standardNameName.getBytes("ISO-8859-1"), "UTF-8");
				List<AppliancePopularName> result = new ArrayList<AppliancePopularName>();
				result = appPopularNameMgr.findByVarProperty(new KeyValueWithOperator("applianceStandardName.name", standardNameName, "="),new KeyValueWithOperator("status", 1, "<>"));
				
				if(result!=null&&result.size()!=0)
				{
					for(AppliancePopularName temp : result){
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("StandardName", temp.getApplianceStandardName().getName());
						option.put("Name", temp.getPopularName());					
						options7.put(option);
					}
				}
				
			}catch(Exception e){
				if (e.getClass() == java.lang.Exception.class) { // �Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 7",e);
				} else {
					log.error("error in AppliancePopularNameServlet-->case 7", e);
				}
				
			}finally{
				
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(options7.toString());
			}
			break;
		case 8://ģ����ѯ���߳�������
			JSONArray jsonArray2 = new JSONArray();
			try {
				String appStanNameStr = req.getParameter("AppliancePopularName");
				if(appStanNameStr != null && appStanNameStr.trim().length() > 0){
					String appStanName =  new String(appStanNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//���URL����������������
					//appStanName = LetterUtil.String2Alpha(appStanName);	//ת����ƴ������
					String[] queryName = appStanName.split(" \\s+");	//���ݿո���ָ�
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
					String queryString = String.format("from AppliancePopularName as model where (model.brief like ? or model.popularName like ?) and model.status = 0");
					List<AppliancePopularName> retList = appPopularNameMgr.findPageAllByHQL(queryString, 1, 30, appStanName, appStanName);
					if(retList != null){
						for(AppliancePopularName temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", temp.getPopularName());
							jsonObj.put("id", temp.getId());
							jsonArray2.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 8", e);
				}else{
					log.error("error in AppliancePopularNameServlet-->case 8", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
		case 9://���ݳ������Ʋ�ѯ��׼����
			JSONArray option9s = new JSONArray();
			try{
				String PopularName = req.getParameter("PopularName");
				if(PopularName != null && PopularName.trim().length() > 0){
					String appPopularName =  new String(PopularName.trim().getBytes("ISO-8859-1"), "UTF-8");	//���URL����������������
					
					List<AppliancePopularName> result = new ArrayList<AppliancePopularName>();
					result = appPopularNameMgr.findByVarProperty(new KeyValueWithOperator("popularName",appPopularName, "="),new KeyValueWithOperator("status",0, "="));
				
					
					if(result!=null&&result.size()!=0)
					{
						for(AppliancePopularName temp : result){
							JSONObject option = new JSONObject();
							option.put("id", temp.getApplianceStandardName().getId());
							option.put("standardname", temp.getApplianceStandardName().getName());
							
							
							option9s.put(option);
						}
					}
				}
			}catch(Exception e){
				if (e.getClass() == java.lang.Exception.class) { // �Զ������Ϣ
					log.debug("exception in AppliancePopularNameServlet-->case 9",e);
				} else {
					log.error("error in AppliancePopularNameServlet-->case 9", e);
				}
				
			}finally{
				
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(option9s.toString());
			}
			break;
		}
	}
	
	private AppliancePopularName initAppliancePopularName(HttpServletRequest req, int id) throws Exception
	{
		String StandardNameId = req.getParameter("StandardNameId");
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String Status = req.getParameter("Status");
		

		AppliancePopularName popularName ;
		if(id==0)
			popularName = new AppliancePopularName();
		else
			popularName = (new AppliancePopularNameManager()).findById(id);
		
		if(id==0||!popularName.getPopularName().equals(Name)){
			List<AppliancePopularName> popularNameList = (new AppliancePopularNameManager()).findByVarProperty(new KeyValueWithOperator("applianceStandardName.id", Integer.valueOf(StandardNameId), "="), new KeyValueWithOperator("popularName", Name, "="));
			if(popularNameList!=null&&popularNameList.size()>0)
				throw new Exception("�ó��������Ѵ��ڣ�");
		}
		
		popularName.setApplianceStandardName((new ApplianceStandardNameManager()).findById(Integer.valueOf(StandardNameId)));
		popularName.setPopularName(Name);
		popularName.setBrief(Brief);
		popularName.setStatus(Integer.valueOf(Status));
		return popularName;
	}
	
}
