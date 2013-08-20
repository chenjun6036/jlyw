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
 * ����
 * @author Administrator
 *
 */
public class WithdrawServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(WithdrawServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		WithdrawManager withdrawMgr = new WithdrawManager();
		switch (method) {
		case 0: // ����һ��ί�е�������������Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
				List<Withdraw> retList = withdrawMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				if(retList != null && retList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(Withdraw w:retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("WithdrawId", w.getId());
						jsonObj.put("WithdrawNumber", w.getNumber());	//��������
						jsonObj.put("WithdrawDesc", w.getDescription()==null?"":w.getDescription());	//��������
						jsonObj.put("RequesterId", w.getSysUserByRequesterId().getId());	//����������
						jsonObj.put("RequesterName", w.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(w.getRequestTime()));	//����ʱ��
						jsonObj.put("Reason", w.getReason().getReason());	//����ԭ��
						jsonObj.put("ExecutorId", w.getSysUserByExecutorId()==null?null:w.getSysUserByExecutorId().getId());	//����������
						jsonObj.put("ExecutorName", w.getSysUserByExecutorId()==null?"":w.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", w.getExecuteResult()==null?-1:(w.getExecuteResult()?1:0));	//������
						jsonObj.put("ExecuteTime", w.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(w.getExecuteTime()));	//����ʱ��
						jsonObj.put("ExecuteMsg", w.getExecuteMsg()==null?"":w.getExecuteMsg());	//����ע��Ϣ
						jsonObj.put("WithdrawDate", w.getWithdrawDate()==null?"":DateTimeFormatUtil.DateFormat.format(w.getWithdrawDate()));	//����ʱ��
						jsonObj.put("Location", w.getLocation()==null?"":w.getLocation());	//���λ��
						
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
					log.debug("exception in WithdrawServlet-->case 0", e);
				}else{
					log.error("error in WithdrawServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//��������������
			JSONObject retJSON1 = new JSONObject();
			try {
				String WithdrawId = req.getParameter("WithdrawId");	//��������Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//��������0��ͨ����1ͨ��
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//����ı�ע��Ϣ
				String WithdrawDate = req.getParameter("WithdrawDate");	//��������
				String Location = req.getParameter("Location");	//���λ��
				
				if(WithdrawId == null || WithdrawId.trim().length() == 0){
					throw new Exception("���������뵥�����ڣ�");
				}
				Withdraw w = withdrawMgr.findById(Integer.parseInt(WithdrawId));
				if(w != null){
//					if(!w.getSysUserByExecutorId().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){
//						throw new Exception("��ǰ��¼�û������������İ����˲�һ�£�");
//					}
					if(w.getExecuteTime() != null){
						throw new Exception("�����������Ѿ������������ظ�������");
					}
					if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(w.getCommissionSheet().getStatus())){
						throw new Exception("��ί�е����깤����ע��������ִ������������");
					}
					w.setExecuteMsg(ExecuteMsg);
					w.setExecuteResult(Integer.parseInt(ExecutorResult)>0?true:false);
					w.setExecuteTime(new Timestamp(System.currentTimeMillis()));
					w.setSysUserByExecutorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//������
					if(WithdrawDate != null && WithdrawDate.trim().length() > 0){
						w.setWithdrawDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(WithdrawDate.trim()).getTime()));
					}
					w.setLocation(Location);	//���λ��
					if(withdrawMgr.update(w)){
						retJSON1.put("IsOK", true);
						return;
					}else{
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}
				}
				throw new Exception("���������뵥�����ڣ�");
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("��������������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in WithdrawServlet-->case 1", e);
				}else{
					log.error("error in WithdrawServlet-->case 1", e);
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
				String WithdrawNumber = req.getParameter("WithdrawNumber");	//��������
				String WithdrawDesc = req.getParameter("WithdrawDesc");	//������Ʒ����
				
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
//				if(ExecutorName == null || ExecutorName.trim().length() == 0){
//					throw new Exception("������ԱΪ�գ�");
//				}
				if(WithdrawNumber == null || WithdrawNumber.trim().length() == 0){
					throw new Exception("������Ʒ����Ϊ�գ�");
				}
				
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("ί�е������ڣ�");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("��ί�е����깤����ע���������ٴ��ύ�������룡");
				}
				
				//�ж��������������Ƿ�С��ί�е���������������������������С��0
				String queryString = "select sum(w.number) from Withdraw as w where w.commissionSheet.id=? and ( w.executeResult is null or w.executeResult=? )";	//������ͨ���ĺ�δ��������������
				List<Long> iRetList = withdrawMgr.findByHQL(queryString, cSheet.getId(), true);
				Integer count = (iRetList.get(0)==null)?0:iRetList.get(0).intValue();
				if(count == null){
					count = 0;
				}
				if(count + Integer.parseInt(WithdrawNumber.trim()) > cSheet.getQuantity()){
					throw new Exception(String.format("����������������'%s'���ܴ���ί�е�����������'%s'��", count+Integer.parseInt(WithdrawNumber), cSheet.getQuantity()));
				}
				if(count + Integer.parseInt(WithdrawNumber.trim()) < 0 ){
					throw new Exception(String.format("����������������'%s'����С��0��", count+Integer.parseInt(WithdrawNumber)));
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
				//����������
				Withdraw w = new Withdraw();
//				w.setSysUserByExecutorId(user);	//������
				w.setSysUserByRequesterId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//������
				w.setRequestTime(today);	//����ʱ��
				w.setCommissionSheet(cSheet);	//ί�е�
				w.setDescription(WithdrawDesc);	//������Ʒ����
				
				w.setNumber(Integer.parseInt(WithdrawNumber.trim()));//������Ʒ����
				
				//������ԭ��
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_Withdraw, "="));//��������ԭ��
				if(rList.size() > 0){	//����ԭ��
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					w.setReason(reason);	//����ԭ��
				}else{	//�½�ԭ��
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(FlagUtil.ReasonType.Type_Withdraw);	//����
					rMgr.save(reason);
					w.setReason(reason);	//����ԭ��
				}

				if(withdrawMgr.save(w)){
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
					log.debug("exception in WithdrawServlet-->case 2", e);
				}else{
					log.error("error in WithdrawServlet-->case 2", e);
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
				
				int total = withdrawMgr.getTotalCount(condList);
				List<Withdraw> tRetList = withdrawMgr.findPagedAllBySort(page, rows, "requestTime", true, condList);
				for(Withdraw w : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("WithdrawId", w.getId());
					
					jsonObj.put("CommissionCode", w.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", w.getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", w.getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", w.getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(w.getCommissionSheet().getCommissionDate()));	//ί��ʱ��
					jsonObj.put("PromiseDate", w.getCommissionSheet().getPromiseDate()==null?"":DateTimeFormatUtil.DateFormat.format(w.getCommissionSheet().getPromiseDate()));	//��ŵ�������
					
					jsonObj.put("WithdrawNumber", w.getNumber());	//��������
					jsonObj.put("WithdrawDesc", w.getDescription()==null?"":w.getDescription());	//��������
					jsonObj.put("RequesterId", w.getSysUserByRequesterId().getId());	//����������
					jsonObj.put("RequesterName", w.getSysUserByRequesterId().getName());
					jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(w.getRequestTime()));	//����ʱ��
					jsonObj.put("Reason", w.getReason().getReason());	//����ԭ��
					jsonObj.put("ExecutorId", w.getSysUserByExecutorId()==null?null:w.getSysUserByExecutorId().getId());	//����������
					jsonObj.put("ExecutorName", w.getSysUserByExecutorId()==null?"":w.getSysUserByExecutorId().getName());
					jsonObj.put("Location", w.getLocation()==null?"":w.getLocation());	//���λ��
					
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
