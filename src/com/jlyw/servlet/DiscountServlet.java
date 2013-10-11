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
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DiscountComSheetManager;
import com.jlyw.manager.DiscountManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class DiscountServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(DiscountServlet.class);
	private static String ClassName = "DiscountServlet";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		DiscountManager discountMgr = new DiscountManager();
		DiscountComSheetManager discountcomSheetMgr=new DiscountComSheetManager();
		switch (method) {
		case 0: // 按折扣申请ID查找折扣申请信息
			JSONObject retJSON = new JSONObject();
			try {
				String DiscountId = req.getParameter("DiscountId");	//折扣申请Id
				
				Discount o = discountMgr.findById(Integer.parseInt(DiscountId));
				if(o != null ){
					JSONArray jsonArray = new JSONArray();
					
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", o.getId());
						jsonObj.put("CustomerName", o.getCustomer()==null?"":o.getCustomer().getName());//  
						jsonObj.put("Contector", o.getContector()==null?"":o.getContector());//  委托方经办人
						jsonObj.put("ContectorTel", o.getContectorTel()==null?"":o.getContectorTel());//经办人联系电话
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//申请人
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//申请时间
						jsonObj.put("Reason", o.getReason().getReason());	//原因
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//超期办理人
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//办理结果
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//办理时间
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//办理备注信息
						
						jsonArray.put(jsonObj);
					
					
					retJSON.put("total", 1);
					retJSON.put("rows", jsonArray);
				}else{
					throw new Exception("");
				}
			} catch (Exception e){
				
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 0", e);
				}else{
					log.error("error in DiscountServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//折扣审批（办理）
			JSONObject retJSON1 = new JSONObject();
			try {
				String DiscountId = req.getParameter("DiscountId");	//折扣申请Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//办理结果：0不通过；1通过
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//办理的备注信息
				//System.out.println(DiscountId);
				Discount discount = discountMgr.findById(Integer.parseInt(DiscountId));
				if(discount != null){
					if(discount.getExecuteTime() != null){
						throw new Exception("该折扣申请已经审批，不能重复操作！");
					}
					discount.setExecuteMsg(ExecuteMsg);
					discount.setExecuteResult(Integer.parseInt(ExecutorResult)>0?true:false);
					discount.setExecuteTime(new Timestamp(System.currentTimeMillis()));
					discount.setSysUserByExecutorId((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));	//办理人
					if(discount.getExecuteResult()){	//审批通过，将费用写入到委托单的证书费用分配表（表CertificateFeeAssign中）
						DiscountComSheetManager disComMgr = new DiscountComSheetManager();
						List<DiscountComSheet> disCountList = disComMgr.findByVarProperty(new KeyValueWithOperator("discount.id",Integer.parseInt(DiscountId),"="),new KeyValueWithOperator("commissionSheet.status",FlagUtil.CommissionSheetStatus.Status_YiWanGong,"="));
						if(disCountList==null||disCountList.size()==0)
							throw new Exception("该委托单不是“已完工”！");						
						discountMgr.discountExecute(discount);
					}else{	//审批不通过
						discountMgr.update(discount);
					}
					retJSON1.put("IsOK", true);
				}else{
					throw new Exception("该折扣申请单不存在！");
				}
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("折扣审批（办理）失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 1", e);
				}else{
					log.error("error in DiscountServlet-->case1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:		//折扣申请
			JSONObject retJSON2 = new JSONObject();
			try {
				String comSheets = req.getParameter("comSheets");
				String Reason = req.getParameter("Reasons");	//折扣原因
				String Contector = req.getParameter("Contector");	//委托方经办人
				String ContectorTel = req.getParameter("ContectorTel");	//
				List<DiscountComSheet> discountComSheetList=new ArrayList<DiscountComSheet>();//折扣-委托单
				JSONArray JsonArray = new JSONArray(comSheets);
				Integer cusId = null;
				Timestamp today = new Timestamp(System.currentTimeMillis());
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				for(int i=0; i<JsonArray.length(); i++){//生成折扣-委托单的List
					JSONObject jsonObj = JsonArray.getJSONObject(i);
					DiscountComSheet discountComSheet=new DiscountComSheet();
					CommissionSheet com = cSheetMgr.findById(Integer.parseInt(jsonObj.getString("Id")));
					int iRet = cSheetMgr.getTotalCountByHQL("select count(*) "+CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId, com.getId());
					if(iRet == 0){
						throw new Exception(String.format("委托单%s没有录入任何费用，不能执行折扣操作！", com.getCode()));
					}
					discountComSheet.setCommissionSheet(com);
					cusId = com.getCustomerId();
					discountComSheet.setOldTestFee(Double.parseDouble(jsonObj.getString("OldTestFee").trim()));//
					discountComSheet.setTestFee(Double.parseDouble(jsonObj.getString("TestFee").trim()));
					discountComSheet.setOldRepairFee(Double.parseDouble(jsonObj.getString("OldRepairFee").trim()));//
					discountComSheet.setRepairFee(Double.parseDouble(jsonObj.getString("RepairFee").trim()));
					discountComSheet.setOldMaterialFee(Double.parseDouble(jsonObj.getString("OldMaterialFee").trim()));//
					discountComSheet.setMaterialFee(Double.parseDouble(jsonObj.getString("MaterialFee").trim()));
					discountComSheet.setOldCarFee(Double.parseDouble(jsonObj.getString("OldCarFee").trim()));//
					discountComSheet.setCarFee(Double.parseDouble(jsonObj.getString("CarFee").trim()));
					discountComSheet.setOldDebugFee(Double.parseDouble(jsonObj.getString("OldDebugFee").trim()));//
					discountComSheet.setDebugFee(Double.parseDouble(jsonObj.getString("DebugFee").trim()));
					discountComSheet.setOldOtherFee(Double.parseDouble(jsonObj.getString("OldOtherFee").trim()));//
					discountComSheet.setOtherFee(Double.parseDouble(jsonObj.getString("OtherFee").trim()));
					discountComSheet.setOldTotalFee(Double.parseDouble(jsonObj.getString("OldTotalFee").trim()));//
					discountComSheet.setTotalFee(Double.parseDouble(jsonObj.getString("TotalFee").trim()));
					
					discountComSheetList.add(discountComSheet);
				}			
				//存折扣申请
				Discount r = new Discount();
				r.setSysUserByRequesterId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//申请人
				r.setApplyTime(today);	//申请时间
				r.setContector(Contector);
				r.setContectorTel(ContectorTel);
				Customer cus=(new CustomerManager()).findById(cusId);	
				r.setCustomer(cus);
				//存折扣原因
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_Discount, "="));//查找折扣原因
				if(rList!=null&&rList.size() > 0){	//更新原因
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					r.setReason(reason);	//折扣原因
				}else{	//新建原因
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(FlagUtil.ReasonType.Type_Discount);	//折扣
					rMgr.save(reason);
					r.setReason(reason);	//折扣原因
				}

				if((new DiscountComSheetManager()).saveByBatch(discountComSheetList,r)){
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("折扣申请提交失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 2", e);
				}else{
					log.error("error in DiscountServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //查询折扣审批任务列表
			JSONObject retJSON3 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int total=0;
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());

				String CustomerName  = req.getParameter("CustomerName");					
				String Code = req.getParameter("Code");	//委托单号
				//String FeeCode = req.getParameter("FeeCode");	//费用清单号
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				String Status = req.getParameter("Status");
				List<Object> keys = new ArrayList<Object>();
				if(Status==null||Status.length()==0){
					Status="";
				}
				
				String QueryHQL = " from Discount as d where  d.id not in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.status=10) ";
				if(CustomerName != null && CustomerName.length() > 0){
					String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					QueryHQL = QueryHQL+" and d.customer.name = ? ";
					keys.add(cusName);
				}
				if(Code != null && Code.length() > 0){
					QueryHQL=QueryHQL + " and d.id in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.code like ?) " ;
					keys.add("%"+Code+"%");
				}
				if(DateFrom != null && DateFrom.length() > 0){
					Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
					QueryHQL=QueryHQL + " and d.id in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.commissionDate >= ?) " ;
					keys.add(Start);
				}
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
					QueryHQL=QueryHQL + " and d.id in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.commissionDate <= ?) " ;
					keys.add(End);
				}
				if(Status!= null&& Status.length() > 0){
					if(Status.equals("1")){//已审批
						QueryHQL = QueryHQL+"and d.executeTime is not null ";
					}
					if(Status.equals("0")){//未审批
						QueryHQL = QueryHQL+"and d.executeTime is null ";
					}
				}
				
				List<Discount> tRetList = discountcomSheetMgr.findPageAllByHQL(QueryHQL+"order by d.applyTime desc", page, rows, keys);
				total = discountcomSheetMgr.getTotalCountByHQL("select count(*) "+QueryHQL, keys);
				if(tRetList!=null){
					
					for(Discount o : tRetList){		
						JSONObject jsonObj= new JSONObject();
						jsonObj.put("Id", o.getId());
						jsonObj.put("CustomerName", o.getCustomer().getName());
						jsonObj.put("Contector", o.getContector()==null?"":o.getContector());//  委托方经办人
						jsonObj.put("ContectorTel", o.getContectorTel()==null?"":o.getContectorTel());//经办人联系电话
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//申请人
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//申请时间
						jsonObj.put("Reason", o.getReason().getReason());	//原因
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//办理人
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//办理结果
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//办理时间
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//办理备注信息
						
						jsonArray.put(jsonObj);
				
					}
				}
				retJSON3.put("total", total);
				retJSON3.put("rows", jsonArray);

			} catch (Exception e){
				e.printStackTrace();
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 3", e);
				}else{
					log.error("error in DiscountServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4: // 按费用清单ID查找的折扣申请信息
			JSONObject retJSON4 = new JSONObject();
			try {
				if(req.getParameter("DetailListId") == null || req.getParameter("DetailListId").length() == 0){
					throw new Exception("费用清单Id为空！");
				}
				int DetailListId =Integer.parseInt(req.getParameter("DetailListId"));	//费用清单Id
				int total=0;
				List<Discount> retList = discountMgr.findByVarProperty(new KeyValueWithOperator("detailList.id", DetailListId, "="));
				JSONArray jsonArray = new JSONArray();
				if(retList != null && retList.size() > 0){
					total=discountMgr.getTotalCount(new KeyValueWithOperator("detailList.id", DetailListId, "="));
					for(Discount o : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("DiscountId", o.getId());
						
						jsonObj.put("Contector", o.getContector()==null?"":o.getContector());//  委托方经办人
						jsonObj.put("ContectorTel", o.getContectorTel()==null?"":o.getContectorTel());//经办人联系电话
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//申请人
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//申请时间
						jsonObj.put("Reason", o.getReason().getReason());	//原因
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//超期办理人
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//办理结果
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//办理时间
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//办理备注信息
						
						jsonArray.put(jsonObj);
						
					}
				}
				retJSON4.put("total", total);
				retJSON4.put("rows", jsonArray);
					
			} catch (Exception e){
				
				try {
					retJSON4.put("total", 0);
					retJSON4.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 4", e);
				}else{
					log.error("error in DiscountServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5://根据委托单号查询折扣信息
			JSONObject retJSON5 = new JSONObject();
			try {
				if(req.getParameter("CommissionId") == null || req.getParameter("CommissionId").length() == 0){
					throw new Exception("委托单Id为空！");
				}
				int CommissionId =Integer.parseInt(req.getParameter("CommissionId"));	//委托单Id
				int total=0;
				ApplianceStandardNameManager appStandMgr=new ApplianceStandardNameManager();
				ApplianceSpeciesManager appSpeMgr=new ApplianceSpeciesManager();
				List<DiscountComSheet> retList = discountcomSheetMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", CommissionId, "="));
				total = discountcomSheetMgr.getTotalCount(new KeyValueWithOperator("commissionSheet.id", CommissionId, "="));
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				JSONArray jsonArray = new JSONArray();
				if(retList != null && retList.size() > 0){
					for(DiscountComSheet o : retList){
						JSONObject jsonObj = new JSONObject();
						
						jsonObj.put("Contactor", o.getDiscount().getContector()==null?"":o.getDiscount().getContector());//  委托方经办人
						jsonObj.put("ContactorTel", o.getDiscount().getContectorTel()==null?"":o.getDiscount().getContectorTel());//经办人联系电话
						jsonObj.put("RequesterId", o.getDiscount().getSysUserByRequesterId().getId());	//申请人
						jsonObj.put("RequesterName", o.getDiscount().getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getDiscount().getApplyTime()));	//申请时间
						jsonObj.put("Reason", o.getDiscount().getReason().getReason());	//原因
						jsonObj.put("ExecutorId", o.getDiscount().getSysUserByExecutorId()==null?null:o.getDiscount().getSysUserByExecutorId().getId());	//超期办理人
						jsonObj.put("ExecutorName", o.getDiscount().getSysUserByExecutorId()==null?"":o.getDiscount().getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getDiscount().getExecuteResult()==null?-1:(o.getDiscount().getExecuteResult()?1:0));	//办理结果
						jsonObj.put("ExecuteTime", o.getDiscount().getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getDiscount().getExecuteTime()));	//办理时间
						jsonObj.put("ExecuteMsg", o.getDiscount().getExecuteMsg()==null?"":o.getDiscount().getExecuteMsg());	//办理备注信息
						
						CommissionSheet cSheet=o.getCommissionSheet();
						jsonObj.put("Id", cSheet.getId());
						//System.out.println(cSheet.getId());
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Pwd", cSheet.getPwd());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
						if(cSheet.getSpeciesType()){	//器具授权（分类）名称
							jsonObj.put("SpeciesType", 1);	//器具分类类型
							ApplianceSpecies spe = appSpeMgr.findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								jsonObj.put("ApplianceSpeciesName", spe.getName());
							}else{
								continue;
							}
						}else{	//器具标准名称
							jsonObj.put("SpeciesType", 0);
							ApplianceStandardName stName = appStandMgr.findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								jsonObj.put("ApplianceSpeciesName", stName.getName());
							}else{
								continue;
							}
						}
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）							
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期								
						jsonObj.put("Status", cSheet.getStatus());
						
						jsonObj.put("TestFee", o.getTestFee());
						jsonObj.put("RepairFee", o.getRepairFee());
						jsonObj.put("MaterialFee",o.getMaterialFee());
						jsonObj.put("CarFee", o.getCarFee());
						jsonObj.put("DebugFee", o.getDebugFee());
						jsonObj.put("OtherFee", o.getOtherFee());
						jsonObj.put("TotalFee", o.getTotalFee());
						
						jsonObj.put("OldTestFee", o.getOldTestFee());
						jsonObj.put("OldRepairFee", o.getOldRepairFee());
						jsonObj.put("OldMaterialFee",o.getOldMaterialFee());
						jsonObj.put("OldCarFee", o.getOldCarFee());
						jsonObj.put("OldDebugFee", o.getOldDebugFee());
						jsonObj.put("OldOtherFee", o.getOldOtherFee());
						jsonObj.put("OldTotalFee", o.getOldTotalFee());
						
						jsonArray.put(jsonObj);
						
					}
				}
				retJSON5.put("total", total);
				retJSON5.put("rows", jsonArray);
					
			} catch (Exception e){
				
				try {
					retJSON5.put("total", 0);
					retJSON5.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 5", e);
				}else{
					log.error("error in DiscountServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6://根据折扣单ID查询委托单信息（折扣任务时使用）
			JSONObject retJSON6 = new JSONObject();
			try {
				if(req.getParameter("DiscountId") == null || req.getParameter("DiscountId").length() == 0){
					throw new Exception("折扣Id为空！");
				}
				int DiscountId =Integer.parseInt(req.getParameter("DiscountId"));	//委托单Id
				
				int total=0;
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				
				condList.add(new KeyValueWithOperator("discount.id", DiscountId, "="));//选择委托单尚未注销的	
				
				List<DiscountComSheet> retList = discountcomSheetMgr.findByVarProperty(condList);
				ApplianceStandardNameManager appStandMgr=new ApplianceStandardNameManager();
				ApplianceSpeciesManager appSpeMgr=new ApplianceSpeciesManager();
				total=discountcomSheetMgr.getTotalCount(condList);
				JSONArray jsonArray = new JSONArray();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0,TotalFee=0.0;
				double OldTestFee=0.0,OldRepairFee=0.0,OldMaterialFee=0.0,OldCarFee=0.0,OldDebugFee=0.0,OldOtherFee=0.0,OldTotalFee=0.0;
				if(retList != null && retList.size() > 0){
					//System.out.println(retList.size());
					for(DiscountComSheet discom : retList){
						JSONObject jsonObj = new JSONObject();
						CommissionSheet cSheet=discom.getCommissionSheet();
						jsonObj.put("Id", cSheet.getId());
						//System.out.println(cSheet.getId());
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Pwd", cSheet.getPwd());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
						if(cSheet.getSpeciesType()){	//器具授权（分类）名称
							jsonObj.put("SpeciesType", 1);	//器具分类类型
							ApplianceSpecies spe = appSpeMgr.findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								jsonObj.put("ApplianceSpeciesName", spe.getName());
							}else{
								continue;
							}
						}else{	//器具标准名称
							jsonObj.put("SpeciesType", 0);
							ApplianceStandardName stName = appStandMgr.findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								jsonObj.put("ApplianceSpeciesName", stName.getName());
							}else{
								continue;
							}
						}
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）							
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期								
						jsonObj.put("Status", cSheet.getStatus());
						
						jsonObj.put("TestFee", discom.getTestFee());
						jsonObj.put("RepairFee", discom.getRepairFee());
						jsonObj.put("MaterialFee",discom.getMaterialFee());
						jsonObj.put("CarFee", discom.getCarFee());
						jsonObj.put("DebugFee", discom.getDebugFee());
						jsonObj.put("OtherFee", discom.getOtherFee());
						jsonObj.put("TotalFee", discom.getTotalFee());
						
						jsonObj.put("OldTestFee", discom.getOldTestFee());
						jsonObj.put("OldRepairFee", discom.getOldRepairFee());
						jsonObj.put("OldMaterialFee",discom.getOldMaterialFee());
						jsonObj.put("OldCarFee", discom.getOldCarFee());
						jsonObj.put("OldDebugFee", discom.getOldDebugFee());
						jsonObj.put("OldOtherFee", discom.getOldOtherFee());
						jsonObj.put("OldTotalFee", discom.getOldTotalFee());
						
						TestFee=TestFee+discom.getTestFee();
						RepairFee=RepairFee+discom.getRepairFee();
						MaterialFee=MaterialFee+discom.getMaterialFee();
						CarFee=CarFee+discom.getCarFee();
						DebugFee=DebugFee+discom.getDebugFee();
						OtherFee=OtherFee+discom.getOtherFee();
						TotalFee=TotalFee+discom.getTotalFee();
						
						OldTestFee=OldTestFee+discom.getOldTestFee();
						OldRepairFee=OldRepairFee+discom.getOldRepairFee();
						OldMaterialFee=OldMaterialFee+discom.getOldMaterialFee();
						OldCarFee=OldCarFee+discom.getOldCarFee();
						OldDebugFee=OldDebugFee+discom.getOldDebugFee();
						OldOtherFee=OldOtherFee+discom.getOldOtherFee();
						OldTotalFee=OldTotalFee+discom.getOldTotalFee();
						
						jsonArray.put(jsonObj);
						
					}				
				}

				JSONArray footerArray = new JSONArray();
				JSONObject footerObj = new JSONObject();
				footerObj.put("CustomerName", "总计");
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				
				footerObj.put("OldTestFee", OldTestFee);
				footerObj.put("OldRepairFee", OldRepairFee);
				footerObj.put("OldMaterialFee",OldMaterialFee);
				footerObj.put("OldCarFee", OldCarFee);
				footerObj.put("OldDebugFee", OldDebugFee);
				footerObj.put("OldOtherFee", OldOtherFee);
				footerObj.put("OldTotalFee", OldTotalFee);
				footerArray.put(footerObj);
				
				retJSON6.put("total", total);
				retJSON6.put("rows", jsonArray);
				retJSON6.put("footer", footerArray);
					
			} catch (Exception e){
				
				try {
					retJSON6.put("total", 0);
					retJSON6.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 6", e);
				}else{
					log.error("error in DiscountServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7://根据折扣单ID打印折扣单（打印折扣单时使用）
			JSONObject retJSON7 = new JSONObject();
			try {
				if(req.getParameter("discountId") == null || req.getParameter("discountId").length() == 0){
					throw new Exception("折扣Id为空！");
				}
				int DiscountId =Integer.parseInt(req.getParameter("discountId"));	//委托单Id
				
				int total=0;
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				
				condList.add(new KeyValueWithOperator("discount.id", DiscountId, "="));//选择委托单尚未注销的	
				
				List<DiscountComSheet> retList = discountcomSheetMgr.findByVarProperty(condList);
				total=discountcomSheetMgr.getTotalCount(condList);
				JSONArray jsonArray = new JSONArray();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				
				Discount discount=discountMgr.findById(DiscountId);
				String customer = (discount.getCustomer()==null?"":discount.getCustomer().getName());
				String date=(discount.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(discount.getExecuteTime()));
				if(retList != null && retList.size() > 0){
					
					for(DiscountComSheet discom : retList){
						JSONObject jsonObj = new JSONObject();
						CommissionSheet cSheet=discom.getCommissionSheet();
						jsonObj.put("Id", cSheet.getId());
						
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Pwd", cSheet.getPwd());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//器具分类Id(或器具标准名称ID)
						if(cSheet.getSpeciesType()){	//器具授权（分类）名称
							jsonObj.put("SpeciesType", 1);	//器具分类类型
							ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								jsonObj.put("ApplianceSpeciesName", spe.getName());
							}else{
								continue;
							}
						}else{	//器具标准名称
							jsonObj.put("SpeciesType", 0);
							ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								jsonObj.put("ApplianceSpeciesName", stName.getName());
							}else{
								continue;
							}
						}
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）							
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期								
						jsonObj.put("Status", cSheet.getStatus());
						
						jsonObj.put("TestFee", discom.getTestFee());
						jsonObj.put("RepairFee", discom.getRepairFee());
						jsonObj.put("MaterialFee",discom.getMaterialFee());
						jsonObj.put("CarFee", discom.getCarFee());
						jsonObj.put("DebugFee", discom.getDebugFee());
						jsonObj.put("OtherFee", discom.getOtherFee());
						jsonObj.put("TotalFee", discom.getTotalFee());
						
						jsonObj.put("OldTestFee", discom.getOldTestFee());
						jsonObj.put("OldRepairFee", discom.getOldRepairFee());
						jsonObj.put("OldMaterialFee",discom.getOldMaterialFee());
						jsonObj.put("OldCarFee", discom.getOldCarFee());
						jsonObj.put("OldDebugFee", discom.getOldDebugFee());
						jsonObj.put("OldOtherFee", discom.getOldOtherFee());
						jsonObj.put("OldTotalFee", discom.getOldTotalFee());
						
						
						jsonArray.put(jsonObj);
						
					}				
				}

				
				
				retJSON7.put("total", total);
				retJSON7.put("rows", jsonArray);
				retJSON7.put("CustomerName", customer);
				retJSON7.put("date", date);
			
				retJSON7.put("IsOK", true);
			
				req.getSession().setAttribute("DiscountList", retJSON7);
	
				resp.sendRedirect("/jlyw/FeeManage/DiscountPrint.jsp");
					
			} catch (Exception e){
				
				try{
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("操作失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in DiscountServlet-->case 7", e);
				}else{
					log.error("error in DiscountServlet-->case 7", e);
				}
				req.getSession().setAttribute("DiscountList", retJSON7);
				resp.sendRedirect("/jlyw/FeeManage/DiscountPrint.jsp");
			}finally{
				
			}
			break;
		
		}
	}

}
