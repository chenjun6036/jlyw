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
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
		
		switch(method){
		case 1 : 	//����֤��Ų�ѯί�е�
			JSONObject retJSON1 = new JSONObject();
			try{
				String CertificateCode = req.getParameter("CertificateCode");
				if(CertificateCode == null || CertificateCode.trim().length() == 0){
					throw new Exception("������������֤��Ų���Ϊ�գ�");
				}
				List<Certificate> cList = new CertificateManager().findByPropertyBySort("lastEditTime", false, 
						new KeyValueWithOperator("certificateCode", CertificateCode, "="));
				if(cList.size() == 0){
					throw new Exception("�Ҳ�����֤��Ŷ�Ӧ��֤�飬��˶�֤����Ƿ���ȷ��");
				}
				Certificate c = cList.get(0);
				CommissionSheet cSheet = c.getCommissionSheet();
				retJSON1.put("IsOK", true);
				
				retJSON1.put("OriginalRecordId", c.getOriginalRecord().getId());	//ԭʼ��¼ID
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("CommissionId", cSheet.getId());	//ί�е�ID
				jsonObj.put("CommissionCode", cSheet.getCode());
				jsonObj.put("CommissionPwd", cSheet.getPwd());
				jsonObj.put("CommissionStatus", cSheet.getStatus());	//ί�е�״̬
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
				jsonObj.put("ApplianceName", cSheet.getApplianceName());	//��������
				jsonObj.put("Model", cSheet.getApplianceModel());
				jsonObj.put("ApplianceCode", cSheet.getAppFactoryCode());	//�������
				jsonObj.put("ApplianceManageCode", cSheet.getAppManageCode());	//������
				jsonObj.put("Manufacturer", cSheet.getManufacturer());
				jsonObj.put("Quantity", cSheet.getQuantity());
				jsonObj.put("Mandatory", cSheet.getMandatory()?1:0);	//ǿ�Ƽ춨
				jsonObj.put("Ness", cSheet.getUrgent()?1:0);			//�Ƿ�Ӽ�
				jsonObj.put("Appearance", cSheet.getAppearance());	//��۸���
				jsonObj.put("OtherRequirements", cSheet.getOtherRequirements());	//����Ҫ��
				jsonObj.put("Status", cSheet.getStatus());	//ί�е�״̬
				
				jsonObj.put("TotalFee", cSheet.getTotalFee());	//ί�е��ܼƷ���
				
				retJSON1.put("CommissionObj", jsonObj);
			}catch(Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("��ѯ֤����Ϣʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in RemakeCertificateServlet-->case 1", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2 : 	//��ѯһ��ί�е������±���֤��ļ�¼
			JSONObject retJSON2 = new JSONObject();
			try{
				String CommissionId = req.getParameter("CommissionId");
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("������������");
				}
				int total = remakeMgr.getTotalCount(new KeyValueWithOperator("originalRecord.commissionSheet.id", Integer.parseInt(CommissionId), "="));
				List<RemakeCertificate> rList = remakeMgr.findByPropertyBySort("createTime", true, new KeyValueWithOperator("originalRecord.commissionSheet.id", Integer.parseInt(CommissionId), "="));
				JSONArray jsonArray = new JSONArray();
				for(RemakeCertificate rc : rList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("Id", rc.getId());
					jsonObj.put("CertificateCode", rc.getCertificateCode());	//֤����
					jsonObj.put("OriginalRecordId", rc.getOriginalRecord().getId());	//ԭʼ��¼Id
					jsonObj.put("CreateTime", DateTimeFormatUtil.DateTimeFormat.format(rc.getCreateTime()));	//����ʱ��
					jsonObj.put("CreatorId", rc.getSysUserByCreatorId().getId());	//������Id
					jsonObj.put("CreatorName", rc.getSysUserByCreatorId().getName());	//����������
					jsonObj.put("CreateRemark", rc.getCreateRemark()==null?"":rc.getCreateRemark());	//������ע
					jsonObj.put("ReceiverId", rc.getSysUserByReceiverId().getId());	//������Id
					jsonObj.put("ReceiverName", rc.getSysUserByReceiverId().getName());	//����������
					jsonObj.put("FinishTime", rc.getFinishTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getFinishTime()));	//���ʱ��
					jsonObj.put("FinishRemark", rc.getFinishRemark()==null?"":rc.getFinishRemark());	
					jsonObj.put("PassedTime", rc.getPassedTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getPassedTime()));	//ͨ��ʱ��
					
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in RemakeCertificateServlet-->case 2", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3 : 	//�������±���֤�������
			JSONObject retJSON3 = new JSONObject();
			try{
				String CertificateCode = req.getParameter("CertificateCode");
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String CreateRemark = req.getParameter("CreateRemark");
				
				if(CertificateCode == null || CertificateCode.length() == 0 ||
						OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
				}
				
				List<Certificate> cList = new CertificateManager().findByPropertyBySort("certificateCode", true, 
						new KeyValueWithOperator("certificateCode", CertificateCode, "="),
						new KeyValueWithOperator("originalRecord.id", Integer.parseInt(OriginalRecordId), "="));
				if(cList.size() == 0){
					throw new Exception("�Ҳ�����֤��Ŷ�Ӧ��֤�飬��˶�֤����Ƿ���ȷ��");
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
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
			}catch(Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("��ѯ֤����Ϣʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in RemakeCertificateServlet-->case 3", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4 : 	//��ѯ��ǰ��¼�û��ġ����±���֤�顯���� �б�
			JSONObject retJSON4 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String CertificateCode = req.getParameter("CertificateCode");	//ί�е���
				String CustomerName = req.getParameter("CustomerName");	//ί�е�λ
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//��ѯ����
				if(CertificateCode != null && CertificateCode.trim().length() > 0){
					CertificateCode = URLDecoder.decode(CertificateCode.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("certificateCode", "%"+CertificateCode+"%", "like"));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("originalRecord.commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				condList.add(new KeyValueWithOperator("sysUserByReceiverId.id", ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId(), "="));  //������
				condList.add(new KeyValueWithOperator("finishTime", null, "is null"));	//������δ��ɵ�
				
				int total = remakeMgr.getTotalCount(condList);
				List<RemakeCertificate> tRetList = remakeMgr.findPagedAllBySort(page, rows, "createTime", true, condList);
				for(RemakeCertificate rc : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CommissionCode", rc.getOriginalRecord().getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", rc.getOriginalRecord().getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", rc.getOriginalRecord().getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", rc.getOriginalRecord().getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CustomerContactor", rc.getOriginalRecord().getCommissionSheet().getCustomerContactor());	//ί�е�λ��ϵ��
					jsonObj.put("CustomerContactorTel", rc.getOriginalRecord().getCommissionSheet().getCustomerContactorTel());	//ί�е�λ��ϵ�˵绰
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(rc.getOriginalRecord().getCommissionSheet().getCommissionDate()));	//ί������
					
					jsonObj.put("Id", rc.getId());
					jsonObj.put("CertificateCode", rc.getCertificateCode());	//֤����
					jsonObj.put("OriginalRecordId", rc.getOriginalRecord().getId());	//ԭʼ��¼Id
					jsonObj.put("CreateTime", DateTimeFormatUtil.DateTimeFormat.format(rc.getCreateTime()));	//����ʱ��
					jsonObj.put("CreatorId", rc.getSysUserByCreatorId().getId());	//������Id
					jsonObj.put("CreatorName", rc.getSysUserByCreatorId().getName());	//����������
					jsonObj.put("CreateRemark", rc.getCreateRemark()==null?"":rc.getCreateRemark());	//������ע
					jsonObj.put("ReceiverId", rc.getSysUserByReceiverId().getId());	//������Id
					jsonObj.put("ReceiverName", rc.getSysUserByReceiverId().getName());	//����������
					jsonObj.put("FinishTime", rc.getFinishTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getFinishTime()));	//���ʱ��
					jsonObj.put("FinishRemark", rc.getFinishRemark()==null?"":rc.getFinishRemark());	
					jsonObj.put("PassedTime", rc.getPassedTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getPassedTime()));	//ͨ��ʱ��
					
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in RemakeCertificateServlet-->case 4", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5 :	//��ѯ��ǰ��¼�û������ġ����±���֤�顯���� �б�
			JSONObject retJSON5 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int page = 0;	//��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				String CertificateCode = req.getParameter("CertificateCode");	//ί�е���
				String CustomerName = req.getParameter("CustomerName");	//ί�е�λ
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//��ѯ����
				if(CertificateCode != null && CertificateCode.trim().length() > 0){
					CertificateCode = URLDecoder.decode(CertificateCode.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("certificateCode", "%"+CertificateCode+"%", "like"));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("originalRecord.commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				condList.add(new KeyValueWithOperator("sysUserByCreatorId.id", ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId(), "="));  //������
				condList.add(new KeyValueWithOperator("passedTime", null, "is null"));	//������δ��ɵ�
				
				int total = remakeMgr.getTotalCount(condList);
				List<RemakeCertificate> tRetList = remakeMgr.findPagedAllBySort(page, rows, "createTime", true, condList);
				for(RemakeCertificate rc : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("CommissionCode", rc.getOriginalRecord().getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", rc.getOriginalRecord().getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", rc.getOriginalRecord().getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", rc.getOriginalRecord().getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CustomerContactor", rc.getOriginalRecord().getCommissionSheet().getCustomerContactor());	//ί�е�λ��ϵ��
					jsonObj.put("CustomerContactorTel", rc.getOriginalRecord().getCommissionSheet().getCustomerContactorTel());	//ί�е�λ��ϵ�˵绰
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(rc.getOriginalRecord().getCommissionSheet().getCommissionDate()));	//ί������
					
					jsonObj.put("Id", rc.getId());
					jsonObj.put("CertificateCode", rc.getCertificateCode());	//֤����
					jsonObj.put("OriginalRecordId", rc.getOriginalRecord().getId());	//ԭʼ��¼Id
					jsonObj.put("CreateTime", DateTimeFormatUtil.DateTimeFormat.format(rc.getCreateTime()));	//����ʱ��
					jsonObj.put("CreatorId", rc.getSysUserByCreatorId().getId());	//������Id
					jsonObj.put("CreatorName", rc.getSysUserByCreatorId().getName());	//����������
					jsonObj.put("CreateRemark", rc.getCreateRemark()==null?"":rc.getCreateRemark());	//������ע
					jsonObj.put("ReceiverId", rc.getSysUserByReceiverId().getId());	//������Id
					jsonObj.put("ReceiverName", rc.getSysUserByReceiverId().getName());	//����������
					jsonObj.put("FinishTime", rc.getFinishTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getFinishTime()));	//���ʱ��
					jsonObj.put("FinishRemark", rc.getFinishRemark()==null?"":rc.getFinishRemark());	
					jsonObj.put("PassedTime", rc.getPassedTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(rc.getPassedTime()));	//ͨ��ʱ��
					
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in RemakeCertificateServlet-->case 5", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6 : 	//ȷ���������
			JSONObject retJSON6 = new JSONObject();
			try{
				String RemakeCertificateId = req.getParameter("Id");
				String FinishRemark = req.getParameter("FinishRemark");
				if(RemakeCertificateId == null || RemakeCertificateId.trim().length() == 0){
					throw new Exception("������������");
				}
				RemakeCertificate rc = remakeMgr.findById(Integer.parseInt(RemakeCertificateId));
				if(rc == null){
					throw new Exception("�Ҳ���ָ�������±���֤�������");
				}
				if(rc.getFinishTime() != null){
					throw new Exception("����������ɣ������ظ�������");
				}
				rc.setFinishRemark(FinishRemark);
				rc.setFinishTime(new Timestamp(System.currentTimeMillis()));
				if(remakeMgr.update(rc)){
					retJSON6.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			}catch(Exception e){
				try {
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("ȷ���������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in RemakeCertificateServlet-->case 6", e);
				}else{
					log.error("error in RemakeCertificateServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7 : 	//���������¼��
			JSONObject retJSON7 = new JSONObject();
			try{
				String RemakeCertificateId = req.getParameter("Id");
				String ExecuteResult = req.getParameter("ExecuteResult");
				String CreateRemark = req.getParameter("CreateRemark");
				if(RemakeCertificateId == null || RemakeCertificateId.trim().length() == 0 ||
						ExecuteResult == null || ExecuteResult.trim().length() == 0){
					throw new Exception("������������");
				}
				RemakeCertificate rc = remakeMgr.findById(Integer.parseInt(RemakeCertificateId));
				if(rc == null){
					throw new Exception("�Ҳ���ָ�������±���֤�������");
				}
				if(rc.getFinishTime() == null){
					throw new Exception("��������δ��ɣ�����¼���������");
				}
				if(rc.getPassedTime() != null){
					throw new Exception("�����������ͨ���������ٴ�¼���������");
				}
				if(Integer.parseInt(ExecuteResult) == 1){	//���ͨ��
					rc.setPassedTime(new Timestamp(System.currentTimeMillis()));
				}else{	//���±���
					rc.setFinishTime(null);
				}
				rc.setCreateRemark(CreateRemark);
				if(remakeMgr.update(rc)){
					retJSON7.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			}catch(Exception e){
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("���������¼��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
