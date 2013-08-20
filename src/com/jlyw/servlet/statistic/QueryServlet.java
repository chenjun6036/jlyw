package com.jlyw.servlet.statistic;

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
import org.json.me.JSONObject;

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Department;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.DepartmentManager;
import com.jlyw.manager.DetailListManager;
import com.jlyw.manager.DiscountComSheetManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.statistic.ExportManager;
import com.jlyw.util.CommissionSheetFlagUtil;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;

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
					Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
					keys.add(new KeyValueWithOperator("finishDate", Start, ">="));
				}
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
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
				DetailListManager detailListMgr = new DetailListManager();
				List<DetailList> detailList = detailListMgr.findPagedAllBySort(page, rows, "lastEditTime", false, keys);
				int total = detailListMgr.getTotalCount(keys);
				JSONArray options = new JSONArray();
				if(detailList != null && detailList.size() > 0){
					for(DetailList detail : detailList)
					{
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", detail.getId());
						jsonObj.put("Code", detail.getCode());
						jsonObj.put("TotalFee", detail.getTotalFee());
						jsonObj.put("PaidFee", detail.getPaidFee());
						jsonObj.put("Cash", detail.getCashPaid());
						jsonObj.put("Cheque", detail.getChequePaid());
						jsonObj.put("Account", detail.getAccountPaid());
						jsonObj.put("InvoiceCode", detail.getInvoiceCode());
						jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						jsonObj.put("CheckOutDate", sf.format(detail.getLastEditTime()));		//结账日期
						
						options.put(jsonObj);
					}
				}
				retObj3.put("total", total);
				retObj3.put("rows", options);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QueryServlet-->case 3", e);
				}else{
					log.error("error in QueryServlet-->case 3", e);
				}
				try{
					retObj3.put("total", 0);
					retObj3.put("rows", new JSONArray());
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
						jsonObj.put("TotalFee", !detail.getCode().equals(tempDetailListCode)?detail.getTotalFee():0);
						jsonObj.put("PaidFee", !detail.getCode().equals(tempDetailListCode)?detail.getPaidFee():0);
						jsonObj.put("Cash", !detail.getCode().equals(tempDetailListCode)?detail.getCashPaid():0);
						jsonObj.put("Cheque",!detail.getCode().equals(tempDetailListCode)?detail.getChequePaid():0);
						jsonObj.put("Account", !detail.getCode().equals(tempDetailListCode)?detail.getAccountPaid():0);
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
						jsonObj.put("TotalFee", detail.getTotalFee());
						jsonObj.put("PaidFee", detail.getPaidFee());
						jsonObj.put("Cash", detail.getCashPaid());
						jsonObj.put("Cheque", detail.getChequePaid());
						jsonObj.put("Account", detail.getAccountPaid());
						jsonObj.put("InvoiceCode", detail.getInvoiceCode());
						jsonObj.put("CheckOutStaff", detail.getSysUser().getName());
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						jsonObj.put("CheckOutDate", sf.format(detail.getLastEditTime()));		//结账日期
						
						options.put(jsonObj);
					}
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
					log.debug("exception in StatisticServlet-->case 7", e);
				}else{
					log.error("error in StatisticServlet-->case 7", e);
				}
			}finally{
				
			}
			break;
		}
	}
	
}
