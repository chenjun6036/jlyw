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
 * 原始记录Servlet
 * @author Administrator
 *
 */
public class OriginalRecordServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(OriginalRecordServlet.class);
	private static Object MutexObjectOfNewCommissionSheet = new Object();		//用于互斥访问
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		OriginalRecordManager oRecordMgr = new OriginalRecordManager();
		switch (method) {
		case 0: // 查找一个委托单的原始记录表信息
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单未指定！");
				}
				
				//先查找证书编号不为空 的并按证书编号排序（按certificate.certificateCode排序只能查找到certificate不为null的记录）
				List<OriginalRecord> oRecRetList = oRecordMgr.findByPropertyBySort("certificate.certificateCode", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), 
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("taskAssign.status", 1, "<>")
				);
				//再查找没有证书编号的记录
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
						jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
						jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
						jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//受检器具ID
						jsonObj.put("ApplianceStandardNameId", o.getTargetAppliance().getApplianceStandardName().getId());	//器具标准名称Id
						jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//器具标准名称
						jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录模板文件存放的文件集名称
						jsonObj.put("Model", o.getModel());
						jsonObj.put("Range", o.getRange());
						jsonObj.put("Accuracy", o.getAccuracy());
						jsonObj.put("Status", o.getStatus());	//原始记录的状态
						jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注信息
						jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//器具数量
						
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
						
						jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//任务分配ID
						jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//检验项目名称
						
						jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//检/校人员
						jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//检/校人员Id
						jsonObj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));	//有效期
//						jsonObj.put("TechnicalDocs", o.getTechnicalDocs()==null?"":o.getTechnicalDocs());
//						jsonObj.put("Standards", o.getStandards()==null?"":o.getStandards());
//						jsonObj.put("StandardAppliances", o.getStandardAppliances()==null?"":o.getStandardAppliances());
						jsonObj.put("StaffChecked", (o.getCertificate() != null && o.getCertificate().getSysUser() != null)?false:true);	//检定员是否核定（针对证书不是检定员自己编制的情况）
						
						jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//原始记录Excel文件ID
						jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//原始记录的xls文件ID
						jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//原始记录版本号
						jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//原始记录的PDF文件ID
						jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//原始记录xls文件的文件名
						
						jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//证书文件ID
						jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//证书文件的Word文件ID
						jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//原始记录版本号
						jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//证书文件的PDF文件ID
						jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//证书文件Doc文件的文件名
						jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//证书编号
						
						jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//核验和授权 记录ID
						jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//核验和授权 记录版本号
						jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
						jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//核验和授权 记录的原始记录PDF
						jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
						jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//核验和授权 记录中证书的PDF
						jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//核验人
						jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
						jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//核验时间
						jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//核验结果
						jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//核验备注
						if(o.getVerifyAndAuthorize() == null){
							jsonObj.put("AuthorizerId", "");	//批准人（签字人）
							jsonObj.put("AuthorizerName","");
						}else{
							if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//实际执行的批准人（签字人）
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
							}else{
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//批准人（签字人）
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
							}
						}
						jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//批准时间
						jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//批准结果
						jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//批准备注
						jsonObj.put("IsAuthBgRuning", (o.getVerifyAndAuthorize()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning())?true:false);	//是否后台生成
						
						//费用信息
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 0", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1: //为委托单添加原始记录
			JSONObject retJSON1 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionId");	//委托单Id
//				String Number = req.getParameter("Number");	//原始记录张数
				String Model = req.getParameter("Model");	//规格型号
				String Range = req.getParameter("Range");	//测量范围
				String Accuracy = req.getParameter("Accuracy");	//精度等级
				String TargetApplianceId = req.getParameter("TargetApplianceId");	//受检器具ID
				String TaskAssignId = req.getParameter("TaskId");	//任务分配ID
//				String Specifications = req.getParameter("Specifications");	//技术规范
//				String Standards = req.getParameter("Standards");	//计量标准
//				String StandardAppliances = req.getParameter("StandardAppliances");	//标准器具
				String ApplianceName = req.getParameter("ApplianceName");	//常用名称
				String WorkType = req.getParameter("WorkType");	//工作性质
				String WorkLocation = req.getParameter("WorkLocation");	//工作地点
				String Manufacturer = req.getParameter("Manufacturer");	//制造厂
				String ApplianceCode = req.getParameter("ApplianceCode");	//出厂编号
				String ApplianceManageCode = req.getParameter("ApplianceManageCode");	//管理编号
				String Temp = req.getParameter("Temp");	//环境温度
				String Humidity = req.getParameter("Humidity");	//相对湿度
				String Pressure = req.getParameter("Pressure");	//大气压
				String Conclusion = req.getParameter("Conclusion");	//结论
				String WorkDate = req.getParameter("WorkDate");	//检校日期				
				String Quantity = req.getParameter("Quantity");	//器具数量
				String RepairLevel = req.getParameter("RepairLevel");	//修理级别
				String MaterialDetail = req.getParameter("MaterialDetail");	//配件明细
				String CarFee = req.getParameter("CarFee");	//交通费
				String DebugFee = req.getParameter("DebugFee");	//调试费
				String MaterialFee = req.getParameter("MaterialFee");	//配件费
				String OtherFee = req.getParameter("OtherFee");	//其他费
				
				String OtherCond = req.getParameter("OtherCond");	//其它
				String StdOrStdAppUsageStatus = req.getParameter("StdOrStdAppUsageStatus");	//标准(器具)使用前后状态检查
				String AbnormalDesc = req.getParameter("AbnormalDesc");	//异常情况说明
				
				String Mandatory = req.getParameter("Mandatory");	//是否强管
				String MandatoryCode = req.getParameter("MandatoryCode");	//强管唯一性号
				String MandatoryPurpose = req.getParameter("MandatoryPurpose");	//强管类型/用途
				String MandatoryItemType = req.getParameter("MandatoryItemType");	//强检目录对应项别
				String MandatoryType = req.getParameter("MandatoryType");	//强检目录对应种别
				String MandatoryApplyPlace = req.getParameter("MandatoryApplyPlace");	//使用/安装地点				
				
				String StandardAppliancesString=req.getParameter("StandardAppliancesString");//选择的标准器具
				String StandardsString=req.getParameter("StandardsString");//选择的计量标准
				String SpecificationsString=req.getParameter("SpecificationsString");//选择的技术规范
				
				String FirstIsUnqualified=req.getParameter("FirstIsUnqualified");//首检是否合格
				String UnqualifiedReason=req.getParameter("UnqualifiedReason");//不合格原因
				String Remark=req.getParameter("Remark");//备注
				
				String VerifyUser = req.getParameter("VerifyUser");	//核验人员
				
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("委托单未指定！");
				}
				if (ApplianceCode != null && ApplianceCode.length() >= 50) {
					throw new Exception("出厂编号长度不能超过50！");
				}
				if(Quantity == null || Quantity.trim().length() == 0){
					throw new Exception("器具数量为空！");
				}
				if(TaskAssignId == null || TaskAssignId.length() == 0){
					throw new Exception("任务分配ID为空！");
				}
				if(WorkType == null || WorkType.length() == 0){
					throw new Exception("工作性质为空！");
				}
				if(WorkDate == null || WorkDate.length() == 0){
					throw new Exception("检校日期为空！");
				}
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("委托单不存在！");
				}
//				JSONArray specificationArray = new JSONArray(Specifications);
//				JSONArray standardArray = new JSONArray(Standards);
//				JSONArray stdApplianceArray = new JSONArray(StandardAppliances);
				Integer NumberInt = Integer.parseInt(Quantity.trim());
				if(NumberInt <= 0 ){
					throw new Exception("器具数量必须为大于0的整数！");
				}
				//查找原始记录数量
				Integer existedNumber = 0;
				List<Long> existedCountList = new OriginalRecordManager().findByHQL("select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 ", 
						cSheet.getId());
				if(existedCountList.get(0) != null){
					existedNumber = existedCountList.get(0).intValue();
				}
				if(NumberInt + existedNumber > cSheet.getQuantity()){
					throw new Exception("器具数量无效，原始记录的器具总数不得超过委托单的器具数量！");
				}
				
				
//				Map<Integer,Specification> speMap = new HashMap<Integer, Specification>();	//所依据的技术规范
//				Map<Integer,Standard> stdMap = new HashMap<Integer, Standard>();	//所依据的标准
//				Map<Integer,StandardAppliance> stdAppMap = new HashMap<Integer, StandardAppliance>();	//使用的标准器具
//				for(int i = 0; i < specificationArray.length(); i++){
//					Specification spe = new Specification();
//					JSONObject jsonObj = specificationArray.getJSONObject(i);
//					spe.setId(jsonObj.getInt("SpecificationId"));	//规程ID
//					speMap.put(spe.getId(), spe);
//				}
//				
//				for(int i = 0; i < standardArray.length(); i++){
//					Standard std = new Standard();
//					JSONObject jsonObj = standardArray.getJSONObject(i);
//					std.setId(jsonObj.getInt("StandardId"));	//标准ID
//					stdMap.put(std.getId(), std);
//				}
//				
//				for(int i = 0; i < stdApplianceArray.length(); i++){
//					StandardAppliance stdapp = new StandardAppliance();
//					JSONObject jsonObj = stdApplianceArray.getJSONObject(i);
//					stdapp.setId(jsonObj.getInt("StandardApplianceId"));	//标准器具ID
//					stdAppMap.put(stdapp.getId(), stdapp);
//				}
				TargetAppliance tApp = new TargetApplianceManager().findById(Integer.parseInt(TargetApplianceId));
				
				//受检器具关联的技术规范、计量标准、标准器具
				List<Specification> speList = new ArrayList<Specification>();
				List<Standard> stdList = new ArrayList<Standard>();
				List<StandardAppliance> stdAppList = new ArrayList<StandardAppliance>();
				
				TgtAppSpecManager TgtAppSpecMgr = new TgtAppSpecManager();//
				StdTgtAppManager StdTgtAppMgr = new StdTgtAppManager();//
				TgtAppStdAppManager TgtAppStdAppMgr = new TgtAppStdAppManager();//
				
				SpecificationManager SpecificationMgr = new SpecificationManager();
				StandardManager StandardMgr = new StandardManager();
				StandardApplianceManager StandardApplianceMgr = new StandardApplianceManager();
				
				
				if(SpecificationsString==null||SpecificationsString.length()==0){//技术规程
					speList = TgtAppSpecMgr.findByHQL("select distinct model.specification from TgtAppSpec as model where model.targetAppliance.id=?", tApp.getId());
				}else{//选择了技术规程，表明是临时建立的器具关系
					JSONArray speListArray=new JSONArray(SpecificationsString);
					for(int i=0;i<speListArray.length();i++){
						speList.add(SpecificationMgr.findById(speListArray.getJSONObject(i).getInt("id")));
					}
				}
				if(StandardsString==null||StandardsString.length()==0){//计量标准
					stdList = StdTgtAppMgr.findByHQL("select distinct model.standard from StdTgtApp as model where model.targetAppliance.id=?", tApp.getId());
				}else{//选择了计量标准，表明是临时建立的器具关系
					JSONArray stdListArray=new JSONArray(StandardsString);
					for(int i=0;i<stdListArray.length();i++){
						stdList.add(StandardMgr.findById(stdListArray.getJSONObject(i).getInt("id")));
					}
				}
				if(StandardAppliancesString==null||StandardAppliancesString.length()==0){//标准器具
					stdAppList = TgtAppStdAppMgr.findByHQL("select distinct model.standardAppliance from TgtAppStdApp as model where model.targetAppliance.id=?", tApp.getId());
				}else{//选择了标准器具，表明是临时建立的器具关系
					JSONArray stdAppListArray=new JSONArray(StandardAppliancesString);
					for(int i=0;i<stdAppListArray.length();i++){
						stdAppList.add(StandardApplianceMgr.findById(stdAppListArray.getJSONObject(i).getInt("id")));
					}
				}
								
				TaskAssign taskAssign = new TaskAssignManager().findById(Integer.parseInt(TaskAssignId));
				if(taskAssign == null){
					throw new Exception("该任务分配不存在，不能添加原始记录！");
				}
				if(taskAssign.getStatus() == 1){
					throw new Exception("该任务分配已注销，不能添加原始记录！");
				}
				
				if(VerifyUser !=  null && VerifyUser.length() > 0){	//核验人
					if(VerifyUser.equalsIgnoreCase(taskAssign.getSysUserByAlloteeId().getName())){
						throw new Exception("核验人员不能与检校人员相同！");
					}
					QualificationManager qfMgr = new QualificationManager();
					List<Integer> qfTypeList = new ArrayList<Integer>();
					qfTypeList.add(FlagUtil.QualificationType.Type_Heyan);
					
					//检查是否有核验的资质
					List<Object[]> qfRetList = qfMgr.getQualifyUsers(VerifyUser, tApp.getApplianceStandardName().getId(), 0, qfTypeList);
					if(qfRetList.size() == 0){
						throw new Exception(String.format("指定的‘核验人员’:'%s'没有器具标准名称'%s'的 核验资质！",
								VerifyUser,
								tApp.getApplianceStandardName().getName()));
					}
				}
				
				OriginalRecord o = new OriginalRecord();
				o.setSysUserByCreatorId((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));	//原始记录创建人
				o.setCreateTime(new Timestamp(System.currentTimeMillis()));	//创建时间
				o.setCommissionSheet(cSheet);	//委托单
				o.setTargetAppliance(tApp);	//受检器具
				o.setStatus(0);	//状态
				o.setSysUserByStaffId(taskAssign.getSysUserByAlloteeId());	//检/校人员
				o.setTaskAssign(taskAssign);	//任务分配
				o.setModel(Model);
				o.setRange(Range);
				o.setAccuracy(Accuracy);
				o.setApplianceName(ApplianceName);	//常用名称
				o.setWorkType(WorkType);	//工作性质
				o.setWorkLocation(WorkLocation);	//工作地点
				o.setManufacturer(Manufacturer);	//制造厂商
				o.setApplianceCode(ApplianceCode);	//出厂编号
				o.setManageCode(ApplianceManageCode);	//管理编号
				o.setTemp(Temp);	//环境温度
				o.setHumidity(Humidity);	//相对湿度
				o.setPressure(Pressure);	//大气压
				o.setConclusion(Conclusion);	//结论
				o.setFirstIsUnqualified(FirstIsUnqualified);//首检是否合格
				o.setUnqualifiedReason(UnqualifiedReason);//不合格原因
				o.setRemark(Remark);//备注
				
				if(WorkDate != null && WorkDate.trim().length() > 0){
					Date workDate = new Date(DateTimeFormatUtil.DateFormat.parse(WorkDate).getTime());
					o.setWorkDate(workDate);	//检校日期
					if(tApp.getTestCycle() != null && !o.getWorkType().equals("校准")){	//计算最大的有效日期
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(workDate);
						calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + tApp.getTestCycle());
						calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);	//有效期=检定日期+检定周期-1天
						o.setValidity(new java.sql.Date(calendar.getTimeInMillis()));  	//有效期
					}
				}				
				o.setQuantity(Integer.parseInt(Quantity));	//该原始记录中的器具数量
				
				o.setMandatory(cSheet.getMandatory()==true?"否":"是");	//是否强管				
				
				o.setTestFee(tApp.getFee()==null?null:tApp.getFee() * NumberInt);	//检定费
				o.setRepairLevel(RepairLevel);	//修理级别
				if(RepairLevel != null && RepairLevel.trim().length() > 0){	//修理费
					if(RepairLevel.equalsIgnoreCase("小")){
						o.setRepairFee(tApp.getSrfee());
					}else if(RepairLevel.equalsIgnoreCase("中")){
						o.setRepairFee(tApp.getMrfee());
					}else if(RepairLevel.equalsIgnoreCase("大")){
						o.setRepairFee(tApp.getLrfee());
					}
				}
				if(CarFee != null && CarFee.length() > 0){
					o.setCarFee(Double.parseDouble(CarFee));	//交通费
				}
				if(DebugFee != null && DebugFee.length() > 0){
					o.setDebugFee(Double.parseDouble(DebugFee));	//调试费
				}
				if(MaterialFee != null && MaterialFee.length() > 0){
					o.setMaterialFee(Double.parseDouble(MaterialFee));	//配件费
				}
				o.setMaterialDetail(MaterialDetail);//配件明细
				if(OtherFee != null && OtherFee.length() > 0){
					o.setOtherFee(Double.parseDouble(OtherFee));	//其他费
				}
				o.setTotalFee((o.getTestFee()==null?0.0:o.getTestFee()) + 
						(o.getRepairFee()==null?0.0:o.getRepairFee()) + 
						(o.getMaterialFee()==null?0.0:o.getMaterialFee()) + 
						(o.getCarFee()==null?0.0:o.getCarFee()) + 
						(o.getDebugFee()==null?0.0:o.getDebugFee()) + 
						(o.getOtherFee()==null?0.0:o.getOtherFee()) );
				
				
				o.setOtherCond(OtherCond);	//其它条件
				o.setStdOrStdAppUsageStatus(StdOrStdAppUsageStatus);	//标准（器具）使用状态前后审查
				o.setAbnormalDesc(AbnormalDesc);	//异常情况说明
				
				o.setMandatory(Mandatory);
				o.setMandatoryCode(MandatoryCode);
				o.setMandatoryPurpose(MandatoryPurpose);
				o.setMandatoryItemType(MandatoryItemType);
				o.setMandatoryType(MandatoryType);
				o.setMandatoryApplyPlace(MandatoryApplyPlace);
//				while(NumberInt-- > 0){
//					//存原始记录
//					OriginalRecord o = new OriginalRecord();
//					o.setCommissionSheet(cSheet);	//委托单
//					o.setModel(Model);
//					o.setRange(Range);
//					o.setAccuracy(Accuracy);
//					o.setManufacturer(cSheet.getManufacturer()==null?"":cSheet.getManufacturer());	//制造厂商
//					o.setApplianceCode(cSheet.getAppFactoryCode()==null?"":cSheet.getAppFactoryCode());	//出厂编号
//					o.setManageCode(cSheet.getAppManageCode()==null?"":cSheet.getAppManageCode());	//管理编号
//					o.setStatus(0);	//状态
//					o.setTargetAppliance(tApp);	//受检器具
//					o.setSysUserByStaffId(taskAssign.getSysUserByAlloteeId());	//检/校人员
//					o.setTaskAssign(taskAssign);	//任务分配
//					o.setQuantity(1);	//该原始记录中的器具数量
//					
//					o.setWorkDate(new Date(System.currentTimeMillis()));	//检验日期默认为当前日期
//					o.setWorkLocation((cSheet.getCommissionType() == 2)?"被测仪器使用现场":"本所实验室");
//					o.setWorkType(CommissionSheetFlagUtil.getReportTypeByFlag(cSheet.getReportType()));
//					o.setMandatory(cSheet.getMandatory()==true?"否":"是");
//					o.setApplianceName(cSheet.getSpeciesType()?null:cSheet.getApplianceName());	//常用名称
//					
//					//检测费
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
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("添加原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 1", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//注销一张原始记录
			JSONObject retJSON2 = new JSONObject();
			try {
				String oRecordId = req.getParameter("OriginalRecordId");	//原始记录号
				
				if(oRecordId == null || oRecordId.trim().length() == 0 ){
					throw new Exception("参数不完整！");
				}
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(oRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				SysUser user = (SysUser) req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(!(oRecord.getSysUserByStaffId().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())||(user.getName().equals("系统管理员")))){
					throw new Exception("您不是该原始记录的检校人员，不能注销该原始记录！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能注销原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					throw new Exception("该委托单已完工，不能注销原始记录！");
				}
				
				oRecord.setStatus(1);	//状态：注销
				if(!oRecordMgr.update(oRecord)){
					throw new Exception("提交服务器失败！");
				}
				retJSON2.put("IsOK", true);
				
			} catch (Exception e){
			
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("注销原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 2", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3:	//查找一份委托单所有已上传的正式的原始记录（不做的内容：为原始记录关联至已上传的Excel）
			JSONObject retJSON3 = new JSONObject();
			try {
				/*
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单未指定！");
				}
				List<OriginalRecord> oRecRetList = oRecordMgr.findByPropertyBySort("taskAssign.id", true,
						new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), 
						new KeyValueWithOperator("status", 1, "<>"),
						new KeyValueWithOperator("originalRecordExcel.pdf", null, "is not null")	//证书为正式版
				);
				
				if(oRecRetList != null && oRecRetList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(OriginalRecord o:oRecRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
						jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
						jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录模板文件存放的文件集名称
						
						
						jsonObj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
						
						jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//任务分配ID
						jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//检验项目名称
						
						jsonObj.put("Staff", o.getSysUser()==null?"":o.getSysUser().getName());	//检/校人员
						jsonObj.put("StaffId", o.getSysUser()==null?-1:o.getSysUser().getId());	//检/校人员Id
						
						jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//原始记录Excel文件ID
						jsonObj.put("ExcelCode", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getCode());	//原始记录Excel文件的编号
						jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//原始记录的xls文件ID
						jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//原始记录版本号
						jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//原始记录的PDF文件ID
						jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":String.format("%s.pdf", o.getOriginalRecordExcel().getFileName().substring(0, o.getOriginalRecordExcel().getFileName().lastIndexOf('.'))));	//原始记录xls文件的文件名
						
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
		case 4:	//添加一份原始记录的“审核和批准”申请
			JSONObject retJSON4 = new JSONObject();
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");	//原始记录Id
				String Version = req.getParameter("Version");	//版本号
				String Verifier = req.getParameter("Verifier");	//核验人Id
				String Authorizer = req.getParameter("Authorizer");//批准人Id
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				int versionInt = -1;
				if(Version != null && Version.length() > 0){
					versionInt = Integer.parseInt(Version);
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null){
					throw new Exception("您尚未登录，不能执行此操作！");
				}
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() == null || //oRecord.getOriginalRecordExcel().getPdf() == null || 
						oRecord.getCertificate() == null || oRecord.getCertificate().getPdf() == null){
					throw new Exception("请先提交证书！");
				}
				VerifyAndAuthorize v = oRecord.getVerifyAndAuthorize();
				if(v != null && v.getAuthorizeResult() != null && v.getAuthorizeResult()){
					throw new Exception("该原始记录的已通过核验和授权签字，不能重新提交申请！");
				}
				if(v != null && !oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId()) && 
						v.getOriginalRecordExcel().getId().equals(oRecord.getOriginalRecordExcel().getId()) &&
						v.getCertificate().getId().equals(oRecord.getCertificate().getId())){
					throw new Exception("已经存在该原始记录的核验和签字申请，您不是该任务所分配的检验员，不能重新提交申请！");
				}
				if(v != null && versionInt < v.getVersion().intValue()){
					throw new Exception("版本冲突，该原始记录的核验和签字申请已被重新提交过，请刷新查看！");
				}
				
				UserManager userMgr = new UserManager();
				SysUser verifier = userMgr.findById(Integer.parseInt(Verifier));
				if(verifier == null){
					throw new Exception("找不到指定的核验人!");
				}
				if(oRecord.getSysUserByStaffId().getId().equals(verifier.getId())){
					throw new Exception("核验人不可以是检验员自己!");
				}
				
				SysUser authorizer = userMgr.findById(Integer.parseInt(Authorizer));
				if(authorizer == null){
					throw new Exception("找不到指定的授权签字人!");
				}
				
				VerifyAndAuthorize vNew = null;
				if(v != null ){	
					if(v.getSysUserByVerifierId() != null && v.getSysUserByAuthorizerId() != null){//原核验和授权签字记录有效：需要保留，重新生成一份新的核验和授权签字记录
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
				if(vNew.getId() != null){  //更新记录
					if(vMgr.update(vNew)){
						retJSON4.put("IsOK", true);
//						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_OriginalAndCertificateVerify, String.format("新收到一条原始记录和证书的核验任务，委托单号：%s", oRecord.getCommissionSheet().getCode()), verifier, FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateVerify);	//即时消息
						return;
					}
				}else{
					if(vMgr.save(vNew)){	//新增记录
						oRecord.setVerifyAndAuthorize(vNew);
						if(oRecordMgr.update(oRecord)){
							retJSON4.put("IsOK", true);
//							InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_OriginalAndCertificateVerify, String.format("新收到一条原始记录和证书的核验任务，委托单号：%s", oRecord.getCommissionSheet().getCode()), verifier, FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateVerify);	//即时消息
							return;
						}
					}
				}
				
				throw new Exception("更新数据库失败！");
			} catch (Exception e){
				try {
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("提交核验和授权签字申请失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 3", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	//查询核验任务列表
			JSONObject retJSON5 = new JSONObject();
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
				String type=req.getParameter("type");	//type=1代表是重新编制证书的核验列表
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
				if(CommissionNumber != null && CommissionNumber.trim().length() > 0){
					CommissionNumber = URLDecoder.decode(CommissionNumber.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("commissionSheet.code", CommissionNumber, "="));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				if(type==null){
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//委托单尚未完工或注销的
				}else{
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">="));	//委托单尚未注销的
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiZhuXiao, "<"));	//委托单尚未注销的
				}
				
				condList.add(new KeyValueWithOperator("status", 1, "<>"));	//原始记录尚未注销的
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.sysUserByVerifierId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //审核人
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.verifyTime", null, "is null"));	//尚未审核的
				condList.add(new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//证书是正式版本
				condList.add(new KeyValueWithOperator("certificate.sysUser", null, "is null"));	//检定员已核验
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));	//任务尚未注销
				
				int total = oRecordMgr.getTotalCount(condList);
				List<OriginalRecord> oRetList = oRecordMgr.findPagedAllBySort(page, rows, "verifyAndAuthorize.createTime", true, condList);
				RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
				for(OriginalRecord o : oRetList){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
//					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//委托时间
					
					jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//受检器具ID
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//器具标准名称
					jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录模板文件存放的文件集名称
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("Status", o.getStatus());	//原始记录的状态
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注信息
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//器具数量
					
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
					
					jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//任务分配ID
					jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//检验项目名称
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//检/校人员
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//检/校人员Id
					jsonObj.put("Validity", o.getValidity()==null?"":o.getValidity());	//有效期
					
					jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//原始记录Excel文件ID
					jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//原始记录的xls文件ID
					jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//原始记录版本号
					jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//原始记录的PDF文件ID
					jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//原始记录xls文件的文件名
					
					jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//证书文件ID
					jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//证书文件的Word文件ID
					jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//原始记录版本号
					jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//证书文件的PDF文件ID
					jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//证书文件Doc文件的文件名
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//证书编号
					
					jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//核验和授权 记录ID
					jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//核验和授权 记录版本号
					jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
					jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//核验和授权 记录的原始记录PDF
					jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
					jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//核验和授权 记录中证书的PDF
					jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//核验人
					jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//核验时间
					jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//核验结果
					jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//核验备注
					if(o.getVerifyAndAuthorize() == null){
						jsonObj.put("AuthorizerId", "");	//批准人（签字人）
						jsonObj.put("AuthorizerName","");
					}else{
						if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//实际执行的批准人（签字人）
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
						}else{
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//批准人（签字人）
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
						}
					}
					jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//批准时间
					jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//批准结果
					jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//批准备注
					if(type!=null){//重新编制证书的相关信息
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 5", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6:	//原始记录和证书核验
			JSONObject retJSON6 = new JSONObject();
			try {
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//办理结果：0不通过；1通过
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//办理的备注信息
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("参数不完整！");
				}
				
				String[] oRecordIdArray = OriginalRecordIds.split(";");
				if(oRecordIdArray.length == 0 || oRecordIdArray[0] == null || oRecordIdArray[0].length() == 0){
					throw new Exception("参数不完整！");
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
//						throw new Exception("核验任务不存在！");
					}
					VerifyAndAuthorize v = o.getVerifyAndAuthorize();
					if(v.getVerifyTime() != null){
						continue;
//						throw new Exception("该核验任务已完成，请勿重复操作！");
					}
					if(v.getSysUserByVerifierId()==null || !v.getSysUserByVerifierId().getId().equals(((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId())){
						continue;
//						throw new Exception("您不是该核验任务的接收者，不能执行此操作！");
					}
					v.setVerifyRemark(ExecuteMsg);
					v.setVerifyResult(Integer.parseInt(ExecutorResult)>0?true:false);
					v.setVerifyTime(new Timestamp(System.currentTimeMillis()));
					vMgr.update(v);
					doneSuccess ++;
					if(v.getVerifyResult()){ //核验通过
//						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_OriginalAndCertificateAuthorize, String.format("新收到一条原始记录和证书的授权签字任务，委托单号：%s", o.getCommissionSheet().getCode()), v.getSysUserByAuthorizerId(), FlagUtil.SmsAndInfomationType.Type_OriginalAndCertificateAuthorize);	//即时消息
					}
				}
				retJSON6.put("IsOK", true);
				retJSON6.put("msg", String.format("已成功核验 '%d'份原始记录和证书！", doneSuccess));
			} catch (Exception e){
				try {
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("提交核验结果失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 6", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7:	//查询签字任务列表
			JSONObject retJSON7 = new JSONObject();
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
				String type=req.getParameter("type");	//type=1代表是重新编制证书的核验列表
				
				String OwnTask = req.getParameter("OwnTask");	//为true表示返回当前用户的任务列表，为false表示返回所有用户的任务列表 
				if(OwnTask == null){
					OwnTask = "true";
				}
				
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();	//查询条件
				if(CommissionNumber != null && CommissionNumber.trim().length() > 0){
					CommissionNumber = URLDecoder.decode(CommissionNumber.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("commissionSheet.code", CommissionNumber, "="));
				}
				if(CustomerName != null && CustomerName.trim().length() > 0){
					CustomerName = URLDecoder.decode(CustomerName.trim(), "UTF-8"); //解决jquery传递中文乱码问题
					condList.add(new KeyValueWithOperator("commissionSheet.customerName", "%"+CustomerName+"%", "like"));
				}
				if(type==null){
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//委托单尚未完工或注销的
				}else{
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">="));	//委托单尚未注销的
					condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiZhuXiao, "<"));	//委托单尚未注销的
				}
				condList.add(new KeyValueWithOperator("status", 1, "<>"));	//原始记录尚未注销的
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.verifyResult", true, "=")); //已通过核验的
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));	//任务分配尚未注销的
				
				if(OwnTask.equalsIgnoreCase("true")){
					condList.add(new KeyValueWithOperator("verifyAndAuthorize.sysUserByAuthorizerId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //申请时指定的批准人
				}
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.authorizeTime", null, "is null"));	//尚未签字的
				condList.add(new KeyValueWithOperator("verifyAndAuthorize.certificate.pdf", null, "is not null")); //证书为正式版本的
				
				int total = oRecordMgr.getTotalCount(condList);
				List<OriginalRecord> oRetList = oRecordMgr.findPagedAllBySort(page, rows, "verifyAndAuthorize.createTime", true, condList);
				RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
				for(OriginalRecord o : oRetList){
					JSONObject jsonObj = new JSONObject();
										
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
//					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//委托时间
					
					jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//受检器具ID
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//器具标准名称
					jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录模板文件存放的文件集名称
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("Status", o.getStatus());	//原始记录的状态
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注信息
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//器具数量
					
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
					
					jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//任务分配ID
					jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//检验项目名称
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//检/校人员
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//检/校人员Id
					jsonObj.put("Validity", o.getValidity()==null?"":o.getValidity());	//有效期
					
					jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//原始记录Excel文件ID
					jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//原始记录的xls文件ID
					jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//原始记录版本号
					jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//原始记录的PDF文件ID
					jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//原始记录xls文件的文件名
					
					jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//证书文件ID
					jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//证书文件的Word文件ID
					jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//原始记录版本号
					jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//证书文件的PDF文件ID
					jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//证书文件Doc文件的文件名
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//证书编号
					
					jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//核验和授权 记录ID
					jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//核验和授权 记录版本号
					jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
					jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//核验和授权 记录的原始记录PDF
					jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
					jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//核验和授权 记录中证书的PDF
					jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//核验人
					jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//核验时间
					jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//核验结果
					jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//核验备注
					if(o.getVerifyAndAuthorize() == null){
						jsonObj.put("AuthorizerId", "");	//批准人（签字人）
						jsonObj.put("AuthorizerName","");
					}else{
						if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//实际执行的批准人（签字人）
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
						}else{
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//批准人（签字人）
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
						}
					}
					jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//批准时间
					jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//批准结果
					jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//批准备注
					jsonObj.put("IsAuthBgRuning", (o.getVerifyAndAuthorize()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning())?true:false);	//是否后台生成
					if(type!=null){//重新编制证书的相关信息
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 7", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 7", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		case 8:	//原始记录和证书授权签字
			JSONObject retJSON8 = new JSONObject();
			try {
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Ids
				String ExecutorResult = req.getParameter("ExecutorResult");	//办理结果：0不通过；1通过
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//办理的备注信息
				String FromAuthByCode = req.getParameter("FromAuthByCode");	//若为代签：则此字段值为true
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0 || 
						ExecutorResult == null || ExecutorResult.length() == 0){
					throw new Exception("参数不完整！");
				}
				
				String []oRecordIdArray = OriginalRecordIds.split(";");
				if(oRecordIdArray.length == 0 || oRecordIdArray[0] == null || oRecordIdArray[0].length() == 0){
					throw new Exception("参数不完整！");
				}
				if(oRecordIdArray.length > 1){
					throw new Exception("授权签字立即执行每次只能处理一张证书！");
				}
				String oRecordId = oRecordIdArray[0];
				
				OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
				if(o == null || o.getVerifyAndAuthorize() == null){
					throw new Exception("签字任务不存在！");
				}
				if(o.getCertificate() == null || o.getCertificate().getPdf() == null){
					throw new Exception("原始记录或证书未提交，不能进行签字操作！");
				}
				VerifyAndAuthorize v = o.getVerifyAndAuthorize();
				if(v.getVerifyTime() == null || !v.getVerifyResult()){
					throw new Exception("该原始记录或证书尚未通过核验，不能执行签字操作！");
				}
				if(v.getAuthorizeTime() != null && v.getIsAuthBgRuning() == null){
					throw new Exception("该签字任务已完成，请勿重复操作！");
				}
				if(!v.getOriginalRecordExcel().getId().equals(o.getOriginalRecordExcel().getId()) ||
						!v.getCertificate().getId().equals(o.getCertificate().getId())){
					throw new Exception("该签字任务已过期，原因：原始记录或证书有改动！");
				}
				
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
//				if(!new QualificationManager().checkUserQualify(loginUser.getId(), o.getTargetAppliance().getApplianceStandardName().getId(), 0, FlagUtil.QualificationType.Type_Qianzi)){
//					throw new Exception("您没有该器具授权签字的权限！");
//				}
				if(FromAuthByCode != null && FromAuthByCode.equals("true")){	//代签
					//无操作
				}else{	//授权签字人执行签名
					if(loginUser == null || !loginUser.getId().equals(v.getSysUserByAuthorizerId().getId())){
						throw new Exception(String.format("您不是该签字任务的接收人:'%s'，不能执行授权签字！", v.getSysUserByAuthorizerId().getName()));
					}
				}
				
				//备份更新数据库前的数据（已被签字失败时还原，用于兼容后台执行签字）
				Boolean bIsBgRun = v.getIsAuthBgRuning();
				String authRemark = v.getAuthorizeRemark();
				Boolean bAuthResult = v.getAuthorizeResult();
				SysUser authExecutor = v.getSysUserByAuthorizeExecutorId();
				Timestamp authTime = v.getAuthorizeTime();
				
				//更新数据库
				v.setAuthorizeRemark(ExecuteMsg);
				v.setAuthorizeResult(Integer.parseInt(ExecutorResult)>0?true:false);
				v.setAuthorizeTime(new Timestamp(System.currentTimeMillis()));
				v.setSysUserByAuthorizeExecutorId(loginUser);
				v.setIsAuthBgRuning(null);
				
				VerifyAndAuthorizeManager vMgr = new VerifyAndAuthorizeManager();
				vMgr.update(v);
				if(v.getVerifyResult()){//签字通过
					try{
						AuthBackgroundRuningService.AuthorizeExecute(o, v);	//执行签名
					}catch(Exception ex){	//接收到异常，则还原数据库
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
					retJSON8.put("msg", String.format("执行授权签字失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 8", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 8", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON8.toString());
			}
			break;
		case 9:	//查询多个委托单下所有的证书PDF，并返回所有证书的ID（用于打印证书）
			JSONObject retJSON9 = new JSONObject();
			try{
				String CommissionIds = req.getParameter("CommissionIds");	//委托单Id
				
				if(CommissionIds == null || CommissionIds.trim().length() == 0){
					throw new Exception("委托单未选择！");
				}
				String []CommissionIdArray = CommissionIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录(签字已通过且不是正在后台执行)
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
								throw new Exception("证书"+o.getCertificate().getCertificateCode()+"未完成核验签字");
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
					retJSON9.put("msg", String.format("查询委托单证书失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 9", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 9", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			break;
		case 10: //查询一个委托单下用户选择的证书PDF，并返回所有证书的ID（用于打印证书）
			JSONObject retJSON10 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("原始记录未选中！");
				}
				String []OriginalRecordIdArray = OriginalRecordIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				JSONArray cerArray = new JSONArray();
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录(签字已通过且不是正在后台执行)
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
								throw new Exception("证书"+o.getCertificate().getCertificateCode()+"未完成核验签字");
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
					retJSON10.put("msg", String.format("查询委托单证书失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 10", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 10", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
			}
			break;
		case 11:	//更新一个原始记录Excel文件的配置文件为当前使用的版本
			JSONObject retJSON11 = new JSONObject();
			try {
				String oRecordId = req.getParameter("OriginalRecordId");	//原始记录Id
				if(oRecordId == null || oRecordId.trim().length() == 0){
					throw new Exception("参数不完整！");
				}
				OriginalRecord o = oRecordMgr.findById(Integer.parseInt(oRecordId));
				if(o == null || o.getOriginalRecordExcel() == null || o.getOriginalRecordExcel().getFileName().length()==0){
					throw new Exception("该原始记录尚未生成Excel文件！");
				}
				OriginalRecordExcel excel = o.getOriginalRecordExcel();
				//查找字段定义xml文件
				HashMap<String, Object> map = new HashMap<String, Object>();
				String xlsXmlFileName = String.format("%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf(".")));	//原始记录配置文件的名称
				map.put(MongoDBUtil.KEYNAME_FileSetName, o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录的配置文件的文件集名称
				Pattern pattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("找不到当前正在使用的配置文件:%s", xlsXmlFileName));
				}
				String xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//原始记录xml文件的ID
				if(excel.getXml().equalsIgnoreCase(xlsXmlFileId)){
					throw new Exception("该Excel配置文件已经是当前使用的版本，无需更新!");
				}
				excel.setXml(xlsXmlFileId);
				if(new OriginalRecordExcelManager().update(excel)){
					retJSON11.put("IsOK", true);
				}else{
					throw new Exception("更新数据库失败！");
				}
				
			} catch (Exception e){
				try {
					retJSON11.put("IsOK", false);
					retJSON11.put("msg", String.format("更新原始记录Excel的配置文件失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 11", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 11", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON11.toString());
			}
			break;
		case 12:	//原始记录和证书授权 （批量签字：若通过则后台签字）
			JSONObject retJSON12 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Ids
				String ExecutorResult = req.getParameter("ExecutorResult");	//办理结果：0不通过；1通过
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//办理的备注信息
				String FromAuthByCode = req.getParameter("FromAuthByCode");	//若为代签：则此字段值为true
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0 || 
						ExecutorResult == null || ExecutorResult.length() == 0){
					throw new Exception("参数不完整！");
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
							if(FromAuthByCode != null && FromAuthByCode.equals("true")){	//代签
								//无操作
							}else{	//授权签字人执行签名
								if(loginUser == null ){
									continue;
								}
							}
							
							if(Integer.parseInt(ExecutorResult)>0){	//审核通过：后台执行签字
								v.setAuthorizeRemark(ExecuteMsg);
								v.setAuthorizeResult(Integer.parseInt(ExecutorResult)>0?true:false);
								v.setAuthorizeTime(new Timestamp(System.currentTimeMillis()));
								v.setSysUserByAuthorizeExecutorId(loginUser);
								AuthBackgroundRuningManager.AddAnAuthBackgroundRuning(v);
							}else{	//审核不通过
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
					retJSON12.put("msg", String.format("成功授权签字 '%d'份证书！", doneSuccess));
				}
			}catch (Exception e){
				try {
					retJSON12.put("IsOK", false);
					retJSON12.put("msg", String.format("授权签字失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 12", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 12", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON12.toString());
			}
			break;
		case 13://查询一个委托单的原始记录，用于查询统计中，含有总计footer
			JSONObject retJSON13 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单未指定！");
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
						jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
						jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
						jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//受检器具ID
						jsonObj.put("ApplianceStandardNameId", o.getTargetAppliance().getApplianceStandardName().getId());	//器具标准名称Id
						jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//器具标准名称
						jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录模板文件存放的文件集名称
						jsonObj.put("Model", o.getModel());
						jsonObj.put("Range", o.getRange());
						jsonObj.put("Accuracy", o.getAccuracy());
						jsonObj.put("Status", o.getStatus());	//原始记录的状态
						jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注信息
						jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//器具数量
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
						
						jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//任务分配ID
						jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//检验项目名称
						
						jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//检/校人员
						jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//检/校人员Id
						jsonObj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));	//有效期
//						jsonObj.put("TechnicalDocs", o.getTechnicalDocs()==null?"":o.getTechnicalDocs());
//						jsonObj.put("Standards", o.getStandards()==null?"":o.getStandards());
//						jsonObj.put("StandardAppliances", o.getStandardAppliances()==null?"":o.getStandardAppliances());
						
						jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//原始记录Excel文件ID
						jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//原始记录的xls文件ID
						jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//原始记录版本号
						jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//原始记录的PDF文件ID
						jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//原始记录xls文件的文件名
						
						jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//证书文件ID
						jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//证书文件的Word文件ID
						jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//原始记录版本号
						jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//证书文件的PDF文件ID
						jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//证书文件Doc文件的文件名
						jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//证书编号
						
						jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//核验和授权 记录ID
						jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//核验和授权 记录版本号
						jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
						jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//核验和授权 记录的原始记录PDF
						jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
						jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//核验和授权 记录中证书的PDF
						jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//核验人
						jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
						jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//核验时间
						jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//核验结果
						jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//核验备注
						if(o.getVerifyAndAuthorize() == null){
							jsonObj.put("AuthorizerId", "");	//批准人（签字人）
							jsonObj.put("AuthorizerName","");
						}else{
							if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//实际执行的批准人（签字人）
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
							}else{
								jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//批准人（签字人）
								jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
							}
						}
						jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//批准时间
						jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//批准结果
						jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//批准备注
						jsonObj.put("IsAuthBgRuning", (o.getVerifyAndAuthorize()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning()!=null&&o.getVerifyAndAuthorize().getIsAuthBgRuning())?true:false);	//是否后台生成
						
						//费用信息
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
					footerObj.put("ApplianceStandardName", "总计");
					footerObj.put("Quantity", Quantity);
					footerObj.put("TestFee", TestFee);
					footerObj.put("RepairFee", RepairFee);
					footerObj.put("MaterialFee", MaterialFee);
					footerObj.put("CarFee", CarFee);
					footerObj.put("AuthorizerId", "");	//批准人（签字人）
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 0", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON13.toString());
			}	
			break;
		case 14:	//查询一个检定人员尚未核定的任务列表
			JSONObject retJSON14 = new JSONObject();
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
				condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//委托单尚未完工或注销的
				condList.add(new KeyValueWithOperator("status", 1, "<>"));	//原始记录尚未注销的
				condList.add(new KeyValueWithOperator("sysUserByStaffId.id", ((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser)).getId(), "="));  //检定员
				condList.add(new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//证书是正式版本
				condList.add(new KeyValueWithOperator("certificate.sysUser", null, "is not null"));	//检定员尚未核定
				condList.add(new KeyValueWithOperator("taskAssign.status", 1, "<>"));	//任务分配尚未注销的
				
				int total = oRecordMgr.getTotalCount(condList);
				List<OriginalRecord> oRetList = oRecordMgr.findPagedAllBySort(page, rows, "certificate.certificateCode", true, condList);
				for(OriginalRecord o : oRetList){
					JSONObject jsonObj = new JSONObject();
					
					jsonObj.put("CommissionCode", o.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", o.getCommissionSheet().getPwd());
//					jsonObj.put("ApplianceName", o.getCommissionSheet().getApplianceName());	//器具名称
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位名称
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(o.getCommissionSheet().getCommissionDate()));	//委托时间
					
					jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
					jsonObj.put("TaskId", o.getTaskAssign().getId());	//任务分配ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//受检器具ID
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//器具标准名称
					jsonObj.put("TemplateFilesetName", o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录模板文件存放的文件集名称
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("Status", o.getStatus());	//原始记录的状态
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注信息
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//器具数量
					
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
					
					jsonObj.put("TaskAssignId", o.getTaskAssign().getId());	//任务分配ID
					jsonObj.put("ProjectName", (o.getTaskAssign().getAppStdNameProTeam()==null || o.getTaskAssign().getAppStdNameProTeam().getProjectName()==null)?"":o.getTaskAssign().getAppStdNameProTeam().getProjectName());	//检验项目名称
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//检/校人员
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//检/校人员Id
					jsonObj.put("Validity", o.getValidity()==null?"":o.getValidity());	//有效期
					jsonObj.put("StaffChecked", (o.getCertificate() != null && o.getCertificate().getSysUser() != null)?false:true);	//检定员是否核定（针对证书不是检定员自己编制的情况）
					
					jsonObj.put("ExcelId", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getId());	//原始记录Excel文件ID
					jsonObj.put("ExcelDoc", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getDoc());	//原始记录的xls文件ID
					jsonObj.put("ExcelVersion", o.getOriginalRecordExcel()==null?-1:o.getOriginalRecordExcel().getVersion());	//原始记录版本号
					jsonObj.put("ExcelPdf", (o.getOriginalRecordExcel()==null || o.getOriginalRecordExcel().getPdf()==null)?"":o.getOriginalRecordExcel().getPdf());	//原始记录的PDF文件ID
					jsonObj.put("ExcelFileName", o.getOriginalRecordExcel()==null?"":o.getOriginalRecordExcel().getFileName());	//原始记录xls文件的文件名
					
					jsonObj.put("CertificateId", o.getCertificate()==null?"":o.getCertificate().getId());	//证书文件ID
					jsonObj.put("CertificateDoc", o.getCertificate()==null?"":o.getCertificate().getDoc());	//证书文件的Word文件ID
					jsonObj.put("CertificateVersion", o.getCertificate()==null?-1:o.getCertificate().getVersion());	//原始记录版本号
					jsonObj.put("CertificatePdf", (o.getCertificate()==null || o.getCertificate().getPdf()==null)?"":o.getCertificate().getPdf());	//证书文件的PDF文件ID
					jsonObj.put("CertificateFileName", o.getCertificate()==null?"":o.getCertificate().getFileName());	//证书文件Doc文件的文件名
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//证书编号
					
					jsonObj.put("VerifyAndAuthorizeId", o.getVerifyAndAuthorize() == null?"":o.getVerifyAndAuthorize().getId());	//核验和授权 记录ID
					jsonObj.put("VerifyAndAuthorizeVersion", o.getVerifyAndAuthorize() == null?-1:o.getVerifyAndAuthorize().getVersion());	//核验和授权 记录版本号
					jsonObj.put("VerifyAndAuthorizeExcelId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getId());
					jsonObj.put("VerifyAndAuthorizeExcelPdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf() == null)?"":o.getVerifyAndAuthorize().getOriginalRecordExcel().getPdf());	//核验和授权 记录的原始记录PDF
					jsonObj.put("VerifyAndAuthorizeCertificateId", o.getVerifyAndAuthorize()==null?"":o.getVerifyAndAuthorize().getCertificate().getId());
					jsonObj.put("VerifyAndAuthorizeCertificatePdf", (o.getVerifyAndAuthorize() == null || o.getVerifyAndAuthorize().getCertificate().getPdf() == null)?"":o.getVerifyAndAuthorize().getCertificate().getPdf());	//核验和授权 记录中证书的PDF
					jsonObj.put("VerifierId", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getId() );	//核验人
					jsonObj.put("VerifierName", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());
					jsonObj.put("VerifyTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getVerifyTime()));	//核验时间
					jsonObj.put("VerifyResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyResult() == null)?"":o.getVerifyAndAuthorize().getVerifyResult()?"1":"0");	//核验结果
					jsonObj.put("VerifyRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getVerifyRemark()==null)?"":o.getVerifyAndAuthorize().getVerifyRemark());	//核验备注
					if(o.getVerifyAndAuthorize() == null){
						jsonObj.put("AuthorizerId", "");	//批准人（签字人）
						jsonObj.put("AuthorizerName","");
					}else{
						if(o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId() != null && !o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().equals(o.getVerifyAndAuthorize().getSysUserByAuthorizerId())){
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getId());	//实际执行的批准人（签字人）
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizeExecutorId().getName());
						}else{
							jsonObj.put("AuthorizerId", o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getId());	//批准人（签字人）
							jsonObj.put("AuthorizerName",o.getVerifyAndAuthorize().getSysUserByAuthorizerId()==null?"":o.getVerifyAndAuthorize().getSysUserByAuthorizerId().getName());
						}
					}
					jsonObj.put("AuthorizeTime", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeTime()==null)?"":DateTimeFormatUtil.DateTimeFormat.format(o.getVerifyAndAuthorize().getAuthorizeTime()));	//批准时间
					jsonObj.put("AuthorizeResult", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeResult() == null)?"":o.getVerifyAndAuthorize().getAuthorizeResult()?"1":"0");	//批准结果
					jsonObj.put("AuthorizeRemark", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getAuthorizeRemark()==null)?"":o.getVerifyAndAuthorize().getAuthorizeRemark());	//批准备注

					
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 14", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 14", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON14.toString());
			}
			break;
		case 15:	//检定员核定通过
			JSONObject retJSON15 = new JSONObject();
			try {
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("参数不完整！");
				}
				
				String[] oRecordIdArray = OriginalRecordIds.split(";");
				if(oRecordIdArray.length == 0 || oRecordIdArray[0] == null || oRecordIdArray[0].length() == 0){
					throw new Exception("参数不完整！");
				}
				int doneSuccess = 0;
				
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能核定该证书！");
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
				retJSON15.put("msg", String.format("已成功核定 '%s'份原始记录和证书！", doneSuccess));
			} catch (Exception e){
				try {
					retJSON15.put("IsOK", false);
					retJSON15.put("msg", String.format("核定失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 15", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 15", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON15.toString());
			}
			break;
		case 16:	//查询多个委托单下所有的原始记录PDF，并返回所有原始记录的ID（用于打印原始记录）
			JSONObject retJSON16 = new JSONObject();
			try{
				String CommissionIds = req.getParameter("CommissionIds");	//委托单Id
				
				if(CommissionIds == null || CommissionIds.trim().length() == 0){
					throw new Exception("委托单未选择！");
				}
				String []CommissionIdArray = CommissionIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				KeyValueWithOperator k4 = new KeyValueWithOperator("taskAssign.status", 1, "<>");
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录(签字已通过且不是正在后台执行)
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
								throw new Exception("证书"+o.getCertificate().getCertificateCode()+"未完成核验签字");
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
					retJSON16.put("msg", String.format("查询委托单原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 16", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 16", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON16.toString());
			}
			break;
		case 17: //查询一个委托单下用户选择的原始记录PDF，并返回所有原始记录的ID（用于打印原始记录）
			JSONObject retJSON17 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("原始记录未选中！");
				}
				String []OriginalRecordIdArray = OriginalRecordIds.split(";");
				KeyValueWithOperator k1 = new KeyValueWithOperator("status", 1, "<>");
				KeyValueWithOperator k2 = new KeyValueWithOperator("certificate.pdf", null, "is not null");
				KeyValueWithOperator k3 = new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, ">=");
				KeyValueWithOperator k4 = new KeyValueWithOperator("taskAssign.status", 1, "<>");
				String hqlQueryString_FinishQuantity = "select model from OriginalRecord as model where model.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//签字通过的原始记录(签字已通过且不是正在后台执行)
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
								throw new Exception("证书"+o.getCertificate().getCertificateCode()+"未完成核验签字");
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
					retJSON17.put("msg", String.format("查询委托单原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 17", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 17", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON17.toString());
			}
			break;
		case 18: //查询一个标准名称的历史证书信息
			JSONObject retJSON18 = new JSONObject();
			try {
				String StandardNameId = req.getParameter("StandardNameId");	//标准名
				String Model = req.getParameter("Model");	//型号规格
				String Range = req.getParameter("Range");	//测量范围
				String Accuracy = req.getParameter("Accuracy");	//准确度等级
				String WorkStaff = req.getParameter("WorkStaff");	//检校人员
				int page18 = 0; // 当前页面
				if (req.getParameter("page") != null)
					page18 = Integer.parseInt(req.getParameter("page").toString());
				int rows18 = 10; // 页面大小
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
					jsonObj.put("OriginalRecordId", o.getId());		//原始记录ID
					jsonObj.put("CommissionSheetId", o.getCommissionSheet().getId());		//委托单ID
					jsonObj.put("TargetApplianceId", o.getTargetAppliance().getId());		//受检器具ID
					jsonObj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位
					jsonObj.put("ApplianceStandardNameId", o.getTargetAppliance().getApplianceStandardName().getId());	//器具标准名称Id
					jsonObj.put("ApplianceStandardName", o.getTargetAppliance().getApplianceStandardName().getName());	//器具标准名称
					jsonObj.put("Model", o.getModel());
					jsonObj.put("Range", o.getRange());
					jsonObj.put("Accuracy", o.getAccuracy());
					jsonObj.put("ApplianceName", o.getApplianceName()==null?"":o.getApplianceName());	//常用名称
					jsonObj.put("Status", o.getStatus());	//原始记录的状态
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注信息
					jsonObj.put("Quantity", o.getQuantity()==null?"":o.getQuantity());	//器具数量
					
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
					jsonObj.put("RepairLevel", o.getRepairLevel()==null?"":o.getRepairLevel());	//修理级别
					jsonObj.put("MaterialDetail", o.getMaterialDetail()==null?"":o.getMaterialDetail());	//配件明细
					
					jsonObj.put("CertificateCode", o.getCertificate()==null?"":o.getCertificate().getCertificateCode());	//证书编号
					
					jsonObj.put("Staff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());	//检/校人员
					jsonObj.put("StaffId", o.getSysUserByStaffId()==null?-1:o.getSysUserByStaffId().getId());	//检/校人员Id
					jsonObj.put("Verifier", (o.getVerifyAndAuthorize()==null || o.getVerifyAndAuthorize().getSysUserByVerifierId()==null)?"":o.getVerifyAndAuthorize().getSysUserByVerifierId().getName());	//核验人
										
					//费用信息
					jsonObj.put("TestFee", o.getTestFee()==null?0:o.getTestFee());
					jsonObj.put("RepairFee", o.getRepairFee()==null?0:o.getRepairFee());
					jsonObj.put("MaterialFee", o.getMaterialFee()==null?0:o.getMaterialFee());
					jsonObj.put("CarFee", o.getCarFee()==null?0:o.getCarFee());
					jsonObj.put("DebugFee", o.getDebugFee()==null?0:o.getDebugFee());
					jsonObj.put("OtherFee", o.getOtherFee()==null?0:o.getOtherFee());
					jsonObj.put("TotalFee", o.getTotalFee()==null?0:o.getTotalFee());
					
					jsonObj.put("Mandatory", o.getMandatory()==null?"":o.getMandatory());	//是否强管
					jsonObj.put("MandatoryCode", o.getMandatoryCode()==null?"":o.getMandatoryCode());	//强管唯一性号
					jsonObj.put("MandatoryItemType", o.getMandatoryItemType()==null?"":o.getMandatoryItemType());	//强检项目对应项别
					jsonObj.put("MandatoryType", o.getMandatoryType()==null?"":o.getMandatoryType());	//强检项目对应种别
					jsonObj.put("MandatoryApplyPlace", o.getMandatoryApplyPlace()==null?"":o.getMandatoryApplyPlace());	//使用/安装地点
					jsonObj.put("MandatoryPurpose", o.getMandatoryPurpose()==null?"":o.getMandatoryPurpose());	//用途/强管类型
					
					jsonObj.put("FirstIsUnqualified", o.getFirstIsUnqualified()==null?"合格":o.getFirstIsUnqualified());	//首检是否合格
					jsonObj.put("UnqualifiedReason", o.getUnqualifiedReason()==null?"":o.getUnqualifiedReason());	//不合格原因
					jsonObj.put("Remark", o.getRemark()==null?"":o.getRemark());	//备注
					
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
				if (e.getClass() == java.lang.Exception.class) { // 自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 18",e);
				} else {
					log.error("error in OriginalRecordServlet-->case 18", e);
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON18.toString());
			}
			break;
			
		case 19:	//打印多个委托单下所有的原始记录的合格证
			JSONObject retJSON19 = new JSONObject();
			try{
				String CommissionIds = req.getParameter("CommissionIds");	//委托单Id
				
				if(CommissionIds == null || CommissionIds.trim().length() == 0){
					throw new Exception("委托单未选择！");
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
							
							if(o.getWorkType()!=null && !o.getWorkType().equals("检验")){
								if(o.getWorkType().equals("检定") && o.getConclusion()!=null&& o.getConclusion().contains("不")){
									continue;
								}
								JSONObject obj = new JSONObject();
								obj.put("ApplianceName", (o.getApplianceName()==null||o.getApplianceName().trim().length()==0)?o.getTargetAppliance().getApplianceStandardName().getName():o.getApplianceName());	//常用名称
								obj.put("Model", o.getModel());			
								obj.put("ApplianceCode", o.getApplianceCode());
								obj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
								obj.put("WorkStaff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());//检/校人员
								obj.put("WorkType", o.getWorkType());//工作性质 检定\校准\检测\检验
								obj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));//有效期
								obj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位
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
					retJSON19.put("msg", String.format("查询委托单原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 19", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 19", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(retJSON19.toString());
			}
			break;
		case 20: //查询一个委托单下用户选择的原始记录PDF，并返回所有原始记录的ID（用于打印原始记录）
			JSONObject retJSON20 = new JSONObject();
			try{
				String OriginalRecordIds = req.getParameter("OriginalRecordIds");	//原始记录Id
				
				if(OriginalRecordIds == null || OriginalRecordIds.trim().length() == 0){
					throw new Exception("原始记录未选中！");
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
							if(o.getWorkType()!=null && !o.getWorkType().equals("检验")){
								if(o.getWorkType().equals("检定") && o.getConclusion()!=null&& o.getConclusion().contains("不")){
									continue;
								}
								JSONObject obj = new JSONObject();
								obj.put("ApplianceName", (o.getApplianceName()==null||o.getApplianceName().trim().length()==0)?o.getTargetAppliance().getApplianceStandardName().getName():o.getApplianceName());	//常用名称
								obj.put("Model", o.getModel());			
								obj.put("ApplianceCode", o.getApplianceCode());
								obj.put("WorkDate", o.getWorkDate()==null?"":DateTimeFormatUtil.DateFormat.format(o.getWorkDate()));
								obj.put("WorkStaff", o.getSysUserByStaffId()==null?"":o.getSysUserByStaffId().getName());//检/校人员
								obj.put("WorkType", o.getWorkType());//工作性质 检定\校准\检测\检验
								obj.put("Validity", o.getValidity()==null?"":DateTimeFormatUtil.DateFormat.format(o.getValidity()));//有效期
								obj.put("CustomerName", o.getCommissionSheet().getCustomerName());	//委托单位
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
					retJSON20.put("msg", String.format("查询委托单原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in OriginalRecordServlet-->case 20", e);
				}else{
					log.error("error in OriginalRecordServlet-->case 20", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON20.toString());
			}
			break;
		case 21://查询一个委托单的历史原始记录
			JSONObject retJSON21 = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionSheetId");	//委托单Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("委托单未指定！");
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
						jsonObj.put("CertificateCode", o.getCertificateCode()==null?"":o.getCertificateCode());	//证书编号
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
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
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
