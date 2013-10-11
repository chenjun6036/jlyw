package com.jlyw.servlet.statistic;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.view.ViewCommissionSheetFee;
import com.jlyw.hibernate.view.ViewTransaction;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.DetailListManager;
import com.jlyw.manager.DiscountComSheetManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.statistic.ExportManager;
import com.jlyw.manager.view.ViewCommissionSheetFeeManager;
import com.jlyw.manager.view.ViewTransactionManager;
import com.jlyw.util.CommissionSheetFlagUtil;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;

public class QueryServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(QueryServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		switch(method){
		case 1://统计收发室业务（出单数、出单台件数、完工数、完工台件数、结账数、结账台件数、注销数）
			JSONObject retObj1 = new JSONObject();
			try{
				String EmpId = req.getParameter("Employee");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				
				String queryStr1 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 and model.status <> 10 ";
				String queryStr2 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 ";
				String queryStr3 = "select model.commissionType, count(model.id), sum(model.quantity), count(distinct model.detailListCode) from CommissionSheet as model where 1=1 ";
				String queryStr4 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 ";
				String queryStr5 = "select model.commissionSheet.commissionType, count(model.commissionSheet.id), sum(model.number) from Withdraw as model where 1=1 ";
				List<Object> key = new ArrayList<Object>();
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				if(EmpId!=null&&!EmpId.equals(""))
				{
					queryStr1 = queryStr1 + " and model.creatorId = ?";
					queryStr2 = queryStr2 + " and model.finishStaffId = ?";
					queryStr3 = queryStr3 + " and model.checkOutStaffId = ?";
					queryStr4 = queryStr4 + " and model.cancelExecuterId = ?";
					queryStr5 = queryStr5 + " and model.sysUserByExecutorId.id = ?";
					key.add(Integer.valueOf(EmpId));
				}
				if(DateFrom!=null&&!DateFrom.equals(""))
				{
					Timestamp Start = new Timestamp(Date.valueOf(DateFrom).getTime());
					queryStr1 = queryStr1 + " and model.createDate >= ?";
					queryStr2 = queryStr2 + " and model.finishDate >= ?";
					queryStr3 = queryStr3 + " and model.checkOutDate >= ?";
					queryStr4 = queryStr4 + " and model.cancelDate >= ?";
					queryStr5 = queryStr5 + " and model.executeTime >= ?";
					key.add(Start);
				}
				if(DateEnd!=null&&!DateEnd.equals(""))
				{
					Timestamp End = new Timestamp(Date.valueOf(DateEnd).getTime());
					queryStr1 = queryStr1 + " and model.createDate <= ?";
					queryStr2 = queryStr2 + " and model.finishDate <= ?";
					queryStr3 = queryStr3 + " and model.checkOutDate <= ?";
					queryStr4 = queryStr4 + " and model.cancelDate <= ?";
					queryStr5 = queryStr5 + " and model.executeTime <= ?";
					key.add(End);
				}
				
				queryStr1 = queryStr1 + " group by model.commissionType order by model.commissionType asc";
				queryStr2 = queryStr2 + " group by model.commissionType order by model.commissionType asc";
				queryStr3 = queryStr3 + " group by model.commissionType order by model.commissionType asc";
				queryStr4 = queryStr4 + " group by model.commissionType order by model.commissionType asc";
				queryStr5 = queryStr5 + " group by model.commissionSheet.commissionType order by model.commissionSheet.commissionType asc";
				
				List<Object[]> result1 = cSheetMgr.findByHQL(queryStr1, key);
				List<Object[]> result2 = cSheetMgr.findByHQL(queryStr2, key);
				List<Object[]> result3 = cSheetMgr.findByHQL(queryStr3, key);
				List<Object[]> result4 = cSheetMgr.findByHQL(queryStr4, key);
				List<Object[]> result5 = cSheetMgr.findByHQL(queryStr5, key);
				
				JSONArray options = new JSONArray();
				int index1, index2, index3, index4, index5;
				index1 = index2 = index3 = index4 = index5 = 0;
				for(int i = 1 ;i <= CommissionSheetFlagUtil.CommissionTypeCount; i++){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionType", i);
					jsonObj.put("CommissionTypeName", CommissionSheetFlagUtil.getCommissionTypeByFlag(i));
					jsonObj.put("EmpName", (new UserManager()).findById(Integer.valueOf(EmpId)).getName());
					if(index1<result1.size()&&(Integer)result1.get(index1)[0]==i){
						jsonObj.put("ChuDanShu", result1.get(index1)[1].toString());
						jsonObj.put("ChuDanTaiJianShu", result1.get(index1)[2]==null?0:result1.get(index1)[2].toString());
						index1++;
					}
					else{
						jsonObj.put("ChuDanShu", 0);
						jsonObj.put("ChuDanTaiJianShu", 0);
					}
					if(index2<result2.size()&&(Integer)result2.get(index2)[0]==i){
						jsonObj.put("WanGongDanShu", result2.get(index2)[1].toString());
						jsonObj.put("WanGongTaiJianShu", result2.get(index2)[2]==null?0:result2.get(index2)[2].toString());
						index2++;
					}
					else{
						jsonObj.put("WanGongDanShu", 0);
						jsonObj.put("WanGongTaiJianShu", 0);
					}
					if(index3<result3.size()&&(Integer)result3.get(index3)[0]==i){
						jsonObj.put("JieZhangDanShu", "委托单数:" + result3.get(index3)[1].toString() + "/结账次数:" + result3.get(index3)[3].toString());
						jsonObj.put("JieZhangTaiJianShu", result3.get(index3)[2]==null?0:result3.get(index3)[2].toString());
						index3++;
					}
					else{
						jsonObj.put("JieZhangDanShu", "委托单数:0/结账次数:0");
						jsonObj.put("JieZhangTaiJianShu", 0);
					}
					if(index4<result4.size()&&(Integer)result4.get(index4)[0]==i){
						jsonObj.put("ZhuXiaoDanShu", result4.get(index4)[1].toString());
						jsonObj.put("ZhuXiaoTaiJianShu", result4.get(index4)[2]==null?0:result4.get(index4)[2].toString());
						index4++;
					}
					else{
						jsonObj.put("ZhuXiaoDanShu", 0);
						jsonObj.put("ZhuXiaoTaiJianShu", 0);
					}
					if(index5<result5.size()&&(Integer)result5.get(index5)[0]==i){
						jsonObj.put("TuiYangDanShu", result5.get(index5)[1].toString());
						jsonObj.put("TuiYangTaiJianShu", result5.get(index5)[2]==null?0:result5.get(index5)[2].toString());
						index5++;
					}
					else{
						jsonObj.put("TuiYangDanShu", 0);
						jsonObj.put("TuiYangTaiJianShu", 0);
					}
					options.put(jsonObj);
				}
				retObj1.put("rows", options);
			}catch(Exception e){
				try{
					retObj1.put("rows", new JSONArray());
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 1", e);
				}else{
					log.error("error in QueryServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 2://查询一个时间段内已完工的委托单
			JSONObject retObj2 = new JSONObject();
			try{
				String EmpId = req.getParameter("EmpId");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
				if(EmpId != null && EmpId.length() > 0){
					keys.add(new KeyValueWithOperator("finishStaffId", Integer.valueOf(EmpId), "="));
				}
				if(DateFrom != null && DateFrom.length() > 0){
					DateFrom = URLDecoder.decode(DateFrom, "utf-8"); 
					DateFrom = DateFrom + " 00:00:00";
					Timestamp Start = Timestamp.valueOf(DateFrom);
					keys.add(new KeyValueWithOperator("finishDate", Start, ">="));
				}
				if(DateEnd != null && DateEnd.length() > 0){
					DateEnd = URLDecoder.decode(DateEnd, "utf-8");
					DateEnd = DateEnd + " 23:59:59";
					Timestamp End = Timestamp.valueOf(DateEnd);
					keys.add(new KeyValueWithOperator("finishDate", End, "<="));
				}
				keys.add(new KeyValueWithOperator("status", Integer.valueOf(FlagUtil.CommissionSheetStatus.Status_YiWanGong), ">="));
				keys.add(new KeyValueWithOperator("status", Integer.valueOf(FlagUtil.CommissionSheetStatus.Status_YiZhuXiao), "<>"));
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				List<CommissionSheet> cSheetRetList = cSheetMgr.findPagedAllBySort(page, rows, "commissionDate", false, keys);
				int total = cSheetMgr.getTotalCount(keys);
				JSONArray options = new JSONArray();
				if(cSheetRetList != null && cSheetRetList.size() > 0){
					for(CommissionSheet cSheet : cSheetRetList)
					{
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", cSheet.getId());
						jsonObj.put("Code", cSheet.getCode());
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
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
						jsonObj.put("Status", cSheet.getStatus());	//委托单状态
						jsonObj.put("FinishStaff", (new UserManager()).findById(cSheet.getFinishStaffId()).getName());
						jsonObj.put("FinishDate", cSheet.getFinishDate());
						
						options.put(jsonObj);
					}
				}
				retObj2.put("total", total);
				retObj2.put("rows", options);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 2", e);
				}else{
					log.error("error in QueryServlet-->case 2", e);
				}
				try{
					retObj2.put("total", 0);
					retObj2.put("rows", new JSONArray());
				}catch(Exception e1){}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj2.toString());
			}
			break;
		case 21://查询已完工的委托单列表，用于打印
			JSONObject retObj21 = new JSONObject();
			try{
				String EmpId = req.getParameter("EmpId");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				UserManager userMgr=new UserManager();
				List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
				if(EmpId != null && EmpId.length() > 0){
					keys.add(new KeyValueWithOperator("finishStaffId", Integer.valueOf(EmpId), "="));
					SysUser user=userMgr.findById(Integer.valueOf(EmpId));
					retObj21.put("UserName", user.getName());
					retObj21.put("UserId", user.getJobNum());
				}else{
					retObj21.put("UserName", "");
					retObj21.put("UserId", "");
				}
				String from="",end="";
				if(DateFrom != null && DateFrom.length() > 0){
					Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
					keys.add(new KeyValueWithOperator("finishDate", Start, ">="));
					from=DateFrom;
				}
				retObj21.put("DateFrom", from);
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
					keys.add(new KeyValueWithOperator("finishDate", End, "<="));
					end=DateEnd;
				}
				retObj21.put("DateEnd", end);
				keys.add(new KeyValueWithOperator("status", Integer.valueOf(FlagUtil.CommissionSheetStatus.Status_YiWanGong), ">="));
				keys.add(new KeyValueWithOperator("status", Integer.valueOf(FlagUtil.CommissionSheetStatus.Status_YiZhuXiao), "<>"));
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				List<CommissionSheet> cSheetRetList = cSheetMgr.findByVarProperty(keys);
				int total = cSheetMgr.getTotalCount(keys);
				JSONArray options = new JSONArray();
				if(cSheetRetList != null && cSheetRetList.size() > 0){
					int i = 1;
					for(CommissionSheet cSheet : cSheetRetList)
					{
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", i++);
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Remark", "");
						
						options.put(jsonObj);
					}
				}
				retObj21.put("total", total);
				retObj21.put("rows", options);
				
				
				
				
				retObj21.put("IsOK", true);
				req.getSession().setAttribute("WGCSList", retObj21);
				
				resp.sendRedirect("/jlyw/StatisticLook/SFSStatistic/PrintWGCommissionSheet.jsp");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 21", e);
				}else{
					log.error("error in QueryServlet-->case 21", e);
				}
				e.printStackTrace();
				try{
					retObj21.put("total", 0);
					retObj21.put("rows", new JSONArray());
					retObj21.put("IsOK", false);
					req.getSession().setAttribute("WGCSList", retObj21);
					resp.sendRedirect("/jlyw/StatisticLook/SFSStatistic/PrintWGCommissionSheet.jsp");
				}catch(Exception e1){}
			}finally{
				
			}
			break;
		case 3://查询一个时间段内已结账的结账清单
			JSONObject retObj3 = new JSONObject();
			try{
				String EmpId = req.getParameter("EmpId");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
				if(EmpId != null && EmpId.length() > 0){
					keys.add(new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpId), "="));
				}
				if(DateFrom != null && DateFrom.length() > 0){
					Timestamp Start = Timestamp.valueOf(URLDecoder.decode(DateFrom, "utf-8"));
					keys.add(new KeyValueWithOperator("lastEditTime", Start, ">="));
				}
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = Timestamp.valueOf(URLDecoder.decode(DateEnd, "utf-8"));
					keys.add(new KeyValueWithOperator("lastEditTime", End, "<="));
				}
				keys.add(new KeyValueWithOperator("sysUser", null, "is not null"));
				
				keys.add(new KeyValueWithOperator("status", 2, "="));
				DetailListManager detailListMgr = new DetailListManager();
				List<DetailList> detailList = detailListMgr.findPagedAllBySort(page, rows, "lastEditTime", false, keys);
				
				int total = detailListMgr.getTotalCount(keys);
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				if(detailList != null && detailList.size() > 0){
					for(DetailList detail : detailList)
					{
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", detail.getId());
						jsonObj.put("Code", detail.getCode());
						jsonObj.put("TotalFee", detail.getTotalFee());
						jsonObj.put("PaidFee", detail.getPaidFee());
						jsonObj.put("Cash", detail.getCashPaid());
						jsonObj.put("Cheque", detail.getChequePaid()==null?0.0:detail.getChequePaid());
						jsonObj.put("Account", detail.getAccountPaid());
						jsonObj.put("InvoiceCode", detail.getInvoiceCode());
						jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						jsonObj.put("CheckOutDate", sf.format(detail.getLastEditTime()));		//结账日期
						
						options.put(jsonObj);
					}
					//以下用于统计
					List<DetailList> detailListAll = detailListMgr.findByVarProperty(keys);
					int number=0;
					double totalFee=0,paidFee = 0,cash=0,cheque=0,account=0;
					if(detailListAll != null && detailListAll.size() > 0){
						for(DetailList detail : detailListAll)
						{
							number++;
							totalFee += detail.getTotalFee();
							paidFee += (detail.getPaidFee()==null?0.0:detail.getPaidFee()); 
						    cash += (detail.getCashPaid()==null?0.0:detail.getCashPaid()); 
						    cheque += (detail.getChequePaid()==null?0.0:detail.getChequePaid()); 
						    account += (detail.getAccountPaid()==null?0.0:detail.getAccountPaid()); 
						}
							
					}
					JSONObject jsonObj = new JSONObject();
					//jsonObj.put("Id", detail.getId());
					jsonObj.put("Code", number);
					jsonObj.put("TotalFee",totalFee);
					jsonObj.put("PaidFee", paidFee);
					jsonObj.put("Cash", cash);
					jsonObj.put("Cheque", cheque);
					jsonObj.put("Account",account);
//					jsonObj.put("InvoiceCode", detail.getInvoiceCode());
//					jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
//					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//					jsonObj.put("CheckOutDate", sf.format(detail.getLastEditTime()));		//结账日期
//					
					foot.put(jsonObj);
				}
				
				retObj3.put("total", total);
				retObj3.put("rows", options);
				retObj3.put("footer", foot);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 3", e);
				}else{
					log.error("error in QueryServlet-->case 3", e);
				}
				try{
					retObj3.put("total", 0);
					retObj3.put("rows", new JSONArray());
					JSONArray foot = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					//jsonObj.put("Id", detail.getId());
					jsonObj.put("Code", 0);
					jsonObj.put("TotalFee",0);
					jsonObj.put("PaidFee", 0);
					jsonObj.put("Cash", 0);
					jsonObj.put("Cheque", 0);
					jsonObj.put("Account",0);
//					jsonObj.put("InvoiceCode", detail.getInvoiceCode());
//					jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
//					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//					jsonObj.put("CheckOutDate", sf.format(detail.getLastEditTime()));		//结账日期
//					
					foot.put(jsonObj);
					retObj3.put("footer", foot);
				}catch(Exception e1){}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		case 4://导出收发室业务统计结果
			JSONObject retObj4 = new JSONObject();
			try{
				String paramsStr = req.getParameter("paramsStr");
				JSONObject params = new JSONObject(paramsStr);
				
				String EmpId = params.getString("Employee");
				String DateFrom = params.getString("DateFrom");
				String DateEnd = params.getString("DateEnd");
				
				String queryStr1 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 and model.status <> 10 ";
				String queryStr2 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 ";
				String queryStr3 = "select model.commissionType, count(model.id), sum(model.quantity), count(distinct model.detailListCode) from CommissionSheet as model where 1=1 ";
				String queryStr4 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 ";
				String queryStr5 = "select model.commissionSheet.commissionType, count(model.commissionSheet.id), sum(model.number) from Withdraw as model where 1=1 ";
				List<Object> key = new ArrayList<Object>();
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				if(EmpId!=null&&!EmpId.equals(""))
				{
					queryStr1 = queryStr1 + " and model.creatorId = ?";
					queryStr2 = queryStr2 + " and model.finishStaffId = ?";
					queryStr3 = queryStr3 + " and model.checkOutStaffId = ?";
					queryStr4 = queryStr4 + " and model.cancelExecuterId = ?";
					queryStr5 = queryStr5 + " and model.sysUserByExecutorId.id = ?";
					key.add(Integer.valueOf(EmpId));
				}
				if(DateFrom!=null&&!DateFrom.equals(""))
				{
					Timestamp Start = new Timestamp(Date.valueOf(DateFrom).getTime());
					queryStr1 = queryStr1 + " and model.createDate >= ?";
					queryStr2 = queryStr2 + " and model.finishDate >= ?";
					queryStr3 = queryStr3 + " and model.checkOutDate >= ?";
					queryStr4 = queryStr4 + " and model.cancelDate >= ?";
					queryStr5 = queryStr5 + " and model.executeTime >= ?";
					key.add(Start);
				}
				if(DateEnd!=null&&!DateEnd.equals(""))
				{
					Timestamp End = new Timestamp(Date.valueOf(DateEnd).getTime());
					queryStr1 = queryStr1 + " and model.createDate <= ?";
					queryStr2 = queryStr2 + " and model.finishDate <= ?";
					queryStr3 = queryStr3 + " and model.checkOutDate <= ?";
					queryStr4 = queryStr4 + " and model.cancelDate <= ?";
					queryStr5 = queryStr5 + " and model.executeTime <= ?";
					key.add(End);
				}
				
				queryStr1 = queryStr1 + " group by model.commissionType order by model.commissionType asc";
				queryStr2 = queryStr2 + " group by model.commissionType order by model.commissionType asc";
				queryStr3 = queryStr3 + " group by model.commissionType order by model.commissionType asc";
				queryStr4 = queryStr4 + " group by model.commissionType order by model.commissionType asc";
				queryStr5 = queryStr5 + " group by model.commissionSheet.commissionType order by model.commissionSheet.commissionType asc";
				
				List<Object[]> result1 = cSheetMgr.findByHQL(queryStr1, key);
				List<Object[]> result2 = cSheetMgr.findByHQL(queryStr2, key);
				List<Object[]> result3 = cSheetMgr.findByHQL(queryStr3, key);
				List<Object[]> result4 = cSheetMgr.findByHQL(queryStr4, key);
				List<Object[]> result5 = cSheetMgr.findByHQL(queryStr5, key);
				
				List<JSONObject> result = new ArrayList<JSONObject>();
				int index1, index2, index3, index4, index5;
				index1 = index2 = index3 = index4 = index5 = 0;
				for(int i = 1 ;i <= CommissionSheetFlagUtil.CommissionTypeCount; i++){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionType", i);
					jsonObj.put("CommissionTypeName", CommissionSheetFlagUtil.getCommissionTypeByFlag(i));
					jsonObj.put("EmpName", (new UserManager()).findById(Integer.valueOf(EmpId)).getName());
					if(index1<result1.size()&&(Integer)result1.get(index1)[0]==i){
						jsonObj.put("ChuDanShu", result1.get(index1)[1].toString());
						jsonObj.put("ChuDanTaiJianShu", result1.get(index1)[2]==null?0:result1.get(index1)[2].toString());
						index1++;
					}
					else{
						jsonObj.put("ChuDanShu", 0);
						jsonObj.put("ChuDanTaiJianShu", 0);
					}
					if(index2<result2.size()&&(Integer)result2.get(index2)[0]==i){
						jsonObj.put("WanGongDanShu", result2.get(index2)[1].toString());
						jsonObj.put("WanGongTaiJianShu", result2.get(index2)[2]==null?0:result2.get(index2)[2].toString());
						index2++;
					}
					else{
						jsonObj.put("WanGongDanShu", 0);
						jsonObj.put("WanGongTaiJianShu", 0);
					}
					if(index3<result3.size()&&(Integer)result3.get(index3)[0]==i){
						jsonObj.put("JieZhangDanShu", "委托单数:" + result3.get(index3)[1].toString() + "/结账次数:" + result3.get(index3)[3].toString());
						jsonObj.put("JieZhangTaiJianShu", result3.get(index3)[2]==null?0:result3.get(index3)[2].toString());
						index3++;
					}
					else{
						jsonObj.put("JieZhangDanShu", "委托单数:0/结账次数:0");
						jsonObj.put("JieZhangTaiJianShu", 0);
					}
					if(index4<result4.size()&&(Integer)result4.get(index4)[0]==i){
						jsonObj.put("ZhuXiaoDanShu", result4.get(index4)[1].toString());
						jsonObj.put("ZhuXiaoTaiJianShu", result4.get(index4)[2]==null?0:result4.get(index4)[2].toString());
						index4++;
					}
					else{
						jsonObj.put("ZhuXiaoDanShu", 0);
						jsonObj.put("ZhuXiaoTaiJianShu", 0);
					}
					if(index5<result5.size()&&(Integer)result5.get(index5)[0]==i){
						jsonObj.put("TuiYangDanShu", result5.get(index5)[1].toString());
						jsonObj.put("TuiYangTaiJianShu", result5.get(index5)[2]==null?0:result5.get(index5)[2].toString());
						index5++;
					}
					else{
						jsonObj.put("TuiYangDanShu", 0);
						jsonObj.put("TuiYangTaiJianShu", 0);
					}
					result.add(jsonObj);
				}
				String filePath = ExportUtil.ExportToExcelByResultSet(result, null, "SFSMission_formatExcel", "SFSMission_formatTitle", ExportManager.class);
				retObj4.put("IsOK", filePath.equals("")?false:true);
				retObj4.put("Path", filePath);
			}catch(Exception e){
				try{
					retObj4.put("IsOK", false);
					retObj4.put("Path", "");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 4", e);
				}else{
					log.error("error in QueryServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		case 5://导出已结账列表
			JSONObject retObj5 = new JSONObject();
			try{
				String paramsStr = req.getParameter("paramsStr");
				JSONObject params = new JSONObject(paramsStr);
				
				String EmpId = params.getString("EmpId");
				String DateFrom = params.getString("DateFrom");
				String DateEnd = params.getString("DateEnd");
				
				String queryStr = "from DetailList as d, CommissionSheet as c where c.detailListCode = d.code and d.sysUser is not null ";
				List<Object> keys = new ArrayList<Object>();
				if(EmpId != null && EmpId.length() > 0){
					queryStr = queryStr + " and d.sysUser.id = ?";
					keys.add(Integer.valueOf(EmpId));
				}
				if(DateFrom != null && DateFrom.length() > 0){
					queryStr = queryStr + " and d.lastEditTime >= ?";
					Timestamp Start = Timestamp.valueOf(URLDecoder.decode(DateFrom, "utf-8"));
					keys.add(Start);
				}
				if(DateEnd != null && DateEnd.length() > 0){
					queryStr = queryStr + " and d.lastEditTime <= ?";
					Timestamp End = Timestamp.valueOf(URLDecoder.decode(DateEnd, "utf-8"));
					keys.add(End);
				}
				DetailListManager detailListMgr = new DetailListManager();
				List<Object[]> retList = detailListMgr.findByHQL("select d,c " + queryStr + " order by d.sysUser.jobNum asc, d.code asc", keys);
				List<JSONObject> result = new ArrayList<JSONObject>();
				if(retList != null && retList.size() > 0){
					double totalFee, paidFee, cash, cheque, account;
					double ctotalFee, ctestFee, crepairFee, cmaterialFee, ccarFee, cdebugFee, cotherFee;
					totalFee = paidFee = cash = cheque = account = ctotalFee = ctestFee = crepairFee = cmaterialFee = ccarFee = cdebugFee = cotherFee = 0;
					String tempDetailListCode = null;
					int tempUserId = 0;
					for(Object[] obj : retList)
					{
						DetailList detail = (DetailList)obj[0];
						CommissionSheet cSheet = (CommissionSheet)obj[1];
						if(tempUserId!=0&&tempUserId!=detail.getSysUser().getId()){
							JSONObject option = new JSONObject();
							option.put("CheckOutStaffJobNum", "个人总金额");
							option.put("TotalFee", totalFee);
							option.put("PaidFee", paidFee);
							option.put("Cash", cash);
							option.put("Cheque", cheque);
							option.put("Account", account);
							option.put("CommissionSheetCode", "个人总金额");
							option.put("cSheetTotalFee", ctotalFee);
							option.put("cSheetTestFee", ctestFee);
							option.put("cSheetRepairFee", crepairFee);
							option.put("cSheetMaterialFee", cmaterialFee);
							option.put("cSheetCarFee", ccarFee);
							option.put("cSheetDebugFee", cdebugFee);
							option.put("cSheetOtherFee", cotherFee);
							
							result.add(option);
							totalFee = paidFee = cash = cheque = account = ctotalFee = ctestFee = crepairFee = cmaterialFee = ccarFee = cdebugFee = cotherFee = 0;
						}
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("DetailListCode", detail.getCode());
						jsonObj.put("TotalFee", !detail.getCode().equals(tempDetailListCode)?detail.getTotalFee()==null?0:detail.getTotalFee():0);
						jsonObj.put("PaidFee", !detail.getCode().equals(tempDetailListCode)?detail.getPaidFee()==null?0:detail.getPaidFee():0);
						jsonObj.put("Cash", !detail.getCode().equals(tempDetailListCode)?detail.getCashPaid()==null?0:detail.getCashPaid():0);
						jsonObj.put("Cheque",!detail.getCode().equals(tempDetailListCode)?detail.getChequePaid()==null?0:detail.getChequePaid():0);
						jsonObj.put("Account", !detail.getCode().equals(tempDetailListCode)?detail.getAccountPaid()==null?0:detail.getAccountPaid():0);
						jsonObj.put("InvoiceCode", !detail.getCode().equals(tempDetailListCode)?detail.getInvoiceCode():"");
						jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
						jsonObj.put("CheckOutStaffJobNum", detail.getSysUser().getJobNum());
						jsonObj.put("CheckOutDate", detail.getLastEditTime());		//结账日期
						jsonObj.put("CommissionSheetCode", cSheet.getCode());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceName", cSheet.getApplianceName());
						jsonObj.put("Model", cSheet.getApplianceModel());
						jsonObj.put("Range", cSheet.getRange());
						jsonObj.put("Accuracy", cSheet.getAccuracy());
						jsonObj.put("Quantity", cSheet.getQuantity());
						
						String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
						List<Long> withdraws = detailListMgr.findByHQL(queryStr2, detail.getSysUser().getId(), Timestamp.valueOf(URLDecoder.decode(DateFrom, "utf-8")), Timestamp.valueOf(URLDecoder.decode(DateEnd, "utf-8")));
						jsonObj.put("Withdraw", withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
						
						jsonObj.put("cSheetTotalFee", cSheet.getTotalFee());
						jsonObj.put("cSheetTestFee", cSheet.getTestFee());
						jsonObj.put("cSheetRepairFee", cSheet.getRepairFee());
						jsonObj.put("cSheetMaterialFee", cSheet.getMaterialFee());
						jsonObj.put("cSheetCarFee", cSheet.getCarFee());
						jsonObj.put("cSheetDebugFee", cSheet.getDebugFee());
						jsonObj.put("cSheetOtherFee", cSheet.getOtherFee());
						jsonObj.put("CommissionDate", cSheet.getCommissionDate());
						
						result.add(jsonObj);
						tempDetailListCode = detail.getCode();
						tempUserId = detail.getSysUser().getId();
						
						totalFee += (Double)jsonObj.get("TotalFee");
						paidFee += (Double)jsonObj.get("PaidFee");
						cash += (Double)jsonObj.get("Cash");
						cheque += (Double)jsonObj.get("Cheque");
						account += (Double)jsonObj.get("Account");
						
						ctotalFee += cSheet.getTotalFee();
						ctestFee += cSheet.getTestFee();
						crepairFee += cSheet.getRepairFee();
						cmaterialFee += cSheet.getMaterialFee();
						ccarFee += cSheet.getCarFee();
						cdebugFee += cSheet.getDebugFee();
						cotherFee += cSheet.getOtherFee();
					}
					
					JSONObject option = new JSONObject();
					option.put("CheckOutStaffJobNum", "个人总金额");
					option.put("TotalFee", totalFee);
					option.put("PaidFee", paidFee);
					option.put("Cash", cash);
					option.put("Cheque", cheque);
					option.put("Account", account);
					option.put("CommissionSheetCode", "个人总金额");
					option.put("cSheetTotalFee", ctotalFee);
					option.put("cSheetTestFee", ctestFee);
					option.put("cSheetRepairFee", crepairFee);
					option.put("cSheetMaterialFee", cmaterialFee);
					option.put("cSheetCarFee", ccarFee);
					option.put("cSheetDebugFee", cdebugFee);
					option.put("cSheetOtherFee", cotherFee);
					
					result.add(option);
					
				}
				
				String filePath = ExportUtil.ExportToExcelByResultSet(result, null, "DetailList_formatExcel", "DetailList__formatTitle", ExportManager.class);
				
				retObj5.put("IsOK", filePath.equals("")?false:true);
				retObj5.put("Path", filePath);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 5", e);
				}else{
					log.error("error in QueryServlet-->case 5", e);
				}
				try{
					retObj5.put("IsOK", false);
					retObj5.put("Path", "");
				}catch(Exception e1){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj5.toString());
			}
			break;
		case 6://根据员工查询该员工所申请的折扣信息
			JSONObject retObj6 = new JSONObject();
			try{
				String EmpId = req.getParameter("EmpId");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
				if(EmpId != null && EmpId.length() > 0){
					String EmpIdStr = URLDecoder.decode(EmpId, "UTF-8");
					keys.add(new KeyValueWithOperator("discount.sysUserByRequesterId.id", Integer.valueOf(EmpIdStr), "="));
				}
				if(DateFrom != null && DateFrom.length() > 0){
					Timestamp Start = Timestamp.valueOf(DateTimeFormatUtil.DateTimeFormat.format(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8"))));
					keys.add(new KeyValueWithOperator("commissionSheet.commissionDate", Start, ">="));
				}
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = Timestamp.valueOf(DateTimeFormatUtil.DateTimeFormat.format(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8"))));
					keys.add(new KeyValueWithOperator("commissionSheet.commissionDate", End, "<="));
				}
				keys.add(new KeyValueWithOperator("discount.executeResult", true, "="));
				DiscountComSheetManager disComSheetMgr = new DiscountComSheetManager();
				List<DiscountComSheet> retList = disComSheetMgr.findPagedAll(page, rows, keys);
				int total = disComSheetMgr.getTotalCount(keys);
				JSONArray options = new JSONArray();
				if(retList != null && retList.size() > 0){
					for(DiscountComSheet disComSheet : retList)
					{
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Requester", disComSheet.getDiscount().getSysUserByRequesterId().getName());
						jsonObj.put("Code", disComSheet.getCommissionSheet().getCode());
						jsonObj.put("CommissionDate", disComSheet.getCommissionSheet().getCommissionDate());
						jsonObj.put("CustomerName", disComSheet.getDiscount().getCustomer().getName());
						jsonObj.put("OldTotalFee", disComSheet.getOldTotalFee()==null?0:disComSheet.getOldTotalFee());
						jsonObj.put("OldTestFee", disComSheet.getOldTestFee()==null?0:disComSheet.getOldTestFee());
						jsonObj.put("OldRepairFee", disComSheet.getOldRepairFee()==null?0:disComSheet.getOldRepairFee());
						jsonObj.put("OldMaterialFee", disComSheet.getOldMaterialFee()==null?0:disComSheet.getOldMaterialFee());
						jsonObj.put("OldCarFee", disComSheet.getOldCarFee()==null?0:disComSheet.getOldCarFee());
						jsonObj.put("OldDebugFee", disComSheet.getOldDebugFee()==null?0:disComSheet.getOldDebugFee());
						jsonObj.put("OldOtherFee", disComSheet.getOldOtherFee()==null?0:disComSheet.getOldOtherFee());
						jsonObj.put("TotalFee", disComSheet.getTotalFee()==null?0:disComSheet.getTotalFee());
						jsonObj.put("TestFee", disComSheet.getTestFee()==null?0:disComSheet.getTestFee());
						jsonObj.put("RepairFee", disComSheet.getRepairFee()==null?0:disComSheet.getRepairFee());
						jsonObj.put("MaterialFee", disComSheet.getMaterialFee()==null?0:disComSheet.getMaterialFee());
						jsonObj.put("CarFee", disComSheet.getCarFee()==null?0:disComSheet.getCarFee());
						jsonObj.put("DebugFee", disComSheet.getDebugFee()==null?0:disComSheet.getDebugFee());
						jsonObj.put("OtherFee", disComSheet.getOtherFee()==null?0:disComSheet.getOtherFee());
						
						options.put(jsonObj);
					}
				}
				retObj6.put("total", total);
				retObj6.put("rows", options);
				
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 6", e);
				}else{
					log.error("error in QueryServlet-->case 6", e);
				}
				try{
					retObj6.put("total", 0);
					retObj6.put("rows", new JSONArray());
				}catch(Exception e1){}
			}finally{
				resp.setContentType("test/json;charset=utf-8");
				resp.getWriter().write(retObj6.toString());
			}
			break;
		case 7://查询一个时间段内已结账的结账清单(打印)
			JSONObject retObj7 = new JSONObject();
			try{
				String EmpId = req.getParameter("EmpId");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				List<KeyValueWithOperator> keys = new ArrayList<KeyValueWithOperator>();
				if(EmpId != null && EmpId.length() > 0){
					keys.add(new KeyValueWithOperator("sysUser.id", Integer.valueOf(EmpId), "="));
				}
				if(DateFrom != null && DateFrom.length() > 0){
					Timestamp Start = Timestamp.valueOf(URLDecoder.decode(DateFrom, "utf-8"));
					keys.add(new KeyValueWithOperator("lastEditTime", Start, ">="));
				}
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = Timestamp.valueOf(URLDecoder.decode(DateEnd, "utf-8"));
					keys.add(new KeyValueWithOperator("lastEditTime", End, "<="));
				}
				keys.add(new KeyValueWithOperator("sysUser", null, "is not null"));
				keys.add(new KeyValueWithOperator("status", 1, "<>"));
				DetailListManager detailListMgr = new DetailListManager();
				List<DetailList> detailList = detailListMgr.findByPropertyBySort("lastEditTime", false, keys);
				int total = detailListMgr.getTotalCount(keys);
				JSONArray options = new JSONArray();
				if(detailList != null && detailList.size() > 0){
					for(DetailList detail : detailList)
					{
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", detail.getId());
						jsonObj.put("Code", detail.getCode());
						jsonObj.put("TotalFee", detail.getTotalFee()==null?0.0:detail.getTotalFee());
						jsonObj.put("PaidFee", detail.getPaidFee()==null?0.0:detail.getPaidFee());
						jsonObj.put("Cash", detail.getCashPaid()==null?0.0:detail.getCashPaid());
						jsonObj.put("Cheque", detail.getChequePaid()==null?0.0:detail.getChequePaid());
						jsonObj.put("Account", detail.getAccountPaid()==null?0.0:detail.getAccountPaid());
						jsonObj.put("InvoiceCode", detail.getInvoiceCode()==null?"":detail.getInvoiceCode());
						jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						jsonObj.put("CheckOutDate", sf.format(detail.getLastEditTime()));		//结账日期
						
						options.put(jsonObj);
					}
					//以下用于统计
					List<DetailList> detailListAll = detailListMgr.findByVarProperty(keys);
					int number=0;
					double totalFee=0,paidFee = 0,cash=0,cheque=0,account=0;
					if(detailListAll != null && detailListAll.size() > 0){
						for(DetailList detail : detailListAll)
						{
							number++;
							totalFee += detail.getTotalFee();
							paidFee += (detail.getPaidFee()==null?0.0:detail.getPaidFee()); 
						    cash += (detail.getCashPaid()==null?0.0:detail.getCashPaid()); 
						    cheque += (detail.getChequePaid()==null?0.0:detail.getChequePaid()); 
						    account += (detail.getAccountPaid()==null?0.0:detail.getAccountPaid()); 
						}
							
					}
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("Id","");
					jsonObj.put("Code", "统计："+number);
					jsonObj.put("TotalFee",totalFee);
					jsonObj.put("PaidFee", paidFee);
					jsonObj.put("Cash", cash);
					jsonObj.put("Cheque", cheque);
					jsonObj.put("Account",account);
					jsonObj.put("InvoiceCode", "");
					jsonObj.put("CheckOutStaff","");
					
					jsonObj.put("CheckOutDate", "");		//结账日期
					
					options.put(jsonObj);
				}
				
				
				retObj7.put("total", total);
				retObj7.put("rows", options);
				
				retObj7.put("IsOK", true);
				
				UserManager userMgr=new UserManager();
				if(EmpId!=null&&EmpId.length()>0){
					retObj7.put("UserName",(userMgr.findById(Integer.valueOf(EmpId))==null?"":userMgr.findById(Integer.valueOf(EmpId)).getName()));
					retObj7.put("UserJobNum",(userMgr.findById(Integer.valueOf(EmpId))==null?"":userMgr.findById(Integer.valueOf(EmpId)).getJobNum()));
				}else{
					retObj7.put("UserName","");
					retObj7.put("UserJobNum","");
				}
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				retObj7.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				
				retObj7.put("DateFrom", URLDecoder.decode(DateFrom, "utf-8"));
				retObj7.put("DateEnd", URLDecoder.decode(DateEnd, "utf-8"));
				req.getSession().setAttribute("CheckOutMissionLookList", retObj7);
				
				resp.sendRedirect("/jlyw/StatisticLook/SFSStatistic/CheckOutMissionLookPrint.jsp");
			}catch(Exception e){
				
				try{
					retObj7.put("total", 0);
					retObj7.put("rows", new JSONArray());
					retObj7.put("IsOK", false);
					req.getSession().setAttribute("CheckOutMissionLookList", retObj7);
					resp.sendRedirect("/jlyw/StatisticLook/SFSStatistic/CheckOutMissionLookPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 7", e);
				}else{
					log.error("error in StatisticServlet-->case 7", e);
				}
			}finally{
				
			}
			break;
		case 8://统计收发室业务（出单数、出单台件数、完工数、完工台件数、结账数、结账台件数、注销数）(打印)
			JSONObject retObj8 = new JSONObject();
			try{
				String EmpId = req.getParameter("Employee");
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				
				String queryStr1 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 and model.status <> 10 ";
				String queryStr2 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 ";
				String queryStr3 = "select model.commissionType, count(model.id), sum(model.quantity), count(distinct model.detailListCode) from CommissionSheet as model where 1=1 ";
				String queryStr4 = "select model.commissionType, count(model.id), sum(model.quantity) from CommissionSheet as model where 1=1 ";
				String queryStr5 = "select model.commissionSheet.commissionType, count(model.commissionSheet.id), sum(model.number) from Withdraw as model where 1=1 ";
				List<Object> key = new ArrayList<Object>();
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				if(EmpId!=null&&!EmpId.equals(""))
				{
					queryStr1 = queryStr1 + " and model.creatorId = ?";
					queryStr2 = queryStr2 + " and model.finishStaffId = ?";
					queryStr3 = queryStr3 + " and model.checkOutStaffId = ?";
					queryStr4 = queryStr4 + " and model.cancelExecuterId = ?";
					queryStr5 = queryStr5 + " and model.sysUserByExecutorId.id = ?";
					key.add(Integer.valueOf(EmpId));
				}
				if(DateFrom!=null&&!DateFrom.equals(""))
				{
					Timestamp Start = new Timestamp(Date.valueOf(DateFrom).getTime());
					queryStr1 = queryStr1 + " and model.createDate >= ?";
					queryStr2 = queryStr2 + " and model.finishDate >= ?";
					queryStr3 = queryStr3 + " and model.checkOutDate >= ?";
					queryStr4 = queryStr4 + " and model.cancelDate >= ?";
					queryStr5 = queryStr5 + " and model.executeTime >= ?";
					key.add(Start);
				}
				if(DateEnd!=null&&!DateEnd.equals(""))
				{
					Timestamp End = new Timestamp(Date.valueOf(DateEnd).getTime());
					queryStr1 = queryStr1 + " and model.createDate <= ?";
					queryStr2 = queryStr2 + " and model.finishDate <= ?";
					queryStr3 = queryStr3 + " and model.checkOutDate <= ?";
					queryStr4 = queryStr4 + " and model.cancelDate <= ?";
					queryStr5 = queryStr5 + " and model.executeTime <= ?";
					key.add(End);
				}
				
				queryStr1 = queryStr1 + " group by model.commissionType order by model.commissionType asc";
				queryStr2 = queryStr2 + " group by model.commissionType order by model.commissionType asc";
				queryStr3 = queryStr3 + " group by model.commissionType order by model.commissionType asc";
				queryStr4 = queryStr4 + " group by model.commissionType order by model.commissionType asc";
				queryStr5 = queryStr5 + " group by model.commissionSheet.commissionType order by model.commissionSheet.commissionType asc";
				
				List<Object[]> result1 = cSheetMgr.findByHQL(queryStr1, key);
				List<Object[]> result2 = cSheetMgr.findByHQL(queryStr2, key);
				List<Object[]> result3 = cSheetMgr.findByHQL(queryStr3, key);
				List<Object[]> result4 = cSheetMgr.findByHQL(queryStr4, key);
				List<Object[]> result5 = cSheetMgr.findByHQL(queryStr5, key);
				
				JSONArray options = new JSONArray();
				int index1, index2, index3, index4, index5;
				index1 = index2 = index3 = index4 = index5 = 0;
				for(int i = 1 ;i <= CommissionSheetFlagUtil.CommissionTypeCount; i++){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionType", i);
					jsonObj.put("CommissionTypeName", CommissionSheetFlagUtil.getCommissionTypeByFlag(i));
					jsonObj.put("EmpName", (new UserManager()).findById(Integer.valueOf(EmpId)).getName());
					if(index1<result1.size()&&(Integer)result1.get(index1)[0]==i){
						jsonObj.put("ChuDanShu", result1.get(index1)[1].toString());
						jsonObj.put("ChuDanTaiJianShu", result1.get(index1)[2]==null?0:result1.get(index1)[2].toString());
						index1++;
					}
					else{
						jsonObj.put("ChuDanShu", 0);
						jsonObj.put("ChuDanTaiJianShu", 0);
					}
					if(index2<result2.size()&&(Integer)result2.get(index2)[0]==i){
						jsonObj.put("WanGongDanShu", result2.get(index2)[1].toString());
						jsonObj.put("WanGongTaiJianShu", result2.get(index2)[2]==null?0:result2.get(index2)[2].toString());
						index2++;
					}
					else{
						jsonObj.put("WanGongDanShu", 0);
						jsonObj.put("WanGongTaiJianShu", 0);
					}
					if(index3<result3.size()&&(Integer)result3.get(index3)[0]==i){
						jsonObj.put("JieZhangDanShu", "委托单数:" + result3.get(index3)[1].toString() + "/结账次数:" + result3.get(index3)[3].toString());
						jsonObj.put("JieZhangTaiJianShu", result3.get(index3)[2]==null?0:result3.get(index3)[2].toString());
						index3++;
					}
					else{
						jsonObj.put("JieZhangDanShu", "委托单数:0/结账次数:0");
						jsonObj.put("JieZhangTaiJianShu", 0);
					}
					if(index4<result4.size()&&(Integer)result4.get(index4)[0]==i){
						jsonObj.put("ZhuXiaoDanShu", result4.get(index4)[1].toString());
						jsonObj.put("ZhuXiaoTaiJianShu", result4.get(index4)[2]==null?0:result4.get(index4)[2].toString());
						index4++;
					}
					else{
						jsonObj.put("ZhuXiaoDanShu", 0);
						jsonObj.put("ZhuXiaoTaiJianShu", 0);
					}
					if(index5<result5.size()&&(Integer)result5.get(index5)[0]==i){
						jsonObj.put("TuiYangDanShu", result5.get(index5)[1].toString());
						jsonObj.put("TuiYangTaiJianShu", result5.get(index5)[2]==null?0:result5.get(index5)[2].toString());
						index5++;
					}
					else{
						jsonObj.put("TuiYangDanShu", 0);
						jsonObj.put("TuiYangTaiJianShu", 0);
					}
					options.put(jsonObj);
				}
				retObj8.put("rows", options);
				
				UserManager userMgr=new UserManager();
				if(EmpId!=null&&EmpId.length()>0){
					retObj8.put("UserName",(userMgr.findById(Integer.valueOf(EmpId))==null?"":userMgr.findById(Integer.valueOf(EmpId)).getName()));
					retObj8.put("UserJobNum",(userMgr.findById(Integer.valueOf(EmpId))==null?"":userMgr.findById(Integer.valueOf(EmpId)).getJobNum()));
				}else{
					retObj8.put("UserName","");
					retObj8.put("UserJobNum","");
				}
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				retObj8.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				
				retObj8.put("DateFrom", URLDecoder.decode(DateFrom, "utf-8"));
				retObj8.put("DateEnd", URLDecoder.decode(DateEnd, "utf-8"));
				req.getSession().setAttribute("SFSMissionStatisticList", retObj8);
				
				resp.sendRedirect("/jlyw/StatisticLook/SFSStatistic/SFSMissionStatisticPrint.jsp");
			}catch(Exception e){
				
				try{
					retObj8.put("total", 0);
					retObj8.put("rows", new JSONArray());
					retObj8.put("IsOK", false);
					req.getSession().setAttribute("SFSMissionStatisticList", retObj8);
					resp.sendRedirect("/jlyw/StatisticLook/SFSStatistic/SFSMissionStatisticPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 8", e);
				}else{
					log.error("error in StatisticServlet-->case 8", e);
				}
			}finally{
				
			}
			break;
		case 9:
			JSONObject retObj=new JSONObject();
			try {
				String Code = req.getParameter("Code").trim();	//证书号信息
				CertificateManager cerMgr = new CertificateManager();
				List<Certificate> certiList = cerMgr.findByPropertyBySort("certificateCode", false, new KeyValueWithOperator("certificateCode",Code,"="));
				if(certiList!=null&&certiList.size()>0){
					Certificate cer = certiList.get(0);
						
					String securityCode = LetterUtil.getCertificateSecurityCode(cer.getCertificateCode(), cer.getOriginalRecord().getWorkDate());
					retObj.put("IsOK", true);
					retObj.put("msg", "查询成功！");
//					String securityCodeP1 = securityCode.substring(0, securityCode.length()>16?16:securityCode.length());
//					String securityCodeP2 = securityCode.length()<=16?"":securityCode.substring(16, securityCode.length());
//					
					StringBuilder strBuilder = new StringBuilder(securityCode);
					if(strBuilder.toString().length()>28){
						strBuilder.insert(28," ");
					}
					if(strBuilder.toString().length()>24){
						strBuilder.insert(24," ");
					}
					if(strBuilder.toString().length()>20){
						strBuilder.insert(20," ");
					}
					if(strBuilder.toString().length()>16){
						strBuilder.insert(16," ");
					}
					if(strBuilder.toString().length()>12){
						strBuilder.insert(12," ");
					}
					if(strBuilder.toString().length()>8){
						strBuilder.insert(8," ");
					}
					if(strBuilder.toString().length()>4){
						strBuilder.insert(4," ");
					}
					retObj.put("securityCode", strBuilder.toString());
				}else{
					retObj.put("IsOK", false);
					retObj.put("msg", "查询证书失败！");
				}
			}catch (Exception e){
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("处理失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 9", e);
				}else{
					log.error("error in StatisticServlet-->case 9", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 10://业务查询导出
			String paramsStr17 = req.getParameter("paramsStr");
			JSONObject retObj17 = new JSONObject();
			try{
				JSONObject params = new JSONObject(paramsStr17);

				List<JSONObject> options = new ArrayList<JSONObject>();
				ViewCommissionSheetFeeManager viewCSheetFeeMgr = new ViewCommissionSheetFeeManager();
				if(params.length()!=0)
				{				
					String Code = params.has("Code")?params.getString("Code"):"";
					String CommissionType = params.has("CommissionType")?params.getString("CommissionType"):"";
					String ReportType = params.has("ReportType")?params.getString("ReportType"):"";
					String CustomerId = params.has("CustomerId")?params.getString("CustomerId"):"";
					String ZipCode = params.has("ZipCode")?params.getString("ZipCode"):"";
					String LocaleMissionCode = params.has("LocaleMissionCode")?params.getString("LocaleMissionCode"):"";
					String RegionId = params.has("RegionId")?params.getString("RegionId"):"";
					String Classi = params.has("Classi")?params.getString("Classi"):"";
					String CommissionDateFrom = params.has("CommissionDateFrom")?params.getString("CommissionDateFrom"):"";
					String CommissionDateEnd = params.has("CommissionDateEnd")?params.getString("CommissionDateEnd"):"";
					String FinishDateFrom = params.has("FinishDateFrom")?params.getString("FinishDateFrom"):"";
					String FinishDateEnd = params.has("FinishDateEnd")?params.getString("FinishDateEnd"):"";
					String CheckOutDateFrom = params.has("CheckOutDateFrom")?params.getString("CheckOutDateFrom"):"";
					String CheckOutDateEnd = params.has("CheckOutDateEnd")?params.getString("CheckOutDateEnd"):"";
					String Status = params.has("Status")?params.getString("Status"):"";
					String Receiver = params.has("Receiver")?params.getString("Receiver"):"";
					String FinishUser = params.has("FinishUser")?params.getString("FinishUser"):"";
					String CheckOutUser = params.has("CheckOutUser")?params.getString("CheckOutUser"):"";
					String SpeciesType = params.has("SpeciesType")?params.getString("SpeciesType"):"";
					String ApplianceSpeciesId = params.has("ApplianceSpeciesId")?params.getString("ApplianceSpeciesId"):"";
					String InsideContactor = params.has("InsideContactor")?params.getString("InsideContactor"):"";
					String HeadName = params.has("HeadName")?params.getString("HeadName"):"";
				
					String queryStr2 = "from CommissionSheet as model,Customer as c where model.customerId = c.id and 1=1 ";
//					String queryStringAllFee = " select SUM(view.testFee),SUM(view.repairFee),SUM(view.materialFee),SUM(view.carFee),SUM(view.debugFee),SUM(view.otherFee),SUM(view.totalFee) from ViewCommissionSheetFee as view, CommissionSheet as model, Customer as c " +
//												" where model.customerId = c.id and view.id.commissionSheetId = model.id ";
//					String queryStringCount = "select SUM(model.quantity) from CommissionSheet as model, Customer as c where model.customerId = c.id and 1=1 ";
//					String withDrawString = "select SUM(w.number) from CommissionSheet as model, Withdraw as w, Customer as c where w.commissionSheet.id = model.id and w.executeResult=1 and model.customerId = c.id and 1=1 ";
					String paramString = "";
					
					List<Object> keys = new ArrayList<Object>();
					if(Code!=null&&!Code.equals("")){
						String CodeStr = URLDecoder.decode(Code, "UTF-8");
						paramString = paramString + " and model.code = ?";
						keys.add(CodeStr);
					}
					if(CommissionType!=null&&!CommissionType.equals("")){
						paramString = paramString + " and model.commissionType = ?";
						keys.add(Integer.valueOf(CommissionType));
					}
					if(ReportType!=null&&!ReportType.equals("")){
						paramString = paramString + " and model.reportType = ?";
						keys.add(Integer.valueOf(ReportType));
					}
					if(CustomerId!=null&&!CustomerId.equals("")){
						String cusIdStr = URLDecoder.decode(CustomerId, "UTF-8");
						paramString = paramString + " and model.customerId = ?";
						keys.add(Integer.valueOf(cusIdStr));
					}
					if(LocaleMissionCode!=null&&!LocaleMissionCode.equals("")){
						paramString = paramString + " and model.localeCommissionCode = ?";
						keys.add(LocaleMissionCode);
					}
					if(Receiver!=null&&!Receiver.equals("")){
						String receiverStr = URLDecoder.decode(Receiver, "UTF-8");
						paramString = paramString + " and model.receiverId = ?";
						keys.add(Integer.valueOf(receiverStr));
					}
					if(FinishUser!=null&&!FinishUser.equals("")){
						String finUserStr = URLDecoder.decode(FinishUser, "UTF-8");
						paramString = paramString + " and model.finishStaffId = ?";
						keys.add(Integer.valueOf(finUserStr));
					}
					if(CheckOutUser!=null&&!CheckOutUser.equals("")){
						String checkUserStr = URLDecoder.decode(CheckOutUser, "UTF-8");
						paramString = paramString + " and model.checkOutStaffId = ?";
						keys.add(Integer.valueOf(checkUserStr));
					}
					if(ZipCode!=null&&!ZipCode.equals("")){
						String zipCodeStr = URLDecoder.decode(ZipCode, "UTF-8");
						paramString = paramString + " and c.zipCode = ?";
						keys.add(zipCodeStr);
					}
					if(RegionId!=null&&!RegionId.equals("")){
						String RegionIdStr = URLDecoder.decode(RegionId, "UTF-8");
						paramString = paramString + " and c.regionId = ?";
						keys.add(Integer.valueOf(RegionIdStr));
					}
					if(Classi!=null&&!Classi.equals(""))
					{
						String cusClassiStr = URLDecoder.decode(Classi, "UTF-8");
						paramString = paramString + " and c.classification like ?";
						keys.add("%" + cusClassiStr + "%");
					}
					if(CommissionDateFrom!=null&&!CommissionDateFrom.equals("")&&CommissionDateEnd!=null&&!CommissionDateEnd.equals("")){
						Timestamp Start = Timestamp.valueOf(CommissionDateFrom + " 0:0:0");
						Timestamp End = Timestamp.valueOf(CommissionDateEnd + " 23:59:59");
						
						paramString = paramString + " and (model.commissionDate >= ? and model.commissionDate <= ? )";
						keys.add(Start);
						keys.add(End);
					}
					if(FinishDateFrom!=null&&!FinishDateFrom.equals("")&&FinishDateEnd!=null&&!FinishDateEnd.equals("")){
						Timestamp Start = Timestamp.valueOf(FinishDateFrom + " 0:0:0");
						Timestamp End = Timestamp.valueOf(FinishDateFrom + " 23:59:59");
						
						paramString = paramString + " and (model.finishDate >= ? and model.finishDate <= ? )";
						keys.add(Start);
						keys.add(End);
					}
					if(CheckOutDateFrom!=null&&!CheckOutDateFrom.equals("")&&CheckOutDateEnd!=null&&!CheckOutDateEnd.equals("")){
						Timestamp Start = Timestamp.valueOf(CheckOutDateFrom + " 0:0:0");
						Timestamp End = Timestamp.valueOf(CheckOutDateEnd + " 23:59:59");
						
						paramString = paramString + " and (model.checkOutDate >= ? and model.checkOutDate <= ? )";
						keys.add(Start);
						keys.add(End);
					}
					if(Status!=null&&!Status.equals("")){
						String statusStr = URLDecoder.decode(Status, "UTF-8");
						if(statusStr.equals("<3")){
							paramString = paramString + " and model.status < ?";
							keys.add(Integer.valueOf(3));
						}else if(statusStr.equals("<4")){
							paramString = paramString + " and (model.status < ? or model.status = ?)";
							keys.add(Integer.valueOf(4));
							keys.add(Integer.valueOf(9));
						}else{
							paramString = paramString + " and model.status = ?";
							keys.add(Integer.valueOf(statusStr));
						}
					}
					if(SpeciesType!=null&&!SpeciesType.equals("")&&ApplianceSpeciesId!=null&&!ApplianceSpeciesId.equals("")){
						String speciesTypeStr = URLDecoder.decode(SpeciesType, "UTF-8");
						String applianceSpeciesIdStr = URLDecoder.decode(ApplianceSpeciesId, "UTF-8");
						paramString = paramString + " and (model.speciesType = ? and model.applianceSpeciesId = ?)";
						keys.add(Integer.valueOf(speciesTypeStr)==1?true:false);
						keys.add(Integer.valueOf(applianceSpeciesIdStr));
					}
					if(InsideContactor!=null&&!InsideContactor.equals("")){
						String InsideContactorStr = URLDecoder.decode(InsideContactor, "UTF-8");
						paramString = paramString + " and c.sysUserByInsideContactorId.id = ?";
						keys.add(Integer.valueOf(InsideContactorStr));
					}
					if(HeadName!=null&&!HeadName.equals("")){
						String HeadNameStr = URLDecoder.decode(HeadName, "UTF-8");
						paramString = paramString + " and model.headNameId = ?";
						keys.add(Integer.valueOf(HeadNameStr));
					}
					System.out.println(new Timestamp(System.currentTimeMillis()));
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					//CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
					//int total = 0;
					//total = cSheetMgr.getTotalCountByHQL("select count(model) " + queryStr2 + paramString, keys2);
					//JSONArray options = new JSONArray();
					//JSONArray foot = new JSONArray();
					double testFee = 0;
					double materialFee = 0;
					double carFee = 0;
					double debugFee = 0;
					double repairFee = 0;
					double otherFee = 0;
					double totalFee = 0;
					int withDrawCount = 0;
					int quantity = 0;
					
					List<CommissionSheet> result = cSheetMgr.findByHQL("select model " + queryStr2 + paramString + " order by model.commissionDate desc, model.id desc", keys);
					String withDrawString = "select model.id, count(w.id) from CommissionSheet as model, Customer as c, Withdraw as w where model.customerId = c.id and w.commissionSheet.id = model.id ";
					List<Object[]> withDrawList = cSheetMgr.findByHQL(withDrawString + paramString + " group by model.id", keys);
					
					HashMap<Integer, Long> withMap = new HashMap<Integer, Long>();
					for(int i = 0; i < withDrawList.size(); i++){
						Object[] temp = withDrawList.get(i);
						withMap.put((Integer)temp[0], (Long)temp[1]);
					}
					
					String feeString = "select v from CommissionSheet as model, Customer as c, ViewCommissionSheetFee as v where model.customerId = c.id and v.id.commissionSheetId = model.id ";
					List<ViewCommissionSheetFee> FList = cSheetMgr.findByHQL(feeString + paramString, keys);
					
					HashMap<Integer, ViewCommissionSheetFee> FMap = new HashMap<Integer, ViewCommissionSheetFee>();
					for(int i = 0; i < FList.size(); i++){
						ViewCommissionSheetFee temp = FList.get(i);
						FMap.put(temp.getId().getCommissionSheetId(), temp);
					}
					
					if(result!=null&&result.size()>0){
						for(CommissionSheet cSheet : result){
							JSONObject option = new JSONObject();
							option.put("Code", cSheet.getCode());
							option.put("Pwd", cSheet.getPwd());
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CustomerTel", cSheet.getCustomerTel());
							option.put("CustomerAddress", cSheet.getCustomerAddress()==null?"":cSheet.getCustomerAddress());
							option.put("CustomerZipCode", cSheet.getCustomerZipCode()==null?"":cSheet.getCustomerZipCode());
							option.put("CustomerContactor", cSheet.getCustomerContactor()==null?"":cSheet.getCustomerContactor());
							option.put("CustomerContactorTel", cSheet.getCustomerContactorTel()==null?"":cSheet.getCustomerContactorTel());
							option.put("CommissionDate", cSheet.getCommissionDate());
							option.put("ApplianceName", cSheet.getApplianceName());
							option.put("ApplianceModel", cSheet.getApplianceModel());
							if(cSheet.getSpeciesType()){	//器具授权（分类）名称
								option.put("SpeciesType", 1);	//器具分类类型
								ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
								if(spe != null){
									option.put("ApplianceSpeciesName", spe.getName());
								}else{
									continue;
								}
							}else{	//器具标准名称
								option.put("SpeciesType", 0);
								ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
								if(stName != null){
									option.put("ApplianceSpeciesName", stName.getName());
								}else{
									continue;
								}
							}
							option.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//器具名称（常用名称）
							option.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
							option.put("AppManageCode", cSheet.getAppManageCode());	//管理编号
							option.put("Model", cSheet.getApplianceName()==null?"":cSheet.getApplianceModel()==null?"":cSheet.getApplianceModel());	//型号规格
							option.put("Range", cSheet.getRange());		//测量范围
							option.put("Accuracy", cSheet.getAccuracy());	//精度等级
							option.put("Manufacturer", cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
							option.put("Quantity", cSheet.getQuantity()==null?"":cSheet.getQuantity());	//台/件数
							option.put("MandatoryInspection", cSheet.getMandatory()?1:0);	//强制检验
							option.put("Urgent", cSheet.getUrgent()?1:0);	//加急
							option.put("Trans", cSheet.getSubcontract()?1:0);	//转包
							if(!cSheet.getSubcontract()){	//0：转包
								List<SubContract> subRetList = (new SubContractManager()).findByVarProperty(new KeyValueWithOperator("commissionSheet.id", cSheet.getId(),"="), new KeyValueWithOperator("status", 0, "="));
								if(subRetList != null && subRetList.size() > 0){
									option.put("SubContractor", subRetList.get(0).getSubContractor().getName());	//转包方
								}else{
									option.put("SubContractor", "");	//转包方
								}
							}else{
								option.put("SubContractor", "");
							}
							option.put("Appearance", cSheet.getAppearance()==null?"":cSheet.getAppearance());	//外观附件
							option.put("Repair", cSheet.getRepair()?1:0);	//修理
							option.put("ReportType", cSheet.getReportType());	//报告形式
							if(cSheet.getLocaleCommissionCode()!=null){
								option.put("LocaleCommissionCode", cSheet.getLocaleCommissionCode());//现场委托单号
								if(cSheet.getLocaleStaffId()!=null){
									SysUser localeStaff = (new UserManager()).findById(cSheet.getLocaleStaffId());
									option.put("LocaleStaff", localeStaff.getName());
								}else{
									option.put("LocaleStaff", "");
								}
							}
							option.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
							option.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
							option.put("FinishLocation", cSheet.getFinishLocation()==null?"":cSheet.getFinishLocation());	//存放位置
							option.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
							SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
							option.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
							option.put("Status", cSheet.getStatus());	//委托单状态
							
							/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
							
							//List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllFeeByCommissionSheetId, cSheet.getId());
							//List<ViewCommissionSheetFee> FList = viewCSheetFeeMgr.findByVarProperty(new KeyValueWithOperator("id.commissionSheetId", cSheet.getId(), "="));
							
							if(!FMap.containsKey(cSheet.getId())){
								option.put("TestFee", 0.0);
						    	option.put("RepairFee", 0.0);
								option.put("MaterialFee", 0.0);
								option.put("CarFee", 0.0);
								option.put("DebugFee", 0.0);
								option.put("OtherFee", 0.0);
								option.put("TotalFee", 0.0);
						    }else{
						    	ViewCommissionSheetFee fee = FMap.get(cSheet.getId());
						    	option.put("TestFee", fee.getTestFee()==null?0.0:(Double)fee.getTestFee());
								option.put("RepairFee", fee.getRepairFee()==null?0.0:(Double)fee.getRepairFee());
								option.put("MaterialFee", fee.getMaterialFee()==null?0.0:(Double)fee.getMaterialFee());
								option.put("CarFee", fee.getCarFee()==null?0.0:(Double)fee.getCarFee());
								option.put("DebugFee", fee.getDebugFee()==null?0.0:(Double)fee.getDebugFee());
								option.put("OtherFee", fee.getOtherFee()==null?0.0:(Double)fee.getOtherFee());
								option.put("TotalFee", fee.getTotalFee()==null?0.0:(Double)fee.getTotalFee());
						    }
							//查询完工器具数量和退样器具数量
							//String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
							//List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
//							if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
//								option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
//								option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
//							}else{
//								option.put("WithdrawQuantity", 0);
//								option.put("EffectQuantity", cSheet.getQuantity());
//							}
							option.put("WithdrawQuantity", withMap.containsKey(cSheet.getId())?withMap.get(cSheet.getId()):0);
							
							//String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
							//String hqlQueryString_WithdrawQuantity1 = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
							
//							List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId(), true);	//完工器具数量
//							if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
//								option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
//							}else{
//								option.put("FinishQuantity", 0);
//							}
							options.add(option);
							//System.out.println(cSheet.getCode());
							testFee += Double.parseDouble(option.getString("TestFee"));
							repairFee += Double.parseDouble(option.getString("RepairFee"));
							materialFee += Double.parseDouble(option.getString("MaterialFee"));
							debugFee += Double.parseDouble(option.getString("DebugFee"));
							carFee += Double.parseDouble(option.getString("CarFee"));
							otherFee += Double.parseDouble(option.getString("OtherFee"));
							totalFee += Double.parseDouble(option.getString("TotalFee"));
							withDrawCount += option.getInt("WithdrawQuantity");
							quantity += option.getInt("Quantity");
						}
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Code", "");
						jsonObj.put("CustomerName", "总计");
						jsonObj.put("CommissionDate", "");
						jsonObj.put("ApplianceName", "");
						jsonObj.put("ApplianceModel", "");
						
						jsonObj.put("ApplianceSpeciesName", "");
						
						jsonObj.put("SpeciesType", "");
						jsonObj.put("ApplianceName", "");	//器具名称（常用名称）
						jsonObj.put("ApplianceCode", "");	//出厂编号
						jsonObj.put("AppManageCode", "");	//管理编号
						jsonObj.put("Model", "");	//型号规格
						jsonObj.put("Range", "");		//测量范围
						jsonObj.put("Accuracy", "");	//精度等级
						jsonObj.put("Manufacturer", "");	//制造厂商
						jsonObj.put("Quantity", "");	//台/件数
						jsonObj.put("MandatoryInspection", "");	//强制检验
						jsonObj.put("Urgent", "");	//加急
						jsonObj.put("Trans", "");	//转包
						jsonObj.put("SubContractor", "");	//转包方
						jsonObj.put("Appearance", "");	//外观附件
						jsonObj.put("Repair", "");	//修理
						jsonObj.put("ReportType", "");	//报告形式
						jsonObj.put("OtherRequirements", "");	//其它要求
						jsonObj.put("Location", "");	//存放位置
						jsonObj.put("Allotee", "");	//派定人
						jsonObj.put("CommissionDate","");		//委托日期
						jsonObj.put("Status","");
						
						jsonObj.put("CustomerName", "总计");
						jsonObj.put("TestFee", testFee);
						jsonObj.put("RepairFee", repairFee);
						jsonObj.put("MaterialFee", materialFee);
						jsonObj.put("CarFee", carFee);
						jsonObj.put("DebugFee", debugFee);
						jsonObj.put("OtherFee", otherFee);
						jsonObj.put("TotalFee", totalFee);
				    	jsonObj.put("Quantity", quantity);
				    	jsonObj.put("WithdrawQuantity", withDrawCount);
						options.add(jsonObj);
					}
				}
				
				String filePath = ExportUtil.ExportToExcelByResultSet(options, null, "Mission_formatExcel", "Mission_formatTitle", ExportManager.class);
				retObj17.put("IsOK", filePath.equals("")?false:true);
				retObj17.put("Path", filePath);
			}catch(Exception e){
				try{
					retObj17.put("IsOK", false);
					retObj17.put("Path", "");
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 10", e);
				}else{
					log.error("error in QueryServlet-->case 10", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj17.toString());
			}
			break;
		case 11://根据日期查询业务量导出
			String paramsStr11 = req.getParameter("paramsStr");
			JSONObject retJSON11 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr11);
				
				String StartTime = params.has("StartTime")?params.getString("StartTime"):"";
				String EndTime = params.has("EndTime")?params.getString("EndTime"):"";
				String CustomerId = params.has("CustomerId")?params.getString("CustomerId"):"";
				String DepartmentId = params.has("DepartmentId")?params.getString("DepartmentId"):"";
				String EmployeeId = params.has("EmployeeId")?params.getString("EmployeeId"):"";
				String Status = params.has("Status")?params.getString("Status"):"";
				String HeadName = params.has("HeadName")?params.getString("HeadName"):"";
				
				String queryStr = "select model from ViewTransaction as model where model.commissionDate >= ? and model.commissionDate <= ? and model.tstatus = 0 ";				
				
				List<ViewTransaction> statistic;
				List<JSONObject> options = new ArrayList<JSONObject>();
				ViewTransactionManager vTranMgr = new ViewTransactionManager();
				List<Object> keys = new ArrayList<Object>();
				
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
				
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					keys.add(Start);
					keys.add(End);
					
					if(CustomerId!=null&&!CustomerId.equals(""))
					{
						queryStr = queryStr + " and model.customerId = ? ";
						keys.add(Integer.valueOf(CustomerId));
					}
					if(DepartmentId!=null&&!DepartmentId.equals(""))
					{
						queryStr = queryStr + " and model.allotee in (select s.id from SysUser as s, ProjectTeam as p where p.department.id = ? and s.projectTeamId = p.id)";
						keys.add(Integer.valueOf(DepartmentId));
					}
					if(EmployeeId!=null&&!EmployeeId.equals(""))
					{
						queryStr = queryStr + " and model.allotee = ? ";
						keys.add(Integer.valueOf(EmployeeId));
					}
					if(HeadName!=null&&!HeadName.equals(""))
					{
						queryStr = queryStr + " and model.headName = ? ";
						keys.add(HeadName);
					}
					if(Status!=null&&!Status.equals(""))
					{
						String statusStr = URLDecoder.decode(Status, "UTF-8");
						if(statusStr.equals("<3")){
							queryStr = queryStr + " and model.cstatus < ?";
							keys.add(Integer.valueOf(3));
						}else if(statusStr.equals("<4")){
							queryStr = queryStr + " and model.cstatus < ?";
							keys.add(Integer.valueOf(4));
						}else{
							queryStr = queryStr + " and model.cstatus = ?";
							keys.add(Integer.valueOf(statusStr));
						}						
					}
					
					statistic = vTranMgr.findByHQL(queryStr + " order by model.code asc, model.id.cid desc", keys);
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(ViewTransaction obj : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("Allotee", (new UserManager()).findById(obj.getAllotee()).getName());
							option.put("Code", obj.getCode());
							option.put("CommissionDate", obj.getCommissionDate());
							option.put("CStatus", obj.getCstatus());
							option.put("ApplianceName", obj.getApplianceName());
							option.put("CustomerId", obj.getCustomerId());
							option.put("CustomerName", obj.getCustomerName());
							
							String FQueryStr = "select sum(model.totalFee), sum(model.testFee), sum(model.materialFee), sum(model.repairFee), sum(model.debugFee), sum(model.carFee), sum(model.otherFee) from OriginalRecord as model where model.taskAssign.id = ? and model.sysUserByStaffId.id = ?";
							List<Object[]> FList = vTranMgr.findByHQL(FQueryStr, obj.getId().getTid(), obj.getAllotee());
							if(FList==null||FList.isEmpty()){
						    	option.put("TestFee", 0.0);
						    	option.put("RepairFee", 0.0);
						    	option.put("MaterialFee", 0.0);
						    	option.put("CarFee", 0.0);
						    	option.put("DebugFee", 0.0);
						    	option.put("OtherFee", 0.0);
						    	option.put("TotalFee", 0.0);
						    }else{
						    	Object[] fee = FList.get(0);					   						    	
						    	option.put("TotalFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
						    	option.put("TestFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
						    	option.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
						    	option.put("RepairFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
						    	option.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
						    	option.put("CarFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
						    	option.put("OtherFee", (Double)fee[6]==null?0.0:(Double)fee[6]);			   
						    }
							
							options.add(option);
						}
					}
				}
				String filePath = ExportUtil.ExportToExcelByResultSet(options, null, "Transaction_formatExcel", "Transaction_formatTitle", ExportManager.class);
				retJSON11.put("IsOK", filePath.equals("")?false:true);
				retJSON11.put("Path", filePath);
				
			} catch (Exception e){
				try {
					retJSON11.put("IsOK", false);
					retJSON11.put("Path", "");
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 11", e);
				}else{
					log.error("error in QueryServlet-->case 11", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON11.toString());
			}
			break;
		}
	}
	
}
