package com.jlyw.util.mongodbService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

public class MongoServiceImpl implements MongoService {

	private String bucketName;
	private DB db;

	public MongoServiceImpl(DB db, String bucketName) throws UnknownHostException, MongoException{
		this.bucketName = bucketName;
		this.db = db;
	}

	/**
	 * ͨ��gridFS�ϴ�����
	 * @param obj Ŀ�����
	 * @param [in][out]paramsMap ����map:����ɹ���paramsMap������һ��<"_id", ObjectId>�ļ�¼���������¼��ID��
	 * @return
	 * @throws Exception
	 */
	public synchronized boolean gridFSUpload(Object obj, HashMap<String, Object> paramsMap)
			throws IOException {
		boolean flag = false;

		GridFS gridFS = new GridFS(db, bucketName);

		GridFSFile gridFSFile = null;
		if (obj instanceof InputStream) {
			gridFSFile = gridFS.createFile((InputStream) obj);
		} else if (obj instanceof byte[]) {
			gridFSFile = gridFS.createFile((byte[]) obj);
		} else if (obj instanceof File) {
			gridFSFile = gridFS.createFile((File) obj);
		}
		if (gridFSFile != null && paramsMap != null) {
			Iterator<Entry<String, Object>> iter = paramsMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				gridFSFile.put(entry.getKey(), entry.getValue());
			}
			gridFSFile.save();
			paramsMap.put("_id", gridFSFile.getId());
			flag = true;
		}
		return flag;
	}

	/**
	 * ͨ��gridFSɾ��
	 * @param paramsMap ����map
	 * @return
	 */
	public synchronized boolean gridFSDelete(HashMap<String, Object> paramsMap) {
		boolean flag = false;
		GridFS gridFS = new GridFS(db, bucketName);
		DBObject query = new BasicDBObject();
		if (paramsMap != null) {
			Iterator<Entry<String, Object>> iter = paramsMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = iter.next();
				query.put(entry.getKey(), entry.getValue());
			}
		}
		DBObject obj = gridFS.findOne(query);
		if(obj != null){
			gridFS.remove(obj);
			flag = true;
		}
		return flag;
	}

	/**
	 * ����DBObject����
	 * @param idName �ö����Id����
	 * @param dbObject �ö���
	 * @return
	 */
	public synchronized DBObject insert(String idName, DBObject dbObject) {
		Integer id = getAutoIncreaseID(idName);
		dbObject.put(idName, id);
		getCollection().insert(dbObject);
		return dbObject;
	}

	/**
	 * ��ȡ����
	 * @return 
	 */
	public DBCollection getCollection() {
		return db.getCollection(this.bucketName);
	}

	/**
	 * ���ݱ�����ȡ����
	 * @param name 
	 * @return
	 */
	public DBCollection getCollection(String name) {
		return db.getCollection(name);
	}

	/**
	 * ����Id
	 * @param idName ����Id����
	 * @return Id
	 */
	public Integer getAutoIncreaseID(String idName) {
		BasicDBObject query = new BasicDBObject();
		query.put("name", idName);

		BasicDBObject update = new BasicDBObject();
		update.put("$inc", new BasicDBObject("id", 1));

		DBObject dbObject2 = getCollection("inc_ids").findAndModify(query,
				null, null, false, update, true, true);
		
		Integer id = (Integer) dbObject2.get("id");
		return id;
	}
	
	/**
	 * ��ȡ����
	 * @param dbObject 
	 * @return
	 */
	public synchronized DBObject getByObj(DBObject dbObject) {
		return getCollection().findOne(dbObject);
	}
	
	/**
	 * ��������
	 * @param query �����ݵĶ���
	 * @param obj ���µ�����
	 * @return 
	 */
	public synchronized Boolean update(DBObject query,DBObject obj) {
		WriteResult rs = getCollection().update(query, obj);
		return (Boolean) rs.getField("updatedExisting");
	}

	

	public void remove(DBObject obj) {
		getCollection().remove(obj);
	}
	
	public DBCursor getFileList(DBObject obj){
		GridFS gridFS = new GridFS(db, bucketName);
		DBCursor cur = gridFS.getFileList(obj);
		return cur;
	}

	public GridFSDBFile findOne(DBObject obj) {
		GridFS gridFS = new GridFS(db, bucketName);
		GridFSDBFile dbFile = gridFS.findOne(obj);
		return dbFile;
	}

	public GridFSDBFile findOneById(ObjectId id) {
		GridFS gridFS = new GridFS(db, bucketName);
		GridFSDBFile dbFile = gridFS.findOne(id);
		return dbFile;
	}

	public boolean gridFSUpdateMetaDataById(ObjectId id, Map<String, Object> metaDataMap) {
		try{
			GridFS gridFS = new GridFS(db, bucketName);
			GridFSDBFile dbFile = gridFS.findOne(id);
//			DBObject metaData = new BasicDBObject();
			if (metaDataMap != null) {
				Iterator<Entry<String, Object>> iter = metaDataMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String, Object> entry = iter.next();
					dbFile.put(entry.getKey(), entry.getValue());
//					metaData.put(entry.getKey(), entry.getValue());
				}
			}
			dbFile.save();
//			dbFile.setMetaData(metaData);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	
	
	
}