package com.jlyw.servlet;

import java.io.IOException;
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

import com.jlyw.hibernate.Address;
import com.jlyw.manager.AddressManager;
import com.jlyw.util.KeyValueWithOperator;

public class AddressServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(AddressServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		AddressManager addrMgr = new AddressManager();
		switch (method) {
		case 0: // 查询所有的计量所单位地址信息
			JSONArray jsonArray = new JSONArray();
			try {
				List<Address> addrList = addrMgr.findByVarProperty(new KeyValueWithOperator("status", 0, "="));
				if(addrList != null){
					int i=1;
					for(Address addr : addrList){
						JSONObject jsonObj = new JSONObject();
						
						jsonObj.put("address", addr.getAddress());
						jsonObj.put("id", addr.getId());
						if(i==1){
							jsonObj.put("selected", true);
						}
						i++;
						jsonArray.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AddressServlet-->case 0", e);
				}else{
					log.error("error in AddressServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
				//System.out.println(jsonArray.toString());
			}
			break;
		case 1: //查询所有的计量所单位的台头名称
			JSONArray jsonArray1 = new JSONArray();
			try {
				List<Address> addrList = addrMgr.findByVarProperty(new KeyValueWithOperator("status", 0, "="));
				if(addrList != null){
					int i=1;
					for(Address addr : addrList){
						JSONObject jsonObj = new JSONObject();
						
						jsonObj.put("headname", addr.getHeadName());	//台头名称
						jsonObj.put("address", addr.getAddress());
						jsonObj.put("id", addr.getId());
						if(i==1){
							jsonObj.put("selected", true);
						}
						i++;
						jsonArray1.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AddressServlet-->case 1", e);
				}else{
					log.error("error in AddressServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray1.toString());
			}
			break;
		case 2://查询所有的计量所单位的台头等信息
			JSONArray jsonArray2 = new JSONArray();
			try {
				List<Address> addrList = addrMgr.findByVarProperty(new KeyValueWithOperator("status", 0, "="));
				if(addrList != null){
					for(Address addr : addrList){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("Tel", addr.getTel());
						jsonObj.put("Fax", addr.getFax());
						jsonObj.put("headname", addr.getHeadName());
						jsonObj.put("Zipcode", addr.getZipCode());
						jsonObj.put("address", addr.getAddress());
						jsonObj.put("id", addr.getId());
						jsonArray2.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AddressServlet-->case 2", e);
				}else{
					log.error("error in AddressServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
		case 3://查询所有的计量所单位的详细信息
			JSONObject retObj3 = new JSONObject();
			try{
				int total = 0;
				List<Address> result = addrMgr.findByVarProperty();
				total = addrMgr.getTotalCount();
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0){
					for(Address addr : result)
					{
						JSONObject option = new JSONObject();
						option.put("Id", addr.getId());
						option.put("Name", addr.getName());
						option.put("Address", addr.getAddress());
						option.put("AddressEn", addr.getAddressEn());
						option.put("Brief", addr.getBrief());
						option.put("HeadName", addr.getHeadName());
						option.put("HeadNameEn", addr.getHeadNameEn());
						option.put("Tel", addr.getTel());
						option.put("Fax", addr.getFax());
						option.put("ZipCode", addr.getZipCode());
						option.put("ComplainTel", addr.getComplainTel());
						option.put("WebSite", addr.getWebSite());
						option.put("Status", addr.getStatus());
						option.put("AuthorizationStatement", addr.getAuthorizationStatement());
						option.put("AuthorizationStatementEn", addr.getAuthorizationStatementEn());
						option.put("CNASStatement", addr.getCnasstatement());
						option.put("CNASStatementEn", addr.getCnasstatementEn());
						option.put("StandardStatement", addr.getStandardStatement());
						option.put("StandardStatementEn", addr.getStandardStatementEn());
						
						options.put(option);
					}
				}
				retObj3.put("total", total);
				retObj3.put("rows", options);
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AddressServlet-->case 3", e);
				}else{
					log.error("error in AddressServlet-->case 3", e);
				}
				try{
					retObj3.put("total", 0);
					retObj3.put("rows", new JSONArray());
				}catch(Exception e1){}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		case 4://新增或修改
			JSONObject retObj4 = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				Address addr;
				if(Id.equals("")){
					addr = initAddress(req, 0);
					boolean res = addrMgr.save(addr);
					retObj4.put("IsOK", res);
					retObj4.put("msg", res?"添加成功！":"添加失败，请重新添加！");
				}
				else{
					addr = initAddress(req, Integer.valueOf(Id));
					boolean res = addrMgr.update(addr);
					retObj4.put("IsOK", res);
					retObj4.put("msg", res?"修改成功！":"修改失败，请重新修改！");
				}
			}catch(Exception e){
				try {
					retObj4.put("IsOK", false);
					retObj4.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AddressServlet-->case 4", e);
				}else{
					log.error("error in AddressServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		case 5://注销
			JSONObject retObj5 = new JSONObject();
			try{
				String Id = req.getParameter("id");
				Address addr = addrMgr.findById(Integer.valueOf(Id));
				addr.setStatus(1);
				boolean res = addrMgr.update(addr);
				retObj5.put("IsOK", res);
				retObj5.put("msg", res?"注销成功！":"注销失败，请重新注销！");
			}catch(Exception e){
				try {
					retObj5.put("IsOK", false);
					retObj5.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in AddressServlet-->case 5", e);
				}else{
					log.error("error in AddressServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().write(retObj5.toString());
			}
			break;
		}
	}

	private Address initAddress(HttpServletRequest req, int id){
		Address addr;
		if(id == 0){
			addr = new Address();
		}
		else{
			addr = (new AddressManager()).findById(id);
		}
		
		String Name = req.getParameter("Name");
		String Address = req.getParameter("Address");
		String AddressEn = req.getParameter("AddressEn");
		String Brief = req.getParameter("Brief");
		String Status = req.getParameter("Status");
		String HeadName = req.getParameter("HeadName");
		String HeadNameEn = req.getParameter("HeadNameEn");
		String Tel = req.getParameter("Tel");
		String Fax = req.getParameter("Fax");
		String ZipCode = req.getParameter("ZipCode");
		String ComplainTel = req.getParameter("ComplainTel");
		String WebSite = req.getParameter("WebSite");
		String AuthorizationStatement = req.getParameter("AuthorizationStatement");
		String CNASStatement = req.getParameter("CNASStatement");
		String StandardStatement = req.getParameter("StandardStatement");
		String AuthorizationStatementEn = req.getParameter("AuthorizationStatementEn");
		String CNASStatementEn = req.getParameter("CNASStatementEn");
		String StandardStatementEn = req.getParameter("StandardStatementEn");
		
		addr.setName(Name);
		addr.setAddress(Address);
		addr.setAddressEn(AddressEn);
		addr.setBrief(Brief);
		addr.setStatus(Integer.valueOf(Status));
		addr.setHeadName(HeadName);
		addr.setHeadNameEn(HeadNameEn);
		addr.setTel(Tel);
		addr.setFax(Fax);
		addr.setZipCode(ZipCode);
		addr.setComplainTel(ComplainTel);
		addr.setWebSite(WebSite);
		addr.setAuthorizationStatement(AuthorizationStatement);
		addr.setCnasstatement(CNASStatement);
		addr.setStandardStatement(StandardStatement);
		addr.setAuthorizationStatementEn(AuthorizationStatementEn);
		addr.setCnasstatementEn(CNASStatementEn);
		addr.setStandardStatementEn(StandardStatementEn);
		
		return addr;
	}
	
}
