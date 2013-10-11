package com.jlyw.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.bson.types.ObjectId;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.Quotation;
import com.jlyw.hibernate.QuotationItem;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.view.ViewApplianceSpecialStandardNamePopularName;
import com.jlyw.hibernate.view.ViewTargetApplianceDetailInfo;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.TargetApplianceManager;
import com.jlyw.manager.quotation.QuotationItemManager;
import com.jlyw.manager.quotation.QuotationManager;
import com.jlyw.manager.view.ViewTargetApplianceDetailInfoManager;
import com.jlyw.util.CommissionSheetFlagUtil;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.SysStringUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UploadDownLoadUtil;
import com.jlyw.util.mongodbService.DBPoolManager;
import com.jlyw.util.mongodbService.MongoService;
import com.jlyw.util.mongodbService.MongoServiceImpl;
import com.mongodb.gridfs.GridFSDBFile;

public class QuotationServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(QuotationServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		QuotationManager quotationMgr = new QuotationManager();
		QuotationItemManager quotationItemMgr = new QuotationItemManager();
		switch (method) {	
		case 0://查询受检器具
			JSONObject retJson0=new JSONObject();
			try{
				String ApplianceName = req.getParameter("appname");
				List<Object> condList = new ArrayList<Object>();
				if(ApplianceName==null)
					ApplianceName="";
				else{
					ApplianceName=URLDecoder.decode(ApplianceName, "UTF-8");
					//ApplianceName = LetterUtil.String2Alpha(ApplianceName);	//返回字母简码
				}
				int page = 1;
				if(req.getParameter("page") != null){
					page = Integer.parseInt(req.getParameter("page").toString());
				}
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				ViewTargetApplianceDetailInfoManager  vTAppDetailMgr = new ViewTargetApplianceDetailInfoManager();
				List<ViewTargetApplianceDetailInfo> vTAppInfoRetList;
				int total;
				
				condList.add("%" + ApplianceName + "%");
				condList.add("%" + ApplianceName + "%");
				String queryStr = "from ViewTargetApplianceDetailInfo as model where (model.standardNameBrief like ? or model.standardNameName like ?) and model.standardNameStatus <> 1 and model.targetApplianceStatus <> 1";
				

				//condList.add(new KeyValueWithOperator("standardNameStatus", 1, "<>"));
				//condList.add(new KeyValueWithOperator("targetApplianceStatus", 1, "<>"));
				//condList.add(new KeyValueWithOperator("standardNameBrief", "%" + ApplianceName + "%", "like"));
				vTAppInfoRetList = vTAppDetailMgr.findPageAllByHQL(queryStr + " order by model.id.targetApplianceId desc", page,rows,condList);
				total = vTAppDetailMgr.getTotalCountByHQL("select count(*) " + queryStr, condList);
					
				//System.out.println(total +"   "+vTAppInfoRetList.size());
				
				if(vTAppInfoRetList != null && vTAppInfoRetList.size() > 0){
					
					JSONArray jsonArray = new JSONArray();
					for(ViewTargetApplianceDetailInfo v:vTAppInfoRetList){
						
							JSONObject jsonObj = new JSONObject();
	
							jsonObj.put("TargetApplianceId", v.getId()==null?"":v.getId().getTargetApplianceId());	
							jsonObj.put("TargetApplianceName", v.getTargetApplianceName()==null?"":v.getTargetApplianceName());
							jsonObj.put("StandardNameid", v.getId().getStandardNameId());	
							jsonObj.put("StandardNameName", v.getStandardNameName());					
							jsonObj.put("Fee", v.getFee()==null?"":v.getFee());
							jsonObj.put("ModelId", v.getId().getModelId()==null?"":v.getId().getModelId());
							jsonObj.put("Model", v.getModel()==null?"":v.getModel());
							jsonObj.put("AccuracyId", v.getId().getAccuracyId()==null?"":v.getId().getAccuracyId());
							jsonObj.put("Accuracy", v.getAccuracy()==null?"":v.getAccuracy());
							jsonObj.put("RangeId", v.getId().getRangeId()==null?"":v.getId().getRangeId());
							jsonObj.put("Range", v.getRange()==null?"":v.getRange());
							jsonArray.put(jsonObj);
						
					}
					retJson0.put("total", total);
					retJson0.put("rows", jsonArray);
				}else{
					throw new Exception("");
				}	
				
			}catch(Exception e){
				e.printStackTrace();
				try {
					retJson0.put("total", 0);
					retJson0.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 0", e);
				}else{
					log.error("error in QuotationServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJson0.toString());
				//System.out.println(retJson0.toString());
			}
			break;
			
		case 1://新增报价单和报价单条目
			Quotation quotation = initQuotation(req,null);
			JSONObject retObj=new JSONObject();
			try {
				
				String Quotationitem = req.getParameter("Item");
				List<QuotationItem> quotationItemList=new ArrayList();
				JSONArray JsonArray = new JSONArray(Quotationitem);
				
				for(int i=0; i<JsonArray.length(); i++){//生成报价单条目的List
					JSONObject jsonObj = JsonArray.getJSONObject(i);
					QuotationItem quotationItem = new QuotationItem();					
					//quotationItem.setQuotation(quotation);					
					quotationItem.setCertificateName(jsonObj.getString("CertificateName"));
					//System.out.println(jsonObj.getString("CertificateName"));
					if(jsonObj.has("StandardNameId") && jsonObj.getString("StandardNameId")!=null && jsonObj.getString("StandardNameId").length()>0){
						ApplianceStandardName applianceStandardName=new ApplianceStandardName();
						applianceStandardName.setId(Integer.parseInt(jsonObj.getString("StandardNameId")));
						quotationItem.setApplianceStandardName(applianceStandardName);
					}
					quotationItem.setStandardName(jsonObj.getString("StandardName"));
					quotationItem.setModel(jsonObj.getString("Model"));
					quotationItem.setAccuracy(jsonObj.getString("Accuracy"));
					quotationItem.setRange(jsonObj.getString("Range"));
					if(jsonObj.getString("Quantity")==null||jsonObj.getString("Quantity").length()==0)
						quotationItem.setQuantity(1);
					else
						quotationItem.setQuantity(Integer.parseInt(jsonObj.getString("Quantity")));
					quotationItem.setCertType(jsonObj.getString("CertType"));
					
					quotationItem.setAppFactoryCode(jsonObj.getString("AppFactoryCode"));
					quotationItem.setAppManageCode(jsonObj.getString("AppManageCode"));
					quotationItem.setManufacturer(jsonObj.getString("Manufacturer"));
					
					quotationItem.setSiteTest(InttoBool(Integer.parseInt(jsonObj.getString("SiteTest"))));
					if(jsonObj.has("MinCost")&&jsonObj.getString("MinCost")!=null&&jsonObj.getString("MinCost").length()>0)
						quotationItem.setMinCost(jsonObj.getString("MinCost"));
					else
						quotationItem.setMinCost("");
					if(jsonObj.has("MaxCost")&&jsonObj.getString("MaxCost")!=null&&jsonObj.getString("MaxCost").length()>0)
						quotationItem.setMaxCost(jsonObj.getString("MaxCost"));
					else
						quotationItem.setMaxCost("");
					
					quotationItem.setRemark(jsonObj.getString("Remark"));
					
					quotationItem.setXh((i+1)*10);
					quotationItemList.add(quotationItem);
						
				}
				
				if(!quotationItemMgr.saveByBatch(quotationItemList, quotation)){//新建报价单，批量添加报价单条目
					throw new Exception("保存委托单条目失败");
				}
				retObj.put("IsOK", true);
				retObj.put("msg", "新建报价单成功！");	
				
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 1", e);
				}else{
					log.error("error in QuotationServlet-->case 1", e);
				}
				try{
					retObj.put("IsOK", false);
					retObj.put("msg" ,String.format("新建报价单失败！原因：%s", e.getMessage()==null?"":e.getMessage()));		
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			
			break;
			
		case 2://查询报价单信息
			JSONObject resJson2 = new JSONObject();
			try{
				String QuotationId = req.getParameter("QuotationId");	
				String Customer = req.getParameter("Customer");
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String QuoStatus = req.getParameter("QuoStatus");
				if(StartTime==null){
					StartTime="";
				}
				if(EndTime==null){
					EndTime="";
				}
				if(QuotationId==null){
					QuotationId="";
				}
				if(Customer==null){
					Customer="";
				}
				if(QuoStatus==null){
					QuoStatus="";
				}
				JSONArray options = new JSONArray();
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				List<Quotation> quotationList;
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				int total;
				
				if(QuotationId !=null && QuotationId.length()>0)
				{
					//System.out.println(QuotationId);
					condList.add(new KeyValueWithOperator("number","%" + URLDecoder.decode(QuotationId.trim(), "UTF-8") + "%","like"));
					
				}
				if(Customer != null && Customer.length()>0){
					String cusName =  URLDecoder.decode(Customer.trim(), "UTF-8");
					condList.add(new KeyValueWithOperator("customerName","%" + cusName + "%","like"));
				}
				if(QuoStatus != null && QuoStatus.length()>0){
					QuoStatus =  URLDecoder.decode(QuoStatus.trim(), "UTF-8");
					condList.add(new KeyValueWithOperator("status",Integer.parseInt(QuoStatus),"="));
				}
				Timestamp Start = null;
				if(StartTime!=null && StartTime.trim().length() > 0){		
					 Start = Timestamp.valueOf(String.format("%s 00:00:00",StartTime.trim()));	
					 condList.add(new KeyValueWithOperator("offerDate",Start,">="));
				}
				Timestamp End = null;
				if(EndTime!=null && EndTime.trim().length() > 0){		
					End = Timestamp.valueOf(String.format("%s 23:59:00",EndTime.trim()));	
					 condList.add(new KeyValueWithOperator("offerDate",End,"<="));
				}
				total = quotationMgr.getTotalCount(condList);				
				quotationList = quotationMgr.findPagedAllBySort(page, rows, "offerDate", false, condList);
				
				for (Quotation quo : quotationList) {
					JSONObject option = new JSONObject();
					option.put("num", quo.getNumber());
					option.put("CustomerName", quo.getCustomerName());
					option.put("Contactor", quo.getContactor());
					option.put("ContactorTel", quo.getContactorTel());
					option.put("CarCost", quo.getCarCost());
					option.put("ContactorEmail", quo.getContactorEmail()==null?"":quo.getContactorEmail());
					option.put("OfferDate", sf.format(quo.getOfferDate()));
					option.put("OffererId", quo.getSysUser().getName());
					option.put("Status",quo.getStatus());
					option.put("Remark", quo.getRemark());
					option.put("Version", quo.getVersion());
						
					options.put(option);
				}
				resJson2.put("total", total);
				resJson2.put("rows", options);
				
			}catch(Exception e){
				try {
					resJson2.put("total", 0);
					resJson2.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 2", e);
				}else{
					log.error("error in QuotationServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(resJson2.toString());
			}
			break;
		
		case 3://修改报价单条目
			JSONObject retObj3 = new JSONObject();
			
			JSONObject ret = null;
		
			try {
				ret = new JSONObject(req.getParameter("quotationId").trim());
				List<QuotationItem> quotationItemList=new ArrayList();
				Quotation quotatn = new Quotation();
				
				String number = ret.getString("num");
				String queryString = "select max(model.number) from Quotation as model where model.number like ?";
				List<Object> retList = quotationMgr.findByHQL(queryString, number.substring(0,11)+"%");
				
				Integer codeBeginInt = Integer.parseInt("01");
				if(retList.size() > 0 && retList.get(0) != null){
					codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(11,13)) + 1;
					String maxCode = retList.get(0).toString();
					Quotation quo=quotationMgr.findByVarProperty(new KeyValueWithOperator("number",maxCode,"=")).get(0);
					quo.setStatus(1);
					if(!quotationMgr.update(quo))
						throw new Exception("修改旧版本数据错误！");
				}
				number = number.substring(0,11) + String.format("%02d", codeBeginInt);

							
				quotatn.setNumber(number);
				quotatn.setVersion(codeBeginInt);
				quotatn.setContactorTel(ret.getString("ContactorTel"));
				quotatn.setContactor(ret.getString("Contactor"));
				quotatn.setCustomerName(ret.getString("CustomerName"));
				quotatn.setCarCost(Double.valueOf(ret.getString("CarCost")));
				quotatn.setOfferDate(new Timestamp(System.currentTimeMillis()));
				quotatn.setSysUser((SysUser) req.getSession().getAttribute("LOGIN_USER"));
				quotatn.setStatus(0);
				quotatn.setRemark(ret.getString("Remark"));
				
				
				String Quotationitem = req.getParameter("Item");
				//System.out.println("Quotationitem:"+Quotationitem);
				JSONArray JsonArray = new JSONArray(Quotationitem);
				for(int i=0; i<JsonArray.length(); i++){
					JSONObject jsonObj = JsonArray.getJSONObject(i);
					QuotationItem quotationItem = new QuotationItem();
			
					if(jsonObj.has("StandardNameId") && jsonObj.getString("StandardNameId")!=null && jsonObj.getString("StandardNameId").length()>0){
						ApplianceStandardName applianceStandardName=new ApplianceStandardName();
						applianceStandardName.setId(Integer.parseInt(jsonObj.getString("StandardNameId")));
						quotationItem.setApplianceStandardName(applianceStandardName);
					}
					//quotationItem.setQuotation(quotatn);
					quotationItem.setCertificateName(jsonObj.getString("CertificateName"));
					quotationItem.setStandardName(jsonObj.getString("StandardName"));
					quotationItem.setModel(jsonObj.getString("Model"));
					quotationItem.setAccuracy(jsonObj.getString("Accuracy"));
					quotationItem.setRange(jsonObj.getString("Range"));
					
					if(jsonObj.getString("Quantity")==null||jsonObj.getString("Quantity").length()==0)
						quotationItem.setQuantity(1);
					else
						quotationItem.setQuantity(Integer.parseInt(jsonObj.getString("Quantity")));
					quotationItem.setAppFactoryCode(jsonObj.getString("AppFactoryCode"));
					quotationItem.setAppManageCode(jsonObj.getString("AppManageCode"));
					quotationItem.setManufacturer(jsonObj.getString("Manufacturer"));
					
					quotationItem.setCertType(jsonObj.getString("CertType"));
					quotationItem.setSiteTest(InttoBool(Integer.parseInt(jsonObj.getString("SiteTest"))));
					if(jsonObj.has("MinCost")&&jsonObj.getString("MinCost")!=null&&jsonObj.getString("MinCost").length()>0)
						quotationItem.setMinCost(jsonObj.getString("MinCost"));
					else
						quotationItem.setMinCost("");
					if(jsonObj.has("MaxCost")&&jsonObj.getString("MaxCost")!=null&&jsonObj.getString("MaxCost").length()>0)
						quotationItem.setMaxCost(jsonObj.getString("MaxCost"));
					else
						quotationItem.setMaxCost("");
					
					if(jsonObj.has("Xh")&&jsonObj.getString("Xh")!=null&&jsonObj.getString("Xh").length()>0)
						quotationItem.setXh(Integer.parseInt(jsonObj.getString("Xh")));
					else
						quotationItem.setMaxCost(null);
					
					quotationItem.setRemark(jsonObj.getString("Remark"));
					
					quotationItemList.add(quotationItem);
				}
				if(!quotationItemMgr.saveByBatch(quotationItemList, quotatn)){//新建报价单，批量添加报价单条目
					throw new Exception("保存委托单条目失败");
				}
				retObj3.put("IsOK", true);
				retObj3.put("msg", "修改报价单成功！");			
				
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 3", e);
				}else{
					log.error("error in QuotationServlet-->case 3", e);
				}
				try{
				retObj3.put("IsOK", false);
				retObj3.put("msg", String.format("修改失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			
			break;
			
		case 4://注销报价单
			JSONObject retJSON4 = new JSONObject();
			try {
				Quotation quotat = quotationMgr.findById(req.getParameter("id"));
				quotat.setStatus(1);
				boolean res1 = quotationMgr.update(quotat);
				retJSON4.put("IsOK", res1);
				retJSON4.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				
				try{
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("注销失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 4", e);
				}else{
					log.error("error in QuotationServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
			
		case 5://查询报价单条目
			JSONObject resJson5 = new JSONObject();
			try{
				String quotationId = req.getParameter("quotationId"); 
				List<KeyValueWithOperator> quoList = new ArrayList<KeyValueWithOperator>();
				List<QuotationItem> quotaItemList;

				int total5;
				if(quotationId == null)
				{
					quotaItemList = quotationItemMgr.findByVarProperty(quoList);
					total5 = quotationItemMgr.getTotalCount();
				}
				else
				{
					quotaItemList = quotationItemMgr.findByPropertyBySort("xh",true,new KeyValueWithOperator("quotation.number",quotationId,"="));
					total5 = quotationItemMgr.getTotalCount(new KeyValueWithOperator("quotation.number",quotationId,"="));
				}
				JSONArray optionsq = new JSONArray();
				
				for (QuotationItem quoItem : quotaItemList) {
					JSONObject option = new JSONObject();
					option.put("Id", quoItem.getId());
					option.put("StandardNameId", quoItem.getApplianceStandardName()==null?"":quoItem.getApplianceStandardName().getId());
					option.put("StandardName", quoItem.getStandardName());
				
					option.put("CertificateName", quoItem.getCertificateName());
					option.put("Model",quoItem.getModel());
					option.put("Accuracy", quoItem.getAccuracy());
					option.put("Range", quoItem.getRange());
					option.put("Quantity", quoItem.getQuantity());
					option.put("AppFactoryCode",quoItem.getAppFactoryCode()==null?"":quoItem.getAppFactoryCode());
					option.put("AppManageCode",quoItem.getAppManageCode()==null?"":quoItem.getAppManageCode());
					option.put("Manufacturer",quoItem.getManufacturer()==null?"":quoItem.getManufacturer());
					option.put("CertType", quoItem.getCertType());
					option.put("SiteTest",quoItem.getSiteTest());
					option.put("MinCost", quoItem.getMinCost()==null?"":quoItem.getMinCost());
					option.put("MaxCost", quoItem.getMaxCost()==null?"":quoItem.getMaxCost());
					option.put("Remark", quoItem.getRemark());
					option.put("Xh", quoItem.getXh()==null?"":quoItem.getXh());
					
					
					optionsq.put(option);
				}
				resJson5.put("total", total5);
				resJson5.put("rows", optionsq);
			}catch(Exception e){
				try {
					resJson5.put("total", 0);
					resJson5.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 5", e);
				}else{
					log.error("error in QuotationServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(resJson5.toString());
			}
			break;
			
		case 6:	//检查报价单是否有效
			JSONObject retJSON6 = new JSONObject();
			try{
				String QuotationId = req.getParameter("QuotationId");
				if(QuotationId != null && QuotationId.trim().length() > 0){
					Quotation q = quotationMgr.findById(QuotationId);
					if(q != null && q.getStatus() != 1){
						retJSON6.put("IsOK", true);
						retJSON6.put("IsValid", true);
						return;
					}
				}
				retJSON6.put("IsOK", true);
				retJSON6.put("IsValid", false);				
			}catch(Exception e){
				
				try{
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("验证失败！原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ex){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 6", e);
				}else{
					log.error("error in QuotationServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7://查询报价单条目（用于打印）
			JSONObject resJson7 = new JSONObject();
			try{
				String quotationId = req.getParameter("quotationId"); 
				List<KeyValueWithOperator> quoList = new ArrayList<KeyValueWithOperator>();
				List<QuotationItem> quotaItemList;
				CustomerManager cusMgr=new CustomerManager();
				
				int total5;
				if(quotationId == null)
				{
					throw new Exception("报价单号无效！");
				}
				else
				{
					quotaItemList = quotationItemMgr.findByPropertyBySort("xh",true,new KeyValueWithOperator("quotation.number",quotationId,"="));
					total5 = quotationItemMgr.getTotalCount(new KeyValueWithOperator("quotation.number",quotationId,"="));
				}
				JSONArray optionsq = new JSONArray();
				int id=1;
				double totalMin=0.0;
				double totalMax=0.0;
				int quantity=0;
				for (QuotationItem quoItem : quotaItemList) {
					JSONObject option = new JSONObject();
					option.put("Id", id++);
					option.put("StandardName", (quoItem.getCertificateName()!=null&&quoItem.getCertificateName().length()>0)?quoItem.getCertificateName():quoItem.getStandardName());
					option.put("CertificateName", quoItem.getCertificateName());
					option.put("Model",quoItem.getModel()==null?"":quoItem.getModel());
					option.put("Accuracy", quoItem.getAccuracy()==null?"":quoItem.getAccuracy());
					option.put("Range", quoItem.getRange()==null?"":quoItem.getRange());
					option.put("Quantity", quoItem.getQuantity());
					quantity = quantity + quoItem.getQuantity();
					option.put("AppFactoryCode", quoItem.getAppFactoryCode()==null?"":quoItem.getAppFactoryCode());
					option.put("AppManageCode", quoItem.getAppManageCode()==null?"":quoItem.getAppManageCode());
					option.put("Manufacturer", quoItem.getManufacturer()==null?"":quoItem.getManufacturer());
					
					option.put("CertType", quoItem.getCertType());
					option.put("SiteTest",quoItem.getSiteTest());
					
					String cost="";
					if(quoItem.getMinCost()!=null&&quoItem.getMaxCost()!=null&&quoItem.getMaxCost().length()>0&&quoItem.getMinCost().length()>0){
						if(quoItem.getMinCost().equals(quoItem.getMaxCost())){
							cost=quoItem.getMinCost();
							
						}else{
							cost=String.format("%s~%s", quoItem.getMinCost(),quoItem.getMaxCost());
						}
						option.put("Cost",cost);
						String totalcost="";
						if(quoItem.getQuantity()!=null){
							if(quoItem.getMinCost().equals(quoItem.getMaxCost())){
								totalcost=String.format("%s",Double.parseDouble(quoItem.getMinCost())*quoItem.getQuantity());
								totalMin=totalMin+Double.parseDouble(quoItem.getMinCost())*quoItem.getQuantity();
								totalMax=totalMax+Double.parseDouble(quoItem.getMinCost())*quoItem.getQuantity();						
							}else{
								totalcost=String.format("%s~%s", Double.parseDouble(quoItem.getMinCost())*quoItem.getQuantity(),Double.parseDouble(quoItem.getMaxCost())*quoItem.getQuantity());
								totalMin=totalMin+Double.parseDouble(quoItem.getMinCost())*quoItem.getQuantity();
								totalMax=totalMax+Double.parseDouble(quoItem.getMaxCost())*quoItem.getQuantity();					
							}
						}
						option.put("TotalCost",totalcost);//总检测费
					}else{
						option.put("Cost","");
						option.put("TotalCost","");//总检测费
					}
					//System.out.println(String.format("%s~%s", Double.parseDouble(quoItem.getMinCost())*quoItem.getQuantity(),Double.parseDouble(quoItem.getMaxCost())*quoItem.getQuantity()));
					option.put("Remark", quoItem.getRemark());
										
					optionsq.put(option);
				}
				JSONObject option1 = new JSONObject();//合计行
				option1.put("Id", id++);
				option1.put("StandardName", "合计：");
				option1.put("CertificateName", " ");
				option1.put("Model"," ");
				option1.put("Accuracy", " ");
				option1.put("Range", " ");
				option1.put("Quantity",quantity);
				option1.put("AppFactoryCode","");
				option1.put("AppManageCode", "");
				option1.put("Manufacturer", "");
				option1.put("CertType", " ");
				option1.put("SiteTest"," ");
				if(totalMin==totalMax){
					option1.put("TotalCost",String.format("%s",totalMin));
				}else{
					option1.put("TotalCost",String.format("%s~%s",totalMin,totalMax));
				}
				
				option1.put("Cost", " ");
				option1.put("Remark", " ");
				optionsq.put(option1);
				
//				for (int i=0;i<21;i++) {   //每页20行
//					JSONObject option = new JSONObject();
//					option.put("Id", id++);
//					option.put("StandardName", " ");
//					option.put("CertificateName", " ");
//					option.put("Model"," ");
//					option.put("Accuracy", " ");
//					option.put("Range", " ");
//					option.put("Quantity", " ");
//					option.put("AppFactoryCode","");
//					option.put("AppManageCode", "");
//					option.put("Manufacturer", "");
//					option.put("CertType", " ");
//					option.put("SiteTest"," ");
//					option.put("TotalCost"," ");
//					option.put("Cost", " ");
//					option.put("Remark", " ");
//										
//					optionsq.put(option);
//				}
				
				if(id<=14){
					int temp=id;
					for (int i=0;i<(15-temp%14);i++) {   //首页14行
						JSONObject option = new JSONObject();
						option.put("Id", id++);
						option.put("StandardName", " ");
						option.put("CertificateName", " ");
						option.put("Model"," ");
						option.put("Accuracy", " ");
						option.put("Range", " ");
						option.put("Quantity", " ");
						option.put("AppFactoryCode","");
						option.put("AppManageCode", "");
						option.put("Manufacturer", "");
						option.put("CertType", " ");
						option.put("SiteTest"," ");
						option.put("TotalCost"," ");
						option.put("Cost", " ");
						option.put("Remark", " ");											
						optionsq.put(option);
					}					
				}
			
				else{
					int temp=id-14;
					for (int i=0;i<(19-temp%18);i++) {   //后续页每页18行
						JSONObject option = new JSONObject();
						option.put("Id", id++);
						option.put("StandardName", " ");
						option.put("CertificateName", " ");
						option.put("Model"," ");
						option.put("Accuracy", " ");
						option.put("Range", " ");
						option.put("Quantity", " ");
						option.put("AppFactoryCode","");
						option.put("AppManageCode", "");
						option.put("Manufacturer", "");
						option.put("CertType", " ");
						option.put("SiteTest"," ");
						option.put("TotalCost"," ");
						option.put("Cost", " ");
						option.put("Remark", " ");
											
						optionsq.put(option);
					}
				}
				resJson7.put("total", id);
				resJson7.put("rows", optionsq);
				resJson7.put("Number", quotationId);
				resJson7.put("IsOK", true);
				
				Quotation quota=quotationMgr.findByVarProperty(new KeyValueWithOperator("number",quotationId,"=")).get(0);
				resJson7.put("CustomerName", quota.getCustomerName());
				Customer cus=cusMgr.findByVarProperty(new KeyValueWithOperator("name",quota.getCustomerName(),"=")).get(0);
				resJson7.put("Fax", cus.getFax());
				resJson7.put("Tel", quota.getContactorTel()==null?"":quota.getContactorTel());
				resJson7.put("Email", quota.getContactorEmail()==null?"":quota.getContactorEmail());
				resJson7.put("Remark", quota.getRemark()==null?"":"        6、备注："+quota.getRemark());
				resJson7.put("Address", cus.getAddress());
				
				resJson7.put("Attn", quota.getContactor()==null?"     ":quota.getContactor());//联系人
				resJson7.put("CarCost", quota.getCarCost());
				
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				resJson7.put("OfferDate", sf.format(quota.getOfferDate()));//报价时间
				
				req.getSession().setAttribute("QuotationList", resJson7);
			
				resp.sendRedirect("/jlyw/FeeManage/QuotationPrint.jsp");
			}catch(Exception e){
				try {
					resJson7.put("total", 0);
					resJson7.put("rows", new JSONArray());

					try {
						resJson7.put("IsOK", false);
						resJson7.put("msg", String.format("查找报价单条目失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 7", e);
				}else{
					log.error("error in QuotationServlet-->case 7", e);
				}
				req.getSession().setAttribute("QuotationList", resJson7);
				
				resp.sendRedirect("/jlyw/FeeManage/QuotationPrint.jsp");
			}
			break;
		case 8://导入EXCEL报价单
			JSONObject retJSON8 = new JSONObject();
			InputStream is=null;
			try{
				String FileId = req.getParameter("FileId");
				String FileType = req.getParameter("FileType");
				MongoDBUtil.CollectionType type = MongoDBUtil.CollectionType.Sharing;
				JSONObject retObj8 = MongoDBUtil.getFileInfoById(FileId, type);
				if(retObj8 != null){
					String filename = retObj8.getString(MongoDBUtil.KEYNAME_FileName);//文件名
					MongoService s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), SystemCfgUtil.BucketSharing);
					ObjectId id = new ObjectId(FileId);
					GridFSDBFile dbFile = s.findOneById(id);
						//dbFile.writeTo(out);
					is=dbFile.getInputStream();
					HSSFWorkbook workbook = new HSSFWorkbook(is);
					HSSFSheet sheet = workbook.getSheetAt(0);
	
					//JSONObject paramsMap = new JSONObject();
 
					JSONArray list = new JSONArray();
					int rowcount = sheet.getLastRowNum();  //取得有效的行数  
	                HSSFRow row;
	                int colcount = 0;    
	                for (int i = 1; i <= rowcount; ++i) {   
	                    	//根据index取得行对象,有了行对象，就可以取得每一个单元对象
						row = sheet.getRow(i);
						if (row.getCell(1) == null||row.getCell(1).toString().equals(""))
							continue;
						if (colcount == 0) {
							colcount = row.getLastCellNum(); // 一个行有多少个单元
						}
						JSONObject option = new JSONObject();
						String popularName = (String) changeCellType(row.getCell(1));//常用名称或者标准名称

						String querySQL="select model.Id from "+SystemCfgUtil.DBPrexName+"ApplianceStandardName as model where model.Name=? and model.Status = 0 " +
								" union " +
								" select p.StandardNameId from "+SystemCfgUtil.DBPrexName+"AppliancePopularName as p where p.PopularName=? and p.Status = 0 ";
						option.put("CertificateName", popularName);//常用名称
						option.put("Model", changeCellType(row.getCell(2)));
						option.put("Accuracy",changeCellType(row.getCell(4)));                        	
						option.put("Range", changeCellType(row.getCell(3)));
						String AppFactoryCode="";
						String AppManageCode="";
						if(row.getCell(6).getCellType()==HSSFCell.CELL_TYPE_NUMERIC)
						{
							if(changeCellType(row.getCell(6)).toString().length()>0&&(changeCellType(row.getCell(6)).toString().indexOf('.')!=-1)){
								int index = changeCellType(row.getCell(6)).toString().indexOf('.');
								int length=changeCellType(row.getCell(6)).toString().length();
								
								AppFactoryCode=changeCellType(row.getCell(6)).toString().substring(0, index);
							}else if(changeCellType(row.getCell(6)).toString().length()>0&&(changeCellType(row.getCell(6)).toString().indexOf('.')==-1)){
								AppFactoryCode=changeCellType(row.getCell(6)).toString();
							}			
						}else{
							AppFactoryCode=changeCellType(row.getCell(6)).toString();
						}
						option.put("AppFactoryCode", AppFactoryCode);//出厂编号
						if(row.getCell(7).getCellType()==HSSFCell.CELL_TYPE_NUMERIC)
						{
							if(changeCellType(row.getCell(7)).toString().length()>0&&(changeCellType(row.getCell(7)).toString().indexOf('.')!=-1)){
								int index = changeCellType(row.getCell(7)).toString().indexOf('.');
								int length=changeCellType(row.getCell(7)).toString().length();
								
								AppManageCode=changeCellType(row.getCell(7)).toString().substring(0, index);
							}else if(changeCellType(row.getCell(7)).toString().length()>0&&(changeCellType(row.getCell(7)).toString().indexOf('.')==-1)){
								AppManageCode=changeCellType(row.getCell(7)).toString();
							}			
						}else{
							AppManageCode=changeCellType(row.getCell(7)).toString();
						}
						
						option.put("AppManageCode",AppManageCode);      //管理编号               	
						option.put("Manufacturer", changeCellType(row.getCell(8)));//制造厂
						
						List<Object> result = quotationMgr.findBySQL(querySQL,popularName,popularName);
						if((!result.isEmpty())&&result.size()==1){							
							option.put("StandardNameId", (Integer)result.get(0));
							ApplianceStandardName appStandardName=(new ApplianceStandardNameManager()).findById(Integer.parseInt(option.getString("StandardNameId")));
							option.put("StandardName", appStandardName.getName());
							
							
							List<Object> keys = new ArrayList<Object>();
							String queryStr="select Min(t.fee) as min,Max(t.fee) as max from TargetAppliance as t where t.applianceStandardName.name = ? and t.status = 0 ";
							keys.add(URLDecoder.decode(option.getString("StandardName"), "UTF-8"));
							
							if(option.getString("Model")!=null&&option.getString("Model").length()>0){
								queryStr = queryStr+ " and t.id in (select m.targetAppliance.id from ApplianceModel as m where m.targetAppliance.id = t.id and m.model = ?)";
								keys.add(URLDecoder.decode(option.getString("Model"), "UTF-8"));
							}
							if(option.getString("Accuracy")!=null&&option.getString("Accuracy").length()>0){
								queryStr = queryStr+ " and t.id in (select a.targetAppliance.id from ApplianceAccuracy as a where a.targetAppliance.id = t.id and a.accuracy = ?)";
								keys.add(URLDecoder.decode(option.getString("Accuracy"), "UTF-8"));
							}
							if(option.getString("Range")!=null&&option.getString("Range").length()>0){
								queryStr = queryStr+ " and t.id in (select r.targetAppliance.id from ApplianceRange as r where r.targetAppliance.id = t.id and r.range = ?)";
								keys.add(URLDecoder.decode(option.getString("Range"), "UTF-8"));
							}
							List<Object[]> fee = (new TargetApplianceManager()).findByHQL(queryStr,keys);
							
							if(!fee.isEmpty()){					
								Object[] obj=fee.get(0);					
								option.put("MinCost",(Double)obj[0]);
								option.put("MaxCost",(Double)obj[1]);
								List<Object> keys1 = new ArrayList<Object>();
							}else{
								option.put("MinCost",0);
								option.put("MaxCost",0);					
							}
							option.put("Remark", "");
							
							
						}
						else{
							option.put("StandardNameId","");
							option.put("StandardName","");
							option.put("Remark", "待定");
							option.put("MinCost","");
							option.put("MaxCost","");	
						}
					
						
						String quantity="1";
						if(changeCellType(row.getCell(5)).toString().length()>0&&(changeCellType(row.getCell(5)).toString().indexOf('.')!=-1)){
							int index = changeCellType(row.getCell(5)).toString().indexOf('.');
							int length=changeCellType(row.getCell(5)).toString().length();
							
							quantity=changeCellType(row.getCell(5)).toString().substring(0, index);
						}else if(changeCellType(row.getCell(5)).toString().length()>0&&(changeCellType(row.getCell(5)).toString().indexOf('.')==-1)){
							quantity=changeCellType(row.getCell(5)).toString();
						}													
						option.put("Quantity",quantity);
						
						String cerType = (String) changeCellType(row.getCell(9));
						if(cerType.equals("检定"))
							option.put("CertType", 1);
						else if(cerType.equals("检验"))
							option.put("CertType", 4);
						else if(cerType.equals("校准"))
							option.put("CertType", 2);
						else if(cerType.equals("检测"))
							option.put("CertType", 3);
						else
							option.put("CertType", "");
						
						list.put(option);
						
					}//for结束
	                retJSON8.put("total", list.length());
	                retJSON8.put("rows", list);
					//createQuotation(req, paramsMap);// 生成报价单
				} else {
	                retJSON8.put("total", 0);
	                retJSON8.put("rows", new JSONArray());
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 8", e);
				}else{
					log.error("error in QuotationServlet-->case 8", e);
				}
				try {
					retJSON8.put("total", 0);
	                retJSON8.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				is.close();
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON8.toString());
				//System.out.println(retJSON8.toString());
			}
			break;
		case 9://导出
			
			JSONObject retObj7 = new JSONObject();
			try {
				String paramsStr = req.getParameter("paramsStr");
				String contactortel = req.getParameter("contactorTel");
				String contactor = req.getParameter("contactor");
				String customername = req.getParameter("customername");
				String queryStr = "from QuotationItem model where ";
				List<Object> keys = new ArrayList<Object>();
				
				queryStr = queryStr + "model.quotation.number = ?";
				keys.add(paramsStr.trim());
				//System.out.println(queryStr);	
				List<String> myTitle=new ArrayList();
				myTitle.add(customername);
				myTitle.add(contactor);
				myTitle.add(contactortel);
				String filePath = ExportUtil.ExportToExcel(queryStr , keys, myTitle, "formatExcel", "formatTitle", QuotationItemManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 9", e);
				}else{
					log.error("error in QuotationServlet-->case 9", e);
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
		case 10://修改询价单位信息
			String number = req.getParameter("number");
			String Contactor = req.getParameter("Contactor");
			String ContactorTel = req.getParameter("ContactorTel");
			String CarCost = req.getParameter("CarCost");
			String ContactorEmail = req.getParameter("ContactorEmail");
			
			
			JSONObject retObj10 = new JSONObject();
			try {
				
				Quotation quo = quotationMgr.findById(number);
				quo.setContactor(Contactor);
				quo.setContactorTel(ContactorTel);
				quo.setContactorEmail(ContactorEmail);
				if(CarCost!=null&&CarCost.length()>0){
					quo.setCarCost(Double.parseDouble(CarCost));
				}
				if(!quotationMgr.update(quo))
					throw new Exception("更新数据库失败！");
					
				retObj10.put("IsOK",true);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 9", e);
				}else{
					log.error("error in QuotationServlet-->case 9", e);
				}
				try {
					retObj10.put("IsOK", false);
					retObj10.put("msg", String.format("修改询价单位信息失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj10.toString());
			}
			break;
		case 11: // 报价单导入到现场业务时，根据委托单位名查询报价单号
			JSONArray jsonArray = new JSONArray();
			try {
				String queryNameStr = req.getParameter("QueryName");	//查询的 “名称”
				
				if(queryNameStr != null && queryNameStr.trim().length() > 0){
					String queryName =  new String(queryNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//解决URL传递中文乱码问题
					
					List<Quotation> quoList = quotationMgr.findPagedAllBySort(0,50,"offerDate",false,new KeyValueWithOperator("customerName", queryName, "="),new KeyValueWithOperator("status", 0, "="));
					for(Quotation quo : quoList){
						JSONObject jsonObj = new JSONObject();						
						jsonObj.put("name", quo.getNumber());	//报价单号						
						jsonArray.put(jsonObj);
					}
					
							
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 11", e);
				}else{
					log.error("error in QuotationServlet-->case 11", e);
				}
				
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUICombobox);
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 12: // 报价单导入到现场业务时，导入报价单条目及报价单信息至现场业务中
			
			JSONObject retObj12 = new JSONObject();
			try {
				String QuoItemRows = req.getParameter("QuoItemRows").trim();
				//System.out.println(QuoItemRows);
				String QuotationNumber = req.getParameter("QuotationNumber");
				JSONArray QuoItemArray = new JSONArray(QuoItemRows);	//报价单条目
				JSONArray options = new JSONArray();
				Quotation quo = quotationMgr.findById(QuotationNumber);
				if(quo.getCustomer()!=null){
					retObj12.put("Name", quo.getCustomer().getName());
					retObj12.put("CustomerId", quo.getCustomer().getId());
					retObj12.put("ZipCode", quo.getCustomer().getZipCode());
					retObj12.put("Region", quo.getCustomer().getRegion().getName());
					retObj12.put("RegionId", quo.getCustomer().getRegion().getId());
					retObj12.put("Address", quo.getCustomer().getAddress());
					retObj12.put("Tel", quo.getCustomer().getTel());
					retObj12.put("Contactor", quo.getContactor()==null?"":quo.getContactor());
					retObj12.put("ContactorTel", quo.getContactorTel()==null?"":quo.getContactorTel());				
				}
				//ApplianceStandardNameManager standardNameMgr = new ApplianceStandardNameManager();
				for(int i = 0; i < QuoItemArray.length(); i++){
					JSONObject jsonObj = QuoItemArray.getJSONObject(i);
					JSONObject option = new JSONObject();
					/**********************   添加报价单条目信息    ************************/
					String StandardNameId = jsonObj.get("StandardNameId").toString();	//器具分类类型
					
					if(StandardNameId==null||StandardNameId.length()==0){
						option.put("ApplianceSpeciesId", "");	//器具分类Id(或器具标准名称ID)
						option.put("ApplianceSpeciesName", "");	//
						option.put("SpeciesType", "");	//器具分类Id(或器具标准名称ID)
						option.put("TestFee", "");						
					}else{
						option.put("ApplianceSpeciesId", StandardNameId);	//器具分类Id(或器具标准名称ID		
						option.put("ApplianceSpeciesName", jsonObj.get("StandardName").toString());	//
						option.put("SpeciesType", 0);	//器具分类Id(或器具标准名称ID)
						if(jsonObj.has("MinCost")&&jsonObj.has("MaxCost")&&jsonObj.get("MinCost").toString().length()>0&&jsonObj.get("MaxCost").toString().length()>0&&(Double.parseDouble(jsonObj.get("MinCost").toString())==Double.parseDouble(jsonObj.get("MaxCost").toString()))){
							option.put("TestFee", jsonObj.get("MinCost").toString());
						}else{
							option.put("TestFee", "");
						}
					}
					String CertificateName = jsonObj.get("CertificateName").toString();	//证书名称即常用名称
					if(CertificateName==null||CertificateName.length()==0){
						option.put("ApplianceName", "");	//
					}else{
						option.put("ApplianceName", CertificateName);	//
					}
					option.put("Model", jsonObj.get("Model").toString());	//型号规格
					option.put("Range", jsonObj.get("Range").toString());
					option.put("Accuracy", jsonObj.get("Accuracy").toString());
					option.put("Quantity", jsonObj.get("Quantity").toString());
					option.put("ApplianceCode", jsonObj.get("AppFactoryCode").toString());
					option.put("AppManageCode", jsonObj.get("AppManageCode").toString());
					option.put("Manufacturer", jsonObj.get("Manufacturer").toString());					
					option.put("ReportType", jsonObj.get("CertType").toString());
					
					
					options.put(option);
				}		
				retObj12.put("rows", options);
				retObj12.put("IsOK", true);
				
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 12", e);
				}else{
					log.error("error in QuotationServlet-->case 12", e);
				}
				try {
					retObj12.put("IsOK", false);
					retObj12.put("msg", String.format("从报价单导入失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj12.toString());
				
				//System.out.println(retObj12.toString());
			}
			break;
		case 13: // 报价单号的模糊查询
			JSONArray jsonarray = new JSONArray();
			try {
				String queryNameStr = req.getParameter("QueryName");	//查询的 “名称”
				
				if(queryNameStr != null && queryNameStr.trim().length() > 0){
					String queryName =  new String(queryNameStr.trim().getBytes("ISO-8859-1"), "UTF-8");	//解决URL传递中文乱码问题
			
					List<Quotation> quoList = quotationMgr.findPagedAllBySort(0,50,"offerDate",false,new KeyValueWithOperator("number", "%"+queryName+"%", "like"),new KeyValueWithOperator("status", 0, "="));
					for(Quotation quo : quoList){
						JSONObject jsonObj = new JSONObject();						
						jsonObj.put("name", quo.getNumber());	//报价单号						
						jsonarray.put(jsonObj);
					}
					
							
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QuotationServlet-->case 13", e);
				}else{
					log.error("error in QuotationServlet-->case 13", e);
				}
				
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUICombobox);
				resp.getWriter().write(jsonarray.toString());
			}
			break;
		}
	}
	
	//类型转换int to boolean
	private boolean InttoBool(int i){
        switch(i){
        case 0:
        	return false;
        default:
        	return true;
        }
	}
	private void createQuotation(HttpServletRequest req,JSONObject json) throws Exception{
		try{
			QuotationManager quoMgr=new QuotationManager();
			QuotationItemManager quoItemMgr=new QuotationItemManager();
			
			String CustomerName=json.getString("CustomerName");
			String Contactor=json.getString("Contactor");
			String ContactorTel=json.getString("ContactorTel");
			JSONArray jArray=json.getJSONArray("List");
			//System.out.println(CustomerName+Contactor+ContactorTel);
			//String number = UIDUtil.get22BitUID();
			String year = String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
			String queryString = "select max(model.number) from Quotation as model where model.number like ?";
			List<Object> retList = quoMgr.findByHQL(queryString, year+"%");
			Integer codeBeginInt = Integer.parseInt("000001");
			if(retList.size() > 0 && retList.get(0) != null){
				codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4,10)) + 1;
			}
			String number = year + String.format("%06d", codeBeginInt) + "-01";
			Quotation quotation = new Quotation();
			quotation.setVersion(1);
			quotation.setStatus(0);
			quotation.setNumber(number);
			quotation.setContactor(Contactor);
			quotation.setContactorTel(ContactorTel);
			quotation.setCustomerName(CustomerName);
			quotation.setCarCost(100.0);
			quotation.setOfferDate(new Timestamp(System.currentTimeMillis()));
			quotation.setSysUser((SysUser) req.getSession().getAttribute("LOGIN_USER"));
			//quotation.setRemark(Remark);
		    if(!quoMgr.save(quotation))
		    	throw new Exception("新建报价单失败！");
			for(int i = 0; i < jArray.length(); i++){
				JSONObject jsonObj = jArray.getJSONObject(i);
				if(jsonObj.getString("StandardName")!=null&&jsonObj.getString("StandardName").length()!=0){
					QuotationItem quotationItem = new QuotationItem();
					
					quotationItem.setQuotation(quotation);
					//quotationItem.setCertificateName(jsonObj.getString("CertificateName"));
					quotationItem.setStandardName(jsonObj.getString("StandardName"));
					quotationItem.setModel(jsonObj.getString("Model"));
					quotationItem.setAccuracy(jsonObj.getString("Accuracy"));
					//quotationItem.setRange(jsonObj.getString("Range"));
					//quotationItem.setQuantity(Integer.parseInt(jsonObj.getString("Quantity")));
					quotationItem.setCertType(jsonObj.getString("CertType"));
					quotationItem.setSiteTest(InttoBool(0));
					quotationItem.setMinCost("0");
					//quotationItem.setRemark(jsonObj.getString("Remark"));
					
					if(!quoItemMgr.save(quotationItem))
						throw new Exception("保存报价单条目失败！");
				}
			}
		}catch(Exception e){
			throw e;
		}
	
	}
	private Object changeCellType(HSSFCell cell){
		 switch (cell.getCellType()) {
	     case HSSFCell.CELL_TYPE_NUMERIC:
	      return cell.getNumericCellValue();
	    
	     case HSSFCell.CELL_TYPE_STRING:
	      return cell.getStringCellValue();
	   
	     case HSSFCell.CELL_TYPE_BOOLEAN:
	      return cell.getBooleanCellValue();
	    
	     case HSSFCell.CELL_TYPE_FORMULA:
	      return cell.getCellFormula();
	     
	     default:
	      return "";
	     
	     }
	}
	
	private Quotation initQuotation(HttpServletRequest req, String number){
		Quotation quotation ;
		QuotationManager quotationMgr = new QuotationManager();
		int version = 1;
		if(number == null){
			String year = String.format("%04d", Calendar.getInstance().get(Calendar.YEAR));
			String queryString = "select max(model.number) from Quotation as model where model.number like ?";
			List<Object> retList = quotationMgr.findByHQL(queryString, year+"%");
			Integer codeBeginInt = Integer.parseInt("000001");
			if(retList.size() > 0 && retList.get(0) != null){
				codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4,10)) + 1;
			}
			number = year + String.format("%06d", codeBeginInt) + "-01";
	
			quotation = new Quotation();
			quotation.setVersion(1);
			quotation.setStatus(0);
		}
		else{
			quotation = (new QuotationManager()).findById(number);
			version = quotation.getVersion()+1;
			number = number.substring(0,10)+'-'+String.format("%02d", version);
			quotation.setVersion(version);
			quotation.setStatus(Integer.parseInt(req.getParameter("Status")));
		}
		String Contactor = req.getParameter("Contactor");
		String ContactorTel = req.getParameter("ContactorTel");
		String CustomerName = req.getParameter("CustomerName");
		String CarCost = req.getParameter("CarCost");
		String Remark = req.getParameter("Remark_1");
		String ContactorEmail = req.getParameter("ContactorEmail");
		
		CustomerManager cusMgr = new CustomerManager();
		List<Customer> cusList = cusMgr.findByVarProperty(new KeyValueWithOperator("name",CustomerName.trim(),"="));
		if(cusList!=null){
			quotation.setCustomer(cusList.get(0));		
		}
		quotation.setNumber(number);
		quotation.setContactor(Contactor);
		quotation.setContactorTel(ContactorTel);
		quotation.setContactorEmail(ContactorEmail);
		quotation.setCustomerName(CustomerName);
		quotation.setCarCost(Double.valueOf(CarCost));
		quotation.setOfferDate(new Timestamp(System.currentTimeMillis()));
		quotation.setSysUser((SysUser) req.getSession().getAttribute("LOGIN_USER"));
		quotation.setRemark(Remark);
		
		
		return quotation;
	}
}
