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

import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.RemakeCertificate;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.RemakeCertificateManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class RemakeCertificateServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(RemakeCertificateServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
		
		switch(method){
		case 1 : 	//根据证书号查询委托单
			JSONObject retJSON1 = new JSONObject();
			try{
				String CertificateCode = req.getParameter("CertificateCode");
				if(CertificateCode == null || CertificateCode.trim().length() == 0){
					throw new Exception("参数不完整：证书号不能为空！");
				}
				List<Certificate> cList = new CertificateManager().findByPropertyBySort("lastEditTime", false, 
						new KeyValueWithOperator("certificateCode", CertificateCode, "="));
				if(cList.size() == 0){
					throw new Exception("找不到该证书号对应的证书，请核对证书号是否正确！");
				}
				Certificate c = cList.get(0);
				CommissionSheet cSheet = c.getCommissionSheet();
				retJSON1.put("IsOK", true);
				
				retJSON1.put("OriginalRecordId", c.getOriginalRecord().getId());	//原始记录ID
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("CommissionId", cSheet.getId());	//委托单ID
				jsonObj.put("CommissionCode", cSheet.getCode());
				jsonObj.put("CommissionPwd", cSheet.getPwd());
				jsonObj.put("CommissionStatus", cSheet.getStatus());	//委托单状态
				jsonObj.put("CommissionType", cSheet.getCommissionType());
				jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(cSheet.getCommissionDate()));
				jsonObj.put("CustomerName", cSheet.getCustomerName());
				jsonObj.put("CustomerTel", cSheet.getCustomerTel());
				jsonObj.put("CustomerAddress", cSheet.getCustomerAddress());
				jsonObj.put("CustomerZipCode", cSheet.getCustomerZipCode());
				jsonObj.put("ContactPerson", cSheet.getCustomerContactor());
				jsonObj.put("ContactorTel", cSheet.getCustomerContactorTel());
				jsonObj.put("SampleFrom", cSheet.getSampleFrom());
				jsonObj.put("BillingTo", cSheet.getBillingTo());
				jsonObj.put("ApplianceName", cSheet.getApplianceName());	//器具名称
				jsonObj.put("Model", cSheet.getApplianceModel());
				jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//出厂编号
				jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//管理编号
				jsonObj.put("Manufacturer", cSheet.getManufacturer());
				jsonObj.put("Quantity", cSheet.getQuantity());
				jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//强制检定
				jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//是否加急
				jsonObj.put("Appearance", cSheet.getAppearance());	//外观附件
				jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//其他要求
				jsonObj.put("Status", cSheet.getStatus());	//委托单状态
				
				jsonObj.put("TotalFee", cSheet.getTotalFee());	//委托单总计费用
				
				retJSON1.put("CommissionObj", jsonObj);
			}catch(Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("查询证书信息失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RemakeCertificateServlet-->case 1", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2 : 	//查询一个委托单的重新编制证书的记录
			JSONObject retJSON2 = new JSONObject();
			try{
				String CommissionId = req.getParameter("CommissionId");
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("参数不完整！");
				}
				int total = remakeMgr.getTotalCount(new KeyValueWithOperator("originalRecord.commissionSheet.id", Integer.parseInt(CommissionId), "="));
				List<RemakeCertificate> rList = remakeMgr.findByPropertyBySort("createTime", true, new KeyValueWithOperator("originalRecord.commissionSheet.id", Integer.parseInt(CommissionId), "="));
				JSONArray jsonArray = new JSONArray();
				for(RemakeCertificate rc : rList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("Id", rc.getId());
					jsonObj.put("CertificateCode", rc.getCertificateCode());	//证书编号
					jsonObj.put("OriginalRecordId", rc.getOriginalRecord().getId());	//原始记录Id
					jsonObj.put("CreateTime", DateTimeFormatUtil.DateTimeFormat.format(rc.getCreateTime()));	//创建时间
					jsonObj.put("CreatorId", rc.getSysUserByCreatorId().getId());	//创建人Id
					jsonObj.put("CreatorName", rc.getSysUserByCreatorId().getName());	//创建人姓名
					jsonObj.put("CreateRemark", rc.getCreateRemark()==null?"":rc.getCreateRemark());	//创建备注
					jsonObj.put("ReceiverId", rc.getSysUserByReceiverId().getId());	//接收人Id
					jsonObj.put("ReceiverName", rc.getSysUserByReceiverId().getName());	//接收人姓名
					jsonObj.put("FinishTime", rc.getFinishTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getFinishTime()));	//完成时间
					jsonObj.put("FinishRemark", rc.getFinishRemark()==null?"":rc.getFinishRemark());	
					jsonObj.put("PassedTime", rc.getPassedTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getPassedTime()));	//通过时间
					
					jsonArray.put(jsonObj);
					
				}
				
				retJSON2.put("total", total);
				retJSON2.put("rows", jsonArray);
			}catch(Exception e){
				try {
					retJSON2.put("total", 0);
					retJSON2.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RemakeCertificateServlet-->case 2", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3 : 	//新增重新编制证书的请求
			JSONObject retJSON3 = new JSONObject();
			try{
				String CertificateCode = req.getParameter("CertificateCode");
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String CreateRemark = req.getParameter("CreateRemark");
				
				if(CertificateCode == null || CertificateCode.length() == 0 ||
						OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				
				List<Certificate> cList = new CertificateManager().findByPropertyBySort("certificateCode", true, 
						new KeyValueWithOperator("certificateCode", CertificateCode, "="),
						new KeyValueWithOperator("originalRecord.id", Integer.parseInt(OriginalRecordId), "="));
				if(cList.size() == 0){
					throw new Exception("找不到该证书号对应的证书，请核对证书号是否正确！");
				}
				Certificate c = cList.get(0);
				RemakeCertificate rc = new RemakeCertificate();
				rc.setCertificateCode(CertificateCode);
				rc.setCreateRemark(CreateRemark);
				rc.setOriginalRecord(c.getOriginalRecord());
				rc.setSysUserByCreatorId((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));
				rc.setCreateTime(new Timestamp(System.currentTimeMillis()));
				rc.setSysUserByReceiverId(c.getOriginalRecord().getSysUserByStaffId());
				
				if(remakeMgr.save(rc)){
					retJSON3.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
			}catch(Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("查询证书信息失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RemakeCertificateServlet-->case 3", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4 : 	//查询当前登录用户的‘重新编制证书’任务 列表
			JSONObject retJSON4 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String CertificateCode = req.getParameter("CertificateCode");	//委托单号
				String CustomerName = req.getParameter("CustomerName");	//委托单位
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
				if(CertificateCode != null && CertificateCode.trim().length() > 0){
					CertificateCode = URLDecoder.decode(CertificateCode.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("certificateCode", "%"+CertificateCode+"%", "like"));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("originalRecord.commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				condList.add(new KeyValueWithOperator("sysUserByReceiverId.id", ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId(), "="));  //检验人
				condList.add(new KeyValueWithOperator("finishTime", null, "is null"));	//任务尚未完成的
				
				int total = remakeMgr.getTotalCount(condList);
				List<RemakeCertificate> tRetList = remakeMgr.findPagedAllBySort(page, rows, "createTime", true, condList);
				for(RemakeCertificate rc : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CommissionCode", rc.getOriginalRecord().getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", rc.getOriginalRecord().getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", rc.getOriginalRecord().getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", rc.getOriginalRecord().getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CustomerContactor", rc.getOriginalRecord().getCommissionSheet().getCustomerContactor());	//委托单位联系人
					jsonObj.put("CustomerContactorTel", rc.getOriginalRecord().getCommissionSheet().getCustomerContactorTel());	//委托单位联系人电话
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(rc.getOriginalRecord().getCommissionSheet().getCommissionDate()));	//委托日期
					
					jsonObj.put("Id", rc.getId());
					jsonObj.put("CertificateCode", rc.getCertificateCode());	//证书编号
					jsonObj.put("OriginalRecordId", rc.getOriginalRecord().getId());	//原始记录Id
					jsonObj.put("CreateTime", DateTimeFormatUtil.DateTimeFormat.format(rc.getCreateTime()));	//创建时间
					jsonObj.put("CreatorId", rc.getSysUserByCreatorId().getId());	//创建人Id
					jsonObj.put("CreatorName", rc.getSysUserByCreatorId().getName());	//创建人姓名
					jsonObj.put("CreateRemark", rc.getCreateRemark()==null?"":rc.getCreateRemark());	//创建备注
					jsonObj.put("ReceiverId", rc.getSysUserByReceiverId().getId());	//接收人Id
					jsonObj.put("ReceiverName", rc.getSysUserByReceiverId().getName());	//接收人姓名
					jsonObj.put("FinishTime", rc.getFinishTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getFinishTime()));	//完成时间
					jsonObj.put("FinishRemark", rc.getFinishRemark()==null?"":rc.getFinishRemark());	
					jsonObj.put("PassedTime", rc.getPassedTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getPassedTime()));	//通过时间
					
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
					log.debug("exception in RemakeCertificateServlet-->case 4", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5 :	//查询当前登录用户待审查的‘重新编制证书’任务 列表
			JSONObject retJSON5 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String CertificateCode = req.getParameter("CertificateCode");	//委托单号
				String CustomerName = req.getParameter("CustomerName");	//委托单位
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
				if(CertificateCode != null && CertificateCode.trim().length() > 0){
					CertificateCode = URLDecoder.decode(CertificateCode.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("certificateCode", "%"+CertificateCode+"%", "like"));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("originalRecord.commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				condList.add(new KeyValueWithOperator("sysUserByCreatorId.id", ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId(), "="));  //检验人
				condList.add(new KeyValueWithOperator("passedTime", null, "is null"));	//任务尚未完成的
				
				int total = remakeMgr.getTotalCount(condList);
				List<RemakeCertificate> tRetList = remakeMgr.findPagedAllBySort(page, rows, "createTime", true, condList);
				for(RemakeCertificate rc : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CommissionCode", rc.getOriginalRecord().getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", rc.getOriginalRecord().getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", rc.getOriginalRecord().getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", rc.getOriginalRecord().getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CustomerContactor", rc.getOriginalRecord().getCommissionSheet().getCustomerContactor());	//委托单位联系人
					jsonObj.put("CustomerContactorTel", rc.getOriginalRecord().getCommissionSheet().getCustomerContactorTel());	//委托单位联系人电话
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(rc.getOriginalRecord().getCommissionSheet().getCommissionDate()));	//委托日期
					
					jsonObj.put("Id", rc.getId());
					jsonObj.put("CertificateCode", rc.getCertificateCode());	//证书编号
					jsonObj.put("OriginalRecordId", rc.getOriginalRecord().getId());	//原始记录Id
					jsonObj.put("CreateTime", DateTimeFormatUtil.DateTimeFormat.format(rc.getCreateTime()));	//创建时间
					jsonObj.put("CreatorId", rc.getSysUserByCreatorId().getId());	//创建人Id
					jsonObj.put("CreatorName", rc.getSysUserByCreatorId().getName());	//创建人姓名
					jsonObj.put("CreateRemark", rc.getCreateRemark()==null?"":rc.getCreateRemark());	//创建备注
					jsonObj.put("ReceiverId", rc.getSysUserByReceiverId().getId());	//接收人Id
					jsonObj.put("ReceiverName", rc.getSysUserByReceiverId().getName());	//接收人姓名
					jsonObj.put("FinishTime", rc.getFinishTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getFinishTime()));	//完成时间
					jsonObj.put("FinishRemark", rc.getFinishRemark()==null?"":rc.getFinishRemark());	
					jsonObj.put("PassedTime", rc.getPassedTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getPassedTime()));	//通过时间
					
					jsonArray.put(jsonObj);
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
					log.debug("exception in RemakeCertificateServlet-->case 5", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6 : 	//确认任务完成
			JSONObject retJSON6 = new JSONObject();
			try{
				String RemakeCertificateId = req.getParameter("Id");
				String FinishRemark = req.getParameter("FinishRemark");
				if(RemakeCertificateId == null || RemakeCertificateId.trim().length() == 0){
					throw new Exception("参数不完整！");
				}
				RemakeCertificate rc = remakeMgr.findById(Integer.parseInt(RemakeCertificateId));
				if(rc == null){
					throw new Exception("找不到指定的重新编制证书的任务！");
				}
				if(rc.getFinishTime() != null){
					throw new Exception("该任务已完成，不能重复操作！");
				}
				rc.setFinishRemark(FinishRemark);
				rc.setFinishTime(new Timestamp(System.currentTimeMillis()));
				if(remakeMgr.update(rc)){
					retJSON6.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			}catch(Exception e){
				try {
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("确认任务完成失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RemakeCertificateServlet-->case 6", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7 : 	//任务审查结果录入
			JSONObject retJSON7 = new JSONObject();
			try{
				String RemakeCertificateId = req.getParameter("Id");
				String ExecuteResult = req.getParameter("ExecuteResult");
				String CreateRemark = req.getParameter("CreateRemark");
				if(RemakeCertificateId == null || RemakeCertificateId.trim().length() == 0 ||
						ExecuteResult == null || ExecuteResult.trim().length() == 0){
					throw new Exception("参数不完整！");
				}
				RemakeCertificate rc = remakeMgr.findById(Integer.parseInt(RemakeCertificateId));
				if(rc == null){
					throw new Exception("找不到指定的重新编制证书的任务！");
				}
				if(rc.getFinishTime() == null){
					throw new Exception("该任务尚未完成，不能录入审查结果！");
				}
				if(rc.getPassedTime() != null){
					throw new Exception("该任务已审核通过，不能再次录入审查结果！");
				}
				if(Integer.parseInt(ExecuteResult) == 1){	//审核通过
					rc.setPassedTime(new Timestamp(System.currentTimeMillis()));
				}else{	//重新编制
					rc.setFinishTime(null);
				}
				rc.setCreateRemark(CreateRemark);
				if(remakeMgr.update(rc)){
					retJSON7.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			}catch(Exception e){
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("任务审查结果录入失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in RemakeCertificateServlet-->case 7", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		}
	}
	
}
