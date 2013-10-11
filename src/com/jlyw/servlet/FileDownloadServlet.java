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
		Integer method = Integer.parseInt(req.getParameter("method"));	//�ж�����ķ�������
		switch (method) {
		case 0: //�����ļ�ID�����ļ�
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
					if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)  //firefox�����
					    filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
					else 
//					     if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)	//IE�����
					     filename = URLEncoder.encode(filename, "UTF-8");
					
					//�����������Ϣͷ��  ����-����,����;�ļ���=(��������ļ���) URLEncoader.encode() ���ı���
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
					resp.getWriter().print("ָ���ļ������ڣ�");
					return;
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 0", e);
				}else{
					log.error("error in FileDownloadServlet-->case 0", e);
				}
			}
			break;
		case 1:	//����FilesetName������Ӧ���ļ���Ϣ�б�(��ҳ����)
			JSONObject retJSON1 = null;
			try{
				int page = 0;	//��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				
				String FilesetName = req.getParameter("FilesetName");
				String FileType = req.getParameter("FileType");
				if(FilesetName == null || FileType == null){
					throw new Exception("������������");
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
					paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�����ļ���Ч���ֶ�������
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
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 1", e);
				}else{
					log.error("error in FileDownloadServlet-->case 1", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().println(retJSON1.toString());
				
			}
			break;
		case 2:	//����ԭʼ��¼�ļ�������WebOffice��
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				OriginalRecord oRecord = new OriginalRecordManager().findById(Integer.parseInt(OriginalRecordId));
				if(oRecord.getOriginalRecordExcel() == null){
					throw new Exception("��ԭʼ��¼��δ����Excel�ļ���");
				}
				java.io.OutputStream outStream = resp.getOutputStream();
				MongoDBUtil.gridFSDownloadById(outStream, oRecord.getOriginalRecordExcel().getDoc(), MongoDBUtil.CollectionType.OriginalRecord);
				outStream.close();
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 2", e);
				}else{
					log.error("error in FileDownloadServlet-->case 2", e);
				}
			}
			break;
		case 3:	//����ԭʼ��¼ģ��
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String TemplateFileId = req.getParameter("TemplateFileId");	//Excelģ���ļ���ID
				String DownloadType = req.getParameter("DownloadType");	//�������ͣ�0����ʾ�����ļ���1����ʾ������WebOffice��
				String VerifierName = req.getParameter("VerifierName");	//������Ա����
				if(VerifierName != null){
					VerifierName = new String(VerifierName.getBytes("ISO-8859-1"), "UTF-8");
				}
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
				}
				OriginalRecordManager recordMgr = new OriginalRecordManager();
				OriginalRecord oRecord = recordMgr.findById(Integer.parseInt(OriginalRecordId));
				if(oRecord == null){
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() != null){	//�Ѵ���ԭʼ��¼Excel�ļ�����ֱ��ת��
					if(DownloadType.equals("1")){
						req.getRequestDispatcher("/FileDownloadServlet.do?method=2").forward(req, resp);
					}else{
						req.getRequestDispatcher(String.format("/FileDownloadServlet.do?method=0&FileType=%d&FileId=%s",
									UploadDownLoadUtil.Type_OriginalRecord,
									oRecord.getOriginalRecordExcel().getDoc())).forward(req, resp);
					}
					return;
				} 
				
				//������Ա
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
					throw new Exception("������������");
				}
				
				JSONObject retObj = MongoDBUtil.getFileInfoById(TemplateFileId, MongoDBUtil.CollectionType.Template);
				if(retObj == null){
					throw new Exception("�Ҳ���ָ����Excelģ���ļ���");
				}
				String fileId = retObj.getString("_id");	//ģ���ļ���ID
				String fileName = retObj.getString(MongoDBUtil.KEYNAME_FileName);	//ģ���ļ����ļ���
				String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, retObj.getString(MongoDBUtil.KEYNAME_FileSetName));
				Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("δ�ҵ�ģ���ļ���Ӧ���ֶζ����ļ�:%s", xmlFileName));
				}
				String xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
				
				
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(oRecord.getCertificate() == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
						throw new Exception("����֤����ʧ�ܣ����Ժ����ԣ�");
					}
					oRecord.setCertificate(c);
					if(!recordMgr.update(oRecord)){
						throw new Exception("����֤����ʧ�ܣ����Ժ����ԣ�");
					}
				}
				
			
				OutputStream osTemplate = null, osRecord = null;	//ģ���ļ���xml�����ļ���ԭʼ��¼�ļ����ļ���
				InputStream isTemplate = null, isRecord = null;
				File fTemplate = null, fXML = null, fRecord = null;
				try{
					//���ļ����ݿ���ȡ��ģ���ļ�
					fTemplate = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
					osTemplate = new FileOutputStream(fTemplate);
					if(!MongoDBUtil.gridFSDownloadById(osTemplate, fileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("����ģ���ļ�ʧ�ܣ�");
					}
					osTemplate.close();
					osTemplate = null;
					isTemplate = new FileInputStream(fTemplate);
					
					//���ļ����ݿ���ȡ��xml�����ļ�
					fXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("����ģ�嶨���ļ�ʧ�ܣ�");
					}
					ParseXMLAll parser = new ParseXMLAll(fXML);
					
					
					//��ѯ�����淶��������׼����׼����
					String queryStringSpe = "from Specification as model where model.id in (select a.specification.id from OriginalRecordTechnicalDocs as a where a.originalRecord.id=?)";
					List<Specification> speList = recordMgr.findByHQL(queryStringSpe, oRecord.getId());
					String queryStringStd = "from Standard as model where model.id in (select a.standard.id from OriginalRecordStandards as a where a.originalRecord.id=?)";
					List<Standard> stdList = recordMgr.findByHQL(queryStringStd, oRecord.getId());
					String queryStringStdApp = "from StandardAppliance as model where model.id in (select a.standardAppliance.id from OriginalRecordStdAppliances as a where a.originalRecord.id=?)";
					List<StandardAppliance> stdAppList = recordMgr.findByHQL(queryStringStdApp, oRecord.getId());
					//����ԭʼ��¼
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
					if(DownloadType.equals("1")){//װ��ԭʼ��¼�ļ�(WebOffice)
						//�޲���
					}else{	//�����ļ�
						String downloadFileName = fileName;
						if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)  //firefox�����
						    downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
						else 
//						     if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)	//IE�����
						     downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
						
						//�����������Ϣͷ��  ����-����,����;�ļ���=(��������ļ���) URLEncoader.encode() ���ı���
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
				}finally{	//�ر��ļ���;ɾ����ʱ�ļ�
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
				resp.getWriter().print(String.format("���棺����ģ���ļ�ʧ�ܣ�ʧ����Ϣ:%s", e.getMessage() == null?"��":e.getMessage()));
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 3", e);
				}else{
					log.error("error in FileDownloadServlet-->case 3", e);
				}
			}
			break;
		case 4:	//����FilesetName������Ӧ���ļ���Ϣ�б�����ҳ��
			JSONObject retJSON4 = new JSONObject();
			try{
				JSONArray jsonArray = null;
				String FilesetName = req.getParameter("FilesetName");
				String FileType = req.getParameter("FileType");
				if(FilesetName == null || FileType == null){
					throw new Exception("������������");
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
					paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�����ļ���Ч���ֶ�������
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
				
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 4", e);
				}else{
					log.error("error in FileDownloadServlet-->case 4", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON4.toString());
			}
			break;
		case 5:	//����FilesetName������Ӧ��ԭʼ��¼ģ��Excel�ļ��ļ���Ϣ�б�
			JSONObject retJSON5 = new JSONObject();
			try{
				JSONArray jsonArray = null;
				String FilesetName = req.getParameter("FilesetName");
				if(FilesetName == null){
					throw new Exception("������������");
				}
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				Pattern pattern = MongoPattern.compile(".+\\.xlsx?$", Pattern.CASE_INSENSITIVE);	//������ʽ:��.xls����.xlsx��β��
				paramsMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
				paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�����ļ���Ч���ֶ�������
				jsonArray = MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Template);
				
				retJSON5.put("total", jsonArray == null?0:jsonArray.length());
				retJSON5.put("rows", jsonArray == null?new JSONArray():jsonArray);
			}catch(Exception e){
				try {
					retJSON5.put("total", 0);
					retJSON5.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 5", e);
				}else{
					log.error("error in FileDownloadServlet-->case 5", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON5.toString());
			}
			break;
		case 6: //����֤��ģ��Doc�ļ���Ϣ�б�(����ѡ��֤��ģ�� )
			JSONObject retJSON6 = null;
			try{
				int page = 0;	//��ǰҳ��
				if (req.getParameter("page") != null)
					page = Integer.parseInt(req.getParameter("page").toString());
				int rows = 10;	//ҳ���С
				if (req.getParameter("rows") != null)
					rows = Integer.parseInt(req.getParameter("rows").toString());
				
				
				String FilesetName = SystemCfgUtil.CertificateTemplateFilesetName;	//֤��ģ���ļ����ļ�������
				HashMap<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, FilesetName);
				Pattern pattern = MongoPattern.compile(".+\\.docx?$", Pattern.CASE_INSENSITIVE);	//������ʽ:��.doc����.docx��β��
				paramsMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
				paramsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�����ļ���Ч���ֶ�������
				
				retJSON6 = MongoDBUtil.getPageAllFileList(page, rows, paramsMap, MongoDBUtil.CollectionType.Template);
				
				
			}catch(Exception e){
				try {
					retJSON6 = new JSONObject();
					retJSON6.put("total", 0);
					retJSON6.put("rows", new JSONArray());
				} catch (JSONException e1) {}
				
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 6", e);
				}else{
					log.error("error in FileDownloadServlet-->case 6", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON6.toString());
			}
			break;
			
		case 7:	//����֤���ļ�������WebOffice��
			try {
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				OriginalRecord oRecord = new OriginalRecordManager().findById(Integer.parseInt(OriginalRecordId));
				if(oRecord.getCertificate() == null || oRecord.getCertificate().getDoc().length() == 0){
					throw new Exception("��ԭʼ��¼��δ����֤���ļ���");
				}
				java.io.OutputStream outStream = resp.getOutputStream();
				MongoDBUtil.gridFSDownloadById(outStream, oRecord.getCertificate().getDoc(), MongoDBUtil.CollectionType.Certificate);
				outStream.close();
			} catch (Exception e) {
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 7", e);
				}else{
					log.error("error in FileDownloadServlet-->case 7", e);
				}
			}
			break;
		case 8:	//����֤��ģ�壨Ĭ����weboffice�У�
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String DownloadType = req.getParameter("DownloadType");	//�������ͣ�0����ʾ�����ļ���1����ʾ������WebOffice��
				String VersionStr = req.getParameter("Version");//֤��İ汾��
				String XlsTemplateFileId = req.getParameter("XlsTemplateFileId");	//ԭʼ��¼Excelģ���ļ�ID�����ڿ���֤������ҳ��
				
				
				StringBuilder alertString=new StringBuilder();//������������ʱ����ʾ��Ϣ
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
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
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
				if(oRecord.getOriginalRecordExcel() !=  null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
					throw new Exception("�ü�¼���ϴ�ԭʼ��¼Excel�ļ�������ֱ�ӱ���֤�飡");
				}
				
				if(oRecord.getCertificate() != null && oRecord.getCertificate().getDoc().length() > 0){	//�Ѵ���֤��Doc�ļ�����ֱ��ת��
					if(DownloadType.equals("1")){
						req.getRequestDispatcher("/FileDownloadServlet.do?method=7").forward(req, resp);
					}else{
						req.getRequestDispatcher(String.format("/FileDownloadServlet.do?method=0&FileType=%d&FileId=%s",
									UploadDownLoadUtil.Type_Certificate,
									oRecord.getCertificate().getDoc())).forward(req, resp);
					}
					return;
				}
				
				//��ȡ֤��ģ���ļ���
				String docTemplateFileName = ExcelUtil.getCertificateModFileName(oRecord.getWorkType(), oRecord.getConclusion());
				if(docTemplateFileName == null || docTemplateFileName.length() == 0){
					throw new Exception("��ȡ֤��ģ���ļ�����������ü�¼�ġ��������ʡ��������ۡ��Ƿ���ȷ��");
				}
				
				HashMap<String, Object> searchMap = new HashMap<String, Object>();
				searchMap.clear();
				searchMap.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);
				Pattern patternCertificate = MongoPattern.compile("^"+docTemplateFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
				searchMap.put(MongoDBUtil.KEYNAME_FileName, patternCertificate);
				searchMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
				
				JSONArray retArray = MongoDBUtil.getFileList(searchMap, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���%s", docTemplateFileName));
				}
				String fileId = ((JSONObject)retArray.get(0)).getString("_id");//֤��ģ���ļ���ID
				String fileName = ((JSONObject)retArray.get(0)).getString(MongoDBUtil.KEYNAME_FileName);	//֤��ģ���ļ����ļ���
				String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, ((JSONObject)retArray.get(0)).getString(MongoDBUtil.KEYNAME_FileSetName));
				Pattern pattern = MongoPattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");	//�ļ�״̬����Ч
				JSONArray retArray2 = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray2.length() == 0){
					throw new Exception(String.format("δ�ҵ�֤��ģ���ļ���Ӧ�������ļ�:%s", xmlFileName));
				}
				String xmlFileId = ((JSONObject)retArray2.get(0)).getString("_id");//xml�ļ���ID
				
				//����û��֤���¼����������һ�����ݿ��֤���¼���汾��Ϊ-1��֤��Doc�ļ��ȷǿ��ֶ�Ϊ���ַ���""��������Ԥ��֤����
				if(oRecord.getCertificate() == null){
					//��ѯί�е��µĵڼ��ݼ�¼
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
						throw new Exception("����֤����ʧ�ܣ����Ժ����ԣ�");
					}
					oRecord.setCertificate(c);
					if(!recordMgr.update(oRecord)){
						throw new Exception("����֤����ʧ�ܣ����Ժ����ԣ�");
					}
				}
				
				
				
				
				File fDocTemplate = null, fDocXML = null, fDocOut = null;//֤��ģ���ļ���֤��xml�����ļ�, ֤���ļ�
				File fXlsTemplate = null, fXlsXML = null;	//ԭʼ��¼Excelģ���ļ���Excel��Ӧ ��XML�����ļ�
				File fPicWorkStaff = null;	//����Ա��ǩ��ͼƬ�ļ�
				InputStream isDocTemplate = null;
				try{
					//���ļ����ݿ���ȡ��ģ���ļ�
					fDocTemplate = File.createTempFile(UIDUtil.get22BitUID(), fileName.substring(fileName.lastIndexOf(".")));
					if(!MongoDBUtil.gridFSDownloadById(fDocTemplate, fileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("����֤��ģ���ļ�ʧ�ܣ�");
					}
					
					//���ļ����ݿ���ȡ��֤��xml�����ļ�
					fDocXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
					if(!MongoDBUtil.gridFSDownloadById(fDocXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
						throw new Exception("����֤��ģ�������ļ�ʧ�ܣ�");
					}
					ParseXMLAll docParser = new ParseXMLAll(fDocXML);
					
					ParseXMLAll xlsParser = null;
					if(XlsTemplateFileId != null && XlsTemplateFileId.trim().length() > 0){
						JSONObject retObj = MongoDBUtil.getFileInfoById(XlsTemplateFileId, MongoDBUtil.CollectionType.Template);
						if(retObj != null){
							//throw new Exception("�Ҳ���ָ����Excelģ���ļ���");
						
						
						String xlsFileId = retObj.getString("_id");	//ģ���ļ���ID
						String xlsFileName = retObj.getString(MongoDBUtil.KEYNAME_FileName);	//ģ���ļ����ļ���
						String xlsXmlFileName = String.format("%s.xml", xlsFileName.substring(0, xlsFileName.lastIndexOf(".")));	//ģ���ֶζ���xml�ļ�������
						HashMap<String, Object> xlsMap = new HashMap<String, Object>();
						xlsMap.put(MongoDBUtil.KEYNAME_FileSetName, retObj.getString(MongoDBUtil.KEYNAME_FileSetName));
						Pattern xlsPattern = MongoPattern.compile("^"+xlsXmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
						xlsMap.put(MongoDBUtil.KEYNAME_FileName, xlsPattern);
						xlsMap.put(MongoDBUtil.KEYNAME_FileStatus, "true");
						JSONArray xlsRetArray = MongoDBUtil.getFileList(xlsMap, MongoDBUtil.CollectionType.Template);
						if(xlsRetArray.length() == 0){
							throw new Exception(String.format("δ�ҵ�ģ���ļ���Ӧ���ֶζ����ļ�:%s", xlsXmlFileName));
						}
						String xlsXmlFileId = ((JSONObject)xlsRetArray.get(0)).getString("_id");//xml�ļ���ID
						
						//���ļ����ݿ���ȡ��Excelģ���ļ�
						fXlsTemplate = File.createTempFile(UIDUtil.get22BitUID(), xlsFileName.substring(xlsFileName.lastIndexOf(".")));
						if(!MongoDBUtil.gridFSDownloadById(fXlsTemplate, xlsFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("����Excelģ���ļ�ʧ�ܣ�");
						}
						
						//���ļ����ݿ���ȡ��Excel xml�����ļ�
						fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
						if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
							throw new Exception("����Excelģ�������ļ�ʧ�ܣ�");
						}
						xlsParser = new ParseXMLAll(fXlsXML);
						}
					}
					
					
					HashMap<String, Object> picParams = new HashMap<String, Object>();
					if(oRecord.getSysUserByStaffId() != null && oRecord.getSysUserByStaffId().getSignature() != null){	//��ȡ������Ա��ǩ��ͼƬ
						picParams.put(MongoDBUtil.KEYNAME_FileSetName, oRecord.getSysUserByStaffId().getSignature());
						JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
						if(jsonInfo != null){
							String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
							fPicWorkStaff = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
							MongoDBUtil.gridFSDownloadById(fPicWorkStaff, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
						}
					}
					
					//��ѯ�����淶��������׼����׼����
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
						throw new Exception("֤���ļ�����ʧ�ܣ�");
					}
					
					isDocTemplate = new FileInputStream(fDocOut);
					java.io.OutputStream outStream = resp.getOutputStream();
					byte[] buf = new byte[2048];
					if(DownloadType.equals("1")){//װ��ԭʼ��¼�ļ�(WebOffice)
						//�޲���
					}else{	//�����ļ�
						String downloadFileName = fileName;
						if (req.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)  //firefox�����
						    downloadFileName = new String(downloadFileName.getBytes("UTF-8"), "ISO8859-1");
						else 
//						     if (req.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)	//IE�����
						     downloadFileName = URLEncoder.encode(downloadFileName, "UTF-8");
						
						//�����������Ϣͷ��  ����-����,����;�ļ���=(��������ļ���) URLEncoader.encode() ���ı���
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
				}finally{	//�ر��ļ���;ɾ����ʱ�ļ�
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
				resp.getWriter().print(String.format("���棺����ģ���ļ�ʧ�ܣ�ʧ����Ϣ:%s", e.getMessage() == null?"��":e.getMessage()));
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 8", e);
				}else{
					log.error("error in FileDownloadServlet-->case 8", e);
				}
			}
			break;
		case 9:	//�޸�֤�飨Ĭ����weboffice�У�
			try{
				String OriginalRecordId = req.getParameter("OriginalRecordId");
				String DownloadType = req.getParameter("DownloadType");	//�������ͣ�0����ʾ�����ļ���1����ʾ������WebOffice��
				String VersionStr = req.getParameter("Version");//֤��İ汾��
				String XlsTemplateFileId = req.getParameter("XlsTemplateFileId");	//ԭʼ��¼Excelģ���ļ�ID�����ڿ���֤������ҳ��
				
				if(OriginalRecordId == null || OriginalRecordId.length() == 0){
					throw new Exception("������������");
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
					throw new Exception("�Ҳ���ָ����ԭʼ��¼��");
				}
//				
//				if(oRecord.getOriginalRecordExcel() !=  null && oRecord.getOriginalRecordExcel().getDoc().length() > 0){
//					throw new Exception("�ü�¼���ϴ�ԭʼ��¼Excel�ļ�������ֱ�ӱ���֤�飡");
//				}
//				
				if(oRecord.getCertificate() != null && oRecord.getCertificate().getDoc().length() > 0){	//�Ѵ���֤��Doc�ļ�����ֱ��ת��
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
					throw new Exception("�ü�¼��δ����֤���ļ���");
				}
					
			}catch(Exception e){
				resp.setContentType("text/html;charset=gbk");
				resp.getWriter().print(String.format("���棺����ģ���ļ�ʧ�ܣ�ʧ����Ϣ:%s", e.getMessage() == null?"��":e.getMessage()));
				if(e.getClass() == java.lang.Exception.class){	//�Զ������Ϣ
					log.debug("exception in FileDownloadServlet-->case 9", e);
				}else{
					log.error("error in FileDownloadServlet-->case 9", e);
				}
			}
			break;
		}
	}
}
