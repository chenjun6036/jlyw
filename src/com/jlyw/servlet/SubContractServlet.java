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
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		SubContractorManager subContorMgr = new SubContractorManager();	//ת����������Mgr
		SubContractManager subContMgr = new SubContractManager();		//ת����¼������Mgr
		switch (method) {
		case 0: // ��ѯ ת������λ����
			JSONArray jsonArray = new JSONArray();
			try {
				String cusNameStr = req.getParameter("QueryName");
				if(cusNameStr != null && cusNameStr.trim().length() > 0){
					String cusName =  new String(cusNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//���URL����������������
					//cusName = LetterUtil.String2Alpha(cusName);	//ת����ƴ������
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractServlet-->case 0", e);
				}else{
					log.error("error in SubContractServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 1:	//¼��ת����¼
			JSONObject retJSON1 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//ί�е�Id
				String SubContractor = req.getParameter("SubContractor");	//ת��������
				String SubContractDate = req.getParameter("SubContractDate");	//ת������
				String Handler = req.getParameter("Handler");	//ת��������
				String ReceiveDate = req.getParameter("ReceiveDate");	//��������
				String Receiver = req.getParameter("Receiver");	//����������
				String Remark = req.getParameter("Remark");	//��ע��Ϣ
				String TotalFee = req.getParameter("TotalFee");	//ת������
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				if(SubContractor == null || SubContractor.trim().length() == 0){
					throw new Exception("ת����δָ����");
				}

				List<SubContractor> subContorRetList = subContorMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
				if(subContorRetList == null || subContorRetList.size() == 0){
					throw new Exception("ת���������ڣ�");
				}
				
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("ί�е������ڣ�");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("ί�е����깤����ע������������ת����¼��");
				}
				if(cSheet.getSubcontract()){
					throw new Exception("��ί�е�����ת��ҵ��");
				}
				
				if(!(cSheet.getCommissionType()==3||cSheet.getCommissionType()==4||cSheet.getCommissionType()==5)){
					throw new Exception("��ί�е���ί����ʽ����ת����");
				}
				
				Timestamp today = new Timestamp(System.currentTimeMillis());
				SubContract subCont = new SubContract();
				
				subCont.setCommissionSheet(cSheet);	//ί�е�
				subCont.setSubContractor(subContorRetList.get(0));//ת����
				subCont.setLastEditTime(today);	//���һ�α༭ʱ��
				subCont.setSysUserByLastEditorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//���һ�α༭��
				subCont.setStatus(0);	//״̬������
				subCont.setAttachment(UIDUtil.get22BitUID());	//�����ļ�������
				if(TotalFee != null && TotalFee.trim().length() > 0){
					subCont.setTotalFee(Double.parseDouble(TotalFee));
				}
				
				if(SubContractDate != null && SubContractDate.trim().length() > 0){
					subCont.setSubContractDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(SubContractDate.trim()).getTime()));	//ת��ʱ��
				}
				if(ReceiveDate != null && ReceiveDate.trim().length() > 0){
					subCont.setReceiveDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(ReceiveDate.trim()).getTime()));	//����ʱ��
				}
				UserManager userMgr = new UserManager();
				if(Handler != null && Handler.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Handler,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("ת���˲����ڣ�");
					}
					subCont.setSysUserByHandlerId(userList.get(0));	//ת����
				}
				if(Receiver != null && Receiver.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Receiver,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("�����˲����ڣ�");
					}
					subCont.setSysUserByReceiverId(userList.get(0));//������
				}
				if(Remark != null && Remark.length() > 0){
					subCont.setRemark(Remark);	//��ע��Ϣ
				}
				
				if(subContMgr.save(subCont)){	//�����ݿ�
					retJSON1.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("¼��ת����¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractServlet-->case 1", e);
				}else{
					log.error("error in SubContractServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//�޸�ת����¼
			JSONObject retJSON2 = new JSONObject();
			try {
				String SubContractId = req.getParameter("SubContractId");	//ί�м�¼Id
				String SubContractor = req.getParameter("SubContractor");	//ת��������
				String SubContractDate = req.getParameter("SubContractDate");	//ת������
				String Handler = req.getParameter("Handler");	//ת��������
				String ReceiveDate = req.getParameter("ReceiveDate");	//��������
				String Receiver = req.getParameter("Receiver");	//����������
				String Remark = req.getParameter("Remark");	//��ע��Ϣ
				String TotalFee = req.getParameter("TotalFee");	//ת������
				if(SubContractId == null || SubContractId.trim().length() == 0){
					throw new Exception("ί�м�¼δָ����");
				}
				if(SubContractor == null || SubContractor.trim().length() == 0){
					throw new Exception("ת����δָ����");
				}

				List<SubContractor> subContorRetList = subContorMgr.findByVarProperty(new KeyValueWithOperator("name",SubContractor.trim(),"="));
				if(subContorRetList == null || subContorRetList.size() == 0){
					throw new Exception("ת���������ڣ�");
				}
				Timestamp today = new Timestamp(System.currentTimeMillis());
				SubContract subCont = subContMgr.findById(Integer.parseInt(SubContractId));	//����ί�м�¼
				if(subCont == null){
					throw new Exception("ת����¼�����ڣ�");
				}
				subCont.setSubContractor(subContorRetList.get(0));//ת����
				subCont.setLastEditTime(today);	//���һ�α༭ʱ��
				subCont.setSysUserByLastEditorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//���һ�α༭��
				
				if(SubContractDate != null && SubContractDate.trim().length() > 0){
					subCont.setSubContractDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(SubContractDate.trim()).getTime()));	//ת��ʱ��
				}
				if(ReceiveDate != null && ReceiveDate.trim().length() > 0){
					subCont.setReceiveDate(new Timestamp(DateTimeFormatUtil.DateFormat.parse(ReceiveDate.trim()).getTime()));	//����ʱ��
				}
				UserManager userMgr = new UserManager();
				if(Handler != null && Handler.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Handler,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("ת���˲����ڣ�");
					}
					subCont.setSysUserByHandlerId(userList.get(0));	//ת����
				}
				if(Receiver != null && Receiver.trim().length() > 0){
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name",Receiver,"="));
					if(userList == null || userList.size() == 0){
						throw new Exception("�����˲����ڣ�");
					}
					subCont.setSysUserByReceiverId(userList.get(0));//������
				}
				if(Remark != null && Remark.length() > 0){
					subCont.setRemark(Remark);	//��ע��Ϣ
				}
				if(TotalFee != null && TotalFee.trim().length() > 0){
					subCont.setTotalFee(Double.parseDouble(TotalFee));
				}else{
					subCont.setTotalFee(null);
				}
				
				if(subContMgr.update(subCont)){	//�����ݿ�
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("¼��ת����¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractServlet-->case 2", e);
				}else{
					log.error("error in SubContractServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: // ����һ��ί�е���ת����Ϣ
			JSONObject retJSON3 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
				List<SubContract> retList = subContMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				if(retList != null && retList.size() > 0){
					JSONArray jsonArrayTemp = new JSONArray();
					for(SubContract sc : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CommissionSheetId", Integer.parseInt(CommissionId));	//ί�е�ID
						jsonObj.put("SubContractId", sc.getId());
						jsonObj.put("SubContractorName", sc.getSubContractor().getName());	//ת������λ����
						jsonObj.put("SubContractorContactor", sc.getSubContractor().getContactor());	//ת������ϵ��
						jsonObj.put("SubContractorContactorTel", sc.getSubContractor().getContactorTel()==null?"":sc.getSubContractor().getContactorTel());	//��ϵ�˵绰
						jsonObj.put("SubContractDate", sc.getSubContractDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getSubContractDate()));	//ת��ʱ��
						jsonObj.put("Handler", sc.getSysUserByHandlerId()==null?"":sc.getSysUserByHandlerId().getName());//ת��������
						jsonObj.put("ReceiveDate", sc.getReceiveDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getReceiveDate()));	//����ʱ��
						jsonObj.put("Receiver", sc.getSysUserByReceiverId()==null?"":sc.getSysUserByReceiverId().getName());	//����������
						jsonObj.put("Remark", sc.getRemark()==null?"":sc.getRemark());	//��ע��Ϣ
						jsonObj.put("LastEditor", sc.getSysUserByLastEditorId().getName());	//���һ�α༭��Ա
						jsonObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(sc.getLastEditTime()));	//���һ�α༭ʱ��
						jsonObj.put("Attachment", sc.getAttachment()==null?"":sc.getAttachment());	//�����ļ�������
						jsonObj.put("TotalFee", sc.getTotalFee()==null?"":sc.getTotalFee());	//ת������
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractServlet-->case 3", e);
				}else{
					log.error("error in SubContractServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4://��ѯ����ת��ҵ��
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
						jsonObj.put("CommissionSheetId", sc.getCommissionSheet().getId());	//ί�е�ID
						jsonObj.put("Code", sc.getCommissionSheet().getCode());
						jsonObj.put("SubContractId", sc.getId());
						jsonObj.put("SubContractorName", sc.getSubContractor().getName());	//ת������λ����
						jsonObj.put("SubContractorContactor", sc.getSubContractor().getContactor());	//ת������ϵ��
						jsonObj.put("SubContractorContactorTel", sc.getSubContractor().getContactorTel()==null?"":sc.getSubContractor().getContactorTel());	//��ϵ�˵绰
						jsonObj.put("SubContractDate", sc.getSubContractDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getSubContractDate()));	//ת��ʱ��
						jsonObj.put("Handler", sc.getSysUserByHandlerId()==null?"":sc.getSysUserByHandlerId().getName());//ת��������
						jsonObj.put("ReceiveDate", sc.getReceiveDate()==null?"":DateTimeFormatUtil.DateFormat.format(sc.getReceiveDate()));	//����ʱ��
						jsonObj.put("Receiver", sc.getSysUserByReceiverId()==null?"":sc.getSysUserByReceiverId().getName());	//����������
						jsonObj.put("Remark", sc.getRemark()==null?"":sc.getRemark());	//��ע��Ϣ
						jsonObj.put("LastEditor", sc.getSysUserByLastEditorId().getName());	//���һ�α༭��Ա
						jsonObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(sc.getLastEditTime()));	//���һ�α༭ʱ��
						jsonObj.put("Attachment", sc.getAttachment()==null?"":sc.getAttachment());	//�����ļ�������
						jsonObj.put("TotalFee", sc.getTotalFee()==null?"":sc.getTotalFee());	//ת������
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
