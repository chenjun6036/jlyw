package com.jlyw.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jlyw.hibernate.CertificateFeeAssign;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SysStringUtil;
import com.jlyw.util.SystemCfgUtil;

public class CertificateFeeAssignServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(CertificateFeeAssignServlet.class);


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		CertificateFeeAssignManager feeAssignMgr = new CertificateFeeAssignManager();
		switch (method) {
		case 0: // 查询一个原始记录的某一份证书的费用分配信息
			JSONObject retJSON = new JSONObject();
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String CertificateId = req.getParameter("CertificateId");
				if(OriginalRecordId == null || OriginalRecordId.length() == 0 ||
						CertificateId == null || CertificateId.length() == 0){
					throw new Exception("参数不完整");
				}
				
				List<CertificateFeeAssign> feeAssignList = feeAssignMgr.findByVarProperty(
						new KeyValueWithOperator("originalRecord.id", Integer.parseInt(OriginalRecordId), "="),
						new KeyValueWithOperator("certificate.id", Integer.parseInt(CertificateId), "="));
				JSONArray jsonArray = new JSONArray();
				for(CertificateFeeAssign feeAssign : feeAssignList){
					JSONObject tempObj = new JSONObject();
					tempObj.put("CertificateFeeAssignId", feeAssign.getId());
					tempObj.put("CommissionSheetId", feeAssign.getCommissionSheet().getId());
					tempObj.put("OriginalRecordId", feeAssign.getOriginalRecord().getId());
					tempObj.put("CertificateId", feeAssign.getCertificate().getId());
					tempObj.put("FeeAlloteeId", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getId());
					tempObj.put("FeeAlloteeName", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getName());
					tempObj.put("TestFee", feeAssign.getTestFee()==null?"":feeAssign.getTestFee());
					tempObj.put("RepairFee", feeAssign.getRepairFee()==null?"":feeAssign.getRepairFee());
					tempObj.put("MaterialFee", feeAssign.getMaterialFee()==null?"":feeAssign.getMaterialFee());
					tempObj.put("CarFee", feeAssign.getCarFee()==null?"":feeAssign.getCarFee());
					tempObj.put("DebugFee", feeAssign.getDebugFee()==null?"":feeAssign.getDebugFee());
					tempObj.put("OtherFee", feeAssign.getOtherFee()==null?"":feeAssign.getOtherFee());
					tempObj.put("TotalFee", feeAssign.getTotalFee()==null?"":feeAssign.getTotalFee());
					tempObj.put("LastEditorId", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getId());
					tempObj.put("LastEditorName", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getName());
					tempObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(feeAssign.getLastEditTime()));
					
					tempObj.put("Remark", feeAssign.getRemark()==null?"":feeAssign.getRemark());
					jsonArray.put(tempObj);
				}
				
				retJSON.put("total", feeAssignList.size());
				retJSON.put("rows", jsonArray);
			}catch (Exception e){
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 0", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 0", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//为一个原始记录的一份证书增加一个费用分配
			JSONObject retJSON1 = new JSONObject();
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String CertificateId = req.getParameter("CertificateId");
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//分配的产值信息
				if(OriginalRecordId == null || OriginalRecordId.length() == 0 ||
						CertificateId == null || CertificateId.length() == 0 ||
						FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("参数不完整");
				}
				
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				CertificateManager cMgr = new CertificateManager();
				Certificate certificate = cMgr.findById(Integer.parseInt(CertificateId));
				if(certificate == null){
					throw new Exception("找不到指定的证书！");
				}
				if(!FlagUtil.CertificateFlag.isCertificateOfficial(certificate)){
					throw new Exception("该证书尚未完成，不能分配产值！");
				}
				if(!certificate.getId().equals(oRecord.getCertificate()==null?null:oRecord.getCertificate().getId())){
					throw new Exception("指定的证书不是最新版本，不能为其分配产值！");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(oRecord.getCommissionSheet().getStatus())){
					throw new Exception("该委托单已完工或者已注销，不能再次分配产值！");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null || !loginUser.getId().equals(oRecord.getSysUserByStaffId().getId())){
					throw new Exception("您不是该证书的检定/校准人员，不能对其分配费用！");
				}
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//费用分配信息
				
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				List<CertificateFeeAssign> feeAssignList = new ArrayList<CertificateFeeAssign>();
				UserManager userMgr = new UserManager();
				double testFeeTotal = 0.0, repairFeeTotal = 0.0, materialFeeTotal = 0.0, carFeeTotal = 0.0, debugFeeTotal = 0.0, otherFeeTotal = 0.0;
				for(int i = 0; i < feeAssignArray.length(); i++){
					JSONObject jsonObj = feeAssignArray.getJSONObject(i);
					CertificateFeeAssign feeAssign = new CertificateFeeAssign();
					String alloteeName = jsonObj.has("FeeAlloteeName")?jsonObj.getString("FeeAlloteeName"):"";
					String testFeeStr = jsonObj.has("TestFee")?jsonObj.getString("TestFee").trim():"";
					String repairFeeStr = jsonObj.has("RepairFee")?jsonObj.getString("RepairFee").trim():"";
					String materialFeeStr = jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee").trim():"";
					String carFeeStr = jsonObj.has("CarFee")?jsonObj.getString("CarFee").trim():"";
					String debugFeeStr = jsonObj.has("DebugFee")?jsonObj.getString("DebugFee").trim():"";
					String otherFeeStr = jsonObj.has("OtherFee")?jsonObj.getString("OtherFee").trim():"";
					
					
					double testFee = 0.0, repairFee = 0.0, materialFee = 0.0, carFee = 0.0, debugFee = 0.0, otherFee = 0.0, totalFee = 0.0;
					if(testFeeStr.length() > 0){
						testFee = Double.parseDouble(testFeeStr);
						totalFee += testFee;
						testFeeTotal += testFee;
						if(testFee < 0){
							throw new Exception(String.format("%s 所分配的检定费不能小于0！", alloteeName));
						}
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						repairFeeTotal += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s 所分配的修理费不能小于0！", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						materialFeeTotal += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s 所分配的配件费不能小于0！", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						carFeeTotal += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s 所分配的交通费不能小于0！", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						debugFeeTotal += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s 所分配的调试费不能小于0！", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						otherFeeTotal += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s 所分配的其它费不能小于0！", alloteeName));
						}
					}
					List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
					if(alloteeList.size() == 0){
						throw new Exception(String.format("找不到姓名为'%s'的人员！", alloteeName));
					}else if(alloteeList.size() > 1){
						throw new Exception(String.format("当前系统中名为'%s'的人员有%s个，无法唯一确定分配产值人员！", alloteeName, alloteeList.size()));
					}
					if(totalFee <= 0){
						throw new Exception(String.format("%s 所分配的产值不能为0，如果该人员没有产值，请删除该条目！", alloteeName));
					}
					
					feeAssign.setSysUserByFeeAlloteeId(alloteeList.get(0));
					feeAssign.setTestFee(testFee);
					feeAssign.setTestFeeOld(testFee);
					feeAssign.setRepairFee(repairFee);
					feeAssign.setRepairFeeOld(repairFee);
					feeAssign.setMaterialFee(materialFee);
					feeAssign.setMaterialFeeOld(materialFee);
					feeAssign.setCarFee(carFee);
					feeAssign.setCarFeeOld(carFee);
					feeAssign.setDebugFee(debugFee);
					feeAssign.setDebugFeeOld(debugFee);
					feeAssign.setOtherFee(otherFee);
					feeAssign.setOtherFeeOld(otherFee);
					feeAssign.setTotalFee(totalFee);
					feeAssign.setTotalFeeOld(totalFee);
					
					//其它信息
					feeAssign.setCertificate(certificate);
					feeAssign.setOriginalRecord(oRecord);
					feeAssign.setCommissionSheet(oRecord.getCommissionSheet());
					feeAssign.setSysUserByLastEditorId(loginUser);
					feeAssign.setLastEditTime(nowTime);
					
					feeAssignList.add(feeAssign);
				}
				if(feeAssignList.size() > 0){	//若feeAssignList.size()==0，则说明是删除原来的分配记录
					if((oRecord.getTestFee()==null?0:oRecord.getTestFee()) != testFeeTotal){
						throw new Exception(String.format("所分配的检定费总额'%s'与原始记录的检定费用'%s'不一致！", testFeeTotal, oRecord.getTestFee()==null?0:oRecord.getTestFee()));
					}
					if((oRecord.getRepairFee()==null?0:oRecord.getRepairFee()) != repairFeeTotal){
						throw new Exception(String.format("所分配的修理费总额'%s'与原始记录的修理费用'%s'不一致！", repairFeeTotal, oRecord.getRepairFee()==null?0:oRecord.getRepairFee()));
					}
					if((oRecord.getMaterialFee()==null?0:oRecord.getMaterialFee()) != materialFeeTotal){
						throw new Exception(String.format("所分配的材料费总额'%s'与原始记录的材料费用'%s'不一致！", materialFeeTotal, oRecord.getMaterialFee()==null?0:oRecord.getMaterialFee()));
					}
					if((oRecord.getCarFee()==null?0:oRecord.getCarFee()) != carFeeTotal){
						throw new Exception(String.format("所分配的交通费总额'%s'与原始记录的交通费用'%s'不一致！", carFeeTotal, oRecord.getCarFee()==null?0:oRecord.getCarFee()));
					}
					if((oRecord.getDebugFee()==null?0:oRecord.getDebugFee()) != debugFeeTotal){
						throw new Exception(String.format("所分配的调试费总额'%s'与原始记录的调试费用'%s'不一致！", debugFeeTotal, oRecord.getDebugFee()==null?0:oRecord.getDebugFee()));
					}
					if((oRecord.getOtherFee()==null?0:oRecord.getOtherFee()) != otherFeeTotal){
						throw new Exception(String.format("所分配的其它费总额'%s'与原始记录的其它费用'%s'不一致！", otherFeeTotal, oRecord.getOtherFee()==null?0:oRecord.getOtherFee()));
					}
				}
				feeAssignMgr.executeFeeAssign(oRecord, certificate, feeAssignList);
				retJSON1.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("产值分配失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 1", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 1", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2: // 查询一个委托单下的所有费用信息，包括检定证书的费用 + 其他费（如转包费、技术服务费等）
			JSONObject retJSON2 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("参数不完整");
				}
				double TotalFee = 0.0;
				double TestFee = 0.0;
				double RepairFee = 0.0;
				double MaterialFee = 0.0;
				double DebugFee = 0.0;
				double CarFee = 0.0;
				double OtherFee = 0.0;
				List<CertificateFeeAssign> feeAssignList = feeAssignMgr.findByHQL(CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId, Integer.parseInt(CommissionSheetId));
				JSONArray jsonArray = new JSONArray();
				for(CertificateFeeAssign feeAssign : feeAssignList){
					JSONObject tempObj = new JSONObject();
					tempObj.put("CertificateFeeAssignId", feeAssign.getId());
					tempObj.put("CommissionSheetId", feeAssign.getCommissionSheet().getId());
					tempObj.put("CustomerName", feeAssign.getCommissionSheet().getCustomerName());
					tempObj.put("OriginalRecordId", feeAssign.getOriginalRecord()==null?"":feeAssign.getOriginalRecord().getId());
					tempObj.put("CertificateId", feeAssign.getCertificate()==null?"":feeAssign.getCertificate().getId());
					tempObj.put("CertificateCode", feeAssign.getCertificate()==null?"":feeAssign.getCertificate().getCertificateCode());
					tempObj.put("Quantity", feeAssign.getOriginalRecord()==null?"":feeAssign.getOriginalRecord().getQuantity());
					tempObj.put("FeeAlloteeId", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getId());
					tempObj.put("FeeAlloteeName", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getName());
					tempObj.put("TestFee", feeAssign.getTestFee()==null?"":feeAssign.getTestFee());
					tempObj.put("RepairFee", feeAssign.getRepairFee()==null?"":feeAssign.getRepairFee());
					tempObj.put("MaterialFee", feeAssign.getMaterialFee()==null?"":feeAssign.getMaterialFee());
					tempObj.put("CarFee", feeAssign.getCarFee()==null?"":feeAssign.getCarFee());
					tempObj.put("DebugFee", feeAssign.getDebugFee()==null?"":feeAssign.getDebugFee());
					tempObj.put("OtherFee", feeAssign.getOtherFee()==null?"":feeAssign.getOtherFee());
					tempObj.put("TotalFee", feeAssign.getTotalFee()==null?"":feeAssign.getTotalFee());
					tempObj.put("LastEditorId", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getId());
					tempObj.put("LastEditorName", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getName());
					tempObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(feeAssign.getLastEditTime()));
					
					tempObj.put("Remark", feeAssign.getRemark()==null?"":feeAssign.getRemark());
					jsonArray.put(tempObj);
					
					TotalFee += feeAssign.getTotalFee()==null?0:feeAssign.getTotalFee();
					TestFee += feeAssign.getTotalFee()==null?0:feeAssign.getTestFee();
					RepairFee += feeAssign.getTotalFee()==null?0:feeAssign.getRepairFee();
					MaterialFee += feeAssign.getTotalFee()==null?0:feeAssign.getMaterialFee();
					CarFee += feeAssign.getTotalFee()==null?0:feeAssign.getCarFee();
					DebugFee += feeAssign.getTotalFee()==null?0:feeAssign.getDebugFee();
					OtherFee += feeAssign.getTotalFee()==null?0:feeAssign.getOtherFee();
				}
				JSONArray footerArray = new JSONArray();
				JSONObject footer = new JSONObject();
				footer.put("CertificateCode", "合计");
				footer.put("TotalFee", TotalFee);
				footer.put("TestFee", TestFee);
				footer.put("RepairFee", RepairFee);
				footer.put("MaterialFee", MaterialFee);
				footer.put("CarFee", CarFee);
				footer.put("DebugFee", DebugFee);
				footer.put("OtherFee", OtherFee);
				footerArray.put(footer);
				
				retJSON2.put("total", feeAssignList.size());
				retJSON2.put("rows", jsonArray);
				retJSON2.put("footer", footerArray);
			}catch (Exception e){
				try {
					retJSON2.put("total", 0);
					retJSON2.put("rows", new JSONArray());
					retJSON2.put("footer", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 2", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 2", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3:	//为一个证书增加一个其他的费用（如转包费、技术服务费，不是证书费用）
			JSONObject retJSON3 = new JSONObject();
			try{
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//分配的产值信息
				if(CommissionSheetId == null || CommissionSheetId.length() == 0 ||
						FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("参数不完整");
				}
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CommissionSheet cSheet = cSheetMgr.findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("找不到指定的委托单！");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("该委托单已完工或者已注销，不能再次分配费用！");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能对委托单分配费用！");
				}
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//费用分配信息
				
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				List<CertificateFeeAssign> feeAssignList = new ArrayList<CertificateFeeAssign>();
				UserManager userMgr = new UserManager();
				for(int i = 0; i < feeAssignArray.length(); i++){
					JSONObject jsonObj = feeAssignArray.getJSONObject(i);
					CertificateFeeAssign feeAssign = new CertificateFeeAssign();
					String alloteeName = jsonObj.has("FeeAlloteeName")?jsonObj.getString("FeeAlloteeName"):"";
					String testFeeStr = jsonObj.has("TestFee")?jsonObj.getString("TestFee").trim():"";
					String repairFeeStr = jsonObj.has("RepairFee")?jsonObj.getString("RepairFee").trim():"";
					String materialFeeStr = jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee").trim():"";
					String carFeeStr = jsonObj.has("CarFee")?jsonObj.getString("CarFee").trim():"";
					String debugFeeStr = jsonObj.has("DebugFee")?jsonObj.getString("DebugFee").trim():"";
					String otherFeeStr = jsonObj.has("OtherFee")?jsonObj.getString("OtherFee").trim():"";
					String remarkStr = jsonObj.has("Remark")?jsonObj.getString("Remark").trim():null;
					
					double testFee = 0.0, repairFee = 0.0, materialFee = 0.0, carFee = 0.0, debugFee = 0.0, otherFee = 0.0, totalFee = 0.0;
					if(testFeeStr.length() > 0){
						testFee = Double.parseDouble(testFeeStr);
						totalFee += testFee;
						if(testFee < 0){
							throw new Exception(String.format("%s 所分配的检定费不能小于0！", alloteeName));
						}
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s 所分配的修理费不能小于0！", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s 所分配的配件费不能小于0！", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s 所分配的交通费不能小于0！", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s 所分配的调试费不能小于0！", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s 所分配的其它费不能小于0！", alloteeName));
						}
					}
					SysUser allotee = null;
					if(alloteeName != null && alloteeName.length() > 0){
						List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
						if(alloteeList.size() == 0){
							throw new Exception(String.format("找不到姓名为'%s'的人员！", alloteeName));
						}else if(alloteeList.size() > 1){
							throw new Exception(String.format("当前系统中名为'%s'的人员有%s个，无法唯一确定分配产值人员！", alloteeName, alloteeList.size()));
						}
						allotee = alloteeList.get(0);
					}
					
					if(totalFee <= 0){
						throw new Exception(String.format("%s 所分配的费用不能为0，如果该人员没有产值，请删除该条目！", alloteeName));
					}
					
					feeAssign.setSysUserByFeeAlloteeId(allotee);
					feeAssign.setTestFee(testFee);
					feeAssign.setTestFeeOld(testFee);
					feeAssign.setRepairFee(repairFee);
					feeAssign.setRepairFeeOld(repairFee);
					feeAssign.setMaterialFee(materialFee);
					feeAssign.setMaterialFeeOld(materialFee);
					feeAssign.setCarFee(carFee);
					feeAssign.setCarFeeOld(carFee);
					feeAssign.setDebugFee(debugFee);
					feeAssign.setDebugFeeOld(debugFee);
					feeAssign.setOtherFee(otherFee);
					feeAssign.setOtherFeeOld(otherFee);
					feeAssign.setTotalFee(totalFee);
					feeAssign.setTotalFeeOld(totalFee);
					feeAssign.setRemark(remarkStr);
					
					//其它信息
					feeAssign.setCertificate(null);
					feeAssign.setOriginalRecord(null);
					feeAssign.setCommissionSheet(cSheet);
					feeAssign.setSysUserByLastEditorId(loginUser);
					feeAssign.setLastEditTime(nowTime);
					
					feeAssignList.add(feeAssign);
				}
				feeAssignMgr.executeFeeAssignWithoutCertificate(cSheet, feeAssignList);
				retJSON3.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("委托单费用添加失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 3", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 3", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4: // 查询一个委托单下的特殊费用信息（如转包费、技术服务费；不包含检定证书的费用）
			JSONObject retJSON4 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("参数不完整");
				}
				
				String queryString = "from CertificateFeeAssign as model " +
						" where model.commissionSheet.id=? and " +
						" (model.originalRecord is null and model.certificate is null) ";
				List<CertificateFeeAssign> feeAssignList = feeAssignMgr.findByHQL(queryString, Integer.parseInt(CommissionSheetId));
				JSONArray jsonArray = new JSONArray();
				for(CertificateFeeAssign feeAssign : feeAssignList){
					JSONObject tempObj = new JSONObject();
					tempObj.put("CertificateFeeAssignId", feeAssign.getId());
					tempObj.put("CommissionSheetId", feeAssign.getCommissionSheet().getId());
					tempObj.put("OriginalRecordId", feeAssign.getOriginalRecord()==null?"":feeAssign.getOriginalRecord().getId());
					tempObj.put("CertificateId", feeAssign.getCertificate()==null?"":feeAssign.getCertificate().getId());
					tempObj.put("FeeAlloteeId", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getId());
					tempObj.put("FeeAlloteeName", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getName());
					tempObj.put("TestFee", feeAssign.getTestFee()==null?"":feeAssign.getTestFee());
					tempObj.put("RepairFee", feeAssign.getRepairFee()==null?"":feeAssign.getRepairFee());
					tempObj.put("MaterialFee", feeAssign.getMaterialFee()==null?"":feeAssign.getMaterialFee());
					tempObj.put("CarFee", feeAssign.getCarFee()==null?"":feeAssign.getCarFee());
					tempObj.put("DebugFee", feeAssign.getDebugFee()==null?"":feeAssign.getDebugFee());
					tempObj.put("OtherFee", feeAssign.getOtherFee()==null?"":feeAssign.getOtherFee());
					tempObj.put("TotalFee", feeAssign.getTotalFee()==null?"":feeAssign.getTotalFee());
					tempObj.put("LastEditorId", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getId());
					tempObj.put("LastEditorName", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getName());
					tempObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(feeAssign.getLastEditTime()));
					
					tempObj.put("Remark", feeAssign.getRemark()==null?"":feeAssign.getRemark());
					jsonArray.put(tempObj);
				}
				
				retJSON4.put("total", feeAssignList.size());
				retJSON4.put("rows", jsonArray);
			}catch (Exception e){
				try {
					retJSON4.put("total", 0);
					retJSON4.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 4", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 4", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	//证书费用修改
			JSONObject retJSON5 = new JSONObject();
			try{
				
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//分配的产值信息
				if(FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("参数不完整");
				}
				
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能对委托单分配费用！");
				}
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//费用分配信息
				
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				List<CertificateFeeAssign> feeAssignList = new ArrayList<CertificateFeeAssign>();
				CertificateFeeAssignManager cerfeeMgr=new CertificateFeeAssignManager();
				UserManager userMgr = new UserManager();
				for(int i = 0; i < feeAssignArray.length(); i++){
					JSONObject jsonObj = feeAssignArray.getJSONObject(i);
					CertificateFeeAssign feeAssign = cerfeeMgr.findById(Integer.parseInt(jsonObj.getString("CertificateFeeAssignId")));
					if(feeAssign.getCommissionSheet().getCheckOutDate() != null){
						throw new Exception("此委托单已经结账，不能修改费用！");
					}
					String alloteeName = jsonObj.has("FeeAlloteeName")?jsonObj.getString("FeeAlloteeName"):"";
					String testFeeStr = jsonObj.has("TestFee")?jsonObj.getString("TestFee").trim():"0";
					String repairFeeStr = jsonObj.has("RepairFee")?jsonObj.getString("RepairFee").trim():"0";
					String materialFeeStr = jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee").trim():"0";
					String carFeeStr = jsonObj.has("CarFee")?jsonObj.getString("CarFee").trim():"0";
					String debugFeeStr = jsonObj.has("DebugFee")?jsonObj.getString("DebugFee").trim():"0";
					String otherFeeStr = jsonObj.has("OtherFee")?jsonObj.getString("OtherFee").trim():"0";
					String remarkStr = jsonObj.has("Remark")?jsonObj.getString("Remark").trim():"";
					if(remarkStr.length() > 50){
						remarkStr = "";
					}
					
					double testFee = 0.0, repairFee = 0.0, materialFee = 0.0, carFee = 0.0, debugFee = 0.0, otherFee = 0.0, totalFee = 0.0;
					if(testFeeStr.length() > 0){
						testFee = Double.parseDouble(testFeeStr);
						totalFee += testFee;
						if(testFee < 0){
							throw new Exception(String.format("%s 所分配的检定费不能小于0！", alloteeName));
						}
						
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s 所分配的修理费不能小于0！", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s 所分配的配件费不能小于0！", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s 所分配的交通费不能小于0！", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s 所分配的调试费不能小于0！", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s 所分配的其它费不能小于0！", alloteeName));
						}
					}
					SysUser allotee = null;
					if(alloteeName != null && alloteeName.length() > 0){
						List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
						if(alloteeList.size() == 0){
							throw new Exception(String.format("找不到姓名为'%s'的人员！", alloteeName));
						}else if(alloteeList.size() > 1){
							throw new Exception(String.format("当前系统中名为'%s'的人员有%s个，无法唯一确定分配产值人员！", alloteeName, alloteeList.size()));
						}
						allotee = alloteeList.get(0);
					}
					
//					if(totalFee <= 0){
//						throw new Exception(String.format("%s 所分配的费用不能为0，如果该人员没有产值，请删除该条目！", alloteeName));
//					}
					
					feeAssign.setSysUserByFeeAlloteeId(allotee);
					feeAssign.setTestFee(testFee);					
					feeAssign.setRepairFee(repairFee);					
					feeAssign.setMaterialFee(materialFee);					
					feeAssign.setCarFee(carFee);				
					feeAssign.setDebugFee(debugFee);				
					feeAssign.setOtherFee(otherFee);				
					feeAssign.setTotalFee(totalFee);
					
					feeAssign.setTestFeeOld(testFee);					
					feeAssign.setRepairFeeOld(repairFee);					
					feeAssign.setMaterialFeeOld(materialFee);					
					feeAssign.setCarFeeOld(carFee);				
					feeAssign.setDebugFeeOld(debugFee);				
					feeAssign.setOtherFeeOld(otherFee);				
					feeAssign.setTotalFeeOld(totalFee);
	
					feeAssign.setRemark(remarkStr + String.format("[%s于%s执行改费(检定费%s->%s,修理费%s->%s,材料费%s->%s,交通费%s->%s,调试费%s->%s,其他费%s->%s,总费用%s->%s)]", 
							loginUser.getName(),
							DateTimeFormatUtil.DateTimeFormat.format(nowTime),
							jsonObj.has("OldTestFee")?jsonObj.getString("OldTestFee").trim():"0", testFee,
							jsonObj.has("OldRepairFee")?jsonObj.getString("OldRepairFee").trim():"0", repairFee,
							jsonObj.has("OldMaterialFee")?jsonObj.getString("OldMaterialFee").trim():"0", materialFee,
							jsonObj.has("OldCarFee")?jsonObj.getString("OldCarFee").trim():"0", carFee,
							jsonObj.has("OldDebugFee")?jsonObj.getString("OldDebugFee").trim():"0", debugFee,											
							jsonObj.has("OldOtherFee")?jsonObj.getString("OldOtherFee").trim():"0", otherFee,
							jsonObj.has("OldOtherFee")?jsonObj.getString("OldTotalFee").trim():"0", totalFee
					));
					
					//其它信息										
					feeAssign.setSysUserByLastEditorId(loginUser);
					feeAssign.setLastEditTime(nowTime);
					
					feeAssignList.add(feeAssign);
				}
				feeAssignMgr.updateFeeAssign(feeAssignList);
				retJSON5.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON5.put("IsOK", false);
					retJSON5.put("msg", String.format("证书费用修改失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 5", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 5", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6: // 查询一个委托单下的所有费用信息，包括检定证书的费用 + 其他费（如转包费、技术服务费等）
			JSONObject retJSON6 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("参数不完整");
				}
				
				List<CertificateFeeAssign> feeAssignList = feeAssignMgr.findByHQL(CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId, Integer.parseInt(CommissionSheetId));
				JSONArray jsonArray = new JSONArray();
				for(CertificateFeeAssign feeAssign : feeAssignList){
					JSONObject tempObj = new JSONObject();
					tempObj.put("CertificateFeeAssignId", feeAssign.getId());
					tempObj.put("CommissionSheetId", feeAssign.getCommissionSheet().getId());
					tempObj.put("CustomerName", feeAssign.getCommissionSheet().getCustomerName());
					tempObj.put("OriginalRecordId", feeAssign.getOriginalRecord()==null?"":feeAssign.getOriginalRecord().getId());
					tempObj.put("ReportType", feeAssign.getOriginalRecord()==null?0:feeAssign.getOriginalRecord().getWorkType());
					
					tempObj.put("ApplianceName", feeAssign.getOriginalRecord()==null?"":(feeAssign.getOriginalRecord().getApplianceName()==null?"":feeAssign.getOriginalRecord().getApplianceName()));
					tempObj.put("CertificateId", feeAssign.getCertificate()==null?"":feeAssign.getCertificate().getId());
					tempObj.put("CertificateCode", feeAssign.getCertificate()==null?"":(feeAssign.getCertificate().getCertificateCode()==null?"":feeAssign.getCertificate().getCertificateCode()));
					tempObj.put("Quantity", feeAssign.getOriginalRecord()==null?"":(feeAssign.getOriginalRecord().getQuantity()==null?"":feeAssign.getOriginalRecord().getQuantity()));
					tempObj.put("FeeAlloteeId", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getId());
					tempObj.put("FeeAlloteeName", feeAssign.getSysUserByFeeAlloteeId()==null?"":feeAssign.getSysUserByFeeAlloteeId().getName());
					tempObj.put("TestFee", feeAssign.getTestFee()==null?"":feeAssign.getTestFee());
					tempObj.put("RepairFee", feeAssign.getRepairFee()==null?"":feeAssign.getRepairFee());
					tempObj.put("MaterialFee", feeAssign.getMaterialFee()==null?"":feeAssign.getMaterialFee());
					tempObj.put("CarFee", feeAssign.getCarFee()==null?"":feeAssign.getCarFee());
					tempObj.put("DebugFee", feeAssign.getDebugFee()==null?"":feeAssign.getDebugFee());
					tempObj.put("OtherFee", feeAssign.getOtherFee()==null?"":feeAssign.getOtherFee());
					tempObj.put("TotalFee", feeAssign.getTotalFee()==null?"":feeAssign.getTotalFee());
					
					tempObj.put("OldTestFee", feeAssign.getTestFeeOld()==null?"":feeAssign.getTestFeeOld());
					tempObj.put("OldRepairFee", feeAssign.getRepairFeeOld()==null?"":feeAssign.getRepairFeeOld());
					tempObj.put("OldMaterialFee", feeAssign.getMaterialFeeOld()==null?"":feeAssign.getMaterialFeeOld());
					tempObj.put("OldCarFee", feeAssign.getCarFeeOld()==null?"":feeAssign.getCarFeeOld());
					tempObj.put("OldDebugFee", feeAssign.getDebugFeeOld()==null?"":feeAssign.getDebugFeeOld());
					tempObj.put("OldOtherFee", feeAssign.getOtherFeeOld()==null?"":feeAssign.getOtherFeeOld());
					tempObj.put("OldTotalFee", feeAssign.getTotalFeeOld()==null?"":feeAssign.getTotalFeeOld());
					
					tempObj.put("LastEditorId", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getId());
					tempObj.put("LastEditorName", feeAssign.getSysUserByLastEditorId()==null?"":feeAssign.getSysUserByLastEditorId().getName());
					tempObj.put("LastEditTime", DateTimeFormatUtil.DateTimeFormat.format(feeAssign.getLastEditTime()));
					
					tempObj.put("Remark", feeAssign.getRemark()==null?"":feeAssign.getRemark());
					jsonArray.put(tempObj);
				}
				
				retJSON6.put("total", feeAssignList.size());
				retJSON6.put("rows", jsonArray);
				
			}catch (Exception e){
				try {
					retJSON6.put("total", 0);
					retJSON6.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 6", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7:		//为一个委托单分配产值：将当前登录用户检验的、尚未分配证书费用的所有证书的费用进行分配
			JSONObject retJSON7 = new JSONObject();
			try{
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//分配的产值信息
				if(CommissionSheetId == null || CommissionSheetId.length() == 0 ||
						FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("参数不完整");
				}
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CommissionSheet cSheet = cSheetMgr.findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("找不到指定的委托单！");
				}
				
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("该委托单已完工或者已注销，不能再次分配产值！");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能对其分配费用！");
				}
				
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//费用分配信息
				if(feeAssignArray.length() == 0){
					throw new Exception("未输入费用分配信息！");
				}
				
				//查询当前用户检验的，且尚未分配费用的证书记录
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				String queryString  = "from OriginalRecord as model where model.status<>1 and model.taskAssign.status<>1 and model.sysUserByStaffId.id = ? and " +
						" model.commissionSheet = ? and model.certificate.pdf is not null and " +	//已生成正式证书的原始记录
						" model not in ( " +	//未分配过费用的证书
						"     select f.originalRecord from CertificateFeeAssign as f where f.originalRecord is not null and f.certificate is not null and f.originalRecord=model and f.certificate=model.certificate " +
						" ) ";
				List<OriginalRecord> oRecordList = oRecordMgr.findByHQL(queryString, loginUser.getId(), cSheet);
				
				if(oRecordList.size() == 0){
					throw new Exception(String.format("该委托单目前没有由 '%s'检验且未分配费用的证书记录！", loginUser.getName()));
				}
				
				Map<Integer, CertificateFeeAssign> maxTestFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//存放每个证书记录所分配的产值的最大值——检测费,索引号与oRecordList的索引号一一对应
				Map<Integer, CertificateFeeAssign> maxRepairFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//存放每个证书记录所分配的产值的最大值——修理费
				Map<Integer, CertificateFeeAssign> maxMaterialFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//存放每个证书记录所分配的产值的最大值——配件费
				Map<Integer, CertificateFeeAssign> maxCarFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//存放每个证书记录所分配的产值的最大值——交通费
				Map<Integer, CertificateFeeAssign> maxDebugFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//存放每个证书记录所分配的产值的最大值——调试费
				Map<Integer, CertificateFeeAssign> maxOtherFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//存放每个证书记录所分配的产值的最大值——其它费
				Map<Integer, Double> totalTestFeeMap = new HashMap<Integer, Double>();	//存放每个证书记录已分配的产值总和——检测费,索引号与oRecordList的索引号一一对应
				Map<Integer, Double> totalRepairFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalMaterialFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalCarFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalDebugFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalOtherFeeMap = new HashMap<Integer, Double>();
								
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				List<CertificateFeeAssign> feeAssignList = new ArrayList<CertificateFeeAssign>();
				UserManager userMgr = new UserManager();
				double testFeeTotal = 0.0, repairFeeTotal = 0.0, materialFeeTotal = 0.0, carFeeTotal = 0.0, debugFeeTotal = 0.0, otherFeeTotal = 0.0;	//各种费用额度的总和
				for(int i = 0; i < feeAssignArray.length(); i++){
					JSONObject jsonObj = feeAssignArray.getJSONObject(i);
					String alloteeName = jsonObj.has("FeeAlloteeName")?jsonObj.getString("FeeAlloteeName"):"";
					String testFeeStr = jsonObj.has("TestFee")?jsonObj.getString("TestFee").trim():"";
					String repairFeeStr = jsonObj.has("RepairFee")?jsonObj.getString("RepairFee").trim():"";
					String materialFeeStr = jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee").trim():"";
					String carFeeStr = jsonObj.has("CarFee")?jsonObj.getString("CarFee").trim():"";
					String debugFeeStr = jsonObj.has("DebugFee")?jsonObj.getString("DebugFee").trim():"";
					String otherFeeStr = jsonObj.has("OtherFee")?jsonObj.getString("OtherFee").trim():"";
					
					
					double testFee = 0.0, repairFee = 0.0, materialFee = 0.0, carFee = 0.0, debugFee = 0.0, otherFee = 0.0, totalFee = 0.0;	 //各费用的折扣额度（totalFee除外）
					if(testFeeStr.length() > 0){
						testFee = Double.parseDouble(testFeeStr);
						totalFee += testFee;
						testFeeTotal += testFee;
						if(testFee < 0){
							throw new Exception(String.format("%s 所分配的检定费额度不能小于0！", alloteeName));
						}else if(testFee > 1){
							throw new Exception(String.format("%s 所分配的检定费额度不能大于1！", alloteeName));
						}
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						repairFeeTotal += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s 所分配的修理费额度不能小于0！", alloteeName));
						}else if(repairFee > 1){
							throw new Exception(String.format("%s 所分配的修理费额度不能大于1！", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						materialFeeTotal += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s 所分配的配件费额度不能小于0！", alloteeName));
						}else if(materialFee > 1){
							throw new Exception(String.format("%s 所分配的配件费额度不能大于1！", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						carFeeTotal += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s 所分配的交通费额度不能小于0！", alloteeName));
						}else if(carFee > 1){
							throw new Exception(String.format("%s 所分配的交通费额度不能大于1！", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						debugFeeTotal += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s 所分配的调试费额度不能小于0！", alloteeName));
						}else if(debugFee > 1){
							throw new Exception(String.format("%s 所分配的调试费额度不能大于1！", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						otherFeeTotal += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s 所分配的其它费额度不能小于0！", alloteeName));
						}else if(otherFee > 1){
							throw new Exception(String.format("%s 所分配的其它费额度不能大于1！", alloteeName));
						}
					}
					List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
					if(alloteeList.size() == 0){
						throw new Exception(String.format("找不到姓名为'%s'的人员！", alloteeName));
					}else if(alloteeList.size() > 1){
						throw new Exception(String.format("当前系统中名为'%s'的人员有%s个，无法唯一确定分配产值人员！", alloteeName, alloteeList.size()));
					}
					if(totalFee <= 0){
						throw new Exception(String.format("%s 所分配的产值不能为0，如果该人员没有产值，请删除该条目！", alloteeName));
					}
					
					for(int j = 0; j < oRecordList.size(); j++){
						OriginalRecord oRecord = oRecordList.get(j);
						
						CertificateFeeAssign feeAssign = new CertificateFeeAssign();
						feeAssign.setSysUserByFeeAlloteeId(alloteeList.get(0));
						feeAssign.setTestFee(new Double(Math.round(testFee * (oRecord.getTestFee()==null?0:oRecord.getTestFee()))));
						feeAssign.setTestFeeOld(feeAssign.getTestFee());
						feeAssign.setRepairFee(new Double(Math.round(repairFee * (oRecord.getRepairFee()==null?0:oRecord.getRepairFee()))));
						feeAssign.setRepairFeeOld(feeAssign.getRepairFee());
						feeAssign.setMaterialFee(new Double(Math.round(materialFee * (oRecord.getMaterialFee()==null?0:oRecord.getMaterialFee()))));
						feeAssign.setMaterialFeeOld(feeAssign.getMaterialFee());
						feeAssign.setCarFee(new Double(Math.round(carFee * (oRecord.getCarFee()==null?0:oRecord.getCarFee()))));
						feeAssign.setCarFeeOld(feeAssign.getCarFee());
						feeAssign.setDebugFee(new Double(Math.round(debugFee * (oRecord.getDebugFee()==null?0:oRecord.getDebugFee()))));
						feeAssign.setDebugFeeOld(feeAssign.getDebugFee());
						feeAssign.setOtherFee(new Double(Math.round(otherFee * (oRecord.getOtherFee()==null?0:oRecord.getOtherFee()))));
						feeAssign.setOtherFeeOld(feeAssign.getOtherFee());
						
						feeAssign.setTotalFee(feeAssign.getTestFee()+feeAssign.getRepairFee()+feeAssign.getMaterialFee()+feeAssign.getCarFee()+feeAssign.getDebugFee()+feeAssign.getOtherFee());
						feeAssign.setTotalFeeOld(feeAssign.getTotalFee());
						
						//其它信息
						feeAssign.setCertificate(oRecord.getCertificate());
						feeAssign.setOriginalRecord(oRecord);
						feeAssign.setCommissionSheet(cSheet);
						feeAssign.setSysUserByLastEditorId(loginUser);
						feeAssign.setLastEditTime(nowTime);
						
						feeAssignList.add(feeAssign);
						//更新每个证书分配的费用的最大值
						if(maxTestFeeMap.containsKey(j)){	//检测费
							if(feeAssign.getTestFee() > maxTestFeeMap.get(j).getTestFee()){
								maxTestFeeMap.put(j, feeAssign);
							}
						}else{
							maxTestFeeMap.put(j, feeAssign);
						}
						if(maxRepairFeeMap.containsKey(j)){	//修理费
							if(feeAssign.getRepairFee() > maxRepairFeeMap.get(j).getRepairFee()){
								maxRepairFeeMap.put(j, feeAssign);
							}
						}else{
							maxRepairFeeMap.put(j, feeAssign);
						}
						if(maxMaterialFeeMap.containsKey(j)){	//配件费
							if(feeAssign.getMaterialFee() > maxMaterialFeeMap.get(j).getMaterialFee()){
								maxMaterialFeeMap.put(j, feeAssign);
							}
						}else{
							maxMaterialFeeMap.put(j, feeAssign);
						}
						if(maxCarFeeMap.containsKey(j)){	//交通费
							if(feeAssign.getCarFee() > maxCarFeeMap.get(j).getCarFee()){
								maxCarFeeMap.put(j, feeAssign);
							}
						}else{
							maxCarFeeMap.put(j, feeAssign);
						}
						if(maxDebugFeeMap.containsKey(j)){	//调试费
							if(feeAssign.getDebugFee() > maxDebugFeeMap.get(j).getDebugFee()){
								maxDebugFeeMap.put(j, feeAssign);
							}
						}else{
							maxDebugFeeMap.put(j, feeAssign);
						}
						if(maxOtherFeeMap.containsKey(j)){	//其他费
							if(feeAssign.getOtherFee() > maxOtherFeeMap.get(j).getOtherFee()){
								maxOtherFeeMap.put(j, feeAssign);
							}
						}else{
							maxOtherFeeMap.put(j, feeAssign);
						}
						//更新每个证书已分配费用的产值总和
						totalTestFeeMap.put(j, feeAssign.getTestFee() + (totalTestFeeMap.containsKey(j)?totalTestFeeMap.get(j):0.0));
						totalRepairFeeMap.put(j, feeAssign.getRepairFee() + (totalRepairFeeMap.containsKey(j)?totalRepairFeeMap.get(j):0.0));
						totalMaterialFeeMap.put(j, feeAssign.getMaterialFee() + (totalMaterialFeeMap.containsKey(j)?totalMaterialFeeMap.get(j):0.0));
						totalCarFeeMap.put(j, feeAssign.getCarFee() + (totalCarFeeMap.containsKey(j)?totalCarFeeMap.get(j):0.0));
						totalDebugFeeMap.put(j, feeAssign.getDebugFee() + (totalDebugFeeMap.containsKey(j)?totalDebugFeeMap.get(j):0.0));
						totalOtherFeeMap.put(j, feeAssign.getOtherFee() + (totalOtherFeeMap.containsKey(j)?totalOtherFeeMap.get(j):0.0));
					}//end inner for
				}//end outer for
				
				//判断各种费用额度的总和是否等于1
				if(1.0 != testFeeTotal){
					throw new Exception(String.format("所分配的检定费额度总和'%s'不等于1.0！", testFeeTotal));
				}
				if(1.0 != repairFeeTotal){
					throw new Exception(String.format("所分配的修理费额度总和'%s'不等于1.0！", repairFeeTotal));
				}
				if(1.0 != materialFeeTotal){
					throw new Exception(String.format("所分配的材料费额度总和'%s'不等于1.0！", materialFeeTotal));
				}
				if(1.0 != carFeeTotal){
					throw new Exception(String.format("所分配的交通费额度总和'%s'不等于1.0！", carFeeTotal));
				}
				if(1.0 != debugFeeTotal){
					throw new Exception(String.format("所分配的调试费额度总和'%s'不等于1.0！", debugFeeTotal));
				}
				if(1.0 != otherFeeTotal){
					throw new Exception(String.format("所分配的其它费额度总和'%s'不等于1.0！", otherFeeTotal));
				}
				
				//处理各个证书费用差额：分摊至最大项
				for(int j = 0; j < oRecordList.size(); j++){
					OriginalRecord oRecord = oRecordList.get(j);
					
					//检测费
					Double balanceTest = (oRecord.getTestFee()==null?0.0:oRecord.getTestFee()) - totalTestFeeMap.get(j);
			    	CertificateFeeAssign feeAssignTest = maxTestFeeMap.get(j);
					feeAssignTest.setTestFee(new Double(Math.round(feeAssignTest.getTestFee() + balanceTest)));
					feeAssignTest.setTestFeeOld(feeAssignTest.getTestFee());
					feeAssignTest.setTotalFee(feeAssignTest.getTestFee() + feeAssignTest.getRepairFee() + feeAssignTest.getMaterialFee() + feeAssignTest.getCarFee() + feeAssignTest.getDebugFee() + feeAssignTest.getOtherFee());
					feeAssignTest.setTotalFeeOld(feeAssignTest.getTotalFee());
					
					//修理费
					Double balanceRepair = (oRecord.getRepairFee()==null?0.0:oRecord.getRepairFee()) - totalRepairFeeMap.get(j);
			    	CertificateFeeAssign feeAssignRepair = maxRepairFeeMap.get(j);
					feeAssignRepair.setRepairFee(new Double(Math.round(feeAssignRepair.getRepairFee() + balanceRepair)));
					feeAssignRepair.setRepairFeeOld(feeAssignRepair.getRepairFee());
					feeAssignRepair.setTotalFee(feeAssignRepair.getTestFee() + feeAssignRepair.getRepairFee() + feeAssignRepair.getMaterialFee() + feeAssignRepair.getCarFee() + feeAssignRepair.getDebugFee() + feeAssignRepair.getOtherFee());
					feeAssignRepair.setTotalFeeOld(feeAssignRepair.getTotalFee());
					
					//配件费
					Double balanceMaterial = (oRecord.getMaterialFee()==null?0.0:oRecord.getMaterialFee()) - totalMaterialFeeMap.get(j);
			    	CertificateFeeAssign feeAssignMaterial = maxMaterialFeeMap.get(j);
					feeAssignMaterial.setMaterialFee(new Double(Math.round(feeAssignMaterial.getMaterialFee() + balanceMaterial)));
					feeAssignMaterial.setMaterialFeeOld(feeAssignMaterial.getMaterialFee());
					feeAssignMaterial.setTotalFee(feeAssignMaterial.getTestFee() + feeAssignMaterial.getRepairFee() + feeAssignMaterial.getMaterialFee() + feeAssignMaterial.getCarFee() + feeAssignMaterial.getDebugFee() + feeAssignMaterial.getOtherFee());
					feeAssignMaterial.setTotalFeeOld(feeAssignMaterial.getTotalFee());
					
					//交通费
					Double balanceCar = (oRecord.getCarFee()==null?0.0:oRecord.getCarFee()) - totalCarFeeMap.get(j);
			    	CertificateFeeAssign feeAssignCar = maxCarFeeMap.get(j);
					feeAssignCar.setCarFee(new Double(Math.round(feeAssignCar.getCarFee() + balanceCar)));
					feeAssignCar.setCarFeeOld(feeAssignCar.getCarFee());
					feeAssignCar.setTotalFee(feeAssignCar.getTestFee() + feeAssignCar.getRepairFee() + feeAssignCar.getMaterialFee() + feeAssignCar.getCarFee() + feeAssignCar.getDebugFee() + feeAssignCar.getOtherFee());
					feeAssignCar.setTotalFeeOld(feeAssignCar.getTotalFee());
					
					//调试费
					Double balanceDebug = (oRecord.getDebugFee()==null?0.0:oRecord.getDebugFee()) - totalDebugFeeMap.get(j);
			    	CertificateFeeAssign feeAssignDebug = maxDebugFeeMap.get(j);
					feeAssignDebug.setDebugFee(new Double(Math.round(feeAssignDebug.getDebugFee() + balanceDebug)));
					feeAssignDebug.setDebugFeeOld(feeAssignDebug.getDebugFee());
					feeAssignDebug.setTotalFee(feeAssignDebug.getTestFee() + feeAssignDebug.getRepairFee() + feeAssignDebug.getMaterialFee() + feeAssignDebug.getCarFee() + feeAssignDebug.getDebugFee() + feeAssignDebug.getOtherFee());
					feeAssignDebug.setTotalFeeOld(feeAssignDebug.getTotalFee());
					
					//其他费
					Double balanceOther = (oRecord.getOtherFee()==null?0.0:oRecord.getOtherFee()) - totalOtherFeeMap.get(j);
			    	CertificateFeeAssign feeAssignOther = maxOtherFeeMap.get(j);
					feeAssignOther.setOtherFee(new Double(Math.round(feeAssignOther.getOtherFee() + balanceOther)));
					feeAssignOther.setOtherFeeOld(feeAssignOther.getOtherFee());
					feeAssignOther.setTotalFee(feeAssignOther.getTestFee() + feeAssignOther.getRepairFee() + feeAssignOther.getMaterialFee() + feeAssignOther.getCarFee() + feeAssignOther.getDebugFee() + feeAssignOther.getOtherFee());
					feeAssignOther.setTotalFeeOld(feeAssignOther.getTotalFee());
				}
				
				feeAssignMgr.executeFeeAssignBySheet(feeAssignList);
				retJSON7.put("IsOK", true);
				retJSON7.put("msg", String.format("产值分配成功，本次共分配‘%d’张证书产值！", oRecordList.size()));
			}catch(Exception e){
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("产值分配失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CertificateFeeAssignServlet-->case 7", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 7", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON7.toString());
			}
			break;
		}
	}

}
