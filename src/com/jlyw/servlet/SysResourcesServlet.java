package com.jlyw.servlet;

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
import org.json.me.JSONObject;

import com.jlyw.hibernate.SysResources;
import com.jlyw.manager.SysResourcesManager;
import com.jlyw.util.KeyValueWithOperator;

public class SysResourcesServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(SysResourcesServlet.class);
	private static String ClassName = "SysResourcesServlet";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		SysResourcesManager resourceMgr = new SysResourcesManager();
		switch (method) {
		case 1:// ��ѯϵͳ��Դ--Combobox
			JSONArray jsonArray = new JSONArray();
			try {
				List<SysResources> regList = resourceMgr
						.findByVarProperty(new KeyValueWithOperator("status",
								0, "="));
				if (regList != null) {
					for (SysResources reg : regList) {
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("name", reg.getName());
						jsonObj.put("id", reg.getId());
						jsonObj.put("url", reg.getMappingUrl());
						jsonArray.put(jsonObj);
					}
				}
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
			} finally {
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 2:// ����ϵͳ��Դ	
			JSONObject retObj = new JSONObject();
			// Ȩ����������
			try {
				SysResources sysResources = initResources(req, 0);
				boolean res = resourceMgr.save(sysResources);
				retObj.put("IsOK", res);
				retObj.put("msg", res ? "�����ɹ���" : "����ʧ�ܣ������´�����");
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 3:// ��ѯϵͳ��Դ
			JSONArray options = new JSONArray();
			JSONObject res = new JSONObject();
			try {
				String ResourcesName = req.getParameter("queryname");

				List<KeyValueWithOperator> list = new ArrayList<KeyValueWithOperator>();
				if (ResourcesName != null && ResourcesName != "") {
					String ResourcesNameStr = URLDecoder.decode(ResourcesName,
							"UTF-8");
					list.add(new KeyValueWithOperator("name", "%"
							+ ResourcesNameStr + "%", "like"));
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

				List<SysResources> result;
				int total;
				result = resourceMgr.findPagedAllBySort(page, rows,"name",false, list);
				total = resourceMgr.getTotalCount(list);

				for (SysResources Resources1 : result) {
					JSONObject option = new JSONObject();
					option.put("Id", Resources1.getId());
					option.put("Name", Resources1.getName());
					option.put("Description", Resources1.getDescription());
					option.put("MappingUrl", Resources1.getMappingUrl());
					option.put("Status", Resources1.getStatus());

					options.put(option);
				}
				res = new JSONObject();
				res.put("total", total);
				res.put("rows", options);

			} catch (Exception ex) {
				log.error(String.format("error in %s", ClassName), ex);
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 4:// �޸�ϵͳ��Դ
			JSONObject retObj1 = new JSONObject();
			try {
				String Name = req.getParameter("Name");
				String Description = req.getParameter("Description");
				String MappingUrl = req.getParameter("MappingUrl");
				int id1 = Integer.parseInt(req.getParameter("Id"));
				SysResources privilege1;
				privilege1 = resourceMgr.findById(id1);
				privilege1.setName(Name);
				privilege1.setDescription(Description);
				privilege1.setMappingUrl(MappingUrl);
				boolean res1 = resourceMgr.update(privilege1);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1 ? "�޸ĳɹ���" : "�޸�ʧ�ܣ��������޸ģ�");
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("�޸�ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());	
			}
			break;
		case 5:// ɾ��ϵͳ��Դ		
			JSONObject ret = new JSONObject();
			try {
				int id = Integer.parseInt(req.getParameter("id"));
				SysResources sysResources2 = resourceMgr.findById(id);
				sysResources2.setStatus(1);
				boolean res1 = resourceMgr.update(sysResources2);
				ret.put("IsOK", res1);
				ret.put("msg", res1 ? "ɾ���ɹ���" : "ɾ��ʧ�ܣ�������ɾ����");
			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					ret.put("IsOK", false);
					ret.put("msg", String.format("ɾ��ʧ��!ԭ��%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(ret.toString());	
			}
			break;
		}
	}

	private SysResources initResources(HttpServletRequest req, int id) {
		String Name = req.getParameter("ResourcesName");
		String Description = req.getParameter("Description");
		String MappingUrl = req.getParameter("MappingUrl");

		SysResources sysResources;
		if (id == 0) {
			sysResources = new SysResources();
		} else {
			SysResourcesManager resourceMgr = new SysResourcesManager();
			sysResources = resourceMgr.findById(id);
		}

		sysResources.setName(Name);
		sysResources.setDescription(Description);
		sysResources.setMappingUrl(MappingUrl);
		sysResources.setStatus(0);

		return sysResources;

	}

}
