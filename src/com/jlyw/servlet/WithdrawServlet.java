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
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.Withdraw;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.manager.WithdrawManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;

/**
 * 退样
 * @author Administrator
 *
 */
public class WithdrawServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(WithdrawServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		WithdrawManager withdrawMgr = new WithdrawManager();
		switch (method) {
		case 0: // 查找一个委托单的退样申请信息
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
				List<Withdraw> retList = withdrawMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				if(retList != null && retList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(Withdraw w:retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("WithdrawId", w.getId());
						jsonObj.put("WithdrawNumber", w.getNumber());	//退样数量
						jsonObj.put("WithdrawDesc", w.getDescription()==null?"":w.getDescription());	//退样描述
						jsonObj.put("RequesterId", w.getSysUserByRequesterId().getId());	//退样申请人
						jsonObj.put("RequesterName", w.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(w.getRequestTime()));	//申请时间
						jsonObj.put("Reason", w.getReason().getReason());	//退样原因
						jsonObj.put("ExecutorId", w.getSysUserByExecutorId()==null?null:w.getSysUserByExecutorId().getId());	//退样办理人
						jsonObj.put("ExecutorName", w.getSysUserByExecutorId()==null?"":w.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", w.getExecuteResult()==null?-1:(w.getExecuteResult()?1:0));	//办理结果
						jsonObj.put("ExecuteTime", w.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(w.getExecuteTime()));	//办理时间
						jsonObj.put("ExecuteMsg", w.getExecuteMsg()==null?"":w.getExecuteMsg());	//办理备注信息
						jsonObj.put("WithdrawDate", w.getWithdrawDate()==null?"":DateTimeFormatUtil.DateFormat.format(w.getWithdrawDate()));	//退样时间
						jsonObj.put("Location", w.getLocation()==null?"":w.getLocation());	//存放位置
						
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
					log.debug("exception in WithdrawServlet-->case 0", e);
				}else{
					log.error("error in WithdrawServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//退样审批（办理）
			JSONObject retJSON1 = new JSONObject();
			try {
				String WithdrawId = req.getParameter("WithdrawId");	//退样申请Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//办理结果：0不通过；1通过
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//办理的备注信息
				String WithdrawDate = req.getParameter("WithdrawDate");	//退样日期
				String Location = req.getParameter("Location");	//存放位置
				
				if(WithdrawId == null || WithdrawId.trim().length() == 0){
					throw new Exception("该退样申请单不存在！");
				}
				Withdraw w = withdrawMgr.findById(Integer.parseInt(WithdrawId));
				if(w != null){
//					if(!w.getSysUserByExecutorId().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){
//						throw new Exception("当前登录用户与该退样申请的办理人不一致！");
//					}
					if(w.getExecuteTime() != null){
						throw new Exception("该退样申请已经审批，不能重复操作！");
					}
					if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(w.getCommissionSheet().getStatus())){
						throw new Exception("该委托单已完工或已注销，不能执行退样审批！");
					}
					w.setExecuteMsg(ExecuteMsg);
					w.setExecuteResult(Integer.parseInt(ExecutorResult)>0?true:false);
					w.setExecuteTime(new Timestamp(System.currentTimeMillis()));
					w.setSysUserByExecutorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//办理人
					if(WithdrawDate != null && WithdrawDate.trim().length() > 0){
						w.setWithdrawDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(WithdrawDate.trim()).getTime()));
					}
					w.setLocation(Location);	//存放位置
					if(withdrawMgr.update(w)){
						retJSON1.put("IsOK", true);
						return;
					}else{
						throw new Exception("更新数据库失败！");
					}
				}
				throw new Exception("该退样申请单不存在！");
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("退样审批（办理）失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in WithdrawServlet-->case 1", e);
				}else{
					log.error("error in WithdrawServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:		//退样申请
			JSONObject retJSON2 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//委托单Id
				String Reason = req.getParameter("Reason");	//退样原因
//				String ExecutorName = req.getParameter("ExecutorName");	//办理人员姓名
				String WithdrawNumber = req.getParameter("WithdrawNumber");	//退样数量
				String WithdrawDesc = req.getParameter("WithdrawDesc");	//退样样品描述
				
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
//				if(ExecutorName == null || ExecutorName.trim().length() == 0){
//					throw new Exception("办理人员为空！");
//				}
				if(WithdrawNumber == null || WithdrawNumber.trim().length() == 0){
					throw new Exception("退样样品数量为空！");
				}
				
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("委托单不存在！");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("该委托单已完工或已注销，不能再次提交退样申请！");
				}
				
				//判断退样的总数量是否小于委托单的器具数量且退样的总数量不小于0
				String queryString = "select sum(w.number) from Withdraw as w where w.commissionSheet.id=? and ( w.executeResult is null or w.executeResult=? )";	//审批已通过的和未审批的退样总数
				List<Long> iRetList = withdrawMgr.findByHQL(queryString, cSheet.getId(), true);
				Integer count = (iRetList.get(0)==null)?0:iRetList.get(0).intValue();
				if(count == null){
					count = 0;
				}
				if(count + Integer.parseInt(WithdrawNumber.trim()) > cSheet.getQuantity()){
					throw new Exception(String.format("申请退样的总数量'%s'不能大于委托单的器具总数'%s'！", count+Integer.parseInt(WithdrawNumber), cSheet.getQuantity()));
				}
				if(count + Integer.parseInt(WithdrawNumber.trim()) < 0 ){
					throw new Exception(String.format("申请退样的总数量'%s'不能小于0！", count+Integer.parseInt(WithdrawNumber)));
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
				//存退样申请
				Withdraw w = new Withdraw();
//				w.setSysUserByExecutorId(user);	//办理人
				w.setSysUserByRequesterId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//申请人
				w.setRequestTime(today);	//申请时间
				w.setCommissionSheet(cSheet);	//委托单
				w.setDescription(WithdrawDesc);	//退样样品描述
				
				w.setNumber(Integer.parseInt(WithdrawNumber.trim()));//退样样品数量
				
				//存退样原因
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_Withdraw, "="));//查找退样原因
				if(rList.size() > 0){	//更新原因
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					w.setReason(reason);	//退样原因
				}else{	//新建原因
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(FlagUtil.ReasonType.Type_Withdraw);	//退样
					rMgr.save(reason);
					w.setReason(reason);	//退样原因
				}

				if(withdrawMgr.save(w)){
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("退样申请提交失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in WithdrawServlet-->case 2", e);
				}else{
					log.error("error in WithdrawServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //查询退样审批任务列表
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
				
				int total = withdrawMgr.getTotalCount(condList);
				List<Withdraw> tRetList = withdrawMgr.findPagedAllBySort(page, rows, "requestTime", true, condList);
				for(Withdraw w : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("WithdrawId", w.getId());
					
					jsonObj.put("CommissionCode", w.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", w.getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", w.getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", w.getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(w.getCommissionSheet().getCommissionDate()));	//委托时间
					jsonObj.put("PromiseDate", w.getCommissionSheet().getPromiseDate()==null?"":DateTimeFormatUtil.DateFormat.format(w.getCommissionSheet().getPromiseDate()));	//承诺检出日期
					
					jsonObj.put("WithdrawNumber", w.getNumber());	//退样数量
					jsonObj.put("WithdrawDesc", w.getDescription()==null?"":w.getDescription());	//退样描述
					jsonObj.put("RequesterId", w.getSysUserByRequesterId().getId());	//退样申请人
					jsonObj.put("RequesterName", w.getSysUserByRequesterId().getName());
					jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(w.getRequestTime()));	//申请时间
					jsonObj.put("Reason", w.getReason().getReason());	//退样原因
					jsonObj.put("ExecutorId", w.getSysUserByExecutorId()==null?null:w.getSysUserByExecutorId().getId());	//退样办理人
					jsonObj.put("ExecutorName", w.getSysUserByExecutorId()==null?"":w.getSysUserByExecutorId().getName());
					jsonObj.put("Location", w.getLocation()==null?"":w.getLocation());	//存放位置
					
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
					log.debug("exception in WithdrawServlet-->case 3", e);
				}else{
					log.error("error in WithdrawServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		}
	}
}
