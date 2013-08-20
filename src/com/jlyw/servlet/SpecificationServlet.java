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

import com.jlyw.hibernate.BaseType;
import com.jlyw.hibernate.Reason;
import com.jlyw.hibernate.Specification;
import com.jlyw.manager.BaseTypeManager;
import com.jlyw.manager.SpecificationManager;
import com.jlyw.manager.StdTgtAppManager;
import com.jlyw.manager.TgtAppSpecManager;
import com.jlyw.util.ExportUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValue;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.UIDUtil;

public class SpecificationServlet extends HttpServlet{
	private static final Log log = LogFactory.getLog(SpecificationServlet.class);
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Integer method = Integer.valueOf(req.getParameter("method"));
		SpecificationManager spemag = new SpecificationManager();
		switch(method){
		case 1://添加规范
			Specification specification = initSpecification(req, 0);
			JSONObject retObj=new JSONObject();
			try {
				Date today = new Date(System.currentTimeMillis());
				if(specification.getEffectiveDate().before(today)||specification.getEffectiveDate().equals(today)){
					specification.setStatus(0);
					boolean res = spemag.save(specification);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"新建成功！":"新建失败！");
				}
				else{
					specification.setStatus(2);//未到实施日期的新规程状态
					boolean res= spemag.save(specification);
					retObj.put("IsOK", res);
					retObj.put("msg", res?"新建成功！因未到实施日期，该规程暂时不能使用，将在实施日到达时开始使用！":"新建失败！");
				}
				retObj.put("file_upload_filesetname", specification.getCopy());
				//retObj.put("file_upload_filesetname", specification.getCopy());
				//retObj.put("methodConfirm_filesetname", specification.getMethodConfirm());
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SpecificationServlet-->case 1", e);
				}else{
					log.error("error in SpecificationServlet-->case 1", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj.toString());
			break;
		case 2://查询规范
			JSONObject res = new JSONObject();
			try {
				String queryCode = req.getParameter("queryCode");
				String queryName = req.getParameter("queryName");
				String queryStatus = req.getParameter("queryStatus");
				String queryInCharge = req.getParameter("queryInCharge");
				String queryType = req.getParameter("queryType");
				String Warn = req.getParameter("Warn");
				
				String queryStr = "from Specification as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				if(queryCode != null&&!queryCode.equals(""))
				{
					String specCodeStr = URLDecoder.decode(queryCode, "UTF-8");
					String[] querycodeStr = specCodeStr.split(" \\s+");	//根据空格符分割
					if(querycodeStr.length == 0){
						return;
					}
					specCodeStr = "";
					for(int i = 0; i < querycodeStr.length; i++){
						specCodeStr += querycodeStr[i];
						if(i != querycodeStr.length-1)
							specCodeStr += "%";
					}
					specCodeStr = "%" + specCodeStr + "%";
					keys.add("%" + specCodeStr + "%");
					queryStr = queryStr + " and model.specificationCode like ?";
				}
				if(queryName != null&&!queryName.equals(""))
				{
					String specNameStr = URLDecoder.decode(queryName, "UTF-8");
					keys.add("%" + specNameStr + "%");
					keys.add("%" + specNameStr + "%");
					queryStr = queryStr + " and (model.nameCn like ? or model.brief like ?)";
				}
				if(queryStatus != null&&!queryStatus.equals(""))
				{
					String specStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
					keys.add(LetterUtil.isNumeric(specStatusStr)?Integer.valueOf(specStatusStr):0);
					queryStr = queryStr + " and model.status = ? ";
				}
				if(queryInCharge != null&&!queryInCharge.equals(""))
				{
					String specInchargeStr = URLDecoder.decode(queryInCharge, "UTF-8");
					keys.add(specInchargeStr.equals("0")?true:false);
					queryStr = queryStr + " and model.inCharge = ? ";
				}
				if(queryType != null&&!queryType.equals(""))
				{
					String specTypeStr = URLDecoder.decode(queryType, "UTF-8");
					keys.add(specTypeStr);
					queryStr = queryStr + " and model.type = ? ";
				}
				if(Warn != null&&!Warn.equals(""))
				{
					queryStr = queryStr + " and model.repealDate >= ? and DATEADD(dd,0-model.warnSlot,model.repealDate) <= ?";
					keys.add(new Date(System.currentTimeMillis()));
					keys.add(new Date(System.currentTimeMillis()));
				}
				
				int page = 1;
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				List<Specification> result;
				int total;
				
				
				result = spemag.findPageAllByHQL(queryStr + "order by model.status asc, model.specificationCode asc", page, rows, keys);
				total = spemag.getTotalCountByHQL("select count(*) " + queryStr, keys);
				JSONArray options = new JSONArray();
			
				for (Specification spe : result) {
					JSONObject option = new JSONObject();
					option.put("Id", spe.getId());
					option.put("SpecificationCode", spe.getSpecificationCode());
					option.put("NameCn", spe.getNameCn());
					option.put("NameEn", spe.getNameEn());
					option.put("Brief", spe.getBrief());
					option.put("Type", spe.getType());
					option.put("InCharge", spe.getInCharge()?0:1);
					option.put("Status", spe.getStatus());
					option.put("LocaleCode", spe.getLocaleCode());
					option.put("IssueDate", spe.getIssueDate());
					option.put("EffectiveDate", spe.getEffectiveDate());
					option.put("RepealDate", spe.getRepealDate());
					option.put("OldSpecification", spe.getOldSpecification());
					option.put("WarnSlot", spe.getWarnSlot());
					option.put("Remark", spe.getRemark());
					option.put("filesetname", spe.getCopy());

					/*if(spe.getCopy()!=null&&spe.getCopy()!="")
					{
						HashMap<String, Object> h = new HashMap<String, Object>();
						h.put(MongoDBUtil.KEYNAME_FileSetName, spe.getCopy());
						JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
						option.put("Copy", file==null?"":file);
					}
					else
					{
						option.put("Copy", "");
					}
					if(spe.getMethodConfirm()!=null&&spe.getMethodConfirm()!="")
					{
						HashMap<String, Object> h = new HashMap<String, Object>();
						h.put(MongoDBUtil.KEYNAME_FileSetName, spe.getMethodConfirm());
						JSONObject file = MongoDBUtil.getLastUploadFileInfo(h, MongoDBUtil.CollectionType.Others);
						option.put("MethodConfirm", file==null?"":file);
					}
					else
					{
						option.put("MethodConfirm", "");
					}*/
										
					options.put(option);
				}
				res.put("total", total);
				res.put("rows", options);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SpecificationServlet-->case 2", e);
				}else{
					log.error("error in SpecificationServlet-->case 2", e);
				}
			}finally{
				resp.setContentType("text/json");
				resp.setCharacterEncoding("UTF-8");
				resp.getWriter().write(res.toString());
			}
			break;
		case 3://更新规范
			int id = Integer.valueOf(req.getParameter("Id"));
			Specification spe = initSpecification(req, id);
			JSONObject retObj1=new JSONObject();
			try {
				boolean res1 = spemag.update(spe);
				retObj1.put("IsOK", res1);
				retObj1.put("msg", res1?"修改成功！":"修改失败，请重新修改！");
				retObj1.put("file_upload_filesetname", spe.getCopy());
				//retObj1.put("copy_filesetname", spe.getCopy());
				//retObj1.put("methodConfirm_filesetname", spe.getMethodConfirm());
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SpecificationServlet-->case 3", e);
				}else{
					log.error("error in SpecificationServlet-->case 3", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(retObj1.toString());
			break;
		case 4://删除规范
			Specification spe1 = spemag.findById(Integer.valueOf(req.getParameter("id")));
			spe1.setStatus(1);
			JSONObject ret=new JSONObject();
			try {
				boolean res1 = spemag.update(spe1);
				ret.put("IsOK", res1);
				ret.put("msg", res1?"删除成功！":"删除失败，请重新删除！");
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SpecificationServlet-->case 4", e);
				}else{
					log.error("error in SpecificationServlet-->case 4", e);
				}
			}
			resp.setContentType("text/html;charset=utf-8");
			resp.getWriter().write(ret.toString());
			break;
		case 5:
			JSONArray jsonArray = new JSONArray();
			try {
				List<Specification> List = spemag.findPagedAll(1, 30, new KeyValueWithOperator("status", 0, "="));
				if(List != null){
					for(Specification spec : List){
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("name", spec.getNameCn());
						jsonObj.put("name_num", spec.getNameCn()+"-"+spec.getSpecificationCode());
						jsonObj.put("id", spec.getId());
						jsonArray.put(jsonObj);	
					}
				}
			}catch (Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SpecificationServlet-->case 5", e);
				}else{
					log.error("error in SpecificationServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray.toString());
			}
			break;
		case 6://导出
			String paramsStr = req.getParameter("paramsStr");
			JSONObject retObj7 = new JSONObject();
			try {
				JSONObject params = new JSONObject(paramsStr);
				String queryStr = "from Specification as model where 1=1 ";
				List<Object> keys = new ArrayList<Object>();
				if(params.length()!=0)
				{
					String queryCode = params.has("queryCode")?params.getString("queryCode"):"";
					String queryName = params.has("queryName")?params.getString("queryName"):"";
					String queryStatus = params.has("queryStatus")?params.getString("queryStatus"):"";
					String queryInCharge = params.has("queryInCharge")?params.getString("queryInCharge"):"";
					String queryType = params.has("queryType")?params.getString("queryType"):"";
					String Warn = params.has("Warn")?params.getString("Warn"):"";
					
					if(queryCode != null&&!queryCode.equals(""))
					{
						String specCodeStr = URLDecoder.decode(queryCode, "UTF-8");
						keys.add("%" + specCodeStr + "%");
						queryStr = queryStr + " and model.specificationCode like ?";
					}
					if(queryName != null&&!queryName.equals(""))
					{
						String specNameStr = URLDecoder.decode(queryName, "UTF-8");
						keys.add("%" + specNameStr + "%");
						keys.add("%" + specNameStr + "%");
						queryStr = queryStr + " and (model.nameCn like ? or model.brief like ?)";
					}
					if(queryStatus != null&&!queryStatus.equals(""))
					{
						String specStatusStr = URLDecoder.decode(queryStatus, "UTF-8");
						keys.add(LetterUtil.isNumeric(specStatusStr)?Integer.valueOf(specStatusStr):0);
						queryStr = queryStr + " and model.status = ? ";
					}
					if(queryInCharge != null&&!queryInCharge.equals(""))
					{
						String specStatusStr = URLDecoder.decode(queryInCharge, "UTF-8");
						keys.add(specStatusStr.equals("0")?true:false);
						queryStr = queryStr + " and model.inCharge = ? ";
					}
					if(queryType != null&&!queryType.equals(""))
					{
						String specTypeStr = URLDecoder.decode(queryType, "UTF-8");
						keys.add(specTypeStr);
						queryStr = queryStr + " and model.type = ? ";
					}
					if(Warn != null&&!Warn.equals(""))
					{
						queryStr = queryStr + " and model.repealDate >= ? and DATEADD(dd,0-model.warnSlot,model.repealDate) <= ?";
						keys.add(new Date(System.currentTimeMillis()));
						keys.add(new Date(System.currentTimeMillis()));
					}
				}
				String filePath = ExportUtil.ExportToExcel(queryStr , keys, null, "formatExcel", "formatTitle", SpecificationManager.class);				
				retObj7.put("IsOK", filePath.equals("")?false:true);
				retObj7.put("Path", filePath);
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in SpecificationServlet-->case 6", e);
				}else{
					log.error("error in SpecificationServlet-->case 6", e);
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
		case 7://规程combobox模糊查询，返回前30个
			String specStr = req.getParameter("SpecificationName");	//
			JSONArray jsonArray10 = new JSONArray();
			try {
				if(specStr != null && specStr.trim().length() > 0){
					String specName =  new String(specStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					specName = LetterUtil.String2Alpha(specName);	//返回字母简码
					String[] queryName = specName.split(" \\s*");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					specName = "";
					for(int i = 0; i < queryName.length; i++){
						specName += queryName[i];
						if(i != queryName.length-1)
							specName += "%";
					}
					specName = "%" + specName + "%";
					String queryString = String.format("from Specification as model where (model.brief like ? or model.specificationCode like ?) and model.status = ?");
					List<Specification> retList  = spemag.findPageAllByHQL(queryString, 1, 30, specName, specName, Integer.valueOf(0));
					if(retList != null){
						for(Specification temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name", temp.getSpecificationCode() + "--" + temp.getNameCn());
							jsonObj.put("id", temp.getId());
							jsonArray10.put(jsonObj);		
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in SpecificaitonServlet-->case 7", e);
				}else{
					log.error("error in SpecificaitonServlet-->case 7", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray10.toString());
			}
			break;
		case 8://用新规程替代旧规程，并更新关系记录
			JSONObject retObj8 = new JSONObject();
			try{
				String old_id = req.getParameter("Id");
				Specification newSpec = initSpecification(req, 0);
				Specification oldSpec = spemag.findById(Integer.valueOf(old_id));
				Date today = new Date(System.currentTimeMillis());
				newSpec.setOldSpecification(oldSpec.getSpecificationCode());
				if(newSpec.getEffectiveDate().before(today)||newSpec.getEffectiveDate().equals(today)){
					newSpec.setStatus(0);
					boolean res1 = spemag.replace(newSpec);
					retObj8.put("IsOK", res1);
					retObj8.put("msg", res1?"替代成功！":"替代失败！");
				}
				else{
					newSpec.setStatus(2);//未到实施日期的新规程状态
					boolean res1 = spemag.save(newSpec);
					retObj8.put("IsOK", res1);
					retObj8.put("msg", res1?"新建成功！因未到实施日期，该规程暂时不能使用，将在实施日到达时开始使用！":"新建失败！");
				}
			}catch(Exception e) {
				try {
					retObj8.put("IsOK", false);
					retObj8.put("msg", String.format("替代失败，请重新尝试替代！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in SpecificationServlet-->case 8", e);
				}else{
					log.error("error in SpecificationServlet-->case 8", e);
				} 
			}finally{
				resp.setContentType("text/html;charset=utf-8");
				resp.getWriter().write(retObj8.toString());
			}
			break;
		case 9://规程combobox模糊查询，返回前30个，在“直接编辑证书”之前的选择标准器具，计量规程等中用到
			String specfiStr = req.getParameter("SpecificationName");	//
			JSONArray jsonArray9 = new JSONArray();
			try {
				if(specfiStr != null && specfiStr.trim().length() > 0){
					String specName =  new String(specfiStr.trim().getBytes("ISO-8859-1"), "GBK");	//解决URL传递中文乱码问题
					specName = LetterUtil.String2Alpha(specName);	//返回字母简码
					String[] queryName = specName.split(" \\s*");	//根据空格符分割
					if(queryName.length == 0){
						return;
					}
					specName = "";
					for(int i = 0; i < queryName.length; i++){
						specName += queryName[i];
						if(i != queryName.length-1)
							specName += "%";
					}
					specName = "%" + specName + "%";
					String queryString = String.format("from Specification as model where (model.brief like ? or model.specificationCode like ?) and model.status = ?");
					List<Specification> retList  = spemag.findPageAllByHQL(queryString, 1, 30, specName, specName, Integer.valueOf(0));
					if(retList != null){
						for(Specification temp : retList){
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("name_num", temp.getNameCn() + "-" + temp.getSpecificationCode());							
							jsonObj.put("id", temp.getId());
							jsonObj.put("specificationcode", temp.getSpecificationCode());
							jsonObj.put("namecn", temp.getNameCn());
							jsonObj.put("type", temp.getType());
							jsonObj.put("localecode", temp.getLocaleCode());
							jsonObj.put("issuedate", temp.getIssueDate());
							jsonObj.put("effectivedate", temp.getEffectiveDate());
							jsonObj.put("repealdate", temp.getRepealDate());
							
							jsonArray9.put(jsonObj);		
						}
					}
				}
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){ //自定义的消息
					log.debug("exception in SpecificaitonServlet-->case 9", e);
				}else{
					log.error("error in SpecificaitonServlet-->case 9", e);
				} 
			}finally{
				resp.setContentType("text/json;charset=gbk");
				resp.getWriter().write(jsonArray9.toString());
			}
			break;
		}
	}
	
	private Specification initSpecification(HttpServletRequest req,int id)
	{
		Specification specification;		
		if(id == 0)
		{
			specification = new Specification();
			specification.setCopy(UIDUtil.get22BitUID());
			specification.setMethodConfirm(UIDUtil.get22BitUID());
		}
		else
		{
			SpecificationManager spemag = new SpecificationManager();
			specification = spemag.findById(id);
		}
		
		String SpecificationCode = req.getParameter("SpecificationCode");
		String NameCn = req.getParameter("NameCn");
		String Brief = req.getParameter("Brief");
		String NameEn = req.getParameter("NameEn");
		String Type = req.getParameter("Type");
		int InCharge = Integer.valueOf(req.getParameter("InCharge"));
		int Status = Integer.valueOf(req.getParameter("Status"));
		String LocaleCode = req.getParameter("LocaleCode");
		Date IssueDate = Date.valueOf(req.getParameter("IssueDate"));
		Date EffectiveDate = Date.valueOf(req.getParameter("EffectiveDate"));
		String RepealDate = req.getParameter("RepealDate");
		String OldSpecification = req.getParameter("OldSpecification");
		String Remark = req.getParameter("Remark");
		String WarnSlot = req.getParameter("WarnSlot");

		BaseTypeManager baseTypeMgr = new BaseTypeManager();
		List<BaseType> retList = baseTypeMgr.findByVarProperty(new KeyValueWithOperator("name",Type.trim(),"="), new KeyValueWithOperator("type", FlagUtil.BaseTypeInfoType.Type_SpecificationType, "="));//查找类型
		BaseType baseType = retList.get(0);
		specification.setType(baseType.getName());	
		
		specification.setSpecificationCode(SpecificationCode);
		specification.setNameCn(NameCn);
		specification.setNameEn(NameEn);
		specification.setBrief(Brief);
		//specification.setType(Type);
		specification.setInCharge(InCharge==0?true:false);
		specification.setStatus(Status);
		specification.setLocaleCode(LocaleCode);
		specification.setIssueDate(IssueDate);
		specification.setEffectiveDate(EffectiveDate);
		if(RepealDate!=null&&!RepealDate.equals(""))
			specification.setRepealDate(Date.valueOf(RepealDate));
		specification.setOldSpecification(OldSpecification);
		if(Remark!=null)
			specification.setRemark(Remark);
		if(WarnSlot!=null)
			specification.setWarnSlot(Integer.valueOf(WarnSlot));
		
		
		return specification;
	}

}
