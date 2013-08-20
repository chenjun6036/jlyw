package com.jlyw.util.mongodbService;

import java.net.UnknownHostException;

import com.jlyw.util.SystemCfgUtil;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;

/**
 * MongoDB���ݿ����ӳ�
 * 1.Mongo����
 *	  �ڲ�ʵ����һ�����ӳء�Mongo�������̰߳�ȫ�ģ���˿���ֻ����һ�����ڶ��̻߳����°�ȫʹ�á���ˣ����ǿ����ý�Mongo������Ϊһ��Singleton��ĳ�Ա�������Ӷ���ֻ֤����һ�����ӳء�
 *   Mongo.close�������رյ�ǰ���л�Ծ�����ӡ�����Ҫ��web���̱���Tomcat����GlassFish������ע����ʱ��ȷ������close������
 * 2.DB����
 *   DB�������ͨ��Mongo.get������ã������˺����ݿ��һ�����ӡ�Ĭ������£���ִ�������ݿ�Ĳ�ѯ���߸��²��������ӽ��Զ��ص����ӳ��С�����Ҫ�����ֶ����ô���Żس��С�
 *   �������ʵ�֣��Ҳ²���update,query,save�����ڲ���finally�飬�������л����ӵ����еĴ��롣
 * example: 
 * ��ʼ����DBManager.getInstance().init("74.208.78.5",27017,200); 
 * ֮��ÿ��ͨ������Ĵ����ȡ���ݿ���� 
 * DBManager.getInstance().getDB(); 
 * @version 1.0 
 * @author Administrator
 *
 */
public class DBPoolManager {
	public static final String MongoDBHost = SystemCfgUtil.MongoDBHost;
	public static final int MongoDBPort = SystemCfgUtil.MongoDBPort;
	public static final String DBName = SystemCfgUtil.FileDBName; 
	public static final int MongoDBPoolSize = SystemCfgUtil.MongoDBPoolSize;
  
    private Mongo mongo;  
    
    public static DBPoolManager getInstance(){  
    	return InnerHolder.INSTANCE;  
    }  
  
    /** 
     * Creates a new <code>DBManager</code> instance. 
     * 
     */  
    private DBPoolManager() {
    }  
  
    private static class InnerHolder{  
    	static final DBPoolManager INSTANCE = new DBPoolManager();  
    }  
  
    public DB getDB() throws UnknownHostException{
    	if(mongo == null){
    		init(MongoDBHost, MongoDBPort, MongoDBPoolSize);
    	}
    	return mongo.getDB(DBName);
    }  
  
   
      
  
    private void init(final String ip, int port, int poolSize) throws java.net.UnknownHostException {  
        System.setProperty("MONGO.POOLSIZE", String.valueOf(poolSize));  
        if (mongo == null) {  
            mongo = new Mongo(ip, port);  
            MongoOptions options = mongo.getMongoOptions();  
            options.autoConnectRetry = true;  
            options.connectionsPerHost = poolSize;  
        }  
    }  
}
