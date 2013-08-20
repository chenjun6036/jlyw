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
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		OverdueManager overdueMgr = new OverdueManager();
		switch (method) {
		case 0: // ����һ��ί�е��ĳ���������Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
				List<Overdue> retList = overdueMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				if(retList != null && retList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(Overdue o:retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("OverdueId", o.getId());
						jsonObj.put("DelayDays", o.getDelayDays());	//��Ҫ��������
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//����������
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//����ʱ��
						jsonObj.put("Reason", o.getReason().getReason());	//����ԭ��
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//���ڰ�����
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//������
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//����ʱ��
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//����ע��Ϣ
						
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OverdueServlet-->case 0", e);
				}else{
					log.error("error in OverdueServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//��������������
			JSONObject retJSON1 = new JSONObject();
			try {
				String OverdueId = req.getParameter("OverdueId");	//��������Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//��������0��ͨ����1ͨ��
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//����ı�ע��Ϣ
				
				if(OverdueId == null || OverdueId.trim().length() == 0){
					throw new Exception("�ó������뵥�����ڣ�");
				}
				Overdue o = overdueMgr.findById(Integer.parseInt(OverdueId));
				if(o != null){
//					if(!o.getSysUserByExecutorId().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){
//						throw new Exception("��ǰ��¼�û���ó�������İ����˲�һ�£�");
//					}
					if(o.getExecuteTime() != null){
						throw new Exception("�ó��������Ѿ������������ظ�������");
					}
					if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(o.getCommissionSheet().getStatus())){
						throw new Exception("��ί�е����깤����ע��������ִ�г���������");
					}
					o.setExecuteMsg(ExecuteMsg);
					o.setExecuteResult(Integer.parseInt(ExecutorResult)>0?true:false);
					o.setExecuteTime(new Timestamp(System.currentTimeMillis()));
					o.setSysUserByExecutorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//������
					if(overdueMgr.overdueHandle(o, o.getCommissionSheet())){
						retJSON1.put("IsOK", true);
						return;
					}else{
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}
				}
				throw new Exception("�ó������뵥�����ڣ�");
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("��������������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OverdueServlet-->case 1", e);
				}else{
					log.error("error in OverdueServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:		//��������
			JSONObject retJSON2 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//ί�е�Id
				String Reason = req.getParameter("Reason");	//����ԭ��
//				String ExecutorName = req.getParameter("ExecutorName");	//������Ա����
				String DelayDays = req.getParameter("DelayDays");	//��������
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
//				if(ExecutorName == null || ExecutorName.trim().length() == 0){
//					throw new Exception("������ԱΪ�գ�");
//				}
				if(DelayDays == null || DelayDays.trim().length() == 0){
					throw new Exception("��������Ϊ�գ�");
				}
				
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("ί�е������ڣ�");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("��ί�е����깤����ע���������ٴ��ύ�������룡");
				}
//				List<SysUser> userList = new UserManager().findByVarProperty(new KeyValueWithOperator("name", ExecutorName, "="), new KeyValueWithOperator("status", 0, "="));
//				if(userList.size() == 0){
//					throw new Exception("������Ա�����ڻ򲻿��ã�");
//				}
//				SysUser user = userList.get(0);
//				if(user.getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){
//					throw new Exception("�������������Ա������ͬһ���ˣ�");
//				}
				Timestamp today = new Timestamp(System.currentTimeMillis());
				//�泬������
				Overdue r = new Overdue();
//				r.setSysUserByExecutorId(user);	//������
				r.setSysUserByRequesterId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//������
				r.setApplyTime(today);	//����ʱ��
				r.setCommissionSheet(cSheet);	//ί�е�
				r.setDelayDays(Integer.parseInt(DelayDays.trim()));//��������
				
				//�泬��ԭ��
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_Overdue, "="));//���ҳ���ԭ��
				if(rList.size() > 0){	//����ԭ��
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					r.setReason(reason);	//����ԭ��
				}else{	//�½�ԭ��
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(FlagUtil.ReasonType.Type_Overdue);	//����
					rMgr.save(reason);
					r.setReason(reason);	//����ԭ��
				}

				if(overdueMgr.save(r)){
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("���������ύʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OverdueServlet-->case 2", e);
				}else{
					log.error("error in OverdueServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //��ѯ�������������б�
			JSONObject retJSON3 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String CommissionNumber = req.getParameter("CommissionNumber");	//ί�е���
				String CustomerName = req.getParameter("CustomerName");	//ί�е�λ
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//��ѯ����
				if(CommissionNumber != null && CommissionNumber.trim().length() > 0){
					CommissionNumber = URLDecoder.decode(CommissionNumber.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("commissionSheet.code", CommissionNumber, "="));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				condList.add(new KeyValueWithOperator("commissionSheet.status", 3, "<"));	//ί�е���δ�깤����δע����
//				condList.add(new KeyValueWithOperator("sysUserByExecutorId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //������
				condList.add(new KeyValueWithOperator("executeTime", null, "is null"));	//����������δ��ɵ�
				
				int total = overdueMgr.getTotalCount(condList);
				List<Overdue> tRetList = overdueMgr.findPagedAllBySort(page, rows, "applyTime", true, condList);
				for(Overdue o : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("OverdueId", o.getId());
					
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//ί��ʱ��
					jsonObj.put("PromiseDate", o.getCommissionSheet().getPromiseDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getPromiseDate()));	//��ŵ�������					
					
					jsonObj.put("DelayDays", o.getDelayDays());	//��Ҫ��������
					jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//����������
					jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
					jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//����ʱ��
					jsonObj.put("Reason", o.getReason().getReason());	//����ԭ��
					jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//���ڰ�����
					jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
//					jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//������
//					jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//����ʱ��
//					jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//����ע��Ϣ
					
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
