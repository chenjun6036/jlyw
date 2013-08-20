package com.jlyw.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.SharingFolder;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.SharingFolderManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.UIDUtil;

public class SharingFolderServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(SharingFolderServlet.class);
	private static String ClassName = "SharingFolderServlet";
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));

		SharingFolderManager sharingfoldingMgr=new SharingFolderManager();
		switch(method)
		{
		
		case 1://新建文件夹
			JSONObject retObj = new JSONObject();
			try {
				SharingFolder sharingfolder=initSharingFolder(req, 0);
				boolean res1 = sharingfoldingMgr.save(sharingfolder);
				retObj.put("IsOK", res1);
				retObj.put("msg", res1?"新建成功！":"新建失败，请重新新建！");

			} catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj.toString());
			}
			
			break;
		case 2://查询所有文件夹名称，返回tree的json格式		
			String res=null;
			try {
				String parentid = req.getParameter("parentid");
				if(parentid==null)
					res = getTreeFolderJSON(0).toString();
				else
				{
					String parentidStr = new String(parentid.trim().getBytes("ISO-8859-1"), "GBK");
					res = getTreeFolderJSON(Integer.valueOf(parentidStr)).toString();
				}

			}catch(Exception e){
				log.error(String.format("error in %s", ClassName), e);
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");		
				resp.getWriter().write(res);
			}
			
			break;
		case 3://修改文件夹名称
			JSONObject retObj1 = new JSONObject();
			try {
				SharingFolder sharingfolder1=initSharingFolder(req,Integer.valueOf(req.getParameter("SpeciesId")));//要修改文件夹ID
	
				boolean res1 = sharingfoldingMgr.update(sharingfolder1);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
			}catch (Exception e) {
				log.error(String.format("error in %s", ClassName), e);
				try{
					retObj1.put("IsOK", false);
					retObj1.put("msg", String.format("修改失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
				}catch(Exception ee){}		
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj1.toString());
			}
			break;
		case 4://注销文件夹中所有文件及下属文件夹
			SharingFolder sharingfolder2 = sharingfoldingMgr.findById(Integer.valueOf(req.getParameter("id")));
			if(sharingfolder2.getSysUser() != null 
					&& !sharingfolder2.getSysUser().getId().equals(((SysUser)req.getSession().getAttribute("LOGIN_USER"))==null?0:((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId())){//操作人和创建人不是同一个人
				JSONObject retObj2 = new JSONObject();	
				try {	
					
					retObj2.put("IsOK", false);
					retObj2.put("msg", "删除失败，请重新删除！");
				} catch (Exception e) {
					log.error(String.format("error in %s", ClassName), e);
					try{
						retObj2.put("IsOK", false);
						retObj2.put("msg", String.format("删除失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
					}catch(Exception ee){}		
				}finally
				{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj2.toString());
				}
			}	
			else{
				JSONObject retObj2 = new JSONObject();				
				try {
					sharingfolder2.setStatus(1);					
					boolean res1 = sharingfoldingMgr.update(sharingfolder2);
					retObj2.put("IsOK", res1);
					retObj2.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
				} catch (Exception e) {
					log.error(String.format("error in %s", ClassName), e);
					try{
						retObj2.put("IsOK", false);
						retObj2.put("msg", String.format("删除失败!原因：%s", e.getMessage()==null?"":e.getMessage()));
					}catch(Exception ee){}		
				}finally
				{
					resp.setContentType("text/html;charset=utf-8");
					resp.getWriter().write(retObj2.toString());
				}
			}
			break;
		}

	}
	

	
	private JSONArray getTreeFolderJSON(int parentid)
	{
		JSONArray nodes = new JSONArray();
		SharingFolderManager sharingfoldingMgr=new SharingFolderManager();

		try{
			List<SharingFolder> folderList;
			if(parentid!=0)
				folderList = sharingfoldingMgr.findByVarProperty(new KeyValueWithOperator("sharingFolder.id", parentid, "="),new KeyValueWithOperator("status",0, "="));
			else
				folderList = sharingfoldingMgr.findByVarProperty(new KeyValueWithOperator("sharingFolder.id", null, "is null"),new KeyValueWithOperator("status",0, "="));

			for(SharingFolder temp : folderList)
			{
				JSONObject option = new JSONObject();
				option.put("id", temp.getId());
				option.put("text", temp.getName());
				JSONObject attr = new JSONObject();
				if(temp.getSharingFolder()!=null)    //获取父文件夹
					attr.put("ParentId", temp.getSharingFolder().getId());
				else
					attr.put("ParentId", 0);
				attr.put("type", 0);
				attr.put("FilesetName", temp.getFilesetName());
				attr.put("CreateTime", temp.getCreateTime());
				attr.put("CreatorName", temp.getSysUser()==null?"":temp.getSysUser().getName());
				option.put("attributes", attr);
				//children = getTreeJSON(temp.getId());
				//option.put("children", children);
				option.put("state", "closed");
				
				nodes.put(option);
			}
			
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return nodes;
	}
	
	private SharingFolder initSharingFolder(HttpServletRequest req, int id)
	{
		
		SharingFolder sharingfolder;
		if(id==0)
		{
			sharingfolder = new SharingFolder();
			sharingfolder.setFilesetName(UIDUtil.get22BitUID());
			String Name = req.getParameter("Name");
			int SpeciesId = Integer.valueOf(req.getParameter("SpeciesId"));//新建文件夹的父文件夹ID
			
			sharingfolder.setName(Name);
			sharingfolder.setSharingFolder((new SharingFolderManager()).findById(SpeciesId));
			sharingfolder.setStatus(0);
			
			//SysUser loginuser=(SysUser)req.getSession().getAttribute("LOGIN_USER");
			sharingfolder.setSysUser((SysUser)req.getSession().getAttribute("LOGIN_USER"));//创建文件夹的用户
			Timestamp today = new Timestamp(System.currentTimeMillis());
			sharingfolder.setCreateTime(today);
			sharingfolder.setFilesetName(UIDUtil.get22BitUID());//文件夹对应的文件集
		}
		else
		{
			sharingfolder = (new SharingFolderManager()).findById(id);  //通过文件夹ID找到要修改 的文件夹
			
			String Name = req.getParameter("Name");
			sharingfolder.setName(Name);
			
		}
		
		
		return sharingfolder;
	}
	
}
