package com.jlyw.servlet.vehicle;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
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
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.BaseHibernateDAO;
import com.jlyw.hibernate.DrivingVehicle;
import com.jlyw.hibernate.LocaleApplianceItem;
import com.jlyw.hibernate.LocaleMission;
import com.jlyw.hibernate.Role;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.UserRole;
import com.jlyw.hibernate.VehicleFee;
import com.jlyw.hibernate.VehicleMission;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DrivingVehicleManager;
import com.jlyw.manager.UserRoleManager;
import com.jlyw.manager.VehicleFeeManager;
import com.jlyw.manager.vehicle.LocaleApplianceItemManager;
import com.jlyw.manager.vehicle.LocaleMissionManager;
import com.jlyw.manager.vehicle.VehicleMissionManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class VehicleMissionServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(VehicleMissionServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println(req.getParameter("method"));
		Integer method = Integer.parseInt(req.getParameter("method"));
		VehicleMissionManager locmissmag = new VehicleMissionManager();
		switch (method) {
	
		case 0://查询当前用户的车辆——任务信息（查询的是任务状态为“已分配”的任务的车辆调度信息）
			JSONObject retJSON = new JSONObject();
			try {
				String StartTime = req.getParameter("StartTime");
				String Licence=req.getParameter("Licence");
				String Department=req.getParameter("Department");
				String EndTime = req.getParameter("EndTime");
				String MissionStatus = req.getParameter("MissionStatus");
				if(StartTime==null){
					StartTime="";
				}
				if(EndTime==null){
					EndTime="";
				}
				if(Department==null){
					Department="";
				}
				if(Licence==null){
					Licence="";
				}
				if(MissionStatus==null){
					MissionStatus="1";
				}
				
				Licence=URLDecoder.decode(Licence.trim(),"UTF-8");
				Department=URLDecoder.decode(Department.trim(),"UTF-8");
				int status=Integer.parseInt(MissionStatus);
				
				SysUser sysuser=(SysUser)req.getSession().getAttribute("LOGIN_USER");
				String username=sysuser.getName();	
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				Timestamp Start = null;
				
				
				if(StartTime!=null && StartTime.trim().length() > 0){		
					 Start = Timestamp.valueOf(String.format("%s 00:00:00",StartTime.trim()));				
				}
				else{
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(System.currentTimeMillis());
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					
					Start = new Timestamp(c.getTimeInMillis());// 当前时间
				}
				
				int doneTotal = 0;
				String queryStr = null;
				List<VehicleMission> retList = null;
				if(EndTime != null && EndTime.trim().length() > 0){//结束时间不为空
					if(status==0){//查询登陆员工出车信息
						 queryStr = "from VehicleMission as model where (model.drivingVehicle.sysUserByDriverId.id = ? or (model.drivingVehicle.people like ? or model.drivingVehicle.people like ? or model.drivingVehicle.people = ?)) and (model.drivingVehicle.beginDate >= ? and model.drivingVehicle.endDate <= ? and  (model.localeMission.status=1 or  model.localeMission.status = 2 )) and model.localeMission.department like ? and model.drivingVehicle.vehicle.licence like ?";				
						 String coutSqlStr = "select count(*) "+queryStr;	
						 //Timestamp End = new Timestamp(DateTimeFormatUtil.DateFormat.parse(EndTime.trim()).getTime());
						 Timestamp End = Timestamp.valueOf(String.format(
									"%s 23:59:00", EndTime.trim()));
						 doneTotal = locmissmag.getTotalCountByHQL(coutSqlStr,sysuser.getId(), "%" + username + ";%", "%;" + username + "%", username,  Start, End,"%" + Department + "%","%;" + Licence + "%");
						 retList = locmissmag.findPageAllByHQL(queryStr+" order by model.localeMission.createTime desc", page, rows,sysuser.getId(), "%" + username + ";%", "%;" + username + "%" , username, Start, End,"%" + Department + "%","%" + Licence + "%");
					}else{//查询全部出车信息
						queryStr = "from VehicleMission as model where model.drivingVehicle.beginDate >= ? and model.drivingVehicle.endDate <= ? and  (model.localeMission.status=1 or  model.localeMission.status = 2 )  and model.localeMission.department like ? and model.drivingVehicle.vehicle.licence like ?";				
						 String coutSqlStr = "select count(*) "+queryStr;	
						 Timestamp End = Timestamp.valueOf(String.format(
									"%s 23:59:00", EndTime.trim()));
						
						 doneTotal = locmissmag.getTotalCountByHQL(coutSqlStr,Start, End,"%" + Department + "%","%" + Licence + "%");
						 retList = locmissmag.findPageAllByHQL(queryStr+" order by model.localeMission.createTime desc", page, rows,Start, End,"%" + Department + "%","%" + Licence + "%");
					}
				}else{//结束时间为空
					if(status==0){//查询登陆员工出车信息
						
						queryStr = "from VehicleMission as model where (model.drivingVehicle.sysUserByDriverId.id = ? or (model.drivingVehicle.people like ? or model.drivingVehicle.people like ? or model.drivingVehicle.people = ?)) and (model.drivingVehicle.beginDate >= ? and  (model.localeMission.status=1 or  model.localeMission.status = 2 ) ) and model.localeMission.department like ? and model.drivingVehicle.vehicle.licence like ?";
						String coutSqlStr = "select count(*) "+queryStr;	
						doneTotal = locmissmag.getTotalCountByHQL(coutSqlStr, sysuser.getId(), "%" + username + ";%", "%;" + username + "%", username, Start,"%" + Department + "%","%" + Licence + "%");
						retList = locmissmag.findPageAllByHQL(queryStr+" order by model.localeMission.createTime desc", page, rows,sysuser.getId(), "%" + username + ";%", "%;" + username + "%", username, Start,"%" + Department + "%","%" + Licence + "%");
						
					}else{//查询全部出车信息
						
						queryStr = "from VehicleMission as model where model.drivingVehicle.beginDate >= ? and (model.localeMission.status=1 or  model.localeMission.status = 2 ) and model.localeMission.department like ? and model.drivingVehicle.vehicle.licence like ?";
						String coutSqlStr = "select count(*) "+queryStr;	
						doneTotal = locmissmag.getTotalCountByHQL(coutSqlStr,Start,"%" + Department + "%","%" + Licence + "%");
						retList = locmissmag.findPageAllByHQL(queryStr+" order by model.localeMission.createTime desc", page, rows,Start,"%" + Department + "%","%" + Licence + "%");
					}
					
				}
				
				JSONArray options = new JSONArray();
				
				if(retList !=null && retList.size()>0){
					for(VehicleMission vm : retList){
						JSONObject option = new JSONObject();
						//车辆信息
						option.put("Id", vm.getId());//此Id，即车辆-任务Id
						option.put("DrivingVehicleId",vm.getDrivingVehicle().getId());//
						option.put("Driver", vm.getDrivingVehicle().getSysUserByDriverId().getName());
						option.put("Licence", vm.getDrivingVehicle().getVehicle().getLicence());
						option.put("Limit", vm.getDrivingVehicle().getVehicle().getLimit());
						option.put("Model", vm.getDrivingVehicle().getVehicle().getModel());
						option.put("Brand", vm.getDrivingVehicle().getVehicle().getBrand());
						
						
						//现场任务信息
						option.put("Code", vm.getLocaleMission().getCode());
						option.put("CreatorName", vm.getLocaleMission().getSysUserByCreatorId().getName());
						option.put("CreateDate",DateTimeFormatUtil.DateTimeFormat.format(vm.getLocaleMission().getCreateTime()));
						option.put("Customer", vm.getLocaleMission().getCustomerName());
						option.put("Address", vm.getLocaleMission().getAddress());
						option.put("Contactor", vm.getLocaleMission().getContactor());
						option.put("Tel", vm.getLocaleMission().getTel());
						option.put("ContactorTel", vm.getLocaleMission().getContactorTel());
						
						//option.put("MissionDesc", vm.getLocaleMission().getMissionDesc().replace("\r\n", "<br/>"));
						
						option.put("SiteManagerId", vm.getLocaleMission().getSysUserBySiteManagerId()==null?"":vm.getLocaleMission().getSysUserBySiteManagerId().getName());
						option.put("Department", vm.getLocaleMission().getDepartment());
						//找现场业务下面的各个器具指派人
					
						LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();
						String staffs="";
						String queryString="from LocaleApplianceItem as a where a.localeMission.id = ?";
						List<SysUser>  workStaffs= locAppItemMgr.findByHQL("select distinct a.sysUser "+queryString, vm.getLocaleMission().getId());
						if(!workStaffs.isEmpty()){
							
							for (SysUser user : workStaffs) {
								staffs = staffs + user.getName()+";"; 							
							}
						}						
						option.put("Staffs", staffs);
						//找现场业务下面的各个器具信息
						String missionDesc="";
						List<LocaleApplianceItem>  locAppList= locAppItemMgr.findByHQL(queryString, vm.getLocaleMission().getId());
						if(!locAppList.isEmpty()){						
							for (LocaleApplianceItem locApp : locAppList) {
								missionDesc = missionDesc + locApp.getApplianceName()+"  "+locApp.getQuantity()+"<br/>"; 							
							}
						}
						option.put("MissionDesc", missionDesc);
						option.put("ExactTime", vm.getLocaleMission().getExactTime()==null?"":DateTimeFormatUtil.DateFormat.format(vm.getLocaleMission().getExactTime()));
						
						//其他任务-车辆信息
						SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						option.put("DriverId", vm.getDrivingVehicle().getSysUserByDriverId().getName());
						option.put("BeginDate", DateTimeFormat.format((vm.getDrivingVehicle().getBeginDate())));
						option.put("EndDate", DateTimeFormat.format(vm.getDrivingVehicle().getEndDate()));
						option.put("Description", vm.getDrivingVehicle().getDescription()==null?"":vm.getDrivingVehicle().getDescription());
						option.put("People", vm.getDrivingVehicle().getPeople()==null?"":vm.getDrivingVehicle().getPeople());
						option.put("AssemblingPlace", vm.getDrivingVehicle().getAssemblingPlace()==null?"":vm.getDrivingVehicle().getAssemblingPlace());
						
						option.put("Kilometers", vm.getDrivingVehicle().getKilometers());
	
						options.put(option);
					}
				}
				
				retJSON.put("total", doneTotal);
				retJSON.put("rows", options);
//				retJSON.put("doneTotal", doneTotal);//已完工委托单数
				
			} catch (Exception e){
				
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
//					retJSON.put("doneTotal", 0);
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 0", e);
				}else{
					log.error("error in VehicleMissionServlet-->case 0", e);
				} 
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
				//System.out.println("test3:" + retJSON.toString());
			}
			break;
		case 1: // 删除车辆-任务记录
			JSONObject retJSON2 = new JSONObject();
			try{
				int id = Integer.parseInt(req.getParameter("id"));
				LocaleMission localemission=locmissmag.findById(id).getLocaleMission();
				String licence=locmissmag.findById(id).getDrivingVehicle().getVehicle().getLicence();
				DrivingVehicle driVe=locmissmag.findById(id).getDrivingVehicle();
										
				if(!locmissmag.deleteById(id))
					throw new Exception("删除车辆任务失败！");
				
				List<VehicleMission> vehicleMissionList = locmissmag.findByVarProperty(new KeyValueWithOperator("drivingVehicle",driVe, "="));
				if(vehicleMissionList==null||vehicleMissionList.size()==0){
					if(!(new DrivingVehicleManager()).deleteById(driVe.getId()))
						throw new Exception("删除出车记录失败！");
				}
				
				List<VehicleMission> vehicleMissionList2=locmissmag.findByVarProperty(new KeyValueWithOperator("localeMission", localemission, "="));
				String templisences="";
				if(vehicleMissionList2!=null&&vehicleMissionList2.size()>0){
					for(VehicleMission vemi:vehicleMissionList2){
						if(templisences.indexOf(vemi.getDrivingVehicle().getVehicle().getLicence())<0){
							templisences=templisences+vemi.getDrivingVehicle().getVehicle().getLicence();
						}
					}
				}else{
					localemission.setStatus(5);
				}
				localemission.setVehicleLisences(templisences);
				if(!(new LocaleMissionManager()).update(localemission))
					throw new Exception("更新任务信息失败！");
				
//				if(localemission.getVehicleLisences()!=null&&localemission.getVehicleLisences().length()>0){
//					if(localemission.getVehicleLisences().indexOf(licence)!=-1){
//						int strbegin=localemission.getVehicleLisences().indexOf(licence);
//						StringBuffer abuffer=new StringBuffer(localemission.getVehicleLisences());
//						abuffer.delete(strbegin, strbegin+licence.length()+1);
//						
//						localemission.setVehicleLisences(abuffer.toString());
//						if(abuffer.toString()==null||abuffer.toString().length()==0)
//							localemission.setStatus(5);
//					}
//					if(!(new LocaleMissionManager()).update(localemission))
//						throw new Exception("更新任务信息失败！");
//				}
				
				retJSON2.put("IsOK", true);
			}catch(Exception e){
				try{
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 1", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 2: // 查询出车记录
			JSONObject retJSON3= new JSONObject();
			try{
				String Licence = req.getParameter("License");
				String DriverName = req.getParameter("DriverName");
				String BeginDate = req.getParameter("History_BeginDate");
				String EndDate = req.getParameter("History_EndDate");
				String MissionStatus = req.getParameter("MissionStatus");
				//System.out.println(Licence+" "+DriverName);
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				
				if (Licence != null && Licence.trim().length() > 0) {
					Licence= URLDecoder.decode(Licence.trim(),"UTF-8");
				
					condList.add(new KeyValueWithOperator("vehicle.licence",
							"%"+Licence+"%", "like"));
				}
				if (DriverName != null && DriverName.trim().length() > 0) {
					DriverName = URLDecoder.decode(DriverName.trim(),
							"UTF-8");
					//System.out.println(DriverName);
					condList.add(new KeyValueWithOperator("sysUserByDriverId.name",
							DriverName, "="));
				}
				if (MissionStatus != null && MissionStatus.trim().length() > 0) {
					MissionStatus = URLDecoder.decode(MissionStatus.trim(),
							"UTF-8");
					
					condList.add(new KeyValueWithOperator("status",
							Integer.parseInt(MissionStatus), "="));
				}
				if (BeginDate != null && BeginDate.trim().length() > 0) {
					Timestamp beginTs = Timestamp.valueOf(String.format(
							"%s 00:00:00", BeginDate.trim()));
					condList.add(new KeyValueWithOperator("beginDate",
							beginTs, ">="));
				}
				if (EndDate != null && EndDate.trim().length() > 0) {
					Timestamp endTs = Timestamp.valueOf(String.format(
							"%s 23:59:00", EndDate.trim()));
					condList.add(new KeyValueWithOperator("beginDate",
							endTs, "<="));
				}
				DrivingVehicleManager driVMgr=new DrivingVehicleManager();
				List<DrivingVehicle> result = driVMgr.findPagedAllBySort(page,rows,"beginDate",false, condList);// 
				int total = driVMgr.getTotalCount(condList);//

				JSONArray options = new JSONArray();
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				for (DrivingVehicle loc : result) {
					
						JSONObject option = new JSONObject();
						option.put("Id", loc.getId());
						option.put("DriverName", loc.getSysUserByDriverId().getName());//司机
						option.put("VehicleLicence", loc.getVehicle().getLicence());						
						option.put("BeginDate", loc.getBeginDate()==null?"":DateTimeFormatUtil.DateTimeFormat.format(loc.getBeginDate()));		
						option.put("EndDate", loc.getEndDate()==null?"":DateTimeFormatUtil.DateTimeFormat.format(loc.getEndDate()));	
						option.put("AssemblingPlace", loc.getAssemblingPlace()==null?"":loc.getAssemblingPlace());	
						option.put("Description", loc.getDescription()==null?"":loc.getDescription());		
						option.put("Kilometers", loc.getKilometers()==null?"":loc.getKilometers());
						option.put("TotalFee", loc.getTotalFee()==null?"":loc.getTotalFee());
						option.put("People", loc.getPeople()==null?"":loc.getPeople());
						option.put("Status", loc.getStatus());
						option.put("SettlementTime", loc.getSettlementTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(loc.getSettlementTime()));		
						option.put("SettlementName", loc.getSysUserBySettlementId()==null?"":loc.getSysUserBySettlementId().getName());		
						options.put(option);
				}
				retJSON3.put("total", total);
				retJSON3.put("rows", options);
			}catch(Exception e){
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException ex) {
				}			
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 2", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 3: // 增加一条交通费用的分配
			JSONObject retJSON4= new JSONObject();
			try{
				String DrivingVehicleId = req.getParameter("DrivingVehicleId");
				String StaffId = req.getParameter("StaffId");
				String StaffFee = req.getParameter("StaffFee");
				
				DrivingVehicleManager driVMgr=new DrivingVehicleManager();
				VehicleFeeManager vefeeMgr=new VehicleFeeManager();
				DrivingVehicle driveVe=driVMgr.findById(Integer.parseInt(DrivingVehicleId));
				VehicleFee vehiclefee=new VehicleFee(); 
				SysUser user=new SysUser();
				user.setId(Integer.parseInt(StaffId));
				
				vehiclefee.setDrivingVehicle(driveVe);	
				//vehiclefee.setStatus(0);		
				vehiclefee.setSysUser(user);
				vehiclefee.setFee(Double.parseDouble(StaffFee));
				if(!vefeeMgr.save(vehiclefee))
					throw new Exception("保存费用分配信息失败！");
				retJSON4.put("IsOK", true);
			}catch(Exception e){
				try{
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 3", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 4: // 查询一条出车记录的费用分配信息
			JSONObject retJSON5= new JSONObject();
			try{
				String DrivingVehicleId = req.getParameter("DrivingVehicleId");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				
				if (DrivingVehicleId != null && DrivingVehicleId.trim().length() > 0) {
					int drivingVehicleId= Integer.parseInt(URLDecoder.decode(DrivingVehicleId.trim(),"UTF-8"));
				
					condList.add(new KeyValueWithOperator("drivingVehicle.id",
							drivingVehicleId, "="));
				}
				
				VehicleFeeManager veFeeMgr=new VehicleFeeManager();
				List<VehicleFee> result = veFeeMgr.findPagedAll(page,rows, condList);// 
				int total = veFeeMgr.getTotalCount(condList);//

				JSONArray options = new JSONArray();
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				for (VehicleFee loc : result) {
					
						JSONObject option = new JSONObject();
						option.put("Id", loc.getId());
						option.put("DrivingVehicleId", loc.getDrivingVehicle().getId());//
						option.put("StaffName", loc.getSysUser().getName());						
						option.put("StaffFee", loc.getFee());		
						option.put("Remark", loc.getRemark()==null?"":loc.getRemark());	
						
						options.put(option);
				}
				retJSON5.put("total", total);
				retJSON5.put("rows", options);
				
			}catch(Exception e){
				try {
					retJSON5.put("total", 0);
					retJSON5.put("rows", new JSONArray());
				} catch (JSONException ex) {
				}			
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 4", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 5: // 删除车辆-任务记录
			JSONObject retJSON6 = new JSONObject();
			try{
				int id = Integer.parseInt(req.getParameter("Id"));
				VehicleFeeManager veFeeMgr=new VehicleFeeManager();
				if(!veFeeMgr.deleteById(id))
					throw new Exception("删除失败！");
				retJSON6.put("IsOK", true);
			}catch(Exception e){
				try{
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 5", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 6: // 保存司机录入的里程数，总交通费用
			JSONObject retJSON7 = new JSONObject();
			try{
				String DrivingVehicleId = req.getParameter("DrivingVehicleId").trim();
				String Kilometers = req.getParameter("Kilometers");
				String TotalFee = req.getParameter("TotalFee");
				
				DrivingVehicleManager driVeMgr=new DrivingVehicleManager();
				DrivingVehicle driVe=driVeMgr.findById(Integer.parseInt(DrivingVehicleId));
				//System.out.println(driVe);
				driVe.setKilometers(Double.parseDouble(Kilometers));
				driVe.setTotalFee(Double.parseDouble(TotalFee));
				if(!driVeMgr.update(driVe))
					throw new Exception("出车记录更新失败！");

				retJSON7.put("IsOK", true);
			}catch(Exception e){
				try{
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 6", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		case 7://结算出车记录
			JSONObject retJSON8 = new JSONObject();
			
			try{
				
				String recordIds = req.getParameter("recordIds");
				
//				String[] Ids = recordIds.split("\\|");				
				int m;	
				
				List<DrivingVehicle> DrivingVehicleList=new ArrayList();
				DrivingVehicleManager driVeMgr=new DrivingVehicleManager();
//				for(m = 0;m < Ids.length; m++)
//				{
//					if(Ids[m]!=null&&Ids[m].length()>0){
//						DrivingVehicle drivingVehicle = driVeMgr.findById(Integer.parseInt(Ids[m]));
//						drivingVehicle.setStatus(1);
//						Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
//						drivingVehicle.setSettlementTime(now);
//						drivingVehicle.setSysUserBySettlementId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
//						DrivingVehicleList.add(drivingVehicle);
//					}
//				}
				JSONObject params = new JSONObject(recordIds);
				String queryStr = "from DrivingVehicle model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String Licence = params.getString("License");
					String DriverName = params.getString("DriverName");
					String BeginDate = params.getString("History_BeginDate");
					String EndDate = params.getString("History_EndDate");
					String MissionStatus = params.getString("MissionStatus");
					
					if (Licence != null && Licence.trim().length() > 0) {
						Licence= URLDecoder.decode(Licence.trim(),"UTF-8");
						queryStr = queryStr + " and model.vehicle.licence like ?";
						
						keys.add("%" + Licence + "%");
					}
					if (DriverName != null && DriverName.trim().length() > 0) {
						DriverName = URLDecoder.decode(DriverName.trim(),
								"UTF-8");
						queryStr = queryStr + " and model.sysUser.name = ?";
						
						keys.add(DriverName);
						
					}
					if (MissionStatus != null && MissionStatus.trim().length() > 0) {
						MissionStatus = URLDecoder.decode(MissionStatus.trim(),
								"UTF-8");
						queryStr = queryStr + " and model.status = ?";
						
						keys.add(MissionStatus);
						
					}
					if (BeginDate != null && BeginDate.trim().length() > 0) {
						Timestamp beginTs = Timestamp.valueOf(String.format(
								"%s 00:00:00", BeginDate.trim()));
						queryStr = queryStr + " and model.beginDate >= ?";
						
						keys.add(beginTs);
					}
					if (EndDate != null && EndDate.trim().length() > 0) {
						Timestamp endTs = Timestamp.valueOf(String.format(
								"%s 23:59:00", EndDate.trim()));
						
						queryStr = queryStr + " and model.beginDate <= ?";
						
						keys.add(endTs);
					}
					
				}
				//BaseHibernateDAO dao = new BaseHibernateDAO();
				List<DrivingVehicle> result = driVeMgr.findAllByHQL(queryStr,keys);
				Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
				if(result!=null&&result.size()>0){
					for (DrivingVehicle drivingVehicle : result) {					
						drivingVehicle.setStatus(1);
						drivingVehicle.setSettlementTime(now);
						drivingVehicle.setSysUserBySettlementId((SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
						DrivingVehicleList.add(drivingVehicle);
					}
				}
				if(!(driVeMgr.updateByBatch(DrivingVehicleList))){
					throw new Exception("保存到数据库失败！");
				}
				retJSON8.put("IsOK", true);
			}catch(Exception e){
				
				try{
					retJSON8.put("IsOK", false);
					retJSON8.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 7", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 7", e);
				}
			}finally{
			
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON8.toString());
				
			}
			break;
		case 8://出车记录导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj8 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from DrivingVehicle model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String Licence = params.getString("License");
					String DriverName = params.getString("DriverName");
					String BeginDate = params.getString("History_BeginDate");
					String EndDate = params.getString("History_EndDate");
					String MissionStatus = params.getString("MissionStatus");
					
					if (Licence != null && Licence.trim().length() > 0) {
						Licence= URLDecoder.decode(Licence.trim(),"UTF-8");
						queryStr = queryStr + " and model.vehicle.licence like ?";
						
						keys.add("%" + Licence + "%");
					}
					if (DriverName != null && DriverName.trim().length() > 0) {
						DriverName = URLDecoder.decode(DriverName.trim(),
								"UTF-8");
						queryStr = queryStr + " and model.sysUser.name = ?";
						
						keys.add(DriverName);
						
					}
					if (MissionStatus != null && MissionStatus.trim().length() > 0) {
						MissionStatus = URLDecoder.decode(MissionStatus.trim(),
								"UTF-8");
						queryStr = queryStr + " and model.status = ?";
						
						keys.add(MissionStatus);
						
					}
					if (BeginDate != null && BeginDate.trim().length() > 0) {
						Timestamp beginTs = Timestamp.valueOf(String.format(
								"%s 00:00:00", BeginDate.trim()));
						queryStr = queryStr + " and model.beginDate >= ?";
						
						keys.add(beginTs);
					}
					if (EndDate != null && EndDate.trim().length() > 0) {
						Timestamp endTs = Timestamp.valueOf(String.format(
								"%s 23:59:00", EndDate.trim()));
						
						queryStr = queryStr + " and model.beginDate <= ?";
						
						keys.add(endTs);
					}
					
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", DrivingVehicleManager.class);				
				retObj8.put("IsOK", filePath.equals("")?false:true);
				retObj8.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 8", e);
				}else{
					log.error("error in VehicleMissionServlet-->case 8", e);
				}
				try {
					retObj8.put("IsOK", false);
					retObj8.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj8.toString());
			}
			break;
		case 9://出车费用承担明细导出
			String paramsStr9 = req.getParameter("paramsStr");
			JSONObject retObj9 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr9);
				String queryStr = "from VehicleFee model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String Licence = params.getString("License");
					String DriverName = params.getString("DriverName");
					String BeginDate = params.getString("History_BeginDate");
					String EndDate = params.getString("History_EndDate");
					String MissionStatus = params.getString("MissionStatus");
					
					if (Licence != null && Licence.trim().length() > 0) {
						Licence= URLDecoder.decode(Licence.trim(),"UTF-8");
						queryStr = queryStr + " and model.drivingVehicle.vehicle.licence like ?";
						
						keys.add("%" + Licence + "%");
					}
					if (DriverName != null && DriverName.trim().length() > 0) {
						DriverName = URLDecoder.decode(DriverName.trim(),
								"UTF-8");
						queryStr = queryStr + " and model.drivingVehicle.sysUser.name = ?";
						
						keys.add(DriverName);
						
					}
					if (MissionStatus != null && MissionStatus.trim().length() > 0) {
						MissionStatus = URLDecoder.decode(MissionStatus.trim(),
								"UTF-8");
						queryStr = queryStr + " and model.drivingVehicle.status = ?";
						
						keys.add(MissionStatus);
						
					}
					if (BeginDate != null && BeginDate.trim().length() > 0) {
						Timestamp beginTs = Timestamp.valueOf(String.format(
								"%s 00:00:00", BeginDate.trim()));
						queryStr = queryStr + " and model.drivingVehicle.beginDate >= ?";
						
						keys.add(beginTs);
					}
					if (EndDate != null && EndDate.trim().length() > 0) {
						Timestamp endTs = Timestamp.valueOf(String.format(
								"%s 23:59:00", EndDate.trim()));
						
						queryStr = queryStr + " and model.drivingVehicle.beginDate <= ?";
						
						keys.add(endTs);
					}
					queryStr = queryStr + " order by model.drivingVehicle";
					
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", VehicleFeeManager.class);				
				retObj9.put("IsOK", filePath.equals("")?false:true);
				retObj9.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 9", e);
				}else{
					log.error("error in VehicleMissionServlet-->case 9", e);
				}
				try {
					retObj9.put("IsOK", false);
					retObj9.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj9.toString());
			}
			break;
		case 10://出车记录结算状态转为未结算
			JSONObject retJSON10 = new JSONObject();
			
			try{
				
				String recordIds = req.getParameter("recordIds");
				
//				String[] Ids = recordIds.split("\\|");				
				int m;	
				
				List<DrivingVehicle> DrivingVehicleList=new ArrayList();
				DrivingVehicleManager driVeMgr=new DrivingVehicleManager();

				
				DrivingVehicle result = driVeMgr.findById(Integer.parseInt(recordIds));
				Timestamp now = new Timestamp(System.currentTimeMillis());// 取当前时间
				if(result!=null){
									
					result.setStatus(0);
					result.setSettlementTime(null);
					result.setSysUserBySettlementId(null);
					
				}
				if(!(driVeMgr.update(result))){
					throw new Exception("保存到数据库失败！");
				}
				retJSON10.put("IsOK", true);
			}catch(Exception e){
				
				try{
					retJSON10.put("IsOK", false);
					retJSON10.put("msg", String.format("处理失败！错误信息：%s",(e != null && e.getMessage() != null)?e.getMessage() : "无"));
				}catch(Exception ex){}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 10", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 10", e);
				}
			}finally{
			
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
				
			}
			break;
		case 11:// 根据出车记录Id查询现场业务(出车费用分配中使用)
			JSONObject res = new JSONObject();
			try {
				String DrivingVehicleId = req.getParameter("DrivingVehicleId");
				
				if (DrivingVehicleId == null) {// 避免NullPointerException
					DrivingVehicleId = "";
				}
			
				int page = 1;// 查询页码
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 10;// 每页显示10行
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());

				String queryStr = "from VehicleMission as model where model.localeMission.status <> 3 ";
				List<Object> keys = new ArrayList<Object>();
				//List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				//condList.add(new KeyValueWithOperator("status", 3, "<>"));//注销
				
				if (DrivingVehicleId != null && DrivingVehicleId.trim().length() > 0) {
					DrivingVehicleId = URLDecoder.decode(DrivingVehicleId.trim(),
							"UTF-8");
					
					//condList.add(new KeyValueWithOperator("customerName",Name, "="));
					queryStr=queryStr+" and model.drivingVehicle.id = ?";
					keys.add(Integer.parseInt(DrivingVehicleId));
				}
				
				List<LocaleMission> result = locmissmag.findPageAllByHQL("select model.localeMission "+queryStr+" order by model.localeMission.createTime desc",page,rows, keys);// 查询未被删除的任务
				int total = locmissmag.getTotalCountByHQL("select count(model.localeMission)"+queryStr,keys);// 未被删除的任务总数

				LocaleApplianceItemManager locAppItemMgr = new LocaleApplianceItemManager();
				JSONArray options = new JSONArray();
				Timestamp today = new Timestamp(System.currentTimeMillis());// 取当前时间
				for (LocaleMission loc : result) {
					// System.out.println("loc.getStatus():"+loc.getStatus());
					
						JSONObject option = new JSONObject();
						option.put("Id", loc.getId());
						option.put("Name", loc.getCustomerName());
						option.put("Code", loc.getCode());
						option.put("CustomerId", loc.getCustomer().getId());
						option.put("CreatorName", loc.getSysUserByCreatorId().getName());
						option.put("CreateDate",DateTimeFormatUtil.DateTimeFormat.format(loc.getCreateTime()));
						option.put("ZipCode", loc.getZipCode());
						option.put("Brief", loc.getBrief());
						option.put("RegionId", loc.getRegion().getId());
						option.put("Region", loc.getRegion().getName());
						option.put("Address", loc.getAddress());
						option.put("Remark", loc.getRemark()==null?"":loc.getRemark());
						option.put("Tel", loc.getTel());
						option.put("ZipCode", loc.getZipCode());
						option.put("Status", loc.getStatus());
						option.put("Contactor", loc.getContactor());
						option.put("ContactorTel", loc.getContactorTel());
						option.put("Department", loc.getDepartment());
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						option.put("ExactTime", loc.getExactTime()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getExactTime()));
						//option.put("MissionDesc", loc.getMissionDesc());
						option.put("ModificatorDate", loc.getModificatorDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getModificatorDate()));
						option.put("ModificatorId", loc.getSysUserByModificatorId().getName());
						option.put("SiteManagerName", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getName());
						option.put("SiteManagerId", loc.getSysUserBySiteManagerId()==null?"":loc.getSysUserBySiteManagerId().getId());
						//找现场业务下面的各个器具指派人
						String staffs="";
						String queryString="from LocaleApplianceItem as a where a.localeMission.id = ?";
						List<SysUser>  workStaffs= locAppItemMgr.findByHQL("select distinct a.sysUser "+queryString, loc.getId());
						if(!workStaffs.isEmpty()){						
							for (SysUser user : workStaffs) {
								
								staffs = staffs + user.getName()+";"; 		
								
							}
						}
						if(loc.getSysUserBySiteManagerId()!=null){
							if(staffs.indexOf(loc.getSysUserBySiteManagerId().getName()+";")<0){
								staffs = staffs + loc.getSysUserBySiteManagerId().getName()+";"; 		
							}
						}
						//找现场业务下面的各个器具信息
						String missionDesc="";
						List<LocaleApplianceItem>  locAppList= locAppItemMgr.findByHQL(queryString, loc.getId());
						if(!locAppList.isEmpty()){						
							for (LocaleApplianceItem locApp : locAppList) {
								missionDesc = missionDesc + locApp.getApplianceName()+"  "+locApp.getQuantity()+"<br/>"; 							
							}
						}
						
						option.put("Staffs", staffs);
						option.put("VehicleLisences", loc.getVehicleLisences()==null?"":loc.getVehicleLisences());
						option.put("MissionDesc", missionDesc);
						
						//option.put("Status", loc.getStatus());
						option.put("Tag", "0");//已核定
						option.put("Feedback", loc.getFeedback()==null?"":loc.getFeedback());//反馈
						option.put("CheckDate", loc.getCheckDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getCheckDate()));//核定时间
						option.put("TentativeDate", loc.getTentativeDate()==null?"":DateTimeFormatUtil.DateFormat.format(loc.getTentativeDate()));
						if(loc.getStatus()==4&&loc.getTentativeDate()!=null){  //未核定
							if(loc.getTentativeDate().after(today)){//已到暂定日期可是未安排
								option.put("Tag", "1");//未核定；；；已到暂定日期可是未安排
							}else{
								option.put("Tag", "2");//未核定；；；未到暂定日期
								
							}
								
						}	
						options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				
				try {
					res.put("total", 0);
					res.put("rows", new JSONArray());
				} catch (JSONException ex) {
				}
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in VehicleMissionServlet-->case 11", e);
				} else {
					log.error("error in VehicleMissionServlet-->case 11", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				
				resp.getWriter().write(res.toString());
			}

			break;
		
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(req, resp);
	}

}
