package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.OriginalRecordExcel;
import com.jlyw.hibernate.RemakeCertificate;
import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.TargetAppliance;
import com.jlyw.hibernate.TaskAssign;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.manager.AuthBackgroundRuningManager;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.InformationManager;
import com.jlyw.manager.OriginalRecordExcelManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.RemakeCertificateManager;
import com.jlyw.manager.SpecificationManager;
import com.jlyw.manager.StandardApplianceManager;
import com.jlyw.manager.StandardManager;
import com.jlyw.manager.StdTgtAppManager;
import com.jlyw.manager.TargetApplianceManager;
import com.jlyw.manager.TaskAssignManager;
import com.jlyw.manager.TgtAppSpecManager;
import com.jlyw.manager.TgtAppStdAppManager;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.VerifyAndAuthorizeManager;
import com.jlyw.service.AuthBackgroundRuningService;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.mongodbService.MongoPattern;

/**
 * ԭʼ��¼Servlet
 * @author Administrator
 *
 */
public class OriginalRecordServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(OriginalRecordServlet.class);
	private static Object MutexObjectOfNewCommissionSheet = new Object();		//���ڻ������
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		OriginalRecordManager oRecordMgr = new OriginalRecordManager();
		switch (method) {
		case 0: // ����һ��ί�е���ԭʼ��¼����Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				
				//�Ȳ���֤���Ų�Ϊ�� �Ĳ���֤�������򣨰�certificate.certificateCode����ֻ�ܲ��ҵ�certificate��Ϊnull�ļ�¼��
				List<OriginalRecord> oRecRetList = oRecordMgr.findByPropertyBySort("certificate.certificateCode", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), 
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("taskAssign.status", 1, "<>")
				);
				//�ٲ���û��֤���ŵļ�¼
				List<OriginalRecord> oRecRetList2 = oRecordMgr.findByPropertyBySort("id", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), 
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("certificate", null, "is null"),
						new KeyValueWithOperator("taskAssign.status", 1, "<>")
				);
				
				oRecRetList.addAll(oRecRetList2);
				
				if(oRecRetList != null && oRecRetList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(OriginalRecord o:oRecRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
						jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
						jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//�ܼ�����ID
						jsonObj.put("ApplianceStandardNameId", o.getTargetAppliance().getApplianceStandardName().getId());	//���߱�׼����Id
						jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//���߱�׼����
						jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼ģ���ļ���ŵ��ļ�������
						jsonObj.put("Model", o.getModel());
						jsonObj.put("Range", o.getRange());
						jsonObj.put("Accuracy", o.getAccuracy());
						jsonObj.put("Status", o.getStatus());	//ԭʼ��¼��״̬
						jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע��Ϣ
						jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//��������
						
						jsonObj.put("Manufacturer", o.getManufacturer());
						jsonObj.put("ApplianceCode", o.getApplianceCode());
						jsonObj.put("ManageCode", o.getManageCode());
						jsonObj.put("WorkType", o.getWorkType()==null?"":o.getWorkType());
						jsonObj.put("WorkLocation", o.getWorkLocation()==null?"":o.getWorkLocation());
						jsonObj.put("Temp", o.getTemp()==null?"":o.getTemp());
						jsonObj.put("Humidity", o.getHumidity()==null?"":o.getHumidity());
						jsonObj.put("Pressure", o.getPressure()==null?"":o.getPressure());
						jsonObj.put("Conclusion", o.getConclusion()==null?"":o.getConclusion());
						jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
						
						jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//�������ID
						jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//������Ŀ����
						
						jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//��/У��Ա
						jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//��/У��ԱId
						jsonObj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));	//��Ч��
//						jsonObj.put("TechnicalDocs", o.getTechnicalDocs()==null?"":o.getTechnicalDocs());
//						jsonObj.put("Standards", o.getStandards()==null?"":o.getStandards());
//						jsonObj.put("StandardAppliances", o.getStandardAppliances()==null?"":o.getStandardAppliances());
						jsonObj.put("StaffChecked", (o.getCertificate() != null && o.getCertificate().getSysUser() != null)?false:true);	//�춨Ա�Ƿ�˶������֤�鲻�Ǽ춨Ա�Լ����Ƶ������
						
						jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//ԭʼ��¼Excel�ļ�ID
						jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//ԭʼ��¼��xls�ļ�ID
						jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//ԭʼ��¼�汾��
						jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//ԭʼ��¼��PDF�ļ�ID
						jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//ԭʼ��¼xls�ļ����ļ���
						
						jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//֤���ļ�ID
						jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//֤���ļ���Word�ļ�ID
						jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//ԭʼ��¼�汾��
						jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//֤���ļ���PDF�ļ�ID
						jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//֤���ļ�Doc�ļ����ļ���
						jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//֤����
						
						jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//�������Ȩ ��¼ID
						jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//�������Ȩ ��¼�汾��
						jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
						jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//�������Ȩ ��¼��ԭʼ��¼PDF
						jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
						jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//�������Ȩ ��¼��֤���PDF
						jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//������
						jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
						jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//����ʱ��
						jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//������
						jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//���鱸ע
						if(o.getVerifyAndAuthorize() == null){
							jsonObj.put("AuthorizerId", "");	//��׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName","");
						}else{
							if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//ʵ��ִ�е���׼�ˣ�ǩ���ˣ�
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
							}else{
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//��׼�ˣ�ǩ���ˣ�
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
							}
						}
						jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//��׼ʱ��
						jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//��׼���
						jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//��׼��ע
						jsonObj.put("IsAuthBgRuning", (o.getVerifyAndAuthorize()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning())?true:false);	//�Ƿ��̨����
						
						//������Ϣ
						jsonObj.put("TestFee", o.getTestFee()==null?0:o.getTestFee());
						jsonObj.put("RepairFee", o.getRepairFee()==null?0:o.getRepairFee());
						jsonObj.put("MaterialFee", o.getMaterialFee()==null?0:o.getMaterialFee());
						jsonObj.put("CarFee", o.getCarFee()==null?0:o.getCarFee());
						jsonObj.put("DebugFee", o.getDebugFee()==null?0:o.getDebugFee());
						jsonObj.put("OtherFee", o.getOtherFee()==null?0:o.getOtherFee());
						jsonObj.put("TotalFee", o.getTotalFee()==null?0:o.getTotalFee());
						
						jsonArray.put(jsonObj);
					}
					
					retJSON.put("total", oRecRetList.size());
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
					log.debug("exception in OriginalRecordServlet-->case 0", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1: //Ϊί�е����ԭʼ��¼
			JSONObject retJSON1 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionId");	//ί�е�Id
//				String Number = req.getParameter("Number");	//ԭʼ��¼����
				String Model = req.getParameter("Model");	//����ͺ�
				String Range = req.getParameter("Range");	//������Χ
				String Accuracy = req.getParameter("Accuracy");	//���ȵȼ�
				String TargetApplianceId = req.getParameter("TargetApplianceId");	//�ܼ�����ID
				String TaskAssignId = req.getParameter("TaskId");	//�������ID
//				String Specifications = req.getParameter("Specifications");	//�����淶
//				String Standards = req.getParameter("Standards");	//������׼
//				String StandardAppliances = req.getParameter("StandardAppliances");	//��׼����
				String ApplianceName = req.getParameter("ApplianceName");	//��������
				String WorkType = req.getParameter("WorkType");	//��������
				String WorkLocation = req.getParameter("WorkLocation");	//�����ص�
				String Manufacturer = req.getParameter("Manufacturer");	//���쳧
				String ApplianceCode = req.getParameter("ApplianceCode");	//�������
				String ApplianceManageCode = req.getParameter("ApplianceManageCode");	//������
				String Temp = req.getParameter("Temp");	//�����¶�
				String Humidity = req.getParameter("Humidity");	//���ʪ��
				String Pressure = req.getParameter("Pressure");	//����ѹ
				String Conclusion = req.getParameter("Conclusion");	//����
				String WorkDate = req.getParameter("WorkDate");	//��У����				
				String Quantity = req.getParameter("Quantity");	//��������
				String RepairLevel = req.getParameter("RepairLevel");	//������
				String MaterialDetail = req.getParameter("MaterialDetail");	//�����ϸ
				String CarFee = req.getParameter("CarFee");	//��ͨ��
				String DebugFee = req.getParameter("DebugFee");	//���Է�
				String MaterialFee = req.getParameter("MaterialFee");	//�����
				String OtherFee = req.getParameter("OtherFee");	//������
				
				String OtherCond = req.getParameter("OtherCond");	//����
				String StdOrStdAppUsageStatus = req.getParameter("StdOrStdAppUsageStatus");	//��׼(����)ʹ��ǰ��״̬���
				String AbnormalDesc = req.getParameter("AbnormalDesc");	//�쳣���˵��
				
				String Mandatory = req.getParameter("Mandatory");	//�Ƿ�ǿ��
				String MandatoryCode = req.getParameter("MandatoryCode");	//ǿ��Ψһ�Ժ�
				String MandatoryPurpose = req.getParameter("MandatoryPurpose");	//ǿ������/��;
				String MandatoryItemType = req.getParameter("MandatoryItemType");	//ǿ��Ŀ¼��Ӧ���
				String MandatoryType = req.getParameter("MandatoryType");	//ǿ��Ŀ¼��Ӧ�ֱ�
				String MandatoryApplyPlace = req.getParameter("MandatoryApplyPlace");	//ʹ��/��װ�ص�				
				
				String StandardAppliancesString=req.getParameter("StandardAppliancesString");//ѡ��ı�׼����
				String StandardsString=req.getParameter("StandardsString");//ѡ��ļ�����׼
				String SpecificationsString=req.getParameter("SpecificationsString");//ѡ��ļ����淶
				
				String FirstIsUnqualified=req.getParameter("FirstIsUnqualified");//�׼��Ƿ�ϸ�
				String UnqualifiedReason=req.getParameter("UnqualifiedReason");//���ϸ�ԭ��
				String Remark=req.getParameter("Remark");//��ע
				
				String VerifyUser = req.getParameter("VerifyUser");	//������Ա
				
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				if (ApplianceCode != null && ApplianceCode.length() >= 50) {
					throw new Exception("������ų��Ȳ��ܳ���50��");
				}
				if(Quantity == null || Quantity.trim().length() == 0){
					throw new Exception("��������Ϊ�գ�");
				}
				if(TaskAssignId == null || TaskAssignId.length() == 0){
					throw new Exception("�������IDΪ�գ�");
				}
				if(WorkType == null || WorkType.length() == 0){
					throw new Exception("��������Ϊ�գ�");
				}
				if(WorkDate == null || WorkDate.length() == 0){
					throw new Exception("��У����Ϊ�գ�");
				}
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("ί�е������ڣ�");
				}
//				JSONArray specificationArray = new JSONArray(Specifications);
//				JSONArray standardArray = new JSONArray(Standards);
//				JSONArray stdApplianceArray = new JSONArray(StandardAppliances);
				Integer NumberInt = Integer.parseInt(Quantity.trim());
				if(NumberInt <= 0 ){
					throw new Exception("������������Ϊ����0��������");
				}
				//����ԭʼ��¼����
				Integer existedNumber = 0;
				List<Long> existedCountList = new OriginalRecordManager().findByHQL("select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 ", 
						cSheet.getId());
				if(existedCountList.get(0) != null){
					existedNumber = existedCountList.get(0).intValue();
				}
				if(NumberInt + existedNumber > cSheet.getQuantity()){
					throw new Exception("����������Ч��ԭʼ��¼�������������ó���ί�е�������������");
				}
				
				
//				Map<Integer,Specification> speMap = new HashMap<Integer, Specification>();	//�����ݵļ����淶
//				Map<Integer,Standard> stdMap = new HashMap<Integer, Standard>();	//�����ݵı�׼
//				Map<Integer,StandardAppliance> stdAppMap = new HashMap<Integer, StandardAppliance>();	//ʹ�õı�׼����
//				for(int i = 0; i < specificationArray.length(); i++){
//					Specification spe = new Specification();
//					JSONObject jsonObj = specificationArray.getJSONObject(i);
//					spe.setId(jsonObj.getInt("SpecificationId"));	//���ID
//					speMap.put(spe.getId(), spe);
//				}
//				
//				for(int i = 0; i < standardArray.length(); i++){
//					Standard std = new Standard();
//					JSONObject jsonObj = standardArray.getJSONObject(i);
//					std.setId(jsonObj.getInt("StandardId"));	//��׼ID
//					stdMap.put(std.getId(), std);
//				}
//				
//				for(int i = 0; i < stdApplianceArray.length(); i++){
//					StandardAppliance stdapp = new StandardAppliance();
//					JSONObject jsonObj = stdApplianceArray.getJSONObject(i);
//					stdapp.setId(jsonObj.getInt("StandardApplianceId"));	//��׼����ID
//					stdAppMap.put(stdapp.getId(), stdapp);
//				}
				TargetAppliance tApp = new TargetApplianceManager().findById(Integer.parseInt(TargetApplianceId));
				
				//�ܼ����߹����ļ����淶��������׼����׼����
				List<Specification> speList = new ArrayList<Specification>();
				List<Standard> stdList = new ArrayList<Standard>();
				List<StandardAppliance> stdAppList = new ArrayList<StandardAppliance>();
				
				TgtAppSpecManager TgtAppSpecMgr = new TgtAppSpecManager();//
				StdTgtAppManager StdTgtAppMgr = new StdTgtAppManager();//
				TgtAppStdAppManager TgtAppStdAppMgr = new TgtAppStdAppManager();//
				
				SpecificationManager SpecificationMgr = new SpecificationManager();
				StandardManager StandardMgr = new StandardManager();
				StandardApplianceManager StandardApplianceMgr = new StandardApplianceManager();
				
				
				if(SpecificationsString==null||SpecificationsString.length()==0){//�������
					speList = TgtAppSpecMgr.findByHQL("select distinct model.specification from TgtAppSpec as model where model.targetAppliance.id=?", tApp.getId());
				}else{//ѡ���˼�����̣���������ʱ���������߹�ϵ
					JSONArray speListArray=new JSONArray(SpecificationsString);
					for(int i=0;i<speListArray.length();i++){
						speList.add(SpecificationMgr.findById(speListArray.getJSONObject(i).getInt("id")));
					}
				}
				if(StandardsString==null||StandardsString.length()==0){//������׼
					stdList = StdTgtAppMgr.findByHQL("select distinct model.standard from StdTgtApp as model where model.targetAppliance.id=?", tApp.getId());
				}else{//ѡ���˼�����׼����������ʱ���������߹�ϵ
					JSONArray stdListArray=new JSONArray(StandardsString);
					for(int i=0;i<stdListArray.length();i++){
						stdList.add(StandardMgr.findById(stdListArray.getJSONObject(i).getInt("id")));
					}
				}
				if(StandardAppliancesString==null||StandardAppliancesString.length()==0){//��׼����
					stdAppList = TgtAppStdAppMgr.findByHQL("select distinct model.standardAppliance from TgtAppStdApp as model where model.targetAppliance.id=?", tApp.getId());
				}else{//ѡ���˱�׼���ߣ���������ʱ���������߹�ϵ
					JSONArray stdAppListArray=new JSONArray(StandardAppliancesString);
					for(int i=0;i<stdAppListArray.length();i++){
						stdAppList.add(StandardApplianceMgr.findById(stdAppListArray.getJSONObject(i).getInt("id")));
					}
				}
								
				TaskAssign taskAssign = new TaskAssignManager().findById(Integer.parseInt(TaskAssignId));
				if(taskAssign == null){
					throw new Exception("��������䲻���ڣ��������ԭʼ��¼��");
				}
				if(taskAssign.getStatus() == 1){
					throw new Exception("�����������ע�����������ԭʼ��¼��");
				}
				
				if(VerifyUser !=  null && VerifyUser.length() > 0){	//������
					if(VerifyUser.equalsIgnoreCase(taskAssign.getSysUserByAlloteeId().getName())){
						throw new Exception("������Ա�������У��Ա��ͬ��");
					}
					QualificationManager qfMgr = new QualificationManager();
					List<Integer> qfTypeList = new ArrayList<Integer>();
					qfTypeList.add(FlagUtil.QualificationType.Type_Heyan);
					
					//����Ƿ��к��������
					List<Object[]> qfRetList = qfMgr.getQualifyUsers(VerifyUser, tApp.getApplianceStandardName().getId(), 0, qfTypeList);
					if(qfRetList.size() == 0){
						throw new Exception(String.format("ָ���ġ�������Ա��:'%s'û�����߱�׼����'%s'�� �������ʣ�",
								VerifyUser,
								tApp.getApplianceStandardName().getName()));
					}
				}
				
				OriginalRecord o = new OriginalRecord();
				o.setSysUserByCreatorId((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));	//ԭʼ��¼������
				o.setCreateTime(new Timestamp(System.currentTimeMillis()));	//����ʱ��
				o.setCommissionSheet(cSheet);	//ί�е�
				o.setTargetAppliance(tApp);	//�ܼ�����
				o.setStatus(0);	//״̬
				o.setSysUserByStaffId(taskAssign.getSysUserByAlloteeId());	//��/У��Ա
				o.setTaskAssign(taskAssign);	//�������
				o.setModel(Model);
				o.setRange(Range);
				o.setAccuracy(Accuracy);
				o.setApplianceName(ApplianceName);	//��������
				o.setWorkType(WorkType);	//��������
				o.setWorkLocation(WorkLocation);	//�����ص�
				o.setManufacturer(Manufacturer);	//���쳧��
				o.setApplianceCode(ApplianceCode);	//�������
				o.setManageCode(ApplianceManageCode);	//������
				o.setTemp(Temp);	//�����¶�
				o.setHumidity(Humidity);	//���ʪ��
				o.setPressure(Pressure);	//����ѹ
				o.setConclusion(Conclusion);	//����
				o.setFirstIsUnqualified(FirstIsUnqualified);//�׼��Ƿ�ϸ�
				o.setUnqualifiedReason(UnqualifiedReason);//���ϸ�ԭ��
				o.setRemark(Remark);//��ע
				
				if(WorkDate != null && WorkDate.trim().length() > 0){
					Date workDate = new Date(DateTimeFormatUtil.DateFormat.parse(WorkDate).getTime());
					o.setWorkDate(workDate);	//��У����
					if(tApp.getTestCycle() != null && !o.getWorkType().equals("У׼")){	//����������Ч����
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(workDate);
						calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + tApp.getTestCycle());
						calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	//��Ч��=�춨����+�춨����-1��
						o.setValidity(new java.sql.Date(calendar.getTimeInMillis()));  	//��Ч��
					}
				}				
				o.setQuantity(Integer.parseInt(Quantity));	//��ԭʼ��¼�е���������
				
				o.setMandatory(cSheet.getMandatory()==true?"��":"��");	//�Ƿ�ǿ��				
				
				o.setTestFee(tApp.getFee()==null?null:tApp.getFee() * NumberInt);	//�춨��
				o.setRepairLevel(RepairLevel);	//������
				if(RepairLevel != null && RepairLevel.trim().length() > 0){	//�����
					if(RepairLevel.equalsIgnoreCase("С")){
						o.setRepairFee(tApp.getSrfee());
					}else if(RepairLevel.equalsIgnoreCase("��")){
						o.setRepairFee(tApp.getMrfee());
					}else if(RepairLevel.equalsIgnoreCase("��")){
						o.setRepairFee(tApp.getLrfee());
					}
				}
				if(CarFee != null && CarFee.length() > 0){
					o.setCarFee(Double.parseDouble(CarFee));	//��ͨ��
				}
				if(DebugFee != null && DebugFee.length() > 0){
					o.setDebugFee(Double.parseDouble(DebugFee));	//���Է�
				}
				if(MaterialFee != null && MaterialFee.length() > 0){
					o.setMaterialFee(Double.parseDouble(MaterialFee));	//�����
				}
				o.setMaterialDetail(MaterialDetail);//�����ϸ
				if(OtherFee != null && OtherFee.length() > 0){
					o.setOtherFee(Double.parseDouble(OtherFee));	//������
				}
				o.setTotalFee((o.getTestFee()==null?0.0:o.getTestFee()) + 
						(o.getRepairFee()==null?0.0:o.getRepairFee()) + 
						(o.getMaterialFee()==null?0.0:o.getMaterialFee()) + 
						(o.getCarFee()==null?0.0:o.getCarFee()) + 
						(o.getDebugFee()==null?0.0:o.getDebugFee()) + 
						(o.getOtherFee()==null?0.0:o.getOtherFee()) );
				
				
				o.setOtherCond(OtherCond);	//��������
				o.setStdOrStdAppUsageStatus(StdOrStdAppUsageStatus);	//��׼�����ߣ�ʹ��״̬ǰ�����
				o.setAbnormalDesc(AbnormalDesc);	//�쳣���˵��
				
				o.setMandatory(Mandatory);
				o.setMandatoryCode(MandatoryCode);
				o.setMandatoryPurpose(MandatoryPurpose);
				o.setMandatoryItemType(MandatoryItemType);
				o.setMandatoryType(MandatoryType);
				o.setMandatoryApplyPlace(MandatoryApplyPlace);
//				while(NumberInt-- > 0){
//					//��ԭʼ��¼
//					OriginalRecord o = new OriginalRecord();
//					o.setCommissionSheet(cSheet);	//ί�е�
//					o.setModel(Model);
//					o.setRange(Range);
//					o.setAccuracy(Accuracy);
//					o.setManufacturer(cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//���쳧��
//					o.setApplianceCode(cSheet.getAppFactoryCode()==null?"":cSheet.getAppFactoryCode());	//�������
//					o.setManageCode(cSheet.getAppManageCode()==null?"":cSheet.getAppManageCode());	//������
//					o.setStatus(0);	//״̬
//					o.setTargetAppliance(tApp);	//�ܼ�����
//					o.setSysUserByStaffId(taskAssign.getSysUserByAlloteeId());	//��/У��Ա
//					o.setTaskAssign(taskAssign);	//�������
//					o.setQuantity(1);	//��ԭʼ��¼�е���������
//					
//					o.setWorkDate(new Date(System.currentTimeMillis()));	//��������Ĭ��Ϊ��ǰ����
//					o.setWorkLocation((cSheet.getCommissionType() == 2)?"��������ʹ���ֳ�":"����ʵ����");
//					o.setWorkType(CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));
//					o.setMandatory(cSheet.getMandatory()==true?"��":"��");
//					o.setApplianceName(cSheet.getSpeciesType()?null:cSheet.getApplianceName());	//��������
//					
//					//����
//					boolean bTestFeeDefault = true;
//					/*if(cSheet.getQuotationId() != null){
//						QuotationItemManager qItemMgr = new QuotationItemManager();
//						List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
//						condList.add(new KeyValueWithOperator("quotation.number", cSheet.getQuotationId(), "="));
//						condList.add(new KeyValueWithOperator("quotation.status", 1, "<>"));
//						condList.add(new KeyValueWithOperator("standardName", tApp.getApplianceStandardName().getName(), "="));
//						if(Model != null && Model.length() > 0){
//							condList.add(new KeyValueWithOperator("model", Model, "="));
//						}
//						if(Range != null && Range.length() > 0){
//							condList.add(new KeyValueWithOperator("range", Range, "="));
//						}
//						if(Accuracy != null && Accuracy.length() > 0){
//							condList.add(new KeyValueWithOperator("accuracy", Accuracy, "="));
//						}
//						List<QuotationItem> qiList = qItemMgr.findByVarProperty(condList);
//						if(qiList != null && qiList.size() > 0){
//							QuotationItem qi = qiList.get(0);
//							if(qi != null && qi.getMinCost() != null && qi.getMinCost().length() > 0 && qi.getMaxCost() != null && qi.getMaxCost().length() > 0 && qi.getMinCost().equals(qi.getMaxCost())){
//								try{
//									o.setTestFee(Double.parseDouble(qi.getMinCost()));
//									bTestFeeDefault = false;
//								}catch(Exception exx){}
//							}
//						}
//					}*/
//					if(bTestFeeDefault){
//						o.setTestFee(tApp.getFee());
//					}
//					
//					
//					
//					oRecordList.add(o);
//				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(oRecordMgr.saveByBatch(o, speList, stdList, stdAppList, loginUser)){
					retJSON1.put("IsOK", true);
					retJSON1.put("OriginalRecordId", o.getId());
					retJSON1.put("WorkStaffId", (loginUser == null || loginUser.getId() == null)?"":loginUser.getId());
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("���ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 1", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//ע��һ��ԭʼ��¼
			JSONObject retJSON2 = new JSONObject();
			try {
				String oRecordId = req.getParameter("OriginalRecordId");	//ԭʼ��¼��
				
				if(oRecordId == null || oRecordId.trim().length() == 0 ){
					throw new Exception("������������");
				}
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(oRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				SysUser user = (SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(!(oRecord.getSysUserByStaffId().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())||(user.getName().equals("ϵͳ����Ա")))){
					throw new Exception("�����Ǹ�ԭʼ��¼�ļ�У��Ա������ע����ԭʼ��¼��");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע��������ע��ԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					throw new Exception("��ί�е����깤������ע��ԭʼ��¼��");
				}
				
				oRecord.setStatus(1);	//״̬��ע��
				if(!oRecordMgr.update(oRecord)){
					throw new Exception("�ύ������ʧ�ܣ�");
				}
				retJSON2.put("IsOK", true);
				
			} catch (Exception e){
			
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("ע��ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 2", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3:	//����һ��ί�е��������ϴ�����ʽ��ԭʼ��¼�����������ݣ�Ϊԭʼ��¼���������ϴ���Excel��
			JSONObject retJSON3 = new JSONObject();
			try {
				/*
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				List<OriginalRecord> oRecRetList = oRecordMgr.findByPropertyBySort("taskAssign.id", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), 
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("originalRecordExcel.pdf", null, "is not null")	//֤��Ϊ��ʽ��
				);
				
				if(oRecRetList != null && oRecRetList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(OriginalRecord o:oRecRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
						jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
						jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼ģ���ļ���ŵ��ļ�������
						
						
						jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
						
						jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//�������ID
						jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//������Ŀ����
						
						jsonObj.put("Staff", o.getSysUser()==null?"":o.getSysUser().getName());	//��/У��Ա
						jsonObj.put("StaffId", o.getSysUser()==null?-1:o.getSysUser().getId());	//��/У��ԱId
						
						jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//ԭʼ��¼Excel�ļ�ID
						jsonObj.put("ExcelCode", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getCode());	//ԭʼ��¼Excel�ļ��ı��
						jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//ԭʼ��¼��xls�ļ�ID
						jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//ԭʼ��¼�汾��
						jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//ԭʼ��¼��PDF�ļ�ID
						jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":String.format("%s.pdf", o.getOriginalRecordExcel().getFileName().substring(0, o.getOriginalRecordExcel().getFileName().lastIndexOf('.'))));	//ԭʼ��¼xls�ļ����ļ���
						
						jsonArray.put(jsonObj);
					}
					
					retJSON3.put("total", oRecRetList.size());
					retJSON3.put("rows", jsonArray);
				}else{
					throw new Exception("");
				}*/
			} catch (Exception e){
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4:	//���һ��ԭʼ��¼�ġ���˺���׼������
			JSONObject retJSON4 = new JSONObject();
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");	//ԭʼ��¼Id
				String Version = req.getParameter("Version");	//�汾��
				String Verifier = req.getParameter("Verifier");	//������Id
				String Authorizer = req.getParameter("Authorizer");//��׼��Id
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
				}
				int versionInt = -1;
				if(Version != null && Version.length() > 0){
					versionInt = Integer.parseInt(Version);
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null){
					throw new Exception("����δ��¼������ִ�д˲�����");
				}
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() == null || //oRecord.getOriginalRecordExcel().getPdf() == null || 
						oRecord.getCertificate() == null || oRecord.getCertificate().getPdf() == null){
					throw new Exception("�����ύ֤�飡");
				}
				VerifyAndAuthorize v = oRecord.getVerifyAndAuthorize();
				if(v != null && v.getAuthorizeResult() != null && v.getAuthorizeResult()){
					throw new Exception("��ԭʼ��¼����ͨ���������Ȩǩ�֣����������ύ���룡");
				}
				if(v != null && !oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId()) && 
						v.getOriginalRecordExcel().getId().equals(oRecord.getOriginalRecordExcel().getId()) &&
						v.getCertificate().getId().equals(oRecord.getCertificate().getId())){
					throw new Exception("�Ѿ����ڸ�ԭʼ��¼�ĺ����ǩ�����룬�����Ǹ�����������ļ���Ա�����������ύ���룡");
				}
				if(v != null && versionInt < v.getVersion().intValue()){
					throw new Exception("�汾��ͻ����ԭʼ��¼�ĺ����ǩ�������ѱ������ύ������ˢ�²鿴��");
				}
				
				UserManager userMgr = new UserManager();
				SysUser verifier = userMgr.findById(Integer.parseInt(Verifier));
				if(verifier == null){
					throw new Exception("�Ҳ���ָ���ĺ�����!");
				}
				if(oRecord.getSysUserByStaffId().getId().equals(verifier.getId())){
					throw new Exception("�����˲������Ǽ���Ա�Լ�!");
				}
				
				SysUser authorizer = userMgr.findById(Integer.parseInt(Authorizer));
				if(authorizer == null){
					throw new Exception("�Ҳ���ָ������Ȩǩ����!");
				}
				
				VerifyAndAuthorize vNew = null;
				if(v != null ){	
					if(v.getSysUserByVerifierId() != null && v.getSysUserByAuthorizerId() != null){//ԭ�������Ȩǩ�ּ�¼��Ч����Ҫ��������������һ���µĺ������Ȩǩ�ּ�¼
						vNew = new VerifyAndAuthorize();
						vNew.setCode(v.getCode());
						vNew.setVersion(v.getVersion() + 1);
					}else{
						vNew = v;
						vNew.setAuthorizeRemark(null);
						vNew.setAuthorizeResult(null);
						vNew.setAuthorizeTime(null);
						vNew.setIsAuthBgRuning(null);
						vNew.setSysUserByAuthorizeExecutorId(null);
						vNew.setVerifyRemark(null);
						vNew.setVerifyResult(null);
						vNew.setVerifyTime(null);
					}
				}else{
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
				}
				vNew.setCommissionSheet(oRecord.getCommissionSheet());
				vNew.setOriginalRecordExcel(oRecord.getOriginalRecordExcel());
				vNew.setCertificate(oRecord.getCertificate());
				vNew.setSysUserByVerifierId(verifier);
				vNew.setSysUserByAuthorizerId(authorizer);
				vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
				vNew.setSysUserByCreatorId(loginUser);
				
				VerifyAndAuthorizeManager vMgr = new VerifyAndAuthorizeManager();
				if(vNew.getId() != null){  //���¼�¼
					if(vMgr.update(vNew)){
						retJSON4.put("IsOK", true);
//						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_OriginalAndCertificateVerify, String.format("���յ�һ��ԭʼ��¼��֤��ĺ�������ί�е��ţ�%s", oRecord.getCommissionSheet().getCode()), verifier, FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateVerify);	//��ʱ��Ϣ
						return;
					}
				}else{
					if(vMgr.save(vNew)){	//������¼
						oRecord.setVerifyAndAuthorize(vNew);
						if(oRecordMgr.update(oRecord)){
							retJSON4.put("IsOK", true);
//							InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_OriginalAndCertificateVerify, String.format("���յ�һ��ԭʼ��¼��֤��ĺ�������ί�е��ţ�%s", oRecord.getCommissionSheet().getCode()), verifier, FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateVerify);	//��ʱ��Ϣ
							return;
						}
					}
				}
				
				throw new Exception("�������ݿ�ʧ�ܣ�");
			} catch (Exception e){
				try {
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("�ύ�������Ȩǩ������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 3", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	//��ѯ���������б�
			JSONObject retJSON5 = new JSONObject();
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
				String type=req.getParameter("type");	//type=1���������±���֤��ĺ����б�
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//��ѯ����
				if(CommissionNumber != null && CommissionNumber.trim().length() > 0){
					CommissionNumber = URLDecoder.decode(CommissionNumber.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("commissionSheet.code", CommissionNumber, "="));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				if(type==null){
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//ί�е���δ�깤��ע����
				}else{
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">="));	//ί�е���δע����
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiZhuXiao, "<"));	//ί�е���δע����
				}
				
				condList.add(new KeyValueWithOperator("status", 1, "<>"));	//ԭʼ��¼��δע����
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.sysUserByVerifierId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //�����
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.verifyTime", null, "is null"));	//��δ��˵�
				condList.add(new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//֤������ʽ�汾
				condList.add(new KeyValueWithOperator("certificate.sysUser", null, "is null"));	//�춨Ա�Ѻ���
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));	//������δע��
				
				int total = oRecordMgr.getTotalCount(condList);
				List<OriginalRecord> oRetList = oRecordMgr.findPagedAllBySort(page, rows, "verifyAndAuthorize.createTime", true, condList);
				RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
				for(OriginalRecord o : oRetList){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
//					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//ί��ʱ��
					
					jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//�ܼ�����ID
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//���߱�׼����
					jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼ģ���ļ���ŵ��ļ�������
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("Status", o.getStatus());	//ԭʼ��¼��״̬
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע��Ϣ
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//��������
					
					jsonObj.put("Manufacturer", o.getManufacturer());
					jsonObj.put("ApplianceCode", o.getApplianceCode());
					jsonObj.put("ManageCode", o.getManageCode());
					jsonObj.put("WorkType", o.getWorkType()==null?"":o.getWorkType());
					jsonObj.put("WorkLocation", o.getWorkLocation()==null?"":o.getWorkLocation());
					jsonObj.put("Temp", o.getTemp()==null?"":o.getTemp());
					jsonObj.put("Humidity", o.getHumidity()==null?"":o.getHumidity());
					jsonObj.put("Pressure", o.getPressure()==null?"":o.getPressure());
					jsonObj.put("Conclusion", o.getConclusion()==null?"":o.getConclusion());
					jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
					
					jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//�������ID
					jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//������Ŀ����
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//��/У��Ա
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//��/У��ԱId
					jsonObj.put("Validity", o.getValidity()==null?"":o.getValidity());	//��Ч��
					
					jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//ԭʼ��¼Excel�ļ�ID
					jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//ԭʼ��¼��xls�ļ�ID
					jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//ԭʼ��¼�汾��
					jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//ԭʼ��¼��PDF�ļ�ID
					jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//ԭʼ��¼xls�ļ����ļ���
					
					jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//֤���ļ�ID
					jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//֤���ļ���Word�ļ�ID
					jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//ԭʼ��¼�汾��
					jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//֤���ļ���PDF�ļ�ID
					jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//֤���ļ�Doc�ļ����ļ���
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//֤����
					
					jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//�������Ȩ ��¼ID
					jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//�������Ȩ ��¼�汾��
					jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
					jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//�������Ȩ ��¼��ԭʼ��¼PDF
					jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
					jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//�������Ȩ ��¼��֤���PDF
					jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//������
					jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//����ʱ��
					jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//������
					jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//���鱸ע
					if(o.getVerifyAndAuthorize() == null){
						jsonObj.put("AuthorizerId", "");	//��׼�ˣ�ǩ���ˣ�
						jsonObj.put("AuthorizerName","");
					}else{
						if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//ʵ��ִ�е���׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
						}else{
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//��׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
						}
					}
					jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//��׼ʱ��
					jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//��׼���
					jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//��׼��ע
					if(type!=null){//���±���֤��������Ϣ
						List<RemakeCertificate> rcList = (new RemakeCertificateManager()).findByVarProperty(new KeyValueWithOperator("originalRecord.id", o.getId(), "="));
						if(rcList==null||rcList.size()==0){
							jsonObj.put("CreateRemark", "");
						}else{
							jsonObj.put("CreateRemark", rcList.get(0).getCreateRemark());
						}
					}
					
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
					log.debug("exception in OriginalRecordServlet-->case 5", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6:	//ԭʼ��¼��֤�����
			JSONObject retJSON6 = new JSONObject();
			try {
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//��������0��ͨ����1ͨ��
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//����ı�ע��Ϣ
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("������������");
				}
				
				String[] oRecordIdArray = OriginalRecordIds.split(";");
				if(oRecordIdArray.length == 0 || oRecordIdArray[0] == null || oRecordIdArray[0].length() == 0){
					throw new Exception("������������");
				}
				int doneSuccess = 0;
				VerifyAndAuthorizeManager vMgr = new VerifyAndAuthorizeManager();
				for(String oRecordId : oRecordIdArray){
					if(oRecordId == null || oRecordId.length() == 0){
						continue;
					}
					OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
					if(o == null || o.getVerifyAndAuthorize() == null){
						continue;
//						throw new Exception("�������񲻴��ڣ�");
					}
					VerifyAndAuthorize v = o.getVerifyAndAuthorize();
					if(v.getVerifyTime() != null){
						continue;
//						throw new Exception("�ú�����������ɣ������ظ�������");
					}
					if(v.getSysUserByVerifierId()==null || !v.getSysUserByVerifierId().getId().equals(((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId())){
						continue;
//						throw new Exception("�����Ǹú�������Ľ����ߣ�����ִ�д˲�����");
					}
					v.setVerifyRemark(ExecuteMsg);
					v.setVerifyResult(Integer.parseInt(ExecutorResult)>0?true:false);
					v.setVerifyTime(new Timestamp(System.currentTimeMillis()));
					vMgr.update(v);
					doneSuccess ++;
					if(v.getVerifyResult()){ //����ͨ��
//						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_OriginalAndCertificateAuthorize, String.format("���յ�һ��ԭʼ��¼��֤�����Ȩǩ������ί�е��ţ�%s", o.getCommissionSheet().getCode()), v.getSysUserByAuthorizerId(), FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateAuthorize);	//��ʱ��Ϣ
					}
				}
				retJSON6.put("IsOK", true);
				retJSON6.put("msg", String.format("�ѳɹ����� '%d'��ԭʼ��¼��֤�飡", doneSuccess));
			} catch (Exception e){
				try {
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("�ύ������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 6", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7:	//��ѯǩ�������б�
			JSONObject retJSON7 = new JSONObject();
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
				String type=req.getParameter("type");	//type=1���������±���֤��ĺ����б�
				
				String OwnTask = req.getParameter("OwnTask");	//Ϊtrue��ʾ���ص�ǰ�û��������б�Ϊfalse��ʾ���������û��������б� 
				if(OwnTask == null){
					OwnTask = "true";
				}
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//��ѯ����
				if(CommissionNumber != null && CommissionNumber.trim().length() > 0){
					CommissionNumber = URLDecoder.decode(CommissionNumber.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("commissionSheet.code", CommissionNumber, "="));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
					condList.add(new KeyValueWithOperator("commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				if(type==null){
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//ί�е���δ�깤��ע����
				}else{
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">="));	//ί�е���δע����
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiZhuXiao, "<"));	//ί�е���δע����
				}
				condList.add(new KeyValueWithOperator("status", 1, "<>"));	//ԭʼ��¼��δע����
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.verifyResult", true, "=")); //��ͨ�������
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));	//���������δע����
				
				if(OwnTask.equalsIgnoreCase("true")){
					condList.add(new KeyValueWithOperator("verifyAndAuthorize.sysUserByAuthorizerId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //����ʱָ������׼��
				}
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.authorizeTime", null, "is null"));	//��δǩ�ֵ�
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.certificate.pdf", null, "is not null")); //֤��Ϊ��ʽ�汾��
				
				int total = oRecordMgr.getTotalCount(condList);
				List<OriginalRecord> oRetList = oRecordMgr.findPagedAllBySort(page, rows, "verifyAndAuthorize.createTime", true, condList);
				RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
				for(OriginalRecord o : oRetList){
					JSONObject jsonObj = new JSONObject();
										
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
//					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//ί��ʱ��
					
					jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//�ܼ�����ID
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//���߱�׼����
					jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼ģ���ļ���ŵ��ļ�������
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("Status", o.getStatus());	//ԭʼ��¼��״̬
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע��Ϣ
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//��������
					
					jsonObj.put("Manufacturer", o.getManufacturer());
					jsonObj.put("ApplianceCode", o.getApplianceCode());
					jsonObj.put("ManageCode", o.getManageCode());
					jsonObj.put("WorkType", o.getWorkType()==null?"":o.getWorkType());
					jsonObj.put("WorkLocation", o.getWorkLocation()==null?"":o.getWorkLocation());
					jsonObj.put("Temp", o.getTemp()==null?"":o.getTemp());
					jsonObj.put("Humidity", o.getHumidity()==null?"":o.getHumidity());
					jsonObj.put("Pressure", o.getPressure()==null?"":o.getPressure());
					jsonObj.put("Conclusion", o.getConclusion()==null?"":o.getConclusion());
					jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
					
					jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//�������ID
					jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//������Ŀ����
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//��/У��Ա
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//��/У��ԱId
					jsonObj.put("Validity", o.getValidity()==null?"":o.getValidity());	//��Ч��
					
					jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//ԭʼ��¼Excel�ļ�ID
					jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//ԭʼ��¼��xls�ļ�ID
					jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//ԭʼ��¼�汾��
					jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//ԭʼ��¼��PDF�ļ�ID
					jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//ԭʼ��¼xls�ļ����ļ���
					
					jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//֤���ļ�ID
					jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//֤���ļ���Word�ļ�ID
					jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//ԭʼ��¼�汾��
					jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//֤���ļ���PDF�ļ�ID
					jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//֤���ļ�Doc�ļ����ļ���
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//֤����
					
					jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//�������Ȩ ��¼ID
					jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//�������Ȩ ��¼�汾��
					jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
					jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//�������Ȩ ��¼��ԭʼ��¼PDF
					jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
					jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//�������Ȩ ��¼��֤���PDF
					jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//������
					jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//����ʱ��
					jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//������
					jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//���鱸ע
					if(o.getVerifyAndAuthorize() == null){
						jsonObj.put("AuthorizerId", "");	//��׼�ˣ�ǩ���ˣ�
						jsonObj.put("AuthorizerName","");
					}else{
						if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//ʵ��ִ�е���׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
						}else{
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//��׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
						}
					}
					jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//��׼ʱ��
					jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//��׼���
					jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//��׼��ע
					jsonObj.put("IsAuthBgRuning", (o.getVerifyAndAuthorize()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning())?true:false);	//�Ƿ��̨����
					if(type!=null){//���±���֤��������Ϣ
						List<RemakeCertificate> rcList = (new RemakeCertificateManager()).findByVarProperty(new KeyValueWithOperator("originalRecord.id", o.getId(), "="));
						if(rcList==null||rcList.size()==0){
							jsonObj.put("CreateRemark", "");
						}else{
							jsonObj.put("CreateRemark", rcList.get(0).getCreateRemark());
						}
					}
					
					jsonArray.put(jsonObj);
				}
				retJSON7.put("total", total);
				retJSON7.put("rows", jsonArray);

			} catch (Exception e){
				
				try {
					retJSON7.put("total", 0);
					retJSON7.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 7", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		case 8:	//ԭʼ��¼��֤����Ȩǩ��
			JSONObject retJSON8 = new JSONObject();
			try {
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Ids
				String ExecutorResult = req.getParameter("ExecutorResult");	//��������0��ͨ����1ͨ��
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//����ı�ע��Ϣ
				String FromAuthByCode = req.getParameter("FromAuthByCode");	//��Ϊ��ǩ������ֶ�ֵΪtrue
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0 || 
						ExecutorResult == null || ExecutorResult.length() == 0){
					throw new Exception("������������");
				}
				
				String []oRecordIdArray = OriginalRecordIds.split(";");
				if(oRecordIdArray.length == 0 || oRecordIdArray[0] == null || oRecordIdArray[0].length() == 0){
					throw new Exception("������������");
				}
				if(oRecordIdArray.length > 1){
					throw new Exception("��Ȩǩ������ִ��ÿ��ֻ�ܴ���һ��֤�飡");
				}
				String oRecordId = oRecordIdArray[0];
				
				OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
				if(o == null || o.getVerifyAndAuthorize() == null){
					throw new Exception("ǩ�����񲻴��ڣ�");
				}
				if(o.getCertificate() == null || o.getCertificate().getPdf() == null){
					throw new Exception("ԭʼ��¼��֤��δ�ύ�����ܽ���ǩ�ֲ�����");
				}
				VerifyAndAuthorize v = o.getVerifyAndAuthorize();
				if(v.getVerifyTime() == null || !v.getVerifyResult()){
					throw new Exception("��ԭʼ��¼��֤����δͨ�����飬����ִ��ǩ�ֲ�����");
				}
				if(v.getAuthorizeTime() != null && v.getIsAuthBgRuning() == null){
					throw new Exception("��ǩ����������ɣ������ظ�������");
				}
				if(!v.getOriginalRecordExcel().getId().equals(o.getOriginalRecordExcel().getId()) ||
						!v.getCertificate().getId().equals(o.getCertificate().getId())){
					throw new Exception("��ǩ�������ѹ��ڣ�ԭ��ԭʼ��¼��֤���иĶ���");
				}
				
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
//				if(!new QualificationManager().checkUserQualify(loginUser.getId(), o.getTargetAppliance().getApplianceStandardName().getId(), 0, FlagUtil.QualificationType.Type_Qianzi)){
//					throw new Exception("��û�и�������Ȩǩ�ֵ�Ȩ�ޣ�");
//				}
				if(FromAuthByCode != null && FromAuthByCode.equals("true")){	//��ǩ
					//�޲���
				}else{	//��Ȩǩ����ִ��ǩ��
					if(loginUser == null || !loginUser.getId().equals(v.getSysUserByAuthorizerId().getId())){
						throw new Exception(String.format("�����Ǹ�ǩ������Ľ�����:'%s'������ִ����Ȩǩ�֣�", v.getSysUserByAuthorizerId().getName()));
					}
				}
				
				//���ݸ������ݿ�ǰ�����ݣ��ѱ�ǩ��ʧ��ʱ��ԭ�����ڼ��ݺ�ִ̨��ǩ�֣�
				Boolean bIsBgRun = v.getIsAuthBgRuning();
				String authRemark = v.getAuthorizeRemark();
				Boolean bAuthResult = v.getAuthorizeResult();
				SysUser authExecutor = v.getSysUserByAuthorizeExecutorId();
				Timestamp authTime = v.getAuthorizeTime();
				
				//�������ݿ�
				v.setAuthorizeRemark(ExecuteMsg);
				v.setAuthorizeResult(Integer.parseInt(ExecutorResult)>0?true:false);
				v.setAuthorizeTime(new Timestamp(System.currentTimeMillis()));
				v.setSysUserByAuthorizeExecutorId(loginUser);
				v.setIsAuthBgRuning(null);
				
				VerifyAndAuthorizeManager vMgr = new VerifyAndAuthorizeManager();
				vMgr.update(v);
				if(v.getVerifyResult()){//ǩ��ͨ��
					try{
						AuthBackgroundRuningService.AuthorizeExecute(o, v);	//ִ��ǩ��
					}catch(Exception ex){	//���յ��쳣����ԭ���ݿ�
						v.setAuthorizeRemark(authRemark);
						v.setAuthorizeResult(bAuthResult);
						v.setAuthorizeTime(authTime);
						v.setSysUserByAuthorizeExecutorId(authExecutor);
						v.setIsAuthBgRuning(bIsBgRun);
						vMgr.update(v);
						throw ex;
					}
				}
				retJSON8.put("IsOK", true);				
			} catch (Exception e){
				
				try {
					retJSON8.put("IsOK", false);
					retJSON8.put("msg", String.format("ִ����Ȩǩ��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 8", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 8", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON8.toString());
			}
			break;
		case 9:	//��ѯ���ί�е������е�֤��PDF������������֤���ID�����ڴ�ӡ֤�飩
			JSONObject retJSON9 = new JSONObject();
			try{
				String CommissionIds = req.getParameter("CommissionIds");	//ί�е�Id
				
				if(CommissionIds == null || CommissionIds.trim().length() == 0){
					throw new Exception("ί�е�δѡ��");
				}
				String []CommissionIdArray = CommissionIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼(ǩ����ͨ���Ҳ������ں�ִ̨��)
				List<OriginalRecord> fQuantityList;
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				JSONArray cerArray = new JSONArray();
				for(String CommissionId : CommissionIdArray){
					List<OriginalRecord> oRecList = oRecordMgr.findByPropertyBySort("certificate.certificateCode", true, 
							k1, k2, k3,
							new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
					for(OriginalRecord o : oRecList){
						if(o.getCertificate().getPdf().length() > 0){
							JSONObject obj = new JSONObject();
							fQuantityList = new ArrayList<OriginalRecord>();
							fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, o.getId(), true);	//
							if(fQuantityList==null||fQuantityList.size()==0){
								throw new Exception("֤��"+o.getCertificate().getCertificateCode()+"δ��ɺ���ǩ��");
							}		
							//fQuantityList=null;
							obj.put("FileId", o.getCertificate().getPdf());
							cerArray.put(obj);
						}
					}
				}
				
				
				retJSON9.put("IsOK", true);
				retJSON9.put("Certificates", cerArray);
			}catch (Exception e){
				
				try {
					retJSON9.put("IsOK", false);
					retJSON9.put("msg", String.format("��ѯί�е�֤��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 9", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 9", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			break;
		case 10: //��ѯһ��ί�е����û�ѡ���֤��PDF������������֤���ID�����ڴ�ӡ֤�飩
			JSONObject retJSON10 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("ԭʼ��¼δѡ�У�");
				}
				String []OriginalRecordIdArray = OriginalRecordIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				JSONArray cerArray = new JSONArray();
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼(ǩ����ͨ���Ҳ������ں�ִ̨��)
				List<OriginalRecord> fQuantityList1;
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				for(String OriginalRecordId : OriginalRecordIdArray){
					List<OriginalRecord> oRecList = oRecordMgr.findByPropertyBySort("certificate.certificateCode", true, 
							k1, k2, k3,
							new KeyValueWithOperator("id", Integer.parseInt(OriginalRecordId), "="));
					for(OriginalRecord o : oRecList){
						if(o.getCertificate().getPdf().length() > 0){
							JSONObject obj = new JSONObject();
							fQuantityList1 = new ArrayList<OriginalRecord>();
							fQuantityList1 = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, Integer.parseInt(OriginalRecordId), true);	//
							if(fQuantityList1==null||fQuantityList1.size()==0){
								throw new Exception("֤��"+o.getCertificate().getCertificateCode()+"δ��ɺ���ǩ��");
							}		
							//fQuantityList1=null;
							obj.put("FileId", o.getCertificate().getPdf());
							cerArray.put(obj);
						}
					}
				}
				
				
				retJSON10.put("IsOK", true);
				retJSON10.put("Certificates", cerArray);
			}catch (Exception e){
				
				try {
					retJSON10.put("IsOK", false);
					retJSON10.put("msg", String.format("��ѯί�е�֤��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 10", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 10", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
			}
			break;
		case 11:	//����һ��ԭʼ��¼Excel�ļ��������ļ�Ϊ��ǰʹ�õİ汾
			JSONObject retJSON11 = new JSONObject();
			try {
				String oRecordId = req.getParameter("OriginalRecordId");	//ԭʼ��¼Id
				if(oRecordId == null || oRecordId.trim().length() == 0){
					throw new Exception("������������");
				}
				OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
				if(o == null || o.getOriginalRecordExcel() == null || o.getOriginalRecordExcel().getFileName().length()==0){
					throw new Exception("��ԭʼ��¼��δ����Excel�ļ���");
				}
				OriginalRecordExcel excel = o.getOriginalRecordExcel();
				//�����ֶζ���xml�ļ�
				HashMap<String, Object> map = new HashMap<String, Object>();
				String xlsXmlFileName = String.format("%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf(".")));	//ԭʼ��¼�����ļ�������
				map.put(MongoDBUtil.KEYNAME_FileSetName, o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼�������ļ����ļ�������
				Pattern pattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("�Ҳ�����ǰ����ʹ�õ������ļ�:%s", xlsXmlFileName));
				}
				String xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//ԭʼ��¼xml�ļ���ID
				if(excel.getXml().equalsIgnoreCase(xlsXmlFileId)){
					throw new Exception("��Excel�����ļ��Ѿ��ǵ�ǰʹ�õİ汾���������!");
				}
				excel.setXml(xlsXmlFileId);
				if(new OriginalRecordExcelManager().update(excel)){
					retJSON11.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			} catch (Exception e){
				try {
					retJSON11.put("IsOK", false);
					retJSON11.put("msg", String.format("����ԭʼ��¼Excel�������ļ�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 11", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 11", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON11.toString());
			}
			break;
		case 12:	//ԭʼ��¼��֤����Ȩ ������ǩ�֣���ͨ�����̨ǩ�֣�
			JSONObject retJSON12 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Ids
				String ExecutorResult = req.getParameter("ExecutorResult");	//��������0��ͨ����1ͨ��
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//����ı�ע��Ϣ
				String FromAuthByCode = req.getParameter("FromAuthByCode");	//��Ϊ��ǩ������ֶ�ֵΪtrue
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0 || 
						ExecutorResult == null || ExecutorResult.length() == 0){
					throw new Exception("������������");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				
				String []oRecordIdArray = OriginalRecordIds.split(";");
				VerifyAndAuthorizeManager vMgr = new VerifyAndAuthorizeManager();
				int doneSuccess = 0;
				
				synchronized(MutexObjectOfNewCommissionSheet) {
					for(String oRecordId : oRecordIdArray){
						if(oRecordId != null && oRecordId.length() > 0){
							OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
							if(o == null || o.getStatus() == 1 || o.getVerifyAndAuthorize() == null){
								continue;
							}
							if(o.getCertificate() == null || o.getCertificate().getPdf() == null){
								continue;
							}
							VerifyAndAuthorize v = o.getVerifyAndAuthorize();
							if(v.getVerifyTime() == null || !v.getVerifyResult()){
								continue;
							}
							if(v.getAuthorizeTime() != null && v.getIsAuthBgRuning() == null){
								continue;
							}
							if(!v.getOriginalRecordExcel().getId().equals(o.getOriginalRecordExcel().getId()) ||
									!v.getCertificate().getId().equals(o.getCertificate().getId())){
								continue;
							}
							if(FromAuthByCode != null && FromAuthByCode.equals("true")){	//��ǩ
								//�޲���
							}else{	//��Ȩǩ����ִ��ǩ��
								if(loginUser == null ){
									continue;
								}
							}
							
							if(Integer.parseInt(ExecutorResult)>0){	//���ͨ������ִ̨��ǩ��
								v.setAuthorizeRemark(ExecuteMsg);
								v.setAuthorizeResult(Integer.parseInt(ExecutorResult)>0?true:false);
								v.setAuthorizeTime(new Timestamp(System.currentTimeMillis()));
								v.setSysUserByAuthorizeExecutorId(loginUser);
								AuthBackgroundRuningManager.AddAnAuthBackgroundRuning(v);
							}else{	//��˲�ͨ��
								v.setAuthorizeRemark(ExecuteMsg);
								v.setAuthorizeResult(Integer.parseInt(ExecutorResult)>0?true:false);
								v.setAuthorizeTime(new Timestamp(System.currentTimeMillis()));
								v.setSysUserByAuthorizeExecutorId(loginUser);
								v.setIsAuthBgRuning(null);
								vMgr.update(v);
							}
							doneSuccess ++;
						}
					}
					retJSON12.put("IsOK", true);
					retJSON12.put("msg", String.format("�ɹ���Ȩǩ�� '%d'��֤�飡", doneSuccess));
				}
			}catch (Exception e){
				try {
					retJSON12.put("IsOK", false);
					retJSON12.put("msg", String.format("��Ȩǩ��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 12", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 12", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON12.toString());
			}
			break;
		case 13://��ѯһ��ί�е���ԭʼ��¼�����ڲ�ѯͳ���У������ܼ�footer
			JSONObject retJSON13 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				List<OriginalRecord> oRecRetList = oRecordMgr.findByPropertyBySort("certificate.certificateCode", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), 
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("taskAssign.status", 1, "<>")
				);
				int Quantity = 0;
				double TestFee = 0;
				double RepairFee = 0;
				double MaterialFee = 0;
				double CarFee = 0;
				double DebugFee = 0;
				double OtherFee = 0;
				double TotalFee = 0;
				
				if(oRecRetList != null && oRecRetList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(OriginalRecord o:oRecRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
						jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
						jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//�ܼ�����ID
						jsonObj.put("ApplianceStandardNameId", o.getTargetAppliance().getApplianceStandardName().getId());	//���߱�׼����Id
						jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//���߱�׼����
						jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼ģ���ļ���ŵ��ļ�������
						jsonObj.put("Model", o.getModel());
						jsonObj.put("Range", o.getRange());
						jsonObj.put("Accuracy", o.getAccuracy());
						jsonObj.put("Status", o.getStatus());	//ԭʼ��¼��״̬
						jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע��Ϣ
						jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//��������
						Quantity += jsonObj.getInt("Quantity");
						jsonObj.put("Manufacturer", o.getManufacturer());
						jsonObj.put("ApplianceCode", o.getApplianceCode());
						jsonObj.put("ManageCode", o.getManageCode());
						jsonObj.put("WorkType", o.getWorkType()==null?"":o.getWorkType());
						jsonObj.put("WorkLocation", o.getWorkLocation()==null?"":o.getWorkLocation());
						jsonObj.put("Temp", o.getTemp()==null?"":o.getTemp());
						jsonObj.put("Humidity", o.getHumidity()==null?"":o.getHumidity());
						jsonObj.put("Pressure", o.getPressure()==null?"":o.getPressure());
						jsonObj.put("Conclusion", o.getConclusion()==null?"":o.getConclusion());
						jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
						
						jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//�������ID
						jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//������Ŀ����
						
						jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//��/У��Ա
						jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//��/У��ԱId
						jsonObj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));	//��Ч��
//						jsonObj.put("TechnicalDocs", o.getTechnicalDocs()==null?"":o.getTechnicalDocs());
//						jsonObj.put("Standards", o.getStandards()==null?"":o.getStandards());
//						jsonObj.put("StandardAppliances", o.getStandardAppliances()==null?"":o.getStandardAppliances());
						
						jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//ԭʼ��¼Excel�ļ�ID
						jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//ԭʼ��¼��xls�ļ�ID
						jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//ԭʼ��¼�汾��
						jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//ԭʼ��¼��PDF�ļ�ID
						jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//ԭʼ��¼xls�ļ����ļ���
						
						jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//֤���ļ�ID
						jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//֤���ļ���Word�ļ�ID
						jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//ԭʼ��¼�汾��
						jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//֤���ļ���PDF�ļ�ID
						jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//֤���ļ�Doc�ļ����ļ���
						jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//֤����
						
						jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//�������Ȩ ��¼ID
						jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//�������Ȩ ��¼�汾��
						jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
						jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//�������Ȩ ��¼��ԭʼ��¼PDF
						jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
						jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//�������Ȩ ��¼��֤���PDF
						jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//������
						jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
						jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//����ʱ��
						jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//������
						jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//���鱸ע
						if(o.getVerifyAndAuthorize() == null){
							jsonObj.put("AuthorizerId", "");	//��׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName","");
						}else{
							if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//ʵ��ִ�е���׼�ˣ�ǩ���ˣ�
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
							}else{
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//��׼�ˣ�ǩ���ˣ�
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
							}
						}
						jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//��׼ʱ��
						jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//��׼���
						jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//��׼��ע
						jsonObj.put("IsAuthBgRuning", (o.getVerifyAndAuthorize()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning())?true:false);	//�Ƿ��̨����
						
						//������Ϣ
						jsonObj.put("TestFee", o.getTestFee()==null?0:o.getTestFee());
						TestFee += Double.parseDouble(jsonObj.getString("TestFee"));
						jsonObj.put("RepairFee", o.getRepairFee()==null?0:o.getRepairFee());
						RepairFee += Double.parseDouble(jsonObj.getString("RepairFee"));
						jsonObj.put("MaterialFee", o.getMaterialFee()==null?0:o.getMaterialFee());
						MaterialFee += Double.parseDouble(jsonObj.getString("MaterialFee"));
						jsonObj.put("CarFee", o.getCarFee()==null?0:o.getCarFee());
						CarFee += Double.parseDouble(jsonObj.getString("CarFee"));
						jsonObj.put("DebugFee", o.getDebugFee()==null?0:o.getDebugFee());
						DebugFee += Double.parseDouble(jsonObj.getString("DebugFee"));
						jsonObj.put("OtherFee", o.getOtherFee()==null?0:o.getOtherFee());
						OtherFee += Double.parseDouble(jsonObj.getString("OtherFee"));
						jsonObj.put("TotalFee", o.getTotalFee()==null?0:o.getTotalFee());
						TotalFee += Double.parseDouble(jsonObj.getString("TotalFee"));
						
						jsonArray.put(jsonObj);
					}
					
					JSONArray footerArray = new JSONArray();
					JSONObject footerObj = new JSONObject();
					footerObj.put("ApplianceStandardName", "�ܼ�");
					footerObj.put("Quantity", Quantity);
					footerObj.put("TestFee", TestFee);
					footerObj.put("RepairFee", RepairFee);
					footerObj.put("MaterialFee", MaterialFee);
					footerObj.put("CarFee", CarFee);
					footerObj.put("AuthorizerId", "");	//��׼�ˣ�ǩ���ˣ�
					footerObj.put("AuthorizerName","");
					footerObj.put("DebugFee", DebugFee);
					footerObj.put("OtherFee", OtherFee);
					footerObj.put("TotalFee", TotalFee);
					footerArray.put(footerObj);
					
					retJSON13.put("total", oRecRetList.size());
					retJSON13.put("rows", jsonArray);
					retJSON13.put("footer", footerArray);
				}else{
					throw new Exception("");
				}
			} catch (Exception e){
				try {
					retJSON13.put("total", 0);
					retJSON13.put("rows", new JSONArray());
					retJSON13.put("footer", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 0", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON13.toString());
			}	
			break;
		case 14:	//��ѯһ���춨��Ա��δ�˶��������б�
			JSONObject retJSON14 = new JSONObject();
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
				condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//ί�е���δ�깤��ע����
				condList.add(new KeyValueWithOperator("status", 1, "<>"));	//ԭʼ��¼��δע����
				condList.add(new KeyValueWithOperator("sysUserByStaffId.id", ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId(), "="));  //�춨Ա
				condList.add(new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//֤������ʽ�汾
				condList.add(new KeyValueWithOperator("certificate.sysUser", null, "is not null"));	//�춨Ա��δ�˶�
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));	//���������δע����
				
				int total = oRecordMgr.getTotalCount(condList);
				List<OriginalRecord> oRetList = oRecordMgr.findPagedAllBySort(page, rows, "certificate.certificateCode", true, condList);
				for(OriginalRecord o : oRetList){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
//					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//ί��ʱ��
					
					jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
					jsonObj.put("TaskId", o.getTaskAssign().getId());	//�������ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//�ܼ�����ID
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//���߱�׼����
					jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼ģ���ļ���ŵ��ļ�������
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("Status", o.getStatus());	//ԭʼ��¼��״̬
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע��Ϣ
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//��������
					
					jsonObj.put("Manufacturer", o.getManufacturer());
					jsonObj.put("ApplianceCode", o.getApplianceCode());
					jsonObj.put("ManageCode", o.getManageCode());
					jsonObj.put("WorkType", o.getWorkType()==null?"":o.getWorkType());
					jsonObj.put("WorkLocation", o.getWorkLocation()==null?"":o.getWorkLocation());
					jsonObj.put("Temp", o.getTemp()==null?"":o.getTemp());
					jsonObj.put("Humidity", o.getHumidity()==null?"":o.getHumidity());
					jsonObj.put("Pressure", o.getPressure()==null?"":o.getPressure());
					jsonObj.put("Conclusion", o.getConclusion()==null?"":o.getConclusion());
					jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
					
					jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//�������ID
					jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//������Ŀ����
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//��/У��Ա
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//��/У��ԱId
					jsonObj.put("Validity", o.getValidity()==null?"":o.getValidity());	//��Ч��
					jsonObj.put("StaffChecked", (o.getCertificate() != null && o.getCertificate().getSysUser() != null)?false:true);	//�춨Ա�Ƿ�˶������֤�鲻�Ǽ춨Ա�Լ����Ƶ������
					
					jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//ԭʼ��¼Excel�ļ�ID
					jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//ԭʼ��¼��xls�ļ�ID
					jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//ԭʼ��¼�汾��
					jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//ԭʼ��¼��PDF�ļ�ID
					jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//ԭʼ��¼xls�ļ����ļ���
					
					jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//֤���ļ�ID
					jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//֤���ļ���Word�ļ�ID
					jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//ԭʼ��¼�汾��
					jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//֤���ļ���PDF�ļ�ID
					jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//֤���ļ�Doc�ļ����ļ���
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//֤����
					
					jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//�������Ȩ ��¼ID
					jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//�������Ȩ ��¼�汾��
					jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
					jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//�������Ȩ ��¼��ԭʼ��¼PDF
					jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
					jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//�������Ȩ ��¼��֤���PDF
					jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//������
					jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//����ʱ��
					jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//������
					jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//���鱸ע
					if(o.getVerifyAndAuthorize() == null){
						jsonObj.put("AuthorizerId", "");	//��׼�ˣ�ǩ���ˣ�
						jsonObj.put("AuthorizerName","");
					}else{
						if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//ʵ��ִ�е���׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
						}else{
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//��׼�ˣ�ǩ���ˣ�
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
						}
					}
					jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//��׼ʱ��
					jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//��׼���
					jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//��׼��ע

					
					jsonArray.put(jsonObj);
				}
				retJSON14.put("total", total);
				retJSON14.put("rows", jsonArray);

			} catch (Exception e){
				try {
					retJSON14.put("total", 0);
					retJSON14.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 14", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 14", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON14.toString());
			}
			break;
		case 15:	//�춨Ա�˶�ͨ��
			JSONObject retJSON15 = new JSONObject();
			try {
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("������������");
				}
				
				String[] oRecordIdArray = OriginalRecordIds.split(";");
				if(oRecordIdArray.length == 0 || oRecordIdArray[0] == null || oRecordIdArray[0].length() == 0){
					throw new Exception("������������");
				}
				int doneSuccess = 0;
				
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�����ܺ˶���֤�飡");
				}
				
				CertificateManager cMgr = new CertificateManager();
				for(String oRecordId : oRecordIdArray){
					if(oRecordId == null || oRecordId.length() == 0){
						continue;
					}
					OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
					if(o == null || o.getCertificate() == null || o.getCertificate().getPdf() == null || 
							o.getCertificate().getSysUser() == null || o.getCertificate().getSysUser().getId().equals(loginUser.getId())){
						continue;
					}
					
					Certificate c = o.getCertificate();
					c.setSysUser(null);
					c.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					cMgr.update(c);
					doneSuccess ++;
				}
				retJSON15.put("IsOK", true);
				retJSON15.put("msg", String.format("�ѳɹ��˶� '%s'��ԭʼ��¼��֤�飡", doneSuccess));
			} catch (Exception e){
				try {
					retJSON15.put("IsOK", false);
					retJSON15.put("msg", String.format("�˶�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 15", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 15", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON15.toString());
			}
			break;
		case 16:	//��ѯ���ί�е������е�ԭʼ��¼PDF������������ԭʼ��¼��ID�����ڴ�ӡԭʼ��¼��
			JSONObject retJSON16 = new JSONObject();
			try{
				String CommissionIds = req.getParameter("CommissionIds");	//ί�е�Id
				
				if(CommissionIds == null || CommissionIds.trim().length() == 0){
					throw new Exception("ί�е�δѡ��");
				}
				String []CommissionIdArray = CommissionIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				KeyValueWithOperator k4 = new KeyValueWithOperator("taskAssign.status", 1, "<>");
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼(ǩ����ͨ���Ҳ������ں�ִ̨��)
				List<OriginalRecord> fQuantityList;
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				JSONArray cerArray = new JSONArray();
				for(String CommissionId : CommissionIdArray){
					List<OriginalRecord> oRecList = oRecordMgr.findByPropertyBySort("id", true, 
							k1, k2, k3, k4,
							new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
					for(OriginalRecord o : oRecList){
						if(o.getOriginalRecordExcel() != null && 
								o.getOriginalRecordExcel().getPdf() != null && 
								o.getOriginalRecordExcel().getPdf().length() > 0){
							JSONObject obj = new JSONObject();
							fQuantityList = new ArrayList<OriginalRecord>();
							fQuantityList = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, o.getId(), true);	//
							if(fQuantityList==null||fQuantityList.size()==0){
								throw new Exception("֤��"+o.getCertificate().getCertificateCode()+"δ��ɺ���ǩ��");
							}		
							//fQuantityList=null;
							obj.put("FileId", o.getOriginalRecordExcel().getPdf());
							cerArray.put(obj);
						}
					}
				}
				
				retJSON16.put("IsOK", true);
				retJSON16.put("Certificates", cerArray);
			}catch (Exception e){
				
				try {
					retJSON16.put("IsOK", false);
					retJSON16.put("msg", String.format("��ѯί�е�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 16", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 16", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON16.toString());
			}
			break;
		case 17: //��ѯһ��ί�е����û�ѡ���ԭʼ��¼PDF������������ԭʼ��¼��ID�����ڴ�ӡԭʼ��¼��
			JSONObject retJSON17 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("ԭʼ��¼δѡ�У�");
				}
				String []OriginalRecordIdArray = OriginalRecordIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				KeyValueWithOperator k4 = new KeyValueWithOperator("taskAssign.status", 1, "<>");
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼(ǩ����ͨ���Ҳ������ں�ִ̨��)
				List<OriginalRecord> fQuantityList1;
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				JSONArray cerArray = new JSONArray();
				for(String OriginalRecordId : OriginalRecordIdArray){
					List<OriginalRecord> oRecList = oRecordMgr.findByPropertyBySort("id", true, 
							k1, k2, k3, k4,
							new KeyValueWithOperator("id", Integer.parseInt(OriginalRecordId), "="));
					for(OriginalRecord o : oRecList){
						if(o.getOriginalRecordExcel() != null && 
								o.getOriginalRecordExcel().getPdf() != null && 
								o.getOriginalRecordExcel().getPdf().length() > 0){
							JSONObject obj = new JSONObject();
							fQuantityList1 = new ArrayList<OriginalRecord>();
							fQuantityList1 = cSheetMgr.findByHQL(hqlQueryString_FinishQuantity, o.getId(), true);	//
							if(fQuantityList1==null||fQuantityList1.size()==0){
								throw new Exception("֤��"+o.getCertificate().getCertificateCode()+"δ��ɺ���ǩ��");
							}		
							//fQuantityList1=null;
							obj.put("FileId", o.getOriginalRecordExcel().getPdf());
							cerArray.put(obj);
						}
					}
				}
				
				retJSON17.put("IsOK", true);
				retJSON17.put("Certificates", cerArray);
			}catch (Exception e){
				
				try {
					retJSON17.put("IsOK", false);
					retJSON17.put("msg", String.format("��ѯί�е�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 17", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 17", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON17.toString());
			}
			break;
		case 18: //��ѯһ����׼���Ƶ���ʷ֤����Ϣ
			JSONObject retJSON18 = new JSONObject();
			try {
				String StandardNameId = req.getParameter("StandardNameId");	//��׼��
				String Model = req.getParameter("Model");	//�ͺŹ��
				String Range = req.getParameter("Range");	//������Χ
				String Accuracy = req.getParameter("Accuracy");	//׼ȷ�ȵȼ�
				String WorkStaff = req.getParameter("WorkStaff");	//��У��Ա
				int page18 = 0; // ��ǰҳ��
				if (req.getParameter("page") != null)
					page18 = Integer.parseInt(req.getParameter("page").toString());
				int rows18 = 10; // ҳ���С
				if (req.getParameter("rows") != null)
					rows18 = Integer.parseInt(req.getParameter("rows").toString());

				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				if(StandardNameId != null && StandardNameId.length() > 0){
					StandardNameId = URLDecoder.decode(StandardNameId, "UTF-8");
					condList.add(new KeyValueWithOperator("targetAppliance.applianceStandardName.id", Integer.parseInt(StandardNameId), "="));
				}
				if(Model != null && Model.length() > 0){
					Model = URLDecoder.decode(Model, "UTF-8");
					condList.add(new KeyValueWithOperator("model", "%"+Model+"%", "like"));
				}
				if(Range != null && Range.length() > 0){
					Range = URLDecoder.decode(Range, "UTF-8");
					condList.add(new KeyValueWithOperator("range", "%"+Range+"%", "like"));
				}
				if(Accuracy != null && Accuracy.length() > 0){
					Accuracy = URLDecoder.decode(Accuracy, "UTF-8");
					condList.add(new KeyValueWithOperator("accuracy", "%"+Accuracy+"%", "like"));
				}
				if(WorkStaff != null && WorkStaff.length() > 0){
					WorkStaff = URLDecoder.decode(WorkStaff, "UTF-8");
					condList.add(new KeyValueWithOperator("sysUserByStaffId.name", WorkStaff, "="));
				}
				condList.add(new KeyValueWithOperator("certificate.pdf", null, "is not null"));
				condList.add(new KeyValueWithOperator("status", 1, "<>"));
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));
				int totalSize = oRecordMgr.getTotalCount(condList);
				
				List<OriginalRecord> oRecordList = oRecordMgr.findPagedAllBySort(page18, rows18, "workDate", false, condList);
				JSONArray jsonArray = new JSONArray();
				for(OriginalRecord o : oRecordList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("OriginalRecordId", o.getId());		//ԭʼ��¼ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//ί�е�ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//�ܼ�����ID
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ
					jsonObj.put("ApplianceStandardNameId", o.getTargetAppliance().getApplianceStandardName().getId());	//���߱�׼����Id
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//���߱�׼����
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("ApplianceName", o.getApplianceName()==null?"":o.getApplianceName());	//��������
					jsonObj.put("Status", o.getStatus());	//ԭʼ��¼��״̬
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע��Ϣ
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//��������
					
					jsonObj.put("Manufacturer", o.getManufacturer());
					jsonObj.put("ApplianceCode", o.getApplianceCode());
					jsonObj.put("ManageCode", o.getManageCode());
					jsonObj.put("WorkType", o.getWorkType()==null?"":o.getWorkType());
					jsonObj.put("WorkLocation", o.getWorkLocation()==null?"":o.getWorkLocation());
					jsonObj.put("Temp", o.getTemp()==null?"":o.getTemp());
					jsonObj.put("Humidity", o.getHumidity()==null?"":o.getHumidity());
					jsonObj.put("Pressure", o.getPressure()==null?"":o.getPressure());
					jsonObj.put("Conclusion", o.getConclusion()==null?"":o.getConclusion());
					jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
					jsonObj.put("OtherCond", o.getOtherCond()==null?"":o.getOtherCond());
					jsonObj.put("StdOrStdAppUsageStatus", o.getStdOrStdAppUsageStatus()==null?"":o.getStdOrStdAppUsageStatus());
					jsonObj.put("AbnormalDesc", o.getAbnormalDesc()==null?"":o.getAbnormalDesc());
					jsonObj.put("RepairLevel", o.getRepairLevel()==null?"":o.getRepairLevel());	//������
					jsonObj.put("MaterialDetail", o.getMaterialDetail()==null?"":o.getMaterialDetail());	//�����ϸ
					
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//֤����
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//��/У��Ա
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//��/У��ԱId
					jsonObj.put("Verifier", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());	//������
										
					//������Ϣ
					jsonObj.put("TestFee", o.getTestFee()==null?0:o.getTestFee());
					jsonObj.put("RepairFee", o.getRepairFee()==null?0:o.getRepairFee());
					jsonObj.put("MaterialFee", o.getMaterialFee()==null?0:o.getMaterialFee());
					jsonObj.put("CarFee", o.getCarFee()==null?0:o.getCarFee());
					jsonObj.put("DebugFee", o.getDebugFee()==null?0:o.getDebugFee());
					jsonObj.put("OtherFee", o.getOtherFee()==null?0:o.getOtherFee());
					jsonObj.put("TotalFee", o.getTotalFee()==null?0:o.getTotalFee());
					
					jsonObj.put("Mandatory", o.getMandatory()==null?"":o.getMandatory());	//�Ƿ�ǿ��
					jsonObj.put("MandatoryCode", o.getMandatoryCode()==null?"":o.getMandatoryCode());	//ǿ��Ψһ�Ժ�
					jsonObj.put("MandatoryItemType", o.getMandatoryItemType()==null?"":o.getMandatoryItemType());	//ǿ����Ŀ��Ӧ���
					jsonObj.put("MandatoryType", o.getMandatoryType()==null?"":o.getMandatoryType());	//ǿ����Ŀ��Ӧ�ֱ�
					jsonObj.put("MandatoryApplyPlace", o.getMandatoryApplyPlace()==null?"":o.getMandatoryApplyPlace());	//ʹ��/��װ�ص�
					jsonObj.put("MandatoryPurpose", o.getMandatoryPurpose()==null?"":o.getMandatoryPurpose());	//��;/ǿ������
					
					jsonObj.put("FirstIsUnqualified", o.getFirstIsUnqualified()==null?"�ϸ�":o.getFirstIsUnqualified());	//�׼��Ƿ�ϸ�
					jsonObj.put("UnqualifiedReason", o.getUnqualifiedReason()==null?"":o.getUnqualifiedReason());	//���ϸ�ԭ��
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//��ע
					
					jsonArray.put(jsonObj);
				}
				retJSON18.put("total", totalSize);
				retJSON18.put("rows", jsonArray);
				
				
			} catch (Exception e) {
				try {
					retJSON18.put("total", 0);
					retJSON18.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if (e.getClass() == java.lang.Exception.class) { // �Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 18",e);
				} else {
					log.error("error in OriginalRecordServlet-->case 18", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON18.toString());
			}
			break;
			
		case 19:	//��ӡ���ί�е������е�ԭʼ��¼�ĺϸ�֤
			JSONObject retJSON19 = new JSONObject();
			try{
				String CommissionIds = req.getParameter("CommissionIds");	//ί�е�Id
				
				if(CommissionIds == null || CommissionIds.trim().length() == 0){
					throw new Exception("ί�е�δѡ��");
				}
				String []CommissionIdArray = CommissionIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				KeyValueWithOperator k4 = new KeyValueWithOperator("taskAssign.status", 1, "<>");
				JSONArray cerArray = new JSONArray();
				for(String CommissionId : CommissionIdArray){
					List<OriginalRecord> oRecList = oRecordMgr.findByPropertyBySort("id", true, 
							k1, k2, k3, k4,
							new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
					for(OriginalRecord o : oRecList){
						if(o.getCertificate().getPdf().length() > 0){
							
							if(o.getWorkType()!=null && !o.getWorkType().equals("����")){
								if(o.getWorkType().equals("�춨") && o.getConclusion()!=null&& o.getConclusion().contains("��")){
									continue;
								}
								JSONObject obj = new JSONObject();
								obj.put("ApplianceName", (o.getApplianceName()==null||o.getApplianceName().trim().length()==0)?o.getTargetAppliance().getApplianceStandardName().getName():o.getApplianceName());	//��������
								obj.put("Model", o.getModel());			
								obj.put("ApplianceCode", o.getApplianceCode());
								obj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
								obj.put("WorkStaff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());//��/У��Ա
								obj.put("WorkType", o.getWorkType());//�������� �춨\У׼\���\����
								obj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));//��Ч��
								obj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ
								cerArray.put(obj);
							}
									
							
						}
					}
				}
				
				retJSON19.put("IsOK", true);
			
				retJSON19.put("PrintArray", cerArray);
				
			}catch (Exception e){
				
				try {
					retJSON19.put("IsOK", false);
					retJSON19.put("msg", String.format("��ѯί�е�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 19", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 19", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(retJSON19.toString());
			}
			break;
		case 20: //��ѯһ��ί�е����û�ѡ���ԭʼ��¼PDF������������ԭʼ��¼��ID�����ڴ�ӡԭʼ��¼��
			JSONObject retJSON20 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//ԭʼ��¼Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("ԭʼ��¼δѡ�У�");
				}
				String []OriginalRecordIdArray = OriginalRecordIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				KeyValueWithOperator k4 = new KeyValueWithOperator("taskAssign.status", 1, "<>");
				
				JSONArray cerArray = new JSONArray();
				for(String OriginalRecordId : OriginalRecordIdArray){
					List<OriginalRecord> oRecList = oRecordMgr.findByPropertyBySort("id", true, 
							k1, k2, k3, k4,
							new KeyValueWithOperator("id", Integer.parseInt(OriginalRecordId), "="));
					for(OriginalRecord o : oRecList){
						if(o.getOriginalRecordExcel() != null && 
								o.getOriginalRecordExcel().getPdf() != null && 
								o.getOriginalRecordExcel().getPdf().length() > 0){
							if(o.getWorkType()!=null && !o.getWorkType().equals("����")){
								if(o.getWorkType().equals("�춨") && o.getConclusion()!=null&& o.getConclusion().contains("��")){
									continue;
								}
								JSONObject obj = new JSONObject();
								obj.put("ApplianceName", (o.getApplianceName()==null||o.getApplianceName().trim().length()==0)?o.getTargetAppliance().getApplianceStandardName().getName():o.getApplianceName());	//��������
								obj.put("Model", o.getModel());			
								obj.put("ApplianceCode", o.getApplianceCode());
								obj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
								obj.put("WorkStaff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());//��/У��Ա
								obj.put("WorkType", o.getWorkType());//�������� �춨\У׼\���\����
								obj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));//��Ч��
								obj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//ί�е�λ
								cerArray.put(obj);
							}
						}
					}
				}
				
				retJSON20.put("IsOK", true);
				retJSON20.put("PrintArray", cerArray);
				
			}catch (Exception e){
				
				try {
					retJSON20.put("IsOK", false);
					retJSON20.put("msg", String.format("��ѯί�е�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 20", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 20", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON20.toString());
			}
			break;
		case 21://��ѯһ��ί�е�����ʷԭʼ��¼
			JSONObject retJSON21 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionSheetId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				OriginalRecordExcel oriReExcel = new OriginalRecordExcel();
				OriginalRecordExcelManager oriReExcelMgr = new OriginalRecordExcelManager();
				List<OriginalRecordExcel> oRecRetExcelList = oriReExcelMgr.findByPropertyBySort("originalRecord.id", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="));
				CertificateManager cerMgr = new CertificateManager();
				List<Certificate> cerList = cerMgr.findByHQL("from Certificate as model where model.commissionSheet.id = ? and model.pdf is not null", Integer.parseInt(CommissionId));
				
				if(oRecRetExcelList != null && oRecRetExcelList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(OriginalRecordExcel o:oRecRetExcelList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("CertificateCode", o.getCertificateCode()==null?"":o.getCertificateCode());	//֤����
						jsonObj.put("ExcelCode", o.getCode()+o.getVersion());		//EXCEL code
						jsonObj.put("Pdf", o.getPdf()==null?"":o.getPdf());	//Excel PDF
						jsonObj.put("Doc", o.getDoc()==null?"":o.getDoc());	//Excel Doc
						for(int i = 0; i < cerList.size(); i++){
							if(cerList.get(i).getCertificateCode().equals(o.getCertificateCode())){
								jsonObj.put("CertificatePdf", cerList.get(i).getPdf());
								jsonObj.put("CertificateDoc", cerList.get(i).getDoc());
								break;
							}
						}
						jsonObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(o.getLastEditTime()));	//
						
						jsonArray.put(jsonObj);
					}
					
					retJSON21.put("total", oRecRetExcelList.size());
					retJSON21.put("rows", jsonArray);
				}else{
					throw new Exception("");
				}
			} catch (Exception e){
				try {
					retJSON21.put("total", 0);
					retJSON21.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in OriginalRecordServlet-->case 21", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 21", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON21.toString());
			}	
			break;
		}
	}
	
}
