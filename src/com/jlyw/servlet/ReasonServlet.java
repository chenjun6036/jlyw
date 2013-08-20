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

import com.jlyw.hibernate.Reason;
import com.jlyw.manager.ReasonManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.SystemCfgUtil;

public class ReasonServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(ReasonServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.valueOf(req.getParameter("method"));
		ReasonManager reasonMgr = new ReasonManager();
		switch(method)
		{
		case 0: //��ѯԭ��
			JSONArray jsonArray = new JSONArray();
			try {
				String Type = req.getParameter("type");	//ԭ������
				
				List<Reason> retList = reasonMgr.findPagedAllBySort(1, 50, "count", false, new KeyValueWithOperator("type",Integer.parseInt(Type),"="),new KeyValueWithOperator("status", 0, "="));
				for(Reason r : retList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", r.getReason());
					jsonObj.put("id", r.getId());
					jsonArray.put(jsonObj);	
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ReasonServlet-->case 0", e);
				}else{
					log.error("error in ReasonServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 1:// ��ѯԭ���б�
			JSONArray options = new JSONArray();
			JSONObject res = new JSONObject();
			try {
				String ReasonTypeStr = req.getParameter("queryname");
				
				List<KeyValueWithOperator> list = new ArrayList<KeyValueWithOperator>();
				if (ReasonTypeStr != null && ReasonTypeStr != "") {
					int ReasonType =Integer.parseInt(URLDecoder.decode(ReasonTypeStr,"UTF-8"));
					list.add(new KeyValueWithOperator("type", ReasonType, "="));
				}
				list.add(new KeyValueWithOperator("status", 0, "="));

				int page = 0;
				if (req.getParameter("page") != null)
					page = Integer
							.parseInt(req.getParameter("page").toString());
				int rows = 0;
				if (req.getParameter("rows") != null)
					rows = Integer
							.parseInt(req.getParameter("rows").toString());

				List<Reason> result;
				int total;
				result = reasonMgr.findPagedAll(page, rows, list);
				total = reasonMgr.getTotalCount(list);

				for (Reason reason : result) {
					JSONObject option = new JSONObject();
					option.put("Id", reason.getId());
					option.put("Reason", reason.getReason());
					option.put("Type", reason.getType());
					option.put("LastUse", reason.getLastUse());
					option.put("Count", reason.getCount());
					option.put("Status", reason.getStatus());

					options.put(option);
				}
				res = new JSONObject();
				res.put("total", total);
				res.put("rows", options);

			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ReasonServlet-->case 1", e);
				}else{
					log.error("error in ReasonServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 2:// ע��ԭ��		
			JSONObject ret = new JSONObject();
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				Reason reason = reasonMgr.findById(id);
				reason.setStatus(1);
				boolean res1 = reasonMgr.update(reason);
				ret.put("IsOK", res1);
				ret.put("msg", res1 ? "ɾ���ɹ���" : "ɾ��ʧ�ܣ�������ɾ����");
			} catch (Exception e) {
				try{
					ret.put("IsOK", false);
					ret.put("msg", String.format("ɾ��ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}	
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ReasonServlet-->case 2", e);
				}else{
					log.error("error in ReasonServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(ret.toString());	
			}
			break;
		case 3:// ��ѯԭ������--Combobox
			JSONArray jsonArray2 = new JSONArray();
			try {
				String queryString = " select distinct type from "+SystemCfgUtil.DBPrexName+"Reason as a1 where a1.status= ?";
				List<Integer> regList = reasonMgr.findBySQL(queryString,0);
				if (regList != null) {
					for (Integer reg : regList) {
						JSONObject jsonObj = new JSONObject();
						//jsonObj.put("Reason", reg.getReason());
						
						int type=reg;
						switch(type){
						 case 11:
							 jsonObj.put("Type", type);
							 jsonObj.put("TypeName", "��������");
							 break;
						 case 12:
							 jsonObj.put("Type",type);
							 jsonObj.put("TypeName", "��������");
							 break;
						 case 13:
							 jsonObj.put("Type", type);
							 jsonObj.put("TypeName", "�ۿ�����");
							 break;
						 case 21:
							 jsonObj.put("Type", type);
							 jsonObj.put("TypeName", "ע��ί�з�");
							 break;
						 case 22:
							 jsonObj.put("Type", type);
							 jsonObj.put("TypeName", "ע��ί�е�λ");
							 break;
						default:
							 jsonObj.put("Type", type);
							 jsonObj.put("TypeName", "����");
							 break;
						
						}
							
						jsonObj.put("Type", type);
						jsonArray2.put(jsonObj);
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in ReasonServlet-->case 3", e);
				}else{
					log.error("error in ReasonServlet-->case 3", e);
				}
			} finally {
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray2.toString());
			}
			break;
			
		case 4://�������޸�
			JSONObject retObj4 = new JSONObject();
			try{
				String Id = req.getParameter("Id");
				Reason reason;
				if(Id.equals("")){
					reason = initReason(req, 0);
					boolean res1 = reasonMgr.save(reason);
					retObj4.put("IsOK", res1);
					retObj4.put("msg", res1?"��ӳɹ���":"���ʧ�ܣ���������ӣ�");
				}
				else{
					reason = initReason(req, Integer.valueOf(Id));
					boolean res2 = reasonMgr.update(reason);
					retObj4.put("IsOK", res2);
					retObj4.put("msg", res2?"�޸ĳɹ���":"�޸�ʧ�ܣ��������޸ģ�");
				}
			}catch(Exception e){
				try {
					retObj4.put("IsOK", false);
					retObj4.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in AddressServlet-->case 4", e);
				}else{
					log.error("error in AddressServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().write(retObj4.toString());
			}
			break;
		case 5://ע��
			JSONObject retObj5 = new JSONObject();
			try{
				String Id = req.getParameter("id");
				Reason addr = reasonMgr.findById(Integer.valueOf(Id));
				addr.setStatus(1);
				boolean res3 = reasonMgr.update(addr);
				retObj5.put("IsOK", res3);
				retObj5.put("msg", res3?"ע���ɹ���":"ע��ʧ�ܣ�������ע����");
			}catch(Exception e){
				try {
					retObj5.put("IsOK", false);
					retObj5.put("msg", String.format("����ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
	
	private Reason initReason(HttpServletRequest req, int id){
		Reason addr;
		if(id == 0){
			addr = new Reason();
			addr.setCount(0);
		}
		else{
			addr = (new ReasonManager()).findById(id);
		}
		
		String Reason = req.getParameter("Reason");
		String Status = req.getParameter("Status");
		String Type = req.getParameter("Type");
		
		addr.setReason(Reason);
		addr.setType(Integer.valueOf(Type));
		addr.setStatus(Integer.valueOf(Status));
		addr.setLastUse(new Timestamp(System.currentTimeMillis()));

		return addr;
	}
}
