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
	private static Object MutexObjectOfNewDetailList = new Object();		//用于互斥访问新建清单
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method")); // 判断请求的方法类型
		CustomerAccountManager cusAccountMgr = new CustomerAccountManager();

		switch (method) {
		case 0: // 查找一个委托单位的账户信息
			JSONObject retJSON = new JSONObject();
			try {
				String cusNameStr = req.getParameter("queryname");
				int page = 0; // 当前页面
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 10; // 页面大小
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());
				String cusName = null;
				if (cusNameStr != null && cusNameStr.trim().length() > 0) {
					cusName = new String(URLDecoder.decode(cusNameStr, "UTF-8")); // 解决URL传递中文乱码问题
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
						jsonObj.put("CustomerId", w.getCustomer().getId()); // 委托单位Id
						jsonObj.put("CustomerName", w.getCustomer().getName()); // 委托单位名称
						jsonObj.put("HandleType", w.getHandleType()); // 操作类型
						jsonObj.put("HandleTime",
								DateTimeFormatUtil.DateTimeFormat.format(w.getHandleTime())); // 操作时间
						jsonObj.put("HandlerName",
								w.getHandlerName() == null ? "" : w
										.getHandlerName());// 操作人姓名
						jsonObj.put("HandlerId", w.getSysUser() == null ? ""
								: w.getSysUser().getId());// 操作人ID
						jsonObj.put("Remark", w.getRemark() == null ? "" : w
								.getRemark()); // 备注
						jsonObj.put("Amount", w.getAmount()); // 金额

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
		case 1: // 充值
			JSONObject retObj = new JSONObject();
			try {

				/*** 取值 **/
				String CustomerName = req.getParameter("Name11"); // 单位名称
				CustomerManager cusmag = new CustomerManager();
				Customer customer = cusmag.findByVarProperty(
						new KeyValueWithOperator("name", CustomerName, "="))
						.get(0);

				double Amount = Float
						.valueOf(req.getParameter("Amount").trim()); // 充值金额
				String Remark = req.getParameter("Remark"); // 备注
				Timestamp today = new Timestamp(System.currentTimeMillis());// 当前时间

				boolean yes1 = false;
				SysUser Handler = (SysUser) req.getSession().getAttribute(
						"LOGIN_USER");// 当前操作用户
				if (Handler != null) {

					int HandlerId = Handler.getId();
					String HandlerName = Handler.getName();

					/*** 创建委托单位账户存取明细对象 **/
					CustomerAccount cusAccount = new CustomerAccount();// 委托单位账户存取明细
					cusAccount.setCustomer(customer);
					cusAccount.setHandleType(0);
					cusAccount.setHandleTime(today);
					cusAccount.setHandlerName(HandlerName);
					cusAccount.setSysUser(Handler);
					cusAccount.setAmount(Amount);
					cusAccount.setRemark(Remark);
					yes1 = cusAccountMgr.save(cusAccount);

				} else {
					CustomerAccount cusAccount = new CustomerAccount();// 委托单位账户存取明细
					cusAccount.setCustomer(customer);
					cusAccount.setHandleType(0);
					cusAccount.setHandleTime(today);
					cusAccount.setAmount(Amount);
					cusAccount.setRemark(Remark);
					yes1 = cusAccountMgr.save(cusAccount);
				}

				/*** 修改Customer 的余额信息 ***/
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
				retObj.put("msg", yes ? "创建成功！" : "创建失败！");
				retObj.put("balance", balance);

			} catch (NumberFormatException e) { // 字符串转Integer错误
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("创建失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		

			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("创建失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2: // 查询账户余额
			JSONObject retObj1 = new JSONObject();
			try {

				/*** 取值 **/
				String CustomerName = req.getParameter("Name22"); // 单位名称
				CustomerManager cusmag1 = new CustomerManager();
				Customer customer1 = cusmag1.findByVarProperty(
						new KeyValueWithOperator("name", CustomerName, "="))
						.get(0);

				double balance1 = customer1.getBalance();
			
				retObj1.put("balance", balance1);

			} catch (NullPointerException e) { // 字符串转Integer错误
				log.error(String.format("error in %s", ClassName), e);
				
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
					
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 3: // 结账：把各项费用录入委托单，并且从账户中扣总费用，
			JSONObject retObj2 = new JSONObject();
			try {

				/*** 把各项费用录入委托单 **/
				String InvoiceCode = req.getParameter("InvoiceCode");// 发票号
				String CashPaidStr = req.getParameter("CashPaid"); //现金支付费用
				String ChequePaidStr = req.getParameter("ChequePaid"); //支票支付费用
				String AccountPaidStr = req.getParameter("AcountFee");  //账户支付费用
				String CommissionSheetsStr = req.getParameter("CommissionSheets");//委托单列表
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
					throw new Exception("总费用不能为空！");
				double TotalFee = Double.valueOf(req.getParameter("TotalFee")); //
				
				CustomerManager cusmag = new CustomerManager();
				int CustomerId = CommissionSheets.getJSONObject(0).getInt("CustomerId");
				Customer cus = cusmag.findById(CustomerId);
				if(AccountPaid > cus.getBalance()){
					throw new Exception("该单位账户余额不足，无法支付。");
				}
				
				Timestamp today = new Timestamp(System.currentTimeMillis());// 当前时间
				SysUser Handler = (SysUser) req.getSession().getAttribute("LOGIN_USER");// 当前操作用户
				
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				DetailListManager deListMgr=new DetailListManager();
				synchronized(MutexObjectOfNewDetailList) {
					//begin-查询本清单最大的清单号
					String queryCode = String.format("%d", today.getYear()+1900);		
					String queryString = "select max(model.code) from DetailList as model where model.code like ?";
					List<Object> retList = deListMgr.findByHQL(queryString, queryCode+"%");
					Integer codeBeginInt = Integer.parseInt("0000001");	//委托单编号
					if(retList.size() > 0 && retList.get(0) != null){
						codeBeginInt = Integer.parseInt(retList.get(0).toString().substring(4)) + 1;
					}
					//end-查询本清单最大的清单号
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
							throw new Exception("委托单" + cSheet.getCode() + "已结账。");
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
							throw new Exception("委托单" + cSheet.getCode() + "价格发生改变（现价：" + (Double)FList.get(0)[6] + "元），请重新添加该委托单进行结账。");
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
							
					/*** 修改Customer 的余额信息 ***/							
					cus.setBalance(cus.getBalance()-AccountPaid);
					
					/*** 创建委托单位账户存取明细对象 **/
					CustomerAccount cusAccount = null;// 委托单位账户存取明细
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
						retObj2.put("msg", "结账成功！");
						retObj2.put("DetailListId", detailList.getId());
						retObj2.put("DetailListCode", detailList.getCode());
					}
					else{
						retObj2.put("IsOK", false);
						retObj2.put("msg", "结账失败！");
						retObj2.put("DetailListId", "");
						retObj2.put("DetailListCode", "");
					}
				}
			} catch (Exception e) {
				try{
					retObj2.put("IsOK", false);
					retObj2.put("msg", String.format("操作失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}
				log.debug(String.format("error in %s", ClassName), e);
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj2.toString());
			break;
		}
	}
}
