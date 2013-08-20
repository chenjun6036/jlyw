package com.jlyw.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.jlyw.hibernate.SysUser;
import com.jlyw.util.MongoDBUtil;
import com.jlyw.util.SystemCfgUtil;
import com.jlyw.util.UIDUtil;
import com.jlyw.util.UploadDownLoadUtil;

public class FileServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(FileServlet.class);
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Integer method = Integer.parseInt(req.getParameter("method"));	//判断请求的方法类型
		switch (method) {
		case 0: //根据文件ID删除文件
			JSONObject retJSON = new JSONObject();
			try{
				String FileType = req.getParameter("FileType");
				String FileId = req.getParameter("FileId");
				
				if(FileType == null || FileId == null || FileType.length() == 0 || FileId.length() == 0){
					throw new Exception("参数不完整！");
				}
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
				
				/********************        判断是否本人上传（只有本人或管理员才能删除）             ********************/
				switch(Integer.parseInt(FileType)){
				case UploadDownLoadUtil.Type_OriginalRecord:
				case UploadDownLoadUtil.Type_Certificate:
					break; 	//原始记录、证书不需要判断
				default:
					JSONObject retFileInfoObj = MongoDBUtil.getFileInfoById(FileId, type);
					if(retFileInfoObj != null && retFileInfoObj.getString(MongoDBUtil.KEYNAME_UploaderId) != null && retFileInfoObj.getString(MongoDBUtil.KEYNAME_UploaderId).length() > 0){
						if(req.getSession().getAttribute("LOGIN_USER") == null || 
								(!((SysUser)req.getSession().getAttribute("LOGIN_USER")).getId().toString().equalsIgnoreCase(retFileInfoObj.getString(MongoDBUtil.KEYNAME_UploaderId)) && !((SysUser)req.getSession().getAttribute("LOGIN_USER")).getUserName().equalsIgnoreCase(SystemCfgUtil.SystemManagerUserName))){
							throw new Exception("您不是该文件的上传者，不能删除！");
						}
					}
					break;
				}
				
				if(Integer.parseInt(FileType) == UploadDownLoadUtil.Type_Template){	//模板文件：设置文件为注销（不真正删除）
					JSONObject jsonObj = MongoDBUtil.getFileInfoById(FileId, type);
					Map<String, Object> metaDataMap = new HashMap<String, Object>();
					metaDataMap.put(MongoDBUtil.KEYNAME_FileName, jsonObj.getString(MongoDBUtil.KEYNAME_FileName));
					metaDataMap.put(MongoDBUtil.KEYNAME_FileSetName, jsonObj.getString(MongoDBUtil.KEYNAME_FileSetName));
					metaDataMap.put(MongoDBUtil.KEYNAME_UploaderId, jsonObj.getString(MongoDBUtil.KEYNAME_UploaderId));
					metaDataMap.put(MongoDBUtil.KEYNAME_UploaderName, jsonObj.getString(MongoDBUtil.KEYNAME_UploaderName));
					metaDataMap.put(MongoDBUtil.KEYNAME_FileStatus, "false");
					
					if(MongoDBUtil.gridFSUpdateMetaDataById(FileId, type, metaDataMap)){
						retJSON.put("IsOK", true);
					}else{
						retJSON.put("IsOK", false);
						retJSON.put("msg", String.format("文件删除失败！"));
					}
				}else{
					if(MongoDBUtil.gridFSDeleteById(FileId, type)){	//真正删除文件
						retJSON.put("IsOK", true);
					}else{
						retJSON.put("IsOK", false);
						retJSON.put("msg", String.format("文件删除失败！"));
					}
				}
				
			}catch(Exception e){
				try {
					retJSON.put("IsOK", false);
					retJSON.put("msg", String.format("文件删除失败！错误信息：%s", (e!=null && e.getMessage()!=null)?e.getMessage():"无"));
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileServlet-->case 0", e);
				}else{
					log.error("error in FileServlet-->case 0", e);
				}
			}finally{
				resp.setContentType("text/json;charset=utf-8");
				resp.getWriter().write(retJSON.toString());
			}
			break;
		case 1 : //输出PDF（用于打印）
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
					/*java.io.OutputStream outStream = resp.getOutputStream();
					resp.setHeader("Content-type","application/pdf");
					try{
						MongoDBUtil.gridFSDownloadById(outStream, FileId, type);
					}catch(Exception e){
						log.debug("excpetion in FileDownloadServlet-->case 0-->MongoDBUtil.gridFSDownloadById", e);
					}finally{
						outStream.close();
					}*/
					File tempFile = null;
					try{
						tempFile = File.createTempFile(UIDUtil.get22BitUID(), ".pdf");
						MongoDBUtil.gridFSDownloadById(tempFile, FileId, type);
						PdfReader  reader = new PdfReader(tempFile.getCanonicalPath());
						
						StringBuffer script = new StringBuffer();
						script.append("this.print({bUI: false,bSilent: true,bShrinkToFit: false});").append("this.closeDoc();");
						java.io.OutputStream outStream = resp.getOutputStream();
						resp.setHeader("Content-type","application/pdf");
						PdfStamper stamp = null;
						try {
						  stamp = new PdfStamper(reader, outStream);
						  stamp.setViewerPreferences(PdfWriter.HideMenubar 
								  | PdfWriter.HideToolbar | PdfWriter.HideWindowUI);
						  stamp.addJavaScript(script.toString());
						  
						} catch (DocumentException e) {
						  log.error("error in FileDownloadServlet-->case 1", e);
						}finally{
							if(stamp != null){
								stamp.close();
								stamp = null;
							}
							outStream.close();
						}

					}catch(Exception e){
						log.debug("excpetion in FileDownloadServlet-->case 1-->MongoDBUtil.gridFSDownloadById", e);
					}finally{
						if(tempFile != null && tempFile.exists()){
							tempFile.delete();
							tempFile = null;
						}
					}
				}else{
					resp.setContentType("text/html;charset=GBK");
					resp.getWriter().print("指定文件不存在！");
					return;
				}
			}catch(Exception e){
				if(e.getClass() == java.lang.Exception.class){	//自定义的消息
					log.debug("exception in FileDownloadServlet-->case 1", e);
				}else{
					log.error("error in FileDownloadServlet-->case 1", e);
				}
			}
			break;
		case 3: //输出图片（用于页面查看图片）
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
//					String filename = retObj.getString(MongoDBUtil.KEYNAME_FileName);
					
					resp.setContentType("image/jpeg");
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
					log.debug("exception in FileServlet-->case 3", e);
				}else{
					log.error("error in FileServlet-->case 3", e);
				}
			}
			break;
		}
	}

}
