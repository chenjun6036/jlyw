package com.jlyw.servlet.vehicle;

import java.io.IOException;
import java.net.URLDecoder;
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

import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.Vehicle;
import com.jlyw.manager.UserManager;
import com.jlyw.manager.vehicle.VehicleManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.KeyValueWithOperator;

public class VehicleServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(VehicleServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		VehicleManager vmag = new VehicleManager();
		switch(method)
		{
		case 1://添加车辆信息
			JSONObject retObj=new JSONObject();
			try {
				String licence = req.getParameter("Licence");
				String Driver = req.getParameter("Driver");
				List<Vehicle> result = vmag.findByVarProperty(new KeyValueWithOperator("licence", "%" + licence + "%", "like"));
				if(result==null||result.size()==0)
				{
					Vehicle v = initVehicle(req,0);
					if(v==null)
					{
						retObj.put("IsOK", false);
						retObj.put("msg", "员工选择有误，请确认！");
					}
					else
					{
						boolean res = vmag.save(v);
						retObj.put("IsOK", res);
						retObj.put("msg", res?"添加成功！":"添加失败，请重新添加！");
					}
				}
				else{
					retObj.put("IsOK", false);
					retObj.put("msg", "该车牌号已存在，请确认！");
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in VehicleServlet-->case 1", e);
				}else{
					log.error("error in VehicleServlet-->case 1", e);
				} 
				try{
					retObj.put("IsOK", false);
					retObj.put("msg", "添加失败，请重新添加！");
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			break;
		case 2://查询车辆信息
			JSONObject res = new JSONObject();
			try {
				String VehicleLicence = req.getParameter("queryname");
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<Vehicle> result;
				int total = 0;
				String queryStr = "from Vehicle as model where 1=1 ";
				if(VehicleLicence != null && !VehicleLicence.equals(""))
				{
					String vLicenceStr = URLDecoder.decode(VehicleLicence, "UTF-8");
					queryStr = queryStr + "and model.licence like ? ";
					result = vmag.findPageAllByHQL(queryStr + "order by model.status asc, model.licence asc", page, rows, "%" + vLicenceStr + "%");
					total = vmag.getTotalCountByHQL("select count(*) " + queryStr, "%" + vLicenceStr + "%", "like");
				}
				else{
					result = vmag.findPageAllByHQL(queryStr + "order by model.status asc, model.licence asc", page, rows);
					total = vmag.getTotalCountByHQL("select count(*) " + queryStr);
				}
				JSONArray options = new JSONArray();
				for (Vehicle vehicle : result) {
					JSONObject option = new JSONObject();
					option.put("Id", vehicle.getId());
					option.put("Licence", vehicle.getLicence());
					option.put("Limit", vehicle.getLimit());
					option.put("Model", vehicle.getModel());
					option.put("FuelFee", vehicle.getFuelFee());
					option.put("Brand", vehicle.getBrand());
					option.put("Status", vehicle.getStatus());
					option.put("Driver", vehicle.getSysUser()==null?"":vehicle.getSysUser().getName());
					options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in VehicleServlet-->case 2", e);
				}else{
					log.error("error in VehicleServlet-->case 2", e);
				} 
				try{
					res.put("total", 0);
					res.put("rows", new JSONArray());
				}
				catch(Exception ex){}
			}finally{
				resp.setContentType("text/plain");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3://修改车辆信息
			JSONObject retObj1=new JSONObject();
			try {
				String Driver = req.getParameter("Driver");
				Vehicle v = initVehicle(req, Integer.valueOf(req.getParameter("Id")));
				if(v==null)
				{
					retObj1.put("IsOK", false);
					retObj1.put("msg", "员工选择有误，请确认！");
				}
				else
				{
					boolean res1 = vmag.update(v);
					retObj1.put("IsOK", res1);
					retObj1.put("msg", res1?"添加成功！":"添加失败，请重新添加！");
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in VehicleServlet-->case 3", e);
				}else{
					log.error("error in VehicleServlet-->case 3", e);
				} 
				try{
					retObj1.put("IsOK", false);
					retObj1.put("msg", "修改失败，请重新修改！");
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			//System.out.println(retObj1.toString());
			break;
		case 4://报废车辆
			int id = Integer.parseInt(req.getParameter("id"));
			Vehicle ve = vmag.findById(id);
			ve.setStatus(2);
			JSONObject ret=new JSONObject();
			try {
				boolean res1 = vmag.update(ve);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"报废成功！":"报废失败，请重新报废！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in VehicleServlet-->case 4", e);
				}else{
					log.error("error in VehicleServlet-->case 4", e);
				}
				try{
					ret.put("IsOK", false);
					ret.put("msg", "报废失败，请重新报废！");
				}catch(Exception ex){}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(ret.toString());
			}
			break;
		case 5://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from Vehicle as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String VehicleLicence = params.getString("queryname");
					
					if(VehicleLicence != null && !VehicleLicence.equals(""))
					{
						String vLicenceStr = URLDecoder.decode(VehicleLicence, "UTF-8");
						queryStr = queryStr + "and model.licence like ? "+"order by model.status asc, model.licence asc";
						keys.add("%" + vLicenceStr + "%");
					}
					else{
						queryStr = queryStr + "order by model.status asc, model.licence asc";					
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr, keys, null, "formatExcel", "formatTitle", VehicleManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in VehicleServlet-->case 5", e);
				}else{
					log.error("error in VehicleServlet-->case 5", e);
				}
				try {
					retObj7.put("IsOK", false);
					retObj7.put("Path", "");
				} catch (JSONException e1) {}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj7.toString());
			}
		case 6://查询车牌号：Combobox（模糊查询）
			JSONArray options = new JSONArray();
			try {
				String VehicleLicence = req.getParameter("queryname");
				int page = 1;			
				int rows = 30;
				List<Vehicle> result;
				
				String queryStr = "from Vehicle as model where model.status = 0 ";
				if(VehicleLicence != null && !VehicleLicence.equals(""))
				{
					String vLicenceStr =  new String(VehicleLicence.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					//System.out.println(vLicenceStr);
					queryStr = queryStr + "and model.licence like ? ";
					result = vmag.findPageAllByHQL(queryStr, page, rows, "%" + vLicenceStr + "%");				
				}
				else{
					result = vmag.findPageAllByHQL(queryStr, page, rows);
				}
				if(result!=null){
					for (Vehicle vehicle : result) {
						JSONObject option = new JSONObject();
						option.put("Id", vehicle.getId());
						option.put("Licence", vehicle.getLicence());
						option.put("Status", vehicle.getStatus());
						option.put("Driver", vehicle.getSysUser()==null?"":vehicle.getSysUser().getName());
						options.put(option);
					}
				}
				
				
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in VehicleServlet-->case 6", e);
				}else{
					log.error("error in VehicleServlet-->case 6", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(options.toString());
			}
			break;
		}
	}
	
	private Vehicle initVehicle(HttpServletRequest req,int id)
	{
		String Licence = req.getParameter("Licence");
		Integer Limit = Integer.valueOf(req.getParameter("Limit"));
		String Model = req.getParameter("Model");
		String Brand = req.getParameter("Brand");
		Double FuelFee = Double.valueOf(req.getParameter("FuelFee"));
		Integer Status = Integer.valueOf(req.getParameter("Status"));
		String Driver = req.getParameter("Driver");
		
		Vehicle v;
		if(id==0)
		{
			v = new Vehicle();
			v.setStatus(0);
		}
		else
		{
			VehicleManager vmag = new VehicleManager();
			v = vmag.findById(id);
			v.setStatus(Status);
		}
		
		v.setLicence(Licence);
		v.setLimit(Limit);
		v.setModel(Model);
		v.setBrand(Brand);
		v.setFuelFee(FuelFee);
		if(Driver!=null&&!Driver.equals(""))
		{
			List<SysUser> check = new ArrayList<SysUser>();
			check = (new UserManager()).findByVarProperty(new KeyValueWithOperator("name", Driver, "="));
			if(check!=null&&check.size()!=0)
			{
				v.setSysUser(check.get(0));	
			}
			else
				return null;
		}
		else
			v.setSysUser(null);
		return v;
	}
}
