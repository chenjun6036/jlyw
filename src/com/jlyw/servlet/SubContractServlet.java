package com.jlyw.servlet;

import java.io.IOException;
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
import com.jlyw.hibernate.SubContract;
import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.SubContractManager;
import com.jlyw.manager.SubContractorManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.UIDUtil;

public class SubContractServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(SubContractServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		SubContractorManager subContorMgr = new SubContractorManager();	//转包方管理者Mgr
		SubContractManager subContMgr = new SubContractManager();		//转包记录管理者Mgr
		switch (method) {
		case 0: // 查询 转包方单位名称
			JSONArray jsonArray = new JSONArray();
			try {
				String cusNameStr = req.getParameter("QueryName");
				if(cusNameStr != null && cusNameStr.trim().length() > 0){
					String cusName =  new String(cusNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					//cusName = LetterUtil.String2Alpha(cusName);	//转换成拼音简码
					cusName = "%" + cusName + "%";
					String queryStr = "from SubContractor as model where model.brief like ? or model.name like ?";
					List<SubContractor> retList = subContorMgr.findPagedAllByHQL(queryStr, 1, 30, cusName, cusName);
					if(retList != null){
						for(SubContractor subContractor : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", subContractor.getName());
							jsonArray.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SubContractServlet-->case 0", e);
				}else{
					log.error("error in SubContractServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 1:	//录入转包记录
			JSONObject retJSON1 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//委托单Id
				String SubContractor = req.getParameter("SubContractor");	//转包方名称
				String SubContractDate = req.getParameter("SubContractDate");	//转包日期
				String Handler = req.getParameter("Handler");	//转包人姓名
				String ReceiveDate = req.getParameter("ReceiveDate");	//接收日期
				String Receiver = req.getParameter("Receiver");	//接收人姓名
				String Remark = req.getParameter("Remark");	//备注信息
				String TotalFee = req.getParameter("TotalFee");	//转包费用
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("委托单未指定！");
				}
				if(SubContractor == null || SubContractor.trim().length() == 0){
					throw new Exception("转包方未指定！");
				}

				List<SubContractor> subContorRetList = subContorMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
				if(subContorRetList == null || subContorRetList.size() == 0){
					throw new Exception("转包方不存在！");
				}
				
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("委托单不存在！");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("委托单已完工或已注销，不能新增转包记录！");
				}
				if(cSheet.getSubcontract()){
					throw new Exception("该委托单不是转包业务！");
				}
				
				if(!(cSheet.getCommissionType()==3||cSheet.getCommissionType()==4||cSheet.getCommissionType()==5)){
					throw new Exception("该委托单的委托形式不能转包！");
				}
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				SubContract subCont = new SubContract();
				
				subCont.setCommissionSheet(cSheet);	//委托单
				subCont.setSubContractor(subContorRetList.get(0));//转包方
				subCont.setLastEditTime(today);	//最后一次编辑时间
				subCont.setSysUserByLastEditorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//最后一次编辑者
				subCont.setStatus(0);	//状态：正常
				subCont.setAttachment(UIDUtil.get22BitUID());	//附件文件集名称
				if(TotalFee != null && TotalFee.trim().length() > 0){
					subCont.setTotalFee(Double.parseDouble(TotalFee));
				}
				
				if(SubContractDate != null && SubContractDate.trim().length() > 0){
					subCont.setSubContractDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(SubContractDate.trim()).getTime()));	//转包时间
				}
				if(ReceiveDate != null && ReceiveDate.trim().length() > 0){
					subCont.setReceiveDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(ReceiveDate.trim()).getTime()));	//接收时间
				}
				UserManager userMgr = new UserManager();
				if(Handler != null && Handler.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Handler,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("转包人不存在！");
					}
					subCont.setSysUserByHandlerId(userList.get(0));	//转包人
				}
				if(Receiver != null && Receiver.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Receiver,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("接收人不存在！");
					}
					subCont.setSysUserByReceiverId(userList.get(0));//接收人
				}
				if(Remark != null && Remark.length() > 0){
					subCont.setRemark(Remark);	//备注信息
				}
				
				if(subContMgr.save(subCont)){	//存数据库
					retJSON1.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("录入转包记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SubContractServlet-->case 1", e);
				}else{
					log.error("error in SubContractServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//修改转包记录
			JSONObject retJSON2 = new JSONObject();
			try {
				String SubContractId = req.getParameter("SubContractId");	//委托记录Id
				String SubContractor = req.getParameter("SubContractor");	//转包方名称
				String SubContractDate = req.getParameter("SubContractDate");	//转包日期
				String Handler = req.getParameter("Handler");	//转包人姓名
				String ReceiveDate = req.getParameter("ReceiveDate");	//接收日期
				String Receiver = req.getParameter("Receiver");	//接收人姓名
				String Remark = req.getParameter("Remark");	//备注信息
				String TotalFee = req.getParameter("TotalFee");	//转包费用
				if(SubContractId == null || SubContractId.trim().length() == 0){
					throw new Exception("委托记录未指定！");
				}
				if(SubContractor == null || SubContractor.trim().length() == 0){
					throw new Exception("转包方未指定！");
				}

				List<SubContractor> subContorRetList = subContorMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
				if(subContorRetList == null || subContorRetList.size() == 0){
					throw new Exception("转包方不存在！");
				}
				Timestamp today = new Timestamp(System.currentTimeMillis());
				SubContract subCont = subContMgr.findById(Integer.parseInt(SubContractId));	//查找委托记录
				if(subCont == null){
					throw new Exception("转包记录不存在！");
				}
				subCont.setSubContractor(subContorRetList.get(0));//转包方
				subCont.setLastEditTime(today);	//最后一次编辑时间
				subCont.setSysUserByLastEditorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//最后一次编辑者
				
				if(SubContractDate != null && SubContractDate.trim().length() > 0){
					subCont.setSubContractDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(SubContractDate.trim()).getTime()));	//转包时间
				}
				if(ReceiveDate != null && ReceiveDate.trim().length() > 0){
					subCont.setReceiveDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(ReceiveDate.trim()).getTime()));	//接收时间
				}
				UserManager userMgr = new UserManager();
				if(Handler != null && Handler.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Handler,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("转包人不存在！");
					}
					subCont.setSysUserByHandlerId(userList.get(0));	//转包人
				}
				if(Receiver != null && Receiver.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Receiver,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("接收人不存在！");
					}
					subCont.setSysUserByReceiverId(userList.get(0));//接收人
				}
				if(Remark != null && Remark.length() > 0){
					subCont.setRemark(Remark);	//备注信息
				}
				if(TotalFee != null && TotalFee.trim().length() > 0){
					subCont.setTotalFee(Double.parseDouble(TotalFee));
				}else{
					subCont.setTotalFee(null);
				}
				
				if(subContMgr.update(subCont)){	//存数据库
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("录入转包记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SubContractServlet-->case 2", e);
				}else{
					log.error("error in SubContractServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: // 查找一个委托单的转包信息
			JSONObject retJSON3 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单不存在！");
				}
				List<SubContract> retList = subContMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				if(retList != null && retList.size() > 0){
					JSONArray jsonArrayTemp = new JSONArray();
					for(SubContract sc : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionSheetId", Integer.parseInt(CommissionId));	//委托单ID
						jsonObj.put("SubContractId", sc.getId());
						jsonObj.put("SubContractorName", sc.getSubContractor().getName());	//转包方单位名称
						jsonObj.put("SubContractorContactor", sc.getSubContractor().getContactor());	//转包方联系人
						jsonObj.put("SubContractorContactorTel", sc.getSubContractor().getContactorTel()==null?"":sc.getSubContractor().getContactorTel());	//联系人电话
						jsonObj.put("SubContractDate", sc.getSubContractDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getSubContractDate()));	//转包时间
						jsonObj.put("Handler", sc.getSysUserByHandlerId()==null?"":sc.getSysUserByHandlerId().getName());//转包人名称
						jsonObj.put("ReceiveDate", sc.getReceiveDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getReceiveDate()));	//接收时间
						jsonObj.put("Receiver", sc.getSysUserByReceiverId()==null?"":sc.getSysUserByReceiverId().getName());	//接收人姓名
						jsonObj.put("Remark", sc.getRemark()==null?"":sc.getRemark());	//备注信息
						jsonObj.put("LastEditor", sc.getSysUserByLastEditorId().getName());	//最后一次编辑人员
						jsonObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(sc.getLastEditTime()));	//最后一次编辑时间
						jsonObj.put("Attachment", sc.getAttachment()==null?"":sc.getAttachment());	//附件文件集名称
						jsonObj.put("TotalFee", sc.getTotalFee()==null?"":sc.getTotalFee());	//转包费用
						jsonArrayTemp.put(jsonObj);
					}
					
					retJSON3.put("total", retList.size());
					retJSON3.put("rows", jsonArrayTemp);
				}else{
					throw new Exception("");
				}
			} catch (Exception e){
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SubContractServlet-->case 3", e);
				}else{
					log.error("error in SubContractServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4://查询所有转包业务
			JSONObject retJSON4 = new JSONObject();
			try {
				String SubContractor = req.getParameter("SubContractor");
				String StartTime = req.getParameter("DateFrom");
				String EndTime = req.getParameter("DateEnd");
				String CustomerName = req.getParameter("CustomerName");
				String Code = req.getParameter("Code");
				String ApplianceName = req.getParameter("ApplianceName");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				String queryStr = "from SubContract as model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(SubContractor!=null&&!SubContractor.equals("")){
					queryStr = queryStr + " and model.subContractor.name like ?";
					keys.add("%" + SubContractor + "&");
				}
				if(StartTime!=null&&StartTime!=""&&EndTime!=null&&EndTime!="")
				{
					Timestamp Start = Timestamp.valueOf(StartTime + " 0:0:0");
					Timestamp End = Timestamp.valueOf(EndTime + " 23:59:59");
					
					queryStr = queryStr + "model.subContractDate >= ? and model.subContractDate <= ?";
					keys.add(Start);
					keys.add(End);					
				}
				if(Code!=null&&!Code.equals("")){
					queryStr = queryStr + " and model.commissionSheet.code like ?";
					keys.add("%" + Code + "%");
				}
				if(CustomerName!=null&&!CustomerName.equals("")){
					queryStr = queryStr + " and model.commissionSheet.customerName like ?";
					keys.add("%" + CustomerName + "%");
				}
				if(ApplianceName!=null&&!ApplianceName.equals("")){
					queryStr = queryStr + " and model.commissionSheet.applianceName like ?";
					keys.add("%" + ApplianceName + "%");
				}
				List<SubContract> retList = subContMgr.findPagedAllByHQL(queryStr, page, rows, keys);
				if(retList != null && retList.size() > 0){
					JSONArray jsonArrayTemp = new JSONArray();
					for(SubContract sc : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionSheetId", sc.getCommissionSheet().getId());	//委托单ID
						jsonObj.put("Code", sc.getCommissionSheet().getCode());
						jsonObj.put("SubContractId", sc.getId());
						jsonObj.put("SubContractorName", sc.getSubContractor().getName());	//转包方单位名称
						jsonObj.put("SubContractorContactor", sc.getSubContractor().getContactor());	//转包方联系人
						jsonObj.put("SubContractorContactorTel", sc.getSubContractor().getContactorTel()==null?"":sc.getSubContractor().getContactorTel());	//联系人电话
						jsonObj.put("SubContractDate", sc.getSubContractDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getSubContractDate()));	//转包时间
						jsonObj.put("Handler", sc.getSysUserByHandlerId()==null?"":sc.getSysUserByHandlerId().getName());//转包人名称
						jsonObj.put("ReceiveDate", sc.getReceiveDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getReceiveDate()));	//接收时间
						jsonObj.put("Receiver", sc.getSysUserByReceiverId()==null?"":sc.getSysUserByReceiverId().getName());	//接收人姓名
						jsonObj.put("Remark", sc.getRemark()==null?"":sc.getRemark());	//备注信息
						jsonObj.put("LastEditor", sc.getSysUserByLastEditorId().getName());	//最后一次编辑人员
						jsonObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(sc.getLastEditTime()));	//最后一次编辑时间
						jsonObj.put("Attachment", sc.getAttachment()==null?"":sc.getAttachment());	//附件文件集名称
						jsonObj.put("TotalFee", sc.getTotalFee()==null?"":sc.getTotalFee());	//转包费用
						jsonArrayTemp.put(jsonObj);
					}
					
					retJSON4.put("total", retList.size());
					retJSON4.put("rows", jsonArrayTemp);
				}else{
					throw new Exception("");
				}
			} catch (Exception e){
				try {
					retJSON4.put("total", 0);
					retJSON4.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SubContractServlet-->case 4", e);
				}else{
					log.error("error in SubContractServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		}
	}

}
