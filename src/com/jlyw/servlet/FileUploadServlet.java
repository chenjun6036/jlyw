package com.jlyw.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Transaction;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.jlyw.hibernate.ApplianceManufacturer;
import com.jlyw.hibernate.ApplianceManufacturerDAO;
import com.jlyw.hibernate.AppliancePopularName;
import com.jlyw.hibernate.AppliancePopularNameDAO;
import com.jlyw.hibernate.ApplianceStandardName;
import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.CertificateDAO;
import com.jlyw.hibernate.CommissionSheet;
import com.jlyw.hibernate.HibernateSessionFactory;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.OriginalRecordDAO;
import com.jlyw.hibernate.OriginalRecordExcel;
import com.jlyw.hibernate.OriginalRecordExcelDAO;
import com.jlyw.hibernate.OriginalRecordStandards;
import com.jlyw.hibernate.OriginalRecordStandardsDAO;
import com.jlyw.hibernate.OriginalRecordStdAppliances;
import com.jlyw.hibernate.OriginalRecordStdAppliancesDAO;
import com.jlyw.hibernate.OriginalRecordTechnicalDocs;
import com.jlyw.hibernate.OriginalRecordTechnicalDocsDAO;
import com.jlyw.hibernate.RemakeCertificate;
import com.jlyw.hibernate.Specification;
import com.jlyw.hibernate.Standard;
import com.jlyw.hibernate.StandardAppliance;
import com.jlyw.hibernate.SysUser;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.hibernate.VerifyAndAuthorizeDAO;
import com.jlyw.manager.AddressManager;
import com.jlyw.manager.ApplianceManufacturerManager;
import com.jlyw.manager.AppliancePopularNameManager;
import com.jlyw.manager.ApplianceStandardNameManager;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.CommissionSheetManager;
import com.jlyw.manager.OriginalRecordExcelManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.manager.QualificationManager;
import com.jlyw.manager.RemakeCertificateManager;
import com.jlyw.manager.UserManager;
import com.jlyw.util.ExcelUtil;
import com.jlyw.util.FlagUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.LetterUtil;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.Office2PdfUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.UploadDownLoadUtil;
import com.jlyw.util.WordUtil;
import com.jlyw.util.mongodbService.MongoPattern;
import com.jlyw.util.xmlHandler.ParseXMLAll;
import com.jspsmart.upload.SmartUpload;

public class FileUploadServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(FileUploadServlet.class);
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(request.getParameter("method"));
		switch(method){
		case 1:	//普通文件上传
			JSONObject retJSON = new JSONObject();
			try{
				String FilesetName = request.getParameter("FilesetName");
				String FileType = request.getParameter("FileType");
				if(FilesetName == null || FileType == null || FilesetName.length() == 0 || FileType.length() == 0){
					throw new Exception("文件上传参数不完整！");
				}
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, loginUser == null?"":loginUser.getId().toString());
				paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, loginUser == null?"":loginUser.getName());
				
				StringBuffer failFileNames = new StringBuffer();	//上传失败的文件名字
				if (ServletFileUpload.isMultipartContent(request)) {
					MongoDBUtil.CollectionType type = null;
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
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");
					FileItemIterator fii = sfu.getItemIterator(request);
					HashMap<String, Object> searchMap = new HashMap<String, Object>();	//查找条件
					searchMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
					
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//含完整路径的文件名
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//取到无路径的纯文件名
							
							if(fileName.contains("-") || fileName.contains("%") || fileName.contains("$") || fileName.contains("^") || fileName.contains("&")){
								throw new Exception(String.format("文件 %s 上传失败，原因：文件名不允许包含'-'、'%%'、'$'、'^'、'&'等特殊符号！", fileName));
							}
							
							//查询条件
							Pattern pattern = MongoPattern.compile("^"+fileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
							searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
							
							if(searchMap.containsKey(MongoDBUtil.KEYNAME_FileStatus)){
								searchMap.remove(MongoDBUtil.KEYNAME_FileStatus);
							}
							if(paramsMap.containsKey(MongoDBUtil.KEYNAME_FileStatus)){
								paramsMap.remove(MongoDBUtil.KEYNAME_FileStatus);
							}
							
							/******       根据需要查询是否有相同的文件名已存在             *******/
							switch(Integer.parseInt(FileType)){
							case UploadDownLoadUtil.Type_OriginalRecord:
								break;
							case UploadDownLoadUtil.Type_Certificate:
								break;
							case UploadDownLoadUtil.Type_Template:
							case UploadDownLoadUtil.Type_Attachment:
							case UploadDownLoadUtil.Type_Sharing:
							case UploadDownLoadUtil.Type_Others:
							default:
								if(Integer.parseInt(FileType) == UploadDownLoadUtil.Type_Template){
									searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");
								}
								if(MongoDBUtil.getFileList(searchMap, type).length() > 0){
									throw new Exception(String.format("文件 %s 已存在，请勿重复上传！", fileName));
								}
								break;
							}
							
							//上传
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
							if(Integer.parseInt(FileType) == UploadDownLoadUtil.Type_Template){
								paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：正常
							}
							InputStream in = fis.openStream();
							try{
								if(!MongoDBUtil.gridFSUpload(in, paramsMap, type)){
									failFileNames.append(fileName).append(";");
								}
							}catch(Exception ex){
								failFileNames.append(fileName).append(String.format("%s;", ex.getMessage()==null?"":(":"+ex.getMessage())));
							}finally{
								in.close();
							}
						}
					}
				}
				
				if(failFileNames.length() == 0){
					retJSON.put("IsOK", true);
				}else{
					retJSON.put("IsOK", false);
					retJSON.put("msg", String.format("错误提示！上传失败的文件：%s", failFileNames.toString()));
				}
			}catch(Exception e){
				
				try {
					retJSON.put("IsOK", false);
					retJSON.put("msg", String.format("文件上传失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 1", e);
				}else{
					log.error("error in FileUploadServlet-->case 1", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON.toString());
			}
			break;
		case 2:	//WebOffice上传原始记录（正式）xlsEdit
			JSONObject retJSON2=new JSONObject();
			try{
				// 初始化上传组件  
				SmartUpload mySmartUpload=new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// 获取上传表单记录		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //原始记录的ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//原始记录的版本号
		        
				StringBuilder alertString = new StringBuilder();//字数超过限制时的提示信息
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				
				if(FileName.contains("-")){	//文件名：模板文件名-序列号.扩展名
					int index1 = FileName.indexOf('-');
					int index2 = FileName.lastIndexOf('.');
					if(index2 > index1){
						FileName = FileName.substring(0, index1) + FileName.substring(index2, FileName.length());
					}
				}
				
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("所编辑的原始记录并非最新版本，请重新下载最新版本的原始记录并在其上编辑！");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null ){
					throw new Exception("您尚未登录，不能提交原始记录！");
				}if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该原始记录已经生成正式的证书！您不是该任务所分配的检验人，不能再次提交原始记录！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次提交原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次提交原始记录！");
					}
				}
				
				String xlsXmlFileId = null;
				if(oRecord.getOriginalRecordExcel() != null && 
						oRecord.getOriginalRecordExcel().getFileName() != null &&
						oRecord.getOriginalRecordExcel().getFileName().equalsIgnoreCase(FileName) &&
						oRecord.getOriginalRecordExcel().getXml().length() > 0){	//使用原XML
					xlsXmlFileId = oRecord.getOriginalRecordExcel().getXml();
				}else{	//使用现行的模板定义文件
					//查找字段定义xml文件
					String xmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
					HashMap<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
					Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
					searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
					searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
					
					JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
					if(retArray.length() == 0){
						throw new Exception(String.format("未找到字段定义文件：%s", xmlFileName));
					}
					xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
				}
				if(xlsXmlFileId == null){
					throw new Exception(String.format("未找到字段定义文件：%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
				}
				
				
				//从文件数据库中取出xml定义文件
				File fXlsXML = null;
				ParseXMLAll xlsParser = null;
				try{
					fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception(String.format("获取字段定义文件失败：%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
					}
					xlsParser = new ParseXMLAll(fXlsXML);
				}catch(Exception e){
					throw e;
				}finally{
					if(fXlsXML != null && fXlsXML.exists()){
						fXlsXML.delete();
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//待删除的证书Doc文件、Excel文件（非正式版的）
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要更新证书
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(-1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName("");
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//该原始记录的证书为正式版本：证书需要重新生成，版本加1
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName("");
					certificate.setPdf(null);
				}else if(certificate.getDoc().length() > 0){	//不是正式版本且存在证书Doc文件：则生成正式版本的证书后，将老版本的证书Doc文件删除
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				//核验和授权签字记录，用于从方法中带出相关人员,如果vNew的ID为null，则新增一个记录，否则，更新原记录
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				if(vNew == null){
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
					vNew.setCommissionSheet(oRecord.getCommissionSheet());
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setSysUserByCreatorId(loginUser);
				}else{
					vNew.setAuthorizeRemark(null);
					vNew.setAuthorizeResult(null);
					vNew.setAuthorizeTime(null);
					vNew.setCertificate(null);
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setIsAuthBgRuning(null);
					vNew.setOriginalRecordExcel(null);
					vNew.setSysUserByAuthorizeExecutorId(null);
					vNew.setSysUserByAuthorizerId(null);
					vNew.setSysUserByCreatorId(loginUser);
					vNew.setSysUserByVerifierId(null);
					vNew.setVerifyRemark(null);
					vNew.setVerifyResult(null);
					vNew.setVerifyTime(null);
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//重新生成一份核验和授权签字记录：版本号+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				
				//处理上传的文件
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					
					File fXlsInput = null, fXlsOut = null, fXlsPdf = null;
					File fXlsOutToPdf = null;	//用于生成PDF的临时Excel文件
					File fDocTemplate = null, fDocXml = null, fDocOut = null, fDocPdf = null;
					File fPicWorkStaff = null;	//检校人员的签名图片
					try{
						fXlsInput = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						myFile.saveAs(fXlsInput.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
						
						//查询技术规范、计量标准、标准器具
						String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
						List<Specification> speList = oRecordMgr.findByHQL(queryStringSpe, oRecord.getId());
						String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
						List<Standard> stdList = oRecordMgr.findByHQL(queryStringStd, oRecord.getId());
						String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
						List<StandardAppliance> stdAppList = oRecordMgr.findByHQL(queryStringStdApp, oRecord.getId());
						
						StringBuffer docModeFileNameBuffer = new StringBuffer();
						fXlsOut = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						//上传Excel校验、处理
						ExcelUtil.uploadExcel(fXlsInput, fXlsOut, 
								oRecord.getCommissionSheet(), oRecord, oRecord.getSysUserByStaffId(), 
								certificate, oRecord.getTargetAppliance().getApplianceStandardName(), 
								speList, stdList, stdAppList, vNew, docModeFileNameBuffer, xlsParser);
						
						if(!fXlsOut.exists() || fXlsOut.length() == 0){
							throw new Exception("校验、处理上传的Excel文件失败！");
						}
						fXlsOutToPdf = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						ExcelUtil.removeAdditionalInfo(fXlsOut, fXlsOutToPdf, xlsParser);	//移除Excel文件中的附加信息
						fXlsPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//生成的Excel pdf文件
						if(fXlsOutToPdf.exists() && fXlsOutToPdf.length() > 0){		//去除额外信息
							Office2PdfUtil.docToPdf(fXlsOutToPdf, fXlsPdf);
						}else{
							Office2PdfUtil.docToPdf(fXlsOut, fXlsPdf);
						}
						if(!fXlsPdf.exists() || fXlsPdf.length() == 0){
							throw new Exception("生成原始记录Excel的PDF文件失败！");
						}
												
						//判断是否需要生成证书
						if(docModeFileNameBuffer.toString().length() > 0){	//生成证书
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							searchMap.clear();
							String docModFileName = docModeFileNameBuffer.toString();
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
							Pattern patternCertificate = MongoPattern.compile("^"+docModFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
							
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("未找到证书模板文件：%s", docModFileName));
							}
							String docModeFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板文件的ID
							
							String docXmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//证书模板配置文件的名称
							patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", docXmlFileName));
							}
							String docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板xml文件的ID
							
							//从文件数据库中取出模板文件
							fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
							if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, docModeFileId, MongoDBUtil.CollectionType.Template)){
								throw new Exception("下载证书模板文件失败！");
							}
							//从文件数据库中取出证书xml定义文件
							fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
							if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
								throw new Exception("下载证书模板配置文件失败！");
							}
							ParseXMLAll docParser = new ParseXMLAll(fDocXml);
							fDocOut = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
							fDocPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
							
							/*******        获取检定员签名图片         ********/
							HashMap<String, Object> picParams = new HashMap<String, Object>();
							if(oRecord.getSysUserByStaffId().getSignature() != null){	//获取检定人员的签名图片
								picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
								JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
								if(jsonInfo != null){
									String picFilename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
									fPicWorkStaff = File.createTempFile(UIDUtil.get22BitUID(), picFilename.substring(picFilename.lastIndexOf('.')>0?picFilename.lastIndexOf('.'):0));
									MongoDBUtil.gridFSDownloadById(fPicWorkStaff, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
								}
							}
							WordUtil.MakeCertificateWord(fXlsOut, fDocTemplate, fDocOut, fDocPdf, 
									xlsParser, docParser,
									(fPicWorkStaff!=null&&fPicWorkStaff.exists()&&fPicWorkStaff.length()>0)?fPicWorkStaff.getAbsolutePath():null,
									oRecord.getCommissionSheet(), oRecord, certificate, 
									new AddressManager().findById(oRecord.getCommissionSheet().getHeadNameId()),
									speList, stdList, stdAppList,alertString);
							if(!fDocOut.exists() || fDocOut.length() == 0){
								throw new Exception("证书文件生成失败！");
							}
							if(!fDocPdf.exists() || fDocPdf.length() == 0){
								throw new Exception("证书Word文件生成PDF失败！");
							}
							certificate.setXml(docXmlFileId);
							certificate.setFileName(docModFileName);
						}else{
							certificate.setFileName("");
						}
						
						/**************上传数据库(Excel文件，Excel的PDF文件，Word文件，Word的PDF文件)**************/
						//判断是否需要新增OriginalRecordExcel
						Integer VersionInt = 0;
						boolean isNewExcel = false;		//标志位，用于更新数据库时判断
						String excelCode = null;
						if(oRecord.getOriginalRecordExcel() == null){	//尚未有过原始记录
							excelCode = UIDUtil.get22BitUID();	//文件集名称
							VersionInt = 0;	//第一版版本号从0开始
							isNewExcel = true;
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//已经是正式版的，需要重新生成版本
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
						}else{	//替换之前的非正式版
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, excelCode);//文件集名称
						
						
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
						if(!MongoDBUtil.gridFSUpload(fXlsOut, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
							throw new Exception("保存原始记录Excel文件至服务器失败！");
						}
						String xlsIdStr = paramsMap.get("_id").toString();	//Excel文件的ID
						if(paramsMap.containsKey("_id")){
							paramsMap.remove("_id");
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", FileName.substring(0, FileName.lastIndexOf('.'))));
						if(!MongoDBUtil.gridFSUpload(fXlsPdf, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
							throw new Exception("保存原始记录Excel的PDF文件至服务器失败！");
						}
						String xlsPdfIdStr = paramsMap.get("_id").toString();	//Excel PDF文件的ID
						
						if(fDocOut != null){	//保存证书文件、证书PDF文件
							if(paramsMap.containsKey("_id")){
								paramsMap.remove("_id");
							}
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, certificate.getFileName());
							if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
								throw new Exception("保存证书文件至服务器失败！");
							}
							String docIdStr = paramsMap.get("_id").toString();	//证书文件的ID
							if(paramsMap.containsKey("_id")){
								paramsMap.remove("_id");
							}
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", certificate.getFileName().substring(0, certificate.getFileName().lastIndexOf("."))));
							if(!MongoDBUtil.gridFSUpload(fDocPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
								throw new Exception("保存证书文件至服务器失败！");
							}
							String docPdfIdStr = paramsMap.get("_id").toString();	//证书PDF文件的ID
							certificate.setDoc(docIdStr);
							certificate.setPdf(docPdfIdStr);
						}
						
						/***********更新数据库数据库 ：oRecord、oRecordExcel、Certificate、Vertificate**********/
						//更新OriginalRecordExcel
						OriginalRecordExcel excel = null;
						if(isNewExcel){
							excel = new OriginalRecordExcel();
						}else{
							excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
						}
						excel.setCode(excelCode);
						excel.setCommissionSheet(oRecord.getCommissionSheet());
						excel.setOriginalRecord(oRecord);
						excel.setDoc(xlsIdStr);
						excel.setPdf(xlsPdfIdStr);
						excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						excel.setVersion(VersionInt);	//版本号
						excel.setXml(xlsXmlFileId);
						excel.setFileName(FileName);
						excel.setCertificateCode(certificate.getCertificateCode());
						
						
						//更新Certificate
						if(certificate.getVersion() < 0){
							certificate.setVersion(0);	//版本号为0开始，-1为预留证书编号
						}
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//上传人员为检定员本人
							certificate.setSysUser(null);
						}else{
							certificate.setSysUser(loginUser);
						}
						
						//更新授权和签字记录
						vNew.setCertificate(certificate);
						vNew.setOriginalRecordExcel(excel);
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						
						oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//更新数据库
						try{
							//更新常用名称
							if(oRecord.getApplianceName() != null && oRecord.getApplianceName().trim().length() > 0 && !oRecord.getApplianceName().equals(oRecord.getTargetAppliance().getApplianceStandardName().getName())){
								AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();
								int iPopNameRet = popNameMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", oRecord.getTargetAppliance().getApplianceStandardName().getId(), "="), 
										new KeyValueWithOperator("popularName", oRecord.getApplianceName(), "="));
								if(iPopNameRet == 0){
									AppliancePopularName popNameTemp = new AppliancePopularName();
									popNameTemp.setApplianceStandardName(oRecord.getTargetAppliance().getApplianceStandardName());
									popNameTemp.setPopularName(oRecord.getApplianceName());
									popNameTemp.setBrief(LetterUtil.String2Alpha(oRecord.getApplianceName()));
									popNameTemp.setStatus(0);
									popNameMgr.save(popNameTemp);
								}
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 2-->更新常用名称", eee);
						}
						try{
							//更新生产厂商
							if(oRecord.getManufacturer() != null && oRecord.getManufacturer().trim().length() > 0){
								ApplianceManufacturerManager manufacturerMgr = new ApplianceManufacturerManager();
								int iManufacturerRet = manufacturerMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", oRecord.getTargetAppliance().getApplianceStandardName().getId(), "="), 
										new KeyValueWithOperator("manufacturer", oRecord.getManufacturer(), "="));
								if(iManufacturerRet == 0){
									ApplianceManufacturer manufacturerTemp = new ApplianceManufacturer();
									manufacturerTemp.setApplianceStandardName(oRecord.getTargetAppliance().getApplianceStandardName());
									manufacturerTemp.setManufacturer(oRecord.getManufacturer());
									manufacturerTemp.setBrief(LetterUtil.String2Alpha(oRecord.getManufacturer()));
									manufacturerTemp.setStatus(0);
									manufacturerMgr.save(manufacturerTemp);
								}
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 2-->更新生产厂商", eee);
						}
						
						//按需删除文件数据库中过时的原始记录
						try{
							if(toBeDeleteFileIdExcel != null){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 2-->Delete file from mongoDB(原始记录Excel id:"+toBeDeleteFileIdExcel+")", eee);
						}
						try{
							if(toBeDeleteFileIdCertificate != null){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 2-->Delete file from mongoDB(证书Word id:"+toBeDeleteFileIdCertificate+")", eee);
						}
						
					}catch(Exception ex){
						throw ex;
					}finally{
						if(fXlsInput != null && fXlsInput.exists()){
							fXlsInput.delete();
						}
						if(fXlsOut != null && fXlsOut.exists()){
							fXlsOut.delete();
						}
						if(fXlsPdf != null && fXlsPdf.exists())
							fXlsPdf.delete();
						
						if(fXlsOutToPdf != null && fXlsOutToPdf.exists()){
							fXlsOutToPdf.delete();
						}
						if(fPicWorkStaff != null && fPicWorkStaff.exists()){
							fPicWorkStaff.delete();
						}
						
						if(fDocTemplate != null && fDocTemplate.exists()){
							fDocTemplate.delete();
						}
						if(fDocXml != null && fDocXml.exists()){
							fDocXml.delete();
						}
						if(fDocOut != null && fDocOut.exists())
							fDocOut.delete();
						
						if(fDocPdf != null && fDocPdf.exists()){
							fDocPdf.delete();
						}
					}
				}else{
					throw new Exception("文件上传失败！");
				}

				retJSON2.put("IsOK", true);
				retJSON2.put("msg", "上传原始记录成功！"+ ((alertString==null||alertString.toString().trim().length()==0)?"":(alertString.toString()+"请自行调整")));
			}catch(Exception e){
				
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("上传原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 2", e);
				}else{
					log.error("error in FileUploadServlet-->case 2", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //WebOffice上传原始记录（暂存）:通过SmartUpload上传
			JSONObject retJSON3 = new JSONObject();
			try{
				// 初始化上传组件  
				SmartUpload mySmartUpload=new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// 获取上传表单记录		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //原始记录的ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//原始记录的版本号
		        
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				
				if(FileName.contains("-")){	//文件名：模板文件名-序列号.扩展名
					int index1 = FileName.indexOf('-');
					int index2 = FileName.lastIndexOf('.');
					if(index2 > index1){
						FileName = FileName.substring(0, index1) + FileName.substring(index2, FileName.length());
					}
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("所编辑的原始记录并非最新版本，请重新下载最新版本的原始记录并在其上编辑！");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null ){
					throw new Exception("您尚未登录，不能暂存原始记录！");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该原始记录已经生成正式的证书！您不是该任务所分配的检验人，不能再次暂存原始记录！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次提交原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次提交原始记录！");
					}
				}
				
				String xmlFileId = null;
				if(oRecord.getOriginalRecordExcel() != null && 
						oRecord.getOriginalRecordExcel().getFileName() != null &&
						oRecord.getOriginalRecordExcel().getFileName().equalsIgnoreCase(FileName) &&
						oRecord.getOriginalRecordExcel().getXml().length() > 0){	//使用原XML
					xmlFileId = oRecord.getOriginalRecordExcel().getXml();
				}else{	//使用现行的模板定义文件
					//查找字段定义xml文件
					String xmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
					HashMap<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
					Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
					searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
					searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
					
					JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
					if(retArray.length() == 0){
						throw new Exception(String.format("未找到对应的字段定义文件：%s，不能暂存该文件！", xmlFileName));
					}
					xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
				}
				if(xmlFileId == null){
					throw new Exception(String.format("未找到对应的字段定义文件：%s.xml，不能暂存该文件！", FileName.substring(0, FileName.lastIndexOf("."))));
				}
				
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要更新证书
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(-1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName("");
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//该原始记录的证书为正式版本：证书需要重新生成，版本加1
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName("");
					certificate.setPdf(null);
				}
				
				//核验和授权签字记录，用于从方法中带出相关人员,如果vNew的ID为null，则新增一个记录，否则，更新原记录
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				if(vNew == null){
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
					vNew.setCommissionSheet(oRecord.getCommissionSheet());
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setSysUserByCreatorId(loginUser);
				}else{
					vNew.setAuthorizeRemark(null);
					vNew.setAuthorizeResult(null);
					vNew.setAuthorizeTime(null);
					vNew.setCertificate(null);
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setIsAuthBgRuning(null);
					vNew.setOriginalRecordExcel(null);
					vNew.setSysUserByAuthorizeExecutorId(null);
					vNew.setSysUserByAuthorizerId(null);
					vNew.setSysUserByCreatorId(loginUser);
					vNew.setSysUserByVerifierId(null);
					vNew.setVerifyRemark(null);
					vNew.setVerifyResult(null);
					vNew.setVerifyTime(null);
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//重新生成一份核验和授权签字记录：版本号+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				
				/******************        上传文件至文件数据库中（暂存：不校验和处理文件）          *************************/
				//处理上传的文件
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, loginUser.getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, loginUser.getName());
					
					File tempInputFile = null;
					try{
						tempInputFile = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						myFile.saveAs(tempInputFile.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
						if(!tempInputFile.exists() || tempInputFile.length() == 0){
							throw new Exception("文件暂存失败！原因：上传文件出错。");
						}
						
						Integer VersionInt = 0;
						String toBeDeletedFileId = null;
						boolean isNewExcel = false;		//标志位，用于更新数据库时判断
						if(oRecord.getOriginalRecordExcel() == null){	//尚未有过原始记录
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, UIDUtil.get22BitUID());//文件集名称
							VersionInt = 0;	//第一版版本号从0开始
							isNewExcel = true;
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//已经是正式版的，需要重新生成版本
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
						}else{	//替换之前的非正式版
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeletedFileId = oRecord.getOriginalRecordExcel().getDoc();
						}
						
						if(!MongoDBUtil.gridFSUpload(tempInputFile, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
							throw new Exception("保存文件至服务器失败！");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Excel文件的ID
						
						/******************         更新数据库            ***********************/
						//更新oRecord、oRecordExcel、Certificate
						OriginalRecordExcel excel = null;
						if(isNewExcel){
							excel = new OriginalRecordExcel();
						}else{
							excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
						}
						excel.setCode((String)paramsMap.get(MongoDBUtil.KEYNAME_FileSetName));
						excel.setCommissionSheet(oRecord.getCommissionSheet());
						excel.setOriginalRecord(oRecord);
						excel.setDoc(docIdStr);
						excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						excel.setVersion(VersionInt);	//版本号
						excel.setXml(xmlFileId);
						excel.setFileName(FileName);
						
						//更新授权和签字记录
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						
						oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//更新数据库
						
						//按需删除文件数据库中过时的原始记录
						try{
							if(toBeDeletedFileId != null){
								MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 3-->Delete file from mongoDB(原始记录Excel id:"+toBeDeletedFileId+")", eee);
						}
					}catch(Exception ex){
						throw ex;
					}
				}else{
					throw new Exception("文件上传失败！");
				}
				
				retJSON3.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("上传原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 3", e);
				}else{
					log.error("error in FileUploadServlet-->case 3", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON3.toString());
			}
			break;
		case 4:	//附件方式上传原始记录（正式）
			JSONObject retJSON4=new JSONObject();
			try{
				StringBuilder alertString = new StringBuilder();//字数超过限制时的提示信息
				// 获取上传表单记录
				String OriginalRecordId = request.getParameter("OriginalRecordId");		//原始记录的ID
				String VersionStr = request.getParameter("Version");	//原始记录的版本号
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("原始记录版本冲突，请刷新查看最新版本的原始记录！");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null ){
					throw new Exception("您尚未登录，不能提交原始记录！");
				}if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该原始记录已经生成正式的证书！您不是该任务所分配的检验人，不能再次提交原始记录！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次提交原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次提交原始记录！");
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//待删除的证书Doc文件、Excel文件（非正式版的）
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要更新证书
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(-1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName("");
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//该原始记录的证书为正式版本：证书需要重新生成，版本加1
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName("");
					certificate.setPdf(null);
				}else if(certificate.getDoc().length() > 0){	//不是正式版本且存在证书Doc文件：则生成正式版本的证书后，将老版本的证书Doc文件删除
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				
				//核验和授权签字记录，用于从方法中带出相关人员,如果vNew的ID为null，则新增一个记录，否则，更新原记录
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				if(vNew == null){
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
					vNew.setCommissionSheet(oRecord.getCommissionSheet());
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setSysUserByCreatorId(loginUser);
				}else{
					vNew.setAuthorizeRemark(null);
					vNew.setAuthorizeResult(null);
					vNew.setAuthorizeTime(null);
					vNew.setCertificate(null);
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setIsAuthBgRuning(null);
					vNew.setOriginalRecordExcel(null);
					vNew.setSysUserByAuthorizeExecutorId(null);
					vNew.setSysUserByAuthorizerId(null);
					vNew.setSysUserByCreatorId(loginUser);
					vNew.setSysUserByVerifierId(null);
					vNew.setVerifyRemark(null);
					vNew.setVerifyResult(null);
					vNew.setVerifyTime(null);
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//重新生成一份核验和授权签字记录：版本号+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				
				//处理上传的文件
				if (ServletFileUpload.isMultipartContent(request)) {
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");

					
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					
					
					FileItemIterator fii = sfu.getItemIterator(request);
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//含完整路径的文件名
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//取到无路径的纯文件名
							
							if(fileName.contains("-")){	//文件名：模板文件名-序列号.扩展名
								int index1 = fileName.indexOf('-');
								int index2 = fileName.lastIndexOf('.');
								if(index2 > index1){
									fileName = fileName.substring(0, index1) + fileName.substring(index2, fileName.length());
								}
							}
							
							String xlsXmlFileId = null;
							if(oRecord.getOriginalRecordExcel() != null && 
									oRecord.getOriginalRecordExcel().getFileName() != null &&
									oRecord.getOriginalRecordExcel().getFileName().equalsIgnoreCase(fileName) &&
									oRecord.getOriginalRecordExcel().getXml().length() > 0){	//使用原XML
								xlsXmlFileId = oRecord.getOriginalRecordExcel().getXml();
							}else{	//使用现行的模板定义文件
								//查找字段定义xml文件
								String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
								HashMap<String, Object> searchMap = new HashMap<String, Object>();
								searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
								Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
								searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
								searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
								
								JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
								if(retArray.length() == 0){
									throw new Exception(String.format("未找到字段定义文件：%s", xmlFileName));
								}
								xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
							}
							if(xlsXmlFileId == null){
								throw new Exception(String.format("未找到字段定义文件：%s.xml", fileName.substring(0, fileName.lastIndexOf("."))));
							}
							
							//从文件数据库中取出xml定义文件
							File fXlsXML = null;
							ParseXMLAll xlsParser = null;
							try{
								fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
								if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
									throw new Exception(String.format("获取字段定义文件失败: %s.xml", fileName.substring(0, fileName.lastIndexOf("."))));
								}
								xlsParser = new ParseXMLAll(fXlsXML);
							}catch(Exception e){
								throw e;
							}finally{
								if(fXlsXML != null && fXlsXML.exists()){
									fXlsXML.delete();
								}
							}
							
							InputStream in = null;
							File fXlsOut = null, fXlsPdf = null;
							File fXlsOutToPdf = null;	//用于生成PDF的临时Excel文件
							File fDocTemplate = null, fDocXml = null, fDocOut = null, fDocPdf = null;
							File fPicWorkStaff = null;	//检校人员的签名图片
							try{
								in = fis.openStream();
								
								//查询技术规范、计量标准、标准器具
								String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
								List<Specification> speList = oRecordMgr.findByHQL(queryStringSpe, oRecord.getId());
								String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
								List<Standard> stdList = oRecordMgr.findByHQL(queryStringStd, oRecord.getId());
								String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
								List<StandardAppliance> stdAppList = oRecordMgr.findByHQL(queryStringStdApp, oRecord.getId());
								
								StringBuffer docModeFileNameBuffer = new StringBuffer();
								fXlsOut = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								//上传Excel校验、处理
								ExcelUtil.uploadExcel(in, fXlsOut, 
										oRecord.getCommissionSheet(), oRecord, oRecord.getSysUserByStaffId(), 
										certificate, oRecord.getTargetAppliance().getApplianceStandardName(), 
										speList, stdList, stdAppList, vNew, docModeFileNameBuffer, xlsParser);
								
								if(!fXlsOut.exists() || fXlsOut.length() == 0){
									throw new Exception("校验、处理上传的Excel文件失败！");
								}
								fXlsOutToPdf = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								ExcelUtil.removeAdditionalInfo(fXlsOut, fXlsOutToPdf, xlsParser);	//移除Excel文件中的附加信息
								fXlsPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//生成的Excel pdf文件
								if(fXlsOutToPdf.exists() && fXlsOutToPdf.length() > 0){		//去除额外信息
									Office2PdfUtil.docToPdf(fXlsOutToPdf, fXlsPdf);
								}else{
									Office2PdfUtil.docToPdf(fXlsOut, fXlsPdf);
								}
								if(!fXlsPdf.exists() || fXlsPdf.length() == 0){
									throw new Exception("生成原始记录Excel的PDF文件失败！");
								}
														
								//判断是否需要生成证书
								if(docModeFileNameBuffer.toString().length() > 0){	//生成证书
									HashMap<String, Object> searchMap = new HashMap<String, Object>();
									searchMap.clear();
									String docModFileName = docModeFileNameBuffer.toString();
									searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
									Pattern patternCertificate = MongoPattern.compile("^"+docModFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
									
									JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("未找到证书模板文件：%s", docModFileName));
									}
									String docModeFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板文件的ID
									
									String docXmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//证书模板配置文件的名称
									patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", docXmlFileName));
									}
									String docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板xml文件的ID
									
									//从文件数据库中取出模板文件
									fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, docModeFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("下载证书模板文件失败！");
									}
									//从文件数据库中取出证书xml定义文件
									fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
									if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("下载证书模板配置文件失败！");
									}
									ParseXMLAll docParser = new ParseXMLAll(fDocXml);
									fDocOut = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									fDocPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
									
									/*******        获取检定员签名图片         ********/
									HashMap<String, Object> picParams = new HashMap<String, Object>();
									if(oRecord.getSysUserByStaffId().getSignature() != null){	//获取检定人员的签名图片
										picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
										JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
										if(jsonInfo != null){
											String picFilename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
											fPicWorkStaff = File.createTempFile(UIDUtil.get22BitUID(), picFilename.substring(picFilename.lastIndexOf('.')>0?picFilename.lastIndexOf('.'):0));
											MongoDBUtil.gridFSDownloadById(fPicWorkStaff, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
										}
									}
									
									WordUtil.MakeCertificateWord(fXlsOut, fDocTemplate, fDocOut, fDocPdf, 
											xlsParser, docParser, 
											(fPicWorkStaff!=null&&fPicWorkStaff.exists()&&fPicWorkStaff.length()>0)?fPicWorkStaff.getAbsolutePath():null,
											oRecord.getCommissionSheet(), oRecord, certificate, 
											new AddressManager().findById(oRecord.getCommissionSheet().getHeadNameId()),
											speList, stdList, stdAppList,alertString);
									if(!fDocOut.exists() || fDocOut.length() == 0){
										throw new Exception("证书文件生成失败！");
									}
									if(!fDocPdf.exists() || fDocPdf.length() == 0){
										throw new Exception("证书Word文件生成PDF失败！");
									}
									certificate.setXml(docXmlFileId);
									certificate.setFileName(docModFileName);
								}else{
									certificate.setFileName("");
								}
								
								/**************上传数据库(Excel文件，Excel的PDF文件，Word文件，Word的PDF文件)**************/
								//判断是否需要新增OriginalRecordExcel
								Integer VersionInt = 0;
								boolean isNewExcel = false;		//标志位，用于更新数据库时判断
								String excelCode = null;
								if(oRecord.getOriginalRecordExcel() == null){	//尚未有过原始记录
									excelCode = UIDUtil.get22BitUID();	//文件集名称
									VersionInt = 0;	//第一版版本号从0开始
									isNewExcel = true;
								}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//已经是正式版的，需要重新生成版本
									excelCode = oRecord.getOriginalRecordExcel().getCode();
									VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
									isNewExcel = true;
								}else{	//替换之前的非正式版
									excelCode = oRecord.getOriginalRecordExcel().getCode();
									VersionInt = oRecord.getOriginalRecordExcel().getVersion();
									toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
								}
								paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, excelCode);//文件集名称
								
								
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
								if(!MongoDBUtil.gridFSUpload(fXlsOut, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存原始记录Excel文件至服务器失败！");
								}
								String xlsIdStr = paramsMap.get("_id").toString();	//Excel文件的ID
								if(paramsMap.containsKey("_id")){
									paramsMap.remove("_id");
								}
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", fileName.substring(0, fileName.lastIndexOf('.'))));
								if(!MongoDBUtil.gridFSUpload(fXlsPdf, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存原始记录Excel的PDF文件至服务器失败！");
								}
								String xlsPdfIdStr = paramsMap.get("_id").toString();	//Excel PDF文件的ID
								
								if(fDocOut != null){	//保存证书文件、证书PDF文件
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, certificate.getFileName());
									if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("保存证书文件至服务器失败！");
									}
									String docIdStr = paramsMap.get("_id").toString();	//证书文件的ID
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", certificate.getFileName().substring(0, certificate.getFileName().lastIndexOf("."))));
									if(!MongoDBUtil.gridFSUpload(fDocPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("保存证书文件至服务器失败！");
									}
									String docPdfIdStr = paramsMap.get("_id").toString();	//证书PDF文件的ID
									certificate.setDoc(docIdStr);
									certificate.setPdf(docPdfIdStr);
								}
								
								/***********更新数据库数据库 ：oRecord、oRecordExcel、Certificate、Vertificate**********/
								//更新OriginalRecordExcel
								OriginalRecordExcel excel = null;
								if(isNewExcel){
									excel = new OriginalRecordExcel();
								}else{
									excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
								}
								excel.setCode(excelCode);
								excel.setCommissionSheet(oRecord.getCommissionSheet());
								excel.setOriginalRecord(oRecord);
								excel.setDoc(xlsIdStr);
								excel.setPdf(xlsPdfIdStr);
								excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
								excel.setVersion(VersionInt);	//版本号
								excel.setXml(xlsXmlFileId);
								excel.setFileName(fileName);
								excel.setCertificateCode(certificate.getCertificateCode());
								
								
								//更新Certificate
								if(certificate.getVersion() < 0){
									certificate.setVersion(0);	//版本号为0开始，-1为预留证书编号
								}
								certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
								if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//上传人员为检定员本人
									certificate.setSysUser(null);
								}else{
									certificate.setSysUser(loginUser);
								}
								
								//更新授权和签字记录
								vNew.setCertificate(certificate);
								vNew.setOriginalRecordExcel(excel);
								if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
									vNew.setSysUserByVerifierId(null);
									vNew.setSysUserByAuthorizerId(null);
								}
								
								oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//更新数据库
								try{
									//更新常用名称
									if(oRecord.getApplianceName() != null && oRecord.getApplianceName().trim().length() > 0 && !oRecord.getApplianceName().equals(oRecord.getTargetAppliance().getApplianceStandardName().getName())){
										AppliancePopularNameManager popNameMgr = new AppliancePopularNameManager();
										int iPopNameRet = popNameMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", oRecord.getTargetAppliance().getApplianceStandardName().getId(), "="), 
												new KeyValueWithOperator("popularName", oRecord.getApplianceName(), "="));
										if(iPopNameRet == 0){
											AppliancePopularName popNameTemp = new AppliancePopularName();
											popNameTemp.setApplianceStandardName(oRecord.getTargetAppliance().getApplianceStandardName());
											popNameTemp.setPopularName(oRecord.getApplianceName());
											popNameTemp.setBrief(LetterUtil.String2Alpha(oRecord.getApplianceName()));
											popNameTemp.setStatus(0);
											popNameMgr.save(popNameTemp);
										}
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 4-->更新常用名称", eee);
								}
								try{
									//更新生产厂商
									if(oRecord.getManufacturer() != null && oRecord.getManufacturer().trim().length() > 0){
										ApplianceManufacturerManager manufacturerMgr = new ApplianceManufacturerManager();
										int iManufacturerRet = manufacturerMgr.getTotalCount(new KeyValueWithOperator("applianceStandardName.id", oRecord.getTargetAppliance().getApplianceStandardName().getId(), "="), 
												new KeyValueWithOperator("manufacturer", oRecord.getManufacturer(), "="));
										if(iManufacturerRet == 0){
											ApplianceManufacturer manufacturerTemp = new ApplianceManufacturer();
											manufacturerTemp.setApplianceStandardName(oRecord.getTargetAppliance().getApplianceStandardName());
											manufacturerTemp.setManufacturer(oRecord.getManufacturer());
											manufacturerTemp.setBrief(LetterUtil.String2Alpha(oRecord.getManufacturer()));
											manufacturerTemp.setStatus(0);
											manufacturerMgr.save(manufacturerTemp);
										}
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 4-->更新生产厂商", eee);
								}
								
								//按需删除文件数据库中过时的原始记录
								try{
									if(toBeDeleteFileIdExcel != null){
										MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 4-->Delete file from mongoDB(原始记录Excel id:"+toBeDeleteFileIdExcel+")", eee);
								}
								try{
									if(toBeDeleteFileIdCertificate != null){
										MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 4-->Delete file from mongoDB(证书Word id:"+toBeDeleteFileIdCertificate+")", eee);
								}
								
							}catch(Exception ex){
								throw ex;
							}finally{
								if(in != null){
									in.close();
								}
								if(fXlsOut != null && fXlsOut.exists()){
									fXlsOut.delete();
								}
								if(fXlsPdf != null && fXlsPdf.exists())
									fXlsPdf.delete();
								
								if(fXlsOutToPdf != null && fXlsOutToPdf.exists()){
									fXlsOutToPdf.delete();
								}
								if(fPicWorkStaff != null && fPicWorkStaff.exists()){
									fPicWorkStaff.delete();
								}
								
								if(fDocTemplate != null && fDocTemplate.exists()){
									fDocTemplate.delete();
								}
								if(fDocXml != null && fDocXml.exists()){
									fDocXml.delete();
								}
								if(fDocOut != null && fDocOut.exists())
									fDocOut.delete();
								
								if(fDocPdf != null && fDocPdf.exists()){
									fDocPdf.delete();
								}
							}
						}
					}
				}
				retJSON4.put("IsOK", true);
				retJSON4.put("msg","上传原始记录成功！ " + (alertString==null?"":alertString.toString())+"请自行调整");
			}catch(Exception e){
				
				try {
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("上传原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 4", e);
				}else{
					log.error("error in FileUploadServlet-->case 4", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON4.toString());
			}
			break;
		case 5: //附件方式上传原始记录（暂存）
			JSONObject retJSON5 = new JSONObject();
			try{
				// 获取上传表单记录
				String OriginalRecordId = request.getParameter("OriginalRecordId");		//原始记录的ID
				String VersionStr = request.getParameter("Version");	//原始记录的版本号
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("原始记录版本冲突，请刷新查看最新版本的原始记录！");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null ){
					throw new Exception("您尚未登录，不能暂存原始记录！");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该原始记录已经生成正式的证书！您不是该任务所分配的检验人，不能再次暂存原始记录！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次提交原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次提交原始记录！");
					}
				}
				
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要更新证书
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(-1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName("");
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//该原始记录的证书为正式版本：证书需要重新生成，版本加1
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName("");
					certificate.setPdf(null);
				}
				
				//核验和授权签字记录，用于从方法中带出相关人员,如果vNew的ID为null，则新增一个记录，否则，更新原记录
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				if(vNew == null){
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
					vNew.setCommissionSheet(oRecord.getCommissionSheet());
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setSysUserByCreatorId(loginUser);
				}else{
					vNew.setAuthorizeRemark(null);
					vNew.setAuthorizeResult(null);
					vNew.setAuthorizeTime(null);
					vNew.setCertificate(null);
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setIsAuthBgRuning(null);
					vNew.setOriginalRecordExcel(null);
					vNew.setSysUserByAuthorizeExecutorId(null);
					vNew.setSysUserByAuthorizerId(null);
					vNew.setSysUserByCreatorId(loginUser);
					vNew.setSysUserByVerifierId(null);
					vNew.setVerifyRemark(null);
					vNew.setVerifyResult(null);
					vNew.setVerifyTime(null);
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//重新生成一份核验和授权签字记录：版本号+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}

				//处理上传的文件
				if (ServletFileUpload.isMultipartContent(request)) {
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");
					
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					
					
					FileItemIterator fii = sfu.getItemIterator(request);
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//含完整路径的文件名
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//取到无路径的纯文件名
							
							if(fileName.contains("-")){	//文件名：模板文件名-序列号.扩展名
								int index1 = fileName.indexOf('-');
								int index2 = fileName.lastIndexOf('.');
								if(index2 > index1){
									fileName = fileName.substring(0, index1) + fileName.substring(index2, fileName.length());
								}
							}
							
							String xmlFileId = null;
							if(oRecord.getOriginalRecordExcel() != null && 
									oRecord.getOriginalRecordExcel().getFileName() != null &&
									oRecord.getOriginalRecordExcel().getFileName().equalsIgnoreCase(fileName) &&
									oRecord.getOriginalRecordExcel().getXml().length() > 0){	//使用原XML
								xmlFileId = oRecord.getOriginalRecordExcel().getXml();
							}else{	//使用现行的模板定义文件
								//查找字段定义xml文件
								String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
								HashMap<String, Object> searchMap = new HashMap<String, Object>();
								searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
								Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
								searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
								searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
								
								JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
								if(retArray.length() == 0){
									throw new Exception(String.format("未找到字段定义文件：%s，不能暂存文件", xmlFileName));
								}
								xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
							}
							if(xmlFileId == null){
								throw new Exception(String.format("未找到字段定义文件：%s.xml，不能暂存文件！", fileName.substring(0, fileName.lastIndexOf("."))));
							}
							
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
							
							InputStream in = null;
							try{
								in = fis.openStream();
								
								Integer VersionInt = 0;
								String toBeDeletedFileId = null;
								boolean isNewExcel = false;		//标志位，用于更新数据库时判断
								if(oRecord.getOriginalRecordExcel() == null){	//尚未有过原始记录
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, UIDUtil.get22BitUID());//文件集名称
									VersionInt = 0;	//第一版版本号从0开始
									isNewExcel = true;
								}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//已经是正式版的，需要重新生成版本
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
									VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
									isNewExcel = true;
								}else{	//替换之前的非正式版
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
									VersionInt = oRecord.getOriginalRecordExcel().getVersion();
									toBeDeletedFileId = oRecord.getOriginalRecordExcel().getDoc();
								}
								
								if(!MongoDBUtil.gridFSUpload(in, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存文件至服务器失败！");
								}
								String docIdStr = paramsMap.get("_id").toString();	//Excel文件的ID
								
								/******************         更新数据库            ***********************/
								//更新oRecord、oRecordExcel、Certificate
								OriginalRecordExcel excel = null;
								if(isNewExcel){
									excel = new OriginalRecordExcel();
								}else{
									excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
								}
								excel.setCode((String)paramsMap.get(MongoDBUtil.KEYNAME_FileSetName));
								excel.setCommissionSheet(oRecord.getCommissionSheet());
								excel.setOriginalRecord(oRecord);
								excel.setDoc(docIdStr);
								excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
								excel.setVersion(VersionInt);	//版本号
								excel.setXml(xmlFileId);
								excel.setFileName(fileName);
								
								//更新授权和签字记录
								if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
									vNew.setSysUserByVerifierId(null);
									vNew.setSysUserByAuthorizerId(null);
								}
								
								oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//更新数据库
								
								//按需删除文件数据库中过时的原始记录
								try{
									if(toBeDeletedFileId != null){
										MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.OriginalRecord);
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 5-->Delete file from mongoDB(原始记录Excel id:"+toBeDeletedFileId+")", eee);
								}
							}catch(Exception ex){
								throw ex;
							}finally{
								if(in != null)
									in.close();
							}
						}
					}
				}
	
				retJSON5.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON5.put("IsOK", false);
					retJSON5.put("msg", String.format("上传原始记录失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 5", e);
				}else{
					log.error("error in FileUploadServlet-->case 5", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON5.toString());
			}
			break;
		case 6:	//上传（提交）证书（Weboffice）docEdit
			JSONObject retJSON6 = new JSONObject();
			try{
				// 初始化上传组件  
				SmartUpload mySmartUpload = new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// 获取上传表单记录		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //原始记录的ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//证书的版本号
				String VerifierName = mySmartUpload.getRequest().getParameter("VerifierName");	//核验人员姓名
			
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateManager certificateMgr = new CertificateManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录或证书记录！");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
					throw new Exception("该记录已上传原始记录Excel文件，不能直接编制证书！");
				}
				if(oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("证书版本冲突，请重新下载最新版本的证书并在其上编辑！");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能提交证书！");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该原始记录已生成证书！您不是该任务所分配的检验人，不能再次提交证书！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次提交原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次提交原始记录！");
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//待删除的证书Doc文件、Excel文件（非正式版的）
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要更新证书
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(-1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName("");
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//该原始记录的证书为正式版本：证书需要重新生成，版本加1
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName("");
					certificate.setPdf(null);
				}else if(certificate.getDoc().length() > 0){	//不是正式版本且存在证书Doc文件：则生成正式版本的证书后，将老版本的证书Doc文件删除
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				
				
				//核验人员
				SysUser checkStaff = null;
				UserManager userMgr = new UserManager();
				if(VerifierName != null && VerifierName.trim().length() > 0 && !VerifierName.equals("-1")){
					
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name", VerifierName.trim(), "="),
							new KeyValueWithOperator("status", 0, "="));
					if(userList != null && userList.size() > 0){
						checkStaff = userList.get(0);
					}
				}
				//核验和授权签字记录，用于从方法中带出相关人员,如果vNew的ID为null，则新增一个记录，否则，更新原记录
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				//获取授权签字人员
				SysUser authStaff = null;	//签字人
				QualificationManager qfMgr = new QualificationManager();
				if(checkStaff != null){ //核验人员不为空
					List<Object[]> qfRetList = qfMgr.getVerifyOrAuthorizeQualifyUsers(oRecord.getTargetAppliance().getApplianceStandardName().getId(), 0, FlagUtil.QualificationType.Type_Qianzi);
					if(qfRetList.size() == 0){
						throw new Exception(String.format("找不到器具标准名称'%s'对应的授权签字人员！请联系基础数据管理员！", oRecord.getTargetAppliance().getApplianceStandardName().getName()));
					}
					Object[] userObj = qfRetList.get(0);
					authStaff = userMgr.findById((Integer)userObj[0]);
				}
				
				
				//核验和授权签字记录，如果vNew的ID为null，则新增一个记录，否则，更新原记录
				if(vNew == null){
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
					vNew.setCommissionSheet(oRecord.getCommissionSheet());
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setSysUserByCreatorId(loginUser);
				}else{
					vNew.setAuthorizeRemark(null);
					vNew.setAuthorizeResult(null);
					vNew.setAuthorizeTime(null);
					vNew.setCertificate(null);
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setIsAuthBgRuning(null);
					vNew.setOriginalRecordExcel(null);
					vNew.setSysUserByAuthorizeExecutorId(null);
					vNew.setSysUserByAuthorizerId(null);
					vNew.setSysUserByCreatorId(loginUser);
					vNew.setSysUserByVerifierId(null);
					vNew.setVerifyRemark(null);
					vNew.setVerifyResult(null);
					vNew.setVerifyTime(null);
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//重新生成一份核验和授权签字记录：版本号+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				if(checkStaff != null&&authStaff!=null){ //核验人员不为空
					vNew.setSysUserByVerifierId(checkStaff);
					vNew.setSysUserByAuthorizerId(authStaff);
				}
				
				//处理上传的文件
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getCertificate().getCode());
					File tempInputFile = null, fDocOut = null, fPdfOut = null, fDocXml = null;
					try{
						
						tempInputFile = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						myFile.saveAs(tempInputFile.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
												
						String docXmlFileId = null;
						if(oRecord.getCertificate() != null && 
								oRecord.getCertificate().getFileName() != null &&
								oRecord.getCertificate().getFileName().equalsIgnoreCase(FileName) &&
								oRecord.getCertificate().getXml() != null &&
								oRecord.getCertificate().getXml().length() > 0){	//使用原XML
							docXmlFileId = oRecord.getCertificate().getXml();
						}else{	//使用现行的模板定义文件
							//获取证书XML配置文件
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
							
							String docXmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//证书模板配置文件的名称
							Pattern patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", docXmlFileName));
							}
							docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板xml文件的ID
						}
						if(docXmlFileId == null){
							throw new Exception(String.format("未找到字段定义文件：%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
						}
						//从文件数据库中取出证书xml定义文件
						fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
						if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("下载证书模板配置文件失败！");
						}
						ParseXMLAll docParser = new ParseXMLAll(fDocXml);
						
						
						fDocOut = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						fPdfOut = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
						
						StringBuilder alertString=new StringBuilder();//生成证书时，字数超限的提示信息
						WordUtil.HandleUploadedCertificateWord(tempInputFile, fDocOut, fPdfOut, docParser, oRecord, certificate);
						
						if(!fDocOut.exists() || fDocOut.length() == 0){
							throw new Exception("证书文件更新证书编号失败！");
						}
						if(!fPdfOut.exists() || fPdfOut.length() == 0){
							throw new Exception("证书Word文件生成PDF失败！");
						}
						certificate.setXml(docXmlFileId);
						certificate.setFileName(FileName);
						
						//保存证书文件
						if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("保存文件至服务器失败！");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Doc文件的ID
						
						//生成并保存pdf文件
						if(paramsMap.containsKey("_id")){
							paramsMap.remove("_id");
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", FileName.substring(0, FileName.lastIndexOf("."))));
						if(!MongoDBUtil.gridFSUpload(fPdfOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("保存PDF文件至服务器失败！");
						}
						String pdfIdStr = paramsMap.get("_id").toString();	//PDF文件的ID;	
						
						certificate.setDoc(docIdStr);
						certificate.setPdf(pdfIdStr);
					
						/***********更新数据库数据库 ：oRecord、oRecordExcel、Certificate、Vertificate**********/
						//判断是否需要新增OriginalRecordExcel
						Integer VersionInt = 0;
						boolean isNewExcel = false;		//标志位，用于更新数据库时判断
						String excelCode = null;
						if(oRecord.getOriginalRecordExcel() == null){	//尚未有过原始记录
							excelCode = UIDUtil.get22BitUID();	//文件集名称
							VersionInt = 0;	//第一版版本号从0开始
							isNewExcel = true;
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//已经是正式版的，需要重新生成版本
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
						}else{	//替换之前的非正式版
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
						}
						
						//更新OriginalRecordExcel
						OriginalRecordExcel excel = null;
						if(isNewExcel){
							excel = new OriginalRecordExcel();
						}else{
							excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
						}
						excel.setCode(excelCode);
						excel.setCommissionSheet(oRecord.getCommissionSheet());
						excel.setOriginalRecord(oRecord);
						excel.setDoc("");
						excel.setPdf(null);
						excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						excel.setVersion(VersionInt);	//版本号
						excel.setXml("");
						excel.setFileName("");
						excel.setCertificateCode(certificate.getCertificateCode());
						
						//更新Certificate
						if(certificate.getVersion() < 0){
							certificate.setVersion(0);	//版本号为0开始，-1为预留证书编号
						}
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//上传人员为检定员本人
							certificate.setSysUser(null);
						}else{
							certificate.setSysUser(loginUser);
						}						
						
						//更新授权和签字记录
						vNew.setCertificate(certificate);
						vNew.setOriginalRecordExcel(excel);
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						oRecordMgr.uploadCertificateUpdateDB(oRecord, excel, certificate, vNew);						
						
						if(!certificateMgr.update(certificate)){
							throw new Exception("更新数据库失败：更新证书信息！");
						}
						
						//按需删除文件数据库中过时的原始记录和证书
						try{
							if(toBeDeleteFileIdExcel != null && toBeDeleteFileIdExcel.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(原始记录Excel id:"+toBeDeleteFileIdExcel+")", eee);
						}
						try{
							if(toBeDeleteFileIdCertificate != null && toBeDeleteFileIdCertificate.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(证书Doc id:"+toBeDeleteFileIdCertificate+")", eee);
						}
						
					}catch(Exception ex){
						throw ex;
					}finally{
						if(tempInputFile != null && tempInputFile.exists()){
							tempInputFile.delete();
						}
						if(fPdfOut != null && fPdfOut.exists()){
							fPdfOut.delete();
						}
						if(fDocOut != null && fDocOut.exists()){
							fDocOut.delete();
						}
						if(fDocXml != null && fDocXml.exists()){
							fDocXml.delete();
						}
					}
				}else{
					throw new Exception("文件上传失败！");
				}
				retJSON6.put("IsOK", true);
			}catch(Exception e){
				
				try {
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("上传证书失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 6", e);
				}else{
					log.error("error in FileUploadServlet-->case 6", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON6.toString());
			}
			break;
/*		case 7:	//暂存证书（Weboffice）:取消该功能，由case 8替代
			JSONObject retJSON7 = new JSONObject();
			try{
				// 初始化上传组件  
				SmartUpload mySmartUpload=new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// 获取上传表单记录		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //原始记录的ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//证书的版本号
		        
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateManager certificateMgr = new CertificateManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null || oRecord.getCertificate() == null){
					throw new Exception("找不到指定的原始记录或证书记录！");
				}
				if(oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("证书版本冲突，请重新下载最新版本的证书并在其上编辑！");
				}
				if(request.getSession().getAttribute("LOGIN_USER") == null ){
					throw new Exception("您尚未登录，不能暂存证书！");
				}if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId())
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该证书已经是正式版！您不是该任务所分配的检验人，不能再次暂存证书！");
				}
				
				//处理上传的文件
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getCertificate().getCode());
					File tempInputFile = null;
					try{
						tempInputFile = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						if(tempInputFile.exists()){
							tempInputFile.delete();
						}
						myFile.saveAs(tempInputFile.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
						
						
						Integer VersionInt = 0;
						String toBeDeletedFileId = null;
						if(oRecord.getCertificate().getPdf() != null){	//已经是正式版的，需要重新生成版本
							VersionInt = oRecord.getCertificate().getVersion() + 1;
						}else{	//替换之前的非正式版
							if(oRecord.getCertificate().getVersion() > 0){
								VersionInt = oRecord.getCertificate().getVersion();
							}
							if(oRecord.getCertificate().getDoc().length() > 0){
								toBeDeletedFileId = oRecord.getCertificate().getDoc();
							}
						}
						
						if(!MongoDBUtil.gridFSUpload(tempInputFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("保存文件至服务器失败！");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Doc文件的ID
						
						//Certificate
						Certificate certificate = certificateMgr.findById(oRecord.getCertificate().getId());
						certificate.setDoc(docIdStr);
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						certificate.setVersion(VersionInt);	//版本号
						certificate.setFileName(FileName);
						certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece(), VersionInt));
						
						if(!certificateMgr.update(certificate)){
							throw new Exception("更新数据库失败：更新证书信息！");
						}
						
						//按需删除文件数据库中过时的原始记录
						if(toBeDeletedFileId != null){
							MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.Certificate);
						}
						
					}catch(Exception ex){
						throw ex;
					}finally{
						if(tempInputFile != null && tempInputFile.exists()){
							tempInputFile.delete();
						}
					}
				}else{
					throw new Exception("文件上传失败！");
				}
				retJSON7.put("IsOK", true);
			}catch(Exception e){
				
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("上传证书失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 7", e);
				}else{
					log.error("error in FileUploadServlet-->case 7", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON7.toString());
			}
			break;
		case 8:	//生成证书--新(不用的功能)
			JSONObject retJSON8 = new JSONObject();
			try{
				String OriginalRecordId = request.getParameter("OriginalRecordId");
				String TemplateFileId = request.getParameter("TemplateFileId");	//Doc模板文件的ID
				String VersionStr = request.getParameter("Version");//证书的版本号

				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager recordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = recordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录！");
				}
				if(oRecord.getCertificate() != null && oRecord.getCertificate().getDoc().length() > 0
						&& oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){	//已存在新版本证书Doc文件，则直接转向
					throw new Exception("证书版本冲突，请刷新列表查看最新版本的证书！");
				}
				if(request.getSession().getAttribute("LOGIN_USER") == null ){
					throw new Exception("您尚未登录，不能生成证书！");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId())
						&& FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){
					throw new Exception("该证书已经是正式版！您不是该任务所分配的检验人，不能再次生成证书！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次生成证书！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次生成证书！");
					}
				}
				
				if(TemplateFileId == null || TemplateFileId.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(oRecord.getOriginalRecordExcel() == null || oRecord.getOriginalRecordExcel().getPdf() == null){
					throw new Exception("原始记录Excel文件未提交！");
				}
				JSONObject retObj = MongoDBUtil.getFileInfoById(TemplateFileId, MongoDBUtil.CollectionType.Template);
				if(retObj == null){
					throw new Exception("找不到指定的证书模板文件！");
				}
				String fileId = retObj.getString("_id");	//证书模板文件的ID
				String docModFileName = retObj.getString(MongoDBUtil.KEYNAME_FileName);	//证书模板文件的文件名
				String xmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//证书模板配置文件的名称
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, retObj.getString(MongoDBUtil.KEYNAME_FileSetName));
				Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", xmlFileName));
				}
				String xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
				
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要增加该证书信息；不为null，则需要更新
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为0，证书Doc文件等最后模板文件处理完后更新）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = recordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(0);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName(docModFileName);
					certificate.setXml(xmlFileId);
					if(!new CertificateManager().save(certificate)){
						throw new Exception("保存证书编号失败，请稍后再试！");
					}
					oRecord.setCertificate(certificate);
					if(!recordMgr.update(oRecord)){
						throw new Exception("保存证书编号失败，请稍后再试！");
					}
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(certificate)){	//证书是正式版本
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName(docModFileName);
					certificate.setXml(xmlFileId);
					certificate.setPdf(null);
				}else if(certificate.getVersion() < 0){		//已有证书为预留证书编号：设置版本号为0（正式证书）
					certificate.setVersion(0);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setFileName(docModFileName);
					certificate.setXml(xmlFileId);					
				}
				
				File fDocTemplate = null, fDocXML = null, fXlsRecord = null, fXlsXML = null;//证书模板文件，证书xml定义文件，原始记录文件, 原始记录文件的配置文件
				File fPicVerifier = null;	//检验员的签名图片文件
				File fOutputFile = null, fOutputPdfFile = null;	//生成的证书Doc文件、证书PDF文件
				File fXlsUpdate = null, fXlsUpdatePdf = null;	//根据需要（证书新生成一个版本）重新生成的一份原始记录（更新证书编号）及其PDF文件
				File fXlsUpdateWithPic = null;	//插入了检校人员签名图片的原始记录文件
				try{
					//从文件数据库中取出模板文件
					fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
					if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, fileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("下载证书模板文件失败！");
					}
					
					//从文件数据库中取出证书xml定义文件
					fDocXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fDocXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("下载证书模板配置文件失败！");
					}
					ParseXMLAll docParser = new ParseXMLAll(fDocXML);
					
					//获取原始记录Excel文件及其配置文件
					String xlsFileName = oRecord.getOriginalRecordExcel().getFileName();	//原始记录Excel文件的文件名
					fXlsRecord = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
					if(!MongoDBUtil.gridFSDownloadById(fXlsRecord, oRecord.getOriginalRecordExcel().getDoc(), MongoDBUtil.CollectionType.OriginalRecord)){
						throw new Exception("获取原始记录Excel文件失败！");
					}
					//查询原始记录配置文件的名称并获取文件（从数据库中读取xml文件的ID 或读取最新的XML文件）
					String xlsXmlFileId = null;
					if(oRecord.getOriginalRecordExcel() != null && 
							oRecord.getOriginalRecordExcel().getXml().length() > 0){	//使用原XML（从数据库中读取xml文件的ID）
						xlsXmlFileId = oRecord.getOriginalRecordExcel().getXml();
					}else{	//使用现行的模板定义文件
						//查找字段定义xml文件
						String xlsXmlFileName = String.format("%s.xml", oRecord.getOriginalRecordExcel().getFileName().substring(0, oRecord.getOriginalRecordExcel().getFileName().lastIndexOf(".")));	//原始记录配置文件的名称
						map.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录的配置文件的文件集名称
						pattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
						map.put(MongoDBUtil.KEYNAME_FileName, pattern);
						map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
						JSONArray retArray2 = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
						if(retArray2.length() == 0){
							throw new Exception(String.format("未找到原始记录文件对应的配置文件:%s", xlsXmlFileName));
						}
						xlsXmlFileId = ((JSONObject)retArray2.get(0)).getString("_id");//原始记录xml文件的ID
						
					}
					if(xlsXmlFileId == null){
						throw new Exception(String.format("未找到字段定义文件：%s.xml", oRecord.getOriginalRecordExcel().getFileName().substring(0, oRecord.getOriginalRecordExcel().getFileName().lastIndexOf("."))));
					}
					
					fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception(String.format("获取原始记录配置文件失败：%s.xml", oRecord.getOriginalRecordExcel().getFileName().substring(0, oRecord.getOriginalRecordExcel().getFileName().lastIndexOf("."))));
					}
					ParseXMLAll xlsParser = new ParseXMLAll(fXlsXML);
					
					HashMap<String, Object> picParams = new HashMap<String, Object>();
					if(oRecord.getSysUserByStaffId() != null && oRecord.getSysUserByStaffId().getSignature() != null){	//获取检验人员的签名图片
						picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
						JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
						if(jsonInfo != null){
							String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
							fPicVerifier = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
							MongoDBUtil.gridFSDownloadById(fPicVerifier, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
						}
					}
					
					//查询技术规范、计量标准、标准器具
					String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
					List<Specification> speList = recordMgr.findByHQL(queryStringSpe, oRecord.getId());
					String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
					List<Standard> stdList = recordMgr.findByHQL(queryStringStd, oRecord.getId());
					String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
					List<StandardAppliance> stdAppList = recordMgr.findByHQL(queryStringStdApp, oRecord.getId());
					
					fOutputFile = File.createTempFile(UIDUtil.get22BitUID(), fDocTemplate.getName().substring(fDocTemplate.getName().lastIndexOf('.')));	//生成的证书文件
					fOutputPdfFile = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//生成的PDF文件
					WordUtil.MakeCertificateWord(fXlsRecord, fDocTemplate, fOutputFile, fOutputPdfFile, 
							xlsParser, docParser, 
							fPicVerifier==null?null:fPicVerifier.getAbsolutePath(),
							oRecord.getCommissionSheet(), oRecord, certificate, new AddressManager().findById(oRecord.getCommissionSheet().getHeadNameId()),
							speList, stdList, stdAppList);
					if(!fOutputFile.exists() || fOutputFile.length() == 0){
						throw new Exception("证书文件生成失败！");
					}
					if(!fOutputPdfFile.exists() || fOutputPdfFile.length() == 0){
						throw new Exception("证书Word文件生成PDF失败！");
					}
					
					//存储生成的证书Doc到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, docModFileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
					if(!MongoDBUtil.gridFSUpload(fOutputFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
						throw new Exception("保存证书至文件服务器失败！");
					}
					String docIdStr = paramsMap.get("_id").toString();	//Doc文件的ID
					
					//生成并保存pdf文件
					if(paramsMap.containsKey("_id")){
						paramsMap.remove("_id");
					}
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", docModFileName.substring(0, docModFileName.lastIndexOf("."))));
					if(!MongoDBUtil.gridFSUpload(fOutputPdfFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
						throw new Exception("保存证书PDF文件至服务器失败！");
					}
					String pdfIdStr = paramsMap.get("_id").toString();	//PDF文件的ID;	
					
					
					OriginalRecordExcel excel = oRecord.getOriginalRecordExcel();
					if(certificate.getId() == null){	//新的证书编号：需要更新原始记录的“证书号字段”
						ParseXMLAll xlsAllParser = new ParseXMLAll(fXlsXML);
						boolean hasTag = false;	//查看原始记录是否有证书编号对应字段
						String key = "CertificateCode";	//证书编号属性名
						for(int k = 0; k < xlsAllParser.getQNameCount(key); k++){
							if(xlsAllParser.getAttribute(key, "fieldClass", k) != null && xlsAllParser.getAttribute(key, "fieldClass", k).equalsIgnoreCase("com.jlyw.hibernate.Certificate")){
								hasTag = true;
								break;
							}
						}
						if(hasTag){	//有相应标签，则需要修改原始记录
							fXlsUpdate = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
							ExcelUtil.updateExcelWithCertificateCode(fXlsRecord, fXlsUpdate, certificate, xlsAllParser);
							
							//存储更新后的Excel到文件数据库中的参数
							HashMap<String, Object> xlsParamsMap = new HashMap<String, Object>();
							xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, xlsFileName);
							xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
							xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
							xlsParamsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
							if(!MongoDBUtil.gridFSUpload(fXlsUpdate, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
								throw new Exception("保存更新后的原始记录Excel至文件服务器失败！");
							}
							String xlsUpdateIdStr = xlsParamsMap.get("_id").toString();	//Excel文件的ID
							
							//将检校人员的签名图片插入Excel中并生成PDF版本
							fXlsUpdateWithPic = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.substring(xlsFileName.lastIndexOf(".")));
//							ExcelUtil.insertWorkerSignatureImgToExcel(fXlsUpdate, fXlsUpdateWithPic, fPicVerifier, xlsAllParser);	//插入签名图片，生成一份新的原始记录Excel文件
							ExcelUtil.removeAdditionalInfo(fXlsUpdate, fXlsUpdateWithPic, xlsAllParser);	//移除Excel文件的附加信息
							fXlsUpdatePdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");//生成pdf文件
							if(fXlsUpdateWithPic.exists() && fXlsUpdateWithPic.length() > 0){	//插入了签名图片
								Office2PdfUtil.docToPdf(fXlsUpdateWithPic, fXlsUpdatePdf);
							}else{
								Office2PdfUtil.docToPdf(fXlsUpdate, fXlsUpdatePdf);
							}
							//保存pdf文件
							if(xlsParamsMap.containsKey("_id")){
								xlsParamsMap.remove("_id");
							}
							xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", xlsFileName.substring(0, xlsFileName.lastIndexOf(".")==-1?0:xlsFileName.lastIndexOf('.'))));
							if(!MongoDBUtil.gridFSUpload(fXlsUpdatePdf, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
								throw new Exception("保存更新后的原始记录PDF至文件服务器失败！");
							}
							String xlsUpdatePdfIdStr = xlsParamsMap.get("_id").toString();	//PDF文件的ID;	
							
							//更新数据库
							excel.setDoc(xlsUpdateIdStr);
							excel.setPdf(xlsUpdatePdfIdStr);
							
						}
					}
					
					String toBeDeletedFileId = null;	//是否有需要删除的无效证书Doc文件：尚未生成正式版的证书Doc
					if(certificate.getId() != null && certificate.getDoc().length() > 0){
						toBeDeletedFileId = certificate.getDoc();
					}
					
					//更新数据库
					certificate.setDoc(docIdStr);
					certificate.setPdf(pdfIdStr);
					certificate.setFileName(docModFileName);	//证书模板的文件名
					certificate.setXml(xmlFileId);	//证书配置文件的ID
//					if(!recordMgr.uploadCertificateUpdateDB(oRecord, excel, fXlsUpdate==null?false:true, certificate, certificate.getId()==null?true:false)){
					if(!recordMgr.uploadCertificateUpdateDB(oRecord, excel, true, certificate, certificate.getId()==null?true:false)){	//第三个参数强制为true用于更新Excel中的证书编号字段等
						throw new Exception("更新数据库失败！");
					}
					
					retJSON8.put("IsOK", true);
					try{
						//按需删除文件数据库中过时的原始记录
						if(toBeDeletedFileId != null){
							MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.Certificate);
						}
					}catch(Exception ex){
						log.debug("exception in FileUploadServlet->case 8->MongoDBUtil.gridFSDeleteById", ex);
					}
				}catch(Exception re){
					throw re;
				}finally{	//关闭文件流;删除临时文件
					if(fDocTemplate != null && fDocTemplate.exists()){
						fDocTemplate.delete();
						
					}
					if(fDocXML != null && fDocXML.exists()){
						fDocXML.delete();
					}
					if(fXlsRecord != null && fXlsRecord.exists()){
						fXlsRecord.delete();
					}
					if(fXlsXML != null && fXlsXML.exists()){
						fXlsXML.delete();
					}
					
					if(fPicVerifier != null && fPicVerifier.exists()){
						fPicVerifier.delete();
					}
					
					if(fOutputFile != null && fOutputFile.exists()){
						fOutputFile.delete();
					}
					if(fOutputPdfFile != null && fOutputPdfFile.exists()){
						fOutputPdfFile.delete();
					}
					if(fXlsUpdate != null && fXlsUpdate.exists()){
						fXlsUpdate.delete();
					}
					if(fXlsUpdatePdf != null && fXlsUpdatePdf.exists()){
						fXlsUpdatePdf.delete();
					}
					if(fXlsUpdateWithPic != null && fXlsUpdateWithPic.exists()){
						fXlsUpdateWithPic.delete();
					}
				}
			}catch(Exception e){
				try {
					retJSON8.put("IsOK", false);
					retJSON8.put("msg", String.format("生成证书失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 8", e);
				}else{
					log.error("error in FileDownloadServlet-->case 8", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON8.toString());
			}
			break;*/
		case 9 : //原始记录Excel批量上传(通过uploadify V3.1控件上传)
			JSONObject retJSON9 = new JSONObject();
			try{
				String alert = "";//字数超过限制时的提示信息
				
				String CommissionSheetId = request.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("委托单未指定！");
				}
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CommissionSheet cSheet = cSheetMgr.findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("找不到指定的委托单！");
				}
				
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能上传原始记录！");
				}
				//判断委托单的状态
				if(cSheet.getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能提交原始记录！");
				}
				if(cSheet.getStatus() == 3 ||			//已完工（完工确认后）
						cSheet.getStatus() == 4 ||	//已结账
						cSheet.getStatus() == 9){		//已结束
					throw new Exception("该委托单已完工，不能添加原始记录！");
				}
				
				//处理上传的文件
				if (ServletFileUpload.isMultipartContent(request)) {
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");
					
					
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, loginUser.getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, loginUser.getName());
					
					
					ApplianceStandardNameManager stdNameMgr = new ApplianceStandardNameManager();
					
					FileItemIterator fii = sfu.getItemIterator(request);
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//含完整路径的文件名
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//取到无路径的纯文件名
							if(fileName.contains("-")){	//文件名：模板文件名-序列号.扩展名
								int index1 = fileName.indexOf('-');
								int index2 = fileName.lastIndexOf('.');
								if(index2 > index1){
									fileName = fileName.substring(0, index1) + fileName.substring(index2, fileName.length());
								}
							}
							//查找Excel字段定义xml文件
							String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//模板字段定义xml文件的名称
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							Pattern pattern0 = Pattern.compile("(?!(" + SystemCfgUtil.CertificateTemplateFilesetName +")).*$");//正则表达式：文件集名称不是以证书模板文件集名开头的
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, pattern0);
							Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
							searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
							
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("未找到Excel配置文件：%s", xmlFileName));
							}
							String xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
							String xlsXmlFileSetName = ((JSONObject)retArray.get(0)).getString(MongoDBUtil.KEYNAME_FileSetName);	//配置文件的文件集名
							
							//从文件数据库中取出原始记录Excel的xml定义文件
							File fXlsXML = null;
							ParseXMLAll xlsParser = null;
							try{
								fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
								if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
									throw new Exception(String.format("获取Excel配置文件失败: %s", xmlFileName));
								}
								xlsParser = new ParseXMLAll(fXlsXML);
							}catch(Exception e){
								throw e;
							}finally{
								if(fXlsXML != null && fXlsXML.exists()){
									fXlsXML.delete();
								}
							}
							
							//获取标准名称
							List<ApplianceStandardName> stdNameList = stdNameMgr.findByVarProperty(new KeyValueWithOperator("filesetName", xlsXmlFileSetName, "="));
							if(stdNameList.size() == 0){
								throw new Exception(String.format("找不到模板文件 %s 所对应的标准名称！", xmlFileName));
							}else if(stdNameList.size() > 1){
								throw new Exception(String.format("模板文件 %s 对应的标准名称不唯一：%s, %s", xmlFileName, stdNameList.get(0).getName(), stdNameList.get(0).getName()));
							}
							ApplianceStandardName stdName = stdNameList.get(0);	//器具标准名称
							
							if(cSheet.getSpeciesType()==false && !cSheet.getApplianceSpeciesId().equals(stdName.getId()) ){
								throw new Exception(String.format("模板文件'%s'所对应的标准名称'%s'不属于该委托单下待检测的器具范围！", xmlFileName, stdName.getName()));
							}else if(cSheet.getSpeciesType()==true && !stdNameMgr.checkStandardNameInSpecial(stdName.getId(), cSheet.getApplianceSpeciesId())){
								throw new Exception(String.format("模板文件'%s'所对应的标准名称'%s'不属于该委托单下待检测的器具范围！", xmlFileName, stdName.getName()));
							}
							
							
							/***************************         开始上传的文件                  ****************************/
							OriginalRecordManager oRecordMgr = new OriginalRecordManager();
							//查找已有的原始记录数量
							String queryStringexistedNumber = "select count(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1";
							List<Long> iRetList = oRecordMgr.findByHQL(queryStringexistedNumber, cSheet.getId());
							int existedNumber = iRetList.get(0).intValue(), NumberInt = 1;	//已存在的原始记录数量，待添加的原始记录数量
							if(NumberInt + existedNumber > cSheet.getQuantity()){
								throw new Exception("待添加的原始记录张数过多，原始记录总数不得超过委托单的器具数量！");
							}
							//生成原始记录
							OriginalRecord oRecord = new OriginalRecord();
							oRecord.setCommissionSheet(cSheet);	//委托单
							oRecord.setStatus(0);	//状态
							oRecord.setQuantity(1);	//该原始记录中的器具数量
							oRecord.setSysUserByCreatorId(loginUser);
							oRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));	//创建时间
							
							//生成一份数据库的证书记录（版本号为0，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
							Certificate certificate = null;
							if(certificate == null){
								//查询委托单下的第几份记录
								Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
								certificate = new Certificate();
								certificate.setCode(UIDUtil.get22BitUID());
								certificate.setVersion(0);
								certificate.setCommissionSheet(oRecord.getCommissionSheet());
								certificate.setOriginalRecord(oRecord);
								certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
								certificate.setDoc("");
								certificate.setSequece(sequence);
								certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), 0));
								certificate.setFileName("");
							}
							//生成一份Excel记录
							OriginalRecordExcel excel = new OriginalRecordExcel();
							excel.setCode(UIDUtil.get22BitUID());
							excel.setCommissionSheet(cSheet);
							excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
							excel.setVersion(0);	//版本号
							excel.setXml(xmlFileId);
							excel.setFileName(fileName);
							excel.setCertificateCode(certificate.getCertificateCode());
							
							//生成 核验和授权签字记录（用于从方法中带出相关数据）
							VerifyAndAuthorize vNew = new VerifyAndAuthorize();
							vNew.setCode(UIDUtil.get22BitUID());
							vNew.setVersion(0);
							vNew.setCommissionSheet(cSheet);
							vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
							vNew.setSysUserByCreatorId(loginUser);
							//所使用的技术规范、计量标准、标准器具(用于从方法中带出相关数据)
							Map<Integer, Specification> speMap = new HashMap<Integer, Specification>();	
							Map<Integer, Standard> stdMap = new HashMap<Integer, Standard>();
							Map<Integer, StandardAppliance> stdAppMap = new HashMap<Integer, StandardAppliance>();
							
							//Excel文件处理
							InputStream in = null;
							File fXlsOutFile = null, fXlsPdfFile= null;
							File fXlsOutFileToPdf = null;	//去除了额外信息的Excel
							File fDocTemplate = null, fDocXml = null, fDocOut = null, fDocPdf = null;	//证书模板文件、证书模板配置文件，生成的证书文件，证书PDF文件
							File fPicWorkStaff = null;	//检校人员的签名图片
							Transaction tran = HibernateSessionFactory.beginTransaction();	//开始事务处理
							try{
								in = fis.openStream();
								fXlsOutFile = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								ExcelUtil.uploadExcelByBatch(in, fXlsOutFile, cSheet, stdName, xlsParser, oRecord, certificate, vNew, speMap, stdMap, stdAppMap);	//校验、处理Excel
								
								if(!fXlsOutFile.exists() || fXlsOutFile.length() == 0){
									throw new Exception("校验、处理上传的Excel文件失败！");
								}
								fXlsOutFileToPdf = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								ExcelUtil.removeAdditionalInfo(fXlsOutFile, fXlsOutFileToPdf, xlsParser);	//移除Excel文件中的附加信息
								fXlsPdfFile = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//生成的Excel pdf文件
								if(fXlsOutFileToPdf.exists() && fXlsOutFileToPdf.length() > 0){		//去除额外信息
									Office2PdfUtil.docToPdf(fXlsOutFileToPdf, fXlsPdfFile);
								}else{
									Office2PdfUtil.docToPdf(fXlsOutFile, fXlsPdfFile);
								}
								if(!fXlsPdfFile.exists() || fXlsPdfFile.length() == 0){
									throw new Exception("生成原始记录Excel的PDF文件失败！");
								}
								
								List<Specification> speList = new ArrayList<Specification>();
								List<Standard> stdList = new ArrayList<Standard>();
								List<StandardAppliance> stdAppList = new ArrayList<StandardAppliance>();
								Iterator<Entry<Integer, Specification>> iterSpe = speMap.entrySet().iterator();
								while(iterSpe.hasNext()){
									speList.add(iterSpe.next().getValue());
								}
								Iterator<Entry<Integer, Standard>> iterStd = stdMap.entrySet().iterator();
								while(iterStd.hasNext()){
									stdList.add(iterStd.next().getValue());
								}
								Iterator<Entry<Integer, StandardAppliance>> iterStdApp = stdAppMap.entrySet().iterator();
								while(iterStdApp.hasNext()){
									stdAppList.add(iterStdApp.next().getValue());
								}
								
								//判断是否需要生成证书
								if(certificate.getFileName() != null && certificate.getFileName().length() > 0){	//生成证书
									searchMap.clear();
									String docModFileName = certificate.getFileName();
									searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
									Pattern patternCertificate = MongoPattern.compile("^"+docModFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
									
									retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("未找到证书模板文件：%s", docModFileName));
									}
									String docModeFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板文件的ID
									
									String docXmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//证书模板配置文件的名称
									patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", docXmlFileName));
									}
									String docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板xml文件的ID
									
									//从文件数据库中取出模板文件
									fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, docModeFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("下载证书模板文件失败！");
									}
									//从文件数据库中取出证书xml定义文件
									fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
									if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("下载证书模板配置文件失败！");
									}
									ParseXMLAll docParser = new ParseXMLAll(fDocXml);
									fDocOut = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									fDocPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
									
									/*******        获取检定员签名图片         ********/
									HashMap<String, Object> picParams = new HashMap<String, Object>();
									if(oRecord.getSysUserByStaffId().getSignature() != null){	//获取检定人员的签名图片
										picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
										JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
										if(jsonInfo != null){
											String picFilename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
											fPicWorkStaff = File.createTempFile(UIDUtil.get22BitUID(), picFilename.substring(picFilename.lastIndexOf('.')>0?picFilename.lastIndexOf('.'):0));
											MongoDBUtil.gridFSDownloadById(fPicWorkStaff, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
										}
									}
									StringBuilder alertString = new StringBuilder();//字数超过限制时的提示信息
									WordUtil.MakeCertificateWord(fXlsOutFile, fDocTemplate, fDocOut, fDocPdf, 
											xlsParser, docParser, 
											(fPicWorkStaff!=null&&fPicWorkStaff.exists()&&fPicWorkStaff.length()>0)?fPicWorkStaff.getAbsolutePath():null,
											cSheet, oRecord, certificate, new AddressManager().findById(cSheet.getHeadNameId()),
											speList, stdList, stdAppList,alertString);
									if(alertString!=null&&alertString.toString().length()>0)
										if(certificate!=null){
											alert = alert + certificate.getCode() + alertString.toString()+";";
										}
									if(!fDocOut.exists() || fDocOut.length() == 0){
										throw new Exception("证书文件生成失败！");
									}
									if(!fDocPdf.exists() || fDocPdf.length() == 0){
										throw new Exception("证书Word文件生成PDF失败！");
									}
									certificate.setXml(docXmlFileId);
								}else{
									certificate.setFileName("");
								}
								//上传数据库(Excel文件，Excel的PDF文件，Word文件，Word的PDF文件)
								paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, excel.getCode());
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
								if(!MongoDBUtil.gridFSUpload(fXlsOutFile, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存原始记录Excel文件至服务器失败！");
								}
								String xlsIdStr = paramsMap.get("_id").toString();	//Excel文件的ID
								if(paramsMap.containsKey("_id")){
									paramsMap.remove("_id");
								}
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", fileName.substring(0, fileName.lastIndexOf('.'))));
								if(!MongoDBUtil.gridFSUpload(fXlsPdfFile, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存原始记录Excel的PDF文件至服务器失败！");
								}
								String xlsPdfIdStr = paramsMap.get("_id").toString();	//Excel PDF文件的ID
								excel.setDoc(xlsIdStr);
								excel.setPdf(xlsPdfIdStr);
								if(fDocOut != null){	//保存证书文件、证书PDF文件
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, certificate.getFileName());
									if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("保存证书文件至服务器失败！");
									}
									String docIdStr = paramsMap.get("_id").toString();	//证书文件的ID
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", certificate.getFileName().substring(0, certificate.getFileName().lastIndexOf("."))));
									if(!MongoDBUtil.gridFSUpload(fDocPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("保存证书文件至服务器失败！");
									}
									String docPdfIdStr = paramsMap.get("_id").toString();	//证书PDF文件的ID
									certificate.setDoc(docIdStr);
									certificate.setPdf(docPdfIdStr);
								}
								//更新数据库
								OriginalRecordDAO oRecordDao = new OriginalRecordDAO();
								OriginalRecordTechnicalDocsDAO oSpeDao = new OriginalRecordTechnicalDocsDAO();
								OriginalRecordStandardsDAO oStdDao = new OriginalRecordStandardsDAO();
								OriginalRecordStdAppliancesDAO oStdAppDao = new OriginalRecordStdAppliancesDAO();
								oRecordDao.save(oRecord);	//保存原始记录
								Timestamp nowTime = new Timestamp(System.currentTimeMillis());
								for(Specification s : speList){	//存技术规范
									OriginalRecordTechnicalDocs oSpe = new OriginalRecordTechnicalDocs();
									oSpe.setOriginalRecord(oRecord);
									oSpe.setSpecification(s);
									oSpe.setLastEditTime(nowTime);
									oSpe.setSysUser(loginUser);
									oSpeDao.save(oSpe);
								}
								for(Standard std : stdList){ //存计量标准
									OriginalRecordStandards oStd = new OriginalRecordStandards();
									oStd.setOriginalRecord(oRecord);
									oStd.setStandard(std);
									oStd.setLastEditTime(nowTime);
									oStd.setSysUser(loginUser);
									oStdDao.save(oStd);
								}
								for(StandardAppliance stdApp : stdAppList){	//存标准器具
									OriginalRecordStdAppliances oStd = new OriginalRecordStdAppliances();
									oStd.setOriginalRecord(oRecord);
									oStd.setStandardAppliance(stdApp);
									oStd.setLastEditTime(nowTime);
									oStd.setSysUser(loginUser);
									oStdAppDao.save(oStd);
								}
								CertificateDAO certificateDao = new CertificateDAO();
								certificate.setOriginalRecord(oRecord);
								if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//上传人员为检定员本人
									certificate.setSysUser(null);
								}else{
									certificate.setSysUser(loginUser);
								}
								certificateDao.save(certificate);
								excel.setOriginalRecord(oRecord);
								OriginalRecordExcelDAO excelDao = new OriginalRecordExcelDAO();
								excelDao.save(excel);
								
								VerifyAndAuthorizeDAO vNewDao = new VerifyAndAuthorizeDAO();
								vNew.setCertificate(certificate);
								vNew.setOriginalRecordExcel(excel);
								vNewDao.save(vNew);
								
								oRecord.setCertificate(certificate);
								oRecord.setOriginalRecordExcel(excel);
								oRecord.setVerifyAndAuthorize(vNew);
								oRecordDao.update(oRecord);
								
								//更新常用名称
								if(oRecord.getApplianceName() != null && oRecord.getApplianceName().trim().length() > 0 && !oRecord.getApplianceName().equals(stdName.getName())){
									AppliancePopularNameDAO popNameDao = new AppliancePopularNameDAO();
									int iPopNameRet = popNameDao.getTotalCount("AppliancePopularName", 
											new KeyValueWithOperator("applianceStandardName.id", stdName.getId(), "="), 
											new KeyValueWithOperator("popularName", oRecord.getApplianceName(), "="));
									if(iPopNameRet == 0){
										AppliancePopularName popNameTemp = new AppliancePopularName();
										popNameTemp.setApplianceStandardName(stdName);
										popNameTemp.setPopularName(oRecord.getApplianceName());
										popNameTemp.setBrief(LetterUtil.String2Alpha(oRecord.getApplianceName()));
										popNameTemp.setStatus(0);
										popNameDao.save(popNameTemp);
									}
								}
								//更新生产厂商
								if(oRecord.getManufacturer() != null && oRecord.getManufacturer().trim().length() > 0){
									ApplianceManufacturerDAO manufacturerDao = new ApplianceManufacturerDAO();
									int iManufacturerRet = manufacturerDao.getTotalCount("ApplianceManufacturer", new KeyValueWithOperator("applianceStandardName.id", oRecord.getTargetAppliance().getApplianceStandardName().getId(), "="), 
											new KeyValueWithOperator("manufacturer", oRecord.getManufacturer(), "="));
									if(iManufacturerRet == 0){
										ApplianceManufacturer manufacturerTemp = new ApplianceManufacturer();
										manufacturerTemp.setApplianceStandardName(oRecord.getTargetAppliance().getApplianceStandardName());
										manufacturerTemp.setManufacturer(oRecord.getManufacturer());
										manufacturerTemp.setBrief(LetterUtil.String2Alpha(oRecord.getManufacturer()));
										manufacturerTemp.setStatus(0);
										manufacturerDao.save(manufacturerTemp);
									}
								}
								
								tran.commit();
							}catch(Exception ex){
								tran.rollback();
								throw ex;
							}finally{
								if(in != null){
									in.close();
									in = null;
								}
								if(fXlsOutFile != null && fXlsOutFile.exists()){
									fXlsOutFile.delete();
								}
								if(fXlsOutFileToPdf != null && fXlsOutFileToPdf.exists()){
									fXlsOutFileToPdf.delete();
								}
								if(fPicWorkStaff != null && fPicWorkStaff.exists()){
									fPicWorkStaff.delete();
								}
								if(fXlsPdfFile != null && fXlsPdfFile.exists()){
									fXlsPdfFile.delete();
								}

								if(fDocTemplate != null && fDocTemplate.exists()){
									fDocTemplate.delete();
								}
								
								if(fDocXml != null && fDocXml.exists()){
									fDocXml.delete();
								}
								if(fDocOut != null && fDocOut.exists()){
									fDocOut.delete();
								}
								if(fDocPdf != null && fDocPdf.exists()){
									fDocPdf.delete();
								}
							}
						}
					}
				}
				
				retJSON9.put("IsOK", true);
				retJSON9.put("msg", "上传原始记录成功！" + alert);
			}catch(Exception e){
				e.printStackTrace();
				try {
					retJSON9.put("IsOK", false);
					retJSON9.put("msg", String.format("上传原始记录Excel失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 9", e);
				}else{
					log.error("error in FileUploadServlet-->case 9", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON9.toString());
			}
			break;
		case 11:	//测试：TaskTime.jsp：上传原始记录
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("1");
			break;
			
		case 12:	//上传（提交）修改后的证书（Weboffice）——有原始记录也可修改证书
			JSONObject retJSON12 = new JSONObject();
			try{
				// 初始化上传组件  
				SmartUpload mySmartUpload = new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// 获取上传表单记录		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //原始记录的ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//证书的版本号
		        
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("参数不完整！");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateManager certificateMgr = new CertificateManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("找不到指定的原始记录或证书记录！");
				}
				
//				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
//					throw new Exception("该记录已上传原始记录Excel文件，不能直接编制证书！");
//				}
				
				if(oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("证书版本冲突，请重新下载最新版本的证书并在其上编辑！");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("您尚未登录，不能提交证书！");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("该原始记录已生成证书！您不是该任务所分配的检验人，不能再次提交证书！");
				}
				
				//判断委托单的状态
				if(oRecord.getCommissionSheet().getStatus() == 10){	//已注销
					throw new Exception("该委托单已注销，不能再次提交原始记录！");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//已完工（完工确认后）
						oRecord.getCommissionSheet().getStatus() == 4 ||	//已结账
						oRecord.getCommissionSheet().getStatus() == 9){		//已结束
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("该委托单已完工，并且没有重新编制证书的申请，不能再次提交原始记录！");
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//待删除的证书Doc文件、Excel文件（非正式版的）
				Certificate certificate = oRecord.getCertificate();	//最后判断certificate的Id是否为null，如果为null，则说明需要更新证书
				//若还没有证书记录，则先生成一份数据库的证书记录（版本号为-1，证书Doc文件等非空字段为空字符串""）：用于预留证书编号
				if(certificate == null){
					//查询委托单下的第几份记录
					Integer sequence = oRecordMgr.getAvailableSequence(oRecord.getCommissionSheet());
					certificate = new Certificate();
					certificate.setCode(UIDUtil.get22BitUID());
					certificate.setVersion(-1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(sequence);
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), sequence.intValue(), -1));
					certificate.setFileName("");
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//该原始记录的证书为正式版本：证书需要重新生成，版本加1
					//更新Certificate
					certificate = new Certificate();
					certificate.setCode(oRecord.getCertificate().getCode());
					certificate.setVersion(oRecord.getCertificate().getVersion() + 1);
					certificate.setCommissionSheet(oRecord.getCommissionSheet());
					certificate.setOriginalRecord(oRecord);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setSequece(oRecord.getCertificate().getSequece());
					certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece().intValue(), certificate.getVersion()));
					certificate.setFileName("");
					certificate.setPdf(null);
				}else if(certificate.getDoc().length() > 0){	//不是正式版本且存在证书Doc文件：则生成正式版本的证书后，将老版本的证书Doc文件删除
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				
				//核验和授权签字记录，如果vNew的ID为null，则新增一个记录，否则，更新原记录
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				if(vNew == null){
					vNew = new VerifyAndAuthorize();
					vNew.setCode(UIDUtil.get22BitUID());
					vNew.setVersion(0);
					vNew.setCommissionSheet(oRecord.getCommissionSheet());
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setSysUserByCreatorId(loginUser);
				}else{
					vNew.setAuthorizeRemark(null);
					vNew.setAuthorizeResult(null);
					vNew.setAuthorizeTime(null);
					vNew.setCertificate(null);
					vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
					vNew.setIsAuthBgRuning(null);
					vNew.setOriginalRecordExcel(null);
					vNew.setSysUserByAuthorizeExecutorId(null);
//					vNew.setSysUserByAuthorizerId(null);
					vNew.setSysUserByCreatorId(loginUser);
//					vNew.setSysUserByVerifierId(null);
					vNew.setVerifyRemark(null);
					vNew.setVerifyResult(null);
					vNew.setVerifyTime(null);
//					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//重新生成一份核验和授权签字记录：版本号+1
//						vNew.setId(null);
//						vNew.setVersion(vNew.getVersion() + 1);
//					}
				}
				
				//处理上传的文件
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//存储生成的原始记录到文件数据库中的参数
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getCertificate().getCode());
					File tempInputFile = null, fDocOut = null, fPdfOut = null, fDocXml = null;
					File fXlsRecord = null, fXlsXML = null, fXlsUpdate = null, fXlsUpdateWithPic = null, fXlsUpdatePdf = null;
					try{
						
						tempInputFile = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						myFile.saveAs(tempInputFile.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
												
						String docXmlFileId = null;
						if(oRecord.getCertificate() != null && 
								oRecord.getCertificate().getFileName() != null &&
								oRecord.getCertificate().getFileName().equalsIgnoreCase(FileName) &&
								oRecord.getCertificate().getXml() != null &&
								oRecord.getCertificate().getXml().length() > 0){	//使用原XML
							docXmlFileId = oRecord.getCertificate().getXml();
						}else{	//使用现行的模板定义文件
							//获取证书XML配置文件
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
							
							String docXmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//证书模板配置文件的名称
							Pattern patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("未找到证书模板文件对应的配置文件:%s", docXmlFileName));
							}
							docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//证书模板xml文件的ID
						}
						if(docXmlFileId == null){
							throw new Exception(String.format("未找到字段定义文件：%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
						}
						//从文件数据库中取出证书xml定义文件
						fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
						if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("下载证书模板配置文件失败！");
						}
						ParseXMLAll docParser = new ParseXMLAll(fDocXml);
						
						
						fDocOut = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						fPdfOut = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
						WordUtil.HandleUploadedCertificateWord(tempInputFile, fDocOut, fPdfOut, docParser, oRecord, certificate);
						
						if(!fDocOut.exists() || fDocOut.length() == 0){
							throw new Exception("证书文件更新证书编号失败！");
						}
						if(!fPdfOut.exists() || fPdfOut.length() == 0){
							throw new Exception("证书Word文件生成PDF失败！");
						}
						certificate.setXml(docXmlFileId);
						certificate.setFileName(FileName);
						
						//保存证书文件
						if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("保存文件至服务器失败！");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Doc文件的ID
						
						//生成并保存pdf文件
						if(paramsMap.containsKey("_id")){
							paramsMap.remove("_id");
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", FileName.substring(0, FileName.lastIndexOf("."))));
						if(!MongoDBUtil.gridFSUpload(fPdfOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("保存PDF文件至服务器失败！");
						}
						String pdfIdStr = paramsMap.get("_id").toString();	//PDF文件的ID;	
						
						certificate.setDoc(docIdStr);
						certificate.setPdf(pdfIdStr);
						
						
						
						/***********更新数据库数据库 ：oRecord、oRecordExcel、Certificate、Vertificate**********/
						//判断是否需要新增OriginalRecordExcel
						OriginalRecordExcel excel = null;
						Integer VersionInt = 0;
						boolean isNewExcel = false;		//标志位，用于更新数据库时判断
						String excelCode = null;
						if(oRecord.getOriginalRecordExcel() == null){	//尚未有过原始记录
							excelCode = UIDUtil.get22BitUID();	//文件集名称
							VersionInt = 0;	//第一版版本号从0开始
							isNewExcel = true;
							excel = new OriginalRecordExcel();
							excel.setDoc("");
							excel.setPdf(null);
							excel.setXml("");
							excel.setFileName("");
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//已经是正式版的，需要重新生成版本
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
							excel = new OriginalRecordExcel();
							excel.setDoc(oRecord.getOriginalRecordExcel().getDoc());
							excel.setPdf(null);
							excel.setXml(oRecord.getOriginalRecordExcel().getXml());
							excel.setFileName(oRecord.getOriginalRecordExcel().getFileName());
						}else{	//替换之前的非正式版
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
							excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
						}
						
						//更新OriginalRecordExcel
						excel.setCode(excelCode);
						excel.setCommissionSheet(oRecord.getCommissionSheet());
						excel.setOriginalRecord(oRecord);
						excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						excel.setVersion(VersionInt);	//版本号
						excel.setCertificateCode(certificate.getCertificateCode());
						
						
						if(excel.getDoc() != null && excel.getDoc().length() > 0 && excel.getFileName() != null && excel.getFileName().length() > 0){
							//获取原始记录Excel文件及其配置文件
							String xlsFileName = excel.getFileName();	//原始记录Excel文件的文件名
							fXlsRecord = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
							if(!MongoDBUtil.gridFSDownloadById(fXlsRecord, excel.getDoc(), MongoDBUtil.CollectionType.OriginalRecord)){
								throw new Exception("获取原始记录Excel文件失败！");
							}
							//查询原始记录配置文件的名称并获取文件（从数据库中读取xml文件的ID 或读取最新的XML文件）
							String xlsXmlFileId = null;
							if(excel.getXml().length() > 0){	//使用原XML（从数据库中读取xml文件的ID）
								xlsXmlFileId = excel.getXml();
							}else{	//使用现行的模板定义文件
								//查找字段定义xml文件
								HashMap<String, Object> map = new HashMap<String, Object>();
								String xlsXmlFileName = String.format("%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf(".")));	//原始记录配置文件的名称
								map.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录的配置文件的文件集名称
								Pattern pattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
								map.put(MongoDBUtil.KEYNAME_FileName, pattern);
								map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//文件状态：有效
								JSONArray retArray2 = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
								if(retArray2.length() == 0){
									throw new Exception(String.format("未找到原始记录文件对应的配置文件:%s", xlsXmlFileName));
								}
								xlsXmlFileId = ((JSONObject)retArray2.get(0)).getString("_id");//原始记录xml文件的ID
								
							}
							if(xlsXmlFileId == null){
								throw new Exception(String.format("未找到字段定义文件：%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf("."))));
							}
							
							fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
							if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
								throw new Exception(String.format("获取原始记录配置文件失败：%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf("."))));
							}
							ParseXMLAll xlsParser = new ParseXMLAll(fXlsXML);
							
							boolean hasTag = false;	//查看原始记录是否有证书编号对应字段
							String key = "CertificateCode";	//证书编号属性名
							for(int k = 0; k < xlsParser.getQNameCount(key); k++){
								if(xlsParser.getAttribute(key, "fieldClass", k) != null && xlsParser.getAttribute(key, "fieldClass", k).equalsIgnoreCase("com.jlyw.hibernate.Certificate")){
									hasTag = true;
									break;
								}
							}
							if(hasTag){	//有相应标签，则需要修改原始记录
								fXlsUpdate = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
								ExcelUtil.updateExcelWithCertificateCode(fXlsRecord, fXlsUpdate, certificate, xlsParser);
								if(!fXlsUpdate.exists() || fXlsUpdate.length() == 0){
									throw new Exception("更新原始记录Excel文件中的证书编号失败！");
								}
								
								//存储更新后的Excel到文件数据库中的参数
								HashMap<String, Object> xlsParamsMap = new HashMap<String, Object>();
								xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, xlsFileName);
								xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
								xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
								xlsParamsMap.put(MongoDBUtil.KEYNAME_FileSetName, excel.getCode());
								if(!MongoDBUtil.gridFSUpload(fXlsUpdate, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存更新后的原始记录Excel至文件服务器失败！");
								}
								String xlsUpdateIdStr = xlsParamsMap.get("_id").toString();	//Excel文件的ID
								
								//将Excel无效信息去除并生成PDF版本
								fXlsUpdateWithPic = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.substring(xlsFileName.lastIndexOf(".")));
								ExcelUtil.removeAdditionalInfo(fXlsUpdate, fXlsUpdateWithPic, xlsParser);	//移除Excel文件的附加信息
								fXlsUpdatePdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");//生成pdf文件
								if(fXlsUpdateWithPic.exists() && fXlsUpdateWithPic.length() > 0){	//插入了签名图片
									Office2PdfUtil.docToPdf(fXlsUpdateWithPic, fXlsUpdatePdf);
								}else{
									Office2PdfUtil.docToPdf(fXlsUpdate, fXlsUpdatePdf);
								}
								//保存pdf文件
								if(xlsParamsMap.containsKey("_id")){
									xlsParamsMap.remove("_id");
								}
								xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", xlsFileName.substring(0, xlsFileName.lastIndexOf(".")==-1?0:xlsFileName.lastIndexOf('.'))));
								if(!MongoDBUtil.gridFSUpload(fXlsUpdatePdf, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("保存更新后的原始记录PDF至文件服务器失败！");
								}
								String xlsUpdatePdfIdStr = xlsParamsMap.get("_id").toString();	//PDF文件的ID;	
								
								//更新数据库
								excel.setDoc(xlsUpdateIdStr);
								excel.setPdf(xlsUpdatePdfIdStr);
							}
						}					
						
						//更新Certificate
						if(certificate.getVersion() < 0){
							certificate.setVersion(0);	//版本号为0开始，-1为预留证书编号
						}
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//上传人员为检定员本人
							certificate.setSysUser(null);
						}else{
							certificate.setSysUser(loginUser);
						}						
						
						//更新授权和签字记录
						vNew.setCertificate(certificate);
						vNew.setOriginalRecordExcel(excel);
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						oRecordMgr.uploadCertificateUpdateDB(oRecord, excel, certificate, vNew);						
						
						if(!certificateMgr.update(certificate)){
							throw new Exception("更新数据库失败：更新证书信息！");
						}
						
						//按需删除文件数据库中过时的原始记录和证书
						try{
							if(toBeDeleteFileIdExcel != null && toBeDeleteFileIdExcel.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(原始记录Excel id:"+toBeDeleteFileIdExcel+")", eee);
						}
						try{
							if(toBeDeleteFileIdCertificate != null && toBeDeleteFileIdCertificate.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(证书Doc id:"+toBeDeleteFileIdCertificate+")", eee);
						}
						
					}catch(Exception ex){
						throw ex;
					}finally{
						if(tempInputFile != null && tempInputFile.exists()){
							tempInputFile.delete();
						}
						if(fPdfOut != null && fPdfOut.exists()){
							fPdfOut.delete();
						}
						if(fDocOut != null && fDocOut.exists()){
							fDocOut.delete();
						}
						if(fDocXml != null && fDocXml.exists()){
							fDocXml.delete();
						}
						
						if(fXlsRecord != null && fXlsRecord.exists()){
							fXlsRecord.delete();
						}
						
						if(fXlsXML != null && fXlsXML.exists()){
							fXlsXML.delete();
						}
						
						if(fXlsUpdate != null && fXlsUpdate.exists()){
							fXlsUpdate.delete();
						}
						
						if(fXlsUpdateWithPic != null && fXlsUpdateWithPic.exists()){
							fXlsUpdateWithPic.delete();
						}
						
						if(fXlsUpdatePdf != null && fXlsUpdatePdf.exists()){
							fXlsUpdatePdf.delete();
						}
					}
				}else{
					throw new Exception("文件上传失败！");
				}
				retJSON12.put("IsOK", true);
				//retJSON12.put("msg", "修改证书成功！" + alertString==null?"":alertString.toString());
			}catch(Exception e){
				
				try {
					retJSON12.put("IsOK", false);
					retJSON12.put("msg", String.format("上传证书失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileUploadServlet-->case 12", e);
				}else{
					log.error("error in FileUploadServlet-->case 12", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON12.toString());
			}
			break;
		}
		
		
	}
}
