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

	private static String BucketOriginalRecord = SystemCfgUtil.BucketOriginalRecord;	//ԭʼ��¼
	private static String BucketCertificate = SystemCfgUtil.BucketCertificate;	//֤��
	private static String BucketTemplate = SystemCfgUtil.BucketTemplate;	//ģ���ļ�
	private static String BucketAttachment = SystemCfgUtil.BucketAttachment;	//����
	private static String BucketSharing = SystemCfgUtil.BucketSharing;	//�����ļ�
	private static String BucketOthers = SystemCfgUtil.BucketOthers;	//����
	
	public static final String KEYNAME_FileName = "filename";	//�ļ�������
	public static final String KEYNAME_FileSetName = "filesetname";	//ϵͳ�ڲ���ʶ��SqlServer�е��ļ�URL�ֶδ洢������ֵ�����ڱ�ʾ��������ļ��������ļ���
	public static final String KEYNAME_UploaderName = "uploadername";	//�ϴ�������
	public static final String KEYNAME_UploaderId = "uploaderid";	//�ϴ���ID
	public static final String KEYNAME_FileStatus = "filestatus";	//�ļ�״̬������ģ���ļ���Ч��'true'���ļ���Ч��'false':�ļ���ע����
	
	public enum CollectionType{
		OriginalRecord,	//ԭʼ��¼
		Certificate,	//֤��
		Template,		//ģ���ļ�
		Attachment,		//����
		Sharing,		//�����ļ�
		Others		//����
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
		Pattern pattern = Pattern.compile(".+\\.xml$", Pattern.CASE_INSENSITIVE);	//������ʽ:��.xls����.xlsx��β��
		paramsMap.put(MongoDBUtil.KEYNAME_FileName, pattern);
		System.out.println(MongoDBUtil.getFileList(paramsMap, MongoDBUtil.CollectionType.Template));
		
//		System.out.println(getLastUploadFileInfo(paramsMap, CollectionType.Template));
		
		
	}
	
	
	/**
	 * �ϴ�һ���ļ����ļ���������MongoDB��
	 * @param in : Ҫ�ϴ����ļ���������InputStream����byte[]����File ���֣�
	 * @param paramsMap �ļ��Ĳ���ֵ�����뺬��filesetname�:�������<"_id", ObjectId>��ֵ�ԣ����ϴ�ʱ���Ҹ�_id��ָ����ļ���ɾ��֮������ָ����_id�ϴ��µ��ļ�
	 * @param type
	 * @return �����ϴ����
	 */
	public static boolean gridFSUpload(Object in, HashMap<String,Object> paramsMap, CollectionType type) throws UnknownHostException, MongoException, Exception{
		if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName��ϵͳ�ڲ���ʶ��SqlServer�е��ļ�URL�ֶδ洢������ֵ�����ڱ�ʾ��������ļ��������ļ���
			throw new Exception("�ļ���ʾΪ�գ�filesetname is null");
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
	 * ���ļ���������MongoDB��������һ���ļ�
	 * @param out
	 * @param paramsMap �������ļ�������
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
	 * ���ļ���������MongoDB��������һ���ļ�
	 * @param out
	 * @param idStr �������ļ���id�ַ���ֵ
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
	 * ���ļ���������MongoDB��������һ���ļ�
	 * @param file
	 * @param idStr �������ļ���id�ַ���ֵ
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
	 * ���ļ���������MongoDB����ɾ��һ���ļ�
	 * @param out
	 * @param idStr ���ļ���id�ַ���ֵ
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
	 * ��ȡ�ļ��б�
	 * @param paramsMap���������������뺬��filesetname�
	 * @param type
	 * @return �ļ��б���Ϣ:������Ϣ��"_id"(�ļ�ID),"filename"(�ļ���),"length"(�ļ���С),"uploadDate"(�ϴ�ʱ��),"filesetname"(�ļ�������),"uploadername"(�ϴ�������),"uploaderid"(�ϴ���ID)
	 */
	public static JSONArray getFileList(HashMap<String, Object> paramsMap, CollectionType type){
		JSONArray retArray = new JSONArray();
		try{
			if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName��ϵͳ�ڲ���ʶ��SqlServer�е��ļ�URL�ֶδ洢������ֵ�����ڱ�ʾ��������ļ��������ļ���
				throw new Exception("�ļ���ʾΪ�գ�filesetname is null");
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
				
				jsonObj.put("filetype", FileType);	//����ɾ���ļ�ʱ�ϴ��ļ����
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
	 * ����ID�ַ��������ļ���Ϣ
	 * @param idStr
	 * @param type
	 * @return �ļ���Ϣ��JSONObject���󣩣���δ�ҵ��򷵻�null
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
				
				retObj.put("filetype", FileType);	//����ɾ���ļ�ʱ�ϴ��ļ����
				
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
	 * ���ҷ����������ļ������һ���ϴ����ļ���Ϣ
	 * @param paramsMap:��ѯ����
	 * @param type
	 * @return �ļ���Ϣ��JSONObject���󣩣���δ�ҵ��򷵻�null
	 */
	public static JSONObject getLastUploadFileInfo(HashMap<String, Object> paramsMap, CollectionType type){
		try{
			if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName��ϵͳ�ڲ���ʶ��SqlServer�е��ļ�URL�ֶδ洢������ֵ�����ڱ�ʾ��������ļ��������ļ���
				throw new Exception("�ļ���ʾΪ�գ�filesetname is null");
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
				
				jsonObj.put("filetype", FileType);	//����ɾ���ļ�ʱ�ϴ��ļ����
				
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
	 * ��ҳ��ȡ�ļ��б�
	 * @param currentPage
	 * @param pageSize
	 * @param paramsMap �������������뺬��filesetname�
	 * @param type
	 * @return �ļ��б���Ϣ:������Ϣ{total:��С, rows:[�б���Ϣ]}�������б��е�ÿһ����Ŀ����������Ϣ��
	 * 				"_id"(�ļ�ID),"filename"(�ļ���),"length"(�ļ���С),"uploadDate"(�ϴ�ʱ��),"filesetname"(�ļ�������),"uploadername"(�ϴ�������),"uploaderid"(�ϴ���ID)
	 */
	public static JSONObject getPageAllFileList(int currentPage, int pageSize, HashMap<String, Object> paramsMap, CollectionType type) throws Exception{
		JSONObject retJSON = new JSONObject();
		try{
			if(paramsMap.get(KEYNAME_FileSetName) == null){	//FileSetName��ϵͳ�ڲ���ʶ��SqlServer�е��ļ�URL�ֶδ洢������ֵ�����ڱ�ʾ��������ļ��������ļ���
				throw new Exception("�ļ���ʾΪ�գ�filesetname is null");
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
			DBCursor newCur = cur.skip((currentPage - 1) * pageSize).limit(pageSize);	//��ҳ
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
				
				jsonObj.put("filetype", FileType);	//����ɾ���ļ�ʱ�ϴ��ļ����
				jsonArray.put(jsonObj);
			}
			retJSON.put("rows", jsonArray);
			return retJSON;
		} catch (Exception e){
			throw e;
		}
	}
	
	/**
	 * ���ļ���������MongoDB���и���һ���ļ���Ԫ��Ϣ
	 * @param idStr ���ļ���id�ַ���ֵ
	 * @param type
	 * @param metaDataMap Ԫ��ϢMap
	 * @return
	 */
	public static boolean gridFSUpdateMetaDataById(String idStr, CollectionType type, Map<String, Object> metaDataMap) throws Exception{
		if(metaDataMap.get(KEYNAME_FileSetName) == null){	//FileSetName��ϵͳ�ڲ���ʶ��SqlServer�е��ļ�URL�ֶδ洢������ֵ�����ڱ�ʾ��������ļ��������ļ���
			throw new Exception("�ļ���ʾΪ�գ�filesetname is null");
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
