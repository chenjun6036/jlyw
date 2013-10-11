package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import com.jlyw.hibernate.ApplianceSpecies;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.Discount;
import com.jlyw.hibernate.DiscountComSheet;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.ApplianceSpeciesManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DiscountComSheetManager;
import com.jlyw.manager.DiscountManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class DiscountServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(DiscountServlet.class);
	private static String ClassName = "DiscountServlet";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		DiscountManager discountMgr = new DiscountManager();
		DiscountComSheetManager discountcomSheetMgr=new DiscountComSheetManager();
		switch (method) {
		case 0: // ���ۿ�����ID�����ۿ�������Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String DiscountId = req.getParameter("DiscountId");	//�ۿ�����Id
				
				Discount o = discountMgr.findById(Integer.parseInt(DiscountId));
				if(o != null ){
					JSONArray jsonArray = new JSONArray();
					
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", o.getId());
						jsonObj.put("CustomerName", o.getCustomer()==null?"":o.getCustomer().getName());//  
						jsonObj.put("Contector", o.getContector()==null?"":o.getContector());//  ί�з�������
						jsonObj.put("ContectorTel", o.getContectorTel()==null?"":o.getContectorTel());//��������ϵ�绰
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//������
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//����ʱ��
						jsonObj.put("Reason", o.getReason().getReason());	//ԭ��
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//���ڰ�����
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//������
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//����ʱ��
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//����ע��Ϣ
						
						jsonArray.put(jsonObj);
					
					
					retJSON.put("total", 1);
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
					log.debug("exception in DiscountServlet-->case 0", e);
				}else{
					log.error("error in DiscountServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1:	//�ۿ�����������
			JSONObject retJSON1 = new JSONObject();
			try {
				String DiscountId = req.getParameter("DiscountId");	//�ۿ�����Id
				String ExecutorResult = req.getParameter("ExecutorResult");	//��������0��ͨ����1ͨ��
				String ExecuteMsg = req.getParameter("ExecuteMsg");	//����ı�ע��Ϣ
				//System.out.println(DiscountId);
				Discount discount = discountMgr.findById(Integer.parseInt(DiscountId));
				if(discount != null){
					if(discount.getExecuteTime() != null){
						throw new Exception("���ۿ������Ѿ������������ظ�������");
					}
					discount.setExecuteMsg(ExecuteMsg);
					discount.setExecuteResult(Integer.parseInt(ExecutorResult)>0?true:false);
					discount.setExecuteTime(new Timestamp(System.currentTimeMillis()));
					discount.setSysUserByExecutorId((SysUser)req.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser));	//������
					if(discount.getExecuteResult()){	//����ͨ����������д�뵽ί�е���֤����÷������CertificateFeeAssign�У�
						DiscountComSheetManager disComMgr = new DiscountComSheetManager();
						List<DiscountComSheet> disCountList = disComMgr.findByVarProperty(new KeyValueWithOperator("discount.id",Integer.parseInt(DiscountId),"="),new KeyValueWithOperator("commissionSheet.status",FlagUtil.CommissionSheetStatus.Status_YiWanGong,"="));
						if(disCountList==null||disCountList.size()==0)
							throw new Exception("��ί�е����ǡ����깤����");						
						discountMgr.discountExecute(discount);
					}else{	//������ͨ��
						discountMgr.update(discount);
					}
					retJSON1.put("IsOK", true);
				}else{
					throw new Exception("���ۿ����뵥�����ڣ�");
				}
			} catch (Exception e){
				try {
					retJSON1.put("IsOK", false);
					retJSON1.put("msg", String.format("�ۿ�����������ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in DiscountServlet-->case 1", e);
				}else{
					log.error("error in DiscountServlet-->case1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON1.toString());
			}
			break;
		case 2:		//�ۿ�����
			JSONObject retJSON2 = new JSONObject();
			try {
				String comSheets = req.getParameter("comSheets");
				String Reason = req.getParameter("Reasons");	//�ۿ�ԭ��
				String Contector = req.getParameter("Contector");	//ί�з�������
				String ContectorTel = req.getParameter("ContectorTel");	//
				List<DiscountComSheet> discountComSheetList=new ArrayList<DiscountComSheet>();//�ۿ�-ί�е�
				JSONArray JsonArray = new JSONArray(comSheets);
				Integer cusId = null;
				Timestamp today = new Timestamp(System.currentTimeMillis());
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				for(int i=0; i<JsonArray.length(); i++){//�����ۿ�-ί�е���List
					JSONObject jsonObj = JsonArray.getJSONObject(i);
					DiscountComSheet discountComSheet=new DiscountComSheet();
					CommissionSheet com = cSheetMgr.findById(Integer.parseInt(jsonObj.getString("Id")));
					int iRet = cSheetMgr.getTotalCountByHQL("select count(*) "+CertificateFeeAssignManager.queryString_CertificateFeeAssignByCommissionSheetId, com.getId());
					if(iRet == 0){
						throw new Exception(String.format("ί�е�%sû��¼���κη��ã�����ִ���ۿ۲�����", com.getCode()));
					}
					discountComSheet.setCommissionSheet(com);
					cusId = com.getCustomerId();
					discountComSheet.setOldTestFee(Double.parseDouble(jsonObj.getString("OldTestFee").trim()));//
					discountComSheet.setTestFee(Double.parseDouble(jsonObj.getString("TestFee").trim()));
					discountComSheet.setOldRepairFee(Double.parseDouble(jsonObj.getString("OldRepairFee").trim()));//
					discountComSheet.setRepairFee(Double.parseDouble(jsonObj.getString("RepairFee").trim()));
					discountComSheet.setOldMaterialFee(Double.parseDouble(jsonObj.getString("OldMaterialFee").trim()));//
					discountComSheet.setMaterialFee(Double.parseDouble(jsonObj.getString("MaterialFee").trim()));
					discountComSheet.setOldCarFee(Double.parseDouble(jsonObj.getString("OldCarFee").trim()));//
					discountComSheet.setCarFee(Double.parseDouble(jsonObj.getString("CarFee").trim()));
					discountComSheet.setOldDebugFee(Double.parseDouble(jsonObj.getString("OldDebugFee").trim()));//
					discountComSheet.setDebugFee(Double.parseDouble(jsonObj.getString("DebugFee").trim()));
					discountComSheet.setOldOtherFee(Double.parseDouble(jsonObj.getString("OldOtherFee").trim()));//
					discountComSheet.setOtherFee(Double.parseDouble(jsonObj.getString("OtherFee").trim()));
					discountComSheet.setOldTotalFee(Double.parseDouble(jsonObj.getString("OldTotalFee").trim()));//
					discountComSheet.setTotalFee(Double.parseDouble(jsonObj.getString("TotalFee").trim()));
					
					discountComSheetList.add(discountComSheet);
				}			
				//���ۿ�����
				Discount r = new Discount();
				r.setSysUserByRequesterId((SysUser)req.getSession().getAttribute("LOGIN_USER"));	//������
				r.setApplyTime(today);	//����ʱ��
				r.setContector(Contector);
				r.setContectorTel(ContectorTel);
				Customer cus=(new CustomerManager()).findById(cusId);	
				r.setCustomer(cus);
				//���ۿ�ԭ��
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", FlagUtil.ReasonType.Type_Discount, "="));//�����ۿ�ԭ��
				if(rList!=null&&rList.size() > 0){	//����ԭ��
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					r.setReason(reason);	//�ۿ�ԭ��
				}else{	//�½�ԭ��
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(FlagUtil.ReasonType.Type_Discount);	//�ۿ�
					rMgr.save(reason);
					r.setReason(reason);	//�ۿ�ԭ��
				}

				if((new DiscountComSheetManager()).saveByBatch(discountComSheetList,r)){
					retJSON2.put("IsOK", true);
				}else{
					throw new Exception("�������ݿ�ʧ�ܣ�");
				}
				
			} catch (Exception e){
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("�ۿ������ύʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in DiscountServlet-->case 2", e);
				}else{
					log.error("error in DiscountServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //��ѯ�ۿ����������б�
			JSONObject retJSON3 = new JSONObject();
			try {
				JSONArray jsonArray = new JSONArray();
				int total=0;
				int page = 0;	//��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());

				String CustomerName  = req.getParameter("CustomerName");					
				String Code = req.getParameter("Code");	//ί�е���
				//String FeeCode = req.getParameter("FeeCode");	//�����嵥��
				String DateFrom = req.getParameter("DateFrom");
				String DateEnd = req.getParameter("DateEnd");
				String Status = req.getParameter("Status");
				List<Object> keys = new ArrayList<Object>();
				if(Status==null||Status.length()==0){
					Status="";
				}
				
				String QueryHQL = " from Discount as d where  d.id not in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.status=10) ";
				if(CustomerName != null && CustomerName.length() > 0){
					String cusName =  URLDecoder.decode(CustomerName.trim(), "UTF-8"); //���jquery����������������
					QueryHQL = QueryHQL+" and d.customer.name = ? ";
					keys.add(cusName);
				}
				if(Code != null && Code.length() > 0){
					QueryHQL=QueryHQL + " and d.id in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.code like ?) " ;
					keys.add("%"+Code+"%");
				}
				if(DateFrom != null && DateFrom.length() > 0){
					Timestamp Start = new Timestamp(Date.valueOf(URLDecoder.decode(DateFrom, "utf-8")).getTime());
					QueryHQL=QueryHQL + " and d.id in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.commissionDate >= ?) " ;
					keys.add(Start);
				}
				if(DateEnd != null && DateEnd.length() > 0){
					Timestamp End = new Timestamp(Date.valueOf(URLDecoder.decode(DateEnd, "utf-8")).getTime());
					QueryHQL=QueryHQL + " and d.id in (select dc.discount.id from DiscountComSheet as dc where dc.commissionSheet.commissionDate <= ?) " ;
					keys.add(End);
				}
				if(Status!= null&& Status.length() > 0){
					if(Status.equals("1")){//������
						QueryHQL = QueryHQL+"and d.executeTime is not null ";
					}
					if(Status.equals("0")){//δ����
						QueryHQL = QueryHQL+"and d.executeTime is null ";
					}
				}
				
				List<Discount> tRetList = discountcomSheetMgr.findPageAllByHQL(QueryHQL+"order by d.applyTime desc", page, rows, keys);
				total = discountcomSheetMgr.getTotalCountByHQL("select count(*) "+QueryHQL, keys);
				if(tRetList!=null){
					
					for(Discount o : tRetList){		
						JSONObject jsonObj= new JSONObject();
						jsonObj.put("Id", o.getId());
						jsonObj.put("CustomerName", o.getCustomer().getName());
						jsonObj.put("Contector", o.getContector()==null?"":o.getContector());//  ί�з�������
						jsonObj.put("ContectorTel", o.getContectorTel()==null?"":o.getContectorTel());//��������ϵ�绰
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//������
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//����ʱ��
						jsonObj.put("Reason", o.getReason().getReason());	//ԭ��
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//������
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//������
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//����ʱ��
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//����ע��Ϣ
						
						jsonArray.put(jsonObj);
				
					}
				}
				retJSON3.put("total", total);
				retJSON3.put("rows", jsonArray);

			} catch (Exception e){
				e.printStackTrace();
				try {
					retJSON3.put("total", 0);
					retJSON3.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in DiscountServlet-->case 3", e);
				}else{
					log.error("error in DiscountServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON3.toString());
			}
			break;
		case 4: // �������嵥ID���ҵ��ۿ�������Ϣ
			JSONObject retJSON4 = new JSONObject();
			try {
				if(req.getParameter("DetailListId") == null || req.getParameter("DetailListId").length() == 0){
					throw new Exception("�����嵥IdΪ�գ�");
				}
				int DetailListId =Integer.parseInt(req.getParameter("DetailListId"));	//�����嵥Id
				int total=0;
				List<Discount> retList = discountMgr.findByVarProperty(new KeyValueWithOperator("detailList.id", DetailListId, "="));
				JSONArray jsonArray = new JSONArray();
				if(retList != null && retList.size() > 0){
					total=discountMgr.getTotalCount(new KeyValueWithOperator("detailList.id", DetailListId, "="));
					for(Discount o : retList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("DiscountId", o.getId());
						
						jsonObj.put("Contector", o.getContector()==null?"":o.getContector());//  ί�з�������
						jsonObj.put("ContectorTel", o.getContectorTel()==null?"":o.getContectorTel());//��������ϵ�绰
						jsonObj.put("RequesterId", o.getSysUserByRequesterId().getId());	//������
						jsonObj.put("RequesterName", o.getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getApplyTime()));	//����ʱ��
						jsonObj.put("Reason", o.getReason().getReason());	//ԭ��
						jsonObj.put("ExecutorId", o.getSysUserByExecutorId()==null?null:o.getSysUserByExecutorId().getId());	//���ڰ�����
						jsonObj.put("ExecutorName", o.getSysUserByExecutorId()==null?"":o.getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getExecuteResult()==null?-1:(o.getExecuteResult()?1:0));	//������
						jsonObj.put("ExecuteTime", o.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getExecuteTime()));	//����ʱ��
						jsonObj.put("ExecuteMsg", o.getExecuteMsg()==null?"":o.getExecuteMsg());	//����ע��Ϣ
						
						jsonArray.put(jsonObj);
						
					}
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
					log.debug("exception in DiscountServlet-->case 4", e);
				}else{
					log.error("error in DiscountServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5://����ί�е��Ų�ѯ�ۿ���Ϣ
			JSONObject retJSON5 = new JSONObject();
			try {
				if(req.getParameter("CommissionId") == null || req.getParameter("CommissionId").length() == 0){
					throw new Exception("ί�е�IdΪ�գ�");
				}
				int CommissionId =Integer.parseInt(req.getParameter("CommissionId"));	//ί�е�Id
				int total=0;
				ApplianceStandardNameManager appStandMgr=new ApplianceStandardNameManager();
				ApplianceSpeciesManager appSpeMgr=new ApplianceSpeciesManager();
				List<DiscountComSheet> retList = discountcomSheetMgr.findByVarProperty(new KeyValueWithOperator("commissionSheet.id", CommissionId, "="));
				total = discountcomSheetMgr.getTotalCount(new KeyValueWithOperator("commissionSheet.id", CommissionId, "="));
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				JSONArray jsonArray = new JSONArray();
				if(retList != null && retList.size() > 0){
					for(DiscountComSheet o : retList){
						JSONObject jsonObj = new JSONObject();
						
						jsonObj.put("Contactor", o.getDiscount().getContector()==null?"":o.getDiscount().getContector());//  ί�з�������
						jsonObj.put("ContactorTel", o.getDiscount().getContectorTel()==null?"":o.getDiscount().getContectorTel());//��������ϵ�绰
						jsonObj.put("RequesterId", o.getDiscount().getSysUserByRequesterId().getId());	//������
						jsonObj.put("RequesterName", o.getDiscount().getSysUserByRequesterId().getName());
						jsonObj.put("RequesterTime", DateTimeFormatUtil.DateTimeFormat.format(o.getDiscount().getApplyTime()));	//����ʱ��
						jsonObj.put("Reason", o.getDiscount().getReason().getReason());	//ԭ��
						jsonObj.put("ExecutorId", o.getDiscount().getSysUserByExecutorId()==null?null:o.getDiscount().getSysUserByExecutorId().getId());	//���ڰ�����
						jsonObj.put("ExecutorName", o.getDiscount().getSysUserByExecutorId()==null?"":o.getDiscount().getSysUserByExecutorId().getName());
						jsonObj.put("ExecutorResult", o.getDiscount().getExecuteResult()==null?-1:(o.getDiscount().getExecuteResult()?1:0));	//������
						jsonObj.put("ExecuteTime", o.getDiscount().getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(o.getDiscount().getExecuteTime()));	//����ʱ��
						jsonObj.put("ExecuteMsg", o.getDiscount().getExecuteMsg()==null?"":o.getDiscount().getExecuteMsg());	//����ע��Ϣ
						
						CommissionSheet cSheet=o.getCommissionSheet();
						jsonObj.put("Id", cSheet.getId());
						//System.out.println(cSheet.getId());
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Pwd", cSheet.getPwd());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
						if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
							jsonObj.put("SpeciesType", 1);	//���߷�������
							ApplianceSpecies spe = appSpeMgr.findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								jsonObj.put("ApplianceSpeciesName", spe.getName());
							}else{
								continue;
							}
						}else{	//���߱�׼����
							jsonObj.put("SpeciesType", 0);
							ApplianceStandardName stName = appStandMgr.findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								jsonObj.put("ApplianceSpeciesName", stName.getName());
							}else{
								continue;
							}
						}
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�							
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������								
						jsonObj.put("Status", cSheet.getStatus());
						
						jsonObj.put("TestFee", o.getTestFee());
						jsonObj.put("RepairFee", o.getRepairFee());
						jsonObj.put("MaterialFee",o.getMaterialFee());
						jsonObj.put("CarFee", o.getCarFee());
						jsonObj.put("DebugFee", o.getDebugFee());
						jsonObj.put("OtherFee", o.getOtherFee());
						jsonObj.put("TotalFee", o.getTotalFee());
						
						jsonObj.put("OldTestFee", o.getOldTestFee());
						jsonObj.put("OldRepairFee", o.getOldRepairFee());
						jsonObj.put("OldMaterialFee",o.getOldMaterialFee());
						jsonObj.put("OldCarFee", o.getOldCarFee());
						jsonObj.put("OldDebugFee", o.getOldDebugFee());
						jsonObj.put("OldOtherFee", o.getOldOtherFee());
						jsonObj.put("OldTotalFee", o.getOldTotalFee());
						
						jsonArray.put(jsonObj);
						
					}
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
					log.debug("exception in DiscountServlet-->case 5", e);
				}else{
					log.error("error in DiscountServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6://�����ۿ۵�ID��ѯί�е���Ϣ���ۿ�����ʱʹ�ã�
			JSONObject retJSON6 = new JSONObject();
			try {
				if(req.getParameter("DiscountId") == null || req.getParameter("DiscountId").length() == 0){
					throw new Exception("�ۿ�IdΪ�գ�");
				}
				int DiscountId =Integer.parseInt(req.getParameter("DiscountId"));	//ί�е�Id
				
				int total=0;
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				
				condList.add(new KeyValueWithOperator("discount.id", DiscountId, "="));//ѡ��ί�е���δע����	
				
				List<DiscountComSheet> retList = discountcomSheetMgr.findByVarProperty(condList);
				ApplianceStandardNameManager appStandMgr=new ApplianceStandardNameManager();
				ApplianceSpeciesManager appSpeMgr=new ApplianceSpeciesManager();
				total=discountcomSheetMgr.getTotalCount(condList);
				JSONArray jsonArray = new JSONArray();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				double TestFee=0.0,RepairFee=0.0,MaterialFee=0.0,CarFee=0.0,DebugFee=0.0,OtherFee=0.0,TotalFee=0.0;
				double OldTestFee=0.0,OldRepairFee=0.0,OldMaterialFee=0.0,OldCarFee=0.0,OldDebugFee=0.0,OldOtherFee=0.0,OldTotalFee=0.0;
				if(retList != null && retList.size() > 0){
					//System.out.println(retList.size());
					for(DiscountComSheet discom : retList){
						JSONObject jsonObj = new JSONObject();
						CommissionSheet cSheet=discom.getCommissionSheet();
						jsonObj.put("Id", cSheet.getId());
						//System.out.println(cSheet.getId());
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Pwd", cSheet.getPwd());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
						if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
							jsonObj.put("SpeciesType", 1);	//���߷�������
							ApplianceSpecies spe = appSpeMgr.findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								jsonObj.put("ApplianceSpeciesName", spe.getName());
							}else{
								continue;
							}
						}else{	//���߱�׼����
							jsonObj.put("SpeciesType", 0);
							ApplianceStandardName stName = appStandMgr.findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								jsonObj.put("ApplianceSpeciesName", stName.getName());
							}else{
								continue;
							}
						}
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�							
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������								
						jsonObj.put("Status", cSheet.getStatus());
						
						jsonObj.put("TestFee", discom.getTestFee());
						jsonObj.put("RepairFee", discom.getRepairFee());
						jsonObj.put("MaterialFee",discom.getMaterialFee());
						jsonObj.put("CarFee", discom.getCarFee());
						jsonObj.put("DebugFee", discom.getDebugFee());
						jsonObj.put("OtherFee", discom.getOtherFee());
						jsonObj.put("TotalFee", discom.getTotalFee());
						
						jsonObj.put("OldTestFee", discom.getOldTestFee());
						jsonObj.put("OldRepairFee", discom.getOldRepairFee());
						jsonObj.put("OldMaterialFee",discom.getOldMaterialFee());
						jsonObj.put("OldCarFee", discom.getOldCarFee());
						jsonObj.put("OldDebugFee", discom.getOldDebugFee());
						jsonObj.put("OldOtherFee", discom.getOldOtherFee());
						jsonObj.put("OldTotalFee", discom.getOldTotalFee());
						
						TestFee=TestFee+discom.getTestFee();
						RepairFee=RepairFee+discom.getRepairFee();
						MaterialFee=MaterialFee+discom.getMaterialFee();
						CarFee=CarFee+discom.getCarFee();
						DebugFee=DebugFee+discom.getDebugFee();
						OtherFee=OtherFee+discom.getOtherFee();
						TotalFee=TotalFee+discom.getTotalFee();
						
						OldTestFee=OldTestFee+discom.getOldTestFee();
						OldRepairFee=OldRepairFee+discom.getOldRepairFee();
						OldMaterialFee=OldMaterialFee+discom.getOldMaterialFee();
						OldCarFee=OldCarFee+discom.getOldCarFee();
						OldDebugFee=OldDebugFee+discom.getOldDebugFee();
						OldOtherFee=OldOtherFee+discom.getOldOtherFee();
						OldTotalFee=OldTotalFee+discom.getOldTotalFee();
						
						jsonArray.put(jsonObj);
						
					}				
				}

				JSONArray footerArray = new JSONArray();
				JSONObject footerObj = new JSONObject();
				footerObj.put("CustomerName", "�ܼ�");
				footerObj.put("TestFee", TestFee);
				footerObj.put("RepairFee", RepairFee);
				footerObj.put("MaterialFee",MaterialFee);
				footerObj.put("CarFee", CarFee);
				footerObj.put("DebugFee", DebugFee);
				footerObj.put("OtherFee", OtherFee);
				footerObj.put("TotalFee", TotalFee);
				
				footerObj.put("OldTestFee", OldTestFee);
				footerObj.put("OldRepairFee", OldRepairFee);
				footerObj.put("OldMaterialFee",OldMaterialFee);
				footerObj.put("OldCarFee", OldCarFee);
				footerObj.put("OldDebugFee", OldDebugFee);
				footerObj.put("OldOtherFee", OldOtherFee);
				footerObj.put("OldTotalFee", OldTotalFee);
				footerArray.put(footerObj);
				
				retJSON6.put("total", total);
				retJSON6.put("rows", jsonArray);
				retJSON6.put("footer", footerArray);
					
			} catch (Exception e){
				
				try {
					retJSON6.put("total", 0);
					retJSON6.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in DiscountServlet-->case 6", e);
				}else{
					log.error("error in DiscountServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
		case 7://�����ۿ۵�ID��ӡ�ۿ۵�����ӡ�ۿ۵�ʱʹ�ã�
			JSONObject retJSON7 = new JSONObject();
			try {
				if(req.getParameter("discountId") == null || req.getParameter("discountId").length() == 0){
					throw new Exception("�ۿ�IdΪ�գ�");
				}
				int DiscountId =Integer.parseInt(req.getParameter("discountId"));	//ί�е�Id
				
				int total=0;
				List<KeyValueWithOperator> condList = new ArrayList<KeyValueWithOperator>();
				
				condList.add(new KeyValueWithOperator("discount.id", DiscountId, "="));//ѡ��ί�е���δע����	
				
				List<DiscountComSheet> retList = discountcomSheetMgr.findByVarProperty(condList);
				total=discountcomSheetMgr.getTotalCount(condList);
				JSONArray jsonArray = new JSONArray();
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				
				Discount discount=discountMgr.findById(DiscountId);
				String customer = (discount.getCustomer()==null?"":discount.getCustomer().getName());
				String date=(discount.getExecuteTime()==null?"":DateTimeFormatUtil.DateTimeFormat.format(discount.getExecuteTime()));
				if(retList != null && retList.size() > 0){
					
					for(DiscountComSheet discom : retList){
						JSONObject jsonObj = new JSONObject();
						CommissionSheet cSheet=discom.getCommissionSheet();
						jsonObj.put("Id", cSheet.getId());
						
						jsonObj.put("Code", cSheet.getCode());
						jsonObj.put("Pwd", cSheet.getPwd());
						jsonObj.put("CustomerName", cSheet.getCustomerName());
						jsonObj.put("ApplianceSpeciesId", cSheet.getApplianceSpeciesId());	//���߷���Id(�����߱�׼����ID)
						if(cSheet.getSpeciesType()){	//������Ȩ�����ࣩ����
							jsonObj.put("SpeciesType", 1);	//���߷�������
							ApplianceSpecies spe = (new ApplianceSpeciesManager()).findById(cSheet.getApplianceSpeciesId());
							if(spe != null){
								jsonObj.put("ApplianceSpeciesName", spe.getName());
							}else{
								continue;
							}
						}else{	//���߱�׼����
							jsonObj.put("SpeciesType", 0);
							ApplianceStandardName stName = (new ApplianceStandardNameManager()).findById(cSheet.getApplianceSpeciesId());
							if(stName != null){
								jsonObj.put("ApplianceSpeciesName", stName.getName());
							}else{
								continue;
							}
						}
						jsonObj.put("Quantity", cSheet.getQuantity());
						jsonObj.put("ApplianceName", cSheet.getApplianceName()==null?"":cSheet.getApplianceName());	//�������ƣ��������ƣ�							
						jsonObj.put("CommissionDate", sf.format(cSheet.getCommissionDate()));		//ί������								
						jsonObj.put("Status", cSheet.getStatus());
						
						jsonObj.put("TestFee", discom.getTestFee());
						jsonObj.put("RepairFee", discom.getRepairFee());
						jsonObj.put("MaterialFee",discom.getMaterialFee());
						jsonObj.put("CarFee", discom.getCarFee());
						jsonObj.put("DebugFee", discom.getDebugFee());
						jsonObj.put("OtherFee", discom.getOtherFee());
						jsonObj.put("TotalFee", discom.getTotalFee());
						
						jsonObj.put("OldTestFee", discom.getOldTestFee());
						jsonObj.put("OldRepairFee", discom.getOldRepairFee());
						jsonObj.put("OldMaterialFee",discom.getOldMaterialFee());
						jsonObj.put("OldCarFee", discom.getOldCarFee());
						jsonObj.put("OldDebugFee", discom.getOldDebugFee());
						jsonObj.put("OldOtherFee", discom.getOldOtherFee());
						jsonObj.put("OldTotalFee", discom.getOldTotalFee());
						
						
						jsonArray.put(jsonObj);
						
					}				
				}

				
				
				retJSON7.put("total", total);
				retJSON7.put("rows", jsonArray);
				retJSON7.put("CustomerName", customer);
				retJSON7.put("date", date);
			
				retJSON7.put("IsOK", true);
			
				req.getSession().setAttribute("DiscountList", retJSON7);
	
				resp.sendRedirect("/jlyw/FeeManage/DiscountPrint.jsp");
					
			} catch (Exception e){
				
				try{
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("����ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in DiscountServlet-->case 7", e);
				}else{
					log.error("error in DiscountServlet-->case 7", e);
				}
				req.getSession().setAttribute("DiscountList", retJSON7);
				resp.sendRedirect("/jlyw/FeeManage/DiscountPrint.jsp");
			}finally{
				
			}
			break;
		
		}
	}

}
