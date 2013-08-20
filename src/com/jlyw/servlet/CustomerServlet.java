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

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.CustomerContactor;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.manager.CustomerContactorManager;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.ReasonManager;
import com.jlyw.manager.RegionManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class CustomerServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(CustomerServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.parseInt(req.getParameter("method"));
		CustomerManager cusmag = new CustomerManager();
		switch (method) {
		case 1: // 添加客户
			JSONObject retObj=new JSONObject();
			try{
				Customer customer = initCustomer(req,0);
				String Contactor = req.getParameter("Contactor");
				String ContactorTel1 = req.getParameter("ContactorTel1");
				String ContactorTel2 = req.getParameter("ContactorTel2");
				CustomerContactor customercontactor = new CustomerContactor();
				customercontactor.setName(Contactor);
				customercontactor.setCellphone1(ContactorTel1);
				if (ContactorTel2 != null && !ContactorTel2.equals(""))
					customercontactor.setCellphone2(ContactorTel2);
				customercontactor.setLastUse(customer.getModifyDate());
				customercontactor.setCount(1);
				boolean res = cusmag.save(customer, customercontactor,null);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
			}catch(Exception e){
				try {
					retObj.put("IsOK", false);
					retObj.put("msg", String.format("新建失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 1", e);
				}else{
					log.error("error in CustomerServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2: // 客户查询
			JSONObject res = new JSONObject();
			try {
				String queryName = req.getParameter("queryname");
				String queryZipCode = req.getParameter("queryZipCode");
				String queryAddress = req.getParameter("queryAddress");
				String queryTel = req.getParameter("queryTel");
				String queryInsideContactor = req.getParameter("queryInsideContactor");
				String queryClassi = req.getParameter("queryClassi");
				String queryCredit = req.getParameter("queryCredit");
				String queryContactor = req.getParameter("queryContactor");
				String queryContactorTel = req.getParameter("queryContactorTel");
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				String queryStr = "from Customer as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				
				if(queryName!=null&&!queryName.equals(""))
				{
					String cusNameStr = URLDecoder.decode(queryName, "UTF-8");
					queryStr = queryStr + " and (model.name like ? or model.nameEn like ? or model.brief like ? or model.code like ?)";
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
					keys.add("%" + cusNameStr + "%");
				}
				if(queryZipCode!=null&&!queryZipCode.equals(""))
				{
					String cusZipCodeStr = URLDecoder.decode(queryZipCode, "UTF-8");
					queryStr = queryStr + " and model.zipCode like ?";
					keys.add("%" + cusZipCodeStr + "%");
				}
				if(queryAddress!=null&&!queryAddress.equals(""))
				{
					String cusAddressStr = URLDecoder.decode(queryAddress, "UTF-8");
					queryStr = queryStr + " and model.address like ?";
					keys.add("%" + cusAddressStr + "%");
				}
				if(queryTel!=null&&!queryTel.equals(""))
				{
					String cusTelStr = URLDecoder.decode(queryTel, "UTF-8");
					queryStr = queryStr + " and (model.tel like ? or model.fax like ?)";
					keys.add("%" + cusTelStr + "%");
					keys.add("%" + cusTelStr + "%");
				}
				if(queryInsideContactor!=null&&!queryInsideContactor.equals(""))
				{
					String cusInsideContactorStr = URLDecoder.decode(queryInsideContactor, "UTF-8");
					queryStr = queryStr + " and (model.sysUserByInsideContactorId.name like ? or model.sysUserByInsideContactorId.brief like ?)";
					keys.add("%" + cusInsideContactorStr + "%");
					keys.add("%" + cusInsideContactorStr + "%");
				}
				if(queryClassi!=null&&!queryClassi.equals(""))
				{
					String cusClassiStr = URLDecoder.decode(queryClassi, "UTF-8");
					queryStr = queryStr + " and model.classification like ?";
					keys.add("%" + cusClassiStr + "%");
				}
				if(queryCredit!=null&&!queryCredit.equals(""))
				{
					String cusCreditStr = URLDecoder.decode(queryCredit, "UTF-8");
					queryStr = queryStr + " and model.creditAmount = ?";
					keys.add(Double.valueOf(cusCreditStr));
				}
				if(queryContactor!=null&&!queryContactor.equals(""))
				{
					String cusContactorStr = URLDecoder.decode(queryContactor, "UTF-8");
					queryStr = queryStr + " and model.id in (select model1.customerId from CustomerContactor as model1 where model1.name like ?)";
					keys.add("%" + cusContactorStr + "%");
				}
				if(queryContactorTel!=null&&!queryContactorTel.equals(""))
				{
					String cusContactorTelStr = URLDecoder.decode(queryContactorTel, "UTF-8");
					queryStr = queryStr + " and model.id in (select model1.customerId from CustomerContactor as model1 where (model1.cellphone1 like ? or model1.cellphone2 like ?))";
					keys.add("%" + cusContactorTelStr + "%");
					keys.add("%" + cusContactorTelStr + "%");
				}
				
				List<Customer> result;
				int total;
				result = cusmag.findPageAllByHQL(queryStr + " order by model.status asc, model.id asc", page, rows, keys);
				total = cusmag.getTotalCountByHQL("select count(*) "+queryStr, keys);
				JSONArray options = new JSONArray();
				
				for (Customer cus : result) {
					JSONObject option = new JSONObject();
					option.put("Id", cus.getId());
					option.put("Name", cus.getName());
					option.put("NameEn", cus.getNameEn());
					option.put("Brief", cus.getBrief());
					option.put("RegionId", cus.getRegion().getId());
					option.put("CustomerType", cus.getCustomerType());
					option.put("Code", cus.getCode());
					option.put("Address", cus.getAddress());
					option.put("AddressEn", cus.getAddressEn());
					option.put("Tel", cus.getTel());
					option.put("Fax", cus.getFax());
					option.put("ZipCode", cus.getZipCode());
					option.put("Status", cus.getStatus());
					option.put("Balance", cus.getBalance());
					option.put("AccountBank", cus.getAccountBank());
					option.put("Account", cus.getAccount());
					option.put("Classification", cus.getClassification());
					option.put("FieldDemands", cus.getFieldDemands());
					option.put("CertificateDemands", cus.getCertificateDemands());
					option.put("SpecialDemands", cus.getSpecialDemands());
					option.put("CreditAmount", cus.getCreditAmount());
					option.put("CancelDate", cus.getCancelDate()==null?"未注销":cus.getCancelDate());
					option.put("CancelReason", cus.getCancelDate()==null?"":cus.getReason().getReason());
					option.put("Remark", cus.getRemark());
					option.put("ModifyDate", cus.getModifyDate());
					option.put("Modificator", cus.getSysUserByModificatorId().getName());
					option.put("InsideContactor", cus.getSysUserByInsideContactorId()==null?"":cus.getSysUserByInsideContactorId().getName());
					
					CustomerContactorManager cusconmag1 = new CustomerContactorManager();
					CustomerContactor cuscon ;
					List<CustomerContactor> resultList = cusconmag1.findByPropertyBySort("lastUse", false,
							new KeyValueWithOperator("customerId", cus.getId(), "="));
					if(resultList != null&& resultList.size()>0)
					{
						cuscon = resultList.get(0);
						option.put("Contactor", cuscon.getName());
						option.put("ContactorTel1", cuscon.getCellphone1());
						option.put("ContactorTel2", cuscon.getCellphone2());
					}
	
					options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception ex) {
				if(ex.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 2", ex);
				}else{
					log.error("error in CustomerServlet-->case 2", ex);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(res.toString());
				//System.out.println(res.toString());
			}
			break;
		case 3: // 修改客户
			JSONObject retObj1=new JSONObject();
			try{
				Customer cus = initCustomer(req, Integer.valueOf(req.getParameter("edit_Id")));
				String Contactor = req.getParameter("Contactor");
				String ContactorTel1 = req.getParameter("ContactorTel1");
				String ContactorTel2 = req.getParameter("ContactorTel2");
				
				boolean res1 = cusmag.update(cus, Contactor, ContactorTel1, ContactorTel2);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 3", e);
				}else{
					log.error("error in CustomerServlet-->case 3", e);
				}
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("修改失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4: // 删除客户
			JSONObject ret=new JSONObject();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			try {
				int id = Integer.parseInt(req.getParameter("del_id"));
				Customer customer1 = cusmag.findById(id);
				String Reason = req.getParameter("reason");
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", 22, "="));//查找注销原因
				if(rList.size() > 0){	//更新原因
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					customer1.setReason(reason);	//注销原因
				}else{	//新建原因
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(22);	//注销客户
					rMgr.save(reason);
					customer1.setReason(reason);	//注销原因
				}
				customer1.setCancelDate(today);
				customer1.setStatus(1);
				customer1.setModifyDate(today);
				customer1.setSysUserByModificatorId((SysUser)req.getSession().getAttribute("LOGIN_USER"));
				boolean res1 = cusmag.update(customer1);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"注销成功！":"注销失败，请重新注销！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 4", e);
				}else{
					log.error("error in CustomerServlet-->case 4", e);
				}
				try{
					ret.put("IsOK", false);
					ret.put("msg", "注销失败，请重新注销！");
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 5: //查询委托单位：Combobox（默认最多显示符合条件的前30条记录）:录入委托单界面中使用
			JSONArray jsonArray = new JSONArray();
			try {
				String cusNameStr = req.getParameter("CustomerName");
				if(cusNameStr != null && cusNameStr.trim().length() > 0){
					String cusName =  new String(cusNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					
					cusName = LetterUtil.String2Alpha(cusName);	//转换成拼音简码
					String[] queryName = cusName.split(" \\s*");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					cusName = "";
					for(int i = 0; i < queryName.length; i++){
						cusName += queryName[i];
						if(i != queryName.length-1)
							cusName += "%";
					}
					
					cusName = "%" + cusName + "%";
					String queryString = String.format("select model.name,model.tel,model.address,model.zipCode,model.region.id,cc.name,cc.cellphone1,model.region.name,model.id from Customer as model, CustomerContactor as cc where model.brief like ? and model.id=cc.customer.id and cc.lastUse in (select max(dd.lastUse) from CustomerContactor as dd where model.id=dd.customer.id)");
					List<Object[]> retList = cusmag.findPageAllByHQL(queryString, 1, 30, cusName);
					if(retList != null){
						for(Object[] objArray : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", objArray[0].toString());
							jsonObj.put("tel", objArray[1].toString());
							jsonObj.put("address", objArray[2].toString());
							jsonObj.put("zipCode", objArray[3].toString());
							jsonObj.put("regionId", ((Integer)objArray[4]).intValue());
							jsonObj.put("contactor", objArray[5].toString());
							jsonObj.put("contactorTel", objArray[6].toString());
							jsonObj.put("regionName", objArray[7].toString());
							jsonObj.put("id", objArray[8].toString());
							jsonArray.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 5", e);
				}else{
					log.error("error in CustomerServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 6:	//查询委托单位名称：Combobox（仅返回委托单位名称）
			JSONArray jsonArray2 = new JSONArray();
			try {
				String cusNameStr = req.getParameter("CustomerName");
				if(cusNameStr != null && cusNameStr.trim().length() > 0){
					String cusName =  new String(cusNameStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					//cusName = LetterUtil.String2Alpha(cusName);	//转换成拼音简码
					String[] queryName = cusName.split(" \\s*");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					cusName = "";
					for(int i = 0; i < queryName.length; i++){
						cusName += queryName[i];
						if(i != queryName.length-1)
							cusName += "%";
					}
					cusName = "%" + cusName + "%";
					String queryString = String.format("select model.id, model.name from Customer as model where (model.name like ? or model.nameEn like ? or model.brief like ? or model.code like ?)");
					List<Object[]> retList = cusmag.findPageAllByHQL(queryString, 1, 30, cusName,cusName,cusName,cusName);
					if(retList != null){
						for(Object[] objArray : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", (String)objArray[1]);
							jsonObj.put("id", (Integer)objArray[0]);
							jsonArray2.put(jsonObj);	
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 6", e);
				}else{
					log.error("error in CustomerServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
		case 7://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "select model,model1 from Customer model left join model.contactors model1 where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String queryName = params.getString("queryname");
					String queryZipCode = params.getString("queryZipCode");
					String queryAddress = params.getString("queryAddress");
					String queryTel = params.getString("queryTel");
					String queryInsideContactor = params.getString("queryInsideContactor");
					String queryClassi = params.getString("queryClassi");
					String queryCredit = params.getString("queryCredit");
					String queryContactor = params.getString("queryContactor");
					String queryContactorTel = params.getString("queryContactorTel");
					
					if(queryName!=null&&!queryName.equals(""))
					{
						String cusNameStr = URLDecoder.decode(queryName, "UTF-8");
						queryStr = queryStr + " and (model.name like ? or model.nameEn like ? or model.brief like ? or model.code like ?)";
						keys.add("%" + cusNameStr + "%");
						keys.add("%" + cusNameStr + "%");
						keys.add("%" + cusNameStr + "%");
						keys.add("%" + cusNameStr + "%");
					}
					if(queryZipCode!=null&&!queryZipCode.equals(""))
					{
						String cusZipCodeStr = URLDecoder.decode(queryZipCode, "UTF-8");
						queryStr = queryStr + " and model.zipCode like ?";
						keys.add("%" + cusZipCodeStr + "%");
					}
					if(queryAddress!=null&&!queryAddress.equals(""))
					{
						String cusAddressStr = URLDecoder.decode(queryAddress, "UTF-8");
						queryStr = queryStr + " and model.address like ?";
						keys.add("%" + cusAddressStr + "%");
					}
					if(queryTel!=null&&!queryTel.equals(""))
					{
						String cusTelStr = URLDecoder.decode(queryTel, "UTF-8");
						queryStr = queryStr + " and (model.tel like ? or model.fax like ?)";
						keys.add("%" + cusTelStr + "%");
						keys.add("%" + cusTelStr + "%");
					}
					if(queryInsideContactor!=null&&!queryInsideContactor.equals(""))
					{
						String cusInsideContactorStr = URLDecoder.decode(queryInsideContactor, "UTF-8");
						queryStr = queryStr + " and (model.sysUserByInsideContactorId.name like ? or model.sysUserByInsideContactorId.brief like ?)";
						keys.add("%" + cusInsideContactorStr + "%");
						keys.add("%" + cusInsideContactorStr + "%");
					}
					if(queryClassi!=null&&!queryClassi.equals(""))
					{
						String cusClassiStr = URLDecoder.decode(queryClassi, "UTF-8");
						keys.add("%" + cusClassiStr + "%");
					}
					if(queryCredit!=null&&!queryCredit.equals(""))
					{
						String cusCreditStr = URLDecoder.decode(queryCredit, "UTF-8");
						queryStr = queryStr + " and model.creditAmount = ?";
						keys.add(Double.valueOf(cusCreditStr));
					}
					if(queryContactor!=null&&!queryContactor.equals(""))
					{
						String cusContactorStr = URLDecoder.decode(queryContactor, "UTF-8");
						queryStr = queryStr + " and model.id in (select model2.customerId from CustomerContactor as model2 where model2.name like ?)";
						keys.add("%" + cusContactorStr + "%");
					}
					if(queryContactorTel!=null&&!queryContactorTel.equals(""))
					{
						String cusContactorTelStr = URLDecoder.decode(queryContactorTel, "UTF-8");
						queryStr = queryStr + " and model.id in (select model3.customerId from CustomerContactor as model3 where (model3.cellphone1 like ? or model3.cellphone2 like ?))";
						keys.add("%" + cusContactorTelStr + "%");
						keys.add("%" + cusContactorTelStr + "%");
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr + " and (model1.lastUse = (select max(model4.lastUse) from CustomerContactor as model4 where model4.customerId = model.id) or model1.lastUse is null)", keys, null, "formatExcel", "formatTitle", CustomerManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in CustomerServlet-->case 7", e);
				}else{
					log.error("error in CustomerServlet-->case 7", e);
				}
				try {
					retObj7.put("IsOK", false);
					retObj7.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj7.toString());
			}
			break;
		}
	}

	public Customer initCustomer(HttpServletRequest req,int id) {
		
		String Name = req.getParameter("Name");
		String NameEn = req.getParameter("NameEn");
		String Brief = req.getParameter("Brief");
		String Type = req.getParameter("CustomerType");
		String Code = req.getParameter("Code");
		String RegionId = req.getParameter("RegionId");
		String ZipCode = req.getParameter("ZipCode");
		
		String Address = req.getParameter("Address");
		String AddressEn = req.getParameter("AddressEn");
		String Tel = req.getParameter("Tel");
		String Fax = req.getParameter("Fax");
		String[] ClassificationStr = req.getParameterValues("Classification");
		String Classification = "";
		for(int i = 0;i< ClassificationStr.length; i++)
			Classification = Classification + (Classification.equals("")?"":",") + ClassificationStr[i];
		//System.out.println(Classification);
		String Status = req.getParameter("Status");
		String AccountBank = req.getParameter("AccountBank");
		String Account = req.getParameter("Account");
		String CreditAmount = req.getParameter("CreditAmount");
		String Remark = req.getParameter("Remark");
		String FieldDemands = req.getParameter("FieldDemands");
		String CertificateDemands = req.getParameter("CertificateDemands");
		String SpecialDemands = req.getParameter("SpecialDemands");
		SysUser user = (SysUser)req.getSession().getAttribute("LOGIN_USER");
		String InsideContactor = req.getParameter("InsideContactor");

		
		Customer customer;
		if(id==0)
		{
			customer = new Customer();
		}
		else
		{
			CustomerManager cusmag = new CustomerManager();
			customer = cusmag.findById(id);
		}
		
		customer.setName(Name);
		customer.setNameEn(NameEn);
		customer.setBrief(Brief);
		customer.setCustomerType(Integer.parseInt(Type));
		customer.setCode(Code);
		customer.setRegion((new RegionManager()).findById(Integer.parseInt(RegionId)));
		customer.setZipCode(ZipCode);
		customer.setAddress(Address);
		customer.setAddressEn(AddressEn);
		customer.setTel(Tel);
		customer.setFax(Fax);
		customer.setBalance(0.0);
		customer.setClassification(Classification);
		if(customer.getStatus()!=null&&customer.getStatus()==1&&Integer.parseInt(Status)==0)
		{
			customer.setStatus(Integer.parseInt(Status));
			customer.setCancelDate(null);
			customer.setReason(null);
		}
		else{
			customer.setStatus(Integer.parseInt(Status));
		}
		customer.setAccountBank(AccountBank);
		customer.setAccount(Account);
		customer.setCreditAmount(Double.parseDouble(CreditAmount));
		if(InsideContactor!=null&!InsideContactor.equals(""))
		{
			customer.setSysUserByInsideContactorId((new UserManager()).findByVarProperty(new KeyValueWithOperator("name", InsideContactor, "=")).get(0));
		}
		else
		{
			customer.setSysUserByInsideContactorId(null);
		}
		customer.setRemark(Remark);
		customer.setFieldDemands(FieldDemands);
		customer.setCertificateDemands(CertificateDemands);
		customer.setSpecialDemands(SpecialDemands);
		customer.setSysUserByModificatorId(user);
		customer.setModifyDate(new Timestamp(System.currentTimeMillis()));
		return customer;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doPost(req, resp);
	}

}
