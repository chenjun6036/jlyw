package com.jlyw.servlet;

import java.io.IOException;
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

import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.TaskAssignManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.SystemCfgUtil;

/**
 * 查询相关任务
 * @author Zhan
 *
 */
public class TaskServlet extends HttpServlet {

	private static final Log log = LogFactory.getLog(TaskServlet.class);
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		TaskAssignManager taskMgr = new TaskAssignManager();
		switch (method) {
		case 0: // 查找登录用户的任务信息（用于首页）
			JSONObject retJSON = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				Integer loginUserId = ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId();
				
				String queryString = " select cast(a2.Code as varchar(15)) as field1,a2.ApplianceName as field2,a2.CustomerName as field3,a2.Quantity as field4,a1.assignTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_TaskReceived+" as taskType from "+SystemCfgUtil.DBPrexName+"TaskAssign as a1,"+SystemCfgUtil.DBPrexName+"CommissionSheet as a2 where a1.AlloteeId= ? and a2.Status <> 10 and a1.status <> 1 and a1.finishTime is null and a1.CommissionSheetId = a2.Id and datediff(dd,a2.CommissionDate,getdate()) <=30" +	  //检验任务
					" union select cast(b2.Code as varchar(15)) as field1,b3.Reason as field2,b2.CustomerName as field3,b2.Quantity as field4,b1.ApplyTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_OverdueApproveTask+" as taskType from "+SystemCfgUtil.DBPrexName+"Overdue as b1,"+SystemCfgUtil.DBPrexName+"CommissionSheet as b2,"+SystemCfgUtil.DBPrexName+"Reason as b3 where b1.ExecutorId= ? and b2.status <> 10 and b1.ExecuteTime is null and b1.CommissionSheetId = b2.Id and b1.Reason = b3.Id " +	//超期审批任务
					" union select cast(c2.Code as varchar(15)) as field1,c3.Reason as field2,c2.CustomerName as field3,c2.Quantity as field4,c1.RequestTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_WithdrawApproveTask+" as taskType from "+SystemCfgUtil.DBPrexName+"Withdraw as c1,"+SystemCfgUtil.DBPrexName+"CommissionSheet as c2,"+SystemCfgUtil.DBPrexName+"Reason as c3 where c1.ExecutorId= ? and c2.status <> 10 and c1.ExecuteTime is null and c1.CommissionSheetId = c2.Id and c1.Reason = c3.Id " +	//退样审批任务
//					" union select cast(d2.Code as varchar(15)) as field1,d3.Reason as field2,d1.ApplyTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_DiscountApproveTask+" as taskType from "+SystemCfgUtil.DBPrexName+"Discount as d1,"+SystemCfgUtil.DBPrexName+"CommissionSheet as d2,"+SystemCfgUtil.DBPrexName+"Reason as d3 where d1.ExecutorId= ? and d2.status <> 10 and d1.ExecuteTime is null and d1.CommissionSheetId = d2.Id and d1.Reason = d3.Id " +	//折扣申请审批任务
					
					" union select cast(e2.Code as varchar(15)) as field1,'' as field2,e2.CustomerName as field3,e2.Quantity as field4,e1.CreateTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateVerify+" as taskType from "+SystemCfgUtil.DBPrexName+"VerifyAndAuthorize as e1,"+SystemCfgUtil.DBPrexName+"CommissionSheet as e2,"+SystemCfgUtil.DBPrexName+"OriginalRecord as e3,"+SystemCfgUtil.DBPrexName+"Certificate as e4 where e1.VerifierId= ? and e2.status < 3 and e1.VerifyTime is null and e1.CommissionSheetId = e2.Id and e1.Id = e3.VerifyAndAuthorizeId and e3.Status <> 1 and e1.CertificateId=e4.Id and e4.LastEditorId is null " +	//原始记录和证书核验
					" union select cast(f2.Code as varchar(15)) as field1,'' as field2,f2.CustomerName as field3,f2.Quantity as field4,f1.CreateTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateAuthorize+" as taskType from "+SystemCfgUtil.DBPrexName+"VerifyAndAuthorize as f1,"+SystemCfgUtil.DBPrexName+"CommissionSheet as f2,"+SystemCfgUtil.DBPrexName+"OriginalRecord as f3 where f1.AuthorizerId= ? and f2.status < 3 and f1.VerifyResult = 1 and f1.AuthorizeTime is null and f1.CommissionSheetId = f2.Id and f1.Id = f3.VerifyAndAuthorizeId and f3.Status <> 1 " +	//原始记录和证书授权签字
				
					" union select g1.CertificateCode as field1,g1.CreateRemark as field2, '' as field3, NULL as field4, g1.CreateTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_RemakeCertificate+" as taskType from "+SystemCfgUtil.DBPrexName+"RemakeCertificate as g1 where g1.ReceiverId= ? and g1.FinishTime is null " +	//重新编制证书
					" union select h1.CertificateCode as field1,h1.FinishRemark as field2, '' as field3, NULL as field4, h1.FinishTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_RemakeCertificateApprove+" as taskType from "+SystemCfgUtil.DBPrexName+"RemakeCertificate as h1 where h1.CreatorId= ? and h1.FinishTime is not null and h1.PassedTime is null " +	//重新编制证书审核
					
					" union select cast(i1.Code as varchar(15)) as field1,'' as field2,i1.CustomerName as field3,i1.Quantity as field4,i3.LastEditTime as taskTime, "+FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateWorkStaffVerify+" as taskType from "+SystemCfgUtil.DBPrexName+"CommissionSheet as i1,"+SystemCfgUtil.DBPrexName+"OriginalRecord as i2,"+SystemCfgUtil.DBPrexName+"Certificate as i3 where i1.status<3 and i1.Id=i2.CommissionSheetId and i2.Status<>1 and i2.CertificateId=i3.Id and i3.LastEditorId is not null and i3.Pdf is not null and i2.StaffId =?";	//检定员核定
				String countQueryString = "select count(*) from ("+queryString+") as model";
				String listQueryString  = "select * from (" + queryString + ") as model order by model.taskType asc, model.field1 asc, model.taskTime desc ";
				int total = taskMgr.getTotalCountBySQL(countQueryString,
						loginUserId,
						loginUserId,
						loginUserId,
//						loginUserId,
						loginUserId,
						loginUserId,
						loginUserId,
						loginUserId,
						loginUserId);				
				List<Object[]> tRetList = taskMgr.findPageAllBySQL(listQueryString, page, rows,
						loginUserId,
						loginUserId,
						loginUserId,
//						loginUserId,
						loginUserId,
						loginUserId,
						loginUserId,
						loginUserId,
						loginUserId);
				for(Object[] obj : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("Type", FlagUtil.SmsAndInfomationType.getMsgTypeInfo((Integer)obj[5]));
					jsonObj.put("TaskTime", obj[4] == null?"":DateTimeFormatUtil.DateTimeFormat.format((java.sql.Timestamp)obj[4]));
					switch((Integer)obj[5]){
					case FlagUtil.SmsAndInfomationType.Type_TaskReceived:
						jsonObj.put("Content", String.format("委托单号:%s，检验器具:%s，数量:%s，委托单位:%s", obj[0].toString(), obj[1].toString(),obj[3]==null?"":obj[3].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/TaskTime.jsp?commissionsheetcode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_OverdueApproveTask:
						jsonObj.put("Content", String.format("委托单号:%s，申请原因:%s，委托单位:%s", obj[0].toString(), obj[1].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/OverdueTask.jsp?commissionsheetcode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_WithdrawApproveTask:
						jsonObj.put("Content", String.format("委托单号:%s，申请原因:%s，委托单位:%s", obj[0].toString(), obj[1].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/WithdrawTask.jsp?commissionsheetcode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_DiscountApproveTask:
						jsonObj.put("Content", String.format("委托单号:%s，申请原因:%s，委托单位:%s", obj[0].toString(), obj[1].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/FeeManage/DiscountTask.jsp?commissionsheetcode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateVerify:
						jsonObj.put("Content", String.format("委托单号:%s，委托单位:%s", obj[0].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/VerifyTask.jsp?commissionsheetcode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateAuthorize:
						jsonObj.put("Content", String.format("委托单号:%s，委托单位:%s", obj[0].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/AuthorizeTask.jsp?commissionsheetcode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_RemakeCertificate:
						jsonObj.put("Content", String.format("证书编号:%s，备注:%s", obj[0].toString(), obj[1]==null?"无":obj[1].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/RemakeCertificate/RemakeCertificateTask.jsp?CertificateCode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_RemakeCertificateApprove:
						jsonObj.put("Content", String.format("证书编号：%s，备注：%s", obj[0].toString(), obj[1]==null?"无":obj[1].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/RemakeCertificate/RemakeCertificateApproveTask.jsp?CertificateCode="+obj[0].toString());
						break;
					case FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateWorkStaffVerify:
						jsonObj.put("Content", String.format("委托单号:%s，委托单位:%s", obj[0].toString(), obj[2]==null?"":obj[2].toString()));
						jsonObj.put("Url", "/jlyw/TaskManage/WorkStaffVerifyTask.jsp?commissionsheetcode="+obj[0].toString());
						break;
					default:
						jsonObj.put("Content", "");
						jsonObj.put("Url", "");
					}
					
					jsonArray.put(jsonObj);
				}
				retJSON.put("total", total);
				retJSON.put("rows", jsonArray);
			}catch(Exception e){
				log.error("error in TaskServlet", e);
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
				} catch (JSONException e1) {				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		}
	}
	
}
