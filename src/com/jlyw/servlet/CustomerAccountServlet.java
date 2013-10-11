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
import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerAccount;
import com.jlyw.hibernate.DetailList;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.CertificateFeeAssignManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.CustomerAccountManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.DetailListManager;
import com.jlyw.util.DateTimeFormatUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;

public class CustomerAccountServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(CustomerAccountServlet.class);
	private static String ClassName = "CustomerAccountServlet";
	private static Object MutexObjectOfNewDetailList = new Object();		//���ڻ�������½��嵥
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method")); // �ж�����ķ�������
		CustomerAccountManager cusAccountMgr = new CustomerAccountManager();

		switch (method) {
		case 0: // ����һ��ί�е�λ���˻���Ϣ
			JSONObject retJSON = new JSONObject();
			try {
				String cusNameStr = req.getParameter("queryname");
				int page = 0; // ��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 10; // ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());
				String cusName = null;
				if (cusNameStr != null && cusNameStr.trim().length() > 0) {
					cusName = new String(URLDecoder.decode(cusNameStr, "UTF-8")); // ���URL����������������
				}
				List<CustomerAccount> retList = cusAccountMgr
						.findPagedAllBySort(page, rows, "handleTime", false,
								new KeyValueWithOperator("customer.name",
										cusName, "="));

				if (retList != null && retList.size() > 0) {
					JSONArray jsonArray = new JSONArray();
					for (CustomerAccount w : retList) {
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Id", w.getId());
						jsonObj.put("CustomerId", w.getCustomer().getId()); // ί�е�λId
						jsonObj.put("CustomerName", w.getCustomer().getName()); // ί�е�λ����
						jsonObj.put("HandleType", w.getHandleType()); // ��������
						jsonObj.put("HandleTime",
								DateTimeFormatUtil.DateTimeFormat.format(w.getHandleTime())); // ����ʱ��
						jsonObj.put("HandlerName",
								w.getHandlerName() == null ? "" : w
										.getHandlerName());// ����������
						jsonObj.put("HandlerId", w.getSysUser() == null ? ""
								: w.getSysUser().getId());// ������ID
						jsonObj.put("Remark", w.getRemark() == null ? "" : w
								.getRemark()); // ��ע
						jsonObj.put("Amount", w.getAmount()); // ���

						jsonArray.put(jsonObj);
					}

					retJSON.put("total", cusAccountMgr
							.getTotalCount(new KeyValueWithOperator(
									"customer.name", cusName, "=")));
					retJSON.put("rows", jsonArray);
				} else {
					throw new Exception("");
				}
			} catch (Exception e) {
				try {
					retJSON.put("total", 0);
					retJSON.put("rows", new JSONArray());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			} finally {
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1: // ��ֵ
			JSONObject retObj = new JSONObject();
			try {

				/*** ȡֵ **/
				String CustomerName = req.getParameter("Name11"); // ��λ����
				CustomerManager cusmag = new CustomerManager();
				Customer customer = cusmag.findByVarProperty(
						new KeyValueWithOperator("name", CustomerName, "="))
						.get(0);

				double Amount = Float
						.valueOf(req.getParameter("Amount").trim()); // ��ֵ���
				String Remark = req.getParameter("Remark"); // ��ע
				Timestamp today = new Timestamp(System.currentTimeMillis());// ��ǰʱ��

				boolean yes1 = false;
				SysUser Handler = (SysUser) req.getSession().getAttribute(
						"LOGIN_USER");// ��ǰ�����û�
				if (Handler != null) {

					int HandlerId = Handler.getId();
					String HandlerName = Handler.getName();

					/*** ����ί�е�λ�˻���ȡ��ϸ���� **/
					CustomerAccount cusAccount = new CustomerAccount();// ί�е�λ�˻���ȡ��ϸ
					cusAccount.setCustomer(customer);
					cusAccount.setHandleType(0);
					cusAccount.setHandleTime(today);
					cusAccount.setHandlerName(HandlerName);
					cusAccount.setSysUser(Handler);
					cusAccount.setAmount(Amount);
					cusAccount.setRemark(Remark);
					yes1 = cusAccountMgr.save(cusAccount);

				} else {
					CustomerAccount cusAccount = new CustomerAccount();// ί�е�λ�˻���ȡ��ϸ
					cusAccount.setCustomer(customer);
					cusAccount.setHandleType(0);
					cusAccount.setHandleTime(today);
					cusAccount.setAmount(Amount);
					cusAccount.setRemark(Remark);
					yes1 = cusAccountMgr.save(cusAccount);
				}

				/*** �޸�Customer �������Ϣ ***/
				double balance = customer.getBalance();
				balance = balance + Amount;
				customer.setBalance(balance);

				boolean yes2 = cusmag.update(customer);

				boolean yes;
				if (yes1 == true && yes2 == true)
					yes = true;
				else
					yes = false;
				retObj.put("IsOK", yes);
				retObj.put("msg", yes ? "�����ɹ���" : "����ʧ�ܣ�");
				retObj.put("balance", balance);

			} catch (NumberFormatException e) { // �ַ���תInteger����
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("����ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		

			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("����ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2: // ��ѯ�˻����
			JSONObject retObj1 = new JSONObject();
			try {

				/*** ȡֵ **/
				String CustomerName = req.getParameter("Name22"); // ��λ����
				CustomerManager cusmag1 = new CustomerManager();
				Customer customer1 = cusmag1.findByVarProperty(
						new KeyValueWithOperator("name", CustomerName, "="))
						.get(0);

				double balance1 = customer1.getBalance();
			
				retObj1.put("balance", balance1);

			} catch (NullPointerException e) { // �ַ���תInteger����
				log.error(String.format("error in %s", ClassName), e);
				
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
					
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 3: // ���ˣ��Ѹ������¼��ί�е������Ҵ��˻��п��ܷ��ã�
			JSONObject retObj2 = new JSONObject();
			try {

				/*** �Ѹ������¼��ί�е� **/
				String InvoiceCode = req.getParameter("InvoiceCode");// ��Ʊ��
				String CashPaidStr = req.getParameter("CashPaid"); //�ֽ�֧������
				String ChequePaidStr = req.getParameter("ChequePaid"); //֧Ʊ֧������
				String AccountPaidStr = req.getParameter("AcountFee");  //�˻�֧������
				String CommissionSheetsStr = req.getParameter("CommissionSheets");//ί�е��б�
				JSONArray CommissionSheets = new JSONArray(CommissionSheetsStr);
				double CashPaid=0, ChequePaid=0, AccountPaid=0;
				if(CashPaidStr!=null&&CashPaidStr.length()>0){
					CashPaid=Double.valueOf(CashPaidStr);
				}
				if(ChequePaidStr!=null&&ChequePaidStr.length()>0){
					ChequePaid=Double.valueOf(ChequePaidStr);
				}
				if(AccountPaidStr!=null&&AccountPaidStr.length()>0){
					AccountPaid=Double.valueOf(AccountPaidStr);
				}
				
				if (req.getParameter("TotalFee") == null)
					throw new Exception("�ܷ��ò���Ϊ�գ�");
				double TotalFee = Double.valueOf(req.getParameter("TotalFee")); //
				
				CustomerManager cusmag = new CustomerManager();
				int CustomerId = CommissionSheets.getJSONObject(0).getInt("CustomerId");
				Customer cus = cusmag.findById(CustomerId);
				if(AccountPaid > cus.getBalance()){
					throw new Exception("�õ�λ�˻����㣬�޷�֧����");
				}
				
				Timestamp today = new Timestamp(System.currentTimeMillis());// ��ǰʱ��
				SysUser Handler = (SysUser) req.getSession().getAttribute("LOGIN_USER");// ��ǰ�����û�
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				DetailListManager deListMgr=new DetailListManager();
				synchronized(MutexObjectOfNewDetailList) {
					//begin-��ѯ���嵥�����嵥��
					String queryCode = String.format("%d", today.getYear()+1900);		
					String queryString = "select max(model.code) from DetailList as model where model.code like ?";
					List<Object> retList = deListMgr.findByHQL(queryString, queryCode+"%");
					Integer codeBeginInt = Integer.parseInt("0000001");	//ί�е����
					if(retList.size() > 0 && retList.get(0) != null){
						codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4)) + 1;
					}
					//end-��ѯ���嵥�����嵥��
					String code=queryCode+String.format("%07d", codeBeginInt);
					DetailList detailList = new DetailList();
					detailList.setCode(code);
					detailList.setTotalFee(TotalFee);
					detailList.setPaidFee(TotalFee);
					detailList.setCashPaid(CashPaid);
					detailList.setChequePaid(ChequePaid);
					detailList.setAccountPaid(AccountPaid);
					detailList.setSysUser(Handler);
					detailList.setLastEditTime(today);
					detailList.setStatus(2);
					detailList.setInvoiceCode(InvoiceCode);
					detailList.setVersion(1);
					
					List<CommissionSheet> updateList = new ArrayList<CommissionSheet>();
					List<DetailList> oldDetailListList = new ArrayList<DetailList>();
					for(int i = 0; i<CommissionSheets.length(); i++){
						JSONObject cSheetObj = CommissionSheets.getJSONObject(i);
						CommissionSheet cSheet = cSheetMgr.findById(cSheetObj.getInt("Id"));
						if(cSheet.getStatus()==FlagUtil.CommissionSheetStatus.Status_YiJieZhang){
							throw new Exception("ί�е�" + cSheet.getCode() + "�ѽ��ˡ�");
						}
						String queryStr = CertificateFeeAssignManager.queryStringAllFeeByCommissionSheetId;
						List<Object[]> FList=new CertificateFeeAssignManager().findByHQL(queryStr, cSheet.getId());
						double nowTotalFee;
						if(FList.isEmpty()){
							nowTotalFee = 0.0;
					    }else{
							nowTotalFee = (Double)FList.get(0)[6]==null?0.0:(Double)FList.get(0)[6];
						   }
						if(nowTotalFee!=Double.parseDouble(cSheetObj.getString("TotalFee"))){
							throw new Exception("ί�е�" + cSheet.getCode() + "�۸����ı䣨�ּۣ�" + (Double)FList.get(0)[6] + "Ԫ������������Ӹ�ί�е����н��ˡ�");
						}
						cSheet.setTestFee(Double.parseDouble(cSheetObj.getString("TestFee")));
						cSheet.setRepairFee(Double.parseDouble(cSheetObj.getString("RepairFee")));
						cSheet.setMaterialFee(Double.parseDouble(cSheetObj.getString("MaterialFee")));
						cSheet.setCarFee(Double.parseDouble(cSheetObj.getString("CarFee")));
						cSheet.setDebugFee(Double.parseDouble(cSheetObj.getString("DebugFee")));
						cSheet.setOtherFee(Double.parseDouble(cSheetObj.getString("OtherFee")));
						cSheet.setTotalFee(Double.parseDouble(cSheetObj.getString("TotalFee")));
						cSheet.setStatus(FlagUtil.CommissionSheetStatus.Status_YiJieZhang);
						int restComCount = cSheetMgr.getTotalCount(new KeyValueWithOperator("detailListCode", cSheet.getDetailListCode()==null?"":cSheet.getDetailListCode(), "="));
						if(restComCount==1){
							DetailList oldDetailList = deListMgr.findByVarProperty(new KeyValueWithOperator("code", cSheet.getDetailListCode(), "=")).get(0);
							oldDetailList.setStatus(1);
							oldDetailListList.add(oldDetailList);
						}
						cSheet.setCheckOutStaffId(Handler.getId());
						cSheet.setCheckOutDate(today);
						cSheet.setDetailListCode(detailList.getCode());
						updateList.add(cSheet);
					}											
							
					/*** �޸�Customer �������Ϣ ***/							
					cus.setBalance(cus.getBalance()-AccountPaid);
					
					/*** ����ί�е�λ�˻���ȡ��ϸ���� **/
					CustomerAccount cusAccount = null;// ί�е�λ�˻���ȡ��ϸ
					if(AccountPaid!=0){
						cusAccount = new CustomerAccount();
						cusAccount.setCustomer(cus);
						cusAccount.setHandleType(1);
						cusAccount.setHandleTime(today);
						if (Handler != null) {
							//int HandlerId = Handler.getId();
							String HandlerName = Handler.getName();
							cusAccount.setHandlerName(HandlerName);
							cusAccount.setSysUser(Handler);
						}
						cusAccount.setAmount(AccountPaid);
					}
					if(cusAccountMgr.saveByBatch(updateList, oldDetailListList, detailList, cus, cusAccount)){
						retObj2.put("IsOK", true);
						retObj2.put("msg", "���˳ɹ���");
						retObj2.put("DetailListId", detailList.getId());
						retObj2.put("DetailListCode", detailList.getCode());
					}
					else{
						retObj2.put("IsOK", false);
						retObj2.put("msg", "����ʧ�ܣ�");
						retObj2.put("DetailListId", "");
						retObj2.put("DetailListCode", "");
					}
				}
			} catch (Exception e) {
				try{
					retObj2.put("IsOK", false);
					retObj2.put("msg", String.format("����ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}
				log.debug(String.format("error in %s", ClassName), e);
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj2.toString());
			break;
		}
	}
}
