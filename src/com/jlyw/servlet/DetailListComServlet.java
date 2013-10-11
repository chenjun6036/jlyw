package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CertificateFeeAssign;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DetailListCom;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.Withdraw;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.DetailListComManager;
import com.jlyw.manager.DetailListManager;
import com.jlyw.manager.DiscountManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.WithdrawManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.FlagUtil.CommissionSheetStatus;

public class DetailListComServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(DetailListComServlet.class);
	
	//HQL语句：根据委托单Id查询该委托单下的所有费用
	public static final String queryStringTotalFeeByCommissionSheetId = CertificateFeeAssignManager.queryStringAllFeeByCommissionSheetId;
	//HQL语句：根据原始记录和证书查询该证书的所有费用
	public static final String queryStringTotalFeeByOriginalRecordIdAndCertificateId = CertificateFeeAssignManager.queryStringAllFeeByOriginalRecordIdAndCertificateId;
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
		
		CommissionSheetManager cSheetMgr = new CommissionSheetManager();
		switch (method) {
		case 0:	//多条件查找委托单的信息（费用信息从CertificateFeeAssign中取）
			JSONObject retJSON9 = new JSONObject();
			int totalSize9 = 0;
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
					String CustomerName  = req.getParameter("CustomerName");
				
					String ApplianceName = req.getParameter("ApplianceName");
					String BeginDate = req.getParameter("BeginDate");
					String EndDate = req.getParameter("EndDate");
					String Code = req.getParameter("Code");	//委托单号
					
					
					List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
					condList.add(new KeyValueWithOperator("status",3, "="));//选择委托单完工确认的
					if(CustomerName != null && CustomerName.length() > 0){
						String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
						condList.add(new KeyValueWithOperator("customerName",cusName,"="));
					}
					if(Code != null && Code.length() > 0){
						condList.add(new KeyValueWithOperator("code", "%"+Code+"%", "like"));
					}
					if(BeginDate != null && BeginDate.length() > 0){
						condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(BeginDate).getTime()), ">="));
					}
					if(EndDate != null && EndDate.length() > 0){
						condList.add(new KeyValueWithOperator("commissionDate", new Timestamp(Date.valueOf(EndDate).getTime()), "<"));
					}
					if(ApplianceName != null && ApplianceName.trim().length() > 0 ){
						String appName = URLDecoder.decode(ApplianceName.trim(), "UTF-8");
						condList.add(new KeyValueWithOperator("applianceName", "%"+appName+"%", "like"));
					}
					totalSize9 = cSheetMgr.getTotalCount(condList);
					CommissionSheetManager feeMgr=new CommissionSheetManager();
					
					List<CommissionSheet> retList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
					if(retList != null && retList.size() > 0){
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						for(CommissionSheet cSheet : retList){
							if(cSheet.getStatus()==3){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("Id", cSheet.getId());
								jsonObj.put("Code", cSheet.getCode());
								jsonObj.put("Pwd", cSheet.getPwd());
								jsonObj.put("CustomerName", cSheet.getCustomerName());
								jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
								jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）							
								jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期			
								int GenerateTag=0;
								if(cSheet.getDetailListCode()!=null&&cSheet.getDetailListCode().length()>0){			
									GenerateTag=1;
								}						
								jsonObj.put("GenerateTag",GenerateTag);//是否已经生成清单的标志位								
								/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
								List<Object[]> FList=feeMgr.findByHQL(queryStringTotalFeeByCommissionSheetId, cSheet.getId());												
								if(FList.isEmpty()){
							    	jsonObj.put("TestFee", 0.0);
									jsonObj.put("RepairFee", 0.0);
									jsonObj.put("MaterialFee", 0.0);
									jsonObj.put("CarFee", 0.0);
									jsonObj.put("DebugFee", 0.0);
									jsonObj.put("OtherFee", 0.0);
									jsonObj.put("TotalFee", 0.0);
							    }else{
								    for(Object[] fee:FList){							    	
										jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
										jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
										jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
										jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
										jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
										jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
										jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
								   }
							    }
								jsonObj.put("Status", cSheet.getStatus());
								
								jsonArray.put(jsonObj);	
							}
						}
					}
				
				retJSON9.put("total", totalSize9);
				retJSON9.put("rows", jsonArray);
			} catch (Exception e) {
				
				try {
					retJSON9.put("total", 0);
					retJSON9.put("rows", new JSONArray());
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 0", e);
				}else{
					log.error("error in DetailListComServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			
			break;
		case 1: // 生成费用清单
			try {
				String DetailList = req.getParameter("DetailList").trim();	//清单信息
				JSONArray DetailListArray = new JSONArray(DetailList);	//清单信息
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				
				DetailListManager deListMgr=new DetailListManager();
				
				DetailList deList=new DetailList();
				/********************  清单对象    ******************/
				double totalFee=0;
				for(int i = 0; i < DetailListArray.length(); i++){
					JSONObject jsonObj = DetailListArray.getJSONObject(i);
					totalFee = totalFee + Double.parseDouble(jsonObj.getString("TotalFee"));
					
				}
				//begin-查询本清单最大的清单号
				String queryCode = String.format("%d", today.getYear()+1900);		
				String queryString = "select max(model.code) from DetailList as model where model.code like ?";
				List<Object> retList = deListMgr.findByHQL(queryString, queryCode+"%");
				Integer codeBeginInt = Integer.parseInt("0000001");	//委托单编号
				if(retList.size() > 0 && retList.get(0) != null){
					codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4)) + 1;
				}
				//end-查询本清单最大的清单号
				String code=queryCode+String.format("%07d", codeBeginInt);
				deList.setTotalFee(totalFee);
				deList.setCode(code);
				deList.setStatus(0);
				deList.setVersion(0);
				SysUser lastEditor=(SysUser)req.getSession().getAttribute("LOGIN_USER");
				deList.setSysUser(lastEditor);
				deList.setLastEditTime(today);
			
				List<CommissionSheet> comsheetList=new ArrayList<CommissionSheet>();
				for(int i = 0; i < DetailListArray.length(); i++){
					JSONObject jsonObj = DetailListArray.getJSONObject(i);				
					CommissionSheet comSheet=cSheetMgr.findById(jsonObj.getInt("Id"));
					if(comSheet.getStatus()!=3)
						throw new Exception("委托单" +comSheet.getCode()+ "状态不是“已完工”！");
				
					//deComList.setDetailList(deList);		//对应的清单in
					if(comSheet.getDetailListCode()!=null){
						String HQLStr="from CommissionSheet as a where a.detailListCode = ? and a.status <> 10 ";
						List<CommissionSheet> comList=cSheetMgr.findByHQL(HQLStr, comSheet.getDetailListCode());
						if(comList != null && comList.size() == 1){
							DetailListManager dMgr = new DetailListManager();
							for(CommissionSheet com : comList){								
								List<DetailList> dlList = dMgr.findByVarProperty(new KeyValueWithOperator("code",com.getDetailListCode(),"="),
										new KeyValueWithOperator("status",0,"="));
								if(dlList.size() > 0){
									DetailList delist = dlList.get(0);
									delist.setStatus(1);
									dMgr.update(delist);
								}
								
							}
						}
					}
					comSheet.setDetailListCode(code);
					comsheetList.add(comSheet);
				}
				/**********************   保存清单 和 委托单      ************************/
				if(cSheetMgr.saveByBatch(comsheetList,deList)){
					JSONObject retObj=new JSONObject();
					retObj.put("IsOK", true);
					retObj.put("msg", "费用清单生成成功！");
					retObj.put("DetailListId", deList.getCode());
					retObj.put("DetailListID", deList.getId());
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj.toString());
				}else{
					throw new Exception("费用清单保存到数据库失败！");
				}
			}catch (Exception e){
				
				JSONObject retObj=new JSONObject();
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("处理失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 1", e);
				}else{
					log.error("error in DetailListComServlet-->case 1", e);
				}
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2:	//多条件查找费用清单信息（在打印费用时使用，包括了已结账的费用清单）
			JSONObject retJSO2 = new JSONObject();
			int totalSize2 = 0;
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
					String CustomerName  = req.getParameter("CustomerName");					
					String Code = req.getParameter("Code");	//委托单号
					String FeeCode = req.getParameter("FeeCode");	//费用清单号

					String DateFrom = req.getParameter("DateFrom");
					String DateEnd = req.getParameter("DateEnd");
					
					String Type = req.getParameter("Type");
				
					List<Object> keys = new ArrayList<Object>();

					String QueryHQL="from DetailList as a where a.status <> 1";
					//condList.add(new KeyValueWithOperator("status", 1, "<>"));//选择清单尚未注销的
					if(FeeCode != null && FeeCode.length() > 0){					
						QueryHQL=QueryHQL + " and a.code like ?";
						keys.add("%"+FeeCode+"%");
					}
					if(CustomerName != null && CustomerName.length() > 0){
						String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
										
						QueryHQL=QueryHQL + " and a.code in (select c.detailListCode from CommissionSheet as c where c.customerName = ?)";
						keys.add(cusName);
						
					}
					if(Code != null && Code.length() > 0){
						
						QueryHQL=QueryHQL + "and a.code in (select d.detailListCode from CommissionSheet as d where d.code like ?)";
						keys.add("%"+Code+"%");
					}
					if(DateFrom != null && DateFrom.length() > 0){
						Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
						QueryHQL=QueryHQL + "and a.code in (select e.detailListCode from CommissionSheet as e where e.commissionDate >= ?)";
						keys.add(Start);
					}
					if(DateEnd != null && DateEnd.length() > 0){
						Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
						QueryHQL=QueryHQL + "and a.code in (select f.detailListCode from CommissionSheet as f where f.commissionDate <= ?)";
						keys.add(End);
					}
					DetailListManager deListMgr=new DetailListManager();
					CommissionSheetManager feeMgr=new CommissionSheetManager();
					List<DetailList> retList = deListMgr.findPageAllByHQL(QueryHQL+"order by a.lastEditTime desc", page,rows, keys);
					totalSize2=deListMgr.getTotalCountByHQL("select count(a)"+QueryHQL,keys);
					if(retList != null && retList.size() > 0){
						
						for(DetailList deList : retList){
							
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("DetailListId", deList.getId());//清单ID
								jsonObj.put("DetailListCode", deList.getCode());//清单号
								jsonObj.put("DetailListStatus", deList.getStatus());//状态
								jsonObj.put("DetailListPaidFee", deList.getPaidFee()==null?0.00:deList.getPaidFee());//已付费用
								jsonObj.put("DetailListLastEditTime",  DateTimeFormatUtil.DateTimeFormat.format(deList.getLastEditTime()));//最后编辑时间
								jsonObj.put("Version", deList.getVersion());//清单版本号
								List<CommissionSheet> comSheetList=(new CommissionSheetManager()).findByVarProperty(new KeyValueWithOperator("detailListCode",deList.getCode(),"="));
								if(!comSheetList.isEmpty()){
									jsonObj.put("Customer", comSheetList.get(0).getCustomerName());//委托单位
									
									double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0,TotalFee=0.0;
									
									for(CommissionSheet com : comSheetList){
	
										/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
										List<Object[]> FList=feeMgr.findByHQL(queryStringTotalFeeByCommissionSheetId, com.getId());												
										if(FList!=null){
										    Object[] fee=FList.get(0);
									    	TestFee =TestFee + (fee[0]==null?0.0:(Double)fee[0]);
									    	RepairFee = RepairFee+ (fee[1]==null?0.0:(Double)fee[1]);
									    	MaterialFee = MaterialFee+ (fee[2]==null?0.0:(Double)fee[2]);
									    	CarFee = CarFee+ (fee[3]==null?0.0:(Double)fee[3]);
									    	DebugFee = DebugFee+ (fee[4]==null?0.0:(Double)fee[4]);
									    	OtherFee = OtherFee+ (fee[5]==null?0.0:(Double)fee[5]);
									    	TotalFee = TotalFee+ (fee[6]==null?0.0:(Double)fee[6]);										
										   
									    }		         									         		
							         	
									}
									jsonObj.put("TestFee", TestFee);
									jsonObj.put("RepairFee", RepairFee);
									jsonObj.put("MaterialFee", MaterialFee);
									jsonObj.put("CarFee", CarFee);
									jsonObj.put("DebugFee", DebugFee);
									jsonObj.put("OtherFee", OtherFee);
									jsonObj.put("TotalFee", TotalFee);
									
								}else{
									jsonObj.put("Customer", "");//委托单位
									jsonObj.put("TestFee", "");
									jsonObj.put("RepairFee","");
									jsonObj.put("MaterialFee","");
									jsonObj.put("CarFee","");
									jsonObj.put("DebugFee","");
									jsonObj.put("OtherFee", "");
									jsonObj.put("TotalFee","");
								}
								
								
								jsonArray.put(jsonObj);	
						}
					}
			
				retJSO2.put("total", totalSize2);
				retJSO2.put("rows", jsonArray);
			} catch (Exception e) {
				
				try {
					retJSO2.put("total", 0);
					retJSO2.put("rows", new JSONArray());
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 2", e);
				}else{
					log.error("error in DetailListComServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSO2.toString());
			}
			
			break;	
		case 21:	//多条件查找费用清单信息（未包含已结账的费用清单,结账时候用）
			JSONObject retJSO21 = new JSONObject();
			int totalSize21 = 0;
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
					String CustomerId = req.getParameter("CustomerId");
					String CustomerName  = req.getParameter("CustomerName");					
					String Code = req.getParameter("Code");	//委托单号
					String FeeCode = req.getParameter("FeeCode");	//费用清单号
					
					//String QueryHQL="select distinct a.detailList from DetailListCom as a where a.detailList.status <> 1 and a.commissionSheet.status=3";
					String QueryHQL="from DetailList as a where a.status = 0";
					//condList.add(new KeyValueWithOperator("status", 1, "<>"));//选择清单尚未注销的
					if(FeeCode != null && FeeCode.length() > 0){
						//condList.add(new KeyValueWithOperator("detailList.code", FeeCode, "="));
						QueryHQL=QueryHQL + " and a.code ='" + FeeCode + "' ";
					}
					if(CustomerId != null && CustomerId.length() > 0){
						String cusId =  URLDecoder.decode(CustomerId.trim(), "UTF-8"); //解决jquery传递中文乱码问题
										
						QueryHQL=QueryHQL + " and a.code in (select c.detailListCode from CommissionSheet as c where c.customerId ="+cusId+")";
					}
					if(CustomerName != null && CustomerName.length() > 0){
						String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
										
						QueryHQL=QueryHQL + " and a.code in (select c.detailListCode from CommissionSheet as c where c.customerName ='"+cusName+"' )";
					}
					if(Code != null && Code.length() > 0){
						
						QueryHQL=QueryHQL + "and a.code in (select d.detailListCode from CommissionSheet as d where d.code ='" + Code + "' )";
					}
				    
					
					DetailListManager deListMgr=new DetailListManager();
					
					totalSize21 = deListMgr.getTotalCountByHQL("select count(a) "+QueryHQL);
					List<DetailList> retList = deListMgr.findPageAllByHQL(QueryHQL, page, rows);
										
					if(retList != null && retList.size() > 0){
						for(DetailList deList : retList){
							
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("DetailListId", deList.getId());//清单ID
								jsonObj.put("DetailListCode", deList.getCode());//清单号
								jsonObj.put("DetailListStatus", deList.getStatus());//状态
								jsonObj.put("DetailListPaidFee", deList.getPaidFee()==null?0.00:deList.getPaidFee());//已付费用
								jsonObj.put("DetailListLastEditTime",  DateTimeFormatUtil.DateTimeFormat.format(deList.getLastEditTime()));//最后编辑时间
								jsonObj.put("Version", deList.getVersion());//清单版本号
								List<CommissionSheet> comSheetList=(new CommissionSheetManager()).findByVarProperty(new KeyValueWithOperator("detailListCode",deList.getCode(),"="));
								if(!comSheetList.isEmpty()){
									jsonObj.put("Customer", comSheetList.get(0).getCustomerName());//委托单位
									
									double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0,TotalFee=0.0;
									
									for(CommissionSheet com : comSheetList){
	
										/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
										List<Object[]> FList=new CertificateFeeAssignManager().findByHQL(queryStringTotalFeeByCommissionSheetId, com.getId());												
										if(!FList.isEmpty()){
										    Object[] fee=FList.get(0);
									    	TestFee =TestFee + (fee[0]==null?0.0:(Double)fee[0]);
									    	RepairFee = RepairFee+ (fee[1]==null?0.0:(Double)fee[1]);
									    	MaterialFee = MaterialFee+ (fee[2]==null?0.0:(Double)fee[2]);
									    	CarFee = CarFee+ (fee[3]==null?0.0:(Double)fee[3]);
									    	DebugFee = DebugFee+ (fee[4]==null?0.0:(Double)fee[4]);
									    	OtherFee = OtherFee+ (fee[5]==null?0.0:(Double)fee[5]);
									    	TotalFee = TotalFee+ (fee[6]==null?0.0:(Double)fee[6]);										
										   
									    }		         									         		
							         	
									}
									jsonObj.put("TestFee", TestFee);
									jsonObj.put("RepairFee", RepairFee);
									jsonObj.put("MaterialFee", MaterialFee);
									jsonObj.put("CarFee", CarFee);
									jsonObj.put("DebugFee", DebugFee);
									jsonObj.put("OtherFee", OtherFee);
									jsonObj.put("TotalFee", TotalFee);
									
								}else{
									jsonObj.put("Customer", "");//委托单位
									jsonObj.put("TestFee", "");
									jsonObj.put("RepairFee","");
									jsonObj.put("MaterialFee","");
									jsonObj.put("CarFee","");
									jsonObj.put("DebugFee","");
									jsonObj.put("OtherFee", "");
									jsonObj.put("TotalFee","");
								}
								
								
								jsonArray.put(jsonObj);	
							
						}
					}
				
				retJSO21.put("total", totalSize21);
				retJSO21.put("rows", jsonArray);
			} catch (Exception e) {
				
				try {
					retJSO21.put("total", 0);
					retJSO21.put("rows", new JSONArray());
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 21", e);
				}else{
					log.error("error in DetailListComServlet-->case 21", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSO21.toString());
			}
			
			break;	
		case 22:	//多条件查找费用清单信息（未包含已结账的费用清单）,在折扣申请时使用
			JSONObject retJSO22 = new JSONObject();
			int totalSize22 = 0;
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
					String CustomerName  = req.getParameter("CustomerName");					
					String Code = req.getParameter("Code");	//委托单号
					String FeeCode = req.getParameter("FeeCode");	//费用清单号
					
					//String QueryHQL="select distinct a.detailList from DetailListCom as a where a.detailList.status <> 1 and a.commissionSheet.status=3";
					String QueryHQL="from DetailListCom as a where a.detailList.status = 0";
					//condList.add(new KeyValueWithOperator("status", 1, "<>"));//选择清单尚未注销的
					if(FeeCode != null && FeeCode.length() > 0){
						//condList.add(new KeyValueWithOperator("detailList.code", FeeCode, "="));
						QueryHQL=QueryHQL + " and a.detailList.code=" + FeeCode;
					}
					if(CustomerName != null && CustomerName.length() > 0){
						String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
						//condList.add(new KeyValueWithOperator("commissionSheet.customerName",cusName,"="));
						
						QueryHQL=QueryHQL + " and a.commissionSheet.customerName ='"+cusName+"'";
					}
					if(Code != null && Code.length() > 0){
						//condList.add(new KeyValueWithOperator("commissionSheet.code", Code, "="));
						QueryHQL=QueryHQL + " and a.commissionSheet.code=" + Code;
					}
				
					DetailListComManager deListComMgr=new DetailListComManager();
					totalSize22 = deListComMgr.getTotalCountByHQL("select count(distinct a.detailList) "+QueryHQL);
					List<DetailList> retList = deListComMgr.findPageAllByHQL("select distinct a.detailList "+QueryHQL, page, rows);
					
					if(retList != null && retList.size() > 0){
						for(DetailList deList : retList){
							
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("DetailListId", deList.getId());//清单ID
								jsonObj.put("DetailListCode", deList.getCode());//清单号
								jsonObj.put("DetailListStatus", deList.getStatus());//状态
								List<DetailListCom> decommissionList=deListComMgr.findByVarProperty(new KeyValueWithOperator("detailList.id",deList.getId(),"="));
								jsonObj.put("Customer", decommissionList.get(0).getCommissionSheet().getCustomerName());//委托单位
								jsonObj.put("DetailListPaidFee", deList.getPaidFee()==null?0.00:deList.getPaidFee());//已付费用
								jsonObj.put("DetailListLastEditTime",  DateTimeFormatUtil.DateTimeFormat.format(deList.getLastEditTime()));//最后编辑时间
								jsonObj.put("Version", deList.getVersion());//清单版本号
								/***********在清单-委托单中查找清单的费用详情***********/
								double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0,TotalFee=0.0;
								/*for(DetailListCom detailListCom : decommissionList){
									String queryString = " select SUM(a.testFee),SUM(a.repairFee),SUM(a.materialFee),SUM(a.carFee),SUM(a.debugFee),SUM(a.otherFee),SUM(a.totalFee) from CertificateFeeAssign as a " +
										" where a.commissionSheet.id = ? and " +
										" ((a.originalRecord is null and a.certificate is null) or (a.originalRecord.status<>1 and a.certificate=a.originalRecord.certificate)) ";
						         	List<Object[]> fList = (new CertificateFeeAssignManager()).findByHQL(querystring, detailListCom.getCommissionSheet().getId());
						         	if(!fList.isEmpty()){
						         		for(Object[] fee:fList){
						         			TestFee = TestFee+((Double)fee[0]==null?0.0:(Double)fee[0]);
						         			RepairFee = RepairFee+((Double)fee[1]==null?0.0:(Double)fee[1]);
						         			MaterialFee = MaterialFee+((Double)fee[2]==null?0.0:(Double)fee[2]);
						         			CarFee = CarFee+((Double)fee[3]==null?0.0:(Double)fee[3]);
						         			DebugFee = DebugFee+((Double)fee[4]==null?0.0:(Double)fee[4]);
						         			OtherFee = OtherFee+((Double)fee[5]==null?0.0:(Double)fee[5]);
						         			TotalFee = TotalFee+((Double)fee[6]==null?0.0:(Double)fee[6]);						         			
						         		}
						         	}
								}*/
								for(DetailListCom detailListCom : decommissionList){

				         			TestFee = TestFee + (detailListCom.getOldTestFee()==null?0:detailListCom.getOldTestFee());
				         			RepairFee = RepairFee+ (detailListCom.getOldRepairFee()==null?0:detailListCom.getOldTestFee());
				         			MaterialFee = MaterialFee+ (detailListCom.getOldMaterialFee()==null?0:detailListCom.getOldTestFee());			         			
				         			CarFee = CarFee+ (detailListCom.getOldCarFee()==null?0:detailListCom.getOldTestFee());
				         			DebugFee = DebugFee+ (detailListCom.getOldDebugFee()==null?0:detailListCom.getOldTestFee());
				         			OtherFee = OtherFee+ (detailListCom.getOldOtherFee()==null?0:detailListCom.getOldTestFee());
				         			TotalFee = TotalFee+ (detailListCom.getOldTotalFee()==null?0:detailListCom.getOldTestFee());		         									         		
						         	
								}
								jsonObj.put("TestFee", TestFee);
								jsonObj.put("RepairFee", RepairFee);
								jsonObj.put("MaterialFee", MaterialFee);
								jsonObj.put("CarFee", CarFee);
								jsonObj.put("DebugFee", DebugFee);
								jsonObj.put("OtherFee", OtherFee);
								jsonObj.put("TotalFee", TotalFee);
								DiscountManager discountMgr = new DiscountManager();
								String discountString="";
								List<Discount> discountList = discountMgr.findByVarProperty(new KeyValueWithOperator("detailList",deList,"="));
								if(!discountList.isEmpty()){
									//discountString=String.format("%s(%s  %s)",discountList.get(0).getNewTotalFee(),discountList.get(0).getSysUserByRequesterId().getName(),DateTimeFormatUtil.DateTimeFormat.format(discountList.get(0).getApplyTime()));
								}
								jsonObj.put("DiscountString", discountString);
								jsonArray.put(jsonObj);	
							
						}
					}
				
				retJSO22.put("total", totalSize22);
				retJSO22.put("rows", jsonArray);
			} catch (Exception e) {
				
				try {
					retJSO22.put("total", 0);
					retJSO22.put("rows", new JSONArray());
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 22", e);
				}else{
					log.error("error in DetailListComServlet-->case 22", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSO22.toString());
			}
			
			break;	
		case 3:	//查找清单（双击清单列表）对应的委托单信息
			JSONObject retJSON3 = new JSONObject();
			int totalSize3 = 0;
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String DetailListCode = req.getParameter("DetailListCode");	
				
				CommissionSheetManager csheetMgr = new CommissionSheetManager();
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
				condList.add(new KeyValueWithOperator("status", 10, "<>"));//选择委托单尚未注销的	
				if(DetailListCode!= null && DetailListCode.length() >=0){
					
					condList.add(new KeyValueWithOperator("detailListCode",DetailListCode, "="));	
					totalSize3 = csheetMgr.getTotalCount(condList);				
					List<CommissionSheet> retList = csheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, condList);
					
					if(retList != null && retList.size() > 0){
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						for(CommissionSheet cSheet : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("Id", cSheet.getId());
							jsonObj.put("DetailListCode", cSheet.getDetailListCode());
							jsonObj.put("Code", cSheet.getCode());
							jsonObj.put("Pwd", cSheet.getPwd());
							jsonObj.put("CustomerId", cSheet.getCustomerId());
							jsonObj.put("CustomerName", cSheet.getCustomerName());
							jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
							if(cSheet.getSpeciesType()){	//器具授权（分类）名称
								jsonObj.put("SpeciesType", 1);	//器具分类类型
								ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
								if(spe != null){
									jsonObj.put("ApplianceSpeciesName", spe.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", spe.getStatus());
								}else{
									continue;
								}
							}else{	//器具标准名称
								jsonObj.put("SpeciesType", 0);
								ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
								if(stName != null){
									jsonObj.put("ApplianceSpeciesName", stName.getName());
									jsonObj.put("ApplianceSpeciesNameStatus", stName.getStatus());
								}else{
									continue;
								}
							}
							jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
							jsonObj.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
							jsonObj.put("ReportType", cSheet.getReportType());	//报告形式
							jsonObj.put("FinishLocation", cSheet.getFinishLocation()==null?"":cSheet.getFinishLocation());	//存放位置
							jsonObj.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
							jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
							jsonObj.put("Status", cSheet.getStatus());	//委托单状态
							
							/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
							List<Object[]> FList=feeMgr.findByHQL(queryStringTotalFeeByCommissionSheetId, cSheet.getId());												
							if(FList.isEmpty()){
						    	jsonObj.put("TestFee", 0.0);
								jsonObj.put("RepairFee", 0.0);
								jsonObj.put("MaterialFee", 0.0);
								jsonObj.put("CarFee", 0.0);
								jsonObj.put("DebugFee", 0.0);
								jsonObj.put("OtherFee", 0.0);
								jsonObj.put("TotalFee", 0.0);
						    }else{
							    for(Object[] fee:FList){							    	
									jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
									jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
									jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
									jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
									jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
									jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
									jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
							   }
						    }
							jsonArray.put(jsonObj);	
						}
					}	
					retJSON3.put("total", totalSize3);
					retJSON3.put("rows", jsonArray);
				}else{
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				}
			} catch (Exception e) {
				
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 3", e);
				}else{
					log.error("error in DetailListComServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			
			break;
		case 5:	//打印所选清单的委托单费用信息
			JSONObject retObj2 = new JSONObject();
			int totalSize5 = 0;
			try {

				/*** 打印所选清单的委托单费用信息 **/
				int DetailListId = Integer.parseInt(req.getParameter("DetailListId")); // 清单ID
				
				String DetailListType = req.getParameter("DetailListType"); // 打印或者预览，有值代表预览
				
				CommissionSheetManager cSheetMgr1 = new CommissionSheetManager();	
				WithdrawManager withdrawMgr = new WithdrawManager();
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				
				DetailListManager deListMgr = new DetailListManager();	
				DetailList deListRet = deListMgr.findById(DetailListId);

				List<CommissionSheet> comList = cSheetMgr1.findByPropertyBySort("code",true,new KeyValueWithOperator("detailListCode", deListRet.getCode(), "="));
				
				if(comList != null && comList.size() > 0){
					totalSize5=comList.size();
					int idid=1;
					String customerName="";
					JSONArray jsonArray = new JSONArray();
					int ApplianceNumber=0;//器具数量
					int TQuantity=0;//退样数量
					int TQuantitytotal=0;//退样数量总计
					double JDfee=0;//检定费合计
					double testfee=0;//原始记录检定费
					CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					for(CommissionSheet comsheet : comList){
						JSONObject jsonObj = new JSONObject();
						
						customerName=comsheet.getCustomerName();
						//System.out.println(customerName);
						jsonObj.put("Id", idid++);
						jsonObj.put("CommissionSheetCode", comsheet.getCode()); // 委托单Code
						jsonObj.put("ApplianceName", comsheet.getApplianceName()); // 器具名称
						jsonObj.put("Quantity", comsheet.getQuantity()); // 数量
						jsonObj.put("FinishLocation", comsheet.getFinishLocation()); // 存放位置
						
						ApplianceNumber = ApplianceNumber+comsheet.getQuantity();
						/********************查看退样数量************************/
						TQuantity=0;
						List<Withdraw> retList = withdrawMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet", comsheet, "="),new KeyValueWithOperator("executeResult",true,"="));
						if(retList != null && retList.size() > 0){
							for(Withdraw w:retList){
								TQuantity+=(w.getNumber()==null?0:w.getNumber());
							}
						}
						jsonObj.put("TQuantity",TQuantity); // 退样数量
						TQuantitytotal+=TQuantity;
						testfee=0;
						/*****************判断原始记录检验类型是否是检定****************************/
						List<OriginalRecord> oRecRetList = oRecordMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet",comsheet, "="), new KeyValueWithOperator("status", 1, "<>"), new KeyValueWithOperator("certificate.pdf", null, "is not null"), new KeyValueWithOperator("taskAssign.status", 1, "<>"));
						if(oRecRetList != null && oRecRetList.size() > 0){		
							for(OriginalRecord o:oRecRetList){
								if(o.getWorkType()!=null && o.getWorkType().equals("检定")){
									List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllFeeByOriginalRecordIdAndCertificateId, o.getId(),o.getCertificate().getId());	
									if(FList!=null&&FList.size()>0){									   									    
									    	Object[] fee=FList.get(0);
											testfee+=(fee[0]==null?0.0:(Double)fee[0]);
								     }
									
								}
							}
						}
						
						JDfee+=testfee;
						/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
						List<Object[]> FList=feeMgr.findByHQL(queryStringTotalFeeByCommissionSheetId, comsheet.getId());												
						if(FList.isEmpty()){
					    	jsonObj.put("TestFee", 0.0);
							jsonObj.put("RepairFee", 0.0);
							jsonObj.put("MaterialFee", 0.0);
							jsonObj.put("CarFee", 0.0);
							jsonObj.put("DebugFee", 0.0);
							jsonObj.put("OtherFee", 0.0);
							jsonObj.put("TotalFee", 0.0);
					    }else{
						    for(Object[] fee:FList){							    	
								jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
								jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
								jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
								jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
								jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
								jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
								jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
						   }
					    }
						jsonArray.put(jsonObj);
					}
					/*int temp=idid;
					for(int m=0;m<19-temp;m++){
						JSONObject jsonObj = new JSONObject();
						
						jsonObj.put("Id", idid++);
						jsonObj.put("CommissionSheetCode","" ); // 委托单Code
						jsonObj.put("ApplianceName", ""); // 器具名称
						jsonObj.put("Quantity", ""); // 数量
						jsonObj.put("FinishLocation",""); // 存放位置
					
						jsonObj.put("TQuantity",""); // 退样数量
						
				    	jsonObj.put("TestFee", 0.0);
						jsonObj.put("RepairFee", 0.0);
						jsonObj.put("MaterialFee", 0.0);
						jsonObj.put("CarFee", 0.0);
						jsonObj.put("DebugFee", 0.0);
						jsonObj.put("OtherFee", 0.0);
						jsonObj.put("TotalFee", 0.0);
					   
						jsonArray.put(jsonObj);
					}*/
										
					retObj2.put("rows",jsonArray);
					retObj2.put("total", totalSize5);
					
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
					retObj2.put("DetailListDate",deListRet.getStatus()==2?sf.format(deListRet.getLastEditTime()):"");//结账日期
					java.util.Date now = new java.util.Date();
					retObj2.put("NowDate", sf.format(now));
					
					retObj2.put("DetailListSysUser",deListRet.getStatus()==2?deListRet.getSysUser().getName():"");//结账人
					//retObj2.put("DetailListSysUser","sssssss");//结账人
					retObj2.put("DetailListCode",deListRet.getCode());
					retObj2.put("DetailListInvoiceCode",deListRet.getInvoiceCode()==null?"":deListRet.getInvoiceCode());
					retObj2.put("DetailListTotalFee",deListRet.getTotalFee()==null?0:deListRet.getTotalFee());
					
					retObj2.put("ApplianceNumber", ApplianceNumber-TQuantitytotal);//检测的器具总数（已经减去了退样的）
					retObj2.put("CustomerName", customerName);
					retObj2.put("JDfee", JDfee);//检定费
					retObj2.put("QTfee", deListRet.getTotalFee()==null?0:deListRet.getTotalFee()-JDfee);//其他费
					retObj2.put("IsOK", true);
					
					if(DetailListType!=null){
						retObj2.put("DetailListType", "YL");
					}else{
						retObj2.put("DetailListType", "DY");
					}
						
				}
				req.getSession().setAttribute("FeeList", retObj2);
				
				resp.sendRedirect("/jlyw/FeeManage/FeeListLook.jsp");
				//resp.sendRedirect("/jlyw/FeeManage/PrintFeeList.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				try{
					retObj2.put("IsOK", false);
					retObj2.put("msg", String.format("操作失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DetailListComServlet-->case 5", e);
				}else{
					log.error("error in DetailListComServlet-->case 5", e);
				}
				req.getSession().setAttribute("FeeList", retObj2);
				resp.sendRedirect("/jlyw/FeeManage/FeeListLook.jsp");
			}finally{}
		
			break;
		case 6:	//打印所选清单的详细费用信息（原始记录）
			JSONObject retObj6 = new JSONObject();
			
			int totalSize6 = 0;
			try {
				/*** 打印所选清单的所有原始记录费用信息 **/
				int DetailListId = Integer.parseInt(req.getParameter("DetailListId")); // 清单ID			
				String DetailListType = req.getParameter("DetailListType"); // 打印或者预览，有值代表预览
				CommissionSheetManager cSheetMgr1 = new CommissionSheetManager();	
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				
				DetailListManager deListMgr = new DetailListManager();	
				DetailList deListRet = deListMgr.findById(DetailListId);
			
				List<CommissionSheet> comList = cSheetMgr1.findByPropertyBySort("code",true,new KeyValueWithOperator("detailListCode", deListRet.getCode(), "="));
				
				if(comList != null && comList.size() > 0){
				totalSize6=comList.size();
				int idid=1;
				String customerName="";
				JSONArray jsonArray = new JSONArray();
				
				int ApplianceNumber=0;//器具数量
			
				int TQuantitytotal=0;//退样数量总计
				double JDfee=0;//检定费合计
				double testfee=0;//原始记录检定费
				
				double testfee1=0;//原始记录检测费
				double materialfee=0;//
				double repairfee=0;//
				double carfee=0;//
				double debugfee=0;//
				double otherfee=0;//
				double totalfee=0;//
				for(CommissionSheet comSheet : comList){					
					customerName = comSheet.getCustomerName();
					ApplianceNumber = ApplianceNumber + comSheet.getQuantity();
					/*****************原始记录费用信息****************************/
					List<OriginalRecord> oRecRetList = oRecordMgr.findByPropertyBySort("certificate.certificateCode",true,new KeyValueWithOperator("commissionSheet",comSheet, "="), new KeyValueWithOperator("status", 1, "<>"), new KeyValueWithOperator("certificate.pdf", null, "is not null"), new KeyValueWithOperator("taskAssign.status", 1, "<>"));
					CertificateFeeAssignManager feeMgr = new CertificateFeeAssignManager();
					if(oRecRetList != null && oRecRetList.size() > 0){		
						for(OriginalRecord o:oRecRetList){//有证书（）
							testfee=0;
							JSONObject jsonObj = new JSONObject();
							/***********在CertificateFeeAssign(原始记录证书费用分配)中查找原始记录的费用详情***********/
							List<Object[]> FList= feeMgr.findByHQL(queryStringTotalFeeByOriginalRecordIdAndCertificateId, o.getId(), o.getCertificate().getId());		
							if(!FList.isEmpty()){
					    	    Object[] fee=FList.get(0);						    	
					    	    testfee1 =(fee[0]==null?0.0:(Double)fee[0]);
					    	    repairfee = (fee[1]==null?0.0:(Double)fee[1]);
					    	    materialfee = (fee[2]==null?0.0:(Double)fee[2]);
					    	    carfee = (fee[3]==null?0.0:(Double)fee[3]);
					    	    debugfee = (fee[4]==null?0.0:(Double)fee[4]);
					    	    otherfee = (fee[5]==null?0.0:(Double)fee[5]);
					    	    totalfee = (fee[6]==null?0.0:(Double)fee[6]);	
					    	    
					    	    if(o.getWorkType()!=null&&o.getWorkType().equals("检定")){
									testfee=(fee[0]==null?0.0:(Double)fee[0]);//检定费
								}
						    }
							jsonObj.put("TestFee", (int)testfee1);
							jsonObj.put("RepairFee", (int)materialfee);
							jsonObj.put("MaterialFee",(int)repairfee);
							jsonObj.put("CarFee", (int)carfee);
							jsonObj.put("DebugFee",(int)debugfee);
							jsonObj.put("OtherFee", (int)otherfee);
							jsonObj.put("TotalFee", (int)totalfee);	
							
							jsonObj.put("Id", idid++);
							jsonObj.put("Quantity", o.getQuantity());
							jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode()); // 证书号
							jsonObj.put("ApplianceName", o.getApplianceName()==null?"":o.getApplianceName()); // 器具名称
							jsonObj.put("FinishLocation", comSheet.getFinishLocation()); // 存放位置
							jsonArray.put(jsonObj);
							
							JDfee+=testfee;
						}
						
					}
					//无证书（ 转包费  ）
					List<CertificateFeeAssign> feeAssignList = feeMgr.findByVarProperty(
							new KeyValueWithOperator("commissionSheet.id", comSheet.getId(), "="),
							new KeyValueWithOperator("originalRecord", null, "is null"),
							new KeyValueWithOperator("certificate", null, "is null"));
					for(CertificateFeeAssign feeAssign : feeAssignList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("TestFee", feeAssign.getTestFee()==null?0:feeAssign.getTestFee());
						jsonObj.put("RepairFee",  feeAssign.getRepairFee()==null?0:feeAssign.getRepairFee());
						jsonObj.put("MaterialFee", feeAssign.getMaterialFee()==null?0:feeAssign.getMaterialFee());
						jsonObj.put("CarFee",  feeAssign.getCarFee()==null?0:feeAssign.getCarFee());
						jsonObj.put("DebugFee", feeAssign.getDebugFee()==null?0:feeAssign.getDebugFee());
						jsonObj.put("OtherFee",  feeAssign.getOtherFee()==null?0:feeAssign.getOtherFee());
						jsonObj.put("TotalFee",  feeAssign.getTotalFee()==null?0:feeAssign.getTotalFee());	
						
						jsonObj.put("Id", idid++);
						jsonObj.put("Quantity", "");
						jsonObj.put("CertificateCode", ""); // 证书号
						jsonObj.put("ApplianceName", comSheet.getApplianceName()); // 器具名称
						jsonObj.put("FinishLocation", comSheet.getFinishLocation()); // 存放位置
						jsonArray.put(jsonObj);
					}		
							
				}
				
				
				retObj6.put("rows",jsonArray);
				retObj6.put("total", totalSize6);
		
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				retObj6.put("DetailListDate",deListRet.getStatus()==2?sf.format(deListRet.getLastEditTime()):"");//结账日期
				
				java.util.Date now = new java.util.Date();
				retObj6.put("NowDate", sf.format(now));
				
				retObj6.put("DetailListSysUser",deListRet.getStatus()==2?deListRet.getSysUser().getName():"");//结账人
				//retObj2.put("DetailListSysUser","sssssss");//结账人
				retObj6.put("DetailListCode",deListRet.getCode());
				retObj6.put("DetailListInvoiceCode",deListRet.getInvoiceCode()==null?"":deListRet.getInvoiceCode());
				retObj6.put("DetailListTotalFee",deListRet.getTotalFee()==null?0:deListRet.getTotalFee());
				
				retObj6.put("ApplianceNumber", ApplianceNumber-TQuantitytotal);//检测的器具总数（已经减去了退样的）
				retObj6.put("CustomerName", customerName);
				retObj6.put("JDfee", JDfee);//检定费
				retObj6.put("QTfee", deListRet.getTotalFee()==null?0:deListRet.getTotalFee()-JDfee);//其他费
				retObj6.put("IsOK", true);
				
				if(DetailListType!=null){
					retObj6.put("DetailListType", "YL");
				}else{
					retObj6.put("DetailListType", "DY");
				}
			}
			req.getSession().setAttribute("FeeList", retObj6);
			//System.out.println("333");
			resp.sendRedirect("/jlyw/FeeManage/DetailFeeListLook.jsp");
		} catch (Exception e) {
			try{
				retObj6.put("IsOK", false);
				retObj6.put("msg", String.format("操作失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
			}catch(Exception ee){}
			if(e.getClass() == java.lang.Exception.class){	//自定义的消息
				log.debug("exception in DetailListComServlet-->case 6", e);
			}else{
				log.error("error in DetailListComServlet-->case 6", e);
			}
			req.getSession().setAttribute("FeeList", retObj6);
			resp.sendRedirect("/jlyw/FeeManage/DetailFeeListLook.jsp");

		}finally{}
		break;
		case 7://根据清单号，注销清单
			JSONObject retObj13 = new JSONObject();
			try{
				String DetailListId = req.getParameter("DetailListId");
				
				if(DetailListId == null || DetailListId.length() == 0){
					throw new Exception("清单号未指定！");
				}
				DetailListManager deListMgr = new DetailListManager();	
				DetailList deListRet = deListMgr.findById(Integer.parseInt(DetailListId));
				
				if(deListRet.getStatus()==1){   //注销
					throw new Exception("该清单已注销 ！");
				}else if(deListRet.getStatus()==0){  //正常			
					deListMgr.logOutDetailList(deListRet);
				}else {
					
					List<CommissionSheet> cSheetList = cSheetMgr.findByVarProperty(new KeyValueWithOperator("detailListCode", deListRet.getCode(), "="),new KeyValueWithOperator("status", CommissionSheetStatus.Status_YiJieZhang, "="));
					
					if(cSheetList!=null&&cSheetList.size()>0){
						for(CommissionSheet cSheet:cSheetList){
							if(cSheet == null){
								throw new Exception("找不到指定的委托单！");
							}
							if(cSheet.getCheckOutDate() == null){
								throw new Exception("该委托单尚未结账，不能执行结账挡回！");
							}
							SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
							cSheetMgr.checkOutReject(cSheet, loginUser);
						}
					}
					
					List<CommissionSheet> cSheetList2 = cSheetMgr.findByVarProperty(new KeyValueWithOperator("detailListCode", deListRet.getCode(), "="),new KeyValueWithOperator("status", CommissionSheetStatus.Status_YiJieZhang, "="));
					if(cSheetList2==null||cSheetList2.size()==0){
						deListRet.setStatus(1);
						deListMgr.update(deListRet);
					}
				}
				retObj13.put("IsOK", true);
				retObj13.put("msg", "挡回成功！");
			}catch(Exception e){
				try {
					retObj13.put("IsOK", false);
					retObj13.put("msg", String.format("挡回失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CommissionSheetServlet-->case 7", e);
				}else{
					log.error("error in CommissionSheetServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj13.toString());
			}
			break;
		}
	}

}
