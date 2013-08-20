package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
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

import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Overdue;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.OverdueManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;

public class OverdueServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(OverdueServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		OverdueManager overdueMgr = new OverdueManager();
		switch (method) {
		case 0: // 查找一个委托单的超期申请信息
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
				List<Overdue> retList = overdueMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				if(retList != null && retList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(Overdue o:retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("OverdueId", o.getId());
						jsonObj.put("DelayDays", o.getDelayDays());	//需要延期天数
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//超期申请人
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//申请时间
						jsonObj.put("Reason", o.getReason().getReason());	//超期原因
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//超期办理人
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//办理结果
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//办理时间
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//办理备注信息
						
						jsonArray.put(jsonObj);
					}
					
					retJSON.put("total", retList.size());
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
					log.debug("exception in OverdueServlet-->case 0", e);
				}else{
					log.error("error in OverdueServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//超期审批（办理）
			JSONObject retJSON1 = new JSONObject();
			try {
				String OverdueId = req.getParameter("OverdueId");	//超期申请Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//办理结果：0不通过；1通过
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//办理的备注信息
				
				if(OverdueId == null || OverdueId.trim().length() == 0){
					throw new Exception("该超期申请单不存在！");
				}
				Overdue o = overdueMgr.findById(Integer.parseInt(OverdueId));
				if(o != null){
//					if(!o.getSysUserByExecutorId().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){
//						throw new Exception("当前登录用户与该超期申请的办理人不一致！");
//					}
					if(o.getExecuteTime() != null){
						throw new Exception("该超期申请已经审批，不能重复操作！");
					}
					if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(o.getCommissionSheet().getStatus())){
						throw new Exception("该委托单已完工或已注销，不能执行超期审批！");
					}
					o.setExecuteMsg(ExecuteMsg);
					o.setExecuteResult(Integer.parseInt(ExecutorResult)>0?true:false);
					o.setExecuteTime(new Timestamp(System.currentTimeMillis()));
					o.setSysUserByExecutorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//办理人
					if(overdueMgr.overdueHandle(o, o.getCommissionSheet())){
						retJSON1.put("IsOK", true);
						return;
					}else{
						throw new Exception("更新数据库失败！");
					}
				}
				throw new Exception("该超期申请单不存在！");
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("超期审批（办理）失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OverdueServlet-->case 1", e);
				}else{
					log.error("error in OverdueServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:		//超期申请
			JSONObject retJSON2 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//委托单Id
				String Reason = req.getParameter("Reason");	//超期原因
//				String ExecutorName = req.getParameter("ExecutorName");	//办理人员姓名
				String DelayDays = req.getParameter("DelayDays");	//延期天数
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
//				if(ExecutorName == null || ExecutorName.trim().length() == 0){
//					throw new Exception("办理人员为空！");
//				}
				if(DelayDays == null || DelayDays.trim().length() == 0){
					throw new Exception("延期天数为空！");
				}
				
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("委托单不存在！");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("该委托单已完工或已注销，不能再次提交超期申请！");
				}
//				List<SysUser> userList = new UserManager().findByVarProperty(new KeyValueWithOperator("name", ExecutorName, "="), new KeyValueWithOperator("status", 0, "="));
//				if(userList.size() == 0){
//					throw new Exception("办理人员不存在或不可用！");
//				}
//				SysUser user = userList.get(0);
//				if(user.getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){
//					throw new Exception("申请人与办理人员不能是同一个人！");
//				}
				Timestamp today = new Timestamp(System.currentTimeMillis());
				//存超期申请
				Overdue r = new Overdue();
//				r.setSysUserByExecutorId(user);	//办理人
				r.setSysUserByRequesterId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//申请人
				r.setApplyTime(today);	//申请时间
				r.setCommissionSheet(cSheet);	//委托单
				r.setDelayDays(Integer.parseInt(DelayDays.trim()));//延期天数
				
				//存超期原因
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_Overdue, "="));//查找超期原因
				if(rList.size() > 0){	//更新原因
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					r.setReason(reason);	//超期原因
				}else{	//新建原因
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(FlagUtil.ReasonType.Type_Overdue);	//超期
					rMgr.save(reason);
					r.setReason(reason);	//超期原因
				}

				if(overdueMgr.save(r)){
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("超期申请提交失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OverdueServlet-->case 2", e);
				}else{
					log.error("error in OverdueServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //查询超期审批任务列表
			JSONObject retJSON3 = new JSONObject();
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
				condList.add(new KeyValueWithOperator("commissionSheet.status", 3, "<"));	//委托单尚未完工或尚未注销的
//				condList.add(new KeyValueWithOperator("sysUserByExecutorId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //办理人
				condList.add(new KeyValueWithOperator("executeTime", null, "is null"));	//审批任务尚未完成的
				
				int total = overdueMgr.getTotalCount(condList);
				List<Overdue> tRetList = overdueMgr.findPagedAllBySort(page, rows, "applyTime", true, condList);
				for(Overdue o : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("OverdueId", o.getId());
					
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//委托时间
					jsonObj.put("PromiseDate", o.getCommissionSheet().getPromiseDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getPromiseDate()));	//承诺检出日期					
					
					jsonObj.put("DelayDays", o.getDelayDays());	//需要延期天数
					jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//超期申请人
					jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
					jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//申请时间
					jsonObj.put("Reason", o.getReason().getReason());	//超期原因
					jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//超期办理人
					jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
//					jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//办理结果
//					jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//办理时间
//					jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//办理备注信息
					
					jsonArray.put(jsonObj);
				}
				retJSON3.put("total", total);
				retJSON3.put("rows", jsonArray);

			} catch (Exception e){
				
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OverdueServlet-->case 3", e);
				}else{
					log.error("error in OverdueServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		}
	}

}
