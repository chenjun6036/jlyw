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
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		CertificateFeeAssignManager feeAssignMgr = new CertificateFeeAssignManager();
		switch (method) {
		case 0: // ��ѯһ��ԭʼ��¼��ĳһ��֤��ķ��÷�����Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String CertificateId = req.getParameter("CertificateId");
				if(OriginalRecordId == null || OriginalRecordId.length() == 0 ||
						CertificateId == null || CertificateId.length() == 0){
					throw new Exception("����������");
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 0", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 0", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//Ϊһ��ԭʼ��¼��һ��֤������һ�����÷���
			JSONObject retJSON1 = new JSONObject();
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String CertificateId = req.getParameter("CertificateId");
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//����Ĳ�ֵ��Ϣ
				if(OriginalRecordId == null || OriginalRecordId.length() == 0 ||
						CertificateId == null || CertificateId.length() == 0 ||
						FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("����������");
				}
				
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				CertificateManager cMgr = new CertificateManager();
				Certificate certificate = cMgr.findById(Integer.parseInt(CertificateId));
				if(certificate == null){
					throw new Exception("�Ҳ���ָ����֤�飡");
				}
				if(!FlagUtil.CertificateFlag.isCertificateOfficial(certificate)){
					throw new Exception("��֤����δ��ɣ����ܷ����ֵ��");
				}
				if(!certificate.getId().equals(oRecord.getCertificate()==null?null:oRecord.getCertificate().getId())){
					throw new Exception("ָ����֤�鲻�����°汾������Ϊ������ֵ��");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(oRecord.getCommissionSheet().getStatus())){
					throw new Exception("��ί�е����깤������ע���������ٴη����ֵ��");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null || !loginUser.getId().equals(oRecord.getSysUserByStaffId().getId())){
					throw new Exception("�����Ǹ�֤��ļ춨/У׼��Ա�����ܶ��������ã�");
				}
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//���÷�����Ϣ
				
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
							throw new Exception(String.format("%s ������ļ춨�Ѳ���С��0��", alloteeName));
						}
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						repairFeeTotal += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s �����������Ѳ���С��0��", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						materialFeeTotal += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s �����������Ѳ���С��0��", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						carFeeTotal += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s ������Ľ�ͨ�Ѳ���С��0��", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						debugFeeTotal += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s ������ĵ��ԷѲ���С��0��", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						otherFeeTotal += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s ������������Ѳ���С��0��", alloteeName));
						}
					}
					List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
					if(alloteeList.size() == 0){
						throw new Exception(String.format("�Ҳ�������Ϊ'%s'����Ա��", alloteeName));
					}else if(alloteeList.size() > 1){
						throw new Exception(String.format("��ǰϵͳ����Ϊ'%s'����Ա��%s�����޷�Ψһȷ�������ֵ��Ա��", alloteeName, alloteeList.size()));
					}
					if(totalFee <= 0){
						throw new Exception(String.format("%s ������Ĳ�ֵ����Ϊ0���������Աû�в�ֵ����ɾ������Ŀ��", alloteeName));
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
					
					//������Ϣ
					feeAssign.setCertificate(certificate);
					feeAssign.setOriginalRecord(oRecord);
					feeAssign.setCommissionSheet(oRecord.getCommissionSheet());
					feeAssign.setSysUserByLastEditorId(loginUser);
					feeAssign.setLastEditTime(nowTime);
					
					feeAssignList.add(feeAssign);
				}
				if(feeAssignList.size() > 0){	//��feeAssignList.size()==0����˵����ɾ��ԭ���ķ����¼
					if((oRecord.getTestFee()==null?0:oRecord.getTestFee()) != testFeeTotal){
						throw new Exception(String.format("������ļ춨���ܶ�'%s'��ԭʼ��¼�ļ춨����'%s'��һ�£�", testFeeTotal, oRecord.getTestFee()==null?0:oRecord.getTestFee()));
					}
					if((oRecord.getRepairFee()==null?0:oRecord.getRepairFee()) != repairFeeTotal){
						throw new Exception(String.format("�������������ܶ�'%s'��ԭʼ��¼���������'%s'��һ�£�", repairFeeTotal, oRecord.getRepairFee()==null?0:oRecord.getRepairFee()));
					}
					if((oRecord.getMaterialFee()==null?0:oRecord.getMaterialFee()) != materialFeeTotal){
						throw new Exception(String.format("������Ĳ��Ϸ��ܶ�'%s'��ԭʼ��¼�Ĳ��Ϸ���'%s'��һ�£�", materialFeeTotal, oRecord.getMaterialFee()==null?0:oRecord.getMaterialFee()));
					}
					if((oRecord.getCarFee()==null?0:oRecord.getCarFee()) != carFeeTotal){
						throw new Exception(String.format("������Ľ�ͨ���ܶ�'%s'��ԭʼ��¼�Ľ�ͨ����'%s'��һ�£�", carFeeTotal, oRecord.getCarFee()==null?0:oRecord.getCarFee()));
					}
					if((oRecord.getDebugFee()==null?0:oRecord.getDebugFee()) != debugFeeTotal){
						throw new Exception(String.format("������ĵ��Է��ܶ�'%s'��ԭʼ��¼�ĵ��Է���'%s'��һ�£�", debugFeeTotal, oRecord.getDebugFee()==null?0:oRecord.getDebugFee()));
					}
					if((oRecord.getOtherFee()==null?0:oRecord.getOtherFee()) != otherFeeTotal){
						throw new Exception(String.format("��������������ܶ�'%s'��ԭʼ��¼����������'%s'��һ�£�", otherFeeTotal, oRecord.getOtherFee()==null?0:oRecord.getOtherFee()));
					}
				}
				feeAssignMgr.executeFeeAssign(oRecord, certificate, feeAssignList);
				retJSON1.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("��ֵ����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 1", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 1", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2: // ��ѯһ��ί�е��µ����з�����Ϣ�������춨֤��ķ��� + �����ѣ���ת���ѡ���������ѵȣ�
			JSONObject retJSON2 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("����������");
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
				footer.put("CertificateCode", "�ϼ�");
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 2", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 2", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3:	//Ϊһ��֤������һ�������ķ��ã���ת���ѡ���������ѣ�����֤����ã�
			JSONObject retJSON3 = new JSONObject();
			try{
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//����Ĳ�ֵ��Ϣ
				if(CommissionSheetId == null || CommissionSheetId.length() == 0 ||
						FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("����������");
				}
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CommissionSheet cSheet = cSheetMgr.findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("�Ҳ���ָ����ί�е���");
				}
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("��ί�е����깤������ע���������ٴη�����ã�");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�����ܶ�ί�е�������ã�");
				}
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//���÷�����Ϣ
				
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
							throw new Exception(String.format("%s ������ļ춨�Ѳ���С��0��", alloteeName));
						}
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s �����������Ѳ���С��0��", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s �����������Ѳ���С��0��", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s ������Ľ�ͨ�Ѳ���С��0��", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s ������ĵ��ԷѲ���С��0��", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s ������������Ѳ���С��0��", alloteeName));
						}
					}
					SysUser allotee = null;
					if(alloteeName != null && alloteeName.length() > 0){
						List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
						if(alloteeList.size() == 0){
							throw new Exception(String.format("�Ҳ�������Ϊ'%s'����Ա��", alloteeName));
						}else if(alloteeList.size() > 1){
							throw new Exception(String.format("��ǰϵͳ����Ϊ'%s'����Ա��%s�����޷�Ψһȷ�������ֵ��Ա��", alloteeName, alloteeList.size()));
						}
						allotee = alloteeList.get(0);
					}
					
					if(totalFee <= 0){
						throw new Exception(String.format("%s ������ķ��ò���Ϊ0���������Աû�в�ֵ����ɾ������Ŀ��", alloteeName));
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
					
					//������Ϣ
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
					retJSON3.put("msg", String.format("ί�е��������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 3", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 3", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4: // ��ѯһ��ί�е��µ����������Ϣ����ת���ѡ���������ѣ��������춨֤��ķ��ã�
			JSONObject retJSON4 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("����������");
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 4", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 4", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	//֤������޸�
			JSONObject retJSON5 = new JSONObject();
			try{
				
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//����Ĳ�ֵ��Ϣ
				if(FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("����������");
				}
				
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�����ܶ�ί�е�������ã�");
				}
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//���÷�����Ϣ
				
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				List<CertificateFeeAssign> feeAssignList = new ArrayList<CertificateFeeAssign>();
				CertificateFeeAssignManager cerfeeMgr=new CertificateFeeAssignManager();
				UserManager userMgr = new UserManager();
				for(int i = 0; i < feeAssignArray.length(); i++){
					JSONObject jsonObj = feeAssignArray.getJSONObject(i);
					CertificateFeeAssign feeAssign = cerfeeMgr.findById(Integer.parseInt(jsonObj.getString("CertificateFeeAssignId")));
					if(feeAssign.getCommissionSheet().getCheckOutDate() != null){
						throw new Exception("��ί�е��Ѿ����ˣ������޸ķ��ã�");
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
							throw new Exception(String.format("%s ������ļ춨�Ѳ���С��0��", alloteeName));
						}
						
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s �����������Ѳ���С��0��", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s �����������Ѳ���С��0��", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s ������Ľ�ͨ�Ѳ���С��0��", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s ������ĵ��ԷѲ���С��0��", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s ������������Ѳ���С��0��", alloteeName));
						}
					}
					SysUser allotee = null;
					if(alloteeName != null && alloteeName.length() > 0){
						List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
						if(alloteeList.size() == 0){
							throw new Exception(String.format("�Ҳ�������Ϊ'%s'����Ա��", alloteeName));
						}else if(alloteeList.size() > 1){
							throw new Exception(String.format("��ǰϵͳ����Ϊ'%s'����Ա��%s�����޷�Ψһȷ�������ֵ��Ա��", alloteeName, alloteeList.size()));
						}
						allotee = alloteeList.get(0);
					}
					
//					if(totalFee <= 0){
//						throw new Exception(String.format("%s ������ķ��ò���Ϊ0���������Աû�в�ֵ����ɾ������Ŀ��", alloteeName));
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
	
					feeAssign.setRemark(remarkStr + String.format("[%s��%sִ�иķ�(�춨��%s->%s,�����%s->%s,���Ϸ�%s->%s,��ͨ��%s->%s,���Է�%s->%s,������%s->%s,�ܷ���%s->%s)]", 
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
					
					//������Ϣ										
					feeAssign.setSysUserByLastEditorId(loginUser);
					feeAssign.setLastEditTime(nowTime);
					
					feeAssignList.add(feeAssign);
				}
				feeAssignMgr.updateFeeAssign(feeAssignList);
				retJSON5.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON5.put("IsOK", false);
					retJSON5.put("msg", String.format("֤������޸�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 5", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 5", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_FormSubmit);
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6: // ��ѯһ��ί�е��µ����з�����Ϣ�������춨֤��ķ��� + �����ѣ���ת���ѡ���������ѵȣ�
			JSONObject retJSON6 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("����������");
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in CertificateFeeAssignServlet-->case 6", e);
				}else{
					log.error("error in CertificateFeeAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType(SysStringUtil.ResponseContentType.Type_EasyUIDatagrid);
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7:		//Ϊһ��ί�е������ֵ������ǰ��¼�û�����ġ���δ����֤����õ�����֤��ķ��ý��з���
			JSONObject retJSON7 = new JSONObject();
			try{
				String CommissionSheetId = req.getParameter("CommissionSheetId");
				String FeeAssignInfo = req.getParameter("FeeAssignInfo");	//����Ĳ�ֵ��Ϣ
				if(CommissionSheetId == null || CommissionSheetId.length() == 0 ||
						FeeAssignInfo == null || FeeAssignInfo.length() == 0){
					throw new Exception("����������");
				}
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CommissionSheet cSheet = cSheetMgr.findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("�Ҳ���ָ����ί�е���");
				}
				
				if(FlagUtil.CommissionSheetStatus.checkCommissionSheetFinishedOrInvalid(cSheet.getStatus())){
					throw new Exception("��ί�е����깤������ע���������ٴη����ֵ��");
				}
				SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�����ܶ��������ã�");
				}
				
				JSONArray feeAssignArray = new JSONArray(FeeAssignInfo);	//���÷�����Ϣ
				if(feeAssignArray.length() == 0){
					throw new Exception("δ������÷�����Ϣ��");
				}
				
				//��ѯ��ǰ�û�����ģ�����δ������õ�֤���¼
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				String queryString  = "from OriginalRecord as model where model.status<>1 and model.taskAssign.status<>1 and model.sysUserByStaffId.id = ? and " +
						" model.commissionSheet = ? and model.certificate.pdf is not null and " +	//��������ʽ֤���ԭʼ��¼
						" model not in ( " +	//δ��������õ�֤��
						"     select f.originalRecord from CertificateFeeAssign as f where f.originalRecord is not null and f.certificate is not null and f.originalRecord=model and f.certificate=model.certificate " +
						" ) ";
				List<OriginalRecord> oRecordList = oRecordMgr.findByHQL(queryString, loginUser.getId(), cSheet);
				
				if(oRecordList.size() == 0){
					throw new Exception(String.format("��ί�е�Ŀǰû���� '%s'������δ������õ�֤���¼��", loginUser.getName()));
				}
				
				Map<Integer, CertificateFeeAssign> maxTestFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//���ÿ��֤���¼������Ĳ�ֵ�����ֵ��������,��������oRecordList��������һһ��Ӧ
				Map<Integer, CertificateFeeAssign> maxRepairFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//���ÿ��֤���¼������Ĳ�ֵ�����ֵ���������
				Map<Integer, CertificateFeeAssign> maxMaterialFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//���ÿ��֤���¼������Ĳ�ֵ�����ֵ���������
				Map<Integer, CertificateFeeAssign> maxCarFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//���ÿ��֤���¼������Ĳ�ֵ�����ֵ������ͨ��
				Map<Integer, CertificateFeeAssign> maxDebugFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//���ÿ��֤���¼������Ĳ�ֵ�����ֵ�������Է�
				Map<Integer, CertificateFeeAssign> maxOtherFeeMap = new HashMap<Integer, CertificateFeeAssign>();	//���ÿ��֤���¼������Ĳ�ֵ�����ֵ����������
				Map<Integer, Double> totalTestFeeMap = new HashMap<Integer, Double>();	//���ÿ��֤���¼�ѷ���Ĳ�ֵ�ܺ͡�������,��������oRecordList��������һһ��Ӧ
				Map<Integer, Double> totalRepairFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalMaterialFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalCarFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalDebugFeeMap = new HashMap<Integer, Double>();
				Map<Integer, Double> totalOtherFeeMap = new HashMap<Integer, Double>();
								
				Timestamp nowTime = new Timestamp(System.currentTimeMillis());
				List<CertificateFeeAssign> feeAssignList = new ArrayList<CertificateFeeAssign>();
				UserManager userMgr = new UserManager();
				double testFeeTotal = 0.0, repairFeeTotal = 0.0, materialFeeTotal = 0.0, carFeeTotal = 0.0, debugFeeTotal = 0.0, otherFeeTotal = 0.0;	//���ַ��ö�ȵ��ܺ�
				for(int i = 0; i < feeAssignArray.length(); i++){
					JSONObject jsonObj = feeAssignArray.getJSONObject(i);
					String alloteeName = jsonObj.has("FeeAlloteeName")?jsonObj.getString("FeeAlloteeName"):"";
					String testFeeStr = jsonObj.has("TestFee")?jsonObj.getString("TestFee").trim():"";
					String repairFeeStr = jsonObj.has("RepairFee")?jsonObj.getString("RepairFee").trim():"";
					String materialFeeStr = jsonObj.has("MaterialFee")?jsonObj.getString("MaterialFee").trim():"";
					String carFeeStr = jsonObj.has("CarFee")?jsonObj.getString("CarFee").trim():"";
					String debugFeeStr = jsonObj.has("DebugFee")?jsonObj.getString("DebugFee").trim():"";
					String otherFeeStr = jsonObj.has("OtherFee")?jsonObj.getString("OtherFee").trim():"";
					
					
					double testFee = 0.0, repairFee = 0.0, materialFee = 0.0, carFee = 0.0, debugFee = 0.0, otherFee = 0.0, totalFee = 0.0;	 //�����õ��ۿ۶�ȣ�totalFee���⣩
					if(testFeeStr.length() > 0){
						testFee = Double.parseDouble(testFeeStr);
						totalFee += testFee;
						testFeeTotal += testFee;
						if(testFee < 0){
							throw new Exception(String.format("%s ������ļ춨�Ѷ�Ȳ���С��0��", alloteeName));
						}else if(testFee > 1){
							throw new Exception(String.format("%s ������ļ춨�Ѷ�Ȳ��ܴ���1��", alloteeName));
						}
					}
					if(repairFeeStr.length() > 0){
						repairFee = Double.parseDouble(repairFeeStr);
						totalFee += repairFee;
						repairFeeTotal += repairFee;
						if(repairFee < 0){
							throw new Exception(String.format("%s �����������Ѷ�Ȳ���С��0��", alloteeName));
						}else if(repairFee > 1){
							throw new Exception(String.format("%s �����������Ѷ�Ȳ��ܴ���1��", alloteeName));
						}
					}
					if(materialFeeStr.length() > 0){
						materialFee = Double.parseDouble(materialFeeStr);
						totalFee += materialFee;
						materialFeeTotal += materialFee;
						if(materialFee < 0){
							throw new Exception(String.format("%s �����������Ѷ�Ȳ���С��0��", alloteeName));
						}else if(materialFee > 1){
							throw new Exception(String.format("%s �����������Ѷ�Ȳ��ܴ���1��", alloteeName));
						}
					}
					if(carFeeStr.length() > 0){
						carFee = Double.parseDouble(carFeeStr);
						totalFee += carFee;
						carFeeTotal += carFee;
						if(carFee < 0){
							throw new Exception(String.format("%s ������Ľ�ͨ�Ѷ�Ȳ���С��0��", alloteeName));
						}else if(carFee > 1){
							throw new Exception(String.format("%s ������Ľ�ͨ�Ѷ�Ȳ��ܴ���1��", alloteeName));
						}
					}
					if(debugFeeStr.length() > 0){
						debugFee = Double.parseDouble(debugFeeStr);
						totalFee += debugFee;
						debugFeeTotal += debugFee;
						if(debugFee < 0){
							throw new Exception(String.format("%s ������ĵ��ԷѶ�Ȳ���С��0��", alloteeName));
						}else if(debugFee > 1){
							throw new Exception(String.format("%s ������ĵ��ԷѶ�Ȳ��ܴ���1��", alloteeName));
						}
					}
					if(otherFeeStr.length() > 0){
						otherFee = Double.parseDouble(otherFeeStr);
						totalFee += otherFee;
						otherFeeTotal += otherFee;
						if(otherFee < 0){
							throw new Exception(String.format("%s ������������Ѷ�Ȳ���С��0��", alloteeName));
						}else if(otherFee > 1){
							throw new Exception(String.format("%s ������������Ѷ�Ȳ��ܴ���1��", alloteeName));
						}
					}
					List<SysUser> alloteeList = userMgr.findByVarProperty(new KeyValueWithOperator("name", alloteeName, "="));
					if(alloteeList.size() == 0){
						throw new Exception(String.format("�Ҳ�������Ϊ'%s'����Ա��", alloteeName));
					}else if(alloteeList.size() > 1){
						throw new Exception(String.format("��ǰϵͳ����Ϊ'%s'����Ա��%s�����޷�Ψһȷ�������ֵ��Ա��", alloteeName, alloteeList.size()));
					}
					if(totalFee <= 0){
						throw new Exception(String.format("%s ������Ĳ�ֵ����Ϊ0���������Աû�в�ֵ����ɾ������Ŀ��", alloteeName));
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
						
						//������Ϣ
						feeAssign.setCertificate(oRecord.getCertificate());
						feeAssign.setOriginalRecord(oRecord);
						feeAssign.setCommissionSheet(cSheet);
						feeAssign.setSysUserByLastEditorId(loginUser);
						feeAssign.setLastEditTime(nowTime);
						
						feeAssignList.add(feeAssign);
						//����ÿ��֤�����ķ��õ����ֵ
						if(maxTestFeeMap.containsKey(j)){	//����
							if(feeAssign.getTestFee() > maxTestFeeMap.get(j).getTestFee()){
								maxTestFeeMap.put(j, feeAssign);
							}
						}else{
							maxTestFeeMap.put(j, feeAssign);
						}
						if(maxRepairFeeMap.containsKey(j)){	//�����
							if(feeAssign.getRepairFee() > maxRepairFeeMap.get(j).getRepairFee()){
								maxRepairFeeMap.put(j, feeAssign);
							}
						}else{
							maxRepairFeeMap.put(j, feeAssign);
						}
						if(maxMaterialFeeMap.containsKey(j)){	//�����
							if(feeAssign.getMaterialFee() > maxMaterialFeeMap.get(j).getMaterialFee()){
								maxMaterialFeeMap.put(j, feeAssign);
							}
						}else{
							maxMaterialFeeMap.put(j, feeAssign);
						}
						if(maxCarFeeMap.containsKey(j)){	//��ͨ��
							if(feeAssign.getCarFee() > maxCarFeeMap.get(j).getCarFee()){
								maxCarFeeMap.put(j, feeAssign);
							}
						}else{
							maxCarFeeMap.put(j, feeAssign);
						}
						if(maxDebugFeeMap.containsKey(j)){	//���Է�
							if(feeAssign.getDebugFee() > maxDebugFeeMap.get(j).getDebugFee()){
								maxDebugFeeMap.put(j, feeAssign);
							}
						}else{
							maxDebugFeeMap.put(j, feeAssign);
						}
						if(maxOtherFeeMap.containsKey(j)){	//������
							if(feeAssign.getOtherFee() > maxOtherFeeMap.get(j).getOtherFee()){
								maxOtherFeeMap.put(j, feeAssign);
							}
						}else{
							maxOtherFeeMap.put(j, feeAssign);
						}
						//����ÿ��֤���ѷ�����õĲ�ֵ�ܺ�
						totalTestFeeMap.put(j, feeAssign.getTestFee() + (totalTestFeeMap.containsKey(j)?totalTestFeeMap.get(j):0.0));
						totalRepairFeeMap.put(j, feeAssign.getRepairFee() + (totalRepairFeeMap.containsKey(j)?totalRepairFeeMap.get(j):0.0));
						totalMaterialFeeMap.put(j, feeAssign.getMaterialFee() + (totalMaterialFeeMap.containsKey(j)?totalMaterialFeeMap.get(j):0.0));
						totalCarFeeMap.put(j, feeAssign.getCarFee() + (totalCarFeeMap.containsKey(j)?totalCarFeeMap.get(j):0.0));
						totalDebugFeeMap.put(j, feeAssign.getDebugFee() + (totalDebugFeeMap.containsKey(j)?totalDebugFeeMap.get(j):0.0));
						totalOtherFeeMap.put(j, feeAssign.getOtherFee() + (totalOtherFeeMap.containsKey(j)?totalOtherFeeMap.get(j):0.0));
					}//end inner for
				}//end outer for
				
				//�жϸ��ַ��ö�ȵ��ܺ��Ƿ����1
				if(1.0 != testFeeTotal){
					throw new Exception(String.format("������ļ춨�Ѷ���ܺ�'%s'������1.0��", testFeeTotal));
				}
				if(1.0 != repairFeeTotal){
					throw new Exception(String.format("�����������Ѷ���ܺ�'%s'������1.0��", repairFeeTotal));
				}
				if(1.0 != materialFeeTotal){
					throw new Exception(String.format("������Ĳ��ϷѶ���ܺ�'%s'������1.0��", materialFeeTotal));
				}
				if(1.0 != carFeeTotal){
					throw new Exception(String.format("������Ľ�ͨ�Ѷ���ܺ�'%s'������1.0��", carFeeTotal));
				}
				if(1.0 != debugFeeTotal){
					throw new Exception(String.format("������ĵ��ԷѶ���ܺ�'%s'������1.0��", debugFeeTotal));
				}
				if(1.0 != otherFeeTotal){
					throw new Exception(String.format("������������Ѷ���ܺ�'%s'������1.0��", otherFeeTotal));
				}
				
				//�������֤����ò���̯�������
				for(int j = 0; j < oRecordList.size(); j++){
					OriginalRecord oRecord = oRecordList.get(j);
					
					//����
					Double balanceTest = (oRecord.getTestFee()==null?0.0:oRecord.getTestFee()) - totalTestFeeMap.get(j);
			    	CertificateFeeAssign feeAssignTest = maxTestFeeMap.get(j);
					feeAssignTest.setTestFee(new Double(Math.round(feeAssignTest.getTestFee() + balanceTest)));
					feeAssignTest.setTestFeeOld(feeAssignTest.getTestFee());
					feeAssignTest.setTotalFee(feeAssignTest.getTestFee() + feeAssignTest.getRepairFee() + feeAssignTest.getMaterialFee() + feeAssignTest.getCarFee() + feeAssignTest.getDebugFee() + feeAssignTest.getOtherFee());
					feeAssignTest.setTotalFeeOld(feeAssignTest.getTotalFee());
					
					//�����
					Double balanceRepair = (oRecord.getRepairFee()==null?0.0:oRecord.getRepairFee()) - totalRepairFeeMap.get(j);
			    	CertificateFeeAssign feeAssignRepair = maxRepairFeeMap.get(j);
					feeAssignRepair.setRepairFee(new Double(Math.round(feeAssignRepair.getRepairFee() + balanceRepair)));
					feeAssignRepair.setRepairFeeOld(feeAssignRepair.getRepairFee());
					feeAssignRepair.setTotalFee(feeAssignRepair.getTestFee() + feeAssignRepair.getRepairFee() + feeAssignRepair.getMaterialFee() + feeAssignRepair.getCarFee() + feeAssignRepair.getDebugFee() + feeAssignRepair.getOtherFee());
					feeAssignRepair.setTotalFeeOld(feeAssignRepair.getTotalFee());
					
					//�����
					Double balanceMaterial = (oRecord.getMaterialFee()==null?0.0:oRecord.getMaterialFee()) - totalMaterialFeeMap.get(j);
			    	CertificateFeeAssign feeAssignMaterial = maxMaterialFeeMap.get(j);
					feeAssignMaterial.setMaterialFee(new Double(Math.round(feeAssignMaterial.getMaterialFee() + balanceMaterial)));
					feeAssignMaterial.setMaterialFeeOld(feeAssignMaterial.getMaterialFee());
					feeAssignMaterial.setTotalFee(feeAssignMaterial.getTestFee() + feeAssignMaterial.getRepairFee() + feeAssignMaterial.getMaterialFee() + feeAssignMaterial.getCarFee() + feeAssignMaterial.getDebugFee() + feeAssignMaterial.getOtherFee());
					feeAssignMaterial.setTotalFeeOld(feeAssignMaterial.getTotalFee());
					
					//��ͨ��
					Double balanceCar = (oRecord.getCarFee()==null?0.0:oRecord.getCarFee()) - totalCarFeeMap.get(j);
			    	CertificateFeeAssign feeAssignCar = maxCarFeeMap.get(j);
					feeAssignCar.setCarFee(new Double(Math.round(feeAssignCar.getCarFee() + balanceCar)));
					feeAssignCar.setCarFeeOld(feeAssignCar.getCarFee());
					feeAssignCar.setTotalFee(feeAssignCar.getTestFee() + feeAssignCar.getRepairFee() + feeAssignCar.getMaterialFee() + feeAssignCar.getCarFee() + feeAssignCar.getDebugFee() + feeAssignCar.getOtherFee());
					feeAssignCar.setTotalFeeOld(feeAssignCar.getTotalFee());
					
					//���Է�
					Double balanceDebug = (oRecord.getDebugFee()==null?0.0:oRecord.getDebugFee()) - totalDebugFeeMap.get(j);
			    	CertificateFeeAssign feeAssignDebug = maxDebugFeeMap.get(j);
					feeAssignDebug.setDebugFee(new Double(Math.round(feeAssignDebug.getDebugFee() + balanceDebug)));
					feeAssignDebug.setDebugFeeOld(feeAssignDebug.getDebugFee());
					feeAssignDebug.setTotalFee(feeAssignDebug.getTestFee() + feeAssignDebug.getRepairFee() + feeAssignDebug.getMaterialFee() + feeAssignDebug.getCarFee() + feeAssignDebug.getDebugFee() + feeAssignDebug.getOtherFee());
					feeAssignDebug.setTotalFeeOld(feeAssignDebug.getTotalFee());
					
					//������
					Double balanceOther = (oRecord.getOtherFee()==null?0.0:oRecord.getOtherFee()) - totalOtherFeeMap.get(j);
			    	CertificateFeeAssign feeAssignOther = maxOtherFeeMap.get(j);
					feeAssignOther.setOtherFee(new Double(Math.round(feeAssignOther.getOtherFee() + balanceOther)));
					feeAssignOther.setOtherFeeOld(feeAssignOther.getOtherFee());
					feeAssignOther.setTotalFee(feeAssignOther.getTestFee() + feeAssignOther.getRepairFee() + feeAssignOther.getMaterialFee() + feeAssignOther.getCarFee() + feeAssignOther.getDebugFee() + feeAssignOther.getOtherFee());
					feeAssignOther.setTotalFeeOld(feeAssignOther.getTotalFee());
				}
				
				feeAssignMgr.executeFeeAssignBySheet(feeAssignList);
				retJSON7.put("IsOK", true);
				retJSON7.put("msg", String.format("��ֵ����ɹ������ι����䡮%d����֤���ֵ��", oRecordList.size()));
			}catch(Exception e){
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("��ֵ����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
