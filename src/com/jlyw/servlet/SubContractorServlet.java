package com.jlyw.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.SubContractor;
import com.jlyw.hibernate.SysUser;
import com.jlyw.manager.ReasonManager;
import com.jlyw.manager.RegionManager;
import com.jlyw.manager.SubContractorManager;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.UIDUtil;

public class SubContractorServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(SubContractorServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		int method = Integer.valueOf(req.getParameter("method"));
		SubContractorManager subContractorMgr = new SubContractorManager();
		switch(method)
		{
		case 1://����һ��ת������¼
			SubContractor contractor = initSubContractor(req, 0);
			JSONObject retObj=new JSONObject();
			try {
				boolean res = subContractorMgr.save(contractor);
				retObj.put("IsOK", res);
				retObj.put("msg", res?"�½��ɹ���":"�½�ʧ�ܣ��������½���");
				retObj.put("Copy_filesetname", contractor.getCopy());
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractorServlet-->case 1", e);
				}else{
					log.error("error in SubContractorServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://��ѯת������¼
			String Name = req.getParameter("name");
			JSONObject res;
			int page = 0;
			if (req.getParameter("page") != null)
				page = Integer.parseInt(req.getParameter("page").toString());
			int rows = 0;
			if (req.getParameter("rows") != null)
				rows = Integer.parseInt(req.getParameter("rows").toString());
			List<SubContractor> result;
			int total;
			KeyValueWithOperator k = new KeyValueWithOperator("status", 0, "=");
				
			if(Name==null)
			{
				result = subContractorMgr.findPagedAll(page, rows, k);
				total = subContractorMgr.getTotalCount(k);
			}
			else
			{
				result = subContractorMgr.findPagedAll(page, rows, new KeyValueWithOperator("name", "%" + URLDecoder.decode(Name, "utf-8") + "%", "like"), k);
				total = subContractorMgr.getTotalCount(new KeyValueWithOperator("name", "%" + URLDecoder.decode(Name, "utf-8") + "%", "like"), k);
			}
			JSONArray options = new JSONArray();
			res = new JSONObject();
			try{
				for(SubContractor subcontractor : result)
				{
					JSONObject option = new JSONObject();
					option.put("Id", subcontractor.getId());
					option.put("Name", subcontractor.getName());
					option.put("Code", subcontractor.getCode());
					option.put("ZipCode", subcontractor.getZipCode());
					option.put("Brief", subcontractor.getBrief());
					option.put("Region", subcontractor.getRegion().getId());
					option.put("Address", subcontractor.getAddress());
					option.put("Tel", subcontractor.getTel());
					option.put("Contactor", subcontractor.getContactor());
					option.put("ContactorTel", subcontractor.getContactorTel());
					option.put("Status", subcontractor.getStatus());
					option.put("Remark", subcontractor.getRemark());
					option.put("CancelDate", subcontractor.getCancelDate()==null?"δע��":subcontractor.getCancelDate());
					option.put("CancelReason", subcontractor.getCancelDate()==null?"":subcontractor.getReason().getReason());
					option.put("ModifyDate", subcontractor.getModifyDate());
					option.put("Modificator", subcontractor.getSysUser().getName());
					if(subcontractor.getCopy()!=null&&subcontractor.getCopy()!="")
					{
						HashMap<String, Object> h = new HashMap<String, Object>();
						h.put(MongoDBUtil.KEYNAME_FileSetName, subcontractor.getCopy());
						JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
						option.put("Copy", file==null?"":file);
					}
					else
					{
						option.put("Copy", "");
					}
					
					options.put(option);
				}
				
				res.put("total", total);
				res.put("rows", options);
			}catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractorServlet-->case 2", e);
				}else{
					log.error("error in SubContractorServlet-->case 2", e);
				}
			}
			resp.setContentType("text/plain");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(res.toString());
			break;
		case 3://�޸�ת������¼
			JSONObject retObj1=new JSONObject();
			try {
				int id = Integer.valueOf(req.getParameter("edit_id"));
				SubContractor edit_subcontractor = initSubContractor(req, id);
				boolean res1 = subContractorMgr.update(edit_subcontractor);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"�޸ĳɹ���":"�޸�ʧ�ܣ��������޸ģ�");
				retObj1.put("Copy_filesetname", edit_subcontractor.getCopy());
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractorServlet-->case 3", e);
				}else{
					log.error("error in SubContractorServlet-->case 3", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj1.toString());
			break;
		case 4://ע��һ��ת����
			JSONObject retObj2=new JSONObject();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			try {
				SubContractor del_subcontractor = subContractorMgr.findById(Integer.valueOf(req.getParameter("del_id")));
				String Reason = req.getParameter("reason");
				ReasonManager rMgr = new ReasonManager();
				List<Reason> rList = rMgr.findByVarProperty(new KeyValueWithOperator("reason",Reason.trim(),"="), new KeyValueWithOperator("type", 22, "="));//����ע��ԭ��
				if(rList.size() > 0){	//����ԭ��
					Reason reason = rList.get(0);
					reason.setCount(reason.getCount()+1);
					reason.setLastUse(today);
					rMgr.update(reason);
					del_subcontractor.setReason(reason);	//ע��ԭ��
				}else{	//�½�ԭ��
					Reason reason = new Reason();
					reason.setCount(1);
					reason.setLastUse(today);
					reason.setReason(Reason.trim());
					reason.setStatus(0);
					reason.setType(21);	//ע��ת����
					rMgr.save(reason);
					del_subcontractor.setReason(reason);	//ע��ԭ��
				}
				del_subcontractor.setCancelDate(today);
				del_subcontractor.setStatus(1);
				del_subcontractor.setModifyDate(today);
				del_subcontractor.setSysUser((SysUser)req.getSession().getAttribute("LOGIN_USER"));
				boolean res2 = subContractorMgr.update(del_subcontractor);
				retObj2.put("IsOK", res2);
				retObj2.put("msg", res2?"ע���ɹ���":"ע��ʧ�ܣ�������ע����");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in SubContractorServlet-->case 4", e);
				}else{
					log.error("error in SubContractorServlet-->case 4", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj2.toString());
			break;
		}
	}
	
	private SubContractor initSubContractor(HttpServletRequest req,int id)
	{
		SubContractor contractor = new SubContractor();
		if(id==0)
		{
			contractor = new SubContractor();
			contractor.setCopy(UIDUtil.get22BitUID());
		}
		else
		{
			contractor = (new SubContractorManager()).findById(id);
		}
		
		String Name = req.getParameter("name");
		String Code = req.getParameter("code");
		int RegionId = Integer.valueOf(req.getParameter("region"));
		String ZipCode = req.getParameter("zipcode");
		String Address = req.getParameter("address");
		String Tel = req.getParameter("tel");
		String Contactor = req.getParameter("contactor");
		String ContactorTel = req.getParameter("contactorTel");
		int Status = Integer.valueOf(req.getParameter("status"));
		String Remark = req.getParameter("remark");
		
		contractor.setName(Name);
		contractor.setCode(Code);
		contractor.setBrief(LetterUtil.String2Alpha(Name));
		contractor.setRegion((new RegionManager()).findById(RegionId));
		contractor.setZipCode(ZipCode);
		contractor.setAddress(Address);
		contractor.setTel(Tel);
		contractor.setContactor(Contactor);
		contractor.setContactorTel(ContactorTel);
		contractor.setStatus(Status);
		contractor.setRemark(Remark);
		contractor.setModifyDate(new Timestamp(System.currentTimeMillis()));
		contractor.setSysUser((SysUser)req.getSession().getAttribute("LOGIN_USER"));
		
		return contractor;
	}

}
