package com.jlyw.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.manager.AddressManager;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.ExcelUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.UploadDownLoadUtil;
import com.jlyw.util.WordUtil;
import com.jlyw.util.mongodbService.MongoPattern;
import com.jlyw.util.xmlHandler.ParseXMLAll;

public class FileDownloadServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(FileDownloadServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		switch (method) {
		case 0: //根据文件ID下载文件
			try{
				String FileId = req.getParameter("FileId");
				String FileType = req.getParameter("FileType");
				MongoDBUtil.CollectionType type;
				switch(Integer.parseInt(FileType)){
				case UploadDownLoadUtil.Type_OriginalRecord:
					type = MongoDBUtil.CollectionType.OriginalRecord;
					break;
				case UploadDownLoadUtil.Type_Certificate:
					type = MongoDBUtil.CollectionType.Certificate;
					break;
				case UploadDownLoadUtil.Type_Template:
					type = MongoDBUtil.CollectionType.Template;
					break; 
				case UploadDownLoadUtil.Type_Attachment:
					type = MongoDBUtil.CollectionType.Attachment;
					break;
				case UploadDownLoadUtil.Type_Sharing:
					type = MongoDBUtil.CollectionType.Sharing;
					break;
				case UploadDownLoadUtil.Type_Others:
				default:
					type = MongoDBUtil.CollectionType.Others;
					break;
				}
				JSONObject retObj = MongoDBUtil.getFileInfoById(FileId, type);
				if(retObj != null){
					java.io.OutputStream outStream = resp.getOutputStream();
					String filename = retObj.getString(MongoDBUtil.KEYNAME_FileName);
					if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)  //firefox浏览器
					    filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
					else 
//					     if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)	//IE浏览器
					     filename = URLEncoder.encode(filename, "UTF-8");
					
					//向浏览器发消息头：  内容-设置,附件;文件名=(如果中文文件名) URLEncoader.encode() 中文编码
					resp.setHeader("Content-disposition", "attachment;filename=" + filename);
					try{
						MongoDBUtil.gridFSDownloadById(outStream, FileId, type);
					}catch(Exception e){
						log.debug("excpetion in FileDownloadServlet-->case 0-->MongoDBUtil.gridFSDownloadById", e);
					}finally{
						outStream.close();
					}
				}else{
					resp.setContentType("text/html;charset=GBK");
					resp.getWriter().print("指定文件不存在！");
					return;
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 0", e);
				}else{
					log.error("error in FileDownloadServlet-->case 0", e);
				}
			}
			break;
		case 1:	//根据FilesetName查找相应的文件信息列表(分页查找)
			JSONObject retJSON1 = null;
			try{
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				
				String FilesetName = req.getParameter("FilesetName");
				String FileType = req.getParameter("FileType");
				if(FilesetName == null || FileType == null){
					throw new Exception("参数不完整！");
				}
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				MongoDBUtil.CollectionType type;
				switch(Integer.parseInt(FileType)){
				case UploadDownLoadUtil.Type_OriginalRecord:
					type = MongoDBUtil.CollectionType.OriginalRecord;
					break;
				case UploadDownLoadUtil.Type_Certificate:
					type = MongoDBUtil.CollectionType.Certificate;
					break;
				case UploadDownLoadUtil.Type_Template:
					type = MongoDBUtil.CollectionType.Template;
					paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//增加文件有效的字段来查找
					break; 
				case UploadDownLoadUtil.Type_Attachment:
					type = MongoDBUtil.CollectionType.Attachment;
					break;
				case UploadDownLoadUtil.Type_Sharing:
					type = MongoDBUtil.CollectionType.Sharing;
					break;
				case UploadDownLoadUtil.Type_Others:
				default:
					type = MongoDBUtil.CollectionType.Others;
					break;
				} 
				retJSON1 = MongoDBUtil.getPageAllFileList(page, rows, paramsMap, type);
			}catch(Exception e){
				try {
					retJSON1 = new JSONObject();
					retJSON1.put("total", 0);
					retJSON1.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 1", e);
				}else{
					log.error("error in FileDownloadServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().println(retJSON1.toString());
				
			}
			break;
		case 2:	//下载原始记录文件（用于WebOffice）
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				OriginalRecord oRecord = new OriginalRecordManager().findById(Integer.parseInt(OriginalRecordId));
				if(oRecord.getOriginalRecordExcel() == null){
					throw new Exception("该原始记录尚未生成Excel文件！");
				}
				java.io.OutputStream outStream = resp.getOutputStream();
				MongoDBUtil.gridFSDownloadById(outStream, oRecord.getOriginalRecordExcel().getDoc(), MongoDBUtil.CollectionType.OriginalRecord);
				outStream.close();
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 2", e);
				}else{
					log.error("error in FileDownloadServlet-->case 2", e);
				}
			}
			break;
		case 3:	//下载原始记录模板
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String TemplateFileId = req.getParameter("TemplateFileId");	//Excel模板文件的ID
				String DownloadType = req.getParameter("DownloadType");	//下载类型：0：表示下载文件；1：表示下载至WebOffice中
				String VerifierName = req.getParameter("VerifierName");	//核验人员姓名
				if(VerifierName != null){
					VerifierName = new String(VerifierName.getBytes("ISO-8859-1"), "UTF-8");
				}
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				OriginalRecordManager recordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = recordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() != null){	//已存在原始记录Excel文件，则直接转向
					if(DownloadType.equals("1")){
						req.getRequestDispatcher("/FileDownloadServlet.do?method=2").forward(req, resp);
					}else{
						req.getRequestDispatcher(String.format("/FileDownloadServlet.do?method=0&FileType=%d&FileId=%s",
									UploadDownLoadUtil.Type_OriginalRecord,
									oRecord.getOriginalRecordExcel().getDoc())).forward(req, resp);
					}
					return;
				} 
				
				//核验人员
				SysUser verifier = null;
				if(VerifierName != null && VerifierName.trim().length() > 0 && !VerifierName.equals("-1")){
					UserManager userMgr = new UserManager();
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name", VerifierName, "="),
							new KeyValueWithOperator("status", 0, "="));
					if(userList != null && userList.size() > 0){
						verifier = userList.get(0);
					}
				}
				
				
				if(TemplateFileId == null || TemplateFileId.length() == 0){
					throw new Exception("参数不完整！");
				}
				
				JSONObject retObj = MongoDBUtil.getFileInfoById(TemplateFileId, MongoDBUtil.CollectionType.Template);
				if(retObj == null){
					throw new Exception("找不到指定的Excel模板文件！");
				}
				String fileId = retObj.getString("_id");	//模板文件的ID
				String fileName = retObj.getString(MongoDBUtil.KEYNAME_FileName);	//模板文件的文件名
				String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, retObj.getString(MongoDBUtil.KEYNAME_FileSetName));
				Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("未找到模板文件对应的字段定义文件:%s", xmlFileName));
				}
				String xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
				
				
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(oRecord.getCertificate() == null){
					//查询委托单下的第几份记录
					Integer sequence = recordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					Certificate c = new Certificate();
					c.setCode(UIDUtil.get22BitUID());
					c.setVersion(-1);
					c.setCommissionSheet(oRecord.getCommissionSheet());
					c.setOriginalRecord(oRecord);
					c.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					c.setDoc("");
					c.setSequece(sequence);
					c.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					c.setFileName("");
					if(!new CertificateManager().save(c)){
						throw new Exception("保存证书编号失败，请稍后再试！");
					}
					oRecord.setCertificate(c);
					if(!recordMgr.update(oRecord)){
						throw new Exception("保存证书编号失败，请稍后再试！");
					}
				}
				
			
				OutputStream osTemplate = null, osRecord = null;	//模板文件，xml定义文件，原始记录文件的文件流
				InputStream isTemplate = null, isRecord = null;
				File fTemplate = null, fXML = null, fRecord = null;
				try{
					//从文件数据库中取出模板文件
					fTemplate = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
					osTemplate = new FileOutputStream(fTemplate);
					if(!MongoDBUtil.gridFSDownloadById(osTemplate, fileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("下载模板文件失败！");
					}
					osTemplate.close();
					osTemplate = null;
					isTemplate = new FileInputStream(fTemplate);
					
					//从文件数据库中取出xml定义文件
					fXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("下载模板定义文件失败！");
					}
					ParseXMLAll parser = new ParseXMLAll(fXML);
					
					
					//查询技术规范、计量标准、标准器具
					String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
					List<Specification> speList = recordMgr.findByHQL(queryStringSpe, oRecord.getId());
					String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
					List<Standard> stdList = recordMgr.findByHQL(queryStringStd, oRecord.getId());
					String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
					List<StandardAppliance> stdAppList = recordMgr.findByHQL(queryStringStdApp, oRecord.getId());
					//生成原始记录
					fRecord = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
					osRecord = new FileOutputStream(fRecord);
					ExcelUtil.downloadExcel(isTemplate, osRecord, 
							oRecord.getCommissionSheet(), oRecord, oRecord.getSysUserByStaffId(), 
							speList, stdList, stdAppList, 
							oRecord.getCertificate(), oRecord.getTargetAppliance().getApplianceStandardName(), verifier,
							parser);
					osRecord.close();
					osRecord = null;
					
					isRecord = new FileInputStream(fRecord);
					java.io.OutputStream outStream = resp.getOutputStream();
					byte[] buf = new byte[2048];
					if(DownloadType.equals("1")){//装载原始记录文件(WebOffice)
						//无操作
					}else{	//下载文件
						String downloadFileName = fileName;
						if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)  //firefox浏览器
						    downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
						else 
//						     if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)	//IE浏览器
						     downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
						
						//向浏览器发消息头：  内容-设置,附件;文件名=(如果中文文件名) URLEncoader.encode() 中文编码
						resp.setHeader("Content-disposition", "attachment;filename=" + downloadFileName);
					}
					int iRead = isRecord.read(buf);
					while(iRead != -1){
						outStream.write(buf, 0, iRead);
						iRead = isRecord.read(buf);
					}
					outStream.close();					
				}catch(Exception re){
					throw re;
				}finally{	//关闭文件流;删除临时文件
					if(osTemplate != null){
						osTemplate.close();
						osTemplate = null;
					}
					if(osRecord != null){
						osRecord.close();
						osRecord = null;
					}
					if(isTemplate != null){
						isTemplate.close();
						isTemplate = null;
					}
					if(isRecord != null){
						isRecord.close();
						isRecord = null;
					}
					if(fTemplate != null && fTemplate.exists()){
						fTemplate.delete();
					}
					if(fXML != null && fXML.exists()){
						fXML.delete();
					}
					if(fRecord != null && fRecord.exists()){
						fRecord.delete();
					}
				}
			}catch(Exception e){
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().print(String.format("警告：加载模板文件失败！失败信息:%s", e.getMessage() == null?"无":e.getMessage()));
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 3", e);
				}else{
					log.error("error in FileDownloadServlet-->case 3", e);
				}
			}
			break;
		case 4:	//根据FilesetName查找相应的文件信息列表（不分页）
			JSONObject retJSON4 = new JSONObject();
			try{
				JSONArray jsonArray = null;
				String FilesetName = req.getParameter("FilesetName");
				String FileType = req.getParameter("FileType");
				if(FilesetName == null || FileType == null){
					throw new Exception("参数不完整！");
				}
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				switch(Integer.parseInt(FileType)){
				case UploadDownLoadUtil.Type_OriginalRecord:
					jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.OriginalRecord);
					break;
				case UploadDownLoadUtil.Type_Certificate:
					jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Certificate);
					break;
				case UploadDownLoadUtil.Type_Template:
					paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//增加文件有效的字段来查找
					jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Template);
					break; 
				case UploadDownLoadUtil.Type_Attachment:
					jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Attachment);
					break;
				case UploadDownLoadUtil.Type_Sharing:
					jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Sharing);
					break;
				case UploadDownLoadUtil.Type_Others:
				default:
					jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Others);
					break;
				} 
				retJSON4.put("total", jsonArray == null?0:jsonArray.length());
				retJSON4.put("rows", jsonArray == null?new JSONArray():jsonArray);
			}catch(Exception e){
				try {
					retJSON4.put("total", 0);
					retJSON4.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 4", e);
				}else{
					log.error("error in FileDownloadServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	//根据FilesetName查找相应的原始记录模板Excel文件文件信息列表
			JSONObject retJSON5 = new JSONObject();
			try{
				JSONArray jsonArray = null;
				String FilesetName = req.getParameter("FilesetName");
				if(FilesetName == null){
					throw new Exception("参数不完整！");
				}
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				Pattern pattern = MongoPattern.compile(".+\\.xlsx?$", Pattern.CASE_INSENSITIVE);	//正则表达式:以.xls或者.xlsx结尾的
				paramsMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
				paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//增加文件有效的字段来查找
				jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Template);
				
				retJSON5.put("total", jsonArray == null?0:jsonArray.length());
				retJSON5.put("rows", jsonArray == null?new JSONArray():jsonArray);
			}catch(Exception e){
				try {
					retJSON5.put("total", 0);
					retJSON5.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 5", e);
				}else{
					log.error("error in FileDownloadServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6: //查找证书模板Doc文件信息列表(用于选择证书模板 )
			JSONObject retJSON6 = null;
			try{
				int page = 0;	//当前页面
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//页面大小
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				
				String FilesetName = SystemCfgUtil.CertificateTemplateFilesetName;	//证书模板文件的文件集名称
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				Pattern pattern = MongoPattern.compile(".+\\.docx?$", Pattern.CASE_INSENSITIVE);	//正则表达式:以.doc或者.docx结尾的
				paramsMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
				paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//增加文件有效的字段来查找
				
				retJSON6 = MongoDBUtil.getPageAllFileList(page, rows, paramsMap, MongoDBUtil.CollectionType.Template);
				
				
			}catch(Exception e){
				try {
					retJSON6 = new JSONObject();
					retJSON6.put("total", 0);
					retJSON6.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 6", e);
				}else{
					log.error("error in FileDownloadServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
			
		case 7:	//下载证书文件（用于WebOffice）
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				OriginalRecord oRecord = new OriginalRecordManager().findById(Integer.parseInt(OriginalRecordId));
				if(oRecord.getCertificate() == null || oRecord.getCertificate().getDoc().length() == 0){
					throw new Exception("该原始记录尚未生成证书文件！");
				}
				java.io.OutputStream outStream = resp.getOutputStream();
				MongoDBUtil.gridFSDownloadById(outStream, oRecord.getCertificate().getDoc(), MongoDBUtil.CollectionType.Certificate);
				outStream.close();
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 7", e);
				}else{
					log.error("error in FileDownloadServlet-->case 7", e);
				}
			}
			break;
		case 8:	//下载证书模板（默认至weboffice中）
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String DownloadType = req.getParameter("DownloadType");	//下载类型：0：表示下载文件；1：表示下载至WebOffice中
				String VersionStr = req.getParameter("Version");//证书的版本号
				String XlsTemplateFileId = req.getParameter("XlsTemplateFileId");	//原始记录Excel模板文件ID（用于拷贝证书数据页）
				
				
				StringBuilder alertString=new StringBuilder();//字数超过限制时的提示信息
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(DownloadType == null || DownloadType.length() == 0){
					DownloadType = "1";
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager recordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = recordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() !=  null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
					throw new Exception("该记录已上传原始记录Excel文件，不能直接编制证书！");
				}
				
				if(oRecord.getCertificate() != null && oRecord.getCertificate().getDoc().length() > 0){	//已存在证书Doc文件，则直接转向
					if(DownloadType.equals("1")){
						req.getRequestDispatcher("/FileDownloadServlet.do?method=7").forward(req, resp);
					}else{
						req.getRequestDispatcher(String.format("/FileDownloadServlet.do?method=0&FileType=%d&FileId=%s",
									UploadDownLoadUtil.Type_Certificate,
									oRecord.getCertificate().getDoc())).forward(req, resp);
					}
					return;
				}
				
				//获取证书模板文件名
				String docTemplateFileName = ExcelUtil.getCertificateModFileName(oRecord.getWorkType(), oRecord.getConclusion());
				if(docTemplateFileName == null || docTemplateFileName.length() == 0){
					throw new Exception("获取证书模板文件名错误，请检查该记录的‘工作性质’及‘结论’是否正确！");
				}
				
				HashMap<String, Object> searchMap = new HashMap<String, Object>();
				searchMap.clear();
				searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
				Pattern patternCertificate = MongoPattern.compile("^"+docTemplateFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
				searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
				searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
				
				JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("未找到证书模板文件：%s", docTemplateFileName));
				}
				String fileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板文件的ID
				String fileName = ((JSONObject)retArray.get(0)).getString(MongoDBUtil.KEYNAME_FileName);	//证书模板文件的文件名
				String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//证书模板配置文件的名称
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, ((JSONObject)retArray.get(0)).getString(MongoDBUtil.KEYNAME_FileSetName));
				Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
				JSONArray retArray2 = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray2.length() == 0){
					throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", xmlFileName));
				}
				String xmlFileId = ((JSONObject)retArray2.get(0)).getString("_id");//xml文件的ID
				
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(oRecord.getCertificate() == null){
					//查询委托单下的第几份记录
					Integer sequence = recordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					Certificate c = new Certificate();
					c.setCode(UIDUtil.get22BitUID());
					c.setVersion(-1);
					c.setCommissionSheet(oRecord.getCommissionSheet());
					c.setOriginalRecord(oRecord);
					c.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					c.setDoc("");
					c.setSequece(sequence);
					c.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					c.setFileName(fileName);
					if(!new CertificateManager().save(c)){
						throw new Exception("保存证书编号失败，请稍后再试！");
					}
					oRecord.setCertificate(c);
					if(!recordMgr.update(oRecord)){
						throw new Exception("保存证书编号失败，请稍后再试！");
					}
				}
				
				
				
				
				File fDocTemplate = null, fDocXML = null, fDocOut = null;//证书模板文件，证书xml定义文件, 证书文件
				File fXlsTemplate = null, fXlsXML = null;	//原始记录Excel模板文件、Excel对应 的XML定义文件
				File fPicWorkStaff = null;	//检验员的签名图片文件
				InputStream isDocTemplate = null;
				try{
					//从文件数据库中取出模板文件
					fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
					if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, fileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("下载证书模板文件失败！");
					}
					
					//从文件数据库中取出证书xml定义文件
					fDocXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fDocXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("下载证书模板配置文件失败！");
					}
					ParseXMLAll docParser = new ParseXMLAll(fDocXML);
					
					ParseXMLAll xlsParser = null;
					if(XlsTemplateFileId != null && XlsTemplateFileId.trim().length() > 0){
						JSONObject retObj = MongoDBUtil.getFileInfoById(XlsTemplateFileId, MongoDBUtil.CollectionType.Template);
						if(retObj != null){
							//throw new Exception("找不到指定的Excel模板文件！");
						
						
						String xlsFileId = retObj.getString("_id");	//模板文件的ID
						String xlsFileName = retObj.getString(MongoDBUtil.KEYNAME_FileName);	//模板文件的文件名
						String xlsXmlFileName = String.format("%s.xml", xlsFileName.substring(0, xlsFileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
						HashMap<String, Object> xlsMap = new HashMap<String, Object>();
						xlsMap.put(MongoDBUtil.KEYNAME_FileSetName, retObj.getString(MongoDBUtil.KEYNAME_FileSetName));
						Pattern xlsPattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
						xlsMap.put(MongoDBUtil.KEYNAME_FileName, xlsPattern);
						xlsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");
						JSONArray xlsRetArray = MongoDBUtil.getFileList(xlsMap, MongoDBUtil.CollectionType.Template);
						if(xlsRetArray.length() == 0){
							throw new Exception(String.format("未找到模板文件对应的字段定义文件:%s", xlsXmlFileName));
						}
						String xlsXmlFileId = ((JSONObject)xlsRetArray.get(0)).getString("_id");//xml文件的ID
						
						//从文件数据库中取出Excel模板文件
						fXlsTemplate = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.substring(xlsFileName.lastIndexOf(".")));
						if(!MongoDBUtil.gridFSDownloadById(fXlsTemplate, xlsFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("下载Excel模板文件失败！");
						}
						
						//从文件数据库中取出Excel xml定义文件
						fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
						if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("下载Excel模板配置文件失败！");
						}
						xlsParser = new ParseXMLAll(fXlsXML);
						}
					}
					
					
					HashMap<String, Object> picParams = new HashMap<String, Object>();
					if(oRecord.getSysUserByStaffId() != null && oRecord.getSysUserByStaffId().getSignature() != null){	//获取检验人员的签名图片
						picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
						JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
						if(jsonInfo != null){
							String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
							fPicWorkStaff = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
							MongoDBUtil.gridFSDownloadById(fPicWorkStaff, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
						}
					}
					
					//查询技术规范、计量标准、标准器具
					String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
					List<Specification> speList = recordMgr.findByHQL(queryStringSpe, oRecord.getId());
					String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
					List<Standard> stdList = recordMgr.findByHQL(queryStringStd, oRecord.getId());
					String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
					List<StandardAppliance> stdAppList = recordMgr.findByHQL(queryStringStdApp, oRecord.getId());
					
					fDocOut = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
					
					WordUtil.MakeCertificateWord(fXlsTemplate, fDocTemplate, fDocOut, null, 
							xlsParser, docParser,
							(fPicWorkStaff!=null&&fPicWorkStaff.exists()&&fPicWorkStaff.length()>0)?fPicWorkStaff.getAbsolutePath():null,
							oRecord.getCommissionSheet(), oRecord, oRecord.getCertificate(), 
							new AddressManager().findById(oRecord.getCommissionSheet().getHeadNameId()),
							speList, stdList, stdAppList,alertString);
					if(!fDocOut.exists() || fDocOut.length() == 0){
						throw new Exception("证书文件生成失败！");
					}
					
					isDocTemplate = new FileInputStream(fDocOut);
					java.io.OutputStream outStream = resp.getOutputStream();
					byte[] buf = new byte[2048];
					if(DownloadType.equals("1")){//装载原始记录文件(WebOffice)
						//无操作
					}else{	//下载文件
						String downloadFileName = fileName;
						if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)  //firefox浏览器
						    downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
						else 
//						     if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)	//IE浏览器
						     downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
						
						//向浏览器发消息头：  内容-设置,附件;文件名=(如果中文文件名) URLEncoader.encode() 中文编码
						resp.setHeader("Content-disposition", "attachment;filename=" + downloadFileName);
					}
					int iRead = isDocTemplate.read(buf);
					while(iRead != -1){
						outStream.write(buf, 0, iRead);
						iRead = isDocTemplate.read(buf);
					}
					outStream.close();					
				}catch(Exception re){
					re.printStackTrace();
					throw re;
				}finally{	//关闭文件流;删除临时文件
					if(isDocTemplate != null){
						isDocTemplate.close();
						isDocTemplate = null;
					}
					if(fDocTemplate != null && fDocTemplate.exists()){
						fDocTemplate.delete();
						
					}
					if(fDocXML != null && fDocXML.exists()){
						fDocXML.delete();
					}
					if(fDocOut != null && fDocOut.exists()){
						fDocOut.delete();
					}
					if(fXlsTemplate != null && fXlsTemplate.exists()){
						fXlsTemplate.delete();
					}
					if(fXlsXML != null && fXlsXML.exists()){
						fXlsXML.delete();
					}
					if(fPicWorkStaff != null && fPicWorkStaff.exists()){
						fPicWorkStaff.delete();
					}
				}
			}catch(Exception e){
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().print(String.format("警告：加载模板文件失败！失败信息:%s", e.getMessage() == null?"无":e.getMessage()));
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 8", e);
				}else{
					log.error("error in FileDownloadServlet-->case 8", e);
				}
			}
			break;
		case 9:	//修改证书（默认至weboffice中）
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String DownloadType = req.getParameter("DownloadType");	//下载类型：0：表示下载文件；1：表示下载至WebOffice中
				String VersionStr = req.getParameter("Version");//证书的版本号
				String XlsTemplateFileId = req.getParameter("XlsTemplateFileId");	//原始记录Excel模板文件ID（用于拷贝证书数据页）
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(DownloadType == null || DownloadType.length() == 0){
					DownloadType = "1";
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager recordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = recordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
//				
//				if(oRecord.getOriginalRecordExcel() !=  null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
//					throw new Exception("该记录已上传原始记录Excel文件，不能直接编制证书！");
//				}
//				
				if(oRecord.getCertificate() != null && oRecord.getCertificate().getDoc().length() > 0){	//已存在证书Doc文件，则直接转向
					if(DownloadType.equals("1")){
						req.getRequestDispatcher("/FileDownloadServlet.do?method=7").forward(req, resp);
					}else{
						req.getRequestDispatcher(String.format("/FileDownloadServlet.do?method=0&FileType=%d&FileId=%s",
									UploadDownLoadUtil.Type_Certificate,
									oRecord.getCertificate().getDoc())).forward(req, resp);
					}
					return;
				}
				else{
					throw new Exception("该记录尚未生成证书文件！");
				}
					
			}catch(Exception e){
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().print(String.format("警告：加载模板文件失败！失败信息:%s", e.getMessage() == null?"无":e.getMessage()));
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 9", e);
				}else{
					log.error("error in FileDownloadServlet-->case 9", e);
				}
			}
			break;
		}
	}
}
