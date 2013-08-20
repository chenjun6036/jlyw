package com.jlyw.servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

import com.jlyw.hibernate.BaseType;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.FlagUtil.BaseTypeInfoType;
import com.jlyw.util.KeyValueWithOperator;

public class BaseTypeServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(BaseTypeServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		BaseTypeManager baseTypeMgr = new BaseTypeManager();
		switch(method){
		case 1://新建或修改
			JSONObject retObj1 = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				BaseType baseType;
				if(Id.equals("")){
					baseType = initBaseType(req, 0);
					boolean res = baseTypeMgr.save(baseType);
					retObj1.put("IsOK", res);
					retObj1.put("msg", res?"添加成功！":"添加失败，请重新添加！");
				}
				else{
					String OldName = baseTypeMgr.findById(Integer.valueOf(Id)).getName();
					baseType = initBaseType(req, Integer.valueOf(Id));
					boolean res = baseTypeMgr.update(baseType, OldName);
					retObj1.put("IsOK", res);
					retObj1.put("msg", res?"修改成功！":"修改失败，请重新修改！");
				}
			}catch(Exception e){
				try {
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in BaseTypeServlet-->case 1", e);
				}else{
					log.error("error in BaseTypeServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 2://查询
			JSONObject retObj2 = new JSONObject();
			try{
				String Type = req.getParameter("Type");
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<BaseType> result = baseTypeMgr.findPagedAll(page, rows, new KeyValueWithOperator("type", Integer.valueOf(Type), "="));
				int total = baseTypeMgr.getTotalCount(new KeyValueWithOperator("type", Integer.valueOf(Type), "="));
				JSONArray options = new JSONArray();
				if(result!=null&&result.size()>0){
					for(BaseType temp: result){
						JSONObject option = new JSONObject();
						option.put("Id", temp.getId());
						option.put("Name", temp.getName());
						option.put("Brief", temp.getBrief());
						option.put("Sequence", temp.getSequence());
						option.put("Type", temp.getType());
						option.put("Status", temp.getStatus());
						option.put("Remark", temp.getRemark());
						
						options.put(option);
					}
				}
				retObj2.put("total", total);
				retObj2.put("rows", options);
			}catch(Exception e){
				try {
					retObj2.put("total", 0);
					retObj2.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in BaseTypeServlet-->case 2", e);
				}else{
					log.error("error in BaseTypeServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retObj2.toString());
			}
			break;
		case 3://注销
			JSONObject retObj3 = new JSONObject();
			try{
				String Id = req.getParameter("id");
				BaseType baseType = baseTypeMgr.findById(Integer.valueOf(Id));
				baseType.setStatus(1);
				boolean res = baseTypeMgr.update(baseType);
				retObj3.put("IsOK", res);
				retObj3.put("msg", res?"注销成功！":"注销失败，请重新注销！");
			}catch(Exception e){
				try {
					retObj3.put("IsOK", false);
					retObj3.put("msg", String.format("操作失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in BaseTypeServlet-->case 3", e);
				}else{
					log.error("error in BaseTypeServlet-->case 3", e);
				}
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj3.toString());
			}
			break;
		case 4://根据类型查询信息列表，用于combobox
			JSONArray jsonArray = new JSONArray();
			try {
				String Type = req.getParameter("type");	//类型
				
				List<BaseType> retList = baseTypeMgr.findByVarProperty(new KeyValueWithOperator("type",Integer.parseInt(Type),"="),new KeyValueWithOperator("status", 0, "="));
				for(BaseType bt : retList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", bt.getName());
					jsonObj.put("id", bt.getId());
					jsonArray.put(jsonObj);	
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in BaseTypeServlet-->case 4", e);
				}else{
					log.error("error in BaseTypeServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
//		case 5://查询基本信息类型，用于combobox
//			JSONArray jsonArray5 = new JSONArray();
//			try{
//				String queryString = " select distinct type from BaseType as a1 where a1.status= ?";
//				List<Integer> regList = baseTypeMgr.findByHQL(queryString,0);
//				if (regList != null) {
//					for (Integer reg : regList) {
//						JSONObject jsonObj = new JSONObject();
//						
//						int type=reg;
//						jsonObj.put("Type", type);
//						jsonObj.put("TypeName", FlagUtil.BaseTypeInfoType.getBaseTypeInfo(type));
//							
//						jsonObj.put("Type", type);
//						jsonArray5.put(jsonObj);
//					}
//				}
//			}catch(Exception e){
//				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
//					log.debug("exception in BaseTypeServlet-->case 5", e);
//				}else{
//					log.error("error in BaseTypeServlet-->case 5", e);
//				}
//			}finally{
//				resp.setContentType("text/html;charset=gbk");
//				resp.getWriter().write(jsonArray5.toString());
//			}
//			break;
		case 6:
			JSONArray options = new JSONArray();
			try{
				Class c = BaseTypeInfoType.class;
				Field[] fields = c.getFields();
				for(Field f : fields){
					JSONObject option = new JSONObject();
					option.put("TypeName", BaseTypeInfoType.getBaseTypeInfo(f.getInt(c.newInstance())));
					option.put("Type", f.getInt(c.newInstance()));
					options.put(option);
				}
			}
			catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in BaseTypeServlet-->case 6", e);
				}else{
					log.error("error in BaseTypeServlet-->case 6", e);
				}
			}
			finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(options.toString());
			}
			break;
		}
	}
	
	private BaseType initBaseType(HttpServletRequest req, int id)
	{
		BaseType baseType;
		if(id==0){
			baseType = new BaseType();
		}
		else{
			baseType = (new BaseTypeManager()).findById(id);
		}
		
		String Name = req.getParameter("Name");
		String Brief = req.getParameter("Brief");
		String Sequence = req.getParameter("Sequence");
		String Status = req.getParameter("Status");
		String Type = req.getParameter("Type");
		String Remark = req.getParameter("Remark");
		
		baseType.setName(Name);
		baseType.setBrief(Brief);
		baseType.setSequence(Integer.valueOf(Sequence));
		baseType.setStatus(Integer.valueOf(Status));
		baseType.setRemark(Remark);
		baseType.setType(Integer.valueOf(Type));
		
		return baseType;
	}

}
