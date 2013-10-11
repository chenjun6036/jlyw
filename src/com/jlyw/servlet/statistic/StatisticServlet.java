package com.jlyw.servlet.statistic;

import java.io.IOException;
import java.net.URLDecoder;
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
import com.jlyw.hibernate.Department;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.view.ViewCommissionSheetFee;
import com.jlyw.hibernate.view.ViewTransaction;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.DepartmentManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.TaskAssignManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.statistic.ExportManager;
import com.jlyw.manager.view.ViewCommissionSheetFeeManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class StatisticServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(StatisticServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		TaskAssignManager taskMgr = new TaskAssignManager();
		CertificateFeeAssignManager cerFeeMgr=new CertificateFeeAssignManager();
		switch(method)
		{
		case 0://根据日期查询业务量
			JSONObject retJSON = new JSONObject();
			try {
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String CustomerId = req.getParameter("CustomerId");
				String DepartmentId = req.getParameter("DepartmentId");
				String EmployeeId = req.getParameter("EmployeeId");
				String HeadName = req.getParameter("HeadName");
				String Status = req.getParameter("Status");
				
				String queryStr = "select model from ViewTransaction as model where model.commissionDate >= ? and model.commissionDate <= ? and model.tstatus = 0 ";
				String TotalqueryStr = "select count(*) from ViewTransaction as model where model.commissionDate >= ? and model.commissionDate <= ? and model.tstatus = 0 ";
				String doneTotalqueryStr = "select count(distinct model.code) from ViewTransaction as model where model.tstatus = 0 and (model.cstatus = 3 or model.cstatus = 4 or model.cstatus = 9) " +
						"and (model.commissionDate >= ? and model.commissionDate <= ?)";				
				
				int total = 0;
				int doneTotal = 0;
				List<ViewTransaction> statistic;
				JSONArray options = new JSONArray();
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
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
						TotalqueryStr = TotalqueryStr + " and model.customerId = ?";
						doneTotalqueryStr = doneTotalqueryStr + " and model.customerId = ?";
						keys.add(Integer.valueOf(CustomerId));
					}
					if(DepartmentId!=null&&!DepartmentId.equals(""))
					{
						queryStr = queryStr + " and model.allotee in (select s.id from SysUser as s, ProjectTeam as p where p.department.id = ? and s.projectTeamId = p.id)";
						TotalqueryStr = TotalqueryStr + " and model.allotee in (select s.id from SysUser as s, ProjectTeam as p where p.department.id = ? and s.projectTeamId = p.id)";
						doneTotalqueryStr = doneTotalqueryStr + " and model.allotee in (select s.id from SysUser as s, ProjectTeam as p where p.department.id = ? and s.projectTeamId = p.id)";
						keys.add(Integer.valueOf(DepartmentId));
					}
					if(EmployeeId!=null&&!EmployeeId.equals(""))
					{
						queryStr = queryStr + " and model.allotee = ? ";
						TotalqueryStr = TotalqueryStr + " and model.allotee = ?";
						doneTotalqueryStr = doneTotalqueryStr + " and model.allotee = ?";
						keys.add(Integer.valueOf(EmployeeId));
					}
					if(HeadName!=null&&!HeadName.equals(""))
					{
						queryStr = queryStr + " and model.headName = ? ";
						TotalqueryStr = TotalqueryStr + " and model.headName = ?";
						doneTotalqueryStr = doneTotalqueryStr + " and model.headName = ?";
						keys.add(HeadName);
					}
					if(Status!=null&&!Status.equals(""))
					{
						String statusStr = URLDecoder.decode(Status, "UTF-8");
						if(statusStr.equals("<3")){
							queryStr = queryStr + " and model.cstatus < ?";
							TotalqueryStr = TotalqueryStr + " and model.cstatus = ?";
							doneTotalqueryStr = doneTotalqueryStr + " and model.cstatus = ?";
							keys.add(Integer.valueOf(3));
						}else if(statusStr.equals("<4")){
							queryStr = queryStr + " and model.cstatus < ?";
							TotalqueryStr = TotalqueryStr + " and model.cstatus = ?";
							doneTotalqueryStr = doneTotalqueryStr + " and model.cstatus = ?";
							keys.add(Integer.valueOf(4));
						}else{
							queryStr = queryStr + " and model.cstatus = ?";
							TotalqueryStr = TotalqueryStr + " and model.cstatus = ?";
							doneTotalqueryStr = doneTotalqueryStr + " and model.cstatus = ?";
							keys.add(Integer.valueOf(statusStr));
						}						
					}
					
					statistic = taskMgr.findPageAllByHQL(queryStr + " order by model.code asc, model.id.cid desc", page, rows, keys);
					total = taskMgr.getTotalCountByHQL(TotalqueryStr, keys);
					doneTotal = taskMgr.getTotalCountByHQL(doneTotalqueryStr, keys);
					
					
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
							List<Object[]> FList = taskMgr.findByHQL(FQueryStr, obj.getId().getTid(), obj.getAllotee());
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
							
							options.put(option);
						}
					}
				}
				retJSON.put("total", total);
				retJSON.put("rows", options);
				retJSON.put("doneTotal", doneTotal);//已完工委托单数
				
			} catch (Exception e){
				
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
					retJSON.put("doneTotal", 0);
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 0", e);
				}else{
					log.error("error in StatisticServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1://根据日期查询产值（全所）
			JSONObject retObj = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				//String CustomerId = req.getParameter("CustomerId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					
					String queryStr = "select dept.DeptCode,dept.Name as deptName,model.Name as UserName,model.JobNum,temp.*,model.id as userId,dept.id as deptId from " + SystemCfgUtil.DBPrexName + "SysUser as model left join (select cer.FeeAlloteeId as feeAlloteeId, SUM(cer.totalFee) as totalfee, SUM(cer.testFee) as testfee, SUM(cer.repairFee) as repairfee, SUM(cer.materialFee) as materialfee, SUM(cer.carFee) as carfee, SUM(cer.debugFee) as debugfee, SUM(cer.otherFee) as otherfee from" + SystemCfgUtil.DBPrexName + "CertificateFeeAssign as cer, " + SystemCfgUtil.DBPrexName + "CommissionSheet as c " +
									  " where cer.CommissionSheetId = c.Id" +
									  " and (c.status = ? or c.status = ?) and (c.checkOutDate >= ? and c.checkOutDate <= ?)";
					String DeptqueryStr1 = "select sum(model.quantity) from OriginalRecord as model where " +
									" model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";
					
					String DeptqueryStr2 = "select sum(model.number) from Withdraw as model where "+
					 				" model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					
					String DeptqueryStr3 = "select count(o.certificate.id) from OriginalRecord as o where" +
									   " o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr = queryStr + " and c.commissionType = " + CommissionType;
						DeptqueryStr1 = DeptqueryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
						DeptqueryStr2 = DeptqueryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
						DeptqueryStr3 = DeptqueryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					if(HeadName!=null&&!HeadName.equals("")){
						queryStr = queryStr + " and c.headNameId = " + HeadName;
						DeptqueryStr1 = DeptqueryStr1 + " and model.commissionSheet.headNameId = " + HeadName;
						DeptqueryStr2 = DeptqueryStr2 + " and model.commissionSheet.headNameId = " + HeadName;
						DeptqueryStr3 = DeptqueryStr3 + " and o.commissionSheet.headNameId = " + HeadName;
					}
					queryStr = queryStr + " and ( " +
									  "   (cer.originalRecordid is null and cer.certificateid is null) or " +
									  "   (cer.originalRecordid in (select o.id from " + SystemCfgUtil.DBPrexName + "OriginalRecord as o," + SystemCfgUtil.DBPrexName + "TaskAssign as t where o.TaskAssignId = t.Id and o.status <>1 and t.status<>1 and o.certificateid = cer.certificateid)) " +
									  " ) " +
									  " group by cer.FeeAlloteeId) as temp on model.id = temp.feeAlloteeId," + SystemCfgUtil.DBPrexName + "ProjectTeam as p," + SystemCfgUtil.DBPrexName + "Department as dept " +
									  " where model.ProjectTeamId = p.Id and p.DeptId = dept.Id" + 
									  "	order by dept.DeptCode asc, model.JobNum asc";
					
					List<Object[]> statistic;
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					
					statistic = cSheetMgr.findBySQL(queryStr, new Integer(4), new Integer(9), Start, End);
					//total = cSheetMgr.getTotalCountByHQL(queryTotalStr, new Integer(4), new Integer(9), Start, End);
					
					int quantity = 0;
					int withdraw = 0;
					int certificate = 0;
					double totalFee = 0;
					double testFee = 0;
					double repairFee = 0;
					double materialFee = 0;
					double carFee = 0;
					double debugFee = 0;
					double otherFee = 0;
					
					if(statistic!=null&&statistic.size()>0)
					{
						String tempCode = null;
						String tempName = null;
						int tempDeptId = 0;
						int quantityByDept = 0;
						int withdrawByDept = 0;
						int certificateByDept = 0;
						double totalFeeByDept = 0;
						double testFeeByDept = 0;
						double repairFeeByDept = 0;
						double materialFeeByDept = 0;
						double carFeeByDept = 0;
						double debugFeeByDept = 0;
						double otherFeeByDept = 0;
						int len = 0;
						
						for(Object[] temp : statistic)
						{
							if(tempCode==null){
								options.put(0);
							}
							if(tempCode!=null&&!tempCode.equals(temp[0].toString())){
								
								List<Long> quantitysByDept = cSheetMgr.findByHQL(DeptqueryStr1 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" , Start, End, tempDeptId);
								quantityByDept = (quantitysByDept==null||quantitysByDept.size()==0?0:quantitysByDept.get(0)==null?0:quantitysByDept.get(0).intValue());
								List<Long> withdrawsByDept = cSheetMgr.findByHQL(DeptqueryStr2 + " and model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, tempDeptId);
								withdrawByDept = (withdrawsByDept==null||withdrawsByDept.size()==0?0:withdrawsByDept.get(0)==null?0:withdrawsByDept.get(0).intValue());
								List<Long> certificatesByDept = (new OriginalRecordManager()).findByHQL(DeptqueryStr3 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, tempDeptId);
								certificateByDept =(certificatesByDept==null||certificatesByDept.size()==0?0:certificatesByDept.get(0)==null?0:certificatesByDept.get(0).intValue());
								
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("DeptId", tempDeptId);
								jsonObj.put("DeptCode", tempCode);
								jsonObj.put("DeptName", tempName);
								jsonObj.put("TotalFee", totalFeeByDept);
								jsonObj.put("TestFee", testFeeByDept);
								jsonObj.put("RepairFee", repairFeeByDept);
								jsonObj.put("MaterialFee", materialFeeByDept);
								jsonObj.put("CarFee", carFeeByDept);
								jsonObj.put("DebugFee", debugFeeByDept);
								jsonObj.put("OtherFee", otherFeeByDept);
								jsonObj.put("Quantity", quantityByDept);
								jsonObj.put("Withdraw", withdrawByDept);
								jsonObj.put("Certificate", certificateByDept);
								
								options.put(options.length()-len-1,jsonObj);
								
								totalFeeByDept = repairFeeByDept = testFeeByDept = materialFeeByDept = carFeeByDept = debugFeeByDept = otherFeeByDept = 0;
								len = 0;
								options.put(0);
							}
							JSONObject option = new JSONObject();
							option.put("UserName", temp[2].toString());
							option.put("UserJobNum", temp[3].toString());
							option.put("TotalFee", temp[5]==null?0:(Double)temp[5]);
							option.put("TestFee", temp[6]==null?0:(Double)temp[6]);
							option.put("RepairFee", temp[7]==null?0:(Double)temp[7]);
							option.put("MaterialFee", temp[8]==null?0:(Double)temp[8]);
							option.put("CarFee", temp[9]==null?0:(Double)temp[9]);
							option.put("DebugFee", temp[10]==null?0:(Double)temp[10]);
							option.put("OtherFee", temp[11]==null?0:(Double)temp[11]);
							option.put("UserId", temp[12]==null?0:(Integer)temp[12]);
							
							String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? and  model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, (Integer)temp[12], Start, End);
							option.put("Quantity", quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
							
							String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, (Integer)temp[12], Start, End);
							option.put("Withdraw", withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
							
							String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
											   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ? and model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr3 = queryStr3 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, (Integer)temp[12], Start, End);
							option.put("Certificate", certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
														
							totalFeeByDept += (Double)option.get("TotalFee");
							testFeeByDept += (Double)option.get("TestFee");
							repairFeeByDept += (Double)option.get("RepairFee");
							materialFeeByDept += (Double)option.get("MaterialFee");
							carFeeByDept += (Double)option.get("CarFee");
							debugFeeByDept += (Double)option.get("DebugFee");
							otherFeeByDept += (Double)option.get("OtherFee");
							
							totalFee += (Double)option.get("TotalFee");
							testFee += (Double)option.get("TestFee");
							repairFee += (Double)option.get("RepairFee");
							materialFee += (Double)option.get("MaterialFee");
							carFee += (Double)option.get("CarFee");
							debugFee += (Double)option.get("DebugFee");
							otherFee += (Double)option.get("OtherFee");
							
							len++;
							
							options.put(option);
							
							tempCode = temp[0].toString();
							tempName = temp[1].toString();
							tempDeptId = (Integer)temp[13];
						}
						
						List<Long> quantitysByDept = cSheetMgr.findByHQL(DeptqueryStr1, Start, End);
						quantity = (quantitysByDept==null||quantitysByDept.size()==0?0:quantitysByDept.get(0)==null?0:quantitysByDept.get(0).intValue());
						List<Long> withdrawsByDept = cSheetMgr.findByHQL(DeptqueryStr2, Start, End);
						withdraw = (withdrawsByDept==null||withdrawsByDept.size()==0?0:withdrawsByDept.get(0)==null?0:withdrawsByDept.get(0).intValue());
						List<Long> certificatesByDept = (new OriginalRecordManager()).findByHQL(DeptqueryStr3, Start, End);
						certificate =(certificatesByDept==null||certificatesByDept.size()==0?0:certificatesByDept.get(0)==null?0:certificatesByDept.get(0).intValue());
						
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("DeptCode", tempCode);
						jsonObj.put("DeptId", tempDeptId);
						jsonObj.put("DeptName", tempName);
						jsonObj.put("TotalFee", totalFeeByDept);
						jsonObj.put("TestFee", testFeeByDept);
						jsonObj.put("RepairFee", repairFeeByDept);
						jsonObj.put("MaterialFee", materialFeeByDept);
						jsonObj.put("CarFee", carFeeByDept);
						jsonObj.put("DebugFee", debugFeeByDept);
						jsonObj.put("OtherFee", otherFeeByDept);
						jsonObj.put("Quantity", quantityByDept);
						jsonObj.put("Withdraw", withdrawByDept);
						jsonObj.put("Certificate", certificateByDept);
						
						options.put(options.length()-len-1,jsonObj);
					}
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("DeptCode", "总产值");
					jsonObj.put("TotalFee", totalFee);
					jsonObj.put("TestFee", testFee);
					jsonObj.put("RepairFee", repairFee);
					jsonObj.put("MaterialFee", materialFee);
					jsonObj.put("CarFee", carFee);
					jsonObj.put("DebugFee", debugFee);
					jsonObj.put("OtherFee", otherFee);
					jsonObj.put("Quantity", quantity);
					jsonObj.put("Withdraw", withdraw);
					jsonObj.put("Certificate", certificate);
					
					foot.put(jsonObj);
				}
				retObj.put("total", total);
				retObj.put("rows", options);
				retObj.put("footer", foot);
			}catch(Exception e){
				
				try{
					retObj.put("total", 0);
					retObj.put("rows", new JSONArray());
					JSONArray foot = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("DeptCode", "总产值");
					jsonObj.put("TotalFee", 0);
					jsonObj.put("TestFee", 0);
					jsonObj.put("RepairFee", 0);
					jsonObj.put("MaterialFee", 0);
					jsonObj.put("CarFee", 0);
					jsonObj.put("DebugFee", 0);
					jsonObj.put("OtherFee", 0);
					jsonObj.put("Quantity", 0);
					jsonObj.put("Withdraw", 0);
					jsonObj.put("Certificate", 0);
					
					foot.put(jsonObj);
					retObj.put("footer", foot);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 1", e);
				}else{
					log.error("error in StatisticServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;	
		case 2://根据员工号统计产值(考虑到一个委托单下可能会有多道检验工序，可能由不同的员工完成，需要从CertificateFeeAssign中查询统计，故单独)
			JSONObject res = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String EmployeeId = req.getParameter("EmployeeId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int total = 0;
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				double JDFee=0.0,JZFee=0.0,JCFee=0.0,JYFee=0.0;
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				JSONArray options = new JSONArray();
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String tempCode="";
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String TotalqueryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList();
					List<Object[]> FList = new ArrayList();
					List<Double> JDList = new ArrayList();
					List<Double> JZList = new ArrayList();
					List<Double> JYList = new ArrayList();
					List<Double> JCList = new ArrayList();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								" and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
										" ) " ;
			
			   	    TotalqueryStr = "select count(model.id) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?"+
								" and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
								" ) " ;
			
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
					         " and model.sysUserByFeeAlloteeId.id = ?"+
					         " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
								" ) " ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检定' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '校准' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检测' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检验' and o.taskAssign.status <> 1)) " +
										" ) ";
				    
				    keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
				    keys.add(Integer.valueOf(EmployeeId));
					if(CommissionType!=null&&CommissionType.length()>0){
					
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&!HeadName.equals("")){
						 queryStr = queryStr + " and model.commissionSheet.headNameId = ?";
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					statistic = cerFeeMgr.findPageAllByHQL(queryStr +"order by model.commissionSheet.code desc,model.id desc", page, rows, keys);
					 FList=cerFeeMgr.findByHQL(queryStr4, keys);
					 total = cerFeeMgr.getTotalCountByHQL(TotalqueryStr, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", DateTimeFormatUtil.DateFormat.format(cSheet.getCheckOutDate()));
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", cerfee.getCommissionSheet().getApplianceName());	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							options.put(option);
						}
					}
					/***统计产值****/
					String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? " + 
									" and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, Integer.valueOf(EmployeeId), Start, End);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, Integer.valueOf(EmployeeId), Start, End);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
									   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, Integer.valueOf(EmployeeId), Start, End);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
					JDList=cerFeeMgr.findByHQL(JDqueryStr,keys);
					if(JDList!=null&&JDList.size()>0){
						Double fee =JDList.get(0);	
						JDFee=((Double)fee==null?0.0:(Double)fee);
					}
					JZList=cerFeeMgr.findByHQL(JZqueryStr,keys);
					if(JZList!=null&&JZList.size()>0){
						Double fee =JZList.get(0);	
						JZFee=((Double)fee==null?0.0:(Double)fee);
					}
					JYList=cerFeeMgr.findByHQL(JYqueryStr,keys);
					if(JYList!=null&&JYList.size()>0){
						Double fee =JYList.get(0);	
						JYFee=((Double)fee==null?0.0:(Double)fee);
					}
					JCList=cerFeeMgr.findByHQL(JCqueryStr,keys);
					if(JCList!=null&&JCList.size()>0){
						Double fee =JCList.get(0);	
						JCFee=((Double)fee==null?0.0:(Double)fee);
					}
					
				}
				JSONArray footerArray = new JSONArray();
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				
				footerArray.put(footerObj);
				res.put("total", total);
				res.put("rows", options);
				res.put("footer", footerArray);
				res.put("JDFee", JDFee);
				res.put("JZFee", JZFee);
				res.put("JYFee", JYFee);
				res.put("JCFee", JCFee);
				res.put("TotalFee", TotalFee);
			}catch(Exception e){
				
				try{
					res.put("total", 0);
					res.put("rows", new JSONArray());
					res.put("TotalFee", 0);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 2", e);
				}else{
					log.error("error in StatisticServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3://部门产值查询（每个人一条记录）
			JSONObject res3 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String DepartmentId = req.getParameter("DepartmentId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int deptId = Integer.valueOf(DepartmentId);
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					
					String queryStr = "select model.Id,dept.Name as deptName,model.Name as UserName,model.JobNum,temp.*,model.id from " + SystemCfgUtil.DBPrexName + "SysUser as model left join (select cer.FeeAlloteeId as feeAlloteeId, SUM(cer.totalFee) as totalfee, SUM(cer.testFee) as testfee, SUM(cer.repairFee) as repairfee, SUM(cer.materialFee) as materialfee, SUM(cer.carFee) as carfee, SUM(cer.debugFee) as debugfee, SUM(cer.otherFee) as otherfee from" + SystemCfgUtil.DBPrexName + "CertificateFeeAssign as cer, " + SystemCfgUtil.DBPrexName + "CommissionSheet as c " +
									  " where cer.CommissionSheetId = c.Id" +
									  " and (c.status = ? or c.status = ?) and (c.checkOutDate >= ? and c.checkOutDate <= ?)";
					String DeptqueryStr1 = "select sum(model.quantity) from OriginalRecord as model where " +
									" model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 " + 
									" and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" ;//签字通过的原始记录(签字已通过且不是正在后台执行)
					String DeptqueryStr2 = "select sum(model.number) from Withdraw as model where "+
					 				" model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" + 
									" and model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" ;
					
					String DeptqueryStr3 = "select count(o.certificate.id) from OriginalRecord as o where" +
									   " o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 " + 
										" and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" ;
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr = queryStr + " and c.commissionType = " + CommissionType;
						DeptqueryStr1 = DeptqueryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
						DeptqueryStr2 = DeptqueryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
						DeptqueryStr3 = DeptqueryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					if(HeadName!=null&&!HeadName.equals("")){
						queryStr = queryStr + " and c.headNameId = " + HeadName;
						DeptqueryStr1 = DeptqueryStr1 + " and model.commissionSheet.headNameId = " + HeadName;
						DeptqueryStr2 = DeptqueryStr2 + " and model.commissionSheet.headNameId = " + HeadName;
						DeptqueryStr3 = DeptqueryStr3 + " and o.commissionSheet.headNameId = " + HeadName;
					}
					queryStr = queryStr + " and ( " +
									  "   (cer.originalRecordid is null and cer.certificateid is null) or " +
									  "   (cer.originalRecordid in (select o.id from " + SystemCfgUtil.DBPrexName + "OriginalRecord as o," + SystemCfgUtil.DBPrexName + "TaskAssign as t where o.TaskAssignId = t.Id and o.status <>1 and t.status<>1 and o.certificateid = cer.certificateid)) " +
									  " ) " +
									  " group by cer.FeeAlloteeId) as temp on model.id = temp.feeAlloteeId," + SystemCfgUtil.DBPrexName + "ProjectTeam as p," + SystemCfgUtil.DBPrexName + "Department as dept " +
									  " where model.ProjectTeamId = p.Id and p.DeptId = dept.Id" + 
									  " and p.DeptId = ?"+
									  "	order by dept.DeptCode asc, model.JobNum asc";
					
					List<Object[]> statistic;
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					
					statistic = cSheetMgr.findBySQL(queryStr, new Integer(4), new Integer(9), Start, End,deptId);
					//total = cSheetMgr.getTotalCountByHQL(queryTotalStr, new Integer(4), new Integer(9), Start, End);
					
					int quantity = 0;
					int withdraw = 0;
					int certificate = 0;
					double totalFee = 0;
					double testFee = 0;
					double repairFee = 0;
					double materialFee = 0;
					double carFee = 0;
					double debugFee = 0;
					double otherFee = 0;
					
					if(statistic!=null&&statistic.size()>0)
					{						
						for(Object[] temp : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("UserId", temp[0].toString());
							option.put("UserName", temp[2].toString());
							option.put("UserJobNum", temp[3].toString());
							option.put("TotalFee", temp[5]==null?0:(Double)temp[5]);
							option.put("TestFee", temp[6]==null?0:(Double)temp[6]);
							option.put("RepairFee", temp[7]==null?0:(Double)temp[7]);
							option.put("MaterialFee", temp[8]==null?0:(Double)temp[8]);
							option.put("CarFee", temp[9]==null?0:(Double)temp[9]);
							option.put("DebugFee", temp[10]==null?0:(Double)temp[10]);
							option.put("OtherFee", temp[11]==null?0:(Double)temp[11]);
							
							String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? and  model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, (Integer)temp[12], Start, End);
							option.put("Quantity", quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
							
							String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, (Integer)temp[12], Start, End);
							option.put("Withdraw", withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
							
							String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
											   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ? and model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, (Integer)temp[12], Start, End);
							option.put("Certificate", certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
							
							totalFee += (Double)option.get("TotalFee");
							testFee += (Double)option.get("TestFee");
							repairFee += (Double)option.get("RepairFee");
							materialFee += (Double)option.get("MaterialFee");
							carFee += (Double)option.get("CarFee");
							debugFee += (Double)option.get("DebugFee");
							otherFee += (Double)option.get("OtherFee");
							
							options.put(option);
						}
						List<Long> quantitysByDept = cSheetMgr.findByHQL(DeptqueryStr1, Start, End, deptId);
						quantity = (quantitysByDept==null||quantitysByDept.size()==0?0:quantitysByDept.get(0)==null?0:quantitysByDept.get(0).intValue());
						List<Long> withdrawsByDept = cSheetMgr.findByHQL(DeptqueryStr2, Start, End, deptId);
						withdraw = (withdrawsByDept==null||withdrawsByDept.size()==0?0:withdrawsByDept.get(0)==null?0:withdrawsByDept.get(0).intValue());
						List<Long> certificatesByDept = (new OriginalRecordManager()).findByHQL(DeptqueryStr3, Start, End, deptId);
						certificate =(certificatesByDept==null||certificatesByDept.size()==0?0:certificatesByDept.get(0)==null?0:certificatesByDept.get(0).intValue());
					}
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("UserJobNum", "总产值");
					jsonObj.put("TotalFee", totalFee);
					jsonObj.put("TestFee", testFee);
					jsonObj.put("RepairFee", repairFee);
					jsonObj.put("MaterialFee", materialFee);
					jsonObj.put("CarFee", carFee);
					jsonObj.put("DebugFee", debugFee);
					jsonObj.put("OtherFee", otherFee);
					jsonObj.put("Quantity", quantity);
					jsonObj.put("Withdraw", withdraw);
					jsonObj.put("Certificate", certificate);
					
					foot.put(jsonObj);
				}
				res3.put("total", total);
				res3.put("rows", options);
				res3.put("footer", foot);
			}catch(Exception e){
				
				try{
					res3.put("total", 0);
					res3.put("rows", new JSONArray());
					JSONArray foot = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("UserJobNum", "总产值");
					jsonObj.put("TotalFee", 0);
					jsonObj.put("TestFee", 0);
					jsonObj.put("RepairFee", 0);
					jsonObj.put("MaterialFee", 0);
					jsonObj.put("CarFee", 0);
					jsonObj.put("DebugFee", 0);
					jsonObj.put("OtherFee", 0);
					jsonObj.put("Quantity", 0);
					jsonObj.put("Withdraw", 0);
					jsonObj.put("Certificate", 0);
					
					foot.put(jsonObj);
					res3.put("footer", foot);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 3", e);
				}else{
					log.error("error in StatisticServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(res3.toString());
			}
			break;		
		case 4://业务查询
			JSONObject retObj4 = new JSONObject();
			try{
				ViewCommissionSheetFeeManager viewCSheetFeeMgr = new ViewCommissionSheetFeeManager();
				String Code = req.getParameter("Code");
				String CommissionType = req.getParameter("CommissionType");
				String ReportType = req.getParameter("ReportType");
				String CustomerId = req.getParameter("CustomerId");
				String ZipCode = req.getParameter("ZipCode");
				String LocaleMissionCode = req.getParameter("LocaleMissionCode");
				String RegionId = req.getParameter("RegionId");
				String Classi = req.getParameter("Classi");
				String CommissionDateFrom = req.getParameter("CommissionDateFrom");
				String CommissionDateEnd = req.getParameter("CommissionDateEnd");
				String FinishDateFrom = req.getParameter("FinishDateFrom");
				String FinishDateEnd = req.getParameter("FinishDateEnd");
				String CheckOutDateFrom = req.getParameter("CheckOutDateFrom");
				String CheckOutDateEnd = req.getParameter("CheckOutDateEnd");
				String Status = req.getParameter("Status");
				String Receiver = req.getParameter("Receiver");
				String FinishUser = req.getParameter("FinishUser");
				String CheckOutUser = req.getParameter("CheckOutUser");
				String SpeciesType = req.getParameter("SpeciesType");
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");
				String InsideContactor = req.getParameter("InsideContactor");
				String HeadName = req.getParameter("HeadName");
				
				
				String queryStr = "from CommissionSheet as model,Customer as c where model.customerId = c.id and 1=1 ";
				String queryStringAllFee = " select SUM(view.testFee),SUM(view.repairFee),SUM(view.materialFee),SUM(view.carFee),SUM(view.debugFee),SUM(view.otherFee),SUM(view.totalFee) from ViewCommissionSheetFee as view, CommissionSheet as model, Customer as c " +
											" where model.customerId = c.id and view.id.commissionSheetId = model.id ";
				String queryStringCount = "select SUM(model.quantity) from CommissionSheet as model, Customer as c where model.customerId = c.id and 1=1 ";
				String withDrawString = "select SUM(w.number) from CommissionSheet as model, Withdraw as w, Customer as c where w.commissionSheet.id = model.id and w.executeResult=1 and model.customerId = c.id and 1=1 ";
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
					Timestamp End = Timestamp.valueOf(FinishDateEnd + " 23:59:59");
					
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
						paramString = paramString + " and (model.status < ?)";
						keys.add(Integer.valueOf(4));
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
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				
				List<CommissionSheet> result = cSheetMgr.findPageAllByHQL("select model " + queryStr + paramString + " order by model.commissionDate desc, model.id desc", page, rows, keys);
				total = cSheetMgr.getTotalCountByHQL("select count(model) " + queryStr + paramString, keys);
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
						List<ViewCommissionSheetFee> FList = viewCSheetFeeMgr.findByVarProperty(new KeyValueWithOperator("id.commissionSheetId", cSheet.getId(), "="));
						
						
						if(FList.isEmpty()){
							option.put("TestFee", 0.0);
					    	option.put("RepairFee", 0.0);
							option.put("MaterialFee", 0.0);
							option.put("CarFee", 0.0);
							option.put("DebugFee", 0.0);
							option.put("OtherFee", 0.0);
							option.put("TotalFee", 0.0);
					    }else{
						    for(ViewCommissionSheetFee fee:FList){							    	
						    	option.put("TestFee", fee.getTestFee()==null?0.0:(Double)fee.getTestFee());
								option.put("RepairFee", fee.getRepairFee()==null?0.0:(Double)fee.getRepairFee());
								option.put("MaterialFee", fee.getMaterialFee()==null?0.0:(Double)fee.getMaterialFee());
								option.put("CarFee", fee.getCarFee()==null?0.0:(Double)fee.getCarFee());
								option.put("DebugFee", fee.getDebugFee()==null?0.0:(Double)fee.getDebugFee());
								option.put("OtherFee", fee.getOtherFee()==null?0.0:(Double)fee.getOtherFee());
								option.put("TotalFee", fee.getTotalFee()==null?0.0:(Double)fee.getTotalFee());
								
						   }
					    }
						//查询完工器具数量和退样器具数量
						String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
						List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
						if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
							option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
							option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
						}else{
							option.put("WithdrawQuantity", 0);
							option.put("EffectQuantity", cSheet.getQuantity());
						}
						
						String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
						//String hqlQueryString_WithdrawQuantity1 = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
						
						List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId(), true);	//完工器具数量
						if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
							option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
						}else{
							option.put("FinishQuantity", 0);
						}
						//List<Long> wQuantityList1 = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity1, cSheet.getId(), true);	//退样器具数量
//						String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
//						int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheet.getId());
//						if(iSubContract > 0 || cSheet.getCommissionType() == 5){	//该委托单有转包(或该委托单为其他业务)，则完工确认时不需要判断‘完工器具数量是否大于等于有效器具数量’;
//							option.put("IsSubContract", true);
//						}else{
//							option.put("IsSubContract", false);
//						}
						options.put(option);
					}
				}
				JSONObject jsonObj = new JSONObject();
				List<Object[]> feeList = viewCSheetFeeMgr.findByHQL(queryStringAllFee + paramString, keys);
				List<Long> quantityList = feeMgr.findByHQL(queryStringCount + paramString, keys);
				List<Long> withdrawList = feeMgr.findByHQL(withDrawString + paramString, keys);
				if(feeList.isEmpty()){
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
			    	jsonObj.put("Quantity", 0.0);
			    	jsonObj.put("WithdrawQuantity", 0.0);
			    }else{
				    for(Object[] fee:feeList){
						jsonObj.put("CustomerName", "总计");
				    	jsonObj.put("TestFee", fee[0]==null?0.0:(Double)fee[0]);
				    	jsonObj.put("RepairFee", fee[1]==null?0.0:(Double)fee[1]);
				    	jsonObj.put("MaterialFee", fee[2]==null?0.0:(Double)fee[2]);
				    	jsonObj.put("CarFee", fee[3]==null?0.0:(Double)fee[3]);
				    	jsonObj.put("DebugFee", fee[4]==null?0.0:(Double)fee[4]);
				    	jsonObj.put("OtherFee", fee[5]==null?0.0:(Double)fee[5]);
				    	jsonObj.put("TotalFee", fee[6]==null?0.0:(Double)fee[6]);
				    	jsonObj.put("Quantity", quantityList.get(0)==null?0.0:(Long)quantityList.get(0));
				    	jsonObj.put("WithdrawQuantity", withdrawList.get(0)==null?0.0:(Long)withdrawList.get(0));
				   }
			    }
				foot.put(jsonObj);
				retObj4.put("total", total);
				retObj4.put("rows", options);
				retObj4.put("footer", foot);
			}catch(Exception e){
				try{
					retObj4.put("total", 0);
					retObj4.put("rows", new JSONArray());
					JSONArray foot = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
			    	jsonObj.put("Quantity", 0.0);
			    	jsonObj.put("WithdrawQuantity", 0.0);
					foot.put(jsonObj);
					retObj4.put("footer", foot);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 4", e);
				}else{
					log.error("error in StatisticServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		case 5://查询到的个人产值明细(打印)
			JSONObject res5 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String EmployeeId = req.getParameter("EmployeeId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int total = 0;
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				JSONArray options = new JSONArray();
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String tempCode="";
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String TotalqueryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList();
					List<Object[]> FList = new ArrayList();
					List<Double> JDList = new ArrayList();
					List<Double> JZList = new ArrayList();
					List<Double> JYList = new ArrayList();
					List<Double> JCList = new ArrayList();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								" and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
										" ) " ;
			
			   	    TotalqueryStr = "select count(model.id) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?"+
								" and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
								" ) " ;
			
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
					         " and model.sysUserByFeeAlloteeId.id = ?"+
					         " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
								" ) " ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检定' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
					"and (" +
							" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
							"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '校准' and o.taskAssign.status <> 1)) " +
							" ) ";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
					"and (" +
							" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
							"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检测' and o.taskAssign.status <> 1 )) " +
							" ) ";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.id = ?" + 
					"and (" +
							" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
							"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检验' and o.taskAssign.status <> 1)) " +
							" ) ";
				    
				    keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
				    keys.add(Integer.valueOf(EmployeeId));
					if(CommissionType!=null&&CommissionType.length()>0){
					
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&HeadName.length()>0){
						
						 queryStr = queryStr+" and model.commissionSheet.headNameId = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					statistic = cerFeeMgr.findPageAllByHQL(queryStr +"order by model.commissionSheet.code desc,model.id desc", page, rows, keys);
					 FList=cerFeeMgr.findByHQL(queryStr4, keys);
					 total = cerFeeMgr.getTotalCountByHQL(TotalqueryStr, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", DateTimeFormatUtil.DateFormat.format(cSheet.getCheckOutDate()));
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", "");	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
								
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							options.put(option);
						}
					}
					/***统计产值****/
					String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? " + 
									" and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1";//签字通过的原始记录(签字已通过且不是正在后台执行)
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, Integer.valueOf(EmployeeId), Start, End);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, Integer.valueOf(EmployeeId), Start, End);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
									   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1  ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, Integer.valueOf(EmployeeId), Start, End);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
				}
				
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("CustomerName", "");
				footerObj.put("CommissionDate", "");
				footerObj.put("CheckOutStaff", "");						
				footerObj.put("CheckOutTime", "");
				footerObj.put("DetailListCode","");
				footerObj.put("ApplianceName", "");	//器具名称（常用名称）
				footerObj.put("Model", "");	//型号规格
				footerObj.put("Range", "");		//测量范围
				footerObj.put("Accuracy", "");	//精度等级
				footerObj.put("Manufacturer", "");	//制造厂商
				footerObj.put("CertificateCode","");	//证书编号
				footerObj.put("WithdrawQuantity", "");	//退样器具数量
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				options.put(footerObj);
				
				res5.put("total", total);
				res5.put("rows", options);
				
				UserManager userMgr=new UserManager();
				SysUser user=userMgr.findById(Integer.valueOf(EmployeeId));
				Timestamp today = new Timestamp(System.currentTimeMillis());
				res5.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				res5.put("UserName", user.getName());
				res5.put("UserId", user.getJobNum());
				res5.put("DateFrom", StartTime);
				res5.put("DateEnd", EndTime);
				
				res5.put("IsOK", true);
				req.getSession().setAttribute("PersonOutputList", res5);
				
				resp.sendRedirect("/jlyw/StatisticLook/OutputLook/PersonPrint.jsp");
			}catch(Exception e){
				
				try{
					res5.put("total", 0);
					res5.put("rows", new JSONArray());
					res5.put("IsOK", false);
					req.getSession().setAttribute("PersonOutputList", res5);
					resp.sendRedirect("/jlyw/StatisticLook/OutputLook/PersonPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 5", e);
				}else{
					log.error("error in StatisticServlet-->case 5", e);
				}
			}finally{
				
			}
			
			break;
		case 6://查询全所产值明细
			JSONObject retObj6 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int total = 0;
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				double JDFee=0.0,JZFee=0.0,JCFee=0.0,JYFee=0.0;
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				JSONArray options = new JSONArray();
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String tempCode="";
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String TotalqueryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList<CertificateFeeAssign>();
					List<Object[]> FList = new ArrayList<Object[]>();
					List<Double> JDList = new ArrayList<Double>();
					List<Double> JZList = new ArrayList<Double>();
					List<Double> JYList = new ArrayList<Double>();
					List<Double> JCList = new ArrayList<Double>();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								" and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
										" ) " ;
			
			   	    TotalqueryStr = "select count(model.id) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								" and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
			
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
					         " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检定' )) " +
										" ) ";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '校准' )) " +
										" ) ";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检测' )) " +
										" ) ";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检验' )) " +
										" ) ";
				    
				    keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
					if(CommissionType!=null&&CommissionType.length()>0){
					
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&HeadName.length()>0&&!HeadName.equals("null")){
						
						 queryStr = queryStr+" and model.commissionSheet.headNameId = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					statistic = cerFeeMgr.findPageAllByHQL(queryStr +"order by model.commissionSheet.code desc,model.id desc", page, rows, keys);
					 FList=cerFeeMgr.findByHQL(queryStr4, keys);
					 total = cerFeeMgr.getTotalCountByHQL(TotalqueryStr, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", DateTimeFormatUtil.DateFormat.format(cSheet.getCheckOutDate()));
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", "");	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
								option.put("FeeAlloteeName", "");//产值人
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								option.put("FeeAlloteeName", cerfee.getCertificate().getSysUser()==null?"":cerfee.getCertificate().getSysUser().getName());//产值人
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							options.put(option);
						}
					}
					/***统计产值****/
					String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where " + 
									" model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1,  Start, End);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					String queryStr2 = "select sum(model.number) from Withdraw as model where model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, Start, End);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where " +
									   " o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, Start, End);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
					JDList=cerFeeMgr.findByHQL(JDqueryStr,keys);
					if(JDList!=null&&JDList.size()>0){
						Double fee =JDList.get(0);	
						JDFee=((Double)fee==null?0.0:(Double)fee);
					}
					JZList=cerFeeMgr.findByHQL(JZqueryStr,keys);
					if(JZList!=null&&JZList.size()>0){
						Double fee =JZList.get(0);	
						JZFee=((Double)fee==null?0.0:(Double)fee);
					}
					JYList=cerFeeMgr.findByHQL(JYqueryStr,keys);
					if(JYList!=null&&JYList.size()>0){
						Double fee =JYList.get(0);	
						JYFee=((Double)fee==null?0.0:(Double)fee);
					}
					JCList=cerFeeMgr.findByHQL(JCqueryStr,keys);
					if(JCList!=null&&JCList.size()>0){
						Double fee =JCList.get(0);	
						JCFee=((Double)fee==null?0.0:(Double)fee);
					}
					
				}
				JSONArray footerArray = new JSONArray();
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				
				footerArray.put(footerObj);
				retObj6.put("total", total);
				retObj6.put("rows", options);
				retObj6.put("footer", footerArray);
				retObj6.put("JDFee", JDFee);
				retObj6.put("JZFee", JZFee);
				retObj6.put("JYFee", JYFee);
				retObj6.put("JCFee", JCFee);
				retObj6.put("TotalFee", TotalFee);
			}catch(Exception e){
				
				try{
					retObj6.put("total", 0);
					retObj6.put("rows", new JSONArray());
					retObj6.put("TotalFee", 0);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 6", e);
				}else{
					log.error("error in StatisticServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj6.toString());
			}
			break;
		case 7:
		//根据部门统计详细产值明细(考虑到一个委托单下可能会有多道检验工序，可能由不同的员工完成，需要从CertificateFeeAssign中查询统计，故单独)
			JSONObject ret7 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String DepartmentId = req.getParameter("DepartmentId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int deptId = Integer.valueOf(DepartmentId);
				int total = 0;
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				double JDFee=0.0,JZFee=0.0,JCFee=0.0,JYFee=0.0;
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				JSONArray options = new JSONArray();
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String tempCode="";
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String TotalqueryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList();
					List<Object[]> FList = new ArrayList();
					List<Double> JDList = new ArrayList();
					List<Double> JZList = new ArrayList();
					List<Double> JYList = new ArrayList();
					List<Double> JCList = new ArrayList();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								" and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
										" ) " ;
			
			   	    TotalqueryStr = "select count(model.id) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)"+
								" and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
			
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
					         " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)"+
					         " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检定' )) " +
										" ) ";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '校准' )) " +
										" ) ";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检测' )) " +
										" ) ";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检验' )) " +
										" ) ";
				    
				    keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
				    keys.add(deptId);
					if(CommissionType!=null&&CommissionType.length()>0){
					
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&HeadName.length()>0){
						
						 queryStr = queryStr+" and model.commissionSheet.headNameId = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					statistic = cerFeeMgr.findPageAllByHQL(queryStr +"order by model.commissionSheet.code desc,model.commissionSheet.id desc", page, rows, keys);
					 FList=cerFeeMgr.findByHQL(queryStr4, keys);
					 total = cerFeeMgr.getTotalCountByHQL(TotalqueryStr, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("FeeAlloteeName", cerfee.getSysUserByFeeAlloteeId().getName());	
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", DateTimeFormatUtil.DateFormat.format(cSheet.getCheckOutDate()));
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", "");	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
								
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							options.put(option);
						}
					}
					/***统计产值****/
					String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where " +
									" model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
									" and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, deptId, Start, End);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					String queryStr2 = "select sum(model.number) from Withdraw as model where "+
					 				" model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)"+
					 				" and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr2 = queryStr2+ " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, deptId, Start, End);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" +
									   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, deptId, Start, End);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
					JDList=cerFeeMgr.findByHQL(JDqueryStr,keys);
					if(JDList!=null&&JDList.size()>0){
						Double fee =JDList.get(0);	
						JDFee=((Double)fee==null?0.0:(Double)fee);
					}
					JZList=cerFeeMgr.findByHQL(JZqueryStr,keys);
					if(JZList!=null&&JZList.size()>0){
						Double fee =JZList.get(0);	
						JZFee=((Double)fee==null?0.0:(Double)fee);
					}
					JYList=cerFeeMgr.findByHQL(JYqueryStr,keys);
					if(JYList!=null&&JYList.size()>0){
						Double fee =JYList.get(0);	
						JYFee=((Double)fee==null?0.0:(Double)fee);
					}
					JCList=cerFeeMgr.findByHQL(JCqueryStr,keys);
					if(JCList!=null&&JCList.size()>0){
						Double fee =JCList.get(0);	
						JCFee=((Double)fee==null?0.0:(Double)fee);
					}
					
				}
				JSONArray footerArray = new JSONArray();
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("FeeAlloteeName", "");		
				footerObj.put("Code","");						
				footerObj.put("CustomerName", "");
				footerObj.put("CommissionDate", "");	
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				
				footerArray.put(footerObj);
				ret7.put("total", total);
				ret7.put("rows", options);
				ret7.put("footer", footerArray);
				ret7.put("JDFee", JDFee);
				ret7.put("JZFee", JZFee);
				ret7.put("JYFee", JYFee);
				ret7.put("JCFee", JCFee);
				ret7.put("TotalFee", TotalFee);
			}catch(Exception e){
				
				try{
					ret7.put("total", 0);
					ret7.put("rows", new JSONArray());
					ret7.put("TotalFee", 0);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 7", e);
				}else{
					log.error("error in StatisticServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(ret7.toString());
			}
			
			break;
		case 8:
			//根据部门统计详细产值(打印)(考虑到一个委托单下可能会有多道检验工序，可能由不同的员工完成，需要从CertificateFeeAssign中查询统计，故单独)
			JSONObject ret8 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String DepartmentId = req.getParameter("DepartmentId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int deptId = Integer.valueOf(DepartmentId);
				//System.out.println(DepartmentId+Integer.parseInt(DepartmentId));
				int total = 0;
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				double JDFee=0.0,JZFee=0.0,JCFee=0.0,JYFee=0.0;
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				JSONArray options = new JSONArray();
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String tempCode="";
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String TotalqueryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList();
					List<Object[]> FList = new ArrayList();
					List<Double> JDList = new ArrayList();
					List<Double> JZList = new ArrayList();
					List<Double> JYList = new ArrayList();
					List<Double> JCList = new ArrayList();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								" and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
										" ) " ;
			
			   	    TotalqueryStr = "select count(model.id) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)"+
								" and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
			
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
					         " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)"+
					         " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检定' )) " +
										" ) ";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '校准' )) " +
										" ) ";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检测' )) " +
										" ) ";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检验' )) " +
										" ) ";
				    
				    keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
				    keys.add(deptId);
					if(CommissionType!=null&&CommissionType.length()>0){
					
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&HeadName.length()>0){
						
						 queryStr = queryStr+" and model.commissionSheet.headNameId = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					 statistic = cerFeeMgr.findByHQL(queryStr +"order by model.commissionSheet.code desc,model.commissionSheet.id desc", keys);
					 FList=cerFeeMgr.findByHQL(queryStr4, keys);
					 total = cerFeeMgr.getTotalCountByHQL(TotalqueryStr, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("FeeAlloteeName", cerfee.getSysUserByFeeAlloteeId().getName());	
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", DateTimeFormatUtil.DateFormat.format(cSheet.getCheckOutDate()));
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", "");	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
								
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							options.put(option);
						}
					}
					/***统计产值****/
					String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where " +
									" model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" + 
									" and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, deptId, Start, End);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					String queryStr2 = "select sum(model.number) from Withdraw as model where "+
					 				" model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)"+
					 				" and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, deptId, Start, End);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" +
									   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, deptId, Start, End);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
					JDList=cerFeeMgr.findByHQL(JDqueryStr,keys);
					if(JDList!=null&&JDList.size()>0){
						Double fee =JDList.get(0);	
						JDFee=((Double)fee==null?0.0:(Double)fee);
					}
					JZList=cerFeeMgr.findByHQL(JZqueryStr,keys);
					if(JZList!=null&&JZList.size()>0){
						Double fee =JZList.get(0);	
						JZFee=((Double)fee==null?0.0:(Double)fee);
					}
					JYList=cerFeeMgr.findByHQL(JYqueryStr,keys);
					if(JYList!=null&&JYList.size()>0){
						Double fee =JYList.get(0);	
						JYFee=((Double)fee==null?0.0:(Double)fee);
					}
					JCList=cerFeeMgr.findByHQL(JCqueryStr,keys);
					if(JCList!=null&&JCList.size()>0){
						Double fee =JCList.get(0);	
						JCFee=((Double)fee==null?0.0:(Double)fee);
					}
					
				}
				
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("CustomerName", "");
				footerObj.put("CommissionDate", "");
				footerObj.put("CheckOutStaff", "");						
				footerObj.put("CheckOutTime", "");
				footerObj.put("DetailListCode","");
				footerObj.put("ApplianceName", "");	//器具名称（常用名称）
				footerObj.put("Model", "");	//型号规格
				footerObj.put("Range", "");		//测量范围
				footerObj.put("Accuracy", "");	//精度等级
				footerObj.put("Manufacturer", "");	//制造厂商
				footerObj.put("CertificateCode","");	//证书编号
				footerObj.put("WithdrawQuantity", "");	//退样器具数量
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				footerObj.put("FeeAlloteeName", "");
				footerObj.put("CheckOutTime", "校准费");
				footerObj.put("DetailListCode", JZFee);
				
				options.put(footerObj);
				ret8.put("total", total);
				ret8.put("rows", options);
			
				ret8.put("JDFee", JDFee);
				ret8.put("JZFee", JZFee);
				ret8.put("JYFee", JYFee);
				ret8.put("JCFee", JCFee);
				ret8.put("TotalFee", TotalFee);
				
				DepartmentManager deptMgr=new DepartmentManager();
				Department dept=deptMgr.findById(deptId);
				Timestamp today = new Timestamp(System.currentTimeMillis());
				ret8.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				ret8.put("DeptName", dept.getName());
				ret8.put("DeptCode", dept.getDeptCode());
				ret8.put("DateFrom", StartTime);
				ret8.put("DateEnd", EndTime);
				
				ret8.put("IsOK", true);
				req.getSession().setAttribute("DeptMXList", ret8);
				
				resp.sendRedirect("/jlyw/StatisticLook/OutputLook/DeptMXPrint.jsp");
			}catch(Exception e){
				
				try{
					ret8.put("total", 0);
					ret8.put("rows", new JSONArray());
					ret8.put("IsOK", false);
					req.getSession().setAttribute("DeptMXList", ret8);
					resp.sendRedirect("/jlyw/StatisticLook/OutputLook/DeptMXPrint.jsp");
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
		//根据日期查询产值（全所）（打印）
			JSONObject retObj9 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				//String CustomerId = req.getParameter("CustomerId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					
					String queryStr = "select dept.DeptCode,dept.Name as deptName,model.Name as UserName,model.JobNum,temp.*,model.id from " + SystemCfgUtil.DBPrexName + "SysUser as model left join (select cer.FeeAlloteeId as feeAlloteeId, SUM(cer.totalFee) as totalfee, SUM(cer.testFee) as testfee, SUM(cer.repairFee) as repairfee, SUM(cer.materialFee) as materialfee, SUM(cer.carFee) as carfee, SUM(cer.debugFee) as debugfee, SUM(cer.otherFee) as otherfee from" + SystemCfgUtil.DBPrexName + "CertificateFeeAssign as cer, " + SystemCfgUtil.DBPrexName + "CommissionSheet as c " +
									  " where cer.CommissionSheetId = c.Id" +
									  " and (c.status = ? or c.status = ?) and (c.checkOutDate >= ? and c.checkOutDate <= ?)";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr = queryStr + " and c.commissionType = " + CommissionType;
					}
					if(HeadName!=null&&HeadName.length()>0){
						 queryStr = queryStr+" and c.headNameId = " + HeadName;
					}
					queryStr = queryStr + " and ( " +
									  "   (cer.originalRecordid is null and cer.certificateid is null) or " +
									  "   (cer.originalRecordid in (select o.id from " + SystemCfgUtil.DBPrexName + "OriginalRecord as o," + SystemCfgUtil.DBPrexName + "TaskAssign as t where o.TaskAssignId = t.Id and o.status <>1 and t.status<>1 and o.certificateid = cer.certificateid)) " +
									  " ) " +
									  " group by cer.FeeAlloteeId) as temp on model.id = temp.feeAlloteeId," + SystemCfgUtil.DBPrexName + "ProjectTeam as p," + SystemCfgUtil.DBPrexName + "Department as dept " +
									  " where model.ProjectTeamId = p.Id and p.DeptId = dept.Id" + 
									  "	order by dept.DeptCode asc, model.JobNum asc";
					
					List<Object[]> statistic;
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					
					statistic = cSheetMgr.findBySQL(queryStr, new Integer(4), new Integer(9), Start, End);
					//total = cSheetMgr.getTotalCountByHQL(queryTotalStr, new Integer(4), new Integer(9), Start, End);
					
					int quantity = 0;
					int withdraw = 0;
					int certificate = 0;
					double totalFee = 0;
					double testFee = 0;
					double repairFee = 0;
					double materialFee = 0;
					double carFee = 0;
					double debugFee = 0;
					double otherFee = 0;
					
					if(statistic!=null&&statistic.size()>0)
					{
						String tempCode = null;
						String tempName = null;
						int quantityByDept = 0;
						int withdrawByDept = 0;
						int certificateByDept = 0;
						double totalFeeByDept = 0;
						double testFeeByDept = 0;
						double repairFeeByDept = 0;
						double materialFeeByDept = 0;
						double carFeeByDept = 0;
						double debugFeeByDept = 0;
						double otherFeeByDept = 0;
						int len = 0;
						
						for(Object[] temp : statistic)
						{
							if(tempCode==null){
								options.put(0);
							}
							if(tempCode!=null&&!tempCode.equals(temp[0].toString())){
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("DeptCode", tempCode);
								jsonObj.put("DeptName", tempName);
								jsonObj.put("UserName", "");
								jsonObj.put("UserJobNum", "");
								jsonObj.put("TotalFee", totalFeeByDept);
								jsonObj.put("TestFee", testFeeByDept);
								jsonObj.put("RepairFee", repairFeeByDept);
								jsonObj.put("MaterialFee", materialFeeByDept);
								jsonObj.put("CarFee", carFeeByDept);
								jsonObj.put("DebugFee", debugFeeByDept);
								jsonObj.put("OtherFee", otherFeeByDept);
								jsonObj.put("Quantity", quantityByDept);
								jsonObj.put("Withdraw", withdrawByDept);
								jsonObj.put("Certificate", certificateByDept);
								
								options.put(options.length()-len-1,jsonObj);
								
								totalFeeByDept = repairFeeByDept = testFeeByDept = materialFeeByDept = carFeeByDept = debugFeeByDept = otherFeeByDept = 0;
								quantityByDept = withdrawByDept = certificateByDept = len = 0;
								options.put(0);
							}
							JSONObject option = new JSONObject();
							option.put("DeptCode", "");
							option.put("DeptName", "");
							option.put("UserName", temp[2].toString());
							option.put("UserJobNum", temp[3].toString());
							option.put("TotalFee", temp[5]==null?0:(Double)temp[5]);
							option.put("TestFee", temp[6]==null?0:(Double)temp[6]);
							option.put("RepairFee", temp[7]==null?0:(Double)temp[7]);
							option.put("MaterialFee", temp[8]==null?0:(Double)temp[8]);
							option.put("CarFee", temp[9]==null?0:(Double)temp[9]);
							option.put("DebugFee", temp[10]==null?0:(Double)temp[10]);
							option.put("OtherFee", temp[11]==null?0:(Double)temp[11]);
							option.put("Quantity", "");
							option.put("Withdraw", "");
							option.put("Certificate", "");
							
							String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? and  model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, (Integer)temp[12], Start, End);
							option.put("Quantity", quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
							
							String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, (Integer)temp[12], Start, End);
							option.put("Withdraw", withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
							
							String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
											   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ? and model.status<>1  ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr3 = queryStr3 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, (Integer)temp[12], Start, End);
							option.put("Certificate", certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
														
							totalFeeByDept += (Double)option.get("TotalFee");
							testFeeByDept += (Double)option.get("TestFee");
							repairFeeByDept += (Double)option.get("RepairFee");
							materialFeeByDept += (Double)option.get("MaterialFee");
							carFeeByDept += (Double)option.get("CarFee");
							debugFeeByDept += (Double)option.get("DebugFee");
							otherFeeByDept += (Double)option.get("OtherFee");
							quantityByDept += option.getInt("Quantity");
							withdrawByDept += option.getInt("Withdraw");
							certificateByDept += option.getInt("Certificate");
							
							totalFee += (Double)option.get("TotalFee");
							testFee += (Double)option.get("TestFee");
							repairFee += (Double)option.get("RepairFee");
							materialFee += (Double)option.get("MaterialFee");
							carFee += (Double)option.get("CarFee");
							debugFee += (Double)option.get("DebugFee");
							otherFee += (Double)option.get("OtherFee");
							quantity += option.getInt("Quantity");
							withdraw += option.getInt("Withdraw");
							certificate += option.getInt("Certificate");
							
							len++;
							
							options.put(option);
							
							tempCode = temp[0].toString();
							tempName = temp[1].toString();
						}
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("DeptCode", tempCode);
						jsonObj.put("DeptName", tempName);
						jsonObj.put("UserName", "");
						jsonObj.put("UserJobNum", "");
						jsonObj.put("TotalFee", totalFeeByDept);
						jsonObj.put("TestFee", testFeeByDept);
						jsonObj.put("RepairFee", repairFeeByDept);
						jsonObj.put("MaterialFee", materialFeeByDept);
						jsonObj.put("CarFee", carFeeByDept);
						jsonObj.put("DebugFee", debugFeeByDept);
						jsonObj.put("OtherFee", otherFeeByDept);
						jsonObj.put("Quantity", quantityByDept);
						jsonObj.put("Withdraw", withdrawByDept);
						jsonObj.put("Certificate", certificateByDept);
						
						options.put(options.length()-len-1,jsonObj);
					}
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("DeptCode", "总产值");
					jsonObj.put("DeptName", "");
					jsonObj.put("UserName", "");
					jsonObj.put("UserJobNum", "");
					jsonObj.put("TotalFee", totalFee);
					jsonObj.put("TestFee", testFee);
					jsonObj.put("RepairFee", repairFee);
					jsonObj.put("MaterialFee", materialFee);
					jsonObj.put("CarFee", carFee);
					jsonObj.put("DebugFee", debugFee);
					jsonObj.put("OtherFee", otherFee);
					jsonObj.put("Quantity", quantity);
					jsonObj.put("Withdraw", withdraw);
					jsonObj.put("Certificate", "");
					
					options.put(jsonObj);
				}
				retObj9.put("total", total);
				retObj9.put("rows", options);
				
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				retObj9.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				retObj9.put("DateFrom", StartTime);
				retObj9.put("DateEnd", EndTime);				
				retObj9.put("IsOK", true);
				req.getSession().setAttribute("CompanyOutputList", retObj9);
				
				resp.sendRedirect("/jlyw/StatisticLook/OutputLook/CompanyPrint.jsp");
			}catch(Exception e){
				
				try{
					retObj9.put("total", 0);
					retObj9.put("rows", new JSONArray());
					retObj9.put("IsOK", false);
					req.getSession().setAttribute("CompanyOutputList", retObj9);
					resp.sendRedirect("/jlyw/StatisticLook/OutputLook/CompanyPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 9", e);
				}else{
					log.error("error in StatisticServlet-->case 9", e);
				}
			}finally{
				
			}
			break;
		
		case 10://全所产值明细（打印）
			JSONObject retObj10 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int total = 0;
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				double JDFee=0.0,JZFee=0.0,JCFee=0.0,JYFee=0.0;
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				JSONArray options = new JSONArray();
				List<Object> keys = new ArrayList<Object>();
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String tempCode="";
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String TotalqueryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList<CertificateFeeAssign>();
					List<Object[]> FList = new ArrayList<Object[]>();
					List<Double> JDList = new ArrayList<Double>();
					List<Double> JZList = new ArrayList<Double>();
					List<Double> JYList = new ArrayList<Double>();
					List<Double> JCList = new ArrayList<Double>();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								" and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
										" ) " ;
			
			   	    TotalqueryStr = "select count(model.id) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								" and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
								" ) " ;
			
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
					         " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.taskAssign.status <> 1)) " +
								" ) " ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检定' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '校准' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检测' and o.taskAssign.status <> 1)) " +
										" ) ";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" + 
								"and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.certificate = model.certificate and o.workType = '检验' and o.taskAssign.status <> 1)) " +
										" ) ";
				    
				    keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
					if(CommissionType!=null&&CommissionType.length()>0){
					
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&HeadName.length()>0){
						
						 queryStr = queryStr+" and model.commissionSheet.headNameId = ?";
						 
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 TotalqueryStr = TotalqueryStr + " and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					statistic = cerFeeMgr.findByHQL(queryStr +"order by model.commissionSheet.code desc, model.commissionSheet.id desc", keys);
					 FList=cerFeeMgr.findByHQL(queryStr4, keys);
					 total = cerFeeMgr.getTotalCountByHQL(TotalqueryStr, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", DateTimeFormatUtil.DateFormat.format(cSheet.getCheckOutDate()));
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", "");	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
								option.put("FeeAlloteeName", "");//产值人
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								option.put("FeeAlloteeName", cerfee.getSysUserByFeeAlloteeId()==null?"":cerfee.getSysUserByFeeAlloteeId().getName());//产值人
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							options.put(option);
						}
					}
					/***统计产值****/
					String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where  " + 
									" model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1,  Start, End);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					String queryStr2 = "select sum(model.number) from Withdraw as model where  model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, Start, End);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where " +
									   " o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, Start, End);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
					JDList=cerFeeMgr.findByHQL(JDqueryStr,keys);
					if(JDList!=null&&JDList.size()>0){
						Double fee =JDList.get(0);	
						JDFee=((Double)fee==null?0.0:(Double)fee);
					}
					JZList=cerFeeMgr.findByHQL(JZqueryStr,keys);
					if(JZList!=null&&JZList.size()>0){
						Double fee =JZList.get(0);	
						JZFee=((Double)fee==null?0.0:(Double)fee);
					}
					JYList=cerFeeMgr.findByHQL(JYqueryStr,keys);
					if(JYList!=null&&JYList.size()>0){
						Double fee =JYList.get(0);	
						JYFee=((Double)fee==null?0.0:(Double)fee);
					}
					JCList=cerFeeMgr.findByHQL(JCqueryStr,keys);
					if(JCList!=null&&JCList.size()>0){
						Double fee =JCList.get(0);	
						JCFee=((Double)fee==null?0.0:(Double)fee);
					}
					
				}
				
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("CustomerName", "");
				footerObj.put("CommissionDate", "");
				footerObj.put("CheckOutStaff", "");						
				footerObj.put("DetailListCode","");
				footerObj.put("ApplianceName", "");	//器具名称（常用名称）
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("Model", "");	//型号规格
				footerObj.put("Range", "");		//测量范围
				footerObj.put("Accuracy", "");	//精度等级
				footerObj.put("Manufacturer", "");	//制造厂商
				
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				footerObj.put("CheckOutTime", "校准费");
				footerObj.put("DetailListCode", JZFee);
				footerObj.put("FeeAlloteeName", "");
				
				options.put(footerObj);
				retObj10.put("total", total);
				retObj10.put("rows", options);
			
				retObj10.put("JDFee", JDFee);
				retObj10.put("JZFee", JZFee);
				retObj10.put("JYFee", JYFee);
				retObj10.put("JCFee", JCFee);
				retObj10.put("TotalFee", TotalFee);
				
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				retObj10.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				retObj10.put("DateFrom", StartTime);
				retObj10.put("DateEnd", EndTime);				
				retObj10.put("IsOK", true);
				req.getSession().setAttribute("CompanyMXList", retObj10);
				
				resp.sendRedirect("/jlyw/StatisticLook/OutputLook/CompanyMXPrint.jsp");
			}catch(Exception e){
				
				try{
					retObj10.put("total", 0);
					retObj10.put("rows", new JSONArray());
					retObj10.put("IsOK", false);
					req.getSession().setAttribute("CompanyMXList", retObj10);
					resp.sendRedirect("/jlyw/StatisticLook/OutputLook/CompanyMXPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 10", e);
				}else{
					log.error("error in StatisticServlet-->case 10", e);
				}
			}finally{
				
			}
			break;
		case 11://导出全所、部门产值信息
			JSONObject retObj11 = new JSONObject();
			try{
				String paramsStr = req.getParameter("paramsStr");
				JSONObject params = new JSONObject(paramsStr);
				List<JSONObject> retList = new ArrayList<JSONObject>();
				if(params.length()>0){
					String StartTime = params.getString("StartTime");
					String EndTime =  params.getString("EndTime");
					String CommissionType =  params.getString("CommissionType");
					String DepartmentId = params.has("DepartmentId")?params.getString("DepartmentId"):"";
					String HeadName = params.getString("HeadName");

					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					
					List<Object> keys = new ArrayList<Object>();
					keys.add(Start);
					keys.add(End);
					String queryStr = "select dept.DeptCode,dept.Name as deptName,model.Name as UserName,model.JobNum,temp.*,model.id as userId,dept.id as deptId from " + SystemCfgUtil.DBPrexName + "SysUser as model left join (select cer.FeeAlloteeId as feeAlloteeId, SUM(cer.totalFee) as totalfee, SUM(cer.testFee) as testfee, SUM(cer.repairFee) as repairfee, SUM(cer.materialFee) as materialfee, SUM(cer.carFee) as carfee, SUM(cer.debugFee) as debugfee, SUM(cer.otherFee) as otherfee from" + SystemCfgUtil.DBPrexName + "CertificateFeeAssign as cer, " + SystemCfgUtil.DBPrexName + "CommissionSheet as c " +
									  " where cer.CommissionSheetId = c.Id" +
									  " and (c.checkOutDate >= ? and c.checkOutDate <= ?)";
					String DeptqueryStr1 = "select sum(model.quantity) from OriginalRecord as model where " +
									" model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
					
					String DeptqueryStr2 = "select sum(model.number) from Withdraw as model where "+
					 				" model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
					
					String DeptqueryStr3 = "select count(o.certificate.id) from OriginalRecord as o where" +
									   " o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
									   " and o.status<>1 ";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr = queryStr + " and c.commissionType = " + CommissionType;
						DeptqueryStr1 = DeptqueryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
						DeptqueryStr2 = DeptqueryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
						DeptqueryStr3 = DeptqueryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
					}
					if(HeadName!=null&&!HeadName.equals("")){
						queryStr = queryStr + " and c.headNameId = " + HeadName;
						DeptqueryStr1 = DeptqueryStr1 + " and model.commissionSheet.headNameId = " + HeadName;
						DeptqueryStr2 = DeptqueryStr2 + " and model.commissionSheet.headNameId = " + HeadName;
						DeptqueryStr3 = DeptqueryStr3 + " and o.commissionSheet.headNameId = " + HeadName;
					}
					queryStr = queryStr + " and ( " +
									  "   (cer.originalRecordid is null and cer.certificateid is null) or " +
									  "   (cer.originalRecordid in (select o.id from " + SystemCfgUtil.DBPrexName + "OriginalRecord as o," + SystemCfgUtil.DBPrexName + "TaskAssign as t where o.TaskAssignId = t.Id and o.status <>1 and t.status<>1 and o.certificateid = cer.certificateid)) " +
									  " ) " +
									  " group by cer.FeeAlloteeId) as temp on model.id = temp.feeAlloteeId," + SystemCfgUtil.DBPrexName + "ProjectTeam as p," + SystemCfgUtil.DBPrexName + "Department as dept " +
									  " where model.ProjectTeamId = p.Id and p.DeptId = dept.Id";
					if(DepartmentId!=null&&!DepartmentId.equals("")){
						queryStr = queryStr + " and dept.Id = ?";
						keys.add(Integer.valueOf(DepartmentId));
					}
					queryStr = queryStr + "	order by dept.DeptCode asc, model.JobNum asc";
					
					List<Object[]> statistic;
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					
					statistic = cSheetMgr.findBySQL(queryStr, keys);
					//total = cSheetMgr.getTotalCountByHQL(queryTotalStr, new Integer(4), new Integer(9), Start, End);
					
					int quantity = 0;
					int withdraw = 0;
					int certificate = 0;
					double totalFee = 0;
					double testFee = 0;
					double repairFee = 0;
					double materialFee = 0;
					double carFee = 0;
					double debugFee = 0;
					double otherFee = 0;
					
					if(statistic!=null&&statistic.size()>0)
					{
						String tempCode = null;
						String tempName = null;
						int tempDeptId = 0;
						int quantityByDept = 0;
						int withdrawByDept = 0;
						int certificateByDept = 0;
						double totalFeeByDept = 0;
						double testFeeByDept = 0;
						double repairFeeByDept = 0;
						double materialFeeByDept = 0;
						double carFeeByDept = 0;
						double debugFeeByDept = 0;
						double otherFeeByDept = 0;
						int len = 0;
						
						for(Object[] temp : statistic)
						{
							if(tempCode!=null&&!tempCode.equals(temp[0].toString())){
								
								List<Long> quantitysByDept = cSheetMgr.findByHQL(DeptqueryStr1 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)" , Start, End, tempDeptId);
								quantityByDept = (quantitysByDept==null||quantitysByDept.size()==0?0:quantitysByDept.get(0)==null?0:quantitysByDept.get(0).intValue());
								List<Long> withdrawsByDept = cSheetMgr.findByHQL(DeptqueryStr2 + " and model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, tempDeptId);
								withdrawByDept = (withdrawsByDept==null||withdrawsByDept.size()==0?0:withdrawsByDept.get(0)==null?0:withdrawsByDept.get(0).intValue());
								List<Long> certificatesByDept = (new OriginalRecordManager()).findByHQL(DeptqueryStr3 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, tempDeptId);
								certificateByDept =(certificatesByDept==null||certificatesByDept.size()==0?0:certificatesByDept.get(0)==null?0:certificatesByDept.get(0).intValue());
								
								JSONObject jsonObj = new JSONObject();
								jsonObj.put("DeptId", tempDeptId);
								jsonObj.put("DeptCode", tempCode);
								jsonObj.put("DeptName", tempName);
								jsonObj.put("UserName", "");
								jsonObj.put("UserJobNum", "");
								jsonObj.put("TotalFee", totalFeeByDept);
								jsonObj.put("TestFee", testFeeByDept);
								jsonObj.put("RepairFee", repairFeeByDept);
								jsonObj.put("MaterialFee", materialFeeByDept);
								jsonObj.put("CarFee", carFeeByDept);
								jsonObj.put("DebugFee", debugFeeByDept);
								jsonObj.put("OtherFee", otherFeeByDept);
								jsonObj.put("Quantity", quantityByDept);
								jsonObj.put("Withdraw", withdrawByDept);
								jsonObj.put("Certificate", certificateByDept);
								
								retList.add(retList.size()-len,jsonObj);
								
								totalFeeByDept = repairFeeByDept = testFeeByDept = materialFeeByDept = carFeeByDept = debugFeeByDept = otherFeeByDept = 0;
								len = 0;
							}
							JSONObject option = new JSONObject();
							option.put("DeptCode", "");
							option.put("DeptName", "");
							option.put("UserName", temp[2].toString());
							option.put("UserJobNum", temp[3].toString());
							option.put("TotalFee", temp[5]==null?0:(Double)temp[5]);
							option.put("TestFee", temp[6]==null?0:(Double)temp[6]);
							option.put("RepairFee", temp[7]==null?0:(Double)temp[7]);
							option.put("MaterialFee", temp[8]==null?0:(Double)temp[8]);
							option.put("CarFee", temp[9]==null?0:(Double)temp[9]);
							option.put("DebugFee", temp[10]==null?0:(Double)temp[10]);
							option.put("OtherFee", temp[11]==null?0:(Double)temp[11]);
							option.put("UserId", temp[12]==null?0:(Integer)temp[12]);
							
							String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? and  model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, (Integer)temp[12], Start, End);
							option.put("Quantity", quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
							
							String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, (Integer)temp[12], Start, End);
							option.put("Withdraw", withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
							
							String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
											   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ? and model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr3 = queryStr3 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, (Integer)temp[12], Start, End);
							option.put("Certificate", certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
														
							totalFeeByDept += (Double)option.get("TotalFee");
							testFeeByDept += (Double)option.get("TestFee");
							repairFeeByDept += (Double)option.get("RepairFee");
							materialFeeByDept += (Double)option.get("MaterialFee");
							carFeeByDept += (Double)option.get("CarFee");
							debugFeeByDept += (Double)option.get("DebugFee");
							otherFeeByDept += (Double)option.get("OtherFee");
							
							totalFee += (Double)option.get("TotalFee");
							testFee += (Double)option.get("TestFee");
							repairFee += (Double)option.get("RepairFee");
							materialFee += (Double)option.get("MaterialFee");
							carFee += (Double)option.get("CarFee");
							debugFee += (Double)option.get("DebugFee");
							otherFee += (Double)option.get("OtherFee");
							
							len++;
							
							retList.add(option);
							
							tempCode = temp[0].toString();
							tempName = temp[1].toString();
							tempDeptId = (Integer)temp[13];
						}
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("DeptCode", tempCode);
						jsonObj.put("DeptId", tempDeptId);
						jsonObj.put("DeptName", tempName);
						jsonObj.put("UserJobNum", "");
						jsonObj.put("UserName", "");
						jsonObj.put("TotalFee", totalFeeByDept);
						jsonObj.put("TestFee", testFeeByDept);
						jsonObj.put("RepairFee", repairFeeByDept);
						jsonObj.put("MaterialFee", materialFeeByDept);
						jsonObj.put("CarFee", carFeeByDept);
						jsonObj.put("DebugFee", debugFeeByDept);
						jsonObj.put("OtherFee", otherFeeByDept);
						jsonObj.put("Quantity", quantityByDept);
						jsonObj.put("Withdraw", withdrawByDept);
						jsonObj.put("Certificate", certificateByDept);
						
						retList.add(retList.size()-len,jsonObj);
					}
					if(DepartmentId!=null&&!DepartmentId.equals("")){
						List<Long> quantitysByDept = cSheetMgr.findByHQL(DeptqueryStr1 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, Integer.valueOf(DepartmentId));
						quantity = (quantitysByDept==null||quantitysByDept.size()==0?0:quantitysByDept.get(0)==null?0:quantitysByDept.get(0).intValue());
						List<Long> withdrawsByDept = cSheetMgr.findByHQL(DeptqueryStr2 + " and model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, Integer.valueOf(DepartmentId));
						withdraw = (withdrawsByDept==null||withdrawsByDept.size()==0?0:withdrawsByDept.get(0)==null?0:withdrawsByDept.get(0).intValue());
						List<Long> certificatesByDept = (new OriginalRecordManager()).findByHQL(DeptqueryStr3 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)", Start, End, Integer.valueOf(DepartmentId));
						certificate =(certificatesByDept==null||certificatesByDept.size()==0?0:certificatesByDept.get(0)==null?0:certificatesByDept.get(0).intValue());
					}else{
						List<Long> quantitysByDept = cSheetMgr.findByHQL(DeptqueryStr1, Start, End);
						quantity = (quantitysByDept==null||quantitysByDept.size()==0?0:quantitysByDept.get(0)==null?0:quantitysByDept.get(0).intValue());
						List<Long> withdrawsByDept = cSheetMgr.findByHQL(DeptqueryStr2, Start, End);
						withdraw = (withdrawsByDept==null||withdrawsByDept.size()==0?0:withdrawsByDept.get(0)==null?0:withdrawsByDept.get(0).intValue());
						List<Long> certificatesByDept = (new OriginalRecordManager()).findByHQL(DeptqueryStr3, Start, End);
						certificate =(certificatesByDept==null||certificatesByDept.size()==0?0:certificatesByDept.get(0)==null?0:certificatesByDept.get(0).intValue());
					}
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("DeptCode", "总产值");
					jsonObj.put("DeptId", "");
					jsonObj.put("DeptName", "");
					jsonObj.put("UserJobNum", "");
					jsonObj.put("UserName", "");
					jsonObj.put("TotalFee", totalFee);
					jsonObj.put("TestFee", testFee);
					jsonObj.put("RepairFee", repairFee);
					jsonObj.put("MaterialFee", materialFee);
					jsonObj.put("CarFee", carFee);
					jsonObj.put("DebugFee", debugFee);
					jsonObj.put("OtherFee", otherFee);
					jsonObj.put("Quantity", quantity);
					jsonObj.put("Withdraw", withdraw);
					jsonObj.put("Certificate", certificate);
					retList.add(jsonObj);
					
				}
				String filePath = ExportUtil.ExportToExcelByResultSet(retList, null, "OutPut_formatExcel", "OutPut_formatTitle", ExportManager.class);
				retObj11.put("IsOK", filePath.equals("")?false:true);
				retObj11.put("Path", filePath);
			}catch(Exception e){
				try{
					retObj11.put("IsOK", false);
					retObj11.put("Path", "");
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 11", e);
				}else{
					log.error("error in StatisticServlet-->case 11", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj11.toString());
			}
			break;
		case 12://导出产值明细（全所、个人、部门）
			JSONObject retObj12 = new JSONObject();
			try{
				String paramsStr = req.getParameter("paramsStr");
				JSONObject params = new JSONObject(paramsStr);
				
				double TotalFee = 0;
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0;//各种费用总计
				double JDFee=0.0,JZFee=0.0,JCFee=0.0,JYFee=0.0;
				int QuantityNum=0,WithdrawQuantity=0,CertificateNum=0;
				List<JSONObject> result = new ArrayList<JSONObject>();
				List<Object> keys = new ArrayList<Object>();
				List<Object> keys1 = new ArrayList<Object>();
				List<Object> keys2 = new ArrayList<Object>();
				String tempCode="";
				if(params.length()>0)
				{
					String StartTime = params.has("StartTime")?params.getString("StartTime"):"";
					String EndTime = params.has("EndTime")?params.getString("EndTime"):"";
					String EmployeeId = params.has("EmployeeId")?params.getString("EmployeeId"):"";
					String DepartmentId = params.has("DepartmentId")?params.getString("DepartmentId"):"";
					String CommissionType = params.has("CommissionType")?params.getString("CommissionType"):"";
					String HeadName = params.has("HeadName")?params.getString("HeadName"):"";
					
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					String queryStr="";
					String queryStr4="";
					String JDqueryStr="",JZqueryStr="",JYqueryStr="",JCqueryStr="";
					List<CertificateFeeAssign> statistic = new ArrayList();
					List<Object[]> FList = new ArrayList();
					List<Double> JDList = new ArrayList();
					List<Double> JZList = new ArrayList();
					List<Double> JYList = new ArrayList();
					List<Double> JCList = new ArrayList();
					CommissionSheet cSheet=new CommissionSheet();
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					queryStr = "select model from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)";
				    queryStr4 = "select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) from CertificateFeeAssign as model " +
				 			 " where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
					         "and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)" ;
				    JDqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)";
				    JZqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)";
				    JCqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)";
				    JYqueryStr = "select SUM(model.testFee) from CertificateFeeAssign as model where (model.commissionSheet.status = ? or model.commissionSheet.status = ?) " +
								"and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?)";
				    String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?" +
									" and model.status<>1 ";//签字通过的原始记录(签字已通过且不是正在后台执行)
				    String queryStr2 = "select sum(model.number) from Withdraw as model where model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
				    String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ?"+
								   " and o.status<>1 ";
					keys.add(new Integer(4));
				    keys.add(new Integer(9));
				    keys.add(Start);
				    keys.add(End);
				    keys1.add(Start);
				    keys1.add(End);
				    keys2.add(Start);
				    keys2.add(End);
					if(EmployeeId!=null&&!EmployeeId.equals("")){
						queryStr = queryStr + " and model.sysUserByFeeAlloteeId.id = ?";
						queryStr4 = queryStr4 + " and model.sysUserByFeeAlloteeId.id = ?";
						JDqueryStr = JDqueryStr + " and model.sysUserByFeeAlloteeId.id = ?";
						JZqueryStr = JZqueryStr + " and model.sysUserByFeeAlloteeId.id = ?";
						JCqueryStr = JCqueryStr + " and model.sysUserByFeeAlloteeId.id = ?";
						JYqueryStr = JYqueryStr + " and model.sysUserByFeeAlloteeId.id = ?";
						queryStr1 = queryStr1 + " and model.sysUserByStaffId.id = ?";
						queryStr2 = queryStr2 + " and model.sysUserByRequesterId.id = ?";
						queryStr3 = queryStr3 + " and o.sysUserByStaffId.id = ?";
						keys.add(Integer.valueOf(EmployeeId));
						keys1.add(Integer.valueOf(EmployeeId));
						keys2.add(Integer.valueOf(EmployeeId));
					}
					if(DepartmentId!=null&&!DepartmentId.equals("")){
						queryStr = queryStr + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						queryStr4 = queryStr4 + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						JDqueryStr = JDqueryStr + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						JZqueryStr = JZqueryStr + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						JCqueryStr = JCqueryStr + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						JYqueryStr = JYqueryStr + " and model.sysUserByFeeAlloteeId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						queryStr1 = queryStr1 + " and model.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						queryStr2 = queryStr2 + " and model.sysUserByRequesterId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						queryStr3 = queryStr3 + " and o.sysUserByStaffId.projectTeamId in (select p.id from ProjectTeam as p where p.department.id = ?)";
						keys.add(Integer.valueOf(DepartmentId));
						keys1.add(Integer.valueOf(DepartmentId));
						keys2.add(Integer.valueOf(DepartmentId));
					}
					if(CommissionType!=null&&CommissionType.length()>0){
						
						 queryStr = queryStr+" and model.commissionSheet.commissionType = ?";
						 queryStr4 = queryStr4+" and model.commissionSheet.commissionType = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.commissionType = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.commissionType = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.commissionType = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.commissionType = ?";
						 keys.add(Integer.valueOf(CommissionType));
					}
					if(HeadName!=null&&HeadName.length()>0){
						
						 queryStr = queryStr+" and model.commissionSheet.headNameId = ?";
						 queryStr4 = queryStr4+" and model.commissionSheet.headNameId = ?";
						 JDqueryStr = JDqueryStr + " and model.commissionSheet.headNameId = ?";
						 JZqueryStr = JZqueryStr + " and model.commissionSheet.headNameId = ?";
						 JCqueryStr = JCqueryStr + " and model.commissionSheet.headNameId = ?";
						 JYqueryStr = JYqueryStr + " and model.commissionSheet.headNameId = ?";
						 keys.add(Integer.valueOf(HeadName));
					}
					queryStr = queryStr + " and ( " +
										" 	(model.originalRecord is null and model.certificate is null) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
										" ) " ;
			
				    queryStr4 = queryStr4 + " and ( " +
								" 	(model.originalRecord is null and model.certificate is null) or " +
								"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate)) " +
								" ) " ;
				    JDqueryStr = JDqueryStr + "and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 1) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检定' )) " +
										" ) ";
				    JZqueryStr = JZqueryStr + "and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 2) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '校准' )) " +
										" ) ";
				    JCqueryStr = JCqueryStr + "and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 3) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检测' )) " +
										" ) ";
				    JYqueryStr = JYqueryStr + "and (" +
										" 	(model.originalRecord is null and model.certificate is null and model.commissionSheet.reportType = 4) or " +
										"	(model.originalRecord in (from OriginalRecord as o where o.status<>1 and o.taskAssign.status<>1 and o.certificate = model.certificate and o.workType = '检验' )) " +
										" ) ";

					statistic = cerFeeMgr.findByHQL(queryStr +" order by model.commissionSheet.code desc, model.commissionSheet.id desc", keys);
					FList=cerFeeMgr.findByHQL(queryStr4, keys);
					
					String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
					String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
					
					UserManager userMgr=new UserManager();
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(CertificateFeeAssign cerfee : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("FeeAlloteeName", cerfee.getSysUserByFeeAlloteeId().getName());	
							cSheet=cerfee.getCommissionSheet();
							option.put("Code", cSheet.getCode());						
							option.put("CustomerName", cSheet.getCustomerName());
							option.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));	
							if(cSheet.getCheckOutStaffId()!=null){
								String name=userMgr.findById(cSheet.getCheckOutStaffId()).getName();
								option.put("CheckOutStaff", name);						
								option.put("CheckOutTime", cSheet.getCheckOutDate());
								option.put("DetailListCode", cSheet.getDetailListCode());	
							}else{
								option.put("CheckOutStaff", "");						
								option.put("CheckOutTime", "");
								option.put("DetailListCode","");	
							}
							if(cerfee.getCertificate()==null){
								option.put("ApplianceName", "");	//器具名称（常用名称）
								option.put("Model", "");	//型号规格
								option.put("Range", "");		//测量范围
								option.put("Accuracy", "");	//精度等级
								option.put("Manufacturer", "");	//制造厂商
								option.put("Quantity", "");	//台/件数
								option.put("CertificateId","");	//证书文件ID								
								option.put("CertificatePdf", "");	//证书文件的PDF文件ID
								option.put("CertificateFileName", "");	//证书文件Doc文件的文件名
								option.put("CertificateCode","");	//证书编号
								
							}else{
								option.put("ApplianceName", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getApplianceName());	//器具名称（常用名称）
								option.put("Model", cerfee.getOriginalRecord().getApplianceName()==null?"":cerfee.getOriginalRecord().getModel()==null?"":cerfee.getOriginalRecord().getModel());	//型号规格
								option.put("Range", cerfee.getOriginalRecord().getRange());		//测量范围
								option.put("Accuracy", cerfee.getOriginalRecord().getAccuracy());	//精度等级
								option.put("Manufacturer", cerfee.getOriginalRecord().getManufacturer()==null?"":cerfee.getOriginalRecord().getManufacturer());	//制造厂商
								option.put("Quantity", cerfee.getOriginalRecord().getQuantity()==null?"":cerfee.getOriginalRecord().getQuantity());	//台/件数
								option.put("CertificateId", cerfee.getCertificate()==null?"":cerfee.getCertificate().getId());	//证书文件ID								
								option.put("CertificatePdf", (cerfee.getCertificate()==null || cerfee.getCertificate().getPdf()==null)?"":cerfee.getCertificate().getPdf());	//证书文件的PDF文件ID
								option.put("CertificateFileName", cerfee.getCertificate()==null?"":cerfee.getCertificate().getFileName());	//证书文件Doc文件的文件名
								option.put("CertificateCode", cerfee.getCertificate()==null?"":cerfee.getCertificate().getCertificateCode());	//证书编号								
								
							}	
							if(option.getString("Code")!=tempCode){//新委托单要统计退样数量
								tempCode=option.getString("Code");
								//查询完工器具数量和退样器具数量，以及是否转包
								List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId());	//完工器具数量
								if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
									option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
								}else{
									option.put("FinishQuantity", 0);
								}
								List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
								if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
									option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
									option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//退样器具数量
									
								}else{
									option.put("EffectQuantity", cSheet.getQuantity());
									option.put("WithdrawQuantity", 0);	//退样器具数量
								}
							}else{
								
								option.put("WithdrawQuantity", "");	//退样器具数量
								option.put("EffectQuantity", "");//退样器具数量
								option.put("FinishQuantity", "");	//完工器具数量
							}
							
							option.put("TestFee", cerfee.getTestFee()==null?0:cerfee.getTestFee());							
							option.put("RepairFee", cerfee.getRepairFee()==null?0:cerfee.getRepairFee());							
							option.put("MaterialFee", cerfee.getMaterialFee()==null?0:cerfee.getMaterialFee());							
							option.put("CarFee", cerfee.getCarFee()==null?0:cerfee.getCarFee());							
							option.put("DebugFee", cerfee.getDebugFee()==null?0:cerfee.getDebugFee());							
							option.put("OtherFee", cerfee.getOtherFee()==null?0:cerfee.getOtherFee());							
							option.put("TotalFee", cerfee.getTotalFee()==null?0:cerfee.getTotalFee());
												
							result.add(option);
						}
					}
					/***统计产值****/
					List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, keys1);
					QuantityNum = (quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
					
					
					List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, keys2);
					WithdrawQuantity = ( withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
					
					
					List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, keys1);
					CertificateNum =( certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
					
					if(FList==null||FList.isEmpty()){
				    	TestFee=0.0;
				    	RepairFee=0.0;
				    	MaterialFee=0.0;
				    	CarFee=0.0;
				    	DebugFee=0.0;
				    	OtherFee=0.0;
				    	TotalFee=0.0;
				    }else{
				    	Object[] fee =FList.get(0);					   						    	
				    	TestFee = ((Double)fee[0]==null?0.0:(Double)fee[0]);
				    	RepairFee =((Double)fee[1]==null?0.0:(Double)fee[1]);
						MaterialFee=((Double)fee[2]==null?0.0:(Double)fee[2]);
						CarFee =((Double)fee[3]==null?0.0:(Double)fee[3]);
						DebugFee =((Double)fee[4]==null?0.0:(Double)fee[4]);
						OtherFee =((Double)fee[5]==null?0.0:(Double)fee[5]);
						TotalFee =((Double)fee[6]==null?0.0:(Double)fee[6]);			   
				    }
					
					JDList=cerFeeMgr.findByHQL(JDqueryStr,keys);
					if(JDList!=null&&JDList.size()>0){
						Double fee =JDList.get(0);	
						JDFee=((Double)fee==null?0.0:(Double)fee);
					}
					JZList=cerFeeMgr.findByHQL(JZqueryStr,keys);
					if(JZList!=null&&JZList.size()>0){
						Double fee =JZList.get(0);	
						JZFee=((Double)fee==null?0.0:(Double)fee);
					}
					JYList=cerFeeMgr.findByHQL(JYqueryStr,keys);
					if(JYList!=null&&JYList.size()>0){
						Double fee =JYList.get(0);	
						JYFee=((Double)fee==null?0.0:(Double)fee);
					}
					JCList=cerFeeMgr.findByHQL(JCqueryStr,keys);
					if(JCList!=null&&JCList.size()>0){
						Double fee =JCList.get(0);	
						JCFee=((Double)fee==null?0.0:(Double)fee);
					}
					
				}
				JSONObject footerObj = new JSONObject();
				footerObj.put("Code", "总计");
				footerObj.put("FeeAlloteeName", "");						
				footerObj.put("CustomerName", "");
				footerObj.put("CommissionDate", "");	
				footerObj.put("Quantity", QuantityNum);	//台/件数
				footerObj.put("WithdrawQuantity", WithdrawQuantity);	//退样总数量
				footerObj.put("CertificateCode",CertificateNum);	//证书数量
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				
				result.add(footerObj);
				
				JSONObject Obj = new JSONObject();
				Obj.put("Code", "检定费:");
				Obj.put("CommissionDate", JDFee);
				Obj.put("CustomerName", "校准费:");
				Obj.put("ApplianceName", JZFee);
				Obj.put("Model", "检验费:");
				Obj.put("Range", JYFee);
				Obj.put("Accuracy", "检测费:");
				Obj.put("Manufacturer", JCFee);
				result.add(Obj);
				String filePath = ExportUtil.ExportToExcelByResultSet(result, null, "Detail_formatExcel", "Detail_formatTitle", ExportManager.class);
				
				retObj12.put("IsOK", filePath.equals("")?false:true);
				retObj12.put("Path", filePath);
			}catch(Exception e){
				try{
					retObj12.put("IsOK", false);
					retObj12.put("Path", "");
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 12", e);
				}else{
					log.error("error in StatisticServlet-->case 12", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj12.toString());
			}
			break;
		case 13://打印部门产值（非明细）
			JSONObject ret13 = new JSONObject();
			try{
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String DepartmentId = req.getParameter("DepartmentId");
				String CommissionType = req.getParameter("CommissionType");
				String HeadName = req.getParameter("HeadName");
				
				int deptId = Integer.valueOf(DepartmentId);
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					
					String queryStr = "select model.Id,dept.Name as deptName,model.Name as UserName,model.JobNum,temp.*,model.id from " + SystemCfgUtil.DBPrexName + "SysUser as model left join (select cer.FeeAlloteeId as feeAlloteeId, SUM(cer.totalFee) as totalfee, SUM(cer.testFee) as testfee, SUM(cer.repairFee) as repairfee, SUM(cer.materialFee) as materialfee, SUM(cer.carFee) as carfee, SUM(cer.debugFee) as debugfee, SUM(cer.otherFee) as otherfee from" + SystemCfgUtil.DBPrexName + "CertificateFeeAssign as cer, " + SystemCfgUtil.DBPrexName + "CommissionSheet as c " +
									  " where cer.CommissionSheetId = c.Id" +
									  " and (c.status = ? or c.status = ?) and (c.checkOutDate >= ? and c.checkOutDate <= ?)";
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr = queryStr + " and c.commissionType = " + CommissionType;
					}
					if(HeadName!=null&&!HeadName.equals("")){
						queryStr = queryStr + " and c.headNameId = " + HeadName;
					}
					queryStr = queryStr + " and ( " +
										  "   (cer.originalRecordid is null and cer.certificateid is null) or " +
										  "   (cer.originalRecordid in (select o.id from " + SystemCfgUtil.DBPrexName + "OriginalRecord as o," + SystemCfgUtil.DBPrexName + "TaskAssign as t where o.TaskAssignId = t.Id and o.status <>1 and t.status<>1 and o.certificateid = cer.certificateid)) " +
										  " ) " +
									  " group by cer.FeeAlloteeId) as temp on model.id = temp.feeAlloteeId," + SystemCfgUtil.DBPrexName + "ProjectTeam as p," + SystemCfgUtil.DBPrexName + "Department as dept " +
									  " where model.ProjectTeamId = p.Id and p.DeptId = dept.Id" + 
									  " and p.DeptId = ?"+
									  "	order by dept.DeptCode asc, model.JobNum asc";
					
					List<Object[]> statistic;
					CommissionSheetManager cSheetMgr = new CommissionSheetManager();
					
					statistic = cSheetMgr.findBySQL(queryStr, new Integer(4), new Integer(9), Start, End, deptId);
					//total = cSheetMgr.getTotalCountByHQL(queryTotalStr, new Integer(4), new Integer(9), Start, End);
					
					int quantity = 0;
					int withdraw = 0;
					int certificate = 0;
					double totalFee = 0;
					double testFee = 0;
					double repairFee = 0;
					double materialFee = 0;
					double carFee = 0;
					double debugFee = 0;
					double otherFee = 0;
					
					if(statistic!=null&&statistic.size()>0)
					{
						String tempCode = null;
						String tempName = null;
						int quantityByDept = 0;
						int withdrawByDept = 0;
						int certificateByDept = 0;
						double totalFeeByDept = 0;
						double testFeeByDept = 0;
						double repairFeeByDept = 0;
						double materialFeeByDept = 0;
						double carFeeByDept = 0;
						double debugFeeByDept = 0;
						double otherFeeByDept = 0;
						int len = 0;
						
						for(Object[] temp : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("UserId", temp[0].toString());
							option.put("UserName", temp[2].toString());
							option.put("UserJobNum", temp[3].toString());
							option.put("TotalFee", temp[5]==null?0:(Double)temp[5]);
							option.put("TestFee", temp[6]==null?0:(Double)temp[6]);
							option.put("RepairFee", temp[7]==null?0:(Double)temp[7]);
							option.put("MaterialFee", temp[8]==null?0:(Double)temp[8]);
							option.put("CarFee", temp[9]==null?0:(Double)temp[9]);
							option.put("DebugFee", temp[10]==null?0:(Double)temp[10]);
							option.put("OtherFee", temp[11]==null?0:(Double)temp[11]);
							
							String queryStr1 = "select sum(model.quantity) from OriginalRecord as model where model.sysUserByStaffId.id = ? and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? and  model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr1 = queryStr1 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> quantitys = cSheetMgr.findByHQL(queryStr1, (Integer)temp[12], Start, End);
							option.put("Quantity", quantitys==null||quantitys.size()==0?0:quantitys.get(0)==null?0:quantitys.get(0).intValue());
							
							String queryStr2 = "select sum(model.number) from Withdraw as model where model.sysUserByRequesterId.id = ? and model.executeResult = 1 and model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ?";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr2 = queryStr2 + " and model.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> withdraws = cSheetMgr.findByHQL(queryStr2, (Integer)temp[12], Start, End);
							option.put("Withdraw", withdraws==null||withdraws.size()==0?0:withdraws.get(0)==null?0:withdraws.get(0).intValue());
							
							String queryStr3 = "select count(o.certificate.id) from OriginalRecord as o where o.sysUserByStaffId.id = ?" +
											   " and o.commissionSheet.checkOutDate >= ? and o.commissionSheet.checkOutDate <= ? and model.status<>1 ";
							if(CommissionType!=null&&!CommissionType.equals("")){
								queryStr3 = queryStr3 + " and o.commissionSheet.commissionType = " + CommissionType;
							}
							List<Long> certificates = (new OriginalRecordManager()).findByHQL(queryStr3, (Integer)temp[12], Start, End);
							option.put("Certificate", certificates==null||certificates.size()==0?0:certificates.get(0)==null?0:certificates.get(0).intValue());
														
							totalFeeByDept += (Double)option.get("TotalFee");
							testFeeByDept += (Double)option.get("TestFee");
							repairFeeByDept += (Double)option.get("RepairFee");
							materialFeeByDept += (Double)option.get("MaterialFee");
							carFeeByDept += (Double)option.get("CarFee");
							debugFeeByDept += (Double)option.get("DebugFee");
							otherFeeByDept += (Double)option.get("OtherFee");
							quantityByDept += option.getInt("Quantity");
							withdrawByDept += option.getInt("Withdraw");
							certificateByDept += option.getInt("Certificate");
							
							totalFee += (Double)option.get("TotalFee");
							testFee += (Double)option.get("TestFee");
							repairFee += (Double)option.get("RepairFee");
							materialFee += (Double)option.get("MaterialFee");
							carFee += (Double)option.get("CarFee");
							debugFee += (Double)option.get("DebugFee");
							otherFee += (Double)option.get("OtherFee");
							quantity += option.getInt("Quantity");
							withdraw += option.getInt("Withdraw");
							certificate += option.getInt("Certificate");
							
							options.put(option);
							
							tempCode = temp[0].toString();
							tempName = temp[1].toString();
						}
						
					}
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("UserJobNum", "");
					jsonObj.put("UserId", "");
					jsonObj.put("UserName", "总产值");
					jsonObj.put("TotalFee", totalFee);
					jsonObj.put("TestFee", testFee);
					jsonObj.put("RepairFee", repairFee);
					jsonObj.put("MaterialFee", materialFee);
					jsonObj.put("CarFee", carFee);
					jsonObj.put("DebugFee", debugFee);
					jsonObj.put("OtherFee", otherFee);
					jsonObj.put("Quantity", quantity);
					jsonObj.put("Withdraw", withdraw);
					jsonObj.put("Certificate", certificate);
					
					options.put(jsonObj);
				}
				ret13.put("total", total);
				ret13.put("rows", options);
				
				
				DepartmentManager deptMgr=new DepartmentManager();
				Department dept=deptMgr.findById(deptId);
				Timestamp today = new Timestamp(System.currentTimeMillis());
				ret13.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				ret13.put("DeptName", dept.getName());
				ret13.put("DeptCode", dept.getDeptCode());
				ret13.put("DateFrom", StartTime);
				ret13.put("DateEnd", EndTime);
				
				ret13.put("IsOK", true);
				req.getSession().setAttribute("DeptOutputList", ret13);
				
				resp.sendRedirect("/jlyw/StatisticLook/OutputLook/DeptPrint.jsp");
			}catch(Exception e){
				
				try{
					ret13.put("total", 0);
					ret13.put("rows", new JSONArray());
					ret13.put("IsOK", false);
					req.getSession().setAttribute("DeptOutputList", ret13);
					resp.sendRedirect("/jlyw/StatisticLook/OutputLook/DeptPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 8", e);
				}else{
					log.error("error in StatisticServlet-->case 8", e);
				}
			}finally{
				
			}
			break;	
		case 14://按照委托单位查询产值明细(打印)
			String PrintStr = req.getParameter("PrintStr");
			JSONObject retObj14 = new JSONObject();
			try{
				ViewCommissionSheetFeeManager viewCSheetFeeMgr = new ViewCommissionSheetFeeManager();
				JSONObject printParams = new JSONObject(PrintStr);
				String Code = printParams.has("Code")?printParams.getString("Code"):"";
				String CommissionType = printParams.has("CommissionType")?printParams.getString("CommissionType"):"";
				String ReportType = printParams.has("ReportType")?printParams.getString("ReportType"):"";
				String CustomerId = printParams.has("CustomerId")?printParams.getString("CustomerId"):"";
				String ZipCode = printParams.has("ZipCode")?printParams.getString("ZipCode"):"";
				String RegionId = printParams.has("RegionId")?printParams.getString("RegionId"):"";
				String Classi = printParams.has("Classi")?printParams.getString("Classi"):"";
				String CommissionDateFrom = printParams.has("CommissionDateFrom")?printParams.getString("CommissionDateFrom"):"";
				String CommissionDateEnd = printParams.has("CommissionDateEnd")?printParams.getString("CommissionDateEnd"):"";
				String FinishDateFrom = printParams.has("FinishDateFrom")?printParams.getString("FinishDateFrom"):"";
				String FinishDateEnd = printParams.has("FinishDateEnd")?printParams.getString("FinishDateEnd"):"";
				String CheckOutDateFrom = printParams.has("CheckOutDateFrom")?printParams.getString("CheckOutDateFrom"):"";
				String CheckOutDateEnd = printParams.has("CheckOutDateEnd")?printParams.getString("CheckOutDateEnd"):"";
				String Status = printParams.has("Status")?printParams.getString("Status"):"";
				String Receiver = printParams.has("Receiver")?printParams.getString("Receiver"):"";
				String FinishUser = printParams.has("FinishUser")?printParams.getString("FinishUser"):"";
				String CheckOutUser = printParams.has("CheckOutUser")?printParams.getString("CheckOutUser"):"";
				String SpeciesType = printParams.has("SpeciesType")?printParams.getString("SpeciesType"):"";
				String ApplianceSpeciesId = printParams.has("ApplianceSpeciesId")?printParams.getString("ApplianceSpeciesId"):"";
				String InsideContactor = printParams.has("InsideContactor")?printParams.getString("InsideContactor"):"";
				String HeadName = printParams.has("HeadName")?printParams.getString("HeadName"):"";
				
				String queryStr = "from CommissionSheet as model,Customer as c where model.customerId = c.id and 1=1 ";
				String queryStringAllFee = " select SUM(view.testFee),SUM(view.repairFee),SUM(view.materialFee),SUM(view.carFee),SUM(view.debugFee),SUM(view.otherFee),SUM(view.totalFee) from ViewCommissionSheetFee as view, CommissionSheet as model, Customer as c " +
											" where model.customerId = c.id and view.id.commissionSheetId = model.id ";
				String queryStringCount = "select SUM(model.quantity) from CommissionSheet as model, Customer as c where model.customerId = c.id and 1=1 ";
				String withDrawString = "select SUM(w.number) from CommissionSheet as model, Withdraw as w, Customer as c where w.commissionSheet.id = model.id and w.executeResult=1 and model.customerId = c.id and 1=1 ";
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
						paramString = paramString + " and model.status < ?";
						keys.add(Integer.valueOf(4));
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
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				
				List<CommissionSheet> result = cSheetMgr.findByHQL("select model " + queryStr + paramString + " order by model.commissionDate desc, model.id desc", keys);
				total = cSheetMgr.getTotalCountByHQL("select count(model) " + queryStr + paramString, keys);
				if(result!=null&&result.size()>0){
					for(CommissionSheet cSheet : result){
						JSONObject option = new JSONObject();
						option.put("Code", cSheet.getCode());
						option.put("Pwd", cSheet.getPwd());
						option.put("CustomerName", cSheet.getCustomerName());
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
						option.put("OtherRequirements", cSheet.getOtherRequirements()==null?"":cSheet.getOtherRequirements());	//其它要求
						option.put("Location", cSheet.getLocation()==null?"":cSheet.getLocation());	//存放位置
						option.put("FinishLocation", cSheet.getFinishLocation()==null?"":cSheet.getFinishLocation());	//存放位置
						option.put("Allotee", cSheet.getAllotee()==null?"":cSheet.getAllotee());	//派定人
						SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						option.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//委托日期
						option.put("Status", FlagUtil.CommissionSheetStatus.getStatusString(cSheet.getStatus()));	//委托单状态
						
						/***********在CertificateFeeAssign(原始记录证书费用分配)中查找委托单的费用详情***********/
						
						//List<Object[]> FList=feeMgr.findByHQL(CertificateFeeAssignManager.queryStringAllFeeByCommissionSheetId, cSheet.getId());
						List<ViewCommissionSheetFee> FList = viewCSheetFeeMgr.findByVarProperty(new KeyValueWithOperator("id.commissionSheetId", cSheet.getId(), "="));
						
						
						if(FList.isEmpty()){
							option.put("TestFee", 0.0);
					    	option.put("RepairFee", 0.0);
							option.put("MaterialFee", 0.0);
							option.put("CarFee", 0.0);
							option.put("DebugFee", 0.0);
							option.put("OtherFee", 0.0);
							option.put("TotalFee", 0.0);
					    }else{
						    for(ViewCommissionSheetFee fee:FList){							    	
						    	option.put("TestFee", fee.getTestFee()==null?0.0:(Double)fee.getTestFee());
								option.put("RepairFee", fee.getRepairFee()==null?0.0:(Double)fee.getRepairFee());
								option.put("MaterialFee", fee.getMaterialFee()==null?0.0:(Double)fee.getMaterialFee());
								option.put("CarFee", fee.getCarFee()==null?0.0:(Double)fee.getCarFee());
								option.put("DebugFee", fee.getDebugFee()==null?0.0:(Double)fee.getDebugFee());
								option.put("OtherFee", fee.getOtherFee()==null?0.0:(Double)fee.getOtherFee());
								option.put("TotalFee", fee.getTotalFee()==null?0.0:(Double)fee.getTotalFee());
						   }
					    }
						String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
						List<Long> wQuantityList = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity, cSheet.getId(), true);	//退样器具数量
						if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
							option.put("WithdrawQuantity", ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
						}else{
							option.put("WithdrawQuantity", 0);
						}
						
						String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
						String hqlQueryString_WithdrawQuantity1 = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
						//查询完工器具数量和退样器具数量，以及是否转包
						List<Long> fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, cSheet.getId(), true);	//完工器具数量
						if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
							option.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
						}else{
							option.put("FinishQuantity", 0);
						}
						List<Long> wQuantityList1 = cSheetMgr.findByHQL(hqlQueryString_WithdrawQuantity1, cSheet.getId(), true);	//退样器具数量
						if(wQuantityList1 != null && wQuantityList1.size() > 0 && wQuantityList1.get(0) != null){
							option.put("EffectQuantity", cSheet.getQuantity() - ((Long)wQuantityList1.get(0)).intValue());	//有效器具数量
						}else{
							option.put("EffectQuantity", cSheet.getQuantity());
						}
						String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
						int iSubContract = cSheetMgr.getTotalCountByHQL(hqlQueryString_SubContract, cSheet.getId());
						if(iSubContract > 0 || cSheet.getCommissionType() == 5){	//该委托单有转包(或该委托单为其他业务)，则完工确认时不需要判断‘完工器具数量是否大于等于有效器具数量’;
							option.put("IsSubContract", true);
						}else{
							option.put("IsSubContract", false);
						}
						options.put(option);
					}
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

				List<Object[]> feeList = viewCSheetFeeMgr.findByHQL(queryStringAllFee + paramString, keys);
				List<Long> quantityList = feeMgr.findByHQL(queryStringCount + paramString, keys);
				List<Long> withdrawList = feeMgr.findByHQL(withDrawString + paramString, keys);
				if(feeList.isEmpty()){
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
			    	jsonObj.put("Quantity", 0.0);
			    	jsonObj.put("WithdrawQuantity", 0.0);
			    }else{
				    for(Object[] fee:feeList){
						jsonObj.put("CustomerName", "总计");
				    	jsonObj.put("TestFee", fee[0]==null?0.0:(Double)fee[0]);
				    	jsonObj.put("RepairFee", fee[1]==null?0.0:(Double)fee[1]);
				    	jsonObj.put("MaterialFee", fee[2]==null?0.0:(Double)fee[2]);
				    	jsonObj.put("CarFee", fee[3]==null?0.0:(Double)fee[3]);
				    	jsonObj.put("DebugFee", fee[4]==null?0.0:(Double)fee[4]);
				    	jsonObj.put("OtherFee", fee[5]==null?0.0:(Double)fee[5]);
				    	jsonObj.put("TotalFee", fee[6]==null?0.0:(Double)fee[6]);
				    	jsonObj.put("Quantity", quantityList.get(0)==null?0.0:(Long)quantityList.get(0));
				    	jsonObj.put("WithdrawQuantity", withdrawList.get(0)==null?0.0:(Long)withdrawList.get(0));
				   }
			    }
				
				options.put(jsonObj);
				retObj14.put("total", total);
				retObj14.put("rows", options);

				Timestamp today = new Timestamp(System.currentTimeMillis());
				retObj14.put("PrintDate", DateTimeFormatUtil.DateFormat.format(today));
				
				retObj14.put("IsOK", true);
				req.getSession().setAttribute("MissionLookList", retObj14);
				
				resp.sendRedirect("/jlyw/StatisticLook/MissionLookPrint.jsp");
			}catch(Exception e){
				
				try{
					retObj14.put("total", 0);
					retObj14.put("rows", new JSONArray());
					retObj14.put("IsOK", false);
					req.getSession().setAttribute("MissionLookList", retObj14);
					resp.sendRedirect("/jlyw/StatisticLook/MissionLookPrint.jsp");
				}catch(Exception e1){}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 14", e);
				}else{
					log.error("error in StatisticServlet-->case 14", e);
				}
			}finally{
				
			}
			break;	
		case 15://证书查询
			JSONObject retObj15 = new JSONObject();
			try{
				String Code = req.getParameter("Code");
				String CommissionType = req.getParameter("CommissionType");
				String ReportType = req.getParameter("ReportType");
				String CustomerId = req.getParameter("CustomerId");
				String CommissionDateFrom = req.getParameter("CommissionDateFrom");
				String CommissionDateEnd = req.getParameter("CommissionDateEnd");
				String FinishDateFrom = req.getParameter("FinishDateFrom");
				String FinishDateEnd = req.getParameter("FinishDateEnd");
				String CheckOutDateFrom = req.getParameter("CheckOutDateFrom");
				String CheckOutDateEnd = req.getParameter("CheckOutDateEnd");
				String Status = req.getParameter("Status");
				String Receiver = req.getParameter("Receiver");
				String FinishUser = req.getParameter("FinishUser");
				String CheckOutUser = req.getParameter("CheckOutUser");
				String SpeciesType = req.getParameter("SpeciesType");
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");
				String HeadName = req.getParameter("HeadName");
				
				
				String queryStr = "from OriginalRecord as model, Customer as c where model.commissionSheet.customerId = c.id and model.status<>1 and model.certificate is not null and model.certificate.pdf is not null and model.taskAssign.status<>1 ";
				String queryStringAllFee = " select SUM(model.testFee),SUM(model.repairFee),SUM(model.materialFee),SUM(model.carFee),SUM(model.debugFee),SUM(model.otherFee),SUM(model.totalFee) " +
											" from OriginalRecord as model, Customer as c " +
											" where model.commissionSheet.customerId = c.id and model.status<>1 and model.certificate is not null and model.certificate.pdf is not null and model.taskAssign.status<>1 "; 
				
				List<Object> keys = new ArrayList<Object>();
				if(Code!=null&&!Code.equals("")){
					String CodeStr = URLDecoder.decode(Code, "UTF-8");
					queryStr = queryStr + " and model.commissionSheet.code = ?";
					queryStringAllFee = queryStringAllFee + " and model.commissionSheet.code = ?";
					keys.add(CodeStr);
				}
				if(CommissionType!=null&&!CommissionType.equals("")){
					queryStr = queryStr + " and model.commissionSheet.commissionType = ?";
					queryStringAllFee = queryStringAllFee + " and model.commissionSheet.commissionType = ?";
					keys.add(Integer.valueOf(CommissionType));
				}
				if(ReportType!=null&&!ReportType.equals("")){
					queryStr = queryStr + " and model.workType = ?";
					queryStringAllFee = queryStringAllFee + " and model.workType = ?";
					keys.add(ReportType);
				}
				if(CustomerId!=null&&!CustomerId.equals("")){
					String cusIdStr = URLDecoder.decode(CustomerId, "UTF-8");
					queryStr = queryStr + " and model.commissionSheet.customerId = ?";
					queryStringAllFee = queryStringAllFee + " and model.commissionSheet.customerId = ?";
					keys.add(Integer.valueOf(cusIdStr));
				}
				if(Receiver!=null&&!Receiver.equals("")){
					String receiverStr = URLDecoder.decode(Receiver, "UTF-8");
					queryStr = queryStr + " and model.commissionSheet.receiverId = ?";
					queryStringAllFee = queryStringAllFee + " and model.commissionSheet.receiverId = ?";
					keys.add(Integer.valueOf(receiverStr));
				}
				if(FinishUser!=null&&!FinishUser.equals("")){
					String finUserStr = URLDecoder.decode(FinishUser, "UTF-8");
					queryStr = queryStr + " and model.commissionSheet.finishStaffId = ?";
					queryStringAllFee = queryStringAllFee + " and model.commissionSheet.finishStaffId = ?";
					keys.add(Integer.valueOf(finUserStr));
				}
				if(CheckOutUser!=null&&!CheckOutUser.equals("")){
					String checkUserStr = URLDecoder.decode(CheckOutUser, "UTF-8");
					queryStr = queryStr + " and model.commissionSheet.checkOutStaffId = ?";
					queryStringAllFee = queryStringAllFee + " and model.commissionSheet.checkOutStaffId = ?";
					keys.add(Integer.valueOf(checkUserStr));
				}
				if(CommissionDateFrom!=null&&!CommissionDateFrom.equals("")&&CommissionDateEnd!=null&&!CommissionDateEnd.equals("")){
					Timestamp Start = Timestamp.valueOf(CommissionDateFrom + " 0:0:0");
					Timestamp End = Timestamp.valueOf(CommissionDateEnd + " 23:59:59");
					queryStr = queryStr + " and (model.commissionSheet.commissionDate >= ? and model.commissionSheet.commissionDate <= ? )";
					queryStringAllFee = queryStringAllFee + " and (model.commissionSheet.commissionDate >= ? and model.commissionSheet.commissionDate <= ? )";
					keys.add(Start);
					keys.add(End);
				}
				if(FinishDateFrom!=null&&!FinishDateFrom.equals("")&&FinishDateEnd!=null&&!FinishDateEnd.equals("")){
					Timestamp Start = Timestamp.valueOf(FinishDateFrom + " 0:0:0");
					Timestamp End = Timestamp.valueOf(FinishDateEnd + " 23:59:59");
					
					queryStr = queryStr + " and (model.commissionSheet.finishDate >= ? and model.commissionSheet.finishDate <= ? )";
					queryStringAllFee = queryStringAllFee + " and (model.commissionSheet.finishDate >= ? and model.commissionSheet.finishDate <= ? )";
					keys.add(Start);
					keys.add(End);
				}
				if(CheckOutDateFrom!=null&&!CheckOutDateFrom.equals("")&&CheckOutDateEnd!=null&&!CheckOutDateEnd.equals("")){
					Timestamp Start = Timestamp.valueOf(CheckOutDateFrom + " 0:0:0");
					Timestamp End = Timestamp.valueOf(CheckOutDateEnd + " 23:59:59");
					
					queryStr = queryStr + " and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? )";
					queryStringAllFee = queryStringAllFee + " and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? )";
					keys.add(Start);
					keys.add(End);
				}
				if(Status!=null&&!Status.equals("")){
					String statusStr = URLDecoder.decode(Status, "UTF-8");
					if(statusStr.equals("<3")){
						queryStr = queryStr + " and model.commissionSheet.status < ?";
						queryStringAllFee = queryStringAllFee + " and model.commissionSheet.status < ?";
						keys.add(Integer.valueOf(3));
					}else if(statusStr.equals("<4")){
						queryStr = queryStr + " and model.commissionSheet.status < ?";
						queryStringAllFee = queryStringAllFee + " and model.commissionSheet.status < ?";
						keys.add(Integer.valueOf(4));
					}else{
						queryStr = queryStr + " and model.commissionSheet.status = ?";
						queryStringAllFee = queryStringAllFee + " and model.commissionSheet.status = ?";
						keys.add(Integer.valueOf(statusStr));
					}
				}
				if(SpeciesType!=null&&!SpeciesType.equals("")&&ApplianceSpeciesId!=null&&!ApplianceSpeciesId.equals("")){
					String speciesTypeStr = URLDecoder.decode(SpeciesType, "UTF-8");
					String applianceSpeciesIdStr = URLDecoder.decode(ApplianceSpeciesId, "UTF-8");
					queryStr = queryStr + " and (model.commissionSheet.speciesType = ? and model.commissionSheet.applianceSpeciesId = ?)";
					queryStringAllFee = queryStringAllFee + " and (model.commissionSheet.speciesType = ? and model.commissionSheet.applianceSpeciesId = ?)";
					keys.add(Integer.valueOf(speciesTypeStr)==1?true:false);
					keys.add(Integer.valueOf(applianceSpeciesIdStr));
				}
				if(HeadName!=null&&!HeadName.equals("")){
					String HeadNameStr = URLDecoder.decode(HeadName, "UTF-8");
					queryStr = queryStr + " and model.headNameId = ?";
					queryStringAllFee = queryStringAllFee + " and model.headNameId = ?";
					keys.add(Integer.valueOf(HeadNameStr));
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
				int total = 0;
				JSONArray options = new JSONArray();
				JSONArray foot = new JSONArray();
				
				List<OriginalRecord> result = oRecordMgr.findPageAllByHQL("select model " + queryStr + " order by model.commissionSheet.code desc, model.id desc", page, rows, keys);
				total = oRecordMgr.getTotalCountByHQL("select count(model) " + queryStr, keys);

				if(result!=null&&result.size()>0){
					for(OriginalRecord oRecord : result){
						JSONObject option = new JSONObject();
						CommissionSheet cSheet = oRecord.getCommissionSheet();
						option.put("CommissionSheetCode", cSheet.getCode());
						option.put("Code", oRecord.getCertificate().getCertificateCode());
						option.put("ReportType", oRecord.getWorkType());
						option.put("CustomerName", cSheet.getCustomerName());
						option.put("CustomerAddress", cSheet.getCustomerAddress());
						option.put("CustomerZipCode", cSheet.getCustomerZipCode());
						option.put("CustomerTel", cSheet.getCustomerTel());
						option.put("CustomerContactor", cSheet.getCustomerContactor());
						option.put("ApplianceName", oRecord.getApplianceName()==null?"":oRecord.getApplianceName());	//器具名称
						option.put("Quantity", oRecord.getQuantity());
						option.put("TestDate", oRecord.getWorkDate());
						option.put("Validity", oRecord.getValidity());
						option.put("ApplianceManufacturer", oRecord.getManufacturer());
						option.put("PDF", oRecord.getCertificate().getPdf());
						option.put("ApplianceCode", oRecord.getApplianceCode());	//出厂编号
						option.put("AppManageCode", oRecord.getManageCode());	//管理编号
						option.put("Model", oRecord.getApplianceName()==null?"":oRecord.getModel()==null?"":oRecord.getModel());	//型号规格
													    	
						option.put("TestFee", oRecord.getTestFee()==null?0.0:oRecord.getTestFee());
						option.put("RepairFee", oRecord.getRepairFee()==null?0.0:oRecord.getRepairFee());
						option.put("MaterialFee", oRecord.getMaterialFee()==null?0.0:oRecord.getMaterialFee());
						option.put("CarFee", oRecord.getCarFee()==null?0.0:oRecord.getCarFee());
						option.put("DebugFee", oRecord.getDebugFee()==null?0.0:oRecord.getDebugFee());
						option.put("OtherFee", oRecord.getOtherFee()==null?0.0:oRecord.getOtherFee());
						option.put("TotalFee", oRecord.getTotalFee()==null?0.0:oRecord.getTotalFee());
						options.put(option);
					}
				}
				List<Object[]> feeList = feeMgr.findByHQL(queryStringAllFee, keys);
				JSONObject jsonObj = new JSONObject();
				
				if(feeList.isEmpty()){
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
			    }else{
				    for(Object[] fee:feeList){
						jsonObj.put("CustomerName", "总计");
				    	jsonObj.put("TestFee", (Double)fee[0]==null?0.0:(Double)fee[0]);
				    	jsonObj.put("RepairFee", (Double)fee[1]==null?0.0:(Double)fee[1]);
				    	jsonObj.put("MaterialFee", (Double)fee[2]==null?0.0:(Double)fee[2]);
				    	jsonObj.put("CarFee", (Double)fee[3]==null?0.0:(Double)fee[3]);
				    	jsonObj.put("DebugFee", (Double)fee[4]==null?0.0:(Double)fee[4]);
				    	jsonObj.put("OtherFee", (Double)fee[5]==null?0.0:(Double)fee[5]);
				    	jsonObj.put("TotalFee", (Double)fee[6]==null?0.0:(Double)fee[6]);
				   }
			    }
				foot.put(jsonObj);
				retObj15.put("total", total);
				retObj15.put("rows", options);
				retObj15.put("footer", foot);
			}catch(Exception e){
				try{
					retObj15.put("total", 0);
					retObj15.put("rows", new JSONArray());
					JSONArray foot = new JSONArray();
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CustomerName", "总计");
					jsonObj.put("TestFee", 0.0);
					jsonObj.put("RepairFee", 0.0);
					jsonObj.put("MaterialFee", 0.0);
					jsonObj.put("CarFee", 0.0);
					jsonObj.put("DebugFee", 0.0);
					jsonObj.put("OtherFee", 0.0);
					jsonObj.put("TotalFee", 0.0);
					foot.put(jsonObj);
					retObj15.put("footer", foot);
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 15", e);
				}else{
					log.error("error in StatisticServlet-->case 15", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj15.toString());
			}
			break;
		case 16://证书查询导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj16 = new JSONObject();
			try{
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from OriginalRecord as model, Customer as c where model.commissionSheet.customerId = c.id and model.status<>1 and model.certificate is not null and model.certificate.pdf is not null and model.taskAssign.status<>1 ";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String Code = params.has("Code")?params.getString("Code"):"";
					String CommissionType = params.has("CommissionType")?params.getString("CommissionType"):"";
					String ReportType = params.has("ReportType")?params.getString("ReportType"):"";
					String CustomerId = params.has("CustomerId")?params.getString("CustomerId"):"";
					String ZipCode = params.has("ZipCode")?params.getString("ZipCode"):"";
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
					String HeadName = params.has("HeadName")?params.getString("HeadName"):"";
				
					if(Code!=null&&!Code.equals("")){
						String CodeStr = URLDecoder.decode(Code, "UTF-8");
						queryStr = queryStr + " and model.commissionSheet.code = ?";
						keys.add(CodeStr);
					}
					if(CommissionType!=null&&!CommissionType.equals("")){
						queryStr = queryStr + " and model.commissionSheet.commissionType = ?";
						keys.add(Integer.valueOf(CommissionType));
					}
					if(ReportType!=null&&!ReportType.equals("")){
						queryStr = queryStr + " and model.workType = ?";
						keys.add(ReportType);
					}
					if(CustomerId!=null&&!CustomerId.equals("")){
						String cusIdStr = URLDecoder.decode(CustomerId, "UTF-8");
						queryStr = queryStr + " and model.commissionSheet.customerId = ?";
						keys.add(Integer.valueOf(cusIdStr));
					}
					if(Receiver!=null&&!Receiver.equals("")){
						String receiverStr = URLDecoder.decode(Receiver, "UTF-8");
						queryStr = queryStr + " and model.commissionSheet.receiverId = ?";
						keys.add(Integer.valueOf(receiverStr));
					}
					if(FinishUser!=null&&!FinishUser.equals("")){
						String finUserStr = URLDecoder.decode(FinishUser, "UTF-8");
						queryStr = queryStr + " and model.commissionSheet.finishStaffId = ?";
						keys.add(Integer.valueOf(finUserStr));
					}
					if(CheckOutUser!=null&&!CheckOutUser.equals("")){
						String checkUserStr = URLDecoder.decode(CheckOutUser, "UTF-8");
						queryStr = queryStr + " and model.commissionSheet.checkOutStaffId = ?";
						keys.add(Integer.valueOf(checkUserStr));
					}
					if(CommissionDateFrom!=null&&!CommissionDateFrom.equals("")&&CommissionDateEnd!=null&&!CommissionDateEnd.equals("")){
						Timestamp Start = Timestamp.valueOf(CommissionDateFrom + " 0:0:0");
						Timestamp End = Timestamp.valueOf(CommissionDateEnd + " 23:59:59");
						queryStr = queryStr + " and (model.commissionSheet.commissionDate >= ? and model.commissionSheet.commissionDate <= ? )";
						keys.add(Start);
						keys.add(End);
					}
					if(FinishDateFrom!=null&&!FinishDateFrom.equals("")&&FinishDateEnd!=null&&!FinishDateEnd.equals("")){
						Timestamp Start = Timestamp.valueOf(FinishDateFrom + " 0:0:0");
						Timestamp End = Timestamp.valueOf(FinishDateEnd + " 23:59:59");
						queryStr = queryStr + " and (model.commissionSheet.finishDate >= ? and model.commissionSheet.finishDate <= ? )";
						keys.add(Start);
						keys.add(End);
					}
					if(CheckOutDateFrom!=null&&!CheckOutDateFrom.equals("")&&CheckOutDateEnd!=null&&!CheckOutDateEnd.equals("")){
						Timestamp Start = Timestamp.valueOf(CheckOutDateFrom + " 0:0:0");
						Timestamp End = Timestamp.valueOf(CheckOutDateEnd + " 23:59:59");
						
						queryStr = queryStr + " and (model.commissionSheet.checkOutDate >= ? and model.commissionSheet.checkOutDate <= ? )";
						keys.add(Start);
						keys.add(End);
					}
					if(Status!=null&&!Status.equals("")){
						String statusStr = URLDecoder.decode(Status, "UTF-8");
						if(statusStr.equals("<3")){
							queryStr = queryStr + " and model.status < ?";
							keys.add(Integer.valueOf(3));
						}else if(statusStr.equals("<4")){
							queryStr = queryStr + " and model.status < ?";
							keys.add(Integer.valueOf(4));
						}
						else{
							queryStr = queryStr + " and model.status = ?";
							keys.add(Integer.valueOf(statusStr));
						}
					}
					if(SpeciesType!=null&&!SpeciesType.equals("")&&ApplianceSpeciesId!=null&&!ApplianceSpeciesId.equals("")){
						String speciesTypeStr = URLDecoder.decode(SpeciesType, "UTF-8");
						String applianceSpeciesIdStr = URLDecoder.decode(ApplianceSpeciesId, "UTF-8");
						queryStr = queryStr + " and (model.commissionSheet.speciesType = ? and model.commissionSheet.applianceSpeciesId = ?)";
						keys.add(Integer.valueOf(speciesTypeStr)==1?true:false);
						keys.add(Integer.valueOf(applianceSpeciesIdStr));
					}
					if(HeadName!=null&&!HeadName.equals("")){
						String HeadNameStr = URLDecoder.decode(HeadName, "UTF-8");
						queryStr = queryStr + " and model.headNameId = ?";
						keys.add(Integer.valueOf(HeadNameStr));
					}
				}
				
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateFeeAssignManager feeMgr=new CertificateFeeAssignManager();
				int total = 0;
				JSONArray options = new JSONArray();
				
				List<OriginalRecord> result = oRecordMgr.findByHQL("select model " + queryStr + " order by model.commissionSheet.code desc, model.id desc", keys);
				List<JSONObject> objList = new ArrayList<JSONObject>();
				if(result!=null&&result.size()>0){
					for(OriginalRecord oRecord : result){
						JSONObject option = new JSONObject();
						CommissionSheet cSheet = oRecord.getCommissionSheet();
						option.put("CommissionSheetCode", cSheet.getCode());
						option.put("Code", oRecord.getCertificate().getCertificateCode());
						option.put("ReportType", oRecord.getWorkType());
						option.put("CustomerName", cSheet.getCustomerName());
						option.put("CustomerAddress", cSheet.getCustomerAddress());
						option.put("CustomerZipCode", cSheet.getCustomerZipCode());
						option.put("CustomerTel", cSheet.getCustomerTel());
						option.put("CustomerContactor", cSheet.getCustomerContactor());
						option.put("ApplianceName", oRecord.getApplianceName()==null?"":oRecord.getApplianceName());	//器具名称
						option.put("Quantity", oRecord.getQuantity());
						option.put("TestDate", oRecord.getWorkDate());
						option.put("Validity", oRecord.getValidity());
						option.put("ApplianceManufacturer", oRecord.getManufacturer());
						option.put("PDF", oRecord.getCertificate().getPdf());
						option.put("ApplianceCode", oRecord.getApplianceCode());	//出厂编号
						option.put("AppManageCode", oRecord.getManageCode());	//管理编号
						option.put("Model", oRecord.getApplianceName()==null?"":oRecord.getModel()==null?"":oRecord.getModel());	//型号规格
													    	
						option.put("TestFee", oRecord.getTestFee()==null?0.0:oRecord.getTestFee());
						option.put("RepairFee", oRecord.getRepairFee()==null?0.0:oRecord.getRepairFee());
						option.put("MaterialFee", oRecord.getMaterialFee()==null?0.0:oRecord.getMaterialFee());
						option.put("CarFee", oRecord.getCarFee()==null?0.0:oRecord.getCarFee());
						option.put("DebugFee", oRecord.getDebugFee()==null?0.0:oRecord.getDebugFee());
						option.put("OtherFee", oRecord.getOtherFee()==null?0.0:oRecord.getOtherFee());
						option.put("TotalFee", oRecord.getTotalFee()==null?0.0:oRecord.getTotalFee());
						objList.add(option);
					}
				}
				String filePath = ExportUtil.ExportToExcelByResultSet(objList, null, "Certificate_formatExcel", "Certificate_formatTitle", ExportManager.class);
				retObj16.put("IsOK", filePath.equals("")?false:true);
				retObj16.put("Path", filePath);
			}catch(Exception e){
				try{
					retObj16.put("IsOK", false);
					retObj16.put("Path", "");
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 16", e);
				}else{
					log.error("error in StatisticServlet-->case 16", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj16.toString());
			}
			break;
		case 17://按委托单位汇总业务量
			JSONObject retJSON17 = new JSONObject();
			try {
				String StartTime = req.getParameter("StartTime");
				String EndTime = req.getParameter("EndTime");
				String CustomerId = req.getParameter("CustomerId");
				String DepartmentId = req.getParameter("DepartmentId");
				String EmployeeId = req.getParameter("EmployeeId");
				String Status = req.getParameter("Status");
				
				String queryStr = "from OriginalRecord as model where model.commissionSheet.commissionDate >= ? and model.commissionSheet.commissionDate <= ?";
						
				int total = 0;
				int doneTotal = 0;
				List<Object[]> statistic;
				JSONArray options = new JSONArray();
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				List<Object> keys = new ArrayList<Object>();
				
				if(StartTime!=null&&!StartTime.equals("")&&EndTime!=null&&!EndTime.equals(""))
				{
				
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					keys.add(Start);
					keys.add(End);
					
					if(CustomerId!=null&&!CustomerId.equals(""))
					{
						queryStr = queryStr + " and model.commissionSheet.customerId = ?";
						keys.add(Integer.valueOf(CustomerId));
					}
					if(DepartmentId!=null&&!DepartmentId.equals(""))
					{
						queryStr = queryStr + " and model.sysUserByStaffId.projectTeamId in (select (p.id) from ProjectTeam as p where p.department.id = ?) ";
						keys.add(Integer.valueOf(DepartmentId));
					}
					if(EmployeeId!=null&&!EmployeeId.equals(""))
					{
						queryStr = queryStr + " and model.sysUserByStaffId.id = ? ";
						keys.add(Integer.valueOf(EmployeeId));
					}
					if(Status!=null&&!Status.equals(""))
					{
						String statusStr = URLDecoder.decode(Status, "UTF-8");
						if(statusStr.equals("<3")){
							queryStr = queryStr + " and model.commissionSheet.status < ?";
							keys.add(Integer.valueOf(3));
						}else if(statusStr.equals("<4")){
							queryStr = queryStr + " and (model.commissionSheet.status < ?)";
							keys.add(Integer.valueOf(4));
						}else{
							queryStr = queryStr + " and model.commissionSheet.status = ?";
							keys.add(Integer.valueOf(statusStr));
						}						
					}
					total = taskMgr.getTotalCountByHQL("select count(distinct model.commissionSheet.customerName) " + queryStr, keys);
					queryStr = "select model.commissionSheet.customerId, model.commissionSheet.customerName, sum(model.totalFee), sum(model.testFee), sum(model.materialFee), sum(model.carFee), sum(model.debugFee), sum(model.repairFee), sum(model.otherFee) " + queryStr + " group by model.commissionSheet.customerId, model.commissionSheet.customerName order by model.commissionSheet.customerId";
					statistic = taskMgr.findPageAllByHQL(queryStr, page, rows, keys);
					
					
					if(statistic!=null&&statistic.size()>0)
					{
						for(Object[] obj : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("CustomerName", obj[1].toString());
							option.put("TotalFee",(Double)obj[2]==null?0.0:(Double)obj[2]);
							option.put("TestFee", (Double)obj[3]==null?0.0:(Double)obj[3]);
							option.put("MaterialFee", (Double)obj[4]==null?0.0:(Double)obj[4]);
							option.put("CarFee", (Double)obj[5]==null?0.0:(Double)obj[5]);
							option.put("DebugFee", (Double)obj[6]==null?0.0:(Double)obj[6]);
							option.put("RepairFee", (Double)obj[7]==null?0.0:(Double)obj[7]);
							option.put("OtherFee", (Double)obj[8]==null?0.0:(Double)obj[8]);
							option.put("CustomerId", Integer.valueOf(obj[0].toString()));
							
							options.put(option);
						}
					}
				}
				retJSON17.put("total", total);
				retJSON17.put("rows", options);
				
			} catch (Exception e){
				
				try {
					retJSON17.put("total", 0);
					retJSON17.put("rows", new JSONArray());
				} catch (JSONException e1) {
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 17", e);
				}else{
					log.error("error in StatisticServlet-->case 17", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON17.toString());
			}
			break;
		case 18://按委托单位汇总业务量
			String paramsStr18 = req.getParameter("paramsStr");
			JSONObject retJSON18 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr18);
				
				String StartTime = params.has("StartTime")?params.getString("StartTime"):"";
				String EndTime = params.has("EndTime")?params.getString("EndTime"):"";
				String CustomerId = params.has("CustomerId")?params.getString("CustomerId"):"";
				String DepartmentId = params.has("DepartmentId")?params.getString("DepartmentId"):"";
				String EmployeeId = params.has("EmployeeId")?params.getString("EmployeeId"):"";
				String Status = params.has("Status")?params.getString("Status"):"";
				
				String queryStr = "from OriginalRecord as model where model.commissionSheet.commissionDate >= ? and model.commissionSheet.commissionDate <= ?";

				List<Object[]> statistic;
				List<JSONObject> options = new ArrayList<JSONObject>();
				
				List<Object> keys = new ArrayList<Object>();
				
				if(StartTime!=null&&!StartTime.equals("")&&EndTime!=null&&!EndTime.equals(""))
				{
				
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					keys.add(Start);
					keys.add(End);
					
					if(CustomerId!=null&&!CustomerId.equals(""))
					{
						queryStr = queryStr + " and model.commissionSheet.customerId = ?";
						keys.add(Integer.valueOf(CustomerId));
					}
					if(DepartmentId!=null&&!DepartmentId.equals(""))
					{
						queryStr = queryStr + " and model.sysUserByStaffId.projectTeamId in (select (p.id) from ProjectTeam as p where p.department.id = ?) ";
						keys.add(Integer.valueOf(DepartmentId));
					}
					if(EmployeeId!=null&&!EmployeeId.equals(""))
					{
						queryStr = queryStr + " and model.sysUserByStaffId.id = ? ";
						keys.add(Integer.valueOf(EmployeeId));
					}
					if(Status!=null&&!Status.equals(""))
					{
						String statusStr = URLDecoder.decode(Status, "UTF-8");
						if(statusStr.equals("<3")){
							queryStr = queryStr + " and model.commissionSheet.status < ?";
							keys.add(Integer.valueOf(3));
						}else if(statusStr.equals("<4")){
							queryStr = queryStr + " and (model.commissionSheet.status < ?)";
							keys.add(Integer.valueOf(4));
						}else{
							queryStr = queryStr + " and model.commissionSheet.status = ?";
							keys.add(Integer.valueOf(statusStr));
						}						
					}

					queryStr = "select model.commissionSheet.customerId, model.commissionSheet.customerName, sum(model.totalFee), sum(model.testFee), sum(model.materialFee), sum(model.carFee), sum(model.debugFee), sum(model.repairFee), sum(model.otherFee) " + queryStr + " group by model.commissionSheet.customerId, model.commissionSheet.customerName order by model.commissionSheet.customerId";
					statistic = taskMgr.findByHQL(queryStr, keys);

					if(statistic!=null&&statistic.size()>0)
					{
						for(Object[] obj : statistic)
						{
							JSONObject option = new JSONObject();
							option.put("CustomerName", obj[1].toString());
							option.put("TotalFee",(Double)obj[2]==null?0.0:(Double)obj[2]);
							option.put("TestFee", (Double)obj[3]==null?0.0:(Double)obj[3]);
							option.put("MaterialFee", (Double)obj[4]==null?0.0:(Double)obj[4]);
							option.put("CarFee", (Double)obj[5]==null?0.0:(Double)obj[5]);
							option.put("DebugFee", (Double)obj[6]==null?0.0:(Double)obj[6]);
							option.put("RepairFee", (Double)obj[7]==null?0.0:(Double)obj[7]);
							option.put("OtherFee", (Double)obj[8]==null?0.0:(Double)obj[8]);
							option.put("CustomerId", Integer.valueOf(obj[0].toString()));
							
							options.add(option);
						}
					}
				}
				String filePath = ExportUtil.ExportToExcelByResultSet(options, null, "TransactionCustomerSUM_formatExcel", "TransactionCustomerSUM_formatTitle", ExportManager.class);
				retJSON18.put("IsOK", filePath.equals("")?false:true);
				retJSON18.put("Path", filePath);
				
			} catch (Exception e){
				try{
					retJSON18.put("IsOK", false);
					retJSON18.put("Path", "");
				} catch(Exception e1){
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in StatisticServlet-->case 18", e);
				}else{
					log.error("error in StatisticServlet-->case 18", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON18.toString());
			}
			break;
		}
	}
}
