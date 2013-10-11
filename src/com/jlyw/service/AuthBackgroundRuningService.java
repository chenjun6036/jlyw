package com.jlyw.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.hibernate.AuthBackgroundRuning;
import com.jlyw.hibernate.Certificate;
import com.jlyw.hibernate.HibernateSessionFactory;
import com.jlyw.hibernate.OriginalRecord;
import com.jlyw.hibernate.OriginalRecordExcel;
import com.jlyw.hibernate.VerifyAndAuthorize;
import com.jlyw.manager.AuthBackgroundRuningManager;
import com.jlyw.manager.CertificateManager;
import com.jlyw.manager.OriginalRecordExcelManager;
import com.jlyw.manager.OriginalRecordManager;
import com.jlyw.util.ExcelUtil;
import com.jlyw.util.KeyValueWithOperator;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.Office2PdfUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.WordUtil;
import com.jlyw.util.xmlHandler.ParseXML;
import com.jlyw.util.xmlHandler.ParseXMLAll;

public class AuthBackgroundRuningService extends TimerTask  {
	private static final Log log = LogFactory.getLog(AuthBackgroundRuningService.class);
	private static boolean isRunning = false;
	private final static Byte[] locks = new Byte[0];  
	public void run() {
		synchronized (locks){ 
			if (!isRunning) {
				try {
					isRunning = true;
					log.debug("��ʼִ��ָ������AuthBackgroundRuningService");
					
					List<AuthBackgroundRuning> tRetList = AuthBackgroundRuningManager.findPageAllAuthBackgroundRuningBySort(1, 10, "createTime", true);
					OriginalRecordManager oRecordMgr = new OriginalRecordManager();
					for(AuthBackgroundRuning t : tRetList){
						VerifyAndAuthorize v = t.getVerifyAndAuthorize();
						if(v.getAuthorizeTime() == null || v.getIsAuthBgRuning() == null || !v.getIsAuthBgRuning()){	//ǩ��������ȡ�����Ѿ���ɣ���IsAuthBgRuning����Ϊnull����
							AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, true);
							continue;
						}
						log.debug("1ִ��ǩ��");
						List<OriginalRecord> oRecordList = oRecordMgr.findByVarProperty(new KeyValueWithOperator("verifyAndAuthorize.id", v.getId(), "="),
								new KeyValueWithOperator("status", 1, "<>"),	//ԭʼ��¼δע��
								new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//ԭʼ��¼��֤��Ϊ��ʽ�汾
						if(oRecordList.size() == 0){	//�Ҳ�����Ӧ��ԭʼ��¼:�ú����ǩ��������Ч
							log.debug("2ִ��ǩ��");
							AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, false);
							continue;
							
						}else{
							OriginalRecord o = oRecordList.get(0);
							if(!v.getOriginalRecordExcel().getId().equals(o.getOriginalRecordExcel().getId()) ||
									!v.getCertificate().getId().equals(o.getCertificate().getId())){	//��ǩ�������ѹ��ڣ�ԭ��ԭʼ��¼��֤���иĶ���
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, false);
								log.debug("3ִ��ǩ��");
								continue;
							}
							if(v.getAuthorizeResult() == null || !v.getAuthorizeResult()){	//��Ȩǩ��δͨ������IsAuthBgRuning����Ϊnull���ɣ�����Ҫ��ǩ��ͼƬ����֤����
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, true);
								log.debug("4ִ��ǩ��");
								continue;
							}
							
							
							try{
								AuthorizeExecute(o, v);	//ִ��ǩ��
								log.debug("AuthorizeExecute(o, v);	//ִ��ǩ��");
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, true);
							}catch(Exception e){
								log.debug("exception	//ִ��ǩ��");
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, false);
								log.error("error in AuthBackgroundRuningService", e);
							}	
						}
					}
					
					log.debug("����ִ��ָ������AuthBackgroundRuningService");
				} catch (Exception e) {
					log.error("error in AuthBackgroundRuningService", e);
				} finally {
					isRunning = false;
					HibernateSessionFactory.closeSessionForFilter();	//�����ر�Hibernate Session
				}
			}
		}
	}
	
	/**
	 * ֤����Ȩǩ��ִ�У���������ԭʼ��¼Excel��PDF����Ӽ춨/У׼��Ա��������Ա��ǩ��ͼƬ����֤��Doc��Pdf����Ӻ��顢ǩ����Ա��ǩ��ͼƬ��
	 * @param o
	 * @param v
	 * @throws Exception
	 */
	public static void AuthorizeExecute(OriginalRecord o, VerifyAndAuthorize v) throws Exception{
		//����֤��(�����˺���Ȩǩ����  ǩ��)����������PDF
		String xmlFileId = null;	//֤���xml�����ļ�
		if(o.getCertificate().getXml() != null && o.getCertificate().getXml().length() > 0){
			xmlFileId = o.getCertificate().getXml();
		}else{
			String fileName = o.getCertificate().getFileName();	//֤���ļ����ļ���
			String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);	//֤��ģ���ļ����ļ�������
			Pattern pattern = Pattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
			map.put(MongoDBUtil.KEYNAME_FileName, pattern);
			map.put(MongoDBUtil.KEYNAME_FileStatus, "true");
			JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
			if(retArray.length() == 0){
				throw new Exception(String.format("֤��ǩ��ʧ�ܣ�δ�ҵ�֤���ļ���Ӧ�������ļ�:%s", xmlFileName));
			}
			xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
		}
		
		String xlsXmlFileId = null;	//ԭʼ��¼Excel�������ļ���xlsXmlFileIdΪ�����ʾû��ԭʼ��¼�ļ���ֱ�ӱ���֤�飩
		if(o.getOriginalRecordExcel().getDoc().length() == 0 || o.getOriginalRecordExcel().getPdf() == null || o.getOriginalRecordExcel().getPdf().length() == 0){
			xlsXmlFileId = null;
		}else{//��ȡԭʼ��¼Excel�������ļ�
			if(o.getOriginalRecordExcel().getXml() != null && o.getOriginalRecordExcel().getXml().length() > 0){
				xlsXmlFileId = o.getOriginalRecordExcel().getXml();
			}else{
				String fileName = o.getOriginalRecordExcel().getFileName();	//Excel�ļ����ļ���
				String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//֤��ģ�������ļ�������
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//ԭʼ��¼Excelģ���ļ����ļ�������
				Pattern pattern = Pattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//������ʽ:�ж��ļ����Ƿ���ڣ������ִ�Сд��
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("֤��ǩ��ʧ�ܣ�δ�ҵ�Excel�ļ���Ӧ�������ļ�:%s", xmlFileName));
				}
				xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml�ļ���ID
			}
		}
		
		
		File fDocOld = null, fDocXML = null, fDocOutputFile = null, fTempPdf = null;//֤���ļ���֤��xml�����ļ�, ��֤���ļ�, ֤��PDF�ļ�
		File fPicVerify = null, fPicChecker = null, fPicAuthorizer = null;	//�춨Ա������Ա����Ȩǩ��Ա��ǩ��ͼƬ�ļ�
		File fXlsOld = null, fXlsXML = null, fXlsOutputFile = null, fTempXlsPdf = null;	//ԭʼ��¼Excel�ļ��������ļ�����Excel�ļ���֤��PDF�ļ�
		try{
			/*******        ��ȡǩ��ͼƬ         ********/
			HashMap<String, Object> picParams = new HashMap<String, Object>();
			if(o.getSysUserByStaffId().getSignature() != null){	//��ȡ�춨��Ա��ǩ��ͼƬ
				picParams.put(MongoDBUtil.KEYNAME_FileSetName, o.getSysUserByStaffId().getSignature());
				JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
				if(jsonInfo != null){
					String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
					fPicVerify = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
					MongoDBUtil.gridFSDownloadById(fPicVerify, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
				}
			}
			if(v.getSysUserByVerifierId() != null && v.getSysUserByVerifierId().getSignature() != null){	//��ȡ������Ա��ǩ��ͼƬ
				picParams.put(MongoDBUtil.KEYNAME_FileSetName, v.getSysUserByVerifierId().getSignature());
				JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
				if(jsonInfo != null){
					String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
					fPicChecker = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
					MongoDBUtil.gridFSDownloadById(fPicChecker, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
				}
			}
			if(v.getSysUserByAuthorizeExecutorId().getSignature() != null){	//��ȡ��Ȩǩ����Ա��ǩ��ͼƬ
				picParams.put(MongoDBUtil.KEYNAME_FileSetName, v.getSysUserByAuthorizeExecutorId().getSignature());
				JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
				if(jsonInfo != null){
					String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
					fPicAuthorizer = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
					MongoDBUtil.gridFSDownloadById(fPicAuthorizer, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
				}
			}
			
			List<String> toBeDeletedFileListExcel = new ArrayList<String>();	//�ļ�����������Ҫɾ������Ч�ļ�
			ParseXMLAll xlsParser = null;
			if(xlsXmlFileId != null){	//����ԭʼ��¼Excel�ļ���PDF��
				//���ļ����ݿ���ȡ��Excel�ļ�
				fXlsOld = File.createTempFile(UIDUtil.get22BitUID(), o.getOriginalRecordExcel().getFileName().substring(o.getOriginalRecordExcel().getFileName().lastIndexOf(".")));
				if(!MongoDBUtil.gridFSDownloadById(fXlsOld, o.getOriginalRecordExcel().getDoc(), MongoDBUtil.CollectionType.OriginalRecord)){
					throw new Exception("��ȡExcel�ļ�ʧ�ܣ�");
				}
				//���ļ����ݿ���ȡ��Excel xml�����ļ�
				fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
				if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
					throw new Exception("����Excel�ļ���Ӧ�������ļ�ʧ�ܣ�");
				}
				xlsParser = new ParseXMLAll(fXlsXML);
				
				fXlsOutputFile = File.createTempFile(UIDUtil.get22BitUID(), fXlsOld.getName().substring(fXlsOld.getName().lastIndexOf('.')));	//���±༭���Excel�ļ�
				ExcelUtil.insertWorkerSignatureImgToExcel(fXlsOld, fXlsOutputFile, fPicVerify, fPicChecker, xlsParser);
				if(fXlsOutputFile.exists() && fXlsOutputFile.length() > 0){	//������Excel�ļ������ɲ��ϴ�PDF���ļ��������У�
					fTempXlsPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//���ɵ�PDF�ļ�
					Office2PdfUtil.docToPdf(fXlsOutputFile, fTempXlsPdf);
					if(fTempXlsPdf.length() == 0){
						throw new Exception("����Excel��PDF�ļ�ʧ�ܣ�");
					}
					//����PDF���ļ�������
					JSONObject jsonObj = MongoDBUtil.getFileInfoById(o.getOriginalRecordExcel().getPdf(), MongoDBUtil.CollectionType.OriginalRecord);
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					
					toBeDeletedFileListExcel.add(jsonObj.getString("_id"));  //ԭExcel��PDF�ļ���Ҫɾ��
					
					//paramsMap.put("_id", new ObjectId((String)jsonObj.get("_id")));	//����ԭ�ļ�
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, jsonObj.get(MongoDBUtil.KEYNAME_FileName));
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, jsonObj.get(MongoDBUtil.KEYNAME_UploaderId));
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, jsonObj.get(MongoDBUtil.KEYNAME_UploaderName));
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, jsonObj.get(MongoDBUtil.KEYNAME_FileSetName));
					if(!MongoDBUtil.gridFSUpload(fTempXlsPdf, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
						throw new Exception("����ԭʼ��¼PDF�ļ���������ʧ�ܣ�");
					}
					String xlsPdfId = paramsMap.get("_id").toString();
					OriginalRecordExcel excel = o.getOriginalRecordExcel();
					excel.setPdf(xlsPdfId);
					if(!new OriginalRecordExcelManager().update(excel)){
						throw new Exception("�������ݿ�ʧ�ܣ�");
					}else{
						//ɾ����Ч���ļ�
						for(String fid : toBeDeletedFileListExcel){
							try{
								MongoDBUtil.gridFSDeleteById(fid, MongoDBUtil.CollectionType.OriginalRecord);
							}catch(Exception ex){
								log.debug("exception in AuthBackgroundRuningService->AuthorizeExecute->MongoDBUtil.gridFSDeleteById", ex);
							}
						}
					}
				}
			}//end if ����ԭʼ��¼Excel�ļ���PDF��			
			
			List<String> toBeDeletedFileList = new ArrayList<String>();	//�ļ�����������Ҫɾ������Ч�ļ�
			/*********       ����֤���ļ���PDF��          **********/
			//���ļ����ݿ���ȡ��֤���ļ�
			fDocOld = File.createTempFile(UIDUtil.get22BitUID(), o.getCertificate().getFileName().substring(o.getCertificate().getFileName().lastIndexOf(".")));
			if(!MongoDBUtil.gridFSDownloadById(fDocOld, o.getCertificate().getDoc(), MongoDBUtil.CollectionType.Certificate)){
				throw new Exception("��ȡ֤��ʧ�ܣ�");
			}
			
			//���ļ����ݿ���ȡ��֤��xml�����ļ�
			fDocXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
			if(!MongoDBUtil.gridFSDownloadById(fDocXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
				throw new Exception("����֤�������ļ�ʧ�ܣ�");
			}
			ParseXML docParser = new ParseXML(fDocXML);

			fDocOutputFile = File.createTempFile(UIDUtil.get22BitUID(), fDocOld.getName().substring(fDocOld.getName().lastIndexOf('.')));	//���ɵ�֤���ļ�
			fTempPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//���ɵ�PDF�ļ�
			WordUtil.AddCertificateSignature(fDocOld, fDocOutputFile, fTempPdf, docParser, 
					(fPicChecker!=null&&fPicChecker.exists()&&fPicChecker.length()>0)?fPicChecker.getAbsolutePath():null, 
					(fPicAuthorizer!=null&&fPicAuthorizer.exists()&&fPicAuthorizer.length()>0)?fPicAuthorizer.getAbsolutePath():null,
					o);
			
			if(!fDocOutputFile.exists() || fDocOutputFile.length() == 0){
				throw new Exception("ִ��֤��ǩ��ʧ�ܣ���ǩ��д��֤���ļ�ʧ�ܣ�");
			}
			if(!fTempPdf.exists() && fTempPdf.length() == 0){
				throw new Exception("֤���ļ�ת����PDF�ļ�ʧ�ܣ�");
			}
			
			
			
			//����Word���ļ�������
			JSONObject jsonObj = MongoDBUtil.getFileInfoById(o.getCertificate().getDoc(), MongoDBUtil.CollectionType.Certificate);
			HashMap<String, Object> paramsMap = new HashMap<String, Object>();
			toBeDeletedFileList.add(jsonObj.getString("_id"));	//ԭ֤���ļ���Ҫɾ��
			//paramsMap.put("_id", new ObjectId((String)jsonObj.get("_id")));	//����ԭ�ļ�
			paramsMap.put(MongoDBUtil.KEYNAME_FileName, jsonObj.get(MongoDBUtil.KEYNAME_FileName));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, jsonObj.get(MongoDBUtil.KEYNAME_UploaderId));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, jsonObj.get(MongoDBUtil.KEYNAME_UploaderName));
			paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, jsonObj.get(MongoDBUtil.KEYNAME_FileSetName));
			if(!MongoDBUtil.gridFSUpload(fDocOutputFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
				throw new Exception("����֤���ļ���������ʧ�ܣ�");
			}
			String docId = paramsMap.get("_id").toString();
			
			//����pdf�ļ�
			if(paramsMap.containsKey("_id")){
				paramsMap.remove("_id");
			}
			
			JSONObject pdfJSON = MongoDBUtil.getFileInfoById(o.getCertificate().getPdf(), MongoDBUtil.CollectionType.Certificate);
			toBeDeletedFileList.add(pdfJSON.getString("_id")); 	//ԭ֤��PDF�ļ�Ҳ��Ҫɾ��
//			paramsMap.put("_id",  new ObjectId((String)pdfJSON.get("_id")));	//����ԭ�ļ�
			paramsMap.put(MongoDBUtil.KEYNAME_FileName, pdfJSON.get(MongoDBUtil.KEYNAME_FileName));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, pdfJSON.get(MongoDBUtil.KEYNAME_UploaderId));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, pdfJSON.get(MongoDBUtil.KEYNAME_UploaderName));
			paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, pdfJSON.get(MongoDBUtil.KEYNAME_FileSetName));
			if(!MongoDBUtil.gridFSUpload(fTempPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
				throw new Exception("����PDF�ļ���������ʧ�ܣ�");
			}
			String pdfId = paramsMap.get("_id").toString();
			
			//����Certificate
			Certificate c = o.getCertificate();
			c.setDoc(docId);
			c.setPdf(pdfId);
			if(!new CertificateManager().update(c)){
				throw new Exception("�������ݿ�ʧ�ܣ�");
			}else{
				//ɾ����Ч���ļ�
				for(String fid : toBeDeletedFileList){
					try{
						MongoDBUtil.gridFSDeleteById(fid, MongoDBUtil.CollectionType.Certificate);
					}catch(Exception ex){
						log.debug("exception in AuthBackgroundRuningService->AuthorizeExecute->MongoDBUtil.gridFSDeleteById", ex);
					}
				}
			}
		}catch(Exception re){
			re.printStackTrace();
			throw re;
		}finally{	//�ر��ļ���;ɾ����ʱ�ļ�
			if(fPicVerify != null && fPicVerify.exists()){
				fPicVerify.delete();
			}
			if(fPicChecker != null && fPicChecker.exists()){
				fPicChecker.delete();
			}
			if(fPicAuthorizer != null && fPicAuthorizer.exists()){
				fPicAuthorizer.delete();
			}
			if(fXlsOld != null && fXlsOld.exists()){
				fXlsOld.delete();
			}
			if(fXlsXML != null && fXlsXML.exists()){
				fXlsXML.delete();
			}
			if(fTempXlsPdf != null && fTempXlsPdf.exists()){
				fTempXlsPdf.delete();
			}
			if(fXlsOutputFile != null && fXlsOutputFile.exists()){
				fXlsOutputFile.delete();
			}
			if(fDocOld != null && fDocOld.exists()){
				fDocOld.delete();
			}
			if(fDocXML != null && fDocXML.exists()){
				fDocXML.delete();
			}
			if(fTempPdf != null && fTempPdf.exists()){
				fTempPdf.delete();
			}
			if(fDocOutputFile != null && fDocOutputFile.exists()){
				fDocOutputFile.delete();
			}
			
		}
	}
}
