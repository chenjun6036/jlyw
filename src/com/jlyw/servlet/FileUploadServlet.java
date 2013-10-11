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
		case 1:	//��ͨ�ļ��ϴ�
			JSONObject retJSON = new JSONObject();
			try{
				String FilesetName = request.getParameter("FilesetName");
				String FileType = request.getParameter("FileType");
				if(FilesetName == null || FileType == null || FilesetName.length() == 0 || FileType.length() == 0){
					throw new Exception("�ļ��ϴ�������������");
				}
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, loginUser == null?"":loginUser.getId().toString());
				paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, loginUser == null?"":loginUser.getName());
				
				StringBuffer failFileNames = new StringBuffer();	//�ϴ�ʧ�ܵ��ļ�����
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
					HashMap<String, Object> searchMap = new HashMap<String, Object>();	//��������
					searchMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
					
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//������·�����ļ���
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//ȡ����·���Ĵ��ļ���
							
							if(fileName.contains("-") || fileName.contains("%") || fileName.contains("$") || fileName.contains("^") || fileName.contains("&")){
								throw new Exception(String.format("�ļ� %s �ϴ�ʧ�ܣ�ԭ���ļ������������'-'��'%%'��'$'��'^'��'&'��������ţ�", fileName));
							}
							
							//��ѯ����
							Pattern pattern = MongoPattern.compile("^"+fileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
							searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
							
							if(searchMap.containsKey(MongoDBUtil.KEYNAME_FileStatus)){
								searchMap.remove(MongoDBUtil.KEYNAME_FileStatus);
							}
							if(paramsMap.containsKey(MongoDBUtil.KEYNAME_FileStatus)){
								paramsMap.remove(MongoDBUtil.KEYNAME_FileStatus);
							}
							
							/******       ������Ҫ��ѯ�Ƿ�����ͬ���ļ����Ѵ���             *******/
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
									throw new Exception(String.format("�ļ� %s �Ѵ��ڣ������ظ��ϴ���", fileName));
								}
								break;
							}
							
							//�ϴ�
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
							if(Integer.parseInt(FileType) == UploadDownLoadUtil.Type_Template){
								paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬������
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
					retJSON.put("msg", String.format("������ʾ���ϴ�ʧ�ܵ��ļ���%s", failFileNames.toString()));
				}
			}catch(Exception e){
				
				try {
					retJSON.put("IsOK", false);
					retJSON.put("msg", String.format("�ļ��ϴ�ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 1", e);
				}else{
					log.error("error in FileUploadServlet-->case 1", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON.toString());
			}
			break;
		case 2:	//WebOffice�ϴ�ԭʼ��¼����ʽ��xlsEdit
			JSONObject retJSON2=new JSONObject();
			try{
				// ��ʼ���ϴ����  
				SmartUpload mySmartUpload=new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// ��ȡ�ϴ�����¼		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //ԭʼ��¼��ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//ԭʼ��¼�İ汾��
		        
				StringBuilder alertString = new StringBuilder();//������������ʱ����ʾ��Ϣ
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				
				if(FileName.contains("-")){	//�ļ�����ģ���ļ���-���к�.��չ��
					int index1 = FileName.indexOf('-');
					int index2 = FileName.lastIndexOf('.');
					if(index2 > index1){
						FileName = FileName.substring(0, index1) + FileName.substring(index2, FileName.length());
					}
				}
				
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("���༭��ԭʼ��¼�������°汾���������������°汾��ԭʼ��¼�������ϱ༭��");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null ){
					throw new Exception("����δ��¼�������ύԭʼ��¼��");
				}if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��ԭʼ��¼�Ѿ�������ʽ��֤�飡�����Ǹ�����������ļ����ˣ������ٴ��ύԭʼ��¼��");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ��ύԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ��ύԭʼ��¼��");
					}
				}
				
				String xlsXmlFileId = null;
				if(oRecord.getOriginalRecordExcel() != null && 
						oRecord.getOriginalRecordExcel().getFileName() != null &&
						oRecord.getOriginalRecordExcel().getFileName().equalsIgnoreCase(FileName) &&
						oRecord.getOriginalRecordExcel().getXml().length() > 0){	//ʹ��ԭXML
					xlsXmlFileId = oRecord.getOriginalRecordExcel().getXml();
				}else{	//ʹ�����е�ģ�嶨���ļ�
					//�����ֶζ���xml�ļ�
					String xmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
					HashMap<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
					Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
					searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
					searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
					
					JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
					if(retArray.length() == 0){
						throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s", xmlFileName));
					}
					xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
				}
				if(xlsXmlFileId == null){
					throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
				}
				
				
				//���ļ����ݿ���ȡ��xml�����ļ�
				File fXlsXML = null;
				ParseXMLAll xlsParser = null;
				try{
					fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception(String.format("��ȡ�ֶζ����ļ�ʧ�ܣ�%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
					}
					xlsParser = new ParseXMLAll(fXlsXML);
				}catch(Exception e){
					throw e;
				}finally{
					if(fXlsXML != null && fXlsXML.exists()){
						fXlsXML.delete();
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//��ɾ����֤��Doc�ļ���Excel�ļ�������ʽ��ģ�
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ����֤��
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//��ԭʼ��¼��֤��Ϊ��ʽ�汾��֤����Ҫ�������ɣ��汾��1
					//����Certificate
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
				}else if(certificate.getDoc().length() > 0){	//������ʽ�汾�Ҵ���֤��Doc�ļ�����������ʽ�汾��֤��󣬽��ϰ汾��֤��Doc�ļ�ɾ��
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				//�������Ȩǩ�ּ�¼�����ڴӷ����д��������Ա,���vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
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
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//��������һ�ݺ������Ȩǩ�ּ�¼���汾��+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				
				//�����ϴ����ļ�
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					
					File fXlsInput = null, fXlsOut = null, fXlsPdf = null;
					File fXlsOutToPdf = null;	//��������PDF����ʱExcel�ļ�
					File fDocTemplate = null, fDocXml = null, fDocOut = null, fDocPdf = null;
					File fPicWorkStaff = null;	//��У��Ա��ǩ��ͼƬ
					try{
						fXlsInput = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						myFile.saveAs(fXlsInput.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
						
						//��ѯ�����淶��������׼����׼����
						String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
						List<Specification> speList = oRecordMgr.findByHQL(queryStringSpe, oRecord.getId());
						String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
						List<Standard> stdList = oRecordMgr.findByHQL(queryStringStd, oRecord.getId());
						String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
						List<StandardAppliance> stdAppList = oRecordMgr.findByHQL(queryStringStdApp, oRecord.getId());
						
						StringBuffer docModeFileNameBuffer = new StringBuffer();
						fXlsOut = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						//�ϴ�ExcelУ�顢����
						ExcelUtil.uploadExcel(fXlsInput, fXlsOut, 
								oRecord.getCommissionSheet(), oRecord, oRecord.getSysUserByStaffId(), 
								certificate, oRecord.getTargetAppliance().getApplianceStandardName(), 
								speList, stdList, stdAppList, vNew, docModeFileNameBuffer, xlsParser);
						
						if(!fXlsOut.exists() || fXlsOut.length() == 0){
							throw new Exception("У�顢�����ϴ���Excel�ļ�ʧ�ܣ�");
						}
						fXlsOutToPdf = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						ExcelUtil.removeAdditionalInfo(fXlsOut, fXlsOutToPdf, xlsParser);	//�Ƴ�Excel�ļ��еĸ�����Ϣ
						fXlsPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//���ɵ�Excel pdf�ļ�
						if(fXlsOutToPdf.exists() && fXlsOutToPdf.length() > 0){		//ȥ��������Ϣ
							Office2PdfUtil.docToPdf(fXlsOutToPdf, fXlsPdf);
						}else{
							Office2PdfUtil.docToPdf(fXlsOut, fXlsPdf);
						}
						if(!fXlsPdf.exists() || fXlsPdf.length() == 0){
							throw new Exception("����ԭʼ��¼Excel��PDF�ļ�ʧ�ܣ�");
						}
												
						//�ж��Ƿ���Ҫ����֤��
						if(docModeFileNameBuffer.toString().length() > 0){	//����֤��
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							searchMap.clear();
							String docModFileName = docModeFileNameBuffer.toString();
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
							Pattern patternCertificate = MongoPattern.compile("^"+docModFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
							
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���%s", docModFileName));
							}
							String docModeFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ���ļ���ID
							
							String docXmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
							patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", docXmlFileName));
							}
							String docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ��xml�ļ���ID
							
							//���ļ����ݿ���ȡ��ģ���ļ�
							fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
							if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, docModeFileId, MongoDBUtil.CollectionType.Template)){
								throw new Exception("����֤��ģ���ļ�ʧ�ܣ�");
							}
							//���ļ����ݿ���ȡ��֤��xml�����ļ�
							fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
							if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
								throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
							}
							ParseXMLAll docParser = new ParseXMLAll(fDocXml);
							fDocOut = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
							fDocPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
							
							/*******        ��ȡ�춨Աǩ��ͼƬ         ********/
							HashMap<String, Object> picParams = new HashMap<String, Object>();
							if(oRecord.getSysUserByStaffId().getSignature() != null){	//��ȡ�춨��Ա��ǩ��ͼƬ
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
								throw new Exception("֤���ļ�����ʧ�ܣ�");
							}
							if(!fDocPdf.exists() || fDocPdf.length() == 0){
								throw new Exception("֤��Word�ļ�����PDFʧ�ܣ�");
							}
							certificate.setXml(docXmlFileId);
							certificate.setFileName(docModFileName);
						}else{
							certificate.setFileName("");
						}
						
						/**************�ϴ����ݿ�(Excel�ļ���Excel��PDF�ļ���Word�ļ���Word��PDF�ļ�)**************/
						//�ж��Ƿ���Ҫ����OriginalRecordExcel
						Integer VersionInt = 0;
						boolean isNewExcel = false;		//��־λ�����ڸ������ݿ�ʱ�ж�
						String excelCode = null;
						if(oRecord.getOriginalRecordExcel() == null){	//��δ�й�ԭʼ��¼
							excelCode = UIDUtil.get22BitUID();	//�ļ�������
							VersionInt = 0;	//��һ��汾�Ŵ�0��ʼ
							isNewExcel = true;
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
						}else{	//�滻֮ǰ�ķ���ʽ��
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, excelCode);//�ļ�������
						
						
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
						if(!MongoDBUtil.gridFSUpload(fXlsOut, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
							throw new Exception("����ԭʼ��¼Excel�ļ���������ʧ�ܣ�");
						}
						String xlsIdStr = paramsMap.get("_id").toString();	//Excel�ļ���ID
						if(paramsMap.containsKey("_id")){
							paramsMap.remove("_id");
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", FileName.substring(0, FileName.lastIndexOf('.'))));
						if(!MongoDBUtil.gridFSUpload(fXlsPdf, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
							throw new Exception("����ԭʼ��¼Excel��PDF�ļ���������ʧ�ܣ�");
						}
						String xlsPdfIdStr = paramsMap.get("_id").toString();	//Excel PDF�ļ���ID
						
						if(fDocOut != null){	//����֤���ļ���֤��PDF�ļ�
							if(paramsMap.containsKey("_id")){
								paramsMap.remove("_id");
							}
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, certificate.getFileName());
							if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
								throw new Exception("����֤���ļ���������ʧ�ܣ�");
							}
							String docIdStr = paramsMap.get("_id").toString();	//֤���ļ���ID
							if(paramsMap.containsKey("_id")){
								paramsMap.remove("_id");
							}
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", certificate.getFileName().substring(0, certificate.getFileName().lastIndexOf("."))));
							if(!MongoDBUtil.gridFSUpload(fDocPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
								throw new Exception("����֤���ļ���������ʧ�ܣ�");
							}
							String docPdfIdStr = paramsMap.get("_id").toString();	//֤��PDF�ļ���ID
							certificate.setDoc(docIdStr);
							certificate.setPdf(docPdfIdStr);
						}
						
						/***********�������ݿ����ݿ� ��oRecord��oRecordExcel��Certificate��Vertificate**********/
						//����OriginalRecordExcel
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
						excel.setVersion(VersionInt);	//�汾��
						excel.setXml(xlsXmlFileId);
						excel.setFileName(FileName);
						excel.setCertificateCode(certificate.getCertificateCode());
						
						
						//����Certificate
						if(certificate.getVersion() < 0){
							certificate.setVersion(0);	//�汾��Ϊ0��ʼ��-1ΪԤ��֤����
						}
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//�ϴ���ԱΪ�춨Ա����
							certificate.setSysUser(null);
						}else{
							certificate.setSysUser(loginUser);
						}
						
						//������Ȩ��ǩ�ּ�¼
						vNew.setCertificate(certificate);
						vNew.setOriginalRecordExcel(excel);
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						
						oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//�������ݿ�
						try{
							//���³�������
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
							log.debug("exception in FileUploadServlet-->case 2-->���³�������", eee);
						}
						try{
							//������������
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
							log.debug("exception in FileUploadServlet-->case 2-->������������", eee);
						}
						
						//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼
						try{
							if(toBeDeleteFileIdExcel != null){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 2-->Delete file from mongoDB(ԭʼ��¼Excel id:"+toBeDeleteFileIdExcel+")", eee);
						}
						try{
							if(toBeDeleteFileIdCertificate != null){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 2-->Delete file from mongoDB(֤��Word id:"+toBeDeleteFileIdCertificate+")", eee);
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
					throw new Exception("�ļ��ϴ�ʧ�ܣ�");
				}

				retJSON2.put("IsOK", true);
				retJSON2.put("msg", "�ϴ�ԭʼ��¼�ɹ���"+ ((alertString==null||alertString.toString().trim().length()==0)?"":(alertString.toString()+"�����е���")));
			}catch(Exception e){
				
				try {
					retJSON2.put("IsOK", false);
					retJSON2.put("msg", String.format("�ϴ�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 2", e);
				}else{
					log.error("error in FileUploadServlet-->case 2", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON2.toString());
			}
			break;
		case 3: //WebOffice�ϴ�ԭʼ��¼���ݴ棩:ͨ��SmartUpload�ϴ�
			JSONObject retJSON3 = new JSONObject();
			try{
				// ��ʼ���ϴ����  
				SmartUpload mySmartUpload=new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// ��ȡ�ϴ�����¼		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //ԭʼ��¼��ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//ԭʼ��¼�İ汾��
		        
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				
				if(FileName.contains("-")){	//�ļ�����ģ���ļ���-���к�.��չ��
					int index1 = FileName.indexOf('-');
					int index2 = FileName.lastIndexOf('.');
					if(index2 > index1){
						FileName = FileName.substring(0, index1) + FileName.substring(index2, FileName.length());
					}
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("���༭��ԭʼ��¼�������°汾���������������°汾��ԭʼ��¼�������ϱ༭��");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null ){
					throw new Exception("����δ��¼�������ݴ�ԭʼ��¼��");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��ԭʼ��¼�Ѿ�������ʽ��֤�飡�����Ǹ�����������ļ����ˣ������ٴ��ݴ�ԭʼ��¼��");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ��ύԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ��ύԭʼ��¼��");
					}
				}
				
				String xmlFileId = null;
				if(oRecord.getOriginalRecordExcel() != null && 
						oRecord.getOriginalRecordExcel().getFileName() != null &&
						oRecord.getOriginalRecordExcel().getFileName().equalsIgnoreCase(FileName) &&
						oRecord.getOriginalRecordExcel().getXml().length() > 0){	//ʹ��ԭXML
					xmlFileId = oRecord.getOriginalRecordExcel().getXml();
				}else{	//ʹ�����е�ģ�嶨���ļ�
					//�����ֶζ���xml�ļ�
					String xmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
					HashMap<String, Object> searchMap = new HashMap<String, Object>();
					searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
					Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
					searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
					searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
					
					JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
					if(retArray.length() == 0){
						throw new Exception(String.format("δ�ҵ���Ӧ���ֶζ����ļ���%s�������ݴ���ļ���", xmlFileName));
					}
					xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
				}
				if(xmlFileId == null){
					throw new Exception(String.format("δ�ҵ���Ӧ���ֶζ����ļ���%s.xml�������ݴ���ļ���", FileName.substring(0, FileName.lastIndexOf("."))));
				}
				
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ����֤��
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//��ԭʼ��¼��֤��Ϊ��ʽ�汾��֤����Ҫ�������ɣ��汾��1
					//����Certificate
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
				
				//�������Ȩǩ�ּ�¼�����ڴӷ����д��������Ա,���vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
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
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//��������һ�ݺ������Ȩǩ�ּ�¼���汾��+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				
				/******************        �ϴ��ļ����ļ����ݿ��У��ݴ棺��У��ʹ����ļ���          *************************/
				//�����ϴ����ļ�
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, FileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, loginUser.getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, loginUser.getName());
					
					File tempInputFile = null;
					try{
						tempInputFile = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						myFile.saveAs(tempInputFile.getAbsolutePath(), SmartUpload.SAVE_PHYSICAL);
						if(!tempInputFile.exists() || tempInputFile.length() == 0){
							throw new Exception("�ļ��ݴ�ʧ�ܣ�ԭ���ϴ��ļ�����");
						}
						
						Integer VersionInt = 0;
						String toBeDeletedFileId = null;
						boolean isNewExcel = false;		//��־λ�����ڸ������ݿ�ʱ�ж�
						if(oRecord.getOriginalRecordExcel() == null){	//��δ�й�ԭʼ��¼
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, UIDUtil.get22BitUID());//�ļ�������
							VersionInt = 0;	//��һ��汾�Ŵ�0��ʼ
							isNewExcel = true;
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
						}else{	//�滻֮ǰ�ķ���ʽ��
							paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeletedFileId = oRecord.getOriginalRecordExcel().getDoc();
						}
						
						if(!MongoDBUtil.gridFSUpload(tempInputFile, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
							throw new Exception("�����ļ���������ʧ�ܣ�");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Excel�ļ���ID
						
						/******************         �������ݿ�            ***********************/
						//����oRecord��oRecordExcel��Certificate
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
						excel.setVersion(VersionInt);	//�汾��
						excel.setXml(xmlFileId);
						excel.setFileName(FileName);
						
						//������Ȩ��ǩ�ּ�¼
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						
						oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//�������ݿ�
						
						//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼
						try{
							if(toBeDeletedFileId != null){
								MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 3-->Delete file from mongoDB(ԭʼ��¼Excel id:"+toBeDeletedFileId+")", eee);
						}
					}catch(Exception ex){
						throw ex;
					}
				}else{
					throw new Exception("�ļ��ϴ�ʧ�ܣ�");
				}
				
				retJSON3.put("IsOK", true);
			}catch(Exception e){
				try {
					retJSON3.put("IsOK", false);
					retJSON3.put("msg", String.format("�ϴ�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 3", e);
				}else{
					log.error("error in FileUploadServlet-->case 3", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON3.toString());
			}
			break;
		case 4:	//������ʽ�ϴ�ԭʼ��¼����ʽ��
			JSONObject retJSON4=new JSONObject();
			try{
				StringBuilder alertString = new StringBuilder();//������������ʱ����ʾ��Ϣ
				// ��ȡ�ϴ�����¼
				String OriginalRecordId = request.getParameter("OriginalRecordId");		//ԭʼ��¼��ID
				String VersionStr = request.getParameter("Version");	//ԭʼ��¼�İ汾��
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("ԭʼ��¼�汾��ͻ����ˢ�²鿴���°汾��ԭʼ��¼��");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null ){
					throw new Exception("����δ��¼�������ύԭʼ��¼��");
				}if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��ԭʼ��¼�Ѿ�������ʽ��֤�飡�����Ǹ�����������ļ����ˣ������ٴ��ύԭʼ��¼��");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ��ύԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ��ύԭʼ��¼��");
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//��ɾ����֤��Doc�ļ���Excel�ļ�������ʽ��ģ�
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ����֤��
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//��ԭʼ��¼��֤��Ϊ��ʽ�汾��֤����Ҫ�������ɣ��汾��1
					//����Certificate
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
				}else if(certificate.getDoc().length() > 0){	//������ʽ�汾�Ҵ���֤��Doc�ļ�����������ʽ�汾��֤��󣬽��ϰ汾��֤��Doc�ļ�ɾ��
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				
				//�������Ȩǩ�ּ�¼�����ڴӷ����д��������Ա,���vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
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
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//��������һ�ݺ������Ȩǩ�ּ�¼���汾��+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				
				//�����ϴ����ļ�
				if (ServletFileUpload.isMultipartContent(request)) {
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");

					
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					
					
					FileItemIterator fii = sfu.getItemIterator(request);
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//������·�����ļ���
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//ȡ����·���Ĵ��ļ���
							
							if(fileName.contains("-")){	//�ļ�����ģ���ļ���-���к�.��չ��
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
									oRecord.getOriginalRecordExcel().getXml().length() > 0){	//ʹ��ԭXML
								xlsXmlFileId = oRecord.getOriginalRecordExcel().getXml();
							}else{	//ʹ�����е�ģ�嶨���ļ�
								//�����ֶζ���xml�ļ�
								String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
								HashMap<String, Object> searchMap = new HashMap<String, Object>();
								searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
								Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
								searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
								searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
								
								JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
								if(retArray.length() == 0){
									throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s", xmlFileName));
								}
								xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
							}
							if(xlsXmlFileId == null){
								throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml", fileName.substring(0, fileName.lastIndexOf("."))));
							}
							
							//���ļ����ݿ���ȡ��xml�����ļ�
							File fXlsXML = null;
							ParseXMLAll xlsParser = null;
							try{
								fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
								if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
									throw new Exception(String.format("��ȡ�ֶζ����ļ�ʧ��: %s.xml", fileName.substring(0, fileName.lastIndexOf("."))));
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
							File fXlsOutToPdf = null;	//��������PDF����ʱExcel�ļ�
							File fDocTemplate = null, fDocXml = null, fDocOut = null, fDocPdf = null;
							File fPicWorkStaff = null;	//��У��Ա��ǩ��ͼƬ
							try{
								in = fis.openStream();
								
								//��ѯ�����淶��������׼����׼����
								String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
								List<Specification> speList = oRecordMgr.findByHQL(queryStringSpe, oRecord.getId());
								String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
								List<Standard> stdList = oRecordMgr.findByHQL(queryStringStd, oRecord.getId());
								String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
								List<StandardAppliance> stdAppList = oRecordMgr.findByHQL(queryStringStdApp, oRecord.getId());
								
								StringBuffer docModeFileNameBuffer = new StringBuffer();
								fXlsOut = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								//�ϴ�ExcelУ�顢����
								ExcelUtil.uploadExcel(in, fXlsOut, 
										oRecord.getCommissionSheet(), oRecord, oRecord.getSysUserByStaffId(), 
										certificate, oRecord.getTargetAppliance().getApplianceStandardName(), 
										speList, stdList, stdAppList, vNew, docModeFileNameBuffer, xlsParser);
								
								if(!fXlsOut.exists() || fXlsOut.length() == 0){
									throw new Exception("У�顢�����ϴ���Excel�ļ�ʧ�ܣ�");
								}
								fXlsOutToPdf = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								ExcelUtil.removeAdditionalInfo(fXlsOut, fXlsOutToPdf, xlsParser);	//�Ƴ�Excel�ļ��еĸ�����Ϣ
								fXlsPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//���ɵ�Excel pdf�ļ�
								if(fXlsOutToPdf.exists() && fXlsOutToPdf.length() > 0){		//ȥ��������Ϣ
									Office2PdfUtil.docToPdf(fXlsOutToPdf, fXlsPdf);
								}else{
									Office2PdfUtil.docToPdf(fXlsOut, fXlsPdf);
								}
								if(!fXlsPdf.exists() || fXlsPdf.length() == 0){
									throw new Exception("����ԭʼ��¼Excel��PDF�ļ�ʧ�ܣ�");
								}
														
								//�ж��Ƿ���Ҫ����֤��
								if(docModeFileNameBuffer.toString().length() > 0){	//����֤��
									HashMap<String, Object> searchMap = new HashMap<String, Object>();
									searchMap.clear();
									String docModFileName = docModeFileNameBuffer.toString();
									searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
									Pattern patternCertificate = MongoPattern.compile("^"+docModFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
									
									JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���%s", docModFileName));
									}
									String docModeFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ���ļ���ID
									
									String docXmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
									patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", docXmlFileName));
									}
									String docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ��xml�ļ���ID
									
									//���ļ����ݿ���ȡ��ģ���ļ�
									fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, docModeFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("����֤��ģ���ļ�ʧ�ܣ�");
									}
									//���ļ����ݿ���ȡ��֤��xml�����ļ�
									fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
									if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
									}
									ParseXMLAll docParser = new ParseXMLAll(fDocXml);
									fDocOut = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									fDocPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
									
									/*******        ��ȡ�춨Աǩ��ͼƬ         ********/
									HashMap<String, Object> picParams = new HashMap<String, Object>();
									if(oRecord.getSysUserByStaffId().getSignature() != null){	//��ȡ�춨��Ա��ǩ��ͼƬ
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
										throw new Exception("֤���ļ�����ʧ�ܣ�");
									}
									if(!fDocPdf.exists() || fDocPdf.length() == 0){
										throw new Exception("֤��Word�ļ�����PDFʧ�ܣ�");
									}
									certificate.setXml(docXmlFileId);
									certificate.setFileName(docModFileName);
								}else{
									certificate.setFileName("");
								}
								
								/**************�ϴ����ݿ�(Excel�ļ���Excel��PDF�ļ���Word�ļ���Word��PDF�ļ�)**************/
								//�ж��Ƿ���Ҫ����OriginalRecordExcel
								Integer VersionInt = 0;
								boolean isNewExcel = false;		//��־λ�����ڸ������ݿ�ʱ�ж�
								String excelCode = null;
								if(oRecord.getOriginalRecordExcel() == null){	//��δ�й�ԭʼ��¼
									excelCode = UIDUtil.get22BitUID();	//�ļ�������
									VersionInt = 0;	//��һ��汾�Ŵ�0��ʼ
									isNewExcel = true;
								}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
									excelCode = oRecord.getOriginalRecordExcel().getCode();
									VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
									isNewExcel = true;
								}else{	//�滻֮ǰ�ķ���ʽ��
									excelCode = oRecord.getOriginalRecordExcel().getCode();
									VersionInt = oRecord.getOriginalRecordExcel().getVersion();
									toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
								}
								paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, excelCode);//�ļ�������
								
								
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
								if(!MongoDBUtil.gridFSUpload(fXlsOut, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("����ԭʼ��¼Excel�ļ���������ʧ�ܣ�");
								}
								String xlsIdStr = paramsMap.get("_id").toString();	//Excel�ļ���ID
								if(paramsMap.containsKey("_id")){
									paramsMap.remove("_id");
								}
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", fileName.substring(0, fileName.lastIndexOf('.'))));
								if(!MongoDBUtil.gridFSUpload(fXlsPdf, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("����ԭʼ��¼Excel��PDF�ļ���������ʧ�ܣ�");
								}
								String xlsPdfIdStr = paramsMap.get("_id").toString();	//Excel PDF�ļ���ID
								
								if(fDocOut != null){	//����֤���ļ���֤��PDF�ļ�
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, certificate.getFileName());
									if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("����֤���ļ���������ʧ�ܣ�");
									}
									String docIdStr = paramsMap.get("_id").toString();	//֤���ļ���ID
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", certificate.getFileName().substring(0, certificate.getFileName().lastIndexOf("."))));
									if(!MongoDBUtil.gridFSUpload(fDocPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("����֤���ļ���������ʧ�ܣ�");
									}
									String docPdfIdStr = paramsMap.get("_id").toString();	//֤��PDF�ļ���ID
									certificate.setDoc(docIdStr);
									certificate.setPdf(docPdfIdStr);
								}
								
								/***********�������ݿ����ݿ� ��oRecord��oRecordExcel��Certificate��Vertificate**********/
								//����OriginalRecordExcel
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
								excel.setVersion(VersionInt);	//�汾��
								excel.setXml(xlsXmlFileId);
								excel.setFileName(fileName);
								excel.setCertificateCode(certificate.getCertificateCode());
								
								
								//����Certificate
								if(certificate.getVersion() < 0){
									certificate.setVersion(0);	//�汾��Ϊ0��ʼ��-1ΪԤ��֤����
								}
								certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
								if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//�ϴ���ԱΪ�춨Ա����
									certificate.setSysUser(null);
								}else{
									certificate.setSysUser(loginUser);
								}
								
								//������Ȩ��ǩ�ּ�¼
								vNew.setCertificate(certificate);
								vNew.setOriginalRecordExcel(excel);
								if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
									vNew.setSysUserByVerifierId(null);
									vNew.setSysUserByAuthorizerId(null);
								}
								
								oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//�������ݿ�
								try{
									//���³�������
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
									log.debug("exception in FileUploadServlet-->case 4-->���³�������", eee);
								}
								try{
									//������������
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
									log.debug("exception in FileUploadServlet-->case 4-->������������", eee);
								}
								
								//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼
								try{
									if(toBeDeleteFileIdExcel != null){
										MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 4-->Delete file from mongoDB(ԭʼ��¼Excel id:"+toBeDeleteFileIdExcel+")", eee);
								}
								try{
									if(toBeDeleteFileIdCertificate != null){
										MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 4-->Delete file from mongoDB(֤��Word id:"+toBeDeleteFileIdCertificate+")", eee);
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
				retJSON4.put("msg","�ϴ�ԭʼ��¼�ɹ��� " + (alertString==null?"":alertString.toString())+"�����е���");
			}catch(Exception e){
				
				try {
					retJSON4.put("IsOK", false);
					retJSON4.put("msg", String.format("�ϴ�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 4", e);
				}else{
					log.error("error in FileUploadServlet-->case 4", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON4.toString());
			}
			break;
		case 5: //������ʽ�ϴ�ԭʼ��¼���ݴ棩
			JSONObject retJSON5 = new JSONObject();
			try{
				// ��ȡ�ϴ�����¼
				String OriginalRecordId = request.getParameter("OriginalRecordId");		//ԭʼ��¼��ID
				String VersionStr = request.getParameter("Version");	//ԭʼ��¼�İ汾��
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("ԭʼ��¼�汾��ͻ����ˢ�²鿴���°汾��ԭʼ��¼��");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute("LOGIN_USER");
				if(loginUser == null ){
					throw new Exception("����δ��¼�������ݴ�ԭʼ��¼��");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��ԭʼ��¼�Ѿ�������ʽ��֤�飡�����Ǹ�����������ļ����ˣ������ٴ��ݴ�ԭʼ��¼��");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ��ύԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ��ύԭʼ��¼��");
					}
				}
				
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ����֤��
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//��ԭʼ��¼��֤��Ϊ��ʽ�汾��֤����Ҫ�������ɣ��汾��1
					//����Certificate
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
				
				//�������Ȩǩ�ּ�¼�����ڴӷ����д��������Ա,���vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
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
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//��������һ�ݺ������Ȩǩ�ּ�¼���汾��+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}

				//�����ϴ����ļ�
				if (ServletFileUpload.isMultipartContent(request)) {
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");
					
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					
					
					FileItemIterator fii = sfu.getItemIterator(request);
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//������·�����ļ���
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//ȡ����·���Ĵ��ļ���
							
							if(fileName.contains("-")){	//�ļ�����ģ���ļ���-���к�.��չ��
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
									oRecord.getOriginalRecordExcel().getXml().length() > 0){	//ʹ��ԭXML
								xmlFileId = oRecord.getOriginalRecordExcel().getXml();
							}else{	//ʹ�����е�ģ�嶨���ļ�
								//�����ֶζ���xml�ļ�
								String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
								HashMap<String, Object> searchMap = new HashMap<String, Object>();
								searchMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());
								Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
								searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
								searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
								
								JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
								if(retArray.length() == 0){
									throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s�������ݴ��ļ�", xmlFileName));
								}
								xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
							}
							if(xmlFileId == null){
								throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml�������ݴ��ļ���", fileName.substring(0, fileName.lastIndexOf("."))));
							}
							
							paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
							
							InputStream in = null;
							try{
								in = fis.openStream();
								
								Integer VersionInt = 0;
								String toBeDeletedFileId = null;
								boolean isNewExcel = false;		//��־λ�����ڸ������ݿ�ʱ�ж�
								if(oRecord.getOriginalRecordExcel() == null){	//��δ�й�ԭʼ��¼
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, UIDUtil.get22BitUID());//�ļ�������
									VersionInt = 0;	//��һ��汾�Ŵ�0��ʼ
									isNewExcel = true;
								}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
									VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
									isNewExcel = true;
								}else{	//�滻֮ǰ�ķ���ʽ��
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
									VersionInt = oRecord.getOriginalRecordExcel().getVersion();
									toBeDeletedFileId = oRecord.getOriginalRecordExcel().getDoc();
								}
								
								if(!MongoDBUtil.gridFSUpload(in, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("�����ļ���������ʧ�ܣ�");
								}
								String docIdStr = paramsMap.get("_id").toString();	//Excel�ļ���ID
								
								/******************         �������ݿ�            ***********************/
								//����oRecord��oRecordExcel��Certificate
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
								excel.setVersion(VersionInt);	//�汾��
								excel.setXml(xmlFileId);
								excel.setFileName(fileName);
								
								//������Ȩ��ǩ�ּ�¼
								if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
									vNew.setSysUserByVerifierId(null);
									vNew.setSysUserByAuthorizerId(null);
								}
								
								oRecordMgr.uploadExcelUpdateDB(oRecord, excel, isNewExcel, certificate, vNew);	//�������ݿ�
								
								//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼
								try{
									if(toBeDeletedFileId != null){
										MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.OriginalRecord);
									}
								}catch(Exception eee){
									log.debug("exception in FileUploadServlet-->case 5-->Delete file from mongoDB(ԭʼ��¼Excel id:"+toBeDeletedFileId+")", eee);
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
					retJSON5.put("msg", String.format("�ϴ�ԭʼ��¼ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 5", e);
				}else{
					log.error("error in FileUploadServlet-->case 5", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON5.toString());
			}
			break;
		case 6:	//�ϴ����ύ��֤�飨Weboffice��docEdit
			JSONObject retJSON6 = new JSONObject();
			try{
				// ��ʼ���ϴ����  
				SmartUpload mySmartUpload = new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// ��ȡ�ϴ�����¼		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //ԭʼ��¼��ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//֤��İ汾��
				String VerifierName = mySmartUpload.getRequest().getParameter("VerifierName");	//������Ա����
			
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateManager certificateMgr = new CertificateManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��֤���¼��");
				}
				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
					throw new Exception("�ü�¼���ϴ�ԭʼ��¼Excel�ļ�������ֱ�ӱ���֤�飡");
				}
				if(oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("֤��汾��ͻ���������������°汾��֤�鲢�����ϱ༭��");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�������ύ֤�飡");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��ԭʼ��¼������֤�飡�����Ǹ�����������ļ����ˣ������ٴ��ύ֤�飡");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ��ύԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ��ύԭʼ��¼��");
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//��ɾ����֤��Doc�ļ���Excel�ļ�������ʽ��ģ�
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ����֤��
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//��ԭʼ��¼��֤��Ϊ��ʽ�汾��֤����Ҫ�������ɣ��汾��1
					//����Certificate
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
				}else if(certificate.getDoc().length() > 0){	//������ʽ�汾�Ҵ���֤��Doc�ļ�����������ʽ�汾��֤��󣬽��ϰ汾��֤��Doc�ļ�ɾ��
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				
				
				//������Ա
				SysUser checkStaff = null;
				UserManager userMgr = new UserManager();
				if(VerifierName != null && VerifierName.trim().length() > 0 && !VerifierName.equals("-1")){
					
					List<SysUser> userList = userMgr.findByVarProperty(new KeyValueWithOperator("name", VerifierName.trim(), "="),
							new KeyValueWithOperator("status", 0, "="));
					if(userList != null && userList.size() > 0){
						checkStaff = userList.get(0);
					}
				}
				//�������Ȩǩ�ּ�¼�����ڴӷ����д��������Ա,���vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
				VerifyAndAuthorize vNew = oRecord.getVerifyAndAuthorize();
				//��ȡ��Ȩǩ����Ա
				SysUser authStaff = null;	//ǩ����
				QualificationManager qfMgr = new QualificationManager();
				if(checkStaff != null){ //������Ա��Ϊ��
					List<Object[]> qfRetList = qfMgr.getVerifyOrAuthorizeQualifyUsers(oRecord.getTargetAppliance().getApplianceStandardName().getId(), 0, FlagUtil.QualificationType.Type_Qianzi);
					if(qfRetList.size() == 0){
						throw new Exception(String.format("�Ҳ������߱�׼����'%s'��Ӧ����Ȩǩ����Ա������ϵ�������ݹ���Ա��", oRecord.getTargetAppliance().getApplianceStandardName().getName()));
					}
					Object[] userObj = qfRetList.get(0);
					authStaff = userMgr.findById((Integer)userObj[0]);
				}
				
				
				//�������Ȩǩ�ּ�¼�����vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
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
					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//��������һ�ݺ������Ȩǩ�ּ�¼���汾��+1
						vNew.setId(null);
						vNew.setVersion(vNew.getVersion() + 1);
					}
				}
				if(checkStaff != null&&authStaff!=null){ //������Ա��Ϊ��
					vNew.setSysUserByVerifierId(checkStaff);
					vNew.setSysUserByAuthorizerId(authStaff);
				}
				
				//�����ϴ����ļ�
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
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
								oRecord.getCertificate().getXml().length() > 0){	//ʹ��ԭXML
							docXmlFileId = oRecord.getCertificate().getXml();
						}else{	//ʹ�����е�ģ�嶨���ļ�
							//��ȡ֤��XML�����ļ�
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
							
							String docXmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
							Pattern patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", docXmlFileName));
							}
							docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ��xml�ļ���ID
						}
						if(docXmlFileId == null){
							throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
						}
						//���ļ����ݿ���ȡ��֤��xml�����ļ�
						fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
						if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
						}
						ParseXMLAll docParser = new ParseXMLAll(fDocXml);
						
						
						fDocOut = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						fPdfOut = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
						
						StringBuilder alertString=new StringBuilder();//����֤��ʱ���������޵���ʾ��Ϣ
						WordUtil.HandleUploadedCertificateWord(tempInputFile, fDocOut, fPdfOut, docParser, oRecord, certificate);
						
						if(!fDocOut.exists() || fDocOut.length() == 0){
							throw new Exception("֤���ļ�����֤����ʧ�ܣ�");
						}
						if(!fPdfOut.exists() || fPdfOut.length() == 0){
							throw new Exception("֤��Word�ļ�����PDFʧ�ܣ�");
						}
						certificate.setXml(docXmlFileId);
						certificate.setFileName(FileName);
						
						//����֤���ļ�
						if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("�����ļ���������ʧ�ܣ�");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Doc�ļ���ID
						
						//���ɲ�����pdf�ļ�
						if(paramsMap.containsKey("_id")){
							paramsMap.remove("_id");
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", FileName.substring(0, FileName.lastIndexOf("."))));
						if(!MongoDBUtil.gridFSUpload(fPdfOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("����PDF�ļ���������ʧ�ܣ�");
						}
						String pdfIdStr = paramsMap.get("_id").toString();	//PDF�ļ���ID;	
						
						certificate.setDoc(docIdStr);
						certificate.setPdf(pdfIdStr);
					
						/***********�������ݿ����ݿ� ��oRecord��oRecordExcel��Certificate��Vertificate**********/
						//�ж��Ƿ���Ҫ����OriginalRecordExcel
						Integer VersionInt = 0;
						boolean isNewExcel = false;		//��־λ�����ڸ������ݿ�ʱ�ж�
						String excelCode = null;
						if(oRecord.getOriginalRecordExcel() == null){	//��δ�й�ԭʼ��¼
							excelCode = UIDUtil.get22BitUID();	//�ļ�������
							VersionInt = 0;	//��һ��汾�Ŵ�0��ʼ
							isNewExcel = true;
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
						}else{	//�滻֮ǰ�ķ���ʽ��
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
						}
						
						//����OriginalRecordExcel
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
						excel.setVersion(VersionInt);	//�汾��
						excel.setXml("");
						excel.setFileName("");
						excel.setCertificateCode(certificate.getCertificateCode());
						
						//����Certificate
						if(certificate.getVersion() < 0){
							certificate.setVersion(0);	//�汾��Ϊ0��ʼ��-1ΪԤ��֤����
						}
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//�ϴ���ԱΪ�춨Ա����
							certificate.setSysUser(null);
						}else{
							certificate.setSysUser(loginUser);
						}						
						
						//������Ȩ��ǩ�ּ�¼
						vNew.setCertificate(certificate);
						vNew.setOriginalRecordExcel(excel);
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						oRecordMgr.uploadCertificateUpdateDB(oRecord, excel, certificate, vNew);						
						
						if(!certificateMgr.update(certificate)){
							throw new Exception("�������ݿ�ʧ�ܣ�����֤����Ϣ��");
						}
						
						//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼��֤��
						try{
							if(toBeDeleteFileIdExcel != null && toBeDeleteFileIdExcel.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(ԭʼ��¼Excel id:"+toBeDeleteFileIdExcel+")", eee);
						}
						try{
							if(toBeDeleteFileIdCertificate != null && toBeDeleteFileIdCertificate.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(֤��Doc id:"+toBeDeleteFileIdCertificate+")", eee);
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
					throw new Exception("�ļ��ϴ�ʧ�ܣ�");
				}
				retJSON6.put("IsOK", true);
			}catch(Exception e){
				
				try {
					retJSON6.put("IsOK", false);
					retJSON6.put("msg", String.format("�ϴ�֤��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 6", e);
				}else{
					log.error("error in FileUploadServlet-->case 6", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON6.toString());
			}
			break;
/*		case 7:	//�ݴ�֤�飨Weboffice��:ȡ���ù��ܣ���case 8���
			JSONObject retJSON7 = new JSONObject();
			try{
				// ��ʼ���ϴ����  
				SmartUpload mySmartUpload=new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// ��ȡ�ϴ�����¼		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //ԭʼ��¼��ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//֤��İ汾��
		        
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateManager certificateMgr = new CertificateManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null || oRecord.getCertificate() == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��֤���¼��");
				}
				if(oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("֤��汾��ͻ���������������°汾��֤�鲢�����ϱ༭��");
				}
				if(request.getSession().getAttribute("LOGIN_USER") == null ){
					throw new Exception("����δ��¼�������ݴ�֤�飡");
				}if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId())
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��֤���Ѿ�����ʽ�棡�����Ǹ�����������ļ����ˣ������ٴ��ݴ�֤�飡");
				}
				
				//�����ϴ����ļ�
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
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
						if(oRecord.getCertificate().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
							VersionInt = oRecord.getCertificate().getVersion() + 1;
						}else{	//�滻֮ǰ�ķ���ʽ��
							if(oRecord.getCertificate().getVersion() > 0){
								VersionInt = oRecord.getCertificate().getVersion();
							}
							if(oRecord.getCertificate().getDoc().length() > 0){
								toBeDeletedFileId = oRecord.getCertificate().getDoc();
							}
						}
						
						if(!MongoDBUtil.gridFSUpload(tempInputFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("�����ļ���������ʧ�ܣ�");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Doc�ļ���ID
						
						//Certificate
						Certificate certificate = certificateMgr.findById(oRecord.getCertificate().getId());
						certificate.setDoc(docIdStr);
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						certificate.setVersion(VersionInt);	//�汾��
						certificate.setFileName(FileName);
						certificate.setCertificateCode(UIDUtil.getCertificateCode(oRecord.getCommissionSheet().getCode(), certificate.getSequece(), VersionInt));
						
						if(!certificateMgr.update(certificate)){
							throw new Exception("�������ݿ�ʧ�ܣ�����֤����Ϣ��");
						}
						
						//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼
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
					throw new Exception("�ļ��ϴ�ʧ�ܣ�");
				}
				retJSON7.put("IsOK", true);
			}catch(Exception e){
				
				try {
					retJSON7.put("IsOK", false);
					retJSON7.put("msg", String.format("�ϴ�֤��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 7", e);
				}else{
					log.error("error in FileUploadServlet-->case 7", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON7.toString());
			}
			break;
		case 8:	//����֤��--��(���õĹ���)
			JSONObject retJSON8 = new JSONObject();
			try{
				String OriginalRecordId = request.getParameter("OriginalRecordId");
				String TemplateFileId = request.getParameter("TemplateFileId");	//Docģ���ļ���ID
				String VersionStr = request.getParameter("Version");//֤��İ汾��

				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager recordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = recordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getCertificate() != null && oRecord.getCertificate().getDoc().length() > 0
						&& oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){	//�Ѵ����°汾֤��Doc�ļ�����ֱ��ת��
					throw new Exception("֤��汾��ͻ����ˢ���б�鿴���°汾��֤�飡");
				}
				if(request.getSession().getAttribute("LOGIN_USER") == null ){
					throw new Exception("����δ��¼����������֤�飡");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId())
						&& FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){
					throw new Exception("��֤���Ѿ�����ʽ�棡�����Ǹ�����������ļ����ˣ������ٴ�����֤�飡");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ�����֤�飡");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ�����֤�飡");
					}
				}
				
				if(TemplateFileId == null || TemplateFileId.length() == 0){
					throw new Exception("������������");
				}
				if(oRecord.getOriginalRecordExcel() == null || oRecord.getOriginalRecordExcel().getPdf() == null){
					throw new Exception("ԭʼ��¼Excel�ļ�δ�ύ��");
				}
				JSONObject retObj = MongoDBUtil.getFileInfoById(TemplateFileId, MongoDBUtil.CollectionType.Template);
				if(retObj == null){
					throw new Exception("�Ҳ���ָ����֤��ģ���ļ���");
				}
				String fileId = retObj.getString("_id");	//֤��ģ���ļ���ID
				String docModFileName = retObj.getString(MongoDBUtil.KEYNAME_FileName);	//֤��ģ���ļ����ļ���
				String xmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, retObj.getString(MongoDBUtil.KEYNAME_FileSetName));
				Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", xmlFileName));
				}
				String xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
				
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ���Ӹ�֤����Ϣ����Ϊnull������Ҫ����
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ0��֤��Doc�ļ������ģ���ļ����������£�������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
						throw new Exception("����֤����ʧ�ܣ����Ժ����ԣ�");
					}
					oRecord.setCertificate(certificate);
					if(!recordMgr.update(oRecord)){
						throw new Exception("����֤����ʧ�ܣ����Ժ����ԣ�");
					}
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(certificate)){	//֤������ʽ�汾
					//����Certificate
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
				}else if(certificate.getVersion() < 0){		//����֤��ΪԤ��֤���ţ����ð汾��Ϊ0����ʽ֤�飩
					certificate.setVersion(0);
					certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
					certificate.setDoc("");
					certificate.setFileName(docModFileName);
					certificate.setXml(xmlFileId);					
				}
				
				File fDocTemplate = null, fDocXML = null, fXlsRecord = null, fXlsXML = null;//֤��ģ���ļ���֤��xml�����ļ���ԭʼ��¼�ļ�, ԭʼ��¼�ļ��������ļ�
				File fPicVerifier = null;	//����Ա��ǩ��ͼƬ�ļ�
				File fOutputFile = null, fOutputPdfFile = null;	//���ɵ�֤��Doc�ļ���֤��PDF�ļ�
				File fXlsUpdate = null, fXlsUpdatePdf = null;	//������Ҫ��֤��������һ���汾���������ɵ�һ��ԭʼ��¼������֤���ţ�����PDF�ļ�
				File fXlsUpdateWithPic = null;	//�����˼�У��Աǩ��ͼƬ��ԭʼ��¼�ļ�
				try{
					//���ļ����ݿ���ȡ��ģ���ļ�
					fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
					if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, fileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("����֤��ģ���ļ�ʧ�ܣ�");
					}
					
					//���ļ����ݿ���ȡ��֤��xml�����ļ�
					fDocXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fDocXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
					}
					ParseXMLAll docParser = new ParseXMLAll(fDocXML);
					
					//��ȡԭʼ��¼Excel�ļ����������ļ�
					String xlsFileName = oRecord.getOriginalRecordExcel().getFileName();	//ԭʼ��¼Excel�ļ����ļ���
					fXlsRecord = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
					if(!MongoDBUtil.gridFSDownloadById(fXlsRecord, oRecord.getOriginalRecordExcel().getDoc(), MongoDBUtil.CollectionType.OriginalRecord)){
						throw new Exception("��ȡԭʼ��¼Excel�ļ�ʧ�ܣ�");
					}
					//��ѯԭʼ��¼�����ļ������Ʋ���ȡ�ļ��������ݿ��ж�ȡxml�ļ���ID ���ȡ���µ�XML�ļ���
					String xlsXmlFileId = null;
					if(oRecord.getOriginalRecordExcel() != null && 
							oRecord.getOriginalRecordExcel().getXml().length() > 0){	//ʹ��ԭXML�������ݿ��ж�ȡxml�ļ���ID��
						xlsXmlFileId = oRecord.getOriginalRecordExcel().getXml();
					}else{	//ʹ�����е�ģ�嶨���ļ�
						//�����ֶζ���xml�ļ�
						String xlsXmlFileName = String.format("%s.xml", oRecord.getOriginalRecordExcel().getFileName().substring(0, oRecord.getOriginalRecordExcel().getFileName().lastIndexOf(".")));	//ԭʼ��¼�����ļ�������
						map.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼�������ļ����ļ�������
						pattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
						map.put(MongoDBUtil.KEYNAME_FileName, pattern);
						map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
						JSONArray retArray2 = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
						if(retArray2.length() == 0){
							throw new Exception(String.format("δ�ҵ�ԭʼ��¼�ļ���Ӧ�������ļ�:%s", xlsXmlFileName));
						}
						xlsXmlFileId = ((JSONObject)retArray2.get(0)).getString("_id");//ԭʼ��¼xml�ļ���ID
						
					}
					if(xlsXmlFileId == null){
						throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml", oRecord.getOriginalRecordExcel().getFileName().substring(0, oRecord.getOriginalRecordExcel().getFileName().lastIndexOf("."))));
					}
					
					fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception(String.format("��ȡԭʼ��¼�����ļ�ʧ�ܣ�%s.xml", oRecord.getOriginalRecordExcel().getFileName().substring(0, oRecord.getOriginalRecordExcel().getFileName().lastIndexOf("."))));
					}
					ParseXMLAll xlsParser = new ParseXMLAll(fXlsXML);
					
					HashMap<String, Object> picParams = new HashMap<String, Object>();
					if(oRecord.getSysUserByStaffId() != null && oRecord.getSysUserByStaffId().getSignature() != null){	//��ȡ������Ա��ǩ��ͼƬ
						picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
						JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
						if(jsonInfo != null){
							String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
							fPicVerifier = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
							MongoDBUtil.gridFSDownloadById(fPicVerifier, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
						}
					}
					
					//��ѯ�����淶��������׼����׼����
					String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
					List<Specification> speList = recordMgr.findByHQL(queryStringSpe, oRecord.getId());
					String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
					List<Standard> stdList = recordMgr.findByHQL(queryStringStd, oRecord.getId());
					String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
					List<StandardAppliance> stdAppList = recordMgr.findByHQL(queryStringStdApp, oRecord.getId());
					
					fOutputFile = File.createTempFile(UIDUtil.get22BitUID(), fDocTemplate.getName().substring(fDocTemplate.getName().lastIndexOf('.')));	//���ɵ�֤���ļ�
					fOutputPdfFile = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//���ɵ�PDF�ļ�
					WordUtil.MakeCertificateWord(fXlsRecord, fDocTemplate, fOutputFile, fOutputPdfFile, 
							xlsParser, docParser, 
							fPicVerifier==null?null:fPicVerifier.getAbsolutePath(),
							oRecord.getCommissionSheet(), oRecord, certificate, new AddressManager().findById(oRecord.getCommissionSheet().getHeadNameId()),
							speList, stdList, stdAppList);
					if(!fOutputFile.exists() || fOutputFile.length() == 0){
						throw new Exception("֤���ļ�����ʧ�ܣ�");
					}
					if(!fOutputPdfFile.exists() || fOutputPdfFile.length() == 0){
						throw new Exception("֤��Word�ļ�����PDFʧ�ܣ�");
					}
					
					//�洢���ɵ�֤��Doc���ļ����ݿ��еĲ���
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, docModFileName);
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
					if(!MongoDBUtil.gridFSUpload(fOutputFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
						throw new Exception("����֤�����ļ�������ʧ�ܣ�");
					}
					String docIdStr = paramsMap.get("_id").toString();	//Doc�ļ���ID
					
					//���ɲ�����pdf�ļ�
					if(paramsMap.containsKey("_id")){
						paramsMap.remove("_id");
					}
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", docModFileName.substring(0, docModFileName.lastIndexOf("."))));
					if(!MongoDBUtil.gridFSUpload(fOutputPdfFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
						throw new Exception("����֤��PDF�ļ���������ʧ�ܣ�");
					}
					String pdfIdStr = paramsMap.get("_id").toString();	//PDF�ļ���ID;	
					
					
					OriginalRecordExcel excel = oRecord.getOriginalRecordExcel();
					if(certificate.getId() == null){	//�µ�֤���ţ���Ҫ����ԭʼ��¼�ġ�֤����ֶΡ�
						ParseXMLAll xlsAllParser = new ParseXMLAll(fXlsXML);
						boolean hasTag = false;	//�鿴ԭʼ��¼�Ƿ���֤���Ŷ�Ӧ�ֶ�
						String key = "CertificateCode";	//֤����������
						for(int k = 0; k < xlsAllParser.getQNameCount(key); k++){
							if(xlsAllParser.getAttribute(key, "fieldClass", k) != null && xlsAllParser.getAttribute(key, "fieldClass", k).equalsIgnoreCase("com.jlyw.hibernate.Certificate")){
								hasTag = true;
								break;
							}
						}
						if(hasTag){	//����Ӧ��ǩ������Ҫ�޸�ԭʼ��¼
							fXlsUpdate = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
							ExcelUtil.updateExcelWithCertificateCode(fXlsRecord, fXlsUpdate, certificate, xlsAllParser);
							
							//�洢���º��Excel���ļ����ݿ��еĲ���
							HashMap<String, Object> xlsParamsMap = new HashMap<String, Object>();
							xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, xlsFileName);
							xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
							xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
							xlsParamsMap.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getOriginalRecordExcel().getCode());
							if(!MongoDBUtil.gridFSUpload(fXlsUpdate, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
								throw new Exception("������º��ԭʼ��¼Excel���ļ�������ʧ�ܣ�");
							}
							String xlsUpdateIdStr = xlsParamsMap.get("_id").toString();	//Excel�ļ���ID
							
							//����У��Ա��ǩ��ͼƬ����Excel�в�����PDF�汾
							fXlsUpdateWithPic = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.substring(xlsFileName.lastIndexOf(".")));
//							ExcelUtil.insertWorkerSignatureImgToExcel(fXlsUpdate, fXlsUpdateWithPic, fPicVerifier, xlsAllParser);	//����ǩ��ͼƬ������һ���µ�ԭʼ��¼Excel�ļ�
							ExcelUtil.removeAdditionalInfo(fXlsUpdate, fXlsUpdateWithPic, xlsAllParser);	//�Ƴ�Excel�ļ��ĸ�����Ϣ
							fXlsUpdatePdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");//����pdf�ļ�
							if(fXlsUpdateWithPic.exists() && fXlsUpdateWithPic.length() > 0){	//������ǩ��ͼƬ
								Office2PdfUtil.docToPdf(fXlsUpdateWithPic, fXlsUpdatePdf);
							}else{
								Office2PdfUtil.docToPdf(fXlsUpdate, fXlsUpdatePdf);
							}
							//����pdf�ļ�
							if(xlsParamsMap.containsKey("_id")){
								xlsParamsMap.remove("_id");
							}
							xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", xlsFileName.substring(0, xlsFileName.lastIndexOf(".")==-1?0:xlsFileName.lastIndexOf('.'))));
							if(!MongoDBUtil.gridFSUpload(fXlsUpdatePdf, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
								throw new Exception("������º��ԭʼ��¼PDF���ļ�������ʧ�ܣ�");
							}
							String xlsUpdatePdfIdStr = xlsParamsMap.get("_id").toString();	//PDF�ļ���ID;	
							
							//�������ݿ�
							excel.setDoc(xlsUpdateIdStr);
							excel.setPdf(xlsUpdatePdfIdStr);
							
						}
					}
					
					String toBeDeletedFileId = null;	//�Ƿ�����Ҫɾ������Ч֤��Doc�ļ�����δ������ʽ���֤��Doc
					if(certificate.getId() != null && certificate.getDoc().length() > 0){
						toBeDeletedFileId = certificate.getDoc();
					}
					
					//�������ݿ�
					certificate.setDoc(docIdStr);
					certificate.setPdf(pdfIdStr);
					certificate.setFileName(docModFileName);	//֤��ģ����ļ���
					certificate.setXml(xmlFileId);	//֤�������ļ���ID
//					if(!recordMgr.uploadCertificateUpdateDB(oRecord, excel, fXlsUpdate==null?false:true, certificate, certificate.getId()==null?true:false)){
					if(!recordMgr.uploadCertificateUpdateDB(oRecord, excel, true, certificate, certificate.getId()==null?true:false)){	//����������ǿ��Ϊtrue���ڸ���Excel�е�֤�����ֶε�
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}
					
					retJSON8.put("IsOK", true);
					try{
						//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼
						if(toBeDeletedFileId != null){
							MongoDBUtil.gridFSDeleteById(toBeDeletedFileId, MongoDBUtil.CollectionType.Certificate);
						}
					}catch(Exception ex){
						log.debug("exception in FileUploadServlet->case 8->MongoDBUtil.gridFSDeleteById", ex);
					}
				}catch(Exception re){
					throw re;
				}finally{	//�ر��ļ���;ɾ����ʱ�ļ�
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
					retJSON8.put("msg", String.format("����֤��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 8", e);
				}else{
					log.error("error in FileDownloadServlet-->case 8", e);
				}
			}finally{
				response.setContentType("text/html;charset=gbk");
				response.getWriter().write(retJSON8.toString());
			}
			break;*/
		case 9 : //ԭʼ��¼Excel�����ϴ�(ͨ��uploadify V3.1�ؼ��ϴ�)
			JSONObject retJSON9 = new JSONObject();
			try{
				String alert = "";//������������ʱ����ʾ��Ϣ
				
				String CommissionSheetId = request.getParameter("CommissionSheetId");
				if(CommissionSheetId == null || CommissionSheetId.length() == 0){
					throw new Exception("ί�е�δָ����");
				}
				CommissionSheetManager cSheetMgr = new CommissionSheetManager();
				CommissionSheet cSheet = cSheetMgr.findById(Integer.parseInt(CommissionSheetId));
				if(cSheet == null){
					throw new Exception("�Ҳ���ָ����ί�е���");
				}
				
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�������ϴ�ԭʼ��¼��");
				}
				//�ж�ί�е���״̬
				if(cSheet.getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ύԭʼ��¼��");
				}
				if(cSheet.getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						cSheet.getStatus() == 4 ||	//�ѽ���
						cSheet.getStatus() == 9){		//�ѽ���
					throw new Exception("��ί�е����깤���������ԭʼ��¼��");
				}
				
				//�����ϴ����ļ�
				if (ServletFileUpload.isMultipartContent(request)) {
					ServletFileUpload sfu = new ServletFileUpload();
					sfu.setHeaderEncoding("UTF-8");
					
					
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, loginUser.getId().toString());
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, loginUser.getName());
					
					
					ApplianceStandardNameManager stdNameMgr = new ApplianceStandardNameManager();
					
					FileItemIterator fii = sfu.getItemIterator(request);
					while (fii.hasNext()) {
						FileItemStream fis = fii.next();
						if (!fis.isFormField()) {
							String fileFullName = fis.getName();	//������·�����ļ���
							String fileName = fileFullName.substring(fileFullName.lastIndexOf("\\")+1);//ȡ����·���Ĵ��ļ���
							if(fileName.contains("-")){	//�ļ�����ģ���ļ���-���к�.��չ��
								int index1 = fileName.indexOf('-');
								int index2 = fileName.lastIndexOf('.');
								if(index2 > index1){
									fileName = fileName.substring(0, index1) + fileName.substring(index2, fileName.length());
								}
							}
							//����Excel�ֶζ���xml�ļ�
							String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							Pattern pattern0 = Pattern.compile("(?!(" + SystemCfgUtil.CertificateTemplateFilesetName +")).*$");//������ʽ���ļ������Ʋ�����֤��ģ���ļ�������ͷ��
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, pattern0);
							Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
							searchMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
							
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("δ�ҵ�Excel�����ļ���%s", xmlFileName));
							}
							String xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
							String xlsXmlFileSetName = ((JSONObject)retArray.get(0)).getString(MongoDBUtil.KEYNAME_FileSetName);	//�����ļ����ļ�����
							
							//���ļ����ݿ���ȡ��ԭʼ��¼Excel��xml�����ļ�
							File fXlsXML = null;
							ParseXMLAll xlsParser = null;
							try{
								fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
								if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
									throw new Exception(String.format("��ȡExcel�����ļ�ʧ��: %s", xmlFileName));
								}
								xlsParser = new ParseXMLAll(fXlsXML);
							}catch(Exception e){
								throw e;
							}finally{
								if(fXlsXML != null && fXlsXML.exists()){
									fXlsXML.delete();
								}
							}
							
							//��ȡ��׼����
							List<ApplianceStandardName> stdNameList = stdNameMgr.findByVarProperty(new KeyValueWithOperator("filesetName", xlsXmlFileSetName, "="));
							if(stdNameList.size() == 0){
								throw new Exception(String.format("�Ҳ���ģ���ļ� %s ����Ӧ�ı�׼���ƣ�", xmlFileName));
							}else if(stdNameList.size() > 1){
								throw new Exception(String.format("ģ���ļ� %s ��Ӧ�ı�׼���Ʋ�Ψһ��%s, %s", xmlFileName, stdNameList.get(0).getName(), stdNameList.get(0).getName()));
							}
							ApplianceStandardName stdName = stdNameList.get(0);	//���߱�׼����
							
							if(cSheet.getSpeciesType()==false && !cSheet.getApplianceSpeciesId().equals(stdName.getId()) ){
								throw new Exception(String.format("ģ���ļ�'%s'����Ӧ�ı�׼����'%s'�����ڸ�ί�е��´��������߷�Χ��", xmlFileName, stdName.getName()));
							}else if(cSheet.getSpeciesType()==true && !stdNameMgr.checkStandardNameInSpecial(stdName.getId(), cSheet.getApplianceSpeciesId())){
								throw new Exception(String.format("ģ���ļ�'%s'����Ӧ�ı�׼����'%s'�����ڸ�ί�е��´��������߷�Χ��", xmlFileName, stdName.getName()));
							}
							
							
							/***************************         ��ʼ�ϴ����ļ�                  ****************************/
							OriginalRecordManager oRecordMgr = new OriginalRecordManager();
							//�������е�ԭʼ��¼����
							String queryStringexistedNumber = "select count(model.quantity) from OriginalRecord as model where model.commissionSheet.id=? and model.status<>1";
							List<Long> iRetList = oRecordMgr.findByHQL(queryStringexistedNumber, cSheet.getId());
							int existedNumber = iRetList.get(0).intValue(), NumberInt = 1;	//�Ѵ��ڵ�ԭʼ��¼����������ӵ�ԭʼ��¼����
							if(NumberInt + existedNumber > cSheet.getQuantity()){
								throw new Exception("����ӵ�ԭʼ��¼�������࣬ԭʼ��¼�������ó���ί�е�������������");
							}
							//����ԭʼ��¼
							OriginalRecord oRecord = new OriginalRecord();
							oRecord.setCommissionSheet(cSheet);	//ί�е�
							oRecord.setStatus(0);	//״̬
							oRecord.setQuantity(1);	//��ԭʼ��¼�е���������
							oRecord.setSysUserByCreatorId(loginUser);
							oRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));	//����ʱ��
							
							//����һ�����ݿ��֤���¼���汾��Ϊ0��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
							Certificate certificate = null;
							if(certificate == null){
								//��ѯί�е��µĵڼ��ݼ�¼
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
							//����һ��Excel��¼
							OriginalRecordExcel excel = new OriginalRecordExcel();
							excel.setCode(UIDUtil.get22BitUID());
							excel.setCommissionSheet(cSheet);
							excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
							excel.setVersion(0);	//�汾��
							excel.setXml(xmlFileId);
							excel.setFileName(fileName);
							excel.setCertificateCode(certificate.getCertificateCode());
							
							//���� �������Ȩǩ�ּ�¼�����ڴӷ����д���������ݣ�
							VerifyAndAuthorize vNew = new VerifyAndAuthorize();
							vNew.setCode(UIDUtil.get22BitUID());
							vNew.setVersion(0);
							vNew.setCommissionSheet(cSheet);
							vNew.setCreateTime(new Timestamp(System.currentTimeMillis()));
							vNew.setSysUserByCreatorId(loginUser);
							//��ʹ�õļ����淶��������׼����׼����(���ڴӷ����д����������)
							Map<Integer, Specification> speMap = new HashMap<Integer, Specification>();	
							Map<Integer, Standard> stdMap = new HashMap<Integer, Standard>();
							Map<Integer, StandardAppliance> stdAppMap = new HashMap<Integer, StandardAppliance>();
							
							//Excel�ļ�����
							InputStream in = null;
							File fXlsOutFile = null, fXlsPdfFile= null;
							File fXlsOutFileToPdf = null;	//ȥ���˶�����Ϣ��Excel
							File fDocTemplate = null, fDocXml = null, fDocOut = null, fDocPdf = null;	//֤��ģ���ļ���֤��ģ�������ļ������ɵ�֤���ļ���֤��PDF�ļ�
							File fPicWorkStaff = null;	//��У��Ա��ǩ��ͼƬ
							Transaction tran = HibernateSessionFactory.beginTransaction();	//��ʼ������
							try{
								in = fis.openStream();
								fXlsOutFile = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								ExcelUtil.uploadExcelByBatch(in, fXlsOutFile, cSheet, stdName, xlsParser, oRecord, certificate, vNew, speMap, stdMap, stdAppMap);	//У�顢����Excel
								
								if(!fXlsOutFile.exists() || fXlsOutFile.length() == 0){
									throw new Exception("У�顢�����ϴ���Excel�ļ�ʧ�ܣ�");
								}
								fXlsOutFileToPdf = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
								ExcelUtil.removeAdditionalInfo(fXlsOutFile, fXlsOutFileToPdf, xlsParser);	//�Ƴ�Excel�ļ��еĸ�����Ϣ
								fXlsPdfFile = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//���ɵ�Excel pdf�ļ�
								if(fXlsOutFileToPdf.exists() && fXlsOutFileToPdf.length() > 0){		//ȥ��������Ϣ
									Office2PdfUtil.docToPdf(fXlsOutFileToPdf, fXlsPdfFile);
								}else{
									Office2PdfUtil.docToPdf(fXlsOutFile, fXlsPdfFile);
								}
								if(!fXlsPdfFile.exists() || fXlsPdfFile.length() == 0){
									throw new Exception("����ԭʼ��¼Excel��PDF�ļ�ʧ�ܣ�");
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
								
								//�ж��Ƿ���Ҫ����֤��
								if(certificate.getFileName() != null && certificate.getFileName().length() > 0){	//����֤��
									searchMap.clear();
									String docModFileName = certificate.getFileName();
									searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
									Pattern patternCertificate = MongoPattern.compile("^"+docModFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
									
									retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���%s", docModFileName));
									}
									String docModeFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ���ļ���ID
									
									String docXmlFileName = String.format("%s.xml", docModFileName.substring(0, docModFileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
									patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
									searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
									retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
									if(retArray.length() == 0){
										throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", docXmlFileName));
									}
									String docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ��xml�ļ���ID
									
									//���ļ����ݿ���ȡ��ģ���ļ�
									fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, docModeFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("����֤��ģ���ļ�ʧ�ܣ�");
									}
									//���ļ����ݿ���ȡ��֤��xml�����ļ�
									fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
									if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
										throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
									}
									ParseXMLAll docParser = new ParseXMLAll(fDocXml);
									fDocOut = File.createTempFile(UIDUtil.get22BitUID(), docModFileName.substring(docModFileName.lastIndexOf(".")));
									fDocPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
									
									/*******        ��ȡ�춨Աǩ��ͼƬ         ********/
									HashMap<String, Object> picParams = new HashMap<String, Object>();
									if(oRecord.getSysUserByStaffId().getSignature() != null){	//��ȡ�춨��Ա��ǩ��ͼƬ
										picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
										JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
										if(jsonInfo != null){
											String picFilename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
											fPicWorkStaff = File.createTempFile(UIDUtil.get22BitUID(), picFilename.substring(picFilename.lastIndexOf('.')>0?picFilename.lastIndexOf('.'):0));
											MongoDBUtil.gridFSDownloadById(fPicWorkStaff, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
										}
									}
									StringBuilder alertString = new StringBuilder();//������������ʱ����ʾ��Ϣ
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
										throw new Exception("֤���ļ�����ʧ�ܣ�");
									}
									if(!fDocPdf.exists() || fDocPdf.length() == 0){
										throw new Exception("֤��Word�ļ�����PDFʧ�ܣ�");
									}
									certificate.setXml(docXmlFileId);
								}else{
									certificate.setFileName("");
								}
								//�ϴ����ݿ�(Excel�ļ���Excel��PDF�ļ���Word�ļ���Word��PDF�ļ�)
								paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, excel.getCode());
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, fileName);
								if(!MongoDBUtil.gridFSUpload(fXlsOutFile, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("����ԭʼ��¼Excel�ļ���������ʧ�ܣ�");
								}
								String xlsIdStr = paramsMap.get("_id").toString();	//Excel�ļ���ID
								if(paramsMap.containsKey("_id")){
									paramsMap.remove("_id");
								}
								paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", fileName.substring(0, fileName.lastIndexOf('.'))));
								if(!MongoDBUtil.gridFSUpload(fXlsPdfFile, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("����ԭʼ��¼Excel��PDF�ļ���������ʧ�ܣ�");
								}
								String xlsPdfIdStr = paramsMap.get("_id").toString();	//Excel PDF�ļ���ID
								excel.setDoc(xlsIdStr);
								excel.setPdf(xlsPdfIdStr);
								if(fDocOut != null){	//����֤���ļ���֤��PDF�ļ�
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, certificate.getCode());
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, certificate.getFileName());
									if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("����֤���ļ���������ʧ�ܣ�");
									}
									String docIdStr = paramsMap.get("_id").toString();	//֤���ļ���ID
									if(paramsMap.containsKey("_id")){
										paramsMap.remove("_id");
									}
									paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", certificate.getFileName().substring(0, certificate.getFileName().lastIndexOf("."))));
									if(!MongoDBUtil.gridFSUpload(fDocPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
										throw new Exception("����֤���ļ���������ʧ�ܣ�");
									}
									String docPdfIdStr = paramsMap.get("_id").toString();	//֤��PDF�ļ���ID
									certificate.setDoc(docIdStr);
									certificate.setPdf(docPdfIdStr);
								}
								//�������ݿ�
								OriginalRecordDAO oRecordDao = new OriginalRecordDAO();
								OriginalRecordTechnicalDocsDAO oSpeDao = new OriginalRecordTechnicalDocsDAO();
								OriginalRecordStandardsDAO oStdDao = new OriginalRecordStandardsDAO();
								OriginalRecordStdAppliancesDAO oStdAppDao = new OriginalRecordStdAppliancesDAO();
								oRecordDao.save(oRecord);	//����ԭʼ��¼
								Timestamp nowTime = new Timestamp(System.currentTimeMillis());
								for(Specification s : speList){	//�漼���淶
									OriginalRecordTechnicalDocs oSpe = new OriginalRecordTechnicalDocs();
									oSpe.setOriginalRecord(oRecord);
									oSpe.setSpecification(s);
									oSpe.setLastEditTime(nowTime);
									oSpe.setSysUser(loginUser);
									oSpeDao.save(oSpe);
								}
								for(Standard std : stdList){ //�������׼
									OriginalRecordStandards oStd = new OriginalRecordStandards();
									oStd.setOriginalRecord(oRecord);
									oStd.setStandard(std);
									oStd.setLastEditTime(nowTime);
									oStd.setSysUser(loginUser);
									oStdDao.save(oStd);
								}
								for(StandardAppliance stdApp : stdAppList){	//���׼����
									OriginalRecordStdAppliances oStd = new OriginalRecordStdAppliances();
									oStd.setOriginalRecord(oRecord);
									oStd.setStandardAppliance(stdApp);
									oStd.setLastEditTime(nowTime);
									oStd.setSysUser(loginUser);
									oStdAppDao.save(oStd);
								}
								CertificateDAO certificateDao = new CertificateDAO();
								certificate.setOriginalRecord(oRecord);
								if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//�ϴ���ԱΪ�춨Ա����
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
								
								//���³�������
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
								//������������
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
				retJSON9.put("msg", "�ϴ�ԭʼ��¼�ɹ���" + alert);
			}catch(Exception e){
				e.printStackTrace();
				try {
					retJSON9.put("IsOK", false);
					retJSON9.put("msg", String.format("�ϴ�ԭʼ��¼Excelʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileUploadServlet-->case 9", e);
				}else{
					log.error("error in FileUploadServlet-->case 9", e);
				}
			}finally{
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().write(retJSON9.toString());
			}
			break;
		case 11:	//���ԣ�TaskTime.jsp���ϴ�ԭʼ��¼
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("1");
			break;
			
		case 12:	//�ϴ����ύ���޸ĺ��֤�飨Weboffice��������ԭʼ��¼Ҳ���޸�֤��
			JSONObject retJSON12 = new JSONObject();
			try{
				// ��ʼ���ϴ����  
				SmartUpload mySmartUpload = new SmartUpload();
		        mySmartUpload.initialize(this.getServletConfig(),request,response);  
		        mySmartUpload.upload();
				// ��ȡ�ϴ�����¼		
		        String FileName = mySmartUpload.getRequest().getParameter("FileName");
				String OriginalRecordId = mySmartUpload.getRequest().getParameter("OriginalRecordId");  //ԭʼ��¼��ID
				String VersionStr = mySmartUpload.getRequest().getParameter("Version");//֤��İ汾��
		        
				if(OriginalRecordId == null || OriginalRecordId.length() == 0
						|| FileName == null || FileName.length() == 0){
					throw new Exception("������������");
				}
				if(VersionStr == null || VersionStr.length() == 0){
					VersionStr = "-1";
				}
				OriginalRecordManager oRecordMgr = new OriginalRecordManager();
				CertificateManager certificateMgr = new CertificateManager();
				OriginalRecord oRecord = oRecordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��֤���¼��");
				}
				
//				if(oRecord.getOriginalRecordExcel() != null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
//					throw new Exception("�ü�¼���ϴ�ԭʼ��¼Excel�ļ�������ֱ�ӱ���֤�飡");
//				}
				
				if(oRecord.getCertificate().getVersion() > Integer.parseInt(VersionStr)){
					throw new Exception("֤��汾��ͻ���������������°汾��֤�鲢�����ϱ༭��");
				}
				SysUser loginUser = (SysUser)request.getSession().getAttribute(SystemCfgUtil.SessionAttrNameLoginUser);
				if(loginUser == null){
					throw new Exception("����δ��¼�������ύ֤�飡");
				}
				if(!oRecord.getTaskAssign().getSysUserByAlloteeId().getId().equals(loginUser.getId())
						&& oRecord.getCertificate() != null
						&& oRecord.getCertificate().getPdf() != null){
					throw new Exception("��ԭʼ��¼������֤�飡�����Ǹ�����������ļ����ˣ������ٴ��ύ֤�飡");
				}
				
				//�ж�ί�е���״̬
				if(oRecord.getCommissionSheet().getStatus() == 10){	//��ע��
					throw new Exception("��ί�е���ע���������ٴ��ύԭʼ��¼��");
				}
				if(oRecord.getCommissionSheet().getStatus() == 3 ||			//���깤���깤ȷ�Ϻ�
						oRecord.getCommissionSheet().getStatus() == 4 ||	//�ѽ���
						oRecord.getCommissionSheet().getStatus() == 9){		//�ѽ���
					RemakeCertificateManager remakeMgr = new RemakeCertificateManager();
					List<RemakeCertificate> remakeList = remakeMgr.findByVarProperty(new KeyValueWithOperator("originalRecord.id", oRecord.getId(), "="),
							new KeyValueWithOperator("finishTime", null, "is null")
					);
					if(remakeList.size() == 0){
						throw new Exception("��ί�е����깤������û�����±���֤������룬�����ٴ��ύԭʼ��¼��");
					}
				}
				
				String toBeDeleteFileIdCertificate = null, toBeDeleteFileIdExcel = null;	//��ɾ����֤��Doc�ļ���Excel�ļ�������ʽ��ģ�
				Certificate certificate = oRecord.getCertificate();	//����ж�certificate��Id�Ƿ�Ϊnull�����Ϊnull����˵����Ҫ����֤��
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(certificate == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
				}else if(FlagUtil.CertificateFlag.isCertificateOfficial(oRecord.getCertificate())){	//��ԭʼ��¼��֤��Ϊ��ʽ�汾��֤����Ҫ�������ɣ��汾��1
					//����Certificate
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
				}else if(certificate.getDoc().length() > 0){	//������ʽ�汾�Ҵ���֤��Doc�ļ�����������ʽ�汾��֤��󣬽��ϰ汾��֤��Doc�ļ�ɾ��
					toBeDeleteFileIdCertificate = certificate.getDoc();
				}
				
				//�������Ȩǩ�ּ�¼�����vNew��IDΪnull��������һ����¼�����򣬸���ԭ��¼
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
//					if(vNew.getSysUserByAuthorizerId() != null && vNew.getSysUserByVerifierId() != null){	//��������һ�ݺ������Ȩǩ�ּ�¼���汾��+1
//						vNew.setId(null);
//						vNew.setVersion(vNew.getVersion() + 1);
//					}
				}
				
				//�����ϴ����ļ�
				com.jspsmart.upload.File myFile = null;
				myFile = mySmartUpload.getFiles().getFile(0);
				if(!myFile.isMissing()){
					//�洢���ɵ�ԭʼ��¼���ļ����ݿ��еĲ���
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
								oRecord.getCertificate().getXml().length() > 0){	//ʹ��ԭXML
							docXmlFileId = oRecord.getCertificate().getXml();
						}else{	//ʹ�����е�ģ�嶨���ļ�
							//��ȡ֤��XML�����ļ�
							HashMap<String, Object> searchMap = new HashMap<String, Object>();
							searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
							searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
							
							String docXmlFileName = String.format("%s.xml", FileName.substring(0, FileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
							Pattern patternCertificate = MongoPattern.compile("^"+docXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
							searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
							JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
							if(retArray.length() == 0){
								throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", docXmlFileName));
							}
							docXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ��xml�ļ���ID
						}
						if(docXmlFileId == null){
							throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml", FileName.substring(0, FileName.lastIndexOf("."))));
						}
						//���ļ����ݿ���ȡ��֤��xml�����ļ�
						fDocXml = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
						if(!MongoDBUtil.gridFSDownloadById(fDocXml, docXmlFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
						}
						ParseXMLAll docParser = new ParseXMLAll(fDocXml);
						
						
						fDocOut = File.createTempFile(UIDUtil.get22BitUID(), FileName.substring(FileName.lastIndexOf(".")));
						fPdfOut = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
						WordUtil.HandleUploadedCertificateWord(tempInputFile, fDocOut, fPdfOut, docParser, oRecord, certificate);
						
						if(!fDocOut.exists() || fDocOut.length() == 0){
							throw new Exception("֤���ļ�����֤����ʧ�ܣ�");
						}
						if(!fPdfOut.exists() || fPdfOut.length() == 0){
							throw new Exception("֤��Word�ļ�����PDFʧ�ܣ�");
						}
						certificate.setXml(docXmlFileId);
						certificate.setFileName(FileName);
						
						//����֤���ļ�
						if(!MongoDBUtil.gridFSUpload(fDocOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("�����ļ���������ʧ�ܣ�");
						}
						String docIdStr = paramsMap.get("_id").toString();	//Doc�ļ���ID
						
						//���ɲ�����pdf�ļ�
						if(paramsMap.containsKey("_id")){
							paramsMap.remove("_id");
						}
						paramsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", FileName.substring(0, FileName.lastIndexOf("."))));
						if(!MongoDBUtil.gridFSUpload(fPdfOut, paramsMap, MongoDBUtil.CollectionType.Certificate)){
							throw new Exception("����PDF�ļ���������ʧ�ܣ�");
						}
						String pdfIdStr = paramsMap.get("_id").toString();	//PDF�ļ���ID;	
						
						certificate.setDoc(docIdStr);
						certificate.setPdf(pdfIdStr);
						
						
						
						/***********�������ݿ����ݿ� ��oRecord��oRecordExcel��Certificate��Vertificate**********/
						//�ж��Ƿ���Ҫ����OriginalRecordExcel
						OriginalRecordExcel excel = null;
						Integer VersionInt = 0;
						boolean isNewExcel = false;		//��־λ�����ڸ������ݿ�ʱ�ж�
						String excelCode = null;
						if(oRecord.getOriginalRecordExcel() == null){	//��δ�й�ԭʼ��¼
							excelCode = UIDUtil.get22BitUID();	//�ļ�������
							VersionInt = 0;	//��һ��汾�Ŵ�0��ʼ
							isNewExcel = true;
							excel = new OriginalRecordExcel();
							excel.setDoc("");
							excel.setPdf(null);
							excel.setXml("");
							excel.setFileName("");
						}else if(oRecord.getOriginalRecordExcel().getPdf() != null){	//�Ѿ�����ʽ��ģ���Ҫ�������ɰ汾
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion() + 1;
							isNewExcel = true;
							excel = new OriginalRecordExcel();
							excel.setDoc(oRecord.getOriginalRecordExcel().getDoc());
							excel.setPdf(null);
							excel.setXml(oRecord.getOriginalRecordExcel().getXml());
							excel.setFileName(oRecord.getOriginalRecordExcel().getFileName());
						}else{	//�滻֮ǰ�ķ���ʽ��
							excelCode = oRecord.getOriginalRecordExcel().getCode();
							VersionInt = oRecord.getOriginalRecordExcel().getVersion();
							toBeDeleteFileIdExcel = oRecord.getOriginalRecordExcel().getDoc();
							excel = new OriginalRecordExcelManager().findById(oRecord.getOriginalRecordExcel().getId());
						}
						
						//����OriginalRecordExcel
						excel.setCode(excelCode);
						excel.setCommissionSheet(oRecord.getCommissionSheet());
						excel.setOriginalRecord(oRecord);
						excel.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						excel.setVersion(VersionInt);	//�汾��
						excel.setCertificateCode(certificate.getCertificateCode());
						
						
						if(excel.getDoc() != null && excel.getDoc().length() > 0 && excel.getFileName() != null && excel.getFileName().length() > 0){
							//��ȡԭʼ��¼Excel�ļ����������ļ�
							String xlsFileName = excel.getFileName();	//ԭʼ��¼Excel�ļ����ļ���
							fXlsRecord = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
							if(!MongoDBUtil.gridFSDownloadById(fXlsRecord, excel.getDoc(), MongoDBUtil.CollectionType.OriginalRecord)){
								throw new Exception("��ȡԭʼ��¼Excel�ļ�ʧ�ܣ�");
							}
							//��ѯԭʼ��¼�����ļ������Ʋ���ȡ�ļ��������ݿ��ж�ȡxml�ļ���ID ���ȡ���µ�XML�ļ���
							String xlsXmlFileId = null;
							if(excel.getXml().length() > 0){	//ʹ��ԭXML�������ݿ��ж�ȡxml�ļ���ID��
								xlsXmlFileId = excel.getXml();
							}else{	//ʹ�����е�ģ�嶨���ļ�
								//�����ֶζ���xml�ļ�
								HashMap<String, Object> map = new HashMap<String, Object>();
								String xlsXmlFileName = String.format("%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf(".")));	//ԭʼ��¼�����ļ�������
								map.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼�������ļ����ļ�������
								Pattern pattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
								map.put(MongoDBUtil.KEYNAME_FileName, pattern);
								map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
								JSONArray retArray2 = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
								if(retArray2.length() == 0){
									throw new Exception(String.format("δ�ҵ�ԭʼ��¼�ļ���Ӧ�������ļ�:%s", xlsXmlFileName));
								}
								xlsXmlFileId = ((JSONObject)retArray2.get(0)).getString("_id");//ԭʼ��¼xml�ļ���ID
								
							}
							if(xlsXmlFileId == null){
								throw new Exception(String.format("δ�ҵ��ֶζ����ļ���%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf("."))));
							}
							
							fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
							if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
								throw new Exception(String.format("��ȡԭʼ��¼�����ļ�ʧ�ܣ�%s.xml", excel.getFileName().substring(0, excel.getFileName().lastIndexOf("."))));
							}
							ParseXMLAll xlsParser = new ParseXMLAll(fXlsXML);
							
							boolean hasTag = false;	//�鿴ԭʼ��¼�Ƿ���֤���Ŷ�Ӧ�ֶ�
							String key = "CertificateCode";	//֤����������
							for(int k = 0; k < xlsParser.getQNameCount(key); k++){
								if(xlsParser.getAttribute(key, "fieldClass", k) != null && xlsParser.getAttribute(key, "fieldClass", k).equalsIgnoreCase("com.jlyw.hibernate.Certificate")){
									hasTag = true;
									break;
								}
							}
							if(hasTag){	//����Ӧ��ǩ������Ҫ�޸�ԭʼ��¼
								fXlsUpdate = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.lastIndexOf('.')==-1?".xls":xlsFileName.substring(xlsFileName.lastIndexOf('.')));
								ExcelUtil.updateExcelWithCertificateCode(fXlsRecord, fXlsUpdate, certificate, xlsParser);
								if(!fXlsUpdate.exists() || fXlsUpdate.length() == 0){
									throw new Exception("����ԭʼ��¼Excel�ļ��е�֤����ʧ�ܣ�");
								}
								
								//�洢���º��Excel���ļ����ݿ��еĲ���
								HashMap<String, Object> xlsParamsMap = new HashMap<String, Object>();
								xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, xlsFileName);
								xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderId, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getId().toString());
								xlsParamsMap.put(MongoDBUtil.KEYNAME_UploaderName, request.getSession().getAttribute("LOGIN_USER")==null?"":((SysUser)request.getSession().getAttribute("LOGIN_USER")).getName());
								xlsParamsMap.put(MongoDBUtil.KEYNAME_FileSetName, excel.getCode());
								if(!MongoDBUtil.gridFSUpload(fXlsUpdate, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("������º��ԭʼ��¼Excel���ļ�������ʧ�ܣ�");
								}
								String xlsUpdateIdStr = xlsParamsMap.get("_id").toString();	//Excel�ļ���ID
								
								//��Excel��Ч��Ϣȥ��������PDF�汾
								fXlsUpdateWithPic = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.substring(xlsFileName.lastIndexOf(".")));
								ExcelUtil.removeAdditionalInfo(fXlsUpdate, fXlsUpdateWithPic, xlsParser);	//�Ƴ�Excel�ļ��ĸ�����Ϣ
								fXlsUpdatePdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");//����pdf�ļ�
								if(fXlsUpdateWithPic.exists() && fXlsUpdateWithPic.length() > 0){	//������ǩ��ͼƬ
									Office2PdfUtil.docToPdf(fXlsUpdateWithPic, fXlsUpdatePdf);
								}else{
									Office2PdfUtil.docToPdf(fXlsUpdate, fXlsUpdatePdf);
								}
								//����pdf�ļ�
								if(xlsParamsMap.containsKey("_id")){
									xlsParamsMap.remove("_id");
								}
								xlsParamsMap.put(MongoDBUtil.KEYNAME_FileName, String.format("%s.pdf", xlsFileName.substring(0, xlsFileName.lastIndexOf(".")==-1?0:xlsFileName.lastIndexOf('.'))));
								if(!MongoDBUtil.gridFSUpload(fXlsUpdatePdf, xlsParamsMap, MongoDBUtil.CollectionType.OriginalRecord)){
									throw new Exception("������º��ԭʼ��¼PDF���ļ�������ʧ�ܣ�");
								}
								String xlsUpdatePdfIdStr = xlsParamsMap.get("_id").toString();	//PDF�ļ���ID;	
								
								//�������ݿ�
								excel.setDoc(xlsUpdateIdStr);
								excel.setPdf(xlsUpdatePdfIdStr);
							}
						}					
						
						//����Certificate
						if(certificate.getVersion() < 0){
							certificate.setVersion(0);	//�汾��Ϊ0��ʼ��-1ΪԤ��֤����
						}
						certificate.setLastEditTime(new Timestamp(System.currentTimeMillis()));
						if(oRecord.getSysUserByStaffId().getId().equals(loginUser.getId())){	//�ϴ���ԱΪ�춨Ա����
							certificate.setSysUser(null);
						}else{
							certificate.setSysUser(loginUser);
						}						
						
						//������Ȩ��ǩ�ּ�¼
						vNew.setCertificate(certificate);
						vNew.setOriginalRecordExcel(excel);
						if(vNew.getSysUserByVerifierId() == null || vNew.getSysUserByAuthorizerId() == null){
							vNew.setSysUserByVerifierId(null);
							vNew.setSysUserByAuthorizerId(null);
						}
						oRecordMgr.uploadCertificateUpdateDB(oRecord, excel, certificate, vNew);						
						
						if(!certificateMgr.update(certificate)){
							throw new Exception("�������ݿ�ʧ�ܣ�����֤����Ϣ��");
						}
						
						//����ɾ���ļ����ݿ��й�ʱ��ԭʼ��¼��֤��
						try{
							if(toBeDeleteFileIdExcel != null && toBeDeleteFileIdExcel.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdExcel, MongoDBUtil.CollectionType.OriginalRecord);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(ԭʼ��¼Excel id:"+toBeDeleteFileIdExcel+")", eee);
						}
						try{
							if(toBeDeleteFileIdCertificate != null && toBeDeleteFileIdCertificate.length() > 0){
								MongoDBUtil.gridFSDeleteById(toBeDeleteFileIdCertificate, MongoDBUtil.CollectionType.Certificate);
							}
						}catch(Exception eee){
							log.debug("exception in FileUploadServlet-->case 6-->Delete file from mongoDB(֤��Doc id:"+toBeDeleteFileIdCertificate+")", eee);
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
					throw new Exception("�ļ��ϴ�ʧ�ܣ�");
				}
				retJSON12.put("IsOK", true);
				//retJSON12.put("msg", "�޸�֤��ɹ���" + alertString==null?"":alertString.toString());
			}catch(Exception e){
				
				try {
					retJSON12.put("IsOK", false);
					retJSON12.put("msg", String.format("�ϴ�֤��ʧ�ܣ�������Ϣ��%s", (e!=null && e.getMessage()!=null)?e.getMessage():"��"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
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
