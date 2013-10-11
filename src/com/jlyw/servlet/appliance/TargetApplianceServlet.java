package com.jlyw.servlet.appliance;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.jlyw.hibernate.ApplianceAccuracy;
import com.jlyw.hibernate.ApplianceModel;
import com.jlyw.hibernate.ApplianceRange;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.manager.ApplianceAccuracyManager;
import com.jlyw.manager.ApplianceModelManager;
import com.jlyw.manager.ApplianceRangeManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.TargetApplianceManager;
import com.jlyw.manager.quotation.QuotationManager;
import com.jlyw.manager.view.ViewTargetApplianceDetailInfoManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.SystemCfgUtil;

public class TargetApplianceServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(TargetApplianceServlet.class);

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		TargetApplianceManager tarappMgr = new TargetApplianceManager();
		switch (method) {
		case 1:// 新建受检器具
			JSONObject retObj = new JSONObject();
			try {
				String StandardNameId = req.getParameter("StandardNameId");
				if(!LetterUtil.isNumeric(StandardNameId))
				{
					throw new Exception("请选择一条标准名称！");
				}
				TargetAppliance targetappliance = initTargetAppliance(req, 0);
				boolean res = tarappMgr.save(targetappliance);
				retObj.put("IsOK", res);
				retObj.put("msg", res ? "添加成功！" : "添加失败，请重新添加！");
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 1",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 1", e);
				}
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("添加失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2:// 查询受检器具信息
			JSONObject res = new JSONObject();
			try {
				String queryApplianceName = req.getParameter("appname");
				String queryStandardName = req.getParameter("queryStandardName");
				String queryPromiseDuration = req.getParameter("queryPromiseDuration");
				String queryTestCycle = req.getParameter("queryTestCycle");
				String queryCertification = req.getParameter("queryCertification");
				String queryStatus = req.getParameter("queryStatus");
				
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				String queryStr = "from TargetAppliance as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				
				List<TargetAppliance> result;
				int total;
				if (queryApplianceName != null && !queryApplianceName.equals("")) {
					String ApplianceNameStr = URLDecoder.decode(queryApplianceName, "UTF-8");
					queryStr = queryStr + " and (model.name like ? or model.brief like ? or model.nameEn like ?)";
					keys.add("%" + ApplianceNameStr + "%");
					keys.add("%" + ApplianceNameStr + "%");
					keys.add("%" + ApplianceNameStr + "%");
					//System.out.println(ApplianceNameStr);
				}
				if (queryStandardName != null && !queryStandardName.equals("")) {
					String StandardNameStr = URLDecoder.decode(queryStandardName, "UTF-8");
					queryStr = queryStr + " and (model.applianceStandardName.id = ?)";
					keys.add(Integer.valueOf(StandardNameStr));
				}
				if (queryPromiseDuration != null && !queryPromiseDuration.equals("")) {
					String PromiseDurationStr = URLDecoder.decode(queryPromiseDuration, "UTF-8");
					queryStr = queryStr + " and model.promiseDuration = ?";
					keys.add(Integer.valueOf(PromiseDurationStr));
				}
				if (queryTestCycle != null && !queryTestCycle.equals("")) {
					String TestCycleStr = URLDecoder.decode(queryTestCycle, "UTF-8");
					queryStr = queryStr + " and model.testCycle = ?";
					keys.add(LetterUtil.isNumeric(TestCycleStr)?Integer.valueOf(TestCycleStr):0);
				}
				if (queryCertification != null && !queryCertification.equals("")) {
					String CertificationStr = URLDecoder.decode(queryCertification, "UTF-8");
					switch(Integer.parseInt(CertificationStr, 2))
					{
					case 0:
						break;
					case 1:
						queryStr = queryStr + " and (model.certification = ? or model.certification = ? or model.certification = ? or model.certification = ?)";
						keys.add(Integer.valueOf(1));
						keys.add(Integer.valueOf(3));
						keys.add(Integer.valueOf(5));
						keys.add(Integer.valueOf(7));
						break;
					case 2:
						queryStr = queryStr + " and (model.certification = ? or model.certification = ? or model.certification = ? or model.certification = ?)";
						keys.add(Integer.valueOf(2));
						keys.add(Integer.valueOf(3));
						keys.add(Integer.valueOf(6));
						keys.add(Integer.valueOf(7));
						break;
					case 3:
						queryStr = queryStr + " and (model.certification = ? or model.certification = ?)";
						keys.add(Integer.valueOf(3));
						keys.add(Integer.valueOf(7));
						break;
					case 4:
						queryStr = queryStr + " and (model.certification = ? or model.certification = ? or model.certification = ? or model.certification = ?)";
						keys.add(Integer.valueOf(4));
						keys.add(Integer.valueOf(5));
						keys.add(Integer.valueOf(6));
						keys.add(Integer.valueOf(7));
						break;
					case 5:
						queryStr = queryStr + " and (model.certification = ? or model.certification = ?)";
						keys.add(Integer.valueOf(5));
						keys.add(Integer.valueOf(7));
						break;
					case 6:
						queryStr = queryStr + " and (model.certification = ? or model.certification = ?)";
						keys.add(Integer.valueOf(6));
						keys.add(Integer.valueOf(7));
						break;
					case 7:
						queryStr = queryStr + " and model.certification = ?";
						keys.add(Integer.valueOf(7));
						break;
					case 8:
						queryStr = queryStr + " and model.certification = ?";
						keys.add(Integer.valueOf(0));
					}
				}
				if (queryStatus != null && !queryStatus.equals("")) {
					String StatusStr = URLDecoder.decode(queryStatus, "UTF-8");
					queryStr = queryStr + " and model.status = ?";
					keys.add(LetterUtil.isNumeric(StatusStr)?Integer.valueOf(StatusStr):0);
				}
				
				result = tarappMgr.findPageAllByHQL(queryStr + " order by model.status asc, model.applianceStandardName.name asc, model.name asc", page, rows, keys);
				total = tarappMgr.getTotalCountByHQL("select count(*) " + queryStr, keys);
				JSONArray options = new JSONArray();

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

					options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 2",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 2", e);
				}
				try {
					res.put("total", 0);
					res.put("rows", new JSONArray());
				} catch (JSONException ex) {
					ex.printStackTrace();
				}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3:// 修改受检器具信息
			JSONObject retObj1 = new JSONObject();
			try {
				String StandardNameId = req.getParameter("StandardNameId");
				if(!LetterUtil.isNumeric(StandardNameId))
				{
					throw new Exception("请选择一条标准名称！");
				}
				int mod_id = Integer.valueOf(req.getParameter("id"));
				TargetAppliance targetapp = initTargetAppliance(req, mod_id);
				boolean res1 = tarappMgr.update(targetapp);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1 ? "修改成功！" : "修改失败，请重新修改！");
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 3",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 3", e);
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
		case 4:// 注销或删除受检器具
			TargetAppliance tapp = tarappMgr.findById(Integer.valueOf(req.getParameter("id")));
			tapp.setStatus(1);
			JSONObject retObj2 = new JSONObject();
			try {
				boolean res1 = tarappMgr.update(tapp);
				retObj2.put("IsOK", res1);
				retObj2.put("msg", res1 ? "注销成功！" : "注销失败，请重新注销！");
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 4",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 4", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj2.toString());
			break;
		case 5: // 查询一个委托单下所有的受检器具列表-by zhan
			JSONObject retJSON5 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId"); // 委托单Id
				String StandardName = req.getParameter("StandardName");	//标准名
				String Model = req.getParameter("Model");	//型号规格
				String Range = req.getParameter("Range");	//测量范围
				String Accuracy = req.getParameter("Accuracy");	//准确度等级
				String TestFee = req.getParameter("TestFee");//检定费
				String TargetAppName = req.getParameter("TargetAppName");	//受检器具名称

				int page5 = 0; // 当前页面
				if (req.getParameter("page") != null)
					page5 = Integer.parseInt(req.getParameter("page").toString());
				int rows5 = 10; // 页面大小
				if (req.getParameter("rows") != null)
					rows5 = Integer.parseInt(req.getParameter("rows").toString());

				if (CommissionId == null || CommissionId.trim().length() == 0) {
					throw new Exception("委托单未指定！");
				}
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionId));
				if (cSheet == null) {
					throw new Exception("委托单不存在！");
				}
				ViewTargetApplianceDetailInfoManager vTAppDetailMgr = new ViewTargetApplianceDetailInfoManager();
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				if(StandardName != null && StandardName.length() > 0){
					StandardName = URLDecoder.decode(StandardName, "UTF-8");
					condList.add(new KeyValueWithOperator("StandardNameName", "%"+StandardName+"%", "like"));
				}
				if(Model != null && Model.length() > 0){
					Model = URLDecoder.decode(Model, "UTF-8");
					condList.add(new KeyValueWithOperator("Model", "%"+Model+"%", "like"));
				}
				if(Range != null && Range.length() > 0){
					Range = URLDecoder.decode(Range, "UTF-8");
					condList.add(new KeyValueWithOperator("Range", "%"+Range+"%", "like"));
				}
				if(Accuracy != null && Accuracy.length() > 0){
					Accuracy = URLDecoder.decode(Accuracy, "UTF-8");
					condList.add(new KeyValueWithOperator("Accuracy", "%"+Accuracy+"%", "like"));
				}
				if(TestFee != null && TestFee.length() > 0){
					TestFee = URLDecoder.decode(TestFee, "UTF-8");
					condList.add(new KeyValueWithOperator("Fee", Integer.valueOf(TestFee), "="));
				}
				if(TargetAppName != null && TargetAppName.length() > 0){
					TargetAppName = URLDecoder.decode(TargetAppName, "UTF-8");
					condList.add(new KeyValueWithOperator("TargetApplianceName", TargetAppName, "="));
				}
				condList.add(new KeyValueWithOperator("targetApplianceStatus", 1, "<>"));
				List<Object> paramList = new ArrayList<Object>();
				
				int SpeciesType = cSheet.getSpeciesType()?1:0;
				paramList.add(SpeciesType);
				paramList.add(cSheet.getApplianceSpeciesId());
				
				String queryAppSpeString = "with appspe as "+	//缓存表：一个项目下的所有子项目（最末级为：标准名称）
					" ( " +
					" select a.Id, a.Type, a.ParentId, a.Name, a.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as a where a.Status<>1 and a.Type=? and a.Id=? "+
					" union all " +
					" select b.Id, b.Type, b.Parentid, b.Name, b.Status from " + SystemCfgUtil.DBPrexName + "View_ApplianceSpecial_StandardName_PopularName as b, appspe where b.Status<>1 and b.ParentId = appspe.Id and appspe.Type=1 and (b.Type=1 or b.Type=0)" +
					" ) ";
				
				String queryString=" select model.StandardNameId, model.TargetApplianceId, " +
						" model.SpeciesId, model.StandardNameName, model.StandardNameStatus, model.TargetApplianceName, model.TargetApplianceCode, " +
						" model.TargetApplianceStatus, model.TargetApplianceRemark, model.Fee, model.SRFee, model.MRFee, model.LRFee, model.Certification, " +
						" model.ModelId, model.Model, model.AccuracyId, model.Accuracy, model.RangeId, model.Range " +
						" from " + SystemCfgUtil.DBPrexName + "View_TargetAppliance_DetailInfo as model " +
						" where model.StandardNameStatus=0 and model.TargetApplianceStatus = 0 and model.StandardNameId in (select Id from appspe where Type = 0) ";
				for(int i = 0; i < condList.size(); i++){
					KeyValueWithOperator k = condList.get(i);
					queryString += " and model.";
					queryString += k.m_keyName;
					queryString += " " + k.m_operator + " ? ";
					paramList.add(k.m_value);
				}
				

				String countString = " select count(*) from "+ SystemCfgUtil.DBPrexName + "View_TargetAppliance_DetailInfo as model " +
					" where model.StandardNameStatus=0 and model.TargetApplianceStatus = 0 and model.StandardNameId in (select Id from appspe where Type = 0) ";
				for(int i = 0; i < condList.size(); i++){
					KeyValueWithOperator k = condList.get(i);
					countString += " and model.";
					countString += k.m_keyName;
					countString += " " + k.m_operator + " ? ";
				}
				int totalSize = vTAppDetailMgr.getTotalCountBySQL(queryAppSpeString + countString, paramList);
				List<Object[]> vTAppInfoRetList = vTAppDetailMgr.findPageAllBySQL(queryAppSpeString + queryString, page5, rows5, paramList);
				HashMap<Integer,ApplianceStandardName> stdNameMap = new HashMap<Integer, ApplianceStandardName>();
				ApplianceStandardNameManager stdNameMgr = new ApplianceStandardNameManager();
				if (vTAppInfoRetList != null) {
					JSONArray jsonArray = new JSONArray();
					for (Object[] v : vTAppInfoRetList) {
						JSONObject jsonObj = new JSONObject();
						
						//标准名称的模板文件集名称
						if(stdNameMap.containsKey((Integer)v[0])){
							jsonObj.put("StandardNameFilesetName", stdNameMap.get((Integer)v[0]).getFilesetName());
						}else{
							ApplianceStandardName stdName = stdNameMgr.findById((Integer)v[0]);
							if(stdName != null){
								jsonObj.put("StandardNameFilesetName", stdName.getFilesetName());
								stdNameMap.put(stdName.getId(), stdName);
							}else{
								jsonObj.put("StandardNameFilesetName", "");
							}
						}
						
						jsonObj.put("StandardNameId", (Integer)v[0]);
						jsonObj.put("TargetApplianceId", (Integer)v[1]);

						jsonObj.put("SpeciesId", (Integer)v[2]);
						jsonObj.put("StandardNameName", (String)v[3]);
						jsonObj.put("StandardNameStatus", (Integer)v[4]);
						jsonObj.put("TargetApplianceName", (String)v[5]);
						jsonObj.put("TargetApplianceCode", (String)v[6]);
						jsonObj.put("TargetApplianceStatus", (Integer)v[7]);
						jsonObj.put("TargetApplianceRemark", (String)v[8]);
						jsonObj.put("Fee", v[9] == null ? "" : (Double)v[9]);
						jsonObj.put("SRFee", v[10] == null ? "" : (Double)v[10]);
						jsonObj.put("MRFee", v[11] == null ? "" : (Double)v[11]);
						jsonObj.put("LRFee", v[12] == null ? "" : (Double)v[12]);
						jsonObj.put("Certification",v[13] == null ? "" : (Integer)v[13]);

						jsonObj.put("ModelId",v[14] == null ? "" : (Integer)v[14]);
						jsonObj.put("Model", v[15] == null ? "" : (String)v[15]);
						jsonObj.put("AccuracyId",v[16] == null ? "" : (Integer)v[16]);
						jsonObj.put("Accuracy", v[17] == null ? "" : (String)v[17]);
						jsonObj.put("RangeId", v[18] == null ? "" : (Integer)v[18]);
						jsonObj.put("Range", v[19] == null ? "" : (String)v[19]);
						jsonArray.put(jsonObj);
					}

					retJSON5.put("total", totalSize);
					retJSON5.put("rows", jsonArray);
				} else {
					throw new Exception("");
				}
			} catch (Exception e) {
				try {
					retJSON5.put("total", 0);
					retJSON5.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 5",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 5", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6://受检器具combobox模糊查询，返回前30个
			JSONArray jsonArray2 = new JSONArray();
			try {
				String TgtAppNameStr = req.getParameter("TgtAppName");
				if (TgtAppNameStr != null && TgtAppNameStr.trim().length() > 0) {
					String TgtAppName = new String(TgtAppNameStr.trim().getBytes("ISO-8859-1"), "GBK"); // 解决URL传递中文乱码问题
					TgtAppName = LetterUtil.String2Alpha(TgtAppName); // 转换成拼音简码
					String[] queryName = TgtAppName.split(" \\s+"); // 根据空格符分割
					if (queryName.length == 0) {
						return;
					}
					TgtAppName = "";
					for (int i = 0; i < queryName.length; i++) {
						TgtAppName += queryName[i];
						if (i != queryName.length - 1)
							TgtAppName += "%";
					}
					TgtAppName = "%" + TgtAppName + "%";
					String queryString = String.format("select model.id, model.name from TargetAppliance as model where model.brief like ?");
					String status = req.getParameter("Status");
					List<Object> keys = new ArrayList<Object>();
					keys.add(TgtAppName);
					if(status != null && status.length() > 0){
						queryString = queryString + " and model.status = ?";
						String statusStr = new String(status.getBytes("ISO-8859-1"), "GBK");
						keys.add(Integer.valueOf(statusStr));
					}
					List<Object[]> retList = tarappMgr.findPageAllByHQL(queryString, 1, 30, keys);
					if (retList != null) {
						for (Object[] objArray : retList) {
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", (String) objArray[1]);
							jsonObj.put("id", (Integer) objArray[0]);
							jsonArray2.put(jsonObj);
						}
					}
				}
			} catch (Exception e) {
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 6",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 6", e);
				}
			} finally {
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
		case 7:// 根据受检器具ID查询型号规格、不确定度、测量范围(根据Type判断查询哪个)
			int TargetAppId = Integer.valueOf(req.getParameter("TargetAppId"));
			int Type = Integer.valueOf(req.getParameter("Type"));// 1：型号规格，2：不确定度，3：测量范围
			KeyValueWithOperator k = new KeyValueWithOperator(
					"targetAppliance.id", TargetAppId, "=");
			JSONObject ret = new JSONObject();
			try {
				int total1 = 0;
				JSONArray array = new JSONArray();
				int page7 = 1;
				if (req.getParameter("page") != null)
					page7 = Integer.parseInt(req.getParameter("page")
							.toString());
				int rows7 = 10;
				if (req.getParameter("rows") != null)
					rows7 = Integer.parseInt(req.getParameter("rows")
							.toString());
				switch (Type) {
				case 1:
					ApplianceModelManager appModelMgr = new ApplianceModelManager();
					List<ApplianceModel> modelList = appModelMgr.findPagedAll(
							page7, rows7, k);
					total1 = appModelMgr.getTotalCount(k);
					for (ApplianceModel model : modelList) {
						JSONObject option = new JSONObject();
						option.put("Id", model.getId());
						option.put("TargetAppliance", model.getTargetAppliance().getName());
						option.put("Model", model.getModel());
						option.put("ModelEn", model.getModelEn());

						array.put(option);
					}
					break;
				case 2:
					ApplianceAccuracyManager appAccuracyMgr = new ApplianceAccuracyManager();
					List<ApplianceAccuracy> accuracyList = appAccuracyMgr
							.findPagedAll(page7, rows7, k);
					total1 = appAccuracyMgr.getTotalCount(k);
					for (ApplianceAccuracy accuracy : accuracyList) {
						JSONObject option = new JSONObject();
						option.put("Id", accuracy.getId());
						option.put("TargetAppliance", accuracy.getTargetAppliance().getName());
						option.put("Accuracy", accuracy.getAccuracy());
						option.put("AccuracyEn", accuracy.getAccuracyEn());

						array.put(option);
					}
					break;
				case 3:
					ApplianceRangeManager appRangeMgr = new ApplianceRangeManager();
					List<ApplianceRange> rangeList = appRangeMgr.findPagedAll(
							page7, rows7, k);
					total1 = appRangeMgr.getTotalCount(k);
					for (ApplianceRange range : rangeList) {
						JSONObject option = new JSONObject();
						option.put("Id", range.getId());
						option.put("TargetAppliance", range.getTargetAppliance().getName());
						option.put("Range", range.getRange());
						option.put("RangeEn", range.getRangeEn());

						array.put(option);
					}
					break;
				}
				ret.put("total", total1);
				ret.put("rows", array);
			} catch (Exception e) {
				try {
					ret.put("total", 0);
					ret.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 7",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 7", e);
				}
			} finally {
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 8:// 根据受检器具ID新建型号规格、不确定度、测量范围的记录
			int Type8 = Integer.valueOf(req.getParameter("Type"));// 1：型号规格，2：不确定度，3：测量范围
			int TargetAppId8 = Integer.valueOf(req.getParameter("TargetAppId"));
			TargetAppliance targetApp = tarappMgr.findById(TargetAppId8);
			JSONObject res8 = new JSONObject();
			try {
				switch (Type8) {
				case 1:
					ApplianceModelManager appModelMgr = new ApplianceModelManager();
					ApplianceModel appModel = new ApplianceModel();
					String Model = req.getParameter("Model");
					String ModelEn = req.getParameter("ModelEn");
					appModel.setTargetAppliance(targetApp);
					appModel.setModel(Model);
					appModel.setModelEn(ModelEn);
					List<ApplianceModel> check1 = appModelMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", targetApp.getId(), "="),new KeyValueWithOperator("model", Model, "="));
					if(check1==null||check1.size()==0){
						boolean res1 = appModelMgr.save(appModel);
						res8.put("IsOK", res1);
						res8.put("msg", res1 ? "添加成功！" : "添加失败，请重新添加！");
					}
					else{
						throw new Exception("该型号规格已存在！");
					}
					break;
				case 2:
					ApplianceAccuracyManager appAccuracyMgr = new ApplianceAccuracyManager();
					ApplianceAccuracy appAccuracy = new ApplianceAccuracy();
					String Accuracy = req.getParameter("Accuracy");
					String AccuracyEn = req.getParameter("AccuracyEn");
					appAccuracy.setTargetAppliance(targetApp);
					appAccuracy.setAccuracy(Accuracy);
					appAccuracy.setAccuracyEn(AccuracyEn);
					List<ApplianceAccuracy> check2 = appAccuracyMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", targetApp.getId(), "="),new KeyValueWithOperator("accuracy", Accuracy, "="));
					if(check2==null||check2.size()==0){
						boolean res2 = appAccuracyMgr.save(appAccuracy);
						res8.put("IsOK", res2);
						res8.put("msg", res2 ? "添加成功！" : "添加失败，请重新添加！");
					}
					else{
						throw new Exception("该准确度等级已存在！");
					}
					break;
				case 3:
					ApplianceRangeManager appRangeMgr = new ApplianceRangeManager();
					ApplianceRange appRange = new ApplianceRange();
					String Range = req.getParameter("Range");
					String RangeEn = req.getParameter("RangeEn");
					appRange.setTargetAppliance(targetApp);
					appRange.setRange(Range);
					appRange.setRangeEn(RangeEn);
					List<ApplianceRange> check3 = appRangeMgr.findByVarProperty(new KeyValueWithOperator("targetAppliance.id", targetApp.getId(), "="),new KeyValueWithOperator("range", Range, "="));
					if(check3==null||check3.size()==0){
						boolean res3 = appRangeMgr.save(appRange);
						res8.put("IsOK", res3);
						res8.put("msg", res3 ? "添加成功！" : "添加失败，请重新添加！");
					}
					else{
						throw new Exception("该测量范围已存在！");
					}
					break;
				}
			} catch (Exception e) {
				try {
					res8.put("IsOK", false);
					res8.put("msg", String.format("添加失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 8",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 8", e);
				}
			} finally {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(res8.toString());
			}
			break;
		case 9:// 根据ID修改型号规格、不确定度、测量范围的记录
			int Type9 = Integer.valueOf(req.getParameter("Type"));// 1：型号规格，2：不确定度，3：测量范围
			int id = Integer.valueOf(req.getParameter("id"));
			JSONObject res9 = new JSONObject();
			try {
				switch (Type9) {
				case 1:
					ApplianceModelManager appModelMgr = new ApplianceModelManager();
					ApplianceModel appModel = appModelMgr.findById(id);
					String Model = req.getParameter("Model");
					String ModelEn = req.getParameter("ModelEn");
					appModel.setModel(Model);
					appModel.setModelEn(ModelEn);
					boolean res1 = appModelMgr.update(appModel);
					res9.put("IsOK", res1);
					res9.put("msg", res1 ? "修改成功！" : "修改失败，请重新修改！");
					break;
				case 2:
					ApplianceAccuracyManager appAccuracyMgr = new ApplianceAccuracyManager();
					ApplianceAccuracy appAccuracy = appAccuracyMgr.findById(id);
					String Accuracy = req.getParameter("Accuracy");
					String AccuracyEn = req.getParameter("AccuracyEn");
					appAccuracy.setAccuracy(Accuracy);
					appAccuracy.setAccuracyEn(AccuracyEn);
					boolean res2 = appAccuracyMgr.update(appAccuracy);
					res9.put("IsOK", res2);
					res9.put("msg", res2 ? "修改成功！" : "修改失败，请重新修改！");
					break;
				case 3:
					ApplianceRangeManager appRangeMgr = new ApplianceRangeManager();
					ApplianceRange appRange = appRangeMgr.findById(id);
					String Range = req.getParameter("Range");
					String RangeEn = req.getParameter("RangeEn");
					appRange.setRange(Range);
					appRange.setRangeEn(RangeEn);
					boolean res3 = appRangeMgr.update(appRange);
					res9.put("IsOK", res3);
					res9.put("msg", res3 ? "修改成功！" : "修改失败，请重新修改！");
					break;
				}
			} catch (Exception e) {
				try {
					res9.put("IsOK", false);
					res9.put("msg", "修改失败，请重新修改！");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 9",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 9", e);
				}
			} finally {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(res9.toString());
			}
			break;
		case 10:// 根据ID删除型号规格、不确定度、测量范围的记录
			int Type10 = Integer.valueOf(req.getParameter("Type"));// 1：型号规格，2：不确定度，3：测量范围
			String idStr = req.getParameter("idStr");
			String[] ids = idStr.split("\\|");
			JSONObject res10 = new JSONObject();
			try {
				switch (Type10) {
				case 1:
					ApplianceModelManager appModelMgr = new ApplianceModelManager();
					List<ApplianceModel> models = new ArrayList<ApplianceModel>();
					for(int i = 0; i < ids.length; i++){
						ApplianceModel model = appModelMgr.findById(Integer.valueOf(ids[i]));
						models.add(model);
					}
					boolean res1 = appModelMgr.deleteByBatch(models);
					res10.put("IsOK", res1);
					res10.put("msg", res1 ? "删除成功！" : "删除失败，请重新删除！");
					break;
				case 2:
					ApplianceAccuracyManager appAccuracyMgr = new ApplianceAccuracyManager();
					List<ApplianceAccuracy> accuracys = new ArrayList<ApplianceAccuracy>();
					for(int i = 0; i < ids.length; i++){
						ApplianceAccuracy accuracy = appAccuracyMgr.findById(Integer.valueOf(ids[i]));
						accuracys.add(accuracy);
					}
					boolean res2 = appAccuracyMgr.deleteByBatch(accuracys);
					res10.put("IsOK", res2);
					res10.put("msg", res2 ? "删除成功！" : "删除失败，请重新删除！");
					break;
				case 3:
					ApplianceRangeManager appRangeMgr = new ApplianceRangeManager();
					List<ApplianceRange> ranges = new ArrayList<ApplianceRange>();
					for(int i = 0; i < ids.length; i++){
						ApplianceRange range = appRangeMgr.findById(Integer.valueOf(ids[i]));
						ranges.add(range);
					}
					boolean res3 = appRangeMgr.deleteByBatch(ranges);
					res10.put("IsOK", res3);
					res10.put("msg", res3 ? "删除成功！" : "删除失败，请重新删除！");
					break;
				}
			} catch (Exception e) {
				try {
					res10.put("IsOK", false);
					res10.put("msg", "删除失败，请重新删除！");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 10",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 10", e);
				}
			} finally {
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(res10.toString());
			}
			break;
		case 11://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj11 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "select model, (select max(ma.model) from ApplianceModel ma where ma.targetAppliance.id = model.id) as appModel, (select count(mb) from ApplianceModel mb where mb.targetAppliance.id = model.id) as modelCount, " +
				"(select max(ra.range) from ApplianceRange ra where ra.targetAppliance.id = model.id) as appRange, (select count(rb) from ApplianceModel rb where rb.targetAppliance.id = model.id) as rangeCount, " +
				"(select max(aa.accuracy) from ApplianceAccuracy aa where aa.targetAppliance.id = model.id) as appAccuracy, (select count(ab) from ApplianceAccuracy ab where ab.targetAppliance.id = model.id) as accuracyCount from TargetAppliance as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String queryApplianceName = params.getString("appname");
					String queryStandardName = params.getString("queryStandardName");
					String queryPromiseDuration = params.getString("queryPromiseDuration");
					String queryTestCycle = params.getString("queryTestCycle");
					String queryCertification = params.getString("queryCertification");
					String queryStatus = params.getString("queryStatus");
					
					if (queryApplianceName != null && !queryApplianceName.equals("")) {
						String ApplianceNameStr = URLDecoder.decode(queryApplianceName, "UTF-8");
						queryStr = queryStr + " and (model.name like ? or model.brief like ? or model.nameEn like ?)";
						keys.add("%" + ApplianceNameStr + "%");
						keys.add("%" + ApplianceNameStr + "%");
						keys.add("%" + ApplianceNameStr + "%");
					}
					if (queryStandardName != null && !queryStandardName.equals("")) {
						String StandardNameStr = URLDecoder.decode(queryStandardName, "UTF-8");
						queryStr = queryStr + " and (model.applianceStandardName.id = ? or model.applianceStandardName.brief like ?)";
						keys.add(LetterUtil.isNumeric(StandardNameStr)?Integer.valueOf(StandardNameStr):0);
						keys.add("%" + StandardNameStr + "%");
					}
					if (queryPromiseDuration != null && !queryPromiseDuration.equals("")) {
						String PromiseDurationStr = URLDecoder.decode(queryPromiseDuration, "UTF-8");
						queryStr = queryStr + " and model.promiseDuration = ?";
						keys.add(Integer.valueOf(PromiseDurationStr));
					}
					if (queryTestCycle != null && !queryTestCycle.equals("")) {
						String TestCycleStr = URLDecoder.decode(queryTestCycle, "UTF-8");
						queryStr = queryStr + " and model.testCycle = ?";
						keys.add(LetterUtil.isNumeric(TestCycleStr)?Integer.valueOf(TestCycleStr):0);
					}
					if (queryCertification != null && !queryCertification.equals("")) {
						String CertificationStr = URLDecoder.decode(queryCertification, "UTF-8");
						switch(Integer.parseInt(CertificationStr, 2))
						{
						case 0:
							break;
						case 1:
							queryStr = queryStr + " and (model.certification = ? or model.certification = ? or model.certification = ? or model.certification = ?)";
							keys.add(Integer.valueOf(1));
							keys.add(Integer.valueOf(3));
							keys.add(Integer.valueOf(5));
							keys.add(Integer.valueOf(7));
							break;
						case 2:
							queryStr = queryStr + " and (model.certification = ? or model.certification = ? or model.certification = ? or model.certification = ?)";
							keys.add(Integer.valueOf(2));
							keys.add(Integer.valueOf(3));
							keys.add(Integer.valueOf(6));
							keys.add(Integer.valueOf(7));
							break;
						case 3:
							queryStr = queryStr + " and (model.certification = ? or model.certification = ?)";
							keys.add(Integer.valueOf(3));
							keys.add(Integer.valueOf(7));
							break;
						case 4:
							queryStr = queryStr + " and (model.certification = ? or model.certification = ? or model.certification = ? or model.certification = ?)";
							keys.add(Integer.valueOf(4));
							keys.add(Integer.valueOf(5));
							keys.add(Integer.valueOf(6));
							keys.add(Integer.valueOf(7));
							break;
						case 5:
							queryStr = queryStr + " and (model.certification = ? or model.certification = ?)";
							keys.add(Integer.valueOf(5));
							keys.add(Integer.valueOf(7));
							break;
						case 6:
							queryStr = queryStr + " and (model.certification = ? or model.certification = ?)";
							keys.add(Integer.valueOf(6));
							keys.add(Integer.valueOf(7));
							break;
						case 7:
							queryStr = queryStr + " and model.certification = ?";
							keys.add(Integer.valueOf(7));
							break;
						case 8:
							queryStr = queryStr + " and model.certification = ?";
							keys.add(Integer.valueOf(0));
						}
					}
					if (queryStatus != null && !queryStatus.equals("")) {
						String StatusStr = URLDecoder.decode(queryStatus, "UTF-8");
						queryStr = queryStr + " and model.status = ?";
						keys.add(LetterUtil.isNumeric(StatusStr)?Integer.valueOf(StatusStr):0);
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr + " order by model.status asc, model.applianceStandardName.name asc, model.name asc", keys, null, "formatExcel", "formatTitle", TargetApplianceManager.class);
				retObj11.put("IsOK", filePath.equals("")?false:true);
				retObj11.put("Path", filePath);
			}catch(Exception e){
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 11",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 11", e);
				}
				try {
					retObj11.put("IsOK", false);
					retObj11.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj11.toString());
			}
			
			break;
		case 12:// 根据器具标准名称查询型号规格、不确定度、测量范围(Combobox)
			
			JSONArray array = new JSONArray();
			try {
				//int targetAppId = Integer.valueOf(req.getParameter("TargetAppId"));
				String standardNameName = req.getParameter("standardNameName");
				
				standardNameName = new String(standardNameName.getBytes("ISO-8859-1"), "UTF-8");
				
				int type = Integer.valueOf(req.getParameter("Type"));// 1：型号规格，2：不确定度，3：测量范围
				//KeyValueWithOperator key = new KeyValueWithOperator("targetAppliance.applianceStandardName.name", targetAppId, "=");
				switch (type) {
				case 1:
					String queryStr = "select distinct (a.model) from ApplianceModel as a where a.targetAppliance.applianceStandardName.name = ?";
					ApplianceModelManager appModelMgr = new ApplianceModelManager();
					List<Object> modelList = appModelMgr.findPageAllByHQL(queryStr, 1,30,standardNameName);
				
					for (Object model : modelList) {
						JSONObject option = new JSONObject();					
						option.put("Name", model.toString());
						array.put(option);
					}
					break;
				case 2:
					String queryStr1 = "select distinct (a.accuracy) from ApplianceAccuracy as a where a.targetAppliance.applianceStandardName.name = ?";
					ApplianceAccuracyManager appAccuracyMgr = new ApplianceAccuracyManager();
					List<Object> accuracyList = appAccuracyMgr.findPageAllByHQL(queryStr1,1,30, standardNameName);
					
					for (Object accuracy : accuracyList) {
						JSONObject option = new JSONObject();				
						option.put("Name", accuracy.toString());					
						array.put(option);
					}
					break;
				case 3:
					String queryStr2 = "select distinct (a.range) from ApplianceRange as a where a.targetAppliance.applianceStandardName.name = ?";
					ApplianceRangeManager appRangeMgr = new ApplianceRangeManager();
					List<Object> rangeList = appRangeMgr.findPageAllByHQL(queryStr2,1,30, standardNameName);
				
					for (Object range : rangeList) {
						JSONObject option = new JSONObject();
						option.put("Name", range.toString());
						array.put(option);
					}
					break;
				}
				
			} catch (Exception e) {
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 12",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 12", e);
				}
			} finally {
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(array.toString());
			}
			break;
		case 13:// 根据受检器具，型号规格、测量范围、准确度等级之类的查找检测费最大最小值。
			JSONObject result = new JSONObject();
			try {
				String standardNameName = req.getParameter("Application");//受检器具标准名称
				String model = req.getParameter("Model");// 1：型号规格，2：不确定度，3：测量范围
				String accuracy = req.getParameter("Accuracy");
				String range = req.getParameter("Range");
				
				List<Object> keys = new ArrayList<Object>();
				String querySQL="select model.Id from "+SystemCfgUtil.DBPrexName+"ApplianceStandardName as model where model.Name=? ";
								
				List<Object> standnameresult = (new QuotationManager()).findBySQL(querySQL,URLDecoder.decode(standardNameName.trim(), "UTF-8"));
				
				if((standnameresult!=null)&&standnameresult.size()==1){							
					result.put("StandardNameId", (Integer)standnameresult.get(0));
					ApplianceStandardName appStandardName=(new ApplianceStandardNameManager()).findById(Integer.parseInt(result.getString("StandardNameId")));
					result.put("StandardName", appStandardName.getName());
										
					String queryStr="select Min(t.fee) as min,Max(t.fee) as max from TargetAppliance as t where t.applianceStandardName.name = ? and t.status = 0 and t.applianceStandardName.status=0";
					keys.add(URLDecoder.decode(standardNameName, "UTF-8"));
					
					if(model!=null&&model.trim().length()>0){
						queryStr = queryStr+ " and t.id in (select m.targetAppliance.id from ApplianceModel as m where m.targetAppliance.id = t.id and m.model = ?)";
						keys.add(URLDecoder.decode(model, "UTF-8"));
					}
					if(accuracy!=null&&accuracy.trim().length()>0){
						queryStr = queryStr+ " and t.id in (select a.targetAppliance.id from ApplianceAccuracy as a where a.targetAppliance.id = t.id and a.accuracy = ?)";
						keys.add(URLDecoder.decode(accuracy, "UTF-8"));
					}
					//if(range!=null&&range.trim().length()>0){
					if(range!=null&&range.trim().length()>0){

						queryStr = queryStr+ " and t.id in (select r.targetAppliance.id from ApplianceRange as r where r.targetAppliance.id = t.id and r.range = ?)";
						keys.add(URLDecoder.decode(range, "UTF-8"));
					}
					List<Object[]> list = tarappMgr.findByHQL(queryStr,keys);
					
					if(list!=null&&list.size()>0){					
						Object[] obj=list.get(0);		
						
						result.put("MinFee",obj[0]==null?"":(Double)obj[0]);
						result.put("MaxFee",obj[1]==null?"":(Double)obj[1]);											
					}else{
						
						result.put("MinFee",0);
						result.put("MaxFee",0);					
						
					}
			
				}else{
					result.put("StandardNameId","");
					result.put("StandardName","");
					
					result.put("MinFee","");
					result.put("MaxFee","");	
				}
				result.put("IsOK", true);
				result.put("msg", "成功");
				
			} catch (Exception e) {
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in TargetApplianceServlet-->case 13",e);
				} else {
					log.error("error in TargetApplianceServlet-->case 13", e);
				}
				try {
					result.put("IsOK", false);
					result.put("msg", String.format("验证失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				} catch (JSONException e1) {}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(result.toString());
			}
			break;
		}
	}

	private TargetAppliance initTargetAppliance(HttpServletRequest req, int id) {
		TargetAppliance tarapp;
		if (id == 0) {
			tarapp = new TargetAppliance();
		} else {
			tarapp = (new TargetApplianceManager()).findById(id);
		}

		String Name = req.getParameter("Name");
		String NameEn = req.getParameter("NameEn");
		String Brief = req.getParameter("Brief");
		String StandardNameId = req.getParameter("StandardNameId");
		String Code = req.getParameter("Code");
		String Fee = req.getParameter("Fee");
		String SRFee = req.getParameter("SRFee");
		String MRFee = req.getParameter("MRFee");
		String LRFee = req.getParameter("LRFee");
		String PromiseDuration = req.getParameter("PromiseDuration");
		String TestCycle = req.getParameter("TestCycle");
		String cer1 = req.getParameter("cer1");
		String cer2 = req.getParameter("cer2");
		String cer3 = req.getParameter("cer3");
		String Remark = req.getParameter("Remark");

		tarapp.setName(Name);
		tarapp.setNameEn(NameEn);
		ApplianceStandardName appstanname = (new ApplianceStandardNameManager()).findById(Integer.valueOf(StandardNameId));
		tarapp.setApplianceStandardName(appstanname);
		tarapp.setBrief(Brief);
		tarapp.setCode(Code);
		if (Fee != null)
			tarapp.setFee(Double.valueOf(Fee));
		if (SRFee != null && !SRFee.equals(""))
			tarapp.setSrfee(Double.valueOf(SRFee));
		if (MRFee != null && !MRFee.equals(""))
			tarapp.setMrfee(Double.valueOf(MRFee));
		if (LRFee != null && !LRFee.equals(""))
			tarapp.setLrfee(Double.valueOf(LRFee));
		if (PromiseDuration != null && !PromiseDuration.equals(""))
			tarapp.setPromiseDuration(Integer.valueOf(PromiseDuration));
		if (TestCycle != null && !TestCycle.equals(""))
			tarapp.setTestCycle(Integer.valueOf(TestCycle));
		String Certification = "";
		Certification += cer1 == null ? "0" : "1";
		Certification += cer2 == null ? "0" : "1";
		Certification += cer3 == null ? "0" : "1";
		tarapp.setCertification(Integer.parseInt(Certification, 2));
		tarapp.setRemark(Remark);
		tarapp.setStatus(0);

		return tarapp;
	}

}
