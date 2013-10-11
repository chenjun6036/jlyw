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
					log.debug("开始执行指定任务：AuthBackgroundRuningService");
					
					List<AuthBackgroundRuning> tRetList = AuthBackgroundRuningManager.findPageAllAuthBackgroundRuningBySort(1, 10, "createTime", true);
					OriginalRecordManager oRecordMgr = new OriginalRecordManager();
					for(AuthBackgroundRuning t : tRetList){
						VerifyAndAuthorize v = t.getVerifyAndAuthorize();
						if(v.getAuthorizeTime() == null || v.getIsAuthBgRuning() == null || !v.getIsAuthBgRuning()){	//签字任务已取消或已经完成：将IsAuthBgRuning设置为null即可
							AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, true);
							continue;
						}
						log.debug("1执行签名");
						List<OriginalRecord> oRecordList = oRecordMgr.findByVarProperty(new KeyValueWithOperator("verifyAndAuthorize.id", v.getId(), "="),
								new KeyValueWithOperator("status", 1, "<>"),	//原始记录未注销
								new KeyValueWithOperator("certificate.pdf", null, "is not null"));	//原始记录的证书为正式版本
						if(oRecordList.size() == 0){	//找不到对应的原始记录:该核验和签字任务无效
							log.debug("2执行签名");
							AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, false);
							continue;
							
						}else{
							OriginalRecord o = oRecordList.get(0);
							if(!v.getOriginalRecordExcel().getId().equals(o.getOriginalRecordExcel().getId()) ||
									!v.getCertificate().getId().equals(o.getCertificate().getId())){	//该签字任务已过期，原因：原始记录或证书有改动！
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, false);
								log.debug("3执行签名");
								continue;
							}
							if(v.getAuthorizeResult() == null || !v.getAuthorizeResult()){	//授权签字未通过：将IsAuthBgRuning设置为null即可，不需要将签名图片插入证书中
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, true);
								log.debug("4执行签名");
								continue;
							}
							
							
							try{
								AuthorizeExecute(o, v);	//执行签名
								log.debug("AuthorizeExecute(o, v);	//执行签名");
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, true);
							}catch(Exception e){
								log.debug("exception	//执行签名");
								AuthBackgroundRuningManager.removeAnAuthBackgroundRuning(t, false);
								log.error("error in AuthBackgroundRuningService", e);
							}	
						}
					}
					
					log.debug("结束执行指定任务：AuthBackgroundRuningService");
				} catch (Exception e) {
					log.error("error in AuthBackgroundRuningService", e);
				} finally {
					isRunning = false;
					HibernateSessionFactory.closeSessionForFilter();	//真正关闭Hibernate Session
				}
			}
		}
	}
	
	/**
	 * 证书授权签字执行：重新生成原始记录Excel的PDF（添加检定/校准人员、核验人员的签名图片）和证书Doc、Pdf（添加核验、签字人员的签名图片）
	 * @param o
	 * @param v
	 * @throws Exception
	 */
	public static void AuthorizeExecute(OriginalRecord o, VerifyAndAuthorize v) throws Exception{
		//更新证书(核验人和授权签字人  签字)并重新生成PDF
		String xmlFileId = null;	//证书的xml配置文件
		if(o.getCertificate().getXml() != null && o.getCertificate().getXml().length() > 0){
			xmlFileId = o.getCertificate().getXml();
		}else{
			String fileName = o.getCertificate().getFileName();	//证书文件的文件名
			String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//证书模板配置文件的名称
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(MongoDBUtil.KEYNAME_FileSetName, SystemCfgUtil.CertificateTemplateFilesetName);	//证书模板文件的文件集名称
			Pattern pattern = Pattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
			map.put(MongoDBUtil.KEYNAME_FileName, pattern);
			map.put(MongoDBUtil.KEYNAME_FileStatus, "true");
			JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
			if(retArray.length() == 0){
				throw new Exception(String.format("证书签字失败，未找到证书文件对应的配置文件:%s", xmlFileName));
			}
			xmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
		}
		
		String xlsXmlFileId = null;	//原始记录Excel的配置文件：xlsXmlFileId为空则表示没有原始记录文件（直接编制证书）
		if(o.getOriginalRecordExcel().getDoc().length() == 0 || o.getOriginalRecordExcel().getPdf() == null || o.getOriginalRecordExcel().getPdf().length() == 0){
			xlsXmlFileId = null;
		}else{//获取原始记录Excel的配置文件
			if(o.getOriginalRecordExcel().getXml() != null && o.getOriginalRecordExcel().getXml().length() > 0){
				xlsXmlFileId = o.getOriginalRecordExcel().getXml();
			}else{
				String fileName = o.getOriginalRecordExcel().getFileName();	//Excel文件的文件名
				String xmlFileName = String.format("%s.xml", fileName.substring(0, fileName.lastIndexOf(".")));	//证书模板配置文件的名称
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(MongoDBUtil.KEYNAME_FileSetName, o.getTargetAppliance().getApplianceStandardName().getFilesetName());	//原始记录Excel模板文件的文件集名称
				Pattern pattern = Pattern.compile("^"+xmlFileName+"$", Pattern.CASE_INSENSITIVE);	//正则表达式:判断文件名是否存在（不区分大小写）
				map.put(MongoDBUtil.KEYNAME_FileName, pattern);
				map.put(MongoDBUtil.KEYNAME_FileStatus, "true");
				JSONArray retArray = MongoDBUtil.getFileList(map, MongoDBUtil.CollectionType.Template);
				if(retArray.length() == 0){
					throw new Exception(String.format("证书签字失败，未找到Excel文件对应的配置文件:%s", xmlFileName));
				}
				xlsXmlFileId = ((JSONObject)retArray.get(0)).getString("_id");//xml文件的ID
			}
		}
		
		
		File fDocOld = null, fDocXML = null, fDocOutputFile = null, fTempPdf = null;//证书文件，证书xml定义文件, 新证书文件, 证书PDF文件
		File fPicVerify = null, fPicChecker = null, fPicAuthorizer = null;	//检定员、核验员、授权签字员的签名图片文件
		File fXlsOld = null, fXlsXML = null, fXlsOutputFile = null, fTempXlsPdf = null;	//原始记录Excel文件、配置文件、新Excel文件，证书PDF文件
		try{
			/*******        获取签名图片         ********/
			HashMap<String, Object> picParams = new HashMap<String, Object>();
			if(o.getSysUserByStaffId().getSignature() != null){	//获取检定人员的签名图片
				picParams.put(MongoDBUtil.KEYNAME_FileSetName, o.getSysUserByStaffId().getSignature());
				JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
				if(jsonInfo != null){
					String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
					fPicVerify = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
					MongoDBUtil.gridFSDownloadById(fPicVerify, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
				}
			}
			if(v.getSysUserByVerifierId() != null && v.getSysUserByVerifierId().getSignature() != null){	//获取核验人员的签名图片
				picParams.put(MongoDBUtil.KEYNAME_FileSetName, v.getSysUserByVerifierId().getSignature());
				JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
				if(jsonInfo != null){
					String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
					fPicChecker = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
					MongoDBUtil.gridFSDownloadById(fPicChecker, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
				}
			}
			if(v.getSysUserByAuthorizeExecutorId().getSignature() != null){	//获取授权签字人员的签名图片
				picParams.put(MongoDBUtil.KEYNAME_FileSetName, v.getSysUserByAuthorizeExecutorId().getSignature());
				JSONObject jsonInfo = MongoDBUtil.getLastUploadFileInfo(picParams, MongoDBUtil.CollectionType.Others);
				if(jsonInfo != null){
					String filename = jsonInfo.getString(MongoDBUtil.KEYNAME_FileName);
					fPicAuthorizer = File.createTempFile(UIDUtil.get22BitUID(), filename.substring(filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):0));
					MongoDBUtil.gridFSDownloadById(fPicAuthorizer, jsonInfo.getString("_id"), MongoDBUtil.CollectionType.Others);
				}
			}
			
			List<String> toBeDeletedFileListExcel = new ArrayList<String>();	//文件服务器中需要删除的无效文件
			ParseXMLAll xlsParser = null;
			if(xlsXmlFileId != null){	//更新原始记录Excel文件（PDF）
				//从文件数据库中取出Excel文件
				fXlsOld = File.createTempFile(UIDUtil.get22BitUID(), o.getOriginalRecordExcel().getFileName().substring(o.getOriginalRecordExcel().getFileName().lastIndexOf(".")));
				if(!MongoDBUtil.gridFSDownloadById(fXlsOld, o.getOriginalRecordExcel().getDoc(), MongoDBUtil.CollectionType.OriginalRecord)){
					throw new Exception("获取Excel文件失败！");
				}
				//从文件数据库中取出Excel xml定义文件
				fXlsXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
				if(!MongoDBUtil.gridFSDownloadById(fXlsXML, xlsXmlFileId, MongoDBUtil.CollectionType.Template)){
					throw new Exception("下载Excel文件对应的配置文件失败！");
				}
				xlsParser = new ParseXMLAll(fXlsXML);
				
				fXlsOutputFile = File.createTempFile(UIDUtil.get22BitUID(), fXlsOld.getName().substring(fXlsOld.getName().lastIndexOf('.')));	//重新编辑后的Excel文件
				ExcelUtil.insertWorkerSignatureImgToExcel(fXlsOld, fXlsOutputFile, fPicVerify, fPicChecker, xlsParser);
				if(fXlsOutputFile.exists() && fXlsOutputFile.length() > 0){	//更新了Excel文件（生成并上传PDF至文件服务器中）
					fTempXlsPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//生成的PDF文件
					Office2PdfUtil.docToPdf(fXlsOutputFile, fTempXlsPdf);
					if(fTempXlsPdf.length() == 0){
						throw new Exception("生成Excel的PDF文件失败！");
					}
					//保存PDF到文件服务器
					JSONObject jsonObj = MongoDBUtil.getFileInfoById(o.getOriginalRecordExcel().getPdf(), MongoDBUtil.CollectionType.OriginalRecord);
					HashMap<String, Object> paramsMap = new HashMap<String, Object>();
					
					toBeDeletedFileListExcel.add(jsonObj.getString("_id"));  //原Excel的PDF文件需要删除
					
					//paramsMap.put("_id", new ObjectId((String)jsonObj.get("_id")));	//覆盖原文件
					paramsMap.put(MongoDBUtil.KEYNAME_FileName, jsonObj.get(MongoDBUtil.KEYNAME_FileName));
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, jsonObj.get(MongoDBUtil.KEYNAME_UploaderId));
					paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, jsonObj.get(MongoDBUtil.KEYNAME_UploaderName));
					paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, jsonObj.get(MongoDBUtil.KEYNAME_FileSetName));
					if(!MongoDBUtil.gridFSUpload(fTempXlsPdf, paramsMap, MongoDBUtil.CollectionType.OriginalRecord)){
						throw new Exception("保存原始记录PDF文件至服务器失败！");
					}
					String xlsPdfId = paramsMap.get("_id").toString();
					OriginalRecordExcel excel = o.getOriginalRecordExcel();
					excel.setPdf(xlsPdfId);
					if(!new OriginalRecordExcelManager().update(excel)){
						throw new Exception("更新数据库失败！");
					}else{
						//删除无效的文件
						for(String fid : toBeDeletedFileListExcel){
							try{
								MongoDBUtil.gridFSDeleteById(fid, MongoDBUtil.CollectionType.OriginalRecord);
							}catch(Exception ex){
								log.debug("exception in AuthBackgroundRuningService->AuthorizeExecute->MongoDBUtil.gridFSDeleteById", ex);
							}
						}
					}
				}
			}//end if 更新原始记录Excel文件（PDF）			
			
			List<String> toBeDeletedFileList = new ArrayList<String>();	//文件服务器中需要删除的无效文件
			/*********       更新证书文件（PDF）          **********/
			//从文件数据库中取出证书文件
			fDocOld = File.createTempFile(UIDUtil.get22BitUID(), o.getCertificate().getFileName().substring(o.getCertificate().getFileName().lastIndexOf(".")));
			if(!MongoDBUtil.gridFSDownloadById(fDocOld, o.getCertificate().getDoc(), MongoDBUtil.CollectionType.Certificate)){
				throw new Exception("获取证书失败！");
			}
			
			//从文件数据库中取出证书xml定义文件
			fDocXML = File.createTempFile(UIDUtil.get22BitUID(), ".xml");
			if(!MongoDBUtil.gridFSDownloadById(fDocXML, xmlFileId, MongoDBUtil.CollectionType.Template)){
				throw new Exception("下载证书配置文件失败！");
			}
			ParseXML docParser = new ParseXML(fDocXML);

			fDocOutputFile = File.createTempFile(UIDUtil.get22BitUID(), fDocOld.getName().substring(fDocOld.getName().lastIndexOf('.')));	//生成的证书文件
			fTempPdf = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");	//生成的PDF文件
			WordUtil.AddCertificateSignature(fDocOld, fDocOutputFile, fTempPdf, docParser, 
					(fPicChecker!=null&&fPicChecker.exists()&&fPicChecker.length()>0)?fPicChecker.getAbsolutePath():null, 
					(fPicAuthorizer!=null&&fPicAuthorizer.exists()&&fPicAuthorizer.length()>0)?fPicAuthorizer.getAbsolutePath():null,
					o);
			
			if(!fDocOutputFile.exists() || fDocOutputFile.length() == 0){
				throw new Exception("执行证书签字失败：将签名写入证书文件失败！");
			}
			if(!fTempPdf.exists() && fTempPdf.length() == 0){
				throw new Exception("证书文件转换成PDF文件失败！");
			}
			
			
			
			//保存Word到文件服务器
			JSONObject jsonObj = MongoDBUtil.getFileInfoById(o.getCertificate().getDoc(), MongoDBUtil.CollectionType.Certificate);
			HashMap<String, Object> paramsMap = new HashMap<String, Object>();
			toBeDeletedFileList.add(jsonObj.getString("_id"));	//原证书文件需要删除
			//paramsMap.put("_id", new ObjectId((String)jsonObj.get("_id")));	//覆盖原文件
			paramsMap.put(MongoDBUtil.KEYNAME_FileName, jsonObj.get(MongoDBUtil.KEYNAME_FileName));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, jsonObj.get(MongoDBUtil.KEYNAME_UploaderId));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, jsonObj.get(MongoDBUtil.KEYNAME_UploaderName));
			paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, jsonObj.get(MongoDBUtil.KEYNAME_FileSetName));
			if(!MongoDBUtil.gridFSUpload(fDocOutputFile, paramsMap, MongoDBUtil.CollectionType.Certificate)){
				throw new Exception("保存证书文件至服务器失败！");
			}
			String docId = paramsMap.get("_id").toString();
			
			//保存pdf文件
			if(paramsMap.containsKey("_id")){
				paramsMap.remove("_id");
			}
			
			JSONObject pdfJSON = MongoDBUtil.getFileInfoById(o.getCertificate().getPdf(), MongoDBUtil.CollectionType.Certificate);
			toBeDeletedFileList.add(pdfJSON.getString("_id")); 	//原证书PDF文件也需要删除
//			paramsMap.put("_id",  new ObjectId((String)pdfJSON.get("_id")));	//覆盖原文件
			paramsMap.put(MongoDBUtil.KEYNAME_FileName, pdfJSON.get(MongoDBUtil.KEYNAME_FileName));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderId, pdfJSON.get(MongoDBUtil.KEYNAME_UploaderId));
			paramsMap.put(MongoDBUtil.KEYNAME_UploaderName, pdfJSON.get(MongoDBUtil.KEYNAME_UploaderName));
			paramsMap.put(MongoDBUtil.KEYNAME_FileSetName, pdfJSON.get(MongoDBUtil.KEYNAME_FileSetName));
			if(!MongoDBUtil.gridFSUpload(fTempPdf, paramsMap, MongoDBUtil.CollectionType.Certificate)){
				throw new Exception("保存PDF文件至服务器失败！");
			}
			String pdfId = paramsMap.get("_id").toString();
			
			//更新Certificate
			Certificate c = o.getCertificate();
			c.setDoc(docId);
			c.setPdf(pdfId);
			if(!new CertificateManager().update(c)){
				throw new Exception("更新数据库失败！");
			}else{
				//删除无效的文件
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
		}finally{	//关闭文件流;删除临时文件
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
