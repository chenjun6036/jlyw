package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
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

import com.jlyw.hibernate.AppStdNameProTeam;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.TaskAssign;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.InformationManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.TaskAssignManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

/**
 * 任务分配
 * @author Administrator
 *
 */
public class TaskAssignServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(TaskAssignServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		TaskAssignManager tAssignMgr = new TaskAssignManager();
		switch (method) {
		case 0: // 查找一个委托单的任务分配信息
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
				List<TaskAssign> taskRetList = tAssignMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), new KeyValueWithOperator("status", 1, "<>"));
				if(taskRetList != null && taskRetList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(TaskAssign t:taskRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("TaskId", t.getId());
						jsonObj.put("AppStdNameProTeamId", t.getAppStdNameProTeam()==null?"-1":t.getAppStdNameProTeam().getId());
						jsonObj.put("AppStdNameProTeamName", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName() == null)?"":t.getAppStdNameProTeam().getProjectName());
						jsonObj.put("AlloteeId", t.getSysUserByAlloteeId().getId());
						jsonObj.put("AlloteeName", t.getSysUserByAlloteeId().getName());
						jsonObj.put("AssignerId", t.getSysUserByAssignerId()==null?"":t.getSysUserByAssignerId().getId());
						jsonObj.put("AssignerName", t.getSysUserByAssignerId()==null?"":t.getSysUserByAssignerId().getName());
						jsonObj.put("AssignTime", DateTimeFormatUtil.DateTimeFormat.format(t.getAssignTime()));
						jsonObj.put("LastFeeAssignerId", t.getSysUserByLastFeeAssignerId()==null?"":t.getSysUserByLastFeeAssignerId().getId());
						jsonObj.put("LastFeeAssignerName", t.getSysUserByLastFeeAssignerId()==null?"":t.getSysUserByLastFeeAssignerId().getName());
						jsonObj.put("LastFeeAssignTime", t.getLastFeeAssignTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(t.getLastFeeAssignTime()));
						
						//费用信息
						jsonObj.put("TestFee", t.getTestFee()==null?"":t.getTestFee());	//检测费
						jsonObj.put("RepairFee", t.getRepairFee()==null?"":t.getRepairFee());	//修理费
						jsonObj.put("MaterialFee", t.getMaterialFee()==null?"":t.getMaterialFee());	//材料费
						jsonObj.put("CarFee", t.getCarFee()==null?"":t.getCarFee());	//交通费
						jsonObj.put("DebugFee", t.getDebugFee()==null?"":t.getDebugFee());	//调试费
						jsonObj.put("OtherFee", t.getOtherFee()==null?"":t.getOtherFee());	//其他费用
						jsonObj.put("TotalFee", t.getTotalFee()==null?"":t.getTotalFee());	//总费用
						
						jsonArray.put(jsonObj);
					}
					
					retJSON.put("total", taskRetList.size());
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
					log.debug("exception in TaskAssignServlet-->case 0", e);
				}else{
					log.error("error in TaskAssignServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//注销任务（有权限的人均可以注销已分配的任务）
			JSONObject retJSON1 = new JSONObject();
			try {
				String TaskId = req.getParameter("TaskId");	//委托单Id
				
				if(TaskId == null || TaskId.trim().length() == 0){
					throw new Exception("任务不存在！");
				}
				TaskAssign t = tAssignMgr.findById(Integer.parseInt(TaskId));
				if(t != null){
					//判断委托单的状态
					if(t.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
							t.getCommissionSheet().getStatus() == 4 ||	//已结账
							t.getCommissionSheet().getStatus() == 9){		//已结束
						throw new Exception("该委托单已完工，不能注销该任务！");
					}
					
					t.setStatus(1);	//注销
					if(tAssignMgr.update(t)){
						retJSON1.put("IsOK", true);
//						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_TaskReceived, String.format("检验任务已取消，委托单号：%s", t.getCommissionSheet().getCode()), t.getSysUserByAlloteeId(), FlagUtil.SmsAndInfomationType.Type_TaskCancel);	//即时消息
						return;
					}else{
						throw new Exception("更新数据库失败！");
					}
				}
				throw new Exception("该任务不存在！");
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("注销任务失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 1", e);
				}else{
					log.error("error in TaskAssignServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//查询委托单对应的检验项目
			JSONArray retJSONArray2 = new JSONArray();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionId));
				if(cSheet == null){
					throw new Exception("该委托单不存在！");
				}else{
					//预留一个空的检验项目:查所有有资质的人
					JSONObject jsonObjTemp = new JSONObject();
					jsonObjTemp.put("appStdProId", "");	//检验项目Id
					jsonObjTemp.put("appStdNameId", cSheet.getApplianceSpeciesId());	//标准名称ID或分类名称ID
					jsonObjTemp.put("appStdProName", "默认");	//检验项目名称
					jsonObjTemp.put("speciesType", cSheet.getSpeciesType()?1:0);	//分类Or标准名称
					retJSONArray2.put(jsonObjTemp);	
					
					List<Object[]> retList = tAssignMgr.getInspectProjects(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0);
					for(Object[] tempArray : retList){
						JSONObject jsonObj2 = new JSONObject();
						jsonObj2.put("appStdProId", (Integer)tempArray[0]);	//检验项目Id
						jsonObj2.put("appStdNameId", (Integer)tempArray[1]);	//标准名称ID
						jsonObj2.put("appStdProName", (String)tempArray[2]);	//检验项目名称
						jsonObj2.put("speciesType", 0);	//标准名称
						retJSONArray2.put(jsonObj2);
					}
					
/*					
					
//					ViewAppProMappingManager vMgr = new ViewAppProMappingManager();
					List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
					condList.add(new KeyValueWithOperator("proTeamStatus", 0, "="));	//项目组状态：正常
					if(cSheet.getSpeciesType()){	//分类名称
						condList.add(new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
					}else{		//标准名称
						condList.add(new KeyValueWithOperator("id.appStaNameId", cSheet.getApplianceSpeciesId(), "="));
					}
					List<ViewApplianceSpecialStandardNameProject> vRetList = vMgr.findByVarProperty(condList);
					Map<Integer, List<Object[]>> map = new HashMap<Integer, List<Object[]>>();
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_YEAR, 1);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					Timestamp tYear = new Timestamp(c.getTimeInMillis()); //今年的开始时间
					for(ViewApplianceSpecialStandardNameProject v : vRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("appStdProId", v.getId().getAppStdProId());	//检验项目Id
						jsonObj.put("appStdProName", v.getAppStdProName());	//检验项目名称
						Integer ProTeamId = v.getId().getProTeamId();
						List<Object[]> uList = map.get(ProTeamId);
						if(uList == null){
							if(SystemCfgUtil.getTaskAllotRule() == 0){	//按业务量排序
								String queryString = "select u.id as field1, u.name as field2, (select count(*) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.AssignTime>=? and t.AlloteeId=u.id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
										" from "+SystemCfgUtil.DBPrexName+"SysUser as u" +
										" where u.projectTeamId=? and u.status=0" +
										" order by field3 asc ";
								
								uList = tAssignMgr.findBySQL(queryString, tYear, ProTeamId);
							}else{	//按产值排序
								String queryString = "select u.id as field1, u.name as field2, (select sum(o.totalFee) from " + SystemCfgUtil.DBPrexName+ "TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c,"+ SystemCfgUtil.DBPrexName+"OriginalRecord as o where t.finishTime>=? and t.AlloteeId=u.id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 and t.id=o.TaskAssignId and o.status<>1 ) as field3 " +
									" from "+SystemCfgUtil.DBPrexName+"SysUser as u" +
									" where u.projectTeamId=? and u.status=0" +
									" order by field3 asc ";
								uList = tAssignMgr.findBySQL(queryString, tYear, ProTeamId);
							}
							if(uList != null){
								map.put(ProTeamId, uList);
							}
						}
						JSONArray jsonArrayUser = new JSONArray();
						for(Object[] user : uList){
							JSONObject jsonObjUser = new JSONObject();
							jsonObjUser.put("id", (Integer)user[0]);
							jsonObjUser.put("name", (String)user[1]);
							jsonArrayUser.put(jsonObjUser);
						}
						jsonObj.put("AlloteeList", jsonArrayUser);
						
						retJSONArray2.put(jsonObj);
					}
					
*/					
				}
			} catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 2", e);
				}else{
					log.error("error in TaskAssignServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSONArray2.toString());
			}
			break;
		case 3:	//分配任务
			JSONObject retJSON3 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//委托单Id
				String ProjectSelect = req.getParameter("ProjectSelect");	//检验项目Id
				String AlloteeSelect = req.getParameter("AlloteeSelect");	//检验人员Id
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
//				if(ProjectSelect == null || ProjectSelect.trim().length() == 0){
//					throw new Exception("检验项目为空！");
//				}
				if(AlloteeSelect == null || AlloteeSelect.trim().length() == 0){
					throw new Exception("检验人员为空！");
				}
				SysUser user = new UserManager().findById(Integer.parseInt(AlloteeSelect.trim()));
				if(user == null){
					throw new Exception("检验人员不存在或不可用！");
				}
				int totalCount = tAssignMgr.getTotalCount(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionSheetId), "="),
//						new KeyValueWithOperator("appStdNameProTeam.id", Integer.parseInt(ProjectSelect), "="),
						new KeyValueWithOperator("sysUserByAlloteeId.id", user.getId(), "="),
						new KeyValueWithOperator("status", 1, "<>"));	//不是注销的任务
				if(totalCount > 0){
					throw new Exception(String.format("该委托单已分配给检验人员'%s'，一个委托单不能重复分配给同一个人。如需重新分配，请先注销已分配的任务！", user.getName()));
				}
				//存任务分配表
				TaskAssign t = new TaskAssign();
				t.setSysUserByAssignerId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//分配人
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				//判断委托单的状态
				if(cSheet.getStatus() == 3 ||			//已完工（完工确认后）
						cSheet.getStatus() == 4 ||	//已结账
						cSheet.getStatus() == 9){		//已结束
					throw new Exception("该委托单已完工，不能分配该任务！");
				}
				
				t.setCommissionSheet(cSheet);	//委托单
				if(ProjectSelect != null && ProjectSelect.trim().length() > 0){
					AppStdNameProTeam aspt = new AppStdNameProTeam();
					aspt.setId(Integer.parseInt(ProjectSelect));
					t.setAppStdNameProTeam(aspt);	//检验项目
				}
				t.setSysUserByAlloteeId(user);	//任务接收人
				t.setAssignTime(new Timestamp(System.currentTimeMillis()));	//分配时间
				t.setStatus(0);	//任务状态：正常
				if(tAssignMgr.save(t)){
					retJSON3.put("IsOK", true);
					InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_TaskReceived, String.format("收到一个新的检验任务，委托单号：%s", cSheet.getCode()), user, FlagUtil.SmsAndInfomationType.Type_TaskReceived);	//即时消息
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("分配任务失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 3", e);
				}else{
					log.error("error in TaskAssignServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4:	//查询当前登录用户的任务列表
			JSONObject retJSON4 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String CommissionNumber = req.getParameter("CommissionNumber");	//委托单号
				String CustomerName = req.getParameter("CustomerName");	//委托单位
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
				if(CommissionNumber != null && CommissionNumber.trim().length() > 0){
					CommissionNumber = URLDecoder.decode(CommissionNumber.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("commissionSheet.code", CommissionNumber, "="));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//委托单尚未注销的
				condList.add(new KeyValueWithOperator("sysUserByAlloteeId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //检验人
				condList.add(new KeyValueWithOperator("status", 1, "<>"));  //任务没有注销的
				condList.add(new KeyValueWithOperator("finishTime", null, "is null"));	//任务尚未完成的
				String hqlQueryString_RecordQuantity = "select count(*) from OriginalRecord as model where model.commissionSheet.id = ? and model.status <> 1 and model.taskAssign.status <> 1";
				String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录的器具总数(签字已通过且不是正在后台执行)
				String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//已批准的退样器具数量
				String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
				
				int total = tAssignMgr.getTotalCount(condList);
				List<TaskAssign> tRetList = tAssignMgr.findPagedAllBySort(page, rows, "assignTime", true, condList);
				for(TaskAssign t : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("TaskId", t.getId());
					jsonObj.put("CommissionSheetId", t.getCommissionSheet().getId());
					jsonObj.put("ProjectName", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName()==null)?"":t.getAppStdNameProTeam().getProjectName());	//检验项目名称
					jsonObj.put("Remark", t.getRemark()==null?"":t.getRemark());	//任务备注
					jsonObj.put("AlloteeName", t.getSysUserByAlloteeId().getName());	//任务接收人名字
					jsonObj.put("AssignTime", DateTimeFormatUtil.DateTimeFormat.format(t.getAssignTime()));	//任务分配时间
					jsonObj.put("CommissionCode", t.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", t.getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", t.getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("Quantity", t.getCommissionSheet().getQuantity());	//器具数量
					List<Long> oQuantityList = tAssignMgr.findByHQL(hqlQueryString_RecordQuantity, t.getCommissionSheet().getId());
					if(oQuantityList != null && oQuantityList.size() > 0 && oQuantityList.get(0) != null){
						jsonObj.put("RecordQuantity", oQuantityList.get(0));	//完工器具数量
					}else{
						jsonObj.put("RecordQuantity", 0);
					}
					List<Long> fQuantityList = tAssignMgr.findByHQL(hqlQueryString_FinishQuantity, t.getCommissionSheet().getId(), true);	//完工器具数量
					if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
						jsonObj.put("FinishQuantity", fQuantityList.get(0));	//完工器具数量
					}else{
						jsonObj.put("FinishQuantity", 0);
					}
					List<Long> wQuantityList = tAssignMgr.findByHQL(hqlQueryString_WithdrawQuantity, t.getCommissionSheet().getId(), true);	//退样器具数量
					if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
						jsonObj.put("EffectQuantity", t.getCommissionSheet().getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//有效器具数量
					}else{
						jsonObj.put("EffectQuantity", t.getCommissionSheet().getQuantity());
					}
					jsonObj.put("CustomerName", t.getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CustomerContactor", t.getCommissionSheet().getCustomerContactor());	//委托单位联系人
					jsonObj.put("CustomerContactorTel", t.getCommissionSheet().getCustomerContactorTel());	//委托单位联系人电话
					jsonObj.put("Urgent", t.getCommissionSheet().getUrgent()?1:0);	//是否加急
					Timestamp CommissionDate = (t.getCommissionSheet().getCommissionType()==2 && t.getCommissionSheet().getLocaleCommissionDate()!=null)?t.getCommissionSheet().getLocaleCommissionDate():t.getCommissionSheet().getCommissionDate();
					Timestamp PromiseDate = t.getCommissionSheet().getPromiseDate()==null?null:new Timestamp(t.getCommissionSheet().getPromiseDate().getTime());
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(CommissionDate));	//委托时间
					Timestamp today = new Timestamp(System.currentTimeMillis());
					if(PromiseDate != null){
						jsonObj.put("PromiseDate", DateTimeFormatUtil.DateFormat.format(PromiseDate));	//承诺检出日期
						if(today.after(PromiseDate)){
							jsonObj.put("IsOverdue", true);	//是否超期
							jsonObj.put("OverdueDays", DateTimeFormatUtil.daysOfTwo(today, PromiseDate));	//超期的天数
						}else{
							jsonObj.put("IsOverdue", false);
							if(DateTimeFormatUtil.daysOfTwo(today, PromiseDate) <= SystemCfgUtil.OverdueThreSholdShort){	//超期预警（短期）
								jsonObj.put("IsOverdueWarningShort", true);	//短期预警
								jsonObj.put("IsOverdueWarningLong", true);	//长期预警
								jsonObj.put("OverdueWarningDays", DateTimeFormatUtil.daysOfTwo(today, PromiseDate));	//距超期还有几天
							}else if(DateTimeFormatUtil.daysOfTwo(today, PromiseDate) <= SystemCfgUtil.OverdueThresholdLong){	//超期预警（长期）
								jsonObj.put("IsOverdueWarningShort", false);	//短期预警
								jsonObj.put("IsOverdueWarningLong", true);	//长期预警
								jsonObj.put("OverdueWarningDays", DateTimeFormatUtil.daysOfTwo(today, PromiseDate));	//距超期还有几天
							}else{
								jsonObj.put("IsOverdueWarningShort", false);	//短期预警
								jsonObj.put("IsOverdueWarningLong", false);	//长期预警
							}
						}
					}else{
						jsonObj.put("PromiseDate", "");	//承诺检出日期
						jsonObj.put("IsOverdue", false);	//是否超期
						jsonObj.put("IsOverdueWarningShort", false);	//短期预警
						jsonObj.put("IsOverdueWarningLong", false);	//长期预警
					}
					
					jsonArray.put(jsonObj);
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
					log.debug("exception in TaskAssignServlet-->case 4", e);
				}else{
					log.error("error in TaskAssignServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	// 查找一份委托单的任务分配信息（用于TaskManage/ComSheetInspectByCode.jsp-通过委托单号和密码进行委托单检验   中检验项目的选择——ComboBox）
			JSONArray jsonArray5 = new JSONArray();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
				List<TaskAssign> taskRetList = tAssignMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), new KeyValueWithOperator("status", 1, "<>"));
				if(taskRetList != null && taskRetList.size() > 0){
					for(TaskAssign t:taskRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("id", t.getId());
						jsonObj.put("text", String.format("%s[分配给:%s]", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName() == null)?"":t.getAppStdNameProTeam().getProjectName(), t.getSysUserByAlloteeId().getName()));
						jsonObj.put("AppStdNameProTeamId", t.getAppStdNameProTeam()==null?"-1":t.getAppStdNameProTeam().getId());
						jsonObj.put("AppStdNameProTeamName", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName() == null)?"":t.getAppStdNameProTeam().getProjectName());
						jsonObj.put("AlloteeId", t.getSysUserByAlloteeId().getId());
						jsonObj.put("AlloteeName", t.getSysUserByAlloteeId().getName());
						
						jsonArray5.put(jsonObj);
					}
				}
			} catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 5", e);
				}else{
					log.error("error in TaskAssignServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray5.toString());
			}
			break;
		case 6:	//查询一个检验项目的检测人员
			JSONArray jsonArray6 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//分类(0:标准名称；1、分类名称)
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//分类或标准名称的Id
				String AlloteeRule = req.getParameter("AlloteeRule");	//分配规则
				
				if(SpeciesType.equals("2")){	//常用名称
					SpeciesType = "0";
				}
				if(AlloteeRule == null || AlloteeRule.trim().length() == 0){
					AlloteeRule = String.format("%s", SystemCfgUtil.getTaskAllotRule());
				}
				List<Integer> typeList = new ArrayList<Integer>();
				typeList.add(FlagUtil.QualificationType.Type_Jianding);
				typeList.add(FlagUtil.QualificationType.Type_Jianyan);
				typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
				
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_YEAR, 1);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Timestamp tYear = new Timestamp(c.getTimeInMillis()); //今年的开始时间
				
				List<Object[]> retList = tAssignMgr.getInspectQualifyUsersByRule(Integer.parseInt(ApplianceSpeciesId), Integer.parseInt(SpeciesType), typeList, Integer.parseInt(AlloteeRule), tYear);
				for(Object []obj : retList){
					JSONObject objTemp = new JSONObject();
					objTemp.put("id", (Integer)obj[0]);
					objTemp.put("name", obj[1].toString());
					jsonArray6.put(objTemp);
				}
				/*if(jsonArray6.length()==0){
					String queryStr = "with appspec as( " + 
									"select a.Id, a.ParentId, 1 as type from" +  SystemCfgUtil.DBPrexName + "ApplianceSpecies as a where a.Id = ? " + 
									"union all " + 
									"select b.Id, b.ParentId, 1 as type from" + SystemCfgUtil.DBPrexName + "ApplianceSpecies as b, appspec where b.ParentId = appspec.Id and appspec.type = 1 " +  
									"union all " + 
									"select c.Id, c.SpeciesId, 0 as type from" + SystemCfgUtil.DBPrexName + "ApplianceStandardName as c, appspec where c.SpeciesId = appspec.Id and appspec.type = 1 " + 
									")" +
									"select distinct s.Id, s.Name from appspec," + SystemCfgUtil.DBPrexName + "Qualification as a," + SystemCfgUtil.DBPrexName + "SysUser as s where (a.Type = 11 or a.Type = 12 or a.Type = 13 or a.Type = 16) and a.UserId = s.Id and a.AuthItemId = appspec.Id and a.AuthItemType = appspec.type";
					QualificationManager quaMgr = new QualificationManager();
					List<Object[]> retList1 = quaMgr.findBySQL(queryStr, Integer.valueOf(ApplianceSpeciesId));
					for(Object []obj : retList1){
						JSONObject objTemp = new JSONObject();
						objTemp.put("id", (Integer)obj[0]);
						objTemp.put("name", obj[1].toString());
						jsonArray6.put(objTemp);
					}
				}
					*/
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 6", e);
				}else{
					log.error("error in TaskAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray6.toString());
			}
			break;
		case 7:	//查询一个检验项目的核验人员（有核验资质）
			JSONArray jsonArray7 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//分类(0:标准名称；1、分类名称)
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//分类或标准名称的Id
				if(SpeciesType.equals("2")){	//常用名称
					SpeciesType = "0";
				}				
				List<Object[]> retList = new QualificationManager().getVerifyOrAuthorizeQualifyUsers(Integer.parseInt(ApplianceSpeciesId), Integer.parseInt(SpeciesType), FlagUtil.QualificationType.Type_Heyan);
				for(Object []obj : retList){
					JSONObject objTemp = new JSONObject();
					objTemp.put("id", (Integer)obj[0]);
					objTemp.put("name", obj[1].toString());
					jsonArray7.put(objTemp);
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 6", e);
				}else{
					log.error("error in TaskAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray7.toString());
			}
			break;
		case 8:	//查询一个检验项目的签字人员（有签字资质）
			JSONArray jsonArray8 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//分类(0:标准名称；1、分类名称)
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//分类或标准名称的Id
				if(SpeciesType.equals("2")){	//常用名称
					SpeciesType = "0";
				}				
				List<Object[]> retList = new QualificationManager().getVerifyOrAuthorizeQualifyUsers(Integer.parseInt(ApplianceSpeciesId), Integer.parseInt(SpeciesType), FlagUtil.QualificationType.Type_Qianzi);
				for(Object []obj : retList){
					JSONObject objTemp = new JSONObject();
					objTemp.put("id", (Integer)obj[0]);
					objTemp.put("name", obj[1].toString());
					jsonArray8.put(objTemp);
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 6", e);
				}else{
					log.error("error in TaskAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray8.toString());
			}
			break;
/*		case 9:	//费用管理中注销一个人的任务分配（只有任务的分配者才能注销，与case 1 有区别）——已不用的功能
			JSONObject retJSON9 = new JSONObject();
			try {
				String TaskId = req.getParameter("TaskId");	//委托单Id
				
				if(TaskId == null || TaskId.trim().length() == 0){
					throw new Exception("任务不存在！");
				}
				TaskAssign t = tAssignMgr.findById(Integer.parseInt(TaskId));
				if(t != null){
					if(t.getCommissionSheet().getStatus() == 10){
						throw new Exception("该委托单已注销，不能注销该检验任务！");
					}
					if(t.getCommissionSheet().getStatus() == 3 ||
							t.getCommissionSheet().getStatus() == 4 ||
							t.getCommissionSheet().getStatus() == 9){
						throw new Exception("该委托单已完工确认，不能注销该检验任务！");
					}
					SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
					if(t.getSysUserByAssignerId() == null || !loginUser.getId().equals(t.getSysUserByAssignerId().getId())){
						throw new Exception("您不是该检验任务的分配人，不能注销该检验任务！");
					}
					t.setStatus(1);	//注销
					if(tAssignMgr.update(t)){
						retJSON9.put("IsOK", true);
						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_TaskReceived, String.format("检验任务已取消，委托单号：%s", t.getCommissionSheet().getCode()), t.getSysUserByAlloteeId(), FlagUtil.SmsAndInfomationType.Type_TaskCancel);	//即时消息
						return;
					}else{
						throw new Exception("更新数据库失败！");
					}
				}
				throw new Exception("该任务不存在！");
			} catch (Exception e){
				try {
					retJSON9.put("IsOK", false);
					retJSON9.put("msg", String.format("注销任务失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 9", e);
				}else{
					log.error("error in TaskAssignServlet-->case 9", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			break;
		case 10:	//手动分配费用（只能分配 ‘检校人员是自身’或‘分配人员是自身’ 的记录的费用信息）——已不用的功能
			//录入的费用信息不能限制为小于 “原始记录和转包费” 的总和（因为还有特殊的委托单-技术服务，没有原始记录但是要收费！）
			JSONObject retJSON10 = new JSONObject();
			try {
				String TaskId = req.getParameter("TaskId");	//委托单Id
				String TestFee = req.getParameter("TestFee");	//检测费
				String RepairFee = req.getParameter("RepairFee");	//修理费
				String MaterialFee = req.getParameter("MaterialFee");	//材料费
				String CarFee = req.getParameter("CarFee");	//交通费
				String DebugFee = req.getParameter("DebugFee");	//调试费
				String OtherFee = req.getParameter("OtherFee");	//其他费
				if(TaskId == null || TaskId.trim().length() == 0){
					throw new Exception("检验任务不存在！");
				}
				if(TestFee == null || TestFee.length() == 0){
					TestFee = "0.0";
				}
				if(RepairFee == null || RepairFee.length() == 0){
					RepairFee = "0.0";
				}
				if(MaterialFee == null || MaterialFee.length() == 0){
					MaterialFee = "0.0";
				}
				if(CarFee == null || CarFee.length() == 0){
					CarFee = "0.0";
				}
				if(DebugFee == null || DebugFee.length() == 0){
					DebugFee = "0.0";
				}
				if(OtherFee == null || OtherFee.length() == 0){
					OtherFee = "0.0";
				}
				
				TaskAssign t = tAssignMgr.findById(Integer.parseInt(TaskId));
				if(t != null){
					if(t.getCommissionSheet().getStatus() == 10){
						throw new Exception("该委托单已注销，不能进行费用分配！");
					}
					if(t.getCommissionSheet().getStatus() == 3 ||
							t.getCommissionSheet().getStatus() == 4 ||
							t.getCommissionSheet().getStatus() == 9){
						throw new Exception("该委托单已完工确认，不能进行费用分配！");
					}
					
					SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
					if(!loginUser.getId().equals(t.getSysUserByAlloteeId().getId()) && 
							(t.getSysUserByAssignerId() == null || !loginUser.getId().equals(t.getSysUserByAssignerId().getId()))){
						throw new Exception("您不是该记录的 '检验人'或'分配人'，不能对其进行费用分配！");
					}
					t.setTestFee(Double.parseDouble(TestFee));
					t.setRepairFee(Double.parseDouble(RepairFee));
					t.setMaterialFee(Double.parseDouble(MaterialFee));
					t.setCarFee(Double.parseDouble(CarFee));
					t.setDebugFee(Double.parseDouble(DebugFee));
					t.setOtherFee(Double.parseDouble(OtherFee));
					t.setTotalFee(t.getTestFee() + t.getRepairFee() + t.getMaterialFee() + t.getCarFee() + t.getDebugFee() + t.getOtherFee());
					t.setSysUserByLastFeeAssignerId(loginUser);
					t.setLastFeeAssignTime(new Timestamp(System.currentTimeMillis()));
					if(tAssignMgr.update(t)){
						retJSON10.put("IsOK", true);
						return;
					}else{
						throw new Exception("更新数据库失败！");
					}
				}
				throw new Exception("该任务不存在！");
			} catch (Exception e){
				try {
					retJSON10.put("IsOK", false);
					retJSON10.put("msg", String.format("费用分配失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in TaskAssignServlet-->case 10", e);
				}else{
					log.error("error in TaskAssignServlet-->case 10", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
			}
			break;
*/
		}
	}

}
