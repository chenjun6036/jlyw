package com.jlyw.util.mongodbService;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * 文件数据库（MongoDB）接口
 * id 自增
 */
public interface MongoService {

	/**
	 * 通过gridFS上传对象
	 * @param obj 目标对象
	 * @param paramsMap 参数map
	 * @return
	 * @throws Exception
	 */
	public boolean gridFSUpload(Object obj, HashMap<String, Object> paramsMap)
			throws Exception;

	/**
	 * 通过gridFS删除
	 * @param paramsMap 参数map
	 * @return
	 */
	public boolean gridFSDelete(HashMap<String, Object> paramsMap);

	/**
	 * 插入DBObject对象
	 * @param idName 该对象的Id名称
	 * @param dbObject 该对象
	 * @return
	 */
	public DBObject insert(String idName, DBObject dbObject);

	/**
	 * 获取对象
	 * @param dbObject 
	 * @return
	 */
	public DBObject getByObj(DBObject dbObject);
	
	/**
	 * 更新数据
	 * @param query 该数据的对象
	 * @param obj 更新的数据
	 * @return 
	 */
	public Boolean update(DBObject query,DBObject obj);
	
	
	/**
	 * 删除对象
	 * @param obj
	 */
	public void remove(DBObject obj);
	
	public GridFSDBFile findOne(DBObject obj);
	public GridFSDBFile findOneById(ObjectId id);
	public DBCursor getFileList(DBObject obj);
	public boolean gridFSUpdateMetaDataById(ObjectId id, Map<String, Object> metaDataMap);	//根据Id更新上传文件的元信息
}