package com.jlyw.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.jlyw.util.mongodbService.DBPoolManager;
import com.jlyw.util.mongodbService.MongoService;
import com.jlyw.util.mongodbService.MongoServiceImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoDBUtil {
	Mongo connection;
	DB db;
	DBCollection collection;
	GridFS myFS;

	private static String BucketOriginalRecord = SystemCfgUtil.BucketOriginalRecord;	//原始记录
	private static String BucketCertificate = SystemCfgUtil.BucketCertificate;	//证书
	private static String BucketTemplate = SystemCfgUtil.BucketTemplate;	//模板文件
	private static String BucketAttachment = SystemCfgUtil.BucketAttachment;	//附件
	private static String BucketSharing = SystemCfgUtil.BucketSharing;	//共享文件
	private static String BucketOthers = SystemCfgUtil.BucketOthers;	//其他
	
	public static final String KEYNAME_FileName = "filename";	//文件名键名
	public static final String KEYNAME_FileSetName = "filesetname";	//系统内部标识，SqlServer中的文件URL字段存储该名称值，用于标示（多个）文件所属的文件集
	public static final String KEYNAME_UploaderName = "uploadername";	//上传者姓名
	public static final String KEYNAME_UploaderId = "uploaderid";	//上传者ID
	public static final String KEYNAME_FileStatus = "filestatus";	//文件状态（仅对模板文件有效，'true'：文件有效；'false':文件已注销）
	
	public enum CollectionType{
		OriginalRecord,	//原始记录
		Certificate,	//证书
		Template,		//模板文件
		Attachment,		//附件
		Sharing,		//共享文件
		Others		//其他
	}

	
	public static void main(String[] args) throws MongoException, IOException, NoSuchAlgorithmException {
//		for(int i = 10000; i < 10004; i++){
//			MyThread t1 = new MyThread(i);
//			t1.start();
//		}
//		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		System.out.println("-------------------------");
//		HashMap<String, Object> map = new HashMap<String, Object>();
//		map.put("_id", new ObjectId("4f715bcb4dec4b9c25bcb70a"));
//		System.out.println(MongoDBUtil.getFileList(map, CollectionType.OriginalRecord));
		
		
		
/*		
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(KEYNAME_FileSetName, "test");
		paramsMap.put(KEYNAME_UploaderId, "2");
		paramsMap.put(KEYNAME_UploaderName, "a");
		
		paramsMap.put(KEYNAME_FileName, "CPU.txt");
		InputStream in2 = new FileInputStream(new java.io.File("D:/CPU.txt"));
		try {
			MongoDBUtil.gridFSUpload(in2, paramsMap, CollectionType.Template);
			System.out.println("id 2 = "+paramsMap.get("_id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			in2.close();
		}
		if(paramsMap.containsKey("_id")){
			paramsMap.remove("_id");
		}
		paramsMap.put("filename", "test.xml");
		InputStream in = new FileInputStream(new java.io.File("D:/WorkSpace\\jlyw\\jlyw\\WebRoot\\WebOffice\\doc\\test.xml"));
		try {
			MongoDBUtil.gridFSUpload(in, paramsMap, CollectionType.Template);
			System.out.println("id 1 = "+paramsMap.get("_id").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			in.close();
		}
*/
		
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put(KEYNAME_FileSetName, "20120406133125031_7823");
		Pattern pattern = Pattern.compile(".+\\.xml$", Pattern.CASE_INSENSITIVE);	//正则表达式:以.xls或者.xlsx结尾的
		paramsMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
		System.out.println(MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Template));
		
//		System.out.println(getLastUploadFileInfo(paramsMap, CollectionType.Template));
		
		
	}
	
	
	/**
	 * 上传一个文件至文件服务器（MongoDB）
	 * @param in : 要上传的文件（可以是InputStream或者byte[]或者File 三种）
	 * @param paramsMap 文件的参数值（必须含有filesetname项）:如果含有<"_id", ObjectId>键值对，则上传时查找该_id所指向的文件并删除之，再用指定的_id上传新的文件
	 * @param type
	 * @return 返回上传结果
	 */
	public static boolean gridFSUpload(Object in, HashMap<String,Object> paramsMap, CollectionType type) throws UnknownHostException, MongoException, Exception{
		if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName：系统内部标识，SqlServer中的文件URL字段存储该名称值，用于标示（多个）文件所属的文件集
			throw new Exception("文件标示为空：filesetname is null");
		}
		MongoService s;
		switch(type){
		case OriginalRecord:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
			break;
		case Certificate:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
			break;
		case Template:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
			break;
		case Attachment:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
			break;
		case Sharing:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
			break;
		case Others:
		default:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
		}
		return s.gridFSUpload(in, paramsMap);
	}
	
	/**
	 * 从文件服务器（MongoDB）中下载一个文件
	 * @param out
	 * @param paramsMap ：查找文件的条件
	 * @param type
	 * @return
	 */
	public static boolean gridFSDownload(OutputStream out,HashMap<String, Object> paramsMap, CollectionType type){
		try{
			MongoService s;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
			}
			Iterator<Entry<String, Object>> iter = paramsMap.entrySet().iterator();
			DBObject query = new BasicDBObject();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				query.put(entry.getKey(), entry.getValue());
			}
			GridFSDBFile dbFile = s.findOne(query);
			dbFile.writeTo(out);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从文件服务器（MongoDB）中下载一个文件
	 * @param out
	 * @param idStr ：查找文件的id字符串值
	 * @param type
	 * @return
	 */
	public static boolean gridFSDownloadById(OutputStream out, String idStr, CollectionType type){
		try{
			MongoService s;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
			}
			ObjectId id = new ObjectId(idStr);
			GridFSDBFile dbFile = s.findOneById(id);
			dbFile.writeTo(out);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 从文件服务器（MongoDB）中下载一个文件
	 * @param file
	 * @param idStr ：查找文件的id字符串值
	 * @param type
	 * @return
	 */
	public static boolean gridFSDownloadById(java.io.File file, String idStr, CollectionType type){
		try{
			MongoService s;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
			}
			ObjectId id = new ObjectId(idStr);
			GridFSDBFile dbFile = s.findOneById(id);
			dbFile.writeTo(file);
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从文件服务器（MongoDB）中删除一个文件
	 * @param out
	 * @param idStr ：文件的id字符串值
	 * @param type
	 * @return
	 */
	public static boolean gridFSDeleteById(String idStr, CollectionType type){
		try{
			MongoService s;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
			}
			ObjectId id = new ObjectId(idStr);
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("_id", id);
			return s.gridFSDelete(map);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 获取文件列表
	 * @param paramsMap：查找条件（必须含有filesetname项）
	 * @param type
	 * @return 文件列表信息:包含信息："_id"(文件ID),"filename"(文件名),"length"(文件大小),"uploadDate"(上传时间),"filesetname"(文件集名称),"uploadername"(上传者姓名),"uploaderid"(上传者ID)
	 */
	public static JSONArray getFileList(HashMap<String, Object> paramsMap, CollectionType type){
		JSONArray retArray = new JSONArray();
		try{
			if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName：系统内部标识，SqlServer中的文件URL字段存储该名称值，用于标示（多个）文件所属的文件集
				throw new Exception("文件标示为空：filesetname is null");
			}
			
			MongoService s;
			int FileType;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				FileType = UploadDownLoadUtil.Type_OriginalRecord;
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				FileType = UploadDownLoadUtil.Type_Certificate;
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				FileType = UploadDownLoadUtil.Type_Template;
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				FileType = UploadDownLoadUtil.Type_Attachment;
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				FileType = UploadDownLoadUtil.Type_Sharing;
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
				FileType = UploadDownLoadUtil.Type_Others;
			}
			
			Iterator<Entry<String, Object>> iter = paramsMap.entrySet().iterator();
			DBObject query = new BasicDBObject();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				query.put(entry.getKey(), entry.getValue());
			}
			DBCursor cur = s.getFileList(query);
			Iterator<DBObject> iterObj = cur.iterator();
			while(iterObj.hasNext()){
				DBObject obj = iterObj.next();
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("_id", obj.get("_id").toString());
				jsonObj.put(KEYNAME_FileName, obj.get(KEYNAME_FileName)==null?"null":obj.get(KEYNAME_FileName).toString());
				jsonObj.put("length", String.format("%.1f KB", ((Long)obj.get("length"))/1024.0));
				jsonObj.put("uploadDate", DateTimeFormatUtil.DateTimeFormat.format((Date)obj.get("uploadDate")));
				jsonObj.put(KEYNAME_FileSetName, obj.get(KEYNAME_FileSetName)==null?"":obj.get(KEYNAME_FileSetName).toString());
				jsonObj.put(KEYNAME_UploaderId, obj.get(KEYNAME_UploaderId)==null?"":obj.get(KEYNAME_UploaderId).toString());
				jsonObj.put(KEYNAME_UploaderName, obj.get(KEYNAME_UploaderName)==null?"":obj.get(KEYNAME_UploaderName).toString());
				
				jsonObj.put("filetype", FileType);	//用于删除文件时上传文件类别
				retArray.put(jsonObj);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return retArray;
	}
	
	/**
	 * 根据ID字符串查找文件信息
	 * @param idStr
	 * @param type
	 * @return 文件信息（JSONObject对象），若未找到则返回null
	 */
	public static JSONObject getFileInfoById(String idStr, CollectionType type){
		try{
			MongoService s;
			int FileType;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				FileType = UploadDownLoadUtil.Type_OriginalRecord;
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				FileType = UploadDownLoadUtil.Type_Certificate;
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				FileType = UploadDownLoadUtil.Type_Template;
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				FileType = UploadDownLoadUtil.Type_Attachment;
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				FileType = UploadDownLoadUtil.Type_Sharing;
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
				FileType = UploadDownLoadUtil.Type_Others;
			}
			
			DBObject query = new BasicDBObject();
			query.put("_id", new ObjectId(idStr));
			DBCursor cur = s.getFileList(query);
			Iterator<DBObject> iterObj = cur.iterator();
			if(iterObj.hasNext()){
				DBObject obj = iterObj.next();
				JSONObject retObj = new JSONObject();
				retObj.put("_id", obj.get("_id").toString());
				retObj.put(KEYNAME_FileName, obj.get("filename")==null?"null":obj.get("filename").toString());
				retObj.put("length", String.format("%.1f KB", ((Long)obj.get("length"))/1024.0));
				retObj.put("uploadDate", DateTimeFormatUtil.DateTimeFormat.format((Date)obj.get("uploadDate")));
				retObj.put(KEYNAME_FileSetName, obj.get(KEYNAME_FileSetName)==null?"":obj.get(KEYNAME_FileSetName).toString());
				retObj.put(KEYNAME_UploaderId, obj.get(KEYNAME_UploaderId)==null?"":obj.get(KEYNAME_UploaderId).toString());
				retObj.put(KEYNAME_UploaderName, obj.get(KEYNAME_UploaderName)==null?"":obj.get(KEYNAME_UploaderName).toString());
				
				retObj.put("filetype", FileType);	//用于删除文件时上传文件类别
				
				return retObj;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查找符合条件的文件中最后一次上传的文件信息
	 * @param paramsMap:查询条件
	 * @param type
	 * @return 文件信息（JSONObject对象），若未找到则返回null
	 */
	public static JSONObject getLastUploadFileInfo(HashMap<String, Object> paramsMap, CollectionType type){
		try{
			if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName：系统内部标识，SqlServer中的文件URL字段存储该名称值，用于标示（多个）文件所属的文件集
				throw new Exception("文件标示为空：filesetname is null");
			}
			
			MongoService s;
			int FileType;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				FileType = UploadDownLoadUtil.Type_OriginalRecord;
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				FileType = UploadDownLoadUtil.Type_Certificate;
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				FileType = UploadDownLoadUtil.Type_Template;
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				FileType = UploadDownLoadUtil.Type_Attachment;
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				FileType = UploadDownLoadUtil.Type_Sharing;
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
				FileType = UploadDownLoadUtil.Type_Others;
			}
			
			Iterator<Entry<String, Object>> iter = paramsMap.entrySet().iterator();
			DBObject query = new BasicDBObject();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				query.put(entry.getKey(), entry.getValue());
			}
			DBObject retObj = null;
			DBCursor cur = s.getFileList(query);
			Iterator<DBObject> iterObj = cur.iterator();
			while(iterObj.hasNext()){
				DBObject obj = iterObj.next();
				if(retObj == null || ((Date)retObj.get("uploadDate")).before((Date)obj.get("uploadDate"))){
					retObj = obj;
				}
			}
			if(retObj == null){
				return null;
			}else{
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("_id", retObj.get("_id").toString());
				jsonObj.put(KEYNAME_FileName, retObj.get(KEYNAME_FileName)==null?"null":retObj.get(KEYNAME_FileName).toString());
				jsonObj.put("length", String.format("%.1f KB", ((Long)retObj.get("length"))/1024.0));
				jsonObj.put("uploadDate", DateTimeFormatUtil.DateTimeFormat.format((Date)retObj.get("uploadDate")));
				jsonObj.put(KEYNAME_FileSetName, retObj.get(KEYNAME_FileSetName)==null?"":retObj.get(KEYNAME_FileSetName).toString());
				jsonObj.put(KEYNAME_UploaderId, retObj.get(KEYNAME_UploaderId)==null?"":retObj.get(KEYNAME_UploaderId).toString());
				jsonObj.put(KEYNAME_UploaderName, retObj.get(KEYNAME_UploaderName)==null?"":retObj.get(KEYNAME_UploaderName).toString());
				
				jsonObj.put("filetype", FileType);	//用于删除文件时上传文件类别
				
				return jsonObj;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分页获取文件列表
	 * @param currentPage
	 * @param pageSize
	 * @param paramsMap 查找条件（必须含有filesetname项）
	 * @param type
	 * @return 文件列表信息:包含信息{total:大小, rows:[列表信息]}，其中列表中的每一个条目包括如下信息：
	 * 				"_id"(文件ID),"filename"(文件名),"length"(文件大小),"uploadDate"(上传时间),"filesetname"(文件集名称),"uploadername"(上传者姓名),"uploaderid"(上传者ID)
	 */
	public static JSONObject getPageAllFileList(int currentPage, int pageSize, HashMap<String, Object> paramsMap, CollectionType type) throws Exception{
		JSONObject retJSON = new JSONObject();
		try{
			if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName：系统内部标识，SqlServer中的文件URL字段存储该名称值，用于标示（多个）文件所属的文件集
				throw new Exception("文件标示为空：filesetname is null");
			}
			if (currentPage <= 0) {
				currentPage = 1;
			}
			MongoService s;
			int FileType;
			switch(type){
			case OriginalRecord:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
				FileType = UploadDownLoadUtil.Type_OriginalRecord;
				break;
			case Certificate:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
				FileType = UploadDownLoadUtil.Type_Certificate;
				break;
			case Template:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
				FileType = UploadDownLoadUtil.Type_Template;
				break;
			case Attachment:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
				FileType = UploadDownLoadUtil.Type_Attachment;
				break;
			case Sharing:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
				FileType = UploadDownLoadUtil.Type_Sharing;
				break;
			case Others:
			default:
				s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
				FileType = UploadDownLoadUtil.Type_Others;
			}
			
			Iterator<Entry<String, Object>> iter = paramsMap.entrySet().iterator();
			DBObject query = new BasicDBObject();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				query.put(entry.getKey(), entry.getValue());
			}
			DBCursor cur = s.getFileList(query);
			retJSON.put("total", cur.size());
			DBCursor newCur = cur.skip((currentPage - 1) * pageSize).limit(pageSize);	//分页
			Iterator<DBObject> iterObj = newCur.iterator();
			JSONArray jsonArray = new JSONArray();
			while(iterObj.hasNext()){
				DBObject obj = iterObj.next();
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("_id", obj.get("_id").toString());
				jsonObj.put(KEYNAME_FileName, obj.get(KEYNAME_FileName)==null?"null":obj.get(KEYNAME_FileName).toString());
				jsonObj.put("length", String.format("%.1f KB", ((Long)obj.get("length"))/1024.0));
				jsonObj.put("uploadDate", DateTimeFormatUtil.DateTimeFormat.format((Date)obj.get("uploadDate")));
				jsonObj.put(KEYNAME_FileSetName, obj.get(KEYNAME_FileSetName)==null?"":obj.get(KEYNAME_FileSetName).toString());
				jsonObj.put(KEYNAME_UploaderId, obj.get(KEYNAME_UploaderId)==null?"":obj.get(KEYNAME_UploaderId).toString());
				jsonObj.put(KEYNAME_UploaderName, obj.get(KEYNAME_UploaderName)==null?"":obj.get(KEYNAME_UploaderName).toString());
				
				jsonObj.put("filetype", FileType);	//用于删除文件时上传文件类别
				jsonArray.put(jsonObj);
			}
			retJSON.put("rows", jsonArray);
			return retJSON;
		} catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * 从文件服务器（MongoDB）中更新一个文件的元信息
	 * @param idStr ：文件的id字符串值
	 * @param type
	 * @param metaDataMap 元信息Map
	 * @return
	 */
	public static boolean gridFSUpdateMetaDataById(String idStr, CollectionType type, Map<String, Object> metaDataMap) throws Exception{
		if(metaDataMap.get(KEYNAME_FileSetName) == null){	//FileSetName：系统内部标识，SqlServer中的文件URL字段存储该名称值，用于标示（多个）文件所属的文件集
			throw new Exception("文件标示为空：filesetname is null");
		}
		MongoService s;
		switch(type){
		case OriginalRecord:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOriginalRecord);
			break;
		case Certificate:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketCertificate);
			break;
		case Template:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketTemplate);
			break;
		case Attachment:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketAttachment);
			break;
		case Sharing:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketSharing);
			break;
		case Others:
		default:
			s = new MongoServiceImpl(DBPoolManager.getInstance().getDB(), BucketOthers);
		}
		ObjectId id = new ObjectId(idStr);
		return s.gridFSUpdateMetaDataById(id, metaDataMap);
	}
	
}
class TestThread extends Thread{
	private int id;
	public TestThread(int id){
		this.id = id;
	}
	@Override
	public void run() {
		try {
			HashMap<String ,Object> map = new HashMap<String, Object>();
			map.put("name", id);
			long beginTime = System.currentTimeMillis();
			System.out.println("Thread begin:  "+TestThread.this.toString());
			MongoDBUtil.gridFSUpload(new FileInputStream("D:/YlmF.GHO"), map, MongoDBUtil.CollectionType.OriginalRecord);

			System.out.println("Thread end:  "+TestThread.this.toString()+"  timeUsed:"+(System.currentTimeMillis()-beginTime));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Thread end:  "+TestThread.this.toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
