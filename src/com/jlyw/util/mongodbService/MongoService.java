package com.jlyw.util.mongodbService;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * �ļ����ݿ⣨MongoDB���ӿ�
 * id ����
 */
public interface MongoService {

	/**
	 * ͨ��gridFS�ϴ�����
	 * @param obj Ŀ�����
	 * @param paramsMap ����map
	 * @return
	 * @throws Exception
	 */
	public boolean gridFSUpload(Object obj, HashMap<String, Object> paramsMap)
			throws Exception;

	/**
	 * ͨ��gridFSɾ��
	 * @param paramsMap ����map
	 * @return
	 */
	public boolean gridFSDelete(HashMap<String, Object> paramsMap);

	/**
	 * ����DBObject����
	 * @param idName �ö����Id����
	 * @param dbObject �ö���
	 * @return
	 */
	public DBObject insert(String idName, DBObject dbObject);

	/**
	 * ��ȡ����
	 * @param dbObject 
	 * @return
	 */
	public DBObject getByObj(DBObject dbObject);
	
	/**
	 * ��������
	 * @param query �����ݵĶ���
	 * @param obj ���µ�����
	 * @return 
	 */
	public Boolean update(DBObject query,DBObject obj);
	
	
	/**
	 * ɾ������
	 * @param obj
	 */
	public void remove(DBObject obj);
	
	public GridFSDBFile findOne(DBObject obj);
	public GridFSDBFile findOneById(ObjectId id);
	public DBCursor getFileList(DBObject obj);
	public boolean gridFSUpdateMetaDataById(ObjectId id, Map<String, Object> metaDataMap);	//����Id�����ϴ��ļ���Ԫ��Ϣ
}