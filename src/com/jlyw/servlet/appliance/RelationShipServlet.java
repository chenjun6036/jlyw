package com.jlyw.servlet.appliance;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.StdStdApp;
import com.jlyw.hibernate.StdTgtApp;
import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.TgtAppSpec;
import com.jlyw.hibernate.TgtAppStdApp;
import com.jlyw.manager.SpecificationManager;
import com.jlyw.manager.StandardApplianceManager;
import com.jlyw.manager.StandardManager;
import com.jlyw.manager.StdStdAppManager;
import com.jlyw.manager.StdTgtAppManager;
import com.jlyw.manager.TargetApplianceManager;
import com.jlyw.manager.TgtAppSpecManager;
import com.jlyw.manager.TgtAppStdAppManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

public class RelationShipServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(RelationShipServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		//ViewStandardStandardApplianceTargetApplianceSpecificationManager viewMgr = new ViewStandardStandardApplianceTargetApplianceSpecificationManager();
		switch(method)
		{
		case 1://新建标准器具与标准的关系
			String StdStdApp_StanAppId = req.getParameter("StandardApplianceIdStr");
			String StdStdApp_StandardId = req.getParameter("StandardId");
			String StdStdApp_IsMain = req.getParameter("IsMainStr");
			String StdStdApp_Sequence = req.getParameter("SequenceStr");
			StdStdAppManager std_stdapp_Mgr1 = new StdStdAppManager();
			
			JSONObject retObj1 = new JSONObject();
			try{
				String[] StanAppIds = StdStdApp_StanAppId.split("\\|");
				String[] IsMainStr = StdStdApp_IsMain.split("\\|");
				String[] SequenceStr = StdStdApp_Sequence.split("\\|");
				
				List<StdStdApp> list1 = std_stdapp_Mgr1.findByVarProperty(new KeyValueWithOperator("standard.id",Integer.valueOf(StdStdApp_StandardId),"="));
				List<StdStdApp> saveList = new ArrayList<StdStdApp>();
				List<StdStdApp> updateList = new ArrayList<StdStdApp>();
				for(int i = 0; i < StanAppIds.length; i++)
				{
					int j;
					for(j = 0; j < list1.size(); j++)
					{
						StdStdApp temp = list1.get(j);
						if(temp.getStandardAppliance().getId().equals(Integer.valueOf(StanAppIds[i])))
						{
							temp.setIsMain(Integer.valueOf(IsMainStr[i])==0?true:false);
							temp.setSequence(LetterUtil.isNumeric(SequenceStr[i])?Integer.valueOf(SequenceStr[i]):1);
							updateList.add(temp);
							break;
						}
					}
					if(j==list1.size())
					{
						StdStdApp temp = new StdStdApp();
						temp.setStandard((new StandardManager()).findById(Integer.valueOf(StdStdApp_StandardId)));
						temp.setStandardAppliance((new StandardApplianceManager()).findById(Integer.valueOf(StanAppIds[i])));
						temp.setIsMain(Integer.valueOf(IsMainStr[i])==0?true:false);
						temp.setSequence(LetterUtil.isNumeric(SequenceStr[i])?Integer.valueOf(SequenceStr[i]):1);
						saveList.add(temp);
					}
				}
				if(std_stdapp_Mgr1.saveByBatch(saveList)&&std_stdapp_Mgr1.updateByBatch(updateList))
				{
					retObj1.put("IsOK", true);
					retObj1.put("msg", "新建成功！");
				}
				else
				{
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("新建失败！错误信息：%s", "无"));
				}
			}catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 1", e);
				}else{
					log.error("error in RelationShipServlet-->case 1", e);
				}
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (Exception ex) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 2://查询标准器具与标准的关系
			JSONObject res2 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<StdStdApp> result;
				StdStdAppManager std_stdapp_Mgr = new StdStdAppManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("standard.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("standardAppliance.name", "%" + queryStr + "%", "like");
				
				if(k == null)
				{
					result = std_stdapp_Mgr.findPagedAll(page, rows);
					total = std_stdapp_Mgr.getTotalCount();
				}
				else
				{
					result = std_stdapp_Mgr.findPagedAll(page, rows, k);
					total = std_stdapp_Mgr.getTotalCount(k);
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(StdStdApp temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getId());
						option.put("Std_Name", temp.getStandard().getName());
						option.put("Std_CertificateCode", temp.getStandard().getCertificateCode());
						option.put("Std_StandardCode", temp.getStandard().getStandardCode());
						option.put("Std_Status", temp.getStandard().getStatus());
						option.put("StdApp_Name", temp.getStandardAppliance().getName());
						option.put("StdApp_Model", temp.getStandardAppliance().getModel());
						option.put("StdApp_Range", temp.getStandardAppliance().getRange());
						option.put("StdApp_Uncertain", temp.getStandardAppliance().getUncertain());
						option.put("StdApp_Release", temp.getStandardAppliance().getManufacturer()+temp.getStandardAppliance().getReleaseNumber());
						option.put("StdApp_LocaleCode", temp.getStandardAppliance().getLocaleCode());
						option.put("StdApp_Status", temp.getStandardAppliance().getStatus());
						option.put("Std_StdApp_IsMain", temp.getIsMain()?0:1);
						option.put("Std_StdApp_Sequence", temp.getSequence());
						
						options.put(option);
					}
				}
				res2 = new JSONObject();
				res2.put("total", total);
				res2.put("rows", options);
			}
			catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 2", e);
				}else{
					log.error("error in RelationShipServlet-->case 2", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res2.toString());
			break;
		case 3://删除标准器具与标准的关系
			int std_stdapp_id = Integer.valueOf(req.getParameter("id"));
			JSONObject ret3 = new JSONObject();
			try {
				boolean res = (new StdStdAppManager()).deleteById(std_stdapp_id);
				ret3.put("IsOK", res);
				ret3.put("msg", res?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 3", e);
				}else{
					log.error("error in RelationShipServlet-->case 3", e);
				}
				try {
					ret3.put("IsOK", false);
					ret3.put("msg", String.format("删除失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (Exception ex) {}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret3.toString());
			break;
		case 4://新建标准器具与受检器具的关系
			String StdAppTgtApp_StdAppId = req.getParameter("StandardApplianceIdStr");
			String StdAppTgtApp_TgtAppId = req.getParameter("TgtAppId");
			TgtAppStdAppManager tgtapp_stdapp_Mgr = new TgtAppStdAppManager();
			
			JSONObject retObj4 = new JSONObject();
			try{
				String[] StanAppIds = StdAppTgtApp_StdAppId.split("\\|");
				
				List<TgtAppStdApp> list = tgtapp_stdapp_Mgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id",Integer.valueOf(StdAppTgtApp_TgtAppId),"="));
				List<TgtAppStdApp> saveList = new ArrayList<TgtAppStdApp>();
				for(int i = 0; i < StanAppIds.length; i++)
				{
					int j;
					for(j = 0; list!=null&&j < list.size(); j++)
					{
						TgtAppStdApp temp = list.get(j);
						if(temp.getStandardAppliance().getId().equals(Integer.valueOf(StanAppIds[i])))
							break;
					}
					if(list==null||j==list.size())
					{
						TgtAppStdApp temp = new TgtAppStdApp();
						temp.setTargetAppliance((new TargetApplianceManager()).findById(Integer.valueOf(StdAppTgtApp_TgtAppId)));
						temp.setStandardAppliance((new StandardApplianceManager()).findById(Integer.valueOf(StanAppIds[i])));
						saveList.add(temp);
					}
				}
				if(tgtapp_stdapp_Mgr.saveByBatch(saveList))
				{
					retObj4.put("IsOK", true);
					retObj4.put("msg", "新建成功！");
				}
				else
				{
					retObj4.put("IsOK", false);
					retObj4.put("msg", String.format("新建失败！错误信息：%s", "无"));
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 4", e);
				}else{
					log.error("error in RelationShipServlet-->case 4", e);
				}
				try {
					retObj4.put("IsOK", false);
					retObj4.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (Exception ex) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		case 5://查询所有标准器具与受检器具的关系
			JSONObject res5 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<TgtAppStdApp> result;
				TgtAppStdAppManager tgtapp_stdapp_Mgr5 = new TgtAppStdAppManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("standardAppliance.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("targetAppliance.name", "%" + queryStr + "%", "like");
				
				if(k == null)
				{
					result = tgtapp_stdapp_Mgr5.findPagedAll(page, rows);
					total = tgtapp_stdapp_Mgr5.getTotalCount();
				}
				else
				{
					result = tgtapp_stdapp_Mgr5.findPagedAll(page, rows, k);
					total = tgtapp_stdapp_Mgr5.getTotalCount(k);
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(TgtAppStdApp temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getId());
						option.put("StdApp_Name", temp.getStandardAppliance().getName());
						option.put("StdApp_Model", temp.getStandardAppliance().getModel());
						option.put("StdApp_Range", temp.getStandardAppliance().getRange());
						option.put("StdApp_Uncertain", temp.getStandardAppliance().getUncertain());
						option.put("StdApp_Release", temp.getStandardAppliance().getManufacturer()+temp.getStandardAppliance().getSeriaNumber());
						option.put("StdApp_LocaleCode", temp.getStandardAppliance().getLocaleCode());
						option.put("StdApp_Status", temp.getStandardAppliance().getStatus());
						option.put("TgtApp_Name", temp.getTargetAppliance().getName());
						option.put("TgtApp_StandardName", temp.getTargetAppliance().getApplianceStandardName().getName());
						option.put("TgtApp_Code", temp.getTargetAppliance().getCode());
						option.put("TgtApp_Status", temp.getTargetAppliance().getStatus());	
						
						options.put(option);
					}
				}
				res5 = new JSONObject();
				res5.put("total", total);
				res5.put("rows", options); 
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 5", e);
				}else{
					log.error("error in RelationShipServlet-->case 5", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res5.toString());
			break;
		case 6://删除标准器具与受检器具的关系
			int tgtapp_stdapp_id = Integer.valueOf(req.getParameter("id"));
			JSONObject ret6 = new JSONObject();
			try {
				boolean res = (new TgtAppStdAppManager()).deleteById(tgtapp_stdapp_id);
				ret6.put("IsOK", res);
				ret6.put("msg", res?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 6", e);
				}else{
					log.error("error in RelationShipServlet-->case 6", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret6.toString());
			break;
		case 7://新建标准与受检器具的关系
			int StdTgtApp_TgtAppId = Integer.valueOf(req.getParameter("StdTgtApp_TargetApplianceId"));
			int StdTgtApp_StandardId = Integer.valueOf(req.getParameter("StdTgtApp_StandardId"));
			StdTgtAppManager std_tgtapp_Mgr = new StdTgtAppManager();
			
			JSONObject retObj7 = new JSONObject();
			try{
				List<StdTgtApp> list1 = std_tgtapp_Mgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id",StdTgtApp_TgtAppId,"="),new KeyValueWithOperator("standard.id",StdTgtApp_StandardId,"="));
				if(list1==null||list1.size()==0)
				{
					TargetAppliance TgtApp = (new TargetApplianceManager()).findById(StdTgtApp_TgtAppId);
					Standard Standard = (new StandardManager()).findById(StdTgtApp_StandardId);
					StdTgtApp stdtgtapp = new StdTgtApp();
					stdtgtapp.setTargetAppliance(TgtApp);
					stdtgtapp.setStandard(Standard);
					
					boolean res1 = std_tgtapp_Mgr.save(stdtgtapp);
					retObj7.put("IsOK", res1);
					retObj7.put("msg", res1?"新建成功！":"新建失败，请重新新建！");
				}
				else
				{
					retObj7.put("IsOK", false);
					retObj7.put("msg", "该关系已存在，无需再次新建！");
				}
			}catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 7", e);
				}else{
					log.error("error in RelationShipServlet-->case 7", e);
				}
				try {
					retObj7.put("IsOK", false);
					retObj7.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (Exception ex) {}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj7.toString());
			break;
		case 8://查询所有标准与受检器具的关系
			JSONObject res8 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<StdTgtApp> result;
				StdTgtAppManager std_tgtapp_Mgr8 = new StdTgtAppManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("standard.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("targetAppliance.name", "%" + queryStr + "%", "like");
				//System.out.println(queryStr);
				
				if(k == null)
				{
					result = std_tgtapp_Mgr8.findPagedAll(page, rows);
					total = std_tgtapp_Mgr8.getTotalCount();
				}
				else
				{
					result = std_tgtapp_Mgr8.findPagedAll(page, rows, k);
					total = std_tgtapp_Mgr8.getTotalCount(k);
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(StdTgtApp temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getId());
						option.put("Std_Name", temp.getStandard().getName());
						option.put("Std_CertificateCode", temp.getStandard().getCertificateCode());
						option.put("Std_StandardCode", temp.getStandard().getStandardCode());
						option.put("Std_Status", temp.getStandard().getStatus());
						option.put("TgtApp_Name", temp.getTargetAppliance().getName());
						option.put("TgtApp_StandardName", temp.getTargetAppliance().getApplianceStandardName().getName());
						option.put("TgtApp_Code", temp.getTargetAppliance().getCode());
						option.put("TgtApp_Status", temp.getTargetAppliance().getStatus());
					
						options.put(option);
					}
				}
				res8 = new JSONObject();
				res8.put("total", total);
				res8.put("rows", options);
			}
			catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 8", e);
				}else{
					log.error("error in RelationShipServlet-->case 8", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res8.toString());

			break;
		case 9://删除标准与受检器具的关系
			int std_tgtapp_id = Integer.valueOf(req.getParameter("id"));
			JSONObject ret9 = new JSONObject();
			try {
				boolean res = (new StdTgtAppManager()).deleteById(std_tgtapp_id);
				ret9.put("IsOK", res);
				ret9.put("msg", res?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 9", e);
				}else{
					log.error("error in RelationShipServlet-->case 9", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret9.toString());
			break;
		case 10://新建技术规范和受检器具的关系
			int TgtAppSpec_TgtAppId = Integer.valueOf(req.getParameter("TgtAppSpec_TargetApplianceId"));
			int TgtAppSpec_SpecificationId = Integer.valueOf(req.getParameter("TgtAppSpec_SpecificationId"));
			JSONObject retObj=new JSONObject();
			try{
				List<TgtAppSpec> list = (new TgtAppSpecManager()).findByVarProperty(new KeyValueWithOperator("targetAppliance.id",TgtAppSpec_TgtAppId,"="),new KeyValueWithOperator("specification.id",TgtAppSpec_SpecificationId,"="));
				
				if(list==null||list.size()==0)
				{
					TgtAppSpec tgtappspec = new TgtAppSpec();
					TargetAppliance tarApp = (new TargetApplianceManager()).findById(TgtAppSpec_TgtAppId);
					Specification spec = (new SpecificationManager()).findById(TgtAppSpec_SpecificationId);
					tgtappspec.setTargetAppliance(tarApp);
					tgtappspec.setSpecification(spec);
					
					boolean res = (new TgtAppSpecManager()).save(tgtappspec);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"新建成功！":"新建失败，请重新新建！");
				}
				else
				{
					retObj.put("IsOK", false);
					retObj.put("msg", "该关系已存在，无需再次新建！");
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 10", e);
				}else{
					log.error("error in RelationShipServlet-->case 10", e);
				}
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (Exception ex) {}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 11://查询技术规范和受检器具的关系
			JSONObject res11 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<TgtAppSpec> result;
				TgtAppSpecManager tgtapp_spec_Mgr = new TgtAppSpecManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("targetAppliance.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("specification.nameCn", "%" + queryStr + "%", "like");
				
				if(k == null)
				{
					result = tgtapp_spec_Mgr.findPagedAll(page, rows);
					total = tgtapp_spec_Mgr.getTotalCount();
				}
				else
				{
					result = tgtapp_spec_Mgr.findPagedAll(page, rows, k);
					total = tgtapp_spec_Mgr.getTotalCount(k);
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(TgtAppSpec temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getId());
						option.put("TgtApp_Name", temp.getTargetAppliance().getName());
						option.put("TgtApp_StandardName", temp.getTargetAppliance().getApplianceStandardName().getName());
						option.put("TgtApp_Code", temp.getTargetAppliance().getCode());
						option.put("TgtApp_Status", temp.getTargetAppliance().getStatus());
						option.put("Spec_Name", temp.getSpecification().getNameCn());
						option.put("Spec_Code", temp.getSpecification().getSpecificationCode());
						option.put("Spec_Type", temp.getSpecification().getType());
						option.put("Spec_InCharge", temp.getSpecification().getInCharge()?0:1);
						option.put("Spec_LocaleCode", temp.getSpecification().getLocaleCode());
						option.put("Spec_Status", temp.getSpecification().getStatus());
						
						options.put(option);
					}
				}
				res11 = new JSONObject();
				res11.put("total", total);
				res11.put("rows", options);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 11", e);
				}else{
					log.error("error in RelationShipServlet-->case 11", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res11.toString());
			break;
		case 12://删除技术规范和受检器具的关系
			int tgtapp_spec_id = Integer.valueOf(req.getParameter("id"));
			JSONObject ret = new JSONObject();
			try {
				boolean res = (new TgtAppSpecManager()).deleteById(tgtapp_spec_id);
				ret.put("IsOK", res);
				ret.put("msg", res?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 12", e);
				}else{
					log.error("error in RelationShipServlet-->case 12", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret.toString());
			break;
		case 13://查询标准与技术规范的引用关系（两者没有直接关系，通过中间关联——受检器具，故只有查询）
			JSONObject res13 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<Object[]> result;
				//ViewStandardStandardApplianceTargetApplianceSpecificationManager  = new TgtAppStdAppManager();
				StdTgtAppManager stdtgtMgr = new StdTgtAppManager();
				String queryString = "from StdTgtApp as model1, TgtAppSpec as model2 where model1.targetAppliance.id = model2.targetAppliance.id";
				
				StandardManager stdMgr = new StandardManager();
				SpecificationManager specMgr = new SpecificationManager();
				
				if(param.equals("1"))
					queryString += " and model1.standard.name like ?"; 
				else if(param.equals("2"))
					queryString += " and model2.specification.nameCn like ?";
				
				if(!param.equals("1")&&!param.equals("2"))
				{
					result = stdtgtMgr.findPageAllByHQL("select distinct model1.standard.id, model2.specification.id " + queryString, page, rows);
					total = stdtgtMgr.getTotalCountByHQL("select count(distinct model1.standard.id) " + queryString);
				}
				else
				{
					result = stdtgtMgr.findPageAllByHQL("select distinct model1.standard.id, model2.specification.id " + queryString, page, rows, "%" + queryStr + "%");
					total = stdtgtMgr.getTotalCountByHQL("select count(distinct model1.standard.id) " + queryString, "%" + queryStr + "%");
				}
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(Object[] temp : result)
					{
						JSONObject option = new JSONObject();
						int stdid = (Integer)temp[0];
						int specid = (Integer)temp[1];
						Specification spec = specMgr.findById(specid);
						Standard std = stdMgr.findById(stdid);
						option.put("Std_Name", std.getName());
						option.put("Std_CertificateCode", std.getCertificateCode());
						option.put("Std_StandardCode", std.getStandardCode());
						option.put("Std_Status", std.getStatus());
						option.put("Spec_Name", spec.getNameCn());
						option.put("Spec_Code", spec.getSpecificationCode());
						option.put("Spec_Type", spec.getType());
						option.put("Spec_InCharge", spec.getInCharge());
						option.put("Spec_LocaleCode", spec.getLocaleCode());
						option.put("Spec_Status", spec.getStatus());
						
						options.put(option);
					}
				}
				res13.put("total", total);
				res13.put("rows", options); 
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 13", e);
				}else{
					log.error("error in RelationShipServlet-->case 13", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(res13.toString());
			}
			break;
		case 14://查询某一受检器具所引用的标准（仅返回ID和Name）
			int TargetApplianceId = Integer.valueOf(req.getParameter("TargetApplianceId"));
			JSONArray options = new JSONArray();
			StdTgtAppManager stdtgtappMgr = new StdTgtAppManager();
			try{
				List<StdTgtApp> stdTgtappList = stdtgtappMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", TargetApplianceId, "="));
				if(stdTgtappList!=null&&stdTgtappList.size()>0)
				{
					for(StdTgtApp temp : stdTgtappList)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getStandard().getId());
						option.put("name", temp.getStandard().getName());
						
						options.put(option);
					}
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 14", e);
				}else{
					log.error("error in RelationShipServlet-->case 14", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(options.toString());
			}
			break;
		case 15://根据标准查询标准器具（仅返回名称、型号、测量范围、不确定度、出厂等信息）
			String StandardId = req.getParameter("StandardId");
			JSONObject retObj15 = new JSONObject();
			try{
				List<StdStdApp> stdstdappList = null;
				int total = 0;
				KeyValueWithOperator k1 = new KeyValueWithOperator("standard.id", Integer.valueOf(StandardId), "=");
				KeyValueWithOperator k2 = new KeyValueWithOperator("standardAppliance.status", Integer.valueOf(0), "=");
				if(StandardId!=null&&!StandardId.equals(""))
				{
					StdStdAppManager stdstdappMgr = new StdStdAppManager();
					stdstdappList = stdstdappMgr.findByVarProperty(k1, k2);
					total = stdstdappMgr.getTotalCount(k1, k2);
				}
				JSONArray options15 = new JSONArray();
				if(stdstdappList!=null&&stdstdappList.size()>0)
				{
					for(StdStdApp temp : stdstdappList)
					{
						JSONObject option = new JSONObject();
						option.put("Id", temp.getStandardAppliance().getId());
						option.put("Name", temp.getStandardAppliance().getName());
						option.put("Model", temp.getStandardAppliance().getModel());
						option.put("Range", temp.getStandardAppliance().getRange());
						option.put("Uncertain", temp.getStandardAppliance().getUncertain());
						option.put("Release", temp.getStandardAppliance().getManufacturer() + " / " + temp.getStandardAppliance().getSeriaNumber());
						option.put("PermanentCode", temp.getStandardAppliance().getPermanentCode());
						
						options15.put(option);
					}
				}
				retObj15.put("total", total);
				retObj15.put("rows", options15);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 15", e);
				}else{
					log.error("error in RelationShipServlet-->case 15", e);
				}
				try {
					retObj15.put("total", 0);
					retObj15.put("rows", new JSONArray());
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj15.toString());
			}
			break;
		case 16://查询未建立标准-标准器具关系的计量标准
			JSONObject retObj16 = new JSONObject();
			try{
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String queryStr = "from Standard as model where model.id not in (select distinct model1.standard.id from StdStdApp as model1)";
				int total = 0;
				StandardManager stdMgr = new StandardManager();
				List<Standard> result = stdMgr.findPageAllByHQL(queryStr, page, rows);
				total = stdMgr.getTotalCountByHQL("select count(model) " + queryStr);
				JSONArray options16 = new JSONArray();
				if(result!=null&&result.size()>0){
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
						
						options16.put(option);
					}
				}
				retObj16.put("total", total);
				retObj16.put("rows", options16);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 16", e);
				}else{
					log.error("error in RelationShipServlet-->case 16", e);
				}
				try {
					retObj16.put("total", 0);
					retObj16.put("rows", new JSONArray());
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj16.toString());
			}
			break;
		case 17://查询未建立关系的受检器具
			JSONObject retObj17 = new JSONObject();
			try{
				String Type = req.getParameter("Type");
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String queryStr = "from TargetAppliance as model where model.id not in (select distinct model1.targetAppliance.id from ";
				if(Type.equals("1")){
					queryStr += "TgtAppStdApp as model1)";
				}
				else if(Type.equals("2")){
					queryStr += "StdTgtApp as model1)";
				}
				else if(Type.equals("3")){
					queryStr += "TgtAppSpec as model1)";
				}
				int total = 0;
				TargetApplianceManager tgtAppMgr = new TargetApplianceManager();
				List<TargetAppliance> result = tgtAppMgr.findPageAllByHQL(queryStr, page, rows);
				total = tgtAppMgr.getTotalCountByHQL("select count(model) " + queryStr);
				JSONArray options17 = new JSONArray();
				if(result!=null&result.size()>0){
					for (TargetAppliance Target : result) {
						JSONObject option = new JSONObject();
						option.put("Id", Target.getId());
						option.put("Brief", Target.getBrief());
						option.put("StandardNameId", Target.getApplianceStandardName().getId());
						option.put("StandardName", Target.getApplianceStandardName().getName());
						option.put("Name", Target.getName());
						option.put("NameEn", Target.getNameEn());
						option.put("Code", Target.getCode());
						option.put("Fee", Target.getFee());
						option.put("SRFee", Target.getSrfee());
						option.put("MRFee", Target.getMrfee());
						option.put("LRFee", Target.getLrfee());
						option.put("PromiseDuration", Target.getPromiseDuration());
						option.put("TestCycle", Target.getTestCycle());
						option.put("Certification", Target.getCertification() == null ? 0 : Target.getCertification());
						option.put("Status", Target.getStatus());
						option.put("Remark", Target.getRemark());
	
						options17.put(option);
					}
				}
				retObj17.put("total", total);
				retObj17.put("rows", options17);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 17", e);
				}else{
					log.error("error in RelationShipServlet-->case 17", e);
				}
				try {
					retObj17.put("total", 0);
					retObj17.put("rows", new JSONArray());
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj17.toString());
			}
			break;
		case 18://查询所有标准器具与受检器具的关系(编辑原始记录汇中，选择计量标准标准器具和技术规范中用)
			JSONObject res18 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<TgtAppStdApp> result;
				TgtAppStdAppManager tgtapp_stdapp_Mgr5 = new TgtAppStdAppManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("standardAppliance.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("targetAppliance.name", "%" + queryStr + "%", "like");
				
				if(k == null)
				{
					result = tgtapp_stdapp_Mgr5.findPagedAll(page, rows);
					total = tgtapp_stdapp_Mgr5.getTotalCount();
				}
				else
				{
					result = tgtapp_stdapp_Mgr5.findPagedAll(page, rows, k);
					total = tgtapp_stdapp_Mgr5.getTotalCount(k);
				}
				JSONArray options18 = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(TgtAppStdApp temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getStandardAppliance().getId());
						
						option.put("name",temp.getStandardAppliance().getName());;
						option.put("localecode",temp.getStandardAppliance().getLocaleCode());
						option.put("model", temp.getStandardAppliance().getModel());
						option.put("range", temp.getStandardAppliance().getRange());
						option.put("uncertain", temp.getStandardAppliance().getUncertain());
						option.put("release", temp.getStandardAppliance().getManufacturer()+temp.getStandardAppliance().getSeriaNumber());
						option.put("permanentcode", temp.getStandardAppliance().getPermanentCode());
						
						options18.put(option);
					}
				}
				res18 = new JSONObject();
				res18.put("total", total);
				res18.put("rows", options18); 
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 18", e);
				}else{
					log.error("error in RelationShipServlet-->case 18", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res18.toString());
			break;
		case 19://查询所有标准与受检器具的关系(编辑原始记录汇中，选择计量标准标准器具和技术规范中用)
			JSONObject res19 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<StdTgtApp> result;
				StdTgtAppManager std_tgtapp_Mgr8 = new StdTgtAppManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("standard.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("targetAppliance.name", "%" + queryStr + "%", "like");
				//System.out.println(queryStr);
				
				if(k == null)
				{
					result = std_tgtapp_Mgr8.findPagedAll(page, rows);
					total = std_tgtapp_Mgr8.getTotalCount();
				}
				else
				{
					result = std_tgtapp_Mgr8.findPagedAll(page, rows, k);
					total = std_tgtapp_Mgr8.getTotalCount(k);
				}
				JSONArray options19 = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(StdTgtApp temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getStandard().getId());
						
						option.put("name", temp.getStandard().getName());
						option.put("certificatecode", temp.getStandard().getCertificateCode());
						option.put("standardcode", temp.getStandard().getStandardCode());
						option.put("projectcode", temp.getStandard().getProjectCode());
						option.put("issuedby", temp.getStandard().getIssuedBy());
						option.put("issuedate", temp.getStandard().getIssueDate());
						option.put("validdate", temp.getStandard().getValidDate());
						option.put("range", temp.getStandard().getRange());
						option.put("uncertain", temp.getStandard().getUncertain());
						option.put("sissuedby", temp.getStandard().getSissuedBy());
						option.put("scertificatecode", temp.getStandard().getScertificateCode());
						option.put("sissuedate", temp.getStandard().getSissueDate());
						option.put("svaliddate", temp.getStandard().getSvalidDate());
						option.put("region", temp.getStandard().getSregion());
						option.put("sauthorizationcode", temp.getStandard().getSauthorizationCode());
						option.put("slocalecode", temp.getStandard().getSlocaleCode());
						option.put("warnslot", temp.getStandard().getWarnSlot());
						option.put("handler", temp.getStandard().getSysUser().getName());
						option.put("projecttype", temp.getStandard().getProjectType());
						options19.put(option);
					}
				}
				res19 = new JSONObject();
				res19.put("total", total);
				res19.put("rows", options19);
			}
			catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 19", e);
				}else{
					log.error("error in RelationShipServlet-->case 19", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res19.toString());

			break;
		case 20://查询技术规范和受检器具的关系(编辑原始记录汇中，选择计量标准标准器具和技术规范中用)
			JSONObject res20 = new JSONObject();
			try{
				String param = req.getParameter("param");
				String queryStr = req.getParameter("queryStr");
				if(param!=null&&queryStr!=null)
				{
					param = URLDecoder.decode(param, "utf-8");
					queryStr = URLDecoder.decode(queryStr, "utf-8");
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				int total;
				List<TgtAppSpec> result;
				TgtAppSpecManager tgtapp_spec_Mgr = new TgtAppSpecManager();
				KeyValueWithOperator k = null;
				if(param.equals("1"))
					k = new KeyValueWithOperator("targetAppliance.name", "%" + queryStr + "%", "like");
				else if(param.equals("2"))
					k = new KeyValueWithOperator("specification.nameCn", "%" + queryStr + "%", "like");
				
				if(k == null)
				{
					result = tgtapp_spec_Mgr.findPagedAll(page, rows);
					total = tgtapp_spec_Mgr.getTotalCount();
				}
				else
				{
					result = tgtapp_spec_Mgr.findPagedAll(page, rows, k);
					total = tgtapp_spec_Mgr.getTotalCount(k);
				}
				JSONArray options20 = new JSONArray();
				if(result!=null&&result.size()>0)
				{
					for(TgtAppSpec temp : result)
					{
						JSONObject option = new JSONObject();
						option.put("id", temp.getSpecification().getId());
						
						option.put("specificationcode", temp.getSpecification().getSpecificationCode());
						option.put("namecn", temp.getSpecification().getNameCn());
						option.put("type", temp.getSpecification().getType());
						option.put("localecode", temp.getSpecification().getLocaleCode());
						option.put("issuedate", temp.getSpecification().getIssueDate());
						option.put("effectivedate", temp.getSpecification().getEffectiveDate());
						option.put("repealdate", temp.getSpecification().getRepealDate());
						
						options20.put(option);
					}
				}
				res20 = new JSONObject();
				res20.put("total", total);
				res20.put("rows", options20);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RelationShipServlet-->case 20", e);
				}else{
					log.error("error in RelationShipServlet-->case 20", e);
				}
			}
			resp.setContentType("text/json;charset=utf-8");
			resp.getWriter().write(res20.toString());
			break;
		}
	}

}
