package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Date;
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

import com.jlyw.hibernate.view.ViewQjtz;
import com.jlyw.manager.CustomerManager;
import com.jlyw.manager.view.ViewQjtzManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;

public class QJTZServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(QJTZServlet.class);

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		ViewQjtzManager qjMgr = new ViewQjtzManager();
		switch(method)
		{
		case 1://查询
			List<KeyValueWithOperator> list = new ArrayList<KeyValueWithOperator>();
			String Customer = req.getParameter("Customer");
			String StartTime = req.getParameter("StartTime");
			String EndTime = req.getParameter("EndTime");
			
			if(Customer!=null)
			{
				list.add(new KeyValueWithOperator("customerName", "%" + URLDecoder.decode(Customer, "UTF-8") + "%", "like"));
			}
			if(StartTime!=null&&StartTime!="")
			{
				list.add(new KeyValueWithOperator("mandatoryDate", Date.valueOf(StartTime), ">="));
			}
			if(EndTime!=null&&EndTime!="")
			{
				list.add(new KeyValueWithOperator("mandatoryDate", Date.valueOf(EndTime), "<="));
			}
			
			int page = 1;
			if (req.getParameter("page") != null)
				page = Integer.parseInt(req.getParameter("page").toString());
			int rows = 10;
			if (req.getParameter("rows") != null)
				rows = Integer.parseInt(req.getParameter("rows").toString());
			
			//String queryString = String.format("from CommissionSheet as model where model.mandatory = 0 and (model.customerName like ? or model.applianceName like ?) group by model.customerName order by model.createDate");
			//List<CommissionSheet> result = csm.findByHQL(queryString, Customer==null?"":"%" + URLDecoder.decode(Customer, "UTF-8") + "%" , ApplianceName==null?"":"%" + URLDecoder.decode(ApplianceName, "UTF-8") + "%");
			List<ViewQjtz> result = qjMgr.findPagedAllBySort(page, rows, "customerName", true, list);
			int total = qjMgr.getTotalCount(list);
			JSONObject res = new JSONObject();
			JSONArray options = new JSONArray();
			try {
				if(result!=null&&result.size()!=0)
				{
					for(ViewQjtz qj : result)
					{
						JSONObject option = new JSONObject();
						option.put("CustomerName", qj.getCustomerName());
						option.put("Address", qj.getCustomerAddress());
						option.put("Zipcode", qj.getCustomerZipCode());
						option.put("name2", qj.getApplianceName());
						option.put("Model", qj.getApplianceModel());
						option.put("QGid", qj.getMandatoryCode()==null?"":qj.getMandatoryCode());
						option.put("time1", qj.getWorkDate());
						option.put("cycle", qj.getTestCycle());
						option.put("time2", qj.getMandatoryDate());
						option.put("Remark", qj.getRemark());	
						
						options.put(option);
					}
				}
				
				res.put("total", total);
				res.put("rows", options);
			}
			catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QJTZServlet-->case 1", e);
				}else{
					log.error("error in QJTZServlet-->case 1", e);
				}
			}
			resp.setContentType("text/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res.toString());
			break;
		case 2://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from ViewQjtz as model where 1=1";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String customer = params.getString("Customer");
					String startTime = params.getString("StartTime");
					String endTime = params.getString("EndTime");
										
					if(customer!=null)
					{
						queryStr = queryStr + " and model.customerName like ? ";
						keys.add("%" + URLDecoder.decode(customer, "UTF-8") + "%");
					}
					if(startTime!=null&&startTime!="")
					{
						queryStr = queryStr + " and model.workDate >= ? ";
						keys.add(Date.valueOf(startTime));
					}
					if(endTime!=null&&endTime!="")
					{
						queryStr = queryStr + " and model.workDate <= ? ";
						keys.add(Date.valueOf(endTime));
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", ViewQjtzManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QJTZServlet-->case 2", e);
				}else{
					log.error("error in QJTZServlet-->case 2", e);
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
		case 3://查询
			List<KeyValueWithOperator> list1 = new ArrayList<KeyValueWithOperator>();
			String Customer1 = req.getParameter("Customer");
			String StartTime1 = req.getParameter("StartTime");
			String EndTime1 = req.getParameter("EndTime");
			
			if(Customer1!=null)
			{
				list1.add(new KeyValueWithOperator("customerName", "%" + URLDecoder.decode(Customer1, "UTF-8") + "%", "like"));
			}
			if(StartTime1!=null&&StartTime1!="")
			{
				list1.add(new KeyValueWithOperator("mandatoryDate", Date.valueOf(StartTime1), ">="));
			}
			if(EndTime1!=null&&EndTime1!="")
			{
				list1.add(new KeyValueWithOperator("mandatoryDate", Date.valueOf(EndTime1), "<="));
			}
			
			//String queryString = String.format("from CommissionSheet as model where model.mandatory = 0 and (model.customerName like ? or model.applianceName like ?) group by model.customerName order by model.createDate");
			//List<CommissionSheet> result = csm.findByHQL(queryString, Customer==null?"":"%" + URLDecoder.decode(Customer, "UTF-8") + "%" , ApplianceName==null?"":"%" + URLDecoder.decode(ApplianceName, "UTF-8") + "%");
			
			List<ViewQjtz> resultAll = qjMgr.findByVarPropertyBySort("customerName", true,list1);
			int total1 = 0;
			JSONObject res1 = new JSONObject();
			String temp="";
			JSONArray optionsThis = null;
			JSONObject customerObject = null;
			JSONArray optionsAll = new JSONArray();//各个委托单位的强检信息。包含很多JSONObject（customerObject），customerObject包含单位信息和该单位下的强检器具列表（optionsThis）。
			try {
				if(resultAll!=null&&resultAll.size()!=0)
				{
					for(ViewQjtz qj : resultAll)
					{
						JSONObject option = new JSONObject();
						
						option.put("CustomerName", qj.getCustomerName());
						option.put("Address", qj.getCustomerAddress());
						option.put("Zipcode", qj.getCustomerZipCode());
						option.put("name2", qj.getApplianceName());
						option.put("Model", qj.getApplianceModel());
						option.put("QGid", qj.getMandatoryCode()==null?"":qj.getMandatoryCode());
						option.put("time1", qj.getWorkDate());
						option.put("cycle", qj.getTestCycle());
						option.put("time2", qj.getMandatoryDate());
						option.put("Remark", qj.getRemark());	
						
						if(!temp.equals(qj.getCustomerName())){//出现新委托单位时，把前委托单位的信息和强检器具列表存入customerObject
							
							if(optionsThis!=null&&optionsThis.length()>0){//原强检器具列表（optionsThis）放入customerObject
								customerObject.put("rows",optionsThis);	
								customerObject.put("Customer",((JSONObject)optionsThis.get(0)).get("CustomerName"));
								customerObject.put("Address",((JSONObject)optionsThis.get(0)).get("Address"));
								customerObject.put("Zipcode",((JSONObject)optionsThis.get(0)).get("Zipcode"));
								optionsAll.put(customerObject);//customerObject存入各个委托单位的强检信息
								total1++;
							}
							optionsThis = new JSONArray();	//强检器具列表（optionsThis）重新置空
							customerObject=new JSONObject();
							optionsThis.put(option);
							temp=qj.getCustomerName();
						}else{
							optionsThis.put(option);
						}
					}
				}
				customerObject.put("rows",optionsThis);	//最后一个委托单位的信息和强检器具列表存入customerObject
				customerObject.put("Customer",((JSONObject)optionsThis.get(0)).get("CustomerName"));
				customerObject.put("Address",((JSONObject)optionsThis.get(0)).get("Address"));
				customerObject.put("Zipcode",((JSONObject)optionsThis.get(0)).get("Zipcode"));
				optionsAll.put(customerObject);
				total1++;
				res1.put("total", total1);
				res1.put("RowsList", optionsAll);
				res1.put("IsOK", true);
			}
			catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in QJTZServlet-->case 1", e);
				}else{
					log.error("error in QJTZServlet-->case 1", e);
				}
				try {
					res1.put("IsOK", false);
					res1.put("msg", e.getMessage());
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}finally{
				resp.setContentType("text/html");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res1.toString());
				//System.out.println(res1.toString());
			}
			break;
		}
	}

}
