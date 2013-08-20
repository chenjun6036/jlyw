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
	 * 通过gridFS上传对象
	 * @param obj 目标对象
	 * @param [in][out]paramsMap 参数map:保存成功后paramsMap里增加一条<"_id", ObjectId>的记录（所保存记录的ID）
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
	 * 通过gridFS删除
	 * @param paramsMap 参数map
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
	 * 插入DBObject对象
	 * @param idName 该对象的Id名称
	 * @param dbObject 该对象
	 * @return
	 */
	public synchronized DBObject insert(String idName, DBObject dbObject) {
		Integer id = getAutoIncreaseID(idName);
		dbObject.put(idName, id);
		getCollection().insert(dbObject);
		return dbObject;
	}

	/**
	 * 获取连接
	 * @return 
	 */
	public DBCollection getCollection() {
		return db.getCollection(this.bucketName);
	}

	/**
	 * 根据表名获取连接
	 * @param name 
	 * @return
	 */
	public DBCollection getCollection(String name) {
		return db.getCollection(name);
	}

	/**
	 * 自增Id
	 * @param idName 自增Id名称
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
	 * 获取对象
	 * @param dbObject 
	 * @return
	 */
	public synchronized DBObject getByObj(DBObject dbObject) {
		return getCollection().findOne(dbObject);
	}
	
	/**
	 * 更新数据
	 * @param query 该数据的对象
	 * @param obj 更新的数据
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