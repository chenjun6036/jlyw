package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.jlyw.hibernate.AppStdNameProTeam;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.TaskAssign;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.InformationManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.TaskAssignManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

/**
 * �������
 * @author Administrator
 *
 */
public class TaskAssignServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(TaskAssignServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		TaskAssignManager tAssignMgr = new TaskAssignManager();
		switch (method) {
		case 0: // ����һ��ί�е������������Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
				List<TaskAssign> taskRetList = tAssignMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), new KeyValueWithOperator("status", 1, "<>"));
				if(taskRetList != null && taskRetList.size() > 0){
					JSONArray jsonArray = new JSONArray();
					for(TaskAssign t:taskRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("TaskId", t.getId());
						jsonObj.put("AppStdNameProTeamId", t.getAppStdNameProTeam()==null?"-1":t.getAppStdNameProTeam().getId());
						jsonObj.put("AppStdNameProTeamName", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName() == null)?"":t.getAppStdNameProTeam().getProjectName());
						jsonObj.put("AlloteeId", t.getSysUserByAlloteeId().getId());
						jsonObj.put("AlloteeName", t.getSysUserByAlloteeId().getName());
						jsonObj.put("AssignerId", t.getSysUserByAssignerId()==null?"":t.getSysUserByAssignerId().getId());
						jsonObj.put("AssignerName", t.getSysUserByAssignerId()==null?"":t.getSysUserByAssignerId().getName());
						jsonObj.put("AssignTime", DateTimeFormatUtil.DateTimeFormat.format(t.getAssignTime()));
						jsonObj.put("LastFeeAssignerId", t.getSysUserByLastFeeAssignerId()==null?"":t.getSysUserByLastFeeAssignerId().getId());
						jsonObj.put("LastFeeAssignerName", t.getSysUserByLastFeeAssignerId()==null?"":t.getSysUserByLastFeeAssignerId().getName());
						jsonObj.put("LastFeeAssignTime", t.getLastFeeAssignTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(t.getLastFeeAssignTime()));
						
						//������Ϣ
						jsonObj.put("TestFee", t.getTestFee()==null?"":t.getTestFee());	//����
						jsonObj.put("RepairFee", t.getRepairFee()==null?"":t.getRepairFee());	//�����
						jsonObj.put("MaterialFee", t.getMaterialFee()==null?"":t.getMaterialFee());	//���Ϸ�
						jsonObj.put("CarFee", t.getCarFee()==null?"":t.getCarFee());	//��ͨ��
						jsonObj.put("DebugFee", t.getDebugFee()==null?"":t.getDebugFee());	//���Է�
						jsonObj.put("OtherFee", t.getOtherFee()==null?"":t.getOtherFee());	//��������
						jsonObj.put("TotalFee", t.getTotalFee()==null?"":t.getTotalFee());	//�ܷ���
						
						jsonArray.put(jsonObj);
					}
					
					retJSON.put("total", taskRetList.size());
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
					log.debug("exception in TaskAssignServlet-->case 0", e);
				}else{
					log.error("error in TaskAssignServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//ע��������Ȩ�޵��˾�����ע���ѷ��������
			JSONObject retJSON1 = new JSONObject();
			try {
				String TaskId = req.getParameter("TaskId");	//ί�е�Id
				
				if(TaskId == null || TaskId.trim().length() == 0){
					throw new Exception("���񲻴��ڣ�");
				}
				TaskAssign t = tAssignMgr.findById(Integer.parseInt(TaskId));
				if(t != null){
					//�ж�ί�е���״̬
					if(t.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
							t.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
							t.getCommissionSheet().getStatus() == 9){		//�ѽ���
						throw new Exception("��ί�е����깤������ע��������");
					}
					
					t.setStatus(1);	//ע��
					if(tAssignMgr.update(t)){
						retJSON1.put("IsOK", true);
//						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_TaskReceived, String.format("����������ȡ����ί�е��ţ�%s", t.getCommissionSheet().getCode()), t.getSysUserByAlloteeId(), FlagUtil.SmsAndInfomationType.Type_TaskCancel);	//��ʱ��Ϣ
						return;
					}else{
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}
				}
				throw new Exception("�����񲻴��ڣ�");
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("ע������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 1", e);
				}else{
					log.error("error in TaskAssignServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:	//��ѯί�е���Ӧ�ļ�����Ŀ
			JSONArray retJSONArray2 = new JSONArray();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionId));
				if(cSheet == null){
					throw new Exception("��ί�е������ڣ�");
				}else{
					//Ԥ��һ���յļ�����Ŀ:�����������ʵ���
					JSONObject jsonObjTemp = new JSONObject();
					jsonObjTemp.put("appStdProId", "");	//������ĿId
					jsonObjTemp.put("appStdNameId", cSheet.getApplianceSpeciesId());	//��׼����ID���������ID
					jsonObjTemp.put("appStdProName", "Ĭ��");	//������Ŀ����
					jsonObjTemp.put("speciesType", cSheet.getSpeciesType()?1:0);	//����Or��׼����
					retJSONArray2.put(jsonObjTemp);	
					
					List<Object[]> retList = tAssignMgr.getInspectProjects(cSheet.getApplianceSpeciesId(), cSheet.getSpeciesType()?1:0);
					for(Object[] tempArray : retList){
						JSONObject jsonObj2 = new JSONObject();
						jsonObj2.put("appStdProId", (Integer)tempArray[0]);	//������ĿId
						jsonObj2.put("appStdNameId", (Integer)tempArray[1]);	//��׼����ID
						jsonObj2.put("appStdProName", (String)tempArray[2]);	//������Ŀ����
						jsonObj2.put("speciesType", 0);	//��׼����
						retJSONArray2.put(jsonObj2);
					}
					
/*					
					
//					ViewAppProMappingManager vMgr = new ViewAppProMappingManager();
					List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
					condList.add(new KeyValueWithOperator("proTeamStatus", 0, "="));	//��Ŀ��״̬������
					if(cSheet.getSpeciesType()){	//��������
						condList.add(new KeyValueWithOperator("id.appSpeId", cSheet.getApplianceSpeciesId(), "="));
					}else{		//��׼����
						condList.add(new KeyValueWithOperator("id.appStaNameId", cSheet.getApplianceSpeciesId(), "="));
					}
					List<ViewApplianceSpecialStandardNameProject> vRetList = vMgr.findByVarProperty(condList);
					Map<Integer, List<Object[]>> map = new HashMap<Integer, List<Object[]>>();
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_YEAR, 1);
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					Timestamp tYear = new Timestamp(c.getTimeInMillis()); //����Ŀ�ʼʱ��
					for(ViewApplianceSpecialStandardNameProject v : vRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("appStdProId", v.getId().getAppStdProId());	//������ĿId
						jsonObj.put("appStdProName", v.getAppStdProName());	//������Ŀ����
						Integer ProTeamId = v.getId().getProTeamId();
						List<Object[]> uList = map.get(ProTeamId);
						if(uList == null){
							if(SystemCfgUtil.getTaskAllotRule() == 0){	//��ҵ��������
								String queryString = "select u.id as field1, u.name as field2, (select count(*) from " + SystemCfgUtil.DBPrexName+"TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c where t.AssignTime>=? and t.AlloteeId=u.id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 ) as field3 " +
										" from "+SystemCfgUtil.DBPrexName+"SysUser as u" +
										" where u.projectTeamId=? and u.status=0" +
										" order by field3 asc ";
								
								uList = tAssignMgr.findBySQL(queryString, tYear, ProTeamId);
							}else{	//����ֵ����
								String queryString = "select u.id as field1, u.name as field2, (select sum(o.totalFee) from " + SystemCfgUtil.DBPrexName+ "TaskAssign as t,"+ SystemCfgUtil.DBPrexName+"CommissionSheet as c,"+ SystemCfgUtil.DBPrexName+"OriginalRecord as o where t.finishTime>=? and t.AlloteeId=u.id and t.status<>1 and t.CommissionSheetId=c.Id and c.status<>10 and t.id=o.TaskAssignId and o.status<>1 ) as field3 " +
									" from "+SystemCfgUtil.DBPrexName+"SysUser as u" +
									" where u.projectTeamId=? and u.status=0" +
									" order by field3 asc ";
								uList = tAssignMgr.findBySQL(queryString, tYear, ProTeamId);
							}
							if(uList != null){
								map.put(ProTeamId, uList);
							}
						}
						JSONArray jsonArrayUser = new JSONArray();
						for(Object[] user : uList){
							JSONObject jsonObjUser = new JSONObject();
							jsonObjUser.put("id", (Integer)user[0]);
							jsonObjUser.put("name", (String)user[1]);
							jsonArrayUser.put(jsonObjUser);
						}
						jsonObj.put("AlloteeList", jsonArrayUser);
						
						retJSONArray2.put(jsonObj);
					}
					
*/					
				}
			} catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 2", e);
				}else{
					log.error("error in TaskAssignServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSONArray2.toString());
			}
			break;
		case 3:	//��������
			JSONObject retJSON3 = new JSONObject();
			try {
				String CommissionSheetId = req.getParameter("CommissionSheetId");	//ί�е�Id
				String ProjectSelect = req.getParameter("ProjectSelect");	//������ĿId
				String AlloteeSelect = req.getParameter("AlloteeSelect");	//������ԱId
				if(CommissionSheetId == null || CommissionSheetId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
//				if(ProjectSelect == null || ProjectSelect.trim().length() == 0){
//					throw new Exception("������ĿΪ�գ�");
//				}
				if(AlloteeSelect == null || AlloteeSelect.trim().length() == 0){
					throw new Exception("������ԱΪ�գ�");
				}
				SysUser user = new UserManager().findById(Integer.parseInt(AlloteeSelect.trim()));
				if(user == null){
					throw new Exception("������Ա�����ڻ򲻿��ã�");
				}
				int totalCount = tAssignMgr.getTotalCount(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionSheetId), "="),
//						new KeyValueWithOperator("appStdNameProTeam.id", Integer.parseInt(ProjectSelect), "="),
						new KeyValueWithOperator("sysUserByAlloteeId.id", user.getId(), "="),
						new KeyValueWithOperator("status", 1, "<>"));	//����ע��������
				if(totalCount > 0){
					throw new Exception(String.format("��ί�е��ѷ����������Ա'%s'��һ��ί�е������ظ������ͬһ���ˡ��������·��䣬����ע���ѷ��������", user.getName()));
				}
				//����������
				TaskAssign t = new TaskAssign();
				t.setSysUserByAssignerId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//������
				CommissionSheet cSheet = new CommissionSheetManager().findById(Integer.parseInt(CommissionSheetId));
				//�ж�ί�е���״̬
				if(cSheet.getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						cSheet.getStatus() == 4 ||	//�ѽ���
						cSheet.getStatus() == 9){		//�ѽ���
					throw new Exception("��ί�е����깤�����ܷ��������");
				}
				
				t.setCommissionSheet(cSheet);	//ί�е�
				if(ProjectSelect != null && ProjectSelect.trim().length() > 0){
					AppStdNameProTeam aspt = new AppStdNameProTeam();
					aspt.setId(Integer.parseInt(ProjectSelect));
					t.setAppStdNameProTeam(aspt);	//������Ŀ
				}
				t.setSysUserByAlloteeId(user);	//���������
				t.setAssignTime(new Timestamp(System.currentTimeMillis()));	//����ʱ��
				t.setStatus(0);	//����״̬������
				if(tAssignMgr.save(t)){
					retJSON3.put("IsOK", true);
					InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_TaskReceived, String.format("�յ�һ���µļ�������ί�е��ţ�%s", cSheet.getCode()), user, FlagUtil.SmsAndInfomationType.Type_TaskReceived);	//��ʱ��Ϣ
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			} catch (Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("��������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 3", e);
				}else{
					log.error("error in TaskAssignServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4:	//��ѯ��ǰ��¼�û��������б�
			JSONObject retJSON4 = new JSONObject();
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
				condList.add(new KeyValueWithOperator("commissionSheet.status", FlagUtil.CommissionSheetStatus.Status_YiWanGong, "<"));	//ί�е���δע����
				condList.add(new KeyValueWithOperator("sysUserByAlloteeId.id", ((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId(), "="));  //������
				condList.add(new KeyValueWithOperator("status", 1, "<>"));  //����û��ע����
				condList.add(new KeyValueWithOperator("finishTime", null, "is null"));	//������δ��ɵ�
				String hqlQueryString_RecordQuantity = "select count(*) from OriginalRecord as model where model.commissionSheet.id = ? and model.status <> 1 and model.taskAssign.status <> 1";
				String hqlQueryString_FinishQuantity = "select sum(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1 and model.taskAssign.status<>1 and model.verifyAndAuthorize.authorizeResult=? and model.verifyAndAuthorize.isAuthBgRuning is null ";	//ǩ��ͨ����ԭʼ��¼����������(ǩ����ͨ���Ҳ������ں�ִ̨��)
				String hqlQueryString_WithdrawQuantity = "select sum(model.number) from Withdraw as model where model.commissionSheet.id=? and model.executeResult=?";	//����׼��������������
				String hqlQueryString_SubContract = "select count(*) from SubContract as model where model.commissionSheet.id=? and model.status<>1 and model.receiveDate is not null";
				
				int total = tAssignMgr.getTotalCount(condList);
				List<TaskAssign> tRetList = tAssignMgr.findPagedAllBySort(page, rows, "assignTime", true, condList);
				for(TaskAssign t : tRetList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("TaskId", t.getId());
					jsonObj.put("CommissionSheetId", t.getCommissionSheet().getId());
					jsonObj.put("ProjectName", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName()==null)?"":t.getAppStdNameProTeam().getProjectName());	//������Ŀ����
					jsonObj.put("Remark", t.getRemark()==null?"":t.getRemark());	//����ע
					jsonObj.put("AlloteeName", t.getSysUserByAlloteeId().getName());	//�������������
					jsonObj.put("AssignTime", DateTimeFormatUtil.DateTimeFormat.format(t.getAssignTime()));	//�������ʱ��
					jsonObj.put("CommissionCode", t.getCommissionSheet().getCode());
					jsonObj.put("CommissionPwd", t.getCommissionSheet().getPwd());
					jsonObj.put("ApplianceName", t.getCommissionSheet().getApplianceName());	//��������
					jsonObj.put("Quantity", t.getCommissionSheet().getQuantity());	//��������
					List<Long> oQuantityList = tAssignMgr.findByHQL(hqlQueryString_RecordQuantity, t.getCommissionSheet().getId());
					if(oQuantityList != null && oQuantityList.size() > 0 && oQuantityList.get(0) != null){
						jsonObj.put("RecordQuantity", oQuantityList.get(0));	//�깤��������
					}else{
						jsonObj.put("RecordQuantity", 0);
					}
					List<Long> fQuantityList = tAssignMgr.findByHQL(hqlQueryString_FinishQuantity, t.getCommissionSheet().getId(), true);	//�깤��������
					if(fQuantityList != null && fQuantityList.size() > 0 && fQuantityList.get(0) != null){
						jsonObj.put("FinishQuantity", fQuantityList.get(0));	//�깤��������
					}else{
						jsonObj.put("FinishQuantity", 0);
					}
					List<Long> wQuantityList = tAssignMgr.findByHQL(hqlQueryString_WithdrawQuantity, t.getCommissionSheet().getId(), true);	//������������
					if(wQuantityList != null && wQuantityList.size() > 0 && wQuantityList.get(0) != null){
						jsonObj.put("EffectQuantity", t.getCommissionSheet().getQuantity() - ((Long)wQuantityList.get(0)).intValue());	//��Ч��������
					}else{
						jsonObj.put("EffectQuantity", t.getCommissionSheet().getQuantity());
					}
					jsonObj.put("CustomerName", t.getCommissionSheet().getCustomerName());	//ί�е�λ����
					jsonObj.put("CustomerContactor", t.getCommissionSheet().getCustomerContactor());	//ί�е�λ��ϵ��
					jsonObj.put("CustomerContactorTel", t.getCommissionSheet().getCustomerContactorTel());	//ί�е�λ��ϵ�˵绰
					jsonObj.put("Urgent", t.getCommissionSheet().getUrgent()?1:0);	//�Ƿ�Ӽ�
					Timestamp CommissionDate = (t.getCommissionSheet().getCommissionType()==2 && t.getCommissionSheet().getLocaleCommissionDate()!=null)?t.getCommissionSheet().getLocaleCommissionDate():t.getCommissionSheet().getCommissionDate();
					Timestamp PromiseDate = t.getCommissionSheet().getPromiseDate()==null?null:new Timestamp(t.getCommissionSheet().getPromiseDate().getTime());
					jsonObj.put("CommissionDate", DateTimeFormatUtil.DateFormat.format(CommissionDate));	//ί��ʱ��
					Timestamp today = new Timestamp(System.currentTimeMillis());
					if(PromiseDate != null){
						jsonObj.put("PromiseDate", DateTimeFormatUtil.DateFormat.format(PromiseDate));	//��ŵ�������
						if(today.after(PromiseDate)){
							jsonObj.put("IsOverdue", true);	//�Ƿ���
							jsonObj.put("OverdueDays", DateTimeFormatUtil.daysOfTwo(today, PromiseDate));	//���ڵ�����
						}else{
							jsonObj.put("IsOverdue", false);
							if(DateTimeFormatUtil.daysOfTwo(today, PromiseDate) <= SystemCfgUtil.OverdueThreSholdShort){	//����Ԥ�������ڣ�
								jsonObj.put("IsOverdueWarningShort", true);	//����Ԥ��
								jsonObj.put("IsOverdueWarningLong", true);	//����Ԥ��
								jsonObj.put("OverdueWarningDays", DateTimeFormatUtil.daysOfTwo(today, PromiseDate));	//�೬�ڻ��м���
							}else if(DateTimeFormatUtil.daysOfTwo(today, PromiseDate) <= SystemCfgUtil.OverdueThresholdLong){	//����Ԥ�������ڣ�
								jsonObj.put("IsOverdueWarningShort", false);	//����Ԥ��
								jsonObj.put("IsOverdueWarningLong", true);	//����Ԥ��
								jsonObj.put("OverdueWarningDays", DateTimeFormatUtil.daysOfTwo(today, PromiseDate));	//�೬�ڻ��м���
							}else{
								jsonObj.put("IsOverdueWarningShort", false);	//����Ԥ��
								jsonObj.put("IsOverdueWarningLong", false);	//����Ԥ��
							}
						}
					}else{
						jsonObj.put("PromiseDate", "");	//��ŵ�������
						jsonObj.put("IsOverdue", false);	//�Ƿ���
						jsonObj.put("IsOverdueWarningShort", false);	//����Ԥ��
						jsonObj.put("IsOverdueWarningLong", false);	//����Ԥ��
					}
					
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
					log.debug("exception in TaskAssignServlet-->case 4", e);
				}else{
					log.error("error in TaskAssignServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	// ����һ��ί�е������������Ϣ������TaskManage/ComSheetInspectByCode.jsp-ͨ��ί�е��ź��������ί�е�����   �м�����Ŀ��ѡ�񡪡�ComboBox��
			JSONArray jsonArray5 = new JSONArray();
			try {
				String CommissionId = req.getParameter("CommissionId");	//ί�е�Id
				
				if(CommissionId == null || CommissionId.trim().length() == 0){
					throw new Exception("ί�е������ڣ�");
				}
				List<TaskAssign> taskRetList = tAssignMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", Integer.parseInt(CommissionId), "="), new KeyValueWithOperator("status", 1, "<>"));
				if(taskRetList != null && taskRetList.size() > 0){
					for(TaskAssign t:taskRetList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("id", t.getId());
						jsonObj.put("text", String.format("%s[�����:%s]", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName() == null)?"":t.getAppStdNameProTeam().getProjectName(), t.getSysUserByAlloteeId().getName()));
						jsonObj.put("AppStdNameProTeamId", t.getAppStdNameProTeam()==null?"-1":t.getAppStdNameProTeam().getId());
						jsonObj.put("AppStdNameProTeamName", (t.getAppStdNameProTeam()==null || t.getAppStdNameProTeam().getProjectName() == null)?"":t.getAppStdNameProTeam().getProjectName());
						jsonObj.put("AlloteeId", t.getSysUserByAlloteeId().getId());
						jsonObj.put("AlloteeName", t.getSysUserByAlloteeId().getName());
						
						jsonArray5.put(jsonObj);
					}
				}
			} catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 5", e);
				}else{
					log.error("error in TaskAssignServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray5.toString());
			}
			break;
		case 6:	//��ѯһ��������Ŀ�ļ����Ա
			JSONArray jsonArray6 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����(0:��׼���ƣ�1����������)
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//������׼���Ƶ�Id
				String AlloteeRule = req.getParameter("AlloteeRule");	//�������
				
				if(SpeciesType.equals("2")){	//��������
					SpeciesType = "0";
				}
				if(AlloteeRule == null || AlloteeRule.trim().length() == 0){
					AlloteeRule = String.format("%s", SystemCfgUtil.getTaskAllotRule());
				}
				List<Integer> typeList = new ArrayList<Integer>();
				typeList.add(FlagUtil.QualificationType.Type_Jianding);
				typeList.add(FlagUtil.QualificationType.Type_Jianyan);
				typeList.add(FlagUtil.QualificationType.Type_Jiaozhun);
				
				Calendar c = Calendar.getInstance();
				c.set(Calendar.DAY_OF_YEAR, 1);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				Timestamp tYear = new Timestamp(c.getTimeInMillis()); //����Ŀ�ʼʱ��
				
				List<Object[]> retList = tAssignMgr.getInspectQualifyUsersByRule(Integer.parseInt(ApplianceSpeciesId), Integer.parseInt(SpeciesType), typeList, Integer.parseInt(AlloteeRule), tYear);
				for(Object []obj : retList){
					JSONObject objTemp = new JSONObject();
					objTemp.put("id", (Integer)obj[0]);
					objTemp.put("name", obj[1].toString());
					jsonArray6.put(objTemp);
				}
				/*if(jsonArray6.length()==0){
					String queryStr = "with appspec as( " + 
									"select a.Id, a.ParentId, 1 as type from" +  SystemCfgUtil.DBPrexName + "ApplianceSpecies as a where a.Id = ? " + 
									"union all " + 
									"select b.Id, b.ParentId, 1 as type from" + SystemCfgUtil.DBPrexName + "ApplianceSpecies as b, appspec where b.ParentId = appspec.Id and appspec.type = 1 " +  
									"union all " + 
									"select c.Id, c.SpeciesId, 0 as type from" + SystemCfgUtil.DBPrexName + "ApplianceStandardName as c, appspec where c.SpeciesId = appspec.Id and appspec.type = 1 " + 
									")" +
									"select distinct s.Id, s.Name from appspec," + SystemCfgUtil.DBPrexName + "Qualification as a," + SystemCfgUtil.DBPrexName + "SysUser as s where (a.Type = 11 or a.Type = 12 or a.Type = 13 or a.Type = 16) and a.UserId = s.Id and a.AuthItemId = appspec.Id and a.AuthItemType = appspec.type";
					QualificationManager quaMgr = new QualificationManager();
					List<Object[]> retList1 = quaMgr.findBySQL(queryStr, Integer.valueOf(ApplianceSpeciesId));
					for(Object []obj : retList1){
						JSONObject objTemp = new JSONObject();
						objTemp.put("id", (Integer)obj[0]);
						objTemp.put("name", obj[1].toString());
						jsonArray6.put(objTemp);
					}
				}
					*/
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 6", e);
				}else{
					log.error("error in TaskAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray6.toString());
			}
			break;
		case 7:	//��ѯһ��������Ŀ�ĺ�����Ա���к������ʣ�
			JSONArray jsonArray7 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����(0:��׼���ƣ�1����������)
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//������׼���Ƶ�Id
				if(SpeciesType.equals("2")){	//��������
					SpeciesType = "0";
				}				
				List<Object[]> retList = new QualificationManager().getVerifyOrAuthorizeQualifyUsers(Integer.parseInt(ApplianceSpeciesId), Integer.parseInt(SpeciesType), FlagUtil.QualificationType.Type_Heyan);
				for(Object []obj : retList){
					JSONObject objTemp = new JSONObject();
					objTemp.put("id", (Integer)obj[0]);
					objTemp.put("name", obj[1].toString());
					jsonArray7.put(objTemp);
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 6", e);
				}else{
					log.error("error in TaskAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray7.toString());
			}
			break;
		case 8:	//��ѯһ��������Ŀ��ǩ����Ա����ǩ�����ʣ�
			JSONArray jsonArray8 = new JSONArray();
			try{
				String SpeciesType = req.getParameter("SpeciesType");	//����(0:��׼���ƣ�1����������)
				String ApplianceSpeciesId = req.getParameter("ApplianceSpeciesId");	//������׼���Ƶ�Id
				if(SpeciesType.equals("2")){	//��������
					SpeciesType = "0";
				}				
				List<Object[]> retList = new QualificationManager().getVerifyOrAuthorizeQualifyUsers(Integer.parseInt(ApplianceSpeciesId), Integer.parseInt(SpeciesType), FlagUtil.QualificationType.Type_Qianzi);
				for(Object []obj : retList){
					JSONObject objTemp = new JSONObject();
					objTemp.put("id", (Integer)obj[0]);
					objTemp.put("name", obj[1].toString());
					jsonArray8.put(objTemp);
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 6", e);
				}else{
					log.error("error in TaskAssignServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray8.toString());
			}
			break;
/*		case 9:	//���ù�����ע��һ���˵�������䣨ֻ������ķ����߲���ע������case 1 �����𣩡����Ѳ��õĹ���
			JSONObject retJSON9 = new JSONObject();
			try {
				String TaskId = req.getParameter("TaskId");	//ί�е�Id
				
				if(TaskId == null || TaskId.trim().length() == 0){
					throw new Exception("���񲻴��ڣ�");
				}
				TaskAssign t = tAssignMgr.findById(Integer.parseInt(TaskId));
				if(t != null){
					if(t.getCommissionSheet().getStatus() == 10){
						throw new Exception("��ί�е���ע��������ע���ü�������");
					}
					if(t.getCommissionSheet().getStatus() == 3 ||
							t.getCommissionSheet().getStatus() == 4 ||
							t.getCommissionSheet().getStatus() == 9){
						throw new Exception("��ί�е����깤ȷ�ϣ�����ע���ü�������");
					}
					SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
					if(t.getSysUserByAssignerId() == null || !loginUser.getId().equals(t.getSysUserByAssignerId().getId())){
						throw new Exception("�����Ǹü�������ķ����ˣ�����ע���ü�������");
					}
					t.setStatus(1);	//ע��
					if(tAssignMgr.update(t)){
						retJSON9.put("IsOK", true);
						InformationManager.AddInformation(FlagUtil.SmsAndInfomationType.Url_TaskReceived, String.format("����������ȡ����ί�е��ţ�%s", t.getCommissionSheet().getCode()), t.getSysUserByAlloteeId(), FlagUtil.SmsAndInfomationType.Type_TaskCancel);	//��ʱ��Ϣ
						return;
					}else{
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}
				}
				throw new Exception("�����񲻴��ڣ�");
			} catch (Exception e){
				try {
					retJSON9.put("IsOK", false);
					retJSON9.put("msg", String.format("ע������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 9", e);
				}else{
					log.error("error in TaskAssignServlet-->case 9", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON9.toString());
			}
			break;
		case 10:	//�ֶ�������ã�ֻ�ܷ��� ����У��Ա�������򡮷�����Ա������ �ļ�¼�ķ�����Ϣ�������Ѳ��õĹ���
			//¼��ķ�����Ϣ��������ΪС�� ��ԭʼ��¼��ת���ѡ� ���ܺͣ���Ϊ���������ί�е�-��������û��ԭʼ��¼����Ҫ�շѣ���
			JSONObject retJSON10 = new JSONObject();
			try {
				String TaskId = req.getParameter("TaskId");	//ί�е�Id
				String TestFee = req.getParameter("TestFee");	//����
				String RepairFee = req.getParameter("RepairFee");	//�����
				String MaterialFee = req.getParameter("MaterialFee");	//���Ϸ�
				String CarFee = req.getParameter("CarFee");	//��ͨ��
				String DebugFee = req.getParameter("DebugFee");	//���Է�
				String OtherFee = req.getParameter("OtherFee");	//������
				if(TaskId == null || TaskId.trim().length() == 0){
					throw new Exception("�������񲻴��ڣ�");
				}
				if(TestFee == null || TestFee.length() == 0){
					TestFee = "0.0";
				}
				if(RepairFee == null || RepairFee.length() == 0){
					RepairFee = "0.0";
				}
				if(MaterialFee == null || MaterialFee.length() == 0){
					MaterialFee = "0.0";
				}
				if(CarFee == null || CarFee.length() == 0){
					CarFee = "0.0";
				}
				if(DebugFee == null || DebugFee.length() == 0){
					DebugFee = "0.0";
				}
				if(OtherFee == null || OtherFee.length() == 0){
					OtherFee = "0.0";
				}
				
				TaskAssign t = tAssignMgr.findById(Integer.parseInt(TaskId));
				if(t != null){
					if(t.getCommissionSheet().getStatus() == 10){
						throw new Exception("��ί�е���ע�������ܽ��з��÷��䣡");
					}
					if(t.getCommissionSheet().getStatus() == 3 ||
							t.getCommissionSheet().getStatus() == 4 ||
							t.getCommissionSheet().getStatus() == 9){
						throw new Exception("��ί�е����깤ȷ�ϣ����ܽ��з��÷��䣡");
					}
					
					SysUser loginUser = (SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
					if(!loginUser.getId().equals(t.getSysUserByAlloteeId().getId()) && 
							(t.getSysUserByAssignerId() == null || !loginUser.getId().equals(t.getSysUserByAssignerId().getId()))){
						throw new Exception("�����Ǹü�¼�� '������'��'������'�����ܶ�����з��÷��䣡");
					}
					t.setTestFee(Double.parseDouble(TestFee));
					t.setRepairFee(Double.parseDouble(RepairFee));
					t.setMaterialFee(Double.parseDouble(MaterialFee));
					t.setCarFee(Double.parseDouble(CarFee));
					t.setDebugFee(Double.parseDouble(DebugFee));
					t.setOtherFee(Double.parseDouble(OtherFee));
					t.setTotalFee(t.getTestFee() + t.getRepairFee() + t.getMaterialFee() + t.getCarFee() + t.getDebugFee() + t.getOtherFee());
					t.setSysUserByLastFeeAssignerId(loginUser);
					t.setLastFeeAssignTime(new Timestamp(System.currentTimeMillis()));
					if(tAssignMgr.update(t)){
						retJSON10.put("IsOK", true);
						return;
					}else{
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}
				}
				throw new Exception("�����񲻴��ڣ�");
			} catch (Exception e){
				try {
					retJSON10.put("IsOK", false);
					retJSON10.put("msg", String.format("���÷���ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in TaskAssignServlet-->case 10", e);
				}else{
					log.error("error in TaskAssignServlet-->case 10", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON10.toString());
			}
			break;
*/
		}
	}

}
